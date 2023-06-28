/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.parts;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;

/**
 * FXML Controller class
 *
 * @author Arsiela
 * Date Created: 06-28-2023
 */
public class ItemPhotoController implements Initializable {
    private GRider oApp;
    //private  oTrans;
    
    public int tbl_row = 0;
    private String psCode;
    private String btncode;
    private String sItemID;
    private String validPhoto;
    private String validPhotoURL;
    private boolean state = false;
    private final String pxeModuleName = "Item Photo";
    double total = 0;
    private int imgRow = -1;
    
    @FXML
    private Button btnExit;
    @FXML
    private FontAwesomeIconView glyphExit;
    @FXML
    private AnchorPane searchBar;
    @FXML
    private Label lblReference;
    @FXML
    private ImageView imgPhoto;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        loadPhoto();
        btnExit.setOnAction(this::cmdButton_Click);
    }

    public void setGRider(GRider foValue) {
        oApp = foValue; //To change bod y of generated methods, choose Tools | Torderlates.
    }

//    public void setPhotoObject( foValue) {
//        oTrans = foValue;
//    }

    public void setPicName(String fsValue) {
        validPhoto = fsValue;
    }
    
    public void setPicUrl(String fsValue) {
        validPhotoURL = fsValue;
    }

    private void loadPhoto() {
        lblReference.setText(validPhoto);
        imgPhoto.setImage(new Image(validPhotoURL));
    }

    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnExit":
                CommonUtils.closeStage(btnExit);
                break;

            default:

        }
    }
}
