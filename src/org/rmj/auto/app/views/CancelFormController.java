/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.views;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.auto.parameters.CancellationMaster;
import org.rmj.auto.sales.base.InquiryFollowUp;

/**
 * FXML Controller class
 *
 * @author Arsiela
 * Date Created: 05-20-2023
 */
public class CancelFormController implements Initializable {
    private GRider oApp;
    private MasterCallback oListener;
    private boolean state;
    private CancellationMaster oTrans;

    private String sSourceNo;
    private String sTransNo;

    private final String pxeModuleName = "Cancellation Remarks";  
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnDCancel;
    @FXML
    private Label lblFormNo;
    @FXML
    private TextArea textArea01;
    
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }
    
    public void setsSourceNo(String fsValue){
       sSourceNo = fsValue;
    }
    
    public void setTransNo(String fsValue){
       sTransNo = fsValue;
    }
    
    public boolean setState(){
       return state;
    }
    
    private Stage getStage(){
        return (Stage) btnCancel.getScene().getWindow();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblFormNo.setText(sTransNo);
        setCapsLockBehavior(textArea01);
        
        Pattern pattern;
        pattern = Pattern.compile("^[a-zA-Z0-9]+$");
        textArea01.setTextFormatter(new InputTextFormatter(pattern));
        
        btnCancel.setOnAction(this::cmdButton_Click);
        btnDCancel.setOnAction(this::cmdButton_Click);
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
            case "btnCancel":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to cancel?")) {
                } else {
                    return;
                }
                
                if (textArea01.getText().length() < 20){
                    ShowMessageFX.Warning(null, pxeModuleName, "Please enter at least 20 characters.");
                    textArea01.requestFocus();
                    return;
                }
                
                if (oTrans.CancelForm(sTransNo, textArea01.getText(), sSourceNo, sSourceNo)){
                    state = true;
                } else {
                    return;
                }
                CommonUtils.closeStage(btnCancel);
                break;
            case "btnDCancel":
                state = false;
                CommonUtils.closeStage(btnDCancel);
               break;

            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;
        }
    }
     
    
}
