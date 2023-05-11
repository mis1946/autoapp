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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import javafx.scene.input.KeyEvent;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.auto.sales.base.InquiryBankApplication;
import org.rmj.auto.sales.base.InquiryFollowUp;
import org.rmj.auto.sales.base.InquiryMaster;

/**
 * FXML Controller class
 * Date Created: 04-24-2023
 * @author Arsiela
 */
public class InquiryFollowUpFormController implements Initializable {
    private GRider oApp;
    private boolean pbLoaded = false;
    private MasterCallback oListener;
    private InquiryFollowUp oTransFollowup;

    private String sTransNo;
    private boolean state = false;

    private final String pxeModuleName = "Inquiry Follow-Up";
    ObservableList<String> cMedium = FXCollections.observableArrayList( "Text", "Call", "Social Media" , "Email", "WhatsUp", "Viber"); 
    ObservableList<String> cPlatforms = FXCollections.observableArrayList("Facebook", "WhatsUp", "Instagram", "Tiktok", "Twitter");
    
    @FXML
    private Button btnClose;
    @FXML
    private Button btnSave;
    @FXML
    private ComboBox comboBox06;
    @FXML
    private DatePicker txtField08;
    @FXML
    private ComboBox comboBox07;
    @FXML
    private DatePicker txtField03;
    @FXML
    private TextArea textArea04;
    @FXML
    private TextArea textArea05;
     
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }
    
    public void setObject(InquiryFollowUp foValue){
       oTransFollowup = foValue;
    }
    public void setState(boolean fsValue){
       state = fsValue;
    }
    public void setsTransNo(String fsValue){
       sTransNo = fsValue;
    }


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboBox06.setItems(cMedium);      
        comboBox07.setItems( cPlatforms);
        comboBox06.setOnAction(event -> {
            comboBox07.setDisable(false);
        });
        
        textArea04.focusedProperty().addListener(txtArea_Focus);  //Remarks
        textArea05.focusedProperty().addListener(txtArea_Focus);  //Follow up abouts
        txtField03.setOnAction(this::getDate); //Follow up Date
        txtField08.setOnAction(this::getDate); //Next Follow up Date
        
        textArea04.setOnKeyPressed(this::txtArea_KeyPressed);  //Remarks
        textArea05.setOnKeyPressed(this::txtArea_KeyPressed);  //Follow up abouts
        txtField03.setOnKeyPressed(this::txtField_KeyPressed);  //Applied Date
        txtField08.setOnKeyPressed(this::txtField_KeyPressed);  //Approved Date

        btnClose.setOnAction(this::cmdButton_Click);
        btnSave.setOnAction(this::cmdButton_Click);
        loadFollowUp();
        initbutton();
    }  

    private void cmdButton_Click(ActionEvent event) {
//          try{
            String lsButton = ((Button)event.getSource()).getId();
            switch (lsButton){
                case "btnClose":
                    CommonUtils.closeStage(btnClose);
                   break;

                default:
                    ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                    break;
            }
//          }catch (SQLException ex) {
//          Logger.getLogger(InquiryVehicleSalesAdvancesFormController.class.getName()).log(Level.SEVERE, null, ex);
//          }   
    }
    
     public void loadFollowUp(){
//        try{
            /**
             * User can edit only if not 
             * Inquiry is not Lost Sale / Sold / Cancelled.
             **/ 
//            txtField03.setValue(strToDate(CommonUtils.xsDateShort((Date) oTransBankApp.getBankApp(2))));
//            txtField08.setValue(strToDate(CommonUtils.xsDateShort((Date) oTransBankApp.getBankApp(3))));
//            txtField16.setText(oTransBankApp.getBankApp(16).toString()); //Bank Name
//            txtField18.setText(oTransBankApp.getBankApp(18).toString()); //Bank Branch
//            if (pnEditMode == EditMode.ADDNEW){
//                comboBox04.getSelectionModel().select(pnInqPayMode); //Payment Mode
//            } else { 
//                comboBox04.getSelectionModel().select(Integer.parseInt(oTransBankApp.getBankApp(4).toString())); //Payment Mode
//            }
//            if (Integer.parseInt(oTransBankApp.getBankApp(9).toString()) == 3){
//                comboBox09.setValue("Cancelled");
//                pnEditMode = EditMode.UNKNOWN;
//            } else {
//                comboBox09.getSelectionModel().select(Integer.parseInt(oTransBankApp.getBankApp(9).toString())); //Bank Application Status
//            }
//            textArea08.setText(oTransBankApp.getBankApp(8).toString()); //Remarks
//                
//        }catch (SQLException ex) {
//        Logger.getLogger(InquiryVehicleSalesAdvancesFormController.class.getName()).log(Level.SEVERE, null, ex);
//        }

    }
     //Search using F3
    private void txtField_KeyPressed(KeyEvent event){
//        TextField txtField = (TextField)event.getSource();
//        int lnIndex = Integer.parseInt(((TextField)event.getSource()).getId().substring(8,10));
//          
//        try{
//            switch (event.getCode()){
//                case F3:
//                case TAB:
//                case ENTER:
//                    switch (lnIndex){ 
//                        case 16: //Bank Name
//                            if (oTransBankApp.searchBank(txtField16.getText(), false)){
//                                loadBankApplication();
//                            } else 
//                                ShowMessageFX.Warning(getStage(), oTransBankApp.getMessage(),"Warning", null);
//                        break;
//                    }
//               }
//            }catch(SQLException e){
//                  ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
//            }
//        switch (event.getCode()){
//        case ENTER:
//        case DOWN:
//            CommonUtils.SetNextFocus(txtField);
//            break;
//        case UP:
//            CommonUtils.SetPreviousFocus(txtField);
//        }
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
//        try {
//            DatePicker datePicker = (DatePicker) event.getSource();
//            String datePickerId = datePicker.getId();
//            System.out.println(SQLUtil.toDate(txtField02.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
//            System.out.println(SQLUtil.toDate(txtField03.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
//            
//            switch (datePickerId) {
//                case "txtField02":
//                    oTransBankApp.setBankApp(2,SQLUtil.toDate(txtField02.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
//                    break;
//                case "txtField03":
//                    oTransBankApp.setBankApp(3,SQLUtil.toDate(txtField03.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
//                    break;
//                default:
//                    break;
//            }
//        }catch (SQLException ex) {
//            Logger.getLogger(InquiryFormController.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
    /*Set TextField Value to Master Class*/
    final ChangeListener<? super Boolean> txtField_Focus = (o,ov,nv)->{
//        try{
//            TextField txtField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
//            int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
//            String lsValue = txtField.getText();
//
//            if (lsValue == null) return;
//            if(!nv){ /*Lost Focus*/
//                switch (lnIndex){
//                    case 16: //
//                    case 18: //
//                        oTransBankApp.setBankApp(lnIndex, lsValue); //Handle Encoded Value
//                        break;
//                }
//
//            } else
//               txtField.selectAll();
//        } catch (SQLException ex) {
//          Logger.getLogger(InquiryFormController.class.getName()).log(Level.SEVERE, null, ex);
//        }
    };
     
    /*Set TextArea to Master Class*/
    final ChangeListener<? super Boolean> txtArea_Focus = (o,ov,nv)->{
//        TextArea txtField = (TextArea)((ReadOnlyBooleanPropertyBase)o).getBean();
//        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
//        String lsValue = txtField.getText();
//
//        if (lsValue == null) return;
//        try {
//           if(!nv){ /*Lost Focus*/
//                switch (lnIndex){
//                    case 8:
//                       oTransBankApp.setBankApp(lnIndex, lsValue); //Handle Encoded Value
//                    break;
//                }
//            } else
//                txtField.selectAll();
//        } catch (SQLException e) {
//           ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
//           System.exit(1);
//        }
    };
     
    /*Set ComboBox Value to Master Class*/ 
    @SuppressWarnings("ResultOfMethodCallIgnored")
//    private boolean setSelection(){
//        try {
//            if (comboBox04.getSelectionModel().getSelectedIndex() < 0){
//                ShowMessageFX.Warning("No `Payment Mode` selected.", pxeModuleName, "Please select `Payment Mode` value.");
//                comboBox04.requestFocus();
//                return false;
//            }else 
//                oTransBankApp.setBankApp(4,comboBox04.getSelectionModel().getSelectedIndex());
//            
//            if (comboBox09.getSelectionModel().getSelectedIndex() < 0){
//                ShowMessageFX.Warning("No `Application Status` selected.", pxeModuleName, "Please select `Application Status` value.");
//                comboBox09.requestFocus();
//                return false;
//            }else 
//                oTransBankApp.setBankApp(9,comboBox09.getSelectionModel().getSelectedIndex());
//
//        } catch (SQLException ex) {
//             ShowMessageFX.Warning(getStage(),ex.getMessage(), "Warning", null);
//        }
//         return true;
//    }
     
    public void initbutton(){
//        txtField16.setDisable(!lbShow); //Bank Name
//        comboBox04.setDisable(!lbShow); //Payment Mode
//        comboBox09.setDisable(!lbShow); //Application Status
//        txtField02.setDisable(!lbShow); //Applied Date
//        txtField03.setDisable(!lbShow); //Approved Date
//        textArea08.setDisable(!lbShow); //Remarks
//        btnSave.setDisable(!lbShow);
    }
     
}
