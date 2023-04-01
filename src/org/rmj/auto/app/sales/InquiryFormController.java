/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.auto.app.sales;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.auto.app.views.ScreenInterface;
import org.rmj.auto.app.views.unloadForm;

/**
 * FXML Controller class
 * 
 * @author John Dave
 * 
 */
public class InquiryFormController implements Initializable, ScreenInterface{
    
    private GRider oApp;
    
    unloadForm unload = new unloadForm(); //Object for closing form
  
    private final String pxeModuleName = "Inquiry Information"; //Form Title
   
    
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
    //TextFields
    @FXML
    private TextField txtFieldInquiryDate; // Inquiry Date
    @FXML
    private TextField txtFieldSalesExecutive; // Sales Executive
    @FXML
    private TextField txtFieldCustomerName; // Customer Name
    @FXML
    private TextField txtFieldCompanyName; // Company Name
    @FXML
    private TextField txtFieldPreferedMail; // Preferred Mailing Address
    @FXML
    private TextField txtFieldContactNumber; // Contact Number
    @FXML 
    private TextField txtFieldEmailAddress; // Email Addresse
    @FXML
    private TextField txtFieldSocialMedia; // Social Media
    //Table View
    @FXML
    private TableView<?> tblInquiry; // Table View Inquiry 
    //Tabs
    @FXML
    private Tab tabCustomerInquiry; // Customer Inquiry Tab
    @FXML
    private Tab tabInquiryProcess; // Inquiry Process Tab
    @FXML
    private Tab tabBankHistory; // Bank History Tab
    @FXML
    private Tab tabFollowingHistory; // Following History Tab
     
    //Costumer Inquiry Elements
 
    //Text Fields   
    @FXML
    private TextField txtFieldRefAgent; // Ref Agent
    @FXML
    private TextField textFieldTargetReleaseDate; //Target Release Date
    @FXML
    private TextField txtFieldCustomerInquiryModel; // Customer Inquiry Model
    @FXML
    private TextField txtFieldBranch; // Branch
    @FXML
    private TextField txtFieldSubCompany; // Sub Company
    @FXML
    private TextArea txtAreaCIRemarks; // Customer Inquiry Remarks
    @FXML
    private TextField txtFieldCIStatus; // Customer Inquiry Status
    @FXML
    private TextField txtFieldCICdot; // C.
    @FXML
    private TextField txtFieldCIRSV; // RSV/DEV Slip No.
    @FXML
    private TextField textFieldRSVAmount; //RSV/DEV Slip No. Amount
    @FXML
    private TextField textFieldRefundSlipNo; //Refund Slip No.
    @FXML
    private TextField textFieldRefundSlipAmount; //Refund Slip No. Amount
    //Combo Box
    @FXML
    private ComboBox comboInquiryType; // Inquiry Type
    @FXML
    private ComboBox comboOnlineStore; // Online Store
    @FXML
    private ComboBox comboEvent; // Event
    //Combo Box Value
    ObservableList<String> cInquiryType = FXCollections.observableArrayList("TYPE 1", "TYPE 2", "TPYE 3"); //Inquiry Type values
    ObservableList<String> cEvent = FXCollections.observableArrayList("EVENT 1", "EVENT 2", "EVENT 3"); // Event values
    ObservableList<String> cOnlineStore = FXCollections.observableArrayList("STORE 1", "STORE 2", "STORE 3"); // STORE values
    //Radio Toogle Group
    @FXML
    private ToggleGroup category; //Toggle Radio Button category 
    @FXML
    private ToggleGroup testDrive;//Toggle Radio Button Test Drive
    @FXML
    private ToggleGroup targetVehicle;//Toggle Radio Button Target Vehicle 
    //Table View
    @FXML
    private TableView<?> tblTargetVehicle; // Table View Target Vehicle
    @FXML
    private TableView<?> tblPromosOffered; // Table View Promos Offered
    //Tables
    
    //Buttons
    @FXML
    private Button btnTargetVehicleAdd; // Add Target Vehicle
    @FXML
    private Button btnTargetVehicleRemove; // Remove Target Vehicle
    @FXML
    private Button btnTargetVehicleUp; // Move Up Target Vehicle Up
    @FXML
    private Button btnTargetVehicleDown; // Move Down Target Vehicle
    @FXML
    private Button btnPromosAdd; // Add Promo Offered
    @FXML
    private Button btnPromosRemove; //Remove Promo Offered
    @FXML
    private Button btnPromosUp; // Move Up Promo Offered
    @FXML
    private Button btnPromosdDown; // Move Down Promo Offered
    
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
    @FXML
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
    private TableView<?> tblBankHistory; //Table VieW Bank Application History
    //Tables
    
    //Follow Up Elements
    
    //Buttons
    @FXML
    private Button btnFollowUp; // FollowUp
    //Table View
    @FXML
    private TableView<?> tblFollowHistory; //Table View Follow Up
    //Tables
    
   
    
  

    /**
     * Initializes the controller class.
    **/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        //ComboBox set Items
        comboInquiryType.setItems(cInquiryType);
        comboEvent.setItems(cEvent);
        comboOnlineStore.setItems(cOnlineStore);
        comboIPModeofPayment.setItems(cModeOfPayment);
        comboIPCustomerType.setItems(cCustomerType);
        
        
        //Button SetOnAction using cmdButton_Click() method
        btnClose.setOnAction(this::cmdButton_Click);
        btnAdd.setOnAction(this::cmdButton_Click);
        btnSave.setOnAction(this::cmdButton_Click);
        btnClear.setOnAction(this::cmdButton_Click);
        btnEdit.setOnAction(this::cmdButton_Click);
        btnConvertSales.setOnAction(this::cmdButton_Click);
        btnPrintRefund.setOnAction(this::cmdButton_Click);
        btnLostSale.setOnAction(this::cmdButton_Click); 
        
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
                ShowMessageFX.Warning(null, "Warning", "You click add button!"); 
                break;
            case "btnEdit":
                 ShowMessageFX.Warning(null, "Warning", "You click edit button!"); 
                break;
            case "btnSave":
                 ShowMessageFX.Warning(null, "Warning", "You click save button!"); 
                break;
            case "btnClear":
                 ShowMessageFX.Warning(null, "Warning", "You click clear button!"); 
                break;
            case "btnConvertSales":
                 ShowMessageFX.Warning(null, "Warning", "You click convert to sales button"); 
                break;
            case "btnPrintRefund":
                 ShowMessageFX.Warning(null, "Warning", "You click print refund button"); 
                break;
            case "btnLostSale":
                 ShowMessageFX.Warning(null, "Warning", "You click lost sale button"); 
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
    //Load Customer Profile Data
    public void loadCustomerProfile(){
            
    }
    //Load Customer Inquiry Data
    public void loadCustomerInquiry(){
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
    //Method for clearing Fields
    public void clearFields(){
                
            //Clear TextFields
            //General
            txtFieldInquiryDate.clear();
            txtFieldSalesExecutive.clear();
            txtFieldCustomerName.clear();
            txtFieldCompanyName.clear();
            txtFieldPreferedMail.clear();
            txtFieldContactNumber.clear();
            txtFieldEmailAddress.clear();
            txtFieldSocialMedia.clear();
            //Customer Inquiry
            txtFieldRefAgent.clear();
            textFieldTargetReleaseDate.clear();
            txtFieldCustomerInquiryModel.clear();
            txtFieldBranch.clear();
            txtFieldSubCompany.clear();
            txtAreaCIRemarks.clear();
            txtFieldCIStatus.clear();
            txtFieldCICdot.clear();
            txtFieldCIRSV.clear();
            textFieldRSVAmount.clear();
            textFieldRefundSlipNo.clear();
            textFieldRefundSlipAmount.clear();
            //InquiryProcess
            txtFieldIPCsNo.clear();
            txtFieldIPModel.clear();
            txtFieldPlateNo.clear();
            txtFieldPaymentConversion.clear();
                 
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
            testDrive.selectToggle(null);
            targetVehicle.selectToggle(null);

        }
    }
