/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.auto.app.views;

import java.awt.Component;
import java.net.URL;
import java.sql.SQLException;
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
import org.rmj.auto.clients.base.Activity;

/**
 * Activity Print FXML Controller class
 *
 * @author John Dave
 */
public class ActivityPrintController implements Initializable, ScreenInterface {

    private Activity oTrans;
    private GRider oApp;
    private MasterCallback oListener;
    private JasperPrint jasperPrint; //Jasper Libraries
    private JRViewer jrViewer;
    private ObservableList<ActivityTableList> actMasterData = FXCollections.observableArrayList();
    private List<ActivityMemberTable> actMembersData = new ArrayList<ActivityMemberTable>();
    private List<ActivityTownEntryTableList> townCitydata = new ArrayList<ActivityTownEntryTableList>();
    private List<ActivityVchlEntryTable> actVhclModelData = new ArrayList<ActivityVchlEntryTable>();
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
    @FXML
    private Button btnPrint;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTrans = new Activity(oApp, oApp.getBranchCode(), true); //Initialize ClientMaster
        oTrans.setCallback(oListener);
        oTrans.setWithUI(true);
        vbProgress.setVisible(true);
        btnPrint.setVisible(false);
        btnPrint.setDisable(true);
        timeline = new Timeline();
        generateReport();

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
                String dApproved = " ";
                if (!((String) oTrans.getDetail(lnCtr, "sApproved")).isEmpty()) {
                    dApproved = CommonUtils.xsDateShort((Date) oTrans.getDetail(lnCtr, "dApproved"));
                }
                String dEntry = dEntry = CommonUtils.xsDateShort((Date) oTrans.getDetail(lnCtr, "dEntryDte"));
                if (dEntry.isEmpty()) {
                    dEntry = " ";
                } else {
                    dEntry = dEntry = CommonUtils.xsDateShort((Date) oTrans.getDetail(lnCtr, "dEntryDte"));
                }
                String from = CommonUtils.xsDateLong((Date) oTrans.getDetail(lnCtr, "dDateFrom"));
                String to = CommonUtils.xsDateLong((Date) oTrans.getDetail(lnCtr, "dDateThru"));
                String logRemark = "";
                if (!oTrans.getDetail(lnCtr, "sLogRemrk").toString().isEmpty()) {
                    logRemark = oTrans.getDetail(lnCtr, "sLogRemrk").toString().toUpperCase();
                }
                String duration = from + " - " + to;
                actMasterData.add(new ActivityTableList(
                        oTrans.getDetail(lnCtr, "sActvtyID").toString().toUpperCase(),
                        oTrans.getDetail(lnCtr, "sActTitle").toString().toUpperCase(),
                        oTrans.getDetail(lnCtr, "sActDescx").toString().toUpperCase(),
                        oTrans.getDetail(lnCtr, "sActTypDs").toString().toUpperCase(),
                        duration,
                        "",
                        oTrans.getDetail(lnCtr, "sLocation").toString().toUpperCase(),
                        oTrans.getDetail(lnCtr, "sCompnynx").toString().toUpperCase(),
                        oTrans.getDetail(lnCtr, "nPropBdgt").toString(),
                        oTrans.getDetail(lnCtr, "nTrgtClnt").toString(),
                        logRemark,
                        oTrans.getDetail(lnCtr, "sRemarksx").toString().toUpperCase(),
                        dEntry,
                        dApproved,
                        oTrans.getDetail(lnCtr, "sDeptName").toString().toUpperCase(),
                        oTrans.getDetail(lnCtr, "sCompnyNm").toString().toUpperCase(),
                        oTrans.getDetail(lnCtr, "sBranchNm").toString().toUpperCase(),
                        oTrans.getDetail(lnCtr, "sProvName").toString().toUpperCase(),
                        oTrans.getDetail(lnCtr, "sActNoxxx").toString().toUpperCase()
                ));
            }
            townCitydata.clear();
            String town = "";
            for (lnCtr = 1; lnCtr <= oTrans.getActTownCount(); lnCtr++) {
                if (town.isEmpty()) {
                    town = oTrans.getActTown(lnCtr, "sTownName").toString();
                } else {
                    town = town + " , " + oTrans.getActTown(lnCtr, "sTownName").toString();

                }

            }

            townCitydata.add(new ActivityTownEntryTableList(
                    "", //ROW
                    "",
                    town.toUpperCase()
            ));
            actMembersData.clear();
            for (lnCtr = 1; lnCtr <= oTrans.getActMemberCount(); lnCtr++) {
                if (oTrans.getActMember(lnCtr, "cOriginal").equals("1")) {
                    actMembersData.add(new ActivityMemberTable(
                            String.valueOf(lnCtr), //ROW
                            oTrans.getActMember(lnCtr, "sDeptName").toString().toUpperCase(),
                            "",
                            oTrans.getActMember(lnCtr, "sCompnyNm").toString().toUpperCase(),
                            oTrans.getActMember(lnCtr, "sEmployID").toString().toUpperCase()));
                }
            }
            actVhclModelData.clear();
            for (lnCtr = 1; lnCtr <= oTrans.getActVehicleCount(); lnCtr++) {
                actVhclModelData.add(new ActivityVchlEntryTable(
                        String.valueOf(lnCtr), //ROW
                        oTrans.getActVehicle(lnCtr, "sSerialID").toString().toUpperCase(),
                        oTrans.getActVehicle(lnCtr, "sDescript").toString().toUpperCase(),
                        oTrans.getActVehicle(lnCtr, "sCSNoxxxx").toString().toUpperCase()));
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
