/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.auto.app.views;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.auto.clients.base.Activity;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class ActivityApprovalController implements Initializable, ScreenInterface {

    private GRider oApp;
    private Activity oTrans;
    private MasterCallback oListener;
    private int lnCtr = 0;
    private final String pxeModuleName = "Activity Approval"; //Form Title
    unloadForm unload = new unloadForm(); //Used in Close Button

    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnApproved;
    @FXML
    private Button btnClose;
    @FXML
    private TableView<ActivityApprovalTable> tblViewActApproval;
    @FXML
    private Button btnActNo;
    @FXML
    private Button btnActTitle;
    @FXML
    private Button btnPerson;
    @FXML
    private Button btnDepart;
    @FXML
    private ComboBox<String> comboFilter;
    ObservableList<String> cFilter = FXCollections.observableArrayList("Activity No.", "Activity Date", "Activity Title", "Activity Type",
            "Person in Charge", "Department");
    ObservableList<String> cType = FXCollections.observableArrayList("EVENT", "PROMO", "SALES");
    private ObservableList<ActivityApprovalTable> actApprovalData = FXCollections.observableArrayList();
    @FXML
    private TextField txtFieldSearch;
    @FXML
    private Button btnDate;
    @FXML
    private Label lFrom;
    @FXML
    private DatePicker fromDate;
    @FXML
    private Label lTo;
    @FXML
    private DatePicker toDate;
    @FXML
    private Button btnRefresh;
    @FXML
    private ComboBox<String> comboType;
    @FXML
    private Button btnType;
    @FXML
    private TableColumn<ActivityApprovalTable, String> tblRow;
    @FXML
    private TableColumn<ActivityApprovalTable, Boolean> tblindexselect;
    @FXML
    private TableColumn<ActivityApprovalTable, String> tblindex01;
    @FXML
    private TableColumn<ActivityApprovalTable, String> tblindexDate;
    @FXML
    private TableColumn<ActivityApprovalTable, String> tblindex02;
    @FXML
    private TableColumn<ActivityApprovalTable, String> tblindex25;
    @FXML
    private TableColumn<ActivityApprovalTable, String> tblindex24;
    @FXML
    private TableColumn<ActivityApprovalTable, String> tblindex26;
    @FXML
    private TableColumn<ActivityApprovalTable, String> tblindex28;
    @FXML
    private CheckBox selectAllCheckBox;
    @FXML
    private TableColumn<ActivityApprovalTable, String> tblindex10;
    @FXML
    private Label lbTotalBudget;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTrans = new Activity(oApp, oApp.getBranchCode(), true); //Initialize ClientMaster
        oTrans.setCallback(oListener);
        oTrans.setWithUI(true);
        initOtherUtils();
        initCombo();
        loadActApprovalTable();
        btnClose.setOnAction(this::cmdButton_Click);
        btnActNo.setOnAction(this::cmdButton_Click);
        btnActTitle.setOnAction(this::cmdButton_Click);
        btnDate.setOnAction(this::cmdButton_Click);
        btnPerson.setOnAction(this::cmdButton_Click);
        btnType.setOnAction(this::cmdButton_Click);
        btnDepart.setOnAction(this::cmdButton_Click);
        btnApproved.setOnAction(this::cmdButton_Click);
        btnRefresh.setOnAction(this::cmdButton_Click);

        tblViewActApproval.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewActApproval.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

    }

    //Date Formatter
    private LocalDate strToDate(String val) {
        DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(val, date_formatter);
        return localDate;
    }

    private void initOtherUtils() {
        btnActNo.setVisible(false);
        btnActNo.setManaged(false);
        btnActTitle.setVisible(false);
        btnActTitle.setManaged(false);
        btnDate.setVisible(false);
        btnDate.setManaged(false);
        btnPerson.setVisible(false);
        btnPerson.setManaged(false);
        btnDepart.setVisible(false);
        btnDepart.setManaged(false);
        btnType.setVisible(false);
        btnType.setManaged(false);
        txtFieldSearch.setVisible(false);
        txtFieldSearch.setManaged(false);
        lFrom.setManaged(false);
        lFrom.setVisible(false);
        fromDate.setVisible(false);
        fromDate.setValue(LocalDate.of(strToDate(CommonUtils.xsDateShort((Date) oApp.getServerDate())).getYear(), strToDate(CommonUtils.xsDateShort((Date) oApp.getServerDate())).getMonth(), 1));
        fromDate.setManaged(false);
        lTo.setVisible(false);
        lTo.setManaged(false);
        toDate.setVisible(false);
        toDate.setValue(strToDate(CommonUtils.xsDateShort((Date) oApp.getServerDate())));
        toDate.setManaged(false);
        comboType.setVisible(false);
        comboType.setManaged(false);
        comboFilter.setItems(cFilter);
        comboType.setItems(cType);
    }

    public void initCombo() {
        comboFilter.setOnAction(e -> {
            String selectedFilter = comboFilter.getSelectionModel().getSelectedItem();
            // Hide all controls first
            txtFieldSearch.setVisible(false);
            txtFieldSearch.setManaged(false);
            btnActNo.setVisible(false);
            btnActNo.setManaged(false);
            btnActTitle.setVisible(false);
            btnActTitle.setManaged(false);
            btnDate.setVisible(false);
            btnDate.setManaged(false);
            btnPerson.setVisible(false);
            btnPerson.setManaged(false);
            btnDepart.setVisible(false);
            btnDepart.setManaged(false);
            btnType.setVisible(false);
            btnType.setManaged(false);
            txtFieldSearch.setVisible(false);
            txtFieldSearch.setManaged(false);
            lFrom.setManaged(false);
            lFrom.setVisible(false);
            fromDate.setVisible(false);
            fromDate.setManaged(false);
            lTo.setVisible(false);
            lTo.setManaged(false);
            toDate.setVisible(false);
            toDate.setManaged(false);
            comboType.setVisible(false);
            comboType.setManaged(false);

            // Show relevant controls based on selected filter
            switch (selectedFilter) {
                case "Activity No.":
                    txtFieldSearch.setText("");
                    txtFieldSearch.setVisible(true);
                    txtFieldSearch.setManaged(true);
                    btnActNo.setVisible(true);
                    btnActNo.setManaged(true);
                    tblViewActApproval.setItems(actApprovalData);

                    break;
                case "Activity Date":
                    btnDate.setVisible(true);
                    btnDate.setManaged(true);
                    lFrom.setVisible(true);
                    lFrom.setManaged(true);

                    fromDate.setVisible(true);
                    fromDate.setManaged(true);
                    lTo.setVisible(true);
                    lTo.setManaged(true);
                    toDate.setVisible(true);
                    toDate.setManaged(true);
                    tblViewActApproval.setItems(actApprovalData);
                    break;
                case "Activity Type":
                    comboType.setVisible(true);
                    comboType.setManaged(true);
                    btnType.setVisible(true);
                    btnType.setManaged(true);
                    tblViewActApproval.setItems(actApprovalData);
                    break;
                case "Activity Title":
                    txtFieldSearch.setText("");
                    txtFieldSearch.setVisible(true);
                    txtFieldSearch.setManaged(true);
                    btnActTitle.setVisible(true);
                    btnActTitle.setManaged(true);
                    tblViewActApproval.setItems(actApprovalData);
                    break;
                case "Person in Charge":
                    txtFieldSearch.setText("");
                    txtFieldSearch.setVisible(true);
                    txtFieldSearch.setManaged(true);
                    btnPerson.setVisible(true);
                    btnPerson.setManaged(true);
                    tblViewActApproval.setItems(actApprovalData);

                    break;
                case "Department":
                    txtFieldSearch.setText("");
                    txtFieldSearch.setVisible(true);
                    txtFieldSearch.setManaged(true);
                    btnDepart.setVisible(true);
                    btnDepart.setManaged(true);
                    tblViewActApproval.setItems(actApprovalData);
                    break;

            }
        });
    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private void loadActApprovalTable() {
        try {
            actApprovalData.clear();
            double totalBudg = 0;
            if (oTrans.loadActForApproval()) {
                for (lnCtr = 1; lnCtr <= oTrans.getItemCount(); lnCtr++) {
                    // Iterate over the data and count the approved item
                    String amountString = oTrans.getDetail(lnCtr, "nPropBdgt").toString();
                    // Convert the amount to a decimal value
                    double amount = Double.parseDouble(amountString);
                    // Format the decimal value with decimal separators
                    DecimalFormat decimalFormat = new DecimalFormat("#,##0.0");
                    String formattedAmount = decimalFormat.format(amount);
                    String res = oTrans.getDetail(lnCtr, "sEventTyp").toString();
                    if (res.equals("sal")) {
                        res = "SALES";
                    }
                    if (res.equals("pro")) {
                        res = "PROMO";
                    }
                    if (res.equals("eve")) {
                        res = "EVENT";
                    }

                    actApprovalData.add(new ActivityApprovalTable(
                            String.valueOf(lnCtr),
                            oTrans.getDetail(lnCtr, "cTranStat").toString().toUpperCase(),
                            oTrans.getDetail(lnCtr, "sActvtyID").toString().toUpperCase(),
                            oTrans.getDetail(lnCtr, "sActTitle").toString().toUpperCase(),
                            oTrans.getDetail(lnCtr, "sActDescx").toString().toUpperCase(),
                            res.toUpperCase(),
                            oTrans.getDetail(lnCtr, "dDateFrom").toString().toUpperCase(),
                            oTrans.getDetail(lnCtr, "dDateThru").toString().toUpperCase(),
                            oTrans.getDetail(lnCtr, "sLocation").toString().toUpperCase(),
                            oTrans.getDetail(lnCtr, "sCompnynx").toString().toUpperCase(),
                            oTrans.getDetail(lnCtr, "nPropBdgt").toString(),
                            oTrans.getDetail(lnCtr, "sDeptName").toString().toUpperCase(),
                            oTrans.getDetail(lnCtr, "sCompnyNm").toString().toUpperCase(),
                            oTrans.getDetail(lnCtr, "sBranchNm").toString().toUpperCase(),
                            oTrans.getDetail(lnCtr, "sProvName").toString().toUpperCase()));
                    totalBudg = totalBudg + Double.parseDouble(oTrans.getDetail(lnCtr, "nPropBdgt").toString());
                }
                lbTotalBudget.setText((CommonUtils.NumberFormat((Number) totalBudg, "₱ " + "#,##0.00")));
                tblViewActApproval.setItems(actApprovalData);
                initActApprovalTable();

            }
        } catch (SQLException ex) {
            Logger.getLogger(ActivityApprovalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initActApprovalTable() {
        tblRow.setCellValueFactory(new PropertyValueFactory<>("tblRow"));  //Row
        // Set up listener for "Select All" checkbox
        tblindexselect.setCellValueFactory(new PropertyValueFactory<>("select"));
        tblViewActApproval.getItems().forEach(item -> {
            CheckBox selectCheckBox = item.getSelect();
            selectCheckBox.setOnAction(event -> {
                if (tblViewActApproval.getItems().stream().allMatch(tableItem -> tableItem.getSelect().isSelected())) {
                    selectAllCheckBox.setSelected(true);
                } else {
                    selectAllCheckBox.setSelected(false);
                }
            });
        });
        selectAllCheckBox.setOnAction(event -> {
            boolean newValue = selectAllCheckBox.isSelected();
            tblViewActApproval.getItems().forEach(item -> item.getSelect().setSelected(newValue));
        });
        tblindex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
        tblindex02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
        tblindexDate.setCellValueFactory(cellData -> {
            // Get the data for the current row
            ActivityApprovalTable rowData = cellData.getValue();

            // Get the values you want to concatenate
            String value1 = rowData.getTblindex06();
            String value2 = rowData.getTblindex07();

            // Concatenate the values
            String concatenatedValue = value1 + " - " + value2;

            // Create a new ObservableValue containing the concatenated value
            ObservableValue<String> observableValue = new SimpleStringProperty(concatenatedValue);

            return observableValue;
        });
        tblindex24.setCellValueFactory(new PropertyValueFactory<>("tblindex24"));
        tblindex25.setCellValueFactory(new PropertyValueFactory<>("tblindex25"));
        tblindex26.setCellValueFactory(new PropertyValueFactory<>("tblindex26"));
        tblindex28.setCellValueFactory(new PropertyValueFactory<>("tblindex28"));
        tblindex10.setCellValueFactory(new PropertyValueFactory<>("tblindex10"));

    }

    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnClose": //close tab
                if (ShowMessageFX.OkayCancel(null, "Close Tab", "Are you sure, do you want to close tab?") == true) {
                    if (unload != null) {
                        unload.unloadForm(AnchorMain, oApp, pxeModuleName);
                    } else {
                        ShowMessageFX.Warning(null, "Warning", "Notify System Admin to Configure Null value at close button.");
                    }
                } else {
                    return;
                }
                break;
            case "btnActNo":
                String filterActNo = txtFieldSearch.getText().trim().toLowerCase();
                // Initialize the filteredData variable
                FilteredList<ActivityApprovalTable> filteredTxtFieldActNo = new FilteredList<>(actApprovalData);
                // Apply the filter predicate based on the entered text
                filteredTxtFieldActNo.setPredicate(clients -> {
                    if (filterActNo.isEmpty()) {
                        // No filter text entered, show all data
                        return true;
                    } else {

                        String actNo = clients.getTblindex01().toLowerCase();
                        return actNo.contains(filterActNo);
                    }
                });
                tblViewActApproval.setItems(filteredTxtFieldActNo);
                if (filteredTxtFieldActNo.isEmpty()) {
                    ShowMessageFX.Information(null, pxeModuleName, "No record found!");
                }
                break;
            case "btnActTitle":
                String filterActTitle = txtFieldSearch.getText().trim().toLowerCase();
                // Initialize the filteredData variable
                FilteredList<ActivityApprovalTable> filteredTxtFieldActTitle = new FilteredList<>(actApprovalData);
                // Apply the filter predicate based on the entered text
                filteredTxtFieldActTitle.setPredicate(clients -> {
                    if (filterActTitle.isEmpty()) {
                        // No filter text entered, show all data
                        return true;
                    } else {
                        String actTitle = clients.getTblindex02().toLowerCase();
                        return actTitle.contains(filterActTitle);
                    }
                });
                tblViewActApproval.setItems(filteredTxtFieldActTitle);
                if (filteredTxtFieldActTitle.isEmpty()) {
                    ShowMessageFX.Information(null, pxeModuleName, "No record found!");
                }
                break;
            case "btnDepart":
                String filterDepart = txtFieldSearch.getText().trim().toLowerCase();
                // Initialize the filteredData variable
                FilteredList<ActivityApprovalTable> filteredTxtFieldDepart = new FilteredList<>(actApprovalData);
                // Apply the filter predicate based on the entered text
                filteredTxtFieldDepart.setPredicate(clients -> {
                    if (filterDepart.isEmpty()) {
                        // No filter text entered, show all data
                        return true;
                    } else {
                        String dePart = clients.getTblindex24().toLowerCase();
                        return dePart.contains(filterDepart);
                    }
                });
                tblViewActApproval.setItems(filteredTxtFieldDepart);
                if (filteredTxtFieldDepart.isEmpty()) {
                    ShowMessageFX.Information(null, pxeModuleName, "No record found!");
                }
                break;
            case "btnPerson":
                String filterPerson = txtFieldSearch.getText().trim().toLowerCase();
                // Initialize the filteredData variable
                FilteredList<ActivityApprovalTable> filteredTxtFieldPerson = new FilteredList<>(actApprovalData);
                // Apply the filter predicate based on the entered text
                filteredTxtFieldPerson.setPredicate(clients -> {
                    if (filterPerson.isEmpty()) {
                        // No filter text entered, show all data
                        return true;
                    } else {
                        String peRson = clients.getTblindex25().toLowerCase();
                        return peRson.contains(filterPerson);
                    }
                });
                tblViewActApproval.setItems(filteredTxtFieldPerson);
                if (filteredTxtFieldPerson.isEmpty()) {
                    ShowMessageFX.Information(null, pxeModuleName, "No record found!");
                }
                break;
            case "btnType": //btn filter for comboBox
                String selectedType = comboType.getValue();
                if (selectedType == null) {
                    // No type selected, show all data
                    tblViewActApproval.setItems(actApprovalData);
                } else {
                    // Filter data based on selected type
                    ObservableList<ActivityApprovalTable> filteredCombo = FXCollections.observableArrayList();
                    for (ActivityApprovalTable actData : actApprovalData) {
                        if (actData.getTblindex29().equals(selectedType)) {
                            filteredCombo.add(actData);
                        }
                    }
                    tblViewActApproval.setItems(filteredCombo);
                    if (filteredCombo.isEmpty()) {
                        ShowMessageFX.Information(null, pxeModuleName, "No record found!");
                    }
                }
                break;
            case "btnDate": //btn filter for Activity Date
                LocalDate filterFromDate = fromDate.getValue();
                LocalDate filterToDate = toDate.getValue();

                ObservableList<ActivityApprovalTable> filteredDate = FXCollections.observableArrayList();
                for (ActivityApprovalTable actData : actApprovalData) {
                    LocalDate actDateFrom = LocalDate.parse(actData.getTblindex06());
                    LocalDate actDateTo = LocalDate.parse(actData.getTblindex07());

                    if (filterFromDate == null || actDateFrom.isAfter(filterFromDate.minusDays(1))) {
                        if (filterToDate == null || actDateTo.isBefore(filterToDate.plusDays(1))) {
                            filteredDate.add(actData);
                        }
                    }
                }
                tblViewActApproval.setItems(filteredDate);
                LocalDate dateFrom = fromDate.getValue();
                LocalDate dateTo = toDate.getValue();
                if (dateFrom != null && dateTo != null && dateTo.isBefore(dateFrom)) {
                    ShowMessageFX.Information(null, pxeModuleName, "Please enter a valid date.");
                    fromDate.setValue(LocalDate.of(strToDate(CommonUtils.xsDateShort((Date) oApp.getServerDate())).getYear(), strToDate(CommonUtils.xsDateShort((Date) oApp.getServerDate())).getMonth(), 1));
                    toDate.setValue(strToDate(CommonUtils.xsDateShort((Date) oApp.getServerDate())));
                    loadActApprovalTable();

                    return;

                }
                if (dateFrom != null && dateTo != null && dateFrom.isAfter(dateTo)) {
                    ShowMessageFX.Information(null, pxeModuleName, "Please enter a valid date.");
                    loadActApprovalTable();
                    fromDate.setValue(LocalDate.of(strToDate(CommonUtils.xsDateShort((Date) oApp.getServerDate())).getYear(), strToDate(CommonUtils.xsDateShort((Date) oApp.getServerDate())).getMonth(), 1));
                    toDate.setValue(strToDate(CommonUtils.xsDateShort((Date) oApp.getServerDate())));
                    loadActApprovalTable();

                    return;

                }

                if (filteredDate.isEmpty()) {
                    ShowMessageFX.Information(null, pxeModuleName, "No record found!");

                }
                break;

            case "btnApproved": //btn for approval
                ObservableList<ActivityApprovalTable> selectedItems = FXCollections.observableArrayList();
                for (ActivityApprovalTable item : tblViewActApproval.getItems()) {
                    if (item.getSelect().isSelected()) {
                        selectedItems.add(item);
                    }
                }
                if (selectedItems.isEmpty()) {
                    ShowMessageFX.Information(null, pxeModuleName, "No items selected to approve.");
                } else {
                    int i = 0;
                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to approve?")) {
                        // Call the ApproveReservation() method here
                        for (ActivityApprovalTable item : selectedItems) {
                            String fsTransNox = item.getTblindex01(); // Assuming there is a method to retrieve the transaction number
                            boolean approved = oTrans.ApproveActivity(fsTransNox); // Handle SQL exception
                            if (approved) {
                                i = i + 1;
//                                    ApprovedCount.setText("" + i);
                            } else {
                                // Handle approval failure
                                ShowMessageFX.Error(null, pxeModuleName, "Failed to approve activity.");
                            }
                        }
//                        SelectedCount.setText("0");
                        loadActApprovalTable();
                        ShowMessageFX.Information(null, pxeModuleName, "Activity approved successfully.");
                        tblViewActApproval.getItems().removeAll(selectedItems);
                        tblViewActApproval.refresh();
                    }
                }
                break;
            case "btnRefresh": //btn for refresh
                // Clear the combo box selection
                comboType.getSelectionModel().clearSelection();
                // Clear the text field
                txtFieldSearch.clear();
                // Clear the date picker values
                fromDate.setValue(LocalDate.of(strToDate(CommonUtils.xsDateShort((Date) oApp.getServerDate())).getYear(), strToDate(CommonUtils.xsDateShort((Date) oApp.getServerDate())).getMonth(), 1));
                toDate.setValue(strToDate(CommonUtils.xsDateShort((Date) oApp.getServerDate())));
                // Set the table items back to the original data
                loadActApprovalTable();
//                SelectedCount.setText("0");
//                ApprovedCount.setText("0");
                selectAllCheckBox.setSelected(false);
                break;
        }
    }

    private Stage getStage() {
        return (Stage) txtFieldSearch.getScene().getWindow();
    }

}
