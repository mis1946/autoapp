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
import org.rmj.auto.parameters.VehicleColor;
import org.rmj.auto.parameters.VehicleMake;

/**
 * FXML Controller class
 *
 * @author Arsiela 
 * Date Created: 05-25-2023
 */
public class VehicleColorFormController implements Initializable {

    private GRider oApp;
    private MasterCallback oListener;
    private VehicleColor oTrans;
    private int pnEditMode;
    private final String pxeModuleName = "Vehicle Color";

    private String oldTransNo = "";
    private String sTransNo = "";
    private int lnRow;
    private int lnCtr;

    private ObservableList<VehicleDescriptionTableParameter> vhclparamdata = FXCollections.observableArrayList();

    @FXML
    private Button btnRefresh;
    @FXML
    private Button btnClose;
    @FXML
    private TextField txtField02;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnSave;
    @FXML
    private TableView tblParamList;
    @FXML
    private TableColumn tblIndex01;
    @FXML
    private TableColumn tblIndex02;
    @FXML
    private TableColumn tblIndex03;

    private Stage getStage() {
        return (Stage) txtField02.getScene().getWindow();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oListener = (int fnIndex, Object foValue) -> {
            System.out.println("Set Class Value " + fnIndex + "-->" + foValue);
        };

        oTrans = new VehicleColor(oApp, oApp.getBranchCode(), true); //Initialize ClientMaster
        oTrans.setCallback(oListener);
        oTrans.setWithUI(true);
        loadVehicleParameterList();

        setCapsLockBehavior(txtField02);
        txtField02.focusedProperty().addListener(txtField_Focus);  // Description

        tblParamList.setRowFactory(tv -> {
            TableRow<VehicleDescriptionTableParameter> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    lnRow = row.getIndex();
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (ShowMessageFX.OkayCancel(null, pxeModuleName, "You have unsaved data, are you sure you want to continue?") == true) {
                        } else {
                            return;
                        }
                    }
                    getSelectedItem(vhclparamdata.get(lnRow).getTblindex04());
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
            String lsButton = ((Button) event.getSource()).getId();
            switch (lsButton) {
                case "btnAdd":
                    txtField02.clear();
                    if (oTrans.NewRecord()) {
                        pnEditMode = oTrans.getEditMode();
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
                        return;
                    }
                    break;
                case "btnEdit":
                    if (oTrans.UpdateRecord()) {
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

                    if (oTrans.SaveRecord()) {
                        ShowMessageFX.Information(null, pxeModuleName, "Vehicle Make save sucessfully.");
                        loadVehicleParameterList();
                        if (pnEditMode == EditMode.ADDNEW) {
                            lnRow = (oTrans.getItemCount() - 1);
                        }

                        getSelectedItem(vhclparamdata.get(lnRow).getTblindex04());
                        pnEditMode = oTrans.getEditMode();
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
                        return;
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
            Logger.getLogger(VehicleColorFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadVehicleParameterList() {
        try {
            /*Populate table*/
            vhclparamdata.clear();
            String sRecStat = "";
            if (oTrans.LoadList()) {
                for (lnCtr = 1; lnCtr <= oTrans.getItemCount(); lnCtr++) {
                    if (oTrans.getDetail(lnCtr, 4).toString().equals("1")) {
                        sRecStat = "Y";
                    } else {
                        sRecStat = "N";
                    }

                    vhclparamdata.add(new VehicleDescriptionTableParameter(
                            String.valueOf(lnCtr) //Row
                            ,
                             (String) oTrans.getDetail(lnCtr, 2) //Description
                            ,
                             sRecStat //Record Status
                            ,
                             (String) oTrans.getDetail(lnCtr, 1) //sSourceID
                            ,
                             "",
                             "",
                             "",
                             "",
                             ""
                    ));
                }
                initVehicleMake();
            } else {
                ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
                return;
            }
        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
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
        if (oTrans.OpenRecord(TransNo)) {
            if (vhclparamdata.get(lnRow).getTblindex03().equals("Y")) {
                pnEditMode = oTrans.getEditMode();
            } else {
                pnEditMode = EditMode.UNKNOWN;
            }

            txtField02.setText(vhclparamdata.get(lnRow).getTblindex02()); // Description
        }
        initbutton(pnEditMode);
    }

    /*Set TextField Value to Master Class*/
    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {
        try {
            TextField txtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
            int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
            String lsValue = txtField.getText();

            if (lsValue == null) {
                return;
            }
            if (!nv) {
                /*Lost Focus*/
                switch (lnIndex) {
                    case 2:
                        oTrans.setMaster(lnIndex, lsValue); //Handle Encoded Value
                        break;
                }

            } else {
                txtField.selectAll();
            }
        } catch (SQLException ex) {
            Logger.getLogger(VehicleMakeFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    };

    private void initbutton(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

        txtField02.setDisable(!lbShow);
        btnEdit.setDisable(true);
        btnSave.setDisable(!lbShow);
        btnAdd.setDisable(lbShow);

        if (fnValue == EditMode.READY) {
            btnEdit.setDisable(false);
        }
    }

}
