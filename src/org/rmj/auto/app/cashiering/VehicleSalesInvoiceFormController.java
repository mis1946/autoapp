/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.cashiering;

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
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
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
import org.rmj.auto.app.views.InputTextFormatter;
import org.rmj.auto.app.views.ScreenInterface;
import org.rmj.auto.app.views.unloadForm;
import org.rmj.auto.cashiering.base.UnitSalesInvoice;

/**
 * FXML Controller class
 *
 * @author Arsiela
 * Date Created 09-25-2023
 */
public class VehicleSalesInvoiceFormController implements Initializable, ScreenInterface {
    
    private GRider oApp;
    private UnitSalesInvoice oTrans;
    private MasterCallback oListener;
    unloadForm unload = new unloadForm(); //Used in Close Button
    private String pxeModuleName = "Vehicle Sales Invoice";
    private int pnEditMode;//Modifying fields
    private int lnCtr = 0;
    
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
    private TextField txtField29_2; //nUnitPrce
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
    
    private Stage getStage() {
        return (Stage) btnSave.getScene().getWindow();
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oListener = (int fnIndex, Object foValue) -> {
            System.out.println("Set Class Value " + fnIndex + "-->" + foValue);
        };

        oTrans = new UnitSalesInvoice(oApp, oApp.getBranchCode(), true); //Initialize ClientMaster
        oTrans.setCallback(oListener);
        oTrans.setWithUI(true);
        
        //Set fields to caps lock
        setFieldsCapsLock();
        
//        txtField06.focusedProperty().addListener(txtField_Focus);  // sSourceNo
//        txtField05.focusedProperty().addListener(txtField_Focus);  // nVatRatex
//        txtField11.focusedProperty().addListener(txtField_Focus);  // nVatRatex
//        txtField12.focusedProperty().addListener(txtField_Focus);  // nVatAmtxx
        txtField10.focusedProperty().addListener(txtField_Focus);  // nDiscount
        txtField03.setOnAction(this::getDate); //dTransact
        txtField03.setDayCellFactory(callB);
        
//        txtField06.setOnKeyPressed(this::txtField_KeyPressed);  // sSourceNo
//        txtField05.setOnKeyPressed(this::txtField_KeyPressed);  // sReferNox
//        txtField11.setOnKeyPressed(this::txtField_KeyPressed);  // nVatRatex
//        txtField12.setOnKeyPressed(this::txtField_KeyPressed);  // nVatAmtxx
        txtField10.setOnKeyPressed(this::txtField_KeyPressed);  // nDiscount
        
        Pattern pattern;
        pattern = Pattern.compile("[[0-9][.][,]]*");
        txtField10.setTextFormatter(new InputTextFormatter(pattern)); //nDiscount
        pattern = Pattern.compile("[0-9]*");
        txtField05.setTextFormatter(new InputTextFormatter(pattern)); //sReferNox
        
        txtField06.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                clearFields();
                clearClassFields();
            }
        });
        
        //Shake 
        addRequiredFieldListener(txtField05);
        addRequiredFieldListener(txtField06);
        
        //Button Click Event
        btnAdd.setOnAction(this::cmdButton_Click);
        btnEdit.setOnAction(this::cmdButton_Click); 
        btnSave.setOnAction(this::cmdButton_Click); 
        btnBrowse.setOnAction(this::cmdButton_Click);
        btnPrint.setOnAction(this::cmdButton_Click);
        btnCancelSI.setOnAction(this::cmdButton_Click);
        btnCancel.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click); 

        /*Clear Fields*/
        clearFields();

        pnEditMode = EditMode.UNKNOWN;
        initButton(pnEditMode); 
    }    
    
    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }
    
    private void cmdButton_Click(ActionEvent event) {
        try {
            String lsButton = ((Button)event.getSource()).getId();
            switch (lsButton){
                case "btnAdd": //create new 
                    /*Clear Fields*/
                    if (oTrans.NewRecord()) {
                    clearFields();
                    //clearClassFields();
                    oTrans.setMaster(2,oApp.getBranchCode());
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
                    if (((String)oTrans.getMaster(5)).isEmpty()){
                        ShowMessageFX.Information(getStage(), "Invalid SI Number.", pxeModuleName, null);
                        return;
                    }
                    //Proceed Saving
                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to save?") == true) {
                    } else {
                        return;
                    }        
                    if (oTrans.SaveRecord()) {
                        ShowMessageFX.Information(getStage(), "Transaction save successfully.", pxeModuleName, null);
                        if (oTrans.OpenRecord(oTrans.getMaster("sTransNox").toString())){
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

                    if (oTrans.SearchRecord()){
                        loadFields();
                        pnEditMode = oTrans.getEditMode();
                    } else {
                        ShowMessageFX.Warning(getStage(), "There was an error while loading information.", "Warning", null);
                        pnEditMode = EditMode.UNKNOWN;
                        clearFields();
                    }
                    break;
                case "btnPrint":
                    break;
                case "btnCancel":
                    clearFields();
                    pnEditMode = EditMode.UNKNOWN;
                    break;
                case "btnClose": //close tab
                    if(ShowMessageFX.OkayCancel(null, "Close Tab", "Are you sure you want to close this Tab?") == true){
                        if (unload != null) {
                            unload.unloadForm(AnchorMain, oApp, pxeModuleName);
                        }else {
                            ShowMessageFX.Warning(null, "Warning", "Please notify the system administrator to configure the null value at the close button.");    
                        }
                        break;
                    } else
                        return;
            }
        } catch (SQLException ex) {
            Logger.getLogger(VehicleSalesInvoiceFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
        initButton(pnEditMode); 
    }
    
    private void txtField_KeyPressed(KeyEvent event){
        TextField txtField = (TextField)event.getSource();
        int lnIndex = Integer.parseInt(((TextField)event.getSource()).getId().substring(8,10));

          try{
               switch (event.getCode()){
                    case F3:
                    case TAB:
                    case ENTER:
                        switch (lnIndex){
                            case 6:
                                if (oTrans.searchUDR(txtField06.getText(), "1")){
                                    oTrans.computeAmount();
                                    loadFields();
                                } else {
                                    ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                                    txtField06.clear();
                                    txtField06.requestFocus();
                                }
                            break;
                        } 
                        break;
               }
          }catch(SQLException e){
                ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
          }

        switch (event.getCode()){
        case ENTER:
        case DOWN:
            CommonUtils.SetNextFocus(txtField);
            break;
        case UP:
            CommonUtils.SetPreviousFocus(txtField);
        }

    }
    
    /*Set TextField Value to Master Class*/
    final ChangeListener<? super Boolean> txtField_Focus = (o,ov,nv)->{
        try {
            TextField txtField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
            int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
            String lsValue = txtField.getText();

            if (lsValue == null) return;
            if(!nv){ /*Lost Focus*/
                    switch (lnIndex){
//                            case 11:
//                            case 12:
                            case 10:
                                if (lsValue.isEmpty()) lsValue = "0.00";
                                
                                if (Double.valueOf(lsValue.replace(",", "")) > ((Double) oTrans.getMaster(29))){
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

            } else
               txtField.selectAll();
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
    
    private void loadFields(){
        try {
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
            textArea18.setText((String) oTrans.getMaster(18)); //sDescript
            txtField23.setText((String) oTrans.getMaster(23)); //sColorDsc
            
            // Format the decimal value with decimal separators
            DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
            
            txtField29.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f",(Double) oTrans.getMaster(29))))); //nUnitPrce
            txtField29_2.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f",(Double) oTrans.getMaster(29))))); //nUnitPrce
            txtField11.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f",(Double) oTrans.getMaster(11))))); //nVatRatex
            txtField12.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f",(Double)oTrans.getMaster(12))))); //nVatAmtxx
            System.out.println("Discount >> " + decimalFormat.format(Double.parseDouble(String.format("%.2f",(Double) oTrans.getMaster(10)))));
            txtField10.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f",(Double) oTrans.getMaster(10))))); //nDiscount
            txtField09.setText(decimalFormat.format(Double.parseDouble(String.format("%.2f",(Double) oTrans.getMaster(9))))); //nTranTotl
            
        } catch (SQLException ex) {
            Logger.getLogger(VehicleSalesInvoiceFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
    public void clearClassFields(){
        try {
            //Class Master
            for (lnCtr = 1; lnCtr <= 31; lnCtr++) {
                switch (lnCtr) {
                    case 3:
                        oTrans.setMaster(lnCtr, oApp.getServerDate()); 
                        break;
                    case 5: //sReferNox
                    case 6: //sSourceNo
                    case 7: //sSourceCd
                    case 30: //sCompnyNm
                    case 31: //sAddressx
                    case 24: //sSalesExe
                    case 19: //sCSNoxxxx
                    case 20: //sPlateNox
                    case 21: //sFrameNox
                    case 22: //sEngineNo
                    case 18: //sDescript
                    case 23: //sColorDsc
                        oTrans.setMaster(lnCtr, ""); 
                        break;
                    case 29: //nUnitPrce
                    case 11: //nVatRatex
                    case 12: //nVatAmtxx
                    case 10: //nDiscount
                    case 9: //nTranTotl
                        oTrans.setMaster(lnCtr, 0.00); 
                        break;
                        
                }
            } 
        } catch (SQLException ex) {
            Logger.getLogger(VehicleSalesInvoiceFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*Clear Fields*/
    public void clearFields(){
        /*Clear Red Color for required fileds*/
        txtField05.getStyleClass().remove("required-field");
        txtField06.getStyleClass().remove("required-field");
        
        txtField05.clear(); //sReferNox
        txtField06.clear(); //sSourceNo
        txtField30.clear(); //sCompnyNm
        txtField31.clear(); //sAddressx
        txtField24.clear(); //sSalesExe
        txtField19.clear(); //sCSNoxxxx
        txtField20.clear(); //sPlateNox
        txtField21.clear(); //sFrameNox
        txtField22.clear(); //sEngineNo
        textArea18.clear(); //sDescript
        txtField23.clear(); //sColorDsc
        txtField29.clear(); //nUnitPrce
        txtField29_2.clear(); //nUnitPrce
        txtField11.clear(); //nVatRatex
        txtField12.clear(); //nVatAmtxx
        txtField10.clear(); //nDiscount
        txtField09.clear(); //nTranTotl
    }
    
    /*Enabling / Disabling Fields*/
    private void initButton(int fnValue){
        /* NOTE:
            lbShow (FALSE)= invisible
            !lbShow (TRUE)= visible
        */
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
        
        if (fnValue == EditMode.ADDNEW){
            btnCancel.setVisible(true);
            btnCancel.setManaged(true);
        }

        if (fnValue == EditMode.READY) { //show edit if user clicked save / browse
            btnEdit.setVisible(true); 
            btnEdit.setManaged(true);
            btnCancelSI.setVisible(true);
            btnCancelSI.setManaged(true);
        }
        
        txtField03.setDisable(!lbShow); //dTransact
        txtField10.setDisable(!lbShow); //nDiscount
        txtField05.setDisable(true);//sReferNox
        txtField06.setDisable(true); //sSourceNo
        if (fnValue == EditMode.ADDNEW){
            txtField05.setDisable(false);//sReferNox
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
        //txtField29_2.setDisable(!lbShow); //nUnitPrce
        //txtField11.setDisable(!lbShow); //nVatRatex
        //txtField12.setDisable(!lbShow); //nVatAmtxx
        //txtField09.setDisable(!lbShow); //nTranTotl
        
    }
    
    private void setFieldsCapsLock(){
        setCapsLockBehavior(txtField05); //sReferNox
        setCapsLockBehavior(txtField06); //sSourceNo
        setCapsLockBehavior(txtField30); //sCompnyNm
        setCapsLockBehavior(txtField31); //sAddressx
        setCapsLockBehavior(txtField24); //sSalesExe
        setCapsLockBehavior(txtField19); //sCSNoxxxx
        setCapsLockBehavior(txtField20); //sPlateNox
        setCapsLockBehavior(txtField21); //sFrameNox
        setCapsLockBehavior(txtField22); //sEngineNo
        setCapsLockBehavior(textArea18); //sDescript
        setCapsLockBehavior(txtField23); //sColorDsc
//        setCapsLockBehavior(txtField29); //nUnitPrce
//        setCapsLockBehavior(txtField29_2); //nUnitPrce
//        setCapsLockBehavior(txtField11); //nVatRatex
//        setCapsLockBehavior(txtField12); //nVatAmtxx
//        setCapsLockBehavior(txtField10); //nDiscount
//        setCapsLockBehavior(txtField09); //nTranTotl
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
    
    //Animation    
    private void shakeTextField(TextField textField) {
        Timeline timeline = new Timeline();
        double originalX = textField.getTranslateX();

        // Add keyframes for the animation
        KeyFrame keyFrame1 = new KeyFrame(Duration.millis(0), new KeyValue(textField.translateXProperty(), 0));
        KeyFrame keyFrame2 = new KeyFrame(Duration.millis(100), new KeyValue(textField.translateXProperty(), -5));
        KeyFrame keyFrame3 = new KeyFrame(Duration.millis(200), new KeyValue(textField.translateXProperty(), 5));
        KeyFrame keyFrame4 = new KeyFrame(Duration.millis(300), new KeyValue(textField.translateXProperty(), -5));
        KeyFrame keyFrame5 = new KeyFrame(Duration.millis(400), new KeyValue(textField.translateXProperty(), 5));
        KeyFrame keyFrame6 = new KeyFrame(Duration.millis(500), new KeyValue(textField.translateXProperty(), -5));
        KeyFrame keyFrame7 = new KeyFrame(Duration.millis(600), new KeyValue(textField.translateXProperty(), 5));
        KeyFrame keyFrame8 = new KeyFrame(Duration.millis(700), new KeyValue(textField.translateXProperty(), originalX));

        // Add keyframes to the timeline
        timeline.getKeyFrames().addAll(
                keyFrame1, keyFrame2, keyFrame3, keyFrame4, keyFrame5, keyFrame6, keyFrame7, keyFrame8
        );

        // Play the animation
        timeline.play();
    }

    //Validation
    private void addRequiredFieldListener(TextField textField) {
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && textField.getText().isEmpty()) {
                shakeTextField(textField);
                textField.getStyleClass().add("required-field");
            } else {
                textField.getStyleClass().remove("required-field");
            }
        });
    }
    
}
