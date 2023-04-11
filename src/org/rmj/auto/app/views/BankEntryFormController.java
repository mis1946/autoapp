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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.ShowMessageFX;

/**
 * FXML Controller class
 *
 * @author User
 */
public class BankEntryFormController implements Initializable, ScreenInterface{
    private GRider oApp;
    unloadForm unload = new unloadForm(); //Object for closing form
    private final String pxeModuleName = "Bank Entry"; //Form Title
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private TextField txtField02;
    @FXML
    private Pagination pagination;
    @FXML
    private TableView<?> tblVehicleDesc;
    @FXML
    private TableColumn<?, ?> tblindex01;
    @FXML
    private TableColumn<?, ?> tblindex02;
    @FXML
    private TableColumn<?, ?> tblindex03;
    @FXML
    private TableColumn<?, ?> tblindex04;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnClose;
    @FXML
    private TextField txtField03;
    @FXML
    private TextField txtField04;
    @FXML
    private TextField txtField06;
    @FXML
    private TextField txtField05;
    @FXML
    private TextField txtField08;
    @FXML
    private ComboBox<?> comboBox07;
    @FXML
    private ComboBox<?> comboBox09;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        btnClose.setOnAction(this::cmdButton_Click);
    }    

    @FXML
    private void tblVehicleDesc_Clicked(MouseEvent event) {
    }
    
      
  private void cmdButton_Click(ActionEvent event) {
          String lsButton = ((Button)event.getSource()).getId();
          switch(lsButton){
            case "btnAdd":
                break;
            case "btnEdit":
                break;
            case "btnSave":
                break;
            case "btnClose": //close tab
                         if(ShowMessageFX.OkayCancel(null, "Close Tab", "Are you sure, do you want to close tab?") == true){
                               if (unload != null) {
                                    unload.unloadForm(AnchorMain, oApp, pxeModuleName);
                               }else {
                                    ShowMessageFX.Warning(null, "Warning", "Notify System Admin to Configure Null value at close button.");    
                               }
                               break;
                               }else
                           return;
            }
        }
  
    @Override
    public void setGRider(GRider foValue) {
       
    }
    
}
