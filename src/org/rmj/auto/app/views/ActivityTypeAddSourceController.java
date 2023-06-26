/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.auto.app.views;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.auto.parameters.ActivitySource;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class ActivityTypeAddSourceController implements Initializable, ScreenInterface {

    private MasterCallback oListener;
    private final String pxeModuleName = "Activity Type Add Source";
    private int pnEditMode;//Modifying fields
    private ActivitySource oTrans;
    private GRider oApp;
    @FXML
    private Button btnClose;
    ObservableList<String> cType = FXCollections.observableArrayList("EVENT", "SALES CALL", "PROMO");
    @FXML
    private ComboBox<String> comboBox29;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnSearch;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnSave;

    private int pnRow = 0;
    @FXML
    private TextField txtField02;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oListener = (int fnIndex, Object foValue) -> {
            System.out.println("Set Class Value " + fnIndex + "-->" + foValue);
        };
        oTrans = new ActivitySource(oApp, oApp.getBranchCode(), true); //Initialize ClientMaster
        oTrans.setCallback(oListener);
        oTrans.setWithUI(true);
        txtField02.focusedProperty().addListener(txtField_Focus);
        comboBox29.setItems(cType);
        btnClose.setOnAction(this::cmdButton_Click);
        btnAdd.setOnAction(this::cmdButton_Click);
        btnSearch.setOnAction(this::cmdButton_Click);
        btnCancel.setOnAction(this::cmdButton_Click);
        btnSave.setOnAction(this::cmdButton_Click);

        setCapsLockBehavior(txtField02);
        comboBox29.setOnAction(e -> {
            String selectedItem = comboBox29.getValue();// Retrieve the type ID for the selected type
            switch (selectedItem) {
                case "EVENT":
                    selectedItem = "eve";
                    break;
                case "SALES CALL":
                    selectedItem = "sal";
                    break;
                case "PROMO":
                    selectedItem = "pro";
                    break;
                default:
                    break;
            }
            try {
                oTrans.setMaster(3, selectedItem); // Pass the selected type to the setMaster method
            } catch (SQLException ex) {
                Logger.getLogger(ActivityTypeAddSourceController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        );
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
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            case "btnSearch":
                try {
                if (oTrans.searchEventType()) {
                    pnEditMode = EditMode.READY;
                }
            } catch (SQLException ex) {
                Logger.getLogger(ActivityTypeAddSourceController.class.getName()).log(Level.SEVERE, null, ex);
            }

            break;

            case "btnSave":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to save?") == true) {
                    if (oTrans.SaveRecord()) {
                        ShowMessageFX.Information(null, pxeModuleName, "New source added sucessfully.");
                        pnEditMode = oTrans.getEditMode();
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
                        return;
                    }
                }
                break;
            case "btnCancel":
                if (ShowMessageFX.OkayCancel(getStage(), "Are you sure you want to cancel?", pxeModuleName, null) == true) {
                    clearFields();
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnAdd": //create
                System.out.println("hello");
                if (oTrans.NewRecord()) {
                    try {
                        loadActivitySourceField();
                        clearFields();
                        pnEditMode = oTrans.getEditMode();
                    } catch (SQLException ex) {
                        Logger.getLogger(ActivityTypeAddSourceController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                }
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;
        }
        initButton(pnEditMode);
    }

    private Stage getStage() {
        return (Stage) txtField02.getScene().getWindow();
    }

    private static void setCapsLockBehavior(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (textField.getText() != null) {
                textField.setText(newValue.toUpperCase());
            }
        });
    }
    /* Set TextField Value to Master Class */
    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {
        try {
            TextField txtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
            int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
            String lsValue = txtField.getText().toUpperCase();

            if (lsValue == null) {
                return;
            }
            if (!nv) {
                /* Lost Focus */
                switch (lnIndex) {
                    case 2: //sActSrcex
                        oTrans.setMaster(lnIndex, lsValue);
                        break;
                }
            } else {
                txtField.selectAll();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ActivityTypeAddSourceController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    };

    public void clearFields() {
        txtField02.clear(); // sActSrcex
        comboBox29.setValue(null); //sEventTyp
    }

    private void loadActivitySourceField() throws SQLException {
        String selectedItem = oTrans.getMaster(3).toString();//sEventTyp
        switch (selectedItem) {
            case "sal":
                selectedItem = "SALES CALL";
                break;
            case "eve":
                selectedItem = "EVENT";
                break;
            case "pro":
                selectedItem = "PROMO";
                break;
            default:
                break;
        }
        if (!selectedItem.isEmpty()) {
            comboBox29.getSelectionModel().select(selectedItem);
        }
        txtField02.setText((String) oTrans.getMaster(2)); // sActSrcex
    }

    private void initButton(int fnValue) {
        pnRow = 0;
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

        comboBox29.setDisable(!lbShow);
        txtField02.setDisable(!lbShow);
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        btnCancel.setVisible(lbShow);
        btnCancel.setManaged(lbShow);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);

    }
}
