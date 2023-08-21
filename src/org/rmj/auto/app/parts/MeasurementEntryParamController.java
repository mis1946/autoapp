/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.auto.app.parts;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.auto.app.views.ScreenInterface;
import org.rmj.auto.parts.parameters.PartsMeasure;

/**
 * FXML Controller class
 *
 * @author John Dave DATE CREATED 06-26-2023
 */
public class MeasurementEntryParamController implements Initializable, ScreenInterface {

    private MasterCallback oListener;
    private final String pxeModuleName = "Measurement Entry Form";
    private int pnEditMode;//Modifying fields
    private PartsMeasure oTrans;
    private GRider oApp;
    @FXML
    private Button btnClose;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnSearch;
    @FXML
    private Button btnDeactivate;
    @FXML
    private TextField txtField02;
    @FXML
    private TextField txtField01;
    @FXML
    private CheckBox cboxActivate;
    @FXML
    private TextField txtField03;
    private int pnRow = 0;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTrans = new PartsMeasure(oApp, oApp.getBranchCode(), true);
        oTrans.setCallback(oListener);
        oTrans.setWithUI(true);

        setCapsLockBehavior(txtField02);
        setCapsLockBehavior(txtField03);
        txtField02.focusedProperty().addListener(txtField_Focus);
        txtField03.focusedProperty().addListener(txtField_Focus);
        CommonUtils.addTextLimiter(txtField03, 5); //Measure Abbrev
        btnClose.setOnAction(this::cmdButton_Click);
        btnAdd.setOnAction(this::cmdButton_Click);
        btnSearch.setOnAction(this::cmdButton_Click);
        btnDeactivate.setOnAction(this::cmdButton_Click);
        btnCancel.setOnAction(this::cmdButton_Click);
        btnSave.setOnAction(this::cmdButton_Click);
        btnEdit.setOnAction(this::cmdButton_Click);
        pnEditMode = EditMode.UNKNOWN;
        initButton(pnEditMode);
    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private static void setCapsLockBehavior(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (textField.getText() != null) {
                textField.setText(newValue.toUpperCase());
            }
        });
    }

    private void loadMeasureField() {
        try {
            txtField01.setText((String) oTrans.getMaster(1));
            txtField02.setText((String) oTrans.getMaster(2));
            txtField03.setText((String) oTrans.getMaster(3));
            if (oTrans.getMaster(4).toString().equals("1")) {
                cboxActivate.setSelected(true);
            } else {
                cboxActivate.setSelected(false);
            }

        } catch (SQLException ex) {
            Logger.getLogger(MeasurementEntryParamController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void cmdButton_Click(ActionEvent event) {
        try {
            String lsButton = ((Button) event.getSource()).getId();
            switch (lsButton) {
                case "btnAdd": //create
                    if (oTrans.NewRecord()) {
                        clearFields();
                        loadMeasureField();
                        pnEditMode = oTrans.getEditMode();
                    } else {
                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                    }
                    break;
                case "btnSave":
                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to save?") == true) {
                        if (txtField02.getText().trim().equals("")) {
                            ShowMessageFX.Warning(getStage(), "Please enter a value for measure description", "Warning", null);
                            txtField02.requestFocus();
                            return;
                        }
                        if (txtField03.getText().trim().equals("")) {
                            ShowMessageFX.Warning(getStage(), "Please enter a value for measure abbrev", "Warning", null);
                            txtField03.requestFocus();
                            return;
                        }
                        if (oTrans.SaveRecord()) {
                            if (pnEditMode == EditMode.ADDNEW) {
                                ShowMessageFX.Information(null, pxeModuleName, "New Measurment added sucessfully.");
                            } else {
                                ShowMessageFX.Information(null, pxeModuleName, "Measurment updated sucessfully.");
                            }
                            if (oTrans.OpenRecord(oTrans.getMaster(1).toString())) {
                                loadMeasureField();
                                pnEditMode = oTrans.getEditMode();
                            }
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
                            return;
                        }
                    }
                    break;
                case "btnEdit":
                    if (oTrans.UpdateRecord()) {
                        pnEditMode = oTrans.getEditMode();
                    } else {
                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                    }
                    break;

                case "btnCancel":
                    if (ShowMessageFX.OkayCancel(getStage(), "Are you sure you want to cancel?", pxeModuleName, null) == true) {
                        clearFields();
                        pnEditMode = EditMode.UNKNOWN;
                    }
                    break;
                case "btnDeactivate":
                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                        String fsValue = oTrans.getMaster(1).toString();
                        boolean fbStatus = false;
                        if (btnDeactivate.getText().equals("Activate")) {
                            fbStatus = true;
                        } else {
                            fbStatus = false;
                        }
                        if (oTrans.UpdateRecordStatus(fsValue, fbStatus)) {
                            ShowMessageFX.Information(getStage(), oTrans.getMessage(), pxeModuleName, null);
                            if (oTrans.OpenRecord(oTrans.getMaster(1).toString())) {
                                loadMeasureField();
                                pnEditMode = oTrans.getEditMode();
                            }
                        } else {
                            ShowMessageFX.Information(getStage(), oTrans.getMessage(), pxeModuleName, null);
                            return;
                        }
                    }
                    break;

                case "btnSearch":
                    if (oTrans.searchRecord()) {
                        loadMeasureField();
                        pnEditMode = EditMode.READY;
                    }
                    break;
                case "btnClose":
                    CommonUtils.closeStage(btnClose);
                    break;
                default:
                    ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                    break;
            }
            initButton(pnEditMode);
        } catch (SQLException ex) {
            Logger.getLogger(MeasurementEntryParamController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {
        try {
            TextField txtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
            int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
            String lsValue = txtField.getText().toUpperCase();

            if (lsValue == null) {
                return;
            }
            if (!nv) {
                /* Lost Focus */
                switch (lnIndex) {
                    case 2:
                    case 3:
                        oTrans.setMaster(lnIndex, lsValue);
                        break;
                }
            } else {
                txtField.selectAll();

            }
        } catch (SQLException ex) {
            Logger.getLogger(MeasurementEntryParamController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    };

    private void clearFields() {
        txtField01.clear();
        txtField02.clear();
        txtField03.clear();
        cboxActivate.setSelected(false);
    }

    private Stage getStage() {
        return (Stage) txtField02.getScene().getWindow();
    }

    private void initButton(int fnValue) {
        pnRow = 0;
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        txtField01.setDisable(true);
        txtField02.setDisable(!lbShow);
        txtField03.setDisable(!lbShow);
        cboxActivate.setDisable(true);
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        btnCancel.setVisible(lbShow);
        btnCancel.setManaged(lbShow);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);
        btnEdit.setVisible(false);
        btnEdit.setManaged(false);
        btnDeactivate.setVisible(false);
        btnDeactivate.setManaged(false);
        if (fnValue == EditMode.READY) {
            try {
                //show edit if user clicked save / browse
                if (oTrans.getMaster(4).toString().equals("1")) {
                    btnDeactivate.setText("Deactivate");
                    btnDeactivate.setVisible(true);
                    btnDeactivate.setManaged(true);
                    btnEdit.setVisible(true);
                    btnEdit.setManaged(true);
                } else {
                    btnDeactivate.setText("Activate");
                    btnDeactivate.setVisible(true);
                    btnDeactivate.setManaged(true);
                }
            } catch (SQLException ex) {
                Logger.getLogger(BinEntryParamController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}
