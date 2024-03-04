/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.views;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.auto.clients.base.ClientVehicleInfo;
import org.rmj.auto.json.TabsStateManager;

/**
 * FXML Controller class
 *
 * @author Arsiela Date Created: 11-06-2023
 *
 */
public class CustomerVehicleInfoFormController implements Initializable, ScreenInterface {

    private GRider oApp;
    private MasterCallback oListener;
    private ClientVehicleInfo oTransVehicle;

    unloadForm unload = new unloadForm(); //Used in Close Button
    private String pxeModuleName = ""; //Form Title
    private int pnEditMode;
    private boolean bBtnVhclAvl = false;
    private int pnRow = -1;
    private int lnCtr;
    private double xOffset = 0;
    private double yOffset = 0;
    private boolean pbisVhclSales = false;
    TextFieldAnimationUtil txtFieldAnimation = new TextFieldAnimationUtil();


    /*populate tables for customer vehicle info and vehicle history*/
    private ObservableList<CustomerTableVehicleInfo> vhclinfodata = FXCollections.observableArrayList();
    private ObservableList<CustomerTableVehicleInfo> vhclhtrydata = FXCollections.observableArrayList();

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
    private Button btnClose;
    @FXML
    private TableView tblViewVhclHsty;
    @FXML
    private TableColumn tblVhcllHsty01;
    @FXML
    private TableColumn tblVhcllHsty02;
    @FXML
    private TableColumn tblVhcllHsty03;
    @FXML
    private TableColumn tblVhcllHsty04;
    @FXML
    private TableColumn tblVhcllHsty05;
    @FXML
    private Button btnVhclAvl;
    @FXML
    private Button btnVhclDesc;
    @FXML
    private TextField txtField24V;
    @FXML
    private TextField txtField26V;
    @FXML
    private TextField txtField28V;
    @FXML
    private TextField txtField31V;
    @FXML
    private TextField txtField30V;
    @FXML
    private TextField txtField32V;
    @FXML
    private TextField txtField20V;
    @FXML
    private TextField txtField03V;
    @FXML
    private TextField txtField11V;
    @FXML
    private TextField txtField08V;
    @FXML
    private TextField txtField04V;
    @FXML
    private TextField txtField09V;
    @FXML
    private DatePicker txtField21V;
    @FXML
    private TextField txtField22V;
    @FXML
    private TextArea textArea34V;
    @FXML
    private TextField txtField35V;
    @FXML
    private TextField txtField36V;
    @FXML
    private TextArea textArea37V;
    @FXML
    private TextArea textArea38V;
    @FXML
    private TextField txtField13; //location
    @FXML
    private Button btnEngFrm;
    @FXML
    private Label lbl_LName1;
    @FXML
    private Label lbl_LName2;
    @FXML
    private Label lbl_LName21;
    @FXML
    private Label lbl_LName22;
    @FXML
    private Button btnTransfer;
    @FXML
    private Button btnVhclMnl;
    @FXML
    private Label lblFormTitle;
    @FXML
    private AnchorPane anchorMisc;
    @FXML
    private GridPane gridMisc;
    @FXML
    private AnchorPane anchorPurch;
    @FXML
    private GridPane gridPurch;
    @FXML
    private AnchorPane anchorSold;
    @FXML
    private VBox vboxSales;
    @FXML
    private GridPane gridSold;
    ObservableList<String> cSoldStats = FXCollections.observableArrayList("NON SALES CUSTOMER", "AVAILABLE FOR SALE", "VSP", "SOLD");
    ObservableList<String> cIsVhclnew = FXCollections.observableArrayList("BRAND NEW", "PRE-OWNED");

    @FXML
    private ComboBox comboBox14; //soldstat
    @FXML
    private ComboBox comboBox15; //isvhclnew
    @FXML
    private TextField txtField42V;
    @FXML
    private TextField txtField41V;
    @FXML
    private TextField txtField40V;

    private Stage getStage() {
        return (Stage) btnAdd.getScene().getWindow();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oListener = (int fnIndex, Object foValue) -> {
            System.out.println("Set Class Value " + fnIndex + "-->" + foValue);
        };

        Platform.runLater(() -> {
            lblFormTitle.setText(getParentTabTitle());

            if (getParentTabTitle().contains("SALES")) {
                pbisVhclSales = true;
                initVhclInfoButton(pnEditMode);
            } else {
                pbisVhclSales = false;
            }

            oTransVehicle.setFormType(pbisVhclSales);
            if (oTransVehicle.loadState()) {
                pnEditMode = oTransVehicle.getEditMode();
                loadClientVehicleInfo();
                loadVehicleHtryTable();
                initVhclInfoButton(pnEditMode);
            } else {
                if (oTransVehicle.getMessage().isEmpty()) {
                } else {
                    ShowMessageFX.Warning(getStage(), oTransVehicle.getMessage(), "Warning", null);
                }
            }
        });

        oTransVehicle = new ClientVehicleInfo(oApp, oApp.getBranchCode(), false);
        oTransVehicle.setCallback(oListener);
        oTransVehicle.setWithUI(true);
        //initVehicleInfo();
        initVehicleHtry();

        comboBox14.setItems(cSoldStats);
        comboBox15.setItems(cIsVhclnew);

        setCapsLockBehavior(txtField24V);
        setCapsLockBehavior(txtField26V);
        setCapsLockBehavior(txtField28V);
        setCapsLockBehavior(txtField31V);
        setCapsLockBehavior(txtField30V);
        setCapsLockBehavior(txtField32V);
        setCapsLockBehavior(txtField20V);
        setCapsLockBehavior(txtField03V);
        setCapsLockBehavior(txtField11V);
        setCapsLockBehavior(txtField08V);
        setCapsLockBehavior(txtField04V);
        setCapsLockBehavior(txtField09V);
        setCapsLockBehavior(txtField22V);
        setCapsLockBehavior(textArea34V);
        setCapsLockBehavior(txtField35V);
        setCapsLockBehavior(txtField36V);
        setCapsLockBehavior(textArea37V);
        setCapsLockBehavior(textArea38V);
        setCapsLockBehavior(txtField13); //location

        //Vehicle Info
        txtFieldAnimation.addRequiredFieldListener(txtField24V);
        txtFieldAnimation.addRequiredFieldListener(txtField26V);
        txtFieldAnimation.addRequiredFieldListener(txtField28V);
        txtFieldAnimation.addRequiredFieldListener(txtField31V);
        txtFieldAnimation.addRequiredFieldListener(txtField30V);
        txtFieldAnimation.addRequiredFieldListener(txtField32V);
        txtFieldAnimation.addRequiredFieldListener(txtField20V);//Plate No
        txtFieldAnimation.addRequiredFieldListener(txtField08V);
        txtFieldAnimation.addRequiredFieldListener(txtField03V);
        txtFieldAnimation.addRequiredFieldListener(txtField35V);
        txtFieldAnimation.addRequiredFieldListener(txtField04V);

        // Add a listener to the textProperty of the TextField
        //Plate number
        txtField20V.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!txtField20V.getText().isEmpty()) {
                txtField08V.getStyleClass().remove("required-field");
            }
        });
        //CS Number
        txtField08V.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!txtField08V.getText().isEmpty()) {
                txtField20V.getStyleClass().remove("required-field");
            }
        });

        //Owner
        txtField35V.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW) {
                if (newValue.isEmpty()) {
                    try {
                        oTransVehicle.setMaster(6, "");
                        oTransVehicle.setMaster(35, "");
                        oTransVehicle.setMaster(37, "");
                        textArea37V.setText("");
                    } catch (SQLException ex) {
                        Logger.getLogger(CustomerVehicleInfoFormController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        //Co-Owner
        txtField36V.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue.isEmpty()) {
                    try {
                        oTransVehicle.setMaster(7, "");
                        oTransVehicle.setMaster(36, "");
                        oTransVehicle.setMaster(38, "");
                        textArea38V.setText("");
                    } catch (SQLException ex) {
                        Logger.getLogger(CustomerVehicleInfoFormController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        setCapsLockBehavior(txtField03V);
        setCapsLockBehavior(txtField04V);
        setCapsLockBehavior(txtField08V);
        setCapsLockBehavior(txtField09V);
        setCapsLockBehavior(txtField11V);
        setCapsLockBehavior(txtField20V);
        setCapsLockBehavior(txtField22V);
        setCapsLockBehavior(txtField24V);
        setCapsLockBehavior(txtField26V);
        setCapsLockBehavior(txtField28V);
        setCapsLockBehavior(txtField35V);
        setCapsLockBehavior(txtField36V);
        setCapsLockBehavior(textArea34V);

        txtField03V.focusedProperty().addListener(txtField_Focus);
        txtField04V.focusedProperty().addListener(txtField_Focus);
        txtField08V.focusedProperty().addListener(txtField_Focus);
        txtField09V.focusedProperty().addListener(txtField_Focus);
        txtField11V.focusedProperty().addListener(txtField_Focus);
        txtField20V.focusedProperty().addListener(txtField_Focus);
        txtField22V.focusedProperty().addListener(txtField_Focus);
        txtField24V.focusedProperty().addListener(txtField_Focus);
        txtField26V.focusedProperty().addListener(txtField_Focus);
        txtField28V.focusedProperty().addListener(txtField_Focus);
        txtField31V.focusedProperty().addListener(txtField_Focus);
        txtField30V.focusedProperty().addListener(txtField_Focus);
        txtField32V.focusedProperty().addListener(txtField_Focus);
        txtField35V.focusedProperty().addListener(txtField_Focus);
        txtField36V.focusedProperty().addListener(txtField_Focus);
        textArea34V.focusedProperty().addListener(txtArea_Focus);
        txtField21V.setOnAction(this::getDate);

        txtField03V.setOnKeyPressed(this::txtField_KeyPressed_Vhcl);
        txtField04V.setOnKeyPressed(this::txtField_KeyPressed_Vhcl);
        txtField08V.setOnKeyPressed(this::txtField_KeyPressed_Vhcl);
        txtField09V.setOnKeyPressed(this::txtField_KeyPressed_Vhcl);
        txtField11V.setOnKeyPressed(this::txtField_KeyPressed_Vhcl);
        txtField20V.setOnKeyPressed(this::txtField_KeyPressed_Vhcl);
        txtField22V.setOnKeyPressed(this::txtField_KeyPressed_Vhcl);
        txtField24V.setOnKeyPressed(this::txtField_KeyPressed_Vhcl);
        txtField26V.setOnKeyPressed(this::txtField_KeyPressed_Vhcl);
        txtField28V.setOnKeyPressed(this::txtField_KeyPressed_Vhcl);
        txtField31V.setOnKeyPressed(this::txtField_KeyPressed_Vhcl);
        txtField30V.setOnKeyPressed(this::txtField_KeyPressed_Vhcl);
        txtField32V.setOnKeyPressed(this::txtField_KeyPressed_Vhcl);
        txtField35V.setOnKeyPressed(this::txtField_KeyPressed_Vhcl);
        txtField36V.setOnKeyPressed(this::txtField_KeyPressed_Vhcl);
        textArea34V.setOnKeyPressed(this::txtArea_KeyPressed);

        btnVhclDesc.setOnAction(this::cmdButton_Click_vhcl);
        btnVhclAvl.setOnAction(this::cmdButton_Click_vhcl);
        btnVhclMnl.setOnAction(this::cmdButton_Click_vhcl);
        btnEngFrm.setOnAction(this::cmdButton_Click_vhcl);
        btnAdd.setOnAction(this::cmdButton_Click_vhcl);
        btnEdit.setOnAction(this::cmdButton_Click_vhcl);
        btnCancel.setOnAction(this::cmdButton_Click_vhcl);
        btnBrowse.setOnAction(this::cmdButton_Click_vhcl);
        btnSave.setOnAction(this::cmdButton_Click_vhcl);
        btnTransfer.setOnAction(this::cmdButton_Click_vhcl);
        btnClose.setOnAction(this::cmdButton_Click_vhcl);

        clearFields();
        pnEditMode = EditMode.UNKNOWN;
        initVhclInfoButton(pnEditMode);
    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
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

    private void cmdButton_Click_vhcl(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        try {
            switch (lsButton) {
                case "btnVhclMnl":
                    oTransVehicle.setFormType(pbisVhclSales);
                    if (oTransVehicle.NewRecord()) {
                        clearVehicleInfoField();
                        bBtnVhclAvl = false;
                        txtField24V.requestFocus();
                        pnEditMode = oTransVehicle.getEditMode();
                    } else {
                        ShowMessageFX.Warning(getStage(), oTransVehicle.getMessage(), "Warning", null);
                    }
                    break;
                case "btnAdd":
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (ShowMessageFX.OkayCancel(null, "Confirmation", "You have unsaved data. Are you sure you want to create a new record?") == true) {
                            clearFields();
                        } else {
                            return;
                        }
                    }

                    clearFields();
                    oTransVehicle.setFormType(pbisVhclSales);
                    if (oTransVehicle.NewRecord()) {
                        bBtnVhclAvl = false;
                        txtField24V.requestFocus();
                        pnEditMode = oTransVehicle.getEditMode();
                    } else {
                        ShowMessageFX.Warning(getStage(), oTransVehicle.getMessage(), "Warning", null);
                    }
                    if (pbisVhclSales) {
                        loadClientVehicleInfo();
                    }
                    break;
                case "btnVhclAvl":
                    if (oTransVehicle.searchAvailableVhcl()) {
                        clearFields();
                        loadClientVehicleInfo();
                        bBtnVhclAvl = true;
                    } else {
                        ShowMessageFX.Warning(getStage(), oTransVehicle.getMessage(), "Warning", null);

                    }
                    break;

                case "btnEdit":
                    if (oTransVehicle.UpdateRecord()) {
                        if (!((String) oTransVehicle.getMaster(7)).isEmpty()) {
                            txtField36V.setDisable(true);
                        }
                        pnEditMode = oTransVehicle.getEditMode();
                    }
                    break;
                case "btnBrowse":
                    oTransVehicle.setFormType(pbisVhclSales);
                    if (oTransVehicle.searchRecord()) {
                        clearFields();
                        loadClientVehicleInfo();
                        pnEditMode = oTransVehicle.getEditMode();
                    } else {
                        ShowMessageFX.Warning(getStage(), oTransVehicle.getMessage(), "Warning", null);
                        return;
                    }

                    break;
                case "btnVhclDesc":
                    loadVehicleDescriptionWindow();
                    break;

                case "btnEngFrm":
                    if (((String) oTransVehicle.getMaster(23)).equals("")) {
                        ShowMessageFX.Warning(getStage(), "Please select Make.", "Warning", null);
                        txtField24V.requestFocus();
                        return;
                    }
                    if (((String) oTransVehicle.getMaster(25)).equals("")) {
                        ShowMessageFX.Warning(getStage(), "Please select Model.", "Warning", null);
                        txtField26V.requestFocus();
                        return;
                    }

                    loadEngineFrameWindow(0, false);
                    break;
                case "btnSave":
                    if (ShowMessageFX.OkayCancel(null, "Confirmation", "Are you sure you want to save this entry?") == true) {
                    } else {
                        return;
                    }

                    if (!pbisVhclSales) {
                        if (txtField35V.getText().isEmpty()) {
                            ShowMessageFX.Warning(getStage(), "Please select Vehicle Owner.", "Warning", null);
                            return;
                        }
                    }

                    if (!setSelection()) {
                        return;
                    }

                    oTransVehicle.setFormType(pbisVhclSales);
                    if (oTransVehicle.SaveRecord()) {
                        ShowMessageFX.Information(getStage(), oTransVehicle.getMessage(), "Client Vehicle Information", null);
                        clearFields();
                        if (oTransVehicle.OpenRecord((String) oTransVehicle.getMaster(1))) {
                            loadClientVehicleInfo();
                        }
                        pnEditMode = oTransVehicle.getEditMode();
                        loadVehicleHtryTable();
                    } else {
                        ShowMessageFX.Warning(getStage(), oTransVehicle.getMessage(), "Warning", null);
                        return;
                    }

                    break;
                case "btnTransfer":
                    if (oTransVehicle.UpdateRecord()) {
                        loadTransferOwnershipWindow();
                    }
                    break;

                case "btnCancel":
                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to cancel?") == true) {
                        clearFields();
                        bBtnVhclAvl = false;
                        pnEditMode = EditMode.UNKNOWN;
                    } else {
                        return;
                    }
                    break;
                case "btnClose":
                    if (ShowMessageFX.OkayCancel(null, "Close Tab", "Are you sure you want to close this Tab?") == true) {
                        if (unload != null) {
                            unload.unloadForm(AnchorMain, oApp, pxeModuleName);
                        } else {
                            ShowMessageFX.Warning(getStage(), "Please notify the system administrator to configure the null value at the close button.", "Warning", pxeModuleName);
                        }
                        break;
                    } else {
                        return;
                    }
            }

            initVhclInfoButton(pnEditMode);
            if (bBtnVhclAvl) {
                disableFields();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }

    }

    /*CLIENT VEHICLE INFORMATION*/
    private void txtField_KeyPressed_Vhcl(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(((TextField) event.getSource()).getId().substring(8, 10));
        String txtFieldID = ((TextField) event.getSource()).getId();
        try {
            switch (event.getCode()) {
                case F3:
                case TAB:
                case ENTER:
                    switch (lnIndex) {
                        case 24: //MAKE
                            if (oTransVehicle.searchVehicleMake(txtField.getText())) {
                            } else {
                                ShowMessageFX.Warning(getStage(), oTransVehicle.getMessage(), "Warning", null);
                                txtField24V.setText("");
                                txtField24V.requestFocus();
                                return;
                            }
                            loadClientVehicleInfo();
                            break;
                        case 26: //MODEL
                            if (oTransVehicle.getMaster(23).toString().isEmpty()) {
                                ShowMessageFX.Warning(getStage(), "Please select Vehicle Make.", "Warning", null);
                                oTransVehicle.setMaster(5, "");
                                txtField24V.requestFocus();
                                return;
                            }
                            if (oTransVehicle.searchVehicleModel(txtField26V.getText())) {
                            } else {
                                ShowMessageFX.Warning(getStage(), oTransVehicle.getMessage(), "Warning", null);
                                txtField26V.setText("");
                                txtField26V.requestFocus();
                                return;
                            }
                            loadClientVehicleInfo();
                            break;
                        case 28: //TYPE
                            if (oTransVehicle.getMaster(23).toString().isEmpty()) {
                                ShowMessageFX.Warning(getStage(), "Please select Vehicle Make.", "Warning", null);
                                oTransVehicle.setMaster(5, "");
                                txtField24V.requestFocus();
                                return;
                            }
                            if (oTransVehicle.getMaster(25).toString().isEmpty()) {
                                ShowMessageFX.Warning(getStage(), "Please select Vehicle Model.", "Warning", null);
                                oTransVehicle.setMaster(5, "");
                                txtField26V.requestFocus();
                                return;
                            }
                            if (oTransVehicle.searchVehicleType(txtField28V.getText())) {
                            } else {
                                ShowMessageFX.Warning(getStage(), oTransVehicle.getMessage(), "Warning", null);
                                txtField28V.setText("");
                                txtField28V.requestFocus();
                                return;
                            }
                            loadClientVehicleInfo();
                            break;
                        case 31: //TRANSMISSION
                            if (oTransVehicle.getMaster(23).toString().isEmpty()) {
                                ShowMessageFX.Warning(getStage(), "Please select Vehicle Make.", "Warning", null);
                                oTransVehicle.setMaster(5, "");
                                txtField24V.requestFocus();
                                return;
                            }
                            if (oTransVehicle.getMaster(25).toString().isEmpty()) {
                                ShowMessageFX.Warning(getStage(), "Please select Vehicle Model.", "Warning", null);
                                oTransVehicle.setMaster(5, "");
                                txtField26V.requestFocus();
                                return;
                            }
                            if (oTransVehicle.getMaster(27).toString().isEmpty()) {
                                ShowMessageFX.Warning(getStage(), "Please select Vehicle Type.", "Warning", null);
                                oTransVehicle.setMaster(5, "");
                                txtField28V.requestFocus();
                                return;
                            }
                            if (oTransVehicle.searchVehicleTrnsMn(txtField31V.getText())) {
                            } else {
                                ShowMessageFX.Warning(getStage(), oTransVehicle.getMessage(), "Warning", null);
                                txtField31V.setText("");
                                txtField31V.requestFocus();
                                return;
                            }
                            loadClientVehicleInfo();
                            break;
                        case 30: //COLOR
                            if (oTransVehicle.getMaster(23).toString().isEmpty()) {
                                ShowMessageFX.Warning(getStage(), "Please select Vehicle Make.", "Warning", null);
                                oTransVehicle.setMaster(5, "");
                                txtField24V.requestFocus();
                                return;
                            }
                            if (oTransVehicle.getMaster(25).toString().isEmpty()) {
                                ShowMessageFX.Warning(getStage(), "Please select Vehicle Model.", "Warning", null);
                                oTransVehicle.setMaster(5, "");
                                txtField26V.requestFocus();
                                return;
                            }
                            if (oTransVehicle.getMaster(27).toString().isEmpty()) {
                                ShowMessageFX.Warning(getStage(), "Please select Vehicle Type.", "Warning", null);
                                oTransVehicle.setMaster(5, "");
                                txtField28V.requestFocus();
                                return;
                            }
                            if (oTransVehicle.getMaster(31).toString().isEmpty()) {
                                ShowMessageFX.Warning(getStage(), "Please select Vehicle Transmission.", "Warning", null);
                                oTransVehicle.setMaster(5, "");
                                txtField31V.requestFocus();
                                return;
                            }
                            if (oTransVehicle.searchVehicleColor(txtField30V.getText())) {
                            } else {
                                ShowMessageFX.Warning(getStage(), oTransVehicle.getMessage(), "Warning", null);
                                txtField30V.setText("");
                                txtField30V.requestFocus();
                                return;
                            }
                            loadClientVehicleInfo();
                            break;
                        case 32: //YEAR
                            if (oTransVehicle.getMaster(23).toString().isEmpty()) {
                                ShowMessageFX.Warning(getStage(), "Please select Vehicle Make.", "Warning", null);
                                oTransVehicle.setMaster(5, "");
                                txtField24V.requestFocus();
                                return;
                            }
                            if (oTransVehicle.getMaster(25).toString().isEmpty()) {
                                ShowMessageFX.Warning(getStage(), "Please select Vehicle Model.", "Warning", null);
                                oTransVehicle.setMaster(5, "");
                                txtField26V.requestFocus();
                                return;
                            }
                            if (oTransVehicle.getMaster(27).toString().isEmpty()) {
                                ShowMessageFX.Warning(getStage(), "Please select Vehicle Type.", "Warning", null);
                                oTransVehicle.setMaster(5, "");
                                txtField28V.requestFocus();
                                return;
                            }
                            if (oTransVehicle.getMaster(31).toString().isEmpty()) {
                                ShowMessageFX.Warning(getStage(), "Please select Vehicle Transmission.", "Warning", null);
                                oTransVehicle.setMaster(5, "");
                                txtField31V.requestFocus();
                                return;
                            }
                            if (oTransVehicle.getMaster(29).toString().isEmpty()) {
                                ShowMessageFX.Warning(getStage(), "Please select Vehicle Color.", "Warning", null);
                                oTransVehicle.setMaster(5, "");
                                txtField30V.requestFocus();
                                return;
                            }
                            if (oTransVehicle.searchVehicleYearMdl(txtField32V.getText())) {
                                txtField21V.setValue(LocalDate.of(Integer.valueOf((String) oTransVehicle.getMaster(32)), Month.JANUARY, 1));
                            } else {
                                ShowMessageFX.Warning(getStage(), oTransVehicle.getMessage(), "Warning", null);
                                txtField32V.setText("");
                                txtField21V.setValue(LocalDate.of(1900, Month.JANUARY, 1));
                                txtField32V.requestFocus();
                                return;
                            }
                            loadClientVehicleInfo();
                            break;
                        case 9:
                            if (oTransVehicle.searchDealer(txtField09V.getText())) {
                                loadClientVehicleInfo();
                            } else {
                                ShowMessageFX.Warning(getStage(), oTransVehicle.getMessage(), "Warning", null);
                                txtField09V.requestFocus();
                                return;
                            }
                            break;
                        case 22:
                            if (oTransVehicle.searchRegsplace(txtField22V.getText())) {
                                loadClientVehicleInfo();
                            } else {
                                ShowMessageFX.Warning(getStage(), oTransVehicle.getMessage(), "Warning", null);
                                txtField22V.requestFocus();
                                return;
                            }
                            break;
                        case 35:
                            if (oTransVehicle.searchCustomer(txtField35V.getText(), true, false)) {
                                loadClientVehicleInfo();
                            } else {
                                ShowMessageFX.Warning(getStage(), oTransVehicle.getMessage(), "Warning", null);
                                txtField35V.requestFocus();
                                return;
                            }
                            break;
                        case 36:
                            if (oTransVehicle.searchCustomer(txtField36V.getText(), false, false)) {
                                loadClientVehicleInfo();
                            } else {
                                ShowMessageFX.Warning(getStage(), oTransVehicle.getMessage(), "Warning", null);
                                txtField36V.requestFocus();
                                return;
                            }
                    }
            }

        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }

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

//    @FXML
//    private void tblViewVhclInfo_Clicked(MouseEvent event) {
//
//    }
    /*LOAD CLIENT VEHICLE INFORMATION*/
    private void loadClientVehicleInfo() {
        try {

            if (pbisVhclSales) {
                txtField40V.setText((String) oTransVehicle.getMaster(40));
                if (!((String) oTransVehicle.getMaster(41)).isEmpty()) {
                    System.out.println(CommonUtils.xsDateMedium(SQLUtil.toDate((String) oTransVehicle.getMaster(41), SQLUtil.FORMAT_SHORT_DATE)));
                    txtField41V.setText(CommonUtils.xsDateMedium(SQLUtil.toDate((String) oTransVehicle.getMaster(41), SQLUtil.FORMAT_SHORT_DATE)));
                }
                txtField42V.setText((String) oTransVehicle.getMaster(42));
            }

            txtField35V.setText((String) oTransVehicle.getMaster(35));
            txtField36V.setText((String) oTransVehicle.getMaster(36));
            textArea37V.setText((String) oTransVehicle.getMaster(37));
            textArea38V.setText((String) oTransVehicle.getMaster(38));

            txtField03V.setText((String) oTransVehicle.getMaster(3));
            txtField04V.setText((String) oTransVehicle.getMaster(4));
            txtField08V.setText((String) oTransVehicle.getMaster(8));
            txtField09V.setText((String) oTransVehicle.getMaster(9));
            txtField11V.setText((String) oTransVehicle.getMaster(11));
            txtField20V.setText((String) oTransVehicle.getMaster(20));
            txtField21V.setValue(strToDate(CommonUtils.xsDateShort((Date) oTransVehicle.getMaster(21))));
            txtField22V.setText((String) oTransVehicle.getMaster(22));
            txtField24V.setText((String) oTransVehicle.getMaster(24));
            txtField26V.setText((String) oTransVehicle.getMaster(26));
            txtField28V.setText((String) oTransVehicle.getMaster(28));
            txtField30V.setText((String) oTransVehicle.getMaster(30));
            txtField31V.setText((String) oTransVehicle.getMaster(31));
            txtField32V.setText((String) oTransVehicle.getMaster(32));
            textArea34V.setText((String) oTransVehicle.getMaster(34));

            comboBox14.getSelectionModel().select(Integer.parseInt(oTransVehicle.getMaster(14).toString()));
            if (!oTransVehicle.getMaster(15).toString().isEmpty()) {
                comboBox15.getSelectionModel().select(Integer.parseInt(oTransVehicle.getMaster(15).toString()));
            }
        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }
    }

    /*Set TextField Value to Master Class*/
    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {
        try {
            TextField txtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
            int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
            String lsValue = txtField.getText();

            if (lsValue == null) {
                return;
            }
            if (!nv) {
                /*Lost Focus*/
                switch (lnIndex) {
                    case 8:
                        if (!txtField20V.getText().isEmpty()) {
                            txtField08V.getStyleClass().remove("required-field");
                        }
                        oTransVehicle.setMaster(lnIndex, lsValue);
                        break;
                    case 20:
                        if (!txtField08V.getText().isEmpty()) {
                            txtField20V.getStyleClass().remove("required-field");
                        }
                        oTransVehicle.setMaster(lnIndex, lsValue);
                        break;
                    case 9:
                    case 11:
                    case 22:
                    case 24:
                    case 26:
                    case 28:
                        oTransVehicle.setMaster(lnIndex, lsValue);
                        break;
                    case 3:
                        if (((String) oTransVehicle.getMaster(23)).equals("")) {
                            ShowMessageFX.Warning(getStage(), "Please select Make.", "Warning", null);
                            txtField03V.setText("");
                            txtField24V.requestFocus();
                            return;
                        }

                        if (((String) oTransVehicle.getMaster(25)).equals("")) {
                            ShowMessageFX.Warning(getStage(), "Please select Model.", "Warning", null);
                            txtField03V.setText("");
                            txtField26V.requestFocus();
                            return;
                        }

                        if (lsValue.length() > 5) {
                            if (oTransVehicle.isMakeFrameOK(lsValue)) {
                                if (oTransVehicle.isModelFrameOK(lsValue)) {
                                    oTransVehicle.setMaster(lnIndex, lsValue);
                                } else {
                                    ShowMessageFX.Warning(getStage(), oTransVehicle.getMessage(), "Warning", null);
                                    txtField03V.setText("");
                                    oTransVehicle.setMaster(lnIndex, "");
                                    loadEngineFrameWindow(1, true);
                                }
                            } else {
                                ShowMessageFX.Warning(getStage(), oTransVehicle.getMessage(), "Warning", null);
                                txtField03V.setText("");
                                oTransVehicle.setMaster(lnIndex, "");
                                loadEngineFrameWindow(0, true);
                            }
                        } else {
                            if (!lsValue.isEmpty()) {
                                ShowMessageFX.Warning(getStage(), "Frame Number must not be less than 5 characters.", "Warning", null);
                                oTransVehicle.setMaster(lnIndex, "");
                                txtField03V.setText("");
                            } else {
                                oTransVehicle.setMaster(lnIndex, "");
                            }
                        }

                        break;
                    case 4:
                        if (((String) oTransVehicle.getMaster(23)).equals("")) {
                            ShowMessageFX.Warning(getStage(), "Please select Make.", "Warning", null);
                            txtField04V.setText("");
                            txtField24V.requestFocus();
                            return;
                        }

                        if (((String) oTransVehicle.getMaster(25)).equals("")) {
                            ShowMessageFX.Warning(getStage(), "Please select Model.", "Warning", null);
                            txtField04V.setText("");
                            txtField26V.requestFocus();
                            return;
                        }

                        if (lsValue.length() > 3) {
                            if (oTransVehicle.isModelEngineOK(lsValue)) {
                                oTransVehicle.setMaster(lnIndex, lsValue);
                            } else {
                                ShowMessageFX.Warning(getStage(), oTransVehicle.getMessage(), "Warning", null);
                                txtField04V.setText("");
                                oTransVehicle.setMaster(lnIndex, "");
                                loadEngineFrameWindow(2, true);
                            }
                        } else {
                            if (!lsValue.isEmpty()) {
                                ShowMessageFX.Warning(getStage(), "Engine Number must not be less than 3 characters.", "Warning", null);
                                oTransVehicle.setMaster(lnIndex, "");
                                txtField04V.setText("");
                            } else {
                                oTransVehicle.setMaster(lnIndex, "");
                            }
                        }
                        break;
                }

            } else {
                txtField.selectAll();
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    };

    /*Set TextArea to Master Class*/
    final ChangeListener<? super Boolean> txtArea_Focus = (o, ov, nv) -> {

        TextArea txtField = (TextArea) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        String txtFieldID = txtField.getId();

        if (lsValue == null) {
            return;
        }
        try {
            if (!nv) {
                switch (lnIndex) {
                    case 34:
                        oTransVehicle.setMaster(lnIndex, lsValue);
                        break;
                }
            } else {
                txtField.selectAll();
            }
        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }
    };

    private void loadVehicleHtryTable() {
//        TODO
//        try {
//            vhclhtrydata.clear();
//            if (oTransVehicle.LoadList(oTrans.getMaster("sClientID").toString())){
//                /*Set Values to table from vehicle history table*/
//                for (lnCtr = 1; lnCtr <= oTransVehicle.getItemCount(); lnCtr++) {
//                    vhclhtrydata.add(new CustomerTableVehicleInfo(
//                                String.valueOf(lnCtr) //ROW
//                            ,  (String) oTransVehicle.getDetail(lnCtr, 8)
//                            ,  (String) oTransVehicle.getDetail(lnCtr, 20)
//                            ,  (String) oTransVehicle.getDetail(lnCtr, 33)
//                            ,  (String) oTransVehicle.getDetail(lnCtr, 9)
//                            ,  (String) oTransVehicle.getDetail(lnCtr, 1)
//                            ,  (String) oTransVehicle.getDetail(lnCtr, 6)
//
//                    ));
//                }
//            }
//        } catch (SQLException e) {
//            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
//        }
    }

    /*OPEN WINDOW FOR VEHICLE DESCRIPTION ENTRY*/
    private void loadVehicleDescriptionWindow() {
        try {
            String sFormName = "Vehicle Description";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("VehicleDescriptionForm.fxml"));
            VehicleDescriptionFormController loControl = new VehicleDescriptionFormController();
            loControl.setGRider(oApp);
            fxmlLoader.setController(loControl);
            Parent parent = fxmlLoader.load();
            AnchorPane otherAnchorPane = loControl.AnchorMain;

            // Get the parent of the TabContent node
            Node tabContent = AnchorMain.getParent();
            Parent tabContentParent = tabContent.getParent();

            // If the parent is a TabPane, you can work with it directly
            if (tabContentParent instanceof TabPane) {
                TabPane tabpane = (TabPane) tabContentParent;

                for (Tab tab : tabpane.getTabs()) {
                    if (tab.getText().equals(sFormName)) {
                        tabpane.getSelectionModel().select(tab);
                        return;
                    }
                }

                Tab newTab = new Tab(sFormName, parent);
                //newTab.setStyle("-fx-font-weight: bold; -fx-pref-width: 180; -fx-font-size: 11px;");
                newTab.setStyle("-fx-font-weight: bold; -fx-pref-width: 180; -fx-font-size: 10.5px; -fx-font-family: arial;");

                tabpane.getTabs().add(newTab);
                tabpane.getSelectionModel().select(newTab);
                newTab.setOnCloseRequest(event -> {
                    if (ShowMessageFX.YesNo(null, "Close Tab", "Are you sure, do you want to close tab?") == true) {
                        if (unload != null) {
                            unload.unloadForm(otherAnchorPane, oApp, sFormName);
                        } else {
                            ShowMessageFX.Warning(getStage(), "Please notify the system administrator to configure the null value at the close button.", "Warning", pxeModuleName);
                        }
                    } else {
                        // Cancel the close request
                        event.consume();
                    }
                });

                List<String> tabName = new ArrayList<>();
                tabName = TabsStateManager.loadCurrentTab();
                tabName.remove(sFormName);
                tabName.add(sFormName);
                // Save the list of tab IDs to the JSON file
                TabsStateManager.saveCurrentTab(tabName);
            }
        } catch (IOException e) {
            e.printStackTrace();
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }

    /*OPEN WINDOW FOR VEHICLE DESCRIPTION ENTRY*/
    private void loadEngineFrameWindow(Integer fnCodeType, Boolean fbState) {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("VehicleEngineFrameFormatForm.fxml"));

            VehicleEngineFrameFormatFormController loControl = new VehicleEngineFrameFormatFormController();
            loControl.setGRider(oApp);
            loControl.setMakeID((String) oTransVehicle.getMaster(23));
            loControl.setMakeDesc((String) oTransVehicle.getMaster(24));
            loControl.setModelID((String) oTransVehicle.getMaster(25));
            loControl.setModelDesc((String) oTransVehicle.getMaster(26));
            loControl.setCodeType(fnCodeType);
            loControl.setState(fbState);
            loControl.setOpenEvent(true);
            fxmlLoader.setController(loControl);

            //load the main interface
            Parent parent = fxmlLoader.load();

            parent.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
            });

            parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    stage.setX(event.getScreenX() - xOffset);
                    stage.setY(event.getScreenY() - yOffset);
                }
            });

            //set the main interface as the scene
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("");
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        } catch (SQLException ex) {
            Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private boolean setSelection() {
        try {
            if (pbisVhclSales) {
                if (comboBox15.getSelectionModel().getSelectedIndex() < 0) {
                    ShowMessageFX.Warning(getStage(), "Please select `Vehicle Category` value.", pxeModuleName, null);
                    comboBox15.requestFocus();
                    return false;
                } else {
                    oTransVehicle.setMaster(15, String.valueOf(comboBox15.getSelectionModel().getSelectedIndex()));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerVehicleInfoFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    /*OPEN WINDOW FOR TRANSFER OWNERSHIP*/
    private void loadTransferOwnershipWindow() {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("VehicleNewOwnerForm.fxml"));

            VehicleNewOwnerFormController loControl = new VehicleNewOwnerFormController();
            loControl.setGRider(oApp);
            loControl.setObject(oTransVehicle);
            fxmlLoader.setController(loControl);

            //load the main interface
            Parent parent = fxmlLoader.load();

            parent.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
            });

            parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    stage.setX(event.getScreenX() - xOffset);
                    stage.setY(event.getScreenY() - yOffset);
                }
            });

            //set the main interface as the scene
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("");
            stage.showAndWait();

            try {
                if (oTransVehicle.OpenRecord((String) oTransVehicle.getMaster(1))) {
                    loadClientVehicleInfo();
                }

                loadVehicleHtryTable();
                pnEditMode = oTransVehicle.getEditMode();
                initVhclInfoButton(pnEditMode);
            } catch (SQLException ex) {
                Logger.getLogger(CustomerVehicleInfoFormController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException e) {
            e.printStackTrace();
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }

//    /*populate vheicle information Table*/
//    private void initVehicleInfo() {
//        tblVhcllist01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
//        tblVhcllist02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
//        tblVhcllist03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
//        tblVhcllist04.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
//        tblVhcllist05.setCellValueFactory(new PropertyValueFactory<>("tblindex05"));
//
//        tblViewVhclInfo.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
//            TableHeaderRow header = (TableHeaderRow) tblViewVhclInfo.lookup("TableHeaderRow");
//            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//                header.setReordering(false);
//            });
//        });
//
//        tblViewVhclInfo.setItems(vhclinfodata);
//    }

    /*populate vehicle history Table*/
    private void initVehicleHtry() {
        tblVhcllHsty01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
        tblVhcllHsty02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
        tblVhcllHsty03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
        tblVhcllHsty04.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
        tblVhcllHsty05.setCellValueFactory(new PropertyValueFactory<>("tblindex05"));

        tblViewVhclHsty.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewVhclHsty.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

        tblViewVhclHsty.setItems(vhclhtrydata);
    }

    /*Convert Date to String*/
    private LocalDate strToDate(String val) {
        DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(val, date_formatter);
        return localDate;
    }

    /*Set Date Value to Master Class*/
    public void getDate(ActionEvent event) {
        try {
            /*CLIENT VEHICLE INFORMATION*/
            oTransVehicle.setMaster(21, SQLUtil.toDate(String.valueOf(txtField21V.getValue()), SQLUtil.FORMAT_SHORT_DATE));
        } catch (SQLException ex) {
            Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void disableFields() {
        txtField03V.setDisable(true);
        txtField04V.setDisable(true);
        //txtField08V.setDisable(true);
        //txtField09V.setDisable(true);
        txtField11V.setDisable(true);
        //txtField20V.setDisable(true);
        //txtField21V.setDisable(true);
        //txtField22V.setDisable(true);
        txtField24V.setDisable(true);
        txtField26V.setDisable(true);
        txtField28V.setDisable(true);
        txtField30V.setDisable(true);
        txtField31V.setDisable(true);
        txtField32V.setDisable(true);
        //textArea34V.setDisable(true);

    }

    private void initVhclInfoButton(int fnValue) {
        pnRow = 0;
        /* NOTE:
               lbShow (FALSE)= invisible
               !lbShow (TRUE)= visible
         */
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

//        if (!txtField26V.getText().isEmpty()){
//            txtField03V.setDisable(!lbShow);
//            txtField04V.setDisable(!lbShow);
//        } else {
//            txtField03V.setDisable(true);
//            txtField04V.setDisable(true);
//        }
        txtField03V.setDisable(!lbShow);
        txtField04V.setDisable(!lbShow);
        txtField08V.setDisable(!lbShow);
        txtField09V.setDisable(!lbShow);
        txtField11V.setDisable(!lbShow);
        txtField20V.setDisable(!lbShow);
        txtField21V.setDisable(!lbShow);
        txtField22V.setDisable(!lbShow);
        txtField24V.setDisable(!lbShow);
        txtField26V.setDisable(!lbShow);
        txtField28V.setDisable(!lbShow);
        txtField30V.setDisable(!lbShow);
        txtField31V.setDisable(!lbShow);
        txtField32V.setDisable(!lbShow);
        textArea34V.setDisable(!lbShow);
        txtField35V.setDisable(!lbShow);
        txtField36V.setDisable(!lbShow);

        if (fnValue == EditMode.UPDATE) {
            txtField35V.setDisable(true);
        }

        btnCancel.setVisible(lbShow);
        btnCancel.setManaged(lbShow);

        btnVhclDesc.setVisible(true);  //Show Vehicle Description Entry Window
        btnEngFrm.setVisible(lbShow);  //Show Engine Frame Entry Window
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);

        btnVhclAvl.setVisible(false);
        btnVhclMnl.setVisible(false);
        comboBox15.setDisable(true);

        if (pnEditMode == EditMode.ADDNEW) {
            if (!pbisVhclSales) {
                btnVhclAvl.setVisible(true);
                btnVhclMnl.setVisible(true);
            } else {
                comboBox15.setDisable(false);
            }
        }

        btnEdit.setVisible(false);
        btnEdit.setManaged(false);
        btnTransfer.setVisible(false);
        btnTransfer.setManaged(false);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);

        if (fnValue == EditMode.READY) {
            btnEdit.setVisible(true);
            btnEdit.setManaged(true);
            if (!pbisVhclSales) {
                btnTransfer.setVisible(true);
                btnTransfer.setManaged(true);
            }
        }

        if (pbisVhclSales) {
            anchorMisc.setVisible(false);
            anchorMisc.setManaged(false);
            gridMisc.setVisible(false);
            gridMisc.setManaged(false);

            anchorPurch.setVisible(true);
            anchorPurch.setManaged(true);
            gridPurch.setVisible(true);
            gridPurch.setManaged(true);
            anchorSold.setVisible(true);
            anchorSold.setManaged(true);
            gridSold.setVisible(true);
            gridSold.setManaged(true);

//            vboxSales.setVisible(true);
//            vboxSales.setManaged(true);
            txtField35V.setDisable(true);
            txtField36V.setDisable(true);
        } else {
            anchorMisc.setVisible(true);
            anchorMisc.setManaged(true);
            gridMisc.setVisible(true);
            gridMisc.setManaged(true);
            anchorPurch.setVisible(false);
            anchorPurch.setManaged(false);
            gridPurch.setVisible(false);
            gridPurch.setManaged(false);
            anchorSold.setVisible(false);
            anchorSold.setManaged(false);
            gridSold.setVisible(false);
            gridSold.setManaged(false);
//            vboxService.setVisible(true);
//            vboxService.setManaged(true);

        }

    }

    private void clearVehicleInfoField() {
        try {

            oTransVehicle.setMaster(5, "");

            txtField24V.clear();
            oTransVehicle.setMaster(23, ""); //make id
            oTransVehicle.setMaster(24, ""); //make name
            txtField26V.clear();
            oTransVehicle.setMaster(25, ""); //model id
            oTransVehicle.setMaster(26, ""); //model name
            txtField28V.clear();
            oTransVehicle.setMaster(27, ""); //type id
            oTransVehicle.setMaster(28, ""); //type name
            txtField30V.clear();
            oTransVehicle.setMaster(29, ""); //color id
            oTransVehicle.setMaster(30, ""); //color name
            txtField31V.clear();
            oTransVehicle.setMaster(31, ""); //transmission
            txtField32V.clear();
            oTransVehicle.setMaster(32, ""); //year

            txtField20V.clear();
            oTransVehicle.setMaster(20, ""); //plate no
            txtField03V.clear();
            oTransVehicle.setMaster(3, ""); //frame no
            txtField04V.clear();
            oTransVehicle.setMaster(4, ""); //engine no
            txtField11V.clear();
            oTransVehicle.setMaster(11, ""); //KeyNo
            txtField08V.clear();
            oTransVehicle.setMaster(8, ""); //cs no

            /*Clear Red Color for required fileds*/
            txtField24V.getStyleClass().remove("required-field");
            txtField26V.getStyleClass().remove("required-field");
            txtField28V.getStyleClass().remove("required-field");
            txtField31V.getStyleClass().remove("required-field");
            txtField30V.getStyleClass().remove("required-field");
            txtField32V.getStyleClass().remove("required-field");
            txtField20V.getStyleClass().remove("required-field");
            txtField08V.getStyleClass().remove("required-field");
            txtField03V.getStyleClass().remove("required-field");
            txtField04V.getStyleClass().remove("required-field");
        } catch (SQLException ex) {
            Logger.getLogger(CustomerVehicleInfoFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void clearFields() {
        if (pnEditMode == EditMode.UNKNOWN) {
            vhclinfodata.clear();
            vhclhtrydata.clear();
        }
        txtField35V.clear();
        txtField36V.clear();

        if (pbisVhclSales) {
            txtField40V.clear();
            txtField41V.clear();
            txtField42V.clear();
        }

        txtField03V.clear();
        txtField04V.clear();
        txtField08V.clear();
        txtField09V.clear();
        txtField11V.clear();
        txtField20V.clear();
        txtField21V.setValue(LocalDate.of(1900, Month.JANUARY, 1));
        txtField22V.clear();
        txtField24V.clear();
        txtField26V.clear();
        txtField28V.clear();
        txtField30V.clear();
        txtField31V.clear();
        txtField32V.clear();
        textArea34V.clear();
        textArea37V.clear();
        textArea38V.clear();

        /*Clear Red Color for required fileds*/
        removeRequired();
    }

    private void removeRequired() {
        txtFieldAnimation.removeShakeAnimation(txtField24V, txtFieldAnimation.shakeTextField(txtField24V), "required-field");
        txtFieldAnimation.removeShakeAnimation(txtField26V, txtFieldAnimation.shakeTextField(txtField26V), "required-field");
        txtFieldAnimation.removeShakeAnimation(txtField28V, txtFieldAnimation.shakeTextField(txtField28V), "required-field");
        txtFieldAnimation.removeShakeAnimation(txtField31V, txtFieldAnimation.shakeTextField(txtField31V), "required-field");
        txtFieldAnimation.removeShakeAnimation(txtField30V, txtFieldAnimation.shakeTextField(txtField30V), "required-field");
        txtFieldAnimation.removeShakeAnimation(txtField32V, txtFieldAnimation.shakeTextField(txtField32V), "required-field");
        txtFieldAnimation.removeShakeAnimation(txtField20V, txtFieldAnimation.shakeTextField(txtField20V), "required-field");
        txtFieldAnimation.removeShakeAnimation(txtField08V, txtFieldAnimation.shakeTextField(txtField08V), "required-field");
        txtFieldAnimation.removeShakeAnimation(txtField03V, txtFieldAnimation.shakeTextField(txtField03V), "required-field");
        txtFieldAnimation.removeShakeAnimation(txtField04V, txtFieldAnimation.shakeTextField(txtField04V), "required-field");
    }

    private String getParentTabTitle() {
        Node parent = AnchorMain.getParent();
        Parent tabContentParent = parent.getParent();

        if (tabContentParent instanceof TabPane) {
            TabPane tabPane = (TabPane) tabContentParent;
            Tab tab = findTabByContent(tabPane, AnchorMain);
            if (tab != null) {
                pxeModuleName = tab.getText();
                return tab.getText().toUpperCase();
            }
        }

        return null; // No parent Tab found
    }

    private Tab findTabByContent(TabPane tabPane, Node content) {
        for (Tab tab : tabPane.getTabs()) {
            if (tab.getContent() == content) {
                return tab;
            }
        }
        return null;
    }
}
