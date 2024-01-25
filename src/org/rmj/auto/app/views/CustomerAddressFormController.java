/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.views;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.auto.clients.base.ClientAddress;

/**
 * FXML Controller class
 *
 * @author Arsiela Date Created: 10-23-2023
 */
public class CustomerAddressFormController implements Initializable, ScreenInterface {

    private GRider oApp;
    private MasterCallback oListener;
    private ClientAddress oTransAddress;
    private final String pxeModuleName = "Customer Address";
    private int pnRow = 0;
    private boolean pbState = true;
    private String psOrigTown = "";
    private String psOrigBrgy = "";
    TextFieldAnimationUtil txtFieldAnimation = new TextFieldAnimationUtil();

    @FXML
    private Button btnEdit;
    @FXML
    private Button btnClose;
    @FXML
    private TextField txtField03Addr;
    @FXML
    private TextField txtField04Addr;
    @FXML
    private TextField txtField05Addr;
    @FXML
    private TextField txtField06Addr;
    @FXML
    private TextField txtField07Addr;
    @FXML
    private RadioButton radiobtn18AddY;
    @FXML
    private ToggleGroup add_active;
    @FXML
    private RadioButton radiobtn18AddN;
    @FXML
    private CheckBox checkBox14Addr;
    @FXML
    private CheckBox checkBox17Addr;
    @FXML
    private CheckBox checkBox12Addr;
    @FXML
    private CheckBox checkBox13Addr;
    @FXML
    private TextArea textArea11Addr;
    @FXML
    private Button btnAdd;

    public void setObject(ClientAddress foObject) {
        oTransAddress = foObject;
    }

    public void setRow(int fnRow) {
        pnRow = fnRow;
    }

    public void setState(boolean fbValue) {
        pbState = fbValue;
    }

    public void setOrigTown(String fsValue) {
        psOrigTown = fsValue;
    }

    public void setOrigBrgy(String fsValue) {
        psOrigBrgy = fsValue;
    }

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

        //addRequiredFieldListener(txtField03Addr);
        txtFieldAnimation.addRequiredFieldListener(txtField04Addr);
        txtFieldAnimation.addRequiredFieldListener(txtField05Addr);
        txtFieldAnimation.addRequiredFieldListener(txtField06Addr);

        CommonUtils.addTextLimiter(txtField03Addr, 5); //HOUSE NO

        Pattern pattern;
        pattern = Pattern.compile("[0-9]*");
        txtField03Addr.setTextFormatter(new InputTextFormatter(pattern)); //House No
        txtField07Addr.setTextFormatter(new InputTextFormatter(pattern)); //Zip code

        //CLIENT Address
        setCapsLockBehavior(txtField03Addr);
        setCapsLockBehavior(txtField04Addr);
        setCapsLockBehavior(txtField05Addr);
        setCapsLockBehavior(txtField06Addr);
        setCapsLockBehavior(txtField07Addr);
        setCapsLockBehavior(textArea11Addr);
        txtField03Addr.setOnKeyPressed(this::txtField_KeyPressed); //House No
        txtField04Addr.setOnKeyPressed(this::txtField_KeyPressed); //Street / Address
        txtField05Addr.setOnKeyPressed(this::txtField_KeyPressed); // Town
        txtField06Addr.setOnKeyPressed(this::txtField_KeyPressed); // Brgy
        txtField07Addr.setOnKeyPressed(this::txtField_KeyPressed); //Zip code
        textArea11Addr.setOnKeyPressed(this::txtArea_KeyPressed); // Address Remarks

        /*Check box Clicked Event*/
 /*client_address*/
        checkBox14Addr.setOnAction(this::cmdCheckBox_Click);
        checkBox17Addr.setOnAction(this::cmdCheckBox_Click);
        checkBox12Addr.setOnAction(this::cmdCheckBox_Click);
        checkBox13Addr.setOnAction(this::cmdCheckBox_Click);

        //Button SetOnAction using cmdButton_Click() method
        btnClose.setOnAction(this::cmdButton_Click);
        btnAdd.setOnAction(this::cmdButton_Click);
        btnEdit.setOnAction(this::cmdButton_Click);

        txtField05Addr.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                try {
                    txtField05Addr.clear(); // Town
                    txtField07Addr.clear(); //Zip code
                    txtField06Addr.clear(); // Brgy
                    txtField06Addr.setDisable(true);// Brgy

                    oTransAddress.setAddressTable(pnRow, 5, ""); //town id
                    oTransAddress.setAddressTable(pnRow, 25, ""); //town
                    oTransAddress.setAddressTable(pnRow, 7, ""); //zip
                    oTransAddress.setAddressTable(pnRow, 6, ""); //brgy id
                    oTransAddress.setAddressTable(pnRow, 24, ""); //brgy

                    txtField06Addr.getStyleClass().remove("required-field");
                } catch (SQLException ex) {
                    Logger.getLogger(CustomerAddressFormController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        if (pbState) {
            btnAdd.setVisible(true);
            btnAdd.setManaged(true);
            btnEdit.setVisible(false);
            btnEdit.setManaged(false);
        } else {
            loadFields();
            btnAdd.setVisible(false);
            btnAdd.setManaged(false);
            btnEdit.setVisible(true);
            btnEdit.setManaged(true);
        }
    }

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private static void setCapsLockBehavior(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (textField.getText() != null) {
                textField.setText(newValue.toUpperCase());
            }
        });
    }

    private static void setCapsLockBehavior(TextArea textArea) {
        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (textArea.getText() != null) {
                textArea.setText(newValue.toUpperCase());
            }
        });
    }

    private void cmdButton_Click(ActionEvent event) {
        try {
            String lsButton = ((Button) event.getSource()).getId();
            switch (lsButton) {
                case "btnEdit":
                case "btnAdd":
                    if (settoClass()) {
                        if (lsButton.equals("btnAdd")) {
                            oTransAddress.addAddress();
                        }
                        CommonUtils.closeStage(btnClose);
                    } else {
                        return;
                    }
                    break;
                case "btnClose":
                    if (oTransAddress.searchTown(pnRow, psOrigTown, true)) {
                        if (oTransAddress.searchBarangay(pnRow, psOrigBrgy, true)) {
                        }
                    }

                    CommonUtils.closeStage(btnClose);
                    break;

                default:
                    ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                    break;

            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerAddressFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean settoClass() {
        try {
            for (int lnCtr = 1; lnCtr <= oTransAddress.getItemCount(); lnCtr++) {
                if (oTransAddress.getAddress(lnCtr, "cPrimaryx").toString().equals("1") && (lnCtr != pnRow)) {
                    if (checkBox14Addr.isSelected()) {
                        ShowMessageFX.Warning(getStage(), "Please note that you cannot add more than 1 primary address.", "Warning", pxeModuleName);
                        return false;
                    }
                }
            }
            
            if(checkBox14Addr.isSelected() && radiobtn18AddN.isSelected()){
                ShowMessageFX.Warning(getStage(), "Please note that you cannot set primary address that is inactive.", "Warning", pxeModuleName);
                return false;
            }
            
            //Validate Before adding to tables
            if (( //txtField03Addr.getText().isEmpty() ||
                    txtField04Addr.getText().isEmpty() || txtField07Addr.getText().isEmpty() || txtField05Addr.getText().isEmpty() || txtField06Addr.getText().isEmpty())
                    || //txtField03Addr.getText().trim().equals("") ||
                    txtField04Addr.getText().trim().equals("") || txtField07Addr.getText().trim().equals("") || txtField05Addr.getText().trim().equals("") || txtField06Addr.getText().trim().equals("")) {
                ShowMessageFX.Warning(getStage(), "Invalid Address. Insert to table Aborted!", "Warning", null);
                return false;
            }

            if (!radiobtn18AddY.isSelected() && !radiobtn18AddN.isSelected()) {
                ShowMessageFX.Warning(getStage(), "Please select Address Status. Insert to table Aborted!", "Warning", null);
                return false;
            }

            if (!checkBox12Addr.isSelected() && !checkBox13Addr.isSelected()
                    && !checkBox14Addr.isSelected() && !checkBox17Addr.isSelected()) {
                ShowMessageFX.Warning(getStage(), "Please select Address Type. Insert to table Aborted!", "Warning", null);
                return false;
            }
            String sHouseNox = "";
            if (!txtField03Addr.getText().isEmpty()) {
                sHouseNox = txtField03Addr.getText();
            }
            oTransAddress.setAddressTable(pnRow, 3, sHouseNox);
            oTransAddress.setAddressTable(pnRow, 4, txtField04Addr.getText());
            oTransAddress.setAddressTable(pnRow, 7, txtField07Addr.getText());
            oTransAddress.setAddressTable(pnRow, 11, textArea11Addr.getText());
            oTransAddress.setAddressTable(pnRow, 25, txtField05Addr.getText());
            oTransAddress.setAddressTable(pnRow, 24, txtField06Addr.getText());

            if (checkBox12Addr.isSelected()) {
                oTransAddress.setAddressTable(pnRow, 12, 1);
            } else {
                oTransAddress.setAddressTable(pnRow, 12, 0);
            }
            if (checkBox13Addr.isSelected()) {
                oTransAddress.setAddressTable(pnRow, 13, 1);
            } else {
                oTransAddress.setAddressTable(pnRow, 13, 0);
            }
            if (checkBox14Addr.isSelected()) {
                oTransAddress.setAddressTable(pnRow, 14, 1);
            } else {
                oTransAddress.setAddressTable(pnRow, 14, 0);
            }
            if (checkBox17Addr.isSelected()) {
                oTransAddress.setAddressTable(pnRow, 17, 1);
            } else {
                oTransAddress.setAddressTable(pnRow, 17, 0);
            }
            if (radiobtn18AddY.isSelected()) {
                oTransAddress.setAddressTable(pnRow, 18, 1);
            } else {
                oTransAddress.setAddressTable(pnRow, 18, 0);
            }

        } catch (SQLException ex) {
            Logger.getLogger(CustomerAddressFormController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }

    private void loadFields() {
        try {
            txtField03Addr.setText((String) oTransAddress.getAddress(pnRow, "sHouseNox"));
            txtField04Addr.setText((String) oTransAddress.getAddress(pnRow, "sAddressx"));
            txtField05Addr.setText((String) oTransAddress.getAddress(pnRow, "sTownName"));
            txtField06Addr.setText((String) oTransAddress.getAddress(pnRow, "sBrgyName"));
            txtField07Addr.setText((String) oTransAddress.getAddress(pnRow, "sZippCode"));
            textArea11Addr.setText((String) oTransAddress.getAddress(pnRow, "sRemarksx"));
            if (oTransAddress.getAddress(pnRow, "cRecdStat").toString().equals("1")) {
                radiobtn18AddY.setSelected(true);
                radiobtn18AddN.setSelected(false);
            } else {
                radiobtn18AddY.setSelected(false);
                radiobtn18AddN.setSelected(true);
            }
            if (oTransAddress.getAddress(pnRow, "cOfficexx").toString().equals("1")) {
                checkBox12Addr.setSelected(true);
            } else {
                checkBox12Addr.setSelected(false);
            }
            if (oTransAddress.getAddress(pnRow, "cProvince").toString().equals("1")) {
                checkBox13Addr.setSelected(true);
            } else {
                checkBox13Addr.setSelected(false);
            }
            if (oTransAddress.getAddress(pnRow, "cPrimaryx").toString().equals("1")) {
                checkBox14Addr.setSelected(true);
            } else {
                checkBox14Addr.setSelected(false);
            }
            if (oTransAddress.getAddress(pnRow, "cCurrentx").toString().equals("1")) {
                checkBox17Addr.setSelected(true);
            } else {
                checkBox17Addr.setSelected(false);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerAddressFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Set CheckBox Action
    private void cmdCheckBox_Click(ActionEvent event) {
        String scbSel = ((CheckBox) event.getSource()).getId();
        switch (scbSel) {
            /*client_address*/
            case "checkBox17Addr": // Current
                if (checkBox17Addr.isSelected()) {
                    checkBox12Addr.setSelected(false);
                }
                break;
            case "checkBox13Addr": // Provincial
                if (checkBox13Addr.isSelected()) {
                    checkBox12Addr.setSelected(false);
                }
                break;
            case "checkBox12Addr": // Office
                if (checkBox12Addr.isSelected()) {
                    checkBox17Addr.setSelected(false);
                    checkBox13Addr.setSelected(false);
                }
                break;
        }
    }

    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(((TextField) event.getSource()).getId().substring(8, 10));
        String txtFieldID = ((TextField) event.getSource()).getId();
        try {
            switch (event.getCode()) {
                case F3:
                case TAB:
                case ENTER:
                    switch (txtFieldID) {
                        case "txtField05Addr":  //Search by Town Address
                            if (oTransAddress.searchTown(pnRow, txtField05Addr.getText(), false)) {
                                txtField05Addr.setText((String) oTransAddress.getAddress(pnRow, "sTownName"));
                                txtField07Addr.setText((String) oTransAddress.getAddress(pnRow, "sZippCode"));
                                txtField06Addr.setDisable(false);
                            } else {
                                oTransAddress.setAddressTable(pnRow, 5, ""); //town id
                                oTransAddress.setAddressTable(pnRow, 25, ""); //town
                                oTransAddress.setAddressTable(pnRow, 7, ""); //zip

                                txtField05Addr.clear(); // Town
                                txtField07Addr.clear(); //Zip code
                                txtField06Addr.setDisable(true);
                                ShowMessageFX.Warning(getStage(), oTransAddress.getMessage(), "Warning", null);
                            }
                            txtField06Addr.getStyleClass().remove("required-field");
                            oTransAddress.setAddressTable(pnRow, 6, ""); //brgy id
                            oTransAddress.setAddressTable(pnRow, 24, ""); //brgy
                            txtField06Addr.clear(); // Brgy
                            break;

                        case "txtField06Addr":  //Search by Brgy Address
                            if (oTransAddress.searchBarangay(pnRow, txtField06Addr.getText(), false)) {
                                txtField06Addr.setText((String) oTransAddress.getAddress(pnRow, "sBrgyName"));

                            } else {
                                ShowMessageFX.Warning(getStage(), oTransAddress.getMessage(), "Warning", null);
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

    /*TRIGGER FOCUS*/
    private void txtArea_KeyPressed(KeyEvent event) {
        if (event.getCode() == ENTER || event.getCode() == DOWN) {
            event.consume();
            CommonUtils.SetNextFocus((TextArea) event.getSource());
        } else if (event.getCode() == KeyCode.UP) {
            event.consume();
            CommonUtils.SetPreviousFocus((TextArea) event.getSource());
        }
    }

}
