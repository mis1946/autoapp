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
import javafx.scene.control.TableView;
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
    private Label lblApprovedBy;
    @FXML
    private Label lblApprovedDate;
    @FXML
    private Button btnActSearch;
    @FXML
    private Button btnActRemove;
    @FXML
    private TableView<?> tblViewActivityMembers;
    @FXML
    private TableView<?> tblViewVehicleModel;
    @FXML
    private Button btnVchlSearch;
    @FXML
    private Button btnVhclRemove;
    @FXML
    private Button btnAddRowTasks;
    @FXML
    private Button btnAddRowBudget;
    @FXML
    private Button btnActivityHistory;
    @FXML
    private Button btnPrint;
    @FXML
    private Button btnClose;
    @FXML
    private TableView<?> tblViewTasks;
    @FXML
    private TableView<?> tblViewBudget;
    @FXML
    private Button btnRemoveTasks;
    @FXML
    private Button btnRemoveBudget;
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
    
