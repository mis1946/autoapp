/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.auto.app.sales;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.auto.app.views.ScreenInterface;
import org.rmj.auto.app.views.unloadForm;

/**
 * Vehicle Sales Approval Controller class
 *
 * @author John Dave
 */
public class VehicleSalesApprovalController implements Initializable,ScreenInterface {
    private GRider oApp;
    
    private final String pxeModuleName = "Vehicle Reservation Approval"; //Form Title
    
    unloadForm unload = new unloadForm(); //Used in Close Button
    
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnClose;
    
    ObservableList<String> cFilter = FXCollections.observableArrayList("Advance Slip Date", "Advances Slip No", "Advances Type",
                                                                       "Customer Name","Employee Name","Unit Description");
        
    ObservableList<String> cType = FXCollections.observableArrayList("Reservation", "Deposit");
    
    @FXML
    private TableView tblVehicleApproval;
    @FXML
    private TableColumn tblindexRow;
    @FXML
    private TableColumn tblindex02;
    @FXML
    private TableColumn tblindex03;
    @FXML
    private TableColumn tblindex17;
    @FXML
    private TableColumn tblindex16;
    @FXML
    private Button btnApproved;
    @FXML
    private Button btnFilter;
    @FXML
    private Button btnRefresh;
    @FXML
    private TextField txtFieldSearch;
    @FXML
    private ComboBox<String> comboFilter;
    @FXML
    private ComboBox comboType;
    @FXML
    private DatePicker fromDate;
    @FXML
    private DatePicker toDate;
    @FXML
    private Label lFrom;
    @FXML
    private Label lTo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        
        txtFieldSearch.setVisible(false);
        txtFieldSearch.setManaged(false);
        lFrom.setManaged(false);
        lFrom.setVisible(false);
        fromDate.setVisible(false);
        fromDate.setManaged(false);
        lTo.setVisible(false);
        lTo.setManaged(false);
        toDate.setVisible(false);
        toDate.setManaged(false);
        comboType.setVisible(false);
        comboType.setManaged(false);
            
        initCombo();
        comboFilter.setItems(cFilter);
        comboType.setItems(cType);
        //cmdButtons_click properties
        btnClose.setOnAction(this::cmdButton_Click);
    }    
     private void cmdButton_Click(ActionEvent event) {
          String lsButton = ((Button)event.getSource()).getId();
          switch(lsButton){
            case "btnClose": //close tab
                         if(ShowMessageFX.OkayCancel(null, "Close Tab", "Are you sure, do you want to close tab?") == true){
                               if (unload != null) {
                                    unload.unloadForm(AnchorMain, oApp, pxeModuleName);
                               }else {
                                    ShowMessageFX.Warning(null, "Warning", "Notify System Admin to Configure Null value at close button.");    
                               }
                               break;
                               }
                         else
                           return;
            }
//              initButton(pnEditMode);  
        }



    
    private Stage getStage(){
          return (Stage) txtFieldSearch.getScene().getWindow();
     }
    
    public void initCombo(){
        comboFilter.setOnAction(e -> {
            String selectedFilter = comboFilter.getSelectionModel().getSelectedItem();

            // Hide all controls first
            txtFieldSearch.setVisible(false);
            txtFieldSearch.setManaged(false);
            lFrom.setManaged(false);
            lFrom.setVisible(false);
            fromDate.setVisible(false);
            fromDate.setManaged(false);
            lTo.setVisible(false);
            lTo.setManaged(false);
            toDate.setVisible(false);
            toDate.setManaged(false);
            comboType.setVisible(false);
            comboType.setManaged(false);

            // Show relevant controls based on selected filter
            switch (selectedFilter) {
                case "Advances Slip No":
                    txtFieldSearch.setVisible(true);
                    txtFieldSearch.setManaged(true);
                    break;
                case "Advance Slip Date":
                    lFrom.setVisible(true);
                    lFrom.setManaged(true);
                    fromDate.setVisible(true);
                    fromDate.setManaged(true);
                    lTo.setVisible(true);
                    lTo.setManaged(true);
                    toDate.setVisible(true);
                    toDate.setManaged(true);
                    break;
                case "Advances Type":
                    comboType.setVisible(true);
                    comboType.setManaged(true);
                    break;
                case "Customer Name":
                    txtFieldSearch.setVisible(true);
                    txtFieldSearch.setManaged(true);
                    break;
                case "Employee Name":
                    txtFieldSearch.setVisible(true);
                    txtFieldSearch.setManaged(true);
                    break;
                case "Unit Description":
                    txtFieldSearch.setVisible(true);
                    txtFieldSearch.setManaged(true);
                    break;
            }
        });
    }
    @Override
    public void setGRider(GRider foValue) {
          oApp = foValue;
     }    
 
}
