/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.views;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.constants.EditMode;

/**
 * FXML Controller class
 *
 * @author Arsiela
 * DATE CREATED 03-01-2023  
 */
public class CustomerFormController implements Initializable, ScreenInterface {
     private GRider oApp;
     unloadForm unload = new unloadForm(); //Used in Close Button
     private final String pxeModuleName = "Client Information"; //Form Title
     private int pnEditMode;//Modifying fields
     private double xOffset = 0; //For Opening Scene
     private double yOffset = 0; //For Opening Scene
     private int iTabCnt = 0;
     
     /*populate comboxes client_master*/
     ObservableList<String> cCvlStat = FXCollections.observableArrayList("Single", "Married", "Divorced", "Separated", "Widowed");
     ObservableList<String> cGender = FXCollections.observableArrayList("Female", "Male");
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
     ObservableList<String> cSocType = FXCollections.observableArrayList("Facebook", "Whatsup", "Intagram", "Others");
     @FXML
     private RadioButton radiobtn06SocY; // SocMed Active status
     @FXML
     private RadioButton radiobtn06SocN; // SocMed Active status
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
     
     private Stage getStage(){
          return (Stage) txtField01.getScene().getWindow();
     }
     /**
      * Initializes the controller class.
      */
     @Override
     public void initialize(URL url, ResourceBundle rb) {
          /*populate combobox*/
          comboBox09.setItems(cCvlStat);
          comboBox08.setItems(cGender);
          comboBox18.setItems(cCusttype);
          comboBox07.setItems(cTitle);
          comboBox04EmAd.setItems(cOwnEmAd); // Email Ownership 
          comboBox04Socm.setItems(cSocType); // SocMed Type 
          comboBox05Cont.setItems(cOwnCont); // Contact Ownership 
          comboBox04Cont.setItems(cTypCont); // Mobile Type 
          
          txtField11.setDayCellFactory(callB); //BDate
          
          txtField05Addr.setOnKeyPressed(this::txtField_KeyPressed); //Town
          txtField06Addr.setOnKeyPressed(this::txtField_KeyPressed); //Brgy
          txtField12.setOnKeyPressed(this::txtField_KeyPressed); //Birth Place
          txtField10.setOnKeyPressed(this::txtField_KeyPressed); //Citizenship
          txtField26.setOnKeyPressed(this::txtField_KeyPressed); //Customer Name Search
          txtField01.setOnKeyPressed(this::txtField_KeyPressed); //Customer ID Search
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
          radiobtn06SocY.setOnAction(this::cmdRadioButton_Click); 
          radiobtn06SocN.setOnAction(this::cmdRadioButton_Click); 
          
          /*Check box Clicked Event*/
          /*client_address*/
          checkBox14Addr.setOnAction(this::cmdCheckBox_Click); 
          checkBox17Addr.setOnAction(this::cmdCheckBox_Click);
          checkBox12Addr.setOnAction(this::cmdCheckBox_Click);
          checkBox13Addr.setOnAction(this::cmdCheckBox_Click);
     
          //Button Click Event
          btnTabAdd.setOnAction(this::cmdButton_Click); 
          btnTabRem.setOnAction(this::cmdButton_Click);
          btnAdd.setOnAction(this::cmdButton_Click);
          btnEdit.setOnAction(this::cmdButton_Click); 
          btnSave.setOnAction(this::cmdButton_Click); 
          btnClose.setOnAction(this::cmdButton_Click); 
          btnBrowse.setOnAction(this::cmdButton_Click);
          
          //Tab Process
          tabPCustCont.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
               @Override
               public void changed(ObservableValue<? extends Tab> observable, Tab oldTab, Tab newTab) {
                   String lsTab = newTab.getId();
                    //Set Tab Count Identifier
                    switch  (lsTab) {
                         case "tabAddrInf":
                              iTabCnt = 0;
                         break;
                         case "tabContNo":
                              iTabCnt = 1; 
                         break;
                         case "tabEmail":
                              iTabCnt = 2;  
                         break;
                         case "tabSocMed":
                              iTabCnt = 3; 
                         break;
                    }
               }  
          });
          
          
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
          ScreenInterface loControl;
          switch (lsButton){
               case "btnBrowse":
                   // if (oTrans.SearchRecord(txtSeeks05.getText(), false)){
                         //loadRecord();
                         pnEditMode = EditMode.READY;
                  //  }
                    break;
               case "btnAdd": //create new client
                    pnEditMode = EditMode.ADDNEW;
                    clearFields();
                    break;
               case "btnEdit": //modify client info
                    pnEditMode = EditMode.UPDATE;
                    break;
               case "btnSave": //save client info
                    pnEditMode = EditMode.READY;
                    break;                        
               case "btnClose": //close tab
                   if(ShowMessageFX.OkayCancel(null, "Close Tab", "Are you sure, do you want to close tab?") == true){
                         if (unload != null) {
                              unload.unloadForm(AnchorMain, oApp, "Customer");
                         }else {
                              // handle the case where unload is null
                              ShowMessageFX.Warning(null, "Warning", "Notify System Admin to Configure Null value at close button.");    
                         }
                         break;
                   } else
                       return;
          }
          initButton(pnEditMode);    
     }
     
     private void loadClientMaster(String userID){
          
     }
     
     private void loadAddress(String userID){
          
     }
     
     private void loadContact(String userID){
          
     }
     
     private void loadEmail(String userID){
          
     }
     
     private void loadSocialMedia(String userID){
          
     }

     /*Enabling / Disabling Fields*/
     private void initButton(int fnValue){
          /* NOTE:
               lbShow (FALSE)= invisible
               !lbShow (TRUE)= visible
          */
          boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

          /*Client Master*/
          txtField02.setDisable(!lbShow); //last name
          txtField03.setDisable(!lbShow); //first name
          txtField04.setDisable(!lbShow); //mid name
          txtField06.setDisable(!lbShow); //suffix 
          txtField05.setDisable(!lbShow); //maiden name
          txtField12.setDisable(!lbShow); // birth plce
          txtField16.setDisable(!lbShow); //company name
          txtField10.setDisable(!lbShow); //citizenship
          txtField14.setDisable(!lbShow); //LTO NO
          txtField13.setDisable(!lbShow); //TIN NO
          textArea15.setDisable(!lbShow); //Remarks
          txtField11.setDisable(!lbShow); //bdate
          comboBox18.setDisable(!lbShow); //Client type
          comboBox08.setDisable(!lbShow); //Gender
          comboBox09.setDisable(!lbShow); //Civil Stat
          comboBox07.setDisable(!lbShow); //Title
          txtField25.setDisable(!lbShow); // Spouse
          txtField29.setDisable(!lbShow); // Business Style
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
          radiobtn06SocY.setDisable(!lbShow); // SocMed Active status
          radiobtn06SocN.setDisable(!lbShow); // SocMed Active status
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
          btnTabRem.setVisible(lbShow);
          
          if (fnValue == EditMode.READY) { //show edit if user clicked save / browse
               btnEdit.setVisible(true); 
               btnEdit.setManaged(true);
          }

     }
     
     /*TRIGGER FOCUS*/
     private void txtField_KeyPressed(KeyEvent event){
          TextField txtField = (TextField)event.getSource();
          int lnIndex = Integer.parseInt(((TextField)event.getSource()).getId().substring(8,10));
          String lsValue = txtField.getText();
          
          switch (event.getCode()){
            case F3:
            case TAB:
            case ENTER:
            case DOWN:
                CommonUtils.SetNextFocus(txtField);break;
            case UP:
                CommonUtils.SetPreviousFocus(txtField);break;
            }
     }
     
     //Set CheckBox Action
     private void cmdCheckBox_Click(ActionEvent event) {
          String scbSel = ((CheckBox)event.getSource()).getId();
          switch (scbSel){
               /*client_address*/
               case "checkBox14Addr": //Primary
                    if (checkBox14Addr.isSelected()) {
                         checkBox12Addr.setSelected(false);
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
               case "radiobtn06SocY":
                    cmdRadioButton(radiobtn06SocY,radiobtn06SocN); break;
               case "radiobtn06SocN":
                    cmdRadioButton(radiobtn06SocN,radiobtn06SocY); break;
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
     
     final ChangeListener<? super Boolean> txtField_Focus = (o,ov,nv)->{
          //try{
            TextField txtField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
            int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
            String lsValue = txtField.getText();

            if (lsValue == null) return;

            if(!nv){ /*Lost Focus*/
                switch (lnIndex){}
            } else
               txtField.selectAll();
//          } catch (SQLException ex) {
//            Logger.getLogger(OrderPaymentTaggingController.class.getName()).log(Level.SEVERE, null, ex);
//          }
     };
     
     /*Clear Fields*/
     public void clearFields(){
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
          txtField11.setValue(null); //bdate
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
          radiobtn06SocY.setSelected(false); // SocMed Active status
          radiobtn06SocN.setSelected(false); // SocMed Active status
          txtField03Socm.clear(); // SocMed Account
          comboBox04Socm.setValue(null); // SocMed Type
     }
     
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

     
}
