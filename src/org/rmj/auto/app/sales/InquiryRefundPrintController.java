/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.sales;

import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.swing.JRViewer;
import net.sf.jasperreports.view.JasperViewer;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;

/**
 * FXML Controller class
 *
 * @author Arsiela 
 * Date Created: 06-17-2023
 */
public class InquiryRefundPrintController implements Initializable {

    private GRider oApp;
    private MasterCallback oListener;

    private JasperPrint jasperPrint; //Jasper Libraries
    private JasperReport jasperReport;
    private JRViewer jrViewer;

    private String psTransNox;
    private int lnCtr = 0;
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

    public String setTransNox(String fsValue) {
        psTransNox = fsValue;
        return psTransNox;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        vbProgress.setVisible(true);
        timeline = new Timeline();
        generateReport();
        btnClose.setOnAction(this::cmdButton_Click);
        btnChangeAmt.setOnAction(this::cmdButton_Click);

    }

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private Stage getStage() {
        return (Stage) btnClose.getScene().getWindow();
    }

    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnChangeAmt":
                break;
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;
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

    private LocalDate strToDate(String val) {
        DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(val, date_formatter);
        return localDate;
    }

    private boolean loadReport() {
//        try {
//            //Create the parameter
            Map<String, Object> params = new HashMap<>();
            params.put("sCompnyNm", "Guanzon Group of Companies");
            params.put("sBranchNm", oApp.getBranchName());
            params.put("sAddressx", oApp.getAddress());
            refundprintdata.clear();
//            if (oTrans.loadReservation(psTransNox, false)) {
                for (lnCtr = 1; lnCtr <= 1; lnCtr++) {
                    refundprintdata.add(new InquiryTableRefund(
                            "1"
                        ,   "2"
                        ,   "3"
                        ,   "4"
                        ,   "5"
                        ,   "6"
                        ,   "7"
                        ,   "8"
                        ,   "9"
                    ));
                }
//            }
//
            refundpaymentsdata.clear();
//            if (oTrans.loadReservation(psTransNox, false)) {
                for (lnCtr = 1; lnCtr <= 3; lnCtr++) {
                    refundpaymentsdata.add(new InquiryTableRefund(
                            "det 1" + lnCtr
                        ,   "det 2"  + lnCtr
                        ,   "det 3"  + lnCtr
                        ,   "det 4"  + lnCtr
                        ,   "det 5"  + lnCtr
                        ,   "det 6"  + lnCtr
                        ,   "det 7" + lnCtr
                        ,   "det 8" + lnCtr
                        ,   "det 9" + lnCtr
                    ));
                }
//            }

            String sourceFileName = "D://GGC_Java_Systems/reports/autoapp/InquiryRefundPrint.jasper";
            String printFileName = null;
            JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(refundprintdata);
            JRBeanCollectionDataSource beanColDataSourceSub = new JRBeanCollectionDataSource(refundpaymentsdata);
            params.put("sPayments", beanColDataSourceSub);
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
//            return false;
//        } catch (SQLException ex) {
//            running = false;
//            vbProgress.setVisible(false);
//            timeline.stop();
//        }
        return false;
    }

}
