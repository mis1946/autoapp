/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.views;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.auto.parameters.VehicleModel;

/**
 * FXML Controller class
 *
 * @author Arsiela
 * Date Created: 05-24-2023
 */
public class VehicleModelFormController implements Initializable {
    private GRider oApp;
    private MasterCallback oListener;
    private VehicleModel oTrans;
    private int pnEditMode;
    private final String pxeModuleName = "Vehicle Model";
    
    private String oldTransNo = "";
    private String sTransNo = "";
    private String sMakeID = "";
    private String sMakeDesc = "";
    private int lnCtr;
    
    private ObservableList<VehicleDescriptionTableParameter> vhclparamdata = FXCollections.observableArrayList();
    ObservableList<String> cUnitType = FXCollections.observableArrayList("COMMERCIAL VEHICLE", "PRIVATE VEHICLE", "LIGHT PRIVATE VEHICLE", "MEDIUM PRIVATE VEHICLE" );
    ObservableList<String> cBodyType = FXCollections.observableArrayList("SUV", "SEDAN", "TRUCK","MOTORCYCLE" ); 
    ObservableList<String> cUnitSize = FXCollections.observableArrayList("BANTAM" , "SMALL","MEDIUM", "LARGE"); 

    @FXML
    private Button btnRefresh;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnClose;
    @FXML
    private TextField txtField02;
    @FXML
    private ComboBox comboBox04;
    @FXML
    private ComboBox comboBox05;
    @FXML
    private ComboBox comboBox06;
    @FXML
    private TableView tblParamList;
    @FXML
    private TableColumn tblIndex01;
    @FXML
    private TableColumn tblIndex02;
    @FXML
    private TableColumn tblIndex03;
    @FXML
    private TextField txtField03;
    
    private Stage getStage(){
         return (Stage) txtField02.getScene().getWindow();
    }
    
    public void setMakeID(String fsValue){
       sMakeID = fsValue;
    }
    
    public void setMakeDesc(String fsValue){
       sMakeDesc = fsValue;
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oListener = (int fnIndex, Object foValue) -> {
            System.out.println("Set Class Value "  + fnIndex + "-->" + foValue);
        };

        oTrans = new VehicleModel(oApp, oApp.getBranchCode(), true); //Initialize ClientMaster
        oTrans.setCallback(oListener);
        oTrans.setWithUI(true);
        loadVehicleParameterList();
        
        comboBox04.setItems( cUnitType);
        comboBox05.setItems( cBodyType);
        comboBox06.setItems( cUnitSize);
        
        setCapsLockBehavior(txtField02);
        setCapsLockBehavior(txtField03);
        
        txtField02.focusedProperty().addListener(txtField_Focus);  // Description

        tblParamList.setRowFactory(tv -> {
            TableRow<VehicleDescriptionTableParameter> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    getSelectedItem(vhclparamdata.get(row.getIndex()).getTblindex04(),row.getIndex());
                }
            });
            return row;
        });
        
        //Button SetOnAction using cmdButton_Click() method
        btnRefresh.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click);
        btnAdd.setOnAction(this::cmdButton_Click);
        btnEdit.setOnAction(this::cmdButton_Click);
        btnSave.setOnAction(this::cmdButton_Click);
        
        pnEditMode = EditMode.UNKNOWN;
        initbutton(pnEditMode);
    }

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
            case "btnAdd":
                clearFields();
                if(oTrans.NewRecord()){
                    
                    txtField03.setText(sMakeDesc); 
                    oTrans.setMaster(3, sMakeID);
                    oTrans.setMaster(13, sMakeDesc);
                    pnEditMode = oTrans.getEditMode();
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
                    return;
                }
                break;
            case "btnEdit":
                if(oTrans.UpdateRecord()){
                    pnEditMode = oTrans.getEditMode();
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
                    return;
                }
                break;
            case "btnSave":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to save?")) {
                } else {
                    return;
                }
                
                
                if (setSelection()){
                    if(oTrans.SaveRecord()){
                    ShowMessageFX.Information(null, pxeModuleName, "Vehicle Make save sucessfully.");
                    if (oTrans.OpenRecord(oTrans.getSourceID())){
                        pnEditMode = oTrans.getEditMode();
                    } else {
                        clearFields();
                        pnEditMode = EditMode.UNKNOWN;
                    }
                    loadVehicleParameterList();
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
                        return;
                    }
                }
                break;

            case "btnRefresh":
                loadVehicleParameterList();
                break;
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;

            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;

        }

        initbutton(pnEditMode);
        } catch (SQLException ex) {
            Logger.getLogger(VehicleModelFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    public void loadVehicleParameterList(){
        try {
            /*Populate table*/
            vhclparamdata.clear();
            String sRecStat = "";
            if(oTrans.LoadList()){
                for (lnCtr = 1; lnCtr <= oTrans.getItemCount(); lnCtr++){
                    if(oTrans.getDetail(lnCtr,8).toString().equals("1")){
                        sRecStat = "Y";
                    } else {
                        sRecStat = "N";
                    }

                    vhclparamdata.add(new VehicleDescriptionTableParameter(
                        String.valueOf(lnCtr) //Row
                        , (String) oTrans.getDetail(lnCtr,2) //Description
                        , sRecStat //Record Status
                        , (String) oTrans.getDetail(lnCtr,1) //sSourceID
                        , (String) oTrans.getDetail(lnCtr,4) //sUnitType
                        , (String) oTrans.getDetail(lnCtr,5) //sBodyType
                        , (String) oTrans.getDetail(lnCtr,6) //cVhclSize
                        , (String) oTrans.getDetail(lnCtr,3) //sMakeIDxx
                        , (String) oTrans.getDetail(lnCtr,13) //sMakeDesc
                    ));
                } 
                 initVehicleMake();
            } else {
                ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
                return;
            }
        } catch (SQLException e) {
             ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
        }

    }
    
    private void initVehicleMake() {
        boolean lbShow = (pnEditMode == EditMode.READY || pnEditMode == EditMode.UPDATE);
        tblIndex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
        tblIndex02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
        tblIndex03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
        
        tblParamList.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblParamList.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                 header.setReordering(false);
            });
        });

        tblParamList.setItems(vhclparamdata);
    }
    
    //Populate Text Field Based on selected transaction in table
    private void getSelectedItem(String TransNo, int nRow) {
        oldTransNo = TransNo;
        if (oTrans.OpenRecord(TransNo)){
            if (vhclparamdata.get(nRow).getTblindex03().equals("Y")){
                pnEditMode = oTrans.getEditMode();
            } else {
                pnEditMode = EditMode.UNKNOWN;
            }
            txtField03.setText(vhclparamdata.get(nRow).getTblindex09()); // MakeID
            txtField02.setText(vhclparamdata.get(nRow).getTblindex02()); // Description
            
            //sUnitType
            switch (vhclparamdata.get(nRow).getTblindex05()) {
                case "com":
                    comboBox04.getSelectionModel().select(0);
                    break;
                case "pr":
                    comboBox04.getSelectionModel().select(1);
                    break;
                case "lpr":
                    comboBox04.getSelectionModel().select(2);
                    break;
                case "mpr":
                    comboBox04.getSelectionModel().select(3);
                    break;
                default:
                    break;
            }
            //sBodyType
            comboBox05.setValue(vhclparamdata.get(nRow).getTblindex06());
            //sVhclSize
            comboBox06.getSelectionModel().select(Integer.parseInt(vhclparamdata.get(nRow).getTblindex07())); 
            
        }
        initbutton(pnEditMode);
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
                    case 2: 
                        oTrans.setMaster(lnIndex, lsValue); //Handle Encoded Value
                        break;
                }

            } else
               txtField.selectAll();
        } catch (SQLException ex) {
          Logger.getLogger(VehicleMakeFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    };
    
    /*Set ComboBox Value to Master Class*/ 
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private boolean setSelection(){
        try {
            if (comboBox04.getSelectionModel().getSelectedIndex() < 0){
                ShowMessageFX.Warning("No `Unit Type` selected.", pxeModuleName, "Please select `Unit Type` value.");
                comboBox04.requestFocus();
                return false;
            } else {
                switch(comboBox04.getSelectionModel().getSelectedIndex()){
                    case 0:
                        oTrans.setMaster(4, "com");
                        break;
                    case 1:
                        oTrans.setMaster(4, "pr");
                        break;
                    case 2:
                        oTrans.setMaster(4, "lpr");
                        break;
                    case 3:
                        oTrans.setMaster(4, "mpr");
                        break;
                }
            }
            
            if (comboBox05.getSelectionModel().getSelectedIndex() < 0){
                ShowMessageFX.Warning("No `Body Type` selected.", pxeModuleName, "Please select `Body Type` value.");
                comboBox05.requestFocus();
                return false;
            } else {
                oTrans.setMaster(5, comboBox05.getValue());
            }
            
            if (comboBox06.getSelectionModel().getSelectedIndex() < 0){
                ShowMessageFX.Warning("No `Model Size` selected.", pxeModuleName, "Please select `Model Size` value.");
                comboBox06.requestFocus();
                return false;
            } else {
                oTrans.setMaster(6, comboBox06.getSelectionModel().getSelectedIndex());
            }

        } catch (SQLException ex) {
        ShowMessageFX.Warning(getStage(),ex.getMessage(), "Warning", null);
        }

        return true;
    }
     
    private void initbutton(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        
        txtField02.setDisable(!lbShow);
        comboBox04.setDisable(!lbShow);
        comboBox05.setDisable(!lbShow);
        comboBox06.setDisable(!lbShow);
        btnEdit.setVisible(false);
        btnEdit.setManaged(false);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        
        if (fnValue == EditMode.READY ){
            btnEdit.setVisible(true);
            btnEdit.setManaged(true);
        }
    } 
    
    private void clearFields(){
        txtField03.clear();
        txtField02.clear();
        comboBox04.setValue(null);
        comboBox05.setValue(null);
        comboBox06.setValue(null);
        
    }
    
}