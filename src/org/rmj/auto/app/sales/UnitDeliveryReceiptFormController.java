/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.auto.app.sales;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
        txtField29.setOnKeyPressed(this::txtField_KeyPressed);

        txtField03.focusedProperty().addListener(txtField_Focus);
        txtField29.focusedProperty().addListener(txtField_Focus);
        comboBox30.setItems(cFormItems);
        pnEditMode = EditMode.UNKNOWN;
        initButton(pnEditMode);
    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
                if (oTrans.NewRecord()) {
                    clearFields();
                    loadCustomerField();
                    pnEditMode = oTrans.getEditMode();
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
                    return;
                }
                break;
            case "btnEdit":
                if (oTrans.UpdateRecord()) {
                    pnEditMode = oTrans.getEditMode();
                } else {
                    ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                }
                break;
            case "btnSave":

                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to save?") == true) {
                    if (comboBox30.getSelectionModel().isEmpty()) {
                        ShowMessageFX.Warning(getStage(), "Please choose a value for Customer Type", "Warning", null);
                        return;
                    }
                    if (txtField03.getText().trim().equals("")) {
                        ShowMessageFX.Warning(getStage(), "Please enter a value for Delivery Receipt No.", "Warning", null);
                        txtField03.requestFocus();
                        return;
                    }
                    if (txtField29.getText().trim().equals("")) {
                        ShowMessageFX.Warning(getStage(), "Please enter a value for VSP No.", "Warning", null);
                        txtField29.requestFocus();
                        return;
                    }
                    //Proceed Saving
                    if (oTrans.SaveRecord()) {
                        ShowMessageFX.Information(getStage(), "Transaction save successfully.", pxeModuleName, null);
                        loadCustomerField();
                        pnEditMode = EditMode.READY;
//                        if (oTrans.SearchRecord()) {
//                            loadCustomerField();
//                            pnEditMode = EditMode.READY;
//                        } else {
//                            ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
//                            clearFields();
//                            pnEditMode = EditMode.UNKNOWN;
//                        }

                    } else {
                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", "Error while saving Activity Information");
                    }

                }
                break;
            case "btnCancel":
                if (ShowMessageFX.OkayCancel(getStage(), "Are you sure you want to cancel?", pxeModuleName, null) == true) {
                    clearFields();
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnBrowse":
                break;
            case "btnPrint":
                break;
            case "btnClose": //close tab
                if (ShowMessageFX.OkayCancel(null, "Close Tab", "Are you sure you want to close this Tab?") == true) {
                    if (unload != null) {
                        unload.unloadForm(AnchorMain, oApp, pxeModuleName);
                    } else {
                        ShowMessageFX.Warning(null, "Warning", "Please notify the system administrator to configure the null value at the close button.");
                    }
                    break;
                } else {
                    return;
                }
        }
        initButton(pnEditMode);
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
                                txtField29.setText((String) oTrans.getMaster(29));
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

    private void loadCustomerField() {
        try {
            txtField03.setText((String) oTrans.getMaster(3));
            textArea06.setText((String) oTrans.getMaster(6));
            txtField22.setText((String) oTrans.getMaster(22));
            txtField23.setText((String) oTrans.getMaster(23));
            txtField24.setText((String) oTrans.getMaster(24));
            txtField25.setText((String) oTrans.getMaster(25));
            txtField26.setText((String) oTrans.getMaster(26));
            txtField27.setText((String) oTrans.getMaster(27));
            txtField28.setText((String) oTrans.getMaster(28));
            txtField29.setText((String) oTrans.getMaster(29));
//            switch (oTrans.getMaster(30).toString()) {
//                case "0":
//                    radioBrandNew.setSelected(true);
//                    break;
//                case "1":
//                    radioPreOwned.setSelected(true);
//                    break;
//                default:
//                    radioBrandNew.setSelected(false);
//                    radioPreOwned.setSelected(false);
//                    break;
//            }

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
        carCategory.selectToggle(null);
        date02.setDisable(!lbShow);
        txtField03.setDisable(!lbShow);
        textArea06.setDisable(!lbShow);
        txtField22.setDisable(!lbShow);
        txtField23.setDisable(!lbShow);
        txtField24.setDisable(!lbShow);
        txtField25.setDisable(!lbShow);
        txtField26.setDisable(!lbShow);
        txtField27.setDisable(!lbShow);
        txtField28.setDisable(!lbShow);
        txtField29.setDisable(!lbShow);

        if (fnValue == EditMode.READY) {
            btnEdit.setVisible(true);
            btnEdit.setManaged(true);
            btnPrint.setVisible(true);
            btnPrint.setManaged(true);
        }

    }

    private void clearFields() {
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
        comboBox30.setItems(null);
    }

    private Stage getStage() {
        return null;
    }
}
