/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.auto.app.views;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.stage.StageStyle;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.auto.clients.base.Activity;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class ActivityFormController implements Initializable,ScreenInterface {
    
    private GRider oApp;
    private Activity oTrans;
    private MasterCallback oListener;
    
    unloadForm unload = new unloadForm(); //Used in Close Button
    private final String pxeModuleName = "Activity"; //Form Title
    private int pnEditMode;//Modifying fields
    private int pnRow;
      private ObservableList<ActivityMembersTableList> actmembersData = FXCollections.observableArrayList();
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnSave;
    @FXML
    private Label lblApprovedBy;
    @FXML
    private Label lblApprovedDate;
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
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private TabPane tabPane;
    @FXML
    private TextField txtField01;
    @FXML
    private TextField txtField26;
    @FXML
    private Button btnAddRowTasks111;
    @FXML
    private Button btnAddRowTasks1111;
    @FXML
    private Button btnBrowse;
    @FXML
    private Label lblActivityNo;
    @FXML
    private DatePicker dateFrom;
    @FXML
    private DatePicker dateTo;
    @FXML
    private ComboBox comboActivityType;
    ObservableList<String> cType = FXCollections.observableArrayList("Event", "Sales Call", "Promo");
    @FXML
    private Button btnActivityMembersSearch;
    @FXML
    private Button btnActivityMemRemove;
    @FXML
    private TableView<?> tblViewActivityMembers;
    @FXML
    private Button btnVhclModelsSearch;
    @FXML
    private Button btnVhlModelRemove;
    @FXML
    private TableView<?> tblViewVhclModels;
    @FXML
    private TableColumn<ActivityMembersTableList, String> tblActvtyMembersRow;
    @FXML
    private TableColumn<ActivityMembersTableList,Boolean> tblselected;
    @FXML
    private CheckBox selectAllCheckBoxEmployee;
    @FXML
    private TableColumn<ActivityMembersTableList, String> tblindex08;
    @FXML
    private TableColumn<ActivityMembersTableList, String> tblindex07;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
          initButton(pnEditMode); 
          
          btnActivityMembersSearch.setOnAction(this::cmdButton_Click);
          btnClose.setOnAction(this::cmdButton_Click);
          comboActivityType.setItems(cType); 

          
    
    }    
    @Override
    public void setGRider(GRider foValue) {
          oApp = foValue;
    }
    private void cmdButton_Click(ActionEvent event) {
        try {
            String lsButton = ((Button)event.getSource()).getId();
            switch (lsButton){
                case "btnAdd": //create
//                         if (oTrans.NewRecord()) {
//                              clearFields(); 
//                              loadVehicleDescField();
//                              pnEditMode = oTrans.getEditMode();
//                         } else 
//                             ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                    break;
                case "btnEdit": //modify 
//                         if (oTrans.UpdateRecord()) {
//                              pnEditMode = oTrans.getEditMode();
//                         } else 
//                              ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                    break;
                case "btnSave":
                    //Validate before saving
//                         if (txtField03.getText().trim().equals("")) {
//                              ShowMessageFX.Warning(getStage(), "Please enter a value for Make.","Warning", null);
//                              txtField03.requestFocus();
//                              return;
//                         }
//                         if (txtField04.getText().trim().equals("")) {
//                              ShowMessageFX.Warning(getStage(), "Please enter a value for Model.","Warning", null);
//                              txtField04.requestFocus();
//                              return;
//                         }
//                         if (txtField06.getText().trim().equals("")) {
//                              ShowMessageFX.Warning(getStage(), "Please enter a value for Type.","Warning", null);
//                              txtField06.requestFocus();
//                              return;
//                         }
//                         if (txtField05.getText().trim().equals("")) {
//                              ShowMessageFX.Warning(getStage(), "Please enter a value for Color.","Warning", null);
//                              txtField05.requestFocus();
//                              return;
//                         }
//                         
//                         if (txtField08.getText().trim().equals("") || Integer.parseInt(txtField08.getText()) < 1900) {
//                              ShowMessageFX.Warning(getStage(), "Please enter a valid value for Year.","Warning", null);
//                              txtField08.requestFocus();
//                              return;
//                         }
//   
//                         //Proceed Saving
//                         if (setSelection()) {
//                              if (oTrans.SaveRecord()){
//                                   ShowMessageFX.Information(getStage(), "Transaction save successfully.", pxeModuleName, null);
//                                   loadVehicleDescTable();
//                                   pnEditMode = oTrans.getEditMode();
//                              } else {
//                                  ShowMessageFX.Warning(getStage(),oTrans.getMessage() ,"Warning", "Error while saving Vehicle Description");
//                              }
//                         }
                    break;
                case "btnActivityMembersSearch":
                    System.out.println("im button");
                    loadActivityMemberDialog();
                    
                    break;
                    
                case "btnBrowse":
                    break;
                case "btnActivityHistory":
                    break;
                case "btnPrint":
                    break;//close tab
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
//               initButton(pnEditMode);  
        } catch (IOException ex) {
            Logger.getLogger(ActivityFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
    private void loadActivityMemberDialog() throws IOException{
        /**
         * if state = true : ADD
         * else if state = false : UPDATE
        ***/
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("ActivityMemberEntryDialog.fxml"));

            ActivityMemberEntryDialogController loControl = new ActivityMemberEntryDialogController();
            loControl.setGRider(oApp);
//            loControl.setVSAObject(oTransProcess);
//            loControl.setTableRows(fnRow);
//            loControl.setState(fstate);
//            loControl.setInqStat(fnStat);
//            loControl.setEditMode(fEditMode);
            fxmlLoader.setController(loControl);

            //load the main interface
            Parent parent = fxmlLoader.load();

            parent.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    double xOffset = event.getSceneX();
                    double yOffset = event.getSceneY();
                }
            });

            parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    double xOffset = 0;
                    stage.setX(event.getScreenX() - xOffset);
                    double yOffset = 0;
                    stage.setY(event.getScreenY() - yOffset);
                }
            });

            //set the main interface as the scene/*
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("");
            stage.showAndWait();

//            loadInquiryAdvances();
        } catch (IOException e) {
            e.printStackTrace();
            ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }
    
    private Stage getStage(){
          return (Stage) txtField01.getScene().getWindow();
    }
    public void loadActivityMembers(){ 
          try {
               /*Populate table*/
               actmembersData.clear();
               for (int lnCtr = 1; lnCtr <= oTrans.getActMemberCount(); lnCtr++){
                    actmembersData.add(new ActivityMembersTableList(
                    String.valueOf(lnCtr),
                    oTrans.getActMember(lnCtr,"sTransNox").toString(), //Priority Unit
                    oTrans.getActMember(lnCtr,"sCompnyNm").toString(),
                    oTrans.getActMember(lnCtr,"sDeptName").toString() // Vehicle Description
                    ));
               }
          } catch (SQLException e) {
               ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
          }
     }   
    private void initButton(int fnValue){
          pnRow = 0;
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
        public void clearFields(){
//          pnRow = 0;
     }
}
    
