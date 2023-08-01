/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.parts;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Arsiela to be continued by John Dave 08-01-2023
 * Date Created: 07-03-2023
 */
public class ItemEntryModelController implements Initializable {

    @FXML
    private Button btnAdd;
    @FXML
    private Button btnClose;
    @FXML
    private TableView tblVModelList;
    @FXML
    private TableColumn tblIndex01;
    @FXML
    private TableColumn tblIndex02;
    @FXML
    private TableColumn tblIndex03;
    @FXML
    private TableColumn tblIndex04;
    @FXML
    private TableView tblVYear;
    @FXML
    private TableColumn tblIndex01_yr;
    @FXML
    private TableColumn tblIndex02_yr;
    @FXML
    private TableColumn tblIndex03_yr;
    @FXML
    private TextField txtField01;
    @FXML
    private Button btnFilter;
    @FXML
    private ComboBox comboBox01;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
