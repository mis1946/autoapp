/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.auto.app.sales;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.auto.app.views.ScreenInterface;
import org.rmj.auto.app.views.unloadForm;
import org.rmj.auto.sales.base.InquiryMaster;

/**
 * FXML Controller class
 * 
 * @author John Dave
 * 
 */
public class InquiryFormController implements Initializable, ScreenInterface{
     private MasterCallback oListener;
     private InquiryMaster oTrans;
     private GRider oApp;

     unloadForm unload = new unloadForm(); //Object for closing form

     private final String pxeModuleName = "Inquiry"; //Form Title
     private int pnEditMode;//Modifying fields
     private int pnRow = -1;
     private int oldPnRow = -1;
     private int lnCtr = 0;
     private int pagecounter;
     private String oldTransNo = "";
     private String TransNo = "";
     
     /*populate tables search List*/
     private ObservableList<InquiryTableList> inqlistdata = FXCollections.observableArrayList();
     private FilteredList<InquiryTableList> filteredData;
     private static final int ROWS_PER_PAGE = 50;
   
    
     //General Elements

     //AnchorPane
     @FXML
     private AnchorPane AnchorMain; 
     //Buttons
     @FXML
     private Button btnClose; //Close
     @FXML
     private Button btnAdd; // Add
     @FXML
     private Button btnEdit; // Edit
     @FXML
     private Button btnSave; // Save
     @FXML
     private Button btnClear; // Clear
     @FXML
     private Button btnConvertSales; // Convert Sales
     @FXML
     private Button btnPrintRefund; // Print Refund
     @FXML
     private Button btnLostSale; // Lost of Sale
    //Table View
     @FXML
     private TableView tblInquiry; // Table View Inquiry 
     @FXML
     private TableColumn listIndex01;
     @FXML
     private TableColumn listIndex02;
     @FXML
     private TableColumn listIndex03;
     @FXML
     private TextField textSeek01;
     @FXML
     private DatePicker dateSeek01;
     @FXML
     private DatePicker dateSeek02;
     @FXML
     private Pagination pagination;
     //Tabs
     @FXML
     private Tab tabCustomerInquiry; // Customer Inquiry Tab
     @FXML
     private Tab tabInquiryProcess; // Inquiry Process Tab
     @FXML
     private Tab tabBankHistory; // Bank History Tab
     @FXML
     private Tab tabFollowingHistory; // Following History Tab
    
    
     /*Customer Inquiry Main */
     @FXML
     private TextField txtField02; //Branch Code
     @FXML
     private TextField txtfield03; //Inqiury Date
     @FXML
     private TextField txtField04; // Sales Executive
     @FXML
     private TextField txtField07; //Customer ID 
     @FXML
     private TextField txtField29; //Company ID 
     @FXML
     private TextField txtField30; //Contact Number
     @FXML
     private TextField txtField31; //Social Media
     @FXML
     private TextField txtField32; //Email
     @FXML
     private TextField txtField09; //Agent ID
     @FXML
     private TextArea textArea08; //Remarks
     @FXML
     private TextField txtField24; //Inquiry Status
     @FXML
     private TextField txtField18; //Reserve Amount
     @FXML
     private TextField txtField17; //Reserved
     @FXML
     private TextField txtField21; //Approved By
     @FXML
     private ComboBox cmbOnstr13; //Online Store
     @FXML
     private ComboBox cmbType012; //Inquiry Type
     @FXML
     private TextField txtField15; //Activity ID
     @FXML
     private ToggleGroup hotcategory; //Hot A, B, and C
     private RadioButton rdbtnHtA05; //Hot A
     private RadioButton rdbtnHtB05; //Hot B
     private RadioButton rdbtnHtC05; //Hot C
     @FXML
     private TextField txtField14; //Test Model
     @FXML
     private DatePicker txtField10; //Target Release Date
     @FXML
     private ToggleGroup targetVehicle;//Toggle Radio Button Target Vehicle 
     //Tables Target Vehicle
     @FXML
     private Button btnTargetVhclAdd;
     @FXML
     private Button btnTargetVhclRemove;
     @FXML
     private Button btnTargetVehicleUp; // Move Up Target Vehicle Up
     @FXML
     private Button btnTargetVehicleDown; // Move Down Target Vehicle
     @FXML
     private TableView tblTargetVehicle; // Table View Target Vehicle
     @FXML
     private TableColumn trgvIndex01;
     @FXML
     private TableColumn trgvIndex02;
     //Tables Promo Offered
     @FXML
     private Button btnPromosAdd; // Add Promo Offered
     @FXML
     private Button btnPromosRemove; //Remove Promo Offered
     @FXML
     private TableView tblPromosOffered;
     @FXML
     private TableColumn prmoIndex01;
     @FXML
     private TableColumn prmoIndex02;
     @FXML
     private TableColumn prmoIndex03;
     @FXML
     private TableColumn prmoIndex04;
    
    
    @FXML
    private TextField txtFieldSubCompany; // Sub Company
    @FXML
    private TextField textFieldRefundSlipNo; //Refund Slip No.
    @FXML
    private TextField textFieldRefundSlipAmount; //Refund Slip No. Amount
    //Combo Box
    private ComboBox comboInquiryType; // Inquiry Type
    private ComboBox comboOnlineStore; // Online Store
    private ComboBox comboEvent; // Event
    //Combo Box Value
    ObservableList<String> cInquiryType = FXCollections.observableArrayList("TYPE 1", "TYPE 2", "TPYE 3"); //Inquiry Type values
    ObservableList<String> cEvent = FXCollections.observableArrayList("EVENT 1", "EVENT 2", "EVENT 3"); // Event values
    ObservableList<String> cOnlineStore = FXCollections.observableArrayList("STORE 1", "STORE 2", "STORE 3"); // STORE values
    //Radio Toogle Group
    private ToggleGroup category; //Toggle Radio Button category 
    private ToggleGroup testDrive;//Toggle Radio Button Test Drive
     
    //Table View
    
    
    
    //Inquiry Process Elements
    
    //Buttons
    @FXML
    private Button btnASadd; // Add Advance Slip
    @FXML
    private Button btnASremove; // Remove Advance Slip
    @FXML
    private Button btnASprint; //  Print Advance Slip
    @FXML
    private Button btnASprintview; // Print View Advance Slip
    @FXML
    private Button btnAScancel;// Cancel Advance Slip
    @FXML
    private Button btnProcess;// Process Advance Slip
    @FXML
    private Button btnModify; // Modify Advance Slip
    @FXML
    private Button btnApply; // Apply Advance Slip
    
    //TextFields
    @FXML
    private TextField txtFieldIPCsNo; //Inquiry Process CS No
    @FXML
    private TextField txtFieldIPModel; //Inquiry Process Model
    @FXML
    private TextField txtFieldPlateNo; // Inquiry Process Plate No.
    @FXML
    private TextField txtFieldPaymentConversion; // Inquiry Process Payment Conversion
    //Table View
    @FXML
    private TableView<?> tblRequirementsInfo; // Table View Requirments Info
    @FXML
    private TableView<?> tblAdvanceSlip; // Table View Advance Slip
    //Combo Box
    @FXML
    private ComboBox comboIPModeofPayment; // Inquiry Process Mode of Payment
    @FXML
    private ComboBox comboIPCustomerType;// Inquiry Process Customer Type
    private ComboBox comboIPProcessApprovedBy; //Process Approved By
    //Combo Box Value  
    ObservableList<String> cModeOfPayment = FXCollections.observableArrayList("PAYMENT 1", "PAYMENT 2", "PAYMENT 3"); //Mode of Payment Values
    ObservableList<String> cCustomerType = FXCollections.observableArrayList("cTYPE 1", "cTYPE 2", "cTYPE 3"); // Customer Type Values
   
    //Bank Application History Elements
    //Buttons
    @FXML
    private Button btnBPHnew; //New Bank Application History
    @FXML
    private Button btnBPHupdate; // Update Bank Application History
    @FXML
    private Button btnBPHview; // View Bank Application History
    @FXML
    private Button btnBPHcancel; // Cancel Bank Application History

    @FXML
    private TableView tblBankHistory; //Table VieW Bank Application History
    //Tables
    
    //Follow Up Elements
    
    //Buttons
     @FXML
     private Button btnFollowUp; // FollowUp
     //Table View
     @FXML
     private TableView tblFollowHistory; //Table View Follow Up
     @FXML
     private RadioButton rdbtnHtA11;
     @FXML
     private RadioButton rdbtnHtB11;
     @FXML
     private RadioButton rdbtnHtC11;
     @FXML
     private RadioButton rdbtnNew05;
     @FXML
     private RadioButton rdbtnPro05;
     
     
     
     
     
     
     
     
     
     
    
   
    
  
     private Stage getStage(){
          return (Stage) textSeek01.getScene().getWindow();
     }
    /**
     * Initializes the controller class.
    **/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
          oListener = (int fnIndex, Object foValue) -> {
                 System.out.println("Set Class Value "  + fnIndex + "-->" + foValue);
          };
          
          oTrans = new InquiryMaster(oApp, oApp.getBranchCode(), true); //Initialize ClientMaster
          oTrans.setCallback(oListener);
          oTrans.setWithUI(true);
          
          
          
          //Populate table
          loadInquiryListTable();
          pagination.setPageFactory(this::createPage); 
         
          //Button SetOnAction using cmdButton_Click() method
          btnClose.setOnAction(this::cmdButton_Click);
          btnAdd.setOnAction(this::cmdButton_Click);
          btnSave.setOnAction(this::cmdButton_Click);
          btnClear.setOnAction(this::cmdButton_Click);
          btnEdit.setOnAction(this::cmdButton_Click);
          btnConvertSales.setOnAction(this::cmdButton_Click);
          btnPrintRefund.setOnAction(this::cmdButton_Click);
          btnLostSale.setOnAction(this::cmdButton_Click); 
          
          /*Clear Fields*/
          clearFields();
          
          pnEditMode = EditMode.UNKNOWN;
          initButton(pnEditMode);
        
    }    
    
     @Override
     public void setGRider(GRider foValue) {
              oApp = foValue;
     }
    
     //Method/Function for general buttons
     private void cmdButton_Click(ActionEvent event) {
           String lsButton = ((Button)event.getSource()).getId();
           switch(lsButton){

             case "btnAdd":
                 ShowMessageFX.Information(null, "Information", "You click add button!"); 
                 break;
             case "btnEdit":
                  ShowMessageFX.Information(null, "Information", "You click edit button!"); 
                 break;
             case "btnSave":
                  ShowMessageFX.Information(null, "Information","You click save button!"); 
                 break;
             case "btnClear":
                  ShowMessageFX.Information(null, "Information", "You click clear button!"); 
                 break;
             case "btnConvertSales":
                  ShowMessageFX.Information(null, "Information", "You click convert to sales button"); 
                 break;
             case "btnPrintRefund":
                  ShowMessageFX.Information(null, "Information", "You click print refund button"); 
                 break;
             case "btnLostSale":
                  ShowMessageFX.Information(null, "Information", "You click lost sale button"); 
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
              default:
                     ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                     return;
             }

     }
     
      //Search using F3
    private void txtField_KeyPressed(KeyEvent event){
         TextField txtField = (TextField)event.getSource();
          int lnIndex = Integer.parseInt(((TextField)event.getSource()).getId().substring(8,10));
          
          try{
               switch (event.getCode()){
                    case F3:
                         switch (lnIndex){ 
                              case 7: //Customer
                                   if (oTrans.searchCustomer(txtField07.getText(),true)){
                                        loadCustomerInquiry();
                                   } else 
                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                              
                              break;
                              case 9: //Agent
//                                   if (oTrans.searchAgent(txtField09.getText())){
//                                        loadCustomerInquiry();
//                                   } else 
//                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
//                              
                              break;
                              
                              case 15: //Event / Activity 
//                                   if (oTrans.searchActivity(txtField15.getText())){
//                                        loadCustomerInquiry();
//                                   } else 
//                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);                            
                              break;
                              
                              case 14: //Model 
                                   if (oTrans.searchVhclPrty(pnRow,txtField14.getText(),false)){
                                        loadCustomerInquiry();
                                   } else 
                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                              break;   
                         } 
                    break;
                    case TAB:
                    case ENTER:
                         switch (lnIndex){ 
                              case 7: //Customer
                                   if (oTrans.searchCustomer(txtField07.getText(),false)){
                                        loadCustomerInquiry();
                                   } else 
                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                              break;
                              
                              case 9: //Agent
//                                   if (oTrans.searchAgent(txtField09.getText())){
//                                        loadCustomerInquiry();
//                                   } else 
//                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                              break;
                              
                              case 15: //Event / Activity 
//                                   if (oTrans.searchActivity(txtField15.getText())){
//                                        loadCustomerInquiry();
//                                   } else 
//                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);                            
                              break;
                              
                              case 14: //Model 
                                   if (oTrans.searchVhclPrty(pnRow,txtField14.getText(),false)){
                                        loadCustomerInquiry();
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
     
     //use for creating new page on pagination 
     private Node createPage(int pageIndex) {
          int fromIndex = pageIndex * ROWS_PER_PAGE;
          int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, inqlistdata.size());
          if(inqlistdata.size()>0){
             tblInquiry.setItems(FXCollections.observableArrayList(inqlistdata.subList(fromIndex, toIndex))); 
          }
          return tblInquiry;
          
     }
    
    //Load Customer Profile Data
    public void loadInquiryListTable(){
         try {
               /*Populate table*/
               inqlistdata.clear();
               //if (oTrans.LoadList("")){
                    String sDescription ;
                    for (lnCtr = 1; lnCtr <= oTrans.getInquiryMasterCount(); lnCtr++){
                         sDescription = oTrans.getInqDetail(lnCtr,"sModelDsc").toString() 
                                 + " " + oTrans.getInqDetail(lnCtr,"sTypeDesc").toString()
                                 + " " + oTrans.getInqDetail(lnCtr,"sTransMsn").toString()
                                 + " " + oTrans.getInqDetail(lnCtr,"sColorDsc").toString();
                         inqlistdata.add(new InquiryTableList(
                         String.valueOf(lnCtr), //ROW
                         oTrans.getInqDetail(lnCtr,"sMakeDesc").toString(), //Make
                         sDescription, //Description
                         oTrans.getInqDetail(lnCtr,"nYearModl").toString(), //Year
                         oTrans.getInqDetail(lnCtr,"sModelDsc").toString(), //Model
                         oTrans.getInqDetail(lnCtr,"sTypeDesc").toString(), //Type
                         oTrans.getInqDetail(lnCtr,"sTransMsn").toString(), //Transmission
                         oTrans.getInqDetail(lnCtr,"cVhclSize").toString(), //Vehicle Size
                         oTrans.getInqDetail(lnCtr,"sColorDsc").toString(), //Color        
                         oTrans.getInqDetail(lnCtr,"sDescript").toString(), //Description 
                         oTrans.getInqDetail(lnCtr,"sVhclIDxx").toString()  //sVhclIDxx
                         ));
                    }
                    initInquiryListTable();
               //}
               loadTab();
               
          } catch (SQLException e) {
               ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
          }
            
    }
    
    // Load Inquiry List
    public void initInquiryListTable(){
          listIndex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
          listIndex02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
          listIndex03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
         
          tblInquiry.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
               TableHeaderRow header = (TableHeaderRow) tblInquiry.lookup("TableHeaderRow");
               header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
               header.setReordering(false);
               });
          });
          
          filteredData = new FilteredList<>(inqlistdata, b -> true);
          autoSearch(textSeek01);
          // 3. Wrap the FilteredList in a SortedList. 
          SortedList<InquiryTableList> sortedData = new SortedList<>(filteredData);

          // 4. Bind the SortedList comparator to the TableView comparator.
          // 	  Otherwise, sorting the TableView would have no effect.
          sortedData.comparatorProperty().bind(tblInquiry.comparatorProperty());

          // 5. Add sorted (and filtered) data to the table.
          tblInquiry.setItems(sortedData);
          tblInquiry.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
              TableHeaderRow header = (TableHeaderRow) tblInquiry.lookup("TableHeaderRow");
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
                           if(lnIndex == 1){
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
                int totalPage = (int) (Math.ceil(inqlistdata.size() * 1.0 / ROWS_PER_PAGE));
                pagination.setPageCount(totalPage);
                pagination.setCurrentPageIndex(0);
                changeTableView(0, ROWS_PER_PAGE);
                pagination.currentPageIndexProperty().addListener(
                        (observable, oldValue, newValue) -> changeTableView(newValue.intValue(), ROWS_PER_PAGE));
      
     } 
     
     private void changeTableView(int index, int limit) {
          int fromIndex = index * limit;
          int toIndex = Math.min(fromIndex + limit, inqlistdata.size());

          int minIndex = Math.min(toIndex, filteredData.size());
          SortedList<InquiryTableList> sortedData = new SortedList<>(
                  FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
          sortedData.comparatorProperty().bind(tblInquiry.comparatorProperty());
          tblInquiry.setItems(sortedData); 
     }
     
     @FXML
     private void tblInquiry_Clicked(MouseEvent event) {
          if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
               if(ShowMessageFX.OkayCancel(null, pxeModuleName, "You have unsaved data, are you sure you want to continue?") == true){   
              } else
                  return;
          }
          
          pnRow = tblInquiry.getSelectionModel().getSelectedIndex(); 
          pagecounter = pnRow + pagination.getCurrentPageIndex() * ROWS_PER_PAGE;
          if (pagecounter >= 0){
               if(event.getClickCount() > 0){
                    getSelectedItem(filteredData.get(pagecounter).getTblindex11()); //Populate field based on selected Item

                    tblInquiry.setOnKeyReleased((KeyEvent t)-> {
                        KeyCode key = t.getCode();
                        switch (key){
                
                             case DOWN:
                                pnRow = tblInquiry.getSelectionModel().getSelectedIndex();
                                pagecounter = pnRow + pagination.getCurrentPageIndex() * ROWS_PER_PAGE;
                                if (pagecounter == tblInquiry.getItems().size()) {
                                    pagecounter = tblInquiry.getItems().size();
                                    getSelectedItem(filteredData.get(pagecounter).getTblindex11());
                                }else {
                                   int y = 1;
                                  pnRow = pnRow + y;
                                    getSelectedItem(filteredData.get(pagecounter).getTblindex11());
                                }
                                break;
                            case UP:
                                pnRow = tblInquiry.getSelectionModel().getSelectedIndex();
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
     
     //Populate Text Field Based on selected transaction in table
     private void getSelectedItem(String TransNo){
          oldTransNo = TransNo;
          if (oTrans.OpenRecord(TransNo)){
//               //txtField02.setText(vhcldescdata.get(pagecounter).getTblindex10()); //Description 
//               txtField03.setText(inqlistdata.get(pagecounter).getTblindex02()); //Make
//               txtField04.setText(inqlistdata.get(pagecounter).getTblindex05()); //Model
//               txtField05.setText(inqlistdata.get(pagecounter).getTblindex09()); //Color
//               txtField06.setText(inqlistdata.get(pagecounter).getTblindex06()); //Type
//               txtField08.setText(inqlistdata.get(pagecounter).getTblindex04()); //Year
//
//               switch (inqlistdata.get(pagecounter).getTblindex07()) { //Transmission
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
//               comboBox09.getSelectionModel().select(Integer.parseInt(inqlistdata.get(pagecounter).getTblindex08())); //Vehicle Size
//               oldPnRow = pagecounter;   
          }

     }
     
    //Load Customer Inquiry Data
    public void loadCustomerInquiry(){
         try {
               txtfield03.setText((String) oTrans.getMaster(3)); //Inquiry Date
               txtField07.setText((String) oTrans.getMaster(29)); //Custmer Name ***
               txtField29.setText((String) oTrans.getMaster(29)); //Company Name
               txtField04.setText((String) oTrans.getMaster(4)); //Sales Executive ID //Employee ID
               //txtField33.setText((String) oTrans.getMaster(33)); //Address
               txtField30.setText((String) oTrans.getMaster(30)); //Contact No
               txtField32.setText((String) oTrans.getMaster(32)); //Email Address
               txtField31.setText((String) oTrans.getMaster(31)); //Social Media
               
               cmbType012.getSelectionModel().select(Integer.parseInt(oTrans.getMaster(12).toString())); //Inquiry Type
               cmbOnstr13.getSelectionModel().select(Integer.parseInt(oTrans.getMaster(13).toString())); //Online Store
               txtField10.setValue(strToDate(CommonUtils.xsDateShort((Date) oTrans.getMaster(10)))); //Target Release Date
               txtField09.setText((String) oTrans.getMaster(9)); //Agent ID
               txtField15.setText((String) oTrans.getMaster(15)); //Activity ID
               txtField02.setText((String) oTrans.getMaster(2)); //Branch Code
               txtField24.setText((String) oTrans.getMaster(24)); //Inquiry Status
               
               switch (oTrans.getMaster(5).toString().toLowerCase()) {
                    case "a":
                        rdbtnNew05.setSelected(true);
                        break;
                   case "b":
                        rdbtnPro05.setSelected(true);
                        break;
                   default:
                        rdbtnNew05.setSelected(false);
                        rdbtnPro05.setSelected(false);
                        break;
               }
               
               switch (oTrans.getMaster(5).toString().toLowerCase()) {
                   case "a":
                        rdbtnHtA05.setSelected(true);
                        break;
                   case "b":
                        rdbtnHtB05.setSelected(true);
                        break;
                   case "c":
                        rdbtnHtC05.setSelected(true);
                        break;
                   default:
                        rdbtnHtA05.setSelected(false);
                        rdbtnHtB05.setSelected(false);
                        rdbtnHtC05.setSelected(false);
                        break;
               }
               textArea08.setText((String) oTrans.getMaster(8)); //Remarks
               txtField17.setText((String) oTrans.getMaster(17)); //Slip No.
               txtField18.setText((String) oTrans.getMaster(18)); //Rsv Amount
               

           } catch (SQLException e) {
                ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
           }
         
          initTargetVehicle();
          initPromosOffered();
    }
    
    
    
    //Load Inquiry Process Data
    public void loadInquiryProcess(){
        initRequirments();
        initAdvanceSlip();
            
    }
    //Load Bank Application History Data
    public void laodBankHistory(){
        initBankHistory();
    }
    //Load FollowUp Data
    public void loadFollowUp(){
        initFollowUp();
    }
    
  
    // Load Customer Inquiry Target Vehicle Data
    public void initTargetVehicle(){
        
    }
    //Load Customer Inquiry PromosOffered
    public void initPromosOffered(){
        
    }
    // Load Inquiry Process Requirements
    public void initRequirments(){
    
    }
    // Load Inquiry Process Advance Slip
    public void initAdvanceSlip(){
        
    }
    //Load Bank Application Data in table
    public void initBankHistory(){
        
    }
    //Load FollowUp Data
    public void initFollowUp(){
        
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
                              if (lsValue.trim().equals("")){
                                   oTrans.setMaster(lnIndex,  0); //Handle Encoded Value     
                              } else {
                                   oTrans.setMaster(lnIndex,  Integer.parseInt(lsValue)); //Handle Encoded Value
                              }
                              break;
                        
                    }
                
            } else
               txtField.selectAll();
          } catch (SQLException ex) {
            Logger.getLogger(InquiryFormController.class.getName()).log(Level.SEVERE, null, ex);
          }
     };
     
     /*Convert Date to String*/
     private LocalDate strToDate(String val){
          DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
          LocalDate localDate = LocalDate.parse(val, date_formatter);
          return localDate;
     }
     
     /*Set Date Value to Master Class*/
     public void getDate(ActionEvent event) { 
          try {
               oTrans.setMaster(10,SQLUtil.toDate(txtField10.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
          }catch (SQLException ex) {
               Logger.getLogger(InquiryFormController.class.getName()).log(Level.SEVERE, null, ex);
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
          
          /*Vehicle Description*/
          //txtField03.setDisable(!lbShow); // sMakeIDxx
          
          
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
    
    //Method for clearing Fields
    public void clearFields(){
          pnRow = 0;      
            //Clear TextFields
            //General
            
            //Customer Inquiry
            
            //InquiryProcess
            
                 
            //COMBOBOX
            //Customer Inquiry  
            comboInquiryType.setValue(null);
            comboOnlineStore.setValue(null);
            comboEvent.setValue(null);

            //Inquiry Process
            comboIPModeofPayment.setValue(null);
            comboIPCustomerType.setValue(null);
            comboIPProcessApprovedBy.setValue(null);
                 
            //Radio Button Toggle Group
            category.selectToggle(null);
            targetVehicle.selectToggle(null);

        }

     
    }
