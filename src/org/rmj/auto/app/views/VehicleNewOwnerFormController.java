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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleGroup;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.constants.EditMode;

/**
 * FXML Controller class
 *
 * @author Arsiela
 * Date Created: 06-21-2023
 */
public class VehicleNewOwnerFormController implements Initializable {
    private final String pxeModuleName = "Vehicle Transfer Ownership";
    private GRider oApp;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnClose;
    @FXML
    private ToggleGroup tgTransOpt;
    /**
     * Initializes the controller class.
     */
    
    //@Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Button SetOnAction using cmdButton_Click() method
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
    
    
}
