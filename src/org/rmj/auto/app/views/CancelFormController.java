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

/**
 * FXML Controller class
 *
 * @author Arsiela
 * Date Created: 05-20-2023
 */
public class CancelFormController implements Initializable {
    private GRider oApp;
    private MasterCallback oListener;
    private CancellationMaster oTrans;
    private boolean state;

    private String sSourceNox;
    private String sTransNo;
    private String sSourceCD;

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
    
    public void setsSourceNox(String fsValue){
       sSourceNox = fsValue;
    }
    
    public void setsSourceCD(String fsValue){
       sSourceCD = fsValue;
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
        oListener = (int fnIndex, Object foValue) -> {
            System.out.println("Set Class Value "  + fnIndex + "-->" + foValue);
        };

        oTrans = new CancellationMaster(oApp, oApp.getBranchCode(), true); //Initialize ClientMaster
        oTrans.setCallback(oListener);
        oTrans.setWithUI(true);
        
        lblFormNo.setText(sTransNo);
        setCapsLockBehavior(textArea01);
        
        Pattern pattern;
        pattern = Pattern.compile("^[a-zA-Z0-9 ]+$");
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
                
                if (oTrans.CancelForm(sTransNo, textArea01.getText(), sSourceCD, sSourceNox)){
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
