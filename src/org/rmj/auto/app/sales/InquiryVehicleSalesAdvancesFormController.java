/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.sales;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
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
          
          btnClose.setOnAction(this::cmdButton_Click);
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
     
     
}
