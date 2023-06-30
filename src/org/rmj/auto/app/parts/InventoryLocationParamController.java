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
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.F3;
import static javafx.scene.input.KeyCode.TAB;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.auto.app.views.ScreenInterface;
import org.rmj.auto.parts.parameters.PartsItemLocation;

/**
 * FXML Controller class
 *
 * @author John Dave DATE CREATED 06-26-2023
 */
public class InventoryLocationParamController implements Initializable, ScreenInterface {

    private MasterCallback oListener;
    private final String pxeModuleName = "Inventory Location Entry Form";
    private int pnEditMode;//Modifying fields
    private int pnRow = 0;
    private PartsItemLocation oTrans;
    private GRider oApp;
    @FXML
    private TextField txtField02;
    @FXML
    private TextField txtField01;
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
    private CheckBox cboxActivate;
    @FXML
    private TextField txtField07;
    @FXML
    private TextField txtField09;
    @FXML
    private TextField txtField11;
    @FXML
    private Button btnCreate;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTrans = new PartsItemLocation(oApp, oApp.getBranchCode(), true); //Initialize ClientMaster
        oTrans.setCallback(oListener);
        oTrans.setWithUI(true);
        setCapsLockBehavior(txtField02);
        setCapsLockBehavior(txtField07);
        setCapsLockBehavior(txtField09);
        setCapsLockBehavior(txtField11);

        txtField07.setOnKeyPressed(this::txtField_KeyPressed);
        txtField09.setOnKeyPressed(this::txtField_KeyPressed);
        txtField11.setOnKeyPressed(this::txtField_KeyPressed);
        txtField07.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                txtField02.clear(); // Clear the contents of textField02
                try {
                    oTrans.setMaster(2, "");
                } catch (SQLException ex) {
                    Logger.getLogger(InventoryLocationParamController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        btnCreate.setOnAction(this::cmdButton_Click);
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

    private void loadLocationField() {
        try {
            txtField01.setText((String) oTrans.getMaster(1)); //
            txtField02.setText((String) oTrans.getMaster(2)); //
            txtField07.setText((String) oTrans.getMaster(7)); //
            txtField09.setText((String) oTrans.getMaster(9)); //
            txtField11.setText((String) oTrans.getMaster(11)); //
            if (oTrans.getMaster(3).toString().equals("1")) {
                cboxActivate.setSelected(true);
            } else {
                cboxActivate.setSelected(false);
            }

        } catch (SQLException ex) {
            Logger.getLogger(InventoryLocationParamController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void cmdButton_Click(ActionEvent event) {
        try {
            String lsButton = ((Button) event.getSource()).getId();
            switch (lsButton) {

                case "btnCreate":
                    if (!txtField07.getText().isEmpty()) {
                        txtField02.setText((String) oTrans.getMaster(7) + "-" + (String) oTrans.getMaster(9)
                                + "-" + (String) oTrans.getMaster(11));
                        String lsValue = txtField02.getText();
                        oTrans.setMaster(2, lsValue);
                    } else {
                        ShowMessageFX.Warning(getStage(), "Please input warehouse description first!", "Warning", null);
                        return;
                    }

                    break;
                case "btnAdd": //create
                    if (oTrans.NewRecord()) {
                        clearFields();
                        loadLocationField();
                        pnEditMode = oTrans.getEditMode();
                    } else {
                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                    }
                    break;
                case "btnSave":
                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to save?") == true) {
                        if (txtField07.getText().trim().equals("")) {
                            ShowMessageFX.Warning(getStage(), "Please enter a value for warehouse description", "Warning", null);
                            txtField07.requestFocus();
                            return;
                        }
                        if (txtField09.getText().trim().equals("")) {
                            ShowMessageFX.Warning(getStage(), "Please enter a value for section description", "Warning", null);
                            txtField09.requestFocus();
                            return;
                        }
                        if (txtField11.getText().trim().equals("")) {
                            ShowMessageFX.Warning(getStage(), "Please enter a value for bin description", "Warning", null);
                            txtField11.requestFocus();
                            return;
                        }
                        if (txtField02.getText().trim().equals("")) {
                            ShowMessageFX.Warning(getStage(), "Please enter a value for location description", "Warning", null);
                            txtField02.requestFocus();
                            return;
                        }
                        if (oTrans.SaveRecord()) {
                            ShowMessageFX.Information(null, pxeModuleName, "New location added sucessfully.");
                            if (oTrans.OpenRecord(oTrans.getMaster(1).toString())) {
                                loadLocationField();
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
                                loadLocationField();
                                pnEditMode = oTrans.getEditMode();
                            }
                        } else {
                            ShowMessageFX.Information(getStage(), oTrans.getMessage(), pxeModuleName, null);

                            ShowMessageFX.Information(getStage(), "Failed to update status.", pxeModuleName, null);
                            return;
                        }
                    }
                    break;
                case "btnSearch":
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (ShowMessageFX.OkayCancel(null, "Confirmation", "You have unsaved data. Are you sure you want to browse a new record?")) {
                        } else {
                            return;
                        }
                    }
                    if (oTrans.searchRecord()) {
                        loadLocationField();
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
            Logger.getLogger(InventoryLocationParamController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void clearFields() {
        txtField01.clear(); //
        txtField02.clear(); //
        txtField07.clear(); //
        txtField09.clear(); //
        txtField11.clear(); //
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
        txtField07.setDisable(!lbShow);
        txtField09.setDisable(!lbShow);
        txtField11.setDisable(!lbShow);
        cboxActivate.setDisable(true);
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        btnCancel.setVisible(lbShow);
        btnCancel.setManaged(lbShow);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);
        btnCreate.setDisable(!lbShow);
        btnEdit.setVisible(false);
        btnEdit.setManaged(false);
        btnDeactivate.setVisible(false);
        btnDeactivate.setManaged(false);
        if (fnValue == EditMode.READY) {
            try {
                //show edit if user clicked save / browse
                if (oTrans.getMaster(3).toString().equals("1")) {
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
                Logger.getLogger(InventoryLocationParamController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(((TextField) event.getSource()).getId().substring(8, 10));
        String txtFieldID = ((TextField) event.getSource()).getId();
        String lsValue = txtField.getText();

        try {
            switch (event.getCode()) {
                case F3:
                case TAB:
                case ENTER:
                    switch (txtFieldID) {
                        case "txtField07": //warehouse
                            if (oTrans.searchWarehouse(txtField07.getText())) {
                                loadLocationField();
                            } else {
                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                                txtField07.clear();
                                txtField02.clear();

                            }
                            break;
                        case "txtField09": //section
                            if (oTrans.searchSection(txtField09.getText())) {
                                loadLocationField();
                            } else {
                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                                txtField09.clear();
                            }
                            initButton(pnEditMode);
                            break;
                        case "txtField11": //bin
                            if (oTrans.searchBin(txtField11.getText())) {
                                loadLocationField();
                            } else {
                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                                txtField11.clear();
                            }
                            break;
                        default:
                            break;

                    }
                    break;
            }
        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }

        switch (event.getCode()) {
            case ENTER:
            case DOWN:
                CommonUtils.SetNextFocus(txtField);
                break;
            case UP:
                CommonUtils.SetPreviousFocus(txtField);
        }

    }
}
