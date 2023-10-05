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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.auto.app.views.ActivityMemberTable;
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
    private TextField txtField42; // STD Fleet Plant
    @FXML
    private TextField txtField43; // STD Fleet Dealer
    @FXML
    private TextField txtField30; //SPL Fleet Discount
    @FXML
    private TextField txtField44; // SPL Fleet Plant
    @FXML
    private TextField txtField45; // SPL Fleet Dealer
    @FXML
    private TextField txtField32;
    @FXML
    private TextField txtField28; //Promo Discount
    @FXML
    private TextField txtField46; // Promo Plant
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
    ObservableList<String> cTplType = FXCollections.observableArrayList("NONE", "FOC", "C/o CLIENT", "C/o COMP", "C/o BANK");
    @FXML
    private TextField txtField26; //TPL Insurance Name
    @FXML
    private TextField txtField17; //Compre Insurance Amount
    @FXML
    private ComboBox<String> comboBox22; //Compre Insurance Type
    ObservableList<String> cCompType = FXCollections.observableArrayList("NONE", "FOC", "C/o CLIENT", "C/o COMP", "C/o BANK ");
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
    @FXML
    private TableColumn<String, VSPTableLaborList> tblLaborRow;
    @FXML
    private TableColumn<String, VSPTableLaborList> tblindex08_Labor;
    @FXML
    private TableColumn<ComboBox, VSPTableLaborList> tblindex05_Labor;
    @FXML
    private TableColumn<ComboBox, VSPTableLaborList> tblindex06_Labor;
    @FXML
    private TableColumn<String, VSPTableLaborList> tblindex04_Labor;
    @FXML
    private TableColumn<String, VSPTablePartList> tblPartsRow;
    @FXML
    private TableColumn<String, VSPTablePartList> tblindex15_Part;
    @FXML
    private TableColumn<String, VSPTablePartList> tblindex10_Part;
    @FXML
    private TableColumn<String, VSPTablePartList> tblindex08_Part;
    @FXML
    private TableColumn<String, VSPTablePartList> tblindex09_Part;
    @FXML
    private TableColumn<String, VSPTablePartList> tblindex05_Part;
    @FXML
    private Tab tabMain;
    @FXML
    private TabPane ImTabPane;
    @FXML
    private Button btnAdditionalLabor;
    @FXML
    private CheckBox chckBoxRustProof;
    @FXML
    private CheckBox chckBoxPermaShine;
    @FXML
    private CheckBox chckBoxUndercoat;
    @FXML
    private CheckBox chckBoxTint;
    private TextField txtField04_LaborRust;
    private TextField txtField04_LaborPerma;
    private TextField txtField04_LaborUnder;
    private TextField txtField04_LaborTint;
    @FXML
    private TableView<VSPTableLaborList> tblViewLabor;
    private ObservableList<VSPTableLaborList> laborData = FXCollections.observableArrayList();
    @FXML
    private CheckBox chckBoxSpecialAccount;

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
        addRowVSPLabor();
        date04.setOnAction(this::getDate);
        date04.setDayCellFactory(DateFormatCell);
//        setupDoubleTextFormatter(txtField46);
//        setupDoubleTextFormatter(txtField47);
        pnEditMode = EditMode.UNKNOWN;
        initButton(pnEditMode);

        tblViewLabor.setOnKeyPressed(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (event.getCode().equals(KeyCode.DELETE)) {
                    String fxTitle = "Remove Labor Confirmatrion";
                    String fxHeader = "Are you sure you want to remove labor?";
                    if (ShowMessageFX.OkayCancel(null, fxTitle, fxHeader)) {
                        VSPTableLaborList selectedVSP = getSelectedItem();
                        if (selectedVSP != null) {
                            try {
                                String fsRow = selectedVSP.getTblLaborRow();
                                int fnRow = Integer.parseInt(fsRow);
                                oTrans.removeVSPLabor(fnRow);
                                loadTableLabor();
                            } catch (SQLException ex) {
                                Logger.getLogger(VSPFormController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } else {
                        return;
                    }

                }
//            if (event.getCode().equals(KeyCode.F3)) {
//                // Check if the selected item's text field is TxtField08_Labor
//                VSPTableLaborList lbDescription = getSelectedItem();
//                if (selectedVSP != null && selectedVSP.getTxtField08_Labor().isFocused()) {
//                    // Handle F3 key press event when TxtField08_Labor is selected
//                    String laborDescription = selectedVSP.getTxtField08_Labor().getText();
//
//                }
//            }
            }
        }
        );
        tblViewLabor.setOnMouseClicked(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                try {
                    List<VSPTableLaborList> itemsToUpdate = new ArrayList<>();
                    for (VSPTableLaborList laborList : tblViewLabor.getItems()) {
                        int fnRow = Integer.parseInt(laborList.getTblLaborRow());
                        String textFieldValue = laborList.getTxtField04_Labor().getText().replace(",", "");
                        String fsLaborDesc = laborList.getTxtField08_Labor().getText();
                        double valueAmount = Double.parseDouble(textFieldValue);
                        oTrans.setVSPLaborDetail(fnRow, "sLaborDsc", fsLaborDesc);
                        oTrans.setVSPLaborDetail(fnRow, "nLaborAmt", valueAmount);
                        itemsToUpdate.add(laborList);
                    }
                    tblViewLabor.getItems().setAll(itemsToUpdate);
                    loadTableLabor();
                } catch (SQLException ex) {
                    Logger.getLogger(VSPFormController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

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
        handleComboBoxSelectionVSPMaster(comboBox24, 24);
        handleComboBoxSelectionVSPMaster(comboBox25, 25);
        handleComboBoxSelectionVSPMaster(comboBox23, 23);
        handleComboBoxSelectionVSPMaster(comboBox20, 20);
        /* SET TO VSP FINANCE */
        handleComboBoxSelectionVSPFinance(comboBox02_Finance, 2);
    }

    private void handleComboBoxSelectionVSPMaster(ComboBox<String> comboBox, int fieldNumber) {
        comboBox.setOnAction(e -> {
            try {
                int selectedType = comboBox.getSelectionModel().getSelectedIndex(); // Retrieve the selected type
                if (selectedType >= 0) {
                    switch (fieldNumber) {
                        case 25:
                            oTrans.setMaster(fieldNumber, Integer.valueOf(comboBox.getValue()));
                            break;
                        default:
                            oTrans.setMaster(fieldNumber, String.valueOf(selectedType));
                            break;

                    }

                    // Pass the selected type to setMaster method
                    initButton(pnEditMode);
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
//    private boolean isProgrammaticChange = false;

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
                txtField09_Finance.setDisable(true);
                txtField06_Finance.setDisable(true);
                txtField07_Finance.setDisable(true);
                txtField14_Finance.setDisable(true);
            }
        });
        txtField29.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (newValue == null || newValue.trim().isEmpty() || newValue.equals("0.00") || newValue.equals("0")) {
                try {
                    txtField42.setText("0.00");
                    txtField43.setText("0.00");
                    oTrans.setMaster(42, Double.valueOf("0.00"));
                    oTrans.setMaster(43, Double.valueOf("0.00"));
                    txtField42.setDisable(true);
                    txtField43.setDisable(true);
                } catch (SQLException ex) {
                    Logger.getLogger(VSPFormController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                txtField42.setDisable(false);
                txtField43.setDisable(false);
            }
        }
        );
        txtField30.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (newValue == null || newValue.trim().isEmpty() || newValue.equals("0.00") || newValue.equals("0")) {
                try {
                    txtField44.setText("0.00");
                    txtField45.setText("0.00");
                    oTrans.setMaster(44, Double.valueOf("0.00"));
                    oTrans.setMaster(45, Double.valueOf("0.00"));
                    txtField44.setDisable(true);
                    txtField45.setDisable(true);
                } catch (SQLException ex) {
                    Logger.getLogger(VSPFormController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                txtField44.setDisable(false);
                txtField45.setDisable(false);
            }
        }
        );
        txtField28.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (newValue == null || newValue.trim().isEmpty() || newValue.equals("0.00") || newValue.equals("0")) {
                try {
                    txtField46.setText("0.00");
                    txtField47.setText("0.00");
                    oTrans.setMaster(46, Double.valueOf("0.00"));
                    oTrans.setMaster(47, Double.valueOf("0.00"));
                    txtField47.setDisable(true);
                    txtField46.setDisable(true);
                } catch (SQLException ex) {
                    Logger.getLogger(VSPFormController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                txtField47.setDisable(false);
                txtField46.setDisable(false);
            }
        }
        );

        txtField12.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (newValue == null || newValue.trim().isEmpty() || newValue.equals("0.00")) {
                txtField11.setDisable(true);
                txtField11.setText("");
            }
        }
        );
        comboBox24.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                switch (newValue) {
                    case "NONE":
                        comboBox25.setDisable(true);
                        oTrans.setMaster(25, "0");
                        loadVSPField();
                        break;
                    case "FREE":
                    case "REGULAR RATE":
                    case "DISCOUNTED RATE":
                    case "FROM PROMO DISC":
                        comboBox25.setDisable(false);
                        break;
                    default:
                        break;
                }

                // If comboBox24 value is not "NONE," remove "0" from comboBox25's items
                if (!"NONE".equals(newValue)) {
                    comboBox25.getItems().remove("0");
                } else {
                    // If comboBox24 value is "NONE," ensure "0" is in comboBox25's items
                    if (!comboBox25.getItems().contains("0")) {
                        comboBox25.getItems().add("0");
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(VSPFormController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        );

    }

    /* Initialize CmdButton */
    private void initCmdButton() {
        btnAdd.setOnAction(this::cmdButton_Click);
        btnEdit.setOnAction(this::cmdButton_Click);
        btnSave.setOnAction(this::cmdButton_Click);
        btnCancel.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click);
        btnBrowse.setOnAction(this::cmdButton_Click);
        btnAdditionalLabor.setOnAction(this::cmdButton_Click);
    }

    private void initcheckBoxes() {
        chckBoxSpecialAccount.setOnAction(event -> {
            if (chckBoxSpecialAccount.isSelected()) {
                txtField29.setDisable(false);
                txtField30.setDisable(false);
            } else {
                txtField29.setDisable(true);
                txtField30.setDisable(true);

            }
        });

    }/* Button Click Action Event */
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
                case "btnAdditionalLabor":
                    initLaborDescript("", Double.valueOf("0.00"));
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
        // Iterate through the tabs in ImTabPane

        // Check if a new record can be created
        if (oTrans.NewRecord()) {
            switchToTab(tabMain, ImTabPane);// Load fields, clear them, and set edit mode
            loadVSPField();
            laborData.clear();
            loadTableLabor();
            clearFields();
            pnEditMode = oTrans.getEditMode();
        } else {
            // Show a warning message if creating a new record fails
            ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
            return;
        }
    }

    private void editRecordVSP() {
        if (oTrans.UpdateRecord()) {
            pnEditMode = oTrans.getEditMode();
            loadTableLabor();
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
//            if (txtField29.getText().trim().equals("")) {
//                ShowMessageFX.Warning(getStage(), "Please enter a value for VSP No.", "Warning", null);
//                txtField29.requestFocus();
//                return;
//            }
            switch (comboBox34.getSelectionModel().getSelectedIndex()) {
                case 1:
                case 2:
                case 3:
                case 4:
                    if (txtField04_Finance.getText().trim().equals("")) {
                        ShowMessageFX.Warning(getStage(), "Please enter a value for Bank", "Warning", null);
                        txtField04_Finance.requestFocus();
                        return;

                    }
                    break;
            }

            //Proceed to saving record
            if (oTrans.SaveRecord()) {
                ShowMessageFX.Information(getStage(), "Transaction save successfully.", pxeModuleName, null);
                loadVSPField();

                pnEditMode = EditMode.READY;

                loadTableLabor();
            } else {
                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", "Error while saving " + pxeModuleName + ".");
            }

        }

    }

    private void cancelRecordVSP() {
        if (ShowMessageFX.OkayCancel(getStage(), "Are you sure you want to cancel?", pxeModuleName, null) == true) {
            clearFields();
            switchToTab(tabMain, ImTabPane);// Load fields, clear them, and set edit mode
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
                    switchToTab(tabMain, ImTabPane);
                    clearFields();
                    pnEditMode = EditMode.READY;
                } else {
                    return;
                }
            }

            if (oTrans.searchRecord()) {
                switchToTab(tabMain, ImTabPane);
//                removeRequiredField();
                loadVSPField();
                loadTableLabor();
                initButton(pnEditMode);
                pnEditMode = oTrans.getEditMode();
            } else {
                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                clearFields();
                switchToTab(tabMain, ImTabPane);
                pnEditMode = EditMode.UNKNOWN;

            }
        } catch (SQLException ex) {
            Logger.getLogger(VSPFormController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void switchToTab(Tab tab, TabPane tabPane) {
        tabPane.getSelectionModel().select(tab);
    }

    private void initTextKeyPressed() {
        txtSeek03.setOnKeyPressed(this::txtField_KeyPressed);
        txtField03.setOnKeyPressed(this::txtField_KeyPressed);
        txtField77.setOnKeyPressed(this::txtField_KeyPressed);
        txtField68.setOnKeyPressed(this::txtField_KeyPressed);
        txtField71.setOnKeyPressed(this::txtField_KeyPressed);
        txtField72.setOnKeyPressed(this::txtField_KeyPressed);

        txtField08.setOnKeyPressed(this::txtField_KeyPressed);
        txtField38.setOnKeyPressed(this::txtField_KeyPressed);
        txtField06_Finance.setOnKeyPressed(this::txtField_KeyPressed);

        txtField07_Finance.setOnKeyPressed(this::txtField_KeyPressed);
        txtField13_Finance.setOnKeyPressed(this::txtField_KeyPressed);
        txtField08_Finance.setOnKeyPressed(this::txtField_KeyPressed);
        txtField28.setOnKeyPressed(this::txtField_KeyPressed);
        txtField31.setOnKeyPressed(this::txtField_KeyPressed);
        txtField32.setOnKeyPressed(this::txtField_KeyPressed);
        txtField42.setOnKeyPressed(this::txtField_KeyPressed);
        txtField43.setOnKeyPressed(this::txtField_KeyPressed);
        txtField44.setOnKeyPressed(this::txtField_KeyPressed);

        txtField45.setOnKeyPressed(this::txtField_KeyPressed);
        txtField46.setOnKeyPressed(this::txtField_KeyPressed);
        txtField47.setOnKeyPressed(this::txtField_KeyPressed);

        txtField10.setOnKeyPressed(this::txtField_KeyPressed);
        txtField16.setOnKeyPressed(this::txtField_KeyPressed);
        txtField17.setOnKeyPressed(this::txtField_KeyPressed);
        txtField18.setOnKeyPressed(this::txtField_KeyPressed);
        txtField19.setOnKeyPressed(this::txtField_KeyPressed);
        txtField12.setOnKeyPressed(this::txtField_KeyPressed);
        txtField11.setOnKeyPressed(this::txtField_KeyPressed);

        txtField04_Finance.setOnKeyPressed(this::txtField_KeyPressed);
        txtField14_Finance.setOnKeyPressed(this::txtField_KeyPressed);
        txtField26.setOnKeyPressed(this::txtField_KeyPressed);
        txtField27.setOnKeyPressed(this::txtField_KeyPressed);

    }

    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        String txtFieldID = ((TextField) event.getSource()).getId();
        try {
            if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
                switch (txtFieldID) {
                    case "txtSeek03":  //Search by VSP NO
                        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                            if (ShowMessageFX.OkayCancel(null, "Confirmation", "You have unsaved data. Are you sure you want to browse a new record?") == true) {
                                switchToTab(tabMain, ImTabPane);
                                clearFields();
                                pnEditMode = EditMode.READY;
                            } else {
                                return;
                            }
                        }
                        if (oTrans.searchRecord()) {
//                                removeRequired();
                            switchToTab(tabMain, ImTabPane);
                            loadVSPField();
                            loadTableLabor();
                            pnEditMode = oTrans.getEditMode();
                        } else {
                            ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                            clearFields();

                            switchToTab(tabMain, ImTabPane);
                            pnEditMode = EditMode.UNKNOWN;
                        }
                        initButton(pnEditMode);
                        break;
                    case "txtField77":
                        if (oTrans.searchInquiry(txtField.getText())) {
                            txtField77.setText((String) oTrans.getMaster(77));
                            clearClassMasterField();
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
                            loadVSPField();
                        } else {
                            ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                        }
                        break;
                    case "txtField04_Finance":
                        if (oTrans.searchBankApplication()) {
                            txtField04_Finance.setText((String) oTrans.getVSPFinance(4));
                            comboBox02_Finance.setDisable(false);
                            txtField13_Finance.setDisable(false);
                            txtField08_Finance.setDisable(false);
                            txtField06_Finance.setDisable(false);
                            txtField07_Finance.setDisable(false);
                            txtField14_Finance.setDisable(false);
                            loadVSPField();
                            initButton(pnEditMode);
                        } else {
                            txtField04_Finance.requestFocus();
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
                    case "txtField26":
                        if (oTrans.searchInsurance(txtField.getText(), true)) {
                            txtField26.setText((String) oTrans.getMaster(85));
                        } else {
                            txtField26.requestFocus();
                            ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                        }
                        break;
                    case "txtField27":
                        if (oTrans.searchInsurance(txtField.getText(), false)) {
                            txtField27.setText((String) oTrans.getMaster(86));
                        } else {
                            txtField27.requestFocus();
                            ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                        }
                        break;

                }
                event.consume();
                CommonUtils.SetNextFocus((TextField) event.getSource());
            } else if (event.getCode() == KeyCode.UP) {
                event.consume();
                CommonUtils.SetPreviousFocus((TextField) event.getSource());
            }
        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
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
        txtField32.focusedProperty().addListener(txtField_Focus);
        txtField28.focusedProperty().addListener(txtField_Focus);
        txtField46.focusedProperty().addListener(txtField_Focus);
        txtField47.focusedProperty().addListener(txtField_Focus);
        txtField48.focusedProperty().addListener(txtField_Focus);
        txtField10.focusedProperty().addListener(txtField_Focus);
        txtField31.focusedProperty().addListener(txtField_Focus);
        txtField11.focusedProperty().addListener(txtField_Focus);
        txtField12.focusedProperty().addListener(txtField_Focus);
        txtField16.focusedProperty().addListener(txtField_Focus);
        txtField17.focusedProperty().addListener(txtField_Focus);
        txtField18.focusedProperty().addListener(txtField_Focus);
        txtField19.focusedProperty().addListener(txtField_Focus);

        textArea09.focusedProperty().addListener(txtArea_Focus);

        txtField04_Finance.focusedProperty().addListener(txtFieldFinance_Focus);
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
                        case 11:
                            oTrans.setMaster(lnIndex, lsValue); // Handle Encoded Value
                            break;
                        case 10:
                            if (lsValue.isEmpty()) {
                                lsValue = "0.00";
                            }
                            oTrans.setMaster(lnIndex, Double.valueOf(lsValue.replace(",", "")));
                            loadVSPField();
                            break;
                        case 42:
                            if (lsValue.isEmpty()) {
                                lsValue = "0.00";
                            }

                            double enteredValue42 = Double.parseDouble(lsValue.replace(",", ""));

                            if (enteredValue42 > 100.00) {
                                ShowMessageFX.Warning(getStage(), "Invalid Amount", "Warning", null);
                                txtField42.requestFocus();
                                break;
                            }

                            if (!txtField29.getText().isEmpty()) {
                                double remainingValue = 100.00 - enteredValue42;
                                oTrans.setMaster(43, remainingValue);
                                txtField43.setText(String.format("%.2f", remainingValue));
                            }

                            oTrans.setMaster(lnIndex, enteredValue42);
                            txtField42.setText(String.format("%.2f", enteredValue42));
                            break;
                        case 43:
                            if (lsValue.isEmpty()) {
                                lsValue = "0.00";
                            }

                            double enteredValue43 = Double.parseDouble(lsValue.replace(",", ""));

                            if (enteredValue43 > 100.00) {
                                ShowMessageFX.Warning(getStage(), "Invalid Amount", "Warning", null);
                                txtField43.requestFocus();
                                break;
                            }

                            if (!txtField29.getText().isEmpty()) {
                                double remainingValue = 100.00 - enteredValue43;
                                oTrans.setMaster(42, remainingValue);
                                txtField42.setText(String.format("%.2f", remainingValue));
                            }

                            oTrans.setMaster(lnIndex, enteredValue43);
                            txtField43.setText(String.format("%.2f", enteredValue43));
                            break;
                        case 44:
                            if (lsValue.isEmpty()) {
                                lsValue = "0.00";
                            }

                            double enteredValue44 = Double.parseDouble(lsValue.replace(",", ""));

                            if (enteredValue44 > 100.00) {
                                ShowMessageFX.Warning(getStage(), "Invalid Amount", "Warning", null);
                                txtField44.requestFocus();
                                break;
                            }

                            if (!txtField30.getText().isEmpty()) {
                                double remainingValue = 100.00 - enteredValue44;
                                oTrans.setMaster(45, remainingValue);
                                txtField45.setText(String.format("%.2f", remainingValue));
                            }

                            oTrans.setMaster(lnIndex, enteredValue44);
                            txtField44.setText(String.format("%.2f", enteredValue44));
                            break;
                        case 45:
                            if (lsValue.isEmpty()) {
                                lsValue = "0.00";
                            }

                            double enteredValue45 = Double.parseDouble(lsValue.replace(",", ""));

                            if (enteredValue45 > 100.00) {
                                ShowMessageFX.Warning(getStage(), "Invalid Amount", "Warning", null);
                                txtField45.requestFocus();
                                break;
                            }

                            if (!txtField30.getText().isEmpty()) {
                                double remainingValue = 100.00 - enteredValue45;
                                oTrans.setMaster(44, remainingValue);
                                txtField44.setText(String.format("%.2f", remainingValue));
                            }

                            oTrans.setMaster(lnIndex, enteredValue45);
                            txtField45.setText(String.format("%.2f", enteredValue45));
                            break;
                        case 29: //STD
                        case 30: // SPL
                        case 28: //Promo Discount
                        case 32:
                        case 31:
                        case 16:
                        case 17:
                        case 18:
                        case 19:
                        case 12:
                            if (lsValue.isEmpty()) {
                                lsValue = "0.00";
                            }
                            oTrans.setMaster(lnIndex, Double.valueOf(lsValue.replace(",", "")));
                            loadVSPField();
                            break;
                        case 8://unit price
                            if (lsValue.isEmpty()) {
                                lsValue = "0.00";
                            }
                            if (Double.parseDouble(lsValue.replace(",", "")) < Double.parseDouble(oTrans.getMaster(38).toString())) {
                                ShowMessageFX.Warning(getStage(), "Unit Price cannot be less than Downpayment", "Warning", null);
                                txtField08.requestFocus();
                            } else {
                                oTrans.setMaster(lnIndex, Double.valueOf(lsValue.replace(",", "")));
                            }
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
                        case 46:
                            if (lsValue.isEmpty()) {
                                lsValue = "0.00";
                            }

                            double enteredValue46 = Double.parseDouble(lsValue.replace(",", ""));

                            if (enteredValue46 > 100.00) {
                                ShowMessageFX.Warning(getStage(), "Invalid Amount", "Warning", null);
                                txtField46.requestFocus();
                                break;
                            }

                            if (!txtField28.getText().isEmpty()) {
                                double remainingValue = 100.00 - enteredValue46;
                                oTrans.setMaster(47, remainingValue);
                                txtField47.setText(String.format("%.2f", remainingValue));
                            }

                            oTrans.setMaster(lnIndex, enteredValue46);
                            txtField46.setText(String.format("%.2f", enteredValue46));
                            break;

                        case 47:
                            if (lsValue.isEmpty()) {
                                lsValue = "0.00";
                            }

                            double enteredValue47 = Double.parseDouble(lsValue.replace(",", ""));

                            if (enteredValue47 > 100.00) {
                                ShowMessageFX.Warning(getStage(), "Invalid Amount", "Warning", null);
                                txtField47.requestFocus();
                                break;
                            }

                            if (!txtField28.getText().isEmpty()) {
                                double remainingValue = 100.00 - enteredValue47;
                                oTrans.setMaster(46, remainingValue);
                                txtField46.setText(String.format("%.2f", remainingValue));
                            }

                            oTrans.setMaster(lnIndex, enteredValue47);
                            txtField47.setText(String.format("%.2f", enteredValue47));

                            break;
                        case 4:
                            if (lsValue.isEmpty()) {
                                lsValue = "0.00";
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
                            case 4:
                                oTrans.setMaster(lnIndex, lsValue); // Handle Encoded Value
                                break;
                            case 6:
                                oTrans.setVSPFinance(lnIndex, Integer.valueOf(lsValue.replace(",", "")));
                                loadVSPField();
                                break;
                            case 7:
                            case 14:
                            case 8:
                                if (lsValue.isEmpty()) {
                                    lsValue = "0.00";
                                }
                                oTrans.setVSPFinance(lnIndex, Double.valueOf(lsValue.replace(",", "")));
                                loadVSPField();
                                break;
                            case 13:
                                if (lsValue.isEmpty()) {
                                    lsValue = "0.00";
                                }
                                if (Double.parseDouble(oTrans.getMaster(8).toString()) > 0.00) {
                                    if (Double.parseDouble(lsValue.replace(",", "")) > Double.parseDouble(oTrans.getMaster(8).toString())) {
                                        txtField13.setText("0.00");
                                        //oTrans.setMaster(lnIndex, Double.valueOf("0.00"));
                                        lsValue = "0.00";
                                        ShowMessageFX.Warning(getStage(), "Net Downpayment cannot be greater than Net SRP", "Warning", null);
                                        txtField13.requestFocus();
                                    }

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
//        Pattern numberAndSymbolPattern = Pattern.compile("[\\d\\p{Punct}]*");
        Pattern numberOnlyPattern = Pattern.compile("[\\d,\\.]+");

        txtField08.setTextFormatter(new InputTextFormatter(numberOnlyPattern));
        txtField38.setTextFormatter(new InputTextFormatter(numberOnlyPattern));
        txtField14_Finance.setTextFormatter(new InputTextFormatter(numberOnlyPattern));
        txtField06_Finance.setTextFormatter(new InputTextFormatter(numberOnlyPattern));
        txtField07_Finance.setTextFormatter(new InputTextFormatter(numberOnlyPattern));
        txtField082_Finance.setTextFormatter(new InputTextFormatter(numberOnlyPattern));
        txtField13_Finance.setTextFormatter(new InputTextFormatter(numberOnlyPattern));
        txtField05_Finance.setTextFormatter(new InputTextFormatter(numberOnlyPattern));
        txtField09_Finance.setTextFormatter(new InputTextFormatter(numberOnlyPattern));
        txtField08_Finance.setTextFormatter(new InputTextFormatter(numberOnlyPattern));
        txtField12_Finance.setTextFormatter(new InputTextFormatter(numberOnlyPattern));
        txtField10_Finance.setTextFormatter(new InputTextFormatter(numberOnlyPattern));
        txtField10.setTextFormatter(new InputTextFormatter(numberOnlyPattern));
        txtField16.setTextFormatter(new InputTextFormatter(numberOnlyPattern));
        txtField17.setTextFormatter(new InputTextFormatter(numberOnlyPattern));
        txtField18.setTextFormatter(new InputTextFormatter(numberOnlyPattern));
        txtField19.setTextFormatter(new InputTextFormatter(numberOnlyPattern));
        txtField13.setTextFormatter(new InputTextFormatter(numberOnlyPattern));
        txtField14.setTextFormatter(new InputTextFormatter(numberOnlyPattern));
        txtField12.setTextFormatter(new InputTextFormatter(numberOnlyPattern));
        txtField36.setTextFormatter(new InputTextFormatter(numberOnlyPattern));
        txtField37.setTextFormatter(new InputTextFormatter(numberOnlyPattern));
        txtField39.setTextFormatter(new InputTextFormatter(numberOnlyPattern));
        txtField362.setTextFormatter(new InputTextFormatter(numberOnlyPattern));
        txtField372.setTextFormatter(new InputTextFormatter(numberOnlyPattern));
        txtField392.setTextFormatter(new InputTextFormatter(numberOnlyPattern));
        txtField29.setTextFormatter(new InputTextFormatter(numberOnlyPattern));
        txtField32.setTextFormatter(new InputTextFormatter(numberOnlyPattern));
        txtField30.setTextFormatter(new InputTextFormatter(numberOnlyPattern));
        txtField28.setTextFormatter(new InputTextFormatter(numberOnlyPattern));
        txtField31.setTextFormatter(new InputTextFormatter(numberOnlyPattern));

        txtField42.setTextFormatter(new InputTextFormatter(numberOnlyPattern));
        txtField43.setTextFormatter(new InputTextFormatter(numberOnlyPattern));
        txtField44.setTextFormatter(new InputTextFormatter(numberOnlyPattern));
        txtField45.setTextFormatter(new InputTextFormatter(numberOnlyPattern));
        txtField46.setTextFormatter(new InputTextFormatter(numberOnlyPattern));
        txtField47.setTextFormatter(new InputTextFormatter(numberOnlyPattern));
        txtField48.setTextFormatter(new InputTextFormatter(numberOnlyPattern));
    }
    private Callback<DatePicker, DateCell> DateFormatCell = (final DatePicker param) -> new DateCell() {
        @Override
        public void updateItem(LocalDate item, boolean empty) {
            super.updateItem(item, empty);
            LocalDate minDate = strToDate(CommonUtils.xsDateShort((Date) oApp.getServerDate())).minusDays(0);
            setDisable(empty || item.isBefore(minDate));
        }
    };

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
            txtField32.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getMaster(32)))));

            txtField42.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getMaster(42)))));
            if (Double.valueOf(oTrans.getMaster(29).toString()) > 0.00 || Double.valueOf(oTrans.getMaster(30).toString()) > 0.00) {
                chckBoxSpecialAccount.setSelected(true);
                if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                    txtField29.setDisable(false);
                    txtField30.setDisable(false);
                }
            } else {
                chckBoxSpecialAccount.setSelected(false);
                txtField29.setDisable(true);
                txtField30.setDisable(true);
            }

            txtField43.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getMaster(43)))));

            txtField44.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getMaster(44)))));

            txtField45.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getMaster(45)))));

            txtField46.setText(decimalFormat.format(Double.parseDouble(String.format("%.3f", oTrans.getMaster(46)))));

//            if (Double.valueOf(oTrans.getMaster(29).toString())) {
//                chckBoxSpecialAccount.setSelected(true);
//            } else {
//                chckBoxSpecialAccount.setSelected(false);
//                txtField47.setDisable(true);d
//            }
            txtField47.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getMaster(47)))));

            txtField10.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getMaster(10)))));
            txtField16.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getMaster(16)))));
            comboBox21.getSelectionModel().select(Integer.parseInt(oTrans.getMaster(21).toString()));
            txtField26.setText((String) oTrans.getMaster(85));

            txtField17.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getMaster(17)))));
            comboBox22.getSelectionModel().select(Integer.parseInt(oTrans.getMaster(22).toString()));
            txtField27.setText((String) oTrans.getMaster(86));
            comboBox24.getSelectionModel().select(Integer.parseInt(oTrans.getMaster(24).toString()));
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
            comboBox25.setValue(selectedItem25);
            txtField18.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getMaster(18)))));
            comboBox23.getSelectionModel().select(Integer.parseInt(oTrans.getMaster(23).toString()));
            txtField19.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", oTrans.getMaster(19)))));
            comboBox20.getSelectionModel().select(Integer.parseInt(oTrans.getMaster(20).toString()));
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
            Logger.getLogger(VSPFormController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*Convert Date to String*/
    private LocalDate strToDate(String val) {
        DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(val, date_formatter);
        return localDate;
    }

    private void loadTableLabor() {
        try {

            /*Populate table*/
            laborData.clear();
            for (int lnCtr = 1; lnCtr <= oTrans.getVSPLaborCount(); lnCtr++) {
                String cType = "";
                switch (oTrans.getVSPLaborDetail(lnCtr, "sChrgeTyp").toString()) {
                    case "0":
                        cType = "FOC";
                        break;
                    case "1":
                        cType = "CHARGE";
                        break;
                }
                String cTo = "";
                switch (oTrans.getVSPLaborDetail(lnCtr, "sChrgeTox").toString()) {
                    case "0":
                        cTo = "C/O CLIENT";
                        break;
                    case "1":
                        cTo = "C/O COMPANY";
                        break;
                }
                String amountString = oTrans.getVSPLaborDetail(lnCtr, "nLaborAmt").toString();
                // Convert the amount to a decimal value
                double amount = Double.parseDouble(amountString);
                DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
                String formattedAmount = decimalFormat.format(amount);
                laborData.add(new VSPTableLaborList(
                        String.valueOf(lnCtr), //ROW
                        oTrans.getVSPLaborDetail(lnCtr, "sLaborCde").toString(),
                        oTrans.getVSPLaborDetail(lnCtr, "sLaborDsc").toString(),
                        cType,
                        cTo,
                        formattedAmount
                ));
            }

            tblViewLabor.setItems(laborData);
            initTableLabor();
        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }
    }

    private void initTableLabor() {
        tblLaborRow.setCellValueFactory(new PropertyValueFactory<>("tblLaborRow"));

        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            tblindex08_Labor.setCellValueFactory(new PropertyValueFactory<String, VSPTableLaborList>("txtField08_Labor"));
        } else {
            tblindex08_Labor.setCellValueFactory(new PropertyValueFactory<String, VSPTableLaborList>("tblindex08_Labor"));
        }
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            tblindex05_Labor.setCellValueFactory(new PropertyValueFactory<>("comboBoxPurchaseType"));
        } else {
            tblindex05_Labor.setCellValueFactory(new PropertyValueFactory<>("tblindex05_Labor"));
        }

        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            tblindex06_Labor.setCellValueFactory(new PropertyValueFactory<>("comboBoxChargesTo"));
        } else {
            tblindex06_Labor.setCellValueFactory(new PropertyValueFactory<>("tblindex06_Labor"));

        }
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            tblindex04_Labor.setCellValueFactory(new PropertyValueFactory<>("txtField04_Labor"));
        } else {
            tblindex04_Labor.setCellValueFactory(new PropertyValueFactory<>("tblindex04_Labor"));
        }

    }

    private void addRowVSPLabor() {
        chckBoxRustProof.setOnAction(event -> {
            if (chckBoxRustProof.isSelected()) {
                initLaborDescript("Rust Proof", Double.valueOf("0.00"));
                chckBoxRustProof.setSelected(false);
            }
        });
        chckBoxPermaShine.setOnAction(event -> {
            if (chckBoxPermaShine.isSelected()) {
                initLaborDescript("PermaShine", Double.valueOf("0.00"));
                chckBoxRustProof.setSelected(false);
            }
        });
        chckBoxUndercoat.setOnAction(event -> {
            if (chckBoxUndercoat.isSelected()) {
                initLaborDescript("UnderCoat", Double.valueOf("0.00"));
                chckBoxUndercoat.setSelected(false);
            }
        });
        chckBoxTint.setOnAction(event -> {
            if (chckBoxTint.isSelected()) {
                initLaborDescript("Tint", Double.valueOf("0.00"));
                chckBoxTint.setSelected(false);
            }
        });

    }

    private void initLaborDescript(String laborDescript, double fdblAmt) {
        if (oTrans.addVSPLabor(laborDescript, true)) {
            try {
                oTrans.setVSPLaborDetail(oTrans.getVSPLaborCount(), "sLaborDsc", laborDescript);
                oTrans.setVSPLaborDetail(oTrans.getVSPLaborCount(), "nLaborAmt", fdblAmt);
            } catch (SQLException ex) {
                Logger.getLogger(VSPFormController.class.getName()).log(Level.SEVERE, null, ex);
            }
            loadTableLabor();
        } else {
            ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", "Error while adding labor.");
        }

    }

    private void loadTableParts() {

    }

    private void initTableParts() {
        tblPartsRow.setCellValueFactory(new PropertyValueFactory<>("tblPartsRow"));
        tblindex15_Part.setCellValueFactory(new PropertyValueFactory<>("tblindex15_Part"));
        tblindex10_Part.setCellValueFactory(new PropertyValueFactory<>("tblindex10_Part"));
        tblindex08_Part.setCellValueFactory(new PropertyValueFactory<>("tblindex08_Part"));
        tblindex09_Part.setCellValueFactory(new PropertyValueFactory<>("tblindex09_Part"));
        tblindex05_Part.setCellValueFactory(new PropertyValueFactory<>("tblindex05_Part"));
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

        txtField12.setDisable(!lbShow);
        txtField11.setDisable(!(lbShow && !txtField77.getText().isEmpty()));
        textArea09.setDisable(!lbShow);
        brandNewCat.setDisable(true);
        preOwnedCat.setDisable(true);
        /* DETAILS INTERFACE */
        txtField08.setDisable(!lbShow);
        txtField38.setDisable(!lbShow);

        comboBox21.setDisable(!lbShow);

        comboBox22.setDisable(!lbShow);
        comboBox24.setDisable(!lbShow);
        comboBox25.setDisable(!lbShow);
        comboBox23.setDisable(!lbShow);
        comboBox20.setDisable(!lbShow);

        //if lbShow = false hide btn
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        btnEdit.setVisible(false);
        btnEdit.setManaged(false);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);
        btnCancel.setVisible(lbShow);
        btnCancel.setManaged(lbShow);

        chckBoxSpecialAccount.setDisable(!lbShow);

        txtField42.setDisable(true);
        txtField43.setDisable(true);
        txtField44.setDisable(true);
        txtField45.setDisable(true);
        txtField46.setDisable(true);
        txtField47.setDisable(true);

        if (lbShow) {
            try {
                if (Double.valueOf(txtField29.getText()) > 0.00) {
                    txtField42.setDisable(false);
                    txtField43.setDisable(false);
                }
            } catch (NumberFormatException e) {
                // Handle the exception or show an error message
            }
        }
        if (lbShow) {
            try {
                if (Double.valueOf(txtField30.getText()) > 0.00) {
                    txtField44.setDisable(false);
                    txtField45.setDisable(false);
                }
            } catch (NumberFormatException e) {
                // Handle the exception or show an error message
            }

        }
        if (lbShow) {
            try {
                if (Double.valueOf(txtField28.getText()) > 0.00) {
                    txtField46.setDisable(false);
                    txtField47.setDisable(false);
                }
            } catch (NumberFormatException e) {
                // Handle the exception or show an error message
            }

        }
        txtField29.setDisable(true);
        txtField30.setDisable(true);

        txtField36.setDisable(true);
        txtField37.setDisable(true);
        txtField39.setDisable(true);

        txtField362.setDisable(true);
        txtField372.setDisable(true);
        txtField392.setDisable(true);
        txtField28.setDisable(!lbShow); // promo
        txtField31.setDisable(!lbShow); // bundle
        txtField32.setDisable(!lbShow); // cash

        txtField04_Finance.setDisable(true);
        comboBox02_Finance.setDisable(true);
        txtField13_Finance.setDisable(true);
        txtField08_Finance.setDisable(true);
        txtField06_Finance.setDisable(true);
        txtField07_Finance.setDisable(true);
        txtField14_Finance.setDisable(true);
        txtField10.setDisable(true);

        switch (comboBox34.getSelectionModel().getSelectedIndex()) {
            case 1: //BANK PURCHASE ORDER
                txtField10.setDisable(!lbShow);
                //txtField04_Finance.setDisable(!lbShow);
//                txtField06_Finance.setDisable(lbShow);
//                txtField07_Finance.setDisable(lbShow);
                txtField04_Finance.setDisable(!lbShow);
                if (!txtField04_Finance.getText().isEmpty()) {
                    comboBox02_Finance.setDisable(!lbShow);
                    txtField13_Finance.setDisable(!lbShow);
                    txtField08_Finance.setDisable(!lbShow);
                    txtField14_Finance.setDisable(!lbShow);
                }

                break;
            case 2:
            case 3:
            case 4:
                txtField10.setDisable(!lbShow);
                txtField04_Finance.setDisable(!lbShow);
                if (!txtField04_Finance.getText().isEmpty()) {
                    comboBox02_Finance.setDisable(!lbShow);
                    txtField13_Finance.setDisable(!lbShow);
                    txtField08_Finance.setDisable(!lbShow);
                    txtField14_Finance.setDisable(!lbShow);
                    txtField06_Finance.setDisable(!lbShow);
                    txtField07_Finance.setDisable(!lbShow);
                }
                break;
        }

        switch (comboBox02_Finance.getSelectionModel().getSelectedIndex()) {
            case 0: //NONE
            case 1: //ALL-IN HOUSE
                txtField31.setDisable(true); // bundle
                 {
                    try {
                        oTrans.setMaster(31, Double.valueOf("0.00"));

                    } catch (SQLException ex) {
                        Logger.getLogger(VSPFormController.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
                }
                loadVSPField();
                break;

            case 2: //ALL-IN PROMO
                txtField32.setDisable(true); // cash
                 {
                    try {
                        oTrans.setMaster(32, Double.valueOf("0.00"));

                    } catch (SQLException ex) {
                        Logger.getLogger(VSPFormController.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
                }
                loadVSPField();
                break;
        }

        switch (comboBox21.getSelectionModel().getSelectedIndex()) {
            case 0: //NONE
                txtField16.setDisable(lbShow);
                txtField26.setDisable(lbShow);
                 {
                    try {
                        oTrans.setMaster(16, Double.valueOf("0.00"));
                        oTrans.setMaster(26, "");

                    } catch (SQLException ex) {
                        Logger.getLogger(VSPFormController.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
                }
                loadVSPField();
                break;
            case 1: //FOC
                txtField16.setDisable(lbShow);
                txtField26.setDisable(!lbShow);
                try {
                    oTrans.setMaster(16, Double.valueOf("0.00"));

                } catch (SQLException ex) {
                    Logger.getLogger(VSPFormController.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
                loadVSPField();
                break;
            case 2:
            case 3:
            case 4:
                txtField16.setDisable(!lbShow);
                txtField26.setDisable(!lbShow);
                break;
        }

        switch (comboBox22.getSelectionModel()
                .getSelectedIndex()) {
            case 0: //NONE
                txtField17.setDisable(lbShow);
                txtField27.setDisable(lbShow);
                comboBox24.setDisable(lbShow);
                comboBox25.setDisable(lbShow);
                 {
                    try {
                        oTrans.setMaster(17, Double.valueOf("0.00"));
                        oTrans.setMaster(27, "");
                        oTrans.setMaster(24, String.valueOf("0"));
                        oTrans.setMaster(25, String.valueOf("0"));

                    } catch (SQLException ex) {
                        Logger.getLogger(VSPFormController.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
                }
                loadVSPField();
                break;
            case 1: //FOC
                txtField17.setDisable(lbShow);
                txtField27.setDisable(!lbShow);
                comboBox24.setDisable(!lbShow);
                comboBox25.setDisable(!lbShow);
                 {
                    try {
                        oTrans.setMaster(17, Double.valueOf("0.00"));

                    } catch (SQLException ex) {
                        Logger.getLogger(VSPFormController.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
                }
                loadVSPField();
                break;
            case 2:
            case 3:
                comboBox24.setDisable(!lbShow);
                comboBox25.setDisable(!lbShow);
                txtField17.setDisable(!lbShow);
                txtField27.setDisable(!lbShow);
                break;
        }
        switch (comboBox23.getSelectionModel().getSelectedIndex()) {
            case 0: //NONE
            case 1: //FOC
                txtField18.setDisable(lbShow);
                 {
                    try {
                        oTrans.setMaster(18, Double.valueOf("0.00"));

                    } catch (SQLException ex) {
                        Logger.getLogger(VSPFormController.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
                }
                loadVSPField();
                break;
            case 2: //
                txtField18.setDisable(!lbShow);
                break;
        }

        switch (comboBox24.getSelectionModel()
                .getSelectedIndex()) {
            case 0: //NONE
                comboBox25.setDisable(lbShow);
                break;
            case 1:
            case 2:
            case 3:
                comboBox25.setDisable(!lbShow);
                break;
        }
        switch (comboBox20.getSelectionModel().getSelectedIndex()) {
            case 0: //NONE
            case 1: //FOC
                txtField19.setDisable(lbShow);
                 {
                    try {
                        oTrans.setMaster(19, Double.valueOf("0.00"));

                    } catch (SQLException ex) {
                        Logger.getLogger(VSPFormController.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
                }
                loadVSPField();
                break;
            default:
                txtField19.setDisable(!lbShow);
                break;
        }
        chckBoxRustProof.setDisable(!lbShow);
        chckBoxPermaShine.setDisable(!lbShow);
        chckBoxUndercoat.setDisable(!lbShow);
        chckBoxTint.setDisable(!lbShow);
        btnAdditionalLabor.setDisable(!lbShow);

//
//        tblViewLabor.setDisable(!lbShow);
        if (fnValue == EditMode.READY) {
            btnEdit.setVisible(true);
            btnEdit.setManaged(true);
            btnPrint.setVisible(true);
            btnPrint.setManaged(true);
            tabAddOns.setDisable(false);
            tabDetails.setDisable(false);
        }
        if (fnValue == EditMode.UPDATE) {

//            try {
//                if (!oTrans.getMaster(79).toString().isEmpty()) {
//
//
//                }
            txtField77.setDisable(true);
            txtField71.setDisable(true);
            txtField72.setDisable(true);
//            } catch (SQLException ex) {
//                Logger.getLogger(VSPFormController.class
//                        .getName()).log(Level.SEVERE, null, ex);
//            }
        }
    }

    /*Clear Fields*/
    public void clearFields() {
        laborData.clear();
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

        txtField10.setText("0.00");
        txtField16.setText("0.00");
        comboBox21.setValue(null);
        txtField26.setText("");

        txtField17.setText("0.00");
        comboBox22.setValue(null);
        txtField27.setText("");

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

        chckBoxSpecialAccount.setSelected(false);

        txtField42.setText("0.00");
        txtField43.setText("0.00");

        txtField44.setText("0.00");
        txtField45.setText("0.00");

        txtField46.setText("0.00");
        txtField47.setText("0.00");

        chckBoxRustProof.setSelected(false);
        chckBoxPermaShine.setSelected(false);
        chckBoxUndercoat.setSelected(false);
        chckBoxTint.setSelected(false);

    }

    private VSPTableLaborList getSelectedItem() {
        return tblViewLabor.getSelectionModel().getSelectedItem();
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
                Logger.getLogger(VSPFormController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
