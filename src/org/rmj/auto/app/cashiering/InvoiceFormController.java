/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.auto.app.cashiering;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.auto.app.views.ScreenInterface;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class InvoiceFormController implements Initializable, ScreenInterface {

    private GRider oApp;
    private MasterCallback oListener;
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnBrowse;
    @FXML
    private Button btnPrint;
    @FXML
    private Button btnClose;
    @FXML
    private Button btnSearchCheck;
    @FXML
    private Button btnAddDetail;
    @FXML
    private Button btnAddBlankRow;
    @FXML
    private Button btnDeleteRow;
    @FXML
    private Button btnAddCheck;
    @FXML
    private Button btnDeleteCheck;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

}
