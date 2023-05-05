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
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.auto.app.views.InputTextFormatter;
import org.rmj.auto.sales.base.InquiryProcess;

/**
 * FXML Controller class
 * Date Created: 04-24-2023
 * @author Arsiela
 */
public class InquiryVehicleSalesAdvancesFormController implements Initializable {
     private GRider oApp;
     private boolean pbLoaded = false;
     private MasterCallback oListener;
     private InquiryProcess oTransProcess;
     
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
     @FXML
     private Button btnApply;
     
     public void setGRider(GRider foValue) {
        oApp = foValue;
     }
     public static void setData(InquiryTableVehicleSalesAdvances inqvsadata){
        incModel = inqvsadata;    
    }
    public void setVSAObject(InquiryProcess foValue){
        oTransProcess = foValue;
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
          oTransProcess.setCallback(oListener);
          comboBox12.setItems(cSlipType); //Slipt Type
          
          Pattern pattern;
          pattern = Pattern.compile("^\\d*(\\.\\d{0,2})?$");
          txtField05.setTextFormatter(new InputTextFormatter(pattern)); //Amount
          
          txtField05.setOnKeyPressed(this::txtField_KeyPressed);   //Amount
          textArea06.setOnKeyPressed(this::txtArea_KeyPressed);   //Remarks
          
          btnClose.setOnAction(this::cmdButton_Click);
          btnApply.setOnAction(this::cmdButton_Click);
          
          loadInquiryReservation();
          pnEditMode = oTransProcess.getEditMode();
     }
     
     //Search using F3
     private void txtField_KeyPressed(KeyEvent event){
          TextField txtField = (TextField)event.getSource();
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
     
     private void loadInquiryReservation() {
          try{
               /**
                * User can edit VSA only if not yet Approved and not Cancelled.
                *
                **/
               if(state){ //Add
                    txtField02.setText(CommonUtils.xsDateShort((Date) oApp.getServerDate()));
                    txtField13.setText("For Approval");
               } else { 
                    txtField02.setText(CommonUtils.xsDateShort((Date) oTransProcess.getInqRsv(tbl_row,2)));
                    txtField03.setText(oTransProcess.getInqRsv(tbl_row,3).toString());
                    txtField05.setText(String.format("%.2f", oTransProcess.getInqRsv(tbl_row,5)));
                    switch (oTransProcess.getInqRsv(tbl_row,13).toString()) {
                         case "0":
                              txtField13.setText("For Approval");
                              txtField05.setDisable(state);
                              comboBox12.setDisable(state);
                              break;
                         case "1":
                              txtField13.setText("Approved");
                              txtField05.setDisable(!state);
                              comboBox12.setDisable(!state);
                              
                              break; 
                         case "2":
                              txtField13.setText("Cancelled");
                              txtField05.setDisable(!state);
                              comboBox12.setDisable(!state);
                              break;
                         default:
                              txtField13.setText("");
                              break;
                    }
                    txtField14.setText((String) oTransProcess.getInqRsv(tbl_row,23));
                    //txtField15.setText( oTransProcess.getInqRsv(tbl_row,15).toString());
                    txtField15.setText(CommonUtils.xsDateShort((Date) oTransProcess.getInqRsv(tbl_row,15)));
                    textArea06.setText((String) oTransProcess.getInqRsv(tbl_row,6));
                    comboBox12.getSelectionModel().select(Integer.parseInt(oTransProcess.getInqRsv(tbl_row,12).toString())); //VSA Type
               }
          }catch (SQLException ex) {
          Logger.getLogger(InquiryVehicleSalesAdvancesFormController.class.getName()).log(Level.SEVERE, null, ex);
          }
          
     }
     
     private void cmdButton_Click(ActionEvent event) {
          try{
               String lsButton = ((Button)event.getSource()).getId();
               switch (lsButton){
                    case "btnClose":
                         CommonUtils.closeStage(btnClose);
                         break;
                    case "btnApply":
                         if(state){
                              oTransProcess.addReserve();
                         }
                         if (setSelection()){
                              oTransProcess.setInqRsv(tbl_row, 2,SQLUtil.toDate(txtField02.getText(), SQLUtil.FORMAT_SHORT_DATE));
                              oTransProcess.setInqRsv(tbl_row, 5,Double.valueOf(txtField05.getText()));
                              oTransProcess.setInqRsv(tbl_row, 6,textArea06.getText());
                         }
                         CommonUtils.closeStage(btnApply);
                         break;

                    default:
                        ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                        break; 
               }
          }catch (SQLException ex) {
          Logger.getLogger(InquiryVehicleSalesAdvancesFormController.class.getName()).log(Level.SEVERE, null, ex);
          }   
    } 
     
     /*Convert Date to String*/
     private LocalDate strToDate(String val){
          DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
          LocalDate localDate = LocalDate.parse(val, date_formatter);
          return localDate;
     }
     
     /*Set TextField Value to Master Class*/
//     final ChangeListener<? super Boolean> txtField_Focus = (o,ov,nv)->{
//          try{
//            TextField txtField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
//            int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
//            String lsValue = txtField.getText();
//            
//            if (lsValue == null) return;
//            if(!nv){ /*Lost Focus*/
//                    switch (lnIndex){
//                         case 2: //
//                         case 5: //
//                         case 6: //
//                              oTrans.setMaster(lnIndex, lsValue); //Handle Encoded Value
//                              break;
//                    }
//                
//            } else
//               txtField.selectAll();
//          } catch (SQLException ex) {
//            Logger.getLogger(InquiryFormController.class.getName()).log(Level.SEVERE, null, ex);
//          }
//     };
     
     /*Set TextArea to Master Class*/
//     final ChangeListener<? super Boolean> txtArea_Focus = (o,ov,nv)->{
//          TextArea txtField = (TextArea)((ReadOnlyBooleanPropertyBase)o).getBean();
//          int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
//          String lsValue = txtField.getText();
//          
//          if (lsValue == null) return;
//          try {
//             if(!nv){ /*Lost Focus*/
//               switch (lnIndex){
//                   case 6:
//                      oTrans.setMaster(lnIndex, lsValue); break;
//               }
//             } else
//                 txtField.selectAll();
//          } catch (SQLException e) {
//             ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
//             System.exit(1);
//          }
//     };
     
     /*Set ComboBox Value to Master Class*/ 
     @SuppressWarnings("ResultOfMethodCallIgnored")
     private boolean setSelection(){
          try {
               if (comboBox12.getSelectionModel().getSelectedIndex() < 0){
                   ShowMessageFX.Warning("No `Slip Type` selected.", pxeModuleName, "Please select `Slip Type` value.");
                   comboBox12.requestFocus();
                   return false;
               }else 
                    oTransProcess.setInqRsv(tbl_row, 12,comboBox12.getSelectionModel().getSelectedIndex());
               
          } catch (SQLException ex) {
               ShowMessageFX.Warning(getStage(),ex.getMessage(), "Warning", null);
          }
          return true;
     }
     
     
}
