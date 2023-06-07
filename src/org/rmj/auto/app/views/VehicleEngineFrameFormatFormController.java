/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.views;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.auto.parameters.VehicleEngineFrame;

/**
 * FXML Controller class
 *
 * @author Arsiela
 * Date Created: 06-05-2023
 */
public class VehicleEngineFrameFormatFormController implements Initializable {
    private GRider oApp;
    private MasterCallback oListener;
    private VehicleEngineFrame oTrans;
    private int pnEditMode;
    private final String pxeModuleName = "Vehicle Engine / Frame";
    private int pnCodeType = 0;
    private String psCodeType;
    private String psMakeID, psMakeDesc;
    private String psModelID, psModelDesc;
    
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
    
    public String setMakeID(String fsValue){
        psMakeID = fsValue;
        return psMakeID;
    }
    
    public String setMakeDesc(String fsValue){
        psMakeDesc = fsValue;
        return psMakeDesc;
    }
    
    public String setModelID(String fsValue){
        psModelID = fsValue;
        return psModelID;
    }
    
    public String setModelDesc(String fsValue){
        psModelDesc = fsValue;
        return psModelDesc;
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
        
        txtField07.setText(psMakeDesc);
        txtField09.setText(psModelDesc);
        
        comboBox10.setItems(cCodeType);
        comboBox10.setOnAction(event -> {
            pnCodeType = comboBox10.getSelectionModel().getSelectedIndex();
            switch(pnCodeType){
                case 0:
                    lblManufacturing.setVisible(true);
                    txtField02_make.setVisible(true);
                    lblCode.setVisible(false);
                    txtField02.setVisible(false);
                    lblLength.setVisible(false);
                    txtField03.setVisible(false);
                    psCodeType = "Manufaturing";
                break;
                case 1:
                    lblManufacturing.setVisible(false);
                    txtField02_make.setVisible(false);
                    lblCode.setVisible(true);
                    txtField02.setVisible(true);
                    lblLength.setVisible(true);
                    txtField03.setVisible(true);
                    lblCode.setText("Frame Code");
                    lblLength.setText("Frame No Length");
                    psCodeType = "Frame";
                break;
                case 2:
                    lblManufacturing.setVisible(false);
                    txtField02_make.setVisible(false);
                    lblCode.setVisible(true);
                    txtField02.setVisible(true);
                    lblLength.setVisible(true);
                    txtField03.setVisible(true);
                    lblCode.setText("Engine Code");
                    lblLength.setText("Engine No Length");
                    psCodeType = "Engine";
                break;
            }
        });
        
        setCapsLockBehavior(txtField02);
        txtField02_make.focusedProperty().addListener(txtField_Focus);  // Make Pattern
        txtField02.focusedProperty().addListener(txtField_Focus);  // Pattern
        
        //Button SetOnAction using cmdButton_Click() method
        btnClose.setOnAction(this::cmdButton_Click);
        btnAdd.setOnAction(this::cmdButton_Click);
        btnEdit.setOnAction(this::cmdButton_Click);
        btnSave.setOnAction(this::cmdButton_Click);

        pnEditMode = EditMode.UNKNOWN;
        initbutton(pnEditMode);
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
                    txtField02.clear();
                    txtField02_make.clear();
                    txtField03.clear();
                    oTrans.setCodeType(pnCodeType);
                    if (oTrans.NewRecord()) {
                        //TODO SET MAKE MODEL ID AND DESC TO CLASS
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
                        if (oTrans.OpenRecord(oTrans.getMaster(2).toString(), pnCodeType)){
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
                    if (oTrans.searchVhclEngineFrame()) {
                        loadEngineFrameField();
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
                        return;
                    }
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
            Logger.getLogger(VehicleColorFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void loadEngineFrameField(){
        try {
            txtField02_make.setText((String) oTrans.getMaster(2)); //Manufacturing
            txtField02.setText((String) oTrans.getMaster(2)); //Frame
            txtField03.setText((String) oTrans.getMaster(3)); //Engine
            comboBox10.getSelectionModel().select((Integer) oTrans.getMaster(10)); //Code Type
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
                        oTrans.setMaster(lnIndex, lsValue); //Handle Encoded Value
                        break;
                }

            } else {
                txtField.selectAll();
            }
        } catch (SQLException ex) {
            Logger.getLogger(VehicleMakeFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    };

    private void initbutton(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        
        comboBox10.setDisable(lbShow);
        txtField02_make.setDisable(!lbShow);
        txtField02.setDisable(!lbShow);
        txtField03.setDisable(!lbShow);
        
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        btnEdit.setVisible(false);
        btnEdit.setManaged(false);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);

        if (fnValue == EditMode.READY) {
            btnEdit.setVisible(true);
            btnEdit.setManaged(true);
        }
    }
    
    
    
}
