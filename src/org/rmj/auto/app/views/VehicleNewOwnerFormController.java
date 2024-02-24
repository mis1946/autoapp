/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.views;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.auto.clients.base.ClientVehicleInfo;

/**
 * FXML Controller class
 *
 * @author Arsiela
 * Date Created: 06-21-2023
 */
public class VehicleNewOwnerFormController implements Initializable {
    private final String pxeModuleName = "Vehicle Transfer Ownership";
    private GRider oApp;
    private ClientVehicleInfo oTransVehicle;
    
    private String psPrevOwner = "";
    
    @FXML
    private Button btnSave;
    @FXML
    private Button btnClose;
    @FXML
    private ToggleGroup tgTransOpt;
    @FXML
    private RadioButton radioOpt1;
    @FXML
    private RadioButton radioOpt2;
    @FXML
    private RadioButton radioOpt3;
    @FXML
    private TextField txtField35V;
    @FXML
    private TextArea textArea37V;
    @FXML
    private Button btnGenerate;
    @FXML
    private TextField txtField35V_2;
    @FXML
    private TextField txtField35V_3;
    /**
     * Initializes the controller class.
     */
    
    //@Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }
    
    public void setObject(ClientVehicleInfo foObject){
        oTransVehicle = foObject;
    } 
    
    public void setOwnerName(String fsValue){
        psPrevOwner = fsValue;
    }
    
    private Stage getStage() {
        return (Stage) btnSave.getScene().getWindow();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtField35V.setOnKeyPressed(this::txtField_KeyPressed);
        //textArea34V.setOnKeyPressed(this::txtArea_KeyPressed);
        
        setCapsLockBehavior(txtField35V);
        setCapsLockBehavior(textArea37V);
        setCapsLockBehavior(txtField35V_3);
        setCapsLockBehavior(txtField35V_2);
        
        txtField35V.textProperty().addListener((observable, oldValue, newValue) -> { 
            try {
                if (newValue.isEmpty() ){
                        textArea37V.clear();
                        txtField35V_2.clear();
                        oTransVehicle.setMaster(6,"");
                        oTransVehicle.setMaster(35,"");
                        oTransVehicle.setMaster(37,"");
                        btnGenerate.setDisable(true);
                }
            } catch (SQLException ex) {
                Logger.getLogger(VehicleNewOwnerFormController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        radioOpt1.selectedProperty().addListener((observable, oldValue, newValue) -> { 
            if(radioOpt1.isSelected()){
                txtField35V.setDisable(false);
            }
        });
        
        radioOpt2.selectedProperty().addListener((observable, oldValue, newValue) -> { 
            if(radioOpt2.isSelected()){
                txtField35V.setDisable(false);
            }
        });
        
        radioOpt3.selectedProperty().addListener((observable, oldValue, newValue) -> { 
            if(radioOpt3.isSelected()){
                txtField35V.setDisable(false);
            }
        });
        
        txtField35V.setDisable(true);
        //Button SetOnAction using cmdButton_Click() method
        btnGenerate.setOnAction(this::cmdButton_Click);
        btnGenerate.setDisable(true);
        btnClose.setOnAction(this::cmdButton_Click);
        btnSave.setOnAction(this::cmdButton_Click);
        btnSave.setDisable(true);
    }   
    
    private static void setCapsLockBehavior(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (textField.getText() != null) {
                textField.setText(newValue.toUpperCase());
            }
        });
    }

    private static void setCapsLockBehavior(TextArea textArea) {
        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (textArea.getText() != null) {
                textArea.setText(newValue.toUpperCase());
            }
        });
    }
    
    private void cmdButton_Click(ActionEvent event) {
        try {
            String lsButton = ((Button) event.getSource()).getId();
            switch (lsButton) {
                case "btnGenerate":
                    txtField35V_2.setText((String) oTransVehicle.getMaster(35));
                    txtField35V_3.setText(psPrevOwner);
                    btnSave.setDisable(false);
                    break;
                case "btnSave":
                    if (oTransVehicle.SaveRecord()) {
                        ShowMessageFX.Information(getStage(), oTransVehicle.getMessage(), "Client Vehicle Information", null);
                        CommonUtils.closeStage(btnClose);
                    } else {
                        ShowMessageFX.Warning(getStage(), oTransVehicle.getMessage(), "Warning", null);
                    }
                    break;
                case "btnClose":
                    CommonUtils.closeStage(btnClose);
                    break;
                default:
                    ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                    break;
                    
            }
        } catch (SQLException ex) {
            Logger.getLogger(VehicleNewOwnerFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*CLIENT VEHICLE INFORMATION*/
    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(((TextField) event.getSource()).getId().substring(8, 10));
        try {
            switch (event.getCode()) {
                case F3:
                case TAB:
                case ENTER:
                    switch (lnIndex) {
                        case 35: //owner
                            if (oTransVehicle.searchCustomer(txtField35V.getText(), true, true)) {
                                txtField35V.setText((String) oTransVehicle.getMaster(35));
                                textArea37V.setText((String) oTransVehicle.getMaster(37));
                                btnGenerate.setDisable(false);
                            } else {
                                ShowMessageFX.Warning(getStage(), oTransVehicle.getMessage(), "Warning", null);
                                btnGenerate.setDisable(true);
                                txtField35V_2.setText("");
                            }
                            break;
//                        case 36: //co owner
//                            if (oTransVehicle.searchCustomer(txtField36V.getText(), false)) {
//                                loadClientVehicleInfo();
//                            } else {
//                                ShowMessageFX.Warning(getStage(), oTransVehicle.getMessage(), "Warning", null);
//                            }
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
    
}
