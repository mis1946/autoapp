/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.auto.app.sales;

import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.auto.app.views.InputTextFormatter;
import org.rmj.auto.app.views.ScreenInterface;
import org.rmj.auto.sales.base.VehicleSalesProposalMaster;

public class VSPPartsDialogController implements Initializable, ScreenInterface {

    private Boolean lbrDsc;
    private int pnRow = 0;
    private boolean pbState = true;
    private String psOrigDsc = "";
    private String psStockID = "";
    private final String pxeModuleName = "Vsp Parts Entry Form";
    private VehicleSalesProposalMaster oTrans;
    ObservableList<String> cChargeType = FXCollections.observableArrayList("FREE OF CHARGE", "CHARGE");
    private GRider oApp;
    @FXML
    private TextField txtField09_Part;
    @FXML
    private ComboBox<String> comboBox8;
    @FXML
    private TextField txtField14_Part;
    @FXML
    private TextField txtField06_Part;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnClose;
    @FXML
    private TextField txtField04_Part;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnClose.setOnAction(this::cmdButton_Click);
        btnAdd.setOnAction(this::cmdButton_Click);
        btnEdit.setOnAction(this::cmdButton_Click);
        txtField04_Part.setOnKeyPressed(this::txtField_KeyPressed);
        txtField06_Part.setOnKeyPressed(this::txtField_KeyPressed);
        txtField09_Part.setOnKeyPressed(this::txtField_KeyPressed);
        comboBox8.setItems(cChargeType);
        handleComboBoxSelectionVSPMaster(comboBox8, 8);
        initNumberFormatterFields();
        initAlphabeticalFormatterFields();
        txtField09_Part.focusedProperty().addListener(txtField_Focus);
        txtField04_Part.focusedProperty().addListener(txtField_Focus);
        txtField06_Part.focusedProperty().addListener(txtField_Focus);
        setCapsLockBehavior(txtField09_Part);
        loadVSPPartsField();
        if (pbState) {
            btnAdd.setVisible(true);
            btnAdd.setManaged(true);
            btnEdit.setVisible(false);
            btnEdit.setManaged(false);
        } else {
            btnAdd.setVisible(false);
            btnAdd.setManaged(false);
            btnEdit.setVisible(true);
            btnEdit.setManaged(true);
        }
    }

    private void initNumberFormatterFields() {
        Pattern pattern = Pattern.compile("[0-9,.]*");
        txtField04_Part.setTextFormatter(new InputTextFormatter(pattern));
        txtField06_Part.setTextFormatter(new InputTextFormatter(pattern));
    }

    private void initAlphabeticalFormatterFields() {
        Pattern lettersOnlyPattern = Pattern.compile("[A-Za-z ]*");
        txtField09_Part.setTextFormatter(new InputTextFormatter(lettersOnlyPattern));
    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    public void setObject(VehicleSalesProposalMaster foValue) {
        oTrans = foValue;
    }

    public void setRow(int fnRow) {
        pnRow = fnRow;
    }

    public void isAdditional(boolean additional) {
        additional = additional;
    }

    public void setState(boolean fbValue) {
        pbState = fbValue;
    }

    public void setLbrDsc(Boolean fbValue) {
        lbrDsc = fbValue;
    }

    public void setOrigDsc(String fsValue) {
        psOrigDsc = fsValue;
    }

    public void setStockID(String fsValue) {
        psStockID = fsValue;
    }

    private void handleComboBoxSelectionVSPMaster(ComboBox<String> comboBox, int fieldNumber) {
        comboBox.setOnAction(e -> {
            try {
                int selectedType = comboBox.getSelectionModel().getSelectedIndex(); // Retrieve the selected type
                if (selectedType >= 0) {
                    switch (fieldNumber) {
                        case 8:
                            if (selectedType == 0) {
                                txtField04_Part.setText("0.00");
                                oTrans.setVSPPartsDetail(pnRow, 5, Double.valueOf("0.00"));
                                txtField04_Part.setDisable(true);
                            } else {
                                txtField04_Part.setDisable(false);
                            }
                            break;
                    }
                }

            } catch (SQLException ex) {
                Logger.getLogger(VSPPartsDialogController.class.getName()).log(Level.SEVERE, null, ex);
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

    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {

        TextField txtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        if (lsValue == null) {
            return;
        }
        if (!nv) {
            /* Lost Focus */
            switch (lnIndex) {
                case 4:
                    DecimalFormat getFormat = new DecimalFormat("#,##0.00");
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    txtField04_Part.setText(String.valueOf(getFormat.format(Double.parseDouble(String.valueOf(txtField04_Part.getText().replace(",", ""))))));
                    break;
            }
        }
    };

    private void loadVSPPartsField() {
        try {
            DecimalFormat getFormat = new DecimalFormat("#,##0.00");
            txtField09_Part.setText((String) oTrans.getVSPPartsDetail(pnRow, "sDescript"));
            if ((!oTrans.getVSPPartsDetail(pnRow, "sTransNox").toString().isEmpty())) {
                txtField09_Part.setDisable(true);
            }

            String selectedItem8 = oTrans.getVSPPartsDetail(pnRow, 8).toString();
            switch (selectedItem8) {
                case "0":
                    selectedItem8 = "FREE OF CHARGE";
                    txtField04_Part.setDisable(true);
                    break;
                case "1":
                    selectedItem8 = "CHARGE";
                    txtField04_Part.setDisable(false);
                    break;
            }
            comboBox8.setValue(selectedItem8);
            txtField06_Part.setText(String.valueOf(oTrans.getVSPPartsDetail(pnRow, 6)));
            txtField04_Part.setText(String.valueOf(getFormat.format(Double.parseDouble(String.valueOf(oTrans.getVSPPartsDetail(pnRow, 4))))));

        } catch (SQLException ex) {
            Logger.getLogger(VSPPartsDialogController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean settoClass() {
        try {
            DecimalFormat setFormat = new DecimalFormat("###0.00");
            String userQuantity = txtField06_Part.getText();
            int userQuant = Integer.parseInt(userQuantity);

            int selectedType = comboBox8.getSelectionModel().getSelectedIndex();
            if (txtField09_Part.getText().trim().isEmpty()) {
                ShowMessageFX.Warning(getStage(), "Please input Parts Description", "Warning", null);
                txtField09_Part.requestFocus();
                return false;
            }
            if (!oTrans.checkPartsExist(txtField09_Part.getText(), pbState)) {
                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                txtField09_Part.requestFocus();
                return false;
            }
            try {
                if (!oTrans.checkVSPJOParts(psStockID, userQuant)) {
                    ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                    txtField06_Part.requestFocus();
                    return false;
                }

                if (userQuant == 0 || userQuant < 0) {
                    ShowMessageFX.Warning(getStage(), "Please input Quantity amount", "Warning", null);
                    txtField06_Part.requestFocus();
                    return false;
                }
            } catch (NumberFormatException e) {
                // Handle the case where laborAmount is not a valid number
                ShowMessageFX.Warning(getStage(), "Invalid Quantity Amount", "Warning", null);
                txtField06_Part.requestFocus();
                return false;
            }
            if (comboBox8.getSelectionModel().getSelectedIndex() == -1) {
                ShowMessageFX.Warning(getStage(), "Please select Charge Type", "Warning", null);
                comboBox8.requestFocus();
                return false;
            }
            if (selectedType == 1) {
                String laborAmount = txtField04_Part.getText().replace(",", ""); // Remove commas from the input string
                try {
                    double amount = Double.parseDouble(laborAmount);
                    if (amount == 0.00 || amount < 0.00) {
                        ShowMessageFX.Warning(getStage(), "Please input Parts Amount", "Warning", null);
                        txtField04_Part.requestFocus();
                        return false;
                    }
                } catch (NumberFormatException e) {
                    // Handle the case where laborAmount is not a valid number
                    ShowMessageFX.Warning(getStage(), "Invalid Parts Amount", "Warning", null);
                    txtField04_Part.requestFocus();
                    return false;
                }
            }

            oTrans.setVSPPartsDetail(pnRow, 9, txtField09_Part.getText());
            oTrans.setVSPPartsDetail(pnRow, 8, String.valueOf(selectedType));
            oTrans.setVSPPartsDetail(pnRow, 6, userQuant);
            oTrans.setVSPPartsDetail(pnRow, 4, setFormat.format(Double.valueOf(txtField04_Part.getText().replace(",", ""))));
        } catch (SQLException ex) {
            Logger.getLogger(VSPLaborEntryDialogController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    private void txtField_KeyPressed(KeyEvent event) {
        String txtFieldID = ((TextField) event.getSource()).getId();
        if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
            switch (txtFieldID) {
                case "txtField09_Labor":
                    break;
            }
            event.consume();
            CommonUtils.SetNextFocus((TextField) event.getSource());
        } else if (event.getCode() == KeyCode.UP) {
            event.consume();
            CommonUtils.SetPreviousFocus((TextField) event.getSource());
        }
    }

    private Stage getStage() {
        return (Stage) txtField09_Part.getScene().getWindow();
    }

    private void cmdButton_Click(ActionEvent event) {
        try {
            String lsButton = ((Button) event.getSource()).getId();
            switch (lsButton) {
                case "btnEdit":
                case "btnAdd":
                    if (settoClass()) {
                        CommonUtils.closeStage(btnClose);
                    } else {
                        return;
                    }
                    break;
                case "btnClose":
                    if (pbState) {
                        if (oTrans.getVSPPartsDetail(pnRow, 1).toString().isEmpty()) {
                            oTrans.removeVSPParts(pnRow);
                        }
                    } else {
                        oTrans.setVSPPartsDetail(pnRow, 9, psOrigDsc);
                    }
                    CommonUtils.closeStage(btnClose);
                    break;
                default:
                    ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                    break;
            }
        } catch (SQLException ex) {
            Logger.getLogger(VSPPartsDialogController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
