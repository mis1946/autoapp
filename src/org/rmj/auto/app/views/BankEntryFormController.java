/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.auto.app.views;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
import org.rmj.auto.clients.base.BankInformation;

/**
 * FXML Controller class
 *
 * @author john dave
 */
public class BankEntryFormController implements Initializable, ScreenInterface{
    private GRider oApp;
    private BankInformation oTrans;
    unloadForm unload = new unloadForm(); //Object for closing form
    private final String pxeModuleName = "Bank Entry"; //Form Title
    private MasterCallback oListener;
    
    
    private int pnEditMode;//Modifying fields
    private int pnRow = -1;
    private int oldPnRow = -1;
    private int lnCtr = 0;
    private int pagecounter;
    
    private FilteredList<BankEntryTableList> filteredData;
    private static final int ROWS_PER_PAGE = 50;
    
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Pagination pagination;
    @FXML
    private TableView<?> tblBankEntry;
    @FXML
    private TableColumn<?, ?> tblindex01;
    @FXML
    private TableColumn<?, ?> tblindex02;
    @FXML
    private TableColumn<?, ?> tblindex03;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnClose;
    @FXML
    private TextField txtField02; //BankName
    @FXML
    private TextField txtField03; //BankAdd
    @FXML
    private TextField txtField04; //ContactPerson
    @FXML
    private TextField txtField06; 
    @FXML
    private TextField txtField08; //Telephone No4
    @FXML
    private TextField txtSecField02;
    private TextField txtField16; //Barangay Name
    @FXML
    private TextField txtField17; //Town Name
    @FXML
    private TextField txtField15; //Province Name
    @FXML
    private TextField txtField07; //Zip Code
    @FXML
    private TextField txtField09; //Fax No
    @FXML
    private TextField txtField05;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
          
        oListener = (int fnIndex, Object foValue) -> {
               System.out.println("Set Class Value "  + fnIndex + "-->" + foValue);
          };
        
          oTrans = new BankInformation(oApp, oApp.getBranchCode(), true); //Initialize ClientMaster
          oTrans.setCallback(oListener);
          oTrans.setWithUI(true);


          /*Set Focus to set Value to Class*/
          txtField02.focusedProperty().addListener(txtField_Focus); // sBankName
          txtField03.focusedProperty().addListener(txtField_Focus); // sBankCode
          txtField06.focusedProperty().addListener(txtField_Focus); // sBranchxx no database
          txtField05.focusedProperty().addListener(txtField_Focus); // sAddressx
          txtField16.focusedProperty().addListener(txtField_Focus); // sTownNamexx
          txtField15.focusedProperty().addListener(txtField_Focus); // sProvName
          txtField07.focusedProperty().addListener(txtField_Focus); // sZipCode
          txtField04.focusedProperty().addListener(txtField_Focus); // sContactP
          txtField08.focusedProperty().addListener(txtField_Focus); // sTelNoxxx
          txtField09.focusedProperty().addListener(txtField_Focus); // sFaxNoxx
    
//          txtField02.setOnKeyPressed(this::txtField_KeyPressed); // sBankNamexx
//          txtField03.setOnKeyPressed(this::txtField_KeyPressed); // sBrgyNamexx
//          txtField04.setOnKeyPressed(this::txtField_KeyPressed); // sTownNamexx
//          txtField04.setOnKeyPressed(this::txtField_KeyPressed); // sProvNamexx
          
            //Button Click Event
            btnAdd.setOnAction(this::cmdButton_Click);
            btnEdit.setOnAction(this::cmdButton_Click); 
            btnSave.setOnAction(this::cmdButton_Click); 
            btnClose.setOnAction(this::cmdButton_Click); 
            
            /*Clear Fields*/
            clearFields();

            pnEditMode = EditMode.UNKNOWN;
            initButton(pnEditMode); 
     
    }    

//     @FXML
//     private void tblBankEntry_Clicked(MouseEvent event) {
//          if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
//               if(ShowMessageFX.OkayCancel(null, pxeModuleName, "You have unsaved data, are you sure you want to continue?") == true){   
//              } else
//                  return;
//          }
//          
//          pnRow = tblVehicleDesc.getSelectionModel().getSelectedIndex(); 
//          pagecounter = pnRow + pagination.getCurrentPageIndex() * ROWS_PER_PAGE;
//          if (pagecounter >= 0){
//               if(event.getClickCount() > 0){
//                    getSelectedItem(filteredData.get(pagecounter).getTblindex11()); //Populate field based on selected Item
//
//                    tblVehicleDesc.setOnKeyReleased((KeyEvent t)-> {
//                        KeyCode key = t.getCode();
//                        switch (key){
//                            case DOWN:
//                                pnRow = tblBankEntry.getSelectionModel().getSelectedIndex();
//                                pagecounter = pnRow + pagination.getCurrentPageIndex() * ROWS_PER_PAGE;
//                                if (pagecounter == tblBankEntry.getItems().size()) {
//                                    pagecounter = tblBankEntry.getItems().size();
//                                    getSelectedItem(filteredData.get(pagecounter).getTblindex11());
//                                }else {
//                                   int y = 1;
//                                  pnRow = pnRow + y;
//                                    getSelectedItem(filteredData.get(pagecounter).getTblindex11());
//                                }
//                                break;
//                            case UP:
//                                pnRow = tblBankEntry.getSelectionModel().getSelectedIndex();
//                                pagecounter = pnRow + pagination.getCurrentPageIndex() * ROWS_PER_PAGE;
//                                getSelectedItem(filteredData.get(pagecounter).getTblindex11());
//                                break;
//                            default:
//                              return; 
//                      }
//                    });
//               } 
//               pnEditMode = EditMode.READY;
//               initButton(pnEditMode);  
//          }     
//     }
    
      
    
//      /*populate Table*/    
//     private void initBankEntryTable() {
//          tblindex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
//          tblindex02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
//          tblindex03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
//          tblindex04.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
//          
//          tblBankEntry.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
//               TableHeaderRow header = (TableHeaderRow) tblBankEntry.lookup("TableHeaderRow");
//               header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//               header.setReordering(false);
//               });
//          });
//          
//          filteredData = new FilteredList<>(bankentrydata, b -> true);
//          autoSearch(txtField02);
//          // 3. Wrap the FilteredList in a SortedList. 
//          SortedList<TableVehicleDescriptionList> sortedData = new SortedList<>(filteredData);
//
//          // 4. Bind the SortedList comparator to the TableView comparator.
//          // 	  Otherwise, sorting the TableView would have no effect.
//          sortedData.comparatorProperty().bind(tblBankEntry.comparatorProperty());
//
//          // 5. Add sorted (and filtered) data to the table.
//          tblBankEntry.setItems(sortedData);
//          tblBankEntry.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
//              TableHeaderRow header = (TableHeaderRow) tblBankEntry.lookup("TableHeaderRow");
//              header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//                  header.setReordering(false);
//              });
//              header.setDisable(true);
//          });
//            
//     }
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
                               }
                         else
                           return;
            }
    //          initButton(pnEditMode);  
        }
  
    @Override
    public void setGRider(GRider foValue) {
       
    }
    
//    private void loadTab(){
//                int totalPage = (int) (Math.ceil(bankentrydata.size() * 1.0 / ROWS_PER_PAGE));
//                pagination.setPageCount(totalPage);
//                pagination.setCurrentPageIndex(0);
//                changeTableView(0, ROWS_PER_PAGE);
//                pagination.currentPageIndexProperty().addListener(
//                        (observable, oldValue, newValue) -> changeTableView(newValue.intValue(), ROWS_PER_PAGE));
//      
//     } 
    
//      private void changeTableView(int index, int limit) {
//          int fromIndex = index * limit;
//          int toIndex = Math.min(fromIndex + limit, bankentrydata.size());
//
//          int minIndex = Math.min(toIndex, filteredData.size());
//          SortedList<BankEntryTableList> sortedData = new SortedList<>(
//                  FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
//          sortedData.comparatorProperty().bind(tblBankEntry.comparatorProperty());
//          tblBankEntry.setItems(sortedData); 
//     }
     
//    
//      //use for creating new page on pagination 
//     private Node createPage(int pageIndex) {
//          int fromIndex = pageIndex * ROWS_PER_PAGE;
//          int toIndex = Math.min(fromIndex + ROWS_PER_PAGE,bankentrydata.size());
//          if(bnkentrydata.size()>0){
//             tblVehicleDesc.setItems(FXCollections.observableArrayList(vhcldescdata.subList(fromIndex, toIndex))); 
//          }
//          return tblVehicleDesc;
//          
//     }
    
    
     /*Populate Text Field Based on selected address in table*/
//     private void getSelectedItem(String TransNo){
//        oldTransNo = TransNo;
//          if (oTrans.OpenRecord(TransNo)){
//               //txtField02.setText(bankentrydata.get(pagecounter).getTblindex10()); //Description 
//               txtField03.setText(bankentrydata.get(pagecounter).getTblindex02()); //Make
//               txtField04.setText(bankentrydata.get(pagecounter).getTblindex09()); //Color
//               txtField06.setText(bankentrydata.get(pagecounter).getTblindex06()); //Type
//               txtField08.setText(bankentrydata.get(pagecounter).getTblindex04()); //Year
//          }
//        
//     }
     private void loadBankEntryField(){
          try {

               txtField03.setText((String) oTrans.getMaster(15));
               txtField04.setText((String) oTrans.getMaster(16));
               txtField05.setText((String) oTrans.getMaster(17));
               txtField06.setText((String) oTrans.getMaster(18));
               txtField08.setText(oTrans.getMaster(8).toString());
               
          } catch (SQLException e) {
               ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
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
                         case 3: //sBankName
                              oTrans.setMaster(15, lsValue); //Handle Encoded Value
                              break;
                         case 4: //sBarangayName
                              oTrans.setMaster(16, lsValue); //Handle Encoded Value
                              break;
                         case 5: //sTownName
                              oTrans.setMaster(17, lsValue); //Handle Encoded Value
                              break;
                         case 6: //sProvNamex
                              oTrans.setMaster(18, lsValue); //Handle Encoded Value
                              break;
                    
                    }
                
            } else
               txtField.selectAll();
          } catch (SQLException ex) {
            Logger.getLogger(BankEntryFormController.class.getName()).log(Level.SEVERE, null, ex);
          }
     };
      private void txtField_KeyPressed(KeyEvent event){
          TextField txtField = (TextField)event.getSource();
          int lnIndex = Integer.parseInt(((TextField)event.getSource()).getId().substring(8,10));
          
          try{
               switch (event.getCode()){
                    case F3:
                         switch (lnIndex){ 

                              case 5: //Town Name
                                   if(oTrans.searchTown(txtField17.getText(),false)){
                                       loadBankEntryField();
                                   } else 
                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
           
                              break;
                              
                              case 6: //Province
                                   if(oTrans.searchProvince(txtField15.getText(),false)){
                                        loadBankEntryField();
                                        pnEditMode = oTrans.getEditMode();
                                   } else 
                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                              break;     
                         } 
                    break;
                    case TAB:
                    case ENTER:
                         switch (lnIndex){ 
                              
                              case 5: //Town Name
                                   if (oTrans.searchProvince(txtField17.getText(),false)){
                                       loadBankEntryField();
                                   } else 
                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
           
                              break;
                              
                              case 6: //Province
                                   if (oTrans.searchProvince(txtField15.getText(),false)){
                                        loadBankEntryField();
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
    
        
            /*Enabling / Disabling Fields*/
            private void initButton(int fnValue){
             pnRow = 0;
             /* NOTE:
                  lbShow (FALSE)= invisible
                  !lbShow (TRUE)= visible
             */
             boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

             /*Bank Entry*/
             txtField02.setDisable(!lbShow); // sBankNamexx
             txtField03.setDisable(!lbShow); // sBankCodex
             txtField06.setDisable(!lbShow); // sBranchx
             txtField05.setDisable(!lbShow); // sBarangayx
             txtField17.setDisable(!lbShow); // sTownNamex
             txtField15.setDisable(!lbShow); // sProvNamexx
             txtField07.setDisable(!lbShow); // sZipCode
             txtField04.setDisable(!lbShow); // sContactP
             txtField08.setDisable(!lbShow); // sTeleNo
             txtField09.setDisable(!lbShow); // sFaxNoxx

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

    
        txtField02.clear(); // sBankNamexx
        txtField03.clear(); // sBankCodex
        txtField06.clear(); // sBranchx
        txtField05.clear(); // sBarangayx
        txtField17.clear(); // sTownNamex
        txtField15.clear(); // sProvNamexx
        txtField07.clear(); // sZipCode
          txtField04.clear(); // sContactP
        txtField08.clear(); // sTeleNo
        txtField09.clear();// sFaxNoxx
          }
    @FXML
//    private void tblBankEntry_Clicked(MouseEvent event) {
//         if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
//               if(ShowMessageFX.OkayCancel(null, pxeModuleName, "You have unsaved data, are you sure you want to continue?") == true){   
//              } else
//                  return;
//          }
//          
//          pnRow = tblBankEntry.getSelectionModel().getSelectedIndex(); 
//          pagecounter = pnRow + pagination.getCurrentPageIndex() * ROWS_PER_PAGE;
//          if (pagecounter >= 0){
//               if(event.getClickCount() > 0){
//                    getSelectedItem(filteredData.get(pagecounter).getTblindex11()); //Populate field based on selected Item
//
//                    tblBankEntry.setOnKeyReleased((KeyEvent t)-> {
//                        KeyCode key = t.getCode();
//                        switch (key){
//                            case DOWN:
//                                pnRow =tblBankEntry.getSelectionModel().getSelectedIndex();
//                                pagecounter = pnRow + pagination.getCurrentPageIndex() * ROWS_PER_PAGE;
//                                if (pagecounter == tblBankEntry.getItems().size()) {
//                                    pagecounter = tblBankEntry.getItems().size();
//                                    getSelectedItem(filteredData.get(pagecounter).getTblindex11());
//                                }else {
//                                   int y = 1;
//                                  pnRow = pnRow + y;
//                                    getSelectedItem(filteredData.get(pagecounter).getTblindex11());
//                                }
//                                break;
//                            case UP:
//                                pnRow = tblBankEntry.getSelectionModel().getSelectedIndex();
//                                pagecounter = pnRow + pagination.getCurrentPageIndex() * ROWS_PER_PAGE;
//                                getSelectedItem(filteredData.get(pagecounter).getTblindex11());
//                                break;
//                            default:
//                              return; 
//                      }
//                    });
//               } 
//               pnEditMode = EditMode.READY;
//               initButton(pnEditMode);  
//          }     
//    }

    
 private Stage getStage(){
          return (Stage) txtField02.getScene().getWindow();
     }
//
//    @FXML
//    private void tblBankEntry_Clicked(MouseEvent event) {
//        
//        
//    }
//    
}
