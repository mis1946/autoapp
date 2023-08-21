/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.sales;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.swing.JRViewer;
import net.sf.jasperreports.swing.JRViewerToolbar;
import net.sf.jasperreports.view.JasperViewer;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.auto.app.views.InputTextFormatter;
import org.rmj.auto.sales.base.InquiryMaster;

/**
 * FXML Controller class
 *
 * @author Arsiela Date Created: 06-17-2023
 */
public class InquiryRefundPrintController implements Initializable {

    private GRider oApp;
    private MasterCallback oListener;
    private InquiryMaster oTrans;

    private JasperPrint jasperPrint; //Jasper Libraries
    private JasperReport jasperReport;
    private JRViewer jrViewer;

    private String psTransNox;
    private int lnCtr = 0;
    private double dPaymentAmt = 0.00;
    private double dRefundAmt = 0.00;
    private boolean running = false;
    final static int interval = 100;
    private Timeline timeline;
    private Integer timeSeconds = 3;
    private final String pxeModuleName = "Refund Print";

    private ObservableList<InquiryTableRefund> refundprintdata = FXCollections.observableArrayList();
    private List<InquiryTableRefund> refundpaymentsdata = new ArrayList();
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnClose;
    @FXML
    private VBox vbProgress;
    @FXML
    private AnchorPane reportPane;
    @FXML
    private Button btnChangeAmt;
    @FXML
    private TextField txtField01;
    @FXML
    private Button btnUpdateAmt;
    @FXML
    private Button btnPrint;
    @FXML
    private Button btnCancel;

    public String setTransNox(String fsValue) {
        psTransNox = fsValue;
        return psTransNox;
    }
    
    public void setObject(InquiryMaster foValue){
        oTrans = foValue;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Hide buttons and text field
        btnPrint.setVisible(false);
        btnPrint.setManaged(false);
        btnCancel.setVisible(false);
        btnCancel.setManaged(false);
        btnUpdateAmt.setVisible(false);
        btnUpdateAmt.setManaged(false);
        txtField01.setVisible(false);
        txtField01.setManaged(false);
        addRequiredFieldListener(txtField01);
        btnChangeAmt.setVisible(false);
        btnChangeAmt.setManaged(false);
        
        vbProgress.setVisible(true);
        timeline = new Timeline();
        generateReport();
        btnClose.setOnAction(this::cmdButton_Click);
        btnChangeAmt.setOnAction(this::cmdButton_Click);
        btnUpdateAmt.setOnAction(this::cmdButton_Click);
        btnPrint.setOnAction(this::cmdButton_Click);
        btnCancel.setOnAction(this::cmdButton_Click);
        
        Pattern pattern;
        pattern = Pattern.compile("^\\d*(\\.\\d{0,2})?$");
        txtField01.setTextFormatter(new InputTextFormatter(pattern)); //Amount
        
    }

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private Stage getStage() {
        return (Stage) btnClose.getScene().getWindow();
    }
    
    //Animation    
    private void shakeTextField(TextField textField) {
        Timeline timeline = new Timeline();
        double originalX = textField.getTranslateX();

        // Add keyframes for the animation
        KeyFrame keyFrame1 = new KeyFrame(Duration.millis(0), new KeyValue(textField.translateXProperty(), 0));
        KeyFrame keyFrame2 = new KeyFrame(Duration.millis(100), new KeyValue(textField.translateXProperty(), -5));
        KeyFrame keyFrame3 = new KeyFrame(Duration.millis(200), new KeyValue(textField.translateXProperty(), 5));
        KeyFrame keyFrame4 = new KeyFrame(Duration.millis(300), new KeyValue(textField.translateXProperty(), -5));
        KeyFrame keyFrame5 = new KeyFrame(Duration.millis(400), new KeyValue(textField.translateXProperty(), 5));
        KeyFrame keyFrame6 = new KeyFrame(Duration.millis(500), new KeyValue(textField.translateXProperty(), -5));
        KeyFrame keyFrame7 = new KeyFrame(Duration.millis(600), new KeyValue(textField.translateXProperty(), 5));
        KeyFrame keyFrame8 = new KeyFrame(Duration.millis(700), new KeyValue(textField.translateXProperty(), originalX));

        // Add keyframes to the timeline
        timeline.getKeyFrames().addAll(
                keyFrame1, keyFrame2, keyFrame3, keyFrame4, keyFrame5, keyFrame6, keyFrame7, keyFrame8
        );

        // Play the animation
        timeline.play();
    }

    //Validation
    private void addRequiredFieldListener(TextField textField) {
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && textField.getText().isEmpty()) {
                shakeTextField(textField);
                textField.getStyleClass().add("required-field");
            } else {
                textField.getStyleClass().remove("required-field");
            }
        });
    }

    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnChangeAmt":
                initButton(true);
                btnPrint.setVisible(false);
                btnPrint.setManaged(false);
                break;
            case "btnUpdateAmt":
                if(txtField01.getText().isEmpty()){
                    ShowMessageFX.Warning(null, pxeModuleName, "Please input valid amount to be refunded.");
                    return;
                }
                if( Double.valueOf(txtField01.getText()) <= 0.00){
                    ShowMessageFX.Warning(null, pxeModuleName, "Please input valid amount to be refunded.");
                    return;
                }
                if( Double.valueOf(txtField01.getText()) > dPaymentAmt){
                    ShowMessageFX.Warning(null, pxeModuleName, "You are not allowed to refund more than the amount of total payments.");
                    return;
                }
                
                refundPrint(Double.valueOf(txtField01.getText()));
                initButton(false);
                break;
            case "btnCancel":
                initButton(false);
                btnPrint.setVisible(true);
                btnPrint.setManaged(true);
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
                    //Logger.getLogger(InquiryRefundPrintController.class.getName()).log(Level.SEVERE, null, ex);
                    ShowMessageFX.Warning(null, pxeModuleName, "Print Aborted");
                }
                break;

            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;
        }
    }
    
    private void refundPrint(Double fdValue){
        String sUserName = "";
        ResultSet name;
        String lsQuery = "SELECT b.sCompnyNm " +
                          " FROM xxxSysUser a" +
                          " LEFT JOIN GGC_ISysDBF.Client_Master b" +  
                              " ON a.sEmployNo  = b.sClientID" +
                          " WHERE a.sUserIDxx = " + SQLUtil.toSQL(oApp.getUserID());
        name = oApp.executeQuery(lsQuery);
        try {
            if(name.next()){
                sUserName = name.getString("sCompnyNm");
            }             
        } catch (SQLException ex) {
            Logger.getLogger(InquiryRefundPrintController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Date serverDate = oApp.getServerDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(serverDate);
        
        Map<String, Object> params = new HashMap<>();
        String sourceFileName = "D://GGC_Java_Systems/reports/autoapp/InquiryRefund.jasper";
        String printFileName = null;
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(refundprintdata);
        JRBeanCollectionDataSource beanColDataSourceSub = new JRBeanCollectionDataSource(refundpaymentsdata);
        params.put("sPayments", beanColDataSourceSub);
        params.put("sSourceCd", calendar.get(Calendar.YEAR)+""+(calendar.get(Calendar.MONTH)+1)+""+calendar.get(Calendar.DAY_OF_MONTH)+ "V000011154");
        params.put("sRefundNo", "V00100011");
        params.put("sRefundDate", CommonUtils.xsDateLong(oApp.getServerDate()));
        params.put("sPreparedBy", sUserName);
        dRefundAmt = fdValue;
        params.put("sRefundAmt", String.format("%.2f", dRefundAmt) );
        try {
            jasperPrint = JasperFillManager.fillReport(sourceFileName, params, beanColDataSource);
            printFileName = jasperPrint.toString();
            if (printFileName != null) {
                showReport();
            }
        } catch (JRException ex) {
            System.out.println("ERROR PRINT");
            running = false;
            vbProgress.setVisible(false);
            timeline.stop();
        }
    }

    /*Convert Date to String*/
    private LocalDate strToDate(String val) {
        DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(val, date_formatter);
        return localDate;
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
                            initButton(false);
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
        // Find and hide the buttons
        findAndHideButton(jrViewer, "Print");
        findAndHideButton(jrViewer, "Save");
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
        
        btnPrint.setVisible(true);
        btnPrint.setManaged(true);
        
    }
    
    private void findAndHideButton(Component component, String buttonText) {
        if (component instanceof AbstractButton) {
            AbstractButton button = (AbstractButton) component;
            if(button.getToolTipText() != null) {
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
        //Create the parameter
        Map<String, Object> params = new HashMap<>();
//        params.put("sCompnyNm", "Guanzon Group of Companies");
//        params.put("sBranchNm", oApp.getBranchName());
//        params.put("sAddressx", oApp.getAddress());

        lnCtr = 0;
        double dTotal = 0.00;
        refundpaymentsdata.clear();
        for (lnCtr = 1; lnCtr <= 3; lnCtr++) {
            dTotal = 15000.00;
            refundpaymentsdata.add(new InquiryTableRefund(
                    "PR1010063" + lnCtr,
                     "2023-06-20",
                     "RSV10106202023-0001",
                     "VEHICLE SALES ADVANCES ITO AY TEST LANG TEST LANG KASI ITO",
                     "5000.00",
                     "det 6" + lnCtr,
                     String.valueOf(lnCtr),
                     "det 8" + lnCtr,
                     "det 9" + lnCtr
            ));
        }
        refundprintdata.clear();
        for (lnCtr = 1; lnCtr <= 1; lnCtr++) {
            refundprintdata.add(new InquiryTableRefund(
                     "ARSIELA LAVARIAS",
                     "DAGUPAN CITY, PANGASINAN",
                     "NO BUDGET KULANG TALAGA",
                     "JUAN DELA CRUZ",
                     "",
                     "",
                     "",
                     "",
                     ""
            ));
        }
        dPaymentAmt = dTotal;
        dRefundAmt = dTotal;
        refundPrint(dPaymentAmt);
//        String sourceFileName = "D://GGC_Java_Systems/reports/autoapp/InquiryRefund.jasper";
//        String printFileName = null;
//        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(refundprintdata);
//        JRBeanCollectionDataSource beanColDataSourceSub = new JRBeanCollectionDataSource(refundpaymentsdata);
//        params.put("sPayments", beanColDataSourceSub);
//        params.put("sSourceCd", CommonUtils.xsDateShort(oApp.getServerDate()) + "V000011154");
//        params.put("sRefundNo", "V00100011");
//        params.put("sRefundDate", CommonUtils.xsDateLong(oApp.getServerDate()));
//        params.put("sPreparedBy", oApp.getClientName());
//        params.put("sRefundAmt", String.format("%.2f", dPaymentAmt) );
//        try {
//            jasperPrint = JasperFillManager.fillReport(sourceFileName, params, beanColDataSource);
//            printFileName = jasperPrint.toString();
//            if (printFileName != null) {
//                showReport();
//            }
//        } catch (JRException ex) {
//            System.out.println("ERROR PRINT");
//            running = false;
//            vbProgress.setVisible(false);
//            timeline.stop();
//        }
        return false;
    }
    
    private void initButton(boolean lbShow){
        btnCancel.setVisible(lbShow);
        btnCancel.setManaged(lbShow);
        btnUpdateAmt.setVisible(lbShow);
        btnUpdateAmt.setManaged(lbShow);
        txtField01.setVisible(lbShow);
        txtField01.setManaged(lbShow);
        btnChangeAmt.setVisible(!lbShow);
        btnChangeAmt.setManaged(!lbShow);
        txtField01.clear();
        /*Clear Red Color for required fileds*/
        txtField01.getStyleClass().remove("required-field");
    }

}
