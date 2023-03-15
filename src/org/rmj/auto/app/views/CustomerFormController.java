/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.views;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.auto.clients.base.ClientAddress;
import org.rmj.auto.clients.base.ClientMaster;
import org.rmj.auto.clients.base.ClientMobile;
import org.rmj.auto.clients.base.ClientEMail;
import org.rmj.auto.clients.base.ClientSocMed;

/**
 * FXML Controller class
 *
 * @author Arsiela
 * DATE CREATED 03-01-2023  
 */
public class CustomerFormController implements Initializable, ScreenInterface {
     private GRider oApp;
     private ClientMaster oTrans;
     private MasterCallback oListener;
     private MasterCallback oListener2;
     private ClientAddress oTransAddress;
     private ClientMobile oTransMobile;
     private ClientEMail oTransEmail;
     private ClientSocMed oTransSocMed;
     unloadForm unload = new unloadForm(); //Used in Close Button
     private final String pxeModuleName = "Customer Information"; //Form Title
     private int pnEditMode;//Modifying fields
     private double xOffset = 0; //For Opening Scene
     private double yOffset = 0; //For Opening Scene
     private int pnRow = -1;
     private int lnCtr;
     private int tbl_row = 0;
     private int iTabIndex = 0; //Set tab index
     private int iClientType;
     
     /*populate tables Address, Mobile, Email and Social Media*/
     private ObservableList<TableClientAddress> addressdata = FXCollections.observableArrayList();
     private ObservableList<TableClientMobile> mobiledata = FXCollections.observableArrayList();
     private ObservableList<TableClientEmail> emaildata = FXCollections.observableArrayList();
     private ObservableList<TableClientSocialMedia> socialmediadata = FXCollections.observableArrayList();

     /*populate comboxes client_master*/
     ObservableList<String> cCvlStat = FXCollections.observableArrayList("Single", "Married", "Divorced", "Separated", "Widowed");
     ObservableList<String> cGender = FXCollections.observableArrayList("Male", "Female", "LGBTQ");
     ObservableList<String> cCusttype = FXCollections.observableArrayList("Client", "Company", "Institutional");
     ObservableList<String> cTitle = FXCollections.observableArrayList("Mr.", "Miss", "Mrs.");
     
     @FXML
     private TextField txtField01; //client id
     @FXML
     private TextField txtField02; //last name
     @FXML
     private TextField txtField03; //first name
     @FXML
     private TextField txtField04; //mid name
     @FXML
     private TextField txtField06; //suffix 
     @FXML
     private TextField txtField05;  //maiden name
     @FXML
     private DatePicker txtField11; //bdate
     @FXML
     private TextField txtField12; // birth plce
     @FXML
     private TextField txtField16; //company name
     @FXML
     private TextField txtField10; //citizenship
     @FXML
     private ComboBox comboBox18; //Client type
     @FXML
     private ComboBox comboBox08; //Gender
     @FXML
     private ComboBox comboBox09; //Civil Stat
     @FXML
     private ComboBox comboBox07; //Title
     @FXML
     private TextField txtField14; //LTO NO
     @FXML
     private TextField txtField13; //TIN NO
     @FXML
     private TextArea textArea15; //Remarks
     @FXML
     private TextField txtField29; //Business Style
     @FXML
     private TextField txtField25; // Spouse
     @FXML
     private TextField txtField26; // Client Search
     
     /*Address*/
     @FXML
     private TextField txtField03Addr; //House No
     @FXML
     private TextField txtField04Addr; //Street / Address
     @FXML
     private TextField txtField05Addr; // Town
     @FXML
     private TextField txtField06Addr; // Brgy
     @FXML
     private TextField txtField07Addr; //Zip code
     @FXML
     private RadioButton radiobtn18AddY; //Active status
     @FXML
     private RadioButton radiobtn18AddN; //Active status
     @FXML
     private CheckBox checkBox14Addr; //Primary
     @FXML
     private CheckBox checkBox17Addr; //Current
     @FXML
     private CheckBox checkBox12Addr; //Office
     @FXML
     private CheckBox checkBox13Addr; //Provincial
     @FXML
     private TextArea textArea11Addr; // Address Remarks
     /*Contact No*/
     ObservableList<String> cOwnCont = FXCollections.observableArrayList("Personal", "Office", "Others");
     ObservableList<String> cTypCont = FXCollections.observableArrayList("Mobile", "Telephone", "Fax");
     @FXML
     private ComboBox comboBox05Cont; // Contact Ownership
     @FXML
     private ComboBox comboBox04Cont; // Mobile Type
     @FXML
     private TextField txtField03Cont;  //Mobile Number
     @FXML
     private RadioButton radiobtn14CntY; // Contact Active Status
     @FXML
     private RadioButton radiobtn14CntN; // Contact Active Status
     @FXML
     private RadioButton radiobtn11CntY; // Contact Primary
     @FXML
     private RadioButton radiobtn11CntN; // Contact Primary
     @FXML
     private TextArea textArea13Cont; // Contact Remarks
     /*Email Address*/
     ObservableList<String> cOwnEmAd = FXCollections.observableArrayList("Personal", "Office", "Others");
     @FXML
     private ComboBox comboBox04EmAd; // Email Ownership
     @FXML
     private RadioButton radiobtn06EmaY; // Email Active status
     @FXML
     private RadioButton radiobtn06EmaN; // Email Active status
     @FXML
     private RadioButton radiobtn05EmaY; // Email Primary
     @FXML
     private RadioButton radiobtn05EmaN; // Email Primary
     @FXML
     private TextField txtField03EmAd; // Email Address
     /*Social Media*/
     ObservableList<String> cSocType = FXCollections.observableArrayList("Facebook", "Whatsup", "Intagram", "Tiktok", "Twitter");
     @FXML
     private RadioButton radiobtn05SocN; // SocMed Active status
     @FXML
     private RadioButton radiobtn05SocY; // SocMed Active status
     @FXML
     private TextField txtField03Socm; // SocMed Account
     @FXML
     private ComboBox comboBox04Socm; // SocMed Type

     /*Buttons*/
     @FXML
     private Button btnTabAdd;
     @FXML
     private Button btnTabRem; 
     @FXML
     private Button btnTabUpd;
     @FXML
     private Button btnAdd;
     @FXML
     private Button btnEdit;
     @FXML
     private Button btnSave;
     @FXML
     private Button btnBrowse;
     @FXML
     private Button btnClose;
     @FXML
     private AnchorPane AnchorMain;
     @FXML
     private TabPane tabPCustCont;
     @FXML
     private Tab tabAddrInf;
     @FXML
     private Tab tabContNo;
     @FXML
     private Tab tabEmail;
     @FXML
     private Tab tabSocMed;
     @FXML
     private TableView tblAddress;
     @FXML
     private TableView tblContact;
     @FXML
     private TableView tblEmail;
     @FXML
     private TableView tblSocMed;
     @FXML
     private TableColumn addrindex01;
     @FXML
     private TableColumn addrindex02;
     @FXML
     private TableColumn addrindex03;
     @FXML
     private TableColumn addrindex04;
     @FXML
     private TableColumn addrindex05;
     @FXML
     private TableColumn contindex01;
     @FXML
     private TableColumn contindex02;
     @FXML
     private TableColumn contindex03;
     @FXML
     private TableColumn emadindex01;
     @FXML
     private TableColumn emadindex02;
     @FXML
     private TableColumn emadindex03;
     @FXML
     private TableColumn socmindex01;
     @FXML
     private TableColumn socmindex02;
     @FXML
     private TableColumn socmindex03;
     

     private Stage getStage(){
          return (Stage) txtField01.getScene().getWindow();
     }
     /**
      * Initializes the controller class.
      */
     @Override
     public void initialize(URL url, ResourceBundle rb) {
          oListener = (int fnIndex, Object foValue) -> {
               System.out.println("Set Class Value "  + fnIndex + "-->" + foValue);
          };
          
          oTrans = new ClientMaster(oApp, oApp.getBranchCode(), true); //Initialize ClientMaster
          oTrans.setCallback(oListener);
          oTrans.setWithUI(true);

          oTransAddress = new ClientAddress(oApp, oApp.getBranchCode(), false); //Initialize ClientAddress
          oTransAddress.setCallback(oListener);
          oTransAddress.setWithUI(true);
          initAddress();
          
          oTransMobile = new ClientMobile(oApp, oApp.getBranchCode(), false); //Initialize ClientMobile
          oTransMobile.setCallback(oListener);
          oTransMobile.setWithUI(true);
          initContact();
          
          oTransEmail = new ClientEMail(oApp, oApp.getBranchCode(), false); //Initialize ClientMobile
          oTransEmail.setCallback(oListener);
          oTransEmail.setWithUI(true);
          initEmail();
          
          oTransSocMed = new ClientSocMed(oApp, oApp.getBranchCode(), false); //Initialize ClientMobile
          oTransSocMed.setCallback(oListener);
          oTransSocMed.setWithUI(true);
          initSocialMedia();
          
          /*Set Focus to set Value to Class*/
          txtField01.focusedProperty().addListener(txtField_Focus); 
          txtField02.focusedProperty().addListener(txtField_Focus);
          txtField03.focusedProperty().addListener(txtField_Focus);
          txtField04.focusedProperty().addListener(txtField_Focus);
          txtField05.focusedProperty().addListener(txtField_Focus);
          txtField06.focusedProperty().addListener(txtField_Focus);
          txtField10.focusedProperty().addListener(txtField_Focus);
          txtField12.focusedProperty().addListener(txtField_Focus);
          txtField13.focusedProperty().addListener(txtField_Focus);
          txtField14.focusedProperty().addListener(txtField_Focus);
          txtField16.focusedProperty().addListener(txtField_Focus);
          txtField25.focusedProperty().addListener(txtField_Focus);
          textArea15.focusedProperty().addListener(txtArea_Focus);
          
          CommonUtils.addTextLimiter(txtField06, 4); // Suffix
          CommonUtils.addTextLimiter(txtField13, 15); // TIN
          CommonUtils.addTextLimiter(txtField14, 15); // LTO
          CommonUtils.addTextLimiter(txtField03Addr, 5); //HOUSE NO
          CommonUtils.addTextLimiter(txtField03Cont, 12); //CONTACT NO
          CommonUtils.isValidEmail(txtField03EmAd.toString());
          
          Pattern pattern = Pattern.compile("[0-9]*");
          txtField03Cont.setTextFormatter(new InputTextFormatter(pattern)); //Mobile No
          txtField03Addr.setTextFormatter(new InputTextFormatter(pattern)); //House No
          
          /*populate combobox*/
          comboBox09.setItems(cCvlStat);
          comboBox08.setItems(cGender);
          comboBox18.setItems(cCusttype);
          comboBox07.setItems(cTitle);
          comboBox04EmAd.setItems(cOwnEmAd); // Email Ownership 
          comboBox04Socm.setItems(cSocType); // SocMed Type 
          comboBox05Cont.setItems(cOwnCont); // Contact Ownership 
          comboBox04Cont.setItems(cTypCont); // Mobile Type 

          //txtField11.setDayCellFactory(callB); //BDate
          
          txtField11.setOnAction(this::getDate); 
          
          txtField05Addr.setOnKeyPressed(this::txtField_KeyPressed); //Town
          txtField06Addr.setOnKeyPressed(this::txtField_KeyPressed); //Brgy
          txtField12.setOnKeyPressed(this::txtField_KeyPressed); //Birth Place
          txtField10.setOnKeyPressed(this::txtField_KeyPressed); //Citizenship
          txtField26.setOnKeyPressed(this::txtField_KeyPressed); //Customer Name Search
          txtField01.setOnKeyPressed(this::txtField_KeyPressed); //Customer ID Search
          txtField25.setOnKeyPressed(this::txtField_KeyPressed); //Spouse
          //txtField13.focusedProperty().addListener(txtField_Focus);
          
          /*Radio Button Click Event Y / N*/
          /*client_address*/
          radiobtn18AddY.setOnAction(this::cmdRadioButton_Click); 
          radiobtn18AddN.setOnAction(this::cmdRadioButton_Click); 
          /*client_mobile*/
          radiobtn14CntY.setOnAction(this::cmdRadioButton_Click); 
          radiobtn14CntN.setOnAction(this::cmdRadioButton_Click); 
          radiobtn11CntY.setOnAction(this::cmdRadioButton_Click); 
          radiobtn11CntN.setOnAction(this::cmdRadioButton_Click); 
          /*client_email_address*/
          radiobtn05EmaY.setOnAction(this::cmdRadioButton_Click); 
          radiobtn05EmaN.setOnAction(this::cmdRadioButton_Click); 
          radiobtn06EmaY.setOnAction(this::cmdRadioButton_Click); 
          radiobtn06EmaN.setOnAction(this::cmdRadioButton_Click); 
          /*client_social_media*/
          radiobtn05SocY.setOnAction(this::cmdRadioButton_Click); 
          radiobtn05SocN.setOnAction(this::cmdRadioButton_Click); 
          
          /*Check box Clicked Event*/
          /*client_address*/
          checkBox14Addr.setOnAction(this::cmdCheckBox_Click); 
          checkBox17Addr.setOnAction(this::cmdCheckBox_Click);
          checkBox12Addr.setOnAction(this::cmdCheckBox_Click);
          checkBox13Addr.setOnAction(this::cmdCheckBox_Click);
     
          //Button Click Event
          btnTabAdd.setOnAction(this::cmdButton_Click); 
          btnTabRem.setOnAction(this::cmdButton_Click);
          btnTabUpd.setOnAction(this::cmdButton_Click);
          btnAdd.setOnAction(this::cmdButton_Click);
          btnEdit.setOnAction(this::cmdButton_Click); 
          btnSave.setOnAction(this::cmdButton_Click); 
          btnClose.setOnAction(this::cmdButton_Click); 
          btnBrowse.setOnAction(this::cmdButton_Click);
          
          // Register the event handler for the selection change event
          comboBox18.setOnAction(e -> {
              comboChange();
          });
          
          //Tab Process
          tabPCustCont.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
               @Override
               public void changed(ObservableValue<? extends Tab> observable, Tab oldTab, Tab newTab) {
                   pnRow = 0;
               }  
          });
          
          /*Clear Fields*/
          clearFields();
          
          pnEditMode = EditMode.UNKNOWN;
          initButton(pnEditMode); 
          
     }     
     
     @Override
     public void setGRider(GRider foValue) {
          oApp = foValue;
     }
     
     /*BUTTON CLICKED*/
     private void cmdButton_Click(ActionEvent event) {
          String lsButton = ((Button)event.getSource()).getId();
          iTabIndex = tabPCustCont.getSelectionModel().getSelectedIndex();
          try {
               switch (lsButton){
                    case "btnBrowse":
                         if (!txtField26.getText().isEmpty() && !txtField26.getText().trim().equals("")) {
                              
                              if (oTrans.SearchRecord(txtField26.getText(), false)){
                                   if (oTransAddress.OpenRecord(oTrans.getMaster("sClientID").toString(), false)
                                    && oTransMobile.OpenRecord(oTrans.getMaster("sClientID").toString(), false)
                                    && oTransEmail.OpenRecord(oTrans.getMaster("sClientID").toString(), false)
                                    && oTransSocMed.OpenRecord(oTrans.getMaster("sClientID").toString(), false)){
                                        loadClientMaster();
                                        loadAddress();
                                        loadContact();
                                        loadEmail();
                                        loadSocialMedia();
                                        pnEditMode = EditMode.READY;
                                   } else {
                                             ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                                             pnEditMode = EditMode.UNKNOWN;
                                   }
                              } else {
                                  ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                                  pnEditMode = EditMode.UNKNOWN;
                              }
                         } else {
                              if (oTrans.SearchRecord(txtField01.getText(), true)){
                                   if (oTransAddress.OpenRecord(oTrans.getMaster("sClientID").toString(), false)
                                    && oTransMobile.OpenRecord(oTrans.getMaster("sClientID").toString(), false)
                                    && oTransEmail.OpenRecord(oTrans.getMaster("sClientID").toString(), false)
                                    && oTransSocMed.OpenRecord(oTrans.getMaster("sClientID").toString(), false)){
                                        loadClientMaster();
                                        loadAddress();
                                        loadContact();
                                        loadEmail();
                                        loadSocialMedia();
                                        pnEditMode = EditMode.READY;
                                   } else {
                                             ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                                             pnEditMode = EditMode.UNKNOWN;
                                   }
                              } else {
                                  ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                                  pnEditMode = EditMode.UNKNOWN;
                              }
                         }
                         break;
                    case "btnAdd": //create new client
                         if (oTrans.NewRecord() && oTransAddress.NewRecord() && oTransMobile.NewRecord() 
                             && oTransEmail.NewRecord() && oTransSocMed.NewRecord() ){
                              
                              clearFields(); //null exception error
                              loadClientMaster();
                              /*Clear tables*/
                              addressdata.clear(); 
                              mobiledata.clear();
                              emaildata.clear();
                              socialmediadata.clear();
                              pnEditMode = oTrans.getEditMode();
                         } else 
                             ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                          break;
                    case "btnEdit": //modify client info
                         if (oTrans.UpdateRecord() && oTransAddress.UpdateRecord() && oTransMobile.UpdateRecord() 
                             && oTransEmail.UpdateRecord() && oTransSocMed.UpdateRecord() ){
                              pnEditMode = oTrans.getEditMode(); 
                         } else 
                              ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                         break;
                    case "btnSave": //save client info
                         if (setSelection()) {
                              if (oTrans.SaveRecord()){
                                   oTransAddress.setClientID(oTrans.getMaster("sClientID").toString());
                                   oTransMobile.setClientID(oTrans.getMaster("sClientID").toString());
                                   oTransEmail.setClientID(oTrans.getMaster("sClientID").toString());
                                   oTransSocMed.setClientID(oTrans.getMaster("sClientID").toString());
                                   
                                   if (pnEditMode == EditMode.ADDNEW) {
                                        oTransAddress.removeAddress(oTransAddress.getItemCount());
                                        oTransMobile.removeMobile(oTransMobile.getItemCount());
                                        oTransEmail.removeEmail(oTransEmail.getItemCount());
                                        oTransSocMed.removeSocMed(oTransSocMed.getItemCount());
                                   }
                                   if (oTransAddress.SaveRecord()){ 
                                   }else {
                                        ShowMessageFX.Warning(getStage(), "Error while saving Client Address","Warning", null);
                                   }
                                   if (oTransMobile.SaveRecord()){ 
                                   }else {
                                        ShowMessageFX.Warning(getStage(), "Error while saving Client Mobile","Warning", null);
                                   }
                                   if (oTransEmail.SaveRecord()){ 
                                   }else {
                                        ShowMessageFX.Warning(getStage(), "Error while saving Client Social Media","Warning", null);
                                   }
                                   if (oTransSocMed.SaveRecord()){ 
                                   }else {
                                        ShowMessageFX.Warning(getStage(), oTransSocMed.getMessage(),"Warning", null);
                                   }
                                   
                                   ShowMessageFX.Warning(getStage(), "Transaction save successfully.", "Warning", null);
                                   pnEditMode = oTrans.getEditMode();
                              } else {
                                  ShowMessageFX.Warning(getStage(), "Error while saving Client Information","Warning", null);
                              }
                         }
                         break;                        
                    case "btnClose": //close tab
                         if(ShowMessageFX.OkayCancel(null, "Close Tab", "Are you sure, do you want to close tab?") == true){
                               if (unload != null) {
                                    unload.unloadForm(AnchorMain, oApp, "Customer");
                               }else {
                                    ShowMessageFX.Warning(null, "Warning", "Notify System Admin to Configure Null value at close button.");    
                               }
                               break;
                         } else
                             return;
                    case "btnTabAdd": 
                         switch(iTabIndex){
                              case 0:
                                   if (pnRow >= 1 ) {
                                        pnRow = 0;   
                                   } else { 
                                        //Validate Primary Before Inserting
                                        if (validateContactInfo() >= 1){
                                             if (checkBox14Addr.isSelected()) {
                                                  ShowMessageFX.Warning(null, "Warning", "You cannot add more than 1 Primary Address.");
                                                  return;
                                             }  
                                        }
                                        if(setItemtoTable("btnTabAdd")) {
                                             loadAddress();
                                             oTransAddress.addAddress();
                                              System.out.println("CLICKED getItemCount btnTabAdd  >>> " + oTransAddress.getItemCount());
                                        } else {
                                             return;
                                        }  
                                   }
                                   /*Address*/
                                   txtField03Addr.clear(); //House No
                                   txtField04Addr.clear(); //Street / Address
                                   txtField05Addr.clear(); // Town
                                   txtField06Addr.clear(); // Brgy
                                   txtField07Addr.clear(); //Zip code
                                   radiobtn18AddY.setSelected(true); //Active status
                                   radiobtn18AddN.setSelected(false); //Active status
                                   checkBox14Addr.setSelected(false); //Primary
                                   checkBox17Addr.setSelected(false); //Current
                                   checkBox12Addr.setSelected(false); //Office
                                   checkBox13Addr.setSelected(false); //Provincial
                                   textArea11Addr.clear(); // Address Remarks
                              break;
                              case 1: //Mobile
                                        
                                   if (pnRow >= 1 ) {
                                        //ShowMessageFX.Warning(getStage(), "You are currently in UPDATE mode Add to Address table aborted!","Warning", null);
                                        pnRow = 0;   
                                   } else { 
                                        if (validateContactInfo() >= 1){
                                             if (radiobtn11CntY.isSelected()) {
                                                  ShowMessageFX.Warning(null, "Warning", "You cannot add more than 1 Primary Contact Number.");
                                                  return;
                                             }  
                                        }
                                        if(setItemtoTable("btnTabAdd")) {
                                             loadContact();
                                             oTransMobile.addMobile();
                                             System.out.println("CLICKED getItemCount btnTabAdd  >>> " + oTransAddress.getItemCount());
                                        } else {
                                             return;
                                        }  
                                   }
                                        
                                   /*Clear Fields*/
                                   comboBox05Cont.setValue(null); // Contact Ownership
                                   comboBox04Cont.setValue(null); // Mobile Type
                                   txtField03Cont.clear();  //Mobile Number
                                   radiobtn14CntY.setSelected(true); // Contact Active Status
                                   radiobtn14CntN.setSelected(false); // Contact Active Status
                                   radiobtn11CntY.setSelected(false); // Contact Primary
                                   radiobtn11CntN.setSelected(false); // Contact Primary
                                   textArea13Cont.clear(); // Contact Remarks
                                   
                              break;
                              case 2: //Email
                                   if (pnRow >= 1 ) {
                                        pnRow = 0;   
                                   } else { 
                                        if (validateContactInfo() >= 1){
                                             if (radiobtn05EmaY.isSelected()) {
                                                  ShowMessageFX.Warning(null, "Warning", "You cannot add more than 1 Primary Email.");
                                                  return;
                                             }  
                                        }
                                        if(setItemtoTable("btnTabAdd")) {
                                             loadEmail();
                                             oTransEmail.addEmail();
                                             System.out.println("CLICKED getItemCount btnTabAdd  >>> " + oTransAddress.getItemCount());
                                        } else {
                                             return;
                                        }  
                                   }
                                   /*Clear Fields*/
                                   comboBox04EmAd.setValue(null); // Email Ownership
                                   radiobtn06EmaY.setSelected(true); // Email Active status
                                   radiobtn06EmaN.setSelected(false); // Email Active status
                                   radiobtn05EmaY.setSelected(false); // Email Primary
                                   radiobtn05EmaN.setSelected(false); // Email Primary
                                   txtField03EmAd.clear(); // Email Address
                                   
                              break;
                              case 3:
                                   if (pnRow >= 1 ) {
                                        pnRow = 0;   
                                   } else { 
                                        if(setItemtoTable("btnTabAdd")) {
                                             loadSocialMedia();
                                             oTransSocMed.addSocMed();
                                             System.out.println("CLICKED getItemCount btnTabAdd  >>> " + oTransAddress.getItemCount());
                                        } else {
                                             return;
                                        }  
                                   }
                                   /*Clear Fields*/
                                   radiobtn05SocY.setSelected(true); // SocMed Active status
                                   radiobtn05SocN.setSelected(false); // SocMed Active status
                                   txtField03Socm.clear(); // SocMed Account
                                   comboBox04Socm.setValue(null); // SocMed Type
                              break;
                         }
                    break;
                    case "btnTabUpd":
                         switch(iTabIndex){
                              case 0:
                                   if(setItemtoTable("btnTabUpd")) {
                                        if (validateContactInfo() >= 1){
                                             if (checkBox14Addr.isSelected()) {
                                                  ShowMessageFX.Warning(null, "Warning", "You cannot add more than 1 Primary Address.");
                                                  return;
                                             }  
                                        }
                                        loadAddress();
                                        System.out.println("CLICKED getItemCount btnTabUpd  >>> " + oTransAddress.getItemCount());
                                        pnRow = 0;
                                        /*Address*/
                                        txtField03Addr.clear(); //House No
                                        txtField04Addr.clear(); //Street / Address
                                        txtField05Addr.clear(); // Town
                                        txtField06Addr.clear(); // Brgy
                                        txtField07Addr.clear(); //Zip code
                                        radiobtn18AddY.setSelected(true); //Active status
                                        radiobtn18AddN.setSelected(false); //Active status
                                        checkBox14Addr.setSelected(false); //Primary
                                        checkBox17Addr.setSelected(false); //Current
                                        checkBox12Addr.setSelected(false); //Office
                                        checkBox13Addr.setSelected(false); //Provincial
                                        textArea11Addr.clear(); // Address Remarks
                                   }
                              break;
                              case 1: //Mobile
                                   if(setItemtoTable("btnTabUpd")) {
                                        if (validateContactInfo() >= 1){
                                             if (radiobtn11CntY.isSelected()) {
                                                  ShowMessageFX.Warning(null, "Warning", "You cannot add more than 1 Primary Contact Number.");
                                                  return;
                                             }  
                                        }
                                        loadContact();
                                        System.out.println("CLICKED getItemCount btnTabUpd  >>> " + oTransAddress.getItemCount());
                                        pnRow = 0;
                                        /*Clear Fields*/
                                        comboBox05Cont.setValue(null); // Contact Ownership
                                        comboBox04Cont.setValue(null); // Mobile Type
                                        txtField03Cont.clear();  //Mobile Number
                                        radiobtn14CntY.setSelected(true); // Contact Active Status
                                        radiobtn14CntN.setSelected(false); // Contact Active Status
                                        radiobtn11CntY.setSelected(false); // Contact Primary
                                        radiobtn11CntN.setSelected(false); // Contact Primary
                                        textArea13Cont.clear(); // Contact Remarks
                                   }
                              break;
                              case 2: //Email
                                   if(setItemtoTable("btnTabUpd")) {
                                        if (validateContactInfo() >= 1){
                                             if (radiobtn05EmaY.isSelected()) {
                                                  ShowMessageFX.Warning(null, "Warning", "You cannot add more than 1 Primary Email.");
                                                  return;
                                             }  
                                        }
                                        loadEmail();
                                        System.out.println("CLICKED getItemCount btnTabUpd  >>> " + oTransAddress.getItemCount());
                                        pnRow = 0;
                                        comboBox04EmAd.setValue(null); // Email Ownership
                                        radiobtn06EmaY.setSelected(true); // Email Active status
                                        radiobtn06EmaN.setSelected(false); // Email Active status
                                        radiobtn05EmaY.setSelected(false); // Email Primary
                                        radiobtn05EmaN.setSelected(false); // Email Primary
                                        txtField03EmAd.clear(); // Email Address
                                   }
                              break;
                              case 3:
                                   if(setItemtoTable("btnTabUpd")) {
                                        loadSocialMedia();
                                        /*Clear Fields*/
                                        radiobtn05SocY.setSelected(true); // SocMed Active status
                                        radiobtn05SocN.setSelected(false); // SocMed Active status
                                        txtField03Socm.clear(); // SocMed Account
                                        comboBox04Socm.setValue(null); // SocMed Type
                                   }
                              break;
                         }
                    break;
                    case "btnTabRem":
                         switch(iTabIndex){
                              case 0:
                                   if(ShowMessageFX.OkayCancel(null, "Confirmation", "Are you sure, you want to remove this Client Address?") == true){
                                   } else 
                                       return;
                                   oTransAddress.removeAddress(pnRow);
                                   pnRow = 0;
                                   loadAddress();
                                   /*Address*/
                                   txtField03Addr.clear(); //House No
                                   txtField04Addr.clear(); //Street / Address
                                   txtField05Addr.clear(); // Town
                                   txtField06Addr.clear(); // Brgy
                                   txtField07Addr.clear(); //Zip code
                                   radiobtn18AddY.setSelected(true); //Active status
                                   radiobtn18AddN.setSelected(false); //Active status
                                   checkBox14Addr.setSelected(false); //Primary
                                   checkBox17Addr.setSelected(false); //Current
                                   checkBox12Addr.setSelected(false); //Office
                                   checkBox13Addr.setSelected(false); //Provincial
                                   textArea11Addr.clear(); // Address Remarks
                              break;
                              case 1://Mobile
                                   if(ShowMessageFX.OkayCancel(null, "Confirmation", "Are you sure, you want to remove this Client Mobile?") == true){
                                   } else 
                                       return;
                                   oTransMobile.removeMobile(pnRow);
                                   pnRow = 0;
                                   loadContact();
                                   /*Clear Fields*/
                                   comboBox05Cont.setValue(null); // Contact Ownership
                                   comboBox04Cont.setValue(null); // Mobile Type
                                   txtField03Cont.clear();  //Mobile Number
                                   radiobtn14CntY.setSelected(true); // Contact Active Status
                                   radiobtn14CntN.setSelected(false); // Contact Active Status
                                   radiobtn11CntY.setSelected(false); // Contact Primary
                                   radiobtn11CntN.setSelected(false); // Contact Primary
                                   textArea13Cont.clear(); // Contact Remarks
                                   
                              break;
                              case 2://Email
                                   if(ShowMessageFX.OkayCancel(null, "Confirmation", "Are you sure, you want to remove this Client Email?") == true){
                                   } else 
                                       return;
                                   oTransEmail.removeEmail(pnRow);
                                   pnRow = 0;
                                   loadEmail();
                                   comboBox04EmAd.setValue(null); // Email Ownership
                                   radiobtn06EmaY.setSelected(true); // Email Active status
                                   radiobtn06EmaN.setSelected(false); // Email Active status
                                   radiobtn05EmaY.setSelected(false); // Email Primary
                                   radiobtn05EmaN.setSelected(false); // Email Primary
                                   txtField03EmAd.clear(); // Email Address
                              break;
                              case 3://Social Media
                                   if(ShowMessageFX.OkayCancel(null, "Confirmation", "Are you sure, you want to remove this Client Social Media?") == true){
                                   } else 
                                       return;
                                   oTransSocMed.removeSocMed(pnRow);
                                   pnRow = 0;
                                   loadSocialMedia();
                                   /*Clear Fields*/
                                   radiobtn05SocY.setSelected(true); // SocMed Active status
                                   radiobtn05SocN.setSelected(false); // SocMed Active status
                                   txtField03Socm.clear(); // SocMed Account
                                   comboBox04Socm.setValue(null); // SocMed Type
                              break;
                         } 
                        
                    break;
               }
               initButton(pnEditMode);    
          } catch (SQLException e) {
               e.printStackTrace();
               ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
          }
     }
     
     private void loadClientMaster(){
          try {
               txtField01.setText((String) oTrans.getMaster(1)); 
               txtField02.setText((String) oTrans.getMaster(2));
               txtField03.setText((String) oTrans.getMaster(3));
               txtField04.setText((String) oTrans.getMaster(4));
               txtField05.setText((String) oTrans.getMaster(5));
               txtField06.setText((String) oTrans.getMaster(6));
               txtField10.setText((String) oTrans.getMaster(24));
               txtField11.setValue(strToDate(CommonUtils.xsDateShort((Date) oTrans.getMaster(11))));
               txtField12.setText((String) oTrans.getMaster(25));
               txtField13.setText((String) oTrans.getMaster(13));
               txtField14.setText((String) oTrans.getMaster(14));
               textArea15.setText((String) oTrans.getMaster(15));
               
               txtField25.setText((String) oTrans.getMaster(28));
               comboBox07.getSelectionModel().select(Integer.parseInt((String)oTrans.getMaster(7)));
               comboBox08.getSelectionModel().select(Integer.parseInt((String)oTrans.getMaster(8)));
               comboBox09.getSelectionModel().select(Integer.parseInt((String)oTrans.getMaster(9)));
               comboBox18.getSelectionModel().select(Integer.parseInt((String)oTrans.getMaster(18))); 
               if (Integer.parseInt((String)oTrans.getMaster(18)) == 1) {
                     txtField16.setText((String) oTrans.getMaster(16));    
               }
               iClientType = comboBox18.getSelectionModel().getSelectedIndex();
               
          } catch (SQLException e) {
               ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
          }
     }
     
     /*LOAD TABLE ADDRESS*/
     private void loadAddress(){
          String sAddress, sStatus ;
         
          try {
               addressdata.clear();
               /*Set Values to Class Address Master*/
               System.out.println("loadAddress getItemCount >>> " + oTransAddress.getItemCount());
               for (lnCtr = 1; lnCtr <= oTransAddress.getItemCount(); lnCtr++){
                    sAddress = oTransAddress.getAddress(lnCtr, "sAddressx").toString() + " " + oTransAddress.getAddress(lnCtr, "sBrgyName").toString() + " " + oTransAddress.getAddress(lnCtr, "sTownName").toString() ;
                    
                    if (oTransAddress.getAddress(lnCtr, "cPrimaryx").toString().equals("1")){
                         sStatus = "Y";
                    } else {
                         sStatus = "N";
                    }
                    if (!sAddress.isEmpty() && !sAddress.trim().equals("")) {
                         System.out.println("Get House Number >>> " + oTransAddress.getAddress(lnCtr, "sHouseNox").toString());
                         addressdata.add(new TableClientAddress(
                         String.valueOf(lnCtr), //ROW
                         sStatus,
                         oTransAddress.getAddress(lnCtr, "sHouseNox").toString(), //HOUSE NUMBER
                         sAddress,
                         oTransAddress.getAddress(lnCtr, "sZippCode").toString()
                         ));
                    }
               }
               
          } catch (SQLException e) {
               ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
          } 
          
     }
     
     
     private void loadContact(){
          String sOwnership;
          try {
               mobiledata.clear();
               /*Set Values to Class Mobile Master*/
               for (lnCtr = 1; lnCtr <= oTransMobile.getItemCount(); lnCtr++){
                    if (oTransMobile.getMobile(lnCtr, "cOwnerxxx").toString().equals("0")) {
                         sOwnership = "Personal";
                    }else if (oTransMobile.getMobile(lnCtr, "cOwnerxxx").toString().equals("1")) {
                         sOwnership = "Office";
                    } else {
                         sOwnership = "Other";
                    }
                    if (!oTransMobile.getMobile(lnCtr, "sMobileNo").toString().trim().equals("") ||
                        !oTransMobile.getMobile(lnCtr, "sMobileNo").toString().trim().isEmpty()){
                         mobiledata.add(new TableClientMobile(
                         String.valueOf(lnCtr), //ROW
                         sOwnership, //OWNERSHIP
                         oTransMobile.getMobile(lnCtr, "sMobileNo").toString() //NUMBER
                         ));
                    }
               }
          } catch (SQLException e) {
               ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
          }

          /*
          try {
               txtField03Addr.setText((String) oTrans.getMaster(3));
               txtField04Addr.setText((String) oTrans.getMaster(4));
               txtField05Addr.setText((String) oTrans.getMaster(23));
               txtField06Addr.setText((String) oTrans.getMaster(24));
               txtField07Addr.setText((String) oTrans.getMaster(26));
               textArea11Addr.setText((String) oTrans.getMaster(11));
          } catch (SQLException e) {
               ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
          }
          */
     }
     
     private void loadEmail(){
          String sOwnership;
          try {
               emaildata.clear();
               /*Set Values to Class Mobile Master*/
               for (lnCtr = 1; lnCtr <= oTransEmail.getItemCount(); lnCtr++){
                    if (oTransEmail.getEmail(lnCtr, "cOwnerxxx").toString().equals("0")) {
                         sOwnership = "Personal";
                    }else if (oTransEmail.getEmail(lnCtr, "cOwnerxxx").toString().equals("1")) {
                         sOwnership = "Office";
                    } else {
                         sOwnership = "Other";
                    }
                    if (!oTransEmail.getEmail(lnCtr, "sEmailAdd").toString().trim().equals("") ||
                        !oTransEmail.getEmail(lnCtr, "sEmailAdd").toString().trim().isEmpty()){
                         emaildata.add(new TableClientEmail(
                         String.valueOf(lnCtr), //ROW
                         sOwnership, //OWNERSHIP
                         oTransEmail.getEmail(lnCtr, "sEmailAdd").toString() //EMAIL
                         ));
                    }
               }   
          } catch (SQLException e) {
               ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
          }
          /*
               txtField03EmAd.setText((String) oTrans.getMaster(3));
               comboBox04EmAd.getSelectionModel().select(Integer.parseInt((String)oTrans.getMaster(4)));
               if ((String) oTrans.getMaster(5) == "1") {
                    cmdRadioButton(radiobtn05EmaY,radiobtn05EmaN);
               } else {
                    cmdRadioButton(radiobtn05EmaN,radiobtn05EmaY);
               }
               if ((String) oTrans.getMaster(6) == "1") {
                    cmdRadioButton(radiobtn06EmaY,radiobtn06EmaN);
               } else {
                    cmdRadioButton(radiobtn06EmaN,radiobtn06EmaY);
               }
          } catch (SQLException e) {
               ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
          }   
          */
          
     }
     
 
     
     private void loadSocialMedia(){
          try {
               String sSocType = "";
               socialmediadata.clear();
               
               /*Set Values to Class Mobile Master*/
               for (lnCtr = 1; lnCtr <= oTransSocMed.getItemCount(); lnCtr++){
                    if (oTransSocMed.getSocMed(lnCtr, "cSocialTp").toString().equals("0")) {
                         sSocType = "Facebook";
                    } else if (oTransSocMed.getSocMed(lnCtr, "cSocialTp").toString().equals("1")) {
                         sSocType = "Whatsup";
                    } else if (oTransSocMed.getSocMed(lnCtr, "cSocialTp").toString().equals("2")) {
                         sSocType = "Intagram";
                    } else if (oTransSocMed.getSocMed(lnCtr, "cSocialTp").toString().equals("3")) {
                         sSocType = "Tiktok";
                    } else if (oTransSocMed.getSocMed(lnCtr, "cSocialTp").toString().equals("4")) {
                         sSocType = "Twitter";
                    } else {
                         sSocType = "Others";
                    }
                    
                    if (!oTransSocMed.getSocMed(lnCtr, "sAccountx").toString().trim().equals("") ||
                        !oTransSocMed.getSocMed(lnCtr, "sAccountx").toString().trim().isEmpty()){
                         socialmediadata.add(new TableClientSocialMedia(
                         String.valueOf(lnCtr), //ROW
                         sSocType, //Social Type
                         oTransSocMed.getSocMed(lnCtr, "sAccountx").toString() //Acount
                         ));
                    }
               }   
          } catch (SQLException e) {
               ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
          }
          
          /*
          try {

               txtField03Socm.setText((String) oTrans.getMaster(3));
               comboBox04Socm.getSelectionModel().select(Integer.parseInt((String)oTrans.getMaster(4)));
               if ((String) oTrans.getMaster(5) == "1") {
                    cmdRadioButton(radiobtn05SocY,radiobtn05SocN);
               } else {
                    cmdRadioButton(radiobtn05SocN,radiobtn05SocY);
               }
          } catch (SQLException e) {
               ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
          }   
          */
     }
     
     
     private boolean setItemtoTable(String btnID){
          iTabIndex = tabPCustCont.getSelectionModel().getSelectedIndex();
          try{
              
               switch(btnID){
                    case "btnTabAdd":
                         switch(iTabIndex){
                              case 0:
                                   tbl_row = oTransAddress.getItemCount();
                              break;
                              case 1:
                                   tbl_row = oTransMobile.getItemCount();
                              break;
                              case 2:
                                   tbl_row = oTransEmail.getItemCount();
                              break;
                              case 3:
                                   tbl_row = oTransSocMed.getItemCount();
                              break;
                         }
                    break;
                    case "btnTabUpd":
                         tbl_row = pnRow;
                    break;
               }
               
               if (tbl_row <= 0) {
                    ShowMessageFX.Warning(getStage(),"Invalid Table Row to Set. Insert to Table Aborted!", "Warning", null);
                    return false;
               }
               
               System.out.println("SET getItemCount current row  >>> " + oTransAddress.getItemCount());
               System.out.println("SET tbl_row set to address in row >>> " + tbl_row);
               
               switch(iTabIndex){
                    case 0:
                         //Validate Before adding to tables
                         if ((txtField03Addr.getText().isEmpty() || txtField04Addr.getText().isEmpty() || 
                              txtField07Addr.getText().isEmpty() || txtField05Addr.getText().isEmpty() || 
                              txtField06Addr.getText().isEmpty()) ||
                              txtField03Addr.getText().trim().equals("") || txtField04Addr.getText().trim().equals("") || 
                              txtField07Addr.getText().trim().equals("") || txtField05Addr.getText().trim().equals("") || 
                              txtField06Addr.getText().trim().equals("")){ 
                              ShowMessageFX.Warning(getStage(),"Invalid Address. Insert to table Aborted!", "Warning", null);
                              return false;
                         }
                         
                         if (!radiobtn18AddY.isSelected() && !radiobtn18AddN.isSelected()) {
                              ShowMessageFX.Warning(getStage(),"Please select Address Status. Insert to table Aborted!", "Warning", null);
                              return false;
                         }
                         
                         if (!checkBox12Addr.isSelected() && !checkBox13Addr.isSelected() &&
                             !checkBox14Addr.isSelected() && !checkBox17Addr.isSelected()) {
                              ShowMessageFX.Warning(getStage(),"Please select Address Type. Insert to table Aborted!", "Warning", null);
                              return false;
                         }
                         
                         oTransAddress.setAddressTable(tbl_row,3, txtField03Addr.getText());
                         oTransAddress.setAddressTable(tbl_row,4, txtField04Addr.getText());
                         oTransAddress.setAddressTable(tbl_row,7, txtField07Addr.getText());
                         oTransAddress.setAddressTable(tbl_row,11, textArea11Addr.getText());
                         oTransAddress.setAddressTable(tbl_row,25, txtField05Addr.getText());
                         oTransAddress.setAddressTable(tbl_row,24, txtField06Addr.getText());

                         if (checkBox12Addr.isSelected()){
                            oTransAddress.setAddressTable(tbl_row,12, 1);
                         } else
                            oTransAddress.setAddressTable(tbl_row,12, 0);  
                         if (checkBox13Addr.isSelected()){
                            oTransAddress.setAddressTable(tbl_row,13, 1);
                         } else
                            oTransAddress.setAddressTable(tbl_row,13, 0);
                         if (checkBox14Addr.isSelected()){
                            oTransAddress.setAddressTable(tbl_row,14, 1);
                         } else
                            oTransAddress.setAddressTable(tbl_row,14, 0);
                         if (checkBox17Addr.isSelected()){
                            oTransAddress.setAddressTable(tbl_row,17, 1);
                         } else
                            oTransAddress.setAddressTable(tbl_row,17, 0);
                         if (radiobtn18AddY.isSelected()){
                            oTransAddress.setAddressTable(tbl_row,18, 1);
                         } else
                            oTransAddress.setAddressTable(tbl_row,18, 0);

                    break;
                    case 1: //Mobile
                         //Validate Before adding to tables
                         if (txtField03Cont.getText().isEmpty() || txtField03Cont.getText().trim().equals("")){ 
                              ShowMessageFX.Warning(getStage(),"Invalid Mobile. Insert to table Aborted!", "Warning", null);
                              return false;
                         }
                         
                         if (!radiobtn11CntY.isSelected() && !radiobtn11CntN.isSelected()) {
                              ShowMessageFX.Warning(getStage(),"Please select Mobile Type. Insert to table Aborted!", "Warning", null);
                              return false;
                         }
                         
                         if (!radiobtn14CntY.isSelected() && !radiobtn14CntN.isSelected()) {
                              ShowMessageFX.Warning(getStage(),"Please select Mobile Status. Insert to table Aborted!", "Warning", null);
                              return false;
                         }
                         
                         if (comboBox05Cont.getValue().equals("")) {
                              ShowMessageFX.Warning(getStage(),"Please select Contact Ownership. Insert to table Aborted!", "Warning", null);
                              return false;
                         }
                         if (comboBox04Cont.getValue().equals("")) {
                              ShowMessageFX.Warning(getStage(),"Please select Mobile Type. Insert to table Aborted!", "Warning", null);
                              return false;
                         }
                         
                         oTransMobile.setMobile(tbl_row,3, txtField03Cont.getText());
                         oTransMobile.setMobile(tbl_row,4, comboBox04Cont.getSelectionModel().getSelectedIndex());
                         oTransMobile.setMobile(tbl_row,5, comboBox05Cont.getSelectionModel().getSelectedIndex());
                         oTransMobile.setMobile(tbl_row,13, textArea13Cont.getText());
                         
                         if (radiobtn11CntY.isSelected()){
                            oTransMobile.setMobile(tbl_row,11, 1);
                         } else
                            oTransMobile.setMobile(tbl_row,11, 0);  
                         if (radiobtn14CntY.isSelected()){
                            oTransMobile.setMobile(tbl_row,14, 1);
                         } else
                            oTransMobile.setMobile(tbl_row,14, 0);
                    break;
                    case 2: //Email
                         //Validate Before adding to tables
                         if (txtField03EmAd.getText().isEmpty() || txtField03EmAd.getText().trim().equals("")){ 
                              ShowMessageFX.Warning(getStage(),"Invalid Email. Insert to table Aborted!", "Warning", null);
                              return false;
                         }
                         
                         if (!radiobtn05EmaY.isSelected() && !radiobtn05EmaN.isSelected()) {
                              ShowMessageFX.Warning(getStage(),"Please select Email Type.Insert to table Aborted!", "Warning", null);
                              return false;
                         }
                         
                         if (!radiobtn06EmaY.isSelected() && !radiobtn06EmaN.isSelected()) {
                              ShowMessageFX.Warning(getStage(),"Please select Email Status. Insert to table Aborted!", "Warning", null);
                              return false;
                         }
                         
                         if (comboBox04EmAd.getValue().equals("")) {
                              ShowMessageFX.Warning(getStage(),"Please select Email Ownership. Insert to table Aborted!", "Warning", null);
                              return false;
                         }
                         
                         oTransEmail.setEmail(tbl_row,3, txtField03EmAd.getText());
                         oTransEmail.setEmail(tbl_row,4, comboBox04EmAd.getSelectionModel().getSelectedIndex());
                         
                         if (radiobtn05EmaY.isSelected()){
                            oTransEmail.setEmail(tbl_row,5, 1);
                         } else
                            oTransEmail.setEmail(tbl_row,5, 0);  
                         if (radiobtn05EmaY.isSelected()){
                            oTransEmail.setEmail(tbl_row,6, 1);
                         } else
                            oTransEmail.setEmail(tbl_row,6, 0);
                    break;
                    case 3: //Socila Media
                         //Validate Before adding to tables
                         if (txtField03Socm.getText().isEmpty() || txtField03Socm.getText().trim().equals("")){ 
                              ShowMessageFX.Warning(getStage(),"Invalid Account. Insert to table Aborted!", "Warning", null);
                              return false;
                         }
                         
                         if (!radiobtn05SocY.isSelected() && !radiobtn05SocN.isSelected()) {
                              ShowMessageFX.Warning(getStage(),"Please select Account Type.Insert to table Aborted!", "Warning", null);
                              return false;
                         }
                         
                         if (comboBox04Socm.getValue().equals("") || comboBox04Socm.getValue() == null) {
                              ShowMessageFX.Warning(getStage(),"Please select Social Media Type. Insert to table Aborted!", "Warning", null);
                              return false;
                         }
                         
                         oTransSocMed.setSocMed(tbl_row,3, txtField03Socm.getText());
                         oTransSocMed.setSocMed(tbl_row,4, comboBox04Socm.getSelectionModel().getSelectedIndex());
                         
                         if (radiobtn05SocY.isSelected()){
                            oTransSocMed.setSocMed(tbl_row,5, 1);
                         } else
                            oTransSocMed.setSocMed(tbl_row,5, 0);
                    break;
               } 
                
          } catch (SQLException e) {
               ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
          }
          
          return true; 
          
     }
     
     private void loadAddress_textfield(){
//          try {
//              /* txtField03Addr.setText((String) oTransAddress.getAddress("sHouseNox"));
//                 txtField04Addr.setText((String) oTransAddress.getAddress("sAddressx")); */
////               txtField05Addr.setText((String) oTransAddress.getAddress("sTownName"));
////               txtField06Addr.setText((String) oTransAddress.getAddress("sBrgyName"));
////               txtField07Addr.setText((String) oTransAddress.getAddress("sZippCode"));
//             /*  textArea11Addr.setText((String) oTransAddress.getAddress("sRemarksx")); */
//          } catch (SQLException e) {
//               ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
//          }   
     }
     
     /*populate Address Table*/
     private void initAddress() {
          addrindex01.setCellValueFactory(new PropertyValueFactory<>("addrindex01"));
          addrindex02.setCellValueFactory(new PropertyValueFactory<>("addrindex02"));
          addrindex03.setCellValueFactory(new PropertyValueFactory<>("addrindex03"));
          addrindex04.setCellValueFactory(new PropertyValueFactory<>("addrindex04"));
          addrindex05.setCellValueFactory(new PropertyValueFactory<>("addrindex05"));
          tblAddress.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
               TableHeaderRow header = (TableHeaderRow) tblAddress.lookup("TableHeaderRow");
               header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                   header.setReordering(false);
               });
           });
        addressdata.clear();
        tblAddress.setItems(addressdata);
     }
     
     /*populate Contact Table*/    
     private void initContact() {
          contindex01.setCellValueFactory(new PropertyValueFactory<>("contindex01"));
          contindex02.setCellValueFactory(new PropertyValueFactory<>("contindex02"));
          contindex03.setCellValueFactory(new PropertyValueFactory<>("contindex03"));
          
          tblContact.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
               TableHeaderRow header = (TableHeaderRow) tblContact.lookup("TableHeaderRow");
               header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
               header.setReordering(false);
               });
          });
        mobiledata.clear();
        tblContact.setItems(mobiledata);
    
     }
     
     /*populate Email Table*/    
     private void initEmail() {
          emadindex01.setCellValueFactory(new PropertyValueFactory<>("emadindex01"));
          emadindex02.setCellValueFactory(new PropertyValueFactory<>("emadindex02"));
          emadindex03.setCellValueFactory(new PropertyValueFactory<>("emadindex03"));
          
          tblEmail.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
               TableHeaderRow header = (TableHeaderRow) tblEmail.lookup("TableHeaderRow");
               header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
               header.setReordering(false);
               });
          });
        emaildata.clear();
        tblEmail.setItems(emaildata);
     }
     
     
     /*populate Social Media Table*/
     private void initSocialMedia() {
          socmindex01.setCellValueFactory(new PropertyValueFactory<>("socmindex01"));
          socmindex02.setCellValueFactory(new PropertyValueFactory<>("socmindex02"));
          socmindex03.setCellValueFactory(new PropertyValueFactory<>("socmindex03"));
          
          tblSocMed.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
               TableHeaderRow header = (TableHeaderRow) tblSocMed.lookup("TableHeaderRow");
               header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
               header.setReordering(false);
               });
          });
        socialmediadata.clear();
        tblSocMed.setItems(socialmediadata);
     }
     
     private int validateContactInfo() {
          iTabIndex = tabPCustCont.getSelectionModel().getSelectedIndex();
          int iCntp = 0;
          try{
               switch(iTabIndex) {
                    case 0: //Address
                         for (lnCtr = 1; lnCtr <= oTransAddress.getItemCount(); lnCtr++){
                              if (oTransAddress.getAddress(lnCtr, "cPrimaryx").toString().equals("1")){
                                   iCntp = iCntp + 1;
                                   System.out.println("Primary Address Counter >>> " + iCntp);
                              }
                         }
                    break;
                    case 1: //Mobile
                         for (lnCtr = 1; lnCtr <= oTransMobile.getItemCount(); lnCtr++){
                              if (oTransMobile.getMobile(lnCtr, "cPrimaryx").toString().equals("1")){
                                   iCntp = iCntp + 1;
                                   System.out.println("Primary Mobile Counter >>> " + iCntp);
                              }
                         }
                    break;
                    case 2: //Email
                         for (lnCtr = 1; lnCtr <= oTransEmail.getItemCount(); lnCtr++){
                              if (oTransEmail.getEmail(lnCtr, "cPrimaryx").toString().equals("1")){
                                   iCntp = iCntp + 1;
                                   System.out.println("Primary Email Counter >>> " + iCntp);
                              }
                         }
                    break;
               }    
          } catch (SQLException e) {
               ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
          } 
          return iCntp;
     }
     
     /*Populate Text Field Based on selected address in table*/
     private void getSelectedItem(){
          iTabIndex = tabPCustCont.getSelectionModel().getSelectedIndex();
          
          try {    
               switch(iTabIndex) {
                    /*Address*/
                    case 0: 
                         txtField03Addr.setText((String) oTransAddress.getAddress(pnRow,"sHouseNox" ));
                         txtField04Addr.setText((String) oTransAddress.getAddress(pnRow, "sAddressx"));
                         txtField05Addr.setText((String) oTransAddress.getAddress(pnRow, "sTownName"));
                         txtField06Addr.setText((String) oTransAddress.getAddress(pnRow, "sBrgyName"));
                         txtField07Addr.setText((String) oTransAddress.getAddress(pnRow, "sZippCode"));
                         textArea11Addr.setText((String) oTransAddress.getAddress(pnRow, "sRemarksx"));
                         if (oTransAddress.getAddress(pnRow, "cRecdStat").toString().equals("1")) {
                              radiobtn18AddY.setSelected(true); radiobtn18AddN.setSelected(false);
                         } else {
                              radiobtn18AddY.setSelected(false);  radiobtn18AddN.setSelected(true);
                         }
                         if (oTransAddress.getAddress(pnRow, "cOfficexx").toString().equals("1")) {
                              checkBox12Addr.setSelected(true);
                         } else {
                              checkBox12Addr.setSelected(false);
                         }
                         if (oTransAddress.getAddress(pnRow, "cProvince").toString().equals("1")) {
                              checkBox13Addr.setSelected(true);
                         } else {
                              checkBox13Addr.setSelected(false);
                         }
                         if (oTransAddress.getAddress(pnRow, "cPrimaryx").toString().equals("1")) {
                             checkBox14Addr.setSelected(true);
                         } else {
                              checkBox14Addr.setSelected(false);
                         }
                         if (oTransAddress.getAddress(pnRow, "cCurrentx").toString().equals("1")) {
                              checkBox17Addr.setSelected(true);
                         } else {
                              checkBox17Addr.setSelected(false);
                         }
                    break;
                    
                    /*Mobile*/
                    case 1:
                         txtField03Cont.setText((String) oTransMobile.getMobile(pnRow,"sMobileNo" ));
                         textArea13Cont.setText((String) oTransMobile.getMobile(pnRow, "sRemarksx"));
                         comboBox04Cont.getSelectionModel().select(Integer.parseInt((String) oTransMobile.getMobile(pnRow,"cMobileTp" )));
                         comboBox05Cont.getSelectionModel().select(Integer.parseInt((String) oTransMobile.getMobile(pnRow,"cOwnerxxx" )));
                         if (oTransMobile.getMobile(pnRow, "cRecdStat").toString().equals("1")) {
                              radiobtn14CntY.setSelected(true); radiobtn14CntN.setSelected(false);
                         } else {
                              radiobtn14CntY.setSelected(false);  radiobtn14CntN.setSelected(true);
                         }
                         if (oTransMobile.getMobile(pnRow, "cPrimaryx").toString().equals("1")) {
                              radiobtn11CntY.setSelected(true); radiobtn11CntN.setSelected(false);
                         } else {
                              radiobtn11CntY.setSelected(false);  radiobtn11CntN.setSelected(true);
                         }
                    break;
                    
                    /*Email*/
                    case 2:
                         txtField03EmAd.setText((String) oTransEmail.getEmail(pnRow,"sEmailAdd" ));
                         comboBox04EmAd.getSelectionModel().select(Integer.parseInt((String) oTransEmail.getEmail(pnRow,"cOwnerxxx" )));
                         if (oTransEmail.getEmail(pnRow, "cRecdStat").toString().equals("1")) {
                              radiobtn06EmaY.setSelected(true); radiobtn06EmaN.setSelected(false);
                         } else {
                              radiobtn06EmaY.setSelected(false);  radiobtn06EmaN.setSelected(true);
                         }
                         if (oTransEmail.getEmail(pnRow, "cPrimaryx").toString().equals("1")) {
                              radiobtn05EmaY.setSelected(true); radiobtn05EmaN.setSelected(false);
                         } else {
                              radiobtn05EmaY.setSelected(false);  radiobtn05EmaN.setSelected(true);
                         }
                    break;
                    
                    /*Social*/
                    case 3:
                         txtField03Socm.setText((String) oTransSocMed.getSocMed(pnRow,"sAccountx" ));
                         comboBox04Socm.getSelectionModel().select(Integer.parseInt((String) oTransSocMed.getSocMed(pnRow,"cSocialTp" )));
                         if (oTransSocMed.getSocMed(pnRow, "cRecdStat").toString().equals("1")) {
                              radiobtn05SocY.setSelected(true); radiobtn05SocN.setSelected(false);
                         } else {
                              radiobtn05SocY.setSelected(false);  radiobtn05SocN.setSelected(true);
                         }
                    break;
               }
          } catch (SQLException e) {
               ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
          }     
     }
     
     @FXML
     private void tblAddress_Clicked(MouseEvent event) {
          pnRow = tblAddress.getSelectionModel().getSelectedIndex() + 1;
          if(pnRow == 0) { return;}
          getSelectedItem();
          //TableClientAddress ti = (TableClientAddress) tblAddress.getItems().get(pnRow);
          
          tblAddress.setOnKeyReleased((KeyEvent t)-> {
                KeyCode key = t.getCode();
                switch (key){
                    case DOWN:
                        pnRow = tblAddress.getSelectionModel().getSelectedIndex();
                        if (pnRow == tblAddress.getItems().size()) {
                            pnRow = tblAddress.getItems().size();
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
                        pnRows = tblAddress.getSelectionModel().getSelectedIndex() + 1;
                            pnRow = pnRows; 
                            getSelectedItem();
                        break;
                    default:
                        return; 
                }
            });

     }
     
          @FXML
     private void tblContact_Clicked(MouseEvent event) {
          pnRow = tblContact.getSelectionModel().getSelectedIndex() + 1;
          if(pnRow == 0) { return;}
          getSelectedItem();
          //TableClientAddress ti = (TableClientAddress) tblAddress.getItems().get(pnRow);
          
          tblContact.setOnKeyReleased((KeyEvent t)-> {
                KeyCode key = t.getCode();
                switch (key){
                    case DOWN:
                        pnRow = tblContact.getSelectionModel().getSelectedIndex();
                        if (pnRow == tblContact.getItems().size()) {
                            pnRow = tblContact.getItems().size();
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
                        pnRows = tblContact.getSelectionModel().getSelectedIndex() + 1;
                            pnRow = pnRows; 
                            getSelectedItem();
                        break;
                    default:
                        return; 
                }
            });
     }

     @FXML
     private void tblEmail_Clicked(MouseEvent event) {
          pnRow = tblEmail.getSelectionModel().getSelectedIndex() + 1;
          if(pnRow == 0) { return;}
          getSelectedItem();
          //TableClientAddress ti = (TableClientAddress) tblAddress.getItems().get(pnRow);
          
          tblEmail.setOnKeyReleased((KeyEvent t)-> {
                KeyCode key = t.getCode();
                switch (key){
                    case DOWN:
                        pnRow = tblEmail.getSelectionModel().getSelectedIndex();
                        if (pnRow == tblEmail.getItems().size()) {
                            pnRow = tblEmail.getItems().size();
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
                        pnRows = tblEmail.getSelectionModel().getSelectedIndex() + 1;
                            pnRow = pnRows; 
                            getSelectedItem();
                        break;
                    default:
                        return; 
                }
            });
     }

     @FXML
     private void tblSocMed_Clicked(MouseEvent event) {
          pnRow = tblSocMed.getSelectionModel().getSelectedIndex() + 1;
          if(pnRow == 0) { return;}
          getSelectedItem();
          //TableClientAddress ti = (TableClientAddress) tblAddress.getItems().get(pnRow);
          
          tblSocMed.setOnKeyReleased((KeyEvent t)-> {
                KeyCode key = t.getCode();
                switch (key){
                    case DOWN:
                        pnRow = tblSocMed.getSelectionModel().getSelectedIndex();
                        if (pnRow == tblSocMed.getItems().size()) {
                            pnRow = tblSocMed.getItems().size();
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
                        pnRows = tblSocMed.getSelectionModel().getSelectedIndex() + 1;
                            pnRow = pnRows; 
                            getSelectedItem();
                        break;
                    default:
                        return; 
                }
            });
     }
     
     /*
     public void tblAddress_Column(){
          addrindex01.prefWidthProperty().bind(tblAddress.widthProperty().multiply(0.04));
          addrindex02.prefWidthProperty().bind(tblAddress.widthProperty().multiply(0.250));
          addrindex03.prefWidthProperty().bind(tblAddress.widthProperty().multiply(0.14));
          addrindex04.prefWidthProperty().bind(tblAddress.widthProperty().multiply(0.14));
          addrindex05.prefWidthProperty().bind(tblAddress.widthProperty().multiply(0.14));

          addrindex01.setResizable(false);  
          addrindex02.setResizable(false);  
          addrindex03.setResizable(false);  
          addrindex04.setResizable(false);  
          addrindex05.setResizable(false);   
     }
     */
     
     /*
     public void tblContact_Column(){
          contindex01.prefWidthProperty().bind(tblContact.widthProperty().multiply(0.04));
          contindex02.prefWidthProperty().bind(tblContact.widthProperty().multiply(0.250));
          contindex03.prefWidthProperty().bind(tblContact.widthProperty().multiply(0.14));

          contindex01.setResizable(false);  
          contindex02.setResizable(false);  
          contindex03.setResizable(false);   
     }
     */
     
     /*Convert Date to String*/
     private LocalDate strToDate(String val){
          DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
          LocalDate localDate = LocalDate.parse(val, date_formatter);
          return localDate;
     }
     
     /*Set Date Value to Master Class*/
     public void getDate(ActionEvent event) { 
          try {
               oTrans.setMaster(11,SQLUtil.toDate(txtField11.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
          }catch (SQLException ex) {
               Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
          }
     }
     
     /*Set ComboBox Value to Master Class*/ 
     @SuppressWarnings("ResultOfMethodCallIgnored")
     private boolean setSelection(){
          try {
               if (comboBox07.getSelectionModel().getSelectedIndex() < 0){
                   ShowMessageFX.Warning("No `Title` selected.", pxeModuleName, "Please select `Title` value.");
                   comboBox07.requestFocus();
                   return false;
               }else 
                   oTrans.setMaster(7, String.valueOf(comboBox07.getSelectionModel().getSelectedIndex()));

               if (comboBox08.getSelectionModel().getSelectedIndex() < 0){
                   ShowMessageFX.Warning("No `Gender` selected.", pxeModuleName, "Please select `Gender` value.");
                   comboBox08.requestFocus();
                   return false;
               }else 
                  oTrans.setMaster(8, String.valueOf(comboBox08.getSelectionModel().getSelectedIndex()));

               if (comboBox09.getSelectionModel().getSelectedIndex() < 0){
                   ShowMessageFX.Warning("No `Civil Status` selected.", pxeModuleName, "Please select `Civil Status` value.");
                   comboBox09.requestFocus();
                   return false;
               }else 
                  oTrans.setMaster(9, String.valueOf(comboBox09.getSelectionModel().getSelectedIndex()));

               if (comboBox18.getSelectionModel().getSelectedIndex() < 0){
                   ShowMessageFX.Warning("No `Client Type` selected.", pxeModuleName, "Please select `Client Type` value.");
                   comboBox18.requestFocus();
                   return false;
               }else 
                  oTrans.setMaster(18, String.valueOf(comboBox18.getSelectionModel().getSelectedIndex()));
              
               /*Address
               if (checkBox12Addr.isSelected()){
                  oTransAddress.setAddress(12, 1);
               } else
                  oTransAddress.setAddress(12, 0);  
               if (checkBox13Addr.isSelected()){
                  oTransAddress.setAddress(13, 1);
               } else
                  oTransAddress.setAddress(13, 0);
               if (checkBox14Addr.isSelected()){
                  oTransAddress.setAddress(14, 1);
               } else
                  oTransAddress.setAddress(14, 0);
               if (checkBox17Addr.isSelected()){
                  oTransAddress.setAddress(17, 1);
               } else
                  oTransAddress.setAddress(17, 0);
               if (radiobtn18AddY.isSelected()){
                  oTransAddress.setAddress(18, 1);
               } else
                  oTransAddress.setAddress(18, 0);
               */
          } catch (SQLException ex) {
          ShowMessageFX.Warning(getStage(),ex.getMessage(), "Warning", null);
          }

          return true;
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
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 13:
                        case 14:
                        case 16:
                             oTrans.setMaster(lnIndex, lsValue); //Handle Encoded Value
                             break; 
                        case 10:
                             oTrans.setMaster(24, lsValue); //Handle Encoded Value
                             break; 
                        case 12:
                             oTrans.setMaster(25, lsValue); //Handle Encoded Value
                             break;  
                        case 25:
                             oTrans.setMaster(28, lsValue); //Handle Encoded Value
                             break;
         //                    case 29: // Business Style
         //                         oTrans.setMaster(25, lsValue); //Handle Encoded Value
         //                         break;    

                    }
                
            } else
               txtField.selectAll();
          } catch (SQLException ex) {
            Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
          }
     };
     
     /*Set TextArea to Master Class*/
     final ChangeListener<? super Boolean> txtArea_Focus = (o,ov,nv)->{ 

          TextArea txtField = (TextArea)((ReadOnlyBooleanPropertyBase)o).getBean();
          int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
          String lsValue = txtField.getText();
          String txtFieldID = txtField.getId();
          
          if (lsValue == null) return;
          try {
             if(!nv){ /*Lost Focus*/
               if (txtFieldID.length() == 10) {
                    switch (lnIndex){
                        case 15:
                           oTrans.setMaster(lnIndex, lsValue); break;
                    }
               } else {
                    /*
                    String txtFieldcat = txtField.getId().substring(10, 13);
                    if (txtFieldcat.equals("Add")){ //set text field focus to Address
                     switch (lnIndex){
                         case 11:
                             oTransAddress.setAddress(lnIndex, lsValue); //Handle Encoded Value
                         break;
                          
                     }
                    }
                    */
               }
             } else
                 txtField.selectAll();
          } catch (SQLException e) {
             ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
             System.exit(1);
          }
     };
  
     public void tblEmail_Column(){
          emadindex01.prefWidthProperty().bind(tblEmail.widthProperty().multiply(0.04));
          emadindex02.prefWidthProperty().bind(tblEmail.widthProperty().multiply(0.14));
          emadindex03.prefWidthProperty().bind(tblEmail.widthProperty().multiply(0.100));

          emadindex01.setResizable(false);  
          emadindex02.setResizable(false);  
          emadindex03.setResizable(false);   
     }
     
     public void tblSocMed_Column(){
          socmindex01.prefWidthProperty().bind(tblSocMed.widthProperty().multiply(0.04));
          socmindex02.prefWidthProperty().bind(tblSocMed.widthProperty().multiply(0.14));
          socmindex03.prefWidthProperty().bind(tblSocMed.widthProperty().multiply(0.100));

          socmindex01.setResizable(false);  
          socmindex02.setResizable(false);  
          socmindex03.setResizable(false);   
     }

     /*TRIGGER FOCUS*/
     private void txtField_KeyPressed(KeyEvent event){
          TextField txtField = (TextField)event.getSource();
          int lnIndex = Integer.parseInt(((TextField)event.getSource()).getId().substring(8,10));
          String txtFieldID = ((TextField) event.getSource()).getId();
          String lsValue = txtField.getText();
          try{
               switch (event.getCode()){
                    case F3:
                         switch (txtFieldID){ 
                              case "txtField01":  //Search by Client ID
                                   if (oTrans.SearchRecord(txtField01.getText(), true)){
                                        if (oTransAddress.OpenRecord(oTrans.getMaster("sClientID").toString(), false)){
                                             loadClientMaster();
                                             loadAddress();
                                             loadContact();
                                             loadEmail();
                                             loadSocialMedia();
                                             pnEditMode = EditMode.READY;
                                        } else {
                                             ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                                             pnEditMode = EditMode.UNKNOWN;
                                        }
                                   } else {
                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                                       pnEditMode = EditMode.UNKNOWN;
                                   }
                              break;
                              case "txtField26": //Search by Name
                                   if (oTrans.SearchRecord(txtField26.getText(), false)){
                                        if (oTransAddress.OpenRecord(oTrans.getMaster("sClientID").toString(), false)){
                                             loadClientMaster();
                                             loadAddress();
                                             loadContact();
                                             loadEmail();
                                             loadSocialMedia();
                                             pnEditMode = EditMode.READY;
                                        } else {
                                             ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                                             pnEditMode = EditMode.UNKNOWN;
                                        }
                                   } else {
                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                                       pnEditMode = EditMode.UNKNOWN;
                                   }
                              break;
                              
                              case "txtField10": //Citizenship
                                   if (oTrans.searchCitizenship(txtField10.getText(), false)){
                                        loadClientMaster();
                                          
                                   } else 
                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                              
                              break;
                              
                              case "txtField12": //BirthPlace
                                   if (oTrans.searchBirthplace(txtField12.getText(), false)){
                                        loadClientMaster();
                                   } else 
                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                              
                              break;
                              
                              case "txtField25": //Spouse 
                                   if (oTrans.searchSpouse(txtField25.getText(), false)){
                                        loadClientMaster();
                                   } else 
                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                              
                              break;
                              
                              case "txtField05Addr":  //Search by Town Address
                                   if (oTransAddress.searchTown(oTransAddress.getItemCount(),txtField05Addr.getText(),false)){
                                       //loadAddress_textfield();
                                        System.out.println("Town getItemCount >>> " + oTransAddress.getItemCount());
                                        txtField05Addr.setText((String) oTransAddress.getAddress(oTransAddress.getItemCount(),"sTownName"));
                                        txtField07Addr.setText((String) oTransAddress.getAddress(oTransAddress.getItemCount(),"sZippCode"));
                                   } else 
                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                              
                              break;
                              
                              case "txtField06Addr":  //Search by Brgy Address
                                   if (oTransAddress.searchBarangay(oTransAddress.getItemCount(),txtField06Addr.getText(), false)){
                                        //loadAddress_textfield();
                                        System.out.println("Brgy getItemCount >>> " + oTransAddress.getItemCount());
                                        txtField06Addr.setText((String) oTransAddress.getAddress(oTransAddress.getItemCount(),"sBrgyName"));
                                        
                                   } else 
                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                              break;
                         } 
                    break;
                    case TAB:
                    case ENTER:
                         switch (txtFieldID){ 
                              case "txtField10": //Citizenship
                                   if (oTrans.searchCitizenship(txtField10.getText(), false)){
                                        loadClientMaster();
                                          
                                   } else 
                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                              
                              break;
                              
                              case "txtField12": //BirthPlace
                                   if (oTrans.searchBirthplace(txtField12.getText(), false)){
                                        loadClientMaster();
                                   } else 
                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                              
                              break;
                              
                              case "txtField25": //Spouse 
                                   if (oTrans.searchSpouse(txtField25.getText(), false)){
                                        loadClientMaster();
                                   } else 
                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                              
                              break;
                              
                              case "txtField05Addr":  //Search by Town Address
                                   if (oTransAddress.searchTown(oTransAddress.getItemCount(),txtField05Addr.getText(),false)){
                                       //loadAddress_textfield();
                                        System.out.println("Town getItemCount >>> " + oTransAddress.getItemCount());
                                        txtField05Addr.setText((String) oTransAddress.getAddress(oTransAddress.getItemCount(),"sTownName"));
                                        txtField07Addr.setText((String) oTransAddress.getAddress(oTransAddress.getItemCount(),"sZippCode"));
                                   } else 
                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                              
                              break;
                              
                              case "txtField06Addr":  //Search by Brgy Address
                                   if (oTransAddress.searchBarangay(oTransAddress.getItemCount(),txtField06Addr.getText(), false)){
                                        //loadAddress_textfield();
                                        System.out.println("Brgy getItemCount >>> " + oTransAddress.getItemCount());
                                        txtField06Addr.setText((String) oTransAddress.getAddress(oTransAddress.getItemCount(),"sBrgyName"));
                                        
                                   } else 
                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                              break;
                         }
                    case DOWN:
                        CommonUtils.SetNextFocus(txtField);break;
                    case UP:
                        CommonUtils.SetPreviousFocus(txtField);break;
               }
          }catch(SQLException e){
                ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
          }
     }
     
     //Set CheckBox Action
     private void cmdCheckBox_Click(ActionEvent event) {
          String scbSel = ((CheckBox)event.getSource()).getId();
          switch (scbSel){
               /*client_address*/
               case "checkBox14Addr": //Primary
                    if (checkBox14Addr.isSelected()) {
                         //checkBox12Addr.setSelected(false);
                         //checkBox17Addr.setSelected(false); checkBox12Addr.setSelected(false); checkBox13Addr.setSelected(false);
                    } break;
               case "checkBox17Addr": // Current
                    if (checkBox17Addr.isSelected()) {
                         checkBox12Addr.setSelected(false);
                         //checkBox14Addr.setSelected(false); checkBox12Addr.setSelected(false); checkBox13Addr.setSelected(false);
                    } break;
               case "checkBox13Addr": // Provincial
                    if (checkBox13Addr.isSelected()) {
                         checkBox12Addr.setSelected(false);
                         //checkBox17Addr.setSelected(false); checkBox12Addr.setSelected(false); checkBox14Addr.setSelected(false);
                    } break;
               case "checkBox12Addr": // Office
                    if (checkBox12Addr.isSelected()) {
                         checkBox17Addr.setSelected(false); checkBox14Addr.setSelected(false); checkBox13Addr.setSelected(false);
                    } break;
               
          }
     }
     //Set Radiobuttons Action
     private void cmdRadioButton_Click(ActionEvent event) {
          String srdbSel = ((RadioButton)event.getSource()).getId();
          switch (srdbSel){
               /*client_social_media*/
               case "radiobtn05SocY":
                    cmdRadioButton(radiobtn05SocY,radiobtn05SocN); break;
               case "radiobtn05SocN":
                    cmdRadioButton(radiobtn05SocN,radiobtn05SocY); break;
               /*client_email_address*/
               case "radiobtn06EmaY":
                    cmdRadioButton(radiobtn06EmaY,radiobtn06EmaN); break;
               case "radiobtn06EmaN":
                    cmdRadioButton(radiobtn06EmaN,radiobtn06EmaY); break;
               case "radiobtn05EmaY":
                    cmdRadioButton(radiobtn05EmaY,radiobtn05EmaN); break;
               case "radiobtn05EmaN":
                    cmdRadioButton(radiobtn05EmaN,radiobtn05EmaY); break;
               /*client_mobile*/
               case "radiobtn11CntY":
                    cmdRadioButton(radiobtn11CntY,radiobtn11CntN); break;
               case "radiobtn11CntN":
                    cmdRadioButton(radiobtn11CntN,radiobtn11CntY); break;
               case "radiobtn14CntY":
                    cmdRadioButton(radiobtn14CntY,radiobtn14CntN); break;
               case "radiobtn14CntN":
                    cmdRadioButton(radiobtn14CntN,radiobtn14CntY); break;
               /*client_address*/
               case "radiobtn18AddY":
                    cmdRadioButton(radiobtn18AddY,radiobtn18AddN); break;
               case "radiobtn18AddN":
                    cmdRadioButton(radiobtn18AddN,radiobtn18AddY); break;
          }
     }
     //Select and Unselect Radio Button
     private void cmdRadioButton(RadioButton rdbsel,RadioButton rdbUnsel) {
          if (rdbsel.isSelected() && rdbUnsel.isSelected()) {
               rdbUnsel.setSelected(false);
          }
     }
   
     public void comboChange() {
          try {
               boolean bAction = true;
               if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
               /*     if(ShowMessageFX.OkayCancel(null, "Confirmation", "Are you sure, you want to change Client Type?") == true){
                    } else {
                        comboBox18.getSelectionModel().select(iClientType);      
                        return;
                    }
               */
                    if (comboBox18.getSelectionModel().getSelectedIndex() == 1 ){
                         bAction = true;
                         txtField16.setDisable(false); //company name
                         txtField29.setDisable(false); // Business Style
                         txtField02.clear(); //last name
                         txtField03.clear(); //first name
                         txtField04.clear(); //mid name
                         txtField06.clear(); //suffix 
                         txtField05.clear(); //maiden nametxtField11.setValue(strToDate(CommonUtils.xsDateShort((Date) oTrans.getMaster(11))));
                         txtField12.clear(); // birth plce
                         txtField10.clear(); //citizenship
                         txtField14.clear(); //LTO NO
                         comboBox07.getSelectionModel().select(Integer.parseInt((String)oTrans.getMaster(7)));
                         comboBox08.getSelectionModel().select(Integer.parseInt((String)oTrans.getMaster(8)));
                         comboBox09.getSelectionModel().select(Integer.parseInt((String)oTrans.getMaster(9)));
                         txtField25.clear(); // Spouse
                         System.out.println("ComboBox18 >>> "+comboBox18.getSelectionModel().getSelectedIndex()+" true" );
                    } else {
                         bAction = false;
                         txtField16.setDisable(true); //company name
                         txtField29.setDisable(true); // Business Style
                         txtField16.clear(); //company name
                         txtField29.clear(); // Business Style
                         System.out.println("ComboBox18 >>> "+comboBox18.getSelectionModel().getSelectedIndex()+" false" );
                    }
                    cmdClientType(true);
               }
          } catch (SQLException ex) {
              Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
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
          
          /*Client Master*/
          txtField14.setDisable(!lbShow); //LTO NO
          txtField13.setDisable(!lbShow); //TIN NO
          textArea15.setDisable(!lbShow); //Remarks
          comboBox18.setDisable(!lbShow); //Client type
        
          txtField02.setDisable(!lbShow); //last name
          txtField03.setDisable(!lbShow); //first name
          txtField04.setDisable(!lbShow); //mid name
          txtField06.setDisable(!lbShow); //suffix 
          txtField05.setDisable(!lbShow); //maiden name
          txtField12.setDisable(!lbShow); // birth plce
          txtField10.setDisable(!lbShow); //citizenship
          txtField11.setDisable(!lbShow); //bdate
          comboBox08.setDisable(!lbShow); //Gender
          comboBox09.setDisable(!lbShow); //Civil Stat
          comboBox07.setDisable(!lbShow); //Title
          txtField25.setDisable(!lbShow); // Spouse
          txtField16.setDisable(!lbShow); //company name
          txtField29.setDisable(!lbShow); // Business Style
          cmdClientType(lbShow);
          
          /*Address*/
          txtField03Addr.setDisable(!lbShow); //House No
          txtField04Addr.setDisable(!lbShow); //Street / Address
          txtField05Addr.setDisable(!lbShow); // Town
          txtField06Addr.setDisable(!lbShow); // Brgy
          txtField07Addr.setDisable(!lbShow); //Zip code
          radiobtn18AddY.setDisable(!lbShow); //Active status
          radiobtn18AddN.setDisable(!lbShow); //Active status
          checkBox14Addr.setDisable(!lbShow); //Primary
          checkBox17Addr.setDisable(!lbShow); //Current
          checkBox12Addr.setDisable(!lbShow); //Office
          checkBox13Addr.setDisable(!lbShow); //Provincial
          textArea11Addr.setDisable(!lbShow); // Address Remarks
          
          /*Contact No*/
          comboBox05Cont.setDisable(!lbShow); // Contact Ownership
          comboBox04Cont.setDisable(!lbShow); // Mobile Type
          txtField03Cont.setDisable(!lbShow);  //Mobile Number
          radiobtn14CntY.setDisable(!lbShow); // Contact Active Status
          radiobtn14CntN.setDisable(!lbShow); // Contact Active Status
          radiobtn11CntY.setDisable(!lbShow); // Contact Primary
          radiobtn11CntN.setDisable(!lbShow); // Contact Primary
          textArea13Cont.setDisable(!lbShow); // Contact Remarks
          
          /*Email Address*/
          comboBox04EmAd.setDisable(!lbShow); // Email Ownership
          radiobtn06EmaY.setDisable(!lbShow); // Email Active status
          radiobtn06EmaN.setDisable(!lbShow); // Email Active status
          radiobtn05EmaY.setDisable(!lbShow); // Email Primary
          radiobtn05EmaN.setDisable(!lbShow); // Email Primary
          txtField03EmAd.setDisable(!lbShow); // Email Address
          
          /*Social Media*/
          radiobtn05SocY.setDisable(!lbShow); // SocMed Active status
          radiobtn05SocN.setDisable(!lbShow); // SocMed Active status
          txtField03Socm.setDisable(!lbShow); // SocMed Account
          comboBox04Socm.setDisable(!lbShow); // SocMed Type
          
          btnAdd.setVisible(!lbShow);
          btnAdd.setManaged(!lbShow);
          //if lbShow = false hide btn          
          btnEdit.setVisible(false); 
          btnEdit.setManaged(false);
          btnSave.setVisible(lbShow);
          btnSave.setManaged(lbShow);
          
          btnTabAdd.setVisible(lbShow); 
          btnTabUpd.setVisible(lbShow);
          
          if (fnValue == EditMode.ADDNEW) {
               btnTabRem.setVisible(lbShow);
          } else {
               btnTabRem.setVisible(false);
          }
          if (fnValue == EditMode.READY) { //show edit if user clicked save / browse
               btnEdit.setVisible(true); 
               btnEdit.setManaged(true);
          }
          
          if(lbShow){
               /*Active Status*/
               radiobtn18AddY.setSelected(true); //Active status
               radiobtn14CntY.setSelected(true); // Contact Active Status
               radiobtn06EmaY.setSelected(true); // Email Active status
               radiobtn05SocY.setSelected(true); // SocMed Active status
          
          }
          

     }
     
     public void cmdClientType(boolean bValue){
          if(bValue) {
               if (comboBox18.getSelectionModel().getSelectedIndex() == 1 ){
                    txtField16.setDisable(false); //company name
                    txtField29.setDisable(false); // Business Style
                    txtField02.setDisable(bValue); //last name
                    txtField03.setDisable(bValue); //first name
                    txtField04.setDisable(bValue); //mid name
                    txtField06.setDisable(bValue); //suffix 
                    txtField05.setDisable(bValue); //maiden name
                    txtField12.setDisable(bValue); // birth plce
                    txtField10.setDisable(bValue); //citizenship
                    txtField11.setDisable(bValue); //bdate
                    comboBox08.setDisable(bValue); //Gender
                    comboBox09.setDisable(bValue); //Civil Stat
                    comboBox07.setDisable(bValue); //Title
                    txtField25.setDisable(bValue); // Spouse
               }else {
                    txtField16.setDisable(true); //company name
                    txtField29.setDisable(true); // Business Style
                    txtField02.setDisable(!bValue); //last name
                    txtField03.setDisable(!bValue); //first name
                    txtField04.setDisable(!bValue); //mid name
                    txtField06.setDisable(!bValue); //suffix 
                    txtField05.setDisable(!bValue); //maiden name
                    txtField12.setDisable(!bValue); // birth plce
                    txtField10.setDisable(!bValue); //citizenship
                    txtField11.setDisable(!bValue); //bdate
                    comboBox08.setDisable(!bValue); //Gender
                    comboBox09.setDisable(!bValue); //Civil Stat
                    comboBox07.setDisable(!bValue); //Title
                    txtField25.setDisable(!bValue); // Spouse
               }
          }
     }
     
     /*Clear Fields*/
     public void clearFields(){
          pnRow = 0;
          /*clear tables*/
          addressdata.clear();
          mobiledata.clear();
          emaildata.clear();
          socialmediadata.clear();
          
          /*Client Master*/
          txtField02.clear(); //last name
          txtField03.clear(); //first name
          txtField04.clear(); //mid name
          txtField06.clear(); //suffix 
          txtField05.clear(); //maiden name
          txtField12.clear(); // birth plce
          txtField16.clear(); //company name
          txtField10.clear(); //citizenship
          txtField14.clear(); //LTO NO
          txtField13.clear(); //TIN NO
          textArea15.clear(); //Remarks
          txtField25.clear(); // Spouse
          txtField29.clear(); // Business Style
          txtField26.clear(); // Client Search
          txtField01.clear(); // Client ID
          //txtField11.setValue(null); //bdate Do not clear bdate since script is already assigning value to prevent nullpointerexception
          comboBox18.setValue(null); //Client type
          comboBox08.setValue(null); //Gender
          comboBox09.setValue(null); //Civil Stat
          comboBox07.setValue(null); //Title
          
          /*Address*/
          txtField03Addr.clear(); //House No
          txtField04Addr.clear(); //Street / Address
          txtField05Addr.clear(); // Town
          txtField06Addr.clear(); // Brgy
          txtField07Addr.clear(); //Zip code
          radiobtn18AddY.setSelected(false); //Active status
          radiobtn18AddN.setSelected(false); //Active status
          checkBox14Addr.setSelected(false); //Primary
          checkBox17Addr.setSelected(false); //Current
          checkBox12Addr.setSelected(false); //Office
          checkBox13Addr.setSelected(false); //Provincial
          textArea11Addr.clear(); // Address Remarks
          
          /*Contact No*/
          comboBox05Cont.setValue(null); // Contact Ownership
          comboBox04Cont.setValue(null); // Mobile Type
          txtField03Cont.clear();  //Mobile Number
          radiobtn14CntY.setSelected(false); // Contact Active Status
          radiobtn14CntN.setSelected(false); // Contact Active Status
          radiobtn11CntY.setSelected(false); // Contact Primary
          radiobtn11CntN.setSelected(false); // Contact Primary
          textArea13Cont.clear(); // Contact Remarks
          
          /*Email Address*/
          comboBox04EmAd.setValue(null); // Email Ownership
          radiobtn06EmaY.setSelected(false); // Email Active status
          radiobtn06EmaN.setSelected(false); // Email Active status
          radiobtn05EmaY.setSelected(false); // Email Primary
          radiobtn05EmaN.setSelected(false); // Email Primary
          txtField03EmAd.clear(); // Email Address
          
          /*Social Media*/
          radiobtn05SocY.setSelected(false); // SocMed Active status
          radiobtn05SocN.setSelected(false); // SocMed Active status
          txtField03Socm.clear(); // SocMed Account
          comboBox04Socm.setValue(null); // SocMed Type
          
          /*Auto select combobox to 0
          comboBox09.getSelectionModel().select(0);
          comboBox08.getSelectionModel().select(0);
          comboBox18.getSelectionModel().select(0);
          comboBox07.getSelectionModel().select(0);
          comboBox04EmAd.getSelectionModel().select(0);
          comboBox04Socm.getSelectionModel().select(0);
          comboBox05Cont.getSelectionModel().select(0);
          comboBox04Cont.getSelectionModel().select(0);
          */
          
     }

/*    
     private Callback<DatePicker, DateCell> callB = new Callback<DatePicker, DateCell>() {
        @Override
        public DateCell call(final DatePicker param) {
            return new DateCell() {
                @Override
                public void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                    LocalDate today = LocalDate.now();
                    setDisable(empty || item.compareTo(today) < 0);
                }

            };
        }

     };
*/
     
}
