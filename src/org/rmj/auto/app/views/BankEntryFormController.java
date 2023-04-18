/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.auto.app.views;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
         
    private String oldTransNo = "";
    private String TransNo = "";
    
    private ObservableList<BankEntryTableList> bankentrydata = FXCollections.observableArrayList();
    private FilteredList<BankEntryTableList> filteredData;
    private static final int ROWS_PER_PAGE = 50;
    
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Pagination pagination;
    @FXML
    private TableView tblBankEntry;
    @FXML
    private TableColumn tblindex01;
    @FXML
    private TableColumn tblindex02;
    @FXML
    private TableColumn tblindex03;
    @FXML
    private TableColumn tblindex17;
    @FXML
    private TableColumn tblindex16;
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
    private TextField txtField08; //Telephone No.
    @FXML
    private TextField txtField18; //Town Name
    @FXML
    private TextField txtField15; //Province Name
    @FXML
    private TextField txtField07; //Zip Code
    @FXML
    private TextField txtField09; //Fax No
    @FXML
    private TextField txtField05;
    @FXML
    private TextField txtField17;
    @FXML
    private TextField textSeek02;
 


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
          txtField17.focusedProperty().addListener(txtField_Focus); // sBankBrch
          txtField05.focusedProperty().addListener(txtField_Focus); // sAddressx
          txtField18.focusedProperty().addListener(txtField_Focus); // sTownNamexx
          txtField15.focusedProperty().addListener(txtField_Focus); // sProvName
          txtField07.focusedProperty().addListener(txtField_Focus); // sZipCode
          txtField04.focusedProperty().addListener(txtField_Focus); // sContactP
          txtField08.focusedProperty().addListener(txtField_Focus); // sTelNoxxx
          txtField09.focusedProperty().addListener(txtField_Focus); // sFaxNoxx
    
          textSeek02.setOnKeyPressed(this::txtField_KeyPressed); //search bank name textfield
          txtField18.setOnKeyPressed(this::txtField_KeyPressed); // sTownNamexx
          txtField15.setOnKeyPressed(this::txtField_KeyPressed); // sProvNamexx
          
          //Button Click Event
          btnAdd.setOnAction(this::cmdButton_Click);
          btnEdit.setOnAction(this::cmdButton_Click); 
          btnSave.setOnAction(this::cmdButton_Click); 
          btnClose.setOnAction(this::cmdButton_Click); 
            
            /*Clear Fields*/
          clearFields();
          
          //Populate table
          loadBankEntryTable();
          pagination.setPageFactory(this::createPage); 
          
          
          pnEditMode = EditMode.UNKNOWN;
          initButton(pnEditMode); 
     
    }    
    
     private Stage getStage(){
          return (Stage) txtField02.getScene().getWindow();
     }

    @FXML
   private void tblBankEntry_Clicked(MouseEvent event) {
          if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
               if(ShowMessageFX.OkayCancel(null, pxeModuleName, "You have unsaved data, are you sure you want to continue?") == true){   
              } else
                  return;
          }
          
          pnRow = tblBankEntry.getSelectionModel().getSelectedIndex(); 
          pagecounter = pnRow + pagination.getCurrentPageIndex() * ROWS_PER_PAGE;
          if (pagecounter >= 0){
               if(event.getClickCount() > 0){
                    getSelectedItem(filteredData.get(pagecounter).getTblindex01()); //Populate field based on selected Item

                    tblBankEntry.setOnKeyReleased((KeyEvent t)-> {
                     KeyCode key = t.getCode();
                    switch (key){
                          case DOWN:
                              pnRow = tblBankEntry.getSelectionModel().getSelectedIndex();
                                pagecounter = pnRow + pagination.getCurrentPageIndex() * ROWS_PER_PAGE;
                               if (pagecounter == tblBankEntry.getItems().size()) {
                                   pagecounter = tblBankEntry.getItems().size();
                                    getSelectedItem(filteredData.get(pagecounter).getTblindex01());
                                }else {
                                   int y = 1;
                                  pnRow = pnRow + y;
                                    getSelectedItem(filteredData.get(pagecounter).getTblindex01());
                                }
                                break;
                            case UP:
                                pnRow = tblBankEntry.getSelectionModel().getSelectedIndex();
                                pagecounter = pnRow + pagination.getCurrentPageIndex() * ROWS_PER_PAGE;
                                getSelectedItem(filteredData.get(pagecounter).getTblindex01());
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
    
      
    
      /*populate Table*/    
     private void initBankEntryTable() {
        
          tblindex01.setCellValueFactory(new PropertyValueFactory<>("tblindexRow")); 
          tblindex02.setCellValueFactory(new PropertyValueFactory<>("tblindex02")); // sBankName
          tblindex03.setCellValueFactory(new PropertyValueFactory<>("tblindex03")); // sBankCode
          tblindex17.setCellValueFactory(new PropertyValueFactory<>("tblindex17")); // sBankBrch
          tblindex16.setCellValueFactory(new PropertyValueFactory<>("tblindex16")); // sTownProvxx
          
    
          
          tblBankEntry.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
               TableHeaderRow header = (TableHeaderRow) tblBankEntry.lookup("TableHeaderRow");
               header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
               header.setReordering(false);
               });
          });
          
          filteredData = new FilteredList<>(bankentrydata, b -> true);
          autoSearch(textSeek02);
          // 3. Wrap the FilteredList in a SortedList. 
          SortedList<BankEntryTableList> sortedData = new SortedList<>(filteredData);
          // 4. Bind the SortedList comparator to the TableView comparator.
          // 	  Otherwise, sorting the TableView would have no effect.
          sortedData.comparatorProperty().bind(tblBankEntry.comparatorProperty());
          // 5. Add sorted (and filtered) data to the table.
          tblBankEntry.setItems(sortedData);
          tblBankEntry.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
              TableHeaderRow header = (TableHeaderRow) tblBankEntry.lookup("TableHeaderRow");
              header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                  header.setReordering(false);
              });
              header.setDisable(true);
          });
            
     }
      //storing values on bankentrydata  
     private void loadBankEntryTable(){
          try {
               /*Populate table*/
               bankentrydata.clear();
               if (oTrans.LoadList("")){
                    for (lnCtr = 1; lnCtr <= oTrans.getItemCount(); lnCtr++){
                         bankentrydata.add(new BankEntryTableList(
                         String.valueOf(lnCtr), //ROW
                         "",//BANKID
                         oTrans.getDetail(lnCtr,"sBankName").toString(),// sBankName
                         oTrans.getDetail(lnCtr,"sBankCode").toString(),// sBankCode
                         oTrans.getDetail(lnCtr,"sBankBrch").toString(), // sBankBrch
                         oTrans.getDetail(lnCtr,"sAddressx").toString(), //sAddressx
                         oTrans.getDetail(lnCtr,"sTownProv").toString(), //sTownProv
                         oTrans.getDetail(lnCtr,"sProvName").toString(), //sProvName
                         oTrans.getDetail(lnCtr,"sZippCode").toString(), //sZipCode     
                         oTrans.getDetail(lnCtr,"sContactP").toString(), //sContactP
                         oTrans.getDetail(lnCtr,"sTelNoxxx").toString(),  //sTelNoxxx
                         oTrans.getDetail(lnCtr,"sFaxNoxxx").toString(), //sFaxNoxx
                         oTrans.getDetail(lnCtr,"sTownName").toString() //sTownName     
                         ));
                    }
                    initBankEntryTable();
               }
               loadTab();
               
          } catch (SQLException e) {
               ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
          }
     }
     private void cmdButton_Click(ActionEvent event) {
          String lsButton = ((Button)event.getSource()).getId();
          switch(lsButton){
            case "btnAdd":
                 if (oTrans.NewRecord()) {
                              clearFields(); 
                              loadBankEntryField();
                              pnEditMode = oTrans.getEditMode();
                         } else 
                             ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                          break;
            case "btnEdit":
               if (oTrans.UpdateRecord()) {
                              pnEditMode = oTrans.getEditMode(); 
                         } else 
                              ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                         break;
            case "btnSave":
                   //Validate before saving
                         if (txtField02.getText().trim().equals("")) {
                              ShowMessageFX.Warning(getStage(), "Please enter a value for Bank Name.","Warning", null);
                              txtField02.requestFocus();
                              return;
                         }
                         if (txtField03.getText().trim().equals("")) {
                              ShowMessageFX.Warning(getStage(), "Please enter a value for Bank Addev.","Warning", null);
                              txtField03.requestFocus();
                              return;
                         }
                         if (txtField17.getText().trim().equals("")) {
                              ShowMessageFX.Warning(getStage(), "Please enter a valid value for Branch","Warning", null);
                              txtField17.requestFocus();
                              return;
                         }
                         if (txtField05.getText().trim().equals("")) {
                              ShowMessageFX.Warning(getStage(), "Please enter a value for HouseNo/Street/Barangay.","Warning", null);
                              txtField05.requestFocus();
                              return;
                         }
                         if (txtField18.getText().trim().equals("")) {
                              ShowMessageFX.Warning(getStage(), "Please enter a value for Municipality.","Warning", null);
                              txtField18.requestFocus();
                              return;
                         }
                         
                         if ( txtField15.getText().trim().equals("")){
                              ShowMessageFX.Warning(getStage(), "Please enter a valid value for Province.","Warning", null);
                              txtField15.requestFocus();
                              return;
                         }
                         if (txtField07.getText().trim().equals("")) {
                              ShowMessageFX.Warning(getStage(), "Please enter a value for Zipcode.","Warning", null);
                              txtField07.requestFocus();
                              return;
                         }
                         if (txtField04.getText().trim().equals("")) {
                              ShowMessageFX.Warning(getStage(), "Please enter a value for Contact Person","Warning", null);
                              txtField04.requestFocus();
                              return;
                         }
                         if (txtField08.getText().trim().equals("")) {
                              ShowMessageFX.Warning(getStage(), "Please enter a value for Telephone No.","Warning", null);
                              txtField08.requestFocus();
                              return;
                         }
                         if (txtField09.getText().trim().equals("")) {
                              ShowMessageFX.Warning(getStage(), "Please enter a value for Fax No.","Warning", null);
                              txtField09.requestFocus();
                              return;
                         }
                         //Proceed Saving
                  
                              if (oTrans.SaveRecord()){
                                   ShowMessageFX.Information(getStage(), "Transaction save successfully.", pxeModuleName, null);
                                   loadBankEntryTable();
                                   pnEditMode = oTrans.getEditMode();
                              } else {
                                  ShowMessageFX.Warning(getStage(),oTrans.getMessage() ,"Warning", "Error while saving Bank Information");
                              }
                         
                         
                         
                         
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
              initButton(pnEditMode);  
        }
  
    @Override
    public void setGRider(GRider foValue) {
          oApp = foValue;
     }
    
    private void loadTab(){
                int totalPage = (int) (Math.ceil(bankentrydata.size() * 1.0 / ROWS_PER_PAGE));
                pagination.setPageCount(totalPage);
                pagination.setCurrentPageIndex(0);
                changeTableView(0, ROWS_PER_PAGE);
                pagination.currentPageIndexProperty().addListener(
                        (observable, oldValue, newValue) -> changeTableView(newValue.intValue(), ROWS_PER_PAGE));
      
     } 
    
     private void changeTableView(int index, int limit) {
     int fromIndex = index * limit;
     int toIndex = Math.min(fromIndex + limit, bankentrydata.size());

      int minIndex = Math.min(toIndex, filteredData.size());
      SortedList<BankEntryTableList> sortedData = new SortedList<>(
      FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
      sortedData.comparatorProperty().bind(tblBankEntry.comparatorProperty());
      tblBankEntry.setItems(sortedData); 
    }
     
    
      //use for creating new page on pagination 
        private Node createPage(int pageIndex) {
          int fromIndex = pageIndex * ROWS_PER_PAGE;
          int toIndex = Math.min(fromIndex + ROWS_PER_PAGE,bankentrydata.size());
          if(bankentrydata.size()>0){
             tblBankEntry.setItems(FXCollections.observableArrayList(bankentrydata.subList(fromIndex, toIndex))); 
          }
          return tblBankEntry;
          
     }
    
          /*Populate Text Field Based on selected address in table*/
          private void getSelectedItem(String TransNo){
          oldTransNo = TransNo;
          if (oTrans.OpenRecord(TransNo)){
          
          txtField02.setText(bankentrydata.get(pagecounter).getTblindex02()); // sBankName
          txtField03.setText(bankentrydata.get(pagecounter).getTblindex03()); // sBankCode
          txtField17.setText(bankentrydata.get(pagecounter).getTblindex17()); // sBankBrch
          txtField05.setText(bankentrydata.get(pagecounter).getTblindex05()); // sAddressx
          txtField18.setText(bankentrydata.get(pagecounter).getTblindex18()); // sTownNamexx
          txtField15.setText(bankentrydata.get(pagecounter).getTblindex15()); // sProvName
          txtField07.setText(bankentrydata.get(pagecounter).getTblindex07()); // sZipCode
          txtField04.setText(bankentrydata.get(pagecounter).getTblindex04()); // sContactP
          txtField08.setText(bankentrydata.get(pagecounter).getTblindex08()); // sTelNoxxx
          txtField09.setText(bankentrydata.get(pagecounter).getTblindex09()); // sFaxNoxx
            }

     }
     private void loadBankEntryField(){
          try {
              
            txtField02.setText((String) oTrans.getMaster(2)); // sBankName
            txtField03.setText((String) oTrans.getMaster(3)); // sBankCode
            txtField17.setText((String) oTrans.getMaster(17)); // sBankBrch
            txtField05.setText((String) oTrans.getMaster(5)); // sAddressx
            txtField18.setText((String) oTrans.getMaster(18)); // sTownNamexx
            txtField15.setText((String) oTrans.getMaster(15)); // sProvName
            txtField07.setText((String) oTrans.getMaster(7)); // sZipCode
            txtField04.setText((String) oTrans.getMaster(4)); // sContactP
            txtField08.setText((String) oTrans.getMaster(8)); // sTelNoxxx
            txtField09.setText((String) oTrans.getMaster(9));// sFaxNoxx
               
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
                         case 2: //sBankName
                              oTrans.setMaster(2, lsValue); //Handle Encoded Value
                              break;
                         case 3: // sBankCode
                              oTrans.setMaster(3, lsValue); //Handle Encoded Value
                              break;
                         case 17: // sBankBrch
                              oTrans.setMaster(17, lsValue); //Handle Encoded Value
                              break;
                         case 5: // sAddressx
                              oTrans.setMaster(5, lsValue); //Handle Encoded Value
                              break;
                         case 18:// sTownNamexx
                              oTrans.setMaster(18, lsValue); //Handle Encoded Value
                              break;
                         case 15:// sProvName
                              oTrans.setMaster(15, lsValue); //Handle Encoded Value
                              break;
                         case 7: // sZipCode
                              oTrans.setMaster(7, lsValue); //Handle Encoded Value
                              break;
                         case 4:// sContactP
                              oTrans.setMaster(4, lsValue); //Handle Encoded Value
                              break;
                         case 8: // sTelNoxxx
                              oTrans.setMaster(8, lsValue); //Handle Encoded Value
                              break;
                         case 9:// sFaxNoxx
                              oTrans.setMaster(9, lsValue); //Handle Encoded Value
                              break;                    
                    }
                
            } else
               txtField.selectAll();
          } catch (SQLException ex) {
            Logger.getLogger(BankEntryFormController.class.getName()).log(Level.SEVERE, null, ex);
          }
     };
     
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
                               return (clients.getTblindex02().toLowerCase().contains(lowerCaseFilter)); // Does not match.   
                            }else {
                               return (clients.getTblindex02().toLowerCase().contains(lowerCaseFilter)); // Does not match.
                            }   
                       default:
                       return true;            
            }
            });
            
            changeTableView(0, ROWS_PER_PAGE);
        });
        loadTab();
     } 
      private void txtField_KeyPressed(KeyEvent event){
          TextField txtField = (TextField)event.getSource();
          int lnIndex = Integer.parseInt(((TextField)event.getSource()).getId().substring(8,10));
          
          try{
               switch (event.getCode()){
                    case F3:
                         switch (lnIndex){ 

                              case 18:// sTownNamexx
                                   if(oTrans.searchTown(txtField18.getText(),false)){
                                       loadBankEntryField();
                                   } else 
                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
           
                              break;
                              
                              case 15: // sProvName
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
                              
                              case 18: // sTownNamexx
                                   if (oTrans.searchProvince(txtField18.getText(),false)){
                                       loadBankEntryField();
                                   } else 
                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
           
                              break;
                              
                              case 15: // sProvName
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
             txtField17.setDisable(!lbShow); // sBranchx
             txtField05.setDisable(!lbShow); // sBarangayx
             txtField18.setDisable(!lbShow); // sTownNamex
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
        txtField17.clear(); // sBranchx
        txtField05.clear(); // sBarangayx
        txtField18.clear(); // sTownNamex
        txtField15.clear(); // sProvNamexx
        txtField07.clear(); // sZipCode
        txtField04.clear(); // sContactP
        txtField08.clear(); // sTeleNo
        txtField09.clear();// sFaxNoxx
          
       }
}
