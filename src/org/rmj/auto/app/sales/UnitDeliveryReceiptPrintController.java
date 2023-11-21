/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.auto.app.sales;

import java.awt.Component;
import java.net.URL;
import java.sql.SQLException;
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
import org.rmj.auto.sales.base.VehicleDeliveryReceipt;

/**
 * FXML Controller class
 *
 * @author User
 */
public class UnitDeliveryReceiptPrintController implements Initializable {

    private VehicleDeliveryReceipt oTrans;
    private final String pxeModuleName = "Unit Delivery Receipt Print";
    private String psTransNox;
    private GRider oApp;
    private MasterCallback oListener;
    private JasperPrint jasperPrint; //Jasper Libraries
    private JRViewer jrViewer;
    private ObservableList<UnitDeliveryReceiptMasterList> udrMasterData = FXCollections.observableArrayList();
    private boolean running = false;
    final static int interval = 100;
    private Timeline timeline;
    private Integer timeSeconds = 3;
    private Map<String, Object> params = new HashMap<>();
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

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private Stage getStage() {
        return (Stage) btnClose.getScene().getWindow();
    }

    public void setTransNox(String fsValue) {
        psTransNox = fsValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTrans = new VehicleDeliveryReceipt(oApp, oApp.getBranchCode(), true);
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
                                Logger.getLogger(UnitDeliveryReceiptPrintController.class.getName()).log(Level.SEVERE, null, ex);
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

    private static String processAndUpperCase(VehicleDeliveryReceipt oTrans, int indexMaster) throws SQLException {
        String fieldValue = "";
        if (oTrans != null && !oTrans.getMaster(indexMaster).toString().isEmpty()) {
            fieldValue = oTrans.getMaster(indexMaster).toString().toUpperCase();
        }
        return fieldValue;
    }

    private boolean loadReport() throws SQLException {
        udrMasterData.clear();
        if (oTrans.OpenRecord(psTransNox)) {
            String deliveryDate = CommonUtils.xsDateShort((Date) oTrans.getMaster(2));;
            if (deliveryDate.isEmpty()) {
                deliveryDate = "";
            }

            String branchName = processAndUpperCase(oTrans, 37);
            String approvedBy = processAndUpperCase(oTrans, 15);
            String branchAdd = processAndUpperCase(oTrans, 36);
            String buyerName = "";
            if (!oTrans.getMaster("sCoCltNmx").toString().isEmpty()) {
                buyerName = oTrans.getMaster("sCompnyNm").toString() + " / " + oTrans.getMaster("sCoCltNmx").toString();
            } else {
                buyerName = oTrans.getMaster("sCompnyNm").toString();
            }
            String pymntMode = "";
            switch (oTrans.getMaster("cPayModex").toString()) {
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
            String buyerAddress = processAndUpperCase(oTrans, 23);
            String preparedBy = processAndUpperCase(oTrans, 35);
            String remarks = processAndUpperCase(oTrans, 6);
            String csNo = "";
            if (!oTrans.getMaster(26).toString().isEmpty()) {
                csNo = oTrans.getMaster(26).toString();
            } else {
                csNo = oTrans.getMaster(25).toString();
            }
            String engineNo = processAndUpperCase(oTrans, 27);
            String description = processAndUpperCase(oTrans, 24);
            String frameNo = processAndUpperCase(oTrans, 28);
            String purchasedNo = processAndUpperCase(oTrans, 3);
            String color = processAndUpperCase(oTrans, 39);

            String sRegex = "";
            sRegex = "\\s*\\b" + color + "\\b\\s*";
            description = description.replaceAll(sRegex, " ");
            udrMasterData.add(new UnitDeliveryReceiptMasterList(
                    "",
                    "",
                    deliveryDate,
                    purchasedNo,
                    "",
                    "",
                    remarks,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    approvedBy,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    buyerName.toUpperCase(),
                    buyerAddress,
                    description,
                    csNo.toUpperCase(),
                    "",
                    engineNo,
                    frameNo,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    preparedBy,
                    branchAdd,
                    branchName,
                    pymntMode,
                    color
            ));
        }
        String sReport = oTrans.getReport("udr");
        if (sReport.isEmpty()) {
            ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
            return false;
        }

        String sourceFileName = "D://GGC_Java_Systems/reports/autoapp/" + sReport;
        String printFileName = null;
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(udrMasterData);
        params.put(
                "quantity", "One(1)");
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
