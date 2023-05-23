/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.auto.app.views;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;

/**
 * FXML Controller class
 *
 * @author User
 */
public class ActivityVehicleEntryDialogController implements Initializable,ScreenInterface {
    private GRider oApp;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnClose;
    unloadForm unload = new unloadForm(); //Used in Close Button
    private final String pxeModuleName = "ActivityVehicleEntryDialog"; //Form Title
    @FXML
    private AnchorPane AnchorMain;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
        btnClose.setOnAction(this::cmdButton_Click);
    }    
    
    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }   
    
     private void cmdButton_Click(ActionEvent event) {
      
            String lsButton = ((Button)event.getSource()).getId();
            switch (lsButton){
                  case "btnClose":
                    CommonUtils.closeStage(btnClose);
                    break;
            }
     }
}
