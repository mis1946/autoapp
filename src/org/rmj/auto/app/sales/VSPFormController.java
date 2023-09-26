/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.sales;

import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.F3;
import static javafx.scene.input.KeyCode.TAB;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.auto.app.views.InputTextFormatter;
import org.rmj.auto.app.views.ScreenInterface;
import org.rmj.auto.app.views.unloadForm;
import org.rmj.auto.sales.base.VehicleSalesProposalMaster;

/**
 * FXML Controller class
 *
 * @author Arsiela Date Created 09-15-2023
 */
public class VSPFormController implements Initializable, ScreenInterface {

    private GRider oApp;
    private VehicleSalesProposalMaster oTrans;
    private MasterCallback oListener;

    unloadForm unload = new unloadForm(); //Used in Close Button
    private final String pxeModuleName = "Vehicle Sales Proposal"; //Form Title
    private int pnEditMode;//Modifying fields
    private int lnCtr = 0;

    private String TransNo = "";

    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancelVSP;
    @FXML
    private Button btnBrowse;
    @FXML
    private Button btnClose;

    @FXML
    private Button btnPrint;
    @FXML
    private TextField txtSeek03; //VSP Number Seach
    @FXML
    private Tab tabDetails;
    @FXML
    private Tab tabAddOns;

    /*MAIN INTERFACE */
    @FXML
    private TextField txtField03; //VSP NO.
    @FXML
    private TextField txtField02; //VSP Date
    @FXML
    private TextField txtField78; //Inquiry Date
    @FXML
    private DatePicker date04; //Target Delivery Date
    @FXML
    private TextField txtField84; //Branch
    @FXML
    private TextField txtField75; //Sales Executive
    @FXML
    private ComboBox<String> comboBox80; //Inquiry Type
    ObservableList<String> cInquiryType = FXCollections.observableArrayList("WALK-IN", "WEB INQUIRY", "PHONE-IN", "REFERRAL", "SALES CALL", "EVENT", "SERVICE", "OFFICE ACCOUNT", "CAREMITTANCE", "DATABASE", "UIO");
    @FXML
    private TextField txtField82; //Referral Type
    @FXML
    private TextField txtField76; //Agent
    @FXML
    private TextField txtField81; //Online Store
    @FXML
    private TextField txtField77; // Inquiring Customer
    @FXML
    private TextField txtField68; //Buying Customer
    @FXML
    private TextArea textArea69; // Address
    @FXML
    private ToggleGroup tgUnitCategory;
    @FXML
    private RadioButton brandNewCat; // Category BrandNew
    @FXML
    private RadioButton preOwnedCat;// Category Pre-Owned
    @FXML
    private TextField txtField71; //CS No.
    @FXML
    private TextField txtField72; //Plate No.
    @FXML
    private TextArea textArea70; //Model
    @FXML
    private TextField txtField48;//Ending Plate No.
    @FXML
    private TextField txtField83; //Key No
    @FXML
    private TextField txtField73; //Frame No.
    @FXML
    private TextField txtField74; //Engine No.
    @FXML
    private TextArea textArea09; //VSP Remarks
    @FXML
    private TextField txtField36; //Gross Amount
    @FXML
    private TextField txtField37; //Reservation
    @FXML
    private TextField txtField39;// Net Amount Due

    /*DETAILS */
    @FXML
    private ComboBox<String> comboBox34; // Payment Mode
    ObservableList<String> cModeOfPayment = FXCollections.observableArrayList("CASH", "BANK PURCHASE ORDER", "BANK FINANCING", "COMPANY PURCHASE ORDER", "COMPANY FINANCING"); //Mode of Payment Values
    @FXML
    private TextField txtField08; //Unit SRP
    @FXML
    private TextField txtField38; //Downpayment
    @FXML
    private TextField txtField04_Finance; // Bank
    @FXML
    private TextField txtField14_Finance; //Bank Discount
    @FXML
    private TextField txtField06_Finance; // Terms
    @FXML
    private TextField txtField07_Finance; //Rates
    @FXML
    private ComboBox<String> comboBox02_Finance; //Finance Promo
    ObservableList<String> cFinPromoType = FXCollections.observableArrayList("NONE", "ALL-IN IN HOUSE", "ALL-IN PROMO");
    @FXML
    private TextField txtField082_Finance; //Net SRP
    @FXML
    private TextField txtField13_Finance; //Net Downpayment
    @FXML
    private TextField txtField05_Finance; //Amount Financed
    @FXML
    private TextField txtField09_Finance; //Net Monthly Inst.
    @FXML
    private TextField txtField08_Finance;// Prompt Payment Disc.
    @FXML
    private TextField txtField12_Finance; //Gross Monthly Inst.
    @FXML
    private TextField txtField10_Finance; //Promissory Note Amt
    @FXML
    private TextField txtField29; //STD Fleet Discount
    @FXML
    private CheckBox chckBoxSTD1;
    @FXML
    private TextField txtField42; // STD Fleet Plant
    @FXML
    private CheckBox chckBoxSTD2;
    @FXML
    private TextField txtField43; // STD Fleet Dealer
    @FXML
    private TextField txtField30; //SPL Fleet Discount
    @FXML
    private CheckBox chckBoxSPL1;
    @FXML
    private TextField txtField44; // SPL Fleet Plant
    @FXML
    private CheckBox chckBoxSPL2;
    @FXML
    private TextField txtField45; // SPL Fleet Dealer
    @FXML
    private TextField txtField28; //Promo Discount
    @FXML
    private CheckBox chckBoxPromo1;
    @FXML
    private TextField txtField46; // Promo Plant
    @FXML
    private CheckBox chckBoxPromo2;
    @FXML
    private TextField txtField47; // Promo Dealer
    @FXML
    private TextField txtField31; //Bundle Discount
    @FXML
    private TextField txtField10; // OMA & CMF
    @FXML
    private TextField txtField16; //TPL Insurance Amount
    @FXML
    private ComboBox<String> comboBox21; //TPL Insurance Type
    ObservableList<String> cTplType = FXCollections.observableArrayList("NONE", "FOC", "C/o CLIENT", "C/o COMP", "C/o COMP");
    @FXML
    private TextField txtField26; //TPL Insurance Name
    @FXML
    private TextField txtField17; //Compre Insurance Amount
    @FXML
    private ComboBox<String> comboBox22; //Compre Insurance Type
    ObservableList<String> cCompType = FXCollections.observableArrayList("NONE", "FOC", "C/o CLIENT", "C/o COMP", "C/o COMP");
    @FXML
    private TextField txtField27; //Compre Insurance Name
    @FXML
    private ComboBox<String> comboBox24; //Compre Ins Type
    ObservableList<String> cCompYearType1 = FXCollections.observableArrayList("NONE", "FREE", "REGULAR RATE", "DISCOUNTED RATE", "FROM PROMO DISC");
    @FXML
    private ComboBox<String> comboBox25; //Compre Ins Year Type
    ObservableList<String> cCompYearType2 = FXCollections.observableArrayList("0", "1", "2", "3", "4");
    @FXML
    private TextField txtField18; //LTO Amount
    @FXML
    private ComboBox<String> comboBox23; //LTO Type
    ObservableList<String> cLTOType = FXCollections.observableArrayList("NONE", "FOC", "CHARGE");
    @FXML
    private TextField txtField19; //CHMO/Doc Stamps Amount
    @FXML
    private ComboBox<String> comboBox20; // CHMO/Doc Stamps Type
    ObservableList<String> cHMOType = FXCollections.observableArrayList("NONE", "FOC", "CHARGE", "C/o BANK");
    @FXML
    private TextField txtField13;//Labor Amount
    @FXML
    private TextField txtField14;//Accessories Amount
    @FXML
    private TextField txtField12;// Misc Charges Amount
    @FXML
    private TextField txtField11; //Misc Charges Name
    @FXML
    private TextField txtField362; // Gross Amount in Details
    @FXML
    private TextField txtField372; //Reservation in Details
    @FXML
    private TextField txtField392; //Net Amount Due in Details

    private Stage getStage() {
        return (Stage) btnClose.getScene().getWindow();
    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oListener = (int fnIndex, Object foValue) -> {
            System.out.println("Set Class Value " + fnIndex + "-->" + foValue);
        };

        oTrans = new VehicleSalesProposalMaster(oApp, oApp.getBranchCode(), true); //Initialize VehicleSalesProposalMaster
        oTrans.setCallback(oListener);
        oTrans.setWithUI(true);

        /*Set Capitalization Fields*/
        initCapitalizationFields();

        /*Set comboBox*/
        initSetComboBoxtoVSPMaster();

        /*Set Combo Items*/
        initComboItems();

        /*Monitoring Inquiry Type TextField*/
        initMonitoringProperty();

        /* Set check boxex */
        initcheckBoxes();

        /*Set Button Click Event*/
        initCmdButton();

        /*Set TextField Key-Pressed*/
        initTextKeyPressed();

        /*Set TextField Focus*/
        initTextFieldFocus();

        /* Set Number Format*/
        initNumberFormatterFields();

        date04.setOnAction(this::getDate);

        pnEditMode = EditMode.UNKNOWN;
        initButton(pnEditMode);
    }

    private void initCapitalizationFields() {
        /*TextField*/
        setTextFieldCapital(txtField03);
        setTextFieldCapital(txtField02);
        setTextFieldCapital(txtField78);
        setTextFieldCapital(txtField84);
        setTextFieldCapital(txtField75);
        setTextFieldCapital(txtField82);
        setTextFieldCapital(txtField81);

        setTextFieldCapital(txtField77);
        setTextFieldCapital(txtField68);
        setTextFieldCapital(txtField28);

        setTextFieldCapital(txtField71);
        setTextFieldCapital(txtField72);
        setTextFieldCapital(txtField73);
        setTextFieldCapital(txtField74);

        setTextFieldCapital(txtField04_Finance);

        setTextFieldCapital(txtField26);
        setTextFieldCapital(txtField27);
        setTextFieldCapital(txtField11);

        /*TextArea*/
        setTextAreaCapital(textArea70);
        setTextAreaCapital(textArea09);
        setTextAreaCapital(textArea69);

    }

    private static void setTextFieldCapital(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (textField.getText() != null) {
                textField.setText(newValue.toUpperCase());
            }
        });
    }

    private static void setTextAreaCapital(TextArea textArea) {
        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (textArea.getText() != null) {
                textArea.setText(newValue.toUpperCase());
            }
        });
    }

    private void initSetComboBoxtoVSPMaster() {

        /* SET TO MASTER */
        handleComboBoxSelectionVSPMaster(comboBox21, 21);
        handleComboBoxSelectionVSPMaster(comboBox22, 22);
        handleComboBoxSelectionVSPMaster(comboBox22, 24);
        handleComboBoxSelectionVSPMaster(comboBox25, 23);
        handleComboBoxSelectionVSPMaster(comboBox25, 20);
        /* SET TO VSP FINANCE */
        handleComboBoxSelectionVSPFinance(comboBox02_Finance, 2);
    }

    private void handleComboBoxSelectionVSPMaster(ComboBox<String> comboBox, int fieldNumber) {
        comboBox.setOnAction(e -> {
            try {
                int selectedType = comboBox.getSelectionModel().getSelectedIndex(); // Retrieve the selected type
                if (selectedType >= 0) {
                    oTrans.setMaster(fieldNumber, String.valueOf(selectedType));
                    // Pass the selected type to setMaster method
                }
            } catch (SQLException ex) {
                Logger.getLogger(VSPFormController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        );
    }

    private void handleComboBoxSelectionVSPFinance(ComboBox<String> comboBox, int fieldNumber) {
        comboBox.setOnAction(e -> {
            try {
                int selectedType = comboBox.getSelectionModel().getSelectedIndex(); // Retrieve the selected type
                if (selectedType >= 0) {
                    oTrans.setVSPFinance(fieldNumber, String.valueOf(selectedType));
                    // Pass the selected type to setMaster method
                    initButton(pnEditMode);
                }

            } catch (SQLException ex) {
                Logger.getLogger(VSPFormController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        );
    }

    private void initComboItems() {
        comboBox34.setItems(cModeOfPayment);
        comboBox21.setItems(cTplType);
        comboBox22.setItems(cCompType);
        comboBox24.setItems(cCompYearType1);
        comboBox25.setItems(cCompYearType2);
        comboBox23.setItems(cLTOType);
        comboBox20.setItems(cHMOType);
        comboBox02_Finance.setItems(cFinPromoType);
        comboBox80.setItems(cInquiryType);
    }

    /*Initialize Monitor Property */
    private void initMonitoringProperty() {

        //Inquiring Customer
        txtField77.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (newValue.isEmpty()) {
                clearFields();
                txtField68.setDisable(true);
//                textArea69.setDisable(true);
                txtField71.setDisable(true);
                txtField72.setDisable(true);
                textArea70.setDisable(true);
                txtField48.setDisable(true);
                txtField83.setDisable(true);
                txtField73.setDisable(true);
                txtField74.setDisable(true);
                tabDetails.setDisable(true);
                tabAddOns.setDisable(true);
            }
        });

        //Bank Name
        txtField04_Finance.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (newValue.isEmpty()) {
                comboBox02_Finance.setDisable(true);
                txtField13_Finance.setDisable(true);
                txtField05_Finance.setDisable(true);
                txtField09_Finance.setDisable(true);
                txtField08_Finance.setDisable(true);
                txtField12_Finance.setDisable(true);
                txtField10_Finance.setDisable(true);
                txtField06_Finance.setDisable(true);
                txtField07_Finance.setDisable(true);
                txtField082_Finance.setDisable(true);
                txtField14_Finance.setDisable(true);
            }
        });

        txtField28.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (newValue == null || newValue.trim().isEmpty() || newValue.equals("0.00")) {
                txtField46.setText("0.00");
                txtField47.setText("0.00");

                txtField46.setDisable(true);
                txtField47.setDisable(true);

                chckBoxPromo1.setSelected(false);
                chckBoxPromo1.setVisible(false);
                chckBoxPromo1.setDisable(true);

                chckBoxPromo2.setDisable(true);
                chckBoxPromo2.setVisible(false);
                chckBoxPromo2.setSelected(false);

            } else {
                chckBoxPromo1.setVisible(true);
                chckBoxPromo2.setVisible(true);
            }
        });
    }

    /* Initialize CmdButton */
    private void initCmdButton() {
        btnAdd.setOnAction(this::cmdButton_Click);
        btnEdit.setOnAction(this::cmdButton_Click);
        btnSave.setOnAction(this::cmdButton_Click);
        btnCancel.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click);
        btnBrowse.setOnAction(this::cmdButton_Click);
    }

    private void initcheckBoxes() {
        chckBoxPromo1.setOnAction(event -> {
            if (chckBoxPromo1.isSelected()) {
                txtField46.setDisable(false);
            } else {
                txtField46.setDisable(true);
            }
        });
        chckBoxPromo2.setOnAction(event -> {
            if (chckBoxPromo2.isSelected()) {
                txtField47.setDisable(false);
            } else {
                txtField47.setDisable(true);
            }
        });

    }


    /* Button Click Action Event */
    private void cmdButton_Click(ActionEvent event) {
        try {
            String lsButton = ((Button) event.getSource()).getId();
            switch (lsButton) {
                case "btnAdd":
                    addRecordVSP();
                    break;
                case "btnEdit":
                    editRecordVSP();
                    break;
                case "btnSave":
                    saveRecordVSP();
                    break;
                case "btnCancel":
                    cancelRecordVSP();
                    break;
                case "btnBrowse":
                    browseRecordVSP();
                    break;
                case "btnPrint":
                    break;
                case "btnClose":
                    closeFormVSP();
                    break;
            }
            initButton(pnEditMode);

        } catch (SQLException ex) {
            Logger.getLogger(UnitDeliveryReceiptFormController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void addRecordVSP() {
        if (oTrans.NewRecord()) {
            loadVSPField();
            clearFields();
            pnEditMode = oTrans.getEditMode();
        } else {
            ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
            return;
        }
    }

    private void editRecordVSP() {
        if (oTrans.UpdateRecord()) {
            pnEditMode = oTrans.getEditMode();
        } else {
            ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
        }
    }

    private void saveRecordVSP() throws SQLException {
        if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to save?") == true) {
            if (txtField77.getText().isEmpty()) {
                ShowMessageFX.Warning(getStage(), "Please choose a value for Inquiring Customer", "Warning", null);
                txtField77.requestFocus();
                return;
            }
            if (txtField68.getText().trim().equals("")) {
                ShowMessageFX.Warning(getStage(), "Please enter a value for Buying Customer", "Warning", null);
                txtField03.requestFocus();
                return;
            }
            if (txtField29.getText().trim().equals("")) {
                ShowMessageFX.Warning(getStage(), "Please enter a value for VSP No.", "Warning", null);
                txtField29.requestFocus();
                return;
            }

            //Proceed to saving record
            if (oTrans.SaveRecord()) {
                ShowMessageFX.Information(getStage(), "Transaction save successfully.", pxeModuleName, null);
                loadVSPField();
                pnEditMode = EditMode.READY;
            } else {
                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", "Error while saving " + pxeModuleName + ".");
            }

        }

    }

    private void cancelRecordVSP() {
        if (ShowMessageFX.OkayCancel(getStage(), "Are you sure you want to cancel?", pxeModuleName, null) == true) {
            clearFields();
            pnEditMode = EditMode.UNKNOWN;
        }
    }

    private void closeFormVSP() {
        if (ShowMessageFX.OkayCancel(null, "Close Tab", "Are you sure you want to close this Tab?")) {
            if (unload != null) {
                unload.unloadForm(AnchorMain, oApp, pxeModuleName);
            } else {
                ShowMessageFX.Warning(null, "Warning", "Please notify the system administrator to configure the null value at the close button.");
            }
        }
    }

    private void browseRecordVSP() {
        try {
            String fxTitle = "Confirmation";
            String fxHeader = "You have unsaved data. Are you sure you want to browse a new record?";
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (ShowMessageFX.OkayCancel(null, fxTitle, fxHeader)) {
                    clearFields();
                } else {
                    return;
                }
            }

            if (oTrans.searchRecord()) {
//                removeRequiredField();
                loadVSPField();

                pnEditMode = EditMode.READY;
            } else {
                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                clearFields();
                pnEditMode = EditMode.UNKNOWN;

            }
        } catch (SQLException ex) {
            Logger.getLogger(UnitDeliveryReceiptFormController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void initTextKeyPressed() {
        txtSeek03.setOnKeyPressed(this::txtField_KeyPressed);
        txtField03.setOnKeyPressed(this::txtField_KeyPressed);
        txtField77.setOnKeyPressed(this::txtField_KeyPressed);
        txtField68.setOnKeyPressed(this::txtField_KeyPressed);
        txtField71.setOnKeyPressed(this::txtField_KeyPressed);
        txtField72.setOnKeyPressed(this::txtField_KeyPressed);
        txtField04_Finance.setOnKeyPressed(this::txtField_KeyPressed);

    }

    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        String txtFieldID = ((TextField) event.getSource()).getId();
        try {
            switch (event.getCode()) {
                case F3:
                case TAB:
                case ENTER:
                    switch (txtFieldID) {
                        case "txtSeek03":  //Search by VSP NO
                            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                                if (ShowMessageFX.OkayCancel(null, "Confirmation", "You have unsaved data. Are you sure you want to browse a new record?") == true) {
                                    clearFields();
                                } else {
                                    return;
                                }
                            }
                            if (oTrans.searchRecord()) {
//                                removeRequired();
                                loadVSPField();
                                pnEditMode = oTrans.getEditMode();
                            } else {
                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                                clearFields();
                                pnEditMode = EditMode.UNKNOWN;
                            }
                            initButton(pnEditMode);
                            break;
                        case "txtField77":
                            if (oTrans.searchInquiry(txtField.getText())) {
                                clearClassMasterField();
                                txtField68.setDisable(false);
                                textArea69.setDisable(false);
                                txtField71.setDisable(false);
                                txtField72.setDisable(false);

                                txtField48.setDisable(false);
                                tabDetails.setDisable(false);
                                tabAddOns.setDisable(false);
                                loadVSPField();
                                initButton(pnEditMode);
                            } else {
                                clearFields();
                                clearClassMasterField();
                                txtField77.clear();
                                txtField77.requestFocus();
                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                            }
                            break;
                        case "txtField68":
                            if (oTrans.searchBuyingCustomer(txtField.getText())) {
                                txtField68.setText((String) oTrans.getMaster(68));
                            } else {
                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                            }
                            break;
                        case "txtField04_Finance":
                            if (oTrans.searchBankApplication(txtField.getText())) {
                                txtField04_Finance.setText((String) oTrans.getVSPFinance(04));
                                comboBox02_Finance.setDisable(false);
                                txtField13_Finance.setDisable(false);
                                txtField08_Finance.setDisable(false);
                                txtField06_Finance.setDisable(false);
                                txtField07_Finance.setDisable(false);
                                txtField14_Finance.setDisable(false);

                                txtField13_Finance.setText("0.00");
                                txtField05_Finance.setText("0.00");
                                txtField09_Finance.setText("0.00");
                                txtField08_Finance.setText("0.00");
                                txtField12_Finance.setText("0.00");
                                txtField10_Finance.setText("0.00");
                                txtField06_Finance.setText("0");
                                txtField07_Finance.setText("0.00");
                                txtField082_Finance.setText("0.00");
                                txtField14_Finance.setText("0.00");
                                loadVSPField();
                                initButton(pnEditMode);
                            } else {
                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                            }
                            break;
                        case "txtField71":
                            if (oTrans.searchAvailableVhcl(txtField.getText())) {
                                loadVSPField();
                            } else {
                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                            }
                            break;
                        case "txtField72":
                            if (oTrans.searchAvailableVhcl(txtField.getText())) {
                                loadVSPField();
                            } else {
                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                            }
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

    private void initTextFieldFocus() {
        txtField77.focusedProperty().addListener(txtField_Focus);
        txtField68.focusedProperty().addListener(txtField_Focus);
        txtField71.focusedProperty().addListener(txtField_Focus);
        txtField72.focusedProperty().addListener(txtField_Focus);
        txtField38.focusedProperty().addListener(txtField_Focus);
        txtField08.focusedProperty().addListener(txtField_Focus);
        txtField29.focusedProperty().addListener(txtField_Focus);
        txtField43.focusedProperty().addListener(txtField_Focus);
        txtField42.focusedProperty().addListener(txtField_Focus);
        txtField30.focusedProperty().addListener(txtField_Focus);
        txtField44.focusedProperty().addListener(txtField_Focus);
        txtField45.focusedProperty().addListener(txtField_Focus);
        txtField28.focusedProperty().addListener(txtField_Focus);
        txtField46.focusedProperty().addListener(txtField_Focus);
        txtField47.focusedProperty().addListener(txtField_Focus);
        txtField48.focusedProperty().addListener(txtField_Focus);
        txtField10.focusedProperty().addListener(txtField_Focus);
        txtField31.focusedProperty().addListener(txtField_Focus);

        textArea09.focusedProperty().addListener(txtArea_Focus);

        txtField14_Finance.focusedProperty().addListener(txtFieldFinance_Focus);
        txtField06_Finance.focusedProperty().addListener(txtFieldFinance_Focus);
        txtField07_Finance.focusedProperty().addListener(txtFieldFinance_Focus);
        txtField08_Finance.focusedProperty().addListener(txtFieldFinance_Focus);
        txtField13_Finance.focusedProperty().addListener(txtFieldFinance_Focus);

    }
    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {
        try {
            TextField txtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
            int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
            String lsValue = txtField.getText();

            if (lsValue == null) {
                return;
            }

            if (!nv) {
                /* Lost Focus */
                try {
                    switch (lnIndex) {
                        case 77:
                        case 68:
                        case 71:
                        case 72:
                        case 48:
                            oTrans.setMaster(lnIndex, lsValue); // Handle Encoded Value
                            break;
                        case 10:
                            if (lsValue.isEmpty()) {
                                lsValue = "0.00";
                            }
                            oTrans.setMaster(lnIndex, Double.valueOf(lsValue.replace(",", "")));
                            loadVSPField();
                            break;
                        case 29: //STD
                        case 31:
                        case 43:
                        case 42:
                        case 30: // SPL
                        case 44:
                        case 45:
                        case 28: //Promo Discount
                        case 46:
                        case 47:
                            if (lsValue.isEmpty()) {
                                lsValue = "0.00";
                            }
                            oTrans.setMaster(lnIndex, Double.valueOf(lsValue.replace(",", "")));
                            loadVSPField();
                            break;
                        case 38://downpayment
                            if (lsValue.isEmpty()) {
                                lsValue = "0.00";
                            }
                            if (Double.parseDouble(oTrans.getMaster(8).toString()) > 0.00) {
                                if (Double.parseDouble(lsValue.replace(",", "")) > Double.parseDouble(oTrans.getMaster(8).toString())) {
                                    txtField38.setText("0.00");
                                    //oTrans.setMaster(lnIndex, Double.valueOf("0.00"));
                                    lsValue = "0.00";
                                    ShowMessageFX.Warning(getStage(), "Downpayment cannot be greater than Unit Price", "Warning", null);
                                    txtField38.requestFocus();
                                }

                            }
                            oTrans.setMaster(lnIndex, Double.valueOf(lsValue.replace(",", "")));
                            loadVSPField();
                            break;
                        case 8://unit price
                            if (lsValue.isEmpty()) {
                                lsValue = "0.00";
                            }
                            if (Double.parseDouble(lsValue.replace(",", "")) < Double.parseDouble(oTrans.getMaster(38).toString())) {
                                // Set txtField38 to 0.00 amount
                                txtField08.setText("0.00");
                                //oTrans.setMaster(lnIndex, Double.valueOf("0.00"));
                                lsValue = "0.00";
                                ShowMessageFX.Warning(getStage(), "Unit Price cannot be less than Downpayment", "Warning", null);
                                txtField08.requestFocus();
                            }
                            oTrans.setMaster(lnIndex, Double.valueOf(lsValue.replace(",", "")));
                            loadVSPField();
                            break;

                    }
                } catch (NumberFormatException e) {
                }

            } else {
                txtField.selectAll();

            }
        } catch (SQLException ex) {
            Logger.getLogger(VSPFormController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    };
    final ChangeListener<? super Boolean> txtFieldFinance_Focus = (o, ov, nv) -> {
        try {
            TextField txtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
            int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
            String lsValue = txtField.getText();

            if (lsValue == null) {
                return;
            }
            if (!nv) {
                /* Lost Focus */

                try {
                    if (oTrans.getVSPFinanceCount() > 0) {
                        switch (lnIndex) {
                            case 6:
                                oTrans.setVSPFinance(lnIndex, Integer.valueOf(lsValue.replace(",", "")));
                                loadVSPField();
                                break;
                            case 7:
                            case 14:
                            case 8:
                            case 13:

                                if (lsValue.isEmpty()) {
                                    lsValue = "0.00";
                                }
                                oTrans.setVSPFinance(lnIndex, Double.valueOf(lsValue.replace(",", "")));
                                loadVSPField();
                                break;

                        }
                    }
                } catch (NumberFormatException e) {
                }

            } else {
                txtField.selectAll();

            }
        } catch (SQLException ex) {
            Logger.getLogger(VSPFormController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    };
    final ChangeListener<? super Boolean> txtArea_Focus = (o, ov, nv) -> {

        TextArea textArea = (TextArea) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(textArea.getId().substring(8, 10));
        String lsValue = textArea.getText();

        if (lsValue == null) {
            return;
        }
        try {
            if (!nv) {
                /*Lost Focus*/
                switch (lnIndex) {
                    case 9:        //sRemarkx
                        oTrans.setMaster(lnIndex, lsValue);
                        break;
                }
            } else {
                textArea.selectAll();
            }
        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }
    };

    private void initNumberFormatterFields() {
        Pattern pattern = Pattern.compile("[\\d\\p{Punct}]*");
        txtField08.setTextFormatter(new InputTextFormatter(pattern));
        txtField38.setTextFormatter(new InputTextFormatter(pattern));
        txtField14_Finance.setTextFormatter(new InputTextFormatter(pattern));
        txtField06_Finance.setTextFormatter(new InputTextFormatter(pattern));
        txtField07_Finance.setTextFormatter(new InputTextFormatter(pattern));
        txtField082_Finance.setTextFormatter(new InputTextFormatter(pattern));
        txtField13_Finance.setTextFormatter(new InputTextFormatter(pattern));
        txtField05_Finance.setTextFormatter(new InputTextFormatter(pattern));
        txtField09_Finance.setTextFormatter(new InputTextFormatter(pattern));
        txtField08_Finance.setTextFormatter(new InputTextFormatter(pattern));
        txtField12_Finance.setTextFormatter(new InputTextFormatter(pattern));
        txtField10_Finance.setTextFormatter(new InputTextFormatter(pattern));
        txtField10.setTextFormatter(new InputTextFormatter(pattern));
        txtField16.setTextFormatter(new InputTextFormatter(pattern));
        txtField17.setTextFormatter(new InputTextFormatter(pattern));
        txtField18.setTextFormatter(new InputTextFormatter(pattern));
        txtField19.setTextFormatter(new InputTextFormatter(pattern));
        txtField13.setTextFormatter(new InputTextFormatter(pattern));
        txtField14.setTextFormatter(new InputTextFormatter(pattern));
        txtField12.setTextFormatter(new InputTextFormatter(pattern));
        txtField36.setTextFormatter(new InputTextFormatter(pattern));
        txtField37.setTextFormatter(new InputTextFormatter(pattern));
        txtField39.setTextFormatter(new InputTextFormatter(pattern));
        txtField362.setTextFormatter(new InputTextFormatter(pattern));
        txtField372.setTextFormatter(new InputTextFormatter(pattern));
        txtField392.setTextFormatter(new InputTextFormatter(pattern));
        txtField29.setTextFormatter(new InputTextFormatter(pattern));
        txtField30.setTextFormatter(new InputTextFormatter(pattern));
        txtField28.setTextFormatter(new InputTextFormatter(pattern));
        txtField31.setTextFormatter(new InputTextFormatter(pattern));
        txtField42.setTextFormatter(new InputTextFormatter(pattern));
        txtField43.setTextFormatter(new InputTextFormatter(pattern));
        txtField44.setTextFormatter(new InputTextFormatter(pattern));
        txtField45.setTextFormatter(new InputTextFormatter(pattern));
        txtField46.setTextFormatter(new InputTextFormatter(pattern));
        txtField47.setTextFormatter(new InputTextFormatter(pattern));
    }

    private void loadVSPField() {
        try {
            oTrans.computeAmount();
            DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
            /* MAIN INTERFACE */
            txtField03.setText((String) oTrans.getMaster(3));
            txtField02.setText(CommonUtils.xsDateMedium((Date) oTrans.getMaster(2)));
            txtField77.setText((String) oTrans.getMaster(77));
            String originalText = (String) oTrans.getMaster(78);
            int indexOfSpace = originalText.lastIndexOf(' '); // Find the last space character
            if (indexOfSpace >= 0) {
                String textWithoutTimestamp = originalText.substring(0, indexOfSpace); // Extract the text before the last space

                // Define a SimpleDateFormat with the expected date format
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Change the format to match your data

                try {
                    Date date = dateFormat.parse(textWithoutTimestamp);
                    txtField78.setText(CommonUtils.xsDateMedium(date)); // Convert the Date to xsDateMedium format and set it in txtField78
                } catch (ParseException e) {
                    e.printStackTrace(); // Handle the ParseException appropriately
                }
            } else {
                txtField78.setText(originalText); // If there's no space, use the original text as is
            }

            date04.setValue(strToDate(CommonUtils.xsDateShort((Date) oTrans.getMaster(4))));
            txtField84.setText((String) oTrans.getMaster(84));
            txtField75.setText((String) oTrans.getMaster(75));
            String selectedItem80 = oTrans.getMaster(80).toString();
            switch (selectedItem80) {
                case "0":
                    selectedItem80 = "WALK-IN";

                    break;
                case "1":
                    selectedItem80 = "WEB INQUIRY";
                    break;
                case "2":
                    selectedItem80 = "PHONE-IN";
                    break;
                case "3":
                    selectedItem80 = "REFERRAL";
                    break;
                case "4":
                    selectedItem80 = "SALES CALL";
                    break;
                case "5":
                    selectedItem80 = "EVENT";
                    break;
                case "6":
                    selectedItem80 = "SERVICE";
                    break;
                case "7":
                    selectedItem80 = "OFFICE ACCOUNT";
                    break;
                case "8":
                    selectedItem80 = "CAREMITTANCE";
                    break;
                case "9":
                    selectedItem80 = "DATABASE";
                    break;
                case "10":
                    selectedItem80 = "UIO";
                    break;
                default:
                    break;
            }
            comboBox80.setValue(selectedItem80);
            txtField82.setText((String) oTrans.getMaster(82));
            txtField76.setText((String) oTrans.getMaster(76));
            txtField81.setText((String) oTrans.getMaster(81));
            txtField68.setText((String) oTrans.getMaster(68));
            textArea69.setText((String) oTrans.getMaster(69));

            String isVchlBrandNew = ((String) oTrans.getMaster(54));
            if (isVchlBrandNew.equals("0")) {
                brandNewCat.setSelected(true);
            } else if (isVchlBrandNew.equals("1")) {
                preOwnedCat.setSelected(true);
            } else {
                brandNewCat.setSelected(false);
                preOwnedCat.setSelected(false);

            }

            txtField71.setText((String) oTrans.getMaster(71));
            txtField72.setText((String) oTrans.getMaster(72));
            textArea70.setText((String) oTrans.getMaster(70));
            txtField48.setText((String) oTrans.getMaster(48));
            txtField83.setText((String) oTrans.getMaster(83));
            txtField73.setText((String) oTrans.getMaster(73));
            txtField74.setText((String) oTrans.getMaster(74));

            textArea09.setText((String) oTrans.getMaster(9));

            // Format the decimal value with decimal separators
            txtField36.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getMaster(36)))));
            txtField37.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getMaster(37)))));
            txtField39.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getMaster(39)))));


            /* DETAILS INTERFACE */
            String selectedItem34 = oTrans.getMaster(34).toString();
            switch (selectedItem34) {
                case "0":
                    selectedItem34 = "CASH";
                    break;
                case "1":
                    selectedItem34 = "BANK PURCHASE ORDER";
                    break;
                case "2":
                    selectedItem34 = "BANK FINANCING";
                    break;
                case "3":
                    selectedItem34 = "COMPANY PURCHASE ORDER";
                    break;
                case "4":
                    selectedItem34 = "COMPANY FINANCING";
                    break;
                default:
                    break;

            }
            comboBox34.setValue(selectedItem34);
            txtField08.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getMaster(8)))));
            txtField38.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getMaster(38)))));
            txtField29.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getMaster(29)))));
            txtField30.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getMaster(30)))));
            txtField28.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getMaster(28)))));
            txtField31.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getMaster(31)))));

            txtField42.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getMaster(42)))));
            txtField43.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getMaster(43)))));
            txtField44.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getMaster(44)))));
            txtField45.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getMaster(45)))));
            txtField46.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getMaster(46)))));
            txtField47.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getMaster(47)))));

            txtField10.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getMaster(10)))));
            txtField16.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getMaster(16)))));
            String selectedItem21 = oTrans.getMaster(21).toString();
            switch (selectedItem21) {
                case "0":
                    selectedItem21 = "NONE";
                    break;
                case "1":
                    selectedItem21 = "FOC";
                    break;
                case "2":
                    selectedItem21 = "C/o CLIENT";
                    break;
                case "3":
                    selectedItem21 = "C/o COMP";
                    break;
                case "4":
                    selectedItem21 = "C/o BANK";
                    break;
                default:
                    break;
            }
            txtField26.setText((String) oTrans.getMaster(26));

            txtField17.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getMaster(19)))));
            String selectedItem22 = oTrans.getMaster(22).toString();
            switch (selectedItem22) {
                case "0":
                    selectedItem22 = "NONE";
                    break;
                case "1":
                    selectedItem22 = "FOC";
                    break;
                case "2":
                    selectedItem22 = "C/o CLIENT";
                    break;
                case "3":
                    selectedItem22 = "C/o COMP";
                    break;
                case "4":
                    selectedItem22 = "C/o BANK";
                    break;
                default:
                    break;
            }
            txtField27.setText((String) oTrans.getMaster(27));
            String selectedItem24 = oTrans.getMaster(24).toString();
            switch (selectedItem24) {
                case "0":
                    selectedItem24 = "NONE";
                    break;
                case "1":
                    selectedItem24 = "FOC";
                    break;
                case "2":
                    selectedItem24 = "C/o CLIENT";
                    break;
                case "3":
                    selectedItem24 = "C/o COMP";
                    break;
                case "4":
                    selectedItem24 = "C/o BANK";
                    break;
                default:
                    break;
            }
            String selectedItem25 = oTrans.getMaster(25).toString();
            switch (selectedItem25) {
                case "0":
                    selectedItem25 = "0";
                    break;
                case "1":
                    selectedItem25 = "1";
                    break;
                case "2":
                    selectedItem25 = "2";
                    break;
                case "3":
                    selectedItem25 = "3";
                    break;
                case "4":
                    selectedItem25 = "4";
                    break;
                default:
                    break;
            }
            txtField18.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getMaster(18)))));
            String selectedItem23 = oTrans.getMaster(23).toString();
            switch (selectedItem23) {
                case "0":
                    selectedItem23 = "NONE";
                    break;
                case "1":
                    selectedItem23 = "FOC";
                    break;
                case "2":
                    selectedItem23 = "CHARGE";
                    break;
                default:
                    break;
            }

            txtField19.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getMaster(19)))));
            String selectedItem20 = oTrans.getMaster(20).toString();
            switch (selectedItem20) {
                case "0":
                    selectedItem20 = "NONE";
                    break;
                case "1":
                    selectedItem20 = "FOC";
                    break;
                case "2":
                    selectedItem20 = "CHARGE";
                    break;
                case "3":
                    selectedItem20 = "C/o BANK";
                    break;
                default:
                    break;
            }

            switch (comboBox34.getSelectionModel().getSelectedIndex()) {
                case 1://FINANCING
                case 2:
                case 3:
                case 4:

                    if (oTrans.getVSPFinanceCount() > 0) {
                        txtField14_Finance.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getVSPFinance(14)))));
                        txtField04_Finance.setText((String) oTrans.getVSPFinance(4)); // Assuming it's an integer
                        txtField13_Finance.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getVSPFinance(13)))));
                        txtField05_Finance.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getVSPFinance(5)))));
                        txtField09_Finance.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getVSPFinance(9)))));
                        txtField08_Finance.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getVSPFinance(8)))));
                        txtField12_Finance.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getVSPFinance(12)))));
                        txtField10_Finance.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getVSPFinance(10)))));
                        txtField06_Finance.setText(String.valueOf(oTrans.getVSPFinance(6)));
                        txtField07_Finance.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getVSPFinance(7)))));
                        txtField082_Finance.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getMaster(8)))));
                        String selectedItem02 = oTrans.getVSPFinance(2).toString();
                        switch (selectedItem02) {
                            case "0":
                                selectedItem02 = "NONE";
                                break;
                            case "1":
                                selectedItem02 = "ALL-IN IN HOUSE";
                                break;
                            case "2":
                                selectedItem02 = "ALL-IN PROMO";
                                break;
                            default:
                                break;
                        }
                        comboBox02_Finance.setValue(selectedItem02);
                    }
                    break;
                default:
                    break;
            }

            txtField13.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getMaster(13)))));
            txtField14.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getMaster(14)))));
            txtField12.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getMaster(12)))));

            txtField11.setText((String) oTrans.getMaster(11));

            txtField362.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getMaster(36)))));
            txtField372.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getMaster(37)))));
            txtField392.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getMaster(39)))));

        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);

        }
    }

    /*Set Date Value to Master Class*/
    public void getDate(ActionEvent event) {
        try {
            oTrans.setMaster(4, SQLUtil.toDate(date04.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));

        } catch (SQLException ex) {
            Logger.getLogger(VSPFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*Convert Date to String*/
    private LocalDate strToDate(String val) {
        DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(val, date_formatter);
        return localDate;
    }

    private void initButton(int fnValue) {
        /* NOTE:
        lbShow (FALSE)= invisible
        !lbShow (TRUE)= visible
         */
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        /* MAIN INTERFACE */
        date04.setDisable(!lbShow);
        comboBox34.setDisable(true);
        txtField77.setDisable(!lbShow);
        txtField68.setDisable(!(lbShow && !txtField77.getText().isEmpty()));
        textArea69.setDisable(true);
        txtField71.setDisable(!(lbShow && !txtField77.getText().isEmpty()));
        txtField72.setDisable(!(lbShow && !txtField77.getText().isEmpty()));
        textArea70.setDisable(true);
        txtField48.setDisable(!(lbShow && !txtField77.getText().isEmpty()));
        txtField73.setDisable(true);
        txtField74.setDisable(true);
        txtField83.setDisable(true);
        tabDetails.setDisable(!(lbShow && !txtField77.getText().isEmpty()));
        tabAddOns.setDisable(!(lbShow && !txtField77.getText().isEmpty()));

        textArea09.setDisable(!lbShow);
        brandNewCat.setDisable(true);
        preOwnedCat.setDisable(true);
        /* DETAILS INTERFACE */
        txtField08.setDisable(!lbShow);
        txtField38.setDisable(!lbShow);
        txtField29.setDisable(true);
        txtField30.setDisable(true);

        comboBox21.setDisable(!lbShow);
        comboBox22.setDisable(!lbShow);
        comboBox24.setDisable(true);
        comboBox25.setDisable(true);
        txtField18.setDisable(!lbShow);
        comboBox23.setDisable(!lbShow);
        txtField19.setDisable(!lbShow);
        comboBox20.setDisable(!lbShow);
        txtField13.setDisable(!lbShow);
        txtField14.setDisable(!lbShow);
        txtField12.setDisable(!lbShow);
        txtField11.setDisable(!lbShow);
        //if lbShow = false hide btn
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        btnEdit.setVisible(false);
        btnEdit.setManaged(false);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);
        btnCancel.setVisible(lbShow);
        btnCancel.setManaged(lbShow);

        chckBoxPromo1.setDisable(!(lbShow && !txtField28.getText().isEmpty()));
        chckBoxPromo2.setDisable(!(lbShow && !txtField28.getText().isEmpty()));
        txtField28.setDisable(!lbShow); // promo
//        chckBoxPromo1.setDisable(!lbShow);
//        txtField46.setDisable(!lbShow);
//        chckBoxPromo2.setDisable(!lbShow);
//        txtField47.setDisable(!lbShow);
        chckBoxSTD1.setDisable(true);
        txtField42.setDisable(true);
        chckBoxSTD2.setDisable(true);
        txtField43.setDisable(true);

        chckBoxSPL1.setDisable(true);
        txtField44.setDisable(true);
        chckBoxSPL2.setDisable(true);
        txtField45.setDisable(true);

        comboBox02_Finance.setDisable(!(lbShow && !txtField04_Finance.getText().isEmpty()));
        txtField13_Finance.setDisable(!(lbShow && !txtField04_Finance.getText().isEmpty()));
        txtField08_Finance.setDisable(!(lbShow && !txtField04_Finance.getText().isEmpty()));
        txtField06_Finance.setDisable(!(lbShow && !txtField04_Finance.getText().isEmpty()));
        txtField07_Finance.setDisable(!(lbShow && !txtField04_Finance.getText().isEmpty()));
        txtField14_Finance.setDisable(!(lbShow && !txtField04_Finance.getText().isEmpty()));

        txtField36.setDisable(true);
        txtField37.setDisable(true);
        txtField39.setDisable(true);
        txtField16.setDisable(true);
        txtField26.setDisable(true);
        txtField17.setDisable(true);
        txtField27.setDisable(true);
        txtField362.setDisable(true);
        txtField372.setDisable(true);
        txtField392.setDisable(true);

        switch (comboBox34.getSelectionModel().getSelectedIndex()) {
            case 0: //CASH
                txtField04_Finance.setDisable(lbShow);
                comboBox02_Finance.setDisable(lbShow);
                txtField13_Finance.setDisable(lbShow);
                txtField05_Finance.setDisable(lbShow);
                txtField09_Finance.setDisable(lbShow);
                txtField08_Finance.setDisable(lbShow);
                txtField12_Finance.setDisable(lbShow);
                txtField10_Finance.setDisable(lbShow);
                txtField06_Finance.setDisable(lbShow);
                txtField07_Finance.setDisable(lbShow);
                txtField082_Finance.setDisable(lbShow);
                txtField14_Finance.setDisable(lbShow);

                txtField10.setDisable(lbShow);
                break;
            case 1: //BANK PURCHASE ORDER
                txtField06_Finance.setDisable(lbShow);
                txtField07_Finance.setDisable(lbShow);
            case 2: //BANK FINANCING
            case 3: //COMPANY PURCHASE ORDER
            case 4: //COMPANY FINANCING
                txtField10.setDisable(!lbShow);
                txtField04_Finance.setDisable(!lbShow);
                break;
        }

        switch (comboBox02_Finance.getSelectionModel().getSelectedIndex()) {
            case 0: //NONE
            case 1: //ALL-IN HOUSE
                txtField31.setDisable(lbShow); // bundle
                break;
            case 2: //ALL-IN PROMO
                txtField31.setDisable(!lbShow); // bundle
                break;
        }
        if (fnValue == EditMode.READY) {
            btnEdit.setVisible(true);
            btnEdit.setManaged(true);
            btnPrint.setVisible(true);
            btnPrint.setManaged(true);
            tabAddOns.setDisable(false);
            tabDetails.setDisable(false);
        }
        if (fnValue == EditMode.UPDATE) {

            try {
                if (!oTrans.getMaster(79).toString().isEmpty()) {
                    txtField71.setDisable(true);
                    txtField72.setDisable(true);
                }

                txtField77.setDisable(true);
            } catch (SQLException ex) {
                Logger.getLogger(VSPFormController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /*Clear Fields*/
    public void clearFields() {
        txtField03.setText("");
        txtField02.setText("");
        txtField77.setText("");
        txtField78.setText("");
        date04.setValue(strToDate(CommonUtils.xsDateShort((Date) oApp.getServerDate())));
        txtField84.setText("");
        txtField75.setText("");
        comboBox80.setValue(null);
        txtField82.setText("");
        txtField76.setText("");
        txtField81.setText("");
        txtField68.setText("");
        textArea69.setText("");

        tgUnitCategory.selectToggle(null);

        txtField71.setText("");
        txtField72.setText("");
        textArea70.setText("");
        txtField48.setText("");
        txtField83.setText("");
        txtField73.setText("");
        txtField74.setText("");

        textArea09.setText("");

        txtField36.setText("0.00");
        txtField37.setText("0.00");
        txtField39.setText("0.00");

        /* DETAILS INTERFACE */
        comboBox34.setValue(null);
        txtField08.setText("0.00");
        txtField38.setText("0.00");

        txtField29.setText("0.00");
        txtField30.setText("0.00");
        txtField28.setText("0.00");
        txtField31.setText("0.00");

        txtField42.setText("0.00");
        txtField43.setText("0.00");
        txtField44.setText("0.00");
        txtField45.setText("0.00");

        txtField46.setText("0.00");
        txtField47.setText("0.00");

        txtField10.setText("0.00");
        txtField16.setText("0.00");
        comboBox21.setValue(null);
        txtField26.setText("");

        txtField17.setText("0.00");
        comboBox22.setValue(null);
        txtField27.setText("");

        comboBox25.setValue(null);
        comboBox25.setValue(null);
        txtField18.setText("0.00");
        comboBox23.setValue(null);
        txtField19.setText("0.00");
        comboBox20.setValue(null);

        comboBox02_Finance.setValue(null);
        txtField14_Finance.setText("0.00");
        txtField04_Finance.setText("");
        txtField13_Finance.setText("0.00");
        txtField05_Finance.setText("0.00");
        txtField09_Finance.setText("0.00");
        txtField08_Finance.setText("0.00");
        txtField12_Finance.setText("0.00");
        txtField10_Finance.setText("0.00");
        txtField06_Finance.setText("0");
        txtField07_Finance.setText("0.00");
        txtField082_Finance.setText("0.00");

        txtField13.setText("0.00");
        txtField14.setText("0.00");
        txtField12.setText("0.00");
        txtField11.setText("");

        txtField362.setText("0.00");
        txtField372.setText("0.00");
        txtField392.setText("0.00");

        chckBoxSTD1.setSelected(false);
        txtField44.setText("0.00");
        chckBoxSTD2.setSelected(false);
        txtField44.setText("0.00");
        chckBoxSPL1.setSelected(false);
        txtField44.setText("0.00");
        chckBoxSPL2.setSelected(false);
        txtField45.setText("0.00");
        chckBoxPromo1.setSelected(false);
        txtField46.setText("0.00");
        chckBoxPromo2.setSelected(false);
        txtField47.setText("0.00");

    }

    private void clearClassMasterField() {
        for (lnCtr = 1; lnCtr <= 86; lnCtr++) {
            try {
                switch (lnCtr) {
                    case 38://downpayment
                        switch (oTrans.getMaster(34).toString()) {
                            case "1": //BANK PURCHASE ORDER
                                oTrans.setMaster(lnCtr, Double.valueOf("0.00"));
                                break;
                        }
                        break;
                }
            } catch (SQLException ex) {
                Logger.getLogger(VSPFormController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
