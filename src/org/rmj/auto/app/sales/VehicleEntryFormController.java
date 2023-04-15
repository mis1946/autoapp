/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.sales;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
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
import org.rmj.auto.app.views.ScreenInterface;
import org.rmj.auto.app.views.unloadForm;

/**
 * FXML Controller class
 * DATE CREATED 04-03-2023 
 * @author Arsiela
 */
public class VehicleEntryFormController implements Initializable, ScreenInterface {
    private GRider oApp;
    private MasterCallback oListener;
    
    unloadForm unload = new unloadForm(); //Used in Close Button
     private final String pxeModuleName = "Vehicle Entry"; //Form Title
     private int pnEditMode;//Modifying fields
     private int pnRow = -1;
     private int oldPnRow = -1;
     private int lnCtr = 0;
     private int pagecounter;
     
     private String oldTransNo = "";
     private String TransNo = "";
     
     /*populate tables Vehicle Description List*/
     private ObservableList<VehicleEntryTableList> vhcllistdata = FXCollections.observableArrayList();
     private FilteredList<VehicleEntryTableList> filteredData;
     private static final int ROWS_PER_PAGE = 50;
     
     @FXML
     private Pagination pagination;
     @FXML
     private TableView tblVhclEntryList;
     @FXML
     private TableColumn tblindex01;
     @FXML
     private TableColumn tblindex02;
     @FXML
     private TableColumn tblindex03;
     @FXML
     private TableColumn tblindex04;
     @FXML
     private Button btnAdd;
     @FXML
     private Button btnEdit;
     @FXML
     private Button btnSave;
     @FXML
     private Button btnClose;
     @FXML
     private TextField txtField04;
     @FXML
     private TextField txtField06;
     @FXML
     private TextField txtField05;
     @FXML
     private TextField txtField08;
     @FXML
     private TextField textSeek01;
     @FXML
     private AnchorPane AnchorMain;
     
     private Stage getStage(){
          return (Stage) textSeek01.getScene().getWindow();
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
          //Populate table
          loadVhclEntryListTable();
          pagination.setPageFactory(this::createPage); 
          
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
               switch (lsButton){
                    case "btnAdd": //create new Vehicle Description
                         break;
                    case "btnEdit": //modify vehicle description
                         break;
                    case "btnSave": 
                         //Validate before saving
                         //Proceed Saving
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
     //use for creating new page on pagination 
     private Node createPage(int pageIndex) {
          int fromIndex = pageIndex * ROWS_PER_PAGE;
          int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, vhcllistdata.size());
          if(vhcllistdata.size()>0){
             tblVhclEntryList.setItems(FXCollections.observableArrayList(vhcllistdata.subList(fromIndex, toIndex))); 
          }
          return tblVhclEntryList;
          
     }
     
     //storing values on vhcldescdata  
     private void loadVhclEntryListTable(){
//          try {
               /*Populate table*/
               vhcllistdata.clear();
               //if (oTrans.LoadList("")){
//                    String sDescription ;
//                    for (lnCtr = 1; lnCtr <= oTrans.getItemCount(); lnCtr++){
//                         sDescription = oTrans.getDetail(lnCtr,"sModelDsc").toString() 
//                                 + " " + oTrans.getDetail(lnCtr,"sTypeDesc").toString()
//                                 + " " + oTrans.getDetail(lnCtr,"sTransMsn").toString()
//                                 + " " + oTrans.getDetail(lnCtr,"sColorDsc").toString();
//                         vhcldescdata.add(new TableVehicleDescriptionList(
//                         String.valueOf(lnCtr), //ROW
//                         oTrans.getDetail(lnCtr,"sMakeDesc").toString(), //Make
//                         sDescription, //Description
//                         oTrans.getDetail(lnCtr,"nYearModl").toString(), //Year
//                         oTrans.getDetail(lnCtr,"sModelDsc").toString(), //Model
//                         oTrans.getDetail(lnCtr,"sTypeDesc").toString(), //Type
//                         oTrans.getDetail(lnCtr,"sTransMsn").toString(), //Transmission
//                         oTrans.getDetail(lnCtr,"cVhclSize").toString(), //Vehicle Size
//                         oTrans.getDetail(lnCtr,"sColorDsc").toString(), //Color        
//                         oTrans.getDetail(lnCtr,"sDescript").toString(), //Description 
//                         oTrans.getDetail(lnCtr,"sVhclIDxx").toString()  //sVhclIDxx
//                         ));
//                    }
//                    initVhclEntryListTable();
               //}
               //loadTab();
               
//          } catch (SQLException e) {
//               ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
//          }
     }
     
     /*populate Table*/    
     private void initVhclEntryListTable() {
          tblindex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
          tblindex02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
          tblindex03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
          tblindex04.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
          
          tblVhclEntryList.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
               TableHeaderRow header = (TableHeaderRow) tblVhclEntryList.lookup("TableHeaderRow");
               header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
               header.setReordering(false);
               });
          });
          
          filteredData = new FilteredList<>(vhcllistdata, b -> true);
          autoSearch(textSeek01);
          // 3. Wrap the FilteredList in a SortedList. 
          SortedList<VehicleEntryTableList> sortedData = new SortedList<>(filteredData);

          // 4. Bind the SortedList comparator to the TableView comparator.
          // 	  Otherwise, sorting the TableView would have no effect.
          sortedData.comparatorProperty().bind(tblVhclEntryList.comparatorProperty());

          // 5. Add sorted (and filtered) data to the table.
          tblVhclEntryList.setItems(sortedData);
          tblVhclEntryList.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
              TableHeaderRow header = (TableHeaderRow) tblVhclEntryList.lookup("TableHeaderRow");
              header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                  header.setReordering(false);
              });
              header.setDisable(true);
          });
            
     }
     
     private void autoSearch(TextField txtField){
          int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
          boolean fsCode = true;
          txtField.textProperty().addListener((observable, oldValue, newValue) -> {
               filteredData.setPredicate(clients-> {
               // If filter text is empty, display all persons.
               if (newValue == null || newValue.isEmpty()) {
                   return true;
               }
               // Compare order no. and last name of every person with filter text.
               String lowerCaseFilter = newValue.toLowerCase();
               switch (lnIndex){
                       case 1:
                           if(lnIndex == 2){
                               return (clients.getTblindex10().toLowerCase().contains(lowerCaseFilter)); // Does not match.   
                            }else {
                               return (clients.getTblindex10().toLowerCase().contains(lowerCaseFilter)); // Does not match.
                            }   
                       default:
                       return true;            
            }
            });
            
            changeTableView(0, ROWS_PER_PAGE);
        });
        loadTab();
     } 
     
     private void loadTab(){
                int totalPage = (int) (Math.ceil(vhcllistdata.size() * 1.0 / ROWS_PER_PAGE));
                pagination.setPageCount(totalPage);
                pagination.setCurrentPageIndex(0);
                changeTableView(0, ROWS_PER_PAGE);
                pagination.currentPageIndexProperty().addListener(
                        (observable, oldValue, newValue) -> changeTableView(newValue.intValue(), ROWS_PER_PAGE));
      
     } 
     
     private void changeTableView(int index, int limit) {
          int fromIndex = index * limit;
          int toIndex = Math.min(fromIndex + limit, vhcllistdata.size());

          int minIndex = Math.min(toIndex, filteredData.size());
          SortedList<VehicleEntryTableList> sortedData = new SortedList<>(
                  FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
          sortedData.comparatorProperty().bind(tblVhclEntryList.comparatorProperty());
          tblVhclEntryList.setItems(sortedData); 
     }

     @FXML
     private void tblVhclEntryList_Clicked(MouseEvent event) {
          if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
               if(ShowMessageFX.OkayCancel(null, pxeModuleName, "You have unsaved data, are you sure you want to continue?") == true){   
              } else
                  return;
          }
          
          pnRow = tblVhclEntryList.getSelectionModel().getSelectedIndex(); 
          pagecounter = pnRow + pagination.getCurrentPageIndex() * ROWS_PER_PAGE;
          if (pagecounter >= 0){
               if(event.getClickCount() > 0){
                    getSelectedItem(filteredData.get(pagecounter).getTblindex11()); //Populate field based on selected Item

                    tblVhclEntryList.setOnKeyReleased((KeyEvent t)-> {
                        KeyCode key = t.getCode();
                        switch (key){
                            case DOWN:
                                pnRow = tblVhclEntryList.getSelectionModel().getSelectedIndex();
                                pagecounter = pnRow + pagination.getCurrentPageIndex() * ROWS_PER_PAGE;
                                if (pagecounter == tblVhclEntryList.getItems().size()) {
                                    pagecounter = tblVhclEntryList.getItems().size();
                                    getSelectedItem(filteredData.get(pagecounter).getTblindex11());
                                }else {
                                   int y = 1;
                                  pnRow = pnRow + y;
                                    getSelectedItem(filteredData.get(pagecounter).getTblindex11());
                                }
                                break;
                            case UP:
                                pnRow = tblVhclEntryList.getSelectionModel().getSelectedIndex();
                                pagecounter = pnRow + pagination.getCurrentPageIndex() * ROWS_PER_PAGE;
                                getSelectedItem(filteredData.get(pagecounter).getTblindex11());
                                break;
                            default:
                              return; 
                      }
                    });
               } 
               pnEditMode = EditMode.READY;
               initButton(pnEditMode);  
          }  
     }
     
     /*Populate Text Field Based on selected address in table*/
     private void getSelectedItem(String TransNo){
//          oldTransNo = TransNo;
//          if (oTrans.OpenRecord(TransNo)){
//               //txtField02.setText(vhcldescdata.get(pagecounter).getTblindex10()); //Description 
//               txtField03.setText(vhcldescdata.get(pagecounter).getTblindex02()); //Make
//               txtField04.setText(vhcldescdata.get(pagecounter).getTblindex05()); //Model
//               txtField05.setText(vhcldescdata.get(pagecounter).getTblindex09()); //Color
//               txtField06.setText(vhcldescdata.get(pagecounter).getTblindex06()); //Type
//               txtField08.setText(vhcldescdata.get(pagecounter).getTblindex04()); //Year
//
//               switch (vhcldescdata.get(pagecounter).getTblindex07()) { //Transmission
//                    case "AT":
//                         comboBox07.getSelectionModel().select(0);
//                         break;
//                    case "M":
//                         comboBox07.getSelectionModel().select(1);
//                         break;
//                    case "CVT":
//                         comboBox07.getSelectionModel().select(2);
//                         break;
//                    default:
//                         break;
//               }
//
//               comboBox09.getSelectionModel().select(Integer.parseInt(vhcldescdata.get(pagecounter).getTblindex08())); //Vehicle Size
//               oldPnRow = pagecounter;   
//          }
        
     }
     
     private void loadVehicleDescField(){
//          try {
//               txtField03.setText((String) oTrans.getMaster(15));
//               txtField04.setText((String) oTrans.getMaster(16));
//               txtField05.setText((String) oTrans.getMaster(17));
//               txtField06.setText((String) oTrans.getMaster(18));
//               txtField08.setText( oTrans.getMaster(8).toString());
//               
//               switch (oTrans.getMaster(7).toString()) {
//                    case "AT":
//                         comboBox07.getSelectionModel().select(0);
//                         break;
//                    case "M":
//                         comboBox07.getSelectionModel().select(1);
//                         break;
//                    case "CVT":
//                         comboBox07.getSelectionModel().select(2);
//                         break;
//                    default:
//                         break;
//               }
//
//               comboBox09.getSelectionModel().select(Integer.parseInt(oTrans.getMaster(9).toString()));
//    
//               
//          } catch (SQLException e) {
//               ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
//          }
     }
     
     private void txtField_KeyPressed(KeyEvent event){
          TextField txtField = (TextField)event.getSource();
          int lnIndex = Integer.parseInt(((TextField)event.getSource()).getId().substring(8,10));
          
//          try{
//               switch (event.getCode()){
//                    case F3:
//                         switch (lnIndex){ 
//                              case 3: //Make
//                                   if (oTrans.searchVehicleMake(txtField03.getText())){
//                                        loadVehicleDescField();
//                                   } else 
//                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
//                              
//                              break;
//                              case 4: //Model
//                                   if (oTrans.searchVehicleModel(txtField04.getText())){
//                                        loadVehicleDescField();
//                                   } else 
//                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
//                              
//                              break;
//                              
//                              case 5: //Color 
//                                   if (oTrans.searchVehicleColor(txtField05.getText())){
//                                        loadVehicleDescField();
//                                   } else 
//                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
//           
//                              break;
//                              
//                              case 6: //Type 
//                                   if (oTrans.searchVehicleType(txtField06.getText())){
//                                        loadVehicleDescField();
//                                        pnEditMode = oTrans.getEditMode();
//                                   } else 
//                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
//                              break;     
//                         } 
//                    break;
//                    case TAB:
//                    case ENTER:
//                         switch (lnIndex){ 
//                              case 3: //Make
//                                   if (oTrans.searchVehicleMake(txtField03.getText())){
//                                        loadVehicleDescField();
//                                   } else 
//                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
//                              break;
//                              
//                              case 4: //Model
//                                   if (oTrans.searchVehicleModel(txtField04.getText())){
//                                        loadVehicleDescField();
//                                   } else 
//                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
//                              break;
//                              
//                              case 5: //Color 
//                                   if (oTrans.searchVehicleColor(txtField05.getText())){
//                                        loadVehicleDescField();
//                                   } else 
//                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);                            
//                              break;
//                              
//                              case 6: //Type 
//                                   if (oTrans.searchVehicleType(txtField06.getText())){
//                                        loadVehicleDescField();
//                                        pnEditMode = oTrans.getEditMode();
//                                   } else 
//                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
//                              break;       
//                         } 
//                         break;
//               }
//          }catch(SQLException e){
//                ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
//          }
          
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
//          try{
            TextField txtField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
            int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
            String lsValue = txtField.getText();
            
            if (lsValue == null) return;
            if(!nv){ /*Lost Focus*/
                    switch (lnIndex){
//                         case 3: //sMakeIDxx
//                              oTrans.setMaster(15, lsValue); //Handle Encoded Value
//                              break;
//                         case 4: //sModelIDx
//                              oTrans.setMaster(16, lsValue); //Handle Encoded Value
//                              break;
//                         case 5: //sColorIDx
//                              oTrans.setMaster(17, lsValue); //Handle Encoded Value
//                              break;
//                         case 6: //sTypeIDxx
//                              oTrans.setMaster(18, lsValue); //Handle Encoded Value
//                              break;
//                         case 8: //nYearModl
//                              if (lsValue.trim().equals("")){
//                                   oTrans.setMaster(lnIndex,  0); //Handle Encoded Value     
//                              } else {
//                                   oTrans.setMaster(lnIndex,  Integer.parseInt(lsValue)); //Handle Encoded Value
//                              }
//                              break;
                        
                    }
                
            } else
               txtField.selectAll();
//          } catch (SQLException ex) {
//            Logger.getLogger(VehicleEntryFormController.class.getName()).log(Level.SEVERE, null, ex);
//          }
     };
     
     /*Set ComboBox Value to Master Class*/ 
     @SuppressWarnings("ResultOfMethodCallIgnored")
     private boolean setSelection(){
//          try {
//               if (comboBox07.getSelectionModel().getSelectedIndex() < 0){
//                   ShowMessageFX.Warning("No `Transmission` selected.", pxeModuleName, "Please select `Transmission` value.");
//                   comboBox07.requestFocus();
//                   return false;
//               }else 
//                    //oTrans.setMaster(7, String.valueOf(comboBox07.getSelectionModel().getSelectedIndex()));
//                    if (comboBox07.getSelectionModel().getSelectedIndex() == 0){
//                         oTrans.setMaster(7, "AT");
//                    }else if  (comboBox07.getSelectionModel().getSelectedIndex() == 1){
//                         oTrans.setMaster(7, "M");
//                    }else if  (comboBox07.getSelectionModel().getSelectedIndex() == 2){
//                         oTrans.setMaster(7, "CVT");
//                    }
//               
//               if (comboBox09.getSelectionModel().getSelectedIndex() < 0){
//                   ShowMessageFX.Warning("No `Vehicle Size` selected.", pxeModuleName, "Please select `Vehicle Size` value.");
//                   comboBox09.requestFocus();
//                   return false;
//               }else 
//                    //oTrans.setMaster(9, String.valueOf(comboBox09.getSelectionModel().getSelectedIndex()));
//                    oTrans.setMaster(9, comboBox09.getSelectionModel().getSelectedIndex());
//
//          } catch (SQLException ex) {
//          ShowMessageFX.Warning(getStage(),ex.getMessage(), "Warning", null);
//          }

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
          
//          /*Vehicle Description*/
//          txtField03.setDisable(!lbShow); // sMakeIDxx
//          txtField04.setDisable(!lbShow); // sModelIDx
//          txtField06.setDisable(!lbShow); // sColorIDx
//          txtField05.setDisable(!lbShow); // sTypeIDxx
//          txtField08.setDisable(!lbShow); // nYearModl
//          comboBox07.setDisable(!lbShow); //Transmission
//          comboBox09.setDisable(!lbShow); //Vehicle Size
          
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
          
//          txtField02.clear(); // sDescript
//          txtField03.clear(); // sMakeIDxx
//          txtField04.clear(); // sModelIDx
//          txtField06.clear(); // sColorIDx
//          txtField05.clear(); // sTypeIDxx
//          txtField08.clear(); // nYearModl
//          comboBox07.setValue(null); //Transmission
//          comboBox09.setValue(null); //Vehicle Size
     }
     
     
}
