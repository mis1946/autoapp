/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.auto.app.sales;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
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
import org.rmj.auto.sales.base.InquiryProcess;

/**
 * FXML Controller class
 * 
 * @author John Dave / Arsiela 
 * 
 */
public class InquiryFormController implements Initializable, ScreenInterface{
     private MasterCallback oListener;
     private InquiryMaster oTrans;
     private InquiryProcess oTransProcess;
     private GRider oApp;

     unloadForm unload = new unloadForm(); //Object for closing form
     private double xOffset = 0;
     private double yOffset = 0;
     private final String pxeModuleName = "Inquiry"; //Form Title
     private int pnEditMode = EditMode.UNKNOWN;;//Modifying fields
     private int pnRow = -1;
     private int oldPnRow = -1;
     private int selectedTblRowIndex = -1;
     private int lnCtr = 0;
     private int lnRow = 0;
     private int pagecounter;
     private String oldTransNo = "";
     private String TransNo = "";
     private String sValue = "";
     private String sInqStat = "";
     
     /*populate tables search List*/
     private ObservableList<InquiryTableList> inqlistdata = FXCollections.observableArrayList();
     private FilteredList<InquiryTableList> filteredData;
     private static final int ROWS_PER_PAGE = 50;
     
     /*Populate Table Details*/
     private ObservableList<InquiryTablePriorityUnit> priorityunitdata = FXCollections.observableArrayList();
     private ObservableList<InquiryTablePromoOffered> promosoffereddata = FXCollections.observableArrayList();
     
     /*General Elements*/

     //AnchorPane
     @FXML
     private AnchorPane AnchorMain;
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
     private TableColumn listIndex04;
     @FXML
     private TableColumn listIndex05;
     @FXML
     private TableColumn listIndex06;
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
     @FXML
     private Button btnCancel; //Cancel / Revert
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
     private TableView tblPriorityUnit; // Table View Target Vehicle
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
     
     /*Customer Inquiry Main */
     @FXML
     private TextField txtField02; //Branch Code 
     @FXML
     private TextField txtField03;//Inqiury Date
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
     private TextField txtField33; //Client Address
     @FXML
     private TextField txtField09; //Agent ID
     @FXML
     private TextArea textArea08; //Remarks
     @FXML
     private TextField txtField18; //Reserve Amount
     @FXML
     private TextField txtField17; //Reserved
     @FXML
     private ComboBox cmbOnstr13; //Online Store
     @FXML
     private ComboBox cmbType012; //Inquiry Type
     @FXML
     private TextField txtField15; //Activity ID
     @FXML
     private TextField txtField14; //Test Model
     @FXML
     private DatePicker txtField10; //Target Release Date
     @FXML
     private ToggleGroup targetVehicle;//Toggle Radio Button Target Vehicle 
     @FXML
     private ToggleGroup hotCategory;
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
     
     //Combo Box Value
     ObservableList<String> cInquiryType = FXCollections.observableArrayList("Walk-in", "Web Inquiry", "Phone-in", "Referral", "Sales Call","Event", "Service", "Office Account","Caremittance","Database","UIO"); //Inquiry Type values
     ObservableList<String> cOnlineStore = FXCollections.observableArrayList("Facebook", "WhatsUp", "Instagram", "Tiktok", "Twitter");
     
    /*INQUIRY PROCESS*/
     //Populate table
     private ObservableList<InquiryTableRequirements> inqrequirementsdata = FXCollections.observableArrayList();
     private ObservableList<InquiryTableVehicleSalesAdvances> inqvsadata = FXCollections.observableArrayList();

     //Combo Box Value  
     ObservableList<String> cModeOfPayment = FXCollections.observableArrayList("Cash", "Purchase Order", "Financing"); //Mode of Payment Values
     ObservableList<String> cCustomerType = FXCollections.observableArrayList("Business", "Employed", "OFW", "Seaman", "Any"); // Customer Type Values
     @FXML
     private ComboBox cmbInqpr01;
     @FXML
     private ComboBox cmbInqpr02;
     // Table View Requirments Info
     @FXML
     private TableView tblRequirementsInfo; 
     @FXML
     private TableColumn rqrmIndex01;
     @FXML
     private TableColumn rqrmIndex02;
     @FXML
     private TableColumn rqrmIndex03;
     @FXML
     private TableColumn rqrmIndex04;
     //Reserve Unit
     @FXML
     private TextField txtRsvcs06;
     @FXML
     private TextField txtRsvpn06;
     @FXML
     private TextField txtRsvmd06;
     // Table View Advance Slip
     @FXML
     private TableView tblAdvanceSlip; 
     @FXML
     private TableColumn vsasIndex01;
     @FXML
     private TableColumn vsasIndex02;
     @FXML
     private TableColumn vsasIndex03;
     @FXML
     private TableColumn vsasIndex04;
     @FXML
     private TableColumn vsasIndex05;
     @FXML
     private TableColumn vsasIndex06;
     @FXML
     private TableColumn vsasIndex07;
     //Buttons for Advance Slip
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
     //Approval and Payments
     @FXML
     private TextField txtField21; //Approved By
     @FXML
     private TextField txtPymtc01;
     @FXML
     private Button btnPymtcon;
     @FXML
     private Button btnProcess;// Process Advance Slip
     @FXML
     private Button btnModify; // Modify Advance Slip
     @FXML
     private Button btnApply; // Apply Advance Slip
     
     /*INQUIRY BANK APPLICATION*/
     @FXML
     private TableView tblBankApplication;
     @FXML
     private Button btnBankAppView;
     @FXML
     private Button btnBankAppNew;
     @FXML
     private Button btnBankAppUpdate;
     @FXML
     private Button btnBankAppCancel;
    
     /*INQUIRY FOLLOW-UP*/
     @FXML
     private Button btnFollowUp; // FollowUp
     //Table View
     @FXML
     private TableView tblFollowHistory; //Table View Follow Up
     ObservableList<String> cInqStatus = FXCollections.observableArrayList("For Follow-up", "On Process", "Lost Sale", "VSP", "Sold", "Retired" ,"Cancelled"); //Inquiry Type Values
     @FXML
     private ComboBox comboBox24;
     @FXML
     private TableColumn<?, ?> vsasCheck01;
     

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
          initTargetVehicle();
          initPromosOffered();
          initInquiryAdvances();
          
          dateSeek02.setOnAction(event -> {
               loadInquiryListTable();
               pagination.setPageFactory(this::createPage);
          });
          dateSeek01.setOnAction(event -> {
               loadInquiryListTable();
               pagination.setPageFactory(this::createPage);
          });
          
          //dateSeek01.setValue(LocalDate.of(2023, Month.JANUARY, 1)); //birthdate
          dateSeek01.setValue(LocalDate.of(strToDate(CommonUtils.xsDateShort((Date) oApp.getServerDate())).getYear(), strToDate(CommonUtils.xsDateShort((Date) oApp.getServerDate())).getMonth(), 1));
          dateSeek02.setValue(strToDate(CommonUtils.xsDateShort((Date) oApp.getServerDate())));
          
          //Populate table
          loadInquiryListTable();
          pagination.setPageFactory(this::createPage);
          
          /*CUSTOMER INQUIRY*/
          /*populate combobox*/
          comboBox24.setItems(cInqStatus); //Inquiry Status
          cmbType012.setItems(cInquiryType); //Inquiry Type
          cmbOnstr13.setItems(cOnlineStore); //Web Inquiry
          cmbType012.setOnAction(event -> {
               cmbOnstr13.setDisable(true);
               txtField09.setDisable(true);
               txtField15.setDisable(true);
               System.out.println("cmbType012 " + cmbType012.getSelectionModel().getSelectedIndex());
               switch (cmbType012.getSelectionModel().getSelectedIndex()) {
                    
                    case 1:
                         cmbOnstr13.setDisable(false);
                         txtField15.setText("");
                         txtField09.setText("");
                         break;
                    case 3:
                         txtField09.setDisable(false);
                         txtField15.setText("");
                         cmbOnstr13.setValue(null);
                         break;
                    case 4:
                    case 5:
                         txtField15.setDisable(false);
                         txtField09.setText("");
                         cmbOnstr13.setValue(null);
                         break;
               }
               
               try {
                    oTrans.setMaster(12, String.valueOf(cmbType012.getSelectionModel().getSelectedIndex()));
               } catch (SQLException ex) {
                    Logger.getLogger(InquiryFormController.class.getName()).log(Level.SEVERE, null, ex);
               }
               
          });
          
          txtField04.focusedProperty().addListener(txtField_Focus);  // Sales Executive
          txtField07.focusedProperty().addListener(txtField_Focus);  //Customer ID 
          txtField29.focusedProperty().addListener(txtField_Focus);  //Company ID 
          txtField09.focusedProperty().addListener(txtField_Focus);  //Agent ID
          textArea08.focusedProperty().addListener(txtArea_Focus);  //Remarks
          txtField15.focusedProperty().addListener(txtField_Focus);  //Activity ID
          txtField14.focusedProperty().addListener(txtField_Focus);  //Test Model  
          txtField10.setOnAction(this::getDate); 
          
          txtField04.setOnKeyPressed(this::txtField_KeyPressed);  // Sales Executive
          txtField07.setOnKeyPressed(this::txtField_KeyPressed);  //Customer ID 
          txtField29.setOnKeyPressed(this::txtField_KeyPressed);  //Company ID 
          txtField09.setOnKeyPressed(this::txtField_KeyPressed);  //Agent ID
          textArea08.setOnKeyPressed(this::txtArea_KeyPressed);  //Remarks
          txtField15.setOnKeyPressed(this::txtField_KeyPressed);  //Activity ID
          txtField14.setOnKeyPressed(this::txtField_KeyPressed);  //Test Model  

          //Button SetOnAction using cmdButton_Click() method
          btnClose.setOnAction(this::cmdButton_Click);
          btnAdd.setOnAction(this::cmdButton_Click);
          btnSave.setOnAction(this::cmdButton_Click);
          btnClear.setOnAction(this::cmdButton_Click);
          btnEdit.setOnAction(this::cmdButton_Click);
          btnConvertSales.setOnAction(this::cmdButton_Click);
          btnPrintRefund.setOnAction(this::cmdButton_Click);
          btnLostSale.setOnAction(this::cmdButton_Click); 
          btnCancel.setOnAction(this::cmdButton_Click); 
          btnTargetVhclAdd.setOnAction(this::cmdButton_Click); 
          btnTargetVhclRemove.setOnAction(this::cmdButton_Click); 
          btnTargetVehicleUp.setOnAction(this::cmdButton_Click);
          btnTargetVehicleDown.setOnAction(this::cmdButton_Click); 
          btnPromosAdd.setOnAction(this::cmdButton_Click); 
          btnPromosRemove.setOnAction(this::cmdButton_Click); 
          
          /*INQUIRY PROCESS*/
          oTransProcess = new InquiryProcess(oApp, oApp.getBranchCode(), true); //Initialize ClientMaster
          oTransProcess.setCallback(oListener);
          oTransProcess.setWithUI(true);
          cmbInqpr01.setItems(cModeOfPayment); //Mode of Payment
          cmbInqpr02.setItems(cCustomerType); //Customer Type
          //Button SetOnAction using cmdButton_Click() method
          btnASadd.setOnAction(this::cmdButton_Click); 
          btnASremove.setOnAction(this::cmdButton_Click);
          btnAScancel.setOnAction(this::cmdButton_Click); 
          btnASprintview.setOnAction(this::cmdButton_Click); 
          btnASprint.setOnAction(this::cmdButton_Click);
          btnProcess.setOnAction(this::cmdButton_Click); 
          btnModify.setOnAction(this::cmdButton_Click); 
          btnApply.setOnAction(this::cmdButton_Click);
          btnPymtcon.setOnAction(this::cmdButton_Click);
          
          tblAdvanceSlip.setRowFactory(tv -> {
               TableRow<InquiryTableVehicleSalesAdvances> row = new TableRow<>();
               row.setOnMouseClicked(event -> {
                    try {
                         if (event.getClickCount() == 2 && !row.isEmpty()) {
                              InquiryTableVehicleSalesAdvances rowData = row.getItem();
                              loadVehicleSalesAdvancesWindow( row.getIndex()+1, false);
                         }
                         if (!row.isEmpty()) {
                              if ((oTransProcess.getInqRsv(row.getIndex()+1, 1).toString() != null) && (!oTransProcess.getInqRsv(row.getIndex()+1, 1).toString().trim().equals(""))){
                                   btnAScancel.setDisable(true);
                                   btnASprintview.setDisable(true);
                                   btnASprint.setDisable(true);
                              } else {
                                   btnAScancel.setDisable(false);
                                   btnASprintview.setDisable(false);
                                   btnASprint.setDisable(false);
                              }
                         }
                         
                    } catch (SQLException ex) {
                         Logger.getLogger(InquiryFormController.class.getName()).log(Level.SEVERE, null, ex);
                    }
            });
            return row;
        });
          
          
          //Mode of Payment
          cmbInqpr01.setOnAction(event -> {
               cmbInqpr02.setValue(null);
               cmbInqpr02.setItems(cCustomerType);
               switch (cmbInqpr01.getSelectionModel().getSelectedIndex()) {
                    case 0:
                    case 1:
                         cmbInqpr02.getSelectionModel().select(4); //Set to Any
                         cmbInqpr02.setDisable(true);
                         break;
                    case 2:
                         ObservableList<String> cCustType = FXCollections.observableArrayList( "Business", "Employed", "OFW", "Seaman"); // Customer Type Values
                         cmbInqpr02.setItems(cCustType);
                         cmbInqpr02.setDisable(false);
                         break;
               }
          });
          
          //Customer Type
          cmbInqpr02.setOnAction(event -> {
               loadInquiryRequirements();
          });
          
          /*INQUIRY BANK APPLICATION*/ 
          btnBankAppNew.setOnAction(this::cmdButton_Click); 
          btnBankAppUpdate.setOnAction(this::cmdButton_Click); 
          btnBankAppCancel.setOnAction(this::cmdButton_Click); 
          btnBankAppView.setOnAction(this::cmdButton_Click);
          
          /*INQUIRY FOLLOW-UP*/
          btnFollowUp.setOnAction(this::cmdButton_Click);
          
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
          try {
          String lsButton = ((Button)event.getSource()).getId();
          switch(lsButton){
               /*CUSTOMER INQUIRY: PRIORITY UNIT*/
               case "btnTargetVhclAdd": 
                    lnRow = priorityunitdata.size();
                    if (lnRow == 0){
                         lnRow = 1;
                    } else {
                         lnRow++;
                    }
                    oTrans.addVhclPrty();
                    if (oTrans.searchVhclPrty(lnRow,"",false)){
                    } else {
                         oTrans.removeTargetVehicle(lnRow);
                         ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                    }
                    loadTargetVehicle();
                    break;
               
               case "btnTargetVhclRemove":
                    selectedTblRowIndex = tblPriorityUnit.getSelectionModel().getSelectedIndex() + 1;
                    if (selectedTblRowIndex >= 1) {
                         if ((oTrans.getVhclPrty(selectedTblRowIndex, 1).toString() != null) && (!oTrans.getVhclPrty(selectedTblRowIndex, 1).toString().trim().equals(""))){
                         } else {
                              oTrans.removeTargetVehicle(selectedTblRowIndex);
                              loadTargetVehicle();
                         }
                    }
                    selectedTblRowIndex = 0;
                    break;     
               case "btnTargetVehicleDown":
                    //if (tblPriorityUnit.getItems().size() > 0 ) {
                         if ((selectedTblRowIndex < tblPriorityUnit.getItems().size() - 1) && selectedTblRowIndex != tblPriorityUnit.getItems().size()) {
                              Collections.swap(tblPriorityUnit.getItems(), selectedTblRowIndex, selectedTblRowIndex + 1);
                              tblPriorityUnit.getSelectionModel().select(selectedTblRowIndex + 1);
                              lnCtr = 1;
                              for (InquiryTablePriorityUnit unit : priorityunitdata) {
                                   oTrans.setVhclPrty(lnCtr, "nPriority",unit.getTblindex01());
                                   oTrans.setVhclPrty(lnCtr, "sDescript",unit.getTblindex02());
                                   lnCtr++;
                              }
                              loadTargetVehicle();
                         selectedTblRowIndex++;
                         }
                         
                    //}
                    break;    
               case "btnTargetVehicleUp":
                    if (selectedTblRowIndex > 0 ) {
                         Collections.swap(tblPriorityUnit.getItems(), selectedTblRowIndex, selectedTblRowIndex - 1);
                         tblPriorityUnit.getSelectionModel().select(selectedTblRowIndex - 1);
                         lnCtr = 1;
                         for (InquiryTablePriorityUnit unit : priorityunitdata) {
                              oTrans.setVhclPrty(lnCtr, "nPriority",unit.getTblindex01());
                              oTrans.setVhclPrty(lnCtr, "sDescript",unit.getTblindex02());
                              lnCtr++;
                         }
                         loadTargetVehicle();
                         selectedTblRowIndex--;
                    }
                    break;
               
               /*CUSTOMER INDQUIRY: PROMO OFFERED*/
               case "btnPromosAdd":
                    lnRow = promosoffereddata.size();
                    if (lnRow == 0){
                         lnRow = 1;
                    } else {
                         lnRow++;
                    }
                    oTrans.addVhclPrty();
                    if (oTrans.searchVhclPrty(lnRow,"",false)){
                    } else {
                         oTrans.removeTargetVehicle(lnRow);
                         ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                    }
                    loadPromosOfferred();
                    break;
               case "btnPromosRemove":
                    selectedTblRowIndex = tblPromosOffered.getSelectionModel().getSelectedIndex() + 1;
                    if (selectedTblRowIndex >= 1) {
                         if ((oTrans.getVhclPrty(selectedTblRowIndex, 1).toString() != null) && (!oTrans.getVhclPrty(selectedTblRowIndex, 1).toString().trim().equals(""))){
                         } else {
                              oTrans.removeTargetVehicle(selectedTblRowIndex);
                              loadTargetVehicle();
                         }
                    }
                    selectedTblRowIndex = 0;
                    break;
               /*CUSTOMER INQUIRY: GENERAL BUTTON*/
               case "btnAdd":
                    //pnEditMode  = EditMode.ADDNEW; 
                    if (oTrans.NewRecord()){
                         /*Clear Fields*/
                         clearFields(); 
                         clearClassFields();
                         loadCustomerInquiry();
                         loadTargetVehicle();
                         loadPromosOfferred();
                         textSeek01.clear(); // Client Search
                         pnEditMode = oTrans.getEditMode();
                    } else 
                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                     break;
               case "btnEdit":
                    if (oTrans.UpdateRecord() ){
                         pnEditMode = oTrans.getEditMode(); 
                    } else 
                         ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                    break;
               case "btnSave":
                    if(ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to save?") == true){
                         //TODO
                         if (setSelection()) {
                              if(oTrans.SaveRecord()){
                                   ShowMessageFX.Information(getStage(), "Transaction save successfully.", pxeModuleName, null);
                                   loadCustomerInquiry();
                                   loadTargetVehicle();
                                   loadPromosOfferred();
                                   loadInquiryListTable();
                                   pagination.setPageFactory(this::createPage);
                                   pnEditMode = oTrans.getEditMode();
                              } else {
                                  ShowMessageFX.Warning(getStage(),oTrans.getMessage() ,"Warning", "Error while saving " + pxeModuleName);
                              }
                         }
                         break;
                    }else
                         return;
               case "btnClear":
                    clearClassFields();
                    loadCustomerInquiry();
                    loadTargetVehicle();
                    loadPromosOfferred();
                    ShowMessageFX.Information(null, pxeModuleName, "You click clear button!"); 
                    break;
               case "btnConvertSales":
                    
                    ShowMessageFX.Information(null, pxeModuleName, "You click convert to sales button"); 
                   break;
               case "btnPrintRefund":
                    ShowMessageFX.Information(null, pxeModuleName, "You click print refund button"); 
                   break;
               case "btnLostSale":
                    if(ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to tag this inquiry as LOST SALE?") == true){
                         //TODO
                         break;
                    }else
                         return;
               /*INQUIRY PROCESS*/
               case "btnASadd":
                    //Open window for inquiry reservation
                    lnRow = inqvsadata.size();
                    if (lnRow == 0){
                         lnRow = 1;
                    } else {
                         lnRow++;
                    }
                    loadVehicleSalesAdvancesWindow(lnRow, true);
                    break; 
               case "btnASremove":
                    selectedTblRowIndex = tblAdvanceSlip.getSelectionModel().getSelectedIndex() + 1;
                    if (selectedTblRowIndex >= 1) {
                         if ((oTransProcess.getInqRsv(selectedTblRowIndex, 1).toString() != null) && (!oTransProcess.getInqRsv(selectedTblRowIndex, 1).toString().trim().equals(""))){
                         } else {
                              oTransProcess.removeInqRes(selectedTblRowIndex);
                              loadInquiryAdvances();
                         }
                    }
                    selectedTblRowIndex = 0;
                    break;
               case "btnAScancel":
                    break;
               case "btnASprintview":
                    break;    
               case "btnASprint":
                    break;
               case "btnProcess":
               case "btnApply":
                    if (lsButton == "btnProcess"){
                         if (oTransProcess.NewRecord()){
                         } else 
                              return;
                    }
                    
                    if(ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to save?") == true){
                         //TODO
                         oTransProcess.setTransNox((String) oTrans.getMaster(1));
                         
                         
                         if(oTransProcess.SaveRecord()){
                              ShowMessageFX.Information(getStage(), "Transaction save successfully.", pxeModuleName, null);
                              loadCustomerInquiry();
                              loadTargetVehicle();
                              loadPromosOfferred();
                              
                              //Retrieve Requirements
                              oTransProcess.loadRequirements((String) oTrans.getMaster(1));
                              //Load Table Requirements
                              loadInquiryRequirements();
                              //Retrieve Reservation
                              oTransProcess.loadReservation((String) oTrans.getMaster(1));
                              //Load Table Reservation
                              loadInquiryAdvances();
                              
                              loadInquiryListTable();
                              pagination.setPageFactory(this::createPage);
                              
                              initBtnProcess(oTransProcess.getEditMode());
                         } else {
                             ShowMessageFX.Warning(getStage(),oTransProcess.getMessage() ,"Warning", "Error while saving " + pxeModuleName);
                         }
                         initBtnProcess(oTransProcess.getEditMode());
                         break;
                    }else
                         return;
                    
               case "btnModify":
                    if (oTransProcess.UpdateRecord()){
                         } else 
                              return;
                    initBtnProcess(oTransProcess.getEditMode());
                    break;
               case "btnPymtcon":
                    break;
               /*INQUIRY BANK APPLICATION*/
               case "btnBankAppNew":
                    //Open window 
                    loadBankApplication("");
                    break;
               case "btnBankAppUpdate":
                    //Open window 
                    loadBankApplication("");
                    break;
               case "btnBankAppCancel":
                    break;
               case "btnBankAppView":
                    //Open window 
                    loadBankApplication("");
                    break;
               
               /*INQUIRY FOR FOLLOW UP*/
               case "btnFollowUp":
                    //Open window 
                    loadFollowUp("");
                    break;
                            
               case "btnCancel":
                    if(ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to cancel?") == true){
                         clearFields();
                         clearClassFields();
                         loadCustomerInquiry();
                         loadTargetVehicle();
                         loadPromosOfferred();
                         pnEditMode = EditMode.UNKNOWN;
                         break;
                    }else
                         return;
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
               initButton(pnEditMode); 
          } catch (SQLException ex) {
               Logger.getLogger(InquiryFormController.class.getName()).log(Level.SEVERE, null, ex);
          }

     }
     
     /*INQUIRY PROCESS*/
     private void loadVehicleSalesAdvancesWindow(int fnRow, boolean fstate) throws SQLException{
          /**
           * if state = true : ADD
           * else if state = false : UPDATE
          ***/
          try {
               Stage stage = new Stage();

               FXMLLoader fxmlLoader = new FXMLLoader();
               fxmlLoader.setLocation(getClass().getResource("InquiryVehicleSalesAdvancesForm.fxml"));

               InquiryVehicleSalesAdvancesFormController loControl = new InquiryVehicleSalesAdvancesFormController();
               loControl.setGRider(oApp);
               loControl.setVSAObject(oTransProcess);
               loControl.setTableRows(fnRow);
               loControl.setState(fstate);

               fxmlLoader.setController(loControl);

               //load the main interface
               Parent parent = fxmlLoader.load();
               
               parent.setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        xOffset = event.getSceneX();
                        yOffset = event.getSceneY();
                    }
               });
               
               parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
                   @Override
                   public void handle(MouseEvent event) {
                       stage.setX(event.getScreenX() - xOffset);
                       stage.setY(event.getScreenY() - yOffset);
                   }
               });

               //set the main interface as the scene
               Scene scene = new Scene(parent);
               stage.setScene(scene);
               stage.initStyle(StageStyle.TRANSPARENT);
               stage.initModality(Modality.APPLICATION_MODAL);
               stage.setTitle("");
               stage.showAndWait();
               
               loadInquiryAdvances();
          } catch (IOException e) {
              e.printStackTrace();
              ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
              System.exit(1);
          }
     }
     
     /*INQUIRY BANK APPLICATION*/
     private void loadBankApplication(String sTransno) throws SQLException{
          try {
               Stage stage = new Stage();

               FXMLLoader fxmlLoader = new FXMLLoader();
               fxmlLoader.setLocation(getClass().getResource("InquiryBankApplicationForm.fxml"));

               InquiryBankApplicationFormController loControl = new InquiryBankApplicationFormController();
               loControl.setGRider(oApp);
               loControl.setVSAObject(oTrans);
               //loControl.setTableRows(sTransno);

               fxmlLoader.setController(loControl);

               //load the main interface
               Parent parent = fxmlLoader.load();
                           
               parent.setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        xOffset = event.getSceneX();
                        yOffset = event.getSceneY();
                    }
               });
               
               parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
                   @Override
                   public void handle(MouseEvent event) {
                       stage.setX(event.getScreenX() - xOffset);
                       stage.setY(event.getScreenY() - yOffset);
                   }
               });

               //set the main interface as the scene
               Scene scene = new Scene(parent);
               stage.setScene(scene);
               stage.initStyle(StageStyle.TRANSPARENT);
               stage.initModality(Modality.APPLICATION_MODAL);
               stage.setTitle("");
               stage.showAndWait();
               
          } catch (IOException e) {
              e.printStackTrace();
              ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
              System.exit(1);
          }
     }
     
     /*INQUIRY FOR FOLLOW-UP*/
     private void loadFollowUp(String sTransno) throws SQLException{
          try {
               Stage stage = new Stage();

               FXMLLoader fxmlLoader = new FXMLLoader();
               fxmlLoader.setLocation(getClass().getResource("InquiryFollowUpForm.fxml"));

               InquiryFollowUpFormController loControl = new InquiryFollowUpFormController();
               loControl.setGRider(oApp);
               loControl.setVSAObject(oTrans);
               //loControl.setTableRows(sTransno);

               fxmlLoader.setController(loControl);

               //load the main interface
               Parent parent = fxmlLoader.load();
               
                                          
               parent.setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        xOffset = event.getSceneX();
                        yOffset = event.getSceneY();
                    }
               });
               
               parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
                   @Override
                   public void handle(MouseEvent event) {
                       stage.setX(event.getScreenX() - xOffset);
                       stage.setY(event.getScreenY() - yOffset);
                   }
               });

               //set the main interface as the scene
               Scene scene = new Scene(parent);
               stage.setScene(scene);
               stage.initStyle(StageStyle.TRANSPARENT);
               stage.initModality(Modality.APPLICATION_MODAL);
               stage.setTitle("");
               stage.showAndWait();

          } catch (IOException e) {
              e.printStackTrace();
              ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
              System.exit(1);
          }
     }
     
     //Search using F3
     private void txtField_KeyPressed(KeyEvent event){
         TextField txtField = (TextField)event.getSource();
          int lnIndex = Integer.parseInt(((TextField)event.getSource()).getId().substring(8,10));
          
          try{
               switch (event.getCode()){
                    case F3:
                    case TAB:
                    case ENTER:
                         switch (lnIndex){ 
                              case 7: //Customer
                                   if (oTrans.searchCustomer(txtField07.getText(),false)){
                                        loadCustomerInquiry();
                                   } else 
                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                              break;
                              case 4: //Sales Executive
                                   if (oTrans.searchSalesExec(txtField04.getText(), false)){
                                        loadCustomerInquiry();
                                   } else 
                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                              break;
                              
                              case 9: //Agent
                                   if (oTrans.searchSalesAgent(txtField09.getText(), false)){
                                        loadCustomerInquiry();
                                   } else 
                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                              break;
                              
                              case 15: //Event / Activity 
//                                   if (oTrans.searchSalesAgent(txtField09.getText(), false)){
//                                        loadCustomerInquiry();
//                                   } else 
//                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);                            
                              break;
                              
                              case 14: //Model 
                                   if (oTrans.searchVhclPrty(0,txtField14.getText(),false)){
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
    
     /*TRIGGER FOCUS*/
     private void txtArea_KeyPressed(KeyEvent event){
        if (event.getCode() == ENTER || event.getCode() == DOWN){ 
            event.consume();
            CommonUtils.SetNextFocus((TextArea)event.getSource());
        }else if (event.getCode() ==KeyCode.UP){
        event.consume();
            CommonUtils.SetPreviousFocus((TextArea)event.getSource());
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
               if (oTrans.loadCustomer("",dateSeek01.getValue().toString(), dateSeek02.getValue().toString(),false)){
                    for (lnCtr = 1; lnCtr <= oTrans.getInquiryMasterCount(); lnCtr++){
                         //Inquiry Status
                         switch (oTrans.getInqDetail(lnCtr, "cTranStat").toString()) { // cTranStat
                              case "0":
                                   sInqStat = "For Follow-up";
                                   break;
                              case "1":
                                   sInqStat = "On Process" ; 
                                   break;
                              case "2":
                                   sInqStat = "Lost Sale";
                                   break;
                              case "3":
                                   sInqStat = "VSP";
                                   break;
                              case "4":
                                   sInqStat = "Sold";
                                   break;
                              case "5":
                                   sInqStat = "Retired" ;
                                   break;
                              case "6":
                                   sInqStat = "Cancelled" ; 
                                   break;
                              default:
                                   sInqStat = "" ; //Inquiry Status
                                   break;
                         }
                         
                         inqlistdata.add(new InquiryTableList(
                         String.valueOf(lnCtr), //ROW
                         CommonUtils.xsDateShort((Date) oTrans.getInqDetail(lnCtr,"dTransact")), //Inquir date
                         oTrans.getInqDetail(lnCtr,"sCompnyNm").toString(), //Company/ customer name
                         sInqStat, 
                                 
                         oTrans.getInqDetail(lnCtr, "sTransNox").toString(), //
                         oTrans.getInqDetail(lnCtr, "sBranchCD").toString(), //
                         CommonUtils.xsDateMedium((Date) oTrans.getInqDetail(lnCtr,"dTransact")), //Inquir date
                         oTrans.getInqDetail(lnCtr, "sEmployID").toString(), // Employee name
                         oTrans.getInqDetail(lnCtr, "cIsVhclNw").toString(), //
                         oTrans.getInqDetail(lnCtr, "sVhclIDxx").toString(), //
                         oTrans.getInqDetail(lnCtr, "sClientID").toString(), //sClientID Customer name
                         oTrans.getInqDetail(lnCtr, "sRemarksx").toString(), //
                         oTrans.getInqDetail(lnCtr, "sAgentIDx").toString(), // Agent name
                         //oTrans.getInqDetail(lnCtr, "dTargetDt").toString(), // 
                         CommonUtils.xsDateShort((Date) oTrans.getInqDetail(lnCtr,"dTargetDt")), //Target date
                         oTrans.getInqDetail(lnCtr, "cIntrstLv").toString(), //
                         oTrans.getInqDetail(lnCtr, "sSourceCD").toString(), //
                         oTrans.getInqDetail(lnCtr, "sSourceNo").toString(), //
                         oTrans.getInqDetail(lnCtr, "sTestModl").toString(), //
                         oTrans.getInqDetail(lnCtr, "sActvtyID").toString(), //
                         //oTrans.getInqDetail(lnCtr, "dLastUpdt").toString(), //
                         oTrans.getInqDetail(lnCtr, "nReserved").toString(), //
                         oTrans.getInqDetail(lnCtr, "nRsrvTotl").toString(), //
                         //oTrans.getInqDetail(lnCtr, "sLockedBy").toString(), //
                         //oTrans.getInqDetail(lnCtr, "sLockedDt").toString(), //
                         oTrans.getInqDetail(lnCtr, "sApproved").toString(), //
                         //oTrans.getInqDetail(lnCtr, "sSerialID").toString(), //
                         //oTrans.getInqDetail(lnCtr, "sInqryCde").toString(), //
                         oTrans.getInqDetail(lnCtr, "cTranStat").toString(),
                        // oTrans.getInqDetail(lnCtr, "sEntryByx").toString(), //
                        // oTrans.getInqDetail(lnCtr, "dEntryDte").toString(), //
                        // oTrans.getInqDetail(lnCtr, "sModified").toString(), //
                        // oTrans.getInqDetail(lnCtr, "dModified").toString(), //
                        // oTrans.getInqDetail(lnCtr, "dTimeStmp").toString(), //
                         oTrans.getInqDetail(lnCtr, "sCompnyNm").toString(), //
                         oTrans.getInqDetail(lnCtr, "sMobileNo").toString(), //
                         oTrans.getInqDetail(lnCtr, "sAccountx").toString(), //        
                         oTrans.getInqDetail(lnCtr, "sEmailAdd").toString(), //        
                         oTrans.getInqDetail(lnCtr, "sAddressx").toString(), //
                         oTrans.getInqDetail(lnCtr, "sSalesExe").toString(), //        
                         oTrans.getInqDetail(lnCtr, "sSalesAgn").toString() //       
                                 
                         ));
                    }
                    initInquiryListTable();
               }
               loadTab();
               
          } catch (SQLException e) {
               ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
          }
            
    }
    
    // Load Inquiry List
    public void initInquiryListTable(){
          listIndex01.setCellValueFactory(new PropertyValueFactory<>("tbllistindex01"));
          listIndex02.setCellValueFactory(new PropertyValueFactory<>("tbllistindex02"));
          listIndex03.setCellValueFactory(new PropertyValueFactory<>("tbllistindex03"));
          listIndex04.setCellValueFactory(new PropertyValueFactory<>("tbllistindex04"));
          listIndex05.setCellValueFactory(new PropertyValueFactory<>("tblcinqindex34"));
          listIndex06.setCellValueFactory(new PropertyValueFactory<>("tblcinqindex17"));
          
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
                               return (clients.getTbllistindex03().toLowerCase().contains(lowerCaseFilter)); // Does not match.   
                            }else {
                               return (clients.getTbllistindex03().toLowerCase().contains(lowerCaseFilter)); // Does not match.
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
                    getSelectedItem(filteredData.get(pagecounter).getTblcinqindex01()); //Populate field based on selected Item

                    tblInquiry.setOnKeyReleased((KeyEvent t)-> {
                        KeyCode key = t.getCode();
                        switch (key){
                             case DOWN:
                                pnRow = tblInquiry.getSelectionModel().getSelectedIndex();
                                pagecounter = pnRow + pagination.getCurrentPageIndex() * ROWS_PER_PAGE;
                                if (pagecounter == tblInquiry.getItems().size()) {
                                    pagecounter = tblInquiry.getItems().size();
                                    getSelectedItem(filteredData.get(pagecounter).getTblcinqindex01());
                                }else {
                                   int y = 1;
                                  pnRow = pnRow + y;
                                    getSelectedItem(filteredData.get(pagecounter).getTblcinqindex01());
                                }
                                break;
                            case UP:
                                pnRow = tblInquiry.getSelectionModel().getSelectedIndex();
                                pagecounter = pnRow + pagination.getCurrentPageIndex() * ROWS_PER_PAGE;
                                
                                getSelectedItem(filteredData.get(pagecounter).getTblcinqindex01());
                                break;
                            default:
                              return; 
                      }
                    });
               } 
               
               initButton(pnEditMode);  
          }  
     }
     
     //Populate Text Field Based on selected transaction in table
     private void getSelectedItem(String TransNo) {
          try {
               oldTransNo = TransNo;
               if (oTrans.OpenRecord(TransNo)){
                    pnEditMode = oTrans.getEditMode();
                    txtField02.setText(inqlistdata.get(pagecounter).getTblcinqindex02()); // sBranchCD
                    txtField03.setText(inqlistdata.get(pagecounter).getTblcinqindex03()); //
                    txtField04.setText(inqlistdata.get(pagecounter).getTblcinqindex34()); // sSalesExe
                    txtField07.setText(inqlistdata.get(pagecounter).getTblcinqindex29()); // sCompnyNm
                    switch (inqlistdata.get(pagecounter).getTblcinqindex05()) { //cIsVhclNw
                         case "0":
                             rdbtnNew05.setSelected(true);
                             break;
                         case "1":
                              rdbtnPro05.setSelected(true);
                              break;
                         default:
                              rdbtnNew05.setSelected(false);
                              rdbtnPro05.setSelected(false);
                              break;
                    }

                    textArea08.setText(inqlistdata.get(pagecounter).getTblcinqindex08()); // sRemarksx
                    txtField09.setText(inqlistdata.get(pagecounter).getTblcinqindex35()); // sSalesAgn
                    txtField10.setValue( strToDate( inqlistdata.get(pagecounter).getTblcinqindex10())); //dTargetDt

                    switch (inqlistdata.get(pagecounter).getTblcinqindex11().toLowerCase()) {
                        case "a":
                             rdbtnHtA11.setSelected(true);
                             break;
                        case "b":
                             rdbtnHtB11.setSelected(true);
                             break;
                        case "c":
                             rdbtnHtC11.setSelected(true);
                             break;
                        default:
                             rdbtnHtA11.setSelected(false);
                             rdbtnHtB11.setSelected(false);
                             rdbtnHtC11.setSelected(false);
                             break;
                    }
                    cmbType012.getSelectionModel().select(Integer.parseInt(inqlistdata.get(pagecounter).getTblcinqindex12())); //Inquiry Type sSourceCD
                    if (Integer.parseInt(inqlistdata.get(pagecounter).getTblcinqindex12()) == 1) {
                         cmbOnstr13.getSelectionModel().select(Integer.parseInt(inqlistdata.get(pagecounter).getTblcinqindex13())); //Online Store sSourceNo
                    }
                    txtField14.setText(inqlistdata.get(pagecounter).getTblcinqindex14()); //sTestModl
                    txtField15.setText(inqlistdata.get(pagecounter).getTblcinqindex15()); //
                    txtField17.setText(inqlistdata.get(pagecounter).getTblcinqindex17()); // nReserved
                    txtField18.setText(inqlistdata.get(pagecounter).getTblcinqindex18()); // nRsrvTotl
                    txtField21.setText(inqlistdata.get(pagecounter).getTblcinqindex21()); // sApproved
                    comboBox24.getSelectionModel().select(Integer.parseInt(inqlistdata.get(pagecounter).getTblcinqindex24())); //Inquiry Status
                    txtField29.setText(inqlistdata.get(pagecounter).getTblcinqindex29()); // sCompnyNm
                    txtField30.setText(inqlistdata.get(pagecounter).getTblcinqindex30()); // sMobileNo
                    txtField31.setText(inqlistdata.get(pagecounter).getTblcinqindex31()); // sAccountx
                    txtField32.setText(inqlistdata.get(pagecounter).getTblcinqindex32()); // sEmailAdd
                    txtField33.setText(inqlistdata.get(pagecounter).getTblcinqindex33()); // sAddressx
                    loadTargetVehicle();
                    loadPromosOfferred();
                    
                    //Retrieve Requirements
                    oTransProcess.loadRequirements(inqlistdata.get(pagecounter).getTblcinqindex01());
                    //Load Table Requirements
                    loadInquiryRequirements();
                    //Retrieve Reservation
                    oTransProcess.loadReservation(inqlistdata.get(pagecounter).getTblcinqindex01());
                    //Load Table Reservation
                    loadInquiryAdvances();
                    
                    //Enable button based on selected inquiry
                    initBtnProcess(pnEditMode);
                    oldPnRow = pagecounter;  
               }
          
          } catch (SQLException e) {
                ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
          }

     }
     
    //Load Customer Inquiry Data
    public void loadCustomerInquiry(){
         try {
               txtField03.setText(CommonUtils.xsDateMedium((Date) oTrans.getMaster(3)));  //Inquiry Date               
               txtField07.setText((String) oTrans.getMaster(29)); //Custmer Name ***
               txtField29.setText((String) oTrans.getMaster(29)); //Company Name
               txtField04.setText((String) oTrans.getMaster(34)); //Sales Executive ID //Employee ID
               txtField30.setText((String) oTrans.getMaster(30)); //Contact No
               txtField32.setText((String) oTrans.getMaster(32)); //Email Address
               txtField33.setText((String) oTrans.getMaster(33)); //Client Address
               txtField31.setText((String) oTrans.getMaster(31)); //Social Media
               cmbType012.getSelectionModel().select(Integer.parseInt(oTrans.getMaster(12).toString())); //Inquiry Type
               if (Integer.parseInt(oTrans.getMaster(12).toString()) == 1) {
                    cmbOnstr13.getSelectionModel().select(Integer.parseInt(oTrans.getMaster(13).toString())); //Online Store
               }
               txtField10.setValue(strToDate(CommonUtils.xsDateShort((Date) oTrans.getMaster(10)))); //Target Release Date
               txtField09.setText((String) oTrans.getMaster(35)); //Agent ID
               txtField15.setText((String) oTrans.getMaster(15)); //Activity ID
               txtField02.setText((String) oTrans.getMaster(2)); //Branch Code
               comboBox24.getSelectionModel().select(Integer.parseInt(oTrans.getMaster(24).toString())); //Inquiry Status
               switch (oTrans.getMaster(5).toString()) {
                    case "0":
                        rdbtnNew05.setSelected(true);
                        break;
                   case "1":
                        rdbtnPro05.setSelected(true);
                        break;
                   default:
                        rdbtnNew05.setSelected(false);
                        rdbtnPro05.setSelected(false);
                        break;
               }
               
               switch (oTrans.getMaster(11).toString().toLowerCase()) {
                   case "a":
                        rdbtnHtA11.setSelected(true);
                        break;
                   case "b":
                        rdbtnHtB11.setSelected(true);
                        break;
                   case "c":
                        rdbtnHtC11.setSelected(true);
                        break;
                   default:
                        rdbtnHtA11.setSelected(false);
                        rdbtnHtB11.setSelected(false);
                        rdbtnHtC11.setSelected(false);
                        break;
               }
               textArea08.setText((String) oTrans.getMaster(8)); //Remarks
               txtField17.setText(oTrans.getMaster(17).toString()); //Slip No.
               txtField18.setText(oTrans.getMaster(18).toString()); //Rsv Amount
               txtField14.setText(oTrans.getMaster(14).toString()); //Rsv Amount
               
               
           } catch (SQLException e) {
                ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
           }
         
    }
     /*CUSTOMER INQUIRY: PRIORITY UNIT*/
    
     //Load Customer Inquiry Priority Unit
     public void loadTargetVehicle(){
          try {
               /*Populate table*/
               priorityunitdata.clear();
               for (lnCtr = 1; lnCtr <= oTrans.getVhclPrtyCount(); lnCtr++){
                    //Update priority row count
                    oTrans.setVhclPrty(lnCtr, "nPriority",lnCtr); //Handle Encoded Value   
                    //Add Priority unit to table display
                    priorityunitdata.add(new InquiryTablePriorityUnit(
                    oTrans.getVhclPrty(lnCtr,"nPriority").toString(), //Priority Unit
                    oTrans.getVhclPrty(lnCtr,"sDescript").toString() // Vehicle Description
                    ));
               }
                     
          } catch (SQLException e) {
               ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
          }

     }   
    // Load Customer Inquiry Target Vehicle Data
    public void initTargetVehicle(){
//          boolean lbShow = (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE);
//          if (lbShow) {
//               tblPriorityUnit.setEditable(true);
//          } else { 
//               tblPriorityUnit.setEditable(false);
//          }
          
          tblPriorityUnit.setEditable(true);
          trgvIndex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
          trgvIndex02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));

//          trgvIndex02.setCellFactory(TextFieldTableCell.forTableColumn()); // make the cells editable
//          // Set the event handler to store the edited value
//          trgvIndex02.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<InquiryTablePriorityUnit, String>>() {
//               @Override
//               public void handle(TableColumn.CellEditEvent<InquiryTablePriorityUnit, String> event) {
//                    // Code to handle edit event
//                    InquiryTablePriorityUnit detail = event.getRowValue();
//                    detail.setTblindex02(event.getNewValue());
//                    sValue = event.getNewValue();
//               }
//          });
//          trgvIndex02.setEditable(true);// make the column editable
//          
//          tblPriorityUnit.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
//               TablePosition<?, ?> focusedCell = tblPriorityUnit.getFocusModel().getFocusedCell();
//               //String columnId = focusedCell.getTableColumn().getId();
//               switch (event.getCode()){
//                    case F3:
//                    case ENTER:
//                         // Check if the focused cell is editable
//                         if (tblPriorityUnit.getItems().size() > 0) {
//                              if (focusedCell.getTableColumn().isEditable()) {
//                                   int lnIndex = Integer.parseInt(focusedCell.getTableColumn().getId().substring(9,11));
//                                   switch (lnIndex){
//                                        case 2: //Vehicle Description
//                                             try {
//                                             if ((oTrans.getVhclPrty(tblPriorityUnit.getSelectionModel().getSelectedIndex() + 1, 1).toString() != null) 
//                                             &&  (!oTrans.getVhclPrty(tblPriorityUnit.getSelectionModel().getSelectedIndex() + 1, 1).toString().trim().equals(""))){
//                                             } else { 
//                                                  if (lbShow) {
//                                                       if (oTrans.searchVhclPrty(tblPriorityUnit.getSelectionModel().getSelectedIndex() + 1,"",false)){
//                                                            loadTargetVehicle();
//                                                       } else {
//                                                            ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
//                                                       }
//                                                  }
//                                             }
//
//                                             } catch (SQLException ex) {
//                                                  Logger.getLogger(InquiryFormController.class.getName()).log(Level.SEVERE, null, ex);
//                                             }
//                                   break;
//                                   }
//                              }
//                         }
//                    break;
//               }
//          });
          tblPriorityUnit.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
               TableHeaderRow header = (TableHeaderRow) tblPriorityUnit.lookup("TableHeaderRow");
               header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    header.setReordering(false);
               });
          });

          tblPriorityUnit.setItems(priorityunitdata);
     }
    
    
     @FXML
     private void tblPriorityUnit_Clicked(MouseEvent event) {
          if(event.getClickCount() > 0){
               selectedTblRowIndex = tblPriorityUnit.getSelectionModel().getSelectedIndex();
               
//               //ShowMessageFX.Information(null, pxeModuleName, selectedTblRowIndex + ""); 
//               tblPriorityUnit.setOnKeyReleased((KeyEvent t)-> {
//                    try {
//                         KeyCode key = t.getCode();
//                         switch (key){
//                               case DOWN:
//                                   if (selectedTblRowIndex > 0) {
////                                       tblPriorityUnit.getSelectionModel().select(selectedIndex - 1);
////                                       tblPriorityUnit.scrollTo(selectedIndex - 1);
////                                        oTrans.setVehiclePriority(selectedTblRowIndex, false);
//                                   }
//                                   event.consume();
//                                   break;
//                               case UP:
//                                    if (selectedTblRowIndex < tblPriorityUnit.getItems().size() - 1) {
//                                        //tblPriorityUnit.getSelectionModel().select(selectedIndex + 1);
//                                        //tblPriorityUnit.scrollTo(selectedIndex + 1);
// //                                       oTrans.setVehiclePriority(selectedTblRowIndex, true);
//                                   }
//                                   event.consume();
//                                   break;
//                         }
//                    } catch (SQLException ex) {
//                         Logger.getLogger(InquiryFormController.class.getName()).log(Level.SEVERE, null, ex);
//                    } catch (org.json.simple.parser.ParseException ex) {
//                         Logger.getLogger(InquiryFormController.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//               });
          }
          
          
     }

     
     
     /*CUSTOMER INQUIRY: PROMOS OFFERED*/
     //Load Customer Inquiry Promo Offered
     public void loadPromosOfferred(){
          try {
               /*Populate table*/
               promosoffereddata.clear();
               for (lnCtr = 1; lnCtr <= oTrans.getInqPromoCount(); lnCtr++){
                    promosoffereddata.add(new InquiryTablePromoOffered(
                         oTrans.getInqPromo(lnCtr,"").toString(), //ROW
                         oTrans.getInqPromo(lnCtr,"").toString(), //Start Date
                         oTrans.getInqPromo(lnCtr,"").toString(), //End Date
                         oTrans.getInqPromo(lnCtr,"").toString() // Promo Offered
                    ));
               }
                    
          } catch (SQLException e) {
               ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
          }

     }  
    
     //Load Customer Inquiry PromosOffered
     public void initPromosOffered(){
          tblPromosOffered.setEditable(true);
          prmoIndex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
          prmoIndex02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
          prmoIndex03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
          prmoIndex04.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
          prmoIndex04.setCellFactory(TextFieldTableCell.forTableColumn()); // make the cells editable
          // Set the event handler to store the edited value
          prmoIndex04.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<InquiryTablePromoOffered, String>>() {
               @Override
               public void handle(TableColumn.CellEditEvent<InquiryTablePromoOffered, String> event) {
                    // Code to handle edit event
                    InquiryTablePromoOffered detail = event.getRowValue();
                    detail.setTblindex04(event.getNewValue());
                    sValue = event.getNewValue();
               }
          });
          prmoIndex04.setEditable(true);// make the column editable
          //unitIndex02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));

          tblPromosOffered.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
               TablePosition<?, ?> focusedCell = tblPromosOffered.getFocusModel().getFocusedCell();
               //String columnId = focusedCell.getTableColumn().getId();
               int lnIndex = Integer.parseInt(focusedCell.getTableColumn().getId().substring(9,11));
               switch (event.getCode()){
                    case F3:
                    case ENTER:
                         // Check if the focused cell is editable
                         if (focusedCell.getTableColumn().isEditable()) {
                              switch (lnIndex){
                                   case 2: //Vehicle Description
                                         // Code to execute when F3 is pressed on an editable column
                                         ShowMessageFX.Warning(getStage(), (tblPromosOffered.getSelectionModel().getSelectedIndex() + 1) + "","Warning", null);
                                         //System.out.println("F3 was pressed on an editable column");
                                        // System.out.println(tblPriorityUnit.getSelectionModel().getSelectedIndex());
//                                         try {
//                                             if (oTrans.searchInqPromo(tblPromosOffered.getSelectionModel().getSelectedIndex() + 1,sValue,false)){
//                                                 loadPromosOfferred();
//                                             } else
//                                                  ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
//                                         } catch (SQLException ex) {
//                                              Logger.getLogger(InquiryFormController.class.getName()).log(Level.SEVERE, null, ex);
//                                         }

                                        break;

                              }
                         }
                         break;
               }
          });
          tblPromosOffered.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
               TableHeaderRow header = (TableHeaderRow) tblPromosOffered.lookup("TableHeaderRow");
               header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    header.setReordering(false);
               });
          });
          
          tblPromosOffered.setItems(promosoffereddata);
     }
     
     /*INQUIRY: PROCESS*/
     // Load Inquiry Process Requirements
     public void loadInquiryRequirements(){
          try {
               /*Populate table*/
               inqrequirementsdata.clear();
               boolean bSubmitted = false;
               int lnCnt;
               String sRecby, sRecdate;
               sRecby = "";
               sRecdate = "";
               
               //if (cmbInqpr02.getValue() == null || cmbInqpr01.getValue() == null) {
               //} else {
                    if (oTransProcess.loadRequirementsSource( String.valueOf(cmbInqpr02.getSelectionModel().getSelectedIndex()), String.valueOf(cmbInqpr01.getSelectionModel().getSelectedIndex()))){
                         for (lnCtr = 1; lnCtr <= oTransProcess.getInqReqSrcCount() ; lnCtr++){
                              //to be continued tomorrow
                              cmbInqpr01.getSelectionModel().select(Integer.parseInt(oTransProcess.getInqReq(lnCtr, "cPayModex").toString())); //Inquiry Status
                              cmbInqpr02.getSelectionModel().select(Integer.parseInt(oTransProcess.getInqReq(lnCtr, "cCustGrpx").toString())); //Inquiry Status
                    
                              //Display selected Item
                              for (lnCnt = 1 ; lnCnt <= oTransProcess.getInqReqCount() ; lnCnt++){
                                   if (oTransProcess.getInqReq(lnCnt,"sRqrmtCde").toString().equals(oTransProcess.getInqReqSrc(lnCtr,"sRqrmtCde").toString())) {
                                        if (oTransProcess.getInqReq(lnCnt,"cSubmittd").toString().equals("1")){
                                             bSubmitted = true;
                                             sRecby = oTransProcess.getInqReq(lnCnt,"sCompnyNm").toString();
                                             sRecdate = CommonUtils.xsDateShort((Date) oTransProcess.getInqReq(lnCnt,"dReceived"));
                                        } else {
                                             bSubmitted = false;
                                             sRecby = "";
                                             sRecdate = "";
                                        }
                                        break;
                                   }
                              }
                              
                              //Add Display for Observable List Table View
                              inqrequirementsdata.add(new InquiryTableRequirements(
                                      bSubmitted //Check box
                                      ,oTransProcess.getInqReqSrc(lnCtr, "sDescript").toString().trim() //Requirements Description
                                      , sRecby //Received By
                                      , sRecdate //Received Date

                              ));
                              
                              //Clear Variables
                              bSubmitted = false;
                              sRecby = "";
                              sRecdate = "";
                         }
                         
                    }
               //}
          initInquiryRequirements();
          } catch (SQLException e) {
               ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
          }
     }
     
     public void initInquiryRequirements(){
          boolean lbShow = (pnEditMode == EditMode.READY || pnEditMode == EditMode.UPDATE );
          tblRequirementsInfo.setEditable(true);
          //tblRequirementsInfo.getSelectionModel().setCellSelectionEnabled(true);
          tblRequirementsInfo.setSelectionModel(null);
          rqrmIndex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
          rqrmIndex02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
          rqrmIndex03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
          rqrmIndex04.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
          
          rqrmIndex01.setCellFactory(CheckBoxTableCell.forTableColumn(new Callback<Integer, ObservableValue<Boolean>>() {
               @Override
               public ObservableValue<Boolean> call(Integer index) {
                    InquiryTableRequirements requirement = inqrequirementsdata.get(index);
                    BooleanProperty selected = requirement.selectedProperty();
                         selected.addListener((obs, oldValue, newValue) -> {
                              //requirement.setTblindex01(newValue);
                              System.out.println("tblindex01 is set to " + newValue + " at row " + (index + 1));
                              try {
                                   if (newValue) {
                                        if (lbShow) {
                                             oTransProcess.addRequirements();
                                             if (oTransProcess.searchSalesExec(oTransProcess.getInqReqCount(),"",false)){
                                                  oTransProcess.setInqReq(oTransProcess.getInqReqCount(),"cSubmittd",1);
                                                  oTransProcess.setInqReq(oTransProcess.getInqReqCount(),"sRqrmtCde",oTransProcess.getInqReqSrc(index + 1,"sRqrmtCde").toString());
                                                  oTransProcess.setInqReq(oTransProcess.getInqReqCount(),"dReceived",SQLUtil.toDate(CommonUtils.xsDateShort((Date) oApp.getServerDate()), SQLUtil.FORMAT_SHORT_DATE));
                                                  oTransProcess.setInqReq(oTransProcess.getInqReqCount(),"cPayModex", String.valueOf(cmbInqpr01.getSelectionModel().getSelectedIndex()));
                                                  oTransProcess.setInqReq(oTransProcess.getInqReqCount(),"cCustGrpx", String.valueOf(cmbInqpr02.getSelectionModel().getSelectedIndex()));
                                             } else {
                                                  oTransProcess.removeInqReq( oTransProcess.getInqReqSrc(index + 1,"sRqrmtCde").toString());
                                                  ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", "No selected Employee!");
                                             } 
                                        }
                                   } else {
                                        if (lbShow) {
                                             oTransProcess.removeInqReq( oTransProcess.getInqReqSrc(index + 1,"sRqrmtCde").toString());
                                        }
                                        
                                   }
                              } catch (SQLException ex) {
                                   Logger.getLogger(InquiryFormController.class.getName()).log(Level.SEVERE, null, ex);
                              }
                              
                         });
                    loadInquiryRequirements();
                    return selected;
               }
          }));
          
          
          tblRequirementsInfo.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
                    TableHeaderRow header = (TableHeaderRow) tblRequirementsInfo.lookup("TableHeaderRow");
                    header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                         header.setReordering(false);
                    });
          });
          
          tblRequirementsInfo.setItems(inqrequirementsdata); 
     }
     
     // Load Inquiry Process Advance Slip
     public void loadInquiryAdvances(){
         try {
               /*Populate table*/
               inqvsadata.clear();
               String sVsaType, sVsaStat;
               sVsaType = "";
               sVsaStat = "";
               for (lnCtr = 1; lnCtr <= oTransProcess.getReserveCount(); lnCtr++){
                    switch (oTransProcess.getInqRsv(lnCtr,12).toString()) {
                         case "0":
                              sVsaType = "Reservation";
                              break;
                         case "1":
                              sVsaType = "Deposit";
                              break; 
                         case "2":
                              sVsaType = "Safeguard Duty";
                              break;
                         default:
                              sVsaType = "";
                              break;
                    }
                    
                    switch (oTransProcess.getInqRsv(lnCtr,13).toString()) {
                         case "0":
                              sVsaStat = "For Approval";
                              break;
                         case "1":
                              sVsaStat = "Approved";
                              break; 
                         case "2":
                              sVsaStat = "Cancelled";
                              break;
                         default:
                              sVsaStat = "";
                              break;
                    }
                    //String formattedPrice = String.format("%.2f", oTransProcess.getInqRsv(lnCtr,5).toString());
                    
                    inqvsadata.add(new InquiryTableVehicleSalesAdvances(
                         false   
                         ,String.valueOf(lnCtr) //Row
                         , CommonUtils.xsDateShort((Date) oTransProcess.getInqRsv(lnCtr,2)) //Date
                         , sVsaType //Type
                         , (String) oTransProcess.getInqRsv(lnCtr,3) //VSA No
                         , String.format("%.2f", oTransProcess.getInqRsv(lnCtr,5)) //Amount
                         , sVsaStat //Status
                         , (String) oTransProcess.getInqRsv(lnCtr,6) // Remarks
                         , (String) oTransProcess.getInqRsv(lnCtr,13) // Approved By
                         , (String) oTransProcess.getInqRsv(lnCtr,14) // Approved Date
                         
                    ));
                    
                    //Clear Variables
                    sVsaType = "";
                    sVsaStat = "";
               }   
          } catch (SQLException e) {
               ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
          }
              
     }
    
     public void initInquiryAdvances(){
          boolean lbShow = (pnEditMode == EditMode.READY || pnEditMode == EditMode.UPDATE );
          tblAdvanceSlip.setEditable(true);
          //tblAdvanceSlip.getSelectionModel().setCellSelectionEnabled(true);
          tblAdvanceSlip.setSelectionModel(null);
          vsasIndex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
          vsasIndex02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
          vsasIndex03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
          vsasIndex04.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
          vsasIndex05.setCellValueFactory(new PropertyValueFactory<>("tblindex05"));
          vsasIndex06.setCellValueFactory(new PropertyValueFactory<>("tblindex06"));
          vsasIndex07.setCellValueFactory(new PropertyValueFactory<>("tblindex07"));
          
          vsasCheck01.setCellValueFactory(new PropertyValueFactory<>("tblcheck01"));
          vsasCheck01.setCellFactory(CheckBoxTableCell.forTableColumn(new Callback<Integer, ObservableValue<Boolean>>() {
               @Override
               public ObservableValue<Boolean> call(Integer index) {
                    InquiryTableVehicleSalesAdvances advances = inqvsadata.get(index);
                    BooleanProperty selected = advances.selectedProperty();
                         selected.addListener((obs, oldValue, newValue) -> {
                              System.out.println("tblcheck01 is set to " + newValue + " at row " + (index + 1));
                              if (newValue) {
                                   if (lbShow) {
                                        advances.setTblcheck01(newValue);
                                   }
                              } else {
                                   if (lbShow) {
                                        advances.setTblcheck01(newValue);
                                   }
                              }
                         });
                    return selected;
               }
          }));
          
          tblAdvanceSlip.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
                    TableHeaderRow header = (TableHeaderRow) tblAdvanceSlip.lookup("TableHeaderRow");
                    header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                         header.setReordering(false);
                    });
          });

          tblAdvanceSlip.setItems(inqvsadata);

     }

    //Load Bank Application History Data
     public void laodBankHistory(){
         initBankHistory();
     }
     //Load FollowUp Data
     public void loadFollowUp(){
         initFollowUp();
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
                         case 2: //
                         case 3: //
                         //case 4: //
                         //case 7: //
                         //case 9: //
                         case 12: //
                         case 13: //
                         case 14: //
                         case 15: //
                         case 17: //
                         case 18: //
                         case 21: //
                         case 24: //
                         case 29: //
                         case 30: //
                         case 31: //
                         case 32: //
                         case 33: //
                              oTrans.setMaster(lnIndex, lsValue); //Handle Encoded Value
                              break;
                         case 4: //
                              oTrans.setMaster(34, lsValue); //Handle Encoded Value
                              break;
                         case 9: //
                              oTrans.setMaster(35, lsValue); //Handle Encoded Value
                              break;
                         case 7: //
                              oTrans.setMaster(29, lsValue); //Handle Encoded Value
                              break;
                    }
                
            } else
               txtField.selectAll();
          } catch (SQLException ex) {
            Logger.getLogger(InquiryFormController.class.getName()).log(Level.SEVERE, null, ex);
          }
     };
     
     /*Set TextArea to Master Class*/
     final ChangeListener<? super Boolean> txtArea_Focus = (o,ov,nv)->{
          TextArea txtField = (TextArea)((ReadOnlyBooleanPropertyBase)o).getBean();
          int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
          String lsValue = txtField.getText();
          
          if (lsValue == null) return;
          try {
             if(!nv){ /*Lost Focus*/
               switch (lnIndex){
                   case 8:
                      oTrans.setMaster(lnIndex, lsValue); break;
               }
             } else
                 txtField.selectAll();
          } catch (SQLException e) {
             ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
             System.exit(1);
          }
     };
     
     /*Set ComboBox Value to Master Class*/ 
     @SuppressWarnings("ResultOfMethodCallIgnored")
     private boolean setSelection(){
          try {
               if (rdbtnHtA11.isSelected()){
                    oTrans.setMaster(11, "a");
               }else if (rdbtnHtB11.isSelected()){
                    oTrans.setMaster(11, "b");   
               } else if (rdbtnHtC11.isSelected()){
                    oTrans.setMaster(11, "c");  
               }
               
               if (!rdbtnNew05.isSelected() && !rdbtnPro05.isSelected()){
                   ShowMessageFX.Warning("No `Vehicle Category` selected.", pxeModuleName, "Please select `Vehicle Category` value.");
                   return false;
               }else {
                    if (rdbtnNew05.isSelected()){
                         oTrans.setMaster(5, 0);
                    } else if (rdbtnPro05.isSelected()){
                         oTrans.setMaster(5, 1);
                    }
               }
               
               if (cmbType012.getSelectionModel().getSelectedIndex() < 0){
                   ShowMessageFX.Warning("No `Inquiry Type` selected.", pxeModuleName, "Please select `Inquiry Type` value.");
                   cmbType012.requestFocus();
                   return false;
               }else 
                  oTrans.setMaster(12, String.valueOf(cmbType012.getSelectionModel().getSelectedIndex()));
               
               if (cmbType012.getSelectionModel().getSelectedIndex() == 1){
                    if (cmbOnstr13.getSelectionModel().getSelectedIndex() < 0){
                        ShowMessageFX.Warning("No `Online Store` selected.", pxeModuleName, "Please select `Online Store` value.");
                        cmbOnstr13.requestFocus();
                        return false;
                    }else 
                       oTrans.setMaster(13, String.valueOf(cmbOnstr13.getSelectionModel().getSelectedIndex()));
               } 
               
               if (cmbType012.getSelectionModel().getSelectedIndex() == 3){
                    if (txtField09.getText().equals("") || txtField09.getText() == null){
                        ShowMessageFX.Warning("No `Refferal Agent` selected.", pxeModuleName, "Please select `Refferal Agent` value.");
                        txtField09.requestFocus();
                        return false;
                    }
               } else if (cmbType012.getSelectionModel().getSelectedIndex() == 4 || cmbType012.getSelectionModel().getSelectedIndex() == 5){
                    if (txtField15.getText().equals("") || txtField15.getText() == null){
                        ShowMessageFX.Warning("No `Event` selected.", pxeModuleName, "Please select `Event` value.");
                        txtField15.requestFocus();
                        return false;
                    }
               }
               
               
               
               
                  
          } catch (SQLException ex) {
          ShowMessageFX.Warning(getStage(),ex.getMessage(), "Warning", null);
          }
          return true;
     }
     
     /*Convert Date to String*/
     private LocalDate strToDate(String val){
          DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
          LocalDate localDate = LocalDate.parse(val, date_formatter);
          return localDate;
     }
     
     /*Set Date Value to Master Class*/
     public void getDate(ActionEvent event) { 
          try {
               //System.out.println(SQLUtil.toDate(txtField10.getValue().toString(), SQLUtil.FORMAT_LONG_DATE));
               //SQLUtil.toDate(txtField10.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE)
               //System.out.println(txtField10.getValue().toString());
               //oTrans.setMaster(10, strToDate(txtField10.getValue().toString()) );
               oTrans.setMaster(10,SQLUtil.toDate(txtField10.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
//               String dateString = txtField10.getValue().toString();
//               SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//               Date date = dateFormat.parse(dateString);
//               oTrans.setMaster(10,SQLUtil.toDate((Date) date));
         }catch (SQLException ex) {
               Logger.getLogger(InquiryFormController.class.getName()).log(Level.SEVERE, null, ex);
         }
     }
     
     /*Enabling / Disabling Fields*/
     /*INQUIRY ENTRY MAIN*/
     private void initButton(int fnValue){
          pnRow = 0;
          /* NOTE:
               lbShow (FALSE)= invisible
               !lbShow (TRUE)= visible
          */
          boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
          
          /*Inquiry Entry*/
          txtField04.setDisable(!lbShow); // Sales Executive
          txtField07.setDisable(true); //Customer ID 
          txtField29.setDisable(true); //Company ID 
          textArea08.setDisable(!lbShow); //Remarks
          cmbType012.setDisable(!lbShow); //Inquiry Type
          txtField14.setDisable(!lbShow); //Test Model
          //txtField10.setValue(LocalDate.of(1900, Month.JANUARY, 1));//Target Release Date
          //Radio Button Toggle Group
          rdbtnHtA11.setDisable(!lbShow);
          rdbtnHtB11.setDisable(!lbShow);
          rdbtnHtC11.setDisable(!lbShow);
          rdbtnNew05.setDisable(!lbShow);
          rdbtnPro05.setDisable(!lbShow);
          
          switch (cmbType012.getSelectionModel().getSelectedIndex()) {
               case 1:
                    cmbOnstr13.setDisable(!lbShow);
                    txtField15.setText("");
                    txtField09.setText("");
                    break;
               case 3:
                    txtField09.setDisable(!lbShow);
                    txtField15.setText("");
                    cmbOnstr13.setValue(null);
                    break;
               case 4:
               case 5:
                    txtField15.setDisable(!lbShow);
                    txtField09.setText("");
                    cmbOnstr13.setValue(null);
                    break;
               default:
                    txtField15.setDisable(true); //Activity ID
                    txtField09.setDisable(true); //Agent ID
                    cmbOnstr13.setDisable(true); //Online Store             
          }
          
          btnAdd.setVisible(!lbShow);
          btnAdd.setManaged(!lbShow);
          btnSave.setVisible(lbShow);
          btnSave.setManaged(lbShow);         
          btnEdit.setVisible(false); 
          btnEdit.setManaged(false);
          btnConvertSales.setVisible(false); 
          btnConvertSales.setManaged(false);
          btnPrintRefund.setVisible(false); 
          btnPrintRefund.setManaged(false);
          btnLostSale.setVisible(false); 
          btnLostSale.setManaged(false);
          btnClear.setVisible(false);
          btnClear.setManaged(false);
          btnCancel.setVisible(lbShow);
          btnCancel.setManaged(lbShow);
          
          btnTargetVhclAdd.setVisible(lbShow);
          btnTargetVhclRemove.setVisible(lbShow);
          btnTargetVehicleUp.setVisible(lbShow);
          btnTargetVehicleDown.setVisible(lbShow);
          btnPromosAdd.setVisible(lbShow);
          btnPromosRemove.setVisible(lbShow);
          
          if (fnValue == EditMode.ADDNEW) {
               btnClear.setVisible(lbShow);
               btnClear.setManaged(lbShow);
               txtField07.setDisable(!lbShow); //Customer ID 
               txtField29.setDisable(!lbShow); //Company ID 
          }
          
          if (fnValue == EditMode.READY) { //show edit if user clicked save / browse
               btnEdit.setVisible(true); 
               btnEdit.setManaged(true);
               btnConvertSales.setVisible(true); 
               btnConvertSales.setManaged(true);
               btnPrintRefund.setVisible(true); 
               btnPrintRefund.setManaged(true);
               btnLostSale.setVisible(true); 
               btnLostSale.setManaged(true);   
          }
          
          boolean lbTab = (fnValue == EditMode.READY);
          
          //tabCustomerInquiry.setDisable(!lbTab);
          tabInquiryProcess.setDisable(!lbTab);
          tabBankHistory.setDisable(!lbTab);
          tabFollowingHistory.setDisable(!lbTab);
          
     }
     
     /*INQUIRY PROCESS*/
     private void initBtnProcess(int fnValue){
          pnRow = 0;
          /* NOTE:
               lbShow (FALSE)= invisible
               !lbShow (TRUE)= visible
          */
          boolean lbShow = (fnValue == EditMode.UPDATE);
          
          /*INQUIRY PROCESS*/
          cmbInqpr01.setDisable(lbShow);
          cmbInqpr02.setDisable(lbShow);
          txtField21.setDisable(lbShow);
          btnASadd.setVisible(lbShow);
          btnASremove.setVisible(lbShow);
          btnAScancel.setVisible(lbShow);
          btnASprintview.setVisible(lbShow);
          btnASprint.setVisible(lbShow);
          btnPymtcon.setVisible(lbShow);
          
          //Enable Button / textfield based on Inquiry Status
          switch (cmbType012.getSelectionModel().getSelectedIndex()) { // cTranStat
               case 0://For Follow up
                    cmbInqpr01.setDisable(false);
                    cmbInqpr02.setDisable(false);
                    txtField21.setDisable(false);
                    btnProcess.setVisible(true);
                    btnModify.setVisible(false);
                    btnModify.setManaged(false);
                    btnApply.setVisible(false);
                    btnApply.setManaged(false);
                    btnASadd.setVisible(true);
                    btnASremove.setVisible(true);
                    btnAScancel.setVisible(true);
                    btnASprintview.setVisible(true);
                    btnASprint.setVisible(true);
                    btnPymtcon.setVisible(true);
                    break;
               case 1: //On process
               case 3: //VSP
                    btnModify.setVisible(true);
                    btnProcess.setVisible(false);
                    btnProcess.setManaged(false);
                    btnApply.setVisible(lbShow);
                    btnApply.setManaged(lbShow);
                    break;
               case 2: //Lost Sale
               case 4: //Sold
               case 5: //Retired
               case 6: //Cancelled
                    btnProcess.setVisible(lbShow);
                    btnModify.setVisible(lbShow);
                    btnApply.setVisible(lbShow); 
                    break;
          }
        
     }
     
     /*Clear Class Value*/
     private void clearClassFields() {
          try {
               //Class Master
               for (lnCtr = 1; lnCtr <= 35; lnCtr++){
                    switch (lnCtr) {
                         case 2: //
                         //case 4: //
                         //case 7: //
                         //case 9: //
                         case 14: //
                         case 15: //
                         case 21: //
                         case 29: //
                         case 30: //
                         case 31: //
                         case 32: //
                         case 33: //
                              oTrans.setMaster(lnCtr, ""); //Handle Encoded Value
                              break;
                         case 4: //
                              oTrans.setMaster(34, ""); //Handle Encoded Value
                              break;
                         case 9: //
                              oTrans.setMaster(35, ""); //Handle Encoded Value
                              break;
                         case 7: //
                              oTrans.setMaster(29, ""); //Handle Encoded Value
                              break;
                         case 11: 
                              oTrans.setMaster(lnCtr, "a"); //Handle Encoded Value
                              break;
                         case 10:
                              //oTrans.setMaster(10,SQLUtil.toDate("1/1/1900", SQLUtil.FORMAT_SHORT_DATE));
                              oTrans.setMaster(lnCtr,LocalDate.of(1900, Month.JANUARY, 1));
                              break;
                         case 5:
                         case 12:
                         case 13:
                              oTrans.setMaster(lnCtr, "0");
                              break; 
                    }   
               }
               
               //Class Priority Unit
               do oTrans.removeTargetVehicle( oTrans.getVhclPrtyCount());
               while  (oTrans.getVhclPrtyCount() != 0);
               //Class Promo Offered
               do oTrans.removeInqPromo( oTrans.getInqPromoCount());
               while  (oTrans.getInqPromoCount() != 0);
               
               
               
          }catch (SQLException ex) {
               Logger.getLogger(InquiryFormController.class.getName()).log(Level.SEVERE, null, ex);
          }
     }
     
     
     //Method for clearing Fields
     public void clearFields(){
           pnRow = 0;
           selectedTblRowIndex= 0;

           /*Inquiry*/
           txtField02.clear(); //Branch Code 
           txtField03.clear();//Inqiury Date
           txtField04.clear(); // Sales Executive
           txtField07.clear(); //Customer ID 
           txtField29.clear(); //Company ID 
           txtField30.clear(); //Contact Number
           txtField31.clear(); //Social Media
           txtField32.clear(); //Email
           txtField33.clear(); //Client Address
           txtField09.clear(); //Agent ID
           textArea08.clear(); //Remarks
           comboBox24.setValue(null);  //Inquiry Status
           txtField18.clear(); //Reserve Amount
           txtField17.clear(); //Reserved
           cmbOnstr13.setValue(null); //Online Store
           cmbType012.setValue(null); //Inquiry Type
           txtField15.clear(); //Activity ID
           txtField14.clear(); //Test Model
           //txtField10.setValue(LocalDate.of(1900, Month.JANUARY, 1)); //birthdate
           
           hotCategory.selectToggle(null);//Radio Button Toggle Group
           targetVehicle.selectToggle(null);//Radio Button Toggle Group
           promosoffereddata.clear();
           priorityunitdata.clear();

           /*Inquiry Process*/
           txtField21.clear(); //Approved By
         }

     }
