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
import org.rmj.appdriver.constants.EditMode;
import org.rmj.auto.app.views.ScreenInterface;
import org.rmj.auto.sales.base.VehicleSalesProposalMaster;

/**
 * FXML Controller class
 *
 * @author User
 */
public class VSPLaborEntryDialogController implements Initializable, ScreenInterface {

    private int nRow;
    private String lbrDsc;
    private boolean additional;
    private final String pxeModuleName = "Vsp Labor Entry Form";
    private int pnEditMode;//Modifying fields
    private VehicleSalesProposalMaster oTrans;
    ObservableList<String> cChargeType = FXCollections.observableArrayList("FREE", "CHARGE");
    private GRider oApp;
    @FXML
    private Button btnAddLabor;
    @FXML
    private Button btnEditLabor;
    @FXML
    private Button btnCloseLabor;
    @FXML
    private TextField txtField07_Labor;
    @FXML
    private TextField txtField04_Labor;
    @FXML
    private Button btnCancelLabor;
    @FXML
    private ComboBox<String> comboBox5;

    /**
     * Initializes the controller class.
     */
    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        loadVSPLaborField();
        btnCloseLabor.setOnAction(this::cmdButton_Click);
        btnCancelLabor.setOnAction(this::cmdButton_Click);
        btnAddLabor.setOnAction(this::cmdButton_Click);
        btnEditLabor.setOnAction(this::cmdButton_Click);
        txtField07_Labor.setOnKeyPressed(this::txtField_KeyPressed);
        txtField07_Labor.focusedProperty().addListener(txtField_Focus);
        txtField04_Labor.focusedProperty().addListener(txtField_Focus);
        comboBox5.setItems(cChargeType);
        handleComboBoxSelectionVSPMaster(comboBox5, 25);
        setCapsLockBehavior(txtField07_Labor);
        pnEditMode = EditMode.UNKNOWN;
        initButton(pnEditMode);
    }

    private void handleComboBoxSelectionVSPMaster(ComboBox<String> comboBox, int fieldNumber) {
        comboBox.setOnAction(e -> {
            try {
                int selectedType = comboBox.getSelectionModel().getSelectedIndex(); // Retrieve the selected type
                if (selectedType >= 0) {
                    switch (fieldNumber) {
                        case 5:
//                            oTrans.setVSPLaborDetail(fnRow, fieldNumber, Integer.valueOf(comboBox.getValue()));
                            break;
                        default:
                            oTrans.setMaster(fieldNumber, String.valueOf(selectedType));
                            break;
                    }
                    // Pass the selected type to setMaster method
                    initButton(pnEditMode);

                }
            } catch (SQLException ex) {
                Logger.getLogger(VSPFormController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        );
    }

    public void setObject(VehicleSalesProposalMaster foValue) {
        oTrans = foValue;
    }

    public void setRow(int fnRow) {
        nRow = fnRow;
    }

    public void setLbrDsc(String fsLbDsc) {
        lbrDsc = fsLbDsc;
    }

    public void isAdditional(boolean additional) {
        additional = additional;
    }

    private static void setCapsLockBehavior(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (textField.getText() != null) {
                textField.setText(newValue.toUpperCase());
            }
        });
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
                    case 7:
                        oTrans.setVSPLaborDetail(nRow, lnIndex, lsValue);
                    case 4:
                        if (lsValue.isEmpty()) {
                            lsValue = "0.00";
                        }
                        oTrans.setVSPLaborDetail(nRow, lnIndex, Double.valueOf(lsValue.replace(",", "")));
                        break;

                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(VSPFormController.class
                    .getName()).log(Level.SEVERE, null, ex);

        }
    };

    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAddLabor":

                CommonUtils.closeStage(btnAddLabor);
                break;
            case "btnSaveLabor":
                initLaborDescript("", Double.valueOf("0.00"), false);
                break;
            case "btnEditLabor":
                if (oTrans.UpdateRecord()) {
                    pnEditMode = oTrans.getEditMode();
                } else {
                    ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                }
                break;
            case "btnCancelLabor":
                if (ShowMessageFX.OkayCancel(getStage(), "Are you sure you want to cancel?", pxeModuleName, null) == true) {
                    CommonUtils.closeStage(btnCancelLabor);
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnCloseLabor":
                CommonUtils.closeStage(btnCloseLabor);
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;
        }
        initButton(pnEditMode);

    }

    private void initLaborDescript(String laborDescript, double fdblAmt, boolean withLabor) {
        if (oTrans.addVSPLabor(laborDescript, withLabor)) {
            try {
                oTrans.setVSPLaborDetail(oTrans.getVSPLaborCount(), "nLaborAmt", fdblAmt);

            } catch (SQLException ex) {
                Logger.getLogger(VSPFormController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", "Error while adding labor.");
        }

    }

    public static String formatAmount(String amountString) {
        DecimalFormat numFormat = new DecimalFormat("#,##0.00");
        String formattedAmount = "";
        if (amountString.contains("0.00") || amountString.isEmpty()) {
            formattedAmount = "";
        } else {
            double amount = Double.parseDouble(amountString);
            formattedAmount = numFormat.format(amount);
        }
        return formattedAmount;
    }

    private void loadVSPLaborField() {
        try {
            for (int i = 1; i <= oTrans.getVSPLaborCount(); i++) {
                txtField07_Labor.setText(oTrans.getVSPLaborDetail(i, 7).toString());
                comboBox5.getSelectionModel().select(Integer.parseInt(oTrans.getVSPLaborDetail(i, 5).toString()));
                String currentLabrAmount = oTrans.getVSPLaborDetail(i, 4).toString();
                txtField04_Labor.setText(formatAmount(currentLabrAmount));
            }

        } catch (SQLException ex) {
            Logger.getLogger(VSPLaborEntryDialogController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void txtField_KeyPressed(KeyEvent event) {
        String txtFieldID = ((TextField) event.getSource()).getId();
        try {
            if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
                switch (txtFieldID) {
                    case "txtField07_Labor":
                        if (oTrans.searchLabor("", nRow, false)) {
                            loadVSPLaborField();
                        } else {
                            txtField07_Labor.clear();
                            txtField07_Labor.requestFocus();
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

    private void initButton(int fnValue) {

        btnEditLabor.setVisible(false);
        btnEditLabor.setManaged(false);
    }

    private Stage getStage() {
        return (Stage) txtField07_Labor.getScene().getWindow();
    }
}
