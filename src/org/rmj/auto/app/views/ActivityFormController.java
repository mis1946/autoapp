/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.auto.app.views;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.auto.clients.base.VehicleDescription;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class ActivityFormController implements Initializable,ScreenInterface {

    private GRider oApp;
    private VehicleDescription oTrans;
    private MasterCallback oListener;
    
    unloadForm unload = new unloadForm(); //Used in Close Button
    private final String pxeModuleName = "Activity"; //Form Title
    private int pnEditMode;//Modifying fields
    private int pnRow;
    
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnSearch;
    @FXML
    private Label lblApprovedBy;
    @FXML
    private Label lblApprovedDate;
    @FXML
    private Button btnActSearch;
    @FXML
    private Button btnActRemove;
    @FXML
    private TableView<?> tblViewActivityMembers;
    @FXML
    private TableView<?> tblViewVehicleModel;
    @FXML
    private Button btnVchlSearch;
    @FXML
    private Button btnVhclRemove;
    @FXML
    private Button btnAddRowTasks;
    @FXML
    private Button btnAddRowBudget;
    @FXML
    private Button btnActivityHistory;
    @FXML
    private Button btnPrint;
    @FXML
    private Button btnClose;
    @FXML
    private TableView<?> tblViewTasks;
    @FXML
    private TableView<?> tblViewBudget;
    @FXML
    private Button btnRemoveTasks;
    @FXML
    private Button btnRemoveBudget;
    @FXML
    private Label lblActivityNo;
    @FXML
    private DatePicker DatepFrom;
    @FXML
    private DatePicker DatepTo;
    @FXML
    private ComboBox aType;
    ObservableList<String> cType = FXCollections.observableArrayList("Event", "Sales Call", "Promo");
    @FXML
    private AnchorPane AnchorMain;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
          initButton(pnEditMode); 
          
          
          btnClose.setOnAction(this::cmdButton_Click);
          aType.setItems(cType);
    
    }    
    @Override
    public void setGRider(GRider foValue) {
          oApp = foValue;
     }
     private void cmdButton_Click(ActionEvent event) {
          String lsButton = ((Button)event.getSource()).getId();
               switch (lsButton){
                    case "btnAdd": //create
//                         if (oTrans.NewRecord()) {
//                              clearFields(); 
//                              loadVehicleDescField();
//                              pnEditMode = oTrans.getEditMode();
//                         } else 
//                             ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                          break;
                    case "btnEdit": //modify 
//                         if (oTrans.UpdateRecord()) {
//                              pnEditMode = oTrans.getEditMode(); 
//                         } else 
//                              ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                         break;
                    case "btnSave": 
                         //Validate before saving
//                         if (txtField03.getText().trim().equals("")) {
//                              ShowMessageFX.Warning(getStage(), "Please enter a value for Make.","Warning", null);
//                              txtField03.requestFocus();
//                              return;
//                         }
//                         if (txtField04.getText().trim().equals("")) {
//                              ShowMessageFX.Warning(getStage(), "Please enter a value for Model.","Warning", null);
//                              txtField04.requestFocus();
//                              return;
//                         }
//                         if (txtField06.getText().trim().equals("")) {
//                              ShowMessageFX.Warning(getStage(), "Please enter a value for Type.","Warning", null);
//                              txtField06.requestFocus();
//                              return;
//                         }
//                         if (txtField05.getText().trim().equals("")) {
//                              ShowMessageFX.Warning(getStage(), "Please enter a value for Color.","Warning", null);
//                              txtField05.requestFocus();
//                              return;
//                         }
//                         
//                         if (txtField08.getText().trim().equals("") || Integer.parseInt(txtField08.getText()) < 1900) {
//                              ShowMessageFX.Warning(getStage(), "Please enter a valid value for Year.","Warning", null);
//                              txtField08.requestFocus();
//                              return;
//                         }
//                         
//                         //Proceed Saving
//                         if (setSelection()) {
//                              if (oTrans.SaveRecord()){
//                                   ShowMessageFX.Information(getStage(), "Transaction save successfully.", pxeModuleName, null);
//                                   loadVehicleDescTable();
//                                   pnEditMode = oTrans.getEditMode();
//                              } else {
//                                  ShowMessageFX.Warning(getStage(),oTrans.getMessage() ,"Warning", "Error while saving Vehicle Description");
//                              }
//                         }
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
        public void clearFields(){
//          pnRow = 0;
     }
}
    
