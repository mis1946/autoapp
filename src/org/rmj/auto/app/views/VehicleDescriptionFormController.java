/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.views;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.auto.clients.base.VehicleDescription;

/**
 * FXML Controller class
 * DATE CREATED: 03-24-2023
 * @author Arsiela
 */
public class VehicleDescriptionFormController implements Initializable, ScreenInterface  {
     private GRider oApp;
     private VehicleDescription oTrans;
     private MasterCallback oListener;
     
     unloadForm unload = new unloadForm(); //Used in Close Button
     private final String pxeModuleName = "Vehicle Description"; //Form Title
     private int pnEditMode;//Modifying fields
     private int pnRow = -1;
     
     /*populate tables Vehicle Description List*/
     private ObservableList<TableVehicleDescriptionList> vhcldescdata = FXCollections.observableArrayList();
     
     ObservableList<String> cTransmission = FXCollections.observableArrayList("Automatic", "Manual", "CVT");
     ObservableList<String> cModelsize = FXCollections.observableArrayList("Bantam", "Small", "Medium", "Large");
     
     @FXML
     private AnchorPane AnchorMain;
     @FXML
     private Button btnAdd;
     @FXML
     private Button btnEdit;
     @FXML
     private Button btnSave;
     @FXML
     private Button btnClose;
     @FXML
     private TableView tblVehicleDesc;
     @FXML
     private TableColumn tblindex01;
     @FXML
     private TableColumn tblindex02;
     @FXML
     private TableColumn tblindex03;
     @FXML
     private TableColumn tblindex04;
     @FXML
     private TextField txtField02;
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
     private ComboBox comboBox07;
     @FXML
     private ComboBox comboBox09;
     
     private Stage getStage(){
          return (Stage) txtField02.getScene().getWindow();
     }

     /**
      * Initializes the controller class.
      */
     @Override
     public void initialize(URL url, ResourceBundle rb) {
          oListener = (int fnIndex, Object foValue) -> {
               System.out.println("Set Class Value "  + fnIndex + "-->" + foValue);
          };
          
          oTrans = new VehicleDescription(oApp, oApp.getBranchCode(), true); //Initialize ClientMaster
          oTrans.setCallback(oListener);
          oTrans.setWithUI(true);
          initVhclDescTable();
          
          /*Set Focus to set Value to Class*/
          txtField03.focusedProperty().addListener(txtField_Focus); // sMakeIDxx
          txtField04.focusedProperty().addListener(txtField_Focus); // sModelIDx
          txtField06.focusedProperty().addListener(txtField_Focus); // sColorIDx
          txtField05.focusedProperty().addListener(txtField_Focus); // sTypeIDxx
          txtField08.focusedProperty().addListener(txtField_Focus); // nYearModl
          
          /*Add limit and text formatter for year model*/
          CommonUtils.addTextLimiter(txtField08, 4); // nYearModl
          Pattern pattern = Pattern.compile("[0-9]*");
          txtField08.setTextFormatter(new InputTextFormatter(pattern)); //nYearModl
          
          //Populate combo box
          comboBox07.setItems(cTransmission);
          comboBox09.setItems(cModelsize);
          
          //Key Pressed Event
          txtField02.setOnKeyPressed(this::txtField_KeyPressed); // sDescript
          txtField03.setOnKeyPressed(this::txtField_KeyPressed); // sMakeIDxx
          txtField04.setOnKeyPressed(this::txtField_KeyPressed); // sModelIDx
          txtField06.setOnKeyPressed(this::txtField_KeyPressed); // sColorIDx
          txtField05.setOnKeyPressed(this::txtField_KeyPressed); // sTypeIDxx
          txtField08.setOnKeyPressed(this::txtField_KeyPressed); // nYearModl
          
          //Button Click Event
          btnAdd.setOnAction(this::cmdButton_Click);
          btnEdit.setOnAction(this::cmdButton_Click); 
          btnSave.setOnAction(this::cmdButton_Click); 
          btnClose.setOnAction(this::cmdButton_Click); 
          //btnBrowse.setOnAction(this::cmdButton_Click);
          
          /*Clear Fields*/
          clearFields();
          
          pnEditMode = EditMode.UNKNOWN;
          initButton(pnEditMode); 
     } 
     
     @Override
     public void setGRider(GRider foValue) {
          oApp = foValue;
     }
     
     private void cmdButton_Click(ActionEvent event) {
          String lsButton = ((Button)event.getSource()).getId();
          //try {
               switch (lsButton){
//                    case "btnBrowse":
//                         if (oTrans.SearchRecord(txtField02.getText(), true)){
//                                   loadVehicleDescription();
//                                   pnEditMode = EditMode.READY;
//                              } else {
//                                  ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
//                                  pnEditMode = EditMode.UNKNOWN;
//                              }
//                         break;
                    case "btnAdd": //create new Vehicle Description
                         if (oTrans.NewRecord()) {
                              clearFields(); 
                              loadVehicleDescription();
                              pnEditMode = oTrans.getEditMode();
                         } else 
                             ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                          break;
                    case "btnEdit": //modify client info
                         if (oTrans.UpdateRecord()) {
                              pnEditMode = oTrans.getEditMode(); 
                         } else 
                              ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                         break;
                    case "btnSave": 
                         //Proceed Saving
                         if (setSelection()) {
                              if (oTrans.SaveRecord()){
                                   ShowMessageFX.Information(getStage(), "Transaction save successfully.", "Client Information", null);
                                   pnEditMode = oTrans.getEditMode();
                              } else {
                                  ShowMessageFX.Warning(getStage(),oTrans.getMessage() ,"Warning", "Error while saving Client Information");
                              }
                         }
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
//          } catch (SQLException e) {
//               e.printStackTrace();
//               ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
//          }
     }
     
     private void loadVehicleDescription(){
          try {
               txtField02.setText((String) oTrans.getMaster(2));
               txtField03.setText((String) oTrans.getMaster(15));
               txtField04.setText((String) oTrans.getMaster(16));
               txtField05.setText((String) oTrans.getMaster(17));
               txtField06.setText((String) oTrans.getMaster(18));
               txtField08.setText( oTrans.getMaster(8).toString());
               
               //comboBox07.getSelectionModel().select(Integer.parseInt(oTrans.getMaster(7).toString()));
               switch (oTrans.getMaster(7).toString()) {
                    case "AT":
                         comboBox07.getSelectionModel().select(0);
                         break;
                    case "M":
                         comboBox07.getSelectionModel().select(1);
                         break;
                    case "CVT":
                         comboBox07.getSelectionModel().select(2);
                         break;
                    default:
                         break;
               }

               comboBox09.getSelectionModel().select(Integer.parseInt(oTrans.getMaster(9).toString()));
               
          } catch (SQLException e) {
               ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
          }
     }
     
     /*populate Table*/    
     private void initVhclDescTable() {
          tblindex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
          tblindex02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
          tblindex03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
          tblindex04.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
          tblVehicleDesc.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
               TableHeaderRow header = (TableHeaderRow) tblVehicleDesc.lookup("TableHeaderRow");
               header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
               header.setReordering(false);
               });
          });
        vhcldescdata.clear();
        tblVehicleDesc.setItems(vhcldescdata);
          
     }
     
     /*Populate Fields after clicking row in table*/
     @FXML
     private void tblVehicleDesc_Clicked(MouseEvent event) {
          if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
               if(ShowMessageFX.OkayCancel(null, pxeModuleName, "You have unsaved data, are you sure you want to continue?") == true){   
              } else
                  return;
          }
          
          pnRow = tblVehicleDesc.getSelectionModel().getSelectedIndex() + 1;
          if(pnRow == 0) { return;}
          getSelectedItem();
          
          tblVehicleDesc.setOnKeyReleased((KeyEvent t)-> {
                KeyCode key = t.getCode();
                switch (key){
                    case DOWN:
                        pnRow = tblVehicleDesc.getSelectionModel().getSelectedIndex();
                        if (pnRow == tblVehicleDesc.getItems().size()) {
                            pnRow = tblVehicleDesc.getItems().size();
                            getSelectedItem();
                        }else {
                            int y = 1;
                            pnRow = pnRow + y;
                            getSelectedItem();
                        }
                        break;
                        
                    case UP:
                        int pnRows = 0;
                        int x = -1;
                        pnRows = tblVehicleDesc.getSelectionModel().getSelectedIndex() + 1;
                            pnRow = pnRows; 
                            getSelectedItem();
                        break;
                    default:
                        return; 
                }
            });
     }
     
     /*Populate Text Field Based on selected address in table*/
     private void getSelectedItem(){
          
//          txtField03.setText((String) oTrans.getMaster(pnRow,"sMakeDesc" ));
//          txtField04.setText((String) oTrans.getMaster(pnRow, "sModelDsc"));
//          txtField05.setText((String) oTrans.getMaster(pnRow, "sColorDsc"));
//          txtField06.setText((String) oTrans.getMaster(pnRow, "sTypeDesc"));
//          txtField08.setText((String) oTrans.getMaster(pnRow, "nYearModl"));
//          comboBox07.getSelectionModel().select(Integer.parseInt((String) oTrans.getMaster(pnRow,"sTransMsn" )));
//          comboBox09.getSelectionModel().select(Integer.parseInt((String) oTrans.getMaster(pnRow,"cVhclSize" )));
                
     }
     
     private void txtField_KeyPressed(KeyEvent event){
          TextField txtField = (TextField)event.getSource();
          int lnIndex = Integer.parseInt(((TextField)event.getSource()).getId().substring(8,10));
          
          try{
               switch (event.getCode()){
                    case F3:
                         switch (lnIndex){ 
                              case 2:  //Search by Vehicle Description
                               if (oTrans.SearchRecord(txtField02.getText(), true)){
                                        loadVehicleDescription();
                                        pnEditMode = oTrans.getEditMode();
                                   } else {
                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                                       pnEditMode = EditMode.UNKNOWN;
                                   }
                                   initButton(pnEditMode); 
                              break;
                              case 3: //Make
                                   if (oTrans.searchVehicleMake(txtField03.getText())){
                                        loadVehicleDescription();
                                   } else 
                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                              
                              break;
                              case 4: //Model
                                   if (oTrans.searchVehicleModel(txtField04.getText())){
                                        loadVehicleDescription();
                                   } else 
                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                              
                              break;
                              
                              case 5: //Color 
                                   if (oTrans.searchVehicleColor(txtField05.getText())){
                                        loadVehicleDescription();
                                   } else 
                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
           
                              break;
                              
                              case 6: //Type 
                                   if (oTrans.searchVehicleType(txtField06.getText())){
                                        loadVehicleDescription();
                                        pnEditMode = oTrans.getEditMode();
                                   } else 
                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                              break;     
                         } 
                    break;
                    case TAB:
                    case ENTER:
                         switch (lnIndex){ 
                              case 3: //Make
                                   if (oTrans.searchVehicleMake(txtField03.getText())){
                                        loadVehicleDescription();
                                   } else 
                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                              
                              break;
                              case 4: //Model
                                   if (oTrans.searchVehicleModel(txtField04.getText())){
                                        loadVehicleDescription();
                                   } else 
                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                              
                              break;
                              
                              case 5: //Color 
                                   if (oTrans.searchVehicleColor(txtField05.getText())){
                                        loadVehicleDescription();
                                   } else 
                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
//                              
                              break;
                              
                              case 6: //Type 
                                   if (oTrans.searchVehicleType(txtField06.getText())){
                                        loadVehicleDescription();
                                        pnEditMode = oTrans.getEditMode();
                                   } else 
                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                              break;       
                         } 
                         break;
               }
          }catch(SQLException e){
                ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
          }
          
          switch (event.getCode()){
          case ENTER:
          case DOWN:
              CommonUtils.SetNextFocus(txtField);
              break;
          case UP:
              CommonUtils.SetPreviousFocus(txtField);
          }
          
     }
     
     /*Set TextField Value to Master Class*/
     final ChangeListener<? super Boolean> txtField_Focus = (o,ov,nv)->{
          try{
            TextField txtField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
            int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
            String lsValue = txtField.getText();
            
            if (lsValue == null) return;
            if(!nv){ /*Lost Focus*/
                    switch (lnIndex){
                         case 3: //sMakeIDxx
                              oTrans.setMaster(15, lsValue); //Handle Encoded Value
                              break;
                         case 4: //sModelIDx
                              oTrans.setMaster(16, lsValue); //Handle Encoded Value
                              break;
                         case 5: //sColorIDx
                              oTrans.setMaster(17, lsValue); //Handle Encoded Value
                              break;
                         case 6: //sTypeIDxx
                              oTrans.setMaster(18, lsValue); //Handle Encoded Value
                              break;
                         case 8: //nYearModl
                              oTrans.setMaster(lnIndex,  Integer.parseInt(lsValue)); //Handle Encoded Value
                              break;
                        
                    }
                
            } else
               txtField.selectAll();
          } catch (SQLException ex) {
            Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
          }
     };
     
     /*Set ComboBox Value to Master Class*/ 
     @SuppressWarnings("ResultOfMethodCallIgnored")
     private boolean setSelection(){
          try {
               if (comboBox07.getSelectionModel().getSelectedIndex() < 0){
                   ShowMessageFX.Warning("No `Transmission` selected.", pxeModuleName, "Please select `Transmission` value.");
                   comboBox07.requestFocus();
                   return false;
               }else 
                    //oTrans.setMaster(7, String.valueOf(comboBox07.getSelectionModel().getSelectedIndex()));
                    if (comboBox07.getSelectionModel().getSelectedIndex() == 0){
                         oTrans.setMaster(7, "AT");
                    }else if  (comboBox07.getSelectionModel().getSelectedIndex() == 1){
                         oTrans.setMaster(7, "M");
                    }else if  (comboBox07.getSelectionModel().getSelectedIndex() == 2){
                         oTrans.setMaster(7, "CVT");
                    }
               
               if (comboBox09.getSelectionModel().getSelectedIndex() < 0){
                   ShowMessageFX.Warning("No `Vehicle Size` selected.", pxeModuleName, "Please select `Vehicle Size` value.");
                   comboBox09.requestFocus();
                   return false;
               }else 
                    //oTrans.setMaster(9, String.valueOf(comboBox09.getSelectionModel().getSelectedIndex()));
                    oTrans.setMaster(9, comboBox09.getSelectionModel().getSelectedIndex());

          } catch (SQLException ex) {
          ShowMessageFX.Warning(getStage(),ex.getMessage(), "Warning", null);
          }

          return true;
     }
     
     /*Enabling / Disabling Fields*/
     private void initButton(int fnValue){
          pnRow = 0;
          /* NOTE:
               lbShow (FALSE)= invisible
               !lbShow (TRUE)= visible
          */
          boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
          
          /*Vehicle Description*/
          txtField03.setDisable(!lbShow); // sMakeIDxx
          txtField04.setDisable(!lbShow); // sModelIDx
          txtField06.setDisable(!lbShow); // sColorIDx
          txtField05.setDisable(!lbShow); // sTypeIDxx
          txtField08.setDisable(!lbShow); // nYearModl
          comboBox07.setDisable(!lbShow); //Transmission
          comboBox09.setDisable(!lbShow); //Vehicle Size
          
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
          vhcldescdata.clear();
          
          txtField02.clear(); // sDescript
          txtField03.clear(); // sMakeIDxx
          txtField04.clear(); // sModelIDx
          txtField06.clear(); // sColorIDx
          txtField05.clear(); // sTypeIDxx
          txtField08.clear(); // nYearModl
          comboBox07.setValue(null); //Transmission
          comboBox09.setValue(null); //Vehicle Size
     }


}
