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
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
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
import org.rmj.auto.app.views.InputTextFormatter;
import org.rmj.auto.sales.base.InquiryFollowUp;

/**
 * FXML Controller class
 * Date Created: 04-24-2023
 * @author Arsiela
 */
public class InquiryLostSaleFormController implements Initializable {
    private GRider oApp;
    private boolean pbLoaded = false;
    private MasterCallback oListener;
    private InquiryFollowUp oTransFollowUp;

    private String sSourceNo;
    private String sClient;
    private int iTag;
    private int iGdsCt;
    private boolean state = false;

    private final String pxeModuleName = "Inquiry Lost Sale Remarks Form";
    ObservableList<String> cTag = FXCollections.observableArrayList("CHANGE OF UNIT", "LOST SALE"); 
    ObservableList<String> cReasons = FXCollections.observableArrayList("BOUGHT FROM COMPETITOR BRAND", "BOUGHT FROM OTHER DEALER", "BOUGHT SECOND HAND VEHICLE", "LACK OF REQUIREMENTS", "NO BUDGET"); 
    ObservableList<String> cGdsCat = FXCollections.observableArrayList("BRAND NEW", "PRE-OWNED"); 

    @FXML
    private Button btnTlost;
    @FXML
    private Button btnDlost;
    @FXML
    private Label lblClientName; //Client Name
    @FXML
    private ComboBox comboBox04; //concat to sRemarksx 
    @FXML
    private ComboBox comboBox13; //sRspnseCd
    @FXML
    private TextField txtField11; //sMkeCmptr
    @FXML
    private TextField txtField12; //sDlrCmptr
    @FXML
    private TextArea textArea04; //sRemarksx 
    @FXML
    private ComboBox comboBox10; //sGdsCmptr
    
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }
    public void setObject(InquiryFollowUp foValue){
       oTransFollowUp = foValue;
    }
    public void setState(boolean fsValue){
       state = fsValue;
    }
    public void setsSourceNo(String fsValue){
       sSourceNo = fsValue;
    }
    public void setClientName(String fsValue){
       sClient = fsValue;
    }
    
    private Stage getStage(){
        return (Stage) btnTlost.getScene().getWindow();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Pattern pattern;
        pattern = Pattern.compile("^[a-zA-Z0-9 ]+$");
        textArea04.setTextFormatter(new InputTextFormatter(pattern));
        
        lblClientName.setText(sClient); //Client Name
        
        comboBox04.setItems(cTag); 
        comboBox13.setItems(cReasons); 
        comboBox10.setItems(cGdsCat); 
        
        if(state){
            comboBox04.getSelectionModel().select(1); //Tag
            comboBox04.setDisable(state);
        }
        
        comboBox13.setOnAction(event -> {
            comboBox10.setValue("");
            txtField11.setText("");
            txtField12.setText("");
            switch(comboBox13.getSelectionModel().getSelectedIndex()){
                case 0:
                case 1:
                    comboBox10.setDisable(false);
                    txtField11.setDisable(false);
                    txtField12.setDisable(false);
                break;
                case 2:
                    comboBox10.getSelectionModel().select(1);
                    comboBox10.setDisable(true);
                    txtField11.setDisable(false);
                    txtField12.setDisable(false);
                break;
                case 3:
                case 4:
                    comboBox10.setDisable(true);
                    txtField11.setDisable(true);
                    txtField12.setDisable(true);
                break;
            }
            
        });
        
        setCapsLockBehavior(txtField11);
        setCapsLockBehavior(txtField12);
        setCapsLockBehavior(textArea04);
        
        txtField11.focusedProperty().addListener(txtField_Focus); //sMkeCmptr
        txtField12.focusedProperty().addListener(txtField_Focus); //sDlrCmptr
        textArea04.focusedProperty().addListener(txtArea_Focus); //sRemarksx 
        txtField11.setOnKeyPressed(this::txtField_KeyPressed); //sMkeCmptr
        txtField12.setOnKeyPressed(this::txtField_KeyPressed); //sDlrCmptr
        textArea04.setOnKeyPressed(this::txtArea_KeyPressed);  //sRemarksx 
        
        btnTlost.setOnAction(this::cmdButton_Click);
        btnDlost.setOnAction(this::cmdButton_Click);
        
        if (oTransFollowUp.NewRecord()){
        } else {
            ShowMessageFX.Warning(null, pxeModuleName, oTransFollowUp.getMessage());
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
        String lsButton = ((Button)event.getSource()).getId();
        switch (lsButton){
            case "btnTlost":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to tag this inquiry as " + comboBox04.getValue().toString()+ " ?")) {
                } else {
                    return;
                }
                
                if (textArea04.getText().length() < 20){
                    ShowMessageFX.Warning(null, pxeModuleName, "Please enter at least 20 characters.");
                    textArea04.requestFocus();
                    return;
                }
                
                if (setSelection()){
                    oTransFollowUp.setTransNox(sSourceNo);
                    if (oTransFollowUp.SaveRecord()){
                        try {
                            if(oTransFollowUp.LostSale()){
                            }else {
                                ShowMessageFX.Warning(null, pxeModuleName, oTransFollowUp.getMessage());
                                return;
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(InquiryLostSaleFormController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        ShowMessageFX.Information(null, pxeModuleName, oTransFollowUp.getMessage());
                    } else {
                        //ShowMessageFX.Warning(null, pxeModuleName, "Failed to Save Inquiry Lost Sales Remarks Form.");
                        ShowMessageFX.Warning(null, pxeModuleName, oTransFollowUp.getMessage());
                        return;
                    }
                } else {
                    return;
                }
                CommonUtils.closeStage(btnTlost);
               break;
            case "btnDlost":
                CommonUtils.closeStage(btnDlost);
               break;

            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;
        }
    }
     
    /*Set TextField Value to Master Class*/
    final ChangeListener<? super Boolean> txtField_Focus = (o,ov,nv)->{
        try{
            TextField txtField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
            int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
            String lsValue = txtField.getText();

            if (lsValue == null) return;
            if(!nv){ /*Lost Focus*/
                switch (lnIndex){
                    case 11: //sMkeCmptr
                    case 12: //sDlrCmptr
                        oTransFollowUp.setFollowUp(lnIndex, lsValue); //Handle Encoded Value
                        break;
                }

            } else
               txtField.selectAll();
        } catch (SQLException ex) {
          Logger.getLogger(InquiryLostSaleFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    };
    /*Set TextArea to Master Class*/
    final ChangeListener<? super Boolean> txtArea_Focus = (o,ov,nv)->{
        TextArea txtField = (TextArea)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();

        if (lsValue == null) return;
        try {
           if(!nv){ /*Lost Focus*/
                switch (lnIndex){
                    case 4://sRemarksx 
                       oTransFollowUp.setFollowUp(lnIndex, comboBox04.getValue().toString() + " " + lsValue); //Handle Encoded Value
                    break;
                }
            } else
                txtField.selectAll();
        } catch (SQLException e) {
           ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
           System.exit(1);
        }
    };
    
    //Search using F3
    private void txtField_KeyPressed(KeyEvent event){
        TextField txtField = (TextField)event.getSource();
        int lnIndex = Integer.parseInt(((TextField)event.getSource()).getId().substring(8,10));
        switch (event.getCode()){
        case ENTER:
        case DOWN:
            CommonUtils.SetNextFocus(txtField);
            break;
        case UP:
            CommonUtils.SetPreviousFocus(txtField);
        }
    }
    
    /*TRIGGER FOCUS*/
    private void txtArea_KeyPressed(KeyEvent event){
        if (event.getCode() == ENTER || event.getCode() == DOWN){ 
            event.consume();
            CommonUtils.SetNextFocus((TextArea)event.getSource());
        }else if (event.getCode() ==KeyCode.UP){
        event.consume();
            CommonUtils.SetPreviousFocus((TextArea)event.getSource());
        }
    }
     
    /*Set ComboBox Value to Master Class*/ 
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private boolean setSelection(){
        try {
            if (comboBox04.getSelectionModel().getSelectedIndex() < 0){
                ShowMessageFX.Warning("No `Tag` selected.", pxeModuleName, "Please select `Tag` value.");
                comboBox04.requestFocus();
                return false;
            }else { 
                oTransFollowUp.setFollowUp(4,comboBox04.getValue().toString());
                if (comboBox13.getSelectionModel().getSelectedIndex() < 0){
                    ShowMessageFX.Warning("No `Reason` selected.", pxeModuleName, "Please select `Reason` value.");
                    comboBox13.requestFocus();
                    return false;
                }else{ 
                    oTransFollowUp.setFollowUp(13,comboBox13.getValue().toString());
                }
            }
            if ( (comboBox13.getSelectionModel().getSelectedIndex() != 4) && (comboBox13.getSelectionModel().getSelectedIndex() != 3) ){
                if (comboBox10.getSelectionModel().getSelectedIndex() < 0){
                    ShowMessageFX.Warning("No `Goods Category` selected.", pxeModuleName, "Please select `Goods Category` value.");
                    comboBox10.requestFocus();
                    return false;
                }else {
                    oTransFollowUp.setFollowUp(10,comboBox10.getValue().toString());
                }
            }
        } catch (SQLException ex) {
             ShowMessageFX.Warning(getStage(),ex.getMessage(), "Warning", null);
        }
         return true;
    }
     

}
