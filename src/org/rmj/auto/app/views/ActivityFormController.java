/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.auto.app.views;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
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
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.TAB;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.StageStyle;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
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
    private Button btnBrowse;
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
    double xOffset;
    double yOffset;
    @FXML
    private Button btnAddCity;
    @FXML
    private Button btnClearCity;
    @FXML
    private Label lbl01;
    @FXML
    private DatePicker dateFrom06;
    @FXML
    private DatePicker dateTo07;
    @FXML
    private ComboBox comboActivityType04;
    @FXML
    private TextArea txtArea02; //sActTitle
    @FXML
    private TextArea txtArea03; //sActDescx 
    @FXML
    private TextArea txtArea17; //sLogRemrk 
    @FXML
    private TextArea txtArea18; //sRemarksx
    @FXML
    private TextField txtField26; //sDeptName
    @FXML
    private TextField txtField27; //sCompnyNm
    @FXML
    private TextField txtField14; //nTrgtClnt
    @FXML
    private TextField txtField28; //sBranchNm
    @FXML
    private TextField txtField05;
    @FXML
    private TextArea txtArea08; //sAddressx
    @FXML
    private TextField txtField31;
    @FXML
    private TextArea txtArea11;
    @FXML
    private TextField textSeek01;
    @FXML
    private TextField textSeek02;
    @FXML
    private TextField txtField32;
    @FXML
    private TextField txtField0;
    @FXML
    private TextArea txtArea29;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oListener = (int fnIndex, Object foValue) -> {
               System.out.println("Set Class Value "  + fnIndex + "-->" + foValue);
          };
        oTrans = new Activity(oApp, oApp.getBranchCode(), true); //Initialize ClientMaster
        oTrans.setCallback(oListener);
        oTrans.setWithUI(true);
        makeAutoCapitalization(txtField05);
        makeAutoCapitalization(txtField26);
        makeAutoCapitalization(txtField27);
        makeAutoCapitalization(txtField28);
        makeAutoCapitalization(txtField31);
        
        makeAutoCapitalization2(txtArea08);
        makeAutoCapitalization2(txtArea02);
        makeAutoCapitalization2(txtArea03);
        makeAutoCapitalization2(txtArea11);
        makeAutoCapitalization2(txtArea17);
        makeAutoCapitalization2(txtArea18);
        makeAutoCapitalization2(txtArea29);
        
        
        btnBrowse.setOnAction(this::cmdButton_Click);
        btnAddCity.setOnAction(this::cmdButton_Click);
        btnClearCity.setOnAction(this::cmdButton_Click);
        btnActivityMembersSearch.setOnAction(this::cmdButton_Click);
        btnVhclModelsSearch.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click);
        
        
        
        
        /*Set Focus to set Value to Class*/
        txtField05.focusedProperty().addListener(txtField_Focus);
        txtField26.focusedProperty().addListener(txtField_Focus);
        txtField27.focusedProperty().addListener(txtField_Focus);
        txtField28.focusedProperty().addListener(txtField_Focus);
        txtField31.focusedProperty().addListener(txtField_Focus);
        txtArea08.focusedProperty().addListener(txtArea_Focus);
        txtArea02.focusedProperty().addListener(txtArea_Focus);
        txtArea03.focusedProperty().addListener(txtArea_Focus);
        txtArea11.focusedProperty().addListener(txtArea_Focus);
        txtArea17.focusedProperty().addListener(txtArea_Focus);
        txtArea18.focusedProperty().addListener(txtArea_Focus);
        txtArea29.focusedProperty().addListener(txtArea_Focus);

        txtField05.setOnKeyPressed(this::txtField_KeyPressed);
        txtField26.setOnKeyPressed(this::txtField_KeyPressed);
        txtField27.setOnKeyPressed(this::txtField_KeyPressed);
        txtField28.setOnKeyPressed(this::txtField_KeyPressed);
        txtField31.setOnKeyPressed(this::txtField_KeyPressed);
                
        txtArea08.setOnKeyPressed(this::txtArea_KeyPressed);
        txtArea02.setOnKeyPressed(this::txtArea_KeyPressed);
        txtArea03.setOnKeyPressed(this::txtArea_KeyPressed);
        txtArea11.setOnKeyPressed(this::txtArea_KeyPressed);
        txtArea17.setOnKeyPressed(this::txtArea_KeyPressed);
        txtArea18.setOnKeyPressed(this::txtArea_KeyPressed);
        txtArea29.setOnKeyPressed(this::txtArea_KeyPressed);
                
                
        
         
        comboActivityType04.setItems(cType); 
        pnEditMode = EditMode.UNKNOWN;
        textSeek01.setOnKeyPressed(this::txtField_KeyPressed); //Customer ID Search
        initButton(pnEditMode); 
          
    
    }   
    
     private void makeAutoCapitalization(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                textField.setText(newValue.toUpperCase());
            }
        });
    }
     private void makeAutoCapitalization2(TextArea textArea) {
        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                textArea.setText(newValue.toUpperCase());
            }
        });
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
                        if (oTrans.NewRecord()) {
                              clearFields(); 
                              loadClientMaster();
                              pnEditMode = oTrans.getEditMode();
                         } else 
                             ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null); 
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
                    loadActivityMemberDialog();     
                    break; 
                case "btnVhclModelsSearch":
                    loadActivityVehicleEntryDialog();
                    break; 
                case "btnAddCity":
                    loadTownDialog();
                    break;
                case "btnClearCity":
                    
                    break;
                case "btnBrowse":
                    try {
                        if (oTrans.SearchRecord("", true)){
                                loadClientMaster();
                                pnEditMode = oTrans.getEditMode();
                        } else {
                            ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                            pnEditMode = EditMode.UNKNOWN;
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(ActivityFormController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                
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
    /*TRIGGER FOCUS*/
     private void txtArea_KeyPressed(KeyEvent event){
        if (event.getCode() == ENTER || event.getCode() == DOWN){ 
            event.consume();
            CommonUtils.SetNextFocus((TextArea)event.getSource());
        }else if (event.getCode() ==KeyCode.UP){
        event.consume();
            CommonUtils.SetPreviousFocus((TextArea)event.getSource());
        }
    }
    private void txtField_KeyPressed(KeyEvent event){
            TextField txtField = (TextField)event.getSource();
            int lnIndex = Integer.parseInt(((TextField)event.getSource()).getId().substring(8,10));
            
            switch (event.getCode()){
                case F3:
                    switch (lnIndex){
                        case 1: {
                    try {
                        //Make
                        if (oTrans.SearchRecord(textSeek01.getText(), false)){
                            loadClientMaster();
                        } else
                            ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                    } catch (SQLException ex) {
                        Logger.getLogger(ActivityFormController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                            
                            break;
//                              case 4: //Model
//                                   if (oTrans.searchVehicleModel(txtField04.getText())){
//                                        loadVehicleDescField();
//                                   } else 
//                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
//                              
//                              break;
//
//                              case 5: //Color 
//                                   if (oTrans.searchVehicleColor(txtField05.getText())){
//                                        loadVehicleDescField();
//                                   } else 
//                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
//           
//                              break;
//
//                              case 6: //Type 
//                                   if (oTrans.searchVehicleType(txtField06.getText())){
//                                        loadVehicleDescField();
//                                        pnEditMode = oTrans.getEditMode();
//                                   } else 
//                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
//                              break;

//                              case 4: //Model
//                                   if (oTrans.searchVehicleModel(txtField04.getText())){
//                                        loadVehicleDescField();
//                                   } else 
//                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
//                              
//                              break;
//
//                              case 5: //Color 
//                                   if (oTrans.searchVehicleColor(txtField05.getText())){
//                                        loadVehicleDescField();
//                                   } else 
//                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
//           
//                              break;
//
//                              case 6: //Type 
//                                   if (oTrans.searchVehicleType(txtField06.getText())){
//                                        loadVehicleDescField();
//                                        pnEditMode = oTrans.getEditMode();
//                                   } else 
//                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
//                              break;
                    } 
                    break;
                case TAB:
                case ENTER:
                    switch (lnIndex){ 
////                              case 3: //Make
////                                   if (oTrans.searchVehicleMake(txtField03.getText())){
////                                        loadVehicleDescField();
////                                   } else 
////                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
////                              break;
////                              
////                              case 4: //Model
////                                   if (oTrans.searchVehicleModel(txtField04.getText())){
////                                        loadVehicleDescField();
////                                   } else 
////                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
////                              break;
////
////                              case 5: //Color 
////                                   if (oTrans.searchVehicleColor(txtField05.getText())){
////                                        loadVehicleDescField();
////                                   } else
////                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);                            
////                              break;
////
////                              case 6: //Type 
////                                   if (oTrans.searchVehicleType(txtField06.getText())){
////                                        loadVehicleDescField();
////                                        pnEditMode = oTrans.getEditMode();
////                                   } else 
////                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
////                              break;
////                         } 
                    }
                        break;
            }
                    switch (event.getCode()){
                        case ENTER:
                        case DOWN:
                            CommonUtils.SetNextFocus(txtField);
                            break;
                        case UP:
                            CommonUtils.SetPreviousFocus(txtField);
                    }
                    
            }
            
    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {
        try {
            TextField txtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
            String txtFieldID = txtField.getId();
            int startIndex = txtFieldID.indexOf("ld") + 2;
            int endIndex = Math.min(startIndex + 2, txtFieldID.length());
            int lnIndex = Integer.parseInt(txtFieldID.substring(startIndex, endIndex));
            String lsValue = txtField.getText();

            if (lsValue == null)
                return;

            if (!nv) { /* Lost Focus */
                switch (lnIndex) {
                    case 5: // sActSrcex
                    case 26: // sDeptName
                    case 27: // sCompnyNm
                    case 28: // sBranchNm
                    case 31: // sProvName
                        oTrans.setMaster(lnIndex, lsValue); // Handle Encoded Value
                        break;
                }
            } else {
                txtField.selectAll();
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    };

            
        /*Set TextArea to Master Class*/
        final ChangeListener<? super Boolean> txtArea_Focus = (o,ov,nv)->{ 

            TextArea txtArea = (TextArea) ((ReadOnlyBooleanPropertyBase) o).getBean();
            String txtFieldID = txtArea.getId();
            int startIndex = txtFieldID.indexOf("ld") + 2;
            int endIndex = Math.min(startIndex + 2, txtFieldID.length());
            int lnIndex = Integer.parseInt(txtFieldID.substring(startIndex, endIndex));
            String lsValue = txtArea.getText();

             if (lsValue == null) return;
             try {
                if(!nv){ /*Lost Focus*/
                  switch (lnIndex){
                        case 2:        //sActTitle 
                        case 3:        //sActDescx 
                        case 8:        //sAddressx 
                        case 11:        //sCompnynx           
                        case 17:        //sLogRemrk 
                        case 18:        //sRemarksx             
                        case 29:        //sTownxxxx
                         oTrans.setMaster(lnIndex, lsValue); break;
                  }
                } else
                    txtArea.selectAll();
             } catch (SQLException e) {
                ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
                System.exit(1);
             }
        };
        

    //Activity Members Entry Dialog
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
                fxmlLoader.setController(loControl);

                //load the main interface
                Parent parent = fxmlLoader.load();

                parent.setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        xOffset = event.getSceneX();
                        yOffset = event.getSceneY();
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
    //Activity Vehicle Entry Dialog 
    private void loadActivityVehicleEntryDialog() throws IOException{
        /**
         * if state = true : ADD
         * else if state = false : UPDATE
        ***/
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("ActivityVehicleEntryDialog.fxml"));

            ActivityVehicleEntryDialogController loControl = new ActivityVehicleEntryDialogController();
            loControl.setGRider(oApp);
            fxmlLoader.setController(loControl);

            //load the main interface
            Parent parent = fxmlLoader.load();

            parent.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
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
    private void loadTownDialog() {
          /**
         * if state = true : ADD
         * else if state = false : UPDATE
        ***/
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("TownCityMainEntryDialog.fxml"));

            TownCityMainEntryDialogController loControl = new TownCityMainEntryDialogController();
            loControl.setGRider(oApp);
            fxmlLoader.setController(loControl);

            //load the main interface
            Parent parent = fxmlLoader.load();

            parent.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
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
          return (Stage) textSeek01.getScene().getWindow();
    }
       /*Convert Date to String*/
     private LocalDate strToDate(String val){
          DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
          LocalDate localDate = LocalDate.parse(val, date_formatter);
          return localDate;
     }
    private void loadClientMaster(){
          try {
               lbl01.setText((String) oTrans.getMaster(1)); 
               txtArea02.setText((String) oTrans.getMaster(2)); //sActTitle
               txtArea03.setText((String) oTrans.getMaster(3)); //sActDescx
               comboActivityType04.getSelectionModel().select(Integer.parseInt((String)oTrans.getMaster(4))); //sActTypID
               txtField05.setText((String) oTrans.getMaster(5)); //sActSrcex 
               txtArea17.setText((String) oTrans.getMaster(17)); //sLogRemrk
               dateFrom06.setValue(strToDate(CommonUtils.xsDateShort((Date) oTrans.getMaster(11)))); //dDateFrom
               dateTo07.setValue(strToDate(CommonUtils.xsDateShort((Date) oTrans.getMaster(7)))); //dDateThru
               txtArea18.setText((String) oTrans.getMaster(18)); //sRemarksx
               txtField26.setText((String) oTrans.getMaster(26)); //sDeptName 
               txtField14.setText((String) oTrans.getMaster(14)); //nTrgtClnt 
               txtField27.setText((String) oTrans.getMaster(27));
               txtField28.setText((String) oTrans.getMaster(28));
          } catch (SQLException e) {
               ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
          }
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

//             /*Bank Entry*/
//             txtField02.setDisable(!lbShow); // sBankNamexx
//             txtField03.setDisable(!lbShow); // sBankAdv
//             txtField17.setDisable(!lbShow); // sBranchx
//             txtField05.setDisable(!lbShow); // sBarangayx
//             txtField18.setDisable(!lbShow); // sTownNamex
//             txtField15.setDisable(!lbShow); // sProvNamexx
//             txtField07.setDisable(!lbShow); // sZipCode
//             txtField04.setDisable(!lbShow); // sContactP
//             txtField08.setDisable(!lbShow); // sTeleNo
//             txtField09.setDisable(!lbShow); // sFaxNoxx

             btnAdd.setVisible(!lbShow);
             btnAdd.setManaged(!lbShow);
                
//             btnCancel.setVisible(lbShow);
//             btnCancel.setManaged(lbShow);
             //if lbShow = false hide btn          
             btnEdit.setVisible(false); 
             btnEdit.setManaged(false);
             btnSave.setVisible(lbShow);
             btnSave.setManaged(lbShow);
             btnActivityHistory.setVisible(lbShow);
             btnActivityHistory.setManaged(lbShow);
             btnPrint.setVisible(lbShow);
             btnPrint.setManaged(lbShow);
             
             
             
             if (fnValue == EditMode.READY) { //show edit if user clicked save / browse
                  btnEdit.setVisible(true); 
                  btnEdit.setManaged(true);
               
                  
             }
    }
        public void clearFields(){
//          pnRow = 0;
               txtArea02.clear();  //sActTitle
               txtArea03.clear();  //sActDescx
               comboActivityType04.setValue(null); //sActTypID
               txtField05.clear(); //sActSrcex 
               txtArea17.clear(); //sLogRemrk
               dateFrom06.setValue(null); //dDateFrom
               dateTo07.setValue(null); //dDateThru
               txtArea18.clear(); //sRemarksx
               txtField26.clear(); //sDeptName 
               txtField14.clear(); //nTrgtClnt 
               txtField27.clear(); //sCompnyNm
               txtField28.clear(); //sBranchNm  
               txtField31.clear();  //sProvName
     }

   
}
    
