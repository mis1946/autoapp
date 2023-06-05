/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.views;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.auto.parameters.VehicleEngineFrame;

/**
 * FXML Controller class
 *
 * @author Arsiela
 * Date Created: 06-05-2023
 */
public class VehicleEngineFrameFormatFormController implements Initializable {
    private GRider oApp;
    private MasterCallback oListener;
    private VehicleEngineFrame oTrans;
    private int pnEditMode;
    private final String pxeModuleName = "Vehicle Engine / Frame";
    
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnSearch;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnClose;
    @FXML
    private Label lblCode;
    @FXML
    private Label lblLength;
    
    private Stage getStage() {
        return (Stage) btnSave.getScene().getWindow();
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }  
    
    public void setGRider(GRider foValue) {
        oApp = foValue;
    } 
    
    
    
}
