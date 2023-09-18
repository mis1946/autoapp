/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.sales;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
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
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.auto.app.views.CustomerFormController;
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
    private TextField txtField02;
    @FXML
    private ToggleGroup tgUnitCategory;
    @FXML
    private Button btnPrint;
    @FXML
    private TextField txtField03;
    @FXML
    private TextField txtField78;
    @FXML
    private TextField txtField84;
    @FXML
    private TextField txtField75;
    @FXML
    private TextField txtField82;
    @FXML
    private TextField txtField76;
    @FXML
    private TextField txtField81;
    @FXML
    private TextField txtField77;
    @FXML
    private TextField txtField68;
    @FXML
    private TextArea textArea69;
    @FXML
    private TextField txtField71;
    @FXML
    private TextField txtField72;
    @FXML
    private TextField txtField48;
    @FXML
    private TextField txtField73;
    @FXML
    private TextField txtField74;
    @FXML
    private TextArea textArea09;
    @FXML
    private TextField txtField40;
    @FXML
    private TextField txtField37;
    @FXML
    private TextField txtField38;
    @FXML
    private TextField txtField39;
    @FXML
    private TextField txtField08;
    @FXML
    private TextField txtField29;
    @FXML
    private TextField txtField30;
    @FXML
    private TextField txtField28;
    @FXML
    private TextField txtField31;
    @FXML
    private TextField txtField42;
    @FXML
    private TextField txtField44;
    @FXML
    private TextField txtField46;
    @FXML
    private TextField txtField43;
    @FXML
    private TextField txtField45;
    @FXML
    private TextField txtField47;
    @FXML
    private TextField txtField16;
    @FXML
    private TextField txtField17;
    @FXML
    private TextField txtField18;
    @FXML
    private TextField txtField19;
    @FXML
    private TextField txtField13;
    @FXML
    private TextField txtField14;
    @FXML
    private TextField txtField12;
    @FXML
    private ComboBox<String> comboBox34;
    ObservableList<String> cModeOfPayment = FXCollections.observableArrayList("CASH", "BANK PURCHASE ORDER", "BANK FINANCING", "COMPANY PURCHASE ORDER", "COMPANY FINANCING"); //Mode of Payment Values
    @FXML
    private ComboBox<String> comboBox21;
    ObservableList<String> cTplType = FXCollections.observableArrayList("NONE", "FOC", "C/o CLIENT", "C/o COMP", "C/o COMP");
    @FXML
    private ComboBox<String> comboBox22;
    ObservableList<String> cCompType = FXCollections.observableArrayList("NONE", "FOC", "C/o CLIENT", "C/o COMP", "C/o COMP");
    @FXML
    private ComboBox<String> comboBox24;
    ObservableList<String> cCompYearType1 = FXCollections.observableArrayList("NONE", "FREE", "REGULAR RATE", "DISCOUNTED RATE", "FROM PROMO DISC");
    @FXML
    private ComboBox<String> comboBox25;
    ObservableList<String> cCompYearType2 = FXCollections.observableArrayList("0", "1", "2", "3", "4");
    @FXML
    private ComboBox<String> comboBox23;
    ObservableList<String> cLTOType = FXCollections.observableArrayList("NONE", "FOC", "CHARGE");
    @FXML
    private ComboBox<String> comboBox20;
    ObservableList<String> cHMOType = FXCollections.observableArrayList("NONE", "FOC", "CHARGE", "C/o BANK");
    @FXML
    private TextField txtField26;
    @FXML
    private ComboBox<String> comboBox02;
    ObservableList<String> cFinPromoType = FXCollections.observableArrayList("NONE", "ALL-IN IN HOUSE", "ALL-IN PROMO");
    @FXML
    private TextField txtField27;
    @FXML
    private TextField txtField11;
    @FXML
    private DatePicker date04;
    @FXML
    private TextArea textArea70;
    @FXML
    private TextField txtField10;
    @FXML
    private TextField txtField80;
    @FXML
    private RadioButton brandNewCat;
    @FXML
    private RadioButton preOwnedCat;
    @FXML
    private TextField txtField83;
    @FXML
    private TextField txtField402;
    @FXML
    private TextField txtField372;
    @FXML
    private TextField txtField392;
    @FXML
    private TextField txtSeek03;

    private Stage getStage() {
        return (Stage) btnClose.getScene().getWindow();
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
        //Button Click Event
        btnAdd.setOnAction(this::cmdButton_Click);
        btnEdit.setOnAction(this::cmdButton_Click);
        btnSave.setOnAction(this::cmdButton_Click);
        btnCancel.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click);
        btnBrowse.setOnAction(this::cmdButton_Click);

        setCapsLockBehavior(txtField28);
        // SET COMBO BOX ITEMS
        comboBox34.setItems(cModeOfPayment);
        comboBox21.setItems(cTplType);
        comboBox22.setItems(cCompType);
        comboBox24.setItems(cCompYearType1);
        comboBox25.setItems(cCompYearType2);
        comboBox23.setItems(cLTOType);
        comboBox20.setItems(cHMOType);
        comboBox02.setItems(cFinPromoType);

        txtField77.focusedProperty().addListener(txtField_Focus);
        txtField71.focusedProperty().addListener(txtField_Focus);
        txtField72.focusedProperty().addListener(txtField_Focus);

        txtSeek03.setOnKeyPressed(this::txtField_KeyPressed);
        txtField03.setOnKeyPressed(this::txtField_KeyPressed);
        txtField77.setOnKeyPressed(this::txtField_KeyPressed);
        txtField68.setOnKeyPressed(this::txtField_KeyPressed);
        txtField71.setOnKeyPressed(this::txtField_KeyPressed);
        txtField72.setOnKeyPressed(this::txtField_KeyPressed);

        /*set comboBox */
        handleComboBoxSelection(comboBox21, 21);
        handleComboBoxSelection(comboBox22, 22);
        handleComboBoxSelection(comboBox22, 24);
        handleComboBoxSelection(comboBox25, 23);
        handleComboBoxSelection(comboBox25, 20);

        pnEditMode = EditMode.UNKNOWN;
        initButton(pnEditMode);
    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private void handleComboBoxSelection(ComboBox<String> comboBox, int fieldNumber) {
        comboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String selectedType = comboBox.getValue(); // Retrieve the selected type
                // Set the type ID in the text field
                try {
                    oTrans.setMaster(fieldNumber, selectedType); // Pass the selected type to setMaster method
                } catch (SQLException ex) {
                    Logger.getLogger(VSPFormController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private void cmdButton_Click(ActionEvent event) {
        try {
            String lsButton = ((Button) event.getSource()).getId();
            switch (lsButton) {
                case "btnAdd":
                    addRecord();
                    break;
                case "btnEdit":
                    editRecord();
                    break;
                case "btnSave":
                    saveRecord();
                    break;
                case "btnCancel":
                    cancelRecord();
                    break;
                case "btnBrowse":
                    browseRecord();
                    break;
                case "btnPrint":
                    break;
                case "btnClose":
                    closeForm();
                    break;
            }
            initButton(pnEditMode);
        } catch (SQLException ex) {
            Logger.getLogger(UnitDeliveryReceiptFormController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void addRecord() {
        if (oTrans.NewRecord()) {
            clearFields();
            loadVSPField();

            pnEditMode = oTrans.getEditMode();
        } else {
            ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
            return;
        }
    }

    private void editRecord() {
        if (oTrans.UpdateRecord()) {
            pnEditMode = oTrans.getEditMode();
        } else {
            ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
        }
    }

    private void saveRecord() throws SQLException {
        if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to save?") == true) {
//            if (comboBox30.getSelectionModel().isEmpty()) {
//                ShowMessageFX.Warning(getStage(), "Please choose a value for Customer Type", "Warning", null);
//                return;
//            }
//            if (txtField03.getText().trim().equals("")) {
//                ShowMessageFX.Warning(getStage(), "Please enter a value for Unit Delivery Receipt No.", "Warning", null);
//                txtField03.requestFocus();
//                return;
//            }
//            if (txtField29.getText().trim().equals("")) {
//                ShowMessageFX.Warning(getStage(), "Please enter a value for VSP No.", "Warning", null);
//                txtField29.requestFocus();
//                return;
//            }

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

    private void cancelRecord() {
        if (ShowMessageFX.OkayCancel(getStage(), "Are you sure you want to cancel?", pxeModuleName, null) == true) {
            clearFields();
            pnEditMode = EditMode.UNKNOWN;
        }
    }

    private void closeForm() {
        if (ShowMessageFX.OkayCancel(null, "Close Tab", "Are you sure you want to close this Tab?")) {
            if (unload != null) {
                unload.unloadForm(AnchorMain, oApp, pxeModuleName);
            } else {
                ShowMessageFX.Warning(null, "Warning", "Please notify the system administrator to configure the null value at the close button.");
            }
        }
    }

    private void browseRecord() {
        try {
            String fxTitle = "Confirmation";
            String fxHeader = "You have unsaved data. Are you sure you want to browse a new record?";
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (ShowMessageFX.OkayCancel(null, fxTitle, fxHeader)) {
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
                switch (lnIndex) {
                    case 77:
                    case 71:
                    case 72:
                        oTrans.setMaster(lnIndex, lsValue); // Handle Encoded Value
                        break;

                }

            } else {
                txtField.selectAll();

            }
        } catch (SQLException ex) {
            Logger.getLogger(VSPFormController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    };

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
                                loadVSPField();
                                initButton(pnEditMode);
                            } else {
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
                        case "txtField71":
                            if (oTrans.searchAvailableVhcl(txtField.getText())) {
                                txtField71.setText((String) oTrans.getMaster(71));
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

    private static void setCapsLockBehavior(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (textField.getText() != null) {
                textField.setText(newValue.toUpperCase());
            }
        });
    }

    /*Convert Date to String*/
    private LocalDate strToDate(String val) {
        DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(val, date_formatter);
        return localDate;
    }

//    public static void setDoubleText(TextField textField, Double value) {
//        if (value != null) {
//            String stringValue = Double.toString(value);
//            textField.setText(stringValue);
//        } else {
//            // Handle the case where the value is null
//            textField.setText("0.00"); // Or set a default value or handle it as needed
//        }
//    }
    private void loadVSPField() {
        try {

            /* MAIN INTERFACE */
            txtField03.setText((String) oTrans.getMaster(3));
            txtField02.setText(CommonUtils.xsDateMedium((Date) oTrans.getMaster(2)));
            txtField77.setText((String) oTrans.getMaster(77));
            txtField78.setText((String) oTrans.getMaster(78));
            date04.setValue(strToDate(CommonUtils.xsDateShort((Date) oTrans.getMaster(4))));
            txtField84.setText((String) oTrans.getMaster(84));
            txtField75.setText((String) oTrans.getMaster(75));
            txtField80.setText((String) oTrans.getMaster(80));
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

            txtField37.setText(String.valueOf(oTrans.getMaster(37)));
            txtField39.setText(String.valueOf(oTrans.getMaster(39)));
            txtField40.setText(String.valueOf(oTrans.getMaster(40)));


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
            txtField08.setText(String.valueOf(oTrans.getMaster(8)));
            txtField38.setText(String.valueOf(oTrans.getMaster(38)));
            txtField29.setText(String.valueOf(oTrans.getMaster(29)));
            txtField30.setText(String.valueOf(oTrans.getMaster(30)));
            txtField28.setText(String.valueOf(oTrans.getMaster(28)));
            txtField31.setText(String.valueOf(oTrans.getMaster(31)));
            String selectedItem02 = oTrans.getMaster(2).toString();
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
            txtField42.setText(String.valueOf(oTrans.getMaster(42)));
            txtField43.setText(String.valueOf(oTrans.getMaster(43)));
            txtField44.setText(String.valueOf(oTrans.getMaster(44)));
            txtField45.setText(String.valueOf(oTrans.getMaster(45)));

            txtField46.setText(String.valueOf(oTrans.getMaster(46)));
            txtField47.setText(String.valueOf(oTrans.getMaster(47)));

            txtField10.setText(String.valueOf(oTrans.getMaster(10)));
            txtField16.setText(String.valueOf(oTrans.getMaster(16)));
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

            txtField17.setText(String.valueOf(oTrans.getMaster(17)));
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
            txtField18.setText(String.valueOf(oTrans.getMaster(18)));
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

            txtField19.setText(String.valueOf(oTrans.getMaster(19)));
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

            txtField13.setText(String.valueOf(oTrans.getMaster(13)));
            txtField14.setText(String.valueOf(oTrans.getMaster(14)));
            txtField12.setText(String.valueOf(oTrans.getMaster(12)));
            txtField11.setText((String) oTrans.getMaster(11));

            txtField402.setText(String.valueOf(oTrans.getMaster(40)));
            txtField372.setText(String.valueOf(oTrans.getMaster(37)));
            txtField392.setText(String.valueOf(oTrans.getMaster(39)));

        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }

    }

//    /*Set ComboBox Value to Master Class*/
//    @SuppressWarnings("ResultOfMethodCallIgnored")
//    private boolean setSelection() {
//        try {
//            if (brandNewCat.isSelected()) {
//                oTrans.setMaster(54, "0");
//            } else if (preOwnedCat.isSelected()) {
//                oTrans.setMaster(54, "1");
//            }
//        } catch (SQLException ex) {
//            ShowMessageFX.Warning(getStage(), ex.getMessage(), "Warning", null);
//        }
//        return true;
//    }

    /*Enabling / Disabling Fields*/
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
        txtField68.setDisable(!lbShow);
        textArea69.setDisable(!lbShow);
        txtField71.setDisable(!lbShow);
        txtField72.setDisable(!lbShow);
        textArea70.setDisable(!lbShow);
        txtField73.setDisable(!lbShow);
        txtField74.setDisable(!lbShow);

        brandNewCat.setDisable(true);
        preOwnedCat.setDisable(true);
        /* DETAILS INTERFACE */
        txtField08.setDisable(!lbShow);
        txtField38.setDisable(!lbShow);
        txtField29.setDisable(!lbShow);
        txtField30.setDisable(!lbShow);

        txtField42.setDisable(!lbShow);
        txtField43.setDisable(!lbShow);
        txtField44.setDisable(!lbShow);
        txtField45.setDisable(!lbShow);
        txtField46.setDisable(!lbShow);
        txtField47.setDisable(!lbShow);
        //        txtField10.setDisable(!lbShow);

        comboBox21.setDisable(!lbShow);
        comboBox22.setDisable(!lbShow);
        comboBox24.setDisable(true);
        comboBox25.setDisable(true);
        txtField18.setDisable(!lbShow);
        comboBox23.setDisable(!lbShow);
        txtField19.setDisable(!lbShow);
        comboBox20.setDisable(!lbShow);
        comboBox02.setDisable(!lbShow);
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

        txtField10.setDisable(true);
        txtField40.setDisable(true);
        txtField37.setDisable(true);
        txtField39.setDisable(true);
        txtField31.setDisable(true);
        txtField16.setDisable(true);
        txtField26.setDisable(true);
        txtField17.setDisable(true);
        txtField27.setDisable(true);
        txtField28.setDisable(true);
        txtField402.setDisable(true);
        txtField372.setDisable(true);
        txtField392.setDisable(true);

        //        if (fnValue == EditMode.READY) {
//            btnEdit.setVisible(true);
//            btnEdit.setManaged(true);
//            if (lblCancelStatus.getText().equals("Cancelled")) {
//                btnCancelVSP.setVisible(false);
//                btnCancelVSP.setManaged(false);
//                btnEdit.setVisible(false);
//                btnEdit.setManaged(false);
//                btnPrint.setVisible(false);
//                btnPrint.setManaged(false);
//
//            } else {
//                btnCancelVSP.setVisible(true);
//                btnCancelVSP.setManaged(true);
//                btnEdit.setVisible(true);
//                btnEdit.setManaged(true);
//                btnPrint.setVisible(true);
//                btnPrint.setManaged(true);
//            }
        if (fnValue == EditMode.ADDNEW) {
            String selectedItem34 = comboBox34.getValue();
            switch (selectedItem34) {
                case "CASH":
                    txtField10.setDisable(true);

                    break;
                case "BANK FINANCING":
                case "COMPANY FINANCING":
                    txtField10.setDisable(false);
                    break;

            }
            String selectedItem02 = comboBox02.getValue();
            switch (selectedItem02) {
                case "NONE":
                    txtField28.setDisable(false);
                    txtField31.setDisable(true);
                    break;
                case "ALL-IN PROMO":
                    txtField31.setDisable(false);
                    txtField28.setDisable(false);
                    break;
            }
            String selectedItem21 = comboBox21.getValue();
            switch (selectedItem21) {
                case "NONE":
                case "FOC":
                    txtField16.setDisable(true);
                    txtField26.setDisable(true);
                    break;
            }
            String selectedItem22 = comboBox22.getValue();
            if (selectedItem22.equals("NONE") && selectedItem22.equals("FOC")) {
                txtField17.setDisable(true);
                txtField27.setDisable(true);
            } else if (selectedItem22.equals("NONE")) {
                comboBox24.setDisable(true);
                comboBox25.setDisable(true);
            } else {
                txtField17.setDisable(false);
                txtField27.setDisable(false);
                comboBox24.setDisable(false);
                comboBox25.setDisable(false);
            }

            String selectedItem24 = comboBox24.getValue();
            switch (selectedItem24) {
                case "NONE":
                    comboBox25.setItems(FXCollections.observableArrayList("0"));
                    comboBox25.setDisable(true);
                    break;
                case "FREE":
                case "REGULAR RATE":
                case "DISCOUNTED RATE":
                case "FROM PROMO DISC":
                    comboBox25.setItems(FXCollections.observableArrayList("1", "2", "3", "4"));
                    comboBox25.setDisable(false);
                    break;
                default:
                    // Handle other cases if needed
                    break;
            }

        }

        if (fnValue == EditMode.READY) {
            btnEdit.setVisible(true);
            btnEdit.setManaged(true);
            btnPrint.setVisible(true);
            btnPrint.setManaged(true);

//            int selectedIndex = comboBox34.getSelectionModel().getSelectedIndex();
//            System.out.println("Selected Index: " + selectedIndex);
        }
        if (fnValue == EditMode.UPDATE) {
            txtField71.setDisable(true);
            txtField72.setDisable(true);
            txtField77.setDisable(true);
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
        txtField80.setText("");
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

        txtField40.setText("0.0");
        txtField37.setText("0.0");
        txtField39.setText("0.0");

        /* DETAILS INTERFACE */
        comboBox34.setValue(null);
        txtField08.setText("0.0");
        txtField38.setText("0.0");

        txtField29.setText("0.0");
        txtField30.setText("0.0");
        txtField28.setText("0.0");
        txtField31.setText("0.0");

        txtField42.setText("");
        txtField43.setText("");
        txtField44.setText("");
        txtField45.setText("");

        txtField46.setText("");
        txtField47.setText("");

        txtField10.setText("0.0");
        txtField16.setText("0.0");
        comboBox21.setValue(null);
        txtField26.setText("");

        txtField17.setText("0.0");
        comboBox22.setValue(null);
        txtField27.setText("");

        comboBox25.setValue(null);
        comboBox25.setValue(null);
        txtField18.setText("0.00");
        comboBox23.setValue(null);
        txtField19.setText("0.00");
        comboBox20.setValue(null);

        comboBox02.setValue(null);

        txtField13.setText("0.0");
        txtField14.setText("0.0");
        txtField12.setText("0.0");
        txtField11.setText("");

        txtField402.setText("0.0");
        txtField372.setText("0.0");
        txtField392.setText("0.0");

    }

}
