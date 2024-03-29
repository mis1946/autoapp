/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.auto.app.views.TextFieldAnimationUtil;
import org.rmj.auto.sales.base.InquiryBankApplication;

/**
 * FXML Controller class Date Created: 04-24-2023
 *
 * @author Arsiela
 */
public class InquiryBankApplicationFormController implements Initializable {

    private GRider oApp;
    private MasterCallback oListener;
    private InquiryBankApplication oTransBankApp;

    private String sTransNo = "";
    private String sOApplieddate = "";
    private int pnInqPayMode;
    private int pnEditMode;
    TextFieldAnimationUtil txtFieldAnimation = new TextFieldAnimationUtil();
    private final String pxeModuleName = "Inquiry Bank Application";

    ObservableList<String> cBankPaymode = FXCollections.observableArrayList("BANK PURCHASE ORDER", "BANK FINANCING", "COMPANY PURCHASE ORDER", "COMPANY FINANCING"); //Mode of Payment Values
    ObservableList<String> cBankStatus = FXCollections.observableArrayList("ON-GOING", "DECLINE", "APPROVED"); //Bank Application Status Values

    @FXML
    private Button btnClose;
    @FXML
    private Button btnSave;
    @FXML
    private TextField txtField16; //Bank Name
    @FXML
    private TextField txtField18; //Bank Branch
    @FXML
    private ComboBox comboBox04; //Payment Mode
    @FXML
    private ComboBox comboBox09; //Application Status
    @FXML
    private DatePicker txtField02; //Applied Date
    @FXML
    private DatePicker txtField03; //Approved Date
    @FXML
    private TextArea textArea08; //Remarks

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    public void setObject(InquiryBankApplication foValue) {
        oTransBankApp = foValue;
    }

    public void setEditMode(Integer fiValue) {
        pnEditMode = fiValue;
    }

    public void setsTransNo(String fsValue) {
        sTransNo = fsValue;
    }

    public void setInqPaymentMode(Integer fiValue) {
        pnInqPayMode = fiValue;
    }

    private Stage getStage() {
        return (Stage) txtField16.getScene().getWindow();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboBox04.setItems(cBankPaymode);
        comboBox09.setItems(cBankStatus);
        comboBox09.setOnAction(event -> {
            if (comboBox09.getSelectionModel().getSelectedIndex() == 2) {
                if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                    txtField03.setDisable(false);
                    txtField03.setValue(strToDate(CommonUtils.xsDateShort((Date) oApp.getServerDate())));
                }
            }
        });

        setCapsLockBehavior(txtField16);
        setCapsLockBehavior(txtField18);
        setCapsLockBehavior(textArea08);

        txtFieldAnimation.addRequiredFieldListener(txtField16);

        txtField16.focusedProperty().addListener(txtField_Focus);  //Bank Name
        txtField18.focusedProperty().addListener(txtField_Focus);  //Bank Branch
        textArea08.focusedProperty().addListener(txtArea_Focus);  //Remarks
        txtField02.setOnAction(this::getDate); //Applied Date
        txtField03.setOnAction(this::getDate); //Approved Date
        txtField02.setDayCellFactory(callApplied);
        txtField03.setDayCellFactory(callApprove);

        txtField16.setOnKeyPressed(this::txtField_KeyPressed);  //Bank Name
        textArea08.setOnKeyPressed(this::txtArea_KeyPressed);  //Remarks
        txtField02.setOnKeyPressed(this::txtField_KeyPressed);  //Applied Date
        txtField03.setOnKeyPressed(this::txtField_KeyPressed);  //Approved Date

        btnClose.setOnAction(this::cmdButton_Click);
        btnSave.setOnAction(this::cmdButton_Click);
        loadBankApplication();
        initbutton(pnEditMode);
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
        String lsButton = ((Button)event.getSource()).getId();
        switch (lsButton){
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            case "btnSave":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to save?")) {
                } else {
                    return;
                }
                if (setSelection()){
                    // oTransBankApp.setPayMode(pnInqPayMode);
                    //oTransBankApp.setTransNox((String) oTransBankApp.getBankApp( 5));
                    if(oTransBankApp.SaveRecord()){
                        ShowMessageFX.Information(null, pxeModuleName, "Bank Application save sucessfully.");
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, oTransBankApp.getMessage());
                        return;
                    }
                } else {
                    return;
                }
                CommonUtils.closeStage(btnSave);
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;
                
        }
        initbutton(pnEditMode);
    }    
    
//    private void cmdButton_Click(ActionEvent event) {
//        String lsButton = ((Button) event.getSource()).getId();
//        switch (lsButton) {
//            case "btnClose":
//                CommonUtils.closeStage(btnClose);
//                break;
//            case "btnSave":
//                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to save?")) {
//                } else {
//                    return;
//                }
//                if (setSelection()) {
//                    // oTransBankApp.setPayMode(pnInqPayMode);
//                    if (oTransBankApp.SaveRecord()) {
//                        ShowMessageFX.Information(null, pxeModuleName, "Bank Application save sucessfully.");
//                    } else {
//                        ShowMessageFX.Warning(null, pxeModuleName, oTransBankApp.getMessage());
//                        return;
//                    }
//                } else {
//                    return;
//                }
//                CommonUtils.closeStage(btnSave);
//                break;
//            default:
//                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
//                break;
//
//        }
//        initbutton(pnEditMode);
//    }

    public void loadBankApplication() {
        try {
            /**
             * User can edit only if not Inquiry is not Lost Sale / Sold /
             * Cancelled.
             *
             */
            txtField02.setValue(strToDate(CommonUtils.xsDateShort((Date) oTransBankApp.getBankApp(2))));
            txtField03.setValue(strToDate(CommonUtils.xsDateShort((Date) oTransBankApp.getBankApp(3))));
            txtField16.setText(oTransBankApp.getBankApp(16).toString()); //Bank Name
            txtField18.setText(oTransBankApp.getBankApp(18).toString()); //Bank Branch
            if (pnEditMode == EditMode.ADDNEW) {
                comboBox04.getSelectionModel().select(pnInqPayMode); //Payment Mode
            } else {
                comboBox04.getSelectionModel().select(Integer.parseInt(oTransBankApp.getBankApp(4).toString())); //Payment Mode
            }
            if (Integer.parseInt(oTransBankApp.getBankApp(9).toString()) == 3) {
                comboBox09.setValue("Cancelled");
                pnEditMode = EditMode.UNKNOWN;
            } else {
                comboBox09.getSelectionModel().select(Integer.parseInt(oTransBankApp.getBankApp(9).toString())); //Bank Application Status
            }
            textArea08.setText(oTransBankApp.getBankApp(8).toString()); //Remarks

            //Get the original applied date
            if (pnEditMode == EditMode.UPDATE) {
                if (sOApplieddate.isEmpty()) {
                    sOApplieddate = oTransBankApp.getBankApp(2).toString();
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(InquiryBankApplicationFormController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        String txtFieldID = ((TextField) event.getSource()).getId();
        try {
            if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
                switch (txtFieldID) {
                    case "txtField16": //Bank Name
                        if (oTransBankApp.searchBank(txtField.getText(), false)) {
                            loadBankApplication();
                        } else {
                            ShowMessageFX.Warning(getStage(), oTransBankApp.getMessage(), "Warning", null);
                            txtField16.requestFocus();
                            return;
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

    /*Convert String to LocalDate*/
    private LocalDate strToDate(String val) {
        DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(val, date_formatter);
        return localDate;
    }

    /*Set Date Value to Master Class*/
    public void getDate(ActionEvent event) {
        try {
            DatePicker datePicker = (DatePicker) event.getSource();
            String datePickerId = datePicker.getId();
            switch (datePickerId) {
                case "txtField02":
                    oTransBankApp.setBankApp(2, SQLUtil.toDate(txtField02.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
                    break;
                case "txtField03":
                    oTransBankApp.setBankApp(3, SQLUtil.toDate(txtField03.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
                    break;
                default:
                    break;
            }
        } catch (SQLException ex) {
            Logger.getLogger(InquiryBankApplicationFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /*Set TextField Value to Master Class*/
    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {
        try {
            TextField txtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
            int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
            String lsValue = txtField.getText();

            if (lsValue == null) {
                return;
            }
            if (!nv) {
                /*Lost Focus*/
                switch (lnIndex) {
                    case 16: //
                    case 18: //
                        oTransBankApp.setBankApp(lnIndex, lsValue); //Handle Encoded Value
                        break;
                }

            } else {
                txtField.selectAll();
            }
        } catch (SQLException ex) {
            Logger.getLogger(InquiryBankApplicationFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    };

    /*Set TextArea to Master Class*/
    final ChangeListener<? super Boolean> txtArea_Focus = (o, ov, nv) -> {
        TextArea txtField = (TextArea) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();

        if (lsValue == null) {
            return;
        }
        try {
            if (!nv) {
                /*Lost Focus*/
                switch (lnIndex) {
                    case 8:
                        oTransBankApp.setBankApp(lnIndex, lsValue); //Handle Encoded Value
                        break;
                }
            } else {
                txtField.selectAll();
            }
        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }
    };

    /*Set ComboBox Value to Master Class*/
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private boolean setSelection() {
        try {
            if (comboBox04.getSelectionModel().getSelectedIndex() < 0) {
                ShowMessageFX.Warning("No `Payment Mode` selected.", pxeModuleName, "Please select `Payment Mode` value.");
                comboBox04.requestFocus();
                return false;
            } else {
                if (comboBox04.getSelectionModel().getSelectedIndex() != pnInqPayMode) {
                    if ((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) && (comboBox09.getSelectionModel().getSelectedIndex() != 1)) {
                        ShowMessageFX.Warning("Invalid `Payment Mode` selected.", pxeModuleName, "Payment Mode selected is not the same with Inquiry Payment Mode.");
                        return false;
                    }
                }

                oTransBankApp.setBankApp(4, comboBox04.getSelectionModel().getSelectedIndex());
            }
            if (comboBox09.getSelectionModel().getSelectedIndex() < 0) {
                ShowMessageFX.Warning("No `Application Status` selected.", pxeModuleName, "Please select `Application Status` value.");
                comboBox09.requestFocus();
                return false;
            } else {
                oTransBankApp.setBankApp(9, comboBox09.getSelectionModel().getSelectedIndex());
            }
        } catch (SQLException ex) {
            ShowMessageFX.Warning(getStage(), ex.getMessage(), "Warning", null);
        }
        return true;
    }

    public void initbutton(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

        txtField16.setDisable(fnValue != EditMode.ADDNEW); //Bank Name
        comboBox04.setDisable(!lbShow); //Payment Mode
        comboBox09.setDisable(!lbShow); //Application Status
        txtField02.setDisable(!lbShow); //Applied Date
        txtField03.setDisable(true); //Approved Date
        textArea08.setDisable(!lbShow); //Remarks
        btnSave.setDisable(!lbShow);

        if (fnValue == EditMode.UPDATE) {
            if ((comboBox09.getSelectionModel().getSelectedIndex() == 1) || (comboBox09.getSelectionModel().getSelectedIndex() == 2)) {
                comboBox04.setDisable(true); //Payment Mode
                comboBox09.setDisable(true); //Application Status
                txtField02.setDisable(true); //Applied Date
            }
        }

    }

    private Callback<DatePicker, DateCell> callApplied = new Callback<DatePicker, DateCell>() {
        @Override
        public DateCell call(final DatePicker param) {
            return new DateCell() {
                @Override
                public void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                    LocalDate today = LocalDate.now();
                    if (pnEditMode == EditMode.ADDNEW) {
                        LocalDate minDate = LocalDate.now().minusDays(7);
                        setDisable(empty || item.isBefore(minDate) || item.compareTo(today) > 0);
                    } else if (pnEditMode == EditMode.UPDATE) {
                        LocalDate minDate = strToDate(sOApplieddate).minusDays(7);
                        setDisable(empty || item.isBefore(minDate) || item.compareTo(strToDate(sOApplieddate)) > 0);
                    }
                }
            };
        }
    };

    private Callback<DatePicker, DateCell> callApprove = new Callback<DatePicker, DateCell>() {
        @Override
        public DateCell call(final DatePicker param) {
            return new DateCell() {
                @Override
                public void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                    LocalDate today = LocalDate.now();
                    LocalDate minDate = LocalDate.now().minusDays(7);
                    setDisable(empty || item.isBefore(minDate) || item.compareTo(today) > 0);
                }
            };
        }
    };

}
