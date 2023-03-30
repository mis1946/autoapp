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
    private Button btnClose;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnConvertSales;
    @FXML
    private Button btnPrintRefund;
    @FXML
    private Button btnLostSale;
    //TextFields
    @FXML
    private TextField txtFieldInquiryDate;
    @FXML
    private TextField txtFieldSalesExecutive;
    @FXML
    private TextField txtFieldCustomerName;
    @FXML
    private TextField txtFieldCompanyName;
    @FXML
    private TextField txtFieldPrefferedMail;
    @FXML
    private TextField txtFieldContactNumber;
    @FXML
    private TextField txtFieldEmailAddress;
    @FXML
    private TextField txtFieldSocialMedia;
    //Table View
    @FXML
    private TableView<?> tblInquiry;
    //Tabs
    @FXML
    private Tab tabCustomerInquiry;
    @FXML
    private Tab tabInquiryProcess;
    @FXML
    private Tab tabBankHistory;
    @FXML
    private Tab tabFollowingHistory;
    
    //Costumer Inquiry Elements
 
    //Text Fields   
    @FXML
    private TextField txtFieldRefAgent;
    @FXML
    private TextField textFieldTargetReleaseDate;
    @FXML
    private TextField txtFieldCustomerInquiryModel;
    @FXML
    private TextField txtFieldBranch;
    @FXML
    private TextField txtFieldSubCompany;
    @FXML
    private TextArea txtAreaCIRemarks;
    @FXML
    private TextField txtFieldCIStatus;
    @FXML
    private TextField txtFieldCICdot;
    @FXML
    private TextField txtFieldCIRSV;
    @FXML
    private TextField textFieldRSVAmount;
    @FXML
    private TextField textFieldRefundSlipNo;
    @FXML
    private TextField textFieldRefundSlipAmount;
    //Combo Box
    @FXML
    private ComboBox comboInquiryType;
    @FXML
    private ComboBox<?> comboOnlineStore;
    @FXML
    private ComboBox comboEvent;
    //Combo Box Value
    ObservableList<String> cInquiryType = FXCollections.observableArrayList("TYPE 1", "TYPE 2", "TPYE 3");
    ObservableList<String> cEvent = FXCollections.observableArrayList("EVENT 1", "EVENT 2", "EVENT 3");
    //Radio Toogle Group
    @FXML
    private ToggleGroup category;
    @FXML
    private ToggleGroup testDrive;
    @FXML
    private ToggleGroup targetVehicle;
    //Table View
    @FXML
    private TableView<?> tblTargetVehicle;
    @FXML
    private TableView<?> tblPromosOFfered;
    //Tables
    
    //Buttons
    @FXML
    private Button btnTargetVehicleAdd;
    @FXML
    private Button btnTargetVehicleRemove;
    @FXML
    private Button btnTargetVehicleUp;
    @FXML
    private Button btnTargetVehicleDown;
    @FXML
    private Button btnPromosAdd;
    @FXML
    private Button btnPromosRemove;
    @FXML
    private Button btnPromosUp;
    @FXML
    private Button btnPromosdDown;
    
    //Inquiry Process Elements
    
    //Buttons
    @FXML
    private Button btnASadd;
    @FXML
    private Button btnASremove;
    @FXML
    private Button btnASprint;
    @FXML
    private Button btnASprintview;
    @FXML
    private Button btnAScancel;
    @FXML
    private Button btnProcess;
    @FXML
    private Button btnModify;
    @FXML
    private Button btnApply;
    
    //TextFields
    @FXML
    private TextField txtFieldIPCsNo;
    @FXML
    private TextField txtFieldIPModel;
    @FXML
    private TextField txtFieldPlateNo;
    @FXML
    private TextField txtFieldPaymentConversion;
    //Table View
    @FXML
    private TableView<?> tblRequirementsInfo;
    @FXML
    private TableView<?> tblAdvanceSlip;
    //Combo Box
    @FXML
    private ComboBox comboIPModeofPayment;
    @FXML
    private ComboBox comboIPCustomerType;
    @FXML
    private ComboBox comboIPProcessApprovedBy;
    //Combo Box Value  
    ObservableList<String> cModeOfPayment = FXCollections.observableArrayList("PAYMENT 1", "PAYMENT 2", "PAYMENT 3");
    ObservableList<String> cCustomerType = FXCollections.observableArrayList("cTYPE 1", "cTYPE 2", "cTYPE 3");
   
    //Bank Application History Elements
    //Buttons
    @FXML
    private Button btnBPHnew;
    @FXML
    private Button btnBPHupdate;
    @FXML
    private Button btnBPHview;
    @FXML
    private Button btnBPHcancel;

    @FXML
    private TableView<?> tblBankHistory;
    //Tables
    
    //Follow Up Elements
    
    //Buttons
    @FXML
    private Button btnFollowUp;
    //Table View
    @FXML
    private TableView<?> tblFollowHistory;
    //Tables
    
   
    
  

    /**
     * Initializes the controller class.
    **/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
     
        comboInquiryType.setItems(cInquiryType);
        comboEvent.setItems(cEvent);
        comboIPModeofPayment.setItems(cModeOfPayment);
        comboIPCustomerType.setItems(cCustomerType);
        btnClose.setOnAction(this::cmdButton_Click);
    }    
    
    @Override
    public void setGRider(GRider foValue) {
             oApp = foValue;
    }
   
  private void cmdButton_Click(ActionEvent event) {
          String lsButton = ((Button)event.getSource()).getId();
          switch(lsButton){
            case "btnAdd":
                break;
            case "btnEdit":
                break;
            case "btnSave":
                break;
            case "btnClear":
                break;
            case "btnPrintClear":
                break;
            case "btnConvertSales":
                break;
            case "btnRefund":
                break;
            case "btnPrintRefund":
                break;
            case "btnLostSale":
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
  
  
        //Clear Fields
        public void clearFields(){
                
                 //Clear TextFields
                 //General
                 txtFieldInquiryDate.clear();
                 txtFieldSalesExecutive.clear();
                 txtFieldCustomerName.clear();
                 txtFieldCompanyName.clear();
                 txtFieldPrefferedMail.clear();
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
