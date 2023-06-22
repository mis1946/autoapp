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
import org.rmj.auto.clients.base.Activity;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class ActivityTypeAddSourceController implements Initializable, ScreenInterface {

    private MasterCallback oListener;
    private final String pxeModuleName = "Activity Type Add Source";
    private int pnEditMode;//Modifying fields
    private Activity oTrans;
    private GRider oApp;
    @FXML
    private Button btnClose;
    ObservableList<String> cType = FXCollections.observableArrayList("EVENT", "SALES CALL", "PROMO");
    @FXML
    private TextField txtField05;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        txtField05.focusedProperty().addListener(txtField_Focus);
        comboBox29.setItems(cType);
        btnClose.setOnAction(this::cmdButton_Click);
        btnAdd.setOnAction(this::cmdButton_Click);
        btnSearch.setOnAction(this::cmdButton_Click);
        btnCancel.setOnAction(this::cmdButton_Click);
        btnSave.setOnAction(this::cmdButton_Click);

        setCapsLockBehavior(txtField05);
        comboBox29.setOnAction(e -> {
            String selectedType = comboBox29.getValue();// Retrieve the type ID for the selected type
            // Set the type ID in the text field
            try {
                oTrans.setMaster(29, selectedType); // Pass the selected type to the setMaster method
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

    public void setObject(Activity foValue) {
        oTrans = foValue;
    }

    private void cmdButton_Click(ActionEvent event) {

        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            case "btnSearch":

                break;
            case "btnSave":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to save?")) {
                } else {
                    return;
                }
                String selectedType = comboBox29.getValue();
                switch (selectedType) {
                    case "EVENT":
                        selectedType = "eve";
                        break;
                    case "SALES CALL":
                        selectedType = "sal";
                        break;
                    case "PROMO":
                        selectedType = "pro";
                        break;
                    default:
                        break;
                }
                try {
                    if (oTrans.SaveEventType(selectedType, txtField05.getText())) {
                        ShowMessageFX.Information(null, pxeModuleName, "New source added sucessfully.");
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
                        return;
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(ActivityTypeAddSourceController.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;

            case "btnCancel":
                if (ShowMessageFX.OkayCancel(getStage(), "Are you sure you want to cancel?", pxeModuleName, null) == true) {
                    clearFields();
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnAdd":
        }
    }

    private Stage getStage() {
        return (Stage) txtField05.getScene().getWindow();
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
                    case 5: //sActSrcex
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
        txtField05.clear(); // sActSrcex
        comboBox29.setValue(null); //sEventTyp
    }

    private void initButton(int fnValue) {
        pnRow = 0;
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

        comboBox29.setDisable(!lbShow);
        txtField05.setDisable(!lbShow);
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        btnCancel.setVisible(lbShow);
        btnCancel.setManaged(lbShow);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);

    }
}
