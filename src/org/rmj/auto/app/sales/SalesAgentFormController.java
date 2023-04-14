/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.sales;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.auto.app.views.ScreenInterface;
import org.rmj.auto.app.views.unloadForm;

/**
 * FXML Controller class
 * DATE CREATED: 03-29-2023
 * @author Arsiela
 */
public class SalesAgentFormController implements Initializable, ScreenInterface  {
     private GRider oApp;
     private MasterCallback oListener;
     
     unloadForm unload = new unloadForm(); //Used in Close Button
     private final String pxeModuleName = "Sales Agent"; //Form Title
     private int pnEditMode;//Modifying fields
     private int pnRow = -1;
     private int oldPnRow = -1;
     private int lnCtr = 0;
     private int pagecounter;
     
     private String oldTransNo = "";
     private String TransNo = "";
     @FXML
     private Pagination pagination;
     @FXML
     private TableView<?> tblAgent;
     @FXML
     private TableColumn<?, ?> listIndex01;
     @FXML
     private TableColumn<?, ?> listIndex02;
     @FXML
     private TableColumn<?, ?> listIndex03;
     @FXML
     private Button btnAdd;
     @FXML
     private Button btnEdit;
     @FXML
     private Button btnSave;
     @FXML
     private Button btnClose;
     @FXML
     private TableColumn<?, ?> refIndex01;
     @FXML
     private TableColumn<?, ?> refIndex02;
     @FXML
     private TableColumn<?, ?> refIndex03;
     @FXML
     private TableColumn<?, ?> refIndex04;
     @FXML
     private TableColumn<?, ?> refIndex05;
     @FXML
     private TableColumn<?, ?> refIndex06;
     @FXML
     private TextField txtField01;
     @FXML
     private AnchorPane AnchorMain;
     @FXML
     private TextField textSeek01;

     private Stage getStage(){
          return (Stage) txtField01.getScene().getWindow();
     }
     
     /**
      * Initializes the controller class.
      */
     @Override
     public void initialize(URL url, ResourceBundle rb) {
          oListener = (int fnIndex, Object foValue) -> {
               System.out.println("Set Class Value "  + fnIndex + "-->" + foValue);
          };
          
           //Button Click Event
          btnAdd.setOnAction(this::cmdButton_Click);
          btnEdit.setOnAction(this::cmdButton_Click); 
          btnSave.setOnAction(this::cmdButton_Click); 
          btnClose.setOnAction(this::cmdButton_Click); 
          
     }

     @Override
     public void setGRider(GRider foValue) {
          oApp = foValue;
     }
     
     private void cmdButton_Click(ActionEvent event) {
          String lsButton = ((Button)event.getSource()).getId();
               switch (lsButton){
                    case "btnAdd": //create new Entry
                         break;
                    case "btnEdit": //modify entry
                         
                    case "btnSave": //Proceed Saving
                         
                         break;                        
                    case "btnClose": //close tab
                         if(ShowMessageFX.OkayCancel(null, "Close Tab", "Are you sure you want to close this Tab?") == true){
                               if (unload != null) {
                                    unload.unloadForm(AnchorMain, oApp, pxeModuleName);
                               }else {
                                    ShowMessageFX.Warning(null, "Warning", "Please notify the system administrator to configure the null value at the close button.");    
                               }
                               break;
                         } else
                             return;
               }
               initButton(pnEditMode);  
     }
     

     @FXML
     private void tblAgent_Clicked(MouseEvent event) {
     }
     
     /*Enabling / Disabling Fields*/
     private void initButton(int fnValue){
          pnRow = 0;
          /* NOTE:
               lbShow (FALSE)= invisible
               !lbShow (TRUE)= visible
          */
          boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
          
         
          btnAdd.setVisible(!lbShow);
          btnAdd.setManaged(!lbShow);
          //if lbShow = false hide btn          
          btnEdit.setVisible(false); 
          btnEdit.setManaged(false);
          btnSave.setVisible(lbShow);
          btnSave.setManaged(lbShow);
          
          if (fnValue == EditMode.READY) { //show edit if user clicked save / browse
               btnEdit.setVisible(true); 
               btnEdit.setManaged(true);
          }
     }
     
     /*Clear Fields*/
     public void clearFields(){
          pnRow = 0;
          /*clear tables*/
          
     }

     
}
