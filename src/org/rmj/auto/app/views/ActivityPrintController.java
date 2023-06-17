/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.auto.app.views;

import java.net.URL;
import java.sql.SQLException;
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
import org.rmj.auto.clients.base.Activity;
import org.rmj.auto.sales.base.InquiryProcess;

/**
 * Reserve Print FXML Controller class
 *
 * @author John Dave
 */
public class ActivityPrintController implements Initializable, ScreenInterface {

    private Activity oTrans;
    private GRider oApp;
    private MasterCallback oListener;

    private JasperPrint jasperPrint; //Jasper Libraries
    private JasperReport jasperReport;
    private JRViewer jrViewer;
    private ObservableList<ActivityTableList> actMasterData = FXCollections.observableArrayList();
    private List<ActivityMemberTable> actMembersData = new ArrayList<ActivityMemberTable>();
    private List<ActivityTownEntryTableList> townCitydata = new ArrayList<ActivityTownEntryTableList>();
    private List<ActivityVchlEntryTable> actVhclModelData = new ArrayList<ActivityVchlEntryTable>();
//    private ObservableList<ActivityTownEntryTableList> townCitydata = FXCollections.observableArrayList();
//    private ObservableList<ActivityVchlEntryTable> actVhclModelData = FXCollections.observableArrayList();
    private boolean running = false;
    final static int interval = 100;
    private Timeline timeline;
    private Integer timeSeconds = 3;

    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnClose;

    private final String pxeModuleName = "ActivityPrint";
    unloadForm unload = new unloadForm(); //Used in Close Button
    @FXML
    private AnchorPane reportPane;

    @FXML
    private VBox vbProgress;
    private String psTransNox;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTrans = new Activity(oApp, oApp.getBranchCode(), true); //Initialize ClientMaster
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

    @Override
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
                                Logger.getLogger(ActivityPrintController.class.getName()).log(Level.SEVERE, null, ex);
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

    private LocalDate strToDate(String val) {
        DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(val, date_formatter);
        return localDate;
    }

    public void setTransNox(String fsValue) {
        psTransNox = fsValue;
    }

    private boolean loadReport() throws SQLException {
        //Create the parameter
        int lnCtr = 1;
        Map<String, Object> params = new HashMap<>();
        actMasterData.clear();
        if (oTrans.OpenRecord(psTransNox)) {
            for (lnCtr = 1; lnCtr <= oTrans.getItemCount(); lnCtr++) {
                actMasterData.add(new ActivityTableList(
                        oTrans.getDetail(lnCtr, "sActvtyID").toString(),
                        oTrans.getDetail(lnCtr, "sActTitle").toString(),
                        oTrans.getDetail(lnCtr, "sActDescx").toString(),
                        "",
                        CommonUtils.xsDateShort((Date) oTrans.getDetail(lnCtr, "dDateFrom")),
                        CommonUtils.xsDateShort((Date) oTrans.getDetail(lnCtr, "dDateThru")),
                        oTrans.getDetail(lnCtr, "sLocation").toString(),
                        oTrans.getDetail(lnCtr, "sCompnynx").toString(),
                        oTrans.getDetail(lnCtr, "nPropBdgt").toString(),
                        oTrans.getDetail(lnCtr, "nTrgtClnt").toString(),
                        oTrans.getDetail(lnCtr, "sLogRemrk").toString(),
                        oTrans.getDetail(lnCtr, "sRemarksx").toString(),
                        CommonUtils.xsDateShort((Date) oTrans.getDetail(lnCtr, "dDateThru")),
                        "",
                        oTrans.getDetail(lnCtr, "sDeptName").toString(),
                        oTrans.getDetail(lnCtr, "sCompnyNm").toString(),
                        oTrans.getDetail(lnCtr, "sBranchNm").toString(),
                        oTrans.getDetail(lnCtr, "sProvName").toString()));
            }
            townCitydata.clear();
            for (lnCtr = 1; lnCtr <= oTrans.getActTownCount(); lnCtr++) {
                townCitydata.add(new ActivityTownEntryTableList(
                        String.valueOf(lnCtr), //ROW
                        oTrans.getActTown(lnCtr, "sTownIDxx").toString(),
                        oTrans.getActTown(lnCtr, "sTownName").toString()));
            }
            actMembersData.clear();
            for (lnCtr = 1; lnCtr <= oTrans.getActMemberCount(); lnCtr++) {
                if (oTrans.getActMember(lnCtr, "cOriginal").equals("1")) {
                    actMembersData.add(new ActivityMemberTable(
                            String.valueOf(lnCtr), //ROW
                            oTrans.getActMember(lnCtr, "sDeptName").toString(),
                            "",
                            oTrans.getActMember(lnCtr, "sCompnyNm").toString(), // Fifth column (Department)
                            oTrans.getActMember(lnCtr, "sEmployID").toString()));
                }
            }
            actVhclModelData.clear();
            for (lnCtr = 1; lnCtr <= oTrans.getActVehicleCount(); lnCtr++) {
                actVhclModelData.add(new ActivityVchlEntryTable(
                        String.valueOf(lnCtr), //ROW
                        oTrans.getActVehicle(lnCtr, "sSerialID").toString(),
                        oTrans.getActVehicle(lnCtr, "sDescript").toString(),
                        oTrans.getActVehicle(lnCtr, "sCSNoxxxx").toString()));
            }
        }
        String sourceFileName = "D://GGC_Java_Systems/reports/autoapp/ActivityPrint.jasper";
        String printFileName = null;
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(actMasterData);
        JRBeanCollectionDataSource vehicle = new JRBeanCollectionDataSource(actVhclModelData);
        JRBeanCollectionDataSource town = new JRBeanCollectionDataSource(townCitydata);
        JRBeanCollectionDataSource member = new JRBeanCollectionDataSource(actMembersData);

        params.put("vehicle", vehicle);
        params.put("town", town);
        params.put("member", member);
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
