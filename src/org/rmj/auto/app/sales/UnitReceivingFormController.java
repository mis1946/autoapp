/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.sales;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.util.ResourceBundle;
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
public class UnitReceivingFormController implements Initializable, ScreenInterface {
     private GRider oApp;
     private MasterCallback oListener;
     
     unloadForm unload = new unloadForm(); //Used in Close Button
     private final String pxeModuleName = "Unit Receiving"; //Form Title
     private int pnEditMode;//Modifying fields
     private int pnRow = -1;
     private int oldPnRow = -1;
     private int lnCtr = 0;
     private int pagecounter;
     
     private String oldTransNo = "";
     private String TransNo = "";
     
     private ObservableList<TableUnitReceivingDetail> unitrdetdata = FXCollections.observableArrayList();
     
     /*populate tables Unit Receiving List List*/
     private ObservableList<TableUnitReceivingList> unitlistdata = FXCollections.observableArrayList();
     private FilteredList<TableUnitReceivingList> filteredData;
     private static final int ROWS_PER_PAGE = 50;
     
     ObservableList<String> cTransmission = FXCollections.observableArrayList("Automatic", "Manual", "CVT");
     ObservableList<String> cModelsize = FXCollections.observableArrayList("Bantam", "Small", "Medium", "Large");

     @FXML
     private AnchorPane AnchorMain;
     @FXML
     private TextField txtField01;
     @FXML
     private Pagination pagination;
     @FXML
     private TableView tblUnitRecList;
     @FXML
     private TableColumn listIndex01; //Row
     @FXML
     private TableColumn listIndex02; //Control No
     @FXML
     private TableColumn listIndex03; //Unit Receiving Date
     @FXML
     private TableColumn listIndex04; //Vehicle Description
     @FXML
     private Button btnAdd;
     @FXML
     private Button btnEdit;
     @FXML
     private Button btnSave;
     @FXML
     private Button btnClose;
     @FXML
     private Button btnCancel;
     
     @FXML
     private TableColumn unitIndex01; //Row
     @FXML
     private TableColumn unitIndex02; //CS No.
     @FXML
     private TableColumn unitIndex03; //Plate No.
     @FXML
     private TableColumn unitIndex04; //Vehicle Description
     @FXML
     private TableColumn unitIndex05; //Frame No.
     @FXML
     private TableColumn unitIndex06; //Engine No.
     @FXML
     private TableView tblUnitRecEntry; //Table Unit Entry
     @FXML
     private Button btnAddRow;
     @FXML
     private Button btnDelRow;

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
          btnCancel.setOnAction(this::cmdButton_Click); 
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
                         break;
                    case "btnSave": //Proceed Saving
                         
                         break; 
                    case "btnCancel": //Proceed Cancel
                         
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
                    case "btnAddRow": //Add Row on table unit details
                         break;
                    case "btnDelRow": //Add Row on table unit details
                         break;
               }
               initButton(pnEditMode);  
     }
     
     //storing values on unitlistdata  
     private void loadUnitDetailTable(){
          
     }
     
     /*populate Table*/    
     private void initUnitDetailTable() {
          tblUnitRecEntry.setEditable(true);
          unitIndex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
          unitIndex02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
          unitIndex03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
          unitIndex04.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
          unitIndex05.setCellValueFactory(new PropertyValueFactory<>("tblindex05"));
          unitIndex06.setCellValueFactory(new PropertyValueFactory<>("tblindex06"));
          
          
          tblUnitRecEntry.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
               TableHeaderRow header = (TableHeaderRow) tblUnitRecEntry.lookup("TableHeaderRow");
               header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
               header.setReordering(false);
               });
          });
          
     }
     
     
     
     
     
     
     
     //use for creating new page on pagination 
     private Node createPage(int pageIndex) {
          int fromIndex = pageIndex * ROWS_PER_PAGE;
          int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, unitlistdata.size());
          if(unitlistdata.size()>0){
             tblUnitRecList.setItems(FXCollections.observableArrayList(unitlistdata.subList(fromIndex, toIndex))); 
          }
          return tblUnitRecList;
          
     }
     
     //storing values on unitlistdata  
     private void loadVehicleDescTable(){
//          try {
//               /*Populate table*/
//               unitlistdata.clear();
//               if (oTrans.LoadList("")){
//                    String sDescription ;
//                    for (lnCtr = 1; lnCtr <= oTrans.getItemCount(); lnCtr++){
//                         sDescription = oTrans.getDetail(lnCtr,"sModelDsc").toString() 
//                                 + " " + oTrans.getDetail(lnCtr,"sTypeDesc").toString()
//                                 + " " + oTrans.getDetail(lnCtr,"sTransMsn").toString()
//                                 + " " + oTrans.getDetail(lnCtr,"sColorDsc").toString();
//                         unitlistdata.add(new TableVehicleDescriptionList(
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
//                    initUnitListTable();
//               }
//               loadTab();
//               
//          } catch (SQLException e) {
//               ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
//          }
     }
     
     /*populate Table*/    
     private void initUnitListTable() {
          listIndex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
          listIndex02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
          listIndex03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
          listIndex04.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
          
          tblUnitRecList.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
               TableHeaderRow header = (TableHeaderRow) tblUnitRecList.lookup("TableHeaderRow");
               header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
               header.setReordering(false);
               });
          });
          
          filteredData = new FilteredList<>(unitlistdata, b -> true);
          //autoSearch(txtField02);
          // 3. Wrap the FilteredList in a SortedList. 
          SortedList<TableUnitReceivingList> sortedData = new SortedList<>(filteredData);

          // 4. Bind the SortedList comparator to the TableView comparator.
          // 	  Otherwise, sorting the TableView would have no effect.
          sortedData.comparatorProperty().bind(tblUnitRecList.comparatorProperty());

          // 5. Add sorted (and filtered) data to the table.
          tblUnitRecList.setItems(sortedData);
          tblUnitRecList.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
              TableHeaderRow header = (TableHeaderRow) tblUnitRecList.lookup("TableHeaderRow");
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
                       case 2:
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
                int totalPage = (int) (Math.ceil(unitlistdata.size() * 1.0 / ROWS_PER_PAGE));
                pagination.setPageCount(totalPage);
                pagination.setCurrentPageIndex(0);
                changeTableView(0, ROWS_PER_PAGE);
                pagination.currentPageIndexProperty().addListener(
                        (observable, oldValue, newValue) -> changeTableView(newValue.intValue(), ROWS_PER_PAGE));
      
     } 
     
     private void changeTableView(int index, int limit) {
          int fromIndex = index * limit;
          int toIndex = Math.min(fromIndex + limit, unitlistdata.size());

          int minIndex = Math.min(toIndex, filteredData.size());
          SortedList<TableUnitReceivingList> sortedData = new SortedList<>(
                  FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
          sortedData.comparatorProperty().bind(tblUnitRecList.comparatorProperty());
          tblUnitRecList.setItems(sortedData); 
     }
     
     @FXML
     private void tblUnit_Cliked(MouseEvent event) {
          if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
               if(ShowMessageFX.OkayCancel(null, pxeModuleName, "You have unsaved data, are you sure you want to continue?") == true){   
              } else
                  return;
          }
          
          pnRow = tblUnitRecList.getSelectionModel().getSelectedIndex(); 
          pagecounter = pnRow + pagination.getCurrentPageIndex() * ROWS_PER_PAGE;
          if (pagecounter >= 0){
               if(event.getClickCount() > 0){
                    getSelectedItem(filteredData.get(pagecounter).getTblindex11()); //Populate field based on selected Item

                    tblUnitRecList.setOnKeyReleased((KeyEvent t)-> {
                        KeyCode key = t.getCode();
                        switch (key){
                            case DOWN:
                                pnRow = tblUnitRecList.getSelectionModel().getSelectedIndex();
                                pagecounter = pnRow + pagination.getCurrentPageIndex() * ROWS_PER_PAGE;
                                if (pagecounter == tblUnitRecList.getItems().size()) {
                                    pagecounter = tblUnitRecList.getItems().size();
                                    getSelectedItem(filteredData.get(pagecounter).getTblindex11());
                                }else {
                                   int y = 1;
                                  pnRow = pnRow + y;
                                    getSelectedItem(filteredData.get(pagecounter).getTblindex11());
                                }
                                break;
                            case UP:
                                pnRow = tblUnitRecList.getSelectionModel().getSelectedIndex();
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
        oldTransNo = TransNo;
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
          btnCancel.setVisible(false); 
          btnCancel.setManaged(false);
          btnSave.setVisible(lbShow);
          btnSave.setManaged(lbShow);
          
          if (fnValue == EditMode.READY) { //show edit if user clicked save / browse
               btnEdit.setVisible(true); 
               btnEdit.setManaged(true);
               btnCancel.setVisible(true); 
               btnCancel.setManaged(true);
          }
     }
     
     /*Clear Fields*/
     public void clearFields(){
          pnRow = 0;
          /*clear tables*/
          
     }

     
     
}
