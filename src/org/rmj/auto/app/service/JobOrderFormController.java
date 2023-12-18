/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.auto.app.service;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.auto.app.sales.UnitDeliveryReceiptFormController;
import org.rmj.auto.app.sales.VSPFormController;
import org.rmj.auto.app.views.CancelForm;
import org.rmj.auto.app.views.ScreenInterface;
import org.rmj.auto.app.views.unloadForm;
import org.rmj.auto.clients.base.Activity;

/**
 * FXML Controller class
 *
 * @author User
 */
public class JobOrderFormController implements Initializable, ScreenInterface {

    private GRider oApp;
//    private Activity oTrans;
    private MasterCallback oListener;
    CancelForm cancelform = new CancelForm(); //Object for closing form
    private final String pxeModuleName = "Job Order Information"; //Form Title
    private int pnEditMode;//Modifying fields
    unloadForm unload = new unloadForm(); //Used in Close Button
    ObservableList<String> cType = FXCollections.observableArrayList("EVENT", "SALES CALL", "PROMO");

    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnClose;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnBrowse;
    @FXML
    private Button btnPrint;
    @FXML
    private Label lblVSPStatus;
    @FXML
    private TextField txtField36;
    @FXML
    private TextField txtField37;
    @FXML
    private Button btnCancelJobOrder;
    @FXML
    private TableView<?> tblViewLabor;
    @FXML
    private TableView<?> tblViewLabor1;
    @FXML
    private TableView<?> tblViewLabor2;
    @FXML
    private TableView<?> tblViewLabor3;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        oListener = (int fnIndex, Object foValue) -> {
//            System.out.println("Set Class Value " + fnIndex + "-->" + foValue);
//        };
//        oTrans = new VehicleSalesProposalMaster(oApp, oApp.getBranchCode(), true); //Initialize VehicleSalesProposalMaster
//        oTrans.setCallback(oListener);
//        oTrans.setWithUI(true);  /*Set Capitalization Fields*/
        initCapitalizationFields();

        /*Set comboBox*/
        initSetComboBoxtoJobOrderMaster();

        /*Set Combo Items*/
        initComboItems();

        /*Monitoring Inquiry Type TextField*/
        initMonitoringProperty();

        /*Set Button Click Event*/
        initCmdButton();

        /*Set TextField Key-Pressed*/
        initTextKeyPressed();

        /*Set TextField Focus*/
        initTextFieldFocus();

        /* Set Number Format*/
        initNumberFormatterFields();

        /* Set Table KeyPressed */
        initTableKeyPressed();

        /*Set TextField Required*/
        initAddRequiredField();

//        tblViewLabor.setOnMouseClicked(this::tblLabor_Clicked);
//        tblViewParts.setOnMouseClicked(this::tblParts_Clicked);
//        date04.setOnAction(this::getDate);
//        date04.setDayCellFactory(DateFormatCell);
        pnEditMode = EditMode.UNKNOWN;
        initButton(pnEditMode);
    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private Stage getStage() {
//        return (Stage) textSeek01.getScene().getWindow();
        return null;
    }

    private void initCapitalizationFields() {

    }

    private static void setTextFieldCapital(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (textField.getText() != null) {
                textField.setText(newValue.toUpperCase());
            }
        });
    }

    private void initSetComboBoxtoJobOrderMaster() {

    }

    private void initComboItems() {

    }

    private void initMonitoringProperty() {

    }

    private void initCmdButton() {
        btnAdd.setOnAction(this::cmdButton_Click);
        btnEdit.setOnAction(this::cmdButton_Click);
        btnSave.setOnAction(this::cmdButton_Click);
        btnCancel.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click);
        btnBrowse.setOnAction(this::cmdButton_Click);
        btnPrint.setOnAction(this::cmdButton_Click);
    }

    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
//                if (oTrans.NewRecord()) {
//                    clearFields();
//                    pnEditMode = oTrans.getEditMode();
//                    try {
//                        oTrans.clearActMember();
//                        oTrans.clearActVehicle();
//                        oTrans.clearActTown();
//                    } catch (SQLException ex) {
////                            Logger.getLogger(JobOrderFormController.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                } else {
//                    ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
//                }
                break;
            case "btnEdit":
                break;
            case "btnSave":
                break;
            case "btnCancel":
                if (ShowMessageFX.OkayCancel(getStage(), "Are you sure you want to cancel?", pxeModuleName, null) == true) {
                    clearFields();
//                    switchToTab(tabMain, ImTabPane);// Load fields, clear them, and set edit mode
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnBrowse":
                break;
            case "btnPrint":
                break;
            case "btnClose":
                if (ShowMessageFX.OkayCancel(null, "Close Tab", "Are you sure you want to close this Tab?")) {
                    if (unload != null) {
                        unload.unloadForm(AnchorMain, oApp, pxeModuleName);
                    } else {
                        ShowMessageFX.Warning(null, "Warning", "Please notify the system administrator to configure the null value at the close button.");
                    }
                }
                break;
            case "btnCancelJobOrder":
                break;
        }
        initButton(pnEditMode);

    }

    private void initTextKeyPressed() {
        /* TxtField KeyPressed */
//        txtField05.setOnKeyPressed(this::txtField_KeyPressed);
    }

    private void txtField_KeyPressed(KeyEvent event) {
        String txtFieldID = ((TextField) event.getSource()).getId();
        if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
            switch (txtFieldID) {
                case "textSeek01":
                    break;
            }
            event.consume();
            CommonUtils.SetNextFocus((TextField) event.getSource());
        } else if (event.getCode() == KeyCode.UP) {
            event.consume();
            CommonUtils.SetPreviousFocus((TextField) event.getSource());
        }

    }

    private void initTextFieldFocus() {
//        txtField19.focusedProperty().addListener(txtField_Focus);
    }
    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {

        TextField txtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        if (lsValue == null) {
            return;
        }
        if (!nv) {
            switch (lnIndex) {

            }
        } else {
            txtField.selectAll();
        }
    };

    private void initNumberFormatterFields() {
    }

    private void initTableKeyPressed() {

    }

    private void initAddRequiredField() {

    }

    private void clearFields() {

    }

    private void initButton(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

        btnAdd.setManaged(!lbShow);
        btnEdit.setVisible(false);
        btnEdit.setManaged(false);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);
        btnCancel.setVisible(lbShow);
        btnCancel.setManaged(lbShow);
        btnPrint.setVisible(false);
        btnPrint.setManaged(false);
        btnCancelJobOrder.setManaged(false);
        btnCancelJobOrder.setVisible(false);

    }

    private void loadJobOrderFields() {

    }
}
