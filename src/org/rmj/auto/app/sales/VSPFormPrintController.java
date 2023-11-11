/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.auto.app.sales;

import java.awt.Component;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
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
import javax.swing.AbstractButton;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.swing.JRViewer;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.auto.app.views.unloadForm;
import org.rmj.auto.sales.base.VehicleSalesProposalMaster;

public class VSPFormPrintController implements Initializable {

    private VehicleSalesProposalMaster oTrans;
    private GRider oApp;
    private MasterCallback oListener;
    private JasperPrint jasperPrint; //Jasper Libraries
    private JRViewer jrViewer;
    private ObservableList<VSPTableMasterList> vspMasterData = FXCollections.observableArrayList();
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
    private Map<String, Object> params = new HashMap<>();
    @FXML
    private Button btnPrint;

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private Stage getStage() {
        return (Stage) btnClose.getScene().getWindow();
    }

    public void setTransNox(String fsValue) {
        psTransNox = fsValue;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTrans = new VehicleSalesProposalMaster(oApp, oApp.getBranchCode(), true); //Initialize VehicleSalesProposalMaster
        oTrans.setCallback(oListener);
        oTrans.setWithUI(true);

        vbProgress.setVisible(true);
        timeline = new Timeline();
        generateReport();
        btnPrint.setVisible(false);
        btnPrint.setDisable(true);
        btnClose.setOnAction(this::cmdButton_Click);
        btnPrint.setOnAction(this::cmdButton_Click);
    }

    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            case "btnPrint":
                try {
                if (JasperPrintManager.printReport(jasperPrint, true)) {
                    ShowMessageFX.Information(null, pxeModuleName, "Printed Successfully");
                    //Set Value to Refunded amount
                    //oTrans.setMaster(0, dRefundAmt);
                    CommonUtils.closeStage(btnClose);
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, "Print Aborted");
                }
            } catch (JRException ex) {

                ShowMessageFX.Warning(null, pxeModuleName, "Print Aborted");
            }
            break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;
        }
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

    private void hideReport() {
        jrViewer = new JRViewer(null);
        reportPane.getChildren().clear();
        jrViewer.setVisible(false);
        running = false;
        reportPane.setVisible(false);
        timeline.stop();
    }

    private void showReport() {
        vbProgress.setVisible(false);
        btnPrint.setVisible(true);
        btnPrint.setDisable(false);
        jrViewer = new JRViewer(jasperPrint);
        jrViewer.setZoomRatio(0.75f);
        findAndHideButton(jrViewer, "Print");
        findAndHideButton(jrViewer, "Save");
        // Find and hide the buttons
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

    private void findAndHideButton(Component component, String buttonText) {
        if (component instanceof AbstractButton) {
            AbstractButton button = (AbstractButton) component;
            if (button.getToolTipText() != null) {
                if (button.getToolTipText().equals(buttonText)) {
                    button.setVisible(false);
                    return;
                }
            }
        }

        if (component instanceof java.awt.Container) {
            java.awt.Container container = (java.awt.Container) component;
            Component[] components = container.getComponents();
            for (Component childComponent : components) {
                findAndHideButton(childComponent, buttonText);
            }
        }
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
        if (amountString.contains("0.00") || amountString.isEmpty()) {
            formattedAmount = "";
        } else {
            double amount = Double.parseDouble(amountString);
            formattedAmount = numFormat.format(amount);
        }
        return formattedAmount;
    }

    private String generateDiscountLabel(String discountValue, String paramName, String label) {
        String formattedDiscount = formatAmount(discountValue);
        if (discountValue.equals("0.00") || discountValue.contains("0.00")) {
            params.put(paramName, "");
            return "";
        } else {
            params.put(paramName, label);
            return formattedDiscount;
        }
    }

    private boolean loadReport() throws SQLException {
        vspMasterData.clear();
        if (oTrans.OpenRecord(psTransNox)) {
            String deliveryDate = deliveryDate = CommonUtils.xsDateShort((Date) oTrans.getMaster("dDelvryDt"));;
            if (deliveryDate.isEmpty()) {
                deliveryDate = "";
            } else {
                deliveryDate = CommonUtils.xsDateShort((Date) oTrans.getMaster("dDelvryDt"));
            }

            String salesExe = oTrans.getMaster("sSalesExe").toString();
            String salesExeFullName = formatName(salesExe);
            String ownerHomeAddress = "";
            String ownerOfficeAddress = "";
            if (oTrans.getMaster("cOfficexx").toString().equals("1")) {
                ownerOfficeAddress = oTrans.getMaster("sAddressx").toString().toUpperCase();
            }
            if (oTrans.getMaster("cOfficexx").toString().equals("0")) {
                ownerHomeAddress = oTrans.getMaster("sAddressx").toString().toUpperCase();
            }
            String notOfficeNumber = "";
            String officeNumber = "";
            if (oTrans.getMaster("cOwnerxxx").toString().equals("0")) {
                notOfficeNumber = oTrans.getMaster("sMobileNo").toString();
            }
            if (oTrans.getMaster("cOwnerxxx").toString().equals("1")) {
                officeNumber = oTrans.getMaster("sMobileNo").toString();
            }
            String platOrCsNo = "";
            if (oTrans.getMaster("sPlateNox").toString().isEmpty()) {
                platOrCsNo = oTrans.getMaster("sCSNoxxxx").toString();
            } else if (!oTrans.getMaster("sPlateNox").toString().isEmpty()) {
                platOrCsNo = oTrans.getMaster("sPlateNox").toString();
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
            String dnPyment = "";
            String nUPrice = formatAmount(oTrans.getMaster("nUnitPrce").toString());
            String dPayment = formatAmount(oTrans.getMaster(38).toString());
            if (oTrans.getMaster("nDownPaym").toString().contains("0.00")) {
                dnPyment = "0.00";
            } else {
                dnPyment = dPayment;
            }
            if (oTrans.getMaster("nUnitPrce").toString().contains("0.00")) {
                unitPrice = "0.00";
            } else {
                unitPrice = nUPrice;
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
            String nAccessAmount = formatAmount(oTrans.getMaster("nAccesAmt").toString());
            String tplInsuranceCompany = oTrans.getMaster("sInsTplNm").toString();
            String tplInsuranceAmount = oTrans.getMaster("nTPLAmtxx").toString();
            String displayTplInsuranceAmount = "";
            String displayTplInsuranceComp = "";
            String compreInsuranceCompany = oTrans.getMaster("sInsComNm").toString();
            String compreInsuranceAmount = oTrans.getMaster("nCompAmtx").toString();
            String displayCompInsuranceAmount = "";
            String displayCompInsuranceComp = "";
            switch (oTrans.getMaster("sTPLStatx").toString()) {
                case "1":
                    displayTplInsuranceComp = tplInsuranceCompany;
                    displayTplInsuranceAmount = "FREE";
                    break;
                case "0":
                case "2":
                case "4":
                    break;
                case "3":
                    displayTplInsuranceAmount = formatAmount(tplInsuranceAmount);
                    displayTplInsuranceComp = tplInsuranceCompany;
                    break;
                default:
                    break;
            }
            switch (oTrans.getMaster("sCompStat").toString()) {
                case "1":
                    displayTplInsuranceComp = compreInsuranceCompany;
                    displayCompInsuranceAmount = "FREE";
                    break;
                case "0":
                case "2":
                case "4":
                    break;
                case "3":
                    displayCompInsuranceAmount = formatAmount(compreInsuranceAmount);
                    displayCompInsuranceComp = compreInsuranceCompany;
                    break;
                default:
                    break;

            }
            String bankName = "";
            String promptNoteAmount = "";
            String amountFinance = "";
            String netMonthlyFinance = "";
            String promptPaymentDiscount = "";
            String grossMonthly = "";
            String termsFinance = "";
            String ratesFinance = "";
            String termsAndRates = "";
            if (oTrans.getVSPFinanceCount() > 0) {
                bankName = oTrans.getVSPFinance(4).toString().toUpperCase();
                promptNoteAmount = formatAmount(oTrans.getVSPFinance(10).toString());
                amountFinance = formatAmount(oTrans.getVSPFinance(5).toString());
                netMonthlyFinance = formatAmount(oTrans.getVSPFinance(9).toString());
                promptPaymentDiscount = formatAmount(oTrans.getVSPFinance(8).toString());
                grossMonthly = formatAmount(oTrans.getVSPFinance(12).toString());
                termsFinance = oTrans.getVSPFinance(6).toString();
                ratesFinance = formatAmount(oTrans.getVSPFinance(7).toString());
                termsAndRates = termsFinance + " / " + ratesFinance;
            }
            String rustAmount = "";
            String underAmount = "";
            String permaAmount = "";
            String tintAmount = "";
            double additionalAmount = 0.00;
            String additionalAmountDisplay = "";
            for (int i = 1; i <= oTrans.getVSPLaborCount(); i++) {
                String currentLaborDsc = oTrans.getVSPLaborDetail(i, 8).toString();
                String currentAmount = oTrans.getVSPLaborDetail(i, 4).toString();
                String currentStatus = oTrans.getVSPLaborDetail(i, 9).toString();
                if (currentLaborDsc.equalsIgnoreCase("RUSTPROOF")) {
                    rustAmount = formatAmount(currentAmount);
                } else if (currentLaborDsc.equalsIgnoreCase("UNDERCOAT")) {
                    underAmount = formatAmount(currentAmount);
                } else if (currentLaborDsc.equalsIgnoreCase("PERMASHINE") && currentStatus.equals("0")) {
                    permaAmount = formatAmount(currentAmount);
                } else if (currentLaborDsc.equalsIgnoreCase("TINT") && currentStatus.equals("0")) {
                    tintAmount = formatAmount(currentAmount);
                } else if (currentStatus.equals("1")) {
                    double amount = Double.parseDouble(currentAmount);
                    additionalAmount += amount;
                    additionalAmountDisplay = formatAmount(String.valueOf(additionalAmount));
                } else {
                    System.out.println("INVALID!");
                }

            }
            String promoValue = oTrans.getMaster(28).toString();
            String cashValue = oTrans.getMaster(32).toString();
            String bundleValue = oTrans.getMaster(31).toString();

            String promoDiscount = generateDiscountLabel(promoValue, "pRomo", "Promo disc:");
            String cashDiscount = generateDiscountLabel(cashValue, "caSh", "Cash disc:");
            String bundleDiscount = generateDiscountLabel(bundleValue, "bundle", "Bundle disc:");

            String branchName_1 = oTrans.getMaster("sBranchNm").toString().toUpperCase();
            String branchName_1_Display = "3. Deposit is good for 30 days(except in cases where stock is not available), and does not guarantee the buyer protection from sudden price increases. "
                    + branchName_1 + " reserves the right to sell the car after the agreed period and will reimburse deposit to the client less administrative and incidental fees.";
            String branchName_3 = oTrans.getMaster("sBranchNm").toString().toUpperCase();
            String branchName_3_Display = "4. " + branchName_3 + " controls neither production, delivery prices nor specifications. The company agrees to relay to the customer all information concerning the order and in turn, the customer agrees to absolve the company from blame if factory cannot meet the requirements.";
            String branchName_6 = oTrans.getMaster("sBranchNm").toString().toUpperCase();

            String brancName_6_Display = "6. As a matter of policy, Sales Personnel are not allowed to receive payments. Please remit all payments to our duly authorized cashier. All checks should be made payable to "
                    + branchName_6 + " .";
            String vsCode = "<" + oTrans.getMaster(1).toString() + ">";

            String buyersName = "";

            if (!oTrans.getMaster("sCoBuyrNm").toString().isEmpty()) {
                buyersName = oTrans.getMaster("sCompnyNm").toString() + " / " + oTrans.getMaster("sCoBuyrNm").toString();
            } else {
                buyersName = oTrans.getMaster("sCompnyNm").toString();
            }
            vspMasterData.add(new VSPTableMasterList(
                    "",
                    vsCode,
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
                    termsAndRates,
                    bankName,
                    promptNoteAmount,
                    amountFinance,
                    netMonthlyFinance,
                    promptPaymentDiscount,
                    grossMonthly,
                    rustAmount,
                    underAmount,
                    permaAmount,
                    tintAmount,
                    additionalAmountDisplay,
                    nAccessAmount,
                    oTrans.getMaster("nInsurAmt").toString(),
                    displayCompInsuranceAmount,
                    displayTplInsuranceAmount,
                    LtoAmount,
                    chmoDocStamps,
                    oTrans.getMaster("sTPLStatx").toString(),
                    oTrans.getMaster("sCompStat").toString(),
                    oTrans.getMaster("sInsurTyp").toString(),
                    oTrans.getMaster("nInsurYrx").toString(),
                    oTrans.getMaster("sInsTplCd").toString(),
                    oTrans.getMaster("sInsCodex").toString(),
                    promoDiscount,
                    oTrans.getMaster("nFleetDsc").toString(),
                    oTrans.getMaster("nSPFltDsc").toString(),
                    bundleDiscount,
                    cashDiscount,
                    oTrans.getMaster("nDealrInc").toString(),
                    paymentMethodDisplay,
                    oTrans.getMaster("sBnkAppCD").toString(),
                    totalAmount,
                    reservationAmount,
                    dnPyment,
                    netAmountDue,
                    oTrans.getMaster("nAmtPaidx").toString(),
                    oTrans.getMaster("nFrgtChrg").toString(),
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
                    buyersName.toUpperCase(),
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
                    oTrans.getMaster("sBranchNm").toString().toUpperCase(),
                    branchName_1_Display,
                    branchName_3_Display,
                    brancName_6_Display,//Branch Name
                    displayTplInsuranceComp.toUpperCase(),
                    displayCompInsuranceComp.toUpperCase(),
                    oTrans.getMaster("sTaxIDNox").toString(),
                    oTrans.getMaster("sJobNoxxx").toString(),
                    CommonUtils.xsDateMedium((Date) oTrans.getMaster("dBirthDte")),
                    oTrans.getMaster("sEmailAdd").toString(),
                    oTrans.getMaster("cOwnerxxx").toString(),
                    notOfficeNumber,
                    officeNumber,
                    oTrans.getMaster("cOfficexx").toString(),
                    oTrans.getMaster("cTrStatus").toString(),
                    oTrans.getMaster("sBrnchAdd").toString().toUpperCase()// Branch Address
            )
            );
        }
        String sourceFileName = "D://GGC_Java_Systems/reports/autoapp/vsp.jasper";
        String printFileName = null;
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(vspMasterData);
        String branchName = "";
        ResultSet name;
        String lsQuery = "SELECT b.sCompnyNm "
                + " FROM xxxSysUser a"
                + " LEFT JOIN GGC_ISysDBF.Client_Master b"
                + " ON a.sEmployNo  = b.sClientID"
                + " WHERE a.sUserIDxx = " + SQLUtil.toSQL(oApp.getUserID());
        name = oApp.executeQuery(lsQuery);
        try {
            if (name.next()) {
                branchName = name.getString("sCompnyNm");
            }
        } catch (SQLException ex) {
            Logger.getLogger(InquiryRefundPrintController.class.getName()).log(Level.SEVERE, null, ex);
        }
        params.put(
                "formName", "VEHICLE SALES PROPOSAL FORM");
        params.put(
                "brnchUserName", formatName(branchName).toUpperCase());
        params.put(
                "dnPymntAmount", "0.00");
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
