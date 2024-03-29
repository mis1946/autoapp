/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.sales;

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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.auto.app.views.InputTextFormatter;
import org.rmj.auto.app.views.TextFieldAnimationUtil;
import org.rmj.auto.sales.base.InquiryProcess;

/**
 * FXML Controller class Date Created: 04-24-2023
 *
 * @author Arsiela
 */
public class InquiryVehicleSalesAdvancesFormController implements Initializable {

    private GRider oApp;
    private MasterCallback oListener;
    private InquiryProcess oTransProcess;

    public int tbl_row = 0;
    private int iInqStat;
    private int iEditMode;
    private boolean state = false;
    TextFieldAnimationUtil txtFieldAnimation = new TextFieldAnimationUtil();
    private final String pxeModuleName = "Inquiry Vehicle Sales Advances";
    ObservableList<String> cSlipType = FXCollections.observableArrayList("RESERVATION", "DEPOSIT", "SAFEGUARD DUTY");

    @FXML
    private Button btnClose;
    @FXML
    private ComboBox comboBox12; //Slip Type
    @FXML
    private TextField txtField02; //RSV Date
    @FXML
    private TextField txtField03; //Slip No
    @FXML
    private TextField txtField05; //Slip Amount
    @FXML
    private TextArea textArea06; //Remarks
    @FXML
    private TextField txtField13; //Approved Status
    @FXML
    private TextField txtField14; //Approved By
    @FXML
    private TextField txtField15; //Approved Date
    @FXML
    private Button btnApply;

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    public void setVSAObject(InquiryProcess foValue) {
        oTransProcess = foValue;
    }

    public void setTableRows(int row) {
        tbl_row = row;
    }

    public void setState(boolean flValue) {
        state = flValue;
    }

    public void setInqStat(Integer fnValue) {
        iInqStat = fnValue;
    }

    public void setEditMode(Integer fnValue) {
        iEditMode = fnValue;
    }

    private Stage getStage() {
        return (Stage) txtField02.getScene().getWindow();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        oTransProcess.setCallback(oListener);
        comboBox12.setItems(cSlipType); //Slipt Type

        Pattern pattern;
        //pattern = Pattern.compile("^\\d*(\\.\\d{0,2})?$");
        pattern = Pattern.compile("^[\\d,.]*$");
        txtField05.setTextFormatter(new InputTextFormatter(pattern)); //Amount

        txtFieldAnimation.addRequiredFieldListener(txtField05);

        txtField05.setOnKeyPressed(this::txtField_KeyPressed);   //Amount
        textArea06.setOnKeyPressed(this::txtArea_KeyPressed);   //Remarks

        btnClose.setOnAction(this::cmdButton_Click);
        btnApply.setOnAction(this::cmdButton_Click);

        loadInquiryReservation();

    }

    //Search using F3
    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
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

    private void loadInquiryReservation() {
        try {
            /**
             * User can edit VSA only if not yet Approved and not Cancelled.
             *
             *
             */

            setCapsLockBehavior(txtField02); //RSV Date
            setCapsLockBehavior(txtField03); //Slip No
            setCapsLockBehavior(txtField05); //Slip Amount
            setCapsLockBehavior(textArea06); //Remarks
            setCapsLockBehavior(txtField13); //Approved Status
            setCapsLockBehavior(txtField14); //Approved By
            setCapsLockBehavior(txtField15); //Approved Date

            if (state) { //Add
                txtField02.setText(CommonUtils.xsDateShort((Date) oApp.getServerDate()));
                txtField13.setText("FOR APPROVAL");
            } else {
                txtField02.setText(CommonUtils.xsDateShort((Date) oTransProcess.getInqRsv(tbl_row, 2)));
                txtField03.setText(oTransProcess.getInqRsv(tbl_row, 3).toString());
                DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
                //DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###,###.######");
                String formattedAmount = decimalFormat.format(Double.parseDouble(String.valueOf(oTransProcess.getInqRsv(tbl_row, 5))));
                System.out.println("formattedAmount >>> " + formattedAmount);
                System.out.println("oTransProcess.getInqRsv(tbl_row, 5) >>> " + oTransProcess.getInqRsv(tbl_row, 5));
                txtField05.setText(formattedAmount);
                switch (oTransProcess.getInqRsv(tbl_row, 13).toString()) {
                    case "0":
                        txtField13.setText("FOR APPROVAL");

                        switch (iInqStat) {
                            case 0: //For Follow up
                                txtField05.setDisable(state);
                                comboBox12.setDisable(state);
                                textArea06.setDisable(state);
                                btnApply.setDisable(state);
                                break;
                            case 1: //On Process
                            case 3: //VSP
                                if ((iEditMode == EditMode.UPDATE)) {
                                    txtField05.setDisable(state);
                                    comboBox12.setDisable(state);
                                    textArea06.setDisable(state);
                                    btnApply.setDisable(state);
                                } else {
                                    txtField05.setDisable(!state);
                                    comboBox12.setDisable(!state);
                                    textArea06.setDisable(!state);
                                    btnApply.setDisable(!state);
                                }
                                break;
                            case 2: //Lost Sale
                            case 4: //Sold
                            case 5: //Retired
                            case 6: //Cancelled
                                txtField05.setDisable(!state);
                                comboBox12.setDisable(!state);
                                textArea06.setDisable(!state);
                                btnApply.setDisable(!state);
                                break;
                        }
                        break;
                    case "1":
                        txtField13.setText("APPROVED");
                        txtField05.setDisable(!state);
                        comboBox12.setDisable(!state);
                        textArea06.setDisable(!state);
                        btnApply.setDisable(!state);
                        break;
                    case "2":
                        txtField13.setText("CANCELLED");
                        txtField05.setDisable(!state);
                        comboBox12.setDisable(!state);
                        textArea06.setDisable(!state);
                        btnApply.setDisable(!state);
                        break;
                    default:
                        txtField13.setText("");
                        break;
                }
                txtField14.setText((String) oTransProcess.getInqRsv(tbl_row, 23));
                txtField15.setText(CommonUtils.xsDateShort((Date) oTransProcess.getInqRsv(tbl_row, 15)));
                textArea06.setText((String) oTransProcess.getInqRsv(tbl_row, 6));
                comboBox12.getSelectionModel().select(Integer.parseInt(oTransProcess.getInqRsv(tbl_row, 12).toString())); //VSA Type
            }
        } catch (SQLException ex) {
            Logger.getLogger(InquiryVehicleSalesAdvancesFormController.class.getName()).log(Level.SEVERE, null, ex);
        }

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
                case "btnClose":
                    CommonUtils.closeStage(btnClose);
                    break;
                case "btnApply":
                    if (setSelection()) {
                        if (state) {
                            oTransProcess.addReserve();
                        }
                        // Create a DecimalFormat with two decimal places
                        DecimalFormat decimalFormat = new DecimalFormat("###0.00");
                        //DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###,###.##");
                        //DecimalFormat decimalFormat = new DecimalFormat("###############.######");
                        System.out.println("replace >>> " + Double.parseDouble(txtField05.getText().replace(",", "")));
                        System.out.println("set to class >>> " + decimalFormat.format(Double.parseDouble(txtField05.getText().replace(",", ""))));
                        oTransProcess.setInqRsv(tbl_row, 2, SQLUtil.toDate(txtField02.getText(), SQLUtil.FORMAT_SHORT_DATE));
                        oTransProcess.setInqRsv(tbl_row, 5, decimalFormat.format(Double.parseDouble(txtField05.getText().replace(",", ""))));
                        //oTransProcess.setInqRsv(tbl_row, 5,Double.parseDouble(txtField05.getText().replace(",", "")));
                        oTransProcess.setInqRsv(tbl_row, 6, textArea06.getText());
                        oTransProcess.setInqRsv(tbl_row, 12, comboBox12.getSelectionModel().getSelectedIndex());
                    } else {
                        return;
                    }
                    CommonUtils.closeStage(btnApply);
                    break;

                default:
                    ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                    break;
            }
        } catch (SQLException ex) {
            Logger.getLogger(InquiryVehicleSalesAdvancesFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*Convert Date to String*/
    private LocalDate strToDate(String val) {
        DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(val, date_formatter);
        return localDate;
    }

    /*Set ComboBox Value to Master Class*/
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private boolean setSelection() {
        if (comboBox12.getSelectionModel().getSelectedIndex() < 0) {
            ShowMessageFX.Warning("No `Slip Type` selected.", pxeModuleName, "Please select `Slip Type` value.");
            comboBox12.requestFocus();
            return false;
        }
        return true;
    }

}
