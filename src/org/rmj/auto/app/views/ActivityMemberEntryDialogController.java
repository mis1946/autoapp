/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.auto.app.views;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.auto.clients.base.Activity;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class ActivityMemberEntryDialogController implements Initializable,ScreenInterface {
    private GRider oApp;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnClose;
    private Activity oTrans;
    private ObservableList<ActivityMemberTable> Employeedata = FXCollections.observableArrayList();
     private ObservableList<ActivityMemberTable> Departdata = FXCollections.observableArrayList();
    unloadForm unload = new unloadForm(); //Used in Close Button
    private final String pxeModuleName = "ActivityMemberEntryDialogController"; //Form Title
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private TableView<ActivityMemberTable> tblViewDepart;
    @FXML
    private TableView<ActivityMemberTable> tblViewEmployee;
    @FXML
    private TableColumn<ActivityMemberTable, String> tblindex24;
    @FXML
    private TableColumn<ActivityMemberTable, String> tblindexRow;
    @FXML
    private TableColumn<ActivityMemberTable, Boolean> tblselect;
    @FXML
    private TableColumn<ActivityMemberTable, String> tblindex25;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
        btnClose.setOnAction(this::cmdButton_Click);
        btnAdd.setOnAction(this::cmdButton_Click);
        loadEmployeeTable();
        loadDepartTable();
    }    
    
     public void setObject(Activity foValue){
       oTrans = foValue;
    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }   
    
     private void cmdButton_Click(ActionEvent event) {
      
            String lsButton = ((Button)event.getSource()).getId();
            switch (lsButton){
                  case "btnClose":
                    CommonUtils.closeStage(btnClose);
                    break;
            }
     } 

    //storing values on bankentrydata  
    private void loadEmployeeTable(){
          try {
               /*Populate table*/
              Employeedata.clear();
               if (oTrans.loadDepartment()){
                    for (int lnCtr = 1; lnCtr <= oTrans.getDeptCount(); lnCtr++){
                         Employeedata.add(new ActivityMemberTable(
                         String.valueOf(lnCtr), //ROW                         
                         "",
                         oTrans.getDepartment(lnCtr,"sDeptName").toString(),
                         ""
                         ));
                    }
                    tblViewEmployee.setItems(Employeedata);
                    initEmployeeTable();
               }
          } catch (SQLException e) {
               ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
          }
     }
    private Stage getStage(){
          return (Stage) tblViewDepart.getScene().getWindow();
     }
    private void initEmployeeTable() {
          tblindexRow.setCellValueFactory(new PropertyValueFactory<>("tblindexRow"));  //Row
          tblselect.setCellValueFactory(new PropertyValueFactory<>("select"));
          tblindex25.setCellValueFactory(new PropertyValueFactory<>("tblindex24"));
    }
    //storing values on bankentrydata  
    private void loadDepartTable(){
          try {
               /*Populate table*/
               Departdata.clear();
               
               if (oTrans.loadDepartment()){
                    for (int lnCtr = 1; lnCtr <= oTrans.getDeptCount(); lnCtr++){
                         Departdata.add(new ActivityMemberTable(
                         String.valueOf(lnCtr), //ROW 
                         "",
                         oTrans.getDepartment(lnCtr,"sDeptName").toString(),
                         ""
                         
                         ));
                    }
                    tblViewDepart.setItems(Departdata);
                    initDepartTable();
               }
          } catch (SQLException e) {
               ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
          }
     }
    private void initDepartTable() {

          tblindex24.setCellValueFactory(new PropertyValueFactory<>("tblindex24"));
     }
}
    
