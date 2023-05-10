/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.auto.app.views;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.auto.clients.base.VehicleDescription;

/**
 * FXML Controller class
 *
 * @author User
 */
public class ActivityFormController implements Initializable,ScreenInterface {

    private GRider oApp;
    private VehicleDescription oTrans;
    private MasterCallback oListener;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnSearch;
    @FXML
    private Button btnSearch1;
    @FXML
    private Button btnSearch2;
    @FXML
    private Button btnClose1;
    @FXML
    private Label lblApprovedBy;
    @FXML
    private Label lblApprovedDate;
    @FXML
    private Button btnActSearch;
    @FXML
    private Button btnActRemove;
    @FXML
    private Button btnVhclSearch;
    @FXML
    private Button btnVhclRemove;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    @Override
     public void setGRider(GRider foValue) {
          oApp = foValue;
     }
}
    
