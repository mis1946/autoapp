/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.auto.app.sales;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.auto.app.views.ScreenInterface;
import org.rmj.auto.app.views.unloadForm;

/**
 * FXML Controller class
 *
 * @author User
 */
public class InquiryFormController implements Initializable, ScreenInterface{
    private GRider oApp;
    @FXML
    private TableView<?> tblInquiry;
    @FXML
    unloadForm unload = new unloadForm();
    private Button btnClose;
    @FXML
    private Button btnClose1;
    @FXML
    private Button btnClose11;
    @FXML
    private Button btnClose12;
    @FXML
    private Button btnClose13;
    @FXML
    private Button btnClose14;
    @FXML
    private Button btnClose141;
    @FXML
    private Button btnClose1411;
    @FXML
    private Button btnTabAdd1;
    @FXML
    private Button btnTabRem;
    @FXML
    private Button btnTabAdd;
    @FXML
    private Button btnTabRem1;
    @FXML
    private Button btnTabAdd11;
    @FXML
    private Button btnTabRem2;
    @FXML
    private Button btnTabAdd2;
    @FXML
    private Button btnTabRem11;
    @FXML
    private Button btnTabAdd3;
    @FXML
    private Button btnTabAdd31;
    @FXML
    private Button btnTabAdd32;
    @FXML
    private Button btnTabAdd33;
    @FXML
    private Button btnTabAdd4;
    @FXML
    private Button btnClose15;
    @FXML
    private Button btnClose151;
    @FXML
    private Button btnClose1511;
    @FXML
    private Button btnTabAdd34;
    @FXML
    private Button btnTabAdd35;
    @FXML
    private Button btnTabAdd36;
    @FXML
    private Button btnTabAdd37;
    @FXML
    private AnchorPane AnchorMain;
  
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
     
     
    }    

    @FXML
    private void tblInquiry_Clicked(MouseEvent event) {
        
    }

    @Override
    public void setGRider(GRider foValue) {
             oApp = foValue;
    }
   
    @FXML
    private void btnClose(ActionEvent event){
        String btn = ((Button)event.getSource()).getId();
      
    
    }
  
}