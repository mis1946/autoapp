/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.auto.app.views;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.TAB;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.StageStyle;
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
public class ActivityFormController implements Initializable, ScreenInterface {

    private GRider oApp;
    private Activity oTrans;
    private MasterCallback oListener;
    private ObservableList<ActivityMemberTable> actMembersData = FXCollections.observableArrayList();
    unloadForm unload = new unloadForm(); //Used in Close Button
    private final String pxeModuleName = "Activity"; //Form Title
    private int pnEditMode;//Modifying fields
    private int pnRow;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnSave;
    @FXML
    private Label lblApprovedBy;
    @FXML
    private Label lblApprovedDate;
    @FXML
    private Button btnAddRowTasks;
    @FXML
    private Button btnAddRowBudget;
    @FXML
    private Button btnActivityHistory;
    @FXML
    private Button btnPrint;
    @FXML
    private Button btnClose;
    @FXML
    private TableView<?> tblViewTasks;
    @FXML
    private TableView<?> tblViewBudget;
    @FXML
    private TableView<?> tblViewCity;
    @FXML
    private Button btnRemoveTasks;
    @FXML
    private Button btnRemoveBudget;
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private TabPane tabPane;
    @FXML
    private Button btnBrowse;
    ObservableList<String> cType = FXCollections.observableArrayList("EVENT", "SALES CALL", "PROMO");
    @FXML
    private Button btnActivityMembersSearch;
    @FXML
    private Button btnActivityMemRemove;
    @FXML
    private TableView<ActivityMemberTable> tblViewActivityMembers;
    @FXML
    private Button btnVhclModelsSearch;
    @FXML
    private Button btnVhlModelRemove;
    @FXML
    private TableView<?> tblViewVhclModels;
    @FXML
    private TableColumn<ActivityMemberTable, String> tblActvtyMembersRow;
    @FXML
    private TableColumn<ActivityMemberTable, Boolean> tblselected;
    @FXML
    private CheckBox selectAllCheckBoxEmployee;
    double xOffset;
    double yOffset;
    @FXML
    private TextArea textArea02;    //sActTitle
    @FXML
    private TextArea textArea03;  //sActDescx
    @FXML
    private DatePicker dateFrom06;   //dDateFrom
    @FXML
    private DatePicker dateTo07;  //dDateThru
    @FXML
    private TextField txtField26;  //sBranchNm
    @FXML
    private TextField txtField28;  //sProvName
    @FXML
    private TextField txtField05;  //sActTypDs
    @FXML
    private TextField textSeek01; //Search Activity No
    @FXML
    private TextField textSeek02; //Search Activity Name
    @FXML
    private TextField txtField32; //Branch
    @FXML
    private TextArea textArea08; //Street
    @FXML
    private Button btnAddSource;
    @FXML
    private Button btnCitySearch;
    @FXML
    private Button btnCityRemove;
    @FXML
    private TextArea textArea15; //sLogRemrk
    @FXML
    private TextArea textArea16; //sRemarksx
    @FXML
    private TextField txtField24; //sDeptName
    @FXML
    private TextField txtField25; //sCompnyNm
    @FXML
    private TextField txtField12; //nTrgtClnt
    @FXML
    private TextField txtField11; //nRcvdBdgt
    @FXML
    private TextArea textArea09; //sCompnynx
    @FXML
    private TableColumn<ActivityMemberTable, String> tblindex25;
    @FXML
    private TableColumn<ActivityMemberTable, String> tblindex24;
    @FXML
    private ComboBox<String> comboBox29; //sEventTyp
    @FXML
    private TextField txtField01; //sActvtyID

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oListener = (int fnIndex, Object foValue) -> {
            System.out.println("Set Class Value " + fnIndex + "-->" + foValue);
        };
        oTrans = new Activity(oApp, oApp.getBranchCode(), true); //Initialize ClientMaster
        oTrans.setCallback(oListener);
        oTrans.setWithUI(true);
        txtField32.setDisable(true);
        setCapsLockBehavior(txtField25); //sCompyNm
        setCapsLockBehavior(txtField05); //sActTypDs
        setCapsLockBehavior(txtField26); //sBranchNm
        setCapsLockBehavior(txtField12); //nTrgtClnt
        setCapsLockBehavior(txtField28); //sProvName
        setCapsLockBehavior(txtField24); //sDeptName
        setCapsLockBehavior(txtField11); //nRcvdBdgt
        setCapsLockBehavior(txtField32); //Branch
        setCapsLockBehavior(txtField12); //nTrgtClnt

        setCapsLockBehavior(textArea09);
        setCapsLockBehavior(textArea02);
        setCapsLockBehavior(textArea03);
        setCapsLockBehavior(textArea15);
        setCapsLockBehavior(textArea16);

        btnBrowse.setOnAction(this::cmdButton_Click);
        btnCitySearch.setOnAction(this::cmdButton_Click);
        btnCityRemove.setOnAction(this::cmdButton_Click);
        btnActivityMembersSearch.setOnAction(this::cmdButton_Click);
        btnVhclModelsSearch.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click);
        btnAdd.setOnAction(this::cmdButton_Click);
        btnSave.setOnAction(this::cmdButton_Click);
        btnEdit.setOnAction(this::cmdButton_Click);
        btnAddSource.setOnAction(this::cmdButton_Click);

        /*Set Focus to set Value to Class*/
        txtField05.focusedProperty().addListener(txtField_Focus); //sActTypDs
        txtField26.focusedProperty().addListener(txtField_Focus); //sBranchNm
        txtField12.focusedProperty().addListener(txtField_Focus); //nTrgtClnt
        txtField28.focusedProperty().addListener(txtField_Focus); //sProvName
        txtField24.focusedProperty().addListener(txtField_Focus); //sDeptName
        txtField11.focusedProperty().addListener(txtField_Focus); //nRcvdBdgt
//        txtField32.focusedProperty().addListener(txtField_Focus); //Branch
        txtField12.focusedProperty().addListener(txtField_Focus); //nTrgtClnt

        textArea08.focusedProperty().addListener(txtArea_Focus);
        textArea15.focusedProperty().addListener(txtArea_Focus);
        textArea16.focusedProperty().addListener(txtArea_Focus);
        textArea09.focusedProperty().addListener(txtArea_Focus);
        textArea03.focusedProperty().addListener(txtArea_Focus);
        textArea02.focusedProperty().addListener(txtArea_Focus);
        /* TxtField KeyPressed */
        txtField05.setOnKeyPressed(this::txtField_KeyPressed); //sActTypDs
        txtField26.setOnKeyPressed(this::txtField_KeyPressed); //sBranchNm
        txtField12.setOnKeyPressed(this::txtField_KeyPressed); //nTrgtClnt
        txtField28.setOnKeyPressed(this::txtField_KeyPressed);//sProvName
        txtField24.setOnKeyPressed(this::txtField_KeyPressed); //sDeptName
        txtField11.setOnKeyPressed(this::txtField_KeyPressed); //nRcvdBdgt
//        txtField32.setOnKeyPressed(this::txtField_KeyPressed); //Branch
        txtField12.setOnKeyPressed(this::txtField_KeyPressed); //nTrgtClnt
        txtField25.setOnKeyPressed(this::txtField_KeyPressed); //sCompyNm

        /* TextArea KeyPressed */
        textArea08.setOnKeyPressed(this::txtArea_KeyPressed);
        textArea15.setOnKeyPressed(this::txtArea_KeyPressed);
        textArea16.setOnKeyPressed(this::txtArea_KeyPressed);
        textArea09.setOnKeyPressed(this::txtArea_KeyPressed);
        textArea03.setOnKeyPressed(this::txtArea_KeyPressed);
        textArea02.setOnKeyPressed(this::txtArea_KeyPressed);
        textSeek01.setOnKeyPressed(this::txtField_KeyPressed); //Activity No Search
        textSeek02.setOnKeyPressed(this::txtField_KeyPressed); //Activity Title Search

        comboBox29.setItems(cType);
        comboBox29.setOnAction(e -> {
            String selectedType = comboBox29.getValue();// Retrieve the type ID for the selected type
            // Set the type ID in the text field
            try {
                oTrans.setMaster(29, selectedType); // Pass the selected type to the setMaster method
            } catch (SQLException ex) {
                Logger.getLogger(ActivityFormController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        );

        Pattern pattern = Pattern.compile("[\\d\\p{Punct}]*");
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            if (!change.getControlNewText().matches(pattern.pattern())) {
                return null;
            }
            return change;
        });

        txtField12.setTextFormatter(new InputTextFormatter(pattern));  //nTrgtClnt
        txtField11.setTextFormatter(new InputTextFormatter(pattern));  //nRcvdBdgt
        pnEditMode = EditMode.UNKNOWN;
        initButton(pnEditMode);
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

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private void cmdButton_Click(ActionEvent event) {
        try {
            String lsButton = ((Button) event.getSource()).getId();
            switch (lsButton) {
                case "btnAdd": //create
                    if (oTrans.NewRecord()) {
                        clearFields();
                        loadActivityField();
                        loadActMembersTable();
                    } else {
                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                    }
                    pnEditMode = oTrans.getEditMode();
                    break;
                case "btnEdit":
                    if (oTrans.UpdateRecord()) {
                        pnEditMode = oTrans.getEditMode();
                    } else {
                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                    }
                    break;
                case "btnSave":
                    //Validate before saving
                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to save?") == true) {
                        if (textArea02.getText().trim().equals("")) {
                            ShowMessageFX.Warning(getStage(), "Please enter a value for Activity Title.", "Warning", null);
                            textArea02.requestFocus();
                            return;
                        }
//                        if (txtField05.getText().trim().equals("")) {
//                              ShowMessageFX.Warning(getStage(), "Please enter a value for Activity Description.","Warning", null);
//                              txtField05.requestFocus();
//                              return;
//                         }
                        if (textArea03.getText().trim().equals("")) {
                            ShowMessageFX.Warning(getStage(), "Please enter a valid value for Activity Description", "Warning", null);
                            textArea03.requestFocus();
                            return;
                        }
                        if (textArea15.getText().trim().equals("")) {
                            ShowMessageFX.Warning(getStage(), "Please enter a value for Logistic Remarks .", "Warning", null);
                            textArea15.requestFocus();
                            return;
                        }
                        if (textArea16.getText().trim().equals("")) {
                            ShowMessageFX.Warning(getStage(), "Please enter a value for Activity Remarks.", "Warning", null);
                            textArea16.requestFocus();
                            return;
                        }
                        if (txtField24.getText().trim().equals("")) {
                            ShowMessageFX.Warning(getStage(), "Please enter a valid value for Department in charge.", "Warning", null);
                            txtField24.requestFocus();
                            return;
                        }
                        if (txtField25.getText().trim().equals("")) {
                            ShowMessageFX.Warning(getStage(), "Please enter a value for Person in charge.", "Warning", null);
                            txtField25.requestFocus();
                            return;
                        }
//                        if (txtField26.getText().trim().equals("")) {
//                              ShowMessageFX.Warning(getStage(), "Please enter a value for Branch in charge","Warning", null);
//                              txtField26.requestFocus();
//                              return;
//                         }
//                        if (txtField32.getText().trim().equals("")) {
//                              ShowMessageFX.Warning(getStage(), "Please enter a value for Telephone No.","Warning", null);
//                              txtField32.requestFocus();
//                              return;
//                         }
                        if (txtField12.getText().trim().equals("")) {
                            ShowMessageFX.Warning(getStage(), "Please enter a value for No. of Target Clients.", "Warning", null);
                            txtField12.requestFocus();
                            return;
                        }
                        if (txtField11.getText().trim().equals("")) {
                            ShowMessageFX.Warning(getStage(), "Please enter a value for Total Event Budget.", "Warning", null);
                            txtField11.requestFocus();
                            return;
                        }
                        if (txtField28.getText().trim().equals("")) {
                            ShowMessageFX.Warning(getStage(), "Please enter a value for Province.", "Warning", null);
                            txtField28.requestFocus();
                            return;
                        }
//                        if (textArea08.getText().trim().equals("")) {
//                              ShowMessageFX.Warning(getStage(), "Please enter a value for Municipality.","Warning", null);
//                              textArea08.requestFocus();
//                              return;
//                         }
                        if (textArea09.getText().trim().equals("")) {
                            ShowMessageFX.Warning(getStage(), "Please enter a value for Establishment.", "Warning", null);
                            textArea09.requestFocus();
                            return;
                        }
                        //Proceed Saving
                        if (oTrans.SaveRecord()) {
                            ShowMessageFX.Information(getStage(), "Transaction save successfully.", pxeModuleName, null);
                            loadActivityField();
                            pnEditMode = oTrans.getEditMode();
                        } else {
                            ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", "Error while saving Activity Information");
                        }
                    }
                    break;
                case "btnAddSource":
                    loadActTypeAddSourceDialog();
                    break;
                case "btnActivityMembersSearch":
                    loadActivityMemberDialog();
                    break;
                case "btnVhclModelsSearch":
                    loadActivityVehicleEntryDialog();
                    break;
                case "btnCitySearch":
                    loadTownDialog();
                    break;
                case "btnCityRemove":
                    break;
                case "btnBrowse":
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (ShowMessageFX.OkayCancel(null, "Confirmation", "You have unsaved data. Are you sure you want to browse a new record?")) {
                        } else {
                            return;
                        }

                    }

                    try {
                        if (oTrans.SearchRecord(textSeek01.getText(), true)) {
                            loadActivityField();
                            pnEditMode = oTrans.getEditMode();
                        } else {
                            ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                            pnEditMode = EditMode.UNKNOWN;
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(ActivityFormController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    break;
                case "btnActivityHistory":
                    break;
                case "btnPrint":
                    break;//close tab
                case "btnClose": //close tab
                    if (ShowMessageFX.OkayCancel(null, "Close Tab", "Are you sure you want to close this Tab?") == true) {
                        if (unload != null) {
                            unload.unloadForm(AnchorMain, oApp, pxeModuleName);
                        } else {
                            ShowMessageFX.Warning(null, "Warning", "Please notify the system administrator to configure the null value at the close button.");
                        }
                        break;
                    } else {
                        return;
                    }
            }
            initButton(pnEditMode);
        } catch (IOException ex) {
            Logger.getLogger(ActivityFormController.class.getName()).log(Level.SEVERE, null, ex);
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

    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        try {
            if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
                event.consume();
                CommonUtils.SetNextFocus((TextField) event.getSource());
                switch (lnIndex) {
                    case 1:
                        if (oTrans.SearchRecord(textSeek01.getText(), false)) {
                            loadActivityField();
                            pnEditMode = oTrans.getEditMode();
                        } else {
                            ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                        }

                        break;
                    case 2:
                        if (oTrans.SearchRecord(textSeek02.getText(), false)) {
                            loadActivityField();
                            pnEditMode = oTrans.getEditMode();
                        } else {
                            ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                        }
                        break;
                    case 5:
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
                        if (oTrans.searchEventType(selectedType)) {
                            loadActivityField();
                        } else {
                            ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                        }
                        break;

                    case 24:
                        if (oTrans.searchDepartment(txtField.getText())) {
                            loadActivityField();
                        } else {
                            ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                        }
                        break;
                    case 25:
                        if (oTrans.searchEmployee(txtField.getText())) {
                            loadActivityField();
                        } else {
                            ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                        }
                        break;
                    case 26:
                        if (oTrans.searchBranch()) {
                            loadActivityField();
                        } else {
                            ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                        }
                        break;
                    case 28:
                        if (oTrans.searchProvince(txtField.getText())) {
                            loadActivityField();
                            pnEditMode = oTrans.getEditMode();
                        } else {
                            ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                        }

                        break;
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(ActivityFormController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
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
                    case 5:  //sActTypDs
                    case 24: //sDeptName
                    case 25: //sCompnyNm
                    case 26: //sBranchNm
                    case 12: //nTrgtClnt
                    case 11: //nRcvdBdgt
                    case 28: //sProvName
                        oTrans.setMaster(lnIndex, lsValue); // Handle Encoded Value
                        break;
                }
            } else {
                txtField.selectAll();

            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerFormController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    };

    /*Set TextArea to Master Class*/
    final ChangeListener<? super Boolean> txtArea_Focus = (o, ov, nv) -> {

        TextArea textArea = (TextArea) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(textArea.getId().substring(8, 10));
        String lsValue = textArea.getText();

        if (lsValue == null) {
            return;
        }
        try {
            if (!nv) {
                /*Lost Focus*/
                switch (lnIndex) {
                    case 2:        //sActTitle
                    case 3:        //sActDescx
//                        case 8:        //sAddressx
                    case 15:        //sLogRemrk
                    case 16:        //sRemarksx
                    case 9:        //sCompnynx
                        oTrans.setMaster(lnIndex, lsValue);
                        break;
                }
            } else {
                textArea.selectAll();
            }
        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }
    };
    //Act Type Add Source Dialog

    private void loadActTypeAddSourceDialog() throws IOException {
        /**
         * if state = true : ADD else if state = false : UPDATE *
         */
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("ActivityTypeAddSource.fxml"));

            ActivityTypeAddSourceController loControl = new ActivityTypeAddSourceController();
            loControl.setGRider(oApp);
            loControl.setObject(oTrans);
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
                    double xOffset = 0;
                    stage.setX(event.getScreenX() - xOffset);
                    double yOffset = 0;
                    stage.setY(event.getScreenY() - yOffset);
                }
            });

            //set the main interface as the scene/*
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
        }
    }

    //Activity Members Entry Dialog
    private void loadActivityMemberDialog() throws IOException {
        /**
         * if state = true : ADD else if state = false : UPDATE *
         */
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("ActivityMemberEntryDialog.fxml"));

            ActivityMemberEntryDialogController loControl = new ActivityMemberEntryDialogController();
            loControl.setGRider(oApp);
            loControl.setObject(oTrans);
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
                    double xOffset = 0;
                    stage.setX(event.getScreenX() - xOffset);
                    double yOffset = 0;
                    stage.setY(event.getScreenY() - yOffset);
                }
            });

            //set the main interface as the scene/*
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
        }
    }

    //Activity Vehicle Entry Dialog
    private void loadActivityVehicleEntryDialog() throws IOException {
        /**
         * if state = true : ADD else if state = false : UPDATE *
         */
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("ActivityVehicleEntryDialog.fxml"));

            ActivityVehicleEntryDialogController loControl = new ActivityVehicleEntryDialogController();
            loControl.setGRider(oApp);
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
                    double xOffset = 0;
                    stage.setX(event.getScreenX() - xOffset);
                    double yOffset = 0;
                    stage.setY(event.getScreenY() - yOffset);
                }
            });

            //set the main interface as the scene/*
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("");
            stage.showAndWait();

//            loadInquiryAdvances();
        } catch (IOException e) {
            e.printStackTrace();
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }

    private void loadTownDialog() {
        /**
         * if state = true : ADD else if state = false : UPDATE *
         */
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("TownCityMainEntryDialog.fxml"));

            TownCityMainEntryDialogController loControl = new TownCityMainEntryDialogController();
            loControl.setGRider(oApp);
            loControl.setObject(oTrans);
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
                    double xOffset = 0;
                    stage.setX(event.getScreenX() - xOffset);
                    double yOffset = 0;
                    stage.setY(event.getScreenY() - yOffset);
                }
            });

            //set the main interface as the scene/*
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("");
            stage.showAndWait();

//            loadInquiryAdvances();
        } catch (IOException e) {
            e.printStackTrace();
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }

    private Stage getStage() {
        return (Stage) textSeek01.getScene().getWindow();
    }

    /*Convert Date to String*/
    private LocalDate strToDate(String val) {
        DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(val, date_formatter);
        return localDate;
    }

    //Activity Member
    private void loadActMembersTable() {
        try {
            // Clear the previous data in the list
            actMembersData.clear();
            String departmentID = "";

            // Load the activity members using the appropriate parameters
            String fsValue = departmentID; // Use the selected department's ID as fsValue
            boolean fbLoadEmp = true; // Set the fbLoadEmp to true to load employees
            if (oTrans.loadEmployee(fsValue, fbLoadEmp)) {
                for (int lnCtr = 1; lnCtr <= oTrans.getActMemberCount(); lnCtr++) {
                    actMembersData.add(new ActivityMemberTable(
                            String.valueOf(lnCtr), // ROW
                            "", // Replace with the appropriate value for the second column
                            oTrans.getDepartment(lnCtr, "sDeptName").toString(),
                            oTrans.getDepartment(lnCtr, "sDeptIDxx").toString(), // Replace with the appropriate value for the fourth column
                            oTrans.getActMember(lnCtr, "sCompnyNm").toString(), // Fifth column (Department)
                            oTrans.getActMember(lnCtr, "sEmployID").toString() // Replace with the appropriate value for the sixth column
                    ));
                }

                // Set the actMembersData list as the items for tblViewActivityMembers TableView
                tblViewActivityMembers.setItems(actMembersData);

                // Call the initActMembersTable() method to initialize the TableView columns if needed
                initActMembersTable();
            }
        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }
    }

    private void loadActivityField() {
        try {
            txtField01.setText((String) oTrans.getMaster(1)); //sActvtyID
            dateFrom06.setValue(strToDate(CommonUtils.xsDateShort((Date) oTrans.getMaster(6)))); //dDateFrom
            dateTo07.setValue(strToDate(CommonUtils.xsDateShort((Date) oTrans.getMaster(7)))); //dDateThru
            String selectedItem = oTrans.getMaster(29).toString();
            if (selectedItem.equals("sal")) {
                selectedItem = "SALES CALL";
            }
            if (selectedItem.equals("eve")) {
                selectedItem = "EVENT";
            }
            if (selectedItem.equals("pro")) {
                selectedItem = "PROMO";
            }// Assuming oTrans.getMaster(29) returns the selected item as a string
            comboBox29.getSelectionModel().select(selectedItem);
            txtField05.setText((String) oTrans.getMaster(5)); //sActSrcex
            textArea02.setText((String) oTrans.getMaster(2)); //sActTitle
            textArea03.setText((String) oTrans.getMaster(3)); //sActDescx
            textArea15.setText((String) oTrans.getMaster(15)); //sLogRemrk
            textArea16.setText((String) oTrans.getMaster(16)); //sRemarksx
            txtField24.setText((String) oTrans.getMaster(24));//sDeptName
            txtField25.setText((String) oTrans.getMaster(25)); //sCompnyNm
            txtField26.setText((String) oTrans.getMaster(26)); //sBranchNm
//               txtField32.setText((String) oTrans.getMaster(32)); //Branch
            txtField12.setText(String.valueOf((int) oTrans.getMaster(12))); //nTrgtClnt
            txtField11.setText(String.valueOf((double) oTrans.getMaster(11)));  //nRcvdBdg
            txtField28.setText((String) oTrans.getMaster(28)); //sProvName
//               textArea08.setText((String) oTrans.getMaster(8)); //Street
            textArea09.setText((String) oTrans.getMaster(9));  //sCompnynx

        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }
    }
//    public void loadActivityMembers(){
//          try {
//               /*Populate table*/
//               actmembersData.clear();
//               for (int lnCtr = 1; lnCtr <= oTrans.getActMemberCount(); lnCtr++){
//                    actmembersData.add(new ActivityMembersTableList(
//                    String.valueOf(lnCtr),
//                    oTrans.getActMember(lnCtr,"sTransNox").toString(), //Priority Unit
//                    oTrans.getActMember(lnCtr,"sCompnyNm").toString(),
//                    oTrans.getActMember(lnCtr,"sDeptName").toString() // Vehicle Description
//                    ));
//               }
//          } catch (SQLException e) {
//               ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
//          }
//     }

    private void initButton(int fnValue) {
        pnRow = 0;
        /* NOTE:
                  lbShow (FALSE)= invisible
                  !lbShow (TRUE)= visible
         */
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

        //Fields
        txtField01.setText(""); //sActvtyID
        dateFrom06.setDisable(!lbShow); //dDateFrom
        dateTo07.setDisable(!lbShow); //dDateThru
        comboBox29.setDisable(!lbShow); //sEventTyp
        txtField05.setDisable(!lbShow); //sActTypDs
        textArea02.setDisable(!lbShow); //sActTitle
        textArea03.setDisable(!lbShow); //sActDescx
        textArea15.setDisable(!lbShow); //sLogRemrk
        textArea16.setDisable(!lbShow); //sRemarksx
        txtField24.setDisable(!lbShow);//sDeptName
        txtField25.setDisable(!lbShow); //sCompnyNm
        txtField26.setDisable(!lbShow); //sBranchNm
//        txtField32.setDisable(!lbShow); //Branch
        txtField12.setDisable(!lbShow); //nTrgtClnt
        txtField11.setDisable(!lbShow);  //nRcvdBdg
        txtField28.setDisable(!lbShow); //sProvName
        textArea08.setDisable(!lbShow); //Street
        textArea09.setDisable(!lbShow);  //sCompnynx

        //Button
        btnCitySearch.setDisable(!lbShow);
        btnCityRemove.setDisable(!lbShow);
        btnActivityMembersSearch.setDisable(!lbShow);
        btnActivityMemRemove.setDisable(!lbShow);
        btnVhclModelsSearch.setDisable(!lbShow);
        btnVhlModelRemove.setDisable(!lbShow);
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        //if lbShow = false hide btn
        btnEdit.setVisible(false);
        btnEdit.setManaged(false);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);
        btnActivityHistory.setVisible(false);
        btnActivityHistory.setManaged(false);
        btnPrint.setVisible(false);
        btnPrint.setManaged(false);

        if (fnValue == EditMode.READY) { //show edit if user clicked save / browse
            btnEdit.setVisible(true);
            btnEdit.setManaged(true);

            btnActivityHistory.setVisible(true);
            btnActivityHistory.setManaged(true);
            btnPrint.setVisible(true);
            btnPrint.setManaged(true);
        }
    }

    public void clearFields() {
        pnRow = 0;
        txtField01.setText(""); //sActvtyID
        dateFrom06.setValue(null); //dDateFrom
        dateTo07.setValue(null); //dDateThru
        comboBox29.setValue(null); //sEventTyp
        txtField05.clear(); //sActTypDs
        textArea02.clear(); //sActTitle
        textArea03.clear(); //sActDescx
        textArea15.clear(); //sLogRemrk
        textArea16.clear(); //sRemarksx
        txtField24.clear();//sDeptName
        txtField25.clear(); //sCompnyNm
        txtField26.clear(); //sBranchNm
//        txtField32.clear(); //Branch
        txtField12.clear(); //nTrgtClnt
        txtField11.clear();  //nRcvdBdg
        txtField28.clear(); //sProvName
        textArea08.clear(); //Street
        textArea09.clear();  //sCompnynx
    }

    private void initActMembersTable() {
        tblActvtyMembersRow.setCellValueFactory(new PropertyValueFactory<>("tblindexRow"));
        tblselected.setCellValueFactory(new PropertyValueFactory<>("select"));
        tblindex24.setCellValueFactory(new PropertyValueFactory<>("tblindex24"));
        tblindex25.setCellValueFactory(new PropertyValueFactory<>("tblindex25"));
    }

}
