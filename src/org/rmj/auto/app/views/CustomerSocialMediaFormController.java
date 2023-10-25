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
import org.rmj.auto.clients.base.ClientSocMed;

/**
 * FXML Controller class
 *
 * @author Arsiela 
 * Date Created: 10-23-2023
 */
public class CustomerSocialMediaFormController implements Initializable, ScreenInterface {
    private GRider oApp;
    private MasterCallback oListener;
    private ClientSocMed oTransSocMed;
    private final String pxeModuleName = "Customer Social Media";
    private int pnRow = 0;
    private boolean pbState = true;

    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnClose;
    @FXML
    private RadioButton radiobtn05SocN;
    @FXML
    private ToggleGroup soc_active;
    @FXML
    private TextField txtField03Socm;
    /*Social Media*/
    ObservableList<String> cSocType = FXCollections.observableArrayList("FACEBOOK", "WHATSAPP", "INSTAGRAM", "TIKTOK", "TWITTER");

    @FXML
    private ComboBox comboBox04Socm;
    @FXML
    private RadioButton radiobtn05SocY;
    
    public void setObject(ClientSocMed foObject){
        oTransSocMed = foObject;
    } 
    
    public void setRow(int fnRow){
        pnRow = fnRow;
    }
    
    public void setState(boolean fbValue){
        pbState = fbValue;
    }
    
    private Stage getStage(){
         return (Stage) btnClose.getScene().getWindow();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oListener = (int fnIndex, Object foValue) -> {
            System.out.println("Set Class Value "  + fnIndex + "-->" + foValue);
        };
        
        //CLIENT Social Media
        txtField03Socm.setOnKeyPressed(this::txtField_KeyPressed); // SocMed Account
        
        comboBox04Socm.setItems(cSocType); // SocMed Type
        
        //Button SetOnAction using cmdButton_Click() method
        btnClose.setOnAction(this::cmdButton_Click);
        btnAdd.setOnAction(this::cmdButton_Click);
        btnEdit.setOnAction(this::cmdButton_Click);
        
        if(pbState){
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
            String lsButton = ((Button)event.getSource()).getId();
            switch (lsButton){
                case "btnEdit":
                case "btnAdd":
                    if (settoClass()){
                        if (lsButton.equals("btnAdd")){
                            oTransSocMed.addSocMed();
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
            Logger.getLogger(CustomerSocialMediaFormController.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }  
    
    private boolean settoClass(){
        
        try {
            //Validate Before adding to tables
            if (txtField03Socm.getText().isEmpty() || txtField03Socm.getText().trim().equals("")) {
                ShowMessageFX.Warning(getStage(), "Invalid Account. Insert to table Aborted!", "Warning", null);
                return false;
            }
            
            if (!radiobtn05SocY.isSelected() && !radiobtn05SocN.isSelected()) {
                ShowMessageFX.Warning(getStage(), "Please select Account Type.Insert to table Aborted!", "Warning", null);
                return false;
            }
            
            if (comboBox04Socm.getValue().equals("") || comboBox04Socm.getValue() == null) {
                ShowMessageFX.Warning(getStage(), "Please select Social Media Type. Insert to table Aborted!", "Warning", null);
                return false;
            }
            
            oTransSocMed.setSocMed(pnRow, 3, txtField03Socm.getText());
            oTransSocMed.setSocMed(pnRow, 4, comboBox04Socm.getSelectionModel().getSelectedIndex());
            
            if (radiobtn05SocY.isSelected()) {
                oTransSocMed.setSocMed(pnRow, 5, 1);
            } else {
                oTransSocMed.setSocMed(pnRow, 5, 0);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(CustomerSocialMediaFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    private void loadFields(){
        try {
            txtField03Socm.setText((String) oTransSocMed.getSocMed(pnRow, "sAccountx"));
            comboBox04Socm.getSelectionModel().select(Integer.parseInt((String) oTransSocMed.getSocMed(pnRow, "cSocialTp")));
            if (oTransSocMed.getSocMed(pnRow, "cRecdStat").toString().equals("1")) {
                radiobtn05SocY.setSelected(true);
                radiobtn05SocN.setSelected(false);
            } else {
                radiobtn05SocY.setSelected(false);
                radiobtn05SocN.setSelected(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerSocialMediaFormController.class.getName()).log(Level.SEVERE, null, ex);
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
