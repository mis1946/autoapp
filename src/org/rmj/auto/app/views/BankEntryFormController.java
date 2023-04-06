/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.auto.app.views;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.rmj.appdriver.GRider;

/**
 * FXML Controller class
 *
 * @author User
 */
public class BankEntryFormController implements Initializable, ScreenInterface{
    private GRider oApp;
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private TextField txtField02;
    @FXML
    private Pagination pagination;
    @FXML
    private TableView<?> tblVehicleDesc;
    @FXML
    private TableColumn<?, ?> tblindex01;
    @FXML
    private TableColumn<?, ?> tblindex02;
    @FXML
    private TableColumn<?, ?> tblindex03;
    @FXML
    private TableColumn<?, ?> tblindex04;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnClose;
    @FXML
    private TextField txtField03;
    @FXML
    private TextField txtField04;
    @FXML
    private TextField txtField06;
    @FXML
    private TextField txtField05;
    @FXML
    private TextField txtField08;
    @FXML
    private ComboBox<?> comboBox07;
    @FXML
    private ComboBox<?> comboBox09;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void tblVehicleDesc_Clicked(MouseEvent event) {
    }

    @Override
    public void setGRider(GRider foValue) {
       
    }
    
}
