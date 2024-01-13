/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.auto.app.service;

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
import org.rmj.auto.service.base.JobOrderMaster;

public class JOLaborInformationController implements Initializable, ScreenInterface {

    private int pnRow = 0;
    private boolean pbState = true;
    private String psOrigDsc = "";
    private final String pxeModuleName = "Labor Information";
    private JobOrderMaster oTrans;
    ObservableList<String> cChargeType = FXCollections.observableArrayList("FREE OF CHARGE", "CHARGE");
    private GRider oApp;
    @FXML
    private Button btnAddLabor;
    @FXML
    private Button btnEditLabor;
    @FXML
    private Button btnCloseLabor;
    @FXML
    private TextField txtField10_Labor;
    @FXML
    private ComboBox<String> comboBox03;
    @FXML
    private TextField txtField06_Labor;

    /**
     * Initializes the controller class.
     */
    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    public void setObject(JobOrderMaster foValue) {
        oTrans = foValue;
    }

    public void setRow(int fnRow) {
        pnRow = fnRow;
    }

    public void setState(boolean fbValue) {
        pbState = fbValue;
    }

    public void setOrigDsc(String fsValue) {
        psOrigDsc = fsValue;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnCloseLabor.setOnAction(this::cmdButton_Click);
        btnAddLabor.setOnAction(this::cmdButton_Click);
        btnEditLabor.setOnAction(this::cmdButton_Click);

        txtField06_Labor.setOnKeyPressed(this::txtField_KeyPressed);
        txtField10_Labor.setOnKeyPressed(this::txtField_KeyPressed);
        txtField06_Labor.focusedProperty().addListener(txtField_Focus);
        comboBox03.setItems(cChargeType);

        handleComboBoxSelectionJoMaster(comboBox03, 3);
        initNumberFormatterFields();
        initAlphabeticalFormatterFields();

        setCapsLockBehavior(txtField10_Labor);
        loadLaborField();
        if (pbState) {
            btnAddLabor.setVisible(true);
            btnAddLabor.setManaged(true);
            btnEditLabor.setVisible(false);
            btnEditLabor.setManaged(false);
        } else {
            btnAddLabor.setVisible(false);
            btnAddLabor.setManaged(false);
            btnEditLabor.setVisible(true);
            btnEditLabor.setManaged(true);
        }
    }

    private void initNumberFormatterFields() {
        Pattern pattern = Pattern.compile("[0-9,.]*");
        txtField06_Labor.setTextFormatter(new InputTextFormatter(pattern));
    }

    private void initAlphabeticalFormatterFields() {
        Pattern lettersOnlyPattern = Pattern.compile("[A-Za-z ]*");
        txtField10_Labor.setTextFormatter(new InputTextFormatter(lettersOnlyPattern));
    }

    private void handleComboBoxSelectionJoMaster(ComboBox<String> comboBox, int fieldNumber) {
        comboBox.setOnAction(e -> {
            try {
                int selectedType = comboBox.getSelectionModel().getSelectedIndex(); // Retrieve the selected type
                if (selectedType >= 0) {
                    switch (fieldNumber) {
                        case 3:
                            if (selectedType == 0) {
                                txtField06_Labor.setText("0.00");
                                oTrans.setJOLaborDetail(pnRow, 6, Double.valueOf("0.00"));
                                txtField06_Labor.setDisable(true);
                            } else {
                                txtField06_Labor.setDisable(false);
                            }
                            break;
                    }
                }

            } catch (SQLException ex) {
                Logger.getLogger(JOLaborInformationController.class.getName()).log(Level.SEVERE, null, ex);
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

    private boolean loadLaborField() {
        try {
            if (!oTrans.computeAmount()) {
                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                return false;
            }
            DecimalFormat getFormat = new DecimalFormat("#,##0.00");
            txtField10_Labor.setText((String) oTrans.getJOLaborDetail(pnRow, 10));
            String selectedItem03 = oTrans.getJOLaborDetail(pnRow, 3).toString();
            switch (selectedItem03) {
                case "0":
                    selectedItem03 = "FREE OF CHARGE";
                    break;
                case "1":
                    selectedItem03 = "CHARGE";
                    break;
            }
            comboBox03.setValue(selectedItem03);
            txtField06_Labor.setText(String.valueOf(getFormat.format(Double.parseDouble(String.valueOf(oTrans.getJOLaborDetail(pnRow, 6))))));

        } catch (SQLException ex) {
            Logger.getLogger(JOLaborInformationController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return true;
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
                case 6:
                    DecimalFormat getFormat = new DecimalFormat("#,##0.00");
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    txtField06_Labor.setText(String.valueOf(getFormat.format(Double.parseDouble(String.valueOf(txtField06_Labor.getText().replace(",", ""))))));
                    break;

            }
        }
    };

    private boolean settoClass() {
        try {
            DecimalFormat setFormat = new DecimalFormat("###0.00");
            int selectedType = comboBox03.getSelectionModel().getSelectedIndex();
            if (txtField10_Labor.getText().trim().isEmpty()) {
                ShowMessageFX.Warning(getStage(), "Please input Labor Description", "Warning", null);
                txtField10_Labor.requestFocus();
                return false;
            }

            if (comboBox03.getSelectionModel().getSelectedIndex() == -1) {
                ShowMessageFX.Warning(getStage(), "Please select Charge Type", "Warning", null);
                comboBox03.requestFocus();
                return false;
            }
            if (selectedType == 1) {
                String laborAmount = txtField06_Labor.getText().replace(",", ""); // Remove commas from the input string
                try {
                    double amount = Double.parseDouble(laborAmount);
                    if (amount == 0.00 || amount < 0.00) {
                        ShowMessageFX.Warning(getStage(), "Please input Labor Amount", "Warning", null);
                        txtField06_Labor.requestFocus();
                        return false;
                    }
                } catch (NumberFormatException e) {
                    // Handle the case where laborAmount is not a valid number
                    ShowMessageFX.Warning(getStage(), "Invalid Labor Amount", "Warning", null);
                    txtField06_Labor.requestFocus();
                    return false;
                }
            }
            oTrans.setJOLaborDetail(pnRow, 3, String.valueOf(selectedType));
            oTrans.setJOLaborDetail(pnRow, 6, setFormat.format(Double.valueOf(txtField06_Labor.getText().replace(",", ""))));
        } catch (SQLException ex) {
            Logger.getLogger(JOLaborInformationController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }

    private void txtField_KeyPressed(KeyEvent event) {
        String txtFieldID = ((TextField) event.getSource()).getId();
        if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
            try {
                switch (txtFieldID) {
                    case "txtField10_Labor":
                        if (oTrans.searchLabor(txtField10_Labor.getText(), pnRow)) {
                            loadLaborField();
                        } else {
                            txtField10_Labor.clear();
                            txtField10_Labor.requestFocus();
                            ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                        }
                        break;
                }
                event.consume();
                CommonUtils.SetNextFocus((TextField) event.getSource());
            } catch (SQLException ex) {
                Logger.getLogger(JOLaborInformationController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (event.getCode() == KeyCode.UP) {
            event.consume();
            CommonUtils.SetPreviousFocus((TextField) event.getSource());
        }
    }

    private Stage getStage() {
        return (Stage) txtField06_Labor.getScene().getWindow();
    }

    private void cmdButton_Click(ActionEvent event) {
        try {
            String lsButton = ((Button) event.getSource()).getId();
            switch (lsButton) {
                case "btnEditLabor":
                case "btnAddLabor":
                    if (settoClass()) {
                        CommonUtils.closeStage(btnCloseLabor);
                    } else {
                        return;
                    }
                    break;
                case "btnCloseLabor":
                    if (pbState) {
                        if (oTrans.getJOLaborDetail(pnRow, 1).toString().isEmpty()) {
                            oTrans.removeJOLabor(pnRow);
                        }
                    } else {
                        if (oTrans.searchLabor(psOrigDsc, pnRow)) {
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
                            return;
                        }

                    }
                    CommonUtils.closeStage(btnCloseLabor);
                    break;
                default:
                    ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                    break;

            }
        } catch (SQLException ex) {
            Logger.getLogger(JOPartsInformationController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

}
