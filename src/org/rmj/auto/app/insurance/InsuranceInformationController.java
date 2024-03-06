/*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.auto.app.insurance;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.auto.app.views.InputTextFormatter;
import org.rmj.auto.app.views.ScreenInterface;
import org.rmj.auto.app.views.TextFieldAnimationUtil;
import org.rmj.auto.app.views.unloadForm;
import org.rmj.auto.clients.base.InsuranceInformation;

/**
 * FXML Controller class
 *
 * @author User
 */
public class InsuranceInformationController implements Initializable, ScreenInterface {

    private GRider oApp;
    private InsuranceInformation oTrans;
    unloadForm unload = new unloadForm(); //Object for closing form
    private final String pxeModuleName = "Insurance"; //Form Title
    private MasterCallback oListener;
    TextFieldAnimationUtil txtFieldAnimation = new TextFieldAnimationUtil();
    private int pnEditMode;//Modifying fields
    private int pnRow = -1;
    private int oldPnRow = -1;
    private int lnCtr = 0;
    private int pagecounter;
    private String oldTransNo = "";
    private String TransNo = "";
    private final ObservableList<InsuranceTableList> insuranceData = FXCollections.observableArrayList();
    private FilteredList<InsuranceTableList> filteredData;
    private static final int ROWS_PER_PAGE = 50;
    ObservableList<String> cInsurType = FXCollections.observableArrayList("NON IN HOUSE", "IN HOUSE (DIRECT)", "SUB-IN HOUSE (ACCREDITED)");
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Pagination pagination;
    @FXML
    private TableColumn<InsuranceTableList, String> tblindexRow;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnClose;
    @FXML
    private TextField txtField07;
    @FXML
    private TextField txtField04;
    @FXML
    private TextField txtField09;
    @FXML
    private TableView<InsuranceTableList> tblViewInsurance;
    @FXML
    private TextField textSeek03;
    @FXML
    private TableColumn<InsuranceTableList, String> tblindex03;
    @FXML
    private TableColumn<InsuranceTableList, String> tblindex04;
    @FXML
    private TableColumn<InsuranceTableList, String> tblindex18;
    @FXML
    private TextField txtField03;
    @FXML
    private TextField txtField19;
    @FXML
    private TextField txtField06;
    @FXML
    private TextField txtField10;
    @FXML
    private TextField txtField11;
    @FXML
    private ComboBox<String> comboBox02;
    @FXML
    private TextField txtField17;

    private Stage getStage() {
        return (Stage) txtField03.getScene().getWindow();
    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        oListener = (int fnIndex, Object foValue) -> {
            System.out.println("Set Class Value " + fnIndex + "-->" + foValue);
        };

        oTrans = new InsuranceInformation(oApp, oApp.getBranchCode(), false);
        oTrans.setCallback(oListener);
        oTrans.setWithUI(true);
        //initialize set capslock
        initSetCapsLockField();
        /*Set Focus to set Value to Class*/
        initTxtFieldFocus();
        //initilize text keypressed
        initTxtFieldKeyPressed();
        //add shakeanimation
        initAddRequiredField();
        //Button Click Event
        initButtonClick();

        initSetComboBoxtoInsuranceMaster();

        tblViewInsurance.setOnMouseClicked(this::tblInsurance_Clicked);

        /*Clear Fields*/
        clearFields();

        comboBox02.setItems(cInsurType);
        Pattern numberOnlyPattern = Pattern.compile("[0-9,+-]*");
        txtField10.setTextFormatter(new InputTextFormatter(numberOnlyPattern)); //sTelNoxxx
        txtField11.setTextFormatter(new InputTextFormatter(numberOnlyPattern)); //sFaxNoxxx

        txtField17.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                txtField19.clear();
                txtField19.setDisable(true);
                txtField09.clear();
                txtField09.setDisable(true);
            }
        });
        loadInsuranceTable();
        pagination.setPageFactory(this::createPage);
        pnEditMode = EditMode.UNKNOWN;
        initButton(pnEditMode);

        Platform.runLater(() -> {
            if (oTrans.loadState()) {
                pnEditMode = oTrans.getEditMode();
                loadInsuranceField();
                initButton(pnEditMode);
            } else {
                if (oTrans.getMessage().isEmpty()) {
                } else {
                    ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                }
            }
        });
    }

    private void loadInsuranceField() {
        try {

            String selectedItem02 = oTrans.getMaster("sCompnyTp").toString();
            switch (selectedItem02) {
                case "0":
                    selectedItem02 = "NON IN HOUSE";
                    break;
                case "1":
                    selectedItem02 = "IN HOUSE (DIRECT)";
                    break;
                case "2":
                    selectedItem02 = "SUB-IN HOUSE (ACCREDITED)";
                    break;

            }
            comboBox02.setValue(selectedItem02);
            txtField03.setText((String) oTrans.getMaster("sInsurNme"));
            txtField04.setText((String) oTrans.getMaster("sBranchxx"));
            txtField07.setText((String) oTrans.getMaster("sAddressx"));
            txtField17.setText((String) oTrans.getMaster("sProvName"));
            txtField19.setText((String) oTrans.getMaster("sTownName"));
            txtField06.setText((String) oTrans.getMaster("sContactP"));
            txtField09.setText((String) oTrans.getMaster("sZippCode"));
            txtField11.setText((String) oTrans.getMaster("sFaxNoxxx"));
            txtField10.setText((String) oTrans.getMaster("sTelNoxxx"));
        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }
    }

    private void initSetCapsLockField() {
        setCapsLockBehavior(txtField03);
        setCapsLockBehavior(txtField04);
        setCapsLockBehavior(txtField07);
        setCapsLockBehavior(txtField17);
        setCapsLockBehavior(txtField19);
        setCapsLockBehavior(txtField04);
        setCapsLockBehavior(txtField06);
        setCapsLockBehavior(txtField09);
        setCapsLockBehavior(txtField11);
        setCapsLockBehavior(txtField10);
    }

    private static void setCapsLockBehavior(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (textField.getText() != null) {
                textField.setText(newValue.toUpperCase());
            }
        });
    }

    private void initTxtFieldFocus() {

        txtField03.focusedProperty().addListener(txtField_Focus);
        txtField04.focusedProperty().addListener(txtField_Focus);
        txtField07.focusedProperty().addListener(txtField_Focus);
        txtField17.focusedProperty().addListener(txtField_Focus);
        txtField19.focusedProperty().addListener(txtField_Focus);
        txtField06.focusedProperty().addListener(txtField_Focus);
        txtField09.focusedProperty().addListener(txtField_Focus);
        txtField11.focusedProperty().addListener(txtField_Focus);
        txtField10.focusedProperty().addListener(txtField_Focus);
    }
    /* Set TextField Value to Master Class */
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
                    case 3:
                    case 4:
                    case 7:
                    case 17:
                    case 19:
                    case 9:
                    case 6:
                    case 10:
                    case 11:
                        oTrans.setMaster(lnIndex, lsValue);
                        break;
                }
            } else {
                txtField.selectAll();
            }
        } catch (SQLException ex) {
            Logger.getLogger(InsuranceInformationController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    };

    private void initButtonClick() {
        btnAdd.setOnAction(this::cmdButton_Click);
        btnEdit.setOnAction(this::cmdButton_Click);
        btnSave.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click);
        btnCancel.setOnAction(this::cmdButton_Click);
    }

    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
                if (oTrans.NewRecord()) {
                    clearFields();
                    loadInsuranceField();
                    pnEditMode = oTrans.getEditMode();
                } else {
                    ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                }
                break;
            case "btnEdit":
                if (oTrans.UpdateRecord()) {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (txtField17.getText().isEmpty()) {
                            ShowMessageFX.Warning(getStage(), "Province is empty.", "Warning", null);
                            break;
                        }
                    }
                    pnEditMode = oTrans.getEditMode();
                } else {
                    ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                }
                break;
            case "btnCancel":
                if (ShowMessageFX.OkayCancel(getStage(), "Are you sure you want to cancel?", pxeModuleName, null) == true) {
                    clearFields();
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;

            case "btnSave":
                //Validate before saving
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to save?") == true) {
                    if (comboBox02.getSelectionModel().isEmpty()) {
                        ShowMessageFX.Warning(getStage(), "Please choose a value for Company Type", "Warning", null);
                        return;
                    }
                    if (txtField03.getText().trim().equals("")) {
                        ShowMessageFX.Warning(getStage(), "Please enter a value for Insurance Name.", "Warning", null);
                        txtField03.requestFocus();
                        return;
                    }
                    if (txtField04.getText().trim().equals("")) {
                        ShowMessageFX.Warning(getStage(), "Please enter a valid value for Branch", "Warning", null);
                        txtField04.requestFocus();
                        return;
                    }
                    if (txtField07.getText().trim().equals("")) {
                        ShowMessageFX.Warning(getStage(), "Please enter a value for HouseNo/Street/Barangay.", "Warning", null);
                        txtField07.requestFocus();
                        return;
                    }
                    if (txtField17.getText().trim().equals("")) {
                        ShowMessageFX.Warning(getStage(), "Please enter a valid value for Province.", "Warning", null);
                        txtField17.requestFocus();
                        return;
                    }
                    if (txtField19.getText().trim().equals("")) {
                        ShowMessageFX.Warning(getStage(), "Please enter a value for Municipality.", "Warning", null);
                        txtField19.requestFocus();
                        return;
                    }
                    if (txtField09.getText().trim().equals("")) {
                        ShowMessageFX.Warning(getStage(), "Please enter a value for Zipcode.", "Warning", null);
                        txtField09.requestFocus();
                        return;
                    }
                    if (txtField06.getText().trim().equals("")) {
                        ShowMessageFX.Warning(getStage(), "Please enter a value for Contact Person", "Warning", null);
                        txtField06.requestFocus();
                        return;
                    }
                    if (txtField10.getText().trim().equals("")) {
                        ShowMessageFX.Warning(getStage(), "Please enter a value for Telephone No.", "Warning", null);
                        txtField10.requestFocus();
                        return;
                    }
                    //Proceed Saving
                    if (oTrans.SaveRecord()) {
                        ShowMessageFX.Information(getStage(), "Transaction save successfully.", pxeModuleName, null);
                        loadInsuranceTable();
                        try {
                            getSelectedItem((String) oTrans.getMaster(1));
                        } catch (SQLException ex) {
                            Logger.getLogger(InsuranceInformationController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        pnEditMode = oTrans.getEditMode();
                    } else {
                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", "Error while saving Vehicle Description");
                    }
                }
                break;
            case "btnClose":
                if (ShowMessageFX.OkayCancel(null, "Close Tab", "Are you sure you want to close this Tab?")) {
                    if (unload != null) {
                        unload.unloadForm(AnchorMain, oApp, pxeModuleName);
                    } else {
                        ShowMessageFX.Warning(null, "Warning", "Please notify the system administrator to configure the null value at the close button.");
                    }
                }
        }
        initButton(pnEditMode);
    }

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, insuranceData.size());
        if (insuranceData.size() > 0) {
            tblViewInsurance.setItems(FXCollections.observableArrayList(insuranceData.subList(fromIndex, toIndex)));
        }
        return tblViewInsurance;
    }
    //storing values on bankentrydata

    private void loadInsuranceTable() {
        try {
            /*Populate table*/
            insuranceData.clear();
            if (oTrans.loadList()) {
                for (int lnCtr = 1; lnCtr <= oTrans.getDetailCount(); lnCtr++) {
                    System.out.println("data count: " + oTrans.getDetailCount());
                    insuranceData.add(new InsuranceTableList(
                            String.valueOf(lnCtr), //ROW
                            oTrans.getDetail(lnCtr, "sInsurIDx").toString(),
                            oTrans.getDetail(lnCtr, "sInsurNme").toString(),
                            oTrans.getDetail(lnCtr, "sBranchxx").toString(),
                            oTrans.getDetail(lnCtr, "sTownProv").toString()
                    ));
                }
                initInsuranceTable();
            }
            loadTab();

        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }
    }

    /*populate Table*/
    private void initInsuranceTable() {

        tblindexRow.setCellValueFactory(new PropertyValueFactory<>("tblindexRow"));  //Row
        tblindex03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
        tblindex04.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
        tblindex18.setCellValueFactory(new PropertyValueFactory<>("tblindex18"));

        filteredData = new FilteredList<>(insuranceData, b -> true);
        autoSearch(textSeek03);
        SortedList<InsuranceTableList> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblViewInsurance.comparatorProperty());
        tblViewInsurance.setItems(sortedData);

        tblViewInsurance.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewInsurance.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
            header.setDisable(true);
        });

    }

    private void autoSearch(TextField txtField) {
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));

        txtField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(clients -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare order no. and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();
                switch (lnIndex) {
                    case 2:
                        if (lnIndex == 2) {
                            return (clients.getTblindex03().toLowerCase().contains(lowerCaseFilter)); // Does not match.
                        } else {
                            return (clients.getTblindex03().toLowerCase().contains(lowerCaseFilter)); // Does not match.
                        }
                    default:
                        return true;
                }
            });

            changeTableView(0, ROWS_PER_PAGE);
        });
        loadTab();
    }

    private void loadTab() {
        int totalPage = (int) (Math.ceil(insuranceData.size() * 1.0 / ROWS_PER_PAGE));
        pagination.setPageCount(totalPage);
        pagination.setCurrentPageIndex(0);
        changeTableView(0, ROWS_PER_PAGE);
        pagination.currentPageIndexProperty().addListener(
                (observable, oldValue, newValue) -> changeTableView(newValue.intValue(), ROWS_PER_PAGE));

    }

    private void changeTableView(int index, int limit) {
        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, insuranceData.size());

        int minIndex = Math.min(toIndex, filteredData.size());
        SortedList<InsuranceTableList> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(tblViewInsurance.comparatorProperty());
        tblViewInsurance.setItems(sortedData);
    }

    private void tblInsurance_Clicked(MouseEvent event) {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            if (ShowMessageFX.OkayCancel(null, pxeModuleName, "You have unsaved data, are you sure you want to continue?") == true) {
            } else {
                return;
            }
        }
        pnRow = tblViewInsurance.getSelectionModel().getSelectedIndex();
        pagecounter = pnRow + pagination.getCurrentPageIndex() * ROWS_PER_PAGE;
        if (pagecounter >= 0) {
            if (event.getClickCount() > 0) {
                getSelectedItem(filteredData.get(pagecounter).getTblindex01()); //Populate field based on selected Item

                tblViewInsurance.setOnKeyReleased((KeyEvent t) -> {
                    KeyCode key = t.getCode();
                    switch (key) {
                        case DOWN:
                            pnRow = tblViewInsurance.getSelectionModel().getSelectedIndex();
                            pagecounter = pnRow + pagination.getCurrentPageIndex() * ROWS_PER_PAGE;
                            if (pagecounter == tblViewInsurance.getItems().size()) {
                                pagecounter = tblViewInsurance.getItems().size();
                                getSelectedItem(filteredData.get(pagecounter).getTblindex01());
                            } else {
                                int y = 1;
                                pnRow = pnRow + y;
                                getSelectedItem(filteredData.get(pagecounter).getTblindex01());
                            }
                            break;
                        case UP:
                            pnRow = tblViewInsurance.getSelectionModel().getSelectedIndex();
                            pagecounter = pnRow + pagination.getCurrentPageIndex() * ROWS_PER_PAGE;
                            getSelectedItem(filteredData.get(pagecounter).getTblindex01());
                            break;
                        default:
                            return;
                    }
                });
            }
            pnEditMode = EditMode.READY;
            initButton(pnEditMode);
        }
    }

    /*Populate Text Field Based on selected address in table*/
    private void getSelectedItem(String TransNo) {
        oldTransNo = TransNo;
        if (oTrans.OpenRecord(TransNo)) {
            clearFields();
            loadInsuranceField();
        }
        oldPnRow = pagecounter;

    }

    private void initTxtFieldKeyPressed() {
        txtField03.setOnKeyPressed(this::txtField_KeyPressed);
        txtField04.setOnKeyPressed(this::txtField_KeyPressed);
        txtField07.setOnKeyPressed(this::txtField_KeyPressed);
        txtField17.setOnKeyPressed(this::txtField_KeyPressed);
        txtField19.setOnKeyPressed(this::txtField_KeyPressed);
        txtField06.setOnKeyPressed(this::txtField_KeyPressed);
        txtField09.setOnKeyPressed(this::txtField_KeyPressed);
        txtField11.setOnKeyPressed(this::txtField_KeyPressed);
        txtField10.setOnKeyPressed(this::txtField_KeyPressed);
    }

    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));

        try {
            if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
                switch (lnIndex) {
                    case 19: // sTownNamexx
                        if (oTrans.searchTown(txtField.getText(), false)) {
                            loadInsuranceField();
                        } else {
                            ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                            txtField17.requestFocus();
                            return;
                        }
                        break;
                    case 17: // sProvName
                        if (oTrans.searchProvince(txtField.getText(), false)) {
                            txtField19.setDisable(false);
                            txtField09.setDisable(false);
                            loadInsuranceField();
                            txtField19.clear();
                            txtField09.clear();
                        } else {
                            txtField17.clear();
                            ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                            txtField17.requestFocus();
                            return;
                        }
                        break;
                }
                event.consume();
                CommonUtils.SetNextFocus((TextField) event.getSource());
            } else if (event.getCode() == KeyCode.UP) {
                event.consume();
                CommonUtils.SetPreviousFocus((TextField) event.getSource());
            }
        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }
    }

    private void initSetComboBoxtoInsuranceMaster() {
        handleComboBoxSelectionInsuranceMaster(comboBox02, 2);

    }

    private void handleComboBoxSelectionInsuranceMaster(ComboBox<String> comboBox, int fieldNumber) {
        if (pnEditMode == EditMode.UPDATE || pnEditMode == EditMode.ADDNEW) {
            comboBox.setOnAction(e -> {
                try {
                    int selectedType = comboBox.getSelectionModel().getSelectedIndex(); // Retrieve the selected type
                    if (selectedType >= 0) {
                        oTrans.setMaster(fieldNumber, String.valueOf(selectedType));
                    }
                    initButton(pnEditMode);
                } catch (SQLException ex) {
                    Logger.getLogger(InsuranceInformationController.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
            );
        }
    }

    private void initButton(int fnValue) {
        pnRow = 0;
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

        comboBox02.setDisable(!lbShow);
        txtField03.setDisable(!lbShow);
        txtField04.setDisable(!lbShow);
        txtField07.setDisable(!lbShow);
        txtField19.setDisable(!(lbShow && !txtField17.getText().isEmpty()));
        txtField17.setDisable(!lbShow);
        txtField09.setDisable(!(lbShow && !txtField17.getText().isEmpty()));
        txtField06.setDisable(!lbShow);
        txtField10.setDisable(!lbShow);
        txtField11.setDisable(!lbShow);

        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);

        btnCancel.setVisible(lbShow);
        btnCancel.setManaged(lbShow);
        //if lbShow = false hide btn
        btnEdit.setVisible(false);
        btnEdit.setManaged(false);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);

        if (fnValue == EditMode.READY) { //show edit if user clicked save / browse
            btnEdit.setVisible(true);
            btnEdit.setManaged(true);

        }
    }

    private void initAddRequiredField() {
        txtFieldAnimation.addRequiredFieldListener(txtField03);
        txtFieldAnimation.addRequiredFieldListener(txtField04);
        txtFieldAnimation.addRequiredFieldListener(txtField07);
        txtFieldAnimation.addRequiredFieldListener(txtField17);
        txtFieldAnimation.addRequiredFieldListener(txtField19);
    }

    /*Clear Fields*/
    public void clearFields() {
        pnRow = 0;
        /*clear tables*/
        removeRequired();
        comboBox02.setValue(null);
        txtField03.setText("");
        txtField04.setText("");
        txtField07.setText("");
        txtField17.setText("");
        txtField19.setText("");
        txtField06.setText("");
        txtField09.setText("");
        txtField11.setText("");
        txtField10.setText("");
        textSeek03.setText("");
    }

    private void removeRequired() {
        txtFieldAnimation.removeShakeAnimation(txtField03, txtFieldAnimation.shakeTextField(txtField03), "required-field");
        txtFieldAnimation.removeShakeAnimation(txtField04, txtFieldAnimation.shakeTextField(txtField04), "required-field");
        txtFieldAnimation.removeShakeAnimation(txtField07, txtFieldAnimation.shakeTextField(txtField07), "required-field");
        txtFieldAnimation.removeShakeAnimation(txtField17, txtFieldAnimation.shakeTextField(txtField17), "required-field");
        txtFieldAnimation.removeShakeAnimation(txtField19, txtFieldAnimation.shakeTextField(txtField19), "required-field");

    }
}
