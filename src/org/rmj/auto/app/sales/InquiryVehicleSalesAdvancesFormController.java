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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.auto.app.views.InputTextFormatter;
import org.rmj.auto.sales.base.InquiryMaster;

/**
 * FXML Controller class
 * Date Created: 04-24-2023
 * @author Arsiela
 */
public class InquiryVehicleSalesAdvancesFormController implements Initializable {
     private GRider oApp;
     private boolean pbLoaded = false;
     private MasterCallback oListener;
     private InquiryMaster oTrans;
     
     public int tbl_row = 0;
     private int pnIndex = -1;    
     private int pnRow = -1;
     private int pnEmp = -1;
     private int lnCtr;
     private String oTransnox = "";
     private String psOldRec;
     private String psCode;
     private int pnEditMode;
     private boolean state = false;
    
     private final String pxeModuleName = "Inquiry Vehicle Sales Advances";
     public static InquiryTableVehicleSalesAdvances incModel;
     
     ObservableList<String> cSlipType = FXCollections.observableArrayList("Reservation", "Deposit", "Safeguard Duty");
     
     @FXML
     private Button btnAdd;
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
     
     public void setGRider(GRider foValue) {
        oApp = foValue;
     }
     public static void setData(InquiryTableVehicleSalesAdvances inqvsadata){
        incModel = inqvsadata;    
    }
    public void setVSAObject(InquiryMaster foValue){
        oTrans = foValue;
    }
    
    public void setDeductionCode(String fsValue){
        psCode = fsValue;
    }
    public void setTableRows(int row){
        tbl_row = row;
    }

    public void setState(boolean fsValue){
        state = fsValue;
    }
    private Stage getStage(){
          return (Stage) txtField02.getScene().getWindow();
     }

     /**
      * Initializes the controller class.
      */
     @Override
     public void initialize(URL url, ResourceBundle rb) {
          // TODO
          comboBox12.setItems(cSlipType); //Slipt Type
          
          Pattern pattern;
          pattern = Pattern.compile("^\\d*(\\.\\d{0,2})?$");
          txtField05.setTextFormatter(new InputTextFormatter(pattern)); //Amount
          
          txtField02.focusedProperty().addListener(txtField_Focus);  
          txtField05.focusedProperty().addListener(txtField_Focus);
          textArea06.focusedProperty().addListener(txtArea_Focus);
          
          btnClose.setOnAction(this::cmdButton_Click);
          
          loadInquiryReservation();
     }
     
     private void loadInquiryReservation() {
          /**
           * User can edit VSA only if not yet Approved and not Cancelled.
           *
           **/
          if(state){ //Add
               txtField02.setText(CommonUtils.xsDateShort((Date) oApp.getServerDate()));
          } else { 
               
               
          
          }
          
     }
     
     private void cmdButton_Click(ActionEvent event) {
//          try{
               String lsButton = ((Button)event.getSource()).getId();
               switch (lsButton){
                    case "btnClose":
                         CommonUtils.closeStage(btnClose);
                         break;
                    case "btnAdd":
                         CommonUtils.closeStage(btnAdd);
                         break;

                    default:
                        ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                        break; 
               }
//          }catch (SQLException ex) {
//          Logger.getLogger(InquiryVehicleSalesAdvancesFormController.class.getName()).log(Level.SEVERE, null, ex);
//          }   
    } 
     
     /*Convert Date to String*/
     private LocalDate strToDate(String val){
          DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
          LocalDate localDate = LocalDate.parse(val, date_formatter);
          return localDate;
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
                         case 2: //
                         case 5: //
                         case 6: //
                              oTrans.setMaster(lnIndex, lsValue); //Handle Encoded Value
                              break;
                    }
                
            } else
               txtField.selectAll();
          } catch (SQLException ex) {
            Logger.getLogger(InquiryFormController.class.getName()).log(Level.SEVERE, null, ex);
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
                   case 6:
                      oTrans.setMaster(lnIndex, lsValue); break;
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
               if (comboBox12.getSelectionModel().getSelectedIndex() < 0){
                   ShowMessageFX.Warning("No `Slip Type` selected.", pxeModuleName, "Please select `Slip Type` value.");
                   comboBox12.requestFocus();
                   return false;
               }else 
                  oTrans.setMaster(12, String.valueOf(comboBox12.getSelectionModel().getSelectedIndex()));
               
          } catch (SQLException ex) {
          ShowMessageFX.Warning(getStage(),ex.getMessage(), "Warning", null);
          }
          return true;
     }
     
     
}
