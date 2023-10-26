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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.auto.app.views.ScreenInterface;
import org.rmj.auto.sales.base.VehicleSalesProposalMaster;

/**
 * FXML Controller class
 *
 * @author User
 */
public class VSPPartsEntryDialogController implements Initializable, ScreenInterface {

    private int pnRow = 0;
    private boolean pbState = true;
    private boolean additional;
    private final String pxeModuleName = "Vsp Parts Entry Form";
    private int pnEditMode;//Modifying fields
    private VehicleSalesProposalMaster oTrans;
    ObservableList<String> cChargeType = FXCollections.observableArrayList("FREE OF CHARGE", "CHARGE");
    private GRider oApp;
    @FXML
    private Button btnAddPart;
    @FXML
    private Button btnEditPart;
    @FXML
    private Button btnClosePart;
    @FXML
    private TextField txtField14_Parts;
    @FXML
    private ComboBox<String> comboBox8;
    @FXML
    private TextField txtField06_Parts;
    @FXML
    private TextField txtField09_Parts;
    @FXML
    private TextField txtField05_Parts;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnClosePart.setOnAction(this::cmdButton_Click);
        btnAddPart.setOnAction(this::cmdButton_Click);
        btnEditPart.setOnAction(this::cmdButton_Click);
        txtField09_Parts.setOnKeyPressed(this::txtField_KeyPressed);
        txtField05_Parts.setOnKeyPressed(this::txtField_KeyPressed);
        txtField05_Parts.focusedProperty().addListener(txtField_Focus);
        txtField06_Parts.setOnKeyPressed(this::txtField_KeyPressed);

        comboBox8.setItems(cChargeType);
        handleComboBoxSelectionVSPMaster(comboBox8, 8);
        setCapsLockBehavior(txtField09_Parts);
        loadVSPPartField();
        if (pbState) {
            btnAddPart.setVisible(true);
            btnAddPart.setManaged(true);
            btnEditPart.setVisible(false);
            btnEditPart.setManaged(false);
        } else {
            btnAddPart.setVisible(false);
            btnAddPart.setManaged(false);
            btnEditPart.setVisible(true);
            btnEditPart.setManaged(true);
        }

    }

    @Override
    public void setGRider(GRider foValue
    ) {
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

    private static void setCapsLockBehavior(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (textField.getText() != null) {
                textField.setText(newValue.toUpperCase());
            }
        });
    }

    private Stage getStage() {
        return (Stage) txtField09_Parts.getScene().getWindow();
    }

    private void handleComboBoxSelectionVSPMaster(ComboBox<String> comboBox, int fieldNumber) {
        comboBox.setOnAction(e -> {
            try {
                int selectedType = comboBox.getSelectionModel().getSelectedIndex(); // Retrieve the selected type
                if (selectedType >= 0) {
                    switch (fieldNumber) {
                        case 8:
                            oTrans.setVSPPartsDetail(pnRow, fieldNumber, String.valueOf(selectedType));
                            if (selectedType == 0) {
                                oTrans.setVSPPartsDetail(pnRow, 5, Double.valueOf("0.00"));
                                txtField05_Parts.setText("0.00");
                                txtField05_Parts.setDisable(true);
                            } else {
                                txtField05_Parts.setDisable(false);
                            }

                            break;
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(VSPPartsEntryDialogController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        );
    }

    private void loadVSPPartField() {
        try {
            txtField09_Parts.setText((String) oTrans.getVSPPartsDetail(pnRow, 9));
            String selectedItem8 = oTrans.getVSPPartsDetail(pnRow, 8).toString();
            switch (selectedItem8) {
                case "0":
                    selectedItem8 = "FREE OF CHARGE";
                    txtField05_Parts.setDisable(true);
                    break;
                case "1":
                    selectedItem8 = "CHARGE";
                    txtField05_Parts.setDisable(false);
                    break;
            }
            comboBox8.setValue(selectedItem8);
            String currenPartAmount = oTrans.getVSPPartsDetail(pnRow, 5).toString();
            txtField05_Parts.setText(formatAmount(currenPartAmount));
            txtField06_Parts.setText(String.valueOf(oTrans.getVSPPartsDetail(pnRow, 6)));

        } catch (SQLException ex) {
            Logger.getLogger(VSPPartsEntryDialogController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void cmdButton_Click(ActionEvent event) {
        try {
            String lsButton = ((Button) event.getSource()).getId();
            switch (lsButton) {
                case "btnEditPart":
                case "btnAddPart":
                    if (setClass()) {
                        CommonUtils.closeStage(btnClosePart);
                    } else {
                        return;
                    }
                    break;
                case "btnClosePart":
                    if (pbState) {
                        if (oTrans.getVSPPartsDetail(pnRow, 1).toString().isEmpty()) {
                            oTrans.removeVSPParts(pnRow);
                        }
                    }
                    CommonUtils.closeStage(btnClosePart);
                    break;
                default:
                    ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                    break;

            }
        } catch (SQLException ex) {
            Logger.getLogger(VSPLaborEntryDialogController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    static String formatAmount(String amountString) {
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
                    case 5:
                        if (lsValue.isEmpty()) {
                            lsValue = "0.00";
                        }
                        oTrans.setVSPPartsDetail(pnRow, lnIndex, Double.valueOf(lsValue.replace(",", "")));
                        loadVSPPartField();
                        break;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(VSPFormController.class
                    .getName()).log(Level.SEVERE, null, ex);

        }
    };

    private void txtField_KeyPressed(KeyEvent event) {
        String txtFieldID = ((TextField) event.getSource()).getId();
        if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
            switch (txtFieldID) {
            }
            event.consume();
            CommonUtils.SetNextFocus((TextField) event.getSource());
        } else if (event.getCode() == KeyCode.UP) {
            event.consume();
            CommonUtils.SetPreviousFocus((TextField) event.getSource());
        }
    }

    private boolean setClass() {
        try {
            String partQuantity = txtField06_Parts.getText().trim();
            if (txtField09_Parts.getText().trim().equals("")) {
                ShowMessageFX.Warning(getStage(), "Please enter a value for Part Description", "Warning", null);
                txtField09_Parts.requestFocus();
                return false;
            }
            if (comboBox8.getSelectionModel().getSelectedIndex() == -1) {
                ShowMessageFX.Warning(getStage(), "Please select Charge Type", "Warning", null);
                comboBox8.requestFocus();
                return false;
            }
            if (oTrans.getVSPPartsDetail(pnRow, 8).toString().equals("1")) {
                String partAmount = txtField05_Parts.getText().trim();
                if (partAmount.equals("0.00")) {
                    ShowMessageFX.Warning(getStage(), "Please input Labor Amount", "Warning", null);
                    txtField05_Parts.requestFocus();
                    return false;
                }
            }
            if (partQuantity.equals("0")) {
                ShowMessageFX.Warning(getStage(), "Please input Part Quantity", "Warning", null);
                txtField06_Parts.requestFocus();
                return false;
            }
            oTrans.setVSPPartsDetail(pnRow, 9, txtField09_Parts.getText());
            oTrans.setVSPPartsDetail(pnRow, 5, Double.valueOf(txtField05_Parts.getText().replace(",", "")));
            oTrans.setVSPPartsDetail(pnRow, 6, Integer.valueOf(txtField06_Parts.getText()));
        } catch (SQLException ex) {
            Logger.getLogger(VSPPartsEntryDialogController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
}
