/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.auto.app.bank;

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
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
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
import org.rmj.auto.clients.base.BankInformation;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class BankEntryFormController implements Initializable, ScreenInterface {

    private GRider oApp;
    private BankInformation oTrans;
    unloadForm unload = new unloadForm(); //Object for closing form
    private final String pxeModuleName = "Bank"; //Form Title
    private MasterCallback oListener;
    TextFieldAnimationUtil txtFieldAnimation = new TextFieldAnimationUtil();
    private int pnEditMode;//Modifying fields
    private int pnRow = -1;
    private int oldPnRow = -1;
    private int lnCtr = 0;
    private int pagecounter;

    private String oldTransNo = "";
    private String TransNo = "";

    private final ObservableList<BankEntryTableList> bankentrydata = FXCollections.observableArrayList();
    private FilteredList<BankEntryTableList> filteredData;
    private static final int ROWS_PER_PAGE = 50;

    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Pagination pagination;
    @FXML
    private TableView<BankEntryTableList> tblBankEntry;
    @FXML
    private TableColumn<BankEntryTableList, String> tblindexRow; //Row
    @FXML
    private TableColumn<BankEntryTableList, String> tblindex02; //sBankName
    @FXML
    private TableColumn<BankEntryTableList, String> tblindex03; //sBankCode
    @FXML
    private TableColumn<BankEntryTableList, String> tblindex17; //sBankBrch
    @FXML
    private TableColumn<BankEntryTableList, String> tblindex16; //sTownProv
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnClose;
    @FXML
    private TextField txtField02; //sBankName
    @FXML
    private TextField txtField03; //sBankCode
    @FXML
    private TextField txtField04; //sContactP
    @FXML
    private TextField txtField08; //sTelNoxxx
    @FXML
    private TextField txtField18; //sTownName
    @FXML
    private TextField txtField15; //sProvName
    @FXML
    private TextField txtField07; //sZippCode
    @FXML
    private TextField txtField09; //sFaxNoxxx
    @FXML
    private TextField txtField05; //sAddressx
    @FXML
    private TextField txtField17; //ssBankBrch
    @FXML
    private TextField textSeek02; //for search
    @FXML
    private Button btnCancel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        oListener = (int fnIndex, Object foValue) -> {
            System.out.println("Set Class Value " + fnIndex + "-->" + foValue);
        };

        oTrans = new BankInformation(oApp, oApp.getBranchCode(), true); //Initialize ClientMaster
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

        tblBankEntry.setOnMouseClicked(this::tblBankEntry_Clicked);

        /*Clear Fields*/
        clearFields();

        Pattern pattern = Pattern.compile("[\\d\\p{Punct}]*");
        txtField08.setTextFormatter(new InputTextFormatter(pattern)); //sTelNoxxx
        txtField09.setTextFormatter(new InputTextFormatter(pattern)); //sFaxNoxxx

        txtField15.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                txtField18.clear(); // Clear the contents of textField18
                txtField18.setDisable(true);
                txtField07.clear(); // Clear the contents of textField18
                txtField07.setDisable(true);// Disable textField18 when textField15 is empty
            }
        });

        loadBankEntryTable();
        pagination.setPageFactory(this::createPage);
        pnEditMode = EditMode.UNKNOWN;
        initButton(pnEditMode);
        
        Platform.runLater(() -> {
            if(oTrans.loadState()){
                pnEditMode = oTrans.getEditMode();
                loadBankEntryField();
                initButton(pnEditMode);
            }else {
                if(oTrans.getMessage().isEmpty()){
                }else{
                    ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                }
            }
        });
    }

    private Stage getStage() {
        return (Stage) txtField02.getScene().getWindow();
    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private void initSetCapsLockField() {
        setCapsLockBehavior(txtField03);
        setCapsLockBehavior(txtField02);
        setCapsLockBehavior(txtField17);
        setCapsLockBehavior(txtField05);
        setCapsLockBehavior(txtField18);
        setCapsLockBehavior(txtField15);
        setCapsLockBehavior(txtField07);
        setCapsLockBehavior(txtField04);
        setCapsLockBehavior(txtField08);
        setCapsLockBehavior(txtField09);
    }

    private static void setCapsLockBehavior(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (textField.getText() != null) {
                textField.setText(newValue.toUpperCase());
            }
        });
    }

    private void initTxtFieldFocus() {
        txtField02.focusedProperty().addListener(txtField_Focus); // sBankName
//          txtField03.focusedProperty().addListener(txtField_Focus); // sBankAdv
        txtField17.focusedProperty().addListener(txtField_Focus); // sBankBrch
        txtField05.focusedProperty().addListener(txtField_Focus); // sAddressx
        txtField18.focusedProperty().addListener(txtField_Focus); // sTownName
        txtField15.focusedProperty().addListener(txtField_Focus); // sProvName
        txtField07.focusedProperty().addListener(txtField_Focus); // sZippCode
        txtField04.focusedProperty().addListener(txtField_Focus); // sContactP
        txtField08.focusedProperty().addListener(txtField_Focus); // sTelNoxxx
        txtField09.focusedProperty().addListener(txtField_Focus); // sFaxNoxxx

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
                    case 2: // sBankName
                    case 17: // sBankBrch
                    case 5: // sAddressx
                    case 7: // sZipCode
                    case 4: // sContactP
                    case 15: // sProvName
                    case 18: // sTownNamexx
                    case 8: // sTelNoxxx
                    case 9: // sFaxNoxx
                        oTrans.setMaster(lnIndex, lsValue);
                        break;
                }
            } else {
                txtField.selectAll();

            }
        } catch (SQLException ex) {
            Logger.getLogger(BankEntryFormController.class
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
                    loadBankEntryField();
                    pnEditMode = oTrans.getEditMode();
                } else {
                    ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                }
                break;
            case "btnEdit":
                if (oTrans.UpdateRecord()) {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (txtField15.getText().isEmpty()) {
                            // Add your logic here when txtField15 is empty
                            ShowMessageFX.Warning(getStage(), "Province is empty.", "Warning", null);
                            break; // Break the switch statement or return if necessary
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
                    if (txtField02.getText().trim().equals("")) {
                        ShowMessageFX.Warning(getStage(), "Please enter a value for Bank Name.", "Warning", null);
                        txtField02.requestFocus();
                        return;
                    }
//                         if (txtField03.getText().trim().equals("")) {
//                              ShowMessageFX.Warning(getStage(), "Please enter a value for Bank Addev.","Warning", null);
//                              txtField03.requestFocus();
//                              return;
//                         }
                    if (txtField17.getText().trim().equals("")) {
                        ShowMessageFX.Warning(getStage(), "Please enter a valid value for Branch", "Warning", null);
                        txtField17.requestFocus();
                        return;
                    }
                    if (txtField05.getText().trim().equals("")) {
                        ShowMessageFX.Warning(getStage(), "Please enter a value for HouseNo/Street/Barangay.", "Warning", null);
                        txtField05.requestFocus();
                        return;
                    }
                    if (txtField15.getText().trim().equals("")) {
                        ShowMessageFX.Warning(getStage(), "Please enter a valid value for Province.", "Warning", null);
                        txtField15.requestFocus();
                        return;
                    }
                    if (txtField18.getText().trim().equals("")) {
                        ShowMessageFX.Warning(getStage(), "Please enter a value for Municipality.", "Warning", null);
                        txtField18.requestFocus();
                        return;
                    }
                    if (txtField07.getText().trim().equals("")) {
                        ShowMessageFX.Warning(getStage(), "Please enter a value for Zipcode.", "Warning", null);
                        txtField07.requestFocus();
                        return;
                    }
                    if (txtField04.getText().trim().equals("")) {
                        ShowMessageFX.Warning(getStage(), "Please enter a value for Contact Person", "Warning", null);
                        txtField04.requestFocus();
                        return;
                    }
                    if (txtField08.getText().trim().equals("")) {
                        ShowMessageFX.Warning(getStage(), "Please enter a value for Telephone No.", "Warning", null);
                        txtField08.requestFocus();
                        return;
                    }
                    //Proceed Saving
                    if (oTrans.SaveRecord()) {
                        ShowMessageFX.Information(getStage(), "Transaction save successfully.", pxeModuleName, null);
                        loadBankEntryTable();
                        try {
                            getSelectedItem((String) oTrans.getMaster(1));
                        } catch (SQLException ex) {
                            Logger.getLogger(BankEntryFormController.class.getName()).log(Level.SEVERE, null, ex);
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
    //use for creating new page on pagination

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, bankentrydata.size());
        if (bankentrydata.size() > 0) {
            tblBankEntry.setItems(FXCollections.observableArrayList(bankentrydata.subList(fromIndex, toIndex)));
        }
        return tblBankEntry;
    }

    //storing values on bankentrydata
    private void loadBankEntryTable() {
        try {
            /*Populate table*/
            bankentrydata.clear();
            if (oTrans.LoadList("")) {
                for (lnCtr = 1; lnCtr <= oTrans.getDetailCount(); lnCtr++) {
                    bankentrydata.add(new BankEntryTableList(
                            String.valueOf(lnCtr), //ROW
                            oTrans.getDetail(lnCtr, "sBankIDxx").toString(),//BANKID
                            oTrans.getDetail(lnCtr, "sBankName").toString().toUpperCase(),// sBankName
                            "",//oTrans.getDetail(lnCtr,"sBankCode").toString(),// sBankCode
                            oTrans.getDetail(lnCtr, "sBankBrch").toString().toUpperCase(), // sBankBrch
                            oTrans.getDetail(lnCtr, "sAddressx").toString(), //sAddressx
                            oTrans.getDetail(lnCtr, "sTownProv").toString().toUpperCase(), //sTownProv
                            oTrans.getDetail(lnCtr, "sProvName").toString(), //sProvName
                            oTrans.getDetail(lnCtr, "sZippCode").toString(), //sZipCode
                            oTrans.getDetail(lnCtr, "sContactP").toString(), //sContactP
                            oTrans.getDetail(lnCtr, "sTelNoxxx").toString(), //sTelNoxxx
                            oTrans.getDetail(lnCtr, "sFaxNoxxx").toString(), //sFaxNoxx
                            oTrans.getDetail(lnCtr, "sTownName").toString() //sTownName
                    ));
                }
                initBankEntryTable();
            }
            loadTab();

        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }
    }

    /*populate Table*/
    private void initBankEntryTable() {

        tblindexRow.setCellValueFactory(new PropertyValueFactory<>("tblindexRow"));  //Row
        tblindex02.setCellValueFactory(new PropertyValueFactory<>("tblindex02")); // sBankName
        tblindex03.setCellValueFactory(new PropertyValueFactory<>("tblindex03")); // sBankCode
        tblindex17.setCellValueFactory(new PropertyValueFactory<>("tblindex17")); // sBankBrch
        tblindex16.setCellValueFactory(new PropertyValueFactory<>("tblindex16")); // sTownProv

        filteredData = new FilteredList<>(bankentrydata, b -> true);
        autoSearch(textSeek02);
        SortedList<BankEntryTableList> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblBankEntry.comparatorProperty());
        tblBankEntry.setItems(sortedData);

        tblBankEntry.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblBankEntry.lookup("TableHeaderRow");
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
                            return (clients.getTblindex02().toLowerCase().contains(lowerCaseFilter)); // Does not match.
                        } else {
                            return (clients.getTblindex02().toLowerCase().contains(lowerCaseFilter)); // Does not match.
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
        int totalPage = (int) (Math.ceil(bankentrydata.size() * 1.0 / ROWS_PER_PAGE));
        pagination.setPageCount(totalPage);
        pagination.setCurrentPageIndex(0);
        changeTableView(0, ROWS_PER_PAGE);
        pagination.currentPageIndexProperty().addListener(
                (observable, oldValue, newValue) -> changeTableView(newValue.intValue(), ROWS_PER_PAGE));

    }

    private void changeTableView(int index, int limit) {
        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, bankentrydata.size());

        int minIndex = Math.min(toIndex, filteredData.size());
        SortedList<BankEntryTableList> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(tblBankEntry.comparatorProperty());
        tblBankEntry.setItems(sortedData);
    }

    private void tblBankEntry_Clicked(MouseEvent event) {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            if (ShowMessageFX.OkayCancel(null, pxeModuleName, "You have unsaved data, are you sure you want to continue?") == true) {
            } else {
                return;
            }
        }
        pnRow = tblBankEntry.getSelectionModel().getSelectedIndex();
        pagecounter = pnRow + pagination.getCurrentPageIndex() * ROWS_PER_PAGE;
        if (pagecounter >= 0) {
            if (event.getClickCount() > 0) {
                getSelectedItem(filteredData.get(pagecounter).getTblindex01()); //Populate field based on selected Item

                tblBankEntry.setOnKeyReleased((KeyEvent t) -> {
                    KeyCode key = t.getCode();
                    switch (key) {
                        case DOWN:
                            pnRow = tblBankEntry.getSelectionModel().getSelectedIndex();
                            pagecounter = pnRow + pagination.getCurrentPageIndex() * ROWS_PER_PAGE;
                            if (pagecounter == tblBankEntry.getItems().size()) {
                                pagecounter = tblBankEntry.getItems().size();
                                getSelectedItem(filteredData.get(pagecounter).getTblindex01());
                            } else {
                                int y = 1;
                                pnRow = pnRow + y;
                                getSelectedItem(filteredData.get(pagecounter).getTblindex01());
                            }
                            break;
                        case UP:
                            pnRow = tblBankEntry.getSelectionModel().getSelectedIndex();
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
            loadBankEntryField();
        }
        oldPnRow = pagecounter;

    }

    private void loadBankEntryField() {
        try {
            txtField02.setText((String) oTrans.getMaster("sBankName"));// sBankName
            txtField03.setText((String) oTrans.getMaster("sBankCode"));// sBankAdv
            txtField17.setText((String) oTrans.getMaster("sBankBrch"));// sBankBrch
            txtField05.setText((String) oTrans.getMaster("sAddressx"));// sAddressx
            txtField18.setText((String) oTrans.getMaster("sTownName"));// sTownName
            txtField15.setText((String) oTrans.getMaster("sProvName"));// sProvName
            txtField07.setText((String) oTrans.getMaster("sZippCode"));// sZippCode
            txtField04.setText((String) oTrans.getMaster("sContactP"));// sContactP
            txtField08.setText((String) oTrans.getMaster("sTelNoxxx"));// sTelNoxxx
            txtField09.setText((String) oTrans.getMaster("sFaxNoxxx"));// sFaxNoxxx
        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }
    }

    private void initTxtFieldKeyPressed() {

        txtField02.setOnKeyPressed(this::txtField_KeyPressed); // sBankName
        txtField03.setOnKeyPressed(this::txtField_KeyPressed); // sBankAdv
        txtField17.setOnKeyPressed(this::txtField_KeyPressed); // sBankBrch
        txtField05.setOnKeyPressed(this::txtField_KeyPressed); // sAddressx
        txtField18.setOnKeyPressed(this::txtField_KeyPressed);// sTownName
        txtField15.setOnKeyPressed(this::txtField_KeyPressed); // sProvName
        txtField07.setOnKeyPressed(this::txtField_KeyPressed); // sZippCode
        txtField04.setOnKeyPressed(this::txtField_KeyPressed); // sContactP
        txtField08.setOnKeyPressed(this::txtField_KeyPressed);// sTelNoxxx
        txtField09.setOnKeyPressed(this::txtField_KeyPressed); // sFaxNoxxx
    }

    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));

        try {
            if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
                switch (lnIndex) {
                    case 18: // sTownNamexx
                        if (oTrans.searchTown(txtField.getText(), false)) {
                            loadBankEntryField();
                        } else {
                            ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                            txtField18.requestFocus();
                            return;
                        }
                        break;
                    case 15: // sProvName
                        if (oTrans.searchProvince(txtField.getText(), false)) {
                            txtField18.setDisable(false);
                            txtField07.setDisable(false);
                            loadBankEntryField();
                            txtField18.clear();
                            txtField07.clear();
                        } else {
                            txtField15.clear();
                            ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                            txtField15.requestFocus();
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

    private void initButton(int fnValue) {
        pnRow = 0;
        /* NOTE:
                  lbShow (FALSE)= invisible
                  !lbShow (TRUE)= visible
         */

        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

        /*Bank Entry*/
        txtField02.setDisable(!lbShow); // sBankNamexx
//        txtField03.setDisable(true); // sBankAdv
        txtField17.setDisable(!lbShow); // sBranchx
        txtField05.setDisable(!lbShow); // sBarangayx
        txtField18.setDisable(!(lbShow && !txtField15.getText().isEmpty()));
        txtField15.setDisable(!lbShow); // sProvNamexx
        txtField07.setDisable(!(lbShow && !txtField15.getText().isEmpty()));
        txtField04.setDisable(!lbShow); // sContactP
        txtField08.setDisable(!lbShow); // sTeleNo
        txtField09.setDisable(!lbShow); // sFaxNoxx

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
        txtFieldAnimation.addRequiredFieldListener(txtField02);
        txtFieldAnimation.addRequiredFieldListener(txtField03);
        txtFieldAnimation.addRequiredFieldListener(txtField17);
        txtFieldAnimation.addRequiredFieldListener(txtField05);
        txtFieldAnimation.addRequiredFieldListener(txtField18);
        txtFieldAnimation.addRequiredFieldListener(txtField15);
    }

    /*Clear Fields*/
    public void clearFields() {
        pnRow = 0;
        /*clear tables*/
        removeRequired();
        txtField02.setText(""); // sBankName
        txtField03.setText("");  // sBankAdv
        txtField17.setText("");  // sBankBrch
        txtField05.setText("");  // sAddressx
        txtField18.setText("");  // sTownName
        txtField15.setText("");  // sProvName
        txtField07.setText("");  // sZippCode
        txtField04.setText("");  // sContactP
        txtField08.setText("");  // sTeleNoxxx
        txtField09.setText(""); // sFaxNoxxx
    }

    private void removeRequired() {
        txtFieldAnimation.removeShakeAnimation(txtField02, txtFieldAnimation.shakeTextField(txtField02), "required-field");
        txtFieldAnimation.removeShakeAnimation(txtField03, txtFieldAnimation.shakeTextField(txtField03), "required-field");
        txtFieldAnimation.removeShakeAnimation(txtField17, txtFieldAnimation.shakeTextField(txtField17), "required-field");
        txtFieldAnimation.removeShakeAnimation(txtField05, txtFieldAnimation.shakeTextField(txtField05), "required-field");
        txtFieldAnimation.removeShakeAnimation(txtField18, txtFieldAnimation.shakeTextField(txtField18), "required-field");
        txtFieldAnimation.removeShakeAnimation(txtField15, txtFieldAnimation.shakeTextField(txtField15), "required-field");

    }
}
