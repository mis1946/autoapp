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
 * @author John Dave
 */
public class TownCityMainEntryDialogController implements Initializable,ScreenInterface {

    @FXML
    private Button btnClose;
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnAddTown;
    private GRider oApp;
  
    unloadForm unload = new unloadForm(); //Used in Close Button
    private final String pxeModuleName = "ActivityMemberEntryDialogController"; //Form Title
   
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
