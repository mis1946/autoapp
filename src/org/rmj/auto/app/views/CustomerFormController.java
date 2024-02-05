/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.views;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.auto.app.sales.InquiryVehicleSalesAdvancesFormController;
import org.rmj.auto.clients.base.ClientAddress;
import org.rmj.auto.clients.base.ClientMaster;
import org.rmj.auto.clients.base.ClientMobile;
import org.rmj.auto.clients.base.ClientEMail;
import org.rmj.auto.clients.base.ClientInformation;
import org.rmj.auto.clients.base.ClientSocMed;
import org.rmj.auto.clients.base.ClientVehicleInfo;

/**
 * FXML Controller class
 *
 * @author Arsiela DATE CREATED 03-01-2023
 */
public class CustomerFormController implements Initializable, ScreenInterface {
    private GRider oApp;
    private ClientMaster oTrans;
    private MasterCallback oListener;
    TextFieldAnimationUtil txtFieldAnimation = new TextFieldAnimationUtil();

    unloadForm unload = new unloadForm(); //Used in Close Button
    private final String pxeModuleName = "Customer"; //Form Title
    private int pnEditMode;//Modifying fields for Customer Entry
    private double xOffset = 0;
    private double yOffset = 0;
    private int pnRow = -1;
    private int lnCtr;
    private int iTabIndex = 0; //Set tab index
    
    /*populate tables Address, Mobile, Email and Social Media*/
    private ObservableList<CustomerTableAddress> addressdata = FXCollections.observableArrayList();
    private ObservableList<CustomerTableMobile> mobiledata = FXCollections.observableArrayList();
    private ObservableList<CustomerTableEmail> emaildata = FXCollections.observableArrayList();
    private ObservableList<CustomerTableSocialMedia> socialmediadata = FXCollections.observableArrayList();

    /*populate tables for customer vehicle info and vehicle history*/
    private ObservableList<CustomerTableVehicleInfo> vhclinfodata = FXCollections.observableArrayList();
    private ObservableList<CustomerTableVehicleInfo> coownvhclinfodata = FXCollections.observableArrayList();
    
    /*populate comboxes client_master*/
    ObservableList<String> cCvlStat = FXCollections.observableArrayList("SINGLE", "MARRIED", "DIVORCED", "SEPARATED", "WIDOWED");
    ObservableList<String> cGender = FXCollections.observableArrayList("MALE", "FEMALE");
    ObservableList<String> cCusttype = FXCollections.observableArrayList("CLIENT", "COMPANY");
    ObservableList<String> cTitle = FXCollections.observableArrayList("MR.", "MISS", "MRS.");
    
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnBrowse;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnClose;
    @FXML
    private TextField txtField01;
    @FXML
    private TextField txtField26;
    @FXML
    private TabPane tabPaneMain;
    @FXML
    private Tab tabCustomer;
    @FXML
    private ComboBox comboBox18;
    @FXML
    private TextField txtField16;
    @FXML
    private Label lbl_CName;
    @FXML
    private TextField txtField02;
    @FXML
    private TextField txtField03;
    @FXML
    private TextField txtField04;
    @FXML
    private TextField txtField06;
    @FXML
    private TextField txtField05;
    @FXML
    private TextField txtField10;
    @FXML
    private TextField txtField25;
    @FXML
    private TextField txtField12;
    @FXML
    private ComboBox comboBox08;
    @FXML
    private ComboBox comboBox09;
    @FXML
    private DatePicker txtField11;
    @FXML
    private ComboBox comboBox07;
    @FXML
    private Label lbl_LName;
    @FXML
    private Label lbl_MName;
    @FXML
    private Label lbl_title;
    @FXML
    private Label lbl_gender;
    @FXML
    private Label lbl_cvlstat;
    @FXML
    private TabPane tabPCustCont;
    @FXML
    private Tab tabAddrInf;
    @FXML
    private TableView tblAddress;
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
    private TableColumn addrindex06;
    @FXML
    private TableColumn addrindex07;
    @FXML
    private TableColumn addrindex08;
    @FXML
    private TableColumn addrindex09;
    @FXML
    private TableColumn addrindex10;
    @FXML
    private Tab tabContNo;
    @FXML
    private TableView tblContact;
    @FXML
    private TableColumn contindex01;
    @FXML
    private TableColumn contindex02;
    @FXML
    private TableColumn contindex03;
    @FXML
    private TableColumn contindex04;
    @FXML
    private TableColumn contindex05;
    @FXML
    private TableColumn contindex06;
    @FXML
    private TableColumn contindex07;
    @FXML
    private Tab tabEmail;
    @FXML
    private TableView tblEmail;
    @FXML
    private TableColumn emadindex01;
    @FXML
    private TableColumn emadindex02;
    @FXML
    private TableColumn emadindex03;
    @FXML
    private TableColumn emadindex04;
    @FXML
    private TableColumn emadindex05;
    @FXML
    private Tab tabSocMed;
    @FXML
    private TableView tblSocMed;
    @FXML
    private TableColumn socmindex01;
    @FXML
    private TableColumn socmindex02;
    @FXML
    private TableColumn socmindex03;
    @FXML
    private TableColumn socmindex04;
    @FXML
    private Button btnTabAdd;
    @FXML
    private Button btnTabRem;
    @FXML
    private TextField txtField14;
    @FXML
    private TextField txtField13;
    @FXML
    private TextArea textArea15;
    @FXML
    private Tab tabVhclInfo;
    @FXML
    private TableView tblViewVhclInfo;
    @FXML
    private TableColumn tblVhcllist01;
    @FXML
    private TableColumn tblVhcllist02;
    @FXML
    private TableColumn tblVhcllist03;
    @FXML
    private TableColumn tblVhcllist04;
    @FXML
    private TableColumn tblVhcllist06;
    @FXML
    private TableColumn tblVhcllist07;
    @FXML
    private TableColumn tblVhcllist05;
    @FXML
    private TableView tblViewCoVhclInfo;
    @FXML
    private TableColumn tblCoVhcllist01;
    @FXML
    private TableColumn tblCoVhcllist02;
    @FXML
    private TableColumn tblCoVhcllist03;
    @FXML
    private TableColumn tblCoVhcllist04;
    @FXML
    private TableColumn tblCoVhcllist06;
    @FXML
    private TableColumn tblCoVhcllist07;
    @FXML
    private TableColumn tblCoVhcllist05;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oListener = (int fnIndex, Object foValue) -> {
            System.out.println("Set Class Value " + fnIndex + "-->" + foValue);
        };
        
        oTrans = new ClientMaster(oApp, oApp.getBranchCode(), true);
        oTrans.setCallback(oListener);
        oTrans.setWithUI(true);
        
        initAddress();
        initContact();
        initEmail();
        initSocialMedia();
        initVehicleInfo();
        initCoVehicleInfo();
        
        cmbFieldAction();
        
        setCapsLockBehavior(txtField01);
        setCapsLockBehavior(txtField02);
        setCapsLockBehavior(txtField03);
        setCapsLockBehavior(txtField04);
        setCapsLockBehavior(txtField05);
        setCapsLockBehavior(txtField06);
        setCapsLockBehavior(txtField10);
        setCapsLockBehavior(txtField12);
        setCapsLockBehavior(txtField13);
        setCapsLockBehavior(txtField14);
        setCapsLockBehavior(txtField16);
        setCapsLockBehavior(txtField25);
        setCapsLockBehavior(txtField26);
        setCapsLockBehavior(textArea15);

        /*Required Fields Animation*/
        txtFieldAnimation.addRequiredFieldListener(txtField16);
        txtFieldAnimation.addRequiredFieldListener(txtField02);
        txtFieldAnimation.addRequiredFieldListener(txtField03);
        
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

        Pattern pattern;
        pattern = Pattern.compile("[[0-9][-]]*");
        txtField13.setTextFormatter(new InputTextFormatter(pattern));

        /*populate combobox*/
        comboBox09.setItems(cCvlStat);
        comboBox08.setItems(cGender);
        comboBox18.setItems(cCusttype);
        comboBox07.setItems(cTitle);

        txtField11.setOnAction(this::getDate);
        txtField12.setOnKeyPressed(this::txtField_KeyPressed); //Birth Place
        txtField10.setOnKeyPressed(this::txtField_KeyPressed); //Citizenship
        txtField26.setOnKeyPressed(this::txtField_KeyPressed); //Customer Name Search
        txtField01.setOnKeyPressed(this::txtField_KeyPressed); //Customer ID Search
        txtField25.setOnKeyPressed(this::txtField_KeyPressed); //Spouse

        //CLIENT Master
        txtField02.setOnKeyPressed(this::txtField_KeyPressed);
        txtField03.setOnKeyPressed(this::txtField_KeyPressed);
        txtField04.setOnKeyPressed(this::txtField_KeyPressed);
        txtField05.setOnKeyPressed(this::txtField_KeyPressed);
        txtField06.setOnKeyPressed(this::txtField_KeyPressed);
        txtField13.setOnKeyPressed(this::txtField_KeyPressed);
        txtField14.setOnKeyPressed(this::txtField_KeyPressed);
        txtField16.setOnKeyPressed(this::txtField_KeyPressed);
        textArea15.setOnKeyPressed(this::txtField_KeyPressed);
        textArea15.setOnKeyPressed(this::txtArea_KeyPressed);
        
        //Button Click Event
        btnTabAdd.setOnAction(this::cmdButton_Click);
        btnTabRem.setOnAction(this::cmdButton_Click);
        btnAdd.setOnAction(this::cmdButton_Click);
        btnEdit.setOnAction(this::cmdButton_Click);
        btnSave.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click);
        btnBrowse.setOnAction(this::cmdButton_Click);
        btnCancel.setOnAction(this::cmdButton_Click);
        
        /*Clear Fields*/
        clearFields();
        pnEditMode = EditMode.UNKNOWN;
        initButton(pnEditMode);
        
        Platform.runLater(() -> {
            if(oTrans.loadState()){
                pnEditMode = oTrans.getEditMode();
                loadClientMaster();
                loadAddress();
                loadContact();
                loadEmail();
                loadSocialMedia();
                loadVehicleInfoTable();
                loadCoOwnVehicleInfoTable();
                initButton(pnEditMode);
            } else {
                if(oTrans.getMessage().isEmpty()){
                }else{
                    ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                }
            }
        });
    }    

    private static void setCapsLockBehavior(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (textField.getText() != null) {
                textField.setText(newValue.toUpperCase());
            }
        });
    }

    private static void setCapsLockBehavior(TextArea textArea) {
        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (textArea.getText() != null) {
                textArea.setText(newValue.toUpperCase());
            }
        });
    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }
    
    private Stage getStage() {
        return (Stage) txtField01.getScene().getWindow();
    }
    
    private void cmbFieldAction(){
        //Update Class master
        comboBox18.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                comboChange();
                try {
                    oTrans.setMaster(18, String.valueOf(comboBox18.getSelectionModel().getSelectedIndex()));

                    if (oTrans.getMaster(18).toString().equals("0")) {
                        oTrans.setMaster(16, ""); //sCompnyNm
                        oTrans.setMaster(7, "0"); //sTitlexxx
                        oTrans.setMaster(8, "0"); //cGenderCd
                        oTrans.setMaster(9, "0"); //cCvilStat
                        oTrans.setMaster(16, ""); //sCompnyNm
                        oTrans.setMaster(11, oApp.getServerDate());
                    } else {
                        oTrans.setMaster(2, ""); //sLastName
                        oTrans.setMaster(3, ""); //sFrstName
                        oTrans.setMaster(4, ""); //sMiddName
                        oTrans.setMaster(5, ""); //sMaidenNm
                        oTrans.setMaster(6, ""); //sSuffixNm
                        oTrans.setMaster(10, ""); //sCitizenx
                        oTrans.setMaster(12, ""); //sBirthPlc
                        oTrans.setMaster(24, ""); //sCntryNme
                        oTrans.setMaster(25, ""); //sTownName
                        oTrans.setMaster(27, ""); //sSpouseID
                        oTrans.setMaster(28, ""); //sSpouseNm
                        oTrans.setMaster(16, ""); //sCompnyNm

                        oTrans.setMaster(7, ""); //sTitlexxx
                        oTrans.setMaster(8, ""); //cGenderCd
                        oTrans.setMaster(9, ""); //cCvilStat
                        oTrans.setMaster(11, LocalDate.of(1900, Month.JANUARY, 1));
                    }
                    loadClientMaster();

                } catch (SQLException ex) {
                    Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });

        comboBox07.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                try {
                    if(comboBox07.getSelectionModel().getSelectedIndex() >= 0){
                        oTrans.setMaster(7, String.valueOf(comboBox07.getSelectionModel().getSelectedIndex()));
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        comboBox08.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                try {
                    if(comboBox08.getSelectionModel().getSelectedIndex() >= 0){
                        oTrans.setMaster(8, String.valueOf(comboBox08.getSelectionModel().getSelectedIndex()));
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        comboBox09.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                try {
                    if (comboBox09.getSelectionModel().getSelectedIndex() == 0) {
                        txtField25.setText("");
                        txtField25.setDisable(true);
                        oTrans.setMaster(27, "");
                        oTrans.setMaster(28, "");
                    } else {
                        txtField25.setDisable(false);
                    }
                    if(comboBox09.getSelectionModel().getSelectedIndex() >= 0){
                        oTrans.setMaster(9, String.valueOf(comboBox09.getSelectionModel().getSelectedIndex()));
                    } 
                } catch (SQLException ex) {
                    Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        txtField12.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                    if (newValue.isEmpty()) {
                        try {
                            oTrans.setMaster(12, "");
                            oTrans.setMaster(25, "");
                        } catch (SQLException ex) {
                            Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        });

        txtField25.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                    if (newValue.isEmpty()) {
                        try {
                            oTrans.setMaster(27, "");
                            oTrans.setMaster(28, "");
                        } catch (SQLException ex) {
                            Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        });

        txtField10.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if(newValue != null){
                    if (newValue.isEmpty()) {
                        try {
                            oTrans.setMaster(10, "");
                            oTrans.setMaster(24, "");
                        } catch (SQLException ex) {
                            Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        });

        //Tab Process
        tabPCustCont.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldTab, Tab newTab) {
                pnRow = 0;
                btnTabRem.setVisible(false);
            }
        });
    }
    
    /*BUTTON CLICKED*/
    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        int iCntp = 0;
        iTabIndex = tabPCustCont.getSelectionModel().getSelectedIndex();
        try {
            switch (lsButton) {
                case "btnAdd": //create new client
                    clearFields();
                    /*Clear tables*/
                    addressdata.clear();
                    mobiledata.clear();
                    emaildata.clear();
                    socialmediadata.clear();
                    vhclinfodata.clear();
                    coownvhclinfodata.clear();
                    if (oTrans.NewRecord()) {
                        loadClientMaster();
                        txtField26.clear(); // CLIENT Search
                        txtField01.clear(); // CLIENT ID
                        pnEditMode = oTrans.getEditMode();
                    } else {
                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                    }
                    break;
                case "btnEdit":
                    /*CLIENT INFORMATION*/
                    //modify client info
                    if (oTrans.UpdateRecord()) {;
                        pnEditMode = oTrans.getEditMode();
                    } else {
                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                    }
                    
                    break;
                case "btnSave": //save client info
                    //Do not Allow to save CLIENT Info if there's no Address / Mobile
                    if (pnEditMode == EditMode.ADDNEW) {
                        if (oTrans.oTransAddress.getItemCount() <= 0) {
                            ShowMessageFX.Warning(getStage(), "Please Add atleast 1 Address.", "Warning", pxeModuleName);
                            return;
                        } 
                        if (oTrans.oTransMobile.getItemCount() <= 0) {
                            ShowMessageFX.Warning(getStage(), "Please Add atleast 1 Contact Number.", "Warning", pxeModuleName);
                            return;
                        }
                    }
                    //Do not Allow to save CLIENT Info if there's no Primary Address / Mobile
                    for (lnCtr = 1; lnCtr <= oTrans.oTransAddress.getItemCount(); lnCtr++) {
                        if (oTrans.oTransAddress.getAddress(lnCtr, "cPrimaryx").toString().equals("1")) {
                            iCntp = iCntp + 1;
                        }
                    }
                    if (iCntp <= 0) {
                        ShowMessageFX.Warning(getStage(), "Please Add Primary Address.", "Warning", pxeModuleName);
                        return;
                    }

                    iCntp = 0;
                    for (lnCtr = 1; lnCtr <= oTrans.oTransMobile.getItemCount(); lnCtr++) {
                        if (oTrans.oTransMobile.getMobile(lnCtr, "cPrimaryx").toString().equals("1")) {
                            iCntp = iCntp + 1;
                        }
                    }
                    if (iCntp <= 0) {
                        ShowMessageFX.Warning(getStage(), "Please Add Primary Contact Number.", "Warning", pxeModuleName);
                        return;
                    }

                    //Proceed Saving
                    if (setSelection()) {
                        if (oTrans.SaveRecord()) {
                            ShowMessageFX.Information(getStage(), "Customer saved successfully.", "Client Information", null);
                            pnEditMode = oTrans.getEditMode();
                        } else {
                            ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", "Error while saving Customer Information");
                            return;
                        }
                    } else {
                        return;
                    }
                    //Retrieve saved data
                    if (oTrans.SearchRecord(oTrans.getMaster("sClientID").toString(), true)) {
                        loadClientMaster();
                        loadAddress();
                        loadContact();
                        loadEmail();
                        loadSocialMedia();
                        loadVehicleInfoTable();
                        loadCoOwnVehicleInfoTable();
                        pnEditMode = oTrans.getEditMode();
                    } else {
                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                        txtField26.clear(); // CLIENT Search
                        txtField01.clear(); // CLIENT ID
                        clearFields();
                        pnEditMode = EditMode.UNKNOWN;
                    }
                    break;
                case "btnClose": //close tab
                    if (ShowMessageFX.OkayCancel(null, "Close Tab", "Are you sure you want to close this Tab?") == true) {
                        if (unload != null) {
                            unload.unloadForm(AnchorMain, oApp, "Customer");
                        } else {
                            ShowMessageFX.Warning(getStage(), "Please notify the system administrator to configure the null value at the close button.", "Warning", pxeModuleName);
                        }
                        break;
                    } else {
                        return;
                    }
                case "btnCancel":
                    if (ShowMessageFX.OkayCancel(getStage(), "Are you sure you want to cancel?", pxeModuleName, null) == true) {
                        removeRequired();
                        clearFields();
                        addressdata.clear();
                        mobiledata.clear();
                        emaildata.clear();
                        socialmediadata.clear();
                        vhclinfodata.clear();
                        coownvhclinfodata.clear();
                        pnEditMode = EditMode.UNKNOWN;
                    }
                    break;
                case "btnTabAdd":
                    switch (iTabIndex) {
                        case 0:
                            oTrans.oTransAddress.addAddress();
                            loadAddressForm(oTrans.oTransAddress.getItemCount(), true);
                            loadAddress();
                            oTrans.saveState(oTrans.toJSONString());
                            break;
                        case 1: //Mobile
                            oTrans.oTransMobile.addMobile();
                            loadMobileForm(oTrans.oTransMobile.getItemCount(), true);
                            loadContact();
                            oTrans.saveState(oTrans.toJSONString());
                            break;
                        case 2: //Email
                            oTrans.oTransEmail.addEmail();
                            loadEmailForm(oTrans.oTransEmail.getItemCount(), true);
                            loadEmail();
                            oTrans.saveState(oTrans.toJSONString());
                            break;
                        case 3:
                            oTrans.oTransSocMed.addSocMed();
                            loadSocialMediaForm(oTrans.oTransSocMed.getItemCount(), true);
                            loadSocialMedia();
                            oTrans.saveState(oTrans.toJSONString());
                            break;
                    }
                    break;
                case "btnTabRem":
                    if (pnRow == 0) {
                        ShowMessageFX.Warning(getStage(), "No selected item!", "Warning", pxeModuleName);
                        return;
                    }
                    switch (iTabIndex) {
                        case 0:
                            if (ShowMessageFX.OkayCancel(null, "Confirmation", "Are you sure you want to remove this Client Address?") == true) {
                            } else {
                                return;
                            }
                            oTrans.oTransAddress.removeAddress(pnRow);
                            pnRow = 0;
                            loadAddress();
                            oTrans.saveState(oTrans.toJSONString());
                            break;
                        case 1://Mobile
                            if (ShowMessageFX.OkayCancel(null, "Confirmation", "Are you sure you want to remove this Client Mobile?") == true) {
                            } else {
                                return;
                            }
                            oTrans.oTransMobile.removeMobile(pnRow);
                            pnRow = 0;
                            loadContact();
                            oTrans.saveState(oTrans.toJSONString());
                            break;
                        case 2://Email
                            if (ShowMessageFX.OkayCancel(null, "Confirmation", "Are you sure you want to remove this  Client Email?") == true) {
                            } else {
                                return;
                            }
                            oTrans.oTransEmail.removeEmail(pnRow);
                            pnRow = 0;
                            loadEmail();
                            oTrans.saveState(oTrans.toJSONString());
                            break;
                        case 3://Social Media
                            if (ShowMessageFX.OkayCancel(null, "Confirmation", "Are you sure you want to remove this  Client Social Media?") == true) {
                            } else {
                                return;
                            }
                            oTrans.oTransSocMed.removeSocMed(pnRow);
                            pnRow = 0;
                            loadSocialMedia();
                            oTrans.saveState(oTrans.toJSONString());
                            break;
                    }

                    btnTabRem.setVisible(false);
                    break;

                case "btnBrowse":
                    boolean byClientID = true;
                    String txtValue = "";
                    if ((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE)) {
                        if (ShowMessageFX.OkayCancel(null, "Confirmation", "You have unsaved data. Are you sure you want to browse a new record?") == true) {
                        } else {
                            return;
                        }
                    }
                    
                    removeRequired();
                    clearFields();
                    addressdata.clear();
                    mobiledata.clear();
                    emaildata.clear();
                    socialmediadata.clear();
                    vhclinfodata.clear();
                    coownvhclinfodata.clear();
                    
                    if (!txtField26.getText().isEmpty() && !txtField26.getText().trim().equals("")) {
                        byClientID = false;
                        txtValue = txtField26.getText();
                    } else {
                        byClientID = true;
                        txtValue = txtField01.getText();
                    }

                    if (oTrans.SearchRecord(txtValue, byClientID)) {
                        loadClientMaster();
                        loadAddress();
                        loadContact();
                        loadEmail();
                        loadSocialMedia();
                        loadVehicleInfoTable();
                        loadCoOwnVehicleInfoTable();
                        pnEditMode = EditMode.READY;
                    } else {
                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                        txtField26.clear(); // CLIENT Search
                        txtField01.clear(); // CLIENT ID
                        pnEditMode = EditMode.UNKNOWN;
                        clearFields();
                    }

                    break;
            }

            initButton(pnEditMode);
        } catch (SQLException e) {
            e.printStackTrace();
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }
    }
    
    /*Set ComboBox Value to Master Class*/
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private boolean setSelection() {
        try {
            if (comboBox18.getSelectionModel().getSelectedIndex() < 0) {
                ShowMessageFX.Warning("No `Client Type` selected.", pxeModuleName, "Please select `Client Type` value.");
                comboBox18.requestFocus();
                return false;
            } else {
                oTrans.setMaster(18, String.valueOf(comboBox18.getSelectionModel().getSelectedIndex()));

                if (comboBox18.getSelectionModel().getSelectedIndex() == 0) {
                    if (comboBox07.getSelectionModel().getSelectedIndex() < 0) {
                        ShowMessageFX.Warning("No `Title` selected.", pxeModuleName, "Please select `Title` value.");
                        comboBox07.requestFocus();
                        return false;
                    } else {
                        oTrans.setMaster(7, String.valueOf(comboBox07.getSelectionModel().getSelectedIndex()));
                    }

                    if (comboBox08.getSelectionModel().getSelectedIndex() < 0) {
                        ShowMessageFX.Warning("No `Gender` selected.", pxeModuleName, "Please select `Gender` value.");
                        comboBox08.requestFocus();
                        return false;
                    } else {
                        oTrans.setMaster(8, String.valueOf(comboBox08.getSelectionModel().getSelectedIndex()));
                    }

                    if (comboBox09.getSelectionModel().getSelectedIndex() < 0) {
                        ShowMessageFX.Warning("No `Civil Status` selected.", pxeModuleName, "Please select `Civil Status` value.");
                        comboBox09.requestFocus();
                        return false;
                    } else {
                        oTrans.setMaster(9, String.valueOf(comboBox09.getSelectionModel().getSelectedIndex()));
                    }
                }

            }

        } catch (SQLException ex) {
            ShowMessageFX.Warning(getStage(), ex.getMessage(), "Warning", null);
        }

        return true;
    }

    private void loadAddressForm(Integer fnRow, boolean isAdd) {
        try {
            if (fnRow <= 0) {
                ShowMessageFX.Warning(getStage(), "Invalid Table Row to Set. ", "Warning", null);
                return;
            }

            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("CustomerAddressForm.fxml"));

            CustomerAddressFormController loControl = new CustomerAddressFormController();
            loControl.setGRider(oApp);
            loControl.setObject(oTrans.oTransAddress);
            loControl.setRow(fnRow);
            loControl.setState(isAdd);
            loControl.setOrigProv((String) oTrans.oTransAddress.getAddress(fnRow, 26));
            loControl.setOrigTown((String) oTrans.oTransAddress.getAddress(fnRow, 5));
            loControl.setOrigBrgy((String) oTrans.oTransAddress.getAddress(fnRow, 6));
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
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        } catch (SQLException ex) {
            Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadMobileForm(Integer fnRow, boolean isAdd) {
        try {
            if (fnRow <= 0) {
                ShowMessageFX.Warning(getStage(), "Invalid Table Row to Set. ", "Warning", null);
                return;
            }

            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("CustomerContactForm.fxml"));

            CustomerContactFormController loControl = new CustomerContactFormController();
            loControl.setGRider(oApp);
            loControl.setObject(oTrans.oTransMobile);
            loControl.setRow(fnRow);
            loControl.setState(isAdd);
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
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }

    private void loadEmailForm(Integer fnRow, boolean isAdd) {
        try {
            if (fnRow <= 0) {
                ShowMessageFX.Warning(getStage(), "Invalid Table Row to Set. ", "Warning", null);
                return;
            }

            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("CustomerEmailForm.fxml"));

            CustomerEmailFormController loControl = new CustomerEmailFormController();
            loControl.setGRider(oApp);
            loControl.setObject(oTrans.oTransEmail);
            loControl.setRow(fnRow);
            loControl.setState(isAdd);
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
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }

    private void loadSocialMediaForm(Integer fnRow, boolean isAdd) {
        try {
            if (fnRow <= 0) {
                ShowMessageFX.Warning(getStage(), "Invalid Table Row to Set. ", "Warning", null);
                return;
            }

            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("CustomerSocialMediaForm.fxml"));

            CustomerSocialMediaFormController loControl = new CustomerSocialMediaFormController();
            loControl.setGRider(oApp);
            loControl.setObject(oTrans.oTransSocMed);
            loControl.setRow(fnRow);
            loControl.setState(isAdd);
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
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }
    
    /*LOAD CLIENT INFORMATION*/
    private void loadClientMaster() {
        try {
            // Get the current event handler
            EventHandler<ActionEvent> eventHandler = comboBox18.getOnAction();
            comboBox18.setOnAction(null);
            comboBox18.getSelectionModel().select(Integer.parseInt((String) oTrans.getMaster(18)));
            comboBox18.setOnAction(eventHandler);
            
            txtField01.setText((String) oTrans.getMaster(1));
            txtField13.setText((String) oTrans.getMaster(13));
            txtField14.setText((String) oTrans.getMaster(14));
            textArea15.setText((String) oTrans.getMaster(15));
            
            if (comboBox18.getSelectionModel().getSelectedIndex() == 0) {
                txtField02.setText((String) oTrans.getMaster(2));
                txtField03.setText((String) oTrans.getMaster(3));
                txtField04.setText((String) oTrans.getMaster(4));
                txtField05.setText((String) oTrans.getMaster(5));
                txtField06.setText((String) oTrans.getMaster(6));
                txtField10.setText((String) oTrans.getMaster(24));
                txtField11.setValue(strToDate(CommonUtils.xsDateShort((Date) oTrans.getMaster(11))));
                txtField12.setText((String) oTrans.getMaster(25));
                txtField25.setText((String) oTrans.getMaster(28));
                comboBox07.getSelectionModel().select(Integer.parseInt((String) oTrans.getMaster(7)));
                comboBox08.getSelectionModel().select(Integer.parseInt((String) oTrans.getMaster(8)));
                comboBox09.getSelectionModel().select(Integer.parseInt((String) oTrans.getMaster(9)));
            } else {
                comboBox07.setValue("");
                comboBox08.setValue("");
                comboBox09.setValue("");
                txtField11.setValue(LocalDate.of(1900, Month.JANUARY, 1)); //birthdate
                txtField16.setText((String) oTrans.getMaster(16));
            }

        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }
    }
    
    private void loadVehicleInfoTable() {
        try {
            vhclinfodata.clear();
            if (oTrans.oTransVehicle.LoadList(oTrans.getMaster("sClientID").toString(), true)) {
                /*Set Values to table from vehicle info table*/
                for (lnCtr = 1; lnCtr <= oTrans.oTransVehicle.getItemCount(); lnCtr++) {
                    vhclinfodata.add(new CustomerTableVehicleInfo(
                            String.valueOf(lnCtr), //ROW
                            (String) oTrans.oTransVehicle.getDetail(lnCtr, 8),
                            (String) oTrans.oTransVehicle.getDetail(lnCtr, 20),
                            (String) oTrans.oTransVehicle.getDetail(lnCtr, 33),
                            (String) oTrans.oTransVehicle.getDetail(lnCtr, 9),
                            (String) oTrans.oTransVehicle.getDetail(lnCtr, 1),
                            (String) oTrans.oTransVehicle.getDetail(lnCtr, 6),
                            (String) oTrans.oTransVehicle.getDetail(lnCtr, 35),
                            (String) oTrans.oTransVehicle.getDetail(lnCtr, 36)
                    ));
                }
            }

        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }

    }

    private void loadCoOwnVehicleInfoTable() {
        try {
            coownvhclinfodata.clear();
            if (oTrans.oTransVehicle.LoadList(oTrans.getMaster("sClientID").toString(), false)) {
                /*Set Values to table from vehicle info table*/
                for (lnCtr = 1; lnCtr <= oTrans.oTransVehicle.getItemCount(); lnCtr++) {
                    coownvhclinfodata.add(new CustomerTableVehicleInfo(
                            String.valueOf(lnCtr), //ROW
                            (String) oTrans.oTransVehicle.getDetail(lnCtr, 8),
                            (String) oTrans.oTransVehicle.getDetail(lnCtr, 20),
                            (String) oTrans.oTransVehicle.getDetail(lnCtr, 33),
                            (String) oTrans.oTransVehicle.getDetail(lnCtr, 9),
                            (String) oTrans.oTransVehicle.getDetail(lnCtr, 1),
                            (String) oTrans.oTransVehicle.getDetail(lnCtr, 6),
                            (String) oTrans.oTransVehicle.getDetail(lnCtr, 35),
                            (String) oTrans.oTransVehicle.getDetail(lnCtr, 36)
                    ));
                }
            }

        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }

    }

    private void loadAddress() {
        String sAddress, sStatus, sPrimary, sCurrent, sProvincial, sOffice;

        try {
            addressdata.clear();
            /*Set Values to Class Address Master*/
            for (lnCtr = 1; lnCtr <= oTrans.oTransAddress.getItemCount(); lnCtr++) {
                sAddress = oTrans.oTransAddress.getAddress(lnCtr, "sAddressx").toString() + " " + oTrans.oTransAddress.getAddress(lnCtr, "sBrgyName").toString() + " " + oTrans.oTransAddress.getAddress(lnCtr, "sTownName").toString()+ ", " + oTrans.oTransAddress.getAddress(lnCtr, "sProvName").toString();

                if (oTrans.oTransAddress.getAddress(lnCtr, "cPrimaryx").toString().equals("1")) {
                    sPrimary = "Y";
                } else {
                    sPrimary = "N";
                }
                if (oTrans.oTransAddress.getAddress(lnCtr, "cCurrentx").toString().equals("1")) {
                    sCurrent = "Y";
                } else {
                    sCurrent = "N";
                }
                if (oTrans.oTransAddress.getAddress(lnCtr, "cProvince").toString().equals("1")) {
                    sProvincial = "Y";
                } else {
                    sProvincial = "N";
                }
                if (oTrans.oTransAddress.getAddress(lnCtr, "cOfficexx").toString().equals("1")) {
                    sOffice = "Y";
                } else {
                    sOffice = "N";
                }
                if (oTrans.oTransAddress.getAddress(lnCtr, "cRecdStat").toString().equals("1")) {
                    sStatus = "ACTIVE";
                } else {
                    sStatus = "INACTIVE";
                }
                //if (!sAddress.isEmpty() && !sAddress.trim().equals("")) {
                if ((!sAddress.isEmpty() && !sAddress.trim().equals("")) //&& !oTransAddress.getAddress(lnCtr, "sHouseNox").toString().isEmpty()
                        ) {
                    addressdata.add(new CustomerTableAddress(
                            String.valueOf(lnCtr), //ROW
                            sPrimary,
                            oTrans.oTransAddress.getAddress(lnCtr, "sHouseNox").toString(), //HOUSE NUMBER
                            sAddress.trim().toUpperCase(),
                            oTrans.oTransAddress.getAddress(lnCtr, "sZippCode").toString(),
                            sCurrent,
                            sProvincial,
                            sOffice,
                            sStatus,
                            oTrans.oTransAddress.getAddress(lnCtr, "sRemarksx").toString()
                    ));
                }
            }

        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }

    }

    private void loadContact() {
        String sOwnership, sPrimary, sType, sStatus;
        try {
            mobiledata.clear();
            /*Set Values to Class Mobile Master*/
            for (lnCtr = 1; lnCtr <= oTrans.oTransMobile.getItemCount(); lnCtr++) {

                switch (oTrans.oTransMobile.getMobile(lnCtr, "cOwnerxxx").toString()) {
                    case "0":
                        sOwnership = "PERSONAL";
                        break;
                    case "1":
                        sOwnership = "OFFICE";
                        break;
                    default:
                        sOwnership = "OTHERS";
                        break;
                }

                switch (oTrans.oTransMobile.getMobile(lnCtr, "cMobileTp").toString()) {
                    case "0":
                        sType = "MOBILE";
                        break;
                    case "1":
                        sType = "TELEPHONE";
                        break;
                    default:
                        sType = "FAX";
                        break;
                }

                if (oTrans.oTransMobile.getMobile(lnCtr, "cRecdStat").toString().equals("1")) {
                    sStatus = "ACTIVE";
                } else {
                    sStatus = "INACITVE";
                }

                if (oTrans.oTransMobile.getMobile(lnCtr, "cPrimaryx").toString().equals("1")) {
                    sPrimary = "Y";
                } else {
                    sPrimary = "N";
                }

                if (!oTrans.oTransMobile.getMobile(lnCtr, "sMobileNo").toString().trim().equals("")
                        || !oTrans.oTransMobile.getMobile(lnCtr, "sMobileNo").toString().trim().isEmpty()) {
                    mobiledata.add(new CustomerTableMobile(
                            String.valueOf(lnCtr), //ROW
                            sPrimary,
                            sOwnership, //OWNERSHIP
                            sType,
                            oTrans.oTransMobile.getMobile(lnCtr, "sMobileNo").toString(), //NUMBER
                            sStatus,
                            oTrans.oTransMobile.getMobile(lnCtr, "sRemarksx").toString()
                    ));
                }
            }
        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }
    }

    private void loadEmail() {
        String sOwnership, sPrimary, sStatus;
        try {
            emaildata.clear();
            /*Set Values to Class Mobile Master*/
            for (lnCtr = 1; lnCtr <= oTrans.oTransEmail.getItemCount(); lnCtr++) {
                if (oTrans.oTransEmail.getEmail(lnCtr, "cOwnerxxx").toString().equals("0")) {
                    sOwnership = "PERSONAL";
                } else if (oTrans.oTransEmail.getEmail(lnCtr, "cOwnerxxx").toString().equals("1")) {
                    sOwnership = "OFFICE";
                } else {
                    sOwnership = "OTHERS";
                }

                if (oTrans.oTransEmail.getEmail(lnCtr, "cPrimaryx").toString().equals("1")) {
                    sPrimary = "Y";
                } else {
                    sPrimary = "N";
                }

                if (oTrans.oTransEmail.getEmail(lnCtr, "cRecdStat").toString().equals("1")) {
                    sStatus = "ACTIVE";
                } else {
                    sStatus = "INACTIVE";
                }

                if (!oTrans.oTransEmail.getEmail(lnCtr, "sEmailAdd").toString().trim().equals("")
                        || !oTrans.oTransEmail.getEmail(lnCtr, "sEmailAdd").toString().trim().isEmpty()) {
                    emaildata.add(new CustomerTableEmail(
                            String.valueOf(lnCtr), //ROW
                            sPrimary,
                            sOwnership, //OWNERSHIP
                            oTrans.oTransEmail.getEmail(lnCtr, "sEmailAdd").toString(), //EMAIL
                            sStatus
                    ));
                }
            }
        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }

    }

    private void loadSocialMedia() {
        try {
            String sSocType = "";
            String sStatus = "";
            socialmediadata.clear();

            /*Set Values to Class Mobile Master*/
            for (lnCtr = 1; lnCtr <= oTrans.oTransSocMed.getItemCount(); lnCtr++) {
                if (oTrans.oTransSocMed.getSocMed(lnCtr, "cSocialTp").toString().equals("0")) {
                    sSocType = "FACEBOOK";
                } else if (oTrans.oTransSocMed.getSocMed(lnCtr, "cSocialTp").toString().equals("1")) {
                    sSocType = "WHATSAPP";
                } else if (oTrans.oTransSocMed.getSocMed(lnCtr, "cSocialTp").toString().equals("2")) {
                    sSocType = "INSTAGRAM";
                } else if (oTrans.oTransSocMed.getSocMed(lnCtr, "cSocialTp").toString().equals("3")) {
                    sSocType = "TIKTOK";
                } else if (oTrans.oTransSocMed.getSocMed(lnCtr, "cSocialTp").toString().equals("4")) {
                    sSocType = "TWITTER";
                } else {
                    sSocType = "OTHERS";
                }

                if (oTrans.oTransSocMed.getSocMed(lnCtr, "cRecdStat").toString().equals("1")) {
                    sStatus = "ACTIVE";
                } else {
                    sStatus = "INACTIVE";
                }
                if (!oTrans.oTransSocMed.getSocMed(lnCtr, "sAccountx").toString().trim().equals("")
                        || !oTrans.oTransSocMed.getSocMed(lnCtr, "sAccountx").toString().trim().isEmpty()) {
                    socialmediadata.add(new CustomerTableSocialMedia(
                            String.valueOf(lnCtr), //ROW
                            sSocType, //Social Type
                            oTrans.oTransSocMed.getSocMed(lnCtr, "sAccountx").toString(), //Acount
                            sStatus
                    ));
                }
            }
        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }
    }

    /*populate Address Table*/
    private void initAddress() {
        addrindex01.setCellValueFactory(new PropertyValueFactory<>("addrindex01"));
        addrindex02.setCellValueFactory(new PropertyValueFactory<>("addrindex02"));
        addrindex03.setCellValueFactory(new PropertyValueFactory<>("addrindex03"));
        addrindex04.setCellValueFactory(new PropertyValueFactory<>("addrindex04"));
        addrindex05.setCellValueFactory(new PropertyValueFactory<>("addrindex05"));
        addrindex06.setCellValueFactory(new PropertyValueFactory<>("addrindex06"));
        addrindex07.setCellValueFactory(new PropertyValueFactory<>("addrindex07"));
        addrindex08.setCellValueFactory(new PropertyValueFactory<>("addrindex08"));
        addrindex09.setCellValueFactory(new PropertyValueFactory<>("addrindex09"));
        addrindex10.setCellValueFactory(new PropertyValueFactory<>("addrindex10"));
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
        contindex04.setCellValueFactory(new PropertyValueFactory<>("contindex04"));
        contindex05.setCellValueFactory(new PropertyValueFactory<>("contindex05"));
        contindex06.setCellValueFactory(new PropertyValueFactory<>("contindex06"));
        contindex07.setCellValueFactory(new PropertyValueFactory<>("contindex07"));
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
        emadindex04.setCellValueFactory(new PropertyValueFactory<>("emadindex04"));
        emadindex05.setCellValueFactory(new PropertyValueFactory<>("emadindex05"));
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
        socmindex04.setCellValueFactory(new PropertyValueFactory<>("socmindex04"));

        tblSocMed.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblSocMed.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        socialmediadata.clear();
        tblSocMed.setItems(socialmediadata);
    }

    /*populate vheicle information Table*/
    private void initVehicleInfo() {
        tblVhcllist01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
        tblVhcllist02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
        tblVhcllist03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
        tblVhcllist04.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
        tblVhcllist05.setCellValueFactory(new PropertyValueFactory<>("tblindex05"));
        tblVhcllist06.setCellValueFactory(new PropertyValueFactory<>("tblindex08"));
        tblVhcllist07.setCellValueFactory(new PropertyValueFactory<>("tblindex09"));

        tblViewVhclInfo.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewVhclInfo.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

        tblViewVhclInfo.setItems(vhclinfodata);
    }

    /*populate vheicle information Table*/
    private void initCoVehicleInfo() {
        tblCoVhcllist01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
        tblCoVhcllist02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
        tblCoVhcllist03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
        tblCoVhcllist04.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
        tblCoVhcllist05.setCellValueFactory(new PropertyValueFactory<>("tblindex05"));
        tblCoVhcllist06.setCellValueFactory(new PropertyValueFactory<>("tblindex08"));
        tblCoVhcllist07.setCellValueFactory(new PropertyValueFactory<>("tblindex09"));

        tblViewCoVhclInfo.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewCoVhclInfo.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

        tblViewCoVhclInfo.setItems(coownvhclinfodata);
    }
    
    /*Set TextField Value to Master Class*/
    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {
        try {
            TextField txtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
            int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
            String lsValue = txtField.getText();

            if (lsValue == null) {
                return;
            }
            if (!nv) {
                /*Lost Focus*/
                switch (lnIndex) {
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
                }

            } else {
                txtField.selectAll();
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    };

    /*Set TextArea to Master Class*/
    final ChangeListener<? super Boolean> txtArea_Focus = (o, ov, nv) -> {

        TextArea txtField = (TextArea) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();

        if (lsValue == null) {
            return;
        }
        try {
            if (!nv) {
                /*Lost Focus*/
                switch (lnIndex) {
                    case 15:
                        oTrans.setMaster(lnIndex, lsValue);
                        break;
                }
            } else {
                txtField.selectAll();
            }
        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }
    };
    
    /*Convert Date to String*/
    private LocalDate strToDate(String val) {
        DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(val, date_formatter);
        return localDate;
    }

    /*Set Date Value to Master Class*/
    private void getDate(ActionEvent event) {
        try {
            /*CLIENT INFORMATION*/
            oTrans.setMaster(11, SQLUtil.toDate(String.valueOf(txtField11.getValue()), SQLUtil.FORMAT_SHORT_DATE));
        } catch (SQLException ex) {
            Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*TRIGGER FOCUS*/
    private void txtArea_KeyPressed(KeyEvent event) {
        if (event.getCode() == ENTER || event.getCode() == DOWN) {
            event.consume();
            CommonUtils.SetNextFocus((TextArea) event.getSource());
        } else if (event.getCode() == KeyCode.UP) {
            event.consume();
            CommonUtils.SetPreviousFocus((TextArea) event.getSource());
        }
    }

    /*CLIENT INFORMATION*/
    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        String txtFieldID = ((TextField) event.getSource()).getId();
        try {
            switch (event.getCode()) {
                case F3:
                case TAB:
                case ENTER:
                    switch (txtFieldID) {
                        case "txtField01":  //Search by CLIENT ID
                        case "txtField26": //Search by Name
                            boolean byClientID = true;
                            String txtValue = "";
                            if (txtFieldID.equals("txtField26")) { //Search by Name
                                byClientID = false;
                                txtValue = txtField26.getText();
                            } else {
                                byClientID = true;
                                txtValue = txtField01.getText();
                            }

                            if ((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE)) {
                                if (ShowMessageFX.OkayCancel(null, "Confirmation", "You have unsaved data. Are you sure you want to browse a new record?") == true) {
                                } else {
                                    return;
                                }
                            }
                            
                            clearFields();
                            addressdata.clear();
                            mobiledata.clear();
                            emaildata.clear();
                            socialmediadata.clear();
                            vhclinfodata.clear();
                            coownvhclinfodata.clear();
                            if (oTrans.SearchRecord(txtValue, byClientID)) {
                                loadClientMaster();
                                loadAddress();
                                loadContact();
                                loadEmail();
                                loadSocialMedia();
                                loadVehicleInfoTable();
                                loadCoOwnVehicleInfoTable();
                                pnEditMode = EditMode.READY;
                            } else {
                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                                txtField26.clear(); // CLIENT Search
                                txtField01.clear(); // CLIENT ID
                                pnEditMode = EditMode.UNKNOWN;
                                clearFields();
                            }
                            
                            initButton(pnEditMode);
                            break;
                        case "txtField10": //Citizenship
                            if (oTrans.searchCitizenship(txtField10.getText())) {
                            } else {
                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                            }

                            loadClientMaster();
                            break;

                        case "txtField12": //BirthPlace
                            if (oTrans.searchBirthplace(txtField12.getText())) {
                            } else {
                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                            }

                            loadClientMaster();
                            break;

                        case "txtField25": //Spouse
                            if (oTrans.searchSpouse(txtField25.getText())) {
                            } else {
                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                            }
                            
                            loadClientMaster();
                            break;
                    }
                    break;
            }

        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }
        
        switch (event.getCode()) {
            case ENTER:
            case DOWN:
                CommonUtils.SetNextFocus(txtField);
                break;
            case UP:
                CommonUtils.SetPreviousFocus(txtField);
        }
    }
    
    private void comboChange() {
        switch (comboBox18.getSelectionModel().getSelectedIndex()) {
            case 0:
                txtField16.clear(); //company name
                txtField16.getStyleClass().remove("required-field");
                break;
            case 1:
                txtField02.clear(); //last name
                txtField03.clear(); //first name
                txtField04.clear(); //mid name
                txtField06.clear(); //suffix
                txtField05.clear(); //maiden nametxtField11.setValue(strToDate(CommonUtils.xsDateShort((Date) oTrans.getMaster(11))));
                txtField12.clear(); // birth plce
                txtField10.clear(); //citizenship
                txtField14.clear(); //LTO NO
                txtField25.clear(); // Spouse
                comboBox07.setValue("");
                comboBox08.setValue("");
                comboBox09.setValue("");
                /*Clear Red Color for required fileds*/
                txtField02.getStyleClass().remove("required-field");
                txtField03.getStyleClass().remove("required-field");
                break;
        }

        cmdCLIENTType(true);
    }
    
    /*Enabling / Disabling Fields*/
    private void initButton(int fnValue) {
        pnRow = 0;
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

        /*CLIENT VEHICLE INFO TAB*/
        tabVhclInfo.setDisable(fnValue == EditMode.ADDNEW);

        /*CLIENT Master*/
        comboBox18.setDisable(!lbShow); //CLIENT type
        txtField14.setDisable(!lbShow); //LTO NO
        txtField13.setDisable(!lbShow); //TIN NO
        textArea15.setDisable(!lbShow); //Remarks
        txtField02.setDisable(true); //last name
        txtField03.setDisable(true); //first name
        txtField04.setDisable(true); //mid name
        txtField06.setDisable(true); //suffix
        txtField05.setDisable(true); //maiden name
        txtField12.setDisable(true); //birth plce
        txtField10.setDisable(true); //citizenship
        txtField11.setDisable(true); //bdate
        comboBox08.setDisable(true); //Gender
        comboBox09.setDisable(true); //Civil Stat
        comboBox07.setDisable(true); //Title
        txtField25.setDisable(true); //Spouse
        txtField16.setDisable(true); //company name
        cmdCLIENTType(lbShow);
        if (comboBox09.getSelectionModel().getSelectedIndex() == 0) {
            txtField25.setText("");
            txtField25.setDisable(true);
        } else {
            txtField25.setDisable(!lbShow);
        }
        
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        btnCancel.setVisible(lbShow);
        btnCancel.setManaged(lbShow);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);
        btnTabAdd.setVisible(lbShow);
        btnTabRem.setVisible(false);
        
        btnEdit.setVisible(false);
        btnEdit.setManaged(false);
        if (fnValue == EditMode.READY) { //show edit if user clicked save / browse
            btnEdit.setVisible(true);
            btnEdit.setManaged(true);
        }

        if (fnValue == EditMode.UPDATE || fnValue == EditMode.READY) {
            comboBox18.setDisable(true); 
        }
    }

    private void cmdCLIENTType(boolean bValue) {
        if (bValue) {
            boolean bCust = true;
            switch (comboBox18.getSelectionModel().getSelectedIndex()) {
                case 0:
                    bCust = true;
                    break;
                case 1:
                    bCust = false;
                    break;
            }
            
            txtField16.setDisable(bCust); //company name
            txtField02.setDisable(!bCust); //last name
            txtField03.setDisable(!bCust); //first name
            txtField04.setDisable(!bCust); //mid name
            txtField06.setDisable(!bCust); //suffix
            txtField05.setDisable(!bCust); //maiden name
            txtField12.setDisable(!bCust); // birth plce
            txtField10.setDisable(!bCust); //citizenship
            txtField11.setDisable(!bCust); //bdate
            comboBox08.setDisable(!bCust); //Gender
            comboBox09.setDisable(!bCust); //Civil Stat
            comboBox07.setDisable(!bCust); //Title
            txtField25.setDisable(!bCust); // Spouse
        }
    }
    
    /*Clear Fields*/
    private void clearFields() {
        pnRow = 0;
        /*clear tables*/
        addressdata.clear();
        mobiledata.clear();
        emaildata.clear();
        socialmediadata.clear();

        removeRequired();

        /*CLIENT Master*/
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
        txtField11.setValue(LocalDate.of(1900, Month.JANUARY, 1)); //birthdate

        //txtField11.setValue(null); //bdate Do not clear bdate since script is already assigning value to prevent nullpointerexception
        comboBox18.setValue(null); //CLIENT type
        comboBox08.setValue(null); //Gender
        comboBox09.setValue(null); //Civil Stat
        comboBox07.setValue(null); //Title
    }

    private void removeRequired() {
        txtFieldAnimation.removeShakeAnimation(txtField16, txtFieldAnimation.shakeTextField(txtField16), "required-field");
        txtFieldAnimation.removeShakeAnimation(txtField02, txtFieldAnimation.shakeTextField(txtField02), "required-field");
        txtFieldAnimation.removeShakeAnimation(txtField03, txtFieldAnimation.shakeTextField(txtField03), "required-field");

    }
    
    @FXML
    private void tblAddress_Clicked(MouseEvent event) {
        try {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                pnRow = tblAddress.getSelectionModel().getSelectedIndex() + 1;
                if (pnRow == 0) {
                    return;
                }

                if (event.getClickCount() == 2) {
                    loadAddressForm(pnRow, false);
                    loadAddress();
                }

                if (oTrans.oTransAddress.getAddress(pnRow, "sAddrssID").toString().isEmpty()) {
                    btnTabRem.setVisible(true);
                } else {
                    btnTabRem.setVisible(false);
                }

                tblAddress.setOnKeyReleased((KeyEvent t) -> {
                    KeyCode key = t.getCode();
                    switch (key) {
                        case DOWN:
                            pnRow = tblAddress.getSelectionModel().getSelectedIndex();
                            if (pnRow == tblAddress.getItems().size()) {
                                pnRow = tblAddress.getItems().size();
                            } else {
                                int y = 1;
                                pnRow = pnRow + y;
                            }
                            break;

                        case UP:
                            int pnRows = 0;
                            int x = -1;
                            pnRows = tblAddress.getSelectionModel().getSelectedIndex() + 1;
                            pnRow = pnRows;
                            break;
                        default:
                            return;
                    }

                    try {
                        if (oTrans.oTransAddress.getAddress(pnRow, "sAddrssID").toString().isEmpty()) {
                            btnTabRem.setVisible(true);
                        } else {
                            btnTabRem.setVisible(false);
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void tblContact_Clicked(MouseEvent event) {
        try {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                pnRow = tblContact.getSelectionModel().getSelectedIndex() + 1;
                if (pnRow == 0) {
                    return;
                }

                if (event.getClickCount() == 2) {
                    loadMobileForm(pnRow, false);
                    loadContact();
                }

                if (oTrans.oTransMobile.getMobile(pnRow, "sMobileID").toString().isEmpty()) {
                    btnTabRem.setVisible(true);
                } else {
                    btnTabRem.setVisible(false);
                }

                tblContact.setOnKeyReleased((KeyEvent t) -> {
                    KeyCode key = t.getCode();
                    switch (key) {
                        case DOWN:
                            pnRow = tblContact.getSelectionModel().getSelectedIndex();
                            if (pnRow == tblContact.getItems().size()) {
                                pnRow = tblContact.getItems().size();
                            } else {
                                int y = 1;
                                pnRow = pnRow + y;
                            }
                            break;
                        case UP:
                            int pnRows = 0;
                            int x = -1;
                            pnRows = tblContact.getSelectionModel().getSelectedIndex() + 1;
                            pnRow = pnRows;
                            break;
                        default:
                            return;
                    }

                    try {
                        if (oTrans.oTransMobile.getMobile(pnRow, "sMobileID").toString().isEmpty()) {
                            btnTabRem.setVisible(true);
                        } else {
                            btnTabRem.setVisible(false);
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void tblEmail_Clicked(MouseEvent event) {
        try {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                pnRow = tblEmail.getSelectionModel().getSelectedIndex() + 1;
                if (pnRow == 0) {
                    return;
                }
                if (event.getClickCount() == 2) {
                    loadEmailForm(pnRow, false);
                    loadEmail();
                }

                if (oTrans.oTransEmail.getEmail(pnRow, "sEmailIDx").toString().isEmpty()) {
                    btnTabRem.setVisible(true);
                } else {
                    btnTabRem.setVisible(false);
                }

                tblEmail.setOnKeyReleased((KeyEvent t) -> {
                    KeyCode key = t.getCode();
                    switch (key) {
                        case DOWN:
                            pnRow = tblEmail.getSelectionModel().getSelectedIndex();
                            if (pnRow == tblEmail.getItems().size()) {
                                pnRow = tblEmail.getItems().size();
                            } else {
                                int y = 1;
                                pnRow = pnRow + y;
                            }
                            break;
                        case UP:
                            int pnRows = 0;
                            int x = -1;
                            pnRows = tblEmail.getSelectionModel().getSelectedIndex() + 1;
                            pnRow = pnRows;
                            break;
                        default:
                            return;
                    }

                    try {
                        if (oTrans.oTransEmail.getEmail(pnRow, "sEmailIDx").toString().isEmpty()) {
                            btnTabRem.setVisible(true);
                        } else {
                            btnTabRem.setVisible(false);
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void tblSocMed_Clicked(MouseEvent event) {
        try {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                pnRow = tblSocMed.getSelectionModel().getSelectedIndex() + 1;
                if (pnRow == 0) {
                    return;
                }
                if (event.getClickCount() == 2) {
                    loadSocialMediaForm(pnRow, false);
                    loadSocialMedia();
                }

                if (oTrans.oTransSocMed.getSocMed(pnRow, "sSocialID").toString().isEmpty()) {
                    btnTabRem.setVisible(true);
                } else {
                    btnTabRem.setVisible(false);
                }

                tblSocMed.setOnKeyReleased((KeyEvent t) -> {
                    KeyCode key = t.getCode();
                    switch (key) {
                        case DOWN:
                            pnRow = tblSocMed.getSelectionModel().getSelectedIndex();
                            if (pnRow == tblSocMed.getItems().size()) {
                                pnRow = tblSocMed.getItems().size();
                            } else {
                                int y = 1;
                                pnRow = pnRow + y;
                            }
                            break;
                        case UP:
                            int pnRows = 0;
                            int x = -1;
                            pnRows = tblSocMed.getSelectionModel().getSelectedIndex() + 1;
                            pnRow = pnRows;
                            break;
                        default:
                            return;
                    }

                    try {
                        if (oTrans.oTransSocMed.getSocMed(pnRow, "sSocialID").toString().isEmpty()) {
                            btnTabRem.setVisible(true);
                        } else {
                            btnTabRem.setVisible(false);
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void tblViewVhclInfo_Clicked(MouseEvent event) {
    }
    
    
    
//
//    private GRider oApp;
//    private ClientMaster oTrans;
//    private MasterCallback oListener;
//    private ClientAddress oTransAddress;
//    private ClientMobile oTransMobile;
//    private ClientEMail oTransEmail;
//    private ClientSocMed oTransSocMed;
//    private ClientVehicleInfo oTransVehicle;
//    TextFieldAnimationUtil txtFieldAnimation = new TextFieldAnimationUtil();
//
//    //FXMLDocumentController load = new FXMLDocumentController(); //Used in open vehicle description at main tab Button
//    unloadForm unload = new unloadForm(); //Used in Close Button
//    private final String pxeModuleName = "Customer"; //Form Title
//    //private int selectedIndex;//Selected Tab Index
//    private int pnEditMode;//Modifying fields for Customer Entry
//    //private int pnVEditMode;//Modifying fields for Customer Vehicle Info
//    private double xOffset = 0;
//    private double yOffset = 0;
//    private int pnRow = -1;
//    private int lnCtr;
//    private int tbl_row = 0;
//    private int iTabIndex = 0; //Set tab index
//    //private boolean bBtnVhclAvl = false;
//    //private int iCLIENTType;  TextFieldAnimationUtil txtFieldAnimation = new TextFieldAnimationUtil();
//
//
//    /*populate tables Address, Mobile, Email and Social Media*/
//    private ObservableList<CustomerTableAddress> addressdata = FXCollections.observableArrayList();
//    private ObservableList<CustomerTableMobile> mobiledata = FXCollections.observableArrayList();
//    private ObservableList<CustomerTableEmail> emaildata = FXCollections.observableArrayList();
//    private ObservableList<CustomerTableSocialMedia> socialmediadata = FXCollections.observableArrayList();
//
//    /*populate tables for customer vehicle info and vehicle history*/
//    private ObservableList<CustomerTableVehicleInfo> vhclinfodata = FXCollections.observableArrayList();
//    private ObservableList<CustomerTableVehicleInfo> coownvhclinfodata = FXCollections.observableArrayList();
//    //private ObservableList<CustomerTableVehicleInfo> vhclhtrydata = FXCollections.observableArrayList();
//
//    /*populate comboxes client_master*/
//    ObservableList<String> cCvlStat = FXCollections.observableArrayList("SINGLE", "MARRIED", "DIVORCED", "SEPARATED", "WIDOWED");
//    ObservableList<String> cGender = FXCollections.observableArrayList("MALE", "FEMALE");
//    ObservableList<String> cCusttype = FXCollections.observableArrayList("CLIENT", "COMPANY");
//    ObservableList<String> cTitle = FXCollections.observableArrayList("MR.", "MISS", "MRS.");
//
//    @FXML
//    private TextField txtField01; //client id
//    @FXML
//    private TextField txtField02; //last name
//    @FXML
//    private TextField txtField03; //first name
//    @FXML
//    private TextField txtField04; //mid name
//    @FXML
//    private TextField txtField06; //suffix
//    @FXML
//    private TextField txtField05;  //maiden name
//    @FXML
//    private DatePicker txtField11; //bdate
//    @FXML
//    private TextField txtField12; // birth plce
//    @FXML
//    private TextField txtField16; //company name
//    @FXML
//    private TextField txtField10; //citizenship
//    @FXML
//    private ComboBox comboBox18; //CLIENT type
//    @FXML
//    private ComboBox comboBox08; //Gender
//    @FXML
//    private ComboBox comboBox09; //Civil Stat
//    @FXML
//    private ComboBox comboBox07; //Title
//    @FXML
//    private TextField txtField14; //LTO NO
//    @FXML
//    private TextField txtField13; //TIN NO
//    @FXML
//    private TextArea textArea15; //Remarks
//    @FXML
//    private TextField txtField25; // Spouse
//    @FXML
//    private TextField txtField26; // CLIENT Search
//
//    /*Contact No*/
//    ObservableList<String> cOwnCont = FXCollections.observableArrayList("PERSONAL", "OFFICE", "OTHERS");
//    ObservableList<String> cTypCont = FXCollections.observableArrayList("MOBILE", "TELEPHONE", "FAX");
//    /*Email Address*/
//    ObservableList<String> cOwnEmAd = FXCollections.observableArrayList("PERSONAL", "OFFICE", "OTHERS");
//    /*Social Media*/
//    ObservableList<String> cSocType = FXCollections.observableArrayList("FACEBOOK", "WHATSAPP", "INSTAGRAM", "TIKTOK", "TWITTER");
//
//
//    /*Buttons*/
//    @FXML
//    private Button btnTabAdd;
//    @FXML
//    private Button btnTabRem;
//    @FXML
//    private Button btnAdd;
//    @FXML
//    private Button btnEdit;
//    @FXML
//    private Button btnSave;
//    @FXML
//    private Button btnBrowse;
//    @FXML
//    private Button btnClose;
//    @FXML
//    private AnchorPane AnchorMain;
//    @FXML
//    private TabPane tabPCustCont;
//    @FXML
//    private Tab tabAddrInf;
//    @FXML
//    private Tab tabContNo;
//    @FXML
//    private Tab tabEmail;
//    @FXML
//    private Tab tabSocMed;
//    @FXML
//    private TableView tblAddress;
//    @FXML
//    private TableView tblContact;
//    @FXML
//    private TableView tblEmail;
//    @FXML
//    private TableView tblSocMed;
//    @FXML
//    private TableColumn addrindex01;
//    @FXML
//    private TableColumn addrindex02;
//    @FXML
//    private TableColumn addrindex03;
//    @FXML
//    private TableColumn addrindex04;
//    @FXML
//    private TableColumn addrindex05;
//    @FXML
//    private TableColumn addrindex06;
//    @FXML
//    private TableColumn addrindex07;
//    @FXML
//    private TableColumn addrindex08;
//    @FXML
//    private TableColumn addrindex09;
//    @FXML
//    private TableColumn addrindex10;
//    @FXML
//    private TableColumn contindex01;
//    @FXML
//    private TableColumn contindex02;
//    @FXML
//    private TableColumn contindex03;
//    @FXML
//    private TableColumn contindex04;
//    @FXML
//    private TableColumn contindex05;
//    @FXML
//    private TableColumn contindex06;
//    @FXML
//    private TableColumn contindex07;
//    @FXML
//    private TableColumn emadindex01;
//    @FXML
//    private TableColumn emadindex02;
//    @FXML
//    private TableColumn emadindex03;
//    @FXML
//    private TableColumn emadindex04;
//    @FXML
//    private TableColumn emadindex05;
//    @FXML
//    private TableColumn socmindex01;
//    @FXML
//    private TableColumn socmindex02;
//    @FXML
//    private TableColumn socmindex03;
//    @FXML
//    private TableColumn socmindex04;
//
//    @FXML
//    private TableView tblViewVhclInfo;
//    @FXML
//    private TableColumn tblVhcllist01;
//    @FXML
//    private TableColumn tblVhcllist02;
//    @FXML
//    private TableColumn tblVhcllist03;
//    @FXML
//    private TableColumn tblVhcllist04;
//    @FXML
//    private TableColumn tblVhcllist05;
//    @FXML
//    private TableColumn tblVhcllist07;
//    @FXML
//    private TableColumn tblVhcllist06;
//    @FXML
//    private TabPane tabPaneMain;
//    @FXML
//    private Tab tabCustomer;
//    @FXML
//    private Tab tabVhclInfo;
//    @FXML
//    private Label lbl_CName;
//    @FXML
//    private Label lbl_LName;
//    @FXML
//    private Label lbl_MName;
//    @FXML
//    private Label lbl_title;
//    @FXML
//    private Label lbl_gender;
//    @FXML
//    private Label lbl_cvlstat;
//    @FXML
//    private TableView tblViewCoVhclInfo;
//    @FXML
//    private TableColumn tblCoVhcllist01;
//    @FXML
//    private TableColumn tblCoVhcllist02;
//    @FXML
//    private TableColumn tblCoVhcllist03;
//    @FXML
//    private TableColumn tblCoVhcllist04;
//    @FXML
//    private TableColumn tblCoVhcllist06;
//    @FXML
//    private TableColumn tblCoVhcllist05;
//    @FXML
//    private TableColumn tblCoVhcllist07;
//    @FXML
//    private Button btnCancel;
//
//    private Stage getStage() {
//        return (Stage) txtField01.getScene().getWindow();
//    }
//
//    /**
//     * Initializes the controller class.
//     */
//    @Override
//    public void initialize(URL url, ResourceBundle rb) {
//        oListener = (int fnIndex, Object foValue) -> {
//            System.out.println("Set Class Value " + fnIndex + "-->" + foValue);
//        };
//
//        oTrans = new ClientMaster(oApp, oApp.getBranchCode(), true);
//        oTrans.setCallback(oListener);
//        oTrans.setWithUI(true);
//
//        oTransAddress = new ClientAddress(oApp, oApp.getBranchCode(), false);
//        oTransAddress.setCallback(oListener);
//        oTransAddress.setWithUI(true);
//        oTrans.setAddressObject(oTransAddress);
//        initAddress();
//
//        oTransMobile = new ClientMobile(oApp, oApp.getBranchCode(), false);
//        oTransMobile.setCallback(oListener);
//        oTransMobile.setWithUI(true);
//        oTrans.setMobileObject(oTransMobile);
//        initContact();
//
//        oTransEmail = new ClientEMail(oApp, oApp.getBranchCode(), false);
//        oTransEmail.setCallback(oListener);
//        oTransEmail.setWithUI(true);
//        oTrans.setEmailObject(oTransEmail);
//        initEmail();
//
//        oTransSocMed = new ClientSocMed(oApp, oApp.getBranchCode(), false);
//        oTransSocMed.setCallback(oListener);
//        oTransSocMed.setWithUI(true);
//        oTrans.setSocMedObject(oTransSocMed);
//        initSocialMedia();
//
//        setCapsLockBehavior(txtField01);
//        setCapsLockBehavior(txtField02);
//        setCapsLockBehavior(txtField03);
//        setCapsLockBehavior(txtField04);
//        setCapsLockBehavior(txtField05);
//        setCapsLockBehavior(txtField06);
//        setCapsLockBehavior(txtField10);
//        setCapsLockBehavior(txtField12);
//        setCapsLockBehavior(txtField13);
//        setCapsLockBehavior(txtField14);
//        setCapsLockBehavior(txtField16);
//        setCapsLockBehavior(txtField25);
//        setCapsLockBehavior(txtField26);
//        setCapsLockBehavior(textArea15);
//
//        /*Required Fields Animation*/
//        txtFieldAnimation.addRequiredFieldListener(txtField16);
//        txtFieldAnimation.addRequiredFieldListener(txtField02);
//        txtFieldAnimation.addRequiredFieldListener(txtField03);
////        addRequiredFieldListener(txtField03Addr);
////        addRequiredFieldListener(txtField05Addr);
////        addRequiredFieldListener(txtField06Addr);
////        addRequiredFieldListener(txtField03Cont);
//
////        // Add a listener to the textProperty of the TextField
////        //Plate number
////        txtField20V.textProperty().addListener((observable, oldValue, newValue) -> {
////            if (!txtField20V.getText().isEmpty() ){
////                txtField08V.getStyleClass().remove("required-field");
////            }
////        });
////        //CS Number
////        txtField08V.textProperty().addListener((observable, oldValue, newValue) -> {
////            if (!txtField08V.getText().isEmpty()){
////                txtField20V.getStyleClass().remove("required-field");
////            }
////        });
//        /*Set Focus to set Value to Class*/
//        txtField01.focusedProperty().addListener(txtField_Focus);
//        txtField02.focusedProperty().addListener(txtField_Focus);
//        txtField03.focusedProperty().addListener(txtField_Focus);
//        txtField04.focusedProperty().addListener(txtField_Focus);
//        txtField05.focusedProperty().addListener(txtField_Focus);
//        txtField06.focusedProperty().addListener(txtField_Focus);
//        txtField10.focusedProperty().addListener(txtField_Focus);
//        txtField12.focusedProperty().addListener(txtField_Focus);
//        txtField13.focusedProperty().addListener(txtField_Focus);
//        txtField14.focusedProperty().addListener(txtField_Focus);
//        txtField16.focusedProperty().addListener(txtField_Focus);
//        txtField25.focusedProperty().addListener(txtField_Focus);
//        textArea15.focusedProperty().addListener(txtArea_Focus);
//
//        CommonUtils.addTextLimiter(txtField06, 4); // Suffix
//        CommonUtils.addTextLimiter(txtField13, 15); // TIN
//        CommonUtils.addTextLimiter(txtField14, 15); // LTO
////        CommonUtils.addTextLimiter(txtField03Addr, 5); //HOUSE NO
////        CommonUtils.addTextLimiter(txtField03Cont, 12); //CONTACT NO
//
//        Pattern pattern;
////        pattern = Pattern.compile("[0-9]*");
////        txtField03Cont.setTextFormatter(new InputTextFormatter(pattern)); //Mobile No
////        txtField03Addr.setTextFormatter(new InputTextFormatter(pattern)); //House No
////        txtField07Addr.setTextFormatter(new InputTextFormatter(pattern)); //Zip code
//        //TIN Number pattern
//        //pattern = Pattern.compile("\\d{0,3}(-\\d{0,3}){0,3}");
//        pattern = Pattern.compile("[[0-9][-]]*");
//        txtField13.setTextFormatter(new InputTextFormatter(pattern));
//
//        /*populate combobox*/
//        comboBox09.setItems(cCvlStat);
//        comboBox08.setItems(cGender);
//        comboBox18.setItems(cCusttype);
//        comboBox07.setItems(cTitle);
////        comboBox04EmAd.setItems(cOwnEmAd); // Email Ownership
////        comboBox04Socm.setItems(cSocType); // SocMed Type
////        comboBox05Cont.setItems(cOwnCont); // Contact Ownership
////        comboBox04Cont.setItems(cTypCont); // Mobile Type
//
//        txtField11.setOnAction(this::getDate);
////        txtField05Addr.setOnKeyPressed(this::txtField_KeyPressed); //Town
////        txtField06Addr.setOnKeyPressed(this::txtField_KeyPressed); //Brgy
//        txtField12.setOnKeyPressed(this::txtField_KeyPressed); //Birth Place
//        txtField10.setOnKeyPressed(this::txtField_KeyPressed); //Citizenship
//        txtField26.setOnKeyPressed(this::txtField_KeyPressed); //Customer Name Search
//        txtField01.setOnKeyPressed(this::txtField_KeyPressed); //Customer ID Search
//        txtField25.setOnKeyPressed(this::txtField_KeyPressed); //Spouse
//
//        //CLIENT Master
//        txtField02.setOnKeyPressed(this::txtField_KeyPressed);
//        txtField03.setOnKeyPressed(this::txtField_KeyPressed);
//        txtField04.setOnKeyPressed(this::txtField_KeyPressed);
//        txtField05.setOnKeyPressed(this::txtField_KeyPressed);
//        txtField06.setOnKeyPressed(this::txtField_KeyPressed);
//        txtField13.setOnKeyPressed(this::txtField_KeyPressed);
//        txtField14.setOnKeyPressed(this::txtField_KeyPressed);
//        txtField16.setOnKeyPressed(this::txtField_KeyPressed);
//        textArea15.setOnKeyPressed(this::txtField_KeyPressed);
//        textArea15.setOnKeyPressed(this::txtArea_KeyPressed);
////        //CLIENT Address
////        setCapsLockBehavior(txtField03Addr);
////        setCapsLockBehavior(txtField04Addr);
////        setCapsLockBehavior(txtField05Addr);
////        setCapsLockBehavior(txtField06Addr);
////        setCapsLockBehavior(txtField07Addr);
////        setCapsLockBehavior(textArea11Addr);
////        txtField03Addr.setOnKeyPressed(this::txtField_KeyPressed); //House No
////        txtField04Addr.setOnKeyPressed(this::txtField_KeyPressed); //Street / Address
////        txtField05Addr.setOnKeyPressed(this::txtField_KeyPressed); // Town
////        txtField06Addr.setOnKeyPressed(this::txtField_KeyPressed); // Brgy
////        txtField07Addr.setOnKeyPressed(this::txtField_KeyPressed); //Zip code
////        textArea11Addr.setOnKeyPressed(this::txtArea_KeyPressed); // Address Remarks
//        //CLIENT Mobile
////        setCapsLockBehavior(txtField03Cont);
////        setCapsLockBehavior(textArea13Cont);
////        txtField03Cont.setOnKeyPressed(this::txtField_KeyPressed);  //Mobile Number
////        textArea13Cont.setOnKeyPressed(this::txtArea_KeyPressed); // Contact Remarks
////        //CLIENT Email
////        txtField03EmAd.setOnKeyPressed(this::txtField_KeyPressed); // Email Address
////        //CLIENT Social Media
////        txtField03Socm.setOnKeyPressed(this::txtField_KeyPressed); // SocMed Account
//
////        /*Check box Clicked Event*/
////        /*client_address*/
////        checkBox14Addr.setOnAction(this::cmdCheckBox_Click);
////        checkBox17Addr.setOnAction(this::cmdCheckBox_Click);
////        checkBox12Addr.setOnAction(this::cmdCheckBox_Click);
////        checkBox13Addr.setOnAction(this::cmdCheckBox_Click);
//        //Button Click Event
//        btnTabAdd.setOnAction(this::cmdButton_Click);
//        btnTabRem.setOnAction(this::cmdButton_Click);
//        //btnTabUpd.setOnAction(this::cmdButton_Click);
//        btnAdd.setOnAction(this::cmdButton_Click);
//        btnEdit.setOnAction(this::cmdButton_Click);
//        btnSave.setOnAction(this::cmdButton_Click);
//        btnClose.setOnAction(this::cmdButton_Click);
//        btnBrowse.setOnAction(this::cmdButton_Click);
//        btnCancel.setOnAction(this::cmdButton_Click);
//        //Update Class master
//        comboBox18.setOnAction(e -> {
//            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
//                comboChange();
//                try {
//                    oTrans.setMaster(18, String.valueOf(comboBox18.getSelectionModel().getSelectedIndex()));
//
//                    if (oTrans.getMaster(18).toString().equals("0")) {
//                        oTrans.setMaster(16, ""); //sCompnyNm
//                        oTrans.setMaster(7, "0"); //sTitlexxx
//                        oTrans.setMaster(8, "0"); //cGenderCd
//                        oTrans.setMaster(9, "0"); //cCvilStat
//                        oTrans.setMaster(16, ""); //sCompnyNm
//                        oTrans.setMaster(11, oApp.getServerDate());
//                    } else {
//                        oTrans.setMaster(2, ""); //sLastName
//                        oTrans.setMaster(3, ""); //sFrstName
//                        oTrans.setMaster(4, ""); //sMiddName
//                        oTrans.setMaster(5, ""); //sMaidenNm
//                        oTrans.setMaster(6, ""); //sSuffixNm
//                        oTrans.setMaster(10, ""); //sCitizenx
//                        oTrans.setMaster(12, ""); //sBirthPlc
//                        oTrans.setMaster(24, ""); //sCntryNme
//                        oTrans.setMaster(25, ""); //sTownName
//                        oTrans.setMaster(27, ""); //sSpouseID
//                        oTrans.setMaster(28, ""); //sSpouseNm
//                        oTrans.setMaster(16, ""); //sCompnyNm
//
//                        oTrans.setMaster(7, ""); //sTitlexxx
//                        oTrans.setMaster(8, ""); //cGenderCd
//                        oTrans.setMaster(9, ""); //cCvilStat
//                        oTrans.setMaster(11, LocalDate.of(1900, Month.JANUARY, 1));
//                    }
//                    loadClientMaster();
//
//                } catch (SQLException ex) {
//                    Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//
//        });
//
//        comboBox07.setOnAction(e -> {
//            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
//                try {
//                    if(comboBox07.getSelectionModel().getSelectedIndex() >= 0){
//                        oTrans.setMaster(7, String.valueOf(comboBox07.getSelectionModel().getSelectedIndex()));
//                    }
//                } catch (SQLException ex) {
//                    Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        });
//        comboBox08.setOnAction(e -> {
//            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
//                try {
//                    if(comboBox08.getSelectionModel().getSelectedIndex() >= 0){
//                        oTrans.setMaster(8, String.valueOf(comboBox08.getSelectionModel().getSelectedIndex()));
//                    }
//                } catch (SQLException ex) {
//                    Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        });
//        comboBox09.setOnAction(e -> {
//            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
//                try {
//                    if (comboBox09.getSelectionModel().getSelectedIndex() == 0) {
//                        txtField25.setText("");
//                        txtField25.setDisable(true);
//                        oTrans.setMaster(27, "");
//                        oTrans.setMaster(28, "");
//                    } else {
//                        txtField25.setDisable(false);
//                    }
//                    if(comboBox09.getSelectionModel().getSelectedIndex() >= 0){
//                        oTrans.setMaster(9, String.valueOf(comboBox09.getSelectionModel().getSelectedIndex()));
//                    } 
//                } catch (SQLException ex) {
//                    Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        });
//
//        txtField12.textProperty().addListener((observable, oldValue, newValue) -> {
//            if(newValue != null){
//                if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
//                    if (newValue.isEmpty()) {
//                        try {
//                            oTrans.setMaster(12, "");
//                            oTrans.setMaster(25, "");
//                        } catch (SQLException ex) {
//                            Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                    }
//                }
//            }
//        });
//
//        txtField25.textProperty().addListener((observable, oldValue, newValue) -> {
//            if(newValue != null){
//                if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
//                    if (newValue.isEmpty()) {
//                        try {
//                            oTrans.setMaster(27, "");
//                            oTrans.setMaster(28, "");
//                        } catch (SQLException ex) {
//                            Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                    }
//                }
//            }
//        });
//
//        txtField10.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
//                if(newValue != null){
//                    if (newValue.isEmpty()) {
//                        try {
//                            oTrans.setMaster(10, "");
//                            oTrans.setMaster(24, "");
//                        } catch (SQLException ex) {
//                            Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                    }
//                }
//            }
//        });
//
//        //Tab Process
//        tabPCustCont.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
//            @Override
//            public void changed(ObservableValue<? extends Tab> observable, Tab oldTab, Tab newTab) {
//                pnRow = 0;
//                btnTabRem.setVisible(false);
//                //clearContactInfo();
//            }
//        });
//       
//        /*Clear Fields*/
//        clearFields();
//        pnEditMode = EditMode.UNKNOWN;
//        initButton(pnEditMode);
//
//        /*CUSTOMER VEHICLE INFORMATION*/
//        custVehicleInfo();
//        
//        Platform.runLater(() -> {
//            if(oTrans.loadState()){
//                pnEditMode = oTrans.getEditMode();
//                loadClientMaster();
//                loadAddress();
//                loadContact();
//                loadEmail();
//                loadSocialMedia();
//                loadVehicleInfoTable();
//                initButton(pnEditMode);
//            } else {
//                if(oTrans.getMessage().isEmpty()){
//                }else{
//                    ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
//                }
//            }
//        });
//    }
//
//    private void custVehicleInfo() {
//        oTransVehicle = new ClientVehicleInfo(oApp, oApp.getBranchCode(), false);
//        oTransVehicle.setCallback(oListener);
//        oTransVehicle.setWithUI(true);
//        oTrans.setVhclInfoObject(oTransVehicle);
//        initVehicleInfo();
//        initCoVehicleInfo();
////        initVehicleHtry();
//
////        //Vehicle Info
////        addRequiredFieldListener(txtField24V);
////        addRequiredFieldListener(txtField26V);
////        addRequiredFieldListener(txtField28V);
////        addRequiredFieldListener(txtField31V);
////        addRequiredFieldListener(txtField30V);
////        addRequiredFieldListener(txtField32V);
////        addRequiredFieldListener(txtField20V);//Plate No
////        addRequiredFieldListener(txtField08V);
////        addRequiredFieldListener(txtField03V);
////        addRequiredFieldListener(txtField04V);
////
////        setCapsLockBehavior(txtField03V);
////        setCapsLockBehavior(txtField04V);
////        setCapsLockBehavior(txtField08V);
////        setCapsLockBehavior(txtField09V);
////        setCapsLockBehavior(txtField11V);
////        setCapsLockBehavior(txtField20V);
////        setCapsLockBehavior(txtField22V);
////        setCapsLockBehavior(txtField24V);
////        setCapsLockBehavior(txtField26V);
////        setCapsLockBehavior(txtField28V);
////        setCapsLockBehavior(textArea34V);
////        txtField03V.focusedProperty().addListener(txtField_Focus);
////        txtField04V.focusedProperty().addListener(txtField_Focus);
////        txtField08V.focusedProperty().addListener(txtField_Focus);
////        txtField09V.focusedProperty().addListener(txtField_Focus);
////        txtField11V.focusedProperty().addListener(txtField_Focus);
////        txtField20V.focusedProperty().addListener(txtField_Focus);
////        txtField22V.focusedProperty().addListener(txtField_Focus);
////        txtField24V.focusedProperty().addListener(txtField_Focus);
////        txtField26V.focusedProperty().addListener(txtField_Focus);
////        txtField28V.focusedProperty().addListener(txtField_Focus);
////        txtField31V.focusedProperty().addListener(txtField_Focus);
////        txtField30V.focusedProperty().addListener(txtField_Focus);
////        txtField32V.focusedProperty().addListener(txtField_Focus);
////        textArea34V.focusedProperty().addListener(txtArea_Focus);
////        txtField21V.setOnAction(this::getDate);
////        txtField03V.setOnKeyPressed(this::txtField_KeyPressed_Vhcl);
////        txtField04V.setOnKeyPressed(this::txtField_KeyPressed_Vhcl);
////        txtField08V.setOnKeyPressed(this::txtField_KeyPressed_Vhcl);
////        txtField09V.setOnKeyPressed(this::txtField_KeyPressed_Vhcl);
////        txtField11V.setOnKeyPressed(this::txtField_KeyPressed_Vhcl);
////        txtField20V.setOnKeyPressed(this::txtField_KeyPressed_Vhcl);
////        txtField22V.setOnKeyPressed(this::txtField_KeyPressed_Vhcl);
////        txtField24V.setOnKeyPressed(this::txtField_KeyPressed_Vhcl);
////        txtField26V.setOnKeyPressed(this::txtField_KeyPressed_Vhcl);
////        txtField28V.setOnKeyPressed(this::txtField_KeyPressed_Vhcl);
////        txtField31V.setOnKeyPressed(this::txtField_KeyPressed_Vhcl);
////        txtField30V.setOnKeyPressed(this::txtField_KeyPressed_Vhcl);
////        txtField32V.setOnKeyPressed(this::txtField_KeyPressed_Vhcl);
////        textArea34V.setOnKeyPressed(this::txtArea_KeyPressed);
////        btnVhclDesc.setOnAction(this::cmdButton_Click_vhcl);
////        btnVhclAvl.setOnAction(this::cmdButton_Click_vhcl);
////        btnVhclMnl.setOnAction(this::cmdButton_Click_vhcl);
////        btnEngFrm.setOnAction(this::cmdButton_Click_vhcl);
////        btnVSave.setOnAction(this::cmdButton_Click_vhcl);
////        btnTransfer.setOnAction(this::cmdButton_Click_vhcl);
////        tabPaneMain.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
////            @Override
////            public void changed(ObservableValue<? extends Tab> observable, Tab oldTab, Tab newTab) {
////                selectedIndex = tabPaneMain.getSelectionModel().getSelectedIndex();
////                pnRow = 0;
////                /*CLIENT INFORMATION*/
////                if (selectedIndex == 0) {
////                    initButton(pnEditMode);
////                    btnTransfer.setVisible(false);
////                    btnTransfer.setManaged(false);
////                    btnVSave.setVisible(false);
////                    btnVSave.setManaged(false);
////                    /*CLIENT VEHICLE INFORMATION*/
////                } else if (selectedIndex == 1) {
////                    initVhclInfoButton(pnVEditMode);
////                    if (bBtnVhclAvl){
////                        disableFields();
////                    }
////                    btnAdd.setVisible(false);
////                    btnAdd.setManaged(false);
////                    btnSave.setVisible(false);
////                    btnSave.setManaged(false);
////                }
////
////            }
////        });
////        clearVehicleInfoFields();
////        pnVEditMode = EditMode.UNKNOWN;
////        initVhclInfoButton(pnVEditMode);
//    }
//
//    private static void setCapsLockBehavior(TextField textField) {
//        textField.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (textField.getText() != null) {
//                textField.setText(newValue.toUpperCase());
//            }
//        });
//    }
//
//    private static void setCapsLockBehavior(TextArea textArea) {
//        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (textArea.getText() != null) {
//                textArea.setText(newValue.toUpperCase());
//            }
//        });
//    }
//
//    @Override
//    public void setGRider(GRider foValue) {
//        oApp = foValue;
//    }
//
//    /*BUTTON CLICKED*/
//    private void cmdButton_Click(ActionEvent event) {
//        int iCntp = 0;
//        String lsButton = ((Button) event.getSource()).getId();
//        iTabIndex = tabPCustCont.getSelectionModel().getSelectedIndex();
//        try {
//            switch (lsButton) {
//                case "btnAdd": //create new client
//                    clearFields();
////                    clearVehicleInfoFields();
//                    if (oTrans.NewRecord() && oTransAddress.NewRecord() && oTransMobile.NewRecord()
//                            && oTransEmail.NewRecord() && oTransSocMed.NewRecord()) {
//                        loadClientMaster();
//                        /*Clear tables*/
//                        addressdata.clear();
//                        mobiledata.clear();
//                        emaildata.clear();
//                        socialmediadata.clear();
//                        vhclinfodata.clear();
////                        vhclhtrydata.clear();
//                        txtField26.clear(); // CLIENT Search
//                        txtField01.clear(); // CLIENT ID
//                        pnEditMode = oTrans.getEditMode();
//                    } else {
//                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
//                    }
//                    break;
//                case "btnEdit":
//                    /*CLIENT INFORMATION*/
////                    if (selectedIndex == 0) {
//                    //modify client info
//                    if (oTrans.UpdateRecord() && oTransAddress.UpdateRecord() && oTransMobile.UpdateRecord()
//                            && oTransEmail.UpdateRecord() && oTransSocMed.UpdateRecord()) {
//                        oTransAddress.addAddress();
//                        oTransMobile.addMobile();
//                        oTransEmail.addEmail();
//                        oTransSocMed.addSocMed();
//                        pnEditMode = oTrans.getEditMode();
//                    } else {
//                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
//                    }
////                        clearContactInfo();
//                    /*CLIENT VEHICLE INFORMATION*/
////                    } else if (selectedIndex == 1) {
////                        if (oTransVehicle.UpdateRecord()) {
////                            pnVEditMode = oTransVehicle.getEditMode();
////                        }
////                    }
//                    break;
//                case "btnSave": //save client info
//                    //Do not Allow to save CLIENT Info if there's no Address / Mobile
//                    if (pnEditMode == EditMode.ADDNEW) {
//                        if (oTransAddress.getItemCount() <= 1) {
//                            ShowMessageFX.Warning(getStage(), "Please Add atleast 1 Address.", "Warning", pxeModuleName);
//                            return;
//                        } else if (oTransMobile.getItemCount() <= 1) {
//                            ShowMessageFX.Warning(getStage(), "Please Add atleast 1 Contact Number.", "Warning", pxeModuleName);
//                            return;
//                        }
//                    }
//                    //Do not Allow to save CLIENT Info if there's no Primary Address / Mobile
//                    for (lnCtr = 1; lnCtr <= oTransAddress.getItemCount(); lnCtr++) {
//                        if (oTransAddress.getAddress(lnCtr, "cPrimaryx").toString().equals("1")) {
//                            iCntp = iCntp + 1;
//                        }
//                    }
//                    if (iCntp <= 0) {
//                        ShowMessageFX.Warning(getStage(), "Please Add Primary Address.", "Warning", pxeModuleName);
//                        return;
//                    }
//
//                    iCntp = 0;
//                    for (lnCtr = 1; lnCtr <= oTransMobile.getItemCount(); lnCtr++) {
//                        if (oTransMobile.getMobile(lnCtr, "cPrimaryx").toString().equals("1")) {
//                            iCntp = iCntp + 1;
//                        }
//                    }
//                    if (iCntp <= 0) {
//                        ShowMessageFX.Warning(getStage(), "Please Add Primary Contact Number.", "Warning", pxeModuleName);
//                        return;
//                    }
//
//                    //Proceed Saving
//                    if (setSelection()) {
//                        if (oTrans.SaveRecord()) {
//                            oTransAddress.setClientID(oTrans.getMaster("sClientID").toString());
//                            oTransMobile.setClientID(oTrans.getMaster("sClientID").toString());
//                            oTransEmail.setClientID(oTrans.getMaster("sClientID").toString());
//                            oTransSocMed.setClientID(oTrans.getMaster("sClientID").toString());
//
//                            oTransAddress.removeAddress(oTransAddress.getItemCount());
//                            oTransMobile.removeMobile(oTransMobile.getItemCount());
//                            oTransEmail.removeEmail(oTransEmail.getItemCount());
//                            oTransSocMed.removeSocMed(oTransSocMed.getItemCount());
//
//                            if (oTransAddress.SaveRecord()) {
//                            } else {
//                                ShowMessageFX.Warning(getStage(), oTransAddress.getMessage(), "Warning", "Client Address");
//                                return;
//                            }
//                            if (oTransMobile.SaveRecord()) {
//                            } else {
//                                ShowMessageFX.Warning(getStage(), oTransMobile.getMessage(), "Warning", "Client Mobile");
//                                return;
//                            }
//                            if (oTransEmail.SaveRecord()) {
//                            } else {
//                                ShowMessageFX.Warning(getStage(), oTransEmail.getMessage(), "Warning", "Client Email");
//                                return;
//                            }
//                            if (oTransSocMed.SaveRecord()) {
//                            } else {
//                                ShowMessageFX.Warning(getStage(), oTransSocMed.getMessage(), "Warning", "Client Social Media");
//                                return;
//                            }
//
//                            ShowMessageFX.Information(getStage(), "Client saved successfully.", "Client Information", null);
//                            pnEditMode = oTrans.getEditMode();
//                        } else {
//                            ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", "Error while saving Client Information");
//                            return;
//                        }
//                    }
////                    clearContactInfo();
////                    clearVehicleInfoFields();
//                    //Retrieve saved data
//                    if (oTrans.SearchRecord(oTrans.getMaster("sClientID").toString(), true)) {
//                        if (oTransAddress.OpenRecord(oTrans.getMaster("sClientID").toString(), false)
//                                && oTransMobile.OpenRecord(oTrans.getMaster("sClientID").toString(), false)
//                                && oTransEmail.OpenRecord(oTrans.getMaster("sClientID").toString(), false)
//                                && oTransSocMed.OpenRecord(oTrans.getMaster("sClientID").toString(), false)) {
//                            loadClientMaster();
//                            loadAddress();
//                            loadContact();
//                            loadEmail();
//                            loadSocialMedia();
//                            loadVehicleInfoTable();
//                            loadCoOwnVehicleInfoTable();
////                            loadVehicleHtryTable();
//                            pnEditMode = oTrans.getEditMode();
//                        } else {
//                            ShowMessageFX.Warning(getStage(), "There was an error while loading Contact Information Details.", "Warning", null);
//                            txtField26.clear(); // CLIENT Search
//                            txtField01.clear(); // CLIENT ID
//                            clearFields();
//                            pnEditMode = EditMode.UNKNOWN;
//
//                        }
//                    } else {
//                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
//                        txtField26.clear(); // CLIENT Search
//                        txtField01.clear(); // CLIENT ID
//                        clearFields();
//                        pnEditMode = EditMode.UNKNOWN;
//                    }
//                    break;
//                case "btnClose": //close tab
//                    if (ShowMessageFX.OkayCancel(null, "Close Tab", "Are you sure you want to close this Tab?") == true) {
//                        if (unload != null) {
//                            unload.unloadForm(AnchorMain, oApp, "Customer");
//                        } else {
//                            ShowMessageFX.Warning(getStage(), "Please notify the system administrator to configure the null value at the close button.", "Warning", pxeModuleName);
//                        }
//                        break;
//                    } else {
//                        return;
//                    }
//                case "btnCancel":
//                    if (ShowMessageFX.OkayCancel(getStage(), "Are you sure you want to cancel?", pxeModuleName, null) == true) {
//                        removeRequired();
//                        clearFields();
//                        addressdata.clear();
//                        mobiledata.clear();
//                        emaildata.clear();
//                        socialmediadata.clear();
//                        vhclinfodata.clear();
//                        coownvhclinfodata.clear();
//                        pnEditMode = EditMode.UNKNOWN;
//                    }
//                    break;
//                case "btnTabAdd":
//                    switch (iTabIndex) {
//                        case 0:
////                            if (pnRow >= 1) {
////                                pnRow = 0;
////                            } else {
//                            loadAddressForm(oTransAddress.getItemCount(), true);
//                            loadAddress();
//                            oTrans.saveState(oTrans.toJSONString());
//
////                                //Validate Primary Before Inserting
////                                if (!validateContactInfo()) {
////                                    if (checkBox14Addr.isSelected()) {
////                                        ShowMessageFX.Warning(getStage(), "Please note that you cannot add more than 1 primary address.", "Warning", pxeModuleName);
////                                        return;
////                                    }
////                                }
////
////                                if (setItemtoTable("btnTabAdd")) {
////                                    loadAddress();
////                                    oTransAddress.addAddress();
////                                } else {
////                                    return;
////                                }
////                            }
//                            /*Address*/
//                            //clearAddress();
//                            break;
//                        case 1: //Mobile
//
//                            loadMobileForm(oTransMobile.getItemCount(), true);
//                            loadContact();
//                            oTrans.saveState(oTrans.toJSONString());
//                            //oTransMobile.addMobile();
//
////                            if (pnRow >= 1) {
////                                pnRow = 0;
////                            } else {
////                                if (!validateContactInfo()) {
////                                    if (radiobtn11CntY.isSelected()) {
////                                        ShowMessageFX.Warning(getStage(), "Please note that you cannot add more than 1 primary contact number.", "Warning", pxeModuleName);
////                                        return;
////                                    }
////                                }
////                                if (setItemtoTable("btnTabAdd")) {
////                                    loadContact();
////                                    oTransMobile.addMobile();
////                                } else {
////                                    return;
////                                }
////                            }
////
////                            /*Clear Fields*/
////                            clearContact();
//                            break;
//                        case 2: //Email
//
//                            loadEmailForm(oTransEmail.getItemCount(), true);
//                            loadEmail();
//                            oTrans.saveState(oTrans.toJSONString());
//                            //oTransEmail.addEmail();
//
////                            if (pnRow >= 1) {
////                                pnRow = 0;
////                            } else {
////                                if (!validateContactInfo()) {
////                                    if (radiobtn05EmaY.isSelected()) {
////                                        ShowMessageFX.Warning(getStage(), "Please note that you cannot add more than 1 primary email.", "Warning", pxeModuleName);
////                                        return;
////                                    }
////                                }
////                                if (setItemtoTable("btnTabAdd")) {
////                                    loadEmail();
////                                    oTransEmail.addEmail();
////                                } else {
////                                    return;
////                                }
////                            }
////                            /*Clear Fields*/
////                            clearEmail();
//                            break;
//                        case 3:
//                            loadSocialMediaForm(oTransSocMed.getItemCount(), true);
//                            loadSocialMedia();
//                            oTrans.saveState(oTrans.toJSONString());
//                            //oTransSocMed.addSocMed();
////                            if (pnRow >= 1) {
////                                pnRow = 0;
////                            } else {
////                                if (setItemtoTable("btnTabAdd")) {
////                                    loadSocialMedia();
////                                    oTransSocMed.addSocMed();
////                                } else {
////                                    return;
////                                }
////                            }
////                            /*Clear Fields*/
////                            clearSocMed();
//                            break;
//                    }
//                    break;
////                case "btnTabUpd":
////                    switch (iTabIndex) {
////                        case 0:
////                            if (!validateContactInfo()) {
////                                if (checkBox14Addr.isSelected()) {
////                                    ShowMessageFX.Warning(getStage(), "Please note that you cannot add more than 1 primary address.", "Warning", pxeModuleName);
////                                    return;
////                                }
////                            }
////                            if (setItemtoTable("btnTabUpd")) {
////                                loadAddress();
////                                pnRow = 0;
////                            } else {
////                                return;
////                            }
////                            /*Address*/
////                            clearAddress();
////                            break;
////                        case 1: //Mobile
////                            if (!validateContactInfo()) {
////                                if (radiobtn11CntY.isSelected()) {
////                                    ShowMessageFX.Warning(getStage(), "Please note that you cannot add more than 1 primary contact number.", "Warning", pxeModuleName);
////                                    return;
////                                }
////                            }
////
////                            if (setItemtoTable("btnTabUpd")) {
////                                loadContact();
////                                pnRow = 0;
////                            } else {
////                                return;
////                            }
////                            /*Clear Fields*/
////                            clearContact();
////                            break;
////                        case 2: //Email
////                            if (!validateContactInfo()) {
////                                if (radiobtn05EmaY.isSelected()) {
////                                    ShowMessageFX.Warning(getStage(), "Please note that you cannot add more than 1 primary email.", "Warning", pxeModuleName);
////                                    return;
////                                }
////                            }
////                            if (setItemtoTable("btnTabUpd")) {
////                                loadEmail();
////                                pnRow = 0;
////                            } else {
////                                return;
////                            }
////                            /*Clear Fields*/
////                            clearEmail();
////                            break;
////                        case 3:
////                            if (setItemtoTable("btnTabUpd")) {
////                                loadSocialMedia();
////                            } else {
////                                return;
////                            }
////                            /*Clear Fields*/
////                            clearSocMed();
////                            break;
////                    }
////                    break;
//                case "btnTabRem":
//                    if (pnRow == 0) {
//                        ShowMessageFX.Warning(getStage(), "No selected item!", "Warning", pxeModuleName);
//                        return;
//                    }
//                    switch (iTabIndex) {
//                        case 0:
//                            if (ShowMessageFX.OkayCancel(null, "Confirmation", "Are you sure you want to remove this Client Address?") == true) {
//                            } else {
//                                return;
//                            }
//                            oTransAddress.removeAddress(pnRow);
//                            pnRow = 0;
//                            loadAddress();
//                            oTrans.saveState(oTrans.toJSONString());
//                            /*Clear Fields*/
////                            clearAddress();
//                            break;
//                        case 1://Mobile
//                            if (ShowMessageFX.OkayCancel(null, "Confirmation", "Are you sure you want to remove this Client Mobile?") == true) {
//                            } else {
//                                return;
//                            }
//                            oTransMobile.removeMobile(pnRow);
//                            pnRow = 0;
//                            loadContact();
//                            oTrans.saveState(oTrans.toJSONString());
//                            /*Clear Fields*/
////                            clearContact();
//
//                            break;
//                        case 2://Email
//                            if (ShowMessageFX.OkayCancel(null, "Confirmation", "Are you sure you want to remove this  Client Email?") == true) {
//                            } else {
//                                return;
//                            }
//                            oTransEmail.removeEmail(pnRow);
//                            pnRow = 0;
//                            loadEmail();
//                            oTrans.saveState(oTrans.toJSONString());
//                            /*Clear Fields*/
////                            clearEmail();
//                            break;
//                        case 3://Social Media
//                            if (ShowMessageFX.OkayCancel(null, "Confirmation", "Are you sure you want to remove this  Client Social Media?") == true) {
//                            } else {
//                                return;
//                            }
//                            oTransSocMed.removeSocMed(pnRow);
//                            pnRow = 0;
//                            loadSocialMedia();
//                            oTrans.saveState(oTrans.toJSONString());
//                            /*Clear Fields*/
////                            clearSocMed();
//                            break;
//                    }
//
//                    btnTabRem.setVisible(false);
//                    break;
//
//                case "btnBrowse":
//                    if ((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) //||(pnVEditMode == EditMode.ADDNEW || pnVEditMode == EditMode.UPDATE)
//                            ) {
//                        if (ShowMessageFX.OkayCancel(null, "Confirmation", "You have unsaved data. Are you sure you want to browse a new record?") == true) {
//                            oTransAddress.removeAddress(oTransAddress.getItemCount());
//                            oTransMobile.removeMobile(oTransMobile.getItemCount());
//                            oTransEmail.removeEmail(oTransEmail.getItemCount());
//                            oTransSocMed.removeSocMed(oTransSocMed.getItemCount());
//                            clearFields();
////                            clearVehicleInfoFields();
//                        } else {
//                            return;
//                        }
//                    }
//
//                    boolean byClientID = true;
//                    String txtValue = "";
//                    if (!txtField26.getText().isEmpty() && !txtField26.getText().trim().equals("")) {
//                        byClientID = false;
//                        txtValue = txtField26.getText();
//                    } else {
//                        byClientID = true;
//                        txtValue = txtField01.getText();
//                    }
//
//                    clearFields();
//                    if (oTrans.SearchRecord(txtValue, byClientID)) {
//                        if (oTransAddress.OpenRecord(oTrans.getMaster("sClientID").toString(), false)
//                                && oTransMobile.OpenRecord(oTrans.getMaster("sClientID").toString(), false)
//                                && oTransEmail.OpenRecord(oTrans.getMaster("sClientID").toString(), false)
//                                && oTransSocMed.OpenRecord(oTrans.getMaster("sClientID").toString(), false)) {
//                            loadClientMaster();
//                            loadAddress();
//                            loadContact();
//                            loadEmail();
//                            loadSocialMedia();
//                            loadVehicleInfoTable();
//                            loadCoOwnVehicleInfoTable();
////                            loadVehicleHtryTable();
//                            pnEditMode = EditMode.READY;
////                            pnVEditMode = EditMode.UNKNOWN;
//                        } else {
//                            ShowMessageFX.Warning(getStage(), "There was an error while loading Contact Information Details.", "Warning", null);
//                            txtField26.clear(); // CLIENT Search
//                            txtField01.clear(); // CLIENT ID
//                            pnEditMode = EditMode.UNKNOWN;
////                            pnVEditMode = EditMode.UNKNOWN;
//                            clearFields();
////                            clearVehicleInfoFields();
//                        }
//                    } else {
//                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
//                        txtField26.clear(); // CLIENT Search
//                        txtField01.clear(); // CLIENT ID
//                        pnEditMode = EditMode.UNKNOWN;
////                        pnVEditMode = EditMode.UNKNOWN;
//                        clearFields();
////                        clearVehicleInfoFields();
//                    }
//
//                    break;
//            }
//
//            initButton(pnEditMode);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
//        }
//    }
//
//    private void loadAddressForm(Integer fnRow, boolean isAdd) {
//        try {
//            if (fnRow <= 0) {
//                ShowMessageFX.Warning(getStage(), "Invalid Table Row to Set. ", "Warning", null);
//                return;
//            }
//
//            Stage stage = new Stage();
//
//            FXMLLoader fxmlLoader = new FXMLLoader();
//            fxmlLoader.setLocation(getClass().getResource("CustomerAddressForm.fxml"));
//
//            CustomerAddressFormController loControl = new CustomerAddressFormController();
//            loControl.setGRider(oApp);
//            loControl.setObject(oTransAddress);
//            loControl.setRow(fnRow);
//            loControl.setState(isAdd);
//            loControl.setOrigTown((String) oTransAddress.getAddress(fnRow, 5));
//            loControl.setOrigBrgy((String) oTransAddress.getAddress(fnRow, 6));
//
//            fxmlLoader.setController(loControl);
//
//            //load the main interface
//            Parent parent = fxmlLoader.load();
//
//            parent.setOnMousePressed(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    xOffset = event.getSceneX();
//                    yOffset = event.getSceneY();
//                }
//            });
//
//            parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    stage.setX(event.getScreenX() - xOffset);
//                    stage.setY(event.getScreenY() - yOffset);
//                }
//            });
//
//            //set the main interface as the scene
//            Scene scene = new Scene(parent);
//            stage.setScene(scene);
//            stage.initStyle(StageStyle.TRANSPARENT);
//            stage.initModality(Modality.APPLICATION_MODAL);
//            stage.setTitle("");
//            stage.showAndWait();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
//            System.exit(1);
//        } catch (SQLException ex) {
//            Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    private void loadMobileForm(Integer fnRow, boolean isAdd) {
//        try {
//            if (fnRow <= 0) {
//                ShowMessageFX.Warning(getStage(), "Invalid Table Row to Set. ", "Warning", null);
//                return;
//            }
//
//            Stage stage = new Stage();
//
//            FXMLLoader fxmlLoader = new FXMLLoader();
//            fxmlLoader.setLocation(getClass().getResource("CustomerContactForm.fxml"));
//
//            CustomerContactFormController loControl = new CustomerContactFormController();
//            loControl.setGRider(oApp);
//            loControl.setObject(oTransMobile);
//            loControl.setRow(fnRow);
//            loControl.setState(isAdd);
//            fxmlLoader.setController(loControl);
//
//            //load the main interface
//            Parent parent = fxmlLoader.load();
//
//            parent.setOnMousePressed(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    xOffset = event.getSceneX();
//                    yOffset = event.getSceneY();
//                }
//            });
//
//            parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    stage.setX(event.getScreenX() - xOffset);
//                    stage.setY(event.getScreenY() - yOffset);
//                }
//            });
//
//            //set the main interface as the scene
//            Scene scene = new Scene(parent);
//            stage.setScene(scene);
//            stage.initStyle(StageStyle.TRANSPARENT);
//            stage.initModality(Modality.APPLICATION_MODAL);
//            stage.setTitle("");
//            stage.showAndWait();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
//            System.exit(1);
//        }
//    }
//
//    private void loadEmailForm(Integer fnRow, boolean isAdd) {
//        try {
//            if (fnRow <= 0) {
//                ShowMessageFX.Warning(getStage(), "Invalid Table Row to Set. ", "Warning", null);
//                return;
//            }
//
//            Stage stage = new Stage();
//
//            FXMLLoader fxmlLoader = new FXMLLoader();
//            fxmlLoader.setLocation(getClass().getResource("CustomerEmailForm.fxml"));
//
//            CustomerEmailFormController loControl = new CustomerEmailFormController();
//            loControl.setGRider(oApp);
//            loControl.setObject(oTransEmail);
//            loControl.setRow(fnRow);
//            loControl.setState(isAdd);
//            fxmlLoader.setController(loControl);
//
//            //load the main interface
//            Parent parent = fxmlLoader.load();
//
//            parent.setOnMousePressed(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    xOffset = event.getSceneX();
//                    yOffset = event.getSceneY();
//                }
//            });
//
//            parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    stage.setX(event.getScreenX() - xOffset);
//                    stage.setY(event.getScreenY() - yOffset);
//                }
//            });
//
//            //set the main interface as the scene
//            Scene scene = new Scene(parent);
//            stage.setScene(scene);
//            stage.initStyle(StageStyle.TRANSPARENT);
//            stage.initModality(Modality.APPLICATION_MODAL);
//            stage.setTitle("");
//            stage.showAndWait();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
//            System.exit(1);
//        }
//    }
//
//    private void loadSocialMediaForm(Integer fnRow, boolean isAdd) {
//        try {
//            if (fnRow <= 0) {
//                ShowMessageFX.Warning(getStage(), "Invalid Table Row to Set. ", "Warning", null);
//                return;
//            }
//
//            Stage stage = new Stage();
//
//            FXMLLoader fxmlLoader = new FXMLLoader();
//            fxmlLoader.setLocation(getClass().getResource("CustomerSocialMediaForm.fxml"));
//
//            CustomerSocialMediaFormController loControl = new CustomerSocialMediaFormController();
//            loControl.setGRider(oApp);
//            loControl.setObject(oTransSocMed);
//            loControl.setRow(fnRow);
//            loControl.setState(isAdd);
//            fxmlLoader.setController(loControl);
//
//            //load the main interface
//            Parent parent = fxmlLoader.load();
//
//            parent.setOnMousePressed(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    xOffset = event.getSceneX();
//                    yOffset = event.getSceneY();
//                }
//            });
//
//            parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    stage.setX(event.getScreenX() - xOffset);
//                    stage.setY(event.getScreenY() - yOffset);
//                }
//            });
//
//            //set the main interface as the scene
//            Scene scene = new Scene(parent);
//            stage.setScene(scene);
//            stage.initStyle(StageStyle.TRANSPARENT);
//            stage.initModality(Modality.APPLICATION_MODAL);
//            stage.setTitle("");
//            stage.showAndWait();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
//            System.exit(1);
//        }
//    }
////    private ScreenInterface getController(String fsValue) {
////        switch (fsValue) {
////            case "CustomerAddressForm.fxml":
////                return new CustomerAddressFormController();
////
////            default:
////                ShowMessageFX.Warning(null, "Warning", "Notify System Admin to Configure Screen Interface for " + fsValue);
////                return null;
////        }
////    }
//
////    private void loadContactInfoForm(String fsFormName){
////        try {
////            Stage stage = new Stage();
////            ScreenInterface fxObj = getController(fsFormName);
////            fxObj.setGRider(oApp);
////
////            FXMLLoader fxmlLoader = new FXMLLoader();
////            fxmlLoader.setLocation(fxObj.getClass().getResource(fsFormName));
////            fxmlLoader.setController(fxObj);
////
////            //load the main interface
////            Parent parent = fxmlLoader.load();
////            parent.setOnMousePressed(new EventHandler<MouseEvent>() {
////                @Override
////                public void handle(MouseEvent event) {
////                    xOffset = event.getSceneX();
////                    yOffset = event.getSceneY();
////                }
////            });
////
////            parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
////                @Override
////                public void handle(MouseEvent event) {
////                    stage.setX(event.getScreenX() - xOffset);
////                    stage.setY(event.getScreenY() - yOffset);
////                }
////            });
////
////            //set the main interface as the scene
////            Scene scene = new Scene(parent);
////            stage.setScene(scene);
////            stage.initStyle(StageStyle.TRANSPARENT);
////            stage.initModality(Modality.APPLICATION_MODAL);
////            stage.setTitle("");
////            stage.showAndWait();
////        } catch (IOException ex) {
////            Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
////        }
////
////    }
////    private void cmdButton_Click_vhcl(ActionEvent event) {
////        String lsButton = ((Button) event.getSource()).getId();
////        try {
////            switch (lsButton) {
////                case "btnVhclDesc":
////                    loadVehicleDescriptionWindow();
////                    break;
////
////                case "btnEngFrm":
////                    if (((String) oTransVehicle.getMaster(23)).equals("")) {
////                        ShowMessageFX.Warning(getStage(), "Please select Make.", "Warning", null);
////                        txtField24V.requestFocus();
////                        return;
////                    }
////                    if (((String) oTransVehicle.getMaster(25)).equals("")) {
////                        ShowMessageFX.Warning(getStage(), "Please select Model.", "Warning", null);
////                        txtField26V.requestFocus();
////                        return;
////                    }
////
////                    loadEngineFrameWindow(0, false);
////                    break;
////                case "btnVhclAvl":
////                    if (pnVEditMode == EditMode.ADDNEW || pnVEditMode == EditMode.UPDATE) {
////                        if (ShowMessageFX.OkayCancel(null, "Confirmation", "You have unsaved data. Are you sure you want to create a new record?") == true) {
////                            clearVehicleInfoFields();
////                        } else {
////                            return;
////                        }
////                    }
////                    if (oTransVehicle.searchAvailableVhcl()) {
////                        clearVehicleInfoFields();
////                        loadClientVehicleInfo();
////                        oTransVehicle.setMaster("sClientID",oTrans.getMaster("sClientID").toString());
////                        bBtnVhclAvl = true;
////                        pnVEditMode = oTransVehicle.getEditMode();
////                    } else {
////                        ShowMessageFX.Warning(getStage(), oTransVehicle.getMessage(), "Warning", null);
////                    }
////
////                    break;
////                case "btnVhclMnl":
////                    if (pnVEditMode == EditMode.ADDNEW || pnVEditMode == EditMode.UPDATE) {
////                        if (ShowMessageFX.OkayCancel(null, "Confirmation", "You have unsaved data. Are you sure you want to create a new record?") == true) {
////                            clearVehicleInfoFields();
////                        } else {
////                            return;
////                        }
////                    }
////                    if (oTransVehicle.NewRecord()) {
////                        clearVehicleInfoFields();
////                        bBtnVhclAvl = false;
////                        txtField24V.requestFocus();
////                        pnVEditMode = oTransVehicle.getEditMode();
////                    } else {
////                        ShowMessageFX.Warning(getStage(), oTransVehicle.getMessage(), "Warning", null);
////                    }
////                    break;
////                case "btnVSave":
////                    if (ShowMessageFX.OkayCancel(null, "Confirmation", "Are you sure you want to save this entry?") == true) {
////                    } else {
////                        return;
////                    }
////                    oTransVehicle.setClientID(oTrans.getMaster("sClientID").toString());
////                    if (oTransVehicle.SaveRecord()) {
////                        ShowMessageFX.Information(getStage(), oTransVehicle.getMessage(), "Client Vehicle Information", null);
////                        pnVEditMode = oTransVehicle.getEditMode();
////                        loadVehicleInfoTable();
////                        loadVehicleHtryTable();
////                    } else {
////                        ShowMessageFX.Warning(getStage(), oTransVehicle.getMessage(), "Warning", null);
////                        return;
////                    }
////                    clearVehicleInfoFields();
////                    break;
////                case "btnTransfer":
////                    loadTransferOwnershipWindow();
////                    break;
////            }
////
////            initVhclInfoButton(pnVEditMode);
////            if (bBtnVhclAvl){
////                disableFields();
////            }
////        } catch (SQLException e) {
////            e.printStackTrace();
////            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
////        }
////
////    }
//    /*OPEN WINDOW FOR TRANSFER OWNERSHIP*/
//    private void loadTransferOwnershipWindow() {
//        try {
//            Stage stage = new Stage();
//
//            FXMLLoader fxmlLoader = new FXMLLoader();
//            fxmlLoader.setLocation(getClass().getResource("VehicleNewOwnerForm.fxml"));
//
//            VehicleNewOwnerFormController loControl = new VehicleNewOwnerFormController();
//            loControl.setGRider(oApp);
//            fxmlLoader.setController(loControl);
//
//            //load the main interface
//            Parent parent = fxmlLoader.load();
//
//            parent.setOnMousePressed(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    xOffset = event.getSceneX();
//                    yOffset = event.getSceneY();
//                }
//            });
//
//            parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    stage.setX(event.getScreenX() - xOffset);
//                    stage.setY(event.getScreenY() - yOffset);
//                }
//            });
//
//            //set the main interface as the scene
//            Scene scene = new Scene(parent);
//            stage.setScene(scene);
//            stage.initStyle(StageStyle.TRANSPARENT);
//            stage.initModality(Modality.APPLICATION_MODAL);
//            stage.setTitle("");
//            stage.showAndWait();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
//            System.exit(1);
//        }
//    }
//
//    /*OPEN WINDOW FOR VEHICLE DESCRIPTION ENTRY*/
//    private void loadVehicleDescriptionWindow() {
//        try {
//            FXMLLoader fxmlLoader = new FXMLLoader();
//            fxmlLoader.setLocation(getClass().getResource("VehicleDescriptionForm.fxml"));
//            VehicleDescriptionFormController loControl = new VehicleDescriptionFormController();
//            loControl.setGRider(oApp);
//            fxmlLoader.setController(loControl);
//            Parent parent = fxmlLoader.load();
//            AnchorPane otherAnchorPane = loControl.AnchorMain;
//
//            // Get the parent of the TabContent node
//            Node tabContent = AnchorMain.getParent();
//            Parent tabContentParent = tabContent.getParent();
//
//            // If the parent is a TabPane, you can work with it directly
//            if (tabContentParent instanceof TabPane) {
//                TabPane tabpane = (TabPane) tabContentParent;
//
//                for (Tab tab : tabpane.getTabs()) {
//                    if (tab.getText().equals("Vehicle Description")) {
//                        tabpane.getSelectionModel().select(tab);
//                        return;
//                    }
//                }
//
//                Tab newTab = new Tab("Vehicle Description", parent);
//                //newTab.setStyle("-fx-font-weight: bold; -fx-pref-width: 180; -fx-font-size: 11px;");
//                newTab.setStyle("-fx-font-weight: bold; -fx-pref-width: 180; -fx-font-size: 10.5px; -fx-font-family: arial;");
//
//                tabpane.getTabs().add(newTab);
//                tabpane.getSelectionModel().select(newTab);
//                newTab.setOnCloseRequest(event -> {
//                    if (ShowMessageFX.YesNo(null, "Close Tab", "Are you sure, do you want to close tab?") == true) {
//                        if (unload != null) {
//                            unload.unloadForm(otherAnchorPane, oApp, "Vehicle Description");
//                        } else {
//                            ShowMessageFX.Warning(getStage(), "Please notify the system administrator to configure the null value at the close button.", "Warning", pxeModuleName);
//                        }
//                    } else {
//                        // Cancel the close request
//                        event.consume();
//                    }
//
//                });
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
//            System.exit(1);
//        }
//    }
//
//    /*OPEN WINDOW FOR VEHICLE DESCRIPTION ENTRY*/
//    private void loadEngineFrameWindow(Integer fnCodeType, Boolean fbState) {
//        try {
//            Stage stage = new Stage();
//
//            FXMLLoader fxmlLoader = new FXMLLoader();
//            fxmlLoader.setLocation(getClass().getResource("VehicleEngineFrameFormatForm.fxml"));
//
//            VehicleEngineFrameFormatFormController loControl = new VehicleEngineFrameFormatFormController();
//            loControl.setGRider(oApp);
//            loControl.setMakeID((String) oTransVehicle.getMaster(23));
//            loControl.setMakeDesc((String) oTransVehicle.getMaster(24));
//            loControl.setModelID((String) oTransVehicle.getMaster(25));
//            loControl.setModelDesc((String) oTransVehicle.getMaster(26));
//            loControl.setCodeType(fnCodeType);
//            loControl.setState(fbState);
//            loControl.setOpenEvent(true);
//            fxmlLoader.setController(loControl);
//
//            //load the main interface
//            Parent parent = fxmlLoader.load();
//
//            parent.setOnMousePressed(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    xOffset = event.getSceneX();
//                    yOffset = event.getSceneY();
//                }
//            });
//
//            parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    stage.setX(event.getScreenX() - xOffset);
//                    stage.setY(event.getScreenY() - yOffset);
//                }
//            });
//
//            //set the main interface as the scene
//            Scene scene = new Scene(parent);
//            stage.setScene(scene);
//            stage.initStyle(StageStyle.TRANSPARENT);
//            stage.initModality(Modality.APPLICATION_MODAL);
//            stage.setTitle("");
//            stage.showAndWait();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
//            System.exit(1);
//        } catch (SQLException ex) {
//            Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    /*LOAD CLIENT INFORMATION*/
//    private void loadClientMaster() {
//        try {
//            // Get the current event handler
//            EventHandler<ActionEvent> eventHandler = comboBox18.getOnAction();
//            // Remove the event handler to prevent it from triggering
//            comboBox18.setOnAction(null);
//            // Set the value without triggering the event
//            comboBox18.getSelectionModel().select(Integer.parseInt((String) oTrans.getMaster(18)));
//            // Add the event handler back
//            comboBox18.setOnAction(eventHandler);
//            
//            txtField01.setText((String) oTrans.getMaster(1));
//            txtField13.setText((String) oTrans.getMaster(13));
//            txtField14.setText((String) oTrans.getMaster(14));
//            textArea15.setText((String) oTrans.getMaster(15));
//            
//            if (comboBox18.getSelectionModel().getSelectedIndex() == 0) {
//                txtField02.setText((String) oTrans.getMaster(2));
//                txtField03.setText((String) oTrans.getMaster(3));
//                txtField04.setText((String) oTrans.getMaster(4));
//                txtField05.setText((String) oTrans.getMaster(5));
//                txtField06.setText((String) oTrans.getMaster(6));
//                txtField10.setText((String) oTrans.getMaster(24));
//                txtField11.setValue(strToDate(CommonUtils.xsDateShort((Date) oTrans.getMaster(11))));
//                txtField12.setText((String) oTrans.getMaster(25));
//                txtField25.setText((String) oTrans.getMaster(28));
//                comboBox07.getSelectionModel().select(Integer.parseInt((String) oTrans.getMaster(7)));
//                comboBox08.getSelectionModel().select(Integer.parseInt((String) oTrans.getMaster(8)));
//                comboBox09.getSelectionModel().select(Integer.parseInt((String) oTrans.getMaster(9)));
//            } else {
//                comboBox07.setValue("");
//                comboBox08.setValue("");
//                comboBox09.setValue("");
//                txtField11.setValue(LocalDate.of(1900, Month.JANUARY, 1)); //birthdate
//                txtField16.setText((String) oTrans.getMaster(16));
//            }
//
//        } catch (SQLException e) {
//            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
//        }
//    }
//
////    /*LOAD CLIENT VEHICLE INFORMATION*/
////    private void loadClientVehicleInfo() {
////        try {
////            txtField03V.setText((String) oTransVehicle.getMaster(3));
////            txtField04V.setText((String) oTransVehicle.getMaster(4));
////            txtField08V.setText((String) oTransVehicle.getMaster(8));
////            txtField09V.setText((String) oTransVehicle.getMaster(9));
////            txtField11V.setText((String) oTransVehicle.getMaster(11));
////            txtField20V.setText((String) oTransVehicle.getMaster(20));
////            txtField21V.setValue(strToDate(CommonUtils.xsDateShort((Date) oTransVehicle.getMaster(21))));
////            txtField22V.setText((String) oTransVehicle.getMaster(22));
////            txtField24V.setText((String) oTransVehicle.getMaster(24));
////            txtField26V.setText((String) oTransVehicle.getMaster(26));
////            txtField28V.setText((String) oTransVehicle.getMaster(28));
////            txtField30V.setText((String) oTransVehicle.getMaster(30));
////            txtField31V.setText((String) oTransVehicle.getMaster(31));
////            txtField32V.setText((String) oTransVehicle.getMaster(32));
////            textArea34V.setText((String) oTransVehicle.getMaster(34));
////        } catch (SQLException e) {
////            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
////        }
////    }
//    private void loadVehicleInfoTable() {
//        try {
//            vhclinfodata.clear();
//            if (oTransVehicle.LoadList(oTrans.getMaster("sClientID").toString(), true)) {
//                /*Set Values to table from vehicle info table*/
//                for (lnCtr = 1; lnCtr <= oTransVehicle.getItemCount(); lnCtr++) {
//                    vhclinfodata.add(new CustomerTableVehicleInfo(
//                            String.valueOf(lnCtr), //ROW
//                            (String) oTransVehicle.getDetail(lnCtr, 8),
//                            (String) oTransVehicle.getDetail(lnCtr, 20),
//                            (String) oTransVehicle.getDetail(lnCtr, 33),
//                            (String) oTransVehicle.getDetail(lnCtr, 9),
//                            (String) oTransVehicle.getDetail(lnCtr, 1),
//                            (String) oTransVehicle.getDetail(lnCtr, 6),
//                            (String) oTransVehicle.getDetail(lnCtr, 35),
//                            (String) oTransVehicle.getDetail(lnCtr, 36)
//                    ));
//                }
//            }
//
//        } catch (SQLException e) {
//            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
//        }
//
//    }
//
//    private void loadCoOwnVehicleInfoTable() {
//        try {
//            coownvhclinfodata.clear();
//            if (oTransVehicle.LoadList(oTrans.getMaster("sClientID").toString(), false)) {
//                /*Set Values to table from vehicle info table*/
//                for (lnCtr = 1; lnCtr <= oTransVehicle.getItemCount(); lnCtr++) {
//                    coownvhclinfodata.add(new CustomerTableVehicleInfo(
//                            String.valueOf(lnCtr), //ROW
//                            (String) oTransVehicle.getDetail(lnCtr, 8),
//                            (String) oTransVehicle.getDetail(lnCtr, 20),
//                            (String) oTransVehicle.getDetail(lnCtr, 33),
//                            (String) oTransVehicle.getDetail(lnCtr, 9),
//                            (String) oTransVehicle.getDetail(lnCtr, 1),
//                            (String) oTransVehicle.getDetail(lnCtr, 6),
//                            (String) oTransVehicle.getDetail(lnCtr, 35),
//                            (String) oTransVehicle.getDetail(lnCtr, 36)
//                    ));
//                }
//            }
//
//        } catch (SQLException e) {
//            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
//        }
//
//    }
//
////    private void loadVehicleHtryTable() {
//////        TODO
//////        try {
//////            vhclhtrydata.clear();
//////            if (oTransVehicle.LoadList(oTrans.getMaster("sClientID").toString())){
//////                /*Set Values to table from vehicle history table*/
//////                for (lnCtr = 1; lnCtr <= oTransVehicle.getItemCount(); lnCtr++) {
//////                    vhclhtrydata.add(new CustomerTableVehicleInfo(
//////                                String.valueOf(lnCtr) //ROW
//////                            ,  (String) oTransVehicle.getDetail(lnCtr, 8)
//////                            ,  (String) oTransVehicle.getDetail(lnCtr, 20)
//////                            ,  (String) oTransVehicle.getDetail(lnCtr, 33)
//////                            ,  (String) oTransVehicle.getDetail(lnCtr, 9)
//////                            ,  (String) oTransVehicle.getDetail(lnCtr, 1)
//////                            ,  (String) oTransVehicle.getDetail(lnCtr, 6)
//////
//////                    ));
//////                }
//////            }
//////        } catch (SQLException e) {
//////            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
//////        }
////    }
//    private void loadAddress() {
//        String sAddress, sStatus, sPrimary, sCurrent, sProvincial, sOffice;
//
//        try {
//            addressdata.clear();
//            /*Set Values to Class Address Master*/
//            for (lnCtr = 1; lnCtr <= oTransAddress.getItemCount(); lnCtr++) {
//                sAddress = oTransAddress.getAddress(lnCtr, "sAddressx").toString() + " " + oTransAddress.getAddress(lnCtr, "sBrgyName").toString() + " " + oTransAddress.getAddress(lnCtr, "sTownName").toString();
//
//                if (oTransAddress.getAddress(lnCtr, "cPrimaryx").toString().equals("1")) {
//                    sPrimary = "Y";
//                } else {
//                    sPrimary = "N";
//                }
//                if (oTransAddress.getAddress(lnCtr, "cCurrentx").toString().equals("1")) {
//                    sCurrent = "Y";
//                } else {
//                    sCurrent = "N";
//                }
//                if (oTransAddress.getAddress(lnCtr, "cProvince").toString().equals("1")) {
//                    sProvincial = "Y";
//                } else {
//                    sProvincial = "N";
//                }
//                if (oTransAddress.getAddress(lnCtr, "cOfficexx").toString().equals("1")) {
//                    sOffice = "Y";
//                } else {
//                    sOffice = "N";
//                }
//                if (oTransAddress.getAddress(lnCtr, "cRecdStat").toString().equals("1")) {
//                    sStatus = "ACTIVE";
//                } else {
//                    sStatus = "INACTIVE";
//                }
//                //if (!sAddress.isEmpty() && !sAddress.trim().equals("")) {
//                if ((!sAddress.isEmpty() && !sAddress.trim().equals("")) //&& !oTransAddress.getAddress(lnCtr, "sHouseNox").toString().isEmpty()
//                        ) {
//                    addressdata.add(new CustomerTableAddress(
//                            String.valueOf(lnCtr), //ROW
//                            sPrimary,
//                            oTransAddress.getAddress(lnCtr, "sHouseNox").toString(), //HOUSE NUMBER
//                            sAddress.toUpperCase(),
//                            oTransAddress.getAddress(lnCtr, "sZippCode").toString(),
//                            sCurrent,
//                            sProvincial,
//                            sOffice,
//                            sStatus,
//                            oTransAddress.getAddress(lnCtr, "sRemarksx").toString()
//                    ));
//                }
//            }
//
//        } catch (SQLException e) {
//            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
//        }
//
//    }
//
//    private void loadContact() {
//        String sOwnership, sPrimary, sType, sStatus;
//        try {
//            mobiledata.clear();
//            /*Set Values to Class Mobile Master*/
//            for (lnCtr = 1; lnCtr <= oTransMobile.getItemCount(); lnCtr++) {
//
//                switch (oTransMobile.getMobile(lnCtr, "cOwnerxxx").toString()) {
//                    case "0":
//                        sOwnership = "PERSONAL";
//                        break;
//                    case "1":
//                        sOwnership = "OFFICE";
//                        break;
//                    default:
//                        sOwnership = "OTHERS";
//                        break;
//                }
//
//                switch (oTransMobile.getMobile(lnCtr, "cMobileTp").toString()) {
//                    case "0":
//                        sType = "MOBILE";
//                        break;
//                    case "1":
//                        sType = "TELEPHONE";
//                        break;
//                    default:
//                        sType = "FAX";
//                        break;
//                }
//
//                if (oTransMobile.getMobile(lnCtr, "cRecdStat").toString().equals("1")) {
//                    sStatus = "ACTIVE";
//                } else {
//                    sStatus = "INACITVE";
//                }
//
//                if (oTransMobile.getMobile(lnCtr, "cPrimaryx").toString().equals("1")) {
//                    sPrimary = "Y";
//                } else {
//                    sPrimary = "N";
//                }
//
//                if (!oTransMobile.getMobile(lnCtr, "sMobileNo").toString().trim().equals("")
//                        || !oTransMobile.getMobile(lnCtr, "sMobileNo").toString().trim().isEmpty()) {
//                    mobiledata.add(new CustomerTableMobile(
//                            String.valueOf(lnCtr), //ROW
//                            sPrimary,
//                            sOwnership, //OWNERSHIP
//                            sType,
//                            oTransMobile.getMobile(lnCtr, "sMobileNo").toString(), //NUMBER
//                            sStatus,
//                            oTransMobile.getMobile(lnCtr, "sRemarksx").toString()
//                    ));
//                }
//            }
//        } catch (SQLException e) {
//            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
//        }
//    }
//
//    private void loadEmail() {
//        String sOwnership, sPrimary, sStatus;
//        try {
//            emaildata.clear();
//            /*Set Values to Class Mobile Master*/
//            for (lnCtr = 1; lnCtr <= oTransEmail.getItemCount(); lnCtr++) {
//                if (oTransEmail.getEmail(lnCtr, "cOwnerxxx").toString().equals("0")) {
//                    sOwnership = "PERSONAL";
//                } else if (oTransEmail.getEmail(lnCtr, "cOwnerxxx").toString().equals("1")) {
//                    sOwnership = "OFFICE";
//                } else {
//                    sOwnership = "OTHERS";
//                }
//
//                if (oTransEmail.getEmail(lnCtr, "cPrimaryx").toString().equals("1")) {
//                    sPrimary = "Y";
//                } else {
//                    sPrimary = "N";
//                }
//
//                if (oTransEmail.getEmail(lnCtr, "cRecdStat").toString().equals("1")) {
//                    sStatus = "ACTIVE";
//                } else {
//                    sStatus = "INACTIVE";
//                }
//
//                if (!oTransEmail.getEmail(lnCtr, "sEmailAdd").toString().trim().equals("")
//                        || !oTransEmail.getEmail(lnCtr, "sEmailAdd").toString().trim().isEmpty()) {
//                    emaildata.add(new CustomerTableEmail(
//                            String.valueOf(lnCtr), //ROW
//                            sPrimary,
//                            sOwnership, //OWNERSHIP
//                            oTransEmail.getEmail(lnCtr, "sEmailAdd").toString(), //EMAIL
//                            sStatus
//                    ));
//                }
//            }
//        } catch (SQLException e) {
//            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
//        }
//
//    }
//
//    private void loadSocialMedia() {
//        try {
//            String sSocType = "";
//            String sStatus = "";
//            socialmediadata.clear();
//
//            /*Set Values to Class Mobile Master*/
//            for (lnCtr = 1; lnCtr <= oTransSocMed.getItemCount(); lnCtr++) {
//                if (oTransSocMed.getSocMed(lnCtr, "cSocialTp").toString().equals("0")) {
//                    sSocType = "FACEBOOK";
//                } else if (oTransSocMed.getSocMed(lnCtr, "cSocialTp").toString().equals("1")) {
//                    sSocType = "WHATSAPP";
//                } else if (oTransSocMed.getSocMed(lnCtr, "cSocialTp").toString().equals("2")) {
//                    sSocType = "INSTAGRAM";
//                } else if (oTransSocMed.getSocMed(lnCtr, "cSocialTp").toString().equals("3")) {
//                    sSocType = "TIKTOK";
//                } else if (oTransSocMed.getSocMed(lnCtr, "cSocialTp").toString().equals("4")) {
//                    sSocType = "TWITTER";
//                } else {
//                    sSocType = "OTHERS";
//                }
//
//                if (oTransSocMed.getSocMed(lnCtr, "cRecdStat").toString().equals("1")) {
//                    sStatus = "ACTIVE";
//                } else {
//                    sStatus = "INACTIVE";
//                }
//                if (!oTransSocMed.getSocMed(lnCtr, "sAccountx").toString().trim().equals("")
//                        || !oTransSocMed.getSocMed(lnCtr, "sAccountx").toString().trim().isEmpty()) {
//                    socialmediadata.add(new CustomerTableSocialMedia(
//                            String.valueOf(lnCtr), //ROW
//                            sSocType, //Social Type
//                            oTransSocMed.getSocMed(lnCtr, "sAccountx").toString(), //Acount
//                            sStatus
//                    ));
//                }
//            }
//        } catch (SQLException e) {
//            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
//        }
//    }
//
////    private boolean setItemtoTable(String btnID) {
////        iTabIndex = tabPCustCont.getSelectionModel().getSelectedIndex();
////        try {
////
////            switch (btnID) {
////                case "btnTabAdd":
////                    switch (iTabIndex) {
////                        case 0:
////                            tbl_row = oTransAddress.getItemCount();
////                            break;
////                        case 1:
////                            tbl_row = oTransMobile.getItemCount();
////                            break;
////                        case 2:
////                            tbl_row = oTransEmail.getItemCount();
////                            break;
////                        case 3:
////                            tbl_row = oTransSocMed.getItemCount();
////                            break;
////                    }
////                    break;
////                case "btnTabUpd":
////                    tbl_row = pnRow;
////                    break;
////            }
////
////            if (tbl_row <= 0) {
////                ShowMessageFX.Warning(getStage(), "Invalid Table Row to Set. Insert to Table Aborted!", "Warning", null);
////                return false;
////            }
////
////            switch (iTabIndex) {
//////                case 0:
//////                    //Validate Before adding to tables
//////                    if ((txtField03Addr.getText().isEmpty() || txtField04Addr.getText().isEmpty()
//////                            || txtField07Addr.getText().isEmpty() || txtField05Addr.getText().isEmpty()
//////                            || txtField06Addr.getText().isEmpty())
//////                            || txtField03Addr.getText().trim().equals("") || txtField04Addr.getText().trim().equals("")
//////                            || txtField07Addr.getText().trim().equals("") || txtField05Addr.getText().trim().equals("")
//////                            || txtField06Addr.getText().trim().equals("")) {
//////                        ShowMessageFX.Warning(getStage(), "Invalid Address. Insert to table Aborted!", "Warning", null);
//////                        return false;
//////                    }
//////
//////                    if (!radiobtn18AddY.isSelected() && !radiobtn18AddN.isSelected()) {
//////                        ShowMessageFX.Warning(getStage(), "Please select Address Status. Insert to table Aborted!", "Warning", null);
//////                        return false;
//////                    }
//////
//////                    if (!checkBox12Addr.isSelected() && !checkBox13Addr.isSelected()
//////                            && !checkBox14Addr.isSelected() && !checkBox17Addr.isSelected()) {
//////                        ShowMessageFX.Warning(getStage(), "Please select Address Type. Insert to table Aborted!", "Warning", null);
//////                        return false;
//////                    }
//////
//////                    oTransAddress.setAddressTable(tbl_row, 3, txtField03Addr.getText());
//////                    oTransAddress.setAddressTable(tbl_row, 4, txtField04Addr.getText());
//////                    oTransAddress.setAddressTable(tbl_row, 7, txtField07Addr.getText());
//////                    oTransAddress.setAddressTable(tbl_row, 11, textArea11Addr.getText());
//////                    oTransAddress.setAddressTable(tbl_row, 25, txtField05Addr.getText());
//////                    oTransAddress.setAddressTable(tbl_row, 24, txtField06Addr.getText());
//////
//////                    if (checkBox12Addr.isSelected()) {
//////                        oTransAddress.setAddressTable(tbl_row, 12, 1);
//////                    } else {
//////                        oTransAddress.setAddressTable(tbl_row, 12, 0);
//////                    }
//////                    if (checkBox13Addr.isSelected()) {
//////                        oTransAddress.setAddressTable(tbl_row, 13, 1);
//////                    } else {
//////                        oTransAddress.setAddressTable(tbl_row, 13, 0);
//////                    }
//////                    if (checkBox14Addr.isSelected()) {
//////                        oTransAddress.setAddressTable(tbl_row, 14, 1);
//////                    } else {
//////                        oTransAddress.setAddressTable(tbl_row, 14, 0);
//////                    }
//////                    if (checkBox17Addr.isSelected()) {
//////                        oTransAddress.setAddressTable(tbl_row, 17, 1);
//////                    } else {
//////                        oTransAddress.setAddressTable(tbl_row, 17, 0);
//////                    }
//////                    if (radiobtn18AddY.isSelected()) {
//////                        oTransAddress.setAddressTable(tbl_row, 18, 1);
//////                    } else {
//////                        oTransAddress.setAddressTable(tbl_row, 18, 0);
//////                    }
//////
//////                    break;
//////                case 1: //Mobile
//////                    //Validate Before adding to tables
//////                    if (txtField03Cont.getText().isEmpty() || txtField03Cont.getText().trim().equals("")) {
//////                        ShowMessageFX.Warning(getStage(), "Invalid Mobile. Insert to table Aborted!", "Warning", null);
//////                        return false;
//////                    }
//////
//////                    if (!radiobtn11CntY.isSelected() && !radiobtn11CntN.isSelected()) {
//////                        ShowMessageFX.Warning(getStage(), "Please select Mobile Type. Insert to table Aborted!", "Warning", null);
//////                        return false;
//////                    }
//////
//////                    if (!radiobtn14CntY.isSelected() && !radiobtn14CntN.isSelected()) {
//////                        ShowMessageFX.Warning(getStage(), "Please select Mobile Status. Insert to table Aborted!", "Warning", null);
//////                        return false;
//////                    }
//////
//////                    if (comboBox05Cont.getValue().equals("")) {
//////                        ShowMessageFX.Warning(getStage(), "Please select Contact Ownership. Insert to table Aborted!", "Warning", null);
//////                        return false;
//////                    }
//////                    if (comboBox04Cont.getValue().equals("")) {
//////                        ShowMessageFX.Warning(getStage(), "Please select Mobile Type. Insert to table Aborted!", "Warning", null);
//////                        return false;
//////                    }
//////
//////                    oTransMobile.setMobile(tbl_row, 3, txtField03Cont.getText());
//////                    oTransMobile.setMobile(tbl_row, 4, comboBox04Cont.getSelectionModel().getSelectedIndex());
//////                    oTransMobile.setMobile(tbl_row, 5, comboBox05Cont.getSelectionModel().getSelectedIndex());
//////                    oTransMobile.setMobile(tbl_row, 13, textArea13Cont.getText());
//////
//////                    if (radiobtn11CntY.isSelected()) {
//////                        oTransMobile.setMobile(tbl_row, 11, 1);
//////                    } else {
//////                        oTransMobile.setMobile(tbl_row, 11, 0);
//////                    }
//////                    if (radiobtn14CntY.isSelected()) {
//////                        oTransMobile.setMobile(tbl_row, 14, 1);
//////                    } else {
//////                        oTransMobile.setMobile(tbl_row, 14, 0);
//////                    }
//////                    break;
////                case 2: //Email
//////                    //Validate Before adding to tables
//////                    if (txtField03EmAd.getText().isEmpty() || txtField03EmAd.getText().trim().equals("")) {
//////                        ShowMessageFX.Warning(getStage(), "Invalid Email. Insert to table Aborted!", "Warning", null);
//////                        return false;
//////                    }
//////
//////                    if (!CommonUtils.isValidEmail(txtField03EmAd.getText())) {
//////                        ShowMessageFX.Warning(getStage(), "Invalid Email. Insert to table Aborted!", "Warning", null);
//////                        return false;
//////                    }
//////
//////                    if (!radiobtn05EmaY.isSelected() && !radiobtn05EmaN.isSelected()) {
//////                        ShowMessageFX.Warning(getStage(), "Please select Email Type.Insert to table Aborted!", "Warning", null);
//////                        return false;
//////                    }
//////
//////                    if (!radiobtn06EmaY.isSelected() && !radiobtn06EmaN.isSelected()) {
//////                        ShowMessageFX.Warning(getStage(), "Please select Email Status. Insert to table Aborted!", "Warning", null);
//////                        return false;
//////                    }
//////
//////                    if (comboBox04EmAd.getValue().equals("")) {
//////                        ShowMessageFX.Warning(getStage(), "Please select Email Ownership. Insert to table Aborted!", "Warning", null);
//////                        return false;
//////                    }
//////
//////                    oTransEmail.setEmail(tbl_row, 3, txtField03EmAd.getText());
//////                    oTransEmail.setEmail(tbl_row, 4, comboBox04EmAd.getSelectionModel().getSelectedIndex());
//////
//////                    if (radiobtn05EmaY.isSelected()) {
//////                        oTransEmail.setEmail(tbl_row, 5, 1);
//////                    } else {
//////                        oTransEmail.setEmail(tbl_row, 5, 0);
//////                    }
//////                    if (radiobtn06EmaY.isSelected()) {
//////                        oTransEmail.setEmail(tbl_row, 6, 1);
//////                    } else {
//////                        oTransEmail.setEmail(tbl_row, 6, 0);
//////                    }
////                    break;
////                case 3: //Socila Media
//////                    //Validate Before adding to tables
//////                    if (txtField03Socm.getText().isEmpty() || txtField03Socm.getText().trim().equals("")) {
//////                        ShowMessageFX.Warning(getStage(), "Invalid Account. Insert to table Aborted!", "Warning", null);
//////                        return false;
//////                    }
//////
//////                    if (!radiobtn05SocY.isSelected() && !radiobtn05SocN.isSelected()) {
//////                        ShowMessageFX.Warning(getStage(), "Please select Account Type.Insert to table Aborted!", "Warning", null);
//////                        return false;
//////                    }
//////
//////                    if (comboBox04Socm.getValue().equals("") || comboBox04Socm.getValue() == null) {
//////                        ShowMessageFX.Warning(getStage(), "Please select Social Media Type. Insert to table Aborted!", "Warning", null);
//////                        return false;
//////                    }
//////
//////                    oTransSocMed.setSocMed(tbl_row, 3, txtField03Socm.getText());
//////                    oTransSocMed.setSocMed(tbl_row, 4, comboBox04Socm.getSelectionModel().getSelectedIndex());
//////
//////                    if (radiobtn05SocY.isSelected()) {
//////                        oTransSocMed.setSocMed(tbl_row, 5, 1);
//////                    } else {
//////                        oTransSocMed.setSocMed(tbl_row, 5, 0);
//////                    }
////                    break;
////            }
////
////        } catch (SQLException e) {
////            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
////        }
////        return true;
////    }
//
//    /*populate Address Table*/
//    private void initAddress() {
//        addrindex01.setCellValueFactory(new PropertyValueFactory<>("addrindex01"));
//        addrindex02.setCellValueFactory(new PropertyValueFactory<>("addrindex02"));
//        addrindex03.setCellValueFactory(new PropertyValueFactory<>("addrindex03"));
//        addrindex04.setCellValueFactory(new PropertyValueFactory<>("addrindex04"));
//        addrindex05.setCellValueFactory(new PropertyValueFactory<>("addrindex05"));
//        addrindex06.setCellValueFactory(new PropertyValueFactory<>("addrindex06"));
//        addrindex07.setCellValueFactory(new PropertyValueFactory<>("addrindex07"));
//        addrindex08.setCellValueFactory(new PropertyValueFactory<>("addrindex08"));
//        addrindex09.setCellValueFactory(new PropertyValueFactory<>("addrindex09"));
//        addrindex10.setCellValueFactory(new PropertyValueFactory<>("addrindex10"));
//        tblAddress.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
//            TableHeaderRow header = (TableHeaderRow) tblAddress.lookup("TableHeaderRow");
//            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//                header.setReordering(false);
//            });
//        });
//        addressdata.clear();
//        tblAddress.setItems(addressdata);
//    }
//
//    /*populate Contact Table*/
//    private void initContact() {
//        contindex01.setCellValueFactory(new PropertyValueFactory<>("contindex01"));
//        contindex02.setCellValueFactory(new PropertyValueFactory<>("contindex02"));
//        contindex03.setCellValueFactory(new PropertyValueFactory<>("contindex03"));
//        contindex04.setCellValueFactory(new PropertyValueFactory<>("contindex04"));
//        contindex05.setCellValueFactory(new PropertyValueFactory<>("contindex05"));
//        contindex06.setCellValueFactory(new PropertyValueFactory<>("contindex06"));
//        contindex07.setCellValueFactory(new PropertyValueFactory<>("contindex07"));
//        tblContact.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
//            TableHeaderRow header = (TableHeaderRow) tblContact.lookup("TableHeaderRow");
//            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//                header.setReordering(false);
//            });
//        });
//        mobiledata.clear();
//        tblContact.setItems(mobiledata);
//
//    }
//
//    /*populate Email Table*/
//    private void initEmail() {
//        emadindex01.setCellValueFactory(new PropertyValueFactory<>("emadindex01"));
//        emadindex02.setCellValueFactory(new PropertyValueFactory<>("emadindex02"));
//        emadindex03.setCellValueFactory(new PropertyValueFactory<>("emadindex03"));
//        emadindex04.setCellValueFactory(new PropertyValueFactory<>("emadindex04"));
//        emadindex05.setCellValueFactory(new PropertyValueFactory<>("emadindex05"));
//        tblEmail.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
//            TableHeaderRow header = (TableHeaderRow) tblEmail.lookup("TableHeaderRow");
//            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//                header.setReordering(false);
//            });
//        });
//        emaildata.clear();
//        tblEmail.setItems(emaildata);
//    }
//
//    /*populate Social Media Table*/
//    private void initSocialMedia() {
//        socmindex01.setCellValueFactory(new PropertyValueFactory<>("socmindex01"));
//        socmindex02.setCellValueFactory(new PropertyValueFactory<>("socmindex02"));
//        socmindex03.setCellValueFactory(new PropertyValueFactory<>("socmindex03"));
//        socmindex04.setCellValueFactory(new PropertyValueFactory<>("socmindex04"));
//
//        tblSocMed.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
//            TableHeaderRow header = (TableHeaderRow) tblSocMed.lookup("TableHeaderRow");
//            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//                header.setReordering(false);
//            });
//        });
//        socialmediadata.clear();
//        tblSocMed.setItems(socialmediadata);
//    }
//
//    /*populate vheicle information Table*/
//    private void initVehicleInfo() {
//        tblVhcllist01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
//        tblVhcllist02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
//        tblVhcllist03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
//        tblVhcllist04.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
//        tblVhcllist05.setCellValueFactory(new PropertyValueFactory<>("tblindex05"));
//        tblVhcllist06.setCellValueFactory(new PropertyValueFactory<>("tblindex08"));
//        tblVhcllist07.setCellValueFactory(new PropertyValueFactory<>("tblindex09"));
//
//        tblViewVhclInfo.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
//            TableHeaderRow header = (TableHeaderRow) tblViewVhclInfo.lookup("TableHeaderRow");
//            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//                header.setReordering(false);
//            });
//        });
//
//        tblViewVhclInfo.setItems(vhclinfodata);
//    }
//
//    /*populate vheicle information Table*/
//    private void initCoVehicleInfo() {
//        tblCoVhcllist01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
//        tblCoVhcllist02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
//        tblCoVhcllist03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
//        tblCoVhcllist04.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
//        tblCoVhcllist05.setCellValueFactory(new PropertyValueFactory<>("tblindex05"));
//        tblCoVhcllist06.setCellValueFactory(new PropertyValueFactory<>("tblindex08"));
//        tblCoVhcllist07.setCellValueFactory(new PropertyValueFactory<>("tblindex09"));
//
//        tblViewCoVhclInfo.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
//            TableHeaderRow header = (TableHeaderRow) tblViewCoVhclInfo.lookup("TableHeaderRow");
//            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//                header.setReordering(false);
//            });
//        });
//
//        tblViewCoVhclInfo.setItems(coownvhclinfodata);
//    }
//
////    /*populate vehicle history Table*/
////    private void initVehicleHtry() {
////        tblVhcllHsty01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
////        tblVhcllHsty02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
////        tblVhcllHsty03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
////        tblVhcllHsty04.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
////        tblVhcllHsty05.setCellValueFactory(new PropertyValueFactory<>("tblindex05"));
////
////        tblViewVhclHsty.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
////            TableHeaderRow header = (TableHeaderRow) tblViewVhclHsty.lookup("TableHeaderRow");
////            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
////                header.setReordering(false);
////            });
////        });
////
////        tblViewVhclHsty.setItems(vhclhtrydata);
////    }
////    private boolean validateContactInfo() {
////        iTabIndex = tabPCustCont.getSelectionModel().getSelectedIndex();
////        try {
////            switch (iTabIndex) {
////                case 0: //Address
////                    for (lnCtr = 1; lnCtr <= oTransAddress.getItemCount(); lnCtr++) {
////                        if (oTransAddress.getAddress(lnCtr, "cPrimaryx").toString().equals("1") && (lnCtr != pnRow)) {
////                            return false;
////                        }
////                    }
////                    break;
////                case 1: //Mobile
////                    for (lnCtr = 1; lnCtr <= oTransMobile.getItemCount(); lnCtr++) {
////                        if (oTransMobile.getMobile(lnCtr, "cPrimaryx").toString().equals("1") && (lnCtr != pnRow)) {
////                            return false;
////                        }
////                    }
////                    break;
////                case 2: //Email
////                    for (lnCtr = 1; lnCtr <= oTransEmail.getItemCount(); lnCtr++) {
////                        if (oTransEmail.getEmail(lnCtr, "cPrimaryx").toString().equals("1") && (lnCtr != pnRow)) {
////                            return false;
////                        }
////                    }
////                    break;
////            }
////        } catch (SQLException e) {
////            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
////        }
////        return true;
////    }
//
//    /*Populate Text Field Based on selected address in table*/
////    private void getSelectedItem() {
////        iTabIndex = tabPCustCont.getSelectionModel().getSelectedIndex();
////        try {
////            if (selectedIndex != 1) {
////                switch (iTabIndex) {
//    /*Address*/
////                    case 0:
////                        if (pnEditMode == EditMode.UPDATE) {
////                            if (oTransAddress.getAddress(pnRow, "sAddrssID").toString().equals("")) {
////                                btnTabRem.setVisible(true);
////                            } else {
////                                btnTabRem.setVisible(false);
////                            }
////                        }
////                        txtField03Addr.setText((String) oTransAddress.getAddress(pnRow, "sHouseNox"));
////                        txtField04Addr.setText((String) oTransAddress.getAddress(pnRow, "sAddressx"));
////                        txtField05Addr.setText((String) oTransAddress.getAddress(pnRow, "sTownName"));
////                        txtField06Addr.setText((String) oTransAddress.getAddress(pnRow, "sBrgyName"));
////                        txtField07Addr.setText((String) oTransAddress.getAddress(pnRow, "sZippCode"));
////                        textArea11Addr.setText((String) oTransAddress.getAddress(pnRow, "sRemarksx"));
////                        if (oTransAddress.getAddress(pnRow, "cRecdStat").toString().equals("1")) {
////                            radiobtn18AddY.setSelected(true);
////                            radiobtn18AddN.setSelected(false);
////                        } else {
////                            radiobtn18AddY.setSelected(false);
////                            radiobtn18AddN.setSelected(true);
////                        }
////                        if (oTransAddress.getAddress(pnRow, "cOfficexx").toString().equals("1")) {
////                            checkBox12Addr.setSelected(true);
////                        } else {
////                            checkBox12Addr.setSelected(false);
////                        }
////                        if (oTransAddress.getAddress(pnRow, "cProvince").toString().equals("1")) {
////                            checkBox13Addr.setSelected(true);
////                        } else {
////                            checkBox13Addr.setSelected(false);
////                        }
////                        if (oTransAddress.getAddress(pnRow, "cPrimaryx").toString().equals("1")) {
////                            checkBox14Addr.setSelected(true);
////                        } else {
////                            checkBox14Addr.setSelected(false);
////                        }
////                        if (oTransAddress.getAddress(pnRow, "cCurrentx").toString().equals("1")) {
////                            checkBox17Addr.setSelected(true);
////                        } else {
////                            checkBox17Addr.setSelected(false);
////                        }
////                        break;
//
//    /*Mobile*/
////                    case 1:
////                        if (pnEditMode == EditMode.UPDATE) {
////                            if (oTransMobile.getMobile(pnRow, "sMobileID").toString().equals("")) {
////                                btnTabRem.setVisible(true);
////                            } else {
////                                btnTabRem.setVisible(false);
////                            }
////                        }
////                        txtField03Cont.setText((String) oTransMobile.getMobile(pnRow, "sMobileNo"));
////                        textArea13Cont.setText((String) oTransMobile.getMobile(pnRow, "sRemarksx"));
////                        comboBox04Cont.getSelectionModel().select(Integer.parseInt((String) oTransMobile.getMobile(pnRow, "cMobileTp")));
////                        comboBox05Cont.getSelectionModel().select(Integer.parseInt((String) oTransMobile.getMobile(pnRow, "cOwnerxxx")));
////                        if (oTransMobile.getMobile(pnRow, "cRecdStat").toString().equals("1")) {
////                            radiobtn14CntY.setSelected(true);
////                            radiobtn14CntN.setSelected(false);
////                        } else {
////                            radiobtn14CntY.setSelected(false);
////                            radiobtn14CntN.setSelected(true);
////                        }
////                        if (oTransMobile.getMobile(pnRow, "cPrimaryx").toString().equals("1")) {
////                            radiobtn11CntY.setSelected(true);
////                            radiobtn11CntN.setSelected(false);
////                        } else {
////                            radiobtn11CntY.setSelected(false);
////                            radiobtn11CntN.setSelected(true);
////                        }
////                        break;
//
//    /*Email*/
////                    case 2:
////                        if (pnEditMode == EditMode.UPDATE) {
////                            if (oTransEmail.getEmail(pnRow, "sEmailIDx").toString().equals("")) {
////                                btnTabRem.setVisible(true);
////                            } else {
////                                btnTabRem.setVisible(false);
////                            }
////                        }
////                        txtField03EmAd.setText((String) oTransEmail.getEmail(pnRow, "sEmailAdd"));
////                        comboBox04EmAd.getSelectionModel().select(Integer.parseInt((String) oTransEmail.getEmail(pnRow, "cOwnerxxx")));
////                        if (oTransEmail.getEmail(pnRow, "cRecdStat").toString().equals("1")) {
////                            radiobtn06EmaY.setSelected(true);
////                            radiobtn06EmaN.setSelected(false);
////                        } else {
////                            radiobtn06EmaY.setSelected(false);
////                            radiobtn06EmaN.setSelected(true);
////                        }
////                        if (oTransEmail.getEmail(pnRow, "cPrimaryx").toString().equals("1")) {
////                            radiobtn05EmaY.setSelected(true);
////                            radiobtn05EmaN.setSelected(false);
////                        } else {
////                            radiobtn05EmaY.setSelected(false);
////                            radiobtn05EmaN.setSelected(true);
////                        }
////                        break;
//
//    /*Social*/
////                    case 3:
////                        if (pnEditMode == EditMode.UPDATE) {
////                            if (oTransSocMed.getSocMed(pnRow, "sSocialID").toString().equals("")) {
////                                btnTabRem.setVisible(true);
////                            } else {
////                                btnTabRem.setVisible(false);
////                            }
////                        }
////                        txtField03Socm.setText((String) oTransSocMed.getSocMed(pnRow, "sAccountx"));
////                        comboBox04Socm.getSelectionModel().select(Integer.parseInt((String) oTransSocMed.getSocMed(pnRow, "cSocialTp")));
////                        if (oTransSocMed.getSocMed(pnRow, "cRecdStat").toString().equals("1")) {
////                            radiobtn05SocY.setSelected(true);
////                            radiobtn05SocN.setSelected(false);
////                        } else {
////                            radiobtn05SocY.setSelected(false);
////                            radiobtn05SocN.setSelected(true);
////                        }
////                        break;
////                }
////            } else if (selectedIndex == 1) {
////                if (oTransVehicle.OpenRecord(vhclinfodata.get(pnRow - 1).getTblindex06())) {
////                    loadClientVehicleInfo();
//////                    pnVEditMode = oTransVehicle.getEditMode();
//////                   initVhclInfoButton(pnVEditMode);
////                } else {
////                    ShowMessageFX.Warning(getStage(), oTransVehicle.getMessage(), "Warning", null);
////                }
//////            }
//////        } catch (SQLException e) {
//////            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
//////        }
////    }
//    @FXML
//    private void tblAddress_Clicked(MouseEvent event) {
//        try {
//            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
//                pnRow = tblAddress.getSelectionModel().getSelectedIndex() + 1;
//                if (pnRow == 0) {
//                    return;
//                }
//
//                if (event.getClickCount() == 2) {
//                    loadAddressForm(pnRow, false);
//                    loadAddress();
//                }
//
//                if (oTransAddress.getAddress(pnRow, "sAddrssID").toString().isEmpty()) {
//                    btnTabRem.setVisible(true);
//                } else {
//                    btnTabRem.setVisible(false);
//                }
//
//                tblAddress.setOnKeyReleased((KeyEvent t) -> {
//                    KeyCode key = t.getCode();
//                    switch (key) {
//                        case DOWN:
//                            pnRow = tblAddress.getSelectionModel().getSelectedIndex();
//                            if (pnRow == tblAddress.getItems().size()) {
//                                pnRow = tblAddress.getItems().size();
//                            } else {
//                                int y = 1;
//                                pnRow = pnRow + y;
//                            }
//                            break;
//
//                        case UP:
//                            int pnRows = 0;
//                            int x = -1;
//                            pnRows = tblAddress.getSelectionModel().getSelectedIndex() + 1;
//                            pnRow = pnRows;
//                            break;
//                        default:
//                            return;
//                    }
//
//                    try {
//                        if (oTransAddress.getAddress(pnRow, "sAddrssID").toString().isEmpty()) {
//                            btnTabRem.setVisible(true);
//                        } else {
//                            btnTabRem.setVisible(false);
//                        }
//                    } catch (SQLException ex) {
//                        Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                });
//            }
//
////getSelectedItem();
////        tblAddress.setOnKeyReleased((KeyEvent t) -> {
////            KeyCode key = t.getCode();
////            switch (key) {
////                case DOWN:
////                    pnRow = tblAddress.getSelectionModel().getSelectedIndex();
////                    if (pnRow == tblAddress.getItems().size()) {
////                        pnRow = tblAddress.getItems().size();
////                        getSelectedItem();
////                    } else {
////                        int y = 1;
////                        pnRow = pnRow + y;
////                        getSelectedItem();
////                    }
////                    break;
////
////                case UP:
////                    int pnRows = 0;
////                    int x = -1;
////                    pnRows = tblAddress.getSelectionModel().getSelectedIndex() + 1;
////                    pnRow = pnRows;
////                    getSelectedItem();
////                    break;
////                default:
////                    return;
////            }
////        });
//        } catch (SQLException ex) {
//            Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//    }
//
//    @FXML
//    private void tblContact_Clicked(MouseEvent event) {
//        try {
//            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
//                pnRow = tblContact.getSelectionModel().getSelectedIndex() + 1;
//                if (pnRow == 0) {
//                    return;
//                }
//
//                if (event.getClickCount() == 2) {
//                    loadMobileForm(pnRow, false);
//                    loadContact();
//                }
//
//                if (oTransMobile.getMobile(pnRow, "sMobileID").toString().isEmpty()) {
//                    btnTabRem.setVisible(true);
//                } else {
//                    btnTabRem.setVisible(false);
//                }
//
//                tblContact.setOnKeyReleased((KeyEvent t) -> {
//                    KeyCode key = t.getCode();
//                    switch (key) {
//                        case DOWN:
//                            pnRow = tblContact.getSelectionModel().getSelectedIndex();
//                            if (pnRow == tblContact.getItems().size()) {
//                                pnRow = tblContact.getItems().size();
//                            } else {
//                                int y = 1;
//                                pnRow = pnRow + y;
//                            }
//                            break;
//                        case UP:
//                            int pnRows = 0;
//                            int x = -1;
//                            pnRows = tblContact.getSelectionModel().getSelectedIndex() + 1;
//                            pnRow = pnRows;
//                            break;
//                        default:
//                            return;
//                    }
//
//                    try {
//                        if (oTransMobile.getMobile(pnRow, "sMobileID").toString().isEmpty()) {
//                            btnTabRem.setVisible(true);
//                        } else {
//                            btnTabRem.setVisible(false);
//                        }
//                    } catch (SQLException ex) {
//                        Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                });
//            }
//
//            //getSelectedItem();
////        tblContact.setOnKeyReleased((KeyEvent t) -> {
////            KeyCode key = t.getCode();
////            switch (key) {
////                case DOWN:
////                    pnRow = tblContact.getSelectionModel().getSelectedIndex();
////                    if (pnRow == tblContact.getItems().size()) {
////                        pnRow = tblContact.getItems().size();
////                        getSelectedItem();
////                    } else {
////                        int y = 1;
////                        pnRow = pnRow + y;
////                        getSelectedItem();
////                    }
////                    break;
////                case UP:
////                    int pnRows = 0;
////                    int x = -1;
////                    pnRows = tblContact.getSelectionModel().getSelectedIndex() + 1;
////                    pnRow = pnRows;
////                    getSelectedItem();
////                    break;
////                default:
////                    return;
////            }
////        });
//        } catch (SQLException ex) {
//            Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    @FXML
//    private void tblEmail_Clicked(MouseEvent event) {
//        try {
//            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
//                pnRow = tblEmail.getSelectionModel().getSelectedIndex() + 1;
//                if (pnRow == 0) {
//                    return;
//                }
//                if (event.getClickCount() == 2) {
//                    loadEmailForm(pnRow, false);
//                    loadEmail();
//                }
//
//                if (oTransEmail.getEmail(pnRow, "sEmailIDx").toString().isEmpty()) {
//                    btnTabRem.setVisible(true);
//                } else {
//                    btnTabRem.setVisible(false);
//                }
//
//                tblEmail.setOnKeyReleased((KeyEvent t) -> {
//                    KeyCode key = t.getCode();
//                    switch (key) {
//                        case DOWN:
//                            pnRow = tblEmail.getSelectionModel().getSelectedIndex();
//                            if (pnRow == tblEmail.getItems().size()) {
//                                pnRow = tblEmail.getItems().size();
//                            } else {
//                                int y = 1;
//                                pnRow = pnRow + y;
//                            }
//                            break;
//                        case UP:
//                            int pnRows = 0;
//                            int x = -1;
//                            pnRows = tblEmail.getSelectionModel().getSelectedIndex() + 1;
//                            pnRow = pnRows;
//                            break;
//                        default:
//                            return;
//                    }
//
//                    try {
//                        if (oTransEmail.getEmail(pnRow, "sEmailIDx").toString().isEmpty()) {
//                            btnTabRem.setVisible(true);
//                        } else {
//                            btnTabRem.setVisible(false);
//                        }
//                    } catch (SQLException ex) {
//                        Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                });
//            }
////
////        tblEmail.setOnKeyReleased((KeyEvent t) -> {
////            KeyCode key = t.getCode();
////            switch (key) {
////                case DOWN:
////                    pnRow = tblEmail.getSelectionModel().getSelectedIndex();
////                    if (pnRow == tblEmail.getItems().size()) {
////                        pnRow = tblEmail.getItems().size();
////                        getSelectedItem();
////                    } else {
////                        int y = 1;
////                        pnRow = pnRow + y;
////                        getSelectedItem();
////                    }
////                    break;
////                case UP:
////                    int pnRows = 0;
////                    int x = -1;
////                    pnRows = tblEmail.getSelectionModel().getSelectedIndex() + 1;
////                    pnRow = pnRows;
////                    getSelectedItem();
////                    break;
////                default:
////                    return;
////            }
////        });
//        } catch (SQLException ex) {
//            Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    @FXML
//    private void tblSocMed_Clicked(MouseEvent event) {
//        try {
//            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
//                pnRow = tblSocMed.getSelectionModel().getSelectedIndex() + 1;
//                if (pnRow == 0) {
//                    return;
//                }
//                if (event.getClickCount() == 2) {
//                    loadSocialMediaForm(pnRow, false);
//                    loadSocialMedia();
//                }
//
//                if (oTransSocMed.getSocMed(pnRow, "sSocialID").toString().isEmpty()) {
//                    btnTabRem.setVisible(true);
//                } else {
//                    btnTabRem.setVisible(false);
//                }
//
//                tblSocMed.setOnKeyReleased((KeyEvent t) -> {
//                    KeyCode key = t.getCode();
//                    switch (key) {
//                        case DOWN:
//                            pnRow = tblSocMed.getSelectionModel().getSelectedIndex();
//                            if (pnRow == tblSocMed.getItems().size()) {
//                                pnRow = tblSocMed.getItems().size();
//                            } else {
//                                int y = 1;
//                                pnRow = pnRow + y;
//                            }
//                            break;
//                        case UP:
//                            int pnRows = 0;
//                            int x = -1;
//                            pnRows = tblSocMed.getSelectionModel().getSelectedIndex() + 1;
//                            pnRow = pnRows;
//                            break;
//                        default:
//                            return;
//                    }
//
//                    try {
//                        if (oTransSocMed.getSocMed(pnRow, "sSocialID").toString().isEmpty()) {
//                            btnTabRem.setVisible(true);
//                        } else {
//                            btnTabRem.setVisible(false);
//                        }
//                    } catch (SQLException ex) {
//                        Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                });
//            }
//
////        getSelectedItem();
////        tblSocMed.setOnKeyReleased((KeyEvent t) -> {
////            KeyCode key = t.getCode();
////            switch (key) {
////                case DOWN:
////                    pnRow = tblSocMed.getSelectionModel().getSelectedIndex();
////                    if (pnRow == tblSocMed.getItems().size()) {
////                        pnRow = tblSocMed.getItems().size();
////                        getSelectedItem();
////                    } else {
////                        int y = 1;
////                        pnRow = pnRow + y;
////                        getSelectedItem();
////                    }
////                    break;
////                case UP:
////                    int pnRows = 0;
////                    int x = -1;
////                    pnRows = tblSocMed.getSelectionModel().getSelectedIndex() + 1;
////                    pnRow = pnRows;
////                    getSelectedItem();
////                    break;
////                default:
////                    return;
////            }
////        });
//        } catch (SQLException ex) {
//            Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    @FXML
//    private void tblViewVhclInfo_Clicked(MouseEvent event) {
////        if (pnVEditMode == EditMode.ADDNEW || pnVEditMode == EditMode.UPDATE) {
////            if (ShowMessageFX.OkayCancel(null, "Confirmation", "You have unsaved data. Are you sure you want to retrieve a new record?") == true) {
////                clearVehicleInfoFields();
////            } else {
////                return;
////            }
////        }
////        pnRow = tblViewVhclInfo.getSelectionModel().getSelectedIndex() + 1;
////        if (pnRow == 0) {
////            return;
////        }
////        getSelectedItem();
////        bBtnVhclAvl = false;
////        tblViewVhclInfo.setOnKeyReleased((KeyEvent t) -> {
////            KeyCode key = t.getCode();
////            switch (key) {
////                case DOWN:
////                    pnRow = tblViewVhclInfo.getSelectionModel().getSelectedIndex();
////                    if (pnRow == tblViewVhclInfo.getItems().size()) {
////                        pnRow = tblViewVhclInfo.getItems().size();
////                        getSelectedItem();
////                    } else {
////                        int y = 1;
////                        pnRow = pnRow + y;
////                        getSelectedItem();
////                    }
////                    break;
////
////                case UP:
////                    int pnRows = 0;
////                    int x = -1;
////                    pnRows = tblViewVhclInfo.getSelectionModel().getSelectedIndex() + 1;
////                    pnRow = pnRows;
////                    getSelectedItem();
////                    break;
////                default:
////                    return;
////            }
////        });
//    }
//
//    /*Convert Date to String*/
//    private LocalDate strToDate(String val) {
//        DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDate localDate = LocalDate.parse(val, date_formatter);
//        return localDate;
//    }
//
//    /*Set Date Value to Master Class*/
//    public void getDate(ActionEvent event) {
//        try {
//            /*CLIENT INFORMATION*/
////            if (selectedIndex == 0) {
//            oTrans.setMaster(11, SQLUtil.toDate(String.valueOf(txtField11.getValue()), SQLUtil.FORMAT_SHORT_DATE));
////            /*CLIENT VEHICLE INFORMATION*/
////            } else if (selectedIndex == 1) {
////                oTransVehicle.setMaster(21, SQLUtil.toDate(String.valueOf(txtField21V.getValue()), SQLUtil.FORMAT_SHORT_DATE));
////            }
//        } catch (SQLException ex) {
//            Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    /*Set ComboBox Value to Master Class*/
//    @SuppressWarnings("ResultOfMethodCallIgnored")
//    private boolean setSelection() {
//        try {
//            if (comboBox18.getSelectionModel().getSelectedIndex() < 0) {
//                ShowMessageFX.Warning("No `Client Type` selected.", pxeModuleName, "Please select `Client Type` value.");
//                comboBox18.requestFocus();
//                return false;
//            } else {
//                oTrans.setMaster(18, String.valueOf(comboBox18.getSelectionModel().getSelectedIndex()));
//
//                if (comboBox18.getSelectionModel().getSelectedIndex() == 0) {
//                    if (comboBox07.getSelectionModel().getSelectedIndex() < 0) {
//                        ShowMessageFX.Warning("No `Title` selected.", pxeModuleName, "Please select `Title` value.");
//                        comboBox07.requestFocus();
//                        return false;
//                    } else {
//                        oTrans.setMaster(7, String.valueOf(comboBox07.getSelectionModel().getSelectedIndex()));
//                    }
//
//                    if (comboBox08.getSelectionModel().getSelectedIndex() < 0) {
//                        ShowMessageFX.Warning("No `Gender` selected.", pxeModuleName, "Please select `Gender` value.");
//                        comboBox08.requestFocus();
//                        return false;
//                    } else {
//                        oTrans.setMaster(8, String.valueOf(comboBox08.getSelectionModel().getSelectedIndex()));
//                    }
//
//                    if (comboBox09.getSelectionModel().getSelectedIndex() < 0) {
//                        ShowMessageFX.Warning("No `Civil Status` selected.", pxeModuleName, "Please select `Civil Status` value.");
//                        comboBox09.requestFocus();
//                        return false;
//                    } else {
//                        oTrans.setMaster(9, String.valueOf(comboBox09.getSelectionModel().getSelectedIndex()));
//                    }
//                }
//
//            }
//
//        } catch (SQLException ex) {
//            ShowMessageFX.Warning(getStage(), ex.getMessage(), "Warning", null);
//        }
//
//        return true;
//    }
//
//    /*Set TextField Value to Master Class*/
//    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {
//        try {
//            TextField txtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
//            int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
//            String lsValue = txtField.getText();
//
//            if (lsValue == null) {
//                return;
//            }
//            if (!nv) {
//                /*Lost Focus*/
// /*CLIENT INFORMATION*/
//                //if (selectedIndex == 0) {
//                switch (lnIndex) {
//                    case 2:
//                    case 3:
//                    case 4:
//                    case 5:
//                    case 6:
//                    case 13:
//                    case 14:
//                    case 16:
//                        oTrans.setMaster(lnIndex, lsValue); //Handle Encoded Value
//                        break;
//                    case 10:
//                        oTrans.setMaster(24, lsValue); //Handle Encoded Value
//                        break;
//                    case 12:
//                        oTrans.setMaster(25, lsValue); //Handle Encoded Value
//                        break;
//                    case 25:
//                        oTrans.setMaster(28, lsValue); //Handle Encoded Value
//                        break;
//                }
////                    /*CLIENT VEHICLE INFORMATION*/
////                } else if (selectedIndex == 1) {
////                    switch (lnIndex) {
////                        case 8:
////                            if (!txtField20V.getText().isEmpty() ){
////                                txtField08V.getStyleClass().remove("required-field");
////                            }
////                            oTransVehicle.setMaster(lnIndex, lsValue);
////                            break;
////                        case 20:
////                            if (!txtField08V.getText().isEmpty() ){
////                                txtField20V.getStyleClass().remove("required-field");
////                            }
////                            oTransVehicle.setMaster(lnIndex, lsValue);
////                            break;
////                        case 9:
////                        case 11:
////                        case 22:
////                        case 24:
////                        case 26:
////                        case 28:
////                            oTransVehicle.setMaster(lnIndex, lsValue);
////                            break;
////                        case 3:
////                            if (((String) oTransVehicle.getMaster(23)).equals("")) {
////                                ShowMessageFX.Warning(getStage(), "Please select Make.", "Warning", null);
////                                txtField03V.setText("");
////                                txtField24V.requestFocus();
////                                return;
////                            }
////
////                            if (((String) oTransVehicle.getMaster(25)).equals("")) {
////                                ShowMessageFX.Warning(getStage(), "Please select Model.", "Warning", null);
////                                txtField03V.setText("");
////                                txtField26V.requestFocus();
////                                return;
////                            }
////
////                            if (lsValue.length() > 5) {
////                                if (oTransVehicle.isMakeFrameOK(lsValue)) {
////                                    if (oTransVehicle.isModelFrameOK(lsValue)) {
////                                        oTransVehicle.setMaster(lnIndex, lsValue);
////                                    } else {
////                                        ShowMessageFX.Warning(getStage(), oTransVehicle.getMessage(), "Warning", null);
////                                        txtField03V.setText("");
////                                        oTransVehicle.setMaster(lnIndex, "");
////                                        loadEngineFrameWindow(1, true);
////                                    }
////                                } else {
////                                    ShowMessageFX.Warning(getStage(), oTransVehicle.getMessage(), "Warning", null);
////                                    txtField03V.setText("");
////                                    oTransVehicle.setMaster(lnIndex, "");
////                                    loadEngineFrameWindow(0, true);
////                                }
////                            } else {
////                                if(!lsValue.isEmpty()){
////                                    ShowMessageFX.Warning(getStage(), "Frame Number must not be less than 5 characters.", "Warning", null);
////                                    oTransVehicle.setMaster(lnIndex, "");
////                                    txtField03V.setText("");
////                                } else {
////                                    oTransVehicle.setMaster(lnIndex, "");
////                                }
////                            }
////
////                            break;
////                        case 4:
////                            if (((String) oTransVehicle.getMaster(23)).equals("")) {
////                                ShowMessageFX.Warning(getStage(), "Please select Make.", "Warning", null);
////                                txtField04V.setText("");
////                                txtField24V.requestFocus();
////                                return;
////                            }
////
////                            if (((String) oTransVehicle.getMaster(25)).equals("")) {
////                                ShowMessageFX.Warning(getStage(), "Please select Model.", "Warning", null);
////                                txtField04V.setText("");
////                                txtField26V.requestFocus();
////                                return;
////                            }
////
////                            if (lsValue.length() > 3) {
////                                if (oTransVehicle.isModelEngineOK(lsValue)) {
////                                    oTransVehicle.setMaster(lnIndex, lsValue);
////                                } else {
////                                    ShowMessageFX.Warning(getStage(), oTransVehicle.getMessage(), "Warning", null);
////                                    txtField04V.setText("");
////                                    oTransVehicle.setMaster(lnIndex, "");
////                                    loadEngineFrameWindow(2, true);
////                                }
////                            }  else {
////                                if(!lsValue.isEmpty()){
////                                    ShowMessageFX.Warning(getStage(), "Engine Number must not be less than 3 characters.", "Warning", null);
////                                    oTransVehicle.setMaster(lnIndex, "");
////                                    txtField04V.setText("");
////                                } else {
////                                    oTransVehicle.setMaster(lnIndex, "");
////                                }
////                            }
////                            break;
////                    }
////                }
//
//            } else {
//                txtField.selectAll();
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    };
//
//    /*Set TextArea to Master Class*/
//    final ChangeListener<? super Boolean> txtArea_Focus = (o, ov, nv) -> {
//
//        TextArea txtField = (TextArea) ((ReadOnlyBooleanPropertyBase) o).getBean();
//        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
//        String lsValue = txtField.getText();
//        String txtFieldID = txtField.getId();
//
//        if (lsValue == null) {
//            return;
//        }
//        try {
//            if (!nv) {
//                /*Lost Focus*/
// /*CLIENT INFORMATION*/
////                if (selectedIndex == 0) {
//                switch (lnIndex) {
//                    case 15:
//                        oTrans.setMaster(lnIndex, lsValue);
//                        break;
//                }
//                /*CLIENT VEHICLE INFORMATION*/
////                } else if (selectedIndex == 1) {
////                    switch (lnIndex) {
////                        case 34:
////                            oTransVehicle.setMaster(lnIndex, lsValue);
////                            break;
////                    }
////
////                }
//            } else {
//                txtField.selectAll();
//            }
//        } catch (SQLException e) {
//            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
//            System.exit(1);
//        }
//    };
////
////    public void tblEmail_Column() {
////        emadindex01.prefWidthProperty().bind(tblEmail.widthProperty().multiply(0.04));
////        emadindex02.prefWidthProperty().bind(tblEmail.widthProperty().multiply(0.14));
////        emadindex03.prefWidthProperty().bind(tblEmail.widthProperty().multiply(0.100));
////
////        emadindex01.setResizable(false);
////        emadindex02.setResizable(false);
////        emadindex03.setResizable(false);
////    }
////
////    public void tblSocMed_Column() {
////        socmindex01.prefWidthProperty().bind(tblSocMed.widthProperty().multiply(0.04));
////        socmindex02.prefWidthProperty().bind(tblSocMed.widthProperty().multiply(0.14));
////        socmindex03.prefWidthProperty().bind(tblSocMed.widthProperty().multiply(0.100));
////
////        socmindex01.setResizable(false);
////        socmindex02.setResizable(false);
////        socmindex03.setResizable(false);
////    }
//
//    /*TRIGGER FOCUS*/
//    private void txtArea_KeyPressed(KeyEvent event) {
//        if (event.getCode() == ENTER || event.getCode() == DOWN) {
//            event.consume();
//            CommonUtils.SetNextFocus((TextArea) event.getSource());
//        } else if (event.getCode() == KeyCode.UP) {
//            event.consume();
//            CommonUtils.SetPreviousFocus((TextArea) event.getSource());
//        }
//    }
//
//    /*CLIENT INFORMATION*/
//    private void txtField_KeyPressed(KeyEvent event) {
//        TextField txtField = (TextField) event.getSource();
//        int lnIndex = Integer.parseInt(((TextField) event.getSource()).getId().substring(8, 10));
//        String txtFieldID = ((TextField) event.getSource()).getId();
//        try {
//            //Set value of row where the town / brgy to be set
//            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
//                if (pnRow == 0) {
//                    tbl_row = oTransAddress.getItemCount();
//                } else {
//                    tbl_row = pnRow;
//                }
//            }
//
//            switch (event.getCode()) {
//                case F3:
//                case TAB:
//                case ENTER:
//                    switch (txtFieldID) {
//                        case "txtField01":  //Search by CLIENT ID
//                        case "txtField26": //Search by Name
//                            boolean byClientID = true;
//                            String txtValue = "";
//                            if (txtFieldID.equals("txtField26")) { //Search by Name
//                                byClientID = false;
//                                txtValue = txtField26.getText();
//                            } else {
//                                byClientID = true;
//                                txtValue = txtField01.getText();
//                            }
//
//                            if ((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) //|| (pnVEditMode == EditMode.ADDNEW || pnVEditMode == EditMode.UPDATE)
//                                    ) {
//                                if (ShowMessageFX.OkayCancel(null, "Confirmation", "You have unsaved data. Are you sure you want to browse a new record?") == true) {
//                                    oTransAddress.removeAddress(oTransAddress.getItemCount());
//                                    oTransMobile.removeMobile(oTransMobile.getItemCount());
//                                    oTransEmail.removeEmail(oTransEmail.getItemCount());
//                                    oTransSocMed.removeSocMed(oTransSocMed.getItemCount());
//                                    clearFields();
////                                    clearVehicleInfoFields();
//                                } else {
//                                    return;
//                                }
//                            }
//                            clearFields();
//                            if (oTrans.SearchRecord(txtValue, byClientID)) {
//                                if (oTransAddress.OpenRecord(oTrans.getMaster("sClientID").toString(), false)
//                                        && oTransMobile.OpenRecord(oTrans.getMaster("sClientID").toString(), false)
//                                        && oTransEmail.OpenRecord(oTrans.getMaster("sClientID").toString(), false)
//                                        && oTransSocMed.OpenRecord(oTrans.getMaster("sClientID").toString(), false)) {
//                                    loadClientMaster();
//                                    loadAddress();
//                                    loadContact();
//                                    loadEmail();
//                                    loadSocialMedia();
//                                    loadVehicleInfoTable();
//                                    loadCoOwnVehicleInfoTable();
////                                    loadVehicleHtryTable();
//                                    pnEditMode = EditMode.READY;
////                                    pnVEditMode = EditMode.UNKNOWN;
//                                } else {
//                                    ShowMessageFX.Warning(getStage(), "There was an error while loading Contact Information Details.", "Warning", null);
//                                    txtField26.clear(); // CLIENT Search
//                                    txtField01.clear(); // CLIENT ID
//                                    pnEditMode = EditMode.UNKNOWN;
////                                    pnVEditMode = EditMode.UNKNOWN;
//                                    clearFields();
////                                    clearVehicleInfoFields();
//                                }
//                            } else {
//                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
//                                txtField26.clear(); // CLIENT Search
//                                txtField01.clear(); // CLIENT ID
//                                pnEditMode = EditMode.UNKNOWN;
////                                pnVEditMode = EditMode.UNKNOWN;
//                                clearFields();
////                                clearVehicleInfoFields();
//                            }
//                            initButton(pnEditMode);
////                            if (selectedIndex == 0) {
////                                initButton(pnEditMode);
////                            } else if (selectedIndex == 1) {
////                                initVhclInfoButton(pnVEditMode);
////                                if (bBtnVhclAvl){
////                                    disableFields();
////                                }
////                            }
//                            break;
//                        case "txtField10": //Citizenship
//                            if (oTrans.searchCitizenship(txtField10.getText(), false)) {
//                                loadClientMaster();
//                            } else {
//                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
//                            }
//
//                            break;
//
//                        case "txtField12": //BirthPlace
//                            if (oTrans.searchBirthplace(txtField12.getText(), false)) {
//                                loadClientMaster();
//                            } else {
//                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
//                            }
//
//                            break;
//
//                        case "txtField25": //Spouse
//                            if (oTrans.searchSpouse(txtField25.getText(), false)) {
//                                loadClientMaster();
//                            } else {
//                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
//                            }
//
//                            break;
//
////                        case "txtField05Addr":  //Search by Town Address
////
////                            if (tbl_row <= 0) {
////                                ShowMessageFX.Warning(getStage(), "Invalid Table Row to Set. Insert to Table Aborted!", "Warning", null);
////                            }
////                            if (oTransAddress.searchTown(tbl_row, txtField05Addr.getText(), false)) {
////                                //loadAddress_textfield();
////                                txtField05Addr.setText((String) oTransAddress.getAddress(tbl_row, "sTownName"));
////                                txtField07Addr.setText((String) oTransAddress.getAddress(tbl_row, "sZippCode"));
////                            } else {
////                                ShowMessageFX.Warning(getStage(), oTransAddress.getMessage(), "Warning", null);
////                            }
////
////                            break;
////
////                        case "txtField06Addr":  //Search by Brgy Address
////
////                            if (tbl_row <= 0) {
////                                ShowMessageFX.Warning(getStage(), "Invalid Table Row to Set. Insert to Table Aborted!", "Warning", null);
////                            }
////                            if (oTransAddress.searchBarangay(tbl_row, txtField06Addr.getText(), false)) {
////                                //loadAddress_textfield();
////                                txtField06Addr.setText((String) oTransAddress.getAddress(tbl_row, "sBrgyName"));
////
////                            } else {
////                                ShowMessageFX.Warning(getStage(), oTransAddress.getMessage(), "Warning", null);
////                            }
////                            break;
//                    }
//                    break;
//                /*
//                case TAB:
//                case ENTER:
//                    switch (txtFieldID) {
//                        case "txtField10": //Citizenship
//                            if (oTrans.searchCitizenship(txtField10.getText(), false)) {
//                                loadClientMaster();
//
//                            } else {
//                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
//                            }
//
//                            break;
//
//                        case "txtField12": //BirthPlace
//                            if (oTrans.searchBirthplace(txtField12.getText(), false)) {
//                                loadClientMaster();
//                            } else {
//                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
//                            }
//
//                            break;
//
//                        case "txtField25": //Spouse
//                            if (oTrans.searchSpouse(txtField25.getText(), false)) {
//                                loadClientMaster();
//                            } else {
//                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
//                            }
//
//                            break;
//
//                        case "txtField05Addr":  //Search by Town Address
//
//                            if (tbl_row <= 0) {
//                                ShowMessageFX.Warning(getStage(), "Invalid Table Row to Set. Insert to Table Aborted!", "Warning", null);
//                            }
//                            if (oTransAddress.searchTown(tbl_row, txtField05Addr.getText(), false)) {
//                                //loadAddress_textfield();
//                                System.out.println("Town getItemCount >>> " + tbl_row);
//                                txtField05Addr.setText((String) oTransAddress.getAddress(tbl_row, "sTownName"));
//                                txtField07Addr.setText((String) oTransAddress.getAddress(tbl_row, "sZippCode"));
//                            } else {
//                                ShowMessageFX.Warning(getStage(), oTransAddress.getMessage(), "Warning", null);
//                            }
//
//                            break;
//
//                        case "txtField06Addr":  //Search by Brgy Address
//
//                            if (tbl_row <= 0) {
//                                ShowMessageFX.Warning(getStage(), "Invalid Table Row to Set. Insert to Table Aborted!", "Warning", null);
//                            }
//                            if (oTransAddress.searchBarangay(tbl_row, txtField06Addr.getText(), false)) {
//                                //loadAddress_textfield();
//                                System.out.println("Brgy getItemCount >>> " + tbl_row);
//                                txtField06Addr.setText((String) oTransAddress.getAddress(tbl_row, "sBrgyName"));
//
//                            } else {
//                                ShowMessageFX.Warning(getStage(), oTransAddress.getMessage(), "Warning", null);
//                            }
//                            break;
//                    }
//                    break;
//                 */
//            }
//
//        } catch (SQLException e) {
//            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
//        }
//
//        switch (event.getCode()) {
//            case ENTER:
//            case DOWN:
//                CommonUtils.SetNextFocus(txtField);
//                break;
//            case UP:
//                CommonUtils.SetPreviousFocus(txtField);
//        }
//
//    }
//
//    /*CLIENT VEHICLE INFORMATION*/
////    private void txtField_KeyPressed_Vhcl(KeyEvent event) {
////        TextField txtField = (TextField) event.getSource();
////        int lnIndex = Integer.parseInt(((TextField) event.getSource()).getId().substring(8, 10));
////        String txtFieldID = ((TextField) event.getSource()).getId();
////        try {
////            switch (event.getCode()) {
////                case F3:
////                case TAB:
////                case ENTER:
////                    switch (lnIndex) {
////                        case 24: //MAKE
////                            if (oTransVehicle.searchVehicleMake(txtField24V.getText())) {
////                            } else {
////                                ShowMessageFX.Warning(getStage(), oTransVehicle.getMessage(), "Warning", null);
////                                txtField24V.setText("");
////                            }
////                            loadClientVehicleInfo();
////                            break;
////                        case 26: //MODEL
////                            if (oTransVehicle.getMaster(23).toString().isEmpty()) {
////                                ShowMessageFX.Warning(getStage(), "Please select Vehicle Make.", "Warning", null);
////                                oTransVehicle.setMaster(5, "");
////                                txtField24V.requestFocus();
////                                return;
////                            }
////                            if (oTransVehicle.searchVehicleModel(txtField26V.getText())) {
////                            } else {
////                                ShowMessageFX.Warning(getStage(), oTransVehicle.getMessage(), "Warning", null);
////                                txtField26V.setText("");
////                            }
////                            loadClientVehicleInfo();
////                            break;
////                        case 28: //TYPE
////                            if (oTransVehicle.getMaster(23).toString().isEmpty()) {
////                                ShowMessageFX.Warning(getStage(), "Please select Vehicle Make.", "Warning", null);
////                                oTransVehicle.setMaster(5, "");
////                                txtField24V.requestFocus();
////                                return;
////                            }
////                            if (oTransVehicle.getMaster(25).toString().isEmpty()) {
////                                ShowMessageFX.Warning(getStage(), "Please select Vehicle Model.", "Warning", null);
////                                oTransVehicle.setMaster(5, "");
////                                txtField26V.requestFocus();
////                                return;
////                            }
////                            if (oTransVehicle.searchVehicleType(txtField28V.getText())) {
////                            } else {
////                                ShowMessageFX.Warning(getStage(), oTransVehicle.getMessage(), "Warning", null);
////                                txtField28V.setText("");
////                            }
////                            loadClientVehicleInfo();
////                            break;
////                        case 31: //TRANSMISSION
////                            if (oTransVehicle.getMaster(23).toString().isEmpty()) {
////                                ShowMessageFX.Warning(getStage(), "Please select Vehicle Make.", "Warning", null);
////                                oTransVehicle.setMaster(5, "");
////                                txtField24V.requestFocus();
////                                return;
////                            }
////                            if (oTransVehicle.getMaster(25).toString().isEmpty()) {
////                                ShowMessageFX.Warning(getStage(), "Please select Vehicle Model.", "Warning", null);
////                                oTransVehicle.setMaster(5, "");
////                                txtField26V.requestFocus();
////                                return;
////                            }
////                            if (oTransVehicle.getMaster(27).toString().isEmpty()) {
////                                ShowMessageFX.Warning(getStage(), "Please select Vehicle Type.", "Warning", null);
////                                oTransVehicle.setMaster(5, "");
////                                txtField28V.requestFocus();
////                                return;
////                            }
////                            if (oTransVehicle.searchVehicleTrnsMn(txtField31V.getText())) {
////                            } else {
////                                ShowMessageFX.Warning(getStage(), oTransVehicle.getMessage(), "Warning", null);
////                                txtField31V.setText("");
////                            }
////                            loadClientVehicleInfo();
////                            break;
////                        case 30: //COLOR
////                            if (oTransVehicle.getMaster(23).toString().isEmpty()) {
////                                ShowMessageFX.Warning(getStage(), "Please select Vehicle Make.", "Warning", null);
////                                oTransVehicle.setMaster(5, "");
////                                txtField24V.requestFocus();
////                                return;
////                            }
////                            if (oTransVehicle.getMaster(25).toString().isEmpty()) {
////                                ShowMessageFX.Warning(getStage(), "Please select Vehicle Model.", "Warning", null);
////                                oTransVehicle.setMaster(5, "");
////                                txtField26V.requestFocus();
////                                return;
////                            }
////                            if (oTransVehicle.getMaster(27).toString().isEmpty()) {
////                                ShowMessageFX.Warning(getStage(), "Please select Vehicle Type.", "Warning", null);
////                                oTransVehicle.setMaster(5, "");
////                                txtField28V.requestFocus();
////                                return;
////                            }
////                            if (oTransVehicle.getMaster(31).toString().isEmpty()) {
////                                ShowMessageFX.Warning(getStage(), "Please select Vehicle Transmission.", "Warning", null);
////                                oTransVehicle.setMaster(5, "");
////                                txtField31V.requestFocus();
////                                return;
////                            }
////                            if (oTransVehicle.searchVehicleColor(txtField30V.getText())) {
////                            } else {
////                                ShowMessageFX.Warning(getStage(), oTransVehicle.getMessage(), "Warning", null);
////                                txtField30V.setText("");
////                            }
////                            loadClientVehicleInfo();
////                            break;
////                        case 32: //YEAR
////                            if (oTransVehicle.getMaster(23).toString().isEmpty()) {
////                                ShowMessageFX.Warning(getStage(), "Please select Vehicle Make.", "Warning", null);
////                                oTransVehicle.setMaster(5, "");
////                                txtField24V.requestFocus();
////                                return;
////                            }
////                            if (oTransVehicle.getMaster(25).toString().isEmpty()) {
////                                ShowMessageFX.Warning(getStage(), "Please select Vehicle Model.", "Warning", null);
////                                oTransVehicle.setMaster(5, "");
////                                txtField26V.requestFocus();
////                                return;
////                            }
////                            if (oTransVehicle.getMaster(27).toString().isEmpty()) {
////                                ShowMessageFX.Warning(getStage(), "Please select Vehicle Type.", "Warning", null);
////                                oTransVehicle.setMaster(5, "");
////                                txtField28V.requestFocus();
////                                return;
////                            }
////                            if (oTransVehicle.getMaster(31).toString().isEmpty()) {
////                                ShowMessageFX.Warning(getStage(), "Please select Vehicle Transmission.", "Warning", null);
////                                oTransVehicle.setMaster(5, "");
////                                txtField31V.requestFocus();
////                                return;
////                            }
////                            if (oTransVehicle.getMaster(29).toString().isEmpty()) {
////                                ShowMessageFX.Warning(getStage(), "Please select Vehicle Color.", "Warning", null);
////                                oTransVehicle.setMaster(5, "");
////                                txtField30V.requestFocus();
////                                return;
////                            }
////                            if (oTransVehicle.searchVehicleYearMdl(txtField32V.getText())) {
////                            } else {
////                                ShowMessageFX.Warning(getStage(), oTransVehicle.getMessage(), "Warning", null);
////                                txtField32V.setText("");
////                            }
////                            loadClientVehicleInfo();
////                            break;
////                        case 9:
////                            if (oTransVehicle.searchDealer(txtField09V.getText())) {
////                                loadClientVehicleInfo();
////                            } else {
////                                ShowMessageFX.Warning(getStage(), oTransVehicle.getMessage(), "Warning", null);
////                            }
////                        case 22:
////                            if (oTransVehicle.searchRegsplace(txtField22V.getText())) {
////                                loadClientVehicleInfo();
////                            } else {
////                                ShowMessageFX.Warning(getStage(), oTransVehicle.getMessage(), "Warning", null);
////                            }
////                    }
////            }
////
////        } catch (SQLException e) {
////            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
////        }
////
////        switch (event.getCode()) {
////            case ENTER:
////            case DOWN:
////                CommonUtils.SetNextFocus(txtField);
////                break;
////            case UP:
////                CommonUtils.SetPreviousFocus(txtField);
////        }
////    }
//    //Set CheckBox Action
////    private void cmdCheckBox_Click(ActionEvent event) {
////        String scbSel = ((CheckBox) event.getSource()).getId();
////        switch (scbSel) {
////            /*client_address*/
////            case "checkBox17Addr": // Current
////                if (checkBox17Addr.isSelected()) {
////                    checkBox12Addr.setSelected(false);
////                }
////                break;
////            case "checkBox13Addr": // Provincial
////                if (checkBox13Addr.isSelected()) {
////                    checkBox12Addr.setSelected(false);
////                }
////                break;
////            case "checkBox12Addr": // Office
////                if (checkBox12Addr.isSelected()) {
////                    checkBox17Addr.setSelected(false);
////                    checkBox13Addr.setSelected(false);
////                }
////                break;
////        }
////    }
//    //Set Radiobuttons Action
////    private void cmdRadioButton_Click(ActionEvent event) {
////        String srdbSel = ((RadioButton) event.getSource()).getId();
////        switch (srdbSel) {
////            /*client_social_media*/
////            case "radiobtn05SocY":
////                cmdRadioButton(radiobtn05SocY, radiobtn05SocN);
////                break;
////            case "radiobtn05SocN":
////                cmdRadioButton(radiobtn05SocN, radiobtn05SocY);
////                break;
////            /*client_email_address*/
////            case "radiobtn06EmaY":
////                cmdRadioButton(radiobtn06EmaY, radiobtn06EmaN);
////                radiobtn05EmaY.setDisable(false);
////                break;
////            case "radiobtn06EmaN":
////                cmdRadioButton(radiobtn06EmaN, radiobtn06EmaY);
////                radiobtn05EmaY.setSelected(false);
////                radiobtn05EmaN.setSelected(true);
////                radiobtn05EmaY.setDisable(true);
////                break;
////            case "radiobtn05EmaY":
////                cmdRadioButton(radiobtn05EmaY, radiobtn05EmaN);
////                break;
////            case "radiobtn05EmaN":
////                cmdRadioButton(radiobtn05EmaN, radiobtn05EmaY);
////                break;
////            /*client_mobile*/
////            case "radiobtn11CntY":
////                cmdRadioButton(radiobtn11CntY, radiobtn11CntN);
////                break;
////            case "radiobtn11CntN":
////                cmdRadioButton(radiobtn11CntN, radiobtn11CntY);
////                break;
////            case "radiobtn14CntY":
////                cmdRadioButton(radiobtn14CntY, radiobtn14CntN);
////                radiobtn11CntY.setDisable(false);
////                break;
////            case "radiobtn14CntN":
////                cmdRadioButton(radiobtn14CntN, radiobtn14CntY);
////                radiobtn11CntY.setSelected(false);
////                radiobtn11CntN.setSelected(true);
////                radiobtn11CntY.setDisable(true);
////                break;
////            /*client_address*/
////            case "radiobtn18AddY":
////                cmdRadioButton(radiobtn18AddY, radiobtn18AddN);
////                checkBox14Addr.setDisable(false);
////                break;
////            case "radiobtn18AddN":
////                cmdRadioButton(radiobtn18AddN, radiobtn18AddY);
////                checkBox14Addr.setSelected(false);
////                checkBox14Addr.setDisable(true);
////                break;
////        }
////    }
////    //Select and Unselect Radio Button
////
////    private void cmdRadioButton(RadioButton rdbsel, RadioButton rdbUnsel) {
////        if (rdbsel.isSelected() && rdbUnsel.isSelected()) {
////            rdbUnsel.setSelected(false);
////        }
////    }
//    public void comboChange() {
//        try {
//            boolean bAction = true;
//            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
//                /*     if(ShowMessageFX.OkayCancel(null, "Confirmation", "Are you sure, you want to change CLIENT Type?") == true){
//                    } else {
//                        comboBox18.getSelectionModel().select(iCLIENTType);
//                        return;
//                    }
//                 */
//
//                if (comboBox18.getSelectionModel().getSelectedIndex() == 1) {
//                    bAction = true;
//                    //txtField16.setDisable(false); //company name
//                    //txtField29.setDisable(false); // Business Style
//                    txtField02.clear(); //last name
//                    txtField03.clear(); //first name
//                    txtField04.clear(); //mid name
//                    txtField06.clear(); //suffix
//                    txtField05.clear(); //maiden nametxtField11.setValue(strToDate(CommonUtils.xsDateShort((Date) oTrans.getMaster(11))));
//                    txtField12.clear(); // birth plce
//                    txtField10.clear(); //citizenship
//                    txtField14.clear(); //LTO NO
//                    comboBox07.getSelectionModel().select(Integer.parseInt((String) oTrans.getMaster(7)));
//                    comboBox08.getSelectionModel().select(Integer.parseInt((String) oTrans.getMaster(8)));
//                    comboBox09.getSelectionModel().select(Integer.parseInt((String) oTrans.getMaster(9)));
//                    txtField25.clear(); // Spouse
//
//                    /*Clear Red Color for required fileds*/
//                    txtField02.getStyleClass().remove("required-field");
//                    txtField03.getStyleClass().remove("required-field");
//
//                } else if (comboBox18.getSelectionModel().getSelectedIndex() == 2) {
//
//                } else {
//                    bAction = false;
//                    //txtField16.setDisable(true); //company name
//                    //txtField29.setDisable(true); // Business Style
//                    txtField16.clear(); //company name
//                    /*Clear Red Color for required fileds*/
//                    txtField16.getStyleClass().remove("required-field");
//                }
//
//                cmdCLIENTType(true);
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//    }
//
//    /*Enabling / Disabling Fields*/
//    private void initButton(int fnValue) {
//        pnRow = 0;
//        /* NOTE:
//               lbShow (FALSE)= invisible
//               !lbShow (TRUE)= visible
//         */
//        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
//
//        /*CLIENT VEHICLE INFO TAB*/
//        tabVhclInfo.setDisable(fnValue == EditMode.ADDNEW);
//
//        /*CLIENT Master*/
//        txtField14.setDisable(!lbShow); //LTO NO
//        txtField13.setDisable(!lbShow); //TIN NO
//        comboBox18.setDisable(!lbShow); //CLIENT type
//        textArea15.setDisable(!lbShow); //Remarks
//        txtField02.setDisable(!lbShow); //last name
//        txtField03.setDisable(!lbShow); //first name
//        txtField04.setDisable(!lbShow); //mid name
//        txtField06.setDisable(!lbShow); //suffix
//        txtField05.setDisable(!lbShow); //maiden name
//        txtField12.setDisable(!lbShow); // birth plce
//        txtField10.setDisable(!lbShow); //citizenship
//        txtField11.setDisable(!lbShow); //bdate
//        comboBox08.setDisable(!lbShow); //Gender
//        comboBox09.setDisable(!lbShow); //Civil Stat
//        comboBox07.setDisable(!lbShow); //Title
//        txtField25.setDisable(!lbShow); // Spouse
//        txtField16.setDisable(!lbShow); //company name
//        btnCancel.setVisible(lbShow);
//        btnCancel.setManaged(lbShow);
//        cmdCLIENTType(lbShow);
//
//        if (comboBox09.getSelectionModel().getSelectedIndex() == 0) {
//            txtField25.setText("");
//            txtField25.setDisable(true);
//        } else {
//            txtField25.setDisable(!lbShow);
//        }
//
//        /*Address*/
////        txtField03Addr.setDisable(!lbShow); //House No
////        txtField04Addr.setDisable(!lbShow); //Street / Address
////        txtField05Addr.setDisable(!lbShow); // Town
////        txtField06Addr.setDisable(!lbShow); // Brgy
////        txtField07Addr.setDisable(!lbShow); //Zip code
////        radiobtn18AddY.setDisable(!lbShow); //Active status
////        radiobtn18AddN.setDisable(!lbShow); //Active status
////        checkBox14Addr.setDisable(!lbShow); //Primary
////        checkBox17Addr.setDisable(!lbShow); //Current
////        checkBox12Addr.setDisable(!lbShow); //Office
////        checkBox13Addr.setDisable(!lbShow); //Provincial
////        textArea11Addr.setDisable(!lbShow); // Address Remarks
////
////        /*Contact No*/
////        comboBox05Cont.setDisable(!lbShow); // Contact Ownership
////        comboBox04Cont.setDisable(!lbShow); // Mobile Type
////        txtField03Cont.setDisable(!lbShow);  //Mobile Number
////        radiobtn14CntY.setDisable(!lbShow); // Contact Active Status
////        radiobtn14CntN.setDisable(!lbShow); // Contact Active Status
////        radiobtn11CntY.setDisable(!lbShow); // Contact Primary
////        radiobtn11CntN.setDisable(!lbShow); // Contact Primary
////        textArea13Cont.setDisable(!lbShow); // Contact Remarks
////
////        /*Email Address*/
////        comboBox04EmAd.setDisable(!lbShow); // Email Ownership
////        radiobtn06EmaY.setDisable(!lbShow); // Email Active status
////        radiobtn06EmaN.setDisable(!lbShow); // Email Active status
////        radiobtn05EmaY.setDisable(!lbShow); // Email Primary
////        radiobtn05EmaN.setDisable(!lbShow); // Email Primary
////        txtField03EmAd.setDisable(!lbShow); // Email Address
////
////        /*Social Media*/
////        radiobtn05SocY.setDisable(!lbShow); // SocMed Active status
////        radiobtn05SocN.setDisable(!lbShow); // SocMed Active status
////        txtField03Socm.setDisable(!lbShow); // SocMed Account
////        comboBox04Socm.setDisable(!lbShow); // SocMed Type
//        btnAdd.setVisible(!lbShow);
//        btnAdd.setManaged(!lbShow);
//        //if lbShow = false hide btn
//        btnEdit.setVisible(false);
//        btnEdit.setManaged(false);
//        btnSave.setVisible(lbShow);
//        btnSave.setManaged(lbShow);
//        btnTabAdd.setVisible(lbShow);
//        //btnTabUpd.setVisible(lbShow);
//        btnTabRem.setVisible(false);
////        if (fnValue == EditMode.ADDNEW) {
////            btnTabRem.setVisible(lbShow);
////        } else {
////            btnTabRem.setVisible(false);
////        }
//        if (fnValue == EditMode.READY) { //show edit if user clicked save / browse
//            btnEdit.setVisible(true);
//            btnEdit.setManaged(true);
//        }
//
//        if (fnValue == EditMode.UPDATE || fnValue == EditMode.READY) {
//            comboBox18.setDisable(true); //CLIENT type *Do not allow user to change client type in Edit Mode
//            //Clear Contact details fields
//            /*Clear Red Color for required fileds*/
////            txtField03Addr.getStyleClass().remove("required-field");
////            txtField05Addr.getStyleClass().remove("required-field");
////            txtField06Addr.getStyleClass().remove("required-field");
////            txtField03Cont.getStyleClass().remove("required-field");
////
////            /*Address*/
////            txtField03Addr.clear(); //House No
////            txtField04Addr.clear(); //Street / Address
////            txtField05Addr.clear(); // Town
////            txtField06Addr.clear(); // Brgy
////            txtField07Addr.clear(); //Zip code
////            radiobtn18AddY.setSelected(false); //Active status
////            radiobtn18AddN.setSelected(false); //Active status
////            checkBox14Addr.setSelected(false); //Primary
////            checkBox17Addr.setSelected(false); //Current
////            checkBox12Addr.setSelected(false); //Office
////            checkBox13Addr.setSelected(false); //Provincial
////            textArea11Addr.clear(); // Address Remarks
////
////            /*Contact No*/
////            comboBox05Cont.setValue(null); // Contact Ownership
////            comboBox04Cont.setValue(null); // Mobile Type
////            txtField03Cont.clear();  //Mobile Number
////            radiobtn14CntY.setSelected(false); // Contact Active Status
////            radiobtn14CntN.setSelected(false); // Contact Active Status
////            radiobtn11CntY.setSelected(false); // Contact Primary
////            radiobtn11CntN.setSelected(false); // Contact Primary
////            textArea13Cont.clear(); // Contact Remarks
////
////            /*Email Address*/
////            comboBox04EmAd.setValue(null); // Email Ownership
////            radiobtn06EmaY.setSelected(false); // Email Active status
////            radiobtn06EmaN.setSelected(false); // Email Active status
////            radiobtn05EmaY.setSelected(false); // Email Primary
////            radiobtn05EmaN.setSelected(false); // Email Primary
////            txtField03EmAd.clear(); // Email Address
////
////            /*Social Media*/
////            radiobtn05SocY.setSelected(false); // SocMed Active status
////            radiobtn05SocN.setSelected(false); // SocMed Active status
////            txtField03Socm.clear(); // SocMed Account
////            comboBox04Socm.setValue(null); // SocMed Type
//        }
//
////        if (lbShow) {
////            /*Active Status*/
////            radiobtn18AddY.setSelected(true); //Active status
////            radiobtn14CntY.setSelected(true); // Contact Active Status
////            radiobtn06EmaY.setSelected(true); // Email Active status
////            radiobtn05SocY.setSelected(true); // SocMed Active status
////        }
//    }
//
//    public void cmdCLIENTType(boolean bValue) {
//        if (bValue) {
//            switch (comboBox18.getSelectionModel().getSelectedIndex()) {
//                case 1:
//                    txtField16.setDisable(false); //company name
//                    txtField02.setDisable(bValue); //last name
//                    txtField03.setDisable(bValue); //first name
//                    txtField04.setDisable(bValue); //mid name
//                    txtField06.setDisable(bValue); //suffix
//                    txtField05.setDisable(bValue); //maiden name
//                    txtField12.setDisable(bValue); // birth plce
//                    txtField10.setDisable(bValue); //citizenship
//                    txtField11.setDisable(bValue); //bdate
//                    comboBox08.setDisable(bValue); //Gender
//                    comboBox09.setDisable(bValue); //Civil Stat
//                    comboBox07.setDisable(bValue); //Title
//                    txtField25.setDisable(bValue); // Spouse
//                    break;
//                case 2:
//                    txtField16.setDisable(!bValue); //company name
//                    txtField02.setDisable(!bValue); //last name
//                    txtField03.setDisable(!bValue); //first name
//                    txtField04.setDisable(!bValue); //mid name
//                    txtField06.setDisable(!bValue); //suffix
//                    txtField05.setDisable(!bValue); //maiden name
//                    txtField12.setDisable(!bValue); // birth plce
//                    txtField10.setDisable(!bValue); //citizenship
//                    txtField11.setDisable(!bValue); //bdate
//                    comboBox08.setDisable(!bValue); //Gender
//                    comboBox09.setDisable(!bValue); //Civil Stat
//                    comboBox07.setDisable(!bValue); //Title
//                    txtField25.setDisable(!bValue); // Spouse
//                    break;
//                default:
//                    txtField16.setDisable(true); //company name
//                    txtField02.setDisable(!bValue); //last name
//                    txtField03.setDisable(!bValue); //first name
//                    txtField04.setDisable(!bValue); //mid name
//                    txtField06.setDisable(!bValue); //suffix
//                    txtField05.setDisable(!bValue); //maiden name
//                    txtField12.setDisable(!bValue); // birth plce
//                    txtField10.setDisable(!bValue); //citizenship
//                    txtField11.setDisable(!bValue); //bdate
//                    comboBox08.setDisable(!bValue); //Gender
//                    comboBox09.setDisable(!bValue); //Civil Stat
//                    comboBox07.setDisable(!bValue); //Title
//                    txtField25.setDisable(!bValue); // Spouse
//                    break;
//            }
//        }
//    }
//
////    public void clearContactInfo() {
////        /*Address*/
////        txtField03Addr.clear(); //House No
////        txtField04Addr.clear(); //Street / Address
////        txtField05Addr.clear(); // Town
////        txtField06Addr.clear(); // Brgy
////        txtField07Addr.clear(); //Zip code
////        radiobtn18AddY.setSelected(true); //Active status
////        radiobtn18AddN.setSelected(false); //Active status
////        checkBox14Addr.setSelected(false); //Primary
////        checkBox17Addr.setSelected(false); //Current
////        checkBox12Addr.setSelected(false); //Office
////        checkBox13Addr.setSelected(false); //Provincial
////        textArea11Addr.clear(); // Address Remarks
////
////        //Mobile
////        comboBox05Cont.setValue(null); // Contact Ownership
////        comboBox04Cont.setValue(null); // Mobile Type
////        txtField03Cont.clear();  //Mobile Number
////        radiobtn14CntY.setSelected(true); // Contact Active Status
////        radiobtn14CntN.setSelected(false); // Contact Active Status
////        radiobtn11CntY.setSelected(false); // Contact Primary
////        radiobtn11CntN.setSelected(false); // Contact Primary
////        textArea13Cont.clear(); // Contact Remarks
////
////        //Email
////        comboBox04EmAd.setValue(null); // Email Ownership
////        radiobtn06EmaY.setSelected(true); // Email Active status
////        radiobtn06EmaN.setSelected(false); // Email Active status
////        radiobtn05EmaY.setSelected(false); // Email Primary
////        radiobtn05EmaN.setSelected(false); // Email Primary
////        txtField03EmAd.clear(); // Email Address
////
////        //Social Media
////        radiobtn05SocY.setSelected(true); // SocMed Active status
////        radiobtn05SocN.setSelected(false); // SocMed Active status
////        txtField03Socm.clear(); // SocMed Account
////        comboBox04Socm.setValue(null); // SocMed Type
////
////        /*Clear Red Color for required fileds*/
////        txtField03Addr.getStyleClass().remove("required-field");
////        txtField05Addr.getStyleClass().remove("required-field");
////        txtField06Addr.getStyleClass().remove("required-field");
////        txtField03Cont.getStyleClass().remove("required-field");
////    }
//
//    /*Clear Fields*/
//    public void clearFields() {
//        pnRow = 0;
//        /*clear tables*/
//        addressdata.clear();
//        mobiledata.clear();
//        emaildata.clear();
//        socialmediadata.clear();
//
//        removeRequired();
////        txtField03Addr.getStyleClass().remove("required-field");
////        txtField05Addr.getStyleClass().remove("required-field");
////        txtField06Addr.getStyleClass().remove("required-field");
////        txtField03Cont.getStyleClass().remove("required-field");
//
//        /*CLIENT Master*/
//        txtField02.clear(); //last name
//        txtField03.clear(); //first name
//        txtField04.clear(); //mid name
//        txtField06.clear(); //suffix
//        txtField05.clear(); //maiden name
//        txtField12.clear(); // birth plce
//        txtField16.clear(); //company name
//        txtField10.clear(); //citizenship
//        txtField14.clear(); //LTO NO
//        txtField13.clear(); //TIN NO
//        textArea15.clear(); //Remarks
//        txtField25.clear(); // Spouse
//        txtField11.setValue(LocalDate.of(1900, Month.JANUARY, 1)); //birthdate
//
//        //txtField11.setValue(null); //bdate Do not clear bdate since script is already assigning value to prevent nullpointerexception
//        comboBox18.setValue(null); //CLIENT type
//        comboBox08.setValue(null); //Gender
//        comboBox09.setValue(null); //Civil Stat
//        comboBox07.setValue(null); //Title
//
//        /*Address*/
////        txtField03Addr.clear(); //House No
////        txtField04Addr.clear(); //Street / Address
////        txtField05Addr.clear(); // Town
////        txtField06Addr.clear(); // Brgy
////        txtField07Addr.clear(); //Zip code
////        radiobtn18AddY.setSelected(false); //Active status
////        radiobtn18AddN.setSelected(false); //Active status
////        checkBox14Addr.setSelected(false); //Primary
////        checkBox17Addr.setSelected(false); //Current
////        checkBox12Addr.setSelected(false); //Office
////        checkBox13Addr.setSelected(false); //Provincial
////        textArea11Addr.clear(); // Address Remarks
////
////        /*Contact No*/
////        comboBox05Cont.setValue(null); // Contact Ownership
////        comboBox04Cont.setValue(null); // Mobile Type
////        txtField03Cont.clear();  //Mobile Number
////        radiobtn14CntY.setSelected(false); // Contact Active Status
////        radiobtn14CntN.setSelected(false); // Contact Active Status
////        radiobtn11CntY.setSelected(false); // Contact Primary
////        radiobtn11CntN.setSelected(false); // Contact Primary
////        textArea13Cont.clear(); // Contact Remarks
////
////        /*Email Address*/
////        comboBox04EmAd.setValue(null); // Email Ownership
////        radiobtn06EmaY.setSelected(false); // Email Active status
////        radiobtn06EmaN.setSelected(false); // Email Active status
////        radiobtn05EmaY.setSelected(false); // Email Primary
////        radiobtn05EmaN.setSelected(false); // Email Primary
////        txtField03EmAd.clear(); // Email Address
////
////        /*Social Media*/
////        radiobtn05SocY.setSelected(false); // SocMed Active status
////        radiobtn05SocN.setSelected(false); // SocMed Active status
////        txtField03Socm.clear(); // SocMed Account
////        comboBox04Socm.setValue(null); // SocMed Type
//    }
//
//    private void removeRequired() {
//        txtFieldAnimation.removeShakeAnimation(txtField16, txtFieldAnimation.shakeTextField(txtField16), "required-field");
//        txtFieldAnimation.removeShakeAnimation(txtField02, txtFieldAnimation.shakeTextField(txtField02), "required-field");
//        txtFieldAnimation.removeShakeAnimation(txtField03, txtFieldAnimation.shakeTextField(txtField03), "required-field");
//
//    }
////    private void initVhclInfoButton(int fnValue) {
////        pnRow = 0;
////        /* NOTE:
////               lbShow (FALSE)= invisible
////               !lbShow (TRUE)= visible
////         */
////        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
////
//////        if (!txtField26V.getText().isEmpty()){
//////            txtField03V.setDisable(!lbShow);
//////            txtField04V.setDisable(!lbShow);
//////        } else {
//////            txtField03V.setDisable(true);
//////            txtField04V.setDisable(true);
//////        }
////
////        txtField03V.setDisable(!lbShow);
////        txtField04V.setDisable(!lbShow);
////        txtField08V.setDisable(!lbShow);
////        txtField09V.setDisable(!lbShow);
////        txtField11V.setDisable(!lbShow);
////        txtField20V.setDisable(!lbShow);
////        txtField21V.setDisable(!lbShow);
////        txtField22V.setDisable(!lbShow);
////        txtField24V.setDisable(!lbShow);
////        txtField26V.setDisable(!lbShow);
////        txtField28V.setDisable(!lbShow);
////        txtField30V.setDisable(!lbShow);
////        txtField31V.setDisable(!lbShow);
////        txtField32V.setDisable(!lbShow);
////        textArea34V.setDisable(!lbShow);
////
////        btnVhclDesc.setVisible(true);  //Show Vehicle Description Entry Window
////        btnEngFrm.setVisible(lbShow);  //Show Engine Frame Entry Window
//////        btnVhclAvl.setVisible(!lbShow && (pnEditMode != EditMode.UNKNOWN)); //Find Available Unit
//////        btnVhclMnl.setVisible(!lbShow && (pnEditMode != EditMode.UNKNOWN)); //Manually selection of vehicle information
////        btnVhclAvl.setVisible((pnEditMode != EditMode.UNKNOWN && pnEditMode != EditMode.ADDNEW)); //Find Available Unit
////        btnVhclMnl.setVisible((pnEditMode != EditMode.UNKNOWN && pnEditMode != EditMode.ADDNEW)); //Manually selection of vehicle information
////
////        btnEdit.setVisible(false);
////        btnEdit.setManaged(false);
////        btnTransfer.setVisible(false);
////        btnTransfer.setManaged(false);
////        btnVSave.setVisible(lbShow);
////        btnVSave.setManaged(lbShow);
////
////        if (fnValue == EditMode.READY) {
////            btnEdit.setVisible(true);
////            btnEdit.setManaged(true);
////            btnTransfer.setVisible(true);
////            btnTransfer.setManaged(true);
////        }
////
////    }
////    private void disableFields(){
////        txtField03V.setDisable(true);
////        txtField04V.setDisable(true);
////        txtField08V.setDisable(true);
////        txtField09V.setDisable(true);
////        txtField11V.setDisable(true);
////        txtField20V.setDisable(true);
////        txtField21V.setDisable(true);
////        txtField22V.setDisable(true);
////        txtField24V.setDisable(true);
////        txtField26V.setDisable(true);
////        txtField28V.setDisable(true);
////        txtField30V.setDisable(true);
////        txtField31V.setDisable(true);
////        txtField32V.setDisable(true);
////        textArea34V.setDisable(true);
////
////    }
////    private void clearVehicleInfoFields() {
////        if (pnEditMode == EditMode.UNKNOWN) {
////            vhclinfodata.clear();
////            vhclhtrydata.clear();
////        }
////
////        txtField03V.clear();
////        txtField04V.clear();
////        txtField08V.clear();
////        txtField09V.clear();
////        txtField11V.clear();
////        txtField20V.clear();
////        txtField21V.setValue(LocalDate.of(1900, Month.JANUARY, 1)); //birthdate
////        txtField22V.clear();
////        txtField24V.clear();
////        txtField26V.clear();
////        txtField28V.clear();
////        txtField30V.clear();
////        txtField31V.clear();
////        txtField32V.clear();
////        textArea34V.clear();
////
////        /*Clear Red Color for required fileds*/
////        txtField24V.getStyleClass().remove("required-field");
////        txtField26V.getStyleClass().remove("required-field");
////        txtField28V.getStyleClass().remove("required-field");
////        txtField31V.getStyleClass().remove("required-field");
////        txtField30V.getStyleClass().remove("required-field");
////        txtField32V.getStyleClass().remove("required-field");
////        txtField20V.getStyleClass().remove("required-field");
////        txtField08V.getStyleClass().remove("required-field");
////        txtField03V.getStyleClass().remove("required-field");
////        txtField04V.getStyleClass().remove("required-field");
////    }
////    private void clearAddress(){
////        /*Clear Red Color for required fileds*/
////        txtField03Addr.getStyleClass().remove("required-field");
////        txtField05Addr.getStyleClass().remove("required-field");
////        txtField06Addr.getStyleClass().remove("required-field");
////        txtField03Addr.clear(); //House No
////        txtField04Addr.clear(); //Street / Address
////        txtField05Addr.clear(); // Town
////        txtField06Addr.clear(); // Brgy
////        txtField07Addr.clear(); //Zip code
////        radiobtn18AddY.setSelected(true); //Active status
////        radiobtn18AddN.setSelected(false); //Active status
////        checkBox14Addr.setSelected(false); //Primary
////        checkBox17Addr.setSelected(false); //Current
////        checkBox12Addr.setSelected(false); //Office
////        checkBox13Addr.setSelected(false); //Provincial
////        textArea11Addr.clear(); // Address Remarks
////    }
////
////    private void clearContact() {
////        comboBox05Cont.setValue(null); // Contact Ownership
////        comboBox04Cont.setValue(null); // Mobile Type
////        txtField03Cont.clear();  //Mobile Number
////        /*Clear Red Color for required fileds*/
////        txtField03Cont.getStyleClass().remove("required-field");
////        radiobtn14CntY.setSelected(true); // Contact Active Status
////        radiobtn14CntN.setSelected(false); // Contact Active Status
////        radiobtn11CntY.setSelected(false); // Contact Primary
////        radiobtn11CntN.setSelected(false); // Contact Primary
////        textArea13Cont.clear(); // Contact Remarks
////    }
////
////    private void clearEmail() {
////        comboBox04EmAd.setValue(null); // Email Ownership
////        radiobtn06EmaY.setSelected(true); // Email Active status
////        radiobtn06EmaN.setSelected(false); // Email Active status
////        radiobtn05EmaY.setSelected(false); // Email Primary
////        radiobtn05EmaN.setSelected(false); // Email Primary
////        txtField03EmAd.clear(); // Email Address
////    }
////
////    private void clearSocMed() {
////        radiobtn05SocY.setSelected(true); // SocMed Active status
////        radiobtn05SocN.setSelected(false); // SocMed Active status
////        txtField03Socm.clear(); // SocMed Account
////        comboBox04Socm.setValue(null); // SocMed Type
////    }
}
