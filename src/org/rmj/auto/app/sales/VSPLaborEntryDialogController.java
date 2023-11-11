/* * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template */package org.rmj.auto.app.sales;import java.net.URL;import java.sql.SQLException;import java.text.DecimalFormat;import java.util.ResourceBundle;import java.util.logging.Level;import java.util.logging.Logger;import java.util.regex.Pattern;import javafx.beans.property.ReadOnlyBooleanPropertyBase;import javafx.beans.value.ChangeListener;import javafx.collections.FXCollections;import javafx.collections.ObservableList;import javafx.event.ActionEvent;import javafx.fxml.FXML;import javafx.fxml.Initializable;import javafx.scene.control.Button;import javafx.scene.control.CheckBox;import javafx.scene.control.ComboBox;import javafx.scene.control.TextField;import javafx.scene.input.KeyCode;import javafx.scene.input.KeyEvent;import javafx.stage.Stage;import org.rmj.appdriver.GRider;import org.rmj.appdriver.agentfx.CommonUtils;import org.rmj.appdriver.agentfx.ShowMessageFX;import org.rmj.auto.app.views.InputTextFormatter;import org.rmj.auto.app.views.ScreenInterface;import org.rmj.auto.sales.base.VehicleSalesProposalMaster;public class VSPLaborEntryDialogController implements Initializable, ScreenInterface {    private Boolean lbrDsc;    private int pnRow = 0;    private boolean pbState = true;    private final String pxeModuleName = "Vsp Labor Entry Form";    private String psOrigDsc = "";    private VehicleSalesProposalMaster oTrans;    ObservableList<String> cChargeType = FXCollections.observableArrayList("FREE OF CHARGE", "CHARGE");    private GRider oApp;    @FXML    private Button btnAddLabor;    @FXML    private Button btnEditLabor;    @FXML    private Button btnCloseLabor;    @FXML    private TextField txtField07_Labor;    @FXML    private TextField txtField04_Labor;    @FXML    private ComboBox<String> comboBox5;    @FXML    private CheckBox isAdditionalChecklBox;    /**     * Initializes the controller class.     */    @Override    public void setGRider(GRider foValue) {        oApp = foValue;    }    @Override    public void initialize(URL url, ResourceBundle rb) {        btnCloseLabor.setOnAction(this::cmdButton_Click);        btnAddLabor.setOnAction(this::cmdButton_Click);        btnEditLabor.setOnAction(this::cmdButton_Click);        txtField07_Labor.setOnKeyPressed(this::txtField_KeyPressed);        txtField04_Labor.setOnKeyPressed(this::txtField_KeyPressed);        comboBox5.setItems(cChargeType);        handleComboBoxSelectionVSPMaster(comboBox5, 5);        setCapsLockBehavior(txtField07_Labor);        txtField04_Labor.focusedProperty().addListener(txtField_Focus);        loadVSPLaborField();        initNumberFormatterFields();        if (pbState) {            btnAddLabor.setVisible(true);            btnAddLabor.setManaged(true);            btnEditLabor.setVisible(false);            btnEditLabor.setManaged(false);        } else {            btnAddLabor.setVisible(false);            btnAddLabor.setManaged(false);            btnEditLabor.setVisible(true);            btnEditLabor.setManaged(true);        }    }    private void initNumberFormatterFields() {        Pattern pattern = Pattern.compile("[0-9,.]*");        txtField04_Labor.setTextFormatter(new InputTextFormatter(pattern));    }    public void setObject(VehicleSalesProposalMaster foValue) {        oTrans = foValue;    }    public void setRow(int fnRow) {        pnRow = fnRow;    }    public void setLbrDsc(Boolean fbValue) {        lbrDsc = fbValue;    }    public void isAdditional(boolean additional) {        additional = additional;    }    public void setState(boolean fbValue) {        pbState = fbValue;    }    public void setOrigDsc(String fsValue) {        psOrigDsc = fsValue;    }    private void handleComboBoxSelectionVSPMaster(ComboBox<String> comboBox, int fieldNumber) {        comboBox.setOnAction(e -> {            int selectedType = comboBox.getSelectionModel().getSelectedIndex(); // Retrieve the selected type            if (selectedType >= 0) {                switch (fieldNumber) {                    case 5:                        if (selectedType == 0) {                            txtField04_Labor.setText("0.00");                            txtField04_Labor.setDisable(true);                        } else {                            txtField04_Labor.setDisable(false);                        }                        break;                }            }        }        );    }    private static void setCapsLockBehavior(TextField textField) {        textField.textProperty().addListener((observable, oldValue, newValue) -> {            if (textField.getText() != null) {                textField.setText(newValue.toUpperCase());            }        });    }    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {        TextField txtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));        String lsValue = txtField.getText();        if (lsValue == null) {            return;        }        if (!nv) {            /* Lost Focus */            switch (lnIndex) {                case 4:                    if (lsValue.isEmpty()) {                        lsValue = "0.00";                    }                    String currentLaborsAmount = lsValue;// oTrans.getVSPLaborDetail(pnRow, 4).toString();                    txtField04_Labor.setText(formatAmount(currentLaborsAmount));                    break;            }        }    };    private void cmdButton_Click(ActionEvent event) {        try {            String lsButton = ((Button) event.getSource()).getId();            switch (lsButton) {                case "btnEditLabor":                case "btnAddLabor":                    if (settoClass()) {                        CommonUtils.closeStage(btnCloseLabor);                    } else {                        return;                    }                    break;                case "btnCloseLabor":                    if (pbState) {                        if (oTrans.getVSPLaborDetail(pnRow, 1).toString().isEmpty()) {                            oTrans.removeVSPLabor(pnRow);                        }                    } else {                        if (oTrans.searchLabor(psOrigDsc, pnRow, false)) {                        } else {                            ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());                            return;                        }                    }                    CommonUtils.closeStage(btnCloseLabor);                    break;                default:                    ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");                    break;            }        } catch (SQLException ex) {            Logger.getLogger(VSPLaborEntryDialogController.class.getName()).log(Level.SEVERE, null, ex);        }    }    private boolean settoClass() {        try {            DecimalFormat numFormat = new DecimalFormat("###0.00");            int selectedType = comboBox5.getSelectionModel().getSelectedIndex();            if (txtField07_Labor.getText().trim().isEmpty()) {                ShowMessageFX.Warning(getStage(), "Please input Labor Description", "Warning", null);                txtField07_Labor.requestFocus();                return false;            }            if (oTrans.getVSPLaborDetail(pnRow, 3).toString().isEmpty()) {                ShowMessageFX.Warning(getStage(), "Please input Labor Description", "Warning", null);                txtField07_Labor.requestFocus();                return false;            }            if (comboBox5.getSelectionModel().getSelectedIndex() == -1) {                ShowMessageFX.Warning(getStage(), "Please select Charge Type", "Warning", null);                comboBox5.requestFocus();                return false;            }            if (selectedType == 1) {                String laborAmount = txtField04_Labor.getText().replace(",", ""); // Remove commas from the input string                try {                    double amount = Double.parseDouble(laborAmount);                    if (amount == 0.00 || amount < 0.00) {                        ShowMessageFX.Warning(getStage(), "Please input Labor Amount", "Warning", null);                        txtField04_Labor.requestFocus();                        return false;                    }                } catch (NumberFormatException e) {                    // Handle the case where laborAmount is not a valid number                    ShowMessageFX.Warning(getStage(), "Invalid Labor Amount", "Warning", null);                    txtField04_Labor.requestFocus();                    return false;                }            }            oTrans.setVSPLaborDetail(pnRow, 5, String.valueOf(selectedType));            oTrans.setVSPLaborDetail(pnRow, 4, numFormat.format(Double.valueOf(txtField04_Labor.getText().replace(",", ""))));        } catch (SQLException ex) {            Logger.getLogger(VSPLaborEntryDialogController.class.getName()).log(Level.SEVERE, null, ex);        }        return true;    }    public static String formatAmount(String amountString) {        DecimalFormat numFormat = new DecimalFormat("#,##0.00");        String formattedAmount = "";        if (amountString.isEmpty()) {            formattedAmount = "";        } else {            try {                double amount = Double.parseDouble(amountString);                if (amount == 0.00 || amount < 0.00) {                    formattedAmount = "";                } else {                    formattedAmount = numFormat.format(amount);                }            } catch (NumberFormatException e) {                // Handle the case where input is not a valid number                formattedAmount = "";            }        }        return formattedAmount;    }    private void loadVSPLaborField() {        try {            txtField07_Labor.setText(oTrans.getVSPLaborDetail(pnRow, 7).toString());            if (oTrans.getVSPLaborDetail(pnRow, 8).equals("0")) {                isAdditionalChecklBox.setSelected(false);                txtField07_Labor.setDisable(true);            } else {                isAdditionalChecklBox.setSelected(true);            }            if ((!oTrans.getVSPLaborDetail(pnRow, "sTransNox").toString().isEmpty())) {                txtField07_Labor.setDisable(true);            }            String selectedItem5 = oTrans.getVSPLaborDetail(pnRow, "sChrgeTyp").toString();            switch (selectedItem5) {                case "0":                    selectedItem5 = "FREE OF CHARGE";                    txtField04_Labor.setDisable(true);                    break;                case "1":                    selectedItem5 = "CHARGE";                    txtField04_Labor.setDisable(false);                    break;            }            comboBox5.setValue(selectedItem5);            String currentLabrAmount = oTrans.getVSPLaborDetail(pnRow, 4).toString();            txtField04_Labor.setText(formatAmount(currentLabrAmount));        } catch (SQLException ex) {            Logger.getLogger(VSPLaborEntryDialogController.class                    .getName()).log(Level.SEVERE, null, ex);        }    }    private void txtField_KeyPressed(KeyEvent event) {        String txtFieldID = ((TextField) event.getSource()).getId();        try {            if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {                switch (txtFieldID) {                    case "txtField07_Labor":                        if (oTrans.searchLabor("", pnRow, true)) {                            loadVSPLaborField();                        } else {                            txtField07_Labor.clear();                            txtField07_Labor.requestFocus();                            ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);                        }                        break;                }                event.consume();                CommonUtils.SetNextFocus((TextField) event.getSource());            } else if (event.getCode() == KeyCode.UP) {                event.consume();                CommonUtils.SetPreviousFocus((TextField) event.getSource());            }        } catch (SQLException e) {            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);        }    }    private Stage getStage() {        return (Stage) txtField07_Labor.getScene().getWindow();    }}