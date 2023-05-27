/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.views;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.auto.clients.base.VehicleDescription;

/**
 * FXML Controller class
 * DATE CREATED: 03-24-2023
 * @author Arsiela
 */
public class VehicleDescriptionFormController implements Initializable, ScreenInterface  {
    private GRider oApp;
    private VehicleDescription oTrans;
    private MasterCallback oListener;

    unloadForm unload = new unloadForm(); //Used in Close Button
    private final String pxeModuleName = "Vehicle Description"; //Form Title
    private int pnEditMode;//Modifying fields
    private int pnRow = -1;
    private int oldPnRow = -1;
    private int lnCtr = 0;
    private int pagecounter;

    private String oldTransNo = "";
    private String TransNo = "";
    private double xOffset = 0;
    private double yOffset = 0;

    /*populate tables Vehicle Description List*/
    private ObservableList<VehicleDescriptionTableList> vhcldescdata = FXCollections.observableArrayList();
    private FilteredList<VehicleDescriptionTableList> filteredData;
    private static final int ROWS_PER_PAGE = 31;

    ObservableList<String> cTransmission = FXCollections.observableArrayList("AUTOMATIC", "MANUAL", "CVT");
    ObservableList<String> cModelsize = FXCollections.observableArrayList("BANTAM", "SMALL", "MEDIUM", "LARGE");

    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnClose;
    @FXML
    private TableView tblVehicleDesc;
    @FXML
    private TableColumn tblindex01;
    @FXML
    private TableColumn tblindex02;
    @FXML
    private TableColumn tblindex03;
    @FXML
    private TableColumn tblindex04;
    @FXML
    private TextField txtField02;
    @FXML
    private TextField txtField03;
    @FXML
    private TextField txtField04;
    @FXML
    private TextField txtField06;
    @FXML
    private TextField txtField05;
    @FXML
    private TextField txtField08;
    @FXML
    private ComboBox comboBox07;
    @FXML
    private ComboBox comboBox09;
    @FXML
    private Pagination pagination;
    @FXML
    private Button btnMake;
    @FXML
    private Button btnModel;
    @FXML
    private Button btnType;
    @FXML
    private Button btnColor;
    @FXML
    private Button btnCancel;

    private Stage getStage(){
         return (Stage) txtField02.getScene().getWindow();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oListener = (int fnIndex, Object foValue) -> {
            System.out.println("Set Class Value "  + fnIndex + "-->" + foValue);
        };

        oTrans = new VehicleDescription(oApp, oApp.getBranchCode(), true); //Initialize ClientMaster
        oTrans.setCallback(oListener);
        oTrans.setWithUI(true);
        
        setCapsLockBehavior(txtField03);
        setCapsLockBehavior(txtField04);
        setCapsLockBehavior(txtField06);
        setCapsLockBehavior(txtField05);

        /*Set Focus to set Value to Class*/
        txtField03.focusedProperty().addListener(txtField_Focus); // sMakeIDxx
        txtField04.focusedProperty().addListener(txtField_Focus); // sModelIDx
        txtField06.focusedProperty().addListener(txtField_Focus); // sColorIDx
        txtField05.focusedProperty().addListener(txtField_Focus); // sTypeIDxx
        txtField08.focusedProperty().addListener(txtField_Focus); // nYearModl

        /*Add limit and text formatter for year model*/
        CommonUtils.addTextLimiter(txtField08, 4); // nYearModl
        Pattern pattern = Pattern.compile("[0-9]*");
        txtField08.setTextFormatter(new InputTextFormatter(pattern)); //nYearModl

        //Populate combo box
        comboBox07.setItems(cTransmission);
        comboBox09.setItems(cModelsize);

        //Key Pressed Event
        txtField02.setOnKeyPressed(this::txtField_KeyPressed); // sDescript
        txtField03.setOnKeyPressed(this::txtField_KeyPressed); // sMakeIDxx
        txtField04.setOnKeyPressed(this::txtField_KeyPressed); // sModelIDx
        txtField06.setOnKeyPressed(this::txtField_KeyPressed); // sColorIDx
        txtField05.setOnKeyPressed(this::txtField_KeyPressed); // sTypeIDxx
        txtField08.setOnKeyPressed(this::txtField_KeyPressed); // nYearModl

        //If the 'Make' field has not been filled out, disable the 'Model' text field. Otherwise, enable it.
        txtField03.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (newValue != null && !newValue.isEmpty()) {
                    // Enable 
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                         txtField04.setDisable(false);
                    } else {
                         txtField04.setDisable(true);
                    }
                } else {
                    // Disable 
                    txtField04.clear();
                    //Set Value to Master
                    oTrans.setMaster(4, txtField04.getText());
                    oTrans.setMaster(16, txtField04.getText());
                    txtField04.setDisable(true);
                }
            } catch (SQLException ex) {
                Logger.getLogger(VehicleDescriptionFormController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        txtField04.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!txtField03.getText().equals(null) && !txtField03.getText().trim().equals("")) {
                if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                    txtField04.setDisable(false);
                }
            } else {
                txtField04.setDisable(true);
            } 
        });

        //Set Value to Master
        comboBox07.setOnAction(e -> {
            try {
                switch (comboBox07.getSelectionModel().getSelectedIndex()) {
                    case 0:
                        oTrans.setMaster(7, "AT");
                        break;
                    case 1:
                        oTrans.setMaster(7, "M");
                        break;
                    case 2:
                        oTrans.setMaster(7, "CVT");
                        break;
                    default:
                        break;
                }

            } catch (SQLException ex) {
                Logger.getLogger(VehicleDescriptionFormController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        comboBox09.setOnAction(e -> {
            try {
                oTrans.setMaster(9, comboBox09.getSelectionModel().getSelectedIndex());
            } catch (SQLException ex) {
                Logger.getLogger(VehicleDescriptionFormController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        //Button Click Event
        btnAdd.setOnAction(this::cmdButton_Click);
        btnEdit.setOnAction(this::cmdButton_Click); 
        btnSave.setOnAction(this::cmdButton_Click); 
        btnClose.setOnAction(this::cmdButton_Click); 
        btnCancel.setOnAction(this::cmdButton_Click); 

        //Parameter Entry Button
        btnMake.setOnAction(this::cmdButton_Click);
        btnModel.setOnAction(this::cmdButton_Click); 
        btnType.setOnAction(this::cmdButton_Click); 
        btnColor.setOnAction(this::cmdButton_Click); 
        //Populate table
        loadVehicleDescTable();
        pagination.setPageFactory(this::createPage); 

        /*Clear Fields*/
        clearFields();

        pnEditMode = EditMode.UNKNOWN;
        initButton(pnEditMode); 
    } 
     
    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }
    
    
    private static void setCapsLockBehavior(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (textField.getText() != null) {
                textField.setText(newValue.toUpperCase());
            }
        });
    }
    
    private void cmdButton_Click(ActionEvent event) {
        try {
        String lsButton = ((Button)event.getSource()).getId();
            switch (lsButton){
                case "btnAdd": //create new Vehicle Description
                    if (oTrans.NewRecord()) {
                        clearFields(); 
                        loadVehicleDescField();
                        pnEditMode = oTrans.getEditMode();
                    } else 
                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                    break;
                case "btnEdit": //modify vehicle description
                    if (oTrans.UpdateRecord()) {
                        pnEditMode = oTrans.getEditMode(); 
                    } else 
                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                    break;
                case "btnSave": 
                    //Validate before saving
                    if (txtField03.getText().trim().equals("")) {
                       ShowMessageFX.Warning(getStage(), "Please enter a value for Make.","Warning", null);
                       txtField03.requestFocus();
                       return;
                    }
                    if (txtField04.getText().trim().equals("")) {
                       ShowMessageFX.Warning(getStage(), "Please enter a value for Model.","Warning", null);
                       txtField04.requestFocus();
                       return;
                    }
                    if (txtField06.getText().trim().equals("")) {
                        ShowMessageFX.Warning(getStage(), "Please enter a value for Type.","Warning", null);
                        txtField06.requestFocus();
                        return;
                    }
                    if (txtField05.getText().trim().equals("")) {
                        ShowMessageFX.Warning(getStage(), "Please enter a value for Color.","Warning", null);
                        txtField05.requestFocus();
                        return;
                    }

                    if (txtField08.getText().trim().equals("") || Integer.parseInt(txtField08.getText()) < 1900) {
                        ShowMessageFX.Warning(getStage(), "Please enter a valid value for Year.","Warning", null);
                        txtField08.requestFocus();
                        return;
                    }

                    //Proceed Saving
                    if (setSelection()) {
                        if (oTrans.SaveRecord()){
                            ShowMessageFX.Information(getStage(), "Transaction save successfully.", pxeModuleName, null);
                            loadVehicleDescTable();
                            if (pnEditMode == EditMode.ADDNEW){
                                pagecounter = (oTrans.getItemCount()-1) + pagination.getCurrentPageIndex() * ROWS_PER_PAGE;
                            }
                            getSelectedItem(filteredData.get(pagecounter).getTblindex11());
                            pnEditMode = oTrans.getEditMode();
                        } else {
                            ShowMessageFX.Warning(getStage(),oTrans.getMessage() ,"Warning", "Error while saving Vehicle Description");
                        }
                    }
                     break; 
                case "btnCancel": 
                    if(ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to cancel?") == true){
                        clearFields();
                        pnEditMode = EditMode.UNKNOWN;
                    break;
                }else {
                    return;
                }
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
                case "btnMake":
                    loadVehicleMake();
                break;
                case "btnModel":
                case "btnType":
                    if (oTrans.getMaster(3).toString().equals("") || oTrans.getMaster(3).toString().isEmpty() || oTrans.getMaster(3).toString() == null){
                        ShowMessageFX.Warning(getStage(),"Kindly ensure that the Vehicle Make is selected before proceeding to set the model." ,"Warning", "");
                        txtField03.requestFocus();
                        return;
                    }
                    if (lsButton.equals("btnModel")){
                        loadVehicleModel(oTrans.getMaster(3).toString(), oTrans.getMaster(15).toString());
                    } else if (lsButton.equals("btnType")){
                        loadVehicleType(oTrans.getMaster(3).toString(), oTrans.getMaster(15).toString());
                    }
                break;
                case "btnColor":
                    loadVehicleColor();
                break;

            }
            initButton(pnEditMode); 
        } catch (SQLException ex) {
            Logger.getLogger(VehicleDescriptionFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
    //use for creating new page on pagination 
    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, vhcldescdata.size());
        if(vhcldescdata.size()>0){
           tblVehicleDesc.setItems(FXCollections.observableArrayList(vhcldescdata.subList(fromIndex, toIndex))); 
        }
        return tblVehicleDesc;

    }
     
    //storing values on vhcldescdata  
    private void loadVehicleDescTable(){
        try {
            /*Populate table*/
            vhcldescdata.clear();
            if (oTrans.LoadList("")){
                String sDescription ;
                for (lnCtr = 1; lnCtr <= oTrans.getItemCount(); lnCtr++){
                    sDescription = oTrans.getDetail(lnCtr,"sModelDsc").toString() 
                            + " " + oTrans.getDetail(lnCtr,"sTypeDesc").toString()
                            + " " + oTrans.getDetail(lnCtr,"sTransMsn").toString()
                            + " " + oTrans.getDetail(lnCtr,"sColorDsc").toString();
                    vhcldescdata.add(new VehicleDescriptionTableList(
                    String.valueOf(lnCtr), //ROW
                    oTrans.getDetail(lnCtr,"sMakeDesc").toString(), //Make
                    sDescription, //Description
                    oTrans.getDetail(lnCtr,"nYearModl").toString(), //Year
                    oTrans.getDetail(lnCtr,"sModelDsc").toString(), //Model
                    oTrans.getDetail(lnCtr,"sTypeDesc").toString(), //Type
                    oTrans.getDetail(lnCtr,"sTransMsn").toString(), //Transmission
                    oTrans.getDetail(lnCtr,"cVhclSize").toString(), //Vehicle Size
                    oTrans.getDetail(lnCtr,"sColorDsc").toString(), //Color        
                    oTrans.getDetail(lnCtr,"sDescript").toString(), //Description 
                    oTrans.getDetail(lnCtr,"sVhclIDxx").toString()  //sVhclIDxx
                    ));
                }
                initVhclDescTable();
            }
            loadTab();

        } catch (SQLException e) {
             ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
        }
    }
     
    /*populate Table*/    
    private void initVhclDescTable() {
        tblindex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
        tblindex02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
        tblindex03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
        tblindex04.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));

        tblVehicleDesc.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblVehicleDesc.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            header.setReordering(false);
            });
        });

        filteredData = new FilteredList<>(vhcldescdata, b -> true);
        autoSearch(txtField02);
        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<VehicleDescriptionTableList> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(tblVehicleDesc.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tblVehicleDesc.setItems(sortedData);
        tblVehicleDesc.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblVehicleDesc.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
            header.setDisable(true);
        });

    }

     
    private void autoSearch(TextField txtField){
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        boolean fsCode = true;
        txtField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(clients-> {
            // If filter text is empty, display all persons.
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }
            // Compare order no. and last name of every person with filter text.
            String lowerCaseFilter = newValue.toLowerCase();
            switch (lnIndex){
                    case 2:
                        if(lnIndex == 2){
                            return (clients.getTblindex10().toLowerCase().contains(lowerCaseFilter)); // Does not match.   
                        }else {
                            return (clients.getTblindex10().toLowerCase().contains(lowerCaseFilter)); // Does not match.
                        }   
                    default:
                    return true;            
            }
            });

        changeTableView(0, ROWS_PER_PAGE);
      });
      loadTab();
    } 
     
    private void loadTab(){
        int totalPage = (int) (Math.ceil(vhcldescdata.size() * 1.0 / ROWS_PER_PAGE));
        pagination.setPageCount(totalPage);
        pagination.setCurrentPageIndex(0);
        changeTableView(0, ROWS_PER_PAGE);
        pagination.currentPageIndexProperty().addListener(
                (observable, oldValue, newValue) -> changeTableView(newValue.intValue(), ROWS_PER_PAGE));

    } 
     
    private void changeTableView(int index, int limit) {
        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, vhcldescdata.size());

        int minIndex = Math.min(toIndex, filteredData.size());
        SortedList<VehicleDescriptionTableList> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(tblVehicleDesc.comparatorProperty());
        tblVehicleDesc.setItems(sortedData); 
    }

    /*Populate Fields after clicking row in table*/
    @FXML
    private void tblVehicleDesc_Clicked(MouseEvent event) {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
             if(ShowMessageFX.OkayCancel(null, pxeModuleName, "You have unsaved data, are you sure you want to continue?") == true){   
            } else
                return;
        }

        pnRow = tblVehicleDesc.getSelectionModel().getSelectedIndex(); 
        pagecounter = pnRow + pagination.getCurrentPageIndex() * ROWS_PER_PAGE;
        if (pagecounter >= 0){
            if(event.getClickCount() > 0){
                getSelectedItem(filteredData.get(pagecounter).getTblindex11()); //Populate field based on selected Item
                tblVehicleDesc.setOnKeyReleased((KeyEvent t)-> {
                    KeyCode key = t.getCode();
                    switch (key){
                        case DOWN:
                            pnRow = tblVehicleDesc.getSelectionModel().getSelectedIndex();
                            pagecounter = pnRow + pagination.getCurrentPageIndex() * ROWS_PER_PAGE;
                            if (pagecounter == tblVehicleDesc.getItems().size()) {
                                pagecounter = tblVehicleDesc.getItems().size();
                                getSelectedItem(filteredData.get(pagecounter).getTblindex11());
                            }else {
                               int y = 1;
                              pnRow = pnRow + y;
                                getSelectedItem(filteredData.get(pagecounter).getTblindex11());
                            }
                            break;
                        case UP:
                            pnRow = tblVehicleDesc.getSelectionModel().getSelectedIndex();
                            pagecounter = pnRow + pagination.getCurrentPageIndex() * ROWS_PER_PAGE;
                            getSelectedItem(filteredData.get(pagecounter).getTblindex11());
                            break;
                        default:
                          return; 
                    }
                });
            } 
            pnEditMode = EditMode.READY;
            initButton(pnEditMode);  
        }     
    }
     
    /*Populate Text Field Based on selected address in table*/
    private void getSelectedItem(String TransNo){
       oldTransNo = TransNo;
        if (oTrans.OpenRecord(TransNo)){
            //txtField02.setText(vhcldescdata.get(pagecounter).getTblindex10()); //Description 
            txtField03.setText(vhcldescdata.get(pagecounter).getTblindex02()); //Make
            txtField04.setText(vhcldescdata.get(pagecounter).getTblindex05()); //Model
            txtField05.setText(vhcldescdata.get(pagecounter).getTblindex09()); //Color
            txtField06.setText(vhcldescdata.get(pagecounter).getTblindex06()); //Type
            txtField08.setText(vhcldescdata.get(pagecounter).getTblindex04()); //Year

            switch (vhcldescdata.get(pagecounter).getTblindex07()) { //Transmission
                case "AT":
                    comboBox07.getSelectionModel().select(0);
                    break;
                case "M":
                    comboBox07.getSelectionModel().select(1);
                    break;
                case "CVT":
                    comboBox07.getSelectionModel().select(2);
                    break;
                default:
                    break;
            }

            comboBox09.getSelectionModel().select(Integer.parseInt(vhcldescdata.get(pagecounter).getTblindex08())); //Vehicle Size
            oldPnRow = pagecounter;   
        }

    }
     
    private void loadVehicleDescField(){
        try {
            txtField03.setText((String) oTrans.getMaster(15));
            txtField04.setText((String) oTrans.getMaster(16));
            txtField05.setText((String) oTrans.getMaster(17));
            txtField06.setText((String) oTrans.getMaster(18));
            txtField08.setText( oTrans.getMaster(8).toString());

            switch (oTrans.getMaster(7).toString()) {
                case "AT":
                    comboBox07.getSelectionModel().select(0);
                    break;
                case "M":
                    comboBox07.getSelectionModel().select(1);
                    break;
                case "CVT":
                    comboBox07.getSelectionModel().select(2);
                    break;
                default:
                    break;
            }

            comboBox09.getSelectionModel().select(Integer.parseInt(oTrans.getMaster(9).toString()));

        } catch (SQLException e) {
             ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
        }
    }

    private void txtField_KeyPressed(KeyEvent event){
        TextField txtField = (TextField)event.getSource();
        int lnIndex = Integer.parseInt(((TextField)event.getSource()).getId().substring(8,10));

        try{
            switch (event.getCode()){
                case F3:
//                     switch (lnIndex){ 
//                          case 3: //Make
//                               if (oTrans.searchVehicleMake(txtField03.getText())){
//                                    loadVehicleDescField();
//                               } else 
//                                   ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
//
//                          break;
//                          case 4: //Model
//                               if (oTrans.searchVehicleModel(txtField04.getText())){
//                                    loadVehicleDescField();
//                               } else 
//                                   ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
//
//                          break;
//
//                          case 5: //Color 
//                               if (oTrans.searchVehicleColor(txtField05.getText())){
//                                    loadVehicleDescField();
//                               } else 
//                                   ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
//
//                          break;
//
//                          case 6: //Type 
//                               if (oTrans.searchVehicleType(txtField06.getText())){
//                                    loadVehicleDescField();
//                                    pnEditMode = oTrans.getEditMode();
//                               } else 
//                                   ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
//                          break;     
//                     } 
//                break;
                case TAB:
                case ENTER:
                    switch (lnIndex){ 
                        case 3: //Make
                            if (oTrans.searchVehicleMake(txtField03.getText())){
                                loadVehicleDescField();
                            } else 
                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                        break;

                        case 4: //Model
                            if (oTrans.searchVehicleModel(txtField04.getText())){
                                loadVehicleDescField();
                            } else 
                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                        break;

                        case 5: //Color 
                            if (oTrans.searchVehicleColor(txtField05.getText())){
                                loadVehicleDescField();
                            } else 
                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);                            
                        break;

                        case 6: //Type 
                            if (oTrans.searchVehicleType(txtField06.getText())){
                                loadVehicleDescField();
                                pnEditMode = oTrans.getEditMode();
                            } else 
                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                        break;       
                    } 
                    break;
            }
        }catch(SQLException e){
            ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
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
     
    /*Set TextField Value to Master Class*/
    final ChangeListener<? super Boolean> txtField_Focus = (o,ov,nv)->{
         try{
           TextField txtField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
           int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
           String lsValue = txtField.getText();

           if (lsValue == null) return;
           if(!nv){ /*Lost Focus*/
                switch (lnIndex){
                    case 3: //sMakeIDxx
                        oTrans.setMaster(15, lsValue); //Handle Encoded Value
                        break;
                    case 4: //sModelIDx
                        oTrans.setMaster(16, lsValue); //Handle Encoded Value
                        break;
                    case 5: //sColorIDx
                        oTrans.setMaster(17, lsValue); //Handle Encoded Value
                        break;
                    case 6: //sTypeIDxx
                        oTrans.setMaster(18, lsValue); //Handle Encoded Value
                        break;
                    case 8: //nYearModl
                        if (lsValue.trim().equals("")){
                            oTrans.setMaster(lnIndex,  0); //Handle Encoded Value     
                        } else {
                            oTrans.setMaster(lnIndex,  Integer.parseInt(lsValue)); //Handle Encoded Value
                        }
                        break;
                }
                
            } else
               txtField.selectAll();
        } catch (SQLException ex) {
          Logger.getLogger(VehicleDescriptionFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    };
     
    /*Set ComboBox Value to Master Class*/ 
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private boolean setSelection(){
        try {
            if (comboBox07.getSelectionModel().getSelectedIndex() < 0){
                ShowMessageFX.Warning("No `Transmission` selected.", pxeModuleName, "Please select `Transmission` value.");
                comboBox07.requestFocus();
                return false;
            }else 
                 //oTrans.setMaster(7, String.valueOf(comboBox07.getSelectionModel().getSelectedIndex()));
                if (comboBox07.getSelectionModel().getSelectedIndex() == 0){
                    oTrans.setMaster(7, "AT");
                }else if  (comboBox07.getSelectionModel().getSelectedIndex() == 1){
                    oTrans.setMaster(7, "M");
                }else if  (comboBox07.getSelectionModel().getSelectedIndex() == 2){
                    oTrans.setMaster(7, "CVT");
                }

            if (comboBox09.getSelectionModel().getSelectedIndex() < 0){
                ShowMessageFX.Warning("No `Vehicle Size` selected.", pxeModuleName, "Please select `Vehicle Size` value.");
                comboBox09.requestFocus();
                return false;
            }else 
                //oTrans.setMaster(9, String.valueOf(comboBox09.getSelectionModel().getSelectedIndex()));
                oTrans.setMaster(9, comboBox09.getSelectionModel().getSelectedIndex());

        } catch (SQLException ex) {
        ShowMessageFX.Warning(getStage(),ex.getMessage(), "Warning", null);
        }

        return true;
    }
     
    /*Enabling / Disabling Fields*/
    private void initButton(int fnValue){
        pnRow = 0;
        /* NOTE:
             lbShow (FALSE)= invisible
             !lbShow (TRUE)= visible
        */
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

        /*Vehicle Description*/
        txtField03.setDisable(!lbShow); // sMakeIDxx
        txtField04.setDisable(!lbShow); // sModelIDx
        txtField06.setDisable(!lbShow); // sColorIDx
        txtField05.setDisable(!lbShow); // sTypeIDxx
        txtField08.setDisable(!lbShow); // nYearModl
        comboBox07.setDisable(!lbShow); //Transmission
        comboBox09.setDisable(!lbShow); //Vehicle Size

        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        //if lbShow = false hide btn          
        btnEdit.setVisible(false); 
        btnEdit.setManaged(false);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);
        btnCancel.setVisible(lbShow);
        btnCancel.setManaged(lbShow);
        btnMake.setVisible(lbShow);
        btnModel.setVisible(lbShow);
        btnType.setVisible(lbShow);
        btnColor.setVisible(lbShow);

        if (fnValue == EditMode.READY) { //show edit if user clicked save / browse
            btnEdit.setVisible(true); 
            btnEdit.setManaged(true);
        }
    }
     
    /*Clear Fields*/
    public void clearFields(){
        pnRow = 0;
        /*clear tables*/

        txtField02.clear(); // sDescript
        txtField03.clear(); // sMakeIDxx
        txtField04.clear(); // sModelIDx
        txtField06.clear(); // sColorIDx
        txtField05.clear(); // sTypeIDxx
        txtField08.clear(); // nYearModl
        comboBox07.setValue(null); //Transmission
        comboBox09.setValue(null); //Vehicle Size
    }
    
    /*LOAD VEHICLE DESCRIPTION PARAMETERS*/
    /*MAKE WINDOW*/
    private void loadVehicleMake() throws SQLException{
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("VehicleMakeForm.fxml"));

            VehicleMakeFormController loControl = new VehicleMakeFormController();
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
                    stage.setX(event.getScreenX() - xOffset);
                    stage.setY(event.getScreenY() - yOffset);
                }
            });

            //set the main interface as the scene
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("");
            stage.showAndWait();
            
        } catch (IOException e) {
            e.printStackTrace();
            ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }
    
    /*MODEL WINDOW*/
    private void loadVehicleModel(String sSourceID, String sSourceDesc) throws SQLException{
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("VehicleModelForm.fxml"));

            VehicleModelFormController loControl = new VehicleModelFormController();
            loControl.setGRider(oApp);
            loControl.setMakeID(sSourceID);
            loControl.setMakeDesc(sSourceDesc);
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
                    stage.setX(event.getScreenX() - xOffset);
                    stage.setY(event.getScreenY() - yOffset);
                }
            });

            //set the main interface as the scene
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("");
            stage.showAndWait();
            
        } catch (IOException e) {
            e.printStackTrace();
            ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }
    
    /*TYPE WINDOW*/
    private void loadVehicleType(String sSourceID, String sSourceDesc) throws SQLException{
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("VehicleTypeForm.fxml"));

            VehicleTypeFormController loControl = new VehicleTypeFormController();
            loControl.setGRider(oApp);
            loControl.setMakeID(sSourceID);
            loControl.setMakeDesc(sSourceDesc);
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
                    stage.setX(event.getScreenX() - xOffset);
                    stage.setY(event.getScreenY() - yOffset);
                }
            });

            //set the main interface as the scene
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("");
            stage.showAndWait();
            
        } catch (IOException e) {
            e.printStackTrace();
            ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }
    
    /*COlOR WINDOW*/
    private void loadVehicleColor() throws SQLException{
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("VehicleColorForm.fxml"));

            VehicleColorFormController loControl = new VehicleColorFormController();
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
                    stage.setX(event.getScreenX() - xOffset);
                    stage.setY(event.getScreenY() - yOffset);
                }
            });

            //set the main interface as the scene
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("");
            stage.showAndWait();
            
        } catch (IOException e) {
            e.printStackTrace();
            ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }


}
