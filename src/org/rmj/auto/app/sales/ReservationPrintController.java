/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.auto.app.sales;

import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
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
import org.rmj.auto.app.views.ScreenInterface;
import org.rmj.auto.app.views.unloadForm;
import org.rmj.auto.sales.base.InquiryProcess;
/**
 * Reserve Print FXML Controller class
 *
 * @author John Dave 
 */
public class ReservationPrintController implements Initializable, ScreenInterface {
    private InquiryProcess oTrans;
    private GRider oApp;
    private MasterCallback oListener;
    
    private JasperPrint jasperPrint; //Jasper Libraries
    private JasperReport jasperReport;
    private JRViewer jrViewer;
    
   
    private int lnCtr = 0;
    private boolean running = false;
    final static int interval = 100;
    private Timeline timeline;
    private Integer timeSeconds = 3;
    
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnClose;
    
    
    private final String pxeModuleName = "ReservationPrint";
    unloadForm unload = new unloadForm(); //Used in Close Button
    @FXML
    private AnchorPane reportPane;
    private ObservableList<InquiryTableVehicleSalesAdvances> vhlApprovalPrintData = FXCollections.observableArrayList();
    @FXML
    private VBox vbProgress;
    private String[] psTransNox;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTrans = new InquiryProcess(oApp, oApp.getBranchCode(), true); //Initialize ClientMaster
        oTrans.setCallback(oListener);
        oTrans.setWithUI(true);
        vbProgress.setVisible(true);
        timeline = new Timeline();
        generateReport();
        btnClose.setOnAction(this::cmdButton_Click);
        
      
        
    }
    private void cmdButton_Click(ActionEvent event) {
       String lsButton = ((Button)event.getSource()).getId();
               switch (lsButton){
                   case "btnClose":
                       CommonUtils.closeStage(btnClose);
                      break;
                   default:
                       ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                       break;
    }
 }
    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }
        private Stage getStage(){
            return (Stage) btnClose.getScene().getWindow();
    }
    private void hideReport(){
        jrViewer =  new JRViewer(null);
        reportPane.getChildren().clear();
        jrViewer.setVisible(false);
        running = false;
        reportPane.setVisible(false);
        timeline.stop();
    }    
      private void generateReport(){
        hideReport();
        if(!running){
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.getKeyFrames().add(
                    new KeyFrame(Duration.seconds(1), (ActionEvent event1) -> {
                        timeSeconds--;
                        // update timerLabel
                        if(timeSeconds <= 0){
                           timeSeconds = 0 ; 
                        }
                        if (timeSeconds == 0) {

                            loadReport();
                        }
                } // KeyFrame event handler
            ));
            timeline.playFromStart();
        }
    }
     private void showReport(){
        vbProgress.setVisible(false);
        jrViewer =  new JRViewer(jasperPrint);
        jrViewer.setZoomRatio(0.75f);
        SwingNode swingNode = new SwingNode();
        jrViewer.setOpaque(true);
        jrViewer.setVisible(true);
        jrViewer.setFitPageZoomRatio();
        swingNode.setContent(jrViewer);
        swingNode.setVisible(true);
        reportPane.setTopAnchor(swingNode,0.0);
        reportPane.setBottomAnchor(swingNode,0.0);
        reportPane.setLeftAnchor(swingNode,0.0);
        reportPane.setRightAnchor(swingNode,0.0);
        reportPane.getChildren().add(swingNode);
        running = true;
        reportPane.setVisible(true);
        timeline.stop();
    }
    private LocalDate strToDate(String val){
          DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
          LocalDate localDate = LocalDate.parse(val, date_formatter);
          return localDate;
    } 
    public void setTransNox(String[] fsValue){
            psTransNox = fsValue;
    }
    private boolean loadReport(){
            try {
                //Create the parameter
                Map<String, Object> params = new HashMap<>();
                params.put("sCompnyNm", "Guanzon Group of Companies");
                params.put("sBranchNm", oApp.getBranchName());
                params.put("sAddressx", oApp.getAddress());
            vhlApprovalPrintData.clear();
            String[] fsValues = {"02", "V00123000003", "V00123000004", "V00123000005", "V00123000006"};
            if (oTrans.loadReservation(fsValues, false)){
                 for (lnCtr = 1; lnCtr <= oTrans.getReserveCount(); lnCtr++){  
                        //Iterate over the data and count the approved item
                        String amountString = oTrans.getInqRsv(lnCtr,"nAmountxx").toString();
                        //Convert the amount to a decimal value
                        double amount = Double.parseDouble(amountString);
                        //Format the decimal value with decimal separators
                        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
                        String formattedAmount = decimalFormat.format(amount);
                        String res = oTrans.getInqRsv(lnCtr, "cResrvTyp").toString();
                                if (res.equals("0")) {
                                   res = "Reservation";
                                }
                                if(res.equals("1")){
                                   res = "Deposit";
                                }
                                 if(res.equals("2")){
                                   res = "Safeguard Duty";
                                }
                            vhlApprovalPrintData.add(new InquiryTableVehicleSalesAdvances(
                            false,
                            String.valueOf(lnCtr),
                            CommonUtils.xsDateShort((Date) oTrans.getInqRsv(lnCtr,"dTransact")),
                            res,        
                            oTrans.getInqRsv(lnCtr,"sReferNox").toString(),
                            formattedAmount,
                            oTrans.getInqRsv(lnCtr,"cTranStat").toString(),
                            oTrans.getInqRsv(lnCtr,"sRemarksx").toString(),
                            oTrans.getInqRsv(lnCtr,"sApprovby").toString(),
                            oTrans.getInqRsv(lnCtr,"dApproved").toString(),
                            oTrans.getInqRsv(lnCtr,"sTransNox").toString(),
                            oTrans.getInqRsv(lnCtr,"sCompnyNm").toString(),
                            oTrans.getInqRsv(lnCtr,"sSeNamexx").toString(),
                            oTrans.getInqRsv(lnCtr,"sDescript").toString()));
                    }       
                }
                String sourceFileName ="D://GGC_Java_Systems/reports/autoapp/reserve.jasper";
                String printFileName = null;
                JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(vhlApprovalPrintData);
                try {
                    jasperPrint = JasperFillManager.fillReport(sourceFileName, params, beanColDataSource);
                    printFileName = jasperPrint.toString();
                    if(printFileName != null){
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
}



    