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

    public static String formatName(String fullName) {
        String[] nameParts = fullName.split(" ");
        String firstNameInitial = nameParts[0].substring(0, 1);
        String lastName = nameParts[nameParts.length - 1];
        return firstNameInitial + ". " + lastName;
    }

    public static String formatAmount(String amountString) {
        DecimalFormat numFormat = new DecimalFormat("#,##0.00");
        String formattedAmount = "";
        if (amountString.contains("0.00")) {
            formattedAmount = "";
        } else {
            double amount = Double.parseDouble(amountString);
            formattedAmount = numFormat.format(amount);
        }
        return formattedAmount;
    }

    private boolean loadReport() throws SQLException {
        //Create the parameter
        int lnCtr = 1;
        boolean bAdditional = false;
        Map<String, Object> params = new HashMap<>();

        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        vspMasterData.clear();
        if (oTrans.OpenRecord(psTransNox)) {
            String deliveryDate = CommonUtils.xsDateShort((Date) oTrans.getMaster("dDelvryDt"));
            if (deliveryDate.isEmpty()) {
                deliveryDate = " ";
            } else {
                deliveryDate = CommonUtils.xsDateShort((Date) oTrans.getMaster("dDelvryDt"));
            }
            String salesExe = oTrans.getMaster("sSalesExe").toString();
            String salesExeFullName = formatName(salesExe);
            String ownerHomeAddress = "";
            String ownerOfficeAddress = "";
            if (oTrans.getMaster("cOfficexx").toString().equals("1")) {
                ownerOfficeAddress = oTrans.getMaster("sAddressx").toString().toUpperCase();
            } else {
                ownerOfficeAddress = "";
            }
            if (oTrans.getMaster("cOfficexx").toString().equals("0")) {
                ownerHomeAddress = oTrans.getMaster("sAddressx").toString().toUpperCase();
            } else {
                ownerHomeAddress = "";
            }
            String notOfficeNumber = "";
            String officeNumber = "";
            if (oTrans.getMaster("cOwnerxxx").toString().equals("0")) {
                notOfficeNumber = oTrans.getMaster("sMobileNo").toString();
            } else {
                notOfficeNumber = "";
            }
            if (oTrans.getMaster("cOwnerxxx").toString().equals("1")) {
                officeNumber = oTrans.getMaster("sMobileNo").toString();
            } else {
                officeNumber = "";
            }
            String platOrCsNo = "";
            if (oTrans.getMaster("sPlateNox").toString().isEmpty()) {
                platOrCsNo = oTrans.getMaster("sCSNoxxxx").toString();
            } else if (!oTrans.getMaster("sPlateNox").toString().isEmpty()) {
                platOrCsNo = oTrans.getMaster("sPlateNox").toString();
            } else {
                platOrCsNo = "";
            }
            String paymentMethodDisplay = "";
            String paymentMethod = oTrans.getMaster("cPayModex").toString();
            switch (paymentMethod) {
                case "0":
                    paymentMethodDisplay = "CASH";
                    break;
                case "1":
                    paymentMethodDisplay = "BANK PURCHASE ORDER";
                    break;
                case "2":
                    paymentMethodDisplay = "BANK FINANCING";
                    break;
                case "3":
                    paymentMethodDisplay = "COMPANY PURCHASE ORDER";
                    break;
                case "4":
                    paymentMethodDisplay = "COMPANY FINANCING";
                    break;
                default:
                    paymentMethodDisplay = "";
                    break;
            }

            String unitPrice = "";
            String nUPrice = decimalFormat.format(Double.parseDouble(oTrans.getMaster("nUnitPrce").toString()));
            String dPayment = decimalFormat.format(Double.parseDouble(oTrans.getMaster("nDownPaym").toString()));
            if (oTrans.getMaster("nUnitPrce").toString().contains("0.00")) {
                unitPrice = "0.00";
            } else {
                unitPrice = nUPrice + " / Downpayment:" + dPayment;
            }

            String LtoAmount = "0.00";
            switch (oTrans.getMaster("sLTOStatx").toString()) {
                case "0":
                    LtoAmount = "";
                    break;
                case "1":
                    LtoAmount = "FREE";
                    break;
                case "2":
                    LtoAmount = formatAmount(oTrans.getMaster("nLTOAmtxx").toString());
                    break;
            }
            String chmoDocStamps = "";
            switch (oTrans.getMaster("sChmoStat").toString()) {
                case "0":
                    LtoAmount = "";
                    break;
                case "1":
                    LtoAmount = "FREE";
                    break;
                case "2":
                case "3":
                    chmoDocStamps = formatAmount(oTrans.getMaster("nChmoAmtx").toString());
                    break;
            }
            String totalAmount = formatAmount(oTrans.getMaster("nTranTotl").toString());
            String reservationAmount = formatAmount(oTrans.getMaster("nResrvFee").toString());
//            String dwnPayment = formatAmount(oTrans.getMaster("nTranTotl").toString(), decimalFormat);
            String netAmountDue = formatAmount(oTrans.getMaster("nNetTTotl").toString());
            String inqTypDisplay = "";
            switch (oTrans.getMaster("sInqTypex").toString()) {
                case "0":
                    inqTypDisplay = "WALK-IN";
                    break;
                case "1":
                    inqTypDisplay = "WEB INQUIRY";
                    break;
                case "2":
                    inqTypDisplay = "PHONE-IN";
                    break;
                case "3":
                    inqTypDisplay = "REFERRAL";
                    break;
                case "4":
                    inqTypDisplay = "SALES CALL";
                    break;
                case "5":
                    inqTypDisplay = "EVENT";
                    break;
                case "6":
                    inqTypDisplay = "SERVICE";
                    break;
                case "7":
                    inqTypDisplay = "OFFICE ACCOUNT";
                    break;
                case "8":
                    inqTypDisplay = "CAREMITTANCE";
                    break;
                case "9":
                    inqTypDisplay = "DATABASE";
                    break;
                case "10":
                    inqTypDisplay = "UIO";
                    break;
                default:
                    inqTypDisplay = "";
                    break;
            }

            vspMasterData.add(new VSPTableMasterList(
                    "",
                    oTrans.getMaster("sTransNox").toString(),
                    oTrans.getMaster("dTransact").toString(),
                    oTrans.getMaster("sVSPNOxxx").toString(), //vspNo
                    deliveryDate, //dDelvryDt
                    oTrans.getMaster("sInqryIDx").toString(),
                    oTrans.getMaster("sClientID").toString(),
                    oTrans.getMaster("sSerialID").toString(),
                    unitPrice,
                    oTrans.getMaster("sRemarksx").toString(),
                    oTrans.getMaster("nAdvDwPmt").toString(),
                    oTrans.getMaster("sOthrDesc").toString(),
                    oTrans.getMaster("nOthrChrg").toString(),
                    oTrans.getMaster("nLaborAmt").toString(),
                    oTrans.getMaster("nAccesAmt").toString(),
                    oTrans.getMaster("nInsurAmt").toString(),
                    oTrans.getMaster("nTPLAmtxx").toString(),
                    oTrans.getMaster("nCompAmtx").toString(),
                    LtoAmount,
                    chmoDocStamps,
                    oTrans.getMaster("sTPLStatx").toString(),
                    oTrans.getMaster("sCompStat").toString(),
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
                    paymentMethodDisplay,
                    oTrans.getMaster("sBnkAppCD").toString(),
                    totalAmount,
                    reservationAmount,
                    netAmountDue,
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
                    "",
                    oTrans.getMaster("cPrintedx").toString(),
                    oTrans.getMaster("sLockedBy").toString(),
                    "",
                    oTrans.getMaster("cTranStat").toString(),
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    oTrans.getMaster("sCompnyNm").toString().toUpperCase(),
                    ownerOfficeAddress,
                    ownerHomeAddress,
                    oTrans.getMaster("sDescript").toString().toUpperCase(),
                    platOrCsNo,
                    oTrans.getMaster("sFrameNox").toString(),
                    oTrans.getMaster("sEngineNo").toString(),
                    salesExeFullName.toUpperCase(),
                    oTrans.getMaster("sSalesAgn").toString(),
                    oTrans.getMaster("sInqClntx").toString(),
                    oTrans.getMaster("dInqDatex").toString(),
                    "",
                    inqTypDisplay,
                    oTrans.getMaster("sOnlStore").toString(),
                    oTrans.getMaster("sRefTypex").toString(),
                    oTrans.getMaster("sKeyNoxxx").toString(),
                    oTrans.getMaster("sBranchNm").toString().toUpperCase(), //Branch Name
                    oTrans.getMaster("sInsTplNm").toString(),
                    oTrans.getMaster("sInsComNm").toString(),
                    oTrans.getMaster("sTaxIDNox").toString(),
                    oTrans.getMaster("sJobNoxxx").toString(),
                    CommonUtils.xsDateMedium((Date) oTrans.getMaster("dBirthDte")),
                    oTrans.getMaster("sEmailAdd").toString(),
                    oTrans.getMaster("cOwnerxxx").toString(),
                    notOfficeNumber,
                    officeNumber,
                    oTrans.getMaster("cOfficexx").toString(),
                    oTrans.getMaster("cTrStatus").toString(),
                    oTrans.getMaster("sBrnchAdd").toString()// Branch Address
            )
            );

//            vspFinanceData.clear();
//            String Finamount = "0.00";
//            if (!oTrans.getVSPFinance(5).toString().contains("0.00")) {
//                Finamount = decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getVSPFinance(5))));
//            } else {
//                Finamount = "0.00";
//            }
//            String bankName = "";
//            if (!oTrans.getVSPFinance("sBankname").toString().isEmpty()) {
//                bankName = oTrans.getVSPFinance("sBankname").toString();
//            } else {
//                bankName = "";
//            }
//
//            vspFinanceData.add(new VSPTableFinanceList(
//                    "",
//                    oTrans.getVSPFinance("sTransNox").toString(),
//                    oTrans.getVSPFinance("cFinPromo").toString(),
//                    oTrans.getVSPFinance("sBankIDxx").toString(),
//                    bankName.toUpperCase(),
//                    Finamount,
//                    oTrans.getVSPFinance("nAcctTerm").toString(),
//                    oTrans.getVSPFinance("nAcctRate").toString(),
//                    oTrans.getVSPFinance("nRebatesx").toString(),
//                    oTrans.getVSPFinance("nMonAmort").toString(),
//                    oTrans.getVSPFinance("nPNValuex").toString(),
//                    oTrans.getVSPFinance("nBnkPaidx").toString(),
//                    oTrans.getVSPFinance("nGrsMonth").toString(),
//                    oTrans.getVSPFinance("nNtDwnPmt").toString(),
//                    oTrans.getVSPFinance("nDiscount").toString()
//            ));
//            vspLaborData.clear();
//
//            for (lnCtr = 1; lnCtr <= oTrans.getVSPLaborCount(); lnCtr++) {
//                String cType = "";
//                switch (oTrans.getVSPLaborDetail(lnCtr, "sChrgeTyp").toString()) {
//                    case "0":
//                        cType = "FREE OF CHARGE";
//                        break;
//                    case "1":
//                        cType = "CHARGE";
//                        break;
//                }
//                String cTo = "";
//                switch (oTrans.getVSPLaborDetail(lnCtr, "sChrgeTox").toString()) {
//                    case "0":
//                        cTo = "C/O CLIENT";
//                        break;
//                    case "1":
//                        cTo = "C/O BANK";
//                        break;
//                }
//
//                if (oTrans.getVSPLaborDetail(lnCtr, "cAddtlxxx").toString().equals("1")) {
//                    bAdditional = true;
//                } else {
//                    bAdditional = false;
//                }
//                String result = "";
//                String rustAmount = oTrans.getVSPLaborDetail(lnCtr, "nLaborAmt").toString();
//                if (oTrans.getVSPLaborDetail(lnCtr, "sLaborDsc").toString().equals("RustProof")) {
//                    result = rustAmount;
//                } else {
//                    result = "";
//                }
//                String amountString = oTrans.getVSPLaborDetail(lnCtr, "nLaborAmt").toString();
//                // Convert the amount to a decimal value
//                double amount = Double.parseDouble(amountString);
//
//                String formattedAmount = decimalFormat.format(amount);
//                vspLaborData.add(new VSPTableLaborList(
//                        String.valueOf(lnCtr), //ROW
//                        oTrans.getVSPLaborDetail(lnCtr, "sLaborCde").toString(),
//                        oTrans.getVSPLaborDetail(lnCtr, "sLaborDsc").toString().toUpperCase(),
//                        cType,
//                        cTo,
//                        formattedAmount,
//                        bAdditional
//                ));
//                bAdditional = false;
//            }
//
//            vspPartData.clear();
//            for (lnCtr = 1;
//                    lnCtr
//                    <= oTrans.getVSPPartsCount();
//                    lnCtr++) {
//                String cType = "";
//                switch (oTrans.getVSPPartsDetail(lnCtr, "sChrgeTyp").toString()) {
//                    case "0":
//                        cType = "FREE OF CHARGE";
//                        break;
//                    case "1":
//                        cType = "CHARGE";
//                        break;
//                }
//                String cTo = "";
//                switch (oTrans.getVSPPartsDetail(lnCtr, "sChrgeTox").toString()) {
//                    case "0":
//                        cTo = "C/O CLIENT";
//                        break;
//                    case "1":
//                        cTo = "C/O BANK";
//                        break;
//                }
//                String amountString = oTrans.getVSPPartsDetail(lnCtr, "nSelPrice").toString();
//                // Convert the amount to a decimal value
//                double amount = Double.parseDouble(amountString);
//                String formattedAmount = decimalFormat.format(amount);
//                if (oTrans.getVSPPartsDetail(lnCtr, "cAddtlxxx").toString().equals("1")) {
//                    bAdditional = true;
//                } else {
//                    bAdditional = false;
//                }
//                vspPartData.add(new VSPTablePartList(
//                        String.valueOf(lnCtr), //ROW
//                        oTrans.getVSPPartsDetail(lnCtr, "sBarCodex").toString(),
//                        oTrans.getVSPPartsDetail(lnCtr, "sDescript").toString(),
//                        cType,
//                        oTrans.getVSPPartsDetail(lnCtr, "nQuantity").toString(),
//                        formattedAmount,
//                        cTo,
//                        bAdditional
//                ));
//                bAdditional = false;
//            }
        }
        String sourceFileName = "D://GGC_Java_Systems/reports/autoapp/vsp.jasper";
        String printFileName = null;
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(vspMasterData);
//        JRBeanCollectionDataSource vspFinance = new JRBeanCollectionDataSource(vspFinanceData);
//        JRBeanCollectionDataSource vspLabor = new JRBeanCollectionDataSource(vspLaborData);
//        JRBeanCollectionDataSource vspPart = new JRBeanCollectionDataSource(vspPartData);
//
//        params.put(
//                "vspLabor", vspLabor);
//        params.put(
//                "vspPart", vspPart);
//        params.put(
//                "vspFinance", vspFinance);
        params.put("formName", "VEHICLE SALES PROPOSAL FORM");
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
