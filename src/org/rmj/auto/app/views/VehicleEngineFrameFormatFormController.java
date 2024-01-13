/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.views;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.auto.parameters.VehicleEngineFrame;

/**
 * FXML Controller class
 *
 * @author Arsiela Date Created: 06-05-2023
 */
public class VehicleEngineFrameFormatFormController implements Initializable, ScreenInterface {

    private GRider oApp;
    private MasterCallback oListener;
    private VehicleEngineFrame oTrans;
    private int pnEditMode;
    private final String pxeModuleName = "Vehicle Engine / Frame";
    private int pnCodeType;
    private boolean pbState;
    private boolean pbOpenEvent = false;
    private String psCodeType;
    private String psMakeID, psMakeDesc;
    private String psModelID, psModelDesc;
    TextFieldAnimationUtil txtFieldAnimation = new TextFieldAnimationUtil();

    ObservableList<String> cCodeType = FXCollections.observableArrayList("MANUFACTURING", "FRAME", "ENGINE");

    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnSearch;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnClose;
    @FXML
    private Label lblCode;
    @FXML
    private Label lblLength;
    @FXML
    private TextField txtField07;
    @FXML
    private TextField txtField09;
    @FXML
    private TextField txtField02_make;
    @FXML
    private TextField txtField02;
    @FXML
    private TextField txtField03;
    @FXML
    private ComboBox comboBox10;
    @FXML
    private Label lblManufacturing;
    @FXML
    private Label lblModel;
    @FXML
    private TextField txtField02_frame;
    @FXML
    private Label label01;
    @FXML
    private Label label02;
    @FXML
    private Label label03;
    @FXML
    private Label label05;
    @FXML
    private Label label04;
    @FXML
    private Label label06;

    public String setMakeID(String fsValue) {
        psMakeID = fsValue;
        return psMakeID;
    }

    public String setMakeDesc(String fsValue) {
        psMakeDesc = fsValue;
        return psMakeDesc;
    }

    public String setModelID(String fsValue) {
        psModelID = fsValue;
        return psModelID;
    }

    public String setModelDesc(String fsValue) {
        psModelDesc = fsValue;
        return psModelDesc;
    }

    public Integer setCodeType(Integer fnValue) {
        pnCodeType = fnValue;
        return pnCodeType;
    }

    public Boolean setState(Boolean fbValue) {
        pbState = fbValue;
        return pbState;
    }

    public Boolean setOpenEvent(Boolean fbValue) {
        pbOpenEvent = fbValue;
        return pbOpenEvent;
    }

    private Stage getStage() {
        return (Stage) btnSave.getScene().getWindow();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oListener = (int fnIndex, Object foValue) -> {
            System.out.println("Set Class Value " + fnIndex + "-->" + foValue);
        };

        oTrans = new VehicleEngineFrame(oApp, oApp.getBranchCode(), true); //Initialize ClientMaster
        oTrans.setCallback(oListener);
        oTrans.setWithUI(true);

        if (pbOpenEvent) {
            txtField07.setText(psMakeDesc);
            txtField09.setText(psModelDesc);
        }
        comboBox10.setItems(cCodeType);
        comboBox10.setOnAction(event -> {
            txtField02_make.clear();
            txtField02_frame.clear();
            txtField02.clear();
            txtField03.clear();
            pnEditMode = EditMode.UNKNOWN;
            if (!pbOpenEvent) {
                txtField07.setText("");
                txtField09.setText("");
            }
            initbutton(pnEditMode);
            pnCodeType = comboBox10.getSelectionModel().getSelectedIndex();
            showFields(pnCodeType);
        });

        if (pbState) {
            comboBox10.getSelectionModel().select(pnCodeType); //Code Type
            showFields(pnCodeType);
        }
        Pattern pattern;
        pattern = Pattern.compile("[0-9]*");
        txtField03.setTextFormatter(new InputTextFormatter(pattern)); //code length No
        setCapsLockBehavior(txtField02_make);
        setCapsLockBehavior(txtField02_frame);
        setCapsLockBehavior(txtField02);
        setCapsLockBehavior(txtField07);
        setCapsLockBehavior(txtField09);

        txtFieldAnimation.addRequiredFieldListener(txtField07);
        txtFieldAnimation.addRequiredFieldListener(txtField09);
        txtFieldAnimation.addRequiredFieldListener(txtField02_make);
        txtFieldAnimation.addRequiredFieldListener(txtField02_frame);
        txtFieldAnimation.addRequiredFieldListener(txtField03);

        CommonUtils.addTextLimiter(txtField02_make, 3); //ManuFacturing
        CommonUtils.addTextLimiter(txtField02_frame, 2); //Frame
        CommonUtils.addTextLimiter(txtField02, 3); //Engine

        txtField02_make.focusedProperty().addListener(txtField_Focus);  // ManuFacturing Pattern
        txtField02_frame.focusedProperty().addListener(txtField_Focus);  // Frame Pattern
        txtField02.focusedProperty().addListener(txtField_Focus);  // Engine Pattern
        txtField03.focusedProperty().addListener(txtField_Focus);  // Length

        txtField07.setOnKeyPressed(this::txtField_KeyPressed);
        txtField09.setOnKeyPressed(this::txtField_KeyPressed);

        //Button SetOnAction using cmdButton_Click() method
        btnClose.setOnAction(this::cmdButton_Click);
        btnSearch.setOnAction(this::cmdButton_Click);
        btnAdd.setOnAction(this::cmdButton_Click);
        btnEdit.setOnAction(this::cmdButton_Click);
        btnCancel.setOnAction(this::cmdButton_Click);
        btnSave.setOnAction(this::cmdButton_Click);
        pnEditMode = EditMode.UNKNOWN;
        initbutton(pnEditMode);
    }

    private void showFields(Integer fnValue) {
        switch (fnValue) {
            case 0:
                psCodeType = "Manufaturing";

                //Make
                //Code Type
                lblModel.setVisible(false); //Model
                txtField09.setVisible(false); //Model
                lblManufacturing.setVisible(true); //Manufacturing
                txtField02_make.setVisible(true); //Make Manufacturing
                lblCode.setVisible(false); //Code
                txtField02_frame.setVisible(false); //Code Frame
                txtField02.setVisible(false); //Code Engine
                lblLength.setVisible(false); //Length
                txtField03.setVisible(false); //Length

                label01.setVisible(true); //Make
                label02.setVisible(false); //Model
                label03.setVisible(true); //Code Type
                label04.setVisible(true); //Manufacturing
                label05.setVisible(false); //code
                label06.setVisible(false); //Length
                break;
            case 1:
                lblManufacturing.setVisible(false);
                txtField02_make.setVisible(false);
                lblCode.setVisible(true);
                txtField02.setVisible(false);
                txtField02_frame.setVisible(true);
                lblLength.setVisible(true);
                txtField03.setVisible(true);
                lblModel.setVisible(true);
                txtField09.setVisible(true);
                lblCode.setText("Frame Code");
                lblLength.setText("Frame No Length");
                psCodeType = "Frame";

                label01.setVisible(true); //Make
                label02.setVisible(true); //Model
                label03.setVisible(true); //Code Type
                label04.setVisible(false); //Manufacturing
                label05.setVisible(true); //code
                label06.setVisible(true); //Length

                break;
            case 2:
                lblManufacturing.setVisible(false);
                txtField02_make.setVisible(false);
                lblCode.setVisible(true);
                txtField02.setVisible(true);
                txtField02_frame.setVisible(false);
                lblLength.setVisible(true);
                txtField03.setVisible(true);
                lblModel.setVisible(true);
                txtField09.setVisible(true);
                lblCode.setText("Engine Code");
                lblLength.setText("Engine No Length");
                psCodeType = "Engine";

                label01.setVisible(true); //Make
                label02.setVisible(true); //Model
                label03.setVisible(true); //Code Type
                label04.setVisible(false); //Manufacturing
                label05.setVisible(true); //code
                label06.setVisible(true); //Length
                break;
        }
    }

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

    private void cmdButton_Click(ActionEvent event) {
        try {
            String lsButton = ((Button) event.getSource()).getId();
            switch (lsButton) {
                case "btnAdd":
                    if (comboBox10.getValue() == null) {
                        ShowMessageFX.Warning(null, pxeModuleName, "Please select code type.");
                        comboBox10.requestFocus();
                        return;
                    }

                    txtField02.clear();
                    txtField02_make.clear();
                    txtField02_frame.clear();
                    txtField03.clear();
                    oTrans.setCodeType(pnCodeType);
                    if (oTrans.NewRecord()) {
                        if (pbOpenEvent) {
                            oTrans.setMaster(6, psMakeID);
                            oTrans.setMaster(7, psMakeDesc);
                            oTrans.setMaster(8, psModelID);
                            oTrans.setMaster(9, psModelDesc);
                        }
                        pnEditMode = oTrans.getEditMode();
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
                        return;
                    }
                    break;
                case "btnEdit":
                    oTrans.setCodeType(pnCodeType);
                    if (oTrans.UpdateRecord()) {
                        pnEditMode = oTrans.getEditMode();
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
                        return;
                    }
                    break;
                case "btnSave":
                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to save?")) {
                    } else {
                        return;
                    }
                    oTrans.setCodeType(pnCodeType);
                    if (oTrans.SaveRecord()) {
                        ShowMessageFX.Information(null, pxeModuleName, psCodeType + " saved sucessfully.");
                        if (oTrans.OpenRecord(oTrans.getMaster(2).toString(), pnCodeType)) {
                            loadEngineFrameField();
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
                            return;
                        }
                        pnEditMode = oTrans.getEditMode();
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
                        return;
                    }
                    break;
                case "btnSearch":
                    if (comboBox10.getValue() == null) {
                        ShowMessageFX.Warning(null, pxeModuleName, "Please select code type.");
                        comboBox10.requestFocus();
                        return;
                    }
                    if (oTrans.searchVhclEngineFrame(pnCodeType)) {
                        //if (oTrans.searchVhclEngineFrame()) {
                        loadEngineFrameField();
                        pnEditMode = oTrans.getEditMode();
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
                        return;
                    }
                    break;
                case "btnCancel":
                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to cancel?")) {
                    } else {
                        return;
                    }
                    clearFields();
                    lblManufacturing.setVisible(true);
                    txtField02_make.setVisible(true);
                    lblCode.setVisible(true);
                    txtField02.setVisible(true);
                    txtField02_frame.setVisible(false);
                    lblLength.setVisible(true);
                    txtField03.setVisible(true);
                    lblModel.setVisible(true);
                    txtField09.setVisible(true);
                    pnEditMode = EditMode.UNKNOWN;
                    break;
                case "btnClose":
                    CommonUtils.closeStage(btnClose);
                    break;
                default:
                    ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                    break;

            }

            initbutton(pnEditMode);
        } catch (SQLException ex) {
            Logger.getLogger(VehicleEngineFrameFormatFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*CLIENT VEHICLE INFORMATION*/
    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(((TextField) event.getSource()).getId().substring(8, 10));
        String txtFieldID = ((TextField) event.getSource()).getId();
        try {
            switch (event.getCode()) {
                case F3:
                case TAB:
                case ENTER:
                    switch (lnIndex) {
                        case 7: //MAKE
                            if (oTrans.searchVehicleMake(txtField07.getText())) {
                            } else {
                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                                txtField07.setText("");
                            }
                            loadEngineFrameField();
                            break;
                        case 9: //MODEL
                            if (oTrans.getMaster(7).toString().isEmpty()) {
                                ShowMessageFX.Warning(getStage(), "Please select Vehicle Make.", "Warning", null);
                                oTrans.setMaster(9, "");
                                txtField07.requestFocus();
                                return;
                            }
                            if (oTrans.searchVehicleModel(txtField09.getText())) {
                            } else {
                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                                txtField09.setText("");
                            }
                            loadEngineFrameField();
                            break;
                    }
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

    private void loadEngineFrameField() {
        try {

            comboBox10.getSelectionModel().select(Integer.parseInt((String) oTrans.getMaster(10))); //Code Type
            txtField07.setText((String) oTrans.getMaster(7));
            if (!((String) oTrans.getMaster(10)).equals("0")) {
                txtField02_frame.setText((String) oTrans.getMaster(2)); //Frame / Engine Pattern
                txtField02.setText((String) oTrans.getMaster(2)); //Frame / Engine Pattern
                txtField03.setText(String.valueOf((Integer) oTrans.getMaster(3))); //Frame / Engine Pattern
                txtField09.setText((String) oTrans.getMaster(9));
            } else {
                txtField02_make.setText((String) oTrans.getMaster(2)); //Manufacturing
            }
        } catch (SQLException ex) {
            Logger.getLogger(VehicleEngineFrameFormatFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*Set TextField Value to Master Class*/
    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {
        try {
            TextField txtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
            int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
            String lsValue = txtField.getText();

            if (lsValue == null) {
                return;
            }
            if (!nv) {
                /*Lost Focus*/
                switch (lnIndex) {
                    case 2:
                    case 7:
                    case 9:
                        oTrans.setMaster(lnIndex, lsValue); //Handle Encoded Value
                        break;
                    case 3:
                        oTrans.setMaster(lnIndex, Integer.parseInt(lsValue)); //Handle Encoded Value
                        break;
                }

            } else {
                txtField.selectAll();
            }
        } catch (SQLException ex) {
            Logger.getLogger(VehicleEngineFrameFormatFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    };

    private void initbutton(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        if (pbOpenEvent) {
            txtField07.setDisable(true);
            txtField09.setDisable(true);
        } else {
            txtField07.setDisable(!(fnValue == EditMode.ADDNEW));
            txtField09.setDisable(!(fnValue == EditMode.ADDNEW));
        }
        comboBox10.setDisable(lbShow);
        txtField02_make.setDisable(!lbShow);
        txtField02_frame.setDisable(!lbShow);
        txtField02.setDisable(!lbShow);
        txtField03.setDisable(!lbShow);

        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        btnEdit.setVisible(false);
        btnEdit.setManaged(false);
        btnCancel.setVisible(lbShow);
        btnCancel.setManaged(lbShow);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);

        if (fnValue == EditMode.READY) {
            btnEdit.setVisible(true);
            btnEdit.setManaged(true);
        }
    }

    private void clearFields() {

        comboBox10.setValue(null);
        txtField02_make.clear();
        txtField02_frame.clear();
        txtField02.clear();
        txtField03.clear();

    }

    private void removeRequired() {
        txtFieldAnimation.removeShakeAnimation(txtField07, txtFieldAnimation.shakeTextField(txtField07), "required-field");
        txtFieldAnimation.removeShakeAnimation(txtField09, txtFieldAnimation.shakeTextField(txtField09), "required-field");
        txtFieldAnimation.removeShakeAnimation(txtField02_make, txtFieldAnimation.shakeTextField(txtField02_make), "required-field");
        txtFieldAnimation.removeShakeAnimation(txtField02_frame, txtFieldAnimation.shakeTextField(txtField02_frame), "required-field");
        txtFieldAnimation.removeShakeAnimation(txtField03, txtFieldAnimation.shakeTextField(txtField03), "required-field");

    }
}
