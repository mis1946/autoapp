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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.auto.parameters.VehicleMake;
import org.rmj.auto.parameters.VehicleType;

/**
 * FXML Controller class
 *
 * @author Arsiela  
 * Date Created: 05-26-2023
 */
public class VehicleTypeFormController implements Initializable {
    private GRider oApp;
    private MasterCallback oListener;
    private VehicleType oTrans;
    private int pnEditMode;
    private final String pxeModuleName = "Vehicle Type";
    
    private String oldTransNo = "";
    private String sTransNo = "";
    private String sMakeID = "";
    private String sMakeDesc = "";
    private String sFormula0 = "E+A+B"; //Default Type Formula
    private String sFormula1 = "";
    private String sFormula2 = "";
    private String lsFormula = "";
    private int lnRow;
    private int lnCtr;
    
    private ObservableList<VehicleDescriptionTableParameter> vhclparamdata = FXCollections.observableArrayList();
    ObservableList<String> cTypeFormat = FXCollections.observableArrayList(); 

    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnClose;
    @FXML
    private Button btnConcat;
    @FXML
    private TableView tblParamList;
    @FXML
    private TableColumn tblIndex01;
    @FXML
    private TableColumn tblIndex02;
    @FXML
    private TableColumn tblIndex03;
    @FXML
    private Button btnRefresh;
    @FXML
    private TextField txtField02;
    @FXML
    private ComboBox cmbFormat;
    @FXML
    private TextField txtField03;
    @FXML
    private TextField txtField01;
    @FXML
    private TextField txtField04;
    
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

        oTrans = new VehicleType(oApp, oApp.getBranchCode(), true); //Initialize ClientMaster
        oTrans.setCallback(oListener);
        oTrans.setWithUI(true);
        loadVehicleParameterList();
        
        setCapsLockBehavior(txtField02);
        setCapsLockBehavior(txtField01);
        setCapsLockBehavior(txtField03);
        setCapsLockBehavior(txtField04);
        txtField02.focusedProperty().addListener(txtField_Focus);  // Description
        
        txtField01.setOnKeyPressed(this::txtField_KeyPressed); 
        txtField03.setOnKeyPressed(this::txtField_KeyPressed); 
        txtField04.setOnKeyPressed(this::txtField_KeyPressed); 
        
        tblParamList.setRowFactory(tv -> {
            TableRow<VehicleDescriptionTableParameter> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    lnRow = row.getIndex();
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if(ShowMessageFX.OkayCancel(null, pxeModuleName, "You have unsaved data, are you sure you want to continue?") == true){   
                        } else
                           return;
                    }
                    
                    getSelectedItem(vhclparamdata.get(lnRow).getTblindex04());
                }
            });
            return row;
        });
        
        cmbFormat.setOnAction(event -> {
            txtField01.setDisable(true);
            txtField03.setDisable(true);
            txtField04.setDisable(true);
            
            switch (cmbFormat.getSelectionModel().getSelectedIndex()) {
                case 0:
                    lsFormula = sFormula0;
                    break;
                case 1:
                    if (!sFormula1.equals(sFormula0) && !sFormula1.equals("")){
                        lsFormula = sFormula1;
                    } else {
                        lsFormula = sFormula2;
                    }
                    break;
                case 2:
                    lsFormula = sFormula2;
                    break;
                default:
                    break;
            }
            
            if(lsFormula.contains("E")){
                txtField01.setDisable(false);
            } else {
                txtField01.clear();
            }
            if(lsFormula.contains("A")){
                txtField03.setDisable(false);
            } else {
                txtField03.clear();
            }
            if(lsFormula.contains("B")){
                txtField04.setDisable(false);
            } else {
                txtField04.clear();
            }
        });
        
        //Button SetOnAction using cmdButton_Click() method
        btnRefresh.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click);
        btnAdd.setOnAction(this::cmdButton_Click);
        btnEdit.setOnAction(this::cmdButton_Click);
        btnSave.setOnAction(this::cmdButton_Click);
        btnConcat.setOnAction(this::cmdButton_Click);
        
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
    
    private boolean loadTypeFormat(){
        try {
            String lsFormat;
            cTypeFormat.clear();
            cmbFormat.setItems(null);
            lsFormat = genFormat(sFormula0);
            //Set Default Formula
            if(!lsFormat.equals("") && !lsFormat.isEmpty()){
                cTypeFormat.add(lsFormat);
                lsFormat=""; //Clear Variable
            }
            if(oTrans.LoadTypeFormat(sMakeID)){
                sFormula1 = oTrans.getFormat( 2).toString();
                sFormula2 = oTrans.getFormat( 3).toString();
                
                if ( (sFormula1.equals("") || sFormula1.isEmpty()) && (sFormula2.equals("") || sFormula2.isEmpty()) ) {
                } else {
                    if (!sFormula1.equals(sFormula0)){
                        lsFormat = genFormat(sFormula1);
                        if (lsFormat != null){
                            if(!lsFormat.equals("") && !lsFormat.isEmpty() ){
                                cTypeFormat.add(lsFormat);
                                lsFormat=""; //Clear Variable
                            }
                        }
                        
                    }
                    if (!sFormula2.equals(sFormula0)){
                        lsFormat = genFormat(sFormula2);
                        if (lsFormat != null){
                            if(!lsFormat.equals("") && !lsFormat.isEmpty() ){
                                cTypeFormat.add(lsFormat);
                                lsFormat=""; //Clear Variable
                            }
                        }
                    }
                }
            cmbFormat.setItems( cTypeFormat);
            cmbFormat.getSelectionModel().select(0);
            
            } else {
                ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(VehicleTypeFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    private String genFormat(String fsValue){
        String sFormat, sCode;
        sFormat = "";
        sCode = "";
        if(fsValue.equals("") || fsValue.isEmpty()){
            return null;
        }
        for (lnCtr = 1; lnCtr <= fsValue.length(); lnCtr++){
            switch (fsValue.substring(lnCtr-1, lnCtr)) {
                case "E":
                    sCode = "ENGINE SIZE";
                    break;
                case "A":
                    sCode = "VARIANT CODE A";
                    break;
                case "B":
                    sCode = "VARIANT CODE B";
                    break;
                default:
                    break;
            }

            if (!sCode.equals("") && !sCode.isEmpty()){
                if (sFormat.equals("")){
                    sFormat = sCode;
                } else {
                    sFormat = sFormat + " + " + sCode;
                }  
            }
            //Clear Variable
            sCode = "";
        }
        return sFormat;
    }
    
    private String genType(String fsValue){
        String sFormat, sCode;
        sFormat = "";
        sCode = "";
        if(fsValue.equals("") || fsValue.isEmpty()){
            return null;
        }
        for (lnCtr = 1; lnCtr <= fsValue.length(); lnCtr++){
            switch (fsValue.substring(lnCtr-1, lnCtr)) {
                case "E":
                    if (txtField01.getText().equals("") || txtField01.getText().isEmpty()){
                        if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to skip Engine Size?")) {
                        } else {
                            txtField01.requestFocus();
                        }
                    } else {
                        sCode = txtField01.getText();
                    }
                    break;
                case "A":
                    if (txtField03.getText().equals("") || txtField03.getText().isEmpty()){
                        if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to skip Variant Code A?")) {
                        } else {
                            txtField03.requestFocus();
                        } 
                    } else {
                        sCode = txtField03.getText();
                    }
                    break;
                case "B":
                    if (txtField04.getText().equals("") || txtField04.getText().isEmpty()){
                        if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to skip Variant Code B?")) {
                        } else {
                            txtField04.requestFocus();
                        }  
                    } else {
                        sCode = txtField04.getText();
                    }
                    break;
                default:
                    break;
            }

            if (!sCode.equals("") && !sCode.isEmpty()){
                if (sFormat.equals("")){
                    sFormat = sCode;
                } else {
                    sFormat = sFormat + " " + sCode;
                }  
            }
            //Clear Variable
            sCode = "";
        }
        return sFormat;
    }
    
    private void cmdButton_Click(ActionEvent event) {
        try {
            String lsButton = ((Button)event.getSource()).getId();
            switch (lsButton){
                case "btnConcat":
                    String lsType = genType(lsFormula);
                    if (!lsType.equals("") && !lsType.isEmpty()){
                
                        if(!txtField02.getText().equals("") && !txtField02.getText().isEmpty()){
                            if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to change concated type?")) {
                            } else {
                                return;
                            }
                        }
                        txtField02.setText(lsType);
                        oTrans.setMaster(2, lsType);
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, "No Type Description to be created.");
                        return;
                    }   
                    
                    clearFields();
                break;
                case "btnAdd":
                    clearFields();
                    if (loadTypeFormat()) {
                        txtField02.clear();
                        if(oTrans.NewRecord()){
                            pnEditMode = oTrans.getEditMode();
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
                            return;
                        }
                    } else {
                        return;
                    }
                    break;
                case "btnEdit":
                    clearFields();
                    if (loadTypeFormat()) {
                        if(oTrans.UpdateRecord()){
                            pnEditMode = oTrans.getEditMode();
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
                            return;
                        }
                    } else {
                        return;
                    }
                    break;
                case "btnSave":
                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to save?")) {
                    } else {
                        return;
                    }

                    if(oTrans.SaveRecord()){
                        ShowMessageFX.Information(null, pxeModuleName, "Vehicle Make save sucessfully.");
                        loadVehicleParameterList();
                        if (pnEditMode == EditMode.ADDNEW){
                            lnRow = (oTrans.getItemCount()-1) ;
                        }
                        
                        getSelectedItem(vhclparamdata.get(lnRow).getTblindex04());
                        pnEditMode = oTrans.getEditMode();
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
                        return;
                    }
                    
                    clearFields();
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
            Logger.getLogger(VehicleTypeFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
    
    private void txtField_KeyPressed(KeyEvent event){
        TextField txtField = (TextField)event.getSource();
        int lnIndex = Integer.parseInt(((TextField)event.getSource()).getId().substring(8,10));

        try{
            switch (event.getCode()){
                case F3:
                case TAB:
                case ENTER:
                    switch (lnIndex){ 
                        case 1: //Engine Size
                            if (oTrans.searchTypeEngine(txtField01.getText())){
                                txtField01.setText(oTrans.getMaster(9).toString());
                            } else 
                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                        break;

                        case 3: //Variant Code A
                            if (oTrans.searchTypeVariant(txtField03.getText(), "A")){
                                txtField03.setText(oTrans.getMaster(10).toString());
                            } else 
                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
                        break;

                        case 4: //Variant Code B 
                            if (oTrans.searchTypeVariant(txtField04.getText(), "B")){
                                 txtField04.setText(oTrans.getMaster(11).toString());
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
    
    public void loadField(){
    
    }
    
    public void loadVehicleParameterList(){
        try {
            /*Populate table*/
            vhclparamdata.clear();
            String sRecStat = "";
            if(oTrans.LoadList()){
                for (lnCtr = 1; lnCtr <= oTrans.getItemCount(); lnCtr++){
                    if(oTrans.getDetail(lnCtr,4).toString().equals("1")){
                        sRecStat = "Y";
                    } else {
                        sRecStat = "N";
                    }

                    vhclparamdata.add(new VehicleDescriptionTableParameter(
                        String.valueOf(lnCtr) //Row
                        , (String) oTrans.getDetail(lnCtr,2) //Description
                        , sRecStat //Record Status
                        , (String) oTrans.getDetail(lnCtr,1) //sSourceID
                        , ""
                        , ""
                        , ""
                        , ""
                        , "" 
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
    private void getSelectedItem(String TransNo) {
        oldTransNo = TransNo;
        if (oTrans.OpenRecord(TransNo)){
            if (vhclparamdata.get(lnRow ).getTblindex03().equals("Y")){
                pnEditMode = oTrans.getEditMode();
            } else {
                pnEditMode = EditMode.UNKNOWN;
            }
            
            txtField02.setText(vhclparamdata.get(lnRow ).getTblindex02()); // Description
        }
        clearFields();
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
     
    private void initbutton(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        //if lbShow = false hide btn          
        btnEdit.setVisible(false); 
        btnEdit.setManaged(false);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);
        cmbFormat.setDisable(!lbShow);
        btnConcat.setDisable(!lbShow);
        
        if (cmbFormat.getValue() == null) {
            txtField01.setDisable(true);
            txtField03.setDisable(true);
            txtField04.setDisable(true);
        }
        
        if (fnValue == EditMode.READY) { //show edit if user clicked save / browse
            btnEdit.setVisible(true); 
            btnEdit.setManaged(true);
        }
    }
    
   private void clearFields(){
        cmbFormat.setValue(null);
        txtField01.clear();
        txtField03.clear();
        txtField04.clear();
   }
    
}
