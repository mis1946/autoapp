/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.auto.app.sales;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.auto.app.views.CancelForm;
import org.rmj.auto.app.views.DateCellDisabler;
import org.rmj.auto.app.views.ScreenInterface;
import org.rmj.auto.app.views.TextFieldAnimationUtil;
import org.rmj.auto.app.views.unloadForm;
import org.rmj.auto.sales.base.VehicleDeliveryReceipt;

/**
 * Unit Delivery Receipt Form Controller class
 *
 * @author
 */
public class UnitDeliveryReceiptFormController implements Initializable, ScreenInterface {

    private VehicleDeliveryReceipt oTrans;
    private GRider oApp;
    private MasterCallback oListener;
    unloadForm unload = new unloadForm();
    private final String pxeModuleName = "Unit Delivery Receipt"; //Form Title
    TextFieldAnimationUtil txtFieldAnimation = new TextFieldAnimationUtil();
    private int pnEditMode;//Modifying fields
    private double xOffset = 0;
    private double yOffset = 0;
    private int daysToDisable = 30;
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnBrowse;
    @FXML
    private Button btnPrint;
    @FXML
    private Button btnClose;
    ObservableList<String> cFormItems = FXCollections.observableArrayList("CUSTOMER", "SUPPLIER");
    @FXML
    private ToggleGroup carCategory;
    @FXML
    private TextField txtField29;
    @FXML
    private DatePicker date02;
    @FXML
    private TextField txtField03;
    @FXML
    private TextField txtField22;
    @FXML
    private TextField txtField23;
    @FXML
    private TextField txtField24;
    @FXML
    private TextField txtField25;
    @FXML
    private TextField txtField26;
    @FXML
    private TextField txtField28;
    @FXML
    private TextField txtField27;
    @FXML
    private TextArea textArea06;
    @FXML
    private RadioButton radioBrandNew;
    @FXML
    private RadioButton radioPreOwned;
    @FXML
    private TextField txtField15;

    @FXML
    private TextField txtField34;
    @FXML
    private TextField txtField35;
    @FXML
    private ComboBox<String> comboBox32;
    @FXML
    private Label lblUDRStatus;
    @FXML
    private Button btnCancelUDR;

    /**
     * Initializes the controller class.
     */
    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private Stage getStage() {
        return null;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        oTrans = new VehicleDeliveryReceipt(oApp, oApp.getBranchCode(), false);
        oTrans.setCallback(oListener);
        oTrans.setWithUI(true);

        initMonitoringProperty();

        initCapitalizationFields();

        initButtonClick();

        initTextFieldFocus();

        initTextAreaFocus();

        initTextKeyPressed();

        initSetComboBoxMaster();

        initRequiredFieldListener();

        date02.setOnAction(this::getDate);
        date02.setDayCellFactory(DateCellDisabler.createDisableDateCallback(daysToDisable));

        comboBox32.setItems(cFormItems);
        pnEditMode = EditMode.UNKNOWN;
        initButton(pnEditMode);

        Platform.runLater(() -> {
            if (oTrans.loadState()) {
                pnEditMode = oTrans.getEditMode();
                loadCustomerField();
                initButton(pnEditMode);
            } else {
                if (oTrans.getMessage().isEmpty()) {
                } else {
                    ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                }
            }
        });

    }

    private void initMonitoringProperty() {
        txtField29.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (newValue.isEmpty()) {
                clearVSPFields();
            }
        });

    }

    private void initCapitalizationFields() {
        setTextAreaCapital(textArea06);
    }

    private static void setTextAreaCapital(TextArea textArea) {
        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (textArea.getText() != null) {
                textArea.setText(newValue.toUpperCase());
            }
        });
    }

    private void initButtonClick() {
        btnBrowse.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click);
        btnAdd.setOnAction(this::cmdButton_Click);
        btnSave.setOnAction(this::cmdButton_Click);
        btnEdit.setOnAction(this::cmdButton_Click);
        btnCancel.setOnAction(this::cmdButton_Click);
        btnPrint.setOnAction(this::cmdButton_Click);
        btnCancelUDR.setOnAction(this::cmdButton_Click);
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
                    String srowdata = oTrans.getMaster(1).toString();
                    loadUDRPrint(srowdata);
                    break;
                case "btnClose":
                    closeForm();
                    break;
                case "btnCancelUDR":
                    String sTransNox = oTrans.getMaster(1).toString();
                    String sReferNox = oTrans.getMaster(3).toString();
                    CancelForm cancelform = new CancelForm();

                    if (cancelform.loadCancelWindow(oApp, sTransNox, sReferNox, "UDR")) {
                        if (oTrans.cancelUDR()) {
                            ShowMessageFX.Information(null, pxeModuleName, "UDR Successfully Cancelled.");
                            if (oTrans.OpenRecord(sTransNox)) {
                                loadCustomerField();
                            }
                            pnEditMode = oTrans.getEditMode();
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
                            return;
                        }
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, "Error while cancelling UDR.");
                        return;
                    }

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
            loadCustomerField();
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
            if (pnEditMode == EditMode.ADDNEW) {
                if (comboBox32.getSelectionModel().isEmpty()) {
                    ShowMessageFX.Warning(getStage(), "Please choose a value for Customer Type", "Warning", null);
                    return;
                }
            }
            switch (comboBox32.getSelectionModel().getSelectedIndex()) {
                case 0:
                    if (txtField29.getText().trim().equals("")) {
                        ShowMessageFX.Warning(getStage(), "Please enter a value for VSP No.", "Warning", null);
                        txtField29.requestFocus();
                        return;
                    }
                    break;
            }

            //Proceed to saving record
            if (oTrans.SaveRecord()) {
                ShowMessageFX.Information(getStage(), "Transaction save successfully.", pxeModuleName, null);
                if (oTrans.OpenRecord((String) oTrans.getMaster(1))) {
                    loadCustomerField();
                    pnEditMode = EditMode.READY;
                } else {
                    ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", "Error while opening Unit Delivery Receipt Information");
                }
            } else {
                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", "Error while saving Unit Delivery Receipt Information");
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
            if (oTrans.searchRecord("")) {
                removeRequiredField();
                loadCustomerField();
                pnEditMode = EditMode.READY;
            } else {
                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                clearFields();
                pnEditMode = EditMode.UNKNOWN;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UnitDeliveryReceiptFormController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void initTextFieldFocus() {
        txtField29.focusedProperty().addListener(txtField_Focus);
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
                    case 14:
                    case 29:
                        oTrans.setMaster(lnIndex, lsValue); // Handle Encoded Value
                        break;

                }

            } else {
                txtField.selectAll();

            }
        } catch (SQLException ex) {
            Logger.getLogger(UnitDeliveryReceiptFormController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    };

    private void initTextAreaFocus() {
        textArea06.focusedProperty().addListener(txtArea_Focus);
    }
    /*Set TextArea to Master Class*/
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
                    case 6:
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

    private void initTextKeyPressed() {
        txtField29.setOnKeyPressed(this::txtField_KeyPressed);
    }

    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        String txtFieldID = ((TextField) event.getSource()).getId();
        try {
            if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
                switch (txtFieldID) {
                    case "txtField29":
                        if (oTrans.searchVSP(txtField.getText())) {
                            loadCustomerField();
                        } else {
                            ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                            txtField29.requestFocus();
                            return;
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

    private void initSetComboBoxMaster() {

        /* SET TO MASTER */
        handleComboBoxSelectionMaster(comboBox32, 32);

    }

    private void handleComboBoxSelectionMaster(ComboBox<String> comboBox, int fieldNumber) {
        comboBox.setOnAction(e -> {
            try {
                int selectedType = comboBox.getSelectionModel().getSelectedIndex(); // Retrieve the selected type
                if (selectedType >= 0) {
                    switch (fieldNumber) {
                        case 32:
                            if (pnEditMode == EditMode.ADDNEW) {
                                oTrans.setMaster(fieldNumber, String.valueOf(selectedType));
                                switch (selectedType) {
                                    case 1:
                                        clearVSPFieldsMaster();
                                        ShowMessageFX.Warning(getStage(), null, pxeModuleName, "Supplier is Under Development, please choose customer to proceed.");
                                        break;
                                }

                                loadCustomerField();
                            }
                            break;
                        default:
                            oTrans.setMaster(fieldNumber, String.valueOf(selectedType));
                            break;

                    }
                    initButton(pnEditMode);
                }
            } catch (SQLException ex) {
                Logger.getLogger(UnitDeliveryReceiptFormController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        );
    }

    private void clearVSPFields() {
        carCategory.selectToggle(null);
        txtField22.setText("");
        txtField23.setText("");
        txtField24.setText("");
        txtField25.setText("");
        txtField26.setText("");
        txtField27.setText("");
        txtField28.setText("");
        txtField29.setText("");
        txtField34.setText("");
    }

    private void clearVSPFieldsMaster() {
        try {
            oTrans.setMaster(11, "");
            oTrans.setMaster(12, "");
            oTrans.setMaster(22, "");
            oTrans.setMaster(23, "");
            oTrans.setMaster(24, "");
            oTrans.setMaster(25, "");
            oTrans.setMaster(26, "");
            oTrans.setMaster(28, "");
            oTrans.setMaster(27, "");
            oTrans.setMaster(29, "");
            oTrans.setMaster(34, "");

        } catch (SQLException ex) {
            Logger.getLogger(UnitDeliveryReceiptFormController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void initRequiredFieldListener() {
        txtFieldAnimation.addRequiredFieldListener(txtField29);
    }

    private void loadCustomerField() {
        try {
            date02.setValue(strToDate(CommonUtils.xsDateShort((Date) oTrans.getMaster(2))));
            txtField03.setText(oTrans.getMaster(3).toString().toUpperCase());
            textArea06.setText(oTrans.getMaster(6).toString().toUpperCase());
            txtField35.setText(oTrans.getMaster(35).toString().toUpperCase());
            txtField22.setText(oTrans.getMaster(22).toString().toUpperCase());
            txtField23.setText(oTrans.getMaster(23).toString().toUpperCase());
            txtField24.setText(oTrans.getMaster(24).toString().toUpperCase());
            txtField25.setText(oTrans.getMaster(25).toString().toUpperCase());
            txtField26.setText(oTrans.getMaster(26).toString().toUpperCase());
            txtField27.setText(oTrans.getMaster(27).toString().toUpperCase());
            txtField28.setText(oTrans.getMaster(28).toString().toUpperCase());
            txtField29.setText(oTrans.getMaster(29).toString().toUpperCase());
            txtField34.setText(oTrans.getMaster(34).toString().toUpperCase());
            if (((String) oTrans.getMaster(17)).equals("0")) {
                lblUDRStatus.setText("Cancelled");
            } else {
                lblUDRStatus.setText("Active");

            }
            String selectedItem32 = oTrans.getMaster(32).toString();
            switch (selectedItem32) {
                case "0":
                    selectedItem32 = "CUSTOMER";
                    break;
                case "1":
                    selectedItem32 = "SUPPLIER";
                    break;
            }
            comboBox32.setValue(selectedItem32);
            String isVchlBrandNew = ((String) oTrans.getMaster(30));
            if (isVchlBrandNew.equals("0")) {
                radioBrandNew.setSelected(true);
            } else if (isVchlBrandNew.equals("1")) {
                radioPreOwned.setSelected(true);
            } else {
                radioBrandNew.setSelected(false);
                radioPreOwned.setSelected(false);
            }

        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }

    }

    public void getDate(ActionEvent event) {
        try {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                Date setDate = SQLUtil.toDate(date02.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE);
                oTrans.setMaster(2, setDate);

            }
        } catch (SQLException ex) {
            Logger.getLogger(UnitDeliveryReceiptFormController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*Convert Date to String*/
    private LocalDate strToDate(String val) {
        DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(val, date_formatter);
        return localDate;
    }

    private void initButton(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        btnEdit.setVisible(false);
        btnEdit.setManaged(false);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);
        btnPrint.setVisible(false);
        btnPrint.setManaged(false);
        btnCancel.setVisible(lbShow);
        btnCancel.setManaged(lbShow);
        date02.setDisable(!lbShow);
        textArea06.setDisable(!lbShow);
        txtField22.setDisable(true);
        txtField23.setDisable(true);
        txtField24.setDisable(true);
        txtField25.setDisable(true);
        txtField26.setDisable(true);
        txtField27.setDisable(true);
        txtField28.setDisable(true);
        txtField29.setDisable(!lbShow);
        comboBox32.setDisable(!lbShow);
        txtField34.setDisable(true);
        btnCancelUDR.setManaged(false);
        btnCancelUDR.setVisible(false);

        radioBrandNew.setDisable(true);
        radioPreOwned.setDisable(true);

        if (fnValue == EditMode.ADDNEW) {
            switch (comboBox32.getSelectionModel().getSelectedIndex()) {
                case 0:
                    txtField29.setDisable(false);
                    break;
                case 1:
                    removeRequiredField();
                    txtField29.setDisable(true);
                    break;
            }
        }
        if (fnValue == EditMode.READY) {
            btnEdit.setVisible(true);
            btnEdit.setManaged(true);
            btnPrint.setVisible(true);
            btnPrint.setManaged(true);
        }
        if (fnValue == EditMode.UPDATE) {
            txtField29.setDisable(true);
            comboBox32.setDisable(true);
        }

        if (fnValue == EditMode.READY) {
            if (lblUDRStatus.getText().equals("Cancelled")) {
                btnCancelUDR.setVisible(false);
                btnCancelUDR.setManaged(false);
                btnEdit.setVisible(false);
                btnEdit.setManaged(false);
                btnPrint.setVisible(false);
                btnPrint.setManaged(false);
            } else {
                btnCancelUDR.setVisible(true);
                btnCancelUDR.setManaged(true);
                btnEdit.setVisible(true);
                btnEdit.setManaged(true);
                btnPrint.setVisible(true);
                btnPrint.setManaged(true);
            }
        }
    }

    private void clearFields() {
        removeRequiredField();
        date02.setValue(strToDate(CommonUtils.xsDateShort((Date) oApp.getServerDate())));
        carCategory.selectToggle(null);
        txtField03.setText("");
        textArea06.setText("");
        txtField22.setText("");
        txtField23.setText("");
        txtField24.setText("");
        txtField25.setText("");
        txtField26.setText("");
        txtField27.setText("");
        txtField28.setText("");
        txtField29.setText("");
        txtField34.setText("");
        lblUDRStatus.setText("");
        comboBox32.setValue(null);
    }

    private void removeRequiredField() {
        txtFieldAnimation.removeShakeAnimation(txtField29, txtFieldAnimation.shakeTextField(txtField29), "required-field");
    }

    private void loadUDRPrint(String sTransno) throws SQLException {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("UnitDeliveryReceiptPrint.fxml"));

            UnitDeliveryReceiptPrintController loControl = new UnitDeliveryReceiptPrintController();
            loControl.setGRider(oApp);
            loControl.setTransNox(sTransno);

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

}
