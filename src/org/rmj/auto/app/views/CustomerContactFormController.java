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
import java.util.regex.Pattern;
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
import org.rmj.auto.clients.base.ClientMobile;

/**
 * FXML Controller class
 *
 * @author Arsiela Date Created: 10-23-2023
 */
public class CustomerContactFormController implements Initializable, ScreenInterface {

    private GRider oApp;
    private MasterCallback oListener;
    private ClientMobile oTransMobile;
    private final String pxeModuleName = "Customer Mobile";
    private int pnRow = 0;
    private boolean pbState = true;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnClose;

    ObservableList<String> cOwnCont = FXCollections.observableArrayList("PERSONAL", "OFFICE", "OTHERS");
    ObservableList<String> cTypCont = FXCollections.observableArrayList("MOBILE", "TELEPHONE", "FAX");

    @FXML
    private ComboBox comboBox05Cont; // Contact Ownership
    @FXML
    private ComboBox comboBox04Cont; // Mobile Type
    @FXML
    private TextField txtField03Cont;
    @FXML
    private RadioButton radiobtn14CntY;
    @FXML
    private ToggleGroup con_active;
    @FXML
    private RadioButton radiobtn14CntN;
    @FXML
    private RadioButton radiobtn11CntY;
    @FXML
    private ToggleGroup con_prim;
    @FXML
    private RadioButton radiobtn11CntN;
    @FXML
    private TextArea textArea13Cont;

    public void setObject(ClientMobile foObject) {
        oTransMobile = foObject;
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
        CommonUtils.addTextLimiter(txtField03Cont, 12); //CONTACT NO
        addRequiredFieldListener(txtField03Cont);
        Pattern pattern;
        pattern = Pattern.compile("[0-9]*");
        txtField03Cont.setTextFormatter(new InputTextFormatter(pattern)); //Mobile No

        comboBox05Cont.setItems(cOwnCont); // Contact Ownership
        comboBox04Cont.setItems(cTypCont); // Mobile Type
        comboBox04Cont.setOnAction(e -> {
            txtField03Cont.clear();
        });
        //CLIENT Mobile
        setCapsLockBehavior(txtField03Cont);
        setCapsLockBehavior(textArea13Cont);
        txtField03Cont.setOnKeyPressed(this::txtField_KeyPressed);  //Mobile Number
        textArea13Cont.setOnKeyPressed(this::txtArea_KeyPressed); // Contact Remarks

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
                            oTransMobile.addMobile();
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
            Logger.getLogger(CustomerContactFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean settoClass() {
        try {
            for (int lnCtr = 1; lnCtr <= oTransMobile.getItemCount(); lnCtr++) {
                if (oTransMobile.getMobile(lnCtr, "cPrimaryx").toString().equals("1") && (lnCtr != pnRow)) {
                    if (radiobtn11CntY.isSelected()) {
                        ShowMessageFX.Warning(getStage(), "Please note that you cannot add more than 1 primary contact number.", "Warning", pxeModuleName);
                        return false;
                    }
                }
            }

            //Validate Before adding to tables
            if (txtField03Cont.getText().isEmpty() || txtField03Cont.getText().trim().equals("")) {
                ShowMessageFX.Warning(getStage(), "Invalid Mobile. Insert to table Aborted!", "Warning", null);
                return false;
            }

            if (!radiobtn11CntY.isSelected() && !radiobtn11CntN.isSelected()) {
                ShowMessageFX.Warning(getStage(), "Please select Mobile Type. Insert to table Aborted!", "Warning", null);
                return false;
            }

            if (!radiobtn14CntY.isSelected() && !radiobtn14CntN.isSelected()) {
                ShowMessageFX.Warning(getStage(), "Please select Mobile Status. Insert to table Aborted!", "Warning", null);
                return false;
            }

            if (comboBox05Cont.getValue().equals("")) {
                ShowMessageFX.Warning(getStage(), "Please select Contact Ownership. Insert to table Aborted!", "Warning", null);
                return false;
            }
            if (comboBox04Cont.getValue().equals("")) {
                ShowMessageFX.Warning(getStage(), "Please select Mobile Type. Insert to table Aborted!", "Warning", null);
                return false;
            }

            oTransMobile.setMobile(pnRow, 3, txtField03Cont.getText());
            oTransMobile.setMobile(pnRow, 4, comboBox04Cont.getSelectionModel().getSelectedIndex());
            oTransMobile.setMobile(pnRow, 5, comboBox05Cont.getSelectionModel().getSelectedIndex());
            oTransMobile.setMobile(pnRow, 13, textArea13Cont.getText());

            if (radiobtn11CntY.isSelected()) {
                oTransMobile.setMobile(pnRow, 11, 1);
            } else {
                oTransMobile.setMobile(pnRow, 11, 0);
            }
            if (radiobtn14CntY.isSelected()) {
                oTransMobile.setMobile(pnRow, 14, 1);
            } else {
                oTransMobile.setMobile(pnRow, 14, 0);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerContactFormController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }

    private void loadFields() {
        try {
            txtField03Cont.setText((String) oTransMobile.getMobile(pnRow, "sMobileNo"));
            textArea13Cont.setText((String) oTransMobile.getMobile(pnRow, "sRemarksx"));
            comboBox04Cont.getSelectionModel().select(Integer.parseInt((String) oTransMobile.getMobile(pnRow, "cMobileTp")));
            comboBox05Cont.getSelectionModel().select(Integer.parseInt((String) oTransMobile.getMobile(pnRow, "cOwnerxxx")));
            if (oTransMobile.getMobile(pnRow, "cRecdStat").toString().equals("1")) {
                radiobtn14CntY.setSelected(true);
                radiobtn14CntN.setSelected(false);
            } else {
                radiobtn14CntY.setSelected(false);
                radiobtn14CntN.setSelected(true);
            }
            if (oTransMobile.getMobile(pnRow, "cPrimaryx").toString().equals("1")) {
                radiobtn11CntY.setSelected(true);
                radiobtn11CntN.setSelected(false);
            } else {
                radiobtn11CntY.setSelected(false);
                radiobtn11CntN.setSelected(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerContactFormController.class.getName()).log(Level.SEVERE, null, ex);
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
