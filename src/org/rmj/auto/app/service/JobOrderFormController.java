/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.auto.app.service;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.auto.app.sales.UnitDeliveryReceiptFormController;
import org.rmj.auto.app.sales.VSPFormController;
import org.rmj.auto.app.views.CancelForm;
import org.rmj.auto.app.views.InputTextFormatter;
import org.rmj.auto.app.views.ScreenInterface;
import org.rmj.auto.app.views.unloadForm;
import org.rmj.auto.clients.base.Activity;
import org.rmj.auto.service.base.JobOrderMaster;

/**
 * FXML Controller class
 *
 * @author User
 */
public class JobOrderFormController implements Initializable, ScreenInterface {

    private GRider oApp;
    private JobOrderMaster oTrans;
    private MasterCallback oListener;
    CancelForm cancelform = new CancelForm(); //Object for closing form
    private String pxeModuleName = "Job Order Information"; //Form Title
    private int pnEditMode;//Modifying fields
    private boolean pbisJobOrderSales = false;
    unloadForm unload = new unloadForm(); //Used in Close Button
    ObservableList<String> cType = FXCollections.observableArrayList("EVENT", "SALES CALL", "PROMO");

    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnClose;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnBrowse;
    @FXML
    private Button btnPrint;
    @FXML
    private TextField txtField36;
    @FXML
    private TextField txtField37;
    @FXML
    private Button btnCancelJobOrder;
    @FXML
    private TableView<?> tblViewLabor;
    @FXML
    private Tab mainTab;
    @FXML
    private Tab laborTab;
    @FXML
    private Tab partsTab;
    @FXML
    private Tab paintingsTab;
    @FXML
    private Tab recommendationTab;
    @FXML
    private TableView<?> tblViewParts;
    @FXML
    private TableView<?> tblViewPaintings;
    @FXML
    private TableView<?> tblViewRecommendation;
    @FXML
    private TextField txtField03;
    @FXML
    private DatePicker date02;
    @FXML
    private ComboBox<String> comboBox08;
    @FXML
    private TextField txtField09;
    @FXML
    private ComboBox<String> comboBox06;
    @FXML
    private TextField txtField40;
    @FXML
    private TextField txtField16;
    @FXML
    private TextField txtField31;
    @FXML
    private TextArea textArea32;
    @FXML
    private TextField txtField33;
    @FXML
    private TextField txtField39;
    @FXML
    private TextField txtField38;
    @FXML
    private TextArea textArea35;
    @FXML
    private TextArea textArea11;
    @FXML
    private TextField txtField19;
    @FXML
    private TextField txtField20;
    @FXML
    private TabPane ImTabPane;
    @FXML
    private Label lblFormTitle;
    @FXML
    private Label lblCoBuyerOrCoOwner;
    @FXML
    private Label lblJobOrderStatus;
    @FXML
    private ComboBox<String> comboBox12;
    @FXML
    private Label lblVSPorIntake;
    @FXML
    private Label lblSEorSA;
    @FXML
    private TextField txtField13;
    @FXML
    private TextField txtField21;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oListener = (int fnIndex, Object foValue) -> {
            System.out.println("Set Class Value " + fnIndex + "-->" + foValue);
        };

        oTrans = new JobOrderMaster(oApp, oApp.getBranchCode(), true); //Initialize JobOrderMaster
        oTrans.setCallback(oListener);
        oTrans.setWithUI(true);
        /*Set Capitalization Fields*/

        Platform.runLater(() -> {
            lblFormTitle.setText(getParentTabTitle());
            if (getParentTabTitle().contains("SALES")) {
                pbisJobOrderSales = true;
                lblCoBuyerOrCoOwner.setText("Co-Owner Name");
                lblVSPorIntake.setText("VSP No.");
                lblSEorSA.setText("Sales Executive");
                initButton(pnEditMode);
            } else {
                pbisJobOrderSales = false;
                lblCoBuyerOrCoOwner.setText("Co-Buyer Name");
                lblVSPorIntake.setText("Intake No.");
                lblSEorSA.setText("Service Advisor");

            }
        });
        initCapitalizationFields();

        /*Set comboBox*/
        initSetComboBoxtoJobOrderMaster();

        /*Set Combo Items*/
        initComboItems();

        /*Monitoring Inquiry Type TextField*/
        initMonitoringProperty();

        /*Set Button Click Event*/
        initCmdButton();

        /*Set TextField Key-Pressed*/
        initTextKeyPressed();

        /*Set TextField Focus*/
        initTextFieldFocus();

        /* Set Number Format*/
        initNumberFormatterFields();

        /* Set Table KeyPressed */
        initTableKeyPressed();

        /*Set TextField Required*/
        initAddRequiredField();

        changeDisplayComponents();
//        tblViewLabor.setOnMouseClicked(this::tblLabor_Clicked);
//        tblViewParts.setOnMouseClicked(this::tblParts_Clicked);
        date02.setOnAction(this::getDate);
        date02.setDayCellFactory(DateFormatCell);
        pnEditMode = EditMode.UNKNOWN;
        initButton(pnEditMode);
    }

    private void changeDisplayComponents() {
        if (pbisJobOrderSales) {

        } else {

        }

    }

    private String getParentTabTitle() {
        Node parent = AnchorMain.getParent();
        Parent tabContentParent = parent.getParent();

        if (tabContentParent instanceof TabPane) {
            TabPane tabPane = (TabPane) tabContentParent;
            Tab tab = findTabByContent(tabPane, AnchorMain);
            if (tab != null) {
                pxeModuleName = tab.getText();
                return tab.getText().toUpperCase();
            }
        }

        return null; // No parent Tab found
    }

    private Tab findTabByContent(TabPane tabPane, Node content) {
        for (Tab tab : tabPane.getTabs()) {
            if (tab.getContent() == content) {
                return tab;
            }
        }
        return null;
    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private Stage getStage() {
//        return (Stage) textSeek01.getScene().getWindow();
        return null;
    }

    /*Set Date Value to Master Class*/
    public void getDate(ActionEvent event) {
        try {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                oTrans.setMaster(2, SQLUtil.toDate(date02.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
            }
        } catch (SQLException ex) {
            Logger.getLogger(JobOrderFormController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*Convert Date to String*/
    private LocalDate strToDate(String val) {
        DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(val, date_formatter);
        return localDate;
    }
    private Callback<DatePicker, DateCell> DateFormatCell = (final DatePicker param) -> new DateCell() {
        @Override
        public void updateItem(LocalDate item, boolean empty) {
            super.updateItem(item, empty);
            LocalDate minDate = strToDate(CommonUtils.xsDateShort((Date) oApp.getServerDate())).minusDays(0);
            setDisable(empty || item.isBefore(minDate));
        }
    };

    private void initCapitalizationFields() {

        /* textField to capital letters.*/
        setTextFieldCapital(txtField40);
        setTextFieldCapital(txtField16);
        setTextFieldCapital(txtField31);
        setTextFieldCapital(txtField40);
        setTextFieldCapital(txtField33);
        setTextFieldCapital(txtField13);
        setTextFieldCapital(txtField03);
        setTextFieldCapital(txtField37);
        setTextFieldCapital(txtField36);
        setTextFieldCapital(txtField38);
        setTextFieldCapital(txtField39);

        /* textArea to capital letters.*/
        setTextAreaCapital(textArea32);
        setTextAreaCapital(textArea35);
        setTextAreaCapital(textArea11);

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

    private void initSetComboBoxtoJobOrderMaster() {

    }

    private void initComboItems() {

    }

    private void initMonitoringProperty() {

    }

    private void initCmdButton() {
        btnAdd.setOnAction(this::cmdButton_Click);
        btnEdit.setOnAction(this::cmdButton_Click);
        btnSave.setOnAction(this::cmdButton_Click);
        btnCancel.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click);
        btnBrowse.setOnAction(this::cmdButton_Click);
        btnPrint.setOnAction(this::cmdButton_Click);
    }

    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
                if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                    if (ShowMessageFX.OkayCancel(null, "Confirmation", "You have unsaved data. Are you sure you want to create a new record?") == true) {
                        clearFields();
                    } else {
                        return;
                    }
                }
                clearFields();
                oTrans.setFormType(pbisJobOrderSales);
                if (oTrans.NewRecord()) {
                    switchToTab(mainTab, ImTabPane);
                    clearFields();
                    pnEditMode = oTrans.getEditMode();
                } else {
                    ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                }
                break;
            case "btnEdit":
                if (oTrans.UpdateRecord()) {
                    pnEditMode = oTrans.getEditMode();
                    loadJobOrderFields();
                    initButton(pnEditMode);
                } else {
                    ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                }
                break;
            case "btnSave":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to save?") == true) {
                    if (pbisJobOrderSales) {
                        if (txtField13.getText().isEmpty()) {
                            ShowMessageFX.Warning(getStage(), "Please choose a value for VSP No.", "Warning", null);
                            txtField13.requestFocus();
                            return;
                        }
                    } else {
                        if (txtField13.getText().isEmpty()) {
                            ShowMessageFX.Warning(getStage(), "Please choose a value for Intake No.", "Warning", null);
                            txtField13.requestFocus();
                            return;
                        }
                    }
                }
                oTrans.setFormType(pbisJobOrderSales);
                if (oTrans.SaveRecord()) {
                    ShowMessageFX.Information(getStage(), "Transaction save successfully.", pxeModuleName, null);
                    loadJobOrderFields();
                    pnEditMode = EditMode.READY;
                    initButton(pnEditMode);
                } else {
                    ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", "Error while saving " + pxeModuleName + ".");
                }
                break;
            case "btnCancel":
                if (ShowMessageFX.OkayCancel(getStage(), "Are you sure you want to cancel?", pxeModuleName, null) == true) {
                    clearFields();
                    switchToTab(mainTab, ImTabPane);// Load fields, clear them, and set edit mode
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnBrowse":
                if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                    if (ShowMessageFX.OkayCancel(null, "Confirmation", "You have unsaved data. Are you sure you want to browse a new record?")) {
                        switchToTab(mainTab, ImTabPane);
                        pnEditMode = EditMode.READY;
                    } else {
                        return;
                    }
                }
                try {
                    clearFields();
                    switchToTab(mainTab, ImTabPane);
                    if (oTrans.SearchRecord()) {
                        loadJobOrderFields();
                        switchToTab(mainTab, ImTabPane);
                        pnEditMode = EditMode.READY;
                        initButton(pnEditMode);
                    } else {
                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                        clearFields();
                        switchToTab(mainTab, ImTabPane);
                        pnEditMode = EditMode.UNKNOWN;

                    }
                } catch (SQLException ex) {
                    Logger.getLogger(VSPFormController.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case "btnPrint":
                break;
            case "btnClose":
                if (ShowMessageFX.OkayCancel(null, "Close Tab", "Are you sure you want to close this Tab?")) {
                    if (unload != null) {
                        unload.unloadForm(AnchorMain, oApp, pxeModuleName);
                    } else {
                        ShowMessageFX.Warning(null, "Warning", "Please notify the system administrator to configure the null value at the close button.");
                    }
                }
                break;
            case "btnCancelJobOrder":
                break;
        }
        initButton(pnEditMode);

    }

    private void switchToTab(Tab tab, TabPane tabPane) {
        tabPane.getSelectionModel().select(tab);
    }

    private void initTextKeyPressed() {
        /* TxtField KeyPressed */
        txtField13.setOnKeyPressed(this::txtField_KeyPressed);
        txtField40.setOnKeyPressed(this::txtField_KeyPressed);
        txtField03.setOnKeyPressed(this::txtField_KeyPressed);
        comboBox08.setOnKeyPressed(this::txtField_KeyPressed);
        txtField16.setOnKeyPressed(this::txtField_KeyPressed);
        txtField09.setOnKeyPressed(this::txtField_KeyPressed);
        comboBox12.setOnKeyPressed(this::txtField_KeyPressed);
    }

    private void txtField_KeyPressed(KeyEvent event) {
        String txtFieldID = ((TextField) event.getSource()).getId();
        if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
            switch (txtFieldID) {
                case "txtField13":
                try {
                    if (pbisJobOrderSales) {
                        if (oTrans.searchVSP(txtField13.getText())) {
                            loadJobOrderFields();
                        }
                    } else {
                        if (oTrans.searchIntake(txtField13.getText())) {
                            loadJobOrderFields();
                        }
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(JobOrderFormController.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
            event.consume();
            CommonUtils.SetNextFocus((TextField) event.getSource());
        } else if (event.getCode() == KeyCode.UP) {
            event.consume();
            CommonUtils.SetPreviousFocus((TextField) event.getSource());
        }

    }

    private void initTextFieldFocus() {
        txtField13.focusedProperty().addListener(txtField_Focus);
        txtField40.focusedProperty().addListener(txtField_Focus);
        txtField09.focusedProperty().addListener(txtField_Focus);
        txtField16.focusedProperty().addListener(txtField_Focus);
        txtField31.focusedProperty().addListener(txtField_Focus);

        textArea11.focusedProperty().addListener(txtArea_Focus);
        textArea35.focusedProperty().addListener(txtArea_Focus);

    }
    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {

        TextField txtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        if (lsValue == null) {
            return;
        }
        if (!nv) {
            switch (lnIndex) {

            }
        } else {
            txtField.selectAll();
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
                    case 11:        //sRemarkx
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
        Pattern numberOnlyPattern = Pattern.compile("[0-9,.]*");
        txtField09.setTextFormatter(new InputTextFormatter(numberOnlyPattern));
    }

    private void initTableKeyPressed() {

    }

    private boolean loadJobOrderFields() {
        try {
            if (!oTrans.computeAmount()) {
                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                return false;
            }
            DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
            txtField13.setText((String) oTrans.getMaster(13));
            txtField03.setText((String) oTrans.getMaster(3));
            date02.setValue(strToDate(CommonUtils.xsDateShort((Date) oTrans.getMaster(2))));
            txtField40.setText((String) oTrans.getMaster(40));
            txtField16.setText((String) oTrans.getMaster(16));
            txtField09.setText(String.valueOf(decimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMaster(9))))));
            txtField31.setText((String) oTrans.getMaster(31));
            textArea32.setText((String) oTrans.getMaster(32));
            if (pbisJobOrderSales) {
                txtField33.setText((String) oTrans.getMaster(33));
            } else {
                txtField33.setText((String) oTrans.getMaster(34));
            }

            if (((String) oTrans.getMaster(25)).equals("0")) {
                lblJobOrderStatus.setText("Cancelled");
            } else {
                lblJobOrderStatus.setText("Active");
            }

            txtField37.setText((String) oTrans.getMaster(37));
            txtField36.setText((String) oTrans.getMaster(36));
            txtField39.setText((String) oTrans.getMaster(39));
            txtField38.setText((String) oTrans.getMaster(38));
            textArea35.setText((String) oTrans.getMaster(35));
            textArea11.setText((String) oTrans.getMaster(11));

            txtField19.setText(String.valueOf(decimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMaster(19))))));
            txtField20.setText(String.valueOf(decimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMaster(20))))));
            txtField21.setText(String.valueOf(decimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMaster(21))))));

        } catch (SQLException ex) {
            Logger.getLogger(JobOrderFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    private void clearFields() {
        removeRequired();
        txtField13.setText("");
        txtField03.setText("");
        date02.setValue(strToDate(CommonUtils.xsDateShort((Date) oApp.getServerDate())));
        txtField40.setText("");
        txtField16.setText("");
        txtField09.setText("0.0");
        txtField31.setText("");
        textArea32.setText("");
        txtField33.setText("");
        txtField37.setText("");
        txtField36.setText("");
        txtField39.setText("");
        txtField38.setText("");
        textArea35.setText("");
        textArea11.setText("");

        txtField19.setText("0.00");
        txtField20.setText("0.00");
        txtField21.setText("0.00");

    }

    private void initButton(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

        btnAdd.setManaged(!lbShow);
        btnEdit.setVisible(false);
        btnEdit.setManaged(false);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);
        btnCancel.setVisible(lbShow);
        btnCancel.setManaged(lbShow);
        btnPrint.setVisible(false);
        btnPrint.setManaged(false);
        btnCancelJobOrder.setManaged(false);
        btnCancelJobOrder.setVisible(false);

        txtField13.setDisable(!lbShow);
        txtField40.setDisable(!lbShow);
        txtField16.setDisable(!lbShow);
        txtField09.setDisable(!lbShow);
        textArea11.setDisable(!lbShow);

        if (fnValue == EditMode.READY) {
            if (lblJobOrderStatus.getText().equals("Cancelled")) {
                btnEdit.setVisible(false);
                btnEdit.setManaged(false);
                btnPrint.setVisible(false);
                btnPrint.setManaged(false);
            } else {
                btnEdit.setVisible(true);
                btnEdit.setManaged(true);
                btnPrint.setVisible(true);
                btnPrint.setManaged(true);

            }
        }
        if (fnValue == EditMode.UPDATE) {
            txtField13.setDisable(true);
        }
        if (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE) {
            if (pbisJobOrderSales) {
                txtField40.setDisable(true);
                txtField03.setDisable(true);
                comboBox08.setDisable(true);
                txtField16.setDisable(true);
                txtField09.setDisable(true);
                comboBox12.setDisable(true);
            } else {
                txtField40.setDisable(false);
                txtField03.setDisable(false);
                comboBox08.setDisable(false);
                txtField16.setDisable(false);
                txtField09.setDisable(false);
                comboBox12.setDisable(false);
            }
        }
    }

    private void initAddRequiredField() {
        addRequiredFieldListener(txtField13);

    }

    //Validation
    private void addRequiredFieldListener(TextField textField) {
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && textField.getText().isEmpty() || !newValue && textField.getText().equals(Double.valueOf("0.00"))) {
                shakeTextField(textField);
                textField.getStyleClass().add("required-field");
            } else {
                textField.getStyleClass().remove("required-field");
            }
        });
    }

    //TextFieldAnimation
    private void shakeTextField(TextField textField) {
        Timeline timeline = new Timeline();
        double originalX = textField.getTranslateX();

        // Add keyframes for the animation
        KeyFrame keyFrame1 = new KeyFrame(Duration.millis(0), new KeyValue(textField.translateXProperty(), 0));
        KeyFrame keyFrame2 = new KeyFrame(Duration.millis(100), new KeyValue(textField.translateXProperty(), -5));
        KeyFrame keyFrame3 = new KeyFrame(Duration.millis(200), new KeyValue(textField.translateXProperty(), 5));
        KeyFrame keyFrame4 = new KeyFrame(Duration.millis(300), new KeyValue(textField.translateXProperty(), -5));
        KeyFrame keyFrame5 = new KeyFrame(Duration.millis(400), new KeyValue(textField.translateXProperty(), 5));
        KeyFrame keyFrame6 = new KeyFrame(Duration.millis(500), new KeyValue(textField.translateXProperty(), -5));
        KeyFrame keyFrame7 = new KeyFrame(Duration.millis(600), new KeyValue(textField.translateXProperty(), 5));
        KeyFrame keyFrame8 = new KeyFrame(Duration.millis(700), new KeyValue(textField.translateXProperty(), originalX));

        // Add keyframes to the timeline
        timeline.getKeyFrames().addAll(
                keyFrame1, keyFrame2, keyFrame3, keyFrame4, keyFrame5, keyFrame6, keyFrame7, keyFrame8
        );

        // Play the animation
        timeline.play();
    }

    public void removeRequired() {
        txtField13.getStyleClass().remove("required-field");

    }

}
