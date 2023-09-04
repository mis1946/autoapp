/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.auto.app.sales;

import java.net.URL;
import java.sql.SQLException;
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
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
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
import javafx.util.Callback;
import javafx.util.Duration;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.auto.app.views.CustomerFormController;
import org.rmj.auto.app.views.DateCellDisabler;
import org.rmj.auto.app.views.InputTextFormatter;
import org.rmj.auto.app.views.ScreenInterface;
import org.rmj.auto.app.views.unloadForm;
import org.rmj.auto.sales.base.VehicleDeliveryReceipt;

/**
 * Unit Delivery Receipt Form Controller class
 *
 * @author John Dave
 */
public class UnitDeliveryReceiptFormController implements Initializable, ScreenInterface {

    private VehicleDeliveryReceipt oTrans;
    private GRider oApp;
    private MasterCallback oListener;
    unloadForm unload = new unloadForm();
    private final String pxeModuleName = "Unit Delivery Receipt"; //Form Title
    private int pnEditMode;//Modifying fields
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
    private ComboBox<String> comboBox30;
    @FXML
    private TextField txtField14;
    @FXML
    private TextField txtField15;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        oTrans = new VehicleDeliveryReceipt(oApp, oApp.getBranchCode(), true);
        oTrans.setCallback(oListener);
        oTrans.setWithUI(true);
        btnBrowse.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click);
        btnAdd.setOnAction(this::cmdButton_Click);
        btnSave.setOnAction(this::cmdButton_Click);
        btnEdit.setOnAction(this::cmdButton_Click);
        btnCancel.setOnAction(this::cmdButton_Click);
        btnPrint.setOnAction(this::cmdButton_Click);

        Pattern pattern;
        pattern = Pattern.compile("[0-9]*");
        txtField03.setTextFormatter(new InputTextFormatter(pattern));
        CommonUtils.addTextLimiter(txtField03, 12);
        txtField29.setOnKeyPressed(this::txtField_KeyPressed);

        textArea06.focusedProperty().addListener(txtArea_Focus);

        addRequiredFieldListener(txtField03);
        addRequiredFieldListener(txtField29);

        txtField03.focusedProperty().addListener(txtField_Focus);
        txtField29.focusedProperty().addListener(txtField_Focus);

        date02.setOnAction(this::getDate);
        int daysToDisable = 30;
        date02.setDayCellFactory(DateCellDisabler.createDisableDateCallback(daysToDisable));

        comboBox30.setItems(cFormItems);
        pnEditMode = EditMode.UNKNOWN;
        initButton(pnEditMode);
    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    //Animation
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

    //Validation
    private void addRequiredFieldListener(TextField textField) {
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && textField.getText().isEmpty()) {
                shakeTextField(textField);
                textField.getStyleClass().add("required-field");
            } else {
                textField.getStyleClass().remove("required-field");
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
            if (comboBox30.getSelectionModel().isEmpty()) {
                ShowMessageFX.Warning(getStage(), "Please choose a value for Customer Type", "Warning", null);
                return;
            }
            if (txtField03.getText().trim().equals("")) {
                ShowMessageFX.Warning(getStage(), "Please enter a value for Unit Delivery Receipt No.", "Warning", null);
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
                loadCustomerField();
                pnEditMode = EditMode.READY;
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

    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        String txtFieldID = ((TextField) event.getSource()).getId();
        try {
            switch (event.getCode()) {
                case F3:
                case TAB:
                case ENTER:
                    switch (txtFieldID) {
                        case "txtField29":
                            if (oTrans.searchVSP(txtField.getText())) {
                                loadCustomerField();
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

    public void getDate(ActionEvent event) {
        try {
            Date setDate = SQLUtil.toDate(date02.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE);
            oTrans.setMaster(2, setDate);
        } catch (SQLException ex) {
            Logger.getLogger(UnitDeliveryReceiptFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*Convert Date to String*/
    private LocalDate strToDate(String val) {
        DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(val, date_formatter);
        return localDate;
    }
    //Moved to own class just call DateCellDisabler -jahn 08292023
    
//    private Callback<DatePicker, DateCell> disableDate = (final DatePicker param) -> {
//        return new DateCell() {
//            @Override
//            public void updateItem(LocalDate item, boolean empty) {
//                super.updateItem(item, empty);
//                if (item == null || empty) {
//                    setDisable(true);
//                    return;
//                }
//                Date serverDate = oApp.getServerDate();
//
//                LocalDate minDate = strToDate(CommonUtils.xsDateShort(serverDate));
//                LocalDate maxDate = strToDate(CommonUtils.xsDateShort(serverDate));
//
//                maxDate = maxDate.plusDays(30);
//
//                setDisable(item.isBefore(minDate) || item.isAfter(maxDate));
//
//            }
//        };
//    };
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
                    case 3:
                    case 14:
                    case 29:
                        oTrans.setMaster(lnIndex, lsValue); // Handle Encoded Value
                        break;

                }

            } else {
                txtField.selectAll();

            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerFormController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    };
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

    private void loadCustomerField() {
        try {
            date02.setValue(strToDate(CommonUtils.xsDateShort((Date) oTrans.getMaster(2))));
            txtField03.setText((String) oTrans.getMaster(3));
            textArea06.setText((String) oTrans.getMaster(6));
            txtField14.setText((String) oTrans.getMaster(14));
            txtField22.setText((String) oTrans.getMaster(22));
            txtField23.setText((String) oTrans.getMaster(23));
            txtField24.setText((String) oTrans.getMaster(24));
            txtField25.setText((String) oTrans.getMaster(25));
            txtField26.setText((String) oTrans.getMaster(26));
            txtField27.setText((String) oTrans.getMaster(27));
            txtField28.setText((String) oTrans.getMaster(28));
            txtField29.setText((String) oTrans.getMaster(29));

            String isVchlBrandNew = ((String) oTrans.getMaster(30));

            System.out.println(isVchlBrandNew);
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
        txtField03.setDisable(!lbShow);
        textArea06.setDisable(!lbShow);
        txtField14.setDisable(!lbShow);
        txtField15.setDisable(true);
        txtField22.setDisable(true);
        txtField23.setDisable(true);
        txtField24.setDisable(true);
        txtField25.setDisable(true);
        txtField26.setDisable(true);
        txtField27.setDisable(true);
        txtField28.setDisable(true);
        txtField29.setDisable(!lbShow);
        comboBox30.setDisable(!lbShow);

        radioBrandNew.setDisable(true);
        radioPreOwned.setDisable(true);

        if (fnValue == EditMode.READY) {
            btnEdit.setVisible(true);
            btnEdit.setManaged(true);
            btnPrint.setVisible(true);
            btnPrint.setManaged(true);
        }
        if (fnValue == EditMode.UPDATE) {
            txtField29.setDisable(true);
            txtField14.setDisable(true);
            txtField15.setDisable(true);
            comboBox30.setDisable(true);
        }
    }

    private void removeRequiredField() {
        txtField03.getStyleClass().remove("required-field");
        txtField29.getStyleClass().remove("required-field");
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
        comboBox30.setValue(null);
    }

    private Stage getStage() {
        return null;
    }
}
