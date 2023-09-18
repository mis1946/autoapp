/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.sales;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.auto.app.views.ScreenInterface;
import org.rmj.auto.app.views.unloadForm;
/**
 * FXML Controller class
 *
 * @author Arsiela
 * Date Created 09-15-2023
 */
public class VSPFormController implements Initializable, ScreenInterface {
    private GRider oApp;
    private MasterCallback oListener;
    
    unloadForm unload = new unloadForm(); //Used in Close Button
    private final String pxeModuleName = "Vehicle Sales Proposal"; //Form Title
    private int pnEditMode;//Modifying fields
    private int lnCtr = 0;

    private String TransNo = "";

    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancelVSP;
    @FXML
    private Button btnBrowse;
    @FXML
    private Button btnClose;
    @FXML
    private TextField txtField02;
    @FXML
    private ToggleGroup tgUnitCategory;
    @FXML
    private Button btnPrint;
    
    private Stage getStage(){
          return (Stage) btnClose.getScene().getWindow();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        //Button Click Event
        btnAdd.setOnAction(this::cmdButton_Click);
        btnEdit.setOnAction(this::cmdButton_Click); 
        btnSave.setOnAction(this::cmdButton_Click); 
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
        String lsButton = ((Button)event.getSource()).getId();
        switch (lsButton){
            case "btnAdd": //create new 
                break;
            case "btnEdit": //modify 
                break;
            case "btnSave": 
                //Validate before saving
                //Proceed Saving
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
        initButton(pnEditMode);  
    }
    
    private void txtField_KeyPressed(KeyEvent event){
          TextField txtField = (TextField)event.getSource();
          int lnIndex = Integer.parseInt(((TextField)event.getSource()).getId().substring(8,10));
          
//          try{
//               switch (event.getCode()){
//                    case F3:
//                    case TAB:
//                    case ENTER:
//                         switch (lnIndex){ 
//                         break;
//               }
//          }catch(SQLException e){
//                ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
//          }
          
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
//          try{
            TextField txtField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
            int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
            String lsValue = txtField.getText();
            
            if (lsValue == null) return;
            if(!nv){ /*Lost Focus*/
                    switch (lnIndex){
                    }
                
            } else
               txtField.selectAll();
//          } catch (SQLException ex) {
//            Logger.getLogger(VehicleEntryFormController.class.getName()).log(Level.SEVERE, null, ex);
//          }
     };
    
    /*Enabling / Disabling Fields*/
    private void initButton(int fnValue){
        /* NOTE:
            lbShow (FALSE)= invisible
            !lbShow (TRUE)= visible
        */
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        //if lbShow = false hide btn          
        btnEdit.setVisible(false); 
        btnEdit.setManaged(false);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);

        if (fnValue == EditMode.READY) { //show edit if user clicked save / browse
             btnEdit.setVisible(true); 
             btnEdit.setManaged(true);
        }
    }
    
    /*Clear Fields*/
    public void clearFields(){
    }
    
}
