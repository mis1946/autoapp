/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.cashiering;

import java.awt.Component;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.auto.app.views.unloadForm;
import org.rmj.auto.cashiering.base.UnitSalesInvoice;

/**
 * FXML Controller class
 *
 * @author Arsiela
 * Date Created 11-20-2023
 */
public class VehicleSalesInvoicePrintController implements Initializable {
    
    private UnitSalesInvoice oTrans;
    private GRider oApp;
    private MasterCallback oListener;

    private JasperPrint jasperPrint; //Jasper Libraries
    private JRViewer jrViewer;

    private int lnCtr = 0;
    private boolean running = false;
    final static int interval = 100;
    private Timeline timeline;
    private Integer timeSeconds = 3;
    private String psTransNox;
    private final String pxeModuleName = "Vehicle Sales Invoice Print";
    unloadForm unload = new unloadForm(); //Used in Close Button
    private ObservableList<VehicleSalesInvoiceTable> PrintData = FXCollections.observableArrayList();
    
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnPrint;
    @FXML
    private Button btnClose;
    @FXML
    private VBox vbProgress;
    @FXML
    private AnchorPane reportPane;
    
    public void setObject(UnitSalesInvoice foValue){
        oTrans = foValue;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        oTrans = new UnitSalesInvoice(oApp, oApp.getBranchCode(), true); 
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
                    if (oTrans.UpdateRecord()) {
                        oTrans.setMaster(14, "1");
                        if (oTrans.SaveRecord()) {
                            ShowMessageFX.Information(null, pxeModuleName, "Printed Successfully");
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
                        }
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
                    }
                    CommonUtils.closeStage(btnClose);
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, "Print Aborted");
                }
            } catch (JRException ex) {

                ShowMessageFX.Warning(null, pxeModuleName, "Print Aborted");
            } catch (SQLException ex) {
                Logger.getLogger(VehicleSalesInvoicePrintController.class.getName()).log(Level.SEVERE, null, ex);
            }
            break;

            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;
        }
    }

    //@Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private Stage getStage() {
        return (Stage) btnClose.getScene().getWindow();
    }

    public void setTransNox(String fsValue) {
        psTransNox = fsValue;
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

                            loadReport();
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
        findAndHideButton(jrViewer, "Print");
        findAndHideButton(jrViewer, "Save");
        btnPrint.setVisible(true);
        btnPrint.setDisable(false);
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

    private boolean loadReport() {
        try {
            //Create the parameter
            Map<String, Object> params = new HashMap<>();
           // params.put("sCompnyNm", "Guanzon Group of Companies");
           // params.put("sBranchNm", oApp.getBranchName());
           //params.put("sAddressx", oApp.getAddress());
           params.put("quantity", "One(1)");
            PrintData.clear();
            if (oTrans.OpenRecord(psTransNox)) {
                
                //String sTransDte = CommonUtils.xsDateMedium((Date) oTrans.getMaster(3));
                String sTransDte = CommonUtils.xsDateShort((Date) oTrans.getMaster(3));
                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date = LocalDate.parse(sTransDte, inputFormatter);
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                sTransDte = date.format(outputFormatter);
                
                String sUDRSINo = "SI # " + (String) oTrans.getMaster(5) + "/ DR # " + (String) oTrans.getMaster(6);
                String sCustName = "";
                String sDescrpt = (String) oTrans.getMaster(18);
                String sColor = (String) oTrans.getMaster(23);
                String sRegex = "";
                
                if (!((String) oTrans.getMaster(36)).isEmpty()){
                    sCustName = (String) oTrans.getMaster(30) + " / " + (String) oTrans.getMaster(36);
                } else {
                    sCustName = (String) oTrans.getMaster(30);
                }
                
                sRegex = "\\s*\\b" + sColor + "\\b\\s*";
                sDescrpt = sDescrpt.replaceAll(sRegex, " ");
                System.out.println(sDescrpt);
                
                String pymntMode = "";
                switch (oTrans.getMaster(37).toString()) {
                    case "0":
                        pymntMode = "CASH";
                        break;
                    case "1":
                        pymntMode = "BANK PURCHASE ORDER";
                        break;
                    case "2":
                        pymntMode = "BANK FINANCING";
                        break;
                    case "3":
                        pymntMode = "COMPANY PURCHASE ORDER";
                        break;
                    case "4":
                        pymntMode = "COMPANY FINANCING";
                        break;
                }
                
                String sCSPlateNo = "";
                if (!oTrans.getMaster(20).toString().isEmpty()) {
                    sCSPlateNo = oTrans.getMaster(20).toString().toUpperCase();
                } else {
                    sCSPlateNo = oTrans.getMaster(19).toString().toUpperCase();
                }
                
                String sVatableAmt = formatAmount(String.valueOf((Double) oTrans.getMaster(9) - (Double) oTrans.getMaster(12)));
                String sSalesAgent = (String) oTrans.getMaster(24);
                if(!sSalesAgent.isEmpty()){
                    sSalesAgent = formatName((String) oTrans.getMaster(24)).toUpperCase();
                }
                
                PrintData.add(new VehicleSalesInvoiceTable(
                        ((String) oTrans.getMaster(1)).toUpperCase()
                        , ((String) oTrans.getMaster(2)).toUpperCase()
                        , sTransDte //(String) oTrans.getMaster(3)
                        , ((String) oTrans.getMaster(4)).toUpperCase()
                        , sUDRSINo //(String) oTrans.getMaster(5)
                        , sUDRSINo //(String) oTrans.getMaster(6)
                        , ((String) oTrans.getMaster(7)).toUpperCase()
                        , ((String) oTrans.getMaster(8)).toUpperCase()
                        , formatAmount(oTrans.getMaster(9).toString())
                        , formatAmount(oTrans.getMaster(10).toString())
                        , formatAmount(oTrans.getMaster(11).toString())
                        , formatAmount(oTrans.getMaster(12).toString())
                        , formatAmount(oTrans.getMaster(13).toString())
                        , ((String) oTrans.getMaster(14)).toUpperCase()
                        , ((String) oTrans.getMaster(15)).toUpperCase()
                        , ((String) oTrans.getMaster(16)).toUpperCase()
                        , CommonUtils.xsDateMedium((Date) oTrans.getMaster(17))
                        , sDescrpt.toUpperCase() //(String) oTrans.getMaster(18)
                        , sCSPlateNo.toUpperCase() //(String) oTrans.getMaster(19)
                        , sCSPlateNo.toUpperCase() //(String) oTrans.getMaster(20)
                        , ((String) oTrans.getMaster(21)).toUpperCase()
                        , ((String) oTrans.getMaster(22)).toUpperCase()
                        , sColor.toUpperCase() //(String) oTrans.getMaster(23)
                        , "SALES AGENT: " + sSalesAgent.toUpperCase()
                        , ((String) oTrans.getMaster(25)).toUpperCase()
                        , formatAmount( oTrans.getMaster(26).toString())
                        , formatAmount(oTrans.getMaster(27).toString())
                        , formatAmount( oTrans.getMaster(28).toString())
                        , formatAmount( oTrans.getMaster(29).toString())
                        , sCustName.toUpperCase() //(String) oTrans.getMaster(30)
                        , ((String) oTrans.getMaster(31)).toUpperCase()
                        , ((String) oTrans.getMaster(32)).toUpperCase()
                        , ((String) oTrans.getMaster(33)).toUpperCase()
                        , ((String) oTrans.getMaster(34)).toUpperCase()
                        , ((String) oTrans.getMaster(35)).toUpperCase()
                        , sCustName //(String) oTrans.getMaster(36)
                        , "TERMS: " + pymntMode.toUpperCase() //(String) oTrans.getMaster(37)
                        , ((String) oTrans.getMaster(38)).toUpperCase()
                        ,sVatableAmt
                       ));

            }

            String sReportNme = oTrans.getReport("VehicleSalesInvoice");
            if (sReportNme.isEmpty()){
                ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
                return false;
            }
            
            String sourceFileName = "D://GGC_Java_Systems/reports/autoapp/"+sReportNme;
            String printFileName = null;
            JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(PrintData);
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
        } catch (SQLException ex) {
            running = false;
            vbProgress.setVisible(false);
            timeline.stop();
        }
        return false;
    }
    
    private static String formatAmount(String amountString) {
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
    
    private static String formatName(String fullName) {
        String[] nameParts = fullName.split(" ");
        String firstNameInitial = nameParts[0].substring(0, 1);
        String lastName = nameParts[nameParts.length - 1];
        return firstNameInitial + ". " + lastName;
    }
}
