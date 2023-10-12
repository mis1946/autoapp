/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.auto.app.sales;

import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.swing.JRViewer;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.auto.app.views.unloadForm;
import org.rmj.auto.sales.base.VehicleSalesProposalMaster;

/**
 * FXML Controller class
 *
 * @author User
 */
public class VSPFormPrintController implements Initializable {

    private VehicleSalesProposalMaster oTrans;
    private GRider oApp;
    private MasterCallback oListener;
    private JasperPrint jasperPrint; //Jasper Libraries
    private JasperReport jasperReport;
    private JRViewer jrViewer;
    private ObservableList<VSPTableMasterList> vspMasterData = FXCollections.observableArrayList();
    private List<VSPTableFinanceList> vspFinanceData = new ArrayList<VSPTableFinanceList>();
    private List<VSPTableLaborList> vspLaborData = new ArrayList<VSPTableLaborList>();
    private List<VSPTablePartList> vspPartData = new ArrayList<VSPTablePartList>();
    private boolean running = false;
    final static int interval = 100;
    private Timeline timeline;
    private Integer timeSeconds = 3;
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnClose;
    private final String pxeModuleName = "Vehicle Sales Proposal Print";
    unloadForm unload = new unloadForm(); //Used in Close Button
    @FXML
    private AnchorPane reportPane;
    @FXML
    private VBox vbProgress;
    private String psTransNox;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTrans = new VehicleSalesProposalMaster(oApp, oApp.getBranchCode(), true);
        oTrans.setCallback(oListener);
        oTrans.setWithUI(true);
        vbProgress.setVisible(true);
        timeline = new Timeline();
        generateReport();
        btnClose.setOnAction(this::cmdButton_Click);

    }

    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;
        }
    }

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private Stage getStage() {
        return (Stage) btnClose.getScene().getWindow();
    }

    private void hideReport() {
        jrViewer = new JRViewer(null);
        reportPane.getChildren().clear();
        jrViewer.setVisible(false);
        running = false;
        reportPane.setVisible(false);
        timeline.stop();
    }

    private void generateReport() {
        hideReport();
        if (!running) {
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.getKeyFrames().add(
                    new KeyFrame(Duration.seconds(1), (ActionEvent event1) -> {
                        timeSeconds--;
                        // update timerLabel
                        if (timeSeconds <= 0) {
                            timeSeconds = 0;
                        }
                        if (timeSeconds == 0) {

                            try {
                                loadReport();
                            } catch (SQLException ex) {
                                Logger.getLogger(VSPFormPrintController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } // KeyFrame event handler
                    ));
            timeline.playFromStart();
        }
    }

    private void showReport() {
        vbProgress.setVisible(false);
        jrViewer = new JRViewer(jasperPrint);
        jrViewer.setZoomRatio(0.75f);
        SwingNode swingNode = new SwingNode();
        jrViewer.setOpaque(true);
        jrViewer.setVisible(true);
        jrViewer.setFitPageZoomRatio();
        swingNode.setContent(jrViewer);
        swingNode.setVisible(true);
        reportPane.setTopAnchor(swingNode, 0.0);
        reportPane.setBottomAnchor(swingNode, 0.0);
        reportPane.setLeftAnchor(swingNode, 0.0);
        reportPane.setRightAnchor(swingNode, 0.0);
        reportPane.getChildren().add(swingNode);
        running = true;
        reportPane.setVisible(true);
        timeline.stop();
    }

    public void setTransNox(String fsValue) {
        psTransNox = fsValue;
    }

    private boolean loadReport() throws SQLException {
        //Create the parameter
        int lnCtr = 1;
        boolean bAdditional = false;
        Map<String, Object> params = new HashMap<>();
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        vspMasterData.clear();
        if (oTrans.OpenRecord(psTransNox)) {
            String dEntry = dEntry = CommonUtils.xsDateShort((Date) oTrans.getMaster("dEntryDte"));
            if (dEntry.isEmpty()) {
                dEntry = " ";
            } else {
                dEntry = dEntry = CommonUtils.xsDateShort((Date) oTrans.getMaster("dEntryDte"));
            }
            String from = CommonUtils.xsDateLong((Date) oTrans.getMaster("dDateFrom"));
            String to = CommonUtils.xsDateLong((Date) oTrans.getMaster("dDateThru"));
            vspMasterData.add(new VSPTableMasterList(
                    oTrans.getMaster("").toString(),
                    oTrans.getMaster("sTransNox").toString(),
                    oTrans.getMaster("dTransact").toString(),
                    oTrans.getMaster("sVSPNOxxx").toString(), //vspNo
                    oTrans.getMaster("dDelvryDt").toString(),
                    oTrans.getMaster("sInqryIDx").toString(),
                    oTrans.getMaster("sClientID").toString(),
                    oTrans.getMaster("sSerialID").toString(),
                    oTrans.getMaster("nUnitPrce").toString(),
                    oTrans.getMaster("sRemarksx").toString(),
                    oTrans.getMaster("nAdvDwPmt").toString(),
                    oTrans.getMaster("sOthrDesc").toString(),
                    oTrans.getMaster("nOthrChrg").toString(),
                    oTrans.getMaster("nLaborAmt").toString(),
                    oTrans.getMaster("nAccesAmt").toString(),
                    oTrans.getMaster("nInsurAmt").toString(),
                    oTrans.getMaster("nTPLAmtxx").toString(),
                    oTrans.getMaster("nCompAmtx").toString(),
                    oTrans.getMaster("nLTOAmtxx").toString(),
                    oTrans.getMaster("nChmoAmtx").toString(),
                    oTrans.getMaster("sChmoStat").toString(),
                    oTrans.getMaster("sTPLStatx").toString(),
                    oTrans.getMaster("sCompStat").toString(),
                    oTrans.getMaster("sLTOStatx").toString(),
                    oTrans.getMaster("sInsurTyp").toString(),
                    oTrans.getMaster("nInsurYrx").toString(),
                    oTrans.getMaster("sInsTplCd").toString(),
                    oTrans.getMaster("sInsCodex").toString(),
                    oTrans.getMaster("nPromoDsc").toString(),
                    oTrans.getMaster("nFleetDsc").toString(),
                    oTrans.getMaster("nSPFltDsc").toString(),
                    oTrans.getMaster("nBndleDsc").toString(),
                    oTrans.getMaster("nAddlDscx").toString(),
                    oTrans.getMaster("nDealrInc").toString(),
                    oTrans.getMaster("cPayModex").toString(),
                    oTrans.getMaster("sBnkAppCD").toString(),
                    oTrans.getMaster("nTranTotl").toString(),
                    oTrans.getMaster("nResrvFee").toString(),
                    oTrans.getMaster("nDownPaym").toString(),
                    oTrans.getMaster("nNetTTotl").toString(),
                    oTrans.getMaster("nAmtPaidx").toString(),
                    oTrans.getMaster("nFrgtChrg").toString(),
                    oTrans.getMaster("nDue2Supx").toString(),
                    oTrans.getMaster("nDue2Dlrx").toString(),
                    oTrans.getMaster("nSPFD2Sup").toString(),
                    oTrans.getMaster("nSPFD2Dlr").toString(),
                    oTrans.getMaster("nPrmD2Sup").toString(),
                    oTrans.getMaster("nPrmD2Dlr").toString(),
                    oTrans.getMaster("sEndPlate").toString(),
                    oTrans.getMaster("sBranchCD").toString(),
                    oTrans.getMaster("nDealrRte").toString(),
                    oTrans.getMaster("nDealrAmt").toString(),
                    oTrans.getMaster("nSlsInRte").toString(),
                    oTrans.getMaster("nSlsInAmt").toString(),
                    oTrans.getMaster("cIsVhclNw").toString(),
                    oTrans.getMaster("cIsVIPxxx").toString(),
                    oTrans.getMaster("sDcStatCd").toString(),
                    oTrans.getMaster("dDcStatDt").toString(),
                    oTrans.getMaster("cPrintedx").toString(),
                    oTrans.getMaster("sLockedBy").toString(),
                    oTrans.getMaster("dLockedDt").toString(),
                    oTrans.getMaster("cTranStat").toString(),
                    "",
                    "",
                    "",
                    "",
                    oTrans.getMaster("sModified").toString(),
                    oTrans.getMaster("dModified").toString(),
                    oTrans.getMaster("sCompnyNm").toString(),
                    oTrans.getMaster("sAddressx").toString(),
                    oTrans.getMaster("sDescript").toString(),
                    oTrans.getMaster("sCSNoxxxx").toString(),
                    oTrans.getMaster("sPlateNox").toString(),
                    oTrans.getMaster("sFrameNox").toString(),
                    oTrans.getMaster("sEngineNo").toString(),
                    oTrans.getMaster("sSalesExe").toString(),
                    oTrans.getMaster("sSalesAgn").toString(),
                    oTrans.getMaster("sInqClntx").toString(),
                    oTrans.getMaster("dInqDatex").toString(),
                    oTrans.getMaster("sUdrNoxxx").toString(),
                    oTrans.getMaster("sInqTypex").toString(),
                    oTrans.getMaster("sOnlStore").toString(),
                    oTrans.getMaster("sRefTypex").toString(),
                    oTrans.getMaster("sKeyNoxxx").toString(),
                    oTrans.getMaster("sBranchNm").toString(), //Branch Name
                    oTrans.getMaster("sInsTplNm").toString(),
                    oTrans.getMaster("sInsComNm").toString() // Branch Address
            ));
        }
        vspFinanceData.clear();
        String finAmount = oTrans.getVSPFinance("nFinAmtxx").toString();
        // Convert the amount to a decimal value
        double Finamount = Double.parseDouble(finAmount);
        String finAmountx = decimalFormat.format(Finamount);
        vspFinanceData.add(new VSPTableFinanceList(
                "",
                oTrans.getVSPFinance("sTransNox").toString(),
                oTrans.getVSPFinance("cFinPromo").toString(),
                oTrans.getVSPFinance("sBankIDxx").toString(),
                oTrans.getVSPFinance("sBankname").toString(),
                finAmountx,
                oTrans.getVSPFinance("nAcctTerm").toString(),
                oTrans.getVSPFinance("nAcctRate").toString(),
                oTrans.getVSPFinance("nRebatesx").toString(),
                oTrans.getVSPFinance("nMonAmort").toString(),
                oTrans.getVSPFinance("nPNValuex").toString(),
                oTrans.getVSPFinance("nBnkPaidx").toString(),
                oTrans.getVSPFinance("nGrsMonth").toString(),
                oTrans.getVSPFinance("nNtDwnPmt").toString(),
                oTrans.getVSPFinance("nDiscount").toString()));

        vspLaborData.clear();
        for (lnCtr = 1;
                lnCtr
                <= oTrans.getVSPLaborCount();
                lnCtr++) {
            String cType = "";
            switch (oTrans.getVSPLaborDetail(lnCtr, "sChrgeTyp").toString()) {
                case "0":
                    cType = "FREE OF CHARGE";
                    break;
                case "1":
                    cType = "CHARGE";
                    break;
            }
            String cTo = "";
            switch (oTrans.getVSPLaborDetail(lnCtr, "sChrgeTox").toString()) {
                case "0":
                    cTo = "C/O CLIENT";
                    break;
                case "1":
                    cTo = "C/O BANK";
                    break;
            }

            if (oTrans.getVSPLaborDetail(lnCtr, "cAddtlxxx").toString().equals("1")) {
                bAdditional = true;
            } else {
                bAdditional = false;
            }

            String amountString = oTrans.getVSPLaborDetail(lnCtr, "nLaborAmt").toString();
            // Convert the amount to a decimal value
            double amount = Double.parseDouble(amountString);

            String formattedAmount = decimalFormat.format(amount);
            vspLaborData.add(new VSPTableLaborList(
                    String.valueOf(lnCtr), //ROW
                    oTrans.getVSPLaborDetail(lnCtr, "sLaborCde").toString(),
                    oTrans.getVSPLaborDetail(lnCtr, "sLaborDsc").toString().toUpperCase(),
                    cType,
                    cTo,
                    formattedAmount,
                    bAdditional
            ));
            bAdditional = false;
        }

        vspPartData.clear();
        for (lnCtr = 1;
                lnCtr
                <= oTrans.getVSPPartsCount();
                lnCtr++) {
            String cType = "";
            switch (oTrans.getVSPPartsDetail(lnCtr, "sChrgeTyp").toString()) {
                case "0":
                    cType = "FREE OF CHARGE";
                    break;
                case "1":
                    cType = "CHARGE";
                    break;
            }
            String cTo = "";
            switch (oTrans.getVSPPartsDetail(lnCtr, "sChrgeTox").toString()) {
                case "0":
                    cTo = "C/O CLIENT";
                    break;
                case "1":
                    cTo = "C/O BANK";
                    break;
            }
            String amountString = oTrans.getVSPPartsDetail(lnCtr, "nSelPrice").toString();
            // Convert the amount to a decimal value
            double amount = Double.parseDouble(amountString);
            String formattedAmount = decimalFormat.format(amount);
            if (oTrans.getVSPPartsDetail(lnCtr, "cAddtlxxx").toString().equals("1")) {
                bAdditional = true;
            } else {
                bAdditional = false;
            }
            vspPartData.add(new VSPTablePartList(
                    String.valueOf(lnCtr), //ROW
                    oTrans.getVSPPartsDetail(lnCtr, "sBarCodex").toString(),
                    oTrans.getVSPPartsDetail(lnCtr, "sDescript").toString(),
                    cType,
                    oTrans.getVSPPartsDetail(lnCtr, "nQuantity").toString(),
                    formattedAmount,
                    cTo,
                    bAdditional
            ));
            bAdditional = false;
        }

        String sourceFileName = "D://GGC_Java_Systems/reports/autoapp/VSPFormPrint.jasper";
        String printFileName = null;
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(vspMasterData);
        JRBeanCollectionDataSource vspFinance = new JRBeanCollectionDataSource(vspFinanceData);
        JRBeanCollectionDataSource vspLabor = new JRBeanCollectionDataSource(vspLaborData);
        JRBeanCollectionDataSource vspPart = new JRBeanCollectionDataSource(vspPartData);

        params.put(
                "vspLabor", vspLabor);
        params.put(
                "vspPart", vspPart);
        params.put(
                "vspFinance", vspFinance);
        try {
            jasperPrint = JasperFillManager.fillReport(sourceFileName, params, beanColDataSource);
            printFileName = jasperPrint.toString();
            if (printFileName != null) {
                showReport();
            }
        } catch (JRException ex) {
            running = false;
            vbProgress.setVisible(false);
            timeline.stop();
        }

        return false;
    }
}
