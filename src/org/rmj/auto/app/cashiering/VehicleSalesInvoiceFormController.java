/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.cashiering;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.auto.app.views.CancelForm;
import org.rmj.auto.app.views.InputTextFormatter;
import org.rmj.auto.app.views.ScreenInterface;
import org.rmj.auto.app.views.TextFieldAnimationUtil;
import org.rmj.auto.app.views.unloadForm;
import org.rmj.auto.cashiering.base.UnitSalesInvoice;

/**
 * FXML Controller class
 *
 * @author Arsiela Date Created 09-25-2023
 */
public class VehicleSalesInvoiceFormController implements Initializable, ScreenInterface {

    private GRider oApp;
    private UnitSalesInvoice oTrans;
    private MasterCallback oListener;
    CancelForm cancelform = new CancelForm(); //Object for cancellation remarks
    unloadForm unload = new unloadForm(); //Used in Close Button
    TextFieldAnimationUtil txtFieldAnimation = new TextFieldAnimationUtil();
    private String pxeModuleName = "Vehicle Sales Invoice";
    private int pnEditMode;//Modifying fields
    private int lnCtr = 0;
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnBrowse;
    @FXML
    private Button btnPrint;
    @FXML
    private Button btnClose;
    @FXML
    private Button btnCancelSI;
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private TextField txtField05; //sReferNox
    @FXML
    private TextField txtField06; //sSourceNo
    @FXML
    private DatePicker txtField03; //dTransact
    @FXML
    private TextField txtField30; //sCompnyNm
    @FXML
    private TextField txtField31; //sAddressx
    @FXML
    private TextField txtField24; //sSalesExe
    @FXML
    private TextField txtField19; //sCSNoxxxx
    @FXML
    private TextField txtField20; //sPlateNox
    @FXML
    private TextField txtField21; //sFrameNox
    @FXML
    private TextField txtField22; //sEngineNo
    @FXML
    private TextArea textArea18; //sDescript
    @FXML
    private TextField txtField23; //sColorDsc
    @FXML
    private TextField txtField29; //nUnitPrce
    @FXML
    private TextField txtField09_2; //nUnitPrce
    @FXML
    private TextField txtField11; //nVatRatex
    @FXML
    private TextField txtField12; //nVatAmtxx
    @FXML
    private TextField txtField10; //nDiscount
    @FXML
    private TextField txtField09; //nTranTotl
    @FXML
    private TextField textSeek01;
    @FXML
    private ComboBox cmbType032;
    ObservableList<String> cCustomerType = FXCollections.observableArrayList("CUSTOMER", "SUPPLIER"); // Customer Type Values
    @FXML
    private Label lblStatus15;
    @FXML
    private TextField txtField33;
    @FXML
    private TextArea textArea34;
    @FXML
    private TextField txtField36;

    private Stage getStage() {
        return (Stage) btnSave.getScene().getWindow();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oListener = (int fnIndex, Object foValue) -> {
            System.out.println("Set Class Value " + fnIndex + "-->" + foValue);
        };

        oTrans = new UnitSalesInvoice(oApp, oApp.getBranchCode(), false); //Initialize ClientMaster
        oTrans.setCallback(oListener);
        oTrans.setWithUI(true);

        //Button Click Event
        initButton_Cick();
        //initialize textfield keypressed
        initTxtFieldKeyPressed();
        //initialize textfield focus
        initTxtFieldFocus();
        //Set fields to caps lock
        initSetFieldsCapsLock();
        //Set fields pattern
        initPattern();

        cmbType032.setItems(cCustomerType); //Customer Type

        txtField06.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue.isEmpty()) {
                    //clearFields();
                    clearClass();
                    //clearClassFields();
                    loadFields();
                }
            }
        });

        cmbType032.setOnAction(event -> {
            if (pnEditMode == EditMode.ADDNEW) {
                clearClass();
                try {
                    oTrans.setMaster(32, String.valueOf(cmbType032.getSelectionModel().getSelectedIndex()));
                    loadFields();
                } catch (SQLException ex) {
                    Logger.getLogger(VehicleSalesInvoiceFormController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        //set shake animation to textfield
        initAddRequiredField();
        /*Clear Fields*/
        clearFields();
        txtField03.setOnAction(this::getDate); //dTransact
        txtField03.setDayCellFactory(callB);
        pnEditMode = EditMode.UNKNOWN;
        initButton(pnEditMode);

        Platform.runLater(() -> {
            if (oTrans.loadState()) {
                pnEditMode = oTrans.getEditMode();
                loadFields();
                initButton(pnEditMode);
            } else {
                if (oTrans.getMessage().isEmpty()) {
                } else {
                    ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                }
            }
        });

    }

    private void initButton_Cick() {
        btnAdd.setOnAction(this::cmdButton_Click);
        btnEdit.setOnAction(this::cmdButton_Click);
        btnSave.setOnAction(this::cmdButton_Click);
        btnBrowse.setOnAction(this::cmdButton_Click);
        btnPrint.setOnAction(this::cmdButton_Click);
        btnCancelSI.setOnAction(this::cmdButton_Click);
        btnCancel.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click);
    }

    private void cmdButton_Click(ActionEvent event) {
        try {
            String lsButton = ((Button) event.getSource()).getId();
            switch (lsButton) {
                case "btnAdd": //create new
                    /*Clear Fields*/
                    if (oTrans.NewRecord()) {
                        clearFields();
                        loadFields();
                        pnEditMode = oTrans.getEditMode();
                    } else {
                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                    }
                    break;
                case "btnEdit": //modify
                    if (oTrans.UpdateRecord()) {
                        pnEditMode = oTrans.getEditMode();
                    } else {
                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                    }
                    break;
                case "btnSave":
                    //Validate before saving
                    if (((String) oTrans.getMaster(5)).isEmpty()) {
                        ShowMessageFX.Information(getStage(), "Invalid SI Number.", pxeModuleName, null);
                        txtField05.requestFocus();
                        return;
                    }
                    //Proceed Saving
                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to save?") == true) {
                    } else {
                        return;
                    }
                    if (oTrans.SaveRecord()) {
                        ShowMessageFX.Information(getStage(), "Transaction save successfully.", pxeModuleName, null);
                        if (oTrans.OpenRecord(oTrans.getMaster("sTransNox").toString())) {
                            loadFields();
                            pnEditMode = oTrans.getEditMode();
                        } else {
                            ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                        }
                    } else {
                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", "Error while saving " + pxeModuleName);
                    }
                    break;
                case "btnBrowse":
                    if ((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE)) {
                        if (ShowMessageFX.OkayCancel(null, "Confirmation", "You have unsaved data. Are you sure you want to browse a new record?") == true) {
                            clearFields();
                        } else {
                            return;
                        }
                    }

                    if (oTrans.SearchRecord()) {
                        loadFields();
                        pnEditMode = oTrans.getEditMode();
                    } else {
                        ShowMessageFX.Warning(getStage(), "There was an error while loading information.", "Warning", null);
                        pnEditMode = EditMode.UNKNOWN;
                        clearFields();
                    }
                    break;
                case "btnCancelSI":
                    if (ShowMessageFX.OkayCancel(null, "Confirmation", "Are you sure you want to cancel this record?") == true) {
                    } else {
                        return;
                    }

                    if (cancelform.loadCancelWindow(oApp, (String) oTrans.getMaster(1), (String) oTrans.getMaster(5), "VSI")) {
                        if (oTrans.CancelInvoice((String) oTrans.getMaster(1))) {
                            if (!oTrans.OpenRecord((String) oTrans.getMaster(1))) {
                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", "");
                            }
                            loadFields();
                            ShowMessageFX.Information(getStage(), "Invoice Successfully Cancelled.", "Success", "");
                            pnEditMode = EditMode.UNKNOWN;
                        } else {
                            ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", "Error while cancelling " + pxeModuleName);
                            return;
                        }
                    } else {
                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", "Error while cancelling " + pxeModuleName);
                        return;
                    }
                    break;
                case "btnPrint":
                    if (!loadPrint((String) oTrans.getMaster(1))) {
                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", "Error while printing " + pxeModuleName);
                        return;
                    }
                    break;
                case "btnCancel":
                    if (ShowMessageFX.OkayCancel(getStage(), "Are you sure you want to cancel?", pxeModuleName, null) == true) {
                        clearFields();
                        pnEditMode = EditMode.UNKNOWN;
                    }
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
        } catch (SQLException ex) {
            Logger.getLogger(VehicleSalesInvoiceFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
        initButton(pnEditMode);
    }

    private void initTxtFieldKeyPressed() {
        txtField06.setOnKeyPressed(this::txtField_KeyPressed);  // sSourceNo
        txtField10.setOnKeyPressed(this::txtField_KeyPressed);  // nDiscount
        textSeek01.setOnKeyPressed(this::txtField_KeyPressed);  // search
    }

    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        String txtFieldID = ((TextField) event.getSource()).getId();
        try {
            if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
                switch (txtFieldID) {
                    case "textSeek01":
                        if ((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE)) {
                            if (ShowMessageFX.OkayCancel(null, "Confirmation", "You have unsaved data. Are you sure you want to browse a new record?") == true) {
                                clearFields();
                            } else {
                                return;
                            }
                        }
                        if (oTrans.SearchRecord()) {
                            loadFields();
                            pnEditMode = oTrans.getEditMode();
                        } else {
                            ShowMessageFX.Warning(getStage(), "There was an error while loading information.", "Warning", null);
                            pnEditMode = EditMode.UNKNOWN;
                            clearFields();
                        }
                        initButton(pnEditMode);
                        break;
                    case "txtField06":
                        if (oTrans.searchUDR(txtField.getText(), String.valueOf(cmbType032.getSelectionModel().getSelectedIndex()))) {
                            oTrans.computeAmount();
                            loadFields();
                        } else {
                            ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                            txtField06.clear();
                            txtField06.requestFocus();
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

    private void initTxtFieldFocus() {
        txtField05.focusedProperty().addListener(txtField_Focus);  // si number
        txtField10.focusedProperty().addListener(txtField_Focus);  // nDiscount
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
//                            case 11:
//                            case 12:
                    case 10:
                        if (lsValue.isEmpty()) {
                            lsValue = "0.00";
                        }

                        if (Double.valueOf(lsValue.replace(",", "")) > ((Double) oTrans.getMaster(29))) {
                            lsValue = "0.00";
                            ShowMessageFX.Warning(getStage(), "Invalid Amount", "Warning", null);
                            txtField10.requestFocus();
                        }

                        oTrans.setMaster(lnIndex, Double.valueOf(lsValue.replace(",", "")));
                        oTrans.computeAmount();
                        loadFields();
                        break;
                    case 5:
                        oTrans.setMaster(lnIndex, lsValue);
                        break;

                }

            } else {
                txtField.selectAll();
            }
        } catch (SQLException ex) {
            Logger.getLogger(VehicleSalesInvoiceFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    };

    /*Set Date Value to Master Class*/
    public void getDate(ActionEvent event) {
        try {
            oTrans.setMaster(3, SQLUtil.toDate(txtField03.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
        } catch (SQLException ex) {
            Logger.getLogger(VehicleSalesInvoiceFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadFields() {
        try {
            // Get the current event handler
            EventHandler<ActionEvent> eventHandler = cmbType032.getOnAction();
            // Remove the event handler to prevent it from triggering
            cmbType032.setOnAction(null);
            // Set the value without triggering the event
            cmbType032.getSelectionModel().select(Integer.parseInt(oTrans.getMaster(32).toString())); // Customer Type
            // Add the event handler back
            cmbType032.setOnAction(eventHandler);

            txtField03.setValue(strToDate(CommonUtils.xsDateShort((Date) oTrans.getMaster(3))));//dTransact
            txtField05.setText((String) oTrans.getMaster(5));//sReferNox
            txtField06.setText((String) oTrans.getMaster(6)); //sSourceNo
            txtField30.setText((String) oTrans.getMaster(30)); //sCompnyNm
            txtField31.setText((String) oTrans.getMaster(31)); //sAddressx
            txtField24.setText((String) oTrans.getMaster(24)); //sSalesExe
            txtField19.setText((String) oTrans.getMaster(19)); //sCSNoxxxx
            txtField20.setText((String) oTrans.getMaster(20)); //sPlateNox
            txtField21.setText((String) oTrans.getMaster(21)); //sFrameNox
            txtField22.setText((String) oTrans.getMaster(22)); //sEngineNo
            String sColor = (String) oTrans.getMaster(23);
            String sRegex = "\\s*\\b" + sColor + "\\b\\s*";
            String sDescrpt = ((String) oTrans.getMaster(18)).replaceAll(sRegex, " ");
            textArea18.setText(sDescrpt); //sDescript
            txtField23.setText(sColor); //sColorDsc
            //  cmbType032.getSelectionModel().select(Integer.parseInt(oTrans.getMaster(32).toString())); //Customer Type

            textArea34.setText((String) oTrans.getMaster(34)); //Remarks
            txtField33.setText((String) oTrans.getMaster(33)); //Tin
            txtField36.setText((String) oTrans.getMaster(36)); //sCoCltNmx
            if (((String) oTrans.getMaster(15)).equals("1")) {
                lblStatus15.setText("Active");
            } else {
                lblStatus15.setText("Cancelled");
            }

            // Format the decimal value with decimal separators
            DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

            txtField29.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", (Double) oTrans.getMaster(29))))); //nUnitPrce
            txtField09_2.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", (Double) oTrans.getMaster(9))))); //nTranTotl
            txtField11.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", (Double) oTrans.getMaster(11))))); //nVatRatex
            txtField12.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", (Double) oTrans.getMaster(12))))); //nVatAmtxx
            //System.out.println("Discount >> " + decimalFormat.format(Double.parseDouble(String.format("%.2f",(Double) oTrans.getMaster(10)))));
            txtField10.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", (Double) oTrans.getMaster(10))))); //nDiscount
            txtField09.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f", (Double) oTrans.getMaster(9))))); //nTranTotl

        } catch (SQLException ex) {
            Logger.getLogger(VehicleSalesInvoiceFormController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private boolean loadPrint(String fsValue) {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("VehicleSalesInvoicePrint.fxml"));

            VehicleSalesInvoicePrintController loControl = new VehicleSalesInvoicePrintController();
            loControl.setGRider(oApp);
            loControl.setTransNox(fsValue);
            loControl.setObject(oTrans);
            fxmlLoader.setController(loControl);

            //load the main interface
            Parent parent = fxmlLoader.load();

            parent.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
            });

            parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    stage.setX(event.getScreenX() - xOffset);
                    stage.setY(event.getScreenY() - yOffset);
                }
            });

            //set the main interface as the scene
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("");
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }
        return true;
    }

    /*Clear Class*/
    public void clearClass() {
        try {
            for (int lnCtr = 1; lnCtr <= 39; lnCtr++) {
                switch (lnCtr) {
                    case 6: //a.sSourceNo
                    case 7: //a.sSourceCd
                    case 8: //a.sClientID
                    case 18: //sDescript
                    case 19: //sCSNoxxxx
                    case 20: //sPlateNox
                    case 21: //sFrameNox
                    case 22: //sEngineNo
                    case 23: //sColorDsc
                    case 24: //sSalesExe
                    case 25: //sEmployID
                    case 30: //sCompnyNm
                    case 31: //sAddressx
                    case 33: //sTaxIDNox
                    case 34: //sRemarksx
                    case 35: //sCoCltIDx
                    case 36: //sCoCltNmx
                    case 37: //cPayModex
                    case 38: //sBankname
                    case 39: //cTrStatus
                        oTrans.setMaster(lnCtr, "");
                        break;
                    case 9: //a.nTranTotl
                    case 10: //a.nDiscount
                    case 11: //a.nVatRatex
                    case 12: //a.nVatAmtxx
                    case 13: //a.nAmtPaidx
                    case 26: //nAddlDscx
                    case 27: //nPromoDsc
                    case 28: //nFleetDsc
                    case 29: //nUnitPrce
                        oTrans.setMaster(lnCtr, 0.00);
                        break;
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(VehicleSalesInvoiceFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initAddRequiredField() {
        txtFieldAnimation.addRequiredFieldListener(txtField05);
        txtFieldAnimation.addRequiredFieldListener(txtField06);
    }

    /*Clear Fields*/
    public void clearFields() {

        removeRequired();
        cmbType032.setValue(null);
        lblStatus15.setText("");
        txtField05.setText("");
        txtField06.setText("");
        txtField30.setText("");
        txtField31.setText("");
        txtField24.setText("");
        txtField19.setText("");
        txtField20.setText("");
        txtField21.setText("");
        txtField22.setText("");
        textArea18.setText("");
        txtField23.setText("");
        txtField29.setText("0.00");
        txtField09_2.setText("0.00");
        txtField11.setText("0.00");
        txtField12.setText("0.00");
        txtField10.setText("0.00");
        txtField09.setText("0.00");
        txtField36.setText("");
    }

    private void removeRequired() {
        txtFieldAnimation.removeShakeAnimation(txtField05, txtFieldAnimation.shakeTextField(txtField05), "required-field");
        txtFieldAnimation.removeShakeAnimation(txtField06, txtFieldAnimation.shakeTextField(txtField06), "required-field");

    }

    /*Enabling / Disabling Fields*/
    private void initButton(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

        //if lbShow = false hide btn
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        btnEdit.setVisible(false);
        btnEdit.setManaged(false);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);
        btnCancel.setVisible(false);
        btnCancel.setManaged(false);
        btnCancelSI.setVisible(false);
        btnCancelSI.setManaged(false);
        btnPrint.setVisible(false);
        btnPrint.setManaged(false);

        if (fnValue == EditMode.READY) { //show edit if user clicked save / browse
            btnEdit.setVisible(true);
            btnEdit.setManaged(true);
            btnCancelSI.setVisible(true);
            btnCancelSI.setManaged(true);
            btnPrint.setVisible(true);
            btnPrint.setManaged(true);
        }
        txtField03.setDisable(!lbShow); //dTransact
        txtField10.setDisable(!lbShow); //nDiscount
        txtField05.setDisable(true);//sReferNox
        txtField06.setDisable(true); //sSourceNo
        cmbType032.setDisable(true); //Customer type
        textArea34.setDisable(!lbShow); //Remarks
        textArea18.setDisable(true); //vehicle description

        if (fnValue == EditMode.ADDNEW) {
            btnCancel.setVisible(true);
            btnCancel.setManaged(true);
            txtField05.setDisable(false);//sReferNox
            cmbType032.setDisable(false); //Customer type
            txtField06.setDisable(false); //sSourceNo
        }

        //txtField30.setDisable(!lbShow); //sCompnyNm
        //txtField31.setDisable(!lbShow); //sAddressx
        //txtField24.setDisable(!lbShow); //sSalesExe
        //txtField19.setDisable(!lbShow); //sCSNoxxxx
        //txtField20.setDisable(!lbShow); //sPlateNox
        //txtField21.setDisable(!lbShow); //sFrameNox
        //txtField22.setDisable(!lbShow); //sEngineNo
        //textArea18.setDisable(!lbShow); //sDescript
        //txtField23.setDisable(!lbShow); //sColorDsc
        //txtField29.setDisable(!lbShow); //nUnitPrce
        //txtField09_2.setDisable(!lbShow); //nUnitPrce
        //txtField11.setDisable(!lbShow); //nVatRatex
        //txtField12.setDisable(!lbShow); //nVatAmtxx
        //txtField09.setDisable(!lbShow); //nTranTotl
    }

    private void initSetFieldsCapsLock() {
        setCapsLockBehavior(txtField05);
        setCapsLockBehavior(txtField06);
        setCapsLockBehavior(txtField30);
        setCapsLockBehavior(txtField31);
        setCapsLockBehavior(txtField24);
        setCapsLockBehavior(txtField19);
        setCapsLockBehavior(txtField20);
        setCapsLockBehavior(txtField21);
        setCapsLockBehavior(txtField22);
        setCapsLockBehavior(txtField23);
        setCapsLockBehavior(txtField33);
        setCapsLockBehavior(txtField36);

        setCapsLockBehavior(textArea18);
        setCapsLockBehavior(textArea34);
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

    private void initPattern() {
        Pattern pattern;
        pattern = Pattern.compile("[[0-9][.][,]]*");
        txtField10.setTextFormatter(new InputTextFormatter(pattern)); //nDiscount
        pattern = Pattern.compile("[0-9]*");
        txtField05.setTextFormatter(new InputTextFormatter(pattern)); //sReferNox

    }

    /*Convert Date to String*/
    private LocalDate strToDate(String val) {
        DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(val, date_formatter);
        return localDate;
    }

    private Callback<DatePicker, DateCell> callB = new Callback<DatePicker, DateCell>() {
        @Override
        public DateCell call(final DatePicker param) {
            return new DateCell() {
                @Override
                public void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                    LocalDate today = LocalDate.now();
                    setDisable(empty || item.compareTo(today) < 0);
                }

            };
        }

    };

}
