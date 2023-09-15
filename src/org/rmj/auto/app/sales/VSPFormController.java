/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.sales;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.auto.app.views.ScreenInterface;
import org.rmj.auto.app.views.unloadForm;
import org.rmj.auto.sales.base.VehicleSalesProposalMaster;

/**
 * FXML Controller class
 *
 * @author Arsiela Date Created 09-15-2023
 */
public class VSPFormController implements Initializable, ScreenInterface {

    private GRider oApp;
    private VehicleSalesProposalMaster oTrans;
    private MasterCallback oListener;

    unloadForm unload = new unloadForm(); //Used in Close Button
    private final String pxeModuleName = "Vehicle Sales Proposal"; //Form Title
    private int pnEditMode;//Modifying fields
    private int lnCtr = 0;

    private String TransNo = "";

    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancelVSP;
    @FXML
    private Button btnBrowse;
    @FXML
    private Button btnClose;
    @FXML
    private TextField txtField02;
    @FXML
    private ToggleGroup tgUnitCategory;
    @FXML
    private Button btnPrint;
    @FXML
    private TextField txtField03;
    @FXML
    private TextField txtField78;
    @FXML
    private TextField txtField84;
    @FXML
    private TextField txtField75;
    @FXML
    private TextField txtField82;
    @FXML
    private TextField txtField76;
    @FXML
    private TextField txtField81;
    @FXML
    private TextField txtField77;
    @FXML
    private TextField txtField68;
    @FXML
    private TextArea textArea69;
    @FXML
    private TextField txtField71;
    @FXML
    private TextField txtField72;
    @FXML
    private TextField txtField48;
    @FXML
    private TextField txtField73;
    @FXML
    private TextField txtField74;
    @FXML
    private TextArea textArea09;
    @FXML
    private TextField txtField40;
    @FXML
    private TextField txtField37;
    @FXML
    private TextField txtField38;
    @FXML
    private TextField txtField39;
    @FXML
    private TextField txtField34;
    @FXML
    private TextField txtField08;
    @FXML
    private TextField txtField29;
    @FXML
    private TextField txtField30;
    @FXML
    private TextField txtField28;
    @FXML
    private TextField txtField31;
    @FXML
    private TextField txtField42;
    @FXML
    private TextField txtField44;
    @FXML
    private TextField txtField46;
    @FXML
    private TextField txtField43;
    @FXML
    private TextField txtField45;
    @FXML
    private TextField txtField47;
    @FXML
    private TextField txtField16;
    @FXML
    private TextField txtField17;
    @FXML
    private TextField txtField24;
    @FXML
    private TextField txtField18;
    @FXML
    private TextField txtField19;
    @FXML
    private TextField txtField13;
    @FXML
    private TextField txtField14;
    @FXML
    private TextField txtField12;
    @FXML
    private ComboBox<String> comboBox21;
    @FXML
    private ComboBox<String> comboBox22;
    @FXML
    private ComboBox<String> comboBox25;
    @FXML
    private ComboBox<String> comboBox23;
    @FXML
    private ComboBox<String> comboBox20;
    @FXML
    private TextField txtField26;
    @FXML
    private TextField txtField27;
    @FXML
    private TextField txtField11;
    @FXML
    private DatePicker date04;
    @FXML
    private TextArea textArea70;
    @FXML
    private TextField txtField10;

    private Stage getStage() {
        return (Stage) btnClose.getScene().getWindow();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oListener = (int fnIndex, Object foValue) -> {
            System.out.println("Set Class Value " + fnIndex + "-->" + foValue);
        };

        oTrans = new VehicleSalesProposalMaster(oApp, oApp.getBranchCode(), true); //Initialize VehicleSalesProposalMaster
        oTrans.setCallback(oListener);
        oTrans.setWithUI(true);
        //Button Click Event
        btnAdd.setOnAction(this::cmdButton_Click);
        btnEdit.setOnAction(this::cmdButton_Click);
        btnSave.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click);

        /*Clear Fields*/
        clearFields();

        pnEditMode = EditMode.UNKNOWN;
        initButton(pnEditMode);
    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd": //create new
                break;
            case "btnEdit": //modify
                break;
            case "btnSave":
                //Validate before saving
                //Proceed Saving
                break;
            case "btnClose": //close tab
                if (ShowMessageFX.OkayCancel(null, "Close Tab", "Are you sure you want to close this Tab?") == true) {
                    if (unload != null) {
                        unload.unloadForm(AnchorMain, oApp, pxeModuleName);
                    } else {
                        ShowMessageFX.Warning(null, "Warning", "Please notify the system administrator to configure the null value at the close button.");
                    }
                    break;
                } else {
                    return;
                }
        }
        initButton(pnEditMode);
    }

    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(((TextField) event.getSource()).getId().substring(8, 10));

//          try{
//               switch (event.getCode()){
//                    case F3:
//                    case TAB:
//                    case ENTER:
//                         switch (lnIndex){
//                         break;
//               }
//          }catch(SQLException e){
//                ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
//          }
        switch (event.getCode()) {
            case ENTER:
            case DOWN:
                CommonUtils.SetNextFocus(txtField);
                break;
            case UP:
                CommonUtils.SetPreviousFocus(txtField);
        }

    }

    /*Set TextField Value to Master Class*/
    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {
//          try{
        TextField txtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();

        if (lsValue == null) {
            return;
        }
        if (!nv) {
            /*Lost Focus*/
            switch (lnIndex) {
            }

        } else {
            txtField.selectAll();
        }
//          } catch (SQLException ex) {
//            Logger.getLogger(VehicleEntryFormController.class.getName()).log(Level.SEVERE, null, ex);
//          }
    };

    /*Enabling / Disabling Fields*/
    private void initButton(int fnValue) {
        /* NOTE:
            lbShow (FALSE)= invisible
            !lbShow (TRUE)= visible
         */
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

        /* MAIN INTERFACE */
        date04.setDisable(!lbShow);
        txtField77.setDisable(!lbShow);
        txtField68.setDisable(!lbShow);
        textArea69.setDisable(!lbShow);
        txtField71.setDisable(!lbShow);
        txtField72.setDisable(!lbShow);
        textArea70.setDisable(!lbShow);
        txtField48.setDisable(!lbShow);
        txtField82.setDisable(!lbShow);
        txtField73.setDisable(!lbShow);
        txtField74.setDisable(!lbShow);

        /* DETAILS INTERFACE */
        txtField08.setDisable(!lbShow);
        txtField38.setDisable(!lbShow);
        txtField29.setDisable(!lbShow);
        txtField30.setDisable(!lbShow);
        txtField28.setDisable(!lbShow);
        txtField31.setDisable(!lbShow);
        txtField42.setDisable(!lbShow);
        txtField43.setDisable(!lbShow);
        txtField44.setDisable(!lbShow);
        txtField45.setDisable(!lbShow);
        txtField46.setDisable(!lbShow);
        txtField47.setDisable(!lbShow);
        txtField10.setDisable(!lbShow);
        txtField16.setDisable(!lbShow);
        comboBox21.setDisable(!lbShow);
        txtField26.setDisable(!lbShow);

        txtField17.setDisable(!lbShow);
        comboBox22.setDisable(!lbShow);
        txtField27.setDisable(!lbShow);
        txtField24.setDisable(!lbShow);
        comboBox25.setDisable(!lbShow);
        txtField18.setDisable(!lbShow);
        comboBox23.setDisable(!lbShow);
        txtField19.setDisable(!lbShow);
        comboBox20.setDisable(!lbShow);
        txtField13.setDisable(!lbShow);
        txtField14.setDisable(!lbShow);
        txtField12.setDisable(!lbShow);
        txtField11.setDisable(!lbShow);

        //if lbShow = false hide btn
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        btnEdit.setVisible(false);
        btnEdit.setManaged(false);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);

        if (fnValue == EditMode.READY) { //show edit if user clicked save / browse
            btnEdit.setVisible(true);
            btnEdit.setManaged(true);
        }
    }

    /*Clear Fields*/
    public void clearFields() {

    }

}
