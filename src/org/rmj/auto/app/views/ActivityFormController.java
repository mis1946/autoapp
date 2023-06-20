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
import javafx.scene.control.DateCell;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.TAB;
import javafx.scene.input.KeyEvent;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.SQLUtil;
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
    private ObservableList<ActivityTownEntryTableList> townCitydata = FXCollections.observableArrayList();
    private ObservableList<ActivityVchlEntryTable> actVhclModelData = FXCollections.observableArrayList();
    unloadForm unload = new unloadForm(); //Used in Close Button
    CancelForm cancelform = new CancelForm(); //Object for closing form
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
    private TableView tblViewTasks;
    @FXML
    private TableView tblViewBudget;
    @FXML
    private TableView<ActivityTownEntryTableList> tblViewCity;
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
    private TableView<ActivityVchlEntryTable> tblViewVhclModels;
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
    private TextArea textArea08; //Street/Barangay
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
    @FXML
    private Button btnCancel;
    @FXML
    private TableColumn<ActivityTownEntryTableList, String> tblRowCity;
    @FXML
    private TableColumn<ActivityTownEntryTableList, Boolean> tblselectCity;
    @FXML
    private TableColumn<ActivityTownEntryTableList, String> tblTownCity;
    @FXML
    private CheckBox selectAllCity;
    @FXML
    private TableColumn<ActivityVchlEntryTable, String> tblindexVchlRow;
    @FXML
    private CheckBox selectAllVchlMode;
    @FXML
    private TableColumn<ActivityVchlEntryTable, String> tblVchlDescription;
    @FXML
    private TableColumn<ActivityVchlEntryTable, Boolean> tblVchlSelect;
    @FXML
    private Button btnActCancel;
    @FXML
    private Label lblCancelStatus;

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

        setCapsLockBehavior(textArea09); //sCompnynx
        setCapsLockBehavior(textArea02); //sActTitle
        setCapsLockBehavior(textArea03); //sActDescx
        setCapsLockBehavior(textArea15); //sLogRemrk
        setCapsLockBehavior(textArea16); //sRemarksx
        setCapsLockBehavior(textArea08); //sAddressx

        btnBrowse.setOnAction(this::cmdButton_Click);
        btnCityRemove.setOnAction(this::cmdButton_Click);
        btnCitySearch.setOnAction(this::cmdButton_Click);

        btnActivityMembersSearch.setOnAction(this::cmdButton_Click);
        btnActivityMemRemove.setOnAction(this::cmdButton_Click);
        btnVhclModelsSearch.setOnAction(this::cmdButton_Click);
        btnVhlModelRemove.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click);
        btnAdd.setOnAction(this::cmdButton_Click);
        btnSave.setOnAction(this::cmdButton_Click);
        btnEdit.setOnAction(this::cmdButton_Click);
        btnAddSource.setOnAction(this::cmdButton_Click);
        btnCancel.setOnAction(this::cmdButton_Click);
        btnPrint.setOnAction(this::cmdButton_Click);
        btnActCancel.setOnAction(this::cmdButton_Click);

        /*Set Focus to set Value to Class*/
        txtField05.focusedProperty().addListener(txtField_Focus); //sActTypDs
        txtField26.focusedProperty().addListener(txtField_Focus); //sBranchNm
        txtField12.focusedProperty().addListener(txtField_Focus); //nTrgtClnt
        txtField28.focusedProperty().addListener(txtField_Focus); //sProvName
        txtField24.focusedProperty().addListener(txtField_Focus); //sDeptName
        txtField11.focusedProperty().addListener(txtField_Focus); //nRcvdBdgt
//        txtField32.focusedProperty().addListener(txtField_Focus); //Branch
        txtField12.focusedProperty().addListener(txtField_Focus); //nTrgtClnt

        textArea08.focusedProperty().addListener(txtArea_Focus); //sAddressx
        textArea15.focusedProperty().addListener(txtArea_Focus); //sLogRemrk
        textArea16.focusedProperty().addListener(txtArea_Focus); //sRemarksx
        textArea09.focusedProperty().addListener(txtArea_Focus);  //sCompnynx
        textArea03.focusedProperty().addListener(txtArea_Focus);   //sActDescx
        textArea02.focusedProperty().addListener(txtArea_Focus);  //sActTitle
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
        //textArea08.setOnKeyPressed(this::txtArea_KeyPressed);
        textArea15.setOnKeyPressed(this::txtArea_KeyPressed);
        textArea16.setOnKeyPressed(this::txtArea_KeyPressed);
        textArea09.setOnKeyPressed(this::txtArea_KeyPressed);
        textArea03.setOnKeyPressed(this::txtArea_KeyPressed);
        textArea02.setOnKeyPressed(this::txtArea_KeyPressed);
        textSeek01.setOnKeyPressed(this::txtField_KeyPressed); //Activity No Search
        textSeek02.setOnKeyPressed(this::txtField_KeyPressed); //Activity Title Search

        txtField28.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                btnCitySearch.setDisable(true);
                btnCityRemove.setDisable(true);
                textArea08.setDisable(true);

            }
        });
        dateFrom06.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                dateTo07.setDayCellFactory(DateTo);
                dateTo07.setValue(newValue.plusDays(1));

            }
        });

        dateFrom06.setOnAction(this::getDateFrom);
        dateTo07.setOnAction(this::getDateTo);
        dateFrom06.setDayCellFactory(DateFrom);
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

        Pattern trgPattern = Pattern.compile("\\p{ASCII}{0,4}");
        TextFormatter<String> trgformatter = new TextFormatter<>(change -> {
            if (!change.getControlNewText().matches(pattern.pattern())) {
                return null;
            }
            return change;
        });

        txtField12.setTextFormatter(new InputTextFormatter(trgPattern));  //nTrgtClnt
        txtField11.setTextFormatter(new InputTextFormatter(pattern));  //nRcvdBdgt
        pnEditMode = EditMode.UNKNOWN;
        initButton(pnEditMode);
    }
    private Callback<DatePicker, DateCell> DateFrom = (final DatePicker param) -> new DateCell() {
        @Override
        public void updateItem(LocalDate item, boolean empty) {
            super.updateItem(item, empty);
            LocalDate minDate = strToDate(CommonUtils.xsDateShort((Date) oApp.getServerDate())).minusDays(7);
            setDisable(empty || item.isBefore(minDate));
        }
    };
    /**
     * Callback for customizing the behavior and appearance of the date cells in
     * the 'DateTo' DatePicker. The callback disables dates before the selected
     * 'dateFrom' value.
     */
    private Callback<DatePicker, DateCell> DateTo = (final DatePicker param) -> new DateCell() {
        @Override
        public void updateItem(LocalDate item, boolean empty) {
            super.updateItem(item, empty);
            LocalDate minDate = dateFrom06.getValue();
            setDisable(empty || item.isBefore(minDate));
        }
    };

    /*Set Date Value to Master Class*/
    public void getDateFrom(ActionEvent event) {
        try {
            System.out.println(dateFrom06.getValue().toString());
            oTrans.setMaster(6, SQLUtil.toDate(dateFrom06.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
        } catch (SQLException ex) {
            Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getDateTo(ActionEvent event) {
        try {
            System.out.println(dateTo07.getValue().toString());
            oTrans.setMaster(7, SQLUtil.toDate(dateTo07.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
        } catch (SQLException ex) {
            Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
                        loadActivityField();
                        clearFields();
                        townCitydata.clear();
                        actVhclModelData.clear();
                        actMembersData.clear();

                        pnEditMode = oTrans.getEditMode();
                        try {
                            oTrans.clearActMember();
                            oTrans.clearActVehicle();
                            oTrans.clearActTown();

                        } catch (SQLException ex) {
                            Logger.getLogger(ActivityFormController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                    }
                    break;

                case "btnEdit":
                    if (oTrans.UpdateRecord()) {
                        pnEditMode = oTrans.getEditMode();
                    } else {
                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                    }
                    break;
                case "btnSave":
                    LocalDate dateFrom = dateFrom06.getValue();
                    LocalDate dateTo = dateTo07.getValue();

                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to save?") == true) {
                        if (dateFrom != null && dateTo != null && dateTo.isBefore(dateFrom)) {
                            ShowMessageFX.Warning(getStage(), "Please enter a valid date.", "Warning", null);
                            return;
                        }
                        if (dateFrom != null && dateTo != null && dateFrom.isAfter(dateTo)) {
                            ShowMessageFX.Warning(getStage(), "Please enter a valid date.", "Warning", null);
                            return;
                        }
                        if (comboBox29.getSelectionModel().isEmpty()) {
                            ShowMessageFX.Warning(getStage(), "Please choose a value for Active Type", "Warning", null);
                            return;
                        }
                        if (txtField05.getText().trim().equals("")) {
                            ShowMessageFX.Warning(getStage(), "Please enter a value for Event Source", "Warning", null);
                            txtField05.requestFocus();
                            return;
                        }
                        if (textArea02.getText().trim().equals("")) {
                            ShowMessageFX.Warning(getStage(), "Please enter a value for Activity Title.", "Warning", null);
                            textArea02.requestFocus();
                            return;
                        }
                        if (textArea03.getText().trim().equals("")) {
                            ShowMessageFX.Warning(getStage(), "Please enter a value for Activity Description", "Warning", null);
                            textArea03.requestFocus();
                            return;
                        }

                        if (txtField24.getText().trim().equals("")) {
                            ShowMessageFX.Warning(getStage(), "Please enter a value for Department in charge.", "Warning", null);
                            txtField24.requestFocus();
                            return;
                        }
                        if (txtField25.getText().trim().equals("")) {
                            ShowMessageFX.Warning(getStage(), "Please enter a value for Person in charge.", "Warning", null);
                            txtField25.requestFocus();
                            return;
                        }
                        if (txtField26.getText().trim().equals("")) {
                            ShowMessageFX.Warning(getStage(), "Please enter a value for Branch in charge", "Warning", null);
                            txtField26.requestFocus();
                            return;
                        }
                        if (Integer.parseInt(txtField12.getText()) <= 0) {
                            ShowMessageFX.Warning(getStage(), "Please enter a value for No. of Target Clients.", "Warning", null);
                            txtField12.requestFocus();
                            return;
                        }
                        if (txtField28.getText().trim().equals("")) {
                            ShowMessageFX.Warning(getStage(), "Please enter a value for Province.", "Warning", null);
                            txtField28.requestFocus();
                            return;
                        }
                        if (tblViewCity.getItems().size() == 1) {
                            if (textArea08.getText().isEmpty()) {
                                ShowMessageFX.Warning(getStage(), "Please enter a value for Street/Barangay", "Warning", null);
                                textArea08.requestFocus();
                                return;
                            }
                        }
                        if (textArea09.getText().trim().equals("")) {
                            ShowMessageFX.Warning(getStage(), "Please enter a value for Establishment.", "Warning", null);
                            textArea09.requestFocus();
                            return;
                        }
                        //Proceed Saving
                        if (oTrans.SaveRecord()) {
                            ShowMessageFX.Information(getStage(), "Transaction save successfully.", pxeModuleName, null);
                            loadActivityField();
                            pnEditMode = EditMode.READY;
                            if (oTrans.SearchRecord(oTrans.getMaster(1).toString(), true)) {
                                loadActivityField();
                                loadActivityVehicleTable();
                                loadActMembersTable();
                                loadTownTable();
                                pnEditMode = EditMode.READY;

                            } else {
                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                                actVhclModelData.clear();
                                actMembersData.clear();
                                clearFields();
                                pnEditMode = EditMode.UNKNOWN;
                            }

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
                case "btnActivityMemRemove":
                    int lnCtrMember = 0;
                    int lnRowMember = 0;

                    for (ActivityMemberTable item : tblViewActivityMembers.getItems()) {
                        if (item.getSelect().isSelected()) {
                            lnCtrMember++;
                        }
                    }
                    Integer[] fsValueMember = new Integer[lnCtrMember];
                    for (ActivityMemberTable item : tblViewActivityMembers.getItems()) {
                        if (item.getSelect().isSelected()) {
                            fsValueMember[lnRowMember] = Integer.parseInt(item.getTblindexRow());
                            lnRowMember++;
                        }

                    }
                    oTrans.removeMember(fsValueMember);
                    loadActMembersTable();
                    tblViewActivityMembers.refresh();
                    break;
                case "btnVhclModelsSearch":
                    loadActivityVehicleEntryDialog();
                    break;
                case "btnVhlModelRemove":
                    int lnCtrVchl = 0;
                    int lnRowVchl = 0;

                    for (ActivityVchlEntryTable item : tblViewVhclModels.getItems()) {
                        if (item.getSelect().isSelected()) {
                            lnCtrVchl++;
                        }
                    }
                    Integer[] fsValueVchl = new Integer[lnCtrVchl];
                    for (ActivityVchlEntryTable item : tblViewVhclModels.getItems()) {
                        if (item.getSelect().isSelected()) {
                            fsValueVchl[lnRowVchl] = Integer.parseInt(item.getTblRow());
                            lnRowVchl++;
                        }

                    }
                    oTrans.removeVehicle(fsValueVchl);
                    loadActivityVehicleTable();
                    tblViewVhclModels.refresh();
                    break;
                case "btnCitySearch":
                    loadTownDialog();
                    break;
                case "btnCityRemove":
                    int lnCtrTown = 0;
                    int lnRowTown = 0;

                    for (ActivityTownEntryTableList item : tblViewCity.getItems()) {
                        if (item.getSelect().isSelected()) {
                            lnCtrTown++;
                        }
                    }
                    Integer[] fsValue = new Integer[lnCtrTown];
                    for (ActivityTownEntryTableList item : tblViewCity.getItems()) {
                        if (item.getSelect().isSelected()) {
                            fsValue[lnRowTown] = Integer.parseInt(item.getTblRow());
                            lnRowTown++;
                        }

                    }
                    oTrans.removeTown(fsValue);
                    loadTownTable();
                    tblViewCity.refresh();

                    break;
                case "btnCancel":
                    if (ShowMessageFX.OkayCancel(getStage(), "Are you sure you want to cancel?", pxeModuleName, null) == true) {
                        clearFields();
                        actVhclModelData.clear();
                        actMembersData.clear();
                        townCitydata.clear();
                        pnEditMode = EditMode.UNKNOWN;
                    }
                    break;
                case "btnBrowse":
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (ShowMessageFX.OkayCancel(null, "Confirmation", "You have unsaved data. Are you sure you want to browse a new record?")) {
                        } else {
                            return;
                        }
                    }
                    try {
                        if (oTrans.SearchRecord(textSeek01.getText(), false)) {
                            loadActivityField();
                            loadActivityVehicleTable();
                            loadActMembersTable();
                            loadTownTable();
                            pnEditMode = EditMode.READY;

                        } else {
                            ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                            actVhclModelData.clear();
                            actMembersData.clear();
                            clearFields();
                            pnEditMode = EditMode.UNKNOWN;
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(ActivityFormController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    break;
                case "btnActivityHistory":
                    break;
                case "btnActCancel":
                    String fsValueCancelAct = oTrans.getMaster(1).toString();
                    if (cancelform.loadCancelWindow(oApp, fsValueCancelAct, fsValueCancelAct, "ACT")) {
                        if (oTrans.CancelActivity(fsValueCancelAct)) {
                            loadActivityField();
                            pnEditMode = EditMode.READY;
                            if (oTrans.SearchRecord(fsValueCancelAct, true)) {
                                loadActivityField();
                                loadActivityVehicleTable();
                                loadActMembersTable();
                                loadTownTable();
                                pnEditMode = EditMode.READY;

                            } else {
                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                                actVhclModelData.clear();
                                actMembersData.clear();
                                clearFields();
                                pnEditMode = EditMode.UNKNOWN;

                            }
                        } else {
                            ShowMessageFX.Information(getStage(), "Failed to cancel activity.", pxeModuleName, null);
                            return;
                        }
                    } else {
                        return;
                    }

                    break;
                case "btnPrint":
                    String srowdata = oTrans.getMaster(1).toString();
                    loadActivityPrint(srowdata);
                    break;
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
            Logger.getLogger(ActivityFormController.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (SQLException ex) {
            Logger.getLogger(ActivityFormController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadActivityPrint(String sTransno) throws SQLException {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("ActivityPrint.fxml"));

            ActivityPrintController loControl = new ActivityPrintController();
            loControl.setGRider(oApp);
            loControl.setTransNox(sTransno);

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
        int lnIndex = Integer.parseInt(((TextField) event.getSource()).getId().substring(8, 10));
        String txtFieldID = ((TextField) event.getSource()).getId();
        String lsValue = txtField.getText();

        try {
            switch (event.getCode()) {
                case F3:
                case TAB:
                case ENTER:
                    switch (txtFieldID) {
                        case "textSeek01":  //Search by Activity No
                            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                                if (ShowMessageFX.OkayCancel(null, "Confirmation", "You have unsaved data. Are you sure you want to browse a new record?") == true) {
                                } else {
                                    return;
                                }
                            }
                            if (oTrans.SearchRecord(textSeek01.getText(), false)) {
                                loadActivityField();
                                loadActivityVehicleTable();
                                loadActMembersTable();
                                loadTownTable();
                                pnEditMode = EditMode.READY;
                            } else {
                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                                textSeek02.clear();
                                actVhclModelData.clear();
                                actMembersData.clear();
                                clearFields();
                                pnEditMode = EditMode.UNKNOWN;
                            }
                            initButton(pnEditMode);
                            break;
                        case "textSeek02":  //Search by Activity Name
                            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                                if (ShowMessageFX.OkayCancel(null, "Confirmation", "You have unsaved data. Are you sure you want to browse a new record?") == true) {
                                } else {
                                    return;
                                }
                            }
                            if (oTrans.SearchRecord(textSeek02.getText(), false)) {
                                loadActivityField();
                                loadActivityVehicleTable();
                                loadActMembersTable();
                                loadTownTable();
                                pnEditMode = oTrans.getEditMode();
                            } else {
                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                                textSeek01.clear();
                                actVhclModelData.clear();
                                actMembersData.clear();
                                clearFields();
                                pnEditMode = EditMode.UNKNOWN;
                            }
                            initButton(pnEditMode);
                            break;
                        case "txtField05":
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
                        case "txtField24":
                            if (oTrans.searchDepartment(txtField.getText())) {
                                loadActivityField();
                            } else {
                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                            }
                            break;
                        case "txtField25":
                            if (oTrans.searchEmployee(txtField.getText())) {
                                loadActivityField();
                            } else {
                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                            }
                            break;
                        case "txtField26":
                            if (oTrans.searchBranch(txtField.getText())) {
                                loadActivityField();
                            } else {
                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                            }
                            break;
                        case "txtField28":
                            if (oTrans.searchProvince(txtField.getText())) {
                                btnCitySearch.setDisable(false);
                                btnCityRemove.setDisable(false);
                                tblViewCity.setDisable(false);

                                int lnCtr = 1;
                                int lnRow = 0;
                                Integer[] fsValue = new Integer[oTrans.getActTownCount()];
                                while (lnCtr <= oTrans.getActTownCount()) {
                                    fsValue[lnRow] = lnCtr;
                                    lnRow++;
                                    lnCtr++;
                                }
                                oTrans.removeTown(fsValue);
                                loadActivityField();
                                loadTownTable();
                                pnEditMode = oTrans.getEditMode();
                            } else {
                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                            }

                            break;
                    }
                    break;

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
    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {
        try {
            TextField txtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
            int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
            String lsValue = txtField.getText();

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
                    case 28: //sProvName
                        oTrans.setMaster(lnIndex, lsValue); // Handle Encoded Value
                        break;
                    case 12: // nTrgtClnt
                        int targetClients = Integer.parseInt(lsValue);
                        oTrans.setMaster(lnIndex, targetClients);
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
                    case 15:       //sLogRemrk
                    case 3:        //sActDesc
                    case 16:       //sRemarksx
                    case 9:        //sCompnynx
                        oTrans.setMaster(lnIndex, lsValue);
                        break;
                    case 8: //sAddressx
                        oTrans.setActTown(1, 3, lsValue);
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

    private void loadActivityField() {
        try {

            txtField01.setText((String) oTrans.getMaster(1)); // sActvtyID
            dateFrom06.setValue(strToDate(CommonUtils.xsDateShort((Date) oTrans.getMaster(6))));
            dateTo07.setValue(strToDate(CommonUtils.xsDateShort((Date) oTrans.getMaster(7))));
            String selectedItem = oTrans.getMaster(29).toString();//sEventTyp
            switch (selectedItem) {
                case "sal":
                    selectedItem = "SALES CALL";
                    break;
                case "eve":
                    selectedItem = "EVENT";
                    break;
                case "pro":
                    selectedItem = "PROMO";
                    break;
                default:
                    break;
            }
            if (!selectedItem.isEmpty()) {
                comboBox29.getSelectionModel().select(selectedItem);
            }
            if (((String) oTrans.getMaster(17)).equals("2")) {
                lblCancelStatus.setText("Cancelled");
            } else {
                lblCancelStatus.setText("");
            }

            if (!((String) oTrans.getMaster(20)).isEmpty()) {
                lblApprovedBy.setText("Approved");
                lblApprovedDate.setText(CommonUtils.xsDateShort((Date) oTrans.getMaster(21)));//dApproved
            } else {
                lblApprovedBy.setText("");
                lblApprovedDate.setText("");//dApproved
            }
            txtField05.setText((String) oTrans.getMaster(5)); // sActSrcex
            textArea02.setText((String) oTrans.getMaster(2)); // sActTitle
            textArea03.setText((String) oTrans.getMaster(3)); // sActDescx
            textArea15.setText((String) oTrans.getMaster(15)); // sLogRemrk
            textArea16.setText((String) oTrans.getMaster(16)); // sRemarksx
            txtField24.setText((String) oTrans.getMaster(24)); // sDeptName
            txtField25.setText((String) oTrans.getMaster(25)); // sCompnyNm
            txtField26.setText((String) oTrans.getMaster(26)); // sBranchNm
            txtField12.setText(String.valueOf(oTrans.getMaster(12))); // nTrgtClnt
            txtField11.setText(String.valueOf(oTrans.getMaster(11))); // nRcvdBdgt
            txtField28.setText((String) oTrans.getMaster(28)); // sProvName
            textArea09.setText((String) oTrans.getMaster(9)); // sCompnynx
            if (oTrans.getActTownCount() == 1) {
                textArea08.setText(oTrans.getActTown(3).toString());
            }
        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }
    }

//Act Type Add Source Dialog
    private void loadActTypeAddSourceDialog() throws IOException {
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
            loadActMembersTable();

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
            loadActivityVehicleTable();
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
            fxmlLoader.setLocation(getClass().getResource("ActivityTownCityMainEntryDialog.fxml"));

            ActivityTownCityMainEntryDialogController loControl = new ActivityTownCityMainEntryDialogController();
            loControl.setGRider(oApp);
            loControl.setObject(oTrans);
            try {
                loControl.setProv((String) oTrans.getMaster(27));

            } catch (SQLException ex) {
                Logger.getLogger(ActivityFormController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
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
            loadTownTable();
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

    private void loadTownTable() {
        try {
            /*Populate table*/
            townCitydata.clear();
            for (int lnCtr = 1; lnCtr <= oTrans.getActTownCount(); lnCtr++) {
                townCitydata.add(new ActivityTownEntryTableList(
                        String.valueOf(lnCtr), //ROW
                        oTrans.getActTown(lnCtr, "sTownIDxx").toString(),
                        oTrans.getActTown(lnCtr, "sTownName").toString()));

            }
            tblViewCity.setItems(townCitydata);
            initTownTable();

        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }
    }

    private void initTownTable() {
        tblRowCity.setCellValueFactory(new PropertyValueFactory<>("tblRow"));
        tblselectCity.setCellValueFactory(new PropertyValueFactory<>("select"));
        tblViewCity.getItems().forEach(item -> {
            CheckBox selectCheckBox = item.getSelect();
            selectCheckBox.setOnAction(event -> {
                if (tblViewCity.getItems().stream().allMatch(tableItem -> tableItem.getSelect().isSelected())) {
                    selectAllCity.setSelected(true);
                } else {
                    selectAllCity.setSelected(false);
                }
            });
        });
        selectAllCity.setOnAction(event -> {
            boolean newValue = selectAllCity.isSelected();
            if (!tblViewCity.getItems().isEmpty()) {
                tblViewCity.getItems().forEach(item -> item.getSelect().setSelected(newValue));
            }
        }
        );
        tblTownCity.setCellValueFactory(new PropertyValueFactory<>("tblCity"));

    }

    //Activity Member
    private void loadActMembersTable() {

        try {
            /*Populate table*/
            actMembersData.clear();
            for (int lnCtr = 1; lnCtr <= oTrans.getActMemberCount(); lnCtr++) {
                System.out.println(oTrans.getActMember(lnCtr, "cOriginal"));
                if (oTrans.getActMember(lnCtr, "cOriginal").equals("1")) {
                    actMembersData.add(new ActivityMemberTable(
                            String.valueOf(lnCtr), //ROW
                            oTrans.getActMember(lnCtr, "sDeptName").toString(),
                            "",
                            oTrans.getActMember(lnCtr, "sCompnyNm").toString(), // Fifth column (Department)
                            oTrans.getActMember(lnCtr, "sEmployID").toString()
                    ));
                }
            }
            tblViewActivityMembers.setItems(actMembersData);
            initActMembersTable();
        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }

    }

    private void initActMembersTable() {
        tblActvtyMembersRow.setCellValueFactory(new PropertyValueFactory<>("tblindexRow"));
        tblselected.setCellValueFactory(new PropertyValueFactory<>("select"));
        tblViewActivityMembers.getItems().forEach(item -> {
            CheckBox selectCheckBox = item.getSelect();
            selectCheckBox.setOnAction(event -> {
                if (tblViewActivityMembers.getItems().stream().allMatch(tableItem -> tableItem.getSelect().isSelected())) {
                    selectAllCheckBoxEmployee.setSelected(true);
                } else {
                    selectAllCheckBoxEmployee.setSelected(false);
                }
            });
        });
        selectAllCheckBoxEmployee.setOnAction(event -> {
            boolean newValue = selectAllCheckBoxEmployee.isSelected();
            if (!tblViewActivityMembers.getItems().isEmpty()) {
                tblViewActivityMembers.getItems().forEach(item -> item.getSelect().setSelected(newValue));
            }
        });
        tblindex24.setCellValueFactory(new PropertyValueFactory<>("tblindexMem24"));
        tblindex25.setCellValueFactory(new PropertyValueFactory<>("tblindexMem25"));
    }

    private void loadActivityVehicleTable() {
        try {
            /*Populate table*/
            actVhclModelData.clear();
            for (int lnCtr = 1; lnCtr <= oTrans.getActVehicleCount(); lnCtr++) {

                actVhclModelData.add(new ActivityVchlEntryTable(
                        String.valueOf(lnCtr), //ROW
                        oTrans.getActVehicle(lnCtr, "sSerialID").toString(),
                        oTrans.getActVehicle(lnCtr, "sDescript").toString(),
                        oTrans.getActVehicle(lnCtr, "sCSNoxxxx").toString()
                ));
            }
            tblViewVhclModels.setItems(actVhclModelData);
            initActivityVehicleTable();
        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }

    }

    private void initActivityVehicleTable() {
        tblindexVchlRow.setCellValueFactory(new PropertyValueFactory<>("tblRow"));  //Row
        tblVchlSelect.setCellValueFactory(new PropertyValueFactory<>("select"));
        tblViewVhclModels.getItems().forEach(item -> {
            CheckBox selectCheckBox = item.getSelect();
            selectCheckBox.setOnAction(event -> {
                if (tblViewVhclModels.getItems().stream().allMatch(tableItem -> tableItem.getSelect().isSelected())) {
                    selectAllVchlMode.setSelected(true);
                } else {
                    selectAllVchlMode.setSelected(false);
                }
            });
        });
        selectAllVchlMode.setOnAction(event -> {
            boolean newValue = selectAllVchlMode.isSelected();
            if (!tblViewVhclModels.getItems().isEmpty()) {
                tblViewVhclModels.getItems().forEach(item -> item.getSelect().setSelected(newValue));
            }
        }
        );
        tblVchlDescription.setCellValueFactory(new PropertyValueFactory<>("tblDescription04"));
    }

    private void initButton(int fnValue) {
        pnRow = 0;

        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

        // Fields
        dateFrom06.setDisable(!lbShow);
        dateTo07.setDisable(!lbShow);
        comboBox29.setDisable(!lbShow);
        txtField05.setDisable(!lbShow);
        textArea02.setDisable(!lbShow);
        textArea03.setDisable(!lbShow);
        textArea15.setDisable(!lbShow);
        textArea16.setDisable(!lbShow);
        txtField24.setDisable(!lbShow);
        txtField25.setDisable(!lbShow);
        txtField26.setDisable(!lbShow);
        txtField12.setDisable(!lbShow);
        txtField11.setDisable(!lbShow);
        txtField28.setDisable(!lbShow);
        textArea09.setDisable(!lbShow);
        // Button
        btnCitySearch.setDisable(!(lbShow && !txtField28.getText().isEmpty()));
        btnCityRemove.setDisable(!(lbShow && !txtField28.getText().isEmpty()));

        if (tblViewCity != null && tblViewCity.getItems() != null) {
            boolean tblViewCityEmpty = tblViewCity.getItems().isEmpty() || tblViewCity.getItems().size() >= 2;
            if (tblViewCityEmpty) {
                textArea08.setDisable(!lbShow);
                textArea08.setText("");
            } else {
                textArea08.setDisable(false);

            }
            textArea08.setDisable(!(lbShow && !tblViewCity.getItems().isEmpty()) || tblViewCity.getItems().size() >= 2);
        }
        btnActCancel.setVisible(false);
        btnActCancel.setManaged(false);
        btnActivityMembersSearch.setDisable(!lbShow);
        btnActivityMemRemove.setDisable(!lbShow);
        btnVhclModelsSearch.setDisable(!lbShow);
        btnVhlModelRemove.setDisable(!lbShow);
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        btnEdit.setVisible(false);
        btnEdit.setManaged(false);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);
        btnActivityHistory.setVisible(false);
        btnActivityHistory.setManaged(false);
        btnPrint.setVisible(false);
        btnPrint.setManaged(false);
        btnCancel.setVisible(lbShow);
        btnCancel.setManaged(lbShow);
        tblselectCity.setVisible(lbShow);
        tblselected.setVisible(lbShow);
        tblVchlSelect.setVisible(lbShow);

        if (fnValue == EditMode.READY) {

            if (lblCancelStatus.getText().equals("Cancelled")) {
                btnActCancel.setVisible(false);
                btnActCancel.setManaged(false);
                btnEdit.setVisible(false);
                btnEdit.setManaged(false);
                btnPrint.setVisible(false);
                btnPrint.setManaged(false);
                btnActivityHistory.setVisible(true);
                btnActivityHistory.setManaged(true);

            } else {
                btnActCancel.setVisible(true);
                btnActCancel.setManaged(true);
                btnEdit.setVisible(true);
                btnEdit.setManaged(true);
                btnPrint.setVisible(true);
                btnPrint.setManaged(true);
                btnActivityHistory.setVisible(true);
                btnActivityHistory.setManaged(true);
            }
        }

    }

    public void clearFields() {
        pnRow = 0;
        townCitydata.clear();
        txtField01.setText("");//sActvtyID
        dateFrom06.setValue(strToDate(CommonUtils.xsDateShort((Date) oApp.getServerDate())));
        dateTo07.setValue(strToDate(CommonUtils.xsDateShort((Date) oApp.getServerDate())));
        lblCancelStatus.setText("");
        lblApprovedDate.setText(null);
        lblApprovedBy.setText("");
        tblViewCity.setItems(null);
        comboBox29.setValue(null); //sEventTyp
        txtField05.setText(""); //sActTypDs
        textArea02.setText(""); //sActTitle
        textArea03.setText(""); //sActDescx
        textArea15.setText("");//sLogRemrk
        textArea16.setText("");//sRemarksx
        txtField24.setText("");//sDeptName
        txtField25.setText(""); //sCompnyNm
        txtField26.setText(""); //sBranchNm
        txtField32.setText(""); //Branch
        txtField12.setText(""); //nTrgtClnt
        txtField11.setText("0.0");  //nRcvdBdg
        txtField28.setText(""); //sProvName
        textArea08.setText(""); //Street
        textArea09.setText(""); //sCompnynx
    }

}
