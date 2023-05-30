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
import org.rmj.auto.sales.base.InquiryFollowUp;

/**
 * FXML Controller class
 * Date Created: 04-24-2023
 * @author Arsiela
 */
public class InquiryFollowUpFormController implements Initializable {
    private GRider oApp;
    private boolean pbLoaded = false;
    private MasterCallback oListener;
    private InquiryFollowUp oTransFollowUp;

    private String sTransNo;
    private String sSourceNo;
    private boolean state = false;

    private final String pxeModuleName = "Inquiry Follow-Up";
    ObservableList<String> cMedium = FXCollections.observableArrayList( "TEXT", "CALL", "SOCIAL MEDIA" , "EMAIL", "WHATSAPP", "VIBER"); 
    //ObservableList<String> cPlatforms = FXCollections.observableArrayList("Facebook", "WHATSAPP", "Instagram", "Tiktok", "Twitter");
    
    @FXML
    private Button btnClose;
    @FXML
    private Button btnSave;
    @FXML
    private ComboBox comboBox06;
    @FXML
    private DatePicker txtField08;
    @FXML
    private TextField txtField03;
    @FXML
    private TextArea textArea04;
    @FXML
    private TextArea textArea05;
    @FXML
    private TextField txtField07;
     
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }
    
    public void setObject(InquiryFollowUp foValue){
       oTransFollowUp = foValue;
    }
    public void setState(boolean fsValue){
       state = fsValue;
    }
    public void setsTransNo(String fsValue){
       sTransNo = fsValue;
    }
    
    public void setsSourceNo(String fsValue){
       sSourceNo = fsValue;
    }
    
    private Stage getStage(){
        return (Stage) txtField03.getScene().getWindow();
    }


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (state) {
            oTransFollowUp.loadFollowUp(sTransNo, false);
            
        }
        comboBox06.setItems(cMedium); 
        comboBox06.setOnAction(event -> {
            if (comboBox06.getSelectionModel().getSelectedIndex() == 2) {
                txtField07.setDisable(false);
                txtField07.clear();
            }
            
            try {
                oTransFollowUp.setFollowUp(6,comboBox06.getValue().toString());
            } catch (SQLException ex) {
                Logger.getLogger(InquiryFollowUpFormController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        });
        setCapsLockBehavior(txtField03);
        setCapsLockBehavior(txtField07);
        setCapsLockBehavior(textArea04);
        setCapsLockBehavior(textArea05);
        
        txtField07.focusedProperty().addListener(txtField_Focus);  //Platform
        textArea04.focusedProperty().addListener(txtArea_Focus);  //Remarks
        textArea05.focusedProperty().addListener(txtArea_Focus);  //Follow up abouts
        txtField08.setOnAction(this::getDate); //Next Follow up Date
        txtField08.setDayCellFactory(callDate);
        textArea04.setOnKeyPressed(this::txtArea_KeyPressed);  //Remarks
        textArea05.setOnKeyPressed(this::txtArea_KeyPressed);  //Follow up abouts
        txtField07.setOnKeyPressed(this::txtField_KeyPressed); //Platform
        
        btnClose.setOnAction(this::cmdButton_Click);
        btnSave.setOnAction(this::cmdButton_Click);
        
        loadFollowUp();
        initbutton();
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
            case "btnSave":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to save?")) {
                } else {
                    return;
                }
                
                if (strToDate(CommonUtils.xsDateShort((Date)oApp.getServerDate())).equals(
                    strToDate(CommonUtils.xsDateShort((Date) SQLUtil.toDate(txtField08.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE))))    
                    ) {
                    ShowMessageFX.Warning(null, pxeModuleName, "You cannot save a Follow-Up entry with the same Follow-Up Date as the Next Follow-Up Date.");
                    txtField08.requestFocus();
                    return;
                }
                
                if (textArea05.getText().length() < 20){
                    ShowMessageFX.Warning(null, pxeModuleName, "Please enter at least 20 characters.");
                    textArea05.requestFocus();
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
                        ShowMessageFX.Information(null, pxeModuleName, oTransFollowUp.getMessage());
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, "Failed to Save Follow Up.");
                        return;
                    }
                } else {
                    return;
                }
                CommonUtils.closeStage(btnSave);
               break;
            case "btnClose":
                CommonUtils.closeStage(btnClose);
               break;

            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;
        }
    }
    
     public void loadFollowUp(){
        try{
            /**
             * User can edit only if not 
             * Inquiry is not Lost Sale / Sold / Cancelled.
             **/ 
            txtField03.setText(CommonUtils.xsDateMedium((Date) oTransFollowUp.getFollowUp(3)));
            txtField08.setValue(strToDate(CommonUtils.xsDateShort((Date) oTransFollowUp.getFollowUp(8))));
            //comboBox06.getSelectionModel().select(Integer.parseInt(oTransFollowUp.getFollowUp(6).toString())); 
            comboBox06.setValue(oTransFollowUp.getFollowUp(6).toString()); 
            System.out.println("sPlatform "+ oTransFollowUp.getFollowUp(16).toString());
            System.out.println("a.sSclMedia "+ oTransFollowUp.getFollowUp(7).toString());
            
            txtField07.setText(oTransFollowUp.getFollowUp(16).toString()); 
            textArea05.setText(oTransFollowUp.getFollowUp(5).toString()); 
            textArea04.setText(oTransFollowUp.getFollowUp(4).toString()); 
                
        }catch (SQLException ex) {
        Logger.getLogger(InquiryFollowUpFormController.class.getName()).log(Level.SEVERE, null, ex);
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
                    case 7: //
                        oTransFollowUp.setFollowUp(16, lsValue); //Handle Encoded Value
                        break;
                }

            } else
               txtField.selectAll();
        } catch (SQLException ex) {
          Logger.getLogger(InquiryFollowUpFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    };
    
    //Search using F3
    private void txtField_KeyPressed(KeyEvent event){
        TextField txtField = (TextField)event.getSource();
        int lnIndex = Integer.parseInt(((TextField)event.getSource()).getId().substring(8,10));
          
        try{
            switch (event.getCode()){
                case F3:
                case TAB:
                case ENTER:
                    switch (lnIndex){ 
                        case 7: //Platform
                            if (oTransFollowUp.searchPlatform(txtField07.getText(), false)){
                                loadFollowUp();
                            } else 
                                ShowMessageFX.Warning(getStage(), oTransFollowUp.getMessage(),"Warning", null);
                        break;
                    }
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
    
    /*Convert Date to String*/
    private LocalDate strToDate(String val){
        DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(val, date_formatter);
        return localDate;
    }
     
    /*Set Date Value to Master Class*/
    public void getDate(ActionEvent event) { 
        try {
            oTransFollowUp.setFollowUp(8,SQLUtil.toDate(txtField08.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
        }catch (SQLException ex) {
            Logger.getLogger(InquiryFollowUpFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*Set TextArea to Master Class*/
    final ChangeListener<? super Boolean> txtArea_Focus = (o,ov,nv)->{
        TextArea txtField = (TextArea)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();

        if (lsValue == null) return;
        try {
           if(!nv){ /*Lost Focus*/
                switch (lnIndex){
                    case 4:
                    case 5:
                       oTransFollowUp.setFollowUp(lnIndex, lsValue); //Handle Encoded Value
                    break;
                }
            } else
                txtField.selectAll();
        } catch (SQLException e) {
           ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
           System.exit(1);
        }
    };
     
    /*Set ComboBox Value to Master Class*/ 
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private boolean setSelection(){
        try {
            if (comboBox06.getSelectionModel().getSelectedIndex() < 0){
                ShowMessageFX.Warning("No `Medium Used` selected.", pxeModuleName, "Please select `Medium Used` value.");
                comboBox06.requestFocus();
                return false;
            }else 
                //oTransFollowUp.setFollowUp(6,comboBox06.getSelectionModel().getSelectedIndex());
                oTransFollowUp.setFollowUp(6,comboBox06.getValue().toString());
            
//            if (comboBox07.getSelectionModel().getSelectedIndex() == 2){
//                if (comboBox07.getSelectionModel().getSelectedIndex() < 0){
//                    ShowMessageFX.Warning("No `Social Media` selected.", pxeModuleName, "Please select `Social Media` value.");
//                    comboBox07.requestFocus();
//                    return false;
//                }else 
//                    oTransFollowUp.setFollowUp(7,comboBox07.getSelectionModel().getSelectedIndex());
//            }
        } catch (SQLException ex) {
             ShowMessageFX.Warning(getStage(),ex.getMessage(), "Warning", null);
        }
         return true;
    }
     
    public void initbutton(){
        txtField08.setDisable(state); //Next Follow Date
        comboBox06.setDisable(state); //Medium Used
        txtField07.setDisable(true); //Platforms
        textArea05.setDisable(state); //Follow up About
        textArea04.setDisable(state); //Remarks
        btnSave.setDisable(state);
    }
    
     private Callback<DatePicker, DateCell> callDate = new Callback<DatePicker, DateCell>() {
        @Override
        public DateCell call(final DatePicker param) {
            return new DateCell() {
                @Override
                public void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                    LocalDate today = LocalDate.now();
                    setDisable(empty || item.compareTo(today) < 0 );
                    
                }
            };
        }
    };
    
}
