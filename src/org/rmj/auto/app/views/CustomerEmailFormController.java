/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.views;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.auto.clients.base.ClientEMail;

/**
 * FXML Controller class
 *
 * @author Arsiela Date Created: 10-23-2023
 */
public class CustomerEmailFormController implements Initializable, ScreenInterface {

    private GRider oApp;
    private MasterCallback oListener;
    private ClientEMail oTransEmail;
    private final String pxeModuleName = "Customer Email";
    private int pnRow = 0;
    private boolean pbState = true;

    @FXML
    private Button btnEdit;
    @FXML
    private Button btnClose;
    ObservableList<String> cOwnEmAd = FXCollections.observableArrayList("PERSONAL", "OFFICE", "OTHERS");
    @FXML
    private ComboBox comboBox04EmAd;
    @FXML
    private RadioButton radiobtn06EmaY;
    @FXML
    private ToggleGroup eml_active;
    @FXML
    private RadioButton radiobtn06EmaN;
    @FXML
    private RadioButton radiobtn05EmaY;
    @FXML
    private ToggleGroup eml_prim;
    @FXML
    private RadioButton radiobtn05EmaN;
    @FXML
    private TextField txtField03EmAd;
    @FXML
    private Button btnAdd;

    public void setObject(ClientEMail foObject) {
        oTransEmail = foObject;
    }

    public void setRow(int fnRow) {
        pnRow = fnRow;
    }

    public void setState(boolean fbValue) {
        pbState = fbValue;
    }

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

        comboBox04EmAd.setItems(cOwnEmAd); // Email Ownership

        //CLIENT Email
        txtField03EmAd.setOnKeyPressed(this::txtField_KeyPressed); // Email Address

        //Button SetOnAction using cmdButton_Click() method
        btnClose.setOnAction(this::cmdButton_Click);
        btnAdd.setOnAction(this::cmdButton_Click);
        btnEdit.setOnAction(this::cmdButton_Click);

        if (pbState) {
            btnAdd.setVisible(true);
            btnAdd.setManaged(true);
            btnEdit.setVisible(false);
            btnEdit.setManaged(false);
        } else {
            loadFields();
            btnAdd.setVisible(false);
            btnAdd.setManaged(false);
            btnEdit.setVisible(true);
            btnEdit.setManaged(true);
        }
    }

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    //Animation
    private void shakeTextField(TextField textField) {
        Timeline timeline = new Timeline();
        double originalX = textField.getTranslateX();

        // Add keyframes for the animation
        KeyFrame keyFrame1 = new KeyFrame(Duration.millis(0), new KeyValue(textField.translateXProperty(), 0));
        KeyFrame keyFrame2 = new KeyFrame(Duration.millis(100), new KeyValue(textField.translateXProperty(), -5));
        KeyFrame keyFrame3 = new KeyFrame(Duration.millis(200), new KeyValue(textField.translateXProperty(), 5));
        KeyFrame keyFrame4 = new KeyFrame(Duration.millis(300), new KeyValue(textField.translateXProperty(), -5));
        KeyFrame keyFrame5 = new KeyFrame(Duration.millis(400), new KeyValue(textField.translateXProperty(), 5));
        KeyFrame keyFrame6 = new KeyFrame(Duration.millis(500), new KeyValue(textField.translateXProperty(), -5));
        KeyFrame keyFrame7 = new KeyFrame(Duration.millis(600), new KeyValue(textField.translateXProperty(), 5));
        KeyFrame keyFrame8 = new KeyFrame(Duration.millis(700), new KeyValue(textField.translateXProperty(), originalX));

        // Add keyframes to the timeline
        timeline.getKeyFrames().addAll(
                keyFrame1, keyFrame2, keyFrame3, keyFrame4, keyFrame5, keyFrame6, keyFrame7, keyFrame8
        );

        // Play the animation
        timeline.play();
    }

    //Validation
    private void addRequiredFieldListener(TextField textField) {
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && textField.getText().isEmpty()) {
                shakeTextField(textField);
                textField.getStyleClass().add("required-field");
            } else {
                textField.getStyleClass().remove("required-field");
            }
        });
    }

    private static void setCapsLockBehavior(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (textField.getText() != null) {
                textField.setText(newValue.toUpperCase());
            }
        });
    }

    private static void setCapsLockBehavior(TextArea textArea) {
        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (textArea.getText() != null) {
                textArea.setText(newValue.toUpperCase());
            }
        });
    }

    private void cmdButton_Click(ActionEvent event) {
        try {
            String lsButton = ((Button) event.getSource()).getId();
            switch (lsButton) {
                case "btnEdit":
                case "btnAdd":
                    if (settoClass()) {
                        if (lsButton.equals("btnAdd")) {
                            oTransEmail.addEmail();
                        }
                        CommonUtils.closeStage(btnClose);
                    } else {
                        return;
                    }
                    break;
                case "btnClose":
                    CommonUtils.closeStage(btnClose);
                    break;

                default:
                    ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                    break;

            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerEmailFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean settoClass() {
        try {
            for (int lnCtr = 1; lnCtr <= oTransEmail.getItemCount(); lnCtr++) {
                if (oTransEmail.getEmail(lnCtr, "cPrimaryx").toString().equals("1") && (lnCtr != pnRow)) {
                    if (radiobtn05EmaY.isSelected()) {
                        ShowMessageFX.Warning(getStage(), "Please note that you cannot add more than 1 primary email.", "Warning", pxeModuleName);
                        return false;
                    }
                }
            }

            //Validate Before adding to tables
            if (txtField03EmAd.getText().isEmpty() || txtField03EmAd.getText().trim().equals("")) {
                ShowMessageFX.Warning(getStage(), "Invalid Email. Insert to table Aborted!", "Warning", null);
                return false;
            }

            if (!CommonUtils.isValidEmail(txtField03EmAd.getText())) {
                ShowMessageFX.Warning(getStage(), "Invalid Email. Insert to table Aborted!", "Warning", null);
                return false;
            }

            if (!radiobtn05EmaY.isSelected() && !radiobtn05EmaN.isSelected()) {
                ShowMessageFX.Warning(getStage(), "Please select Email Type.Insert to table Aborted!", "Warning", null);
                return false;
            }

            if (!radiobtn06EmaY.isSelected() && !radiobtn06EmaN.isSelected()) {
                ShowMessageFX.Warning(getStage(), "Please select Email Status. Insert to table Aborted!", "Warning", null);
                return false;
            }

            if (comboBox04EmAd.getValue().equals("")) {
                ShowMessageFX.Warning(getStage(), "Please select Email Ownership. Insert to table Aborted!", "Warning", null);
                return false;
            }

            oTransEmail.setEmail(pnRow, 3, txtField03EmAd.getText());
            oTransEmail.setEmail(pnRow, 4, comboBox04EmAd.getSelectionModel().getSelectedIndex());

            if (radiobtn05EmaY.isSelected()) {
                oTransEmail.setEmail(pnRow, 5, 1);
            } else {
                oTransEmail.setEmail(pnRow, 5, 0);
            }
            if (radiobtn06EmaY.isSelected()) {
                oTransEmail.setEmail(pnRow, 6, 1);
            } else {
                oTransEmail.setEmail(pnRow, 6, 0);
            }

        } catch (SQLException ex) {
            Logger.getLogger(CustomerEmailFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    private void loadFields() {
        try {
            txtField03EmAd.setText((String) oTransEmail.getEmail(pnRow, "sEmailAdd"));
            comboBox04EmAd.getSelectionModel().select(Integer.parseInt((String) oTransEmail.getEmail(pnRow, "cOwnerxxx")));
            if (oTransEmail.getEmail(pnRow, "cRecdStat").toString().equals("1")) {
                radiobtn06EmaY.setSelected(true);
                radiobtn06EmaN.setSelected(false);
            } else {
                radiobtn06EmaY.setSelected(false);
                radiobtn06EmaN.setSelected(true);
            }
            if (oTransEmail.getEmail(pnRow, "cPrimaryx").toString().equals("1")) {
                radiobtn05EmaY.setSelected(true);
                radiobtn05EmaN.setSelected(false);
            } else {
                radiobtn05EmaY.setSelected(false);
                radiobtn05EmaN.setSelected(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerEmailFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(((TextField) event.getSource()).getId().substring(8, 10));
        String txtFieldID = ((TextField) event.getSource()).getId();

        switch (event.getCode()) {
            case ENTER:
            case DOWN:
                CommonUtils.SetNextFocus(txtField);
                break;
            case UP:
                CommonUtils.SetPreviousFocus(txtField);
        }

    }

    /*TRIGGER FOCUS*/
    private void txtArea_KeyPressed(KeyEvent event) {
        if (event.getCode() == ENTER || event.getCode() == DOWN) {
            event.consume();
            CommonUtils.SetNextFocus((TextArea) event.getSource());
        } else if (event.getCode() == KeyCode.UP) {
            event.consume();
            CommonUtils.SetPreviousFocus((TextArea) event.getSource());
        }
    }

}
