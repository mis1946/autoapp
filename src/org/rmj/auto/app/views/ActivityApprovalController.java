/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.auto.app.views;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;

/**
 * FXML Controller class
 *
 * @author User
 */
public class ActivityApprovalController implements Initializable, ScreenInterface {

    private GRider oApp;
//    private InquiryProcess oTrans;
    private MasterCallback oListener;
    private int lnCtr = 0;
    private final String pxeModuleName = "Activity Approval"; //Form Title
    unloadForm unload = new unloadForm(); //Used in Close Button

    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnApproved;
    @FXML
    private Button btnPrint;
    @FXML
    private Button btnClose;
    @FXML
    private TableView<?> tblViewActApproval;
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
//    private ObservableList<VehicleSalesApprovalTable> actApprovalData = FXCollections.observableArrayList();
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        oTrans = new InquiryProcess(oApp, oApp.getBranchCode(), true); //Initialize ClientMaster
//        oTrans.setCallback(oListener);
//        oTrans.setWithUI(true);
        initOtherUtils();
        initCombo();
//        loadVhlApprovalTable();
        //Buttons
//        tblRow.setSortable(false);
//        tblselected.setSortable(false);
//        tblindex03.setSortable(false);
//        tblindex20.setSortable(false);
//        tblindex12.setSortable(false);
//        tblindex23.setSortable(false);
//        tblindex05.setSortable(false);
//        tblindex24.setSortable(false);
//        tblindex02.setSortable(false);
//        tblbranch.setSortable(false);
        btnClose.setOnAction(this::cmdButton_Click);
        btnActNo.setOnAction(this::cmdButton_Click);
        btnActTitle.setOnAction(this::cmdButton_Click);
        btnDate.setOnAction(this::cmdButton_Click);
        btnPerson.setOnAction(this::cmdButton_Click);
        btnType.setOnAction(this::cmdButton_Click);
        btnDepart.setOnAction(this::cmdButton_Click);
        btnApproved.setOnAction(this::cmdButton_Click);
        btnRefresh.setOnAction(this::cmdButton_Click);
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
//                    tblViewActApproval.setItems(actApprovalData);

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
//                    tblViewActApproval.setItems(actApprovalData);
                    break;
                case "Activity Type":
                    comboType.setVisible(true);
                    comboType.setManaged(true);
                    btnType.setVisible(true);
                    btnType.setManaged(true);
//                    tblViewActApproval.setItems(actApprovalData);
                    break;
                case "Activity Title":
                    txtFieldSearch.setText("");
                    txtFieldSearch.setVisible(true);
                    txtFieldSearch.setManaged(true);
                    btnActTitle.setVisible(true);
                    btnActTitle.setManaged(true);
//                    tblViewActApproval.setItems(actApprovalData);
                    break;
                case "Person in Charge":
                    txtFieldSearch.setText("");
                    txtFieldSearch.setVisible(true);
                    txtFieldSearch.setManaged(true);
                    btnPerson.setVisible(true);
                    btnPerson.setManaged(true);
//                    tblViewActApproval.setItems(actApprovalData);

                    break;
                case "Department":
                    txtFieldSearch.setText("");
                    txtFieldSearch.setVisible(true);
                    txtFieldSearch.setManaged(true);
                    btnDepart.setVisible(true);
                    btnDepart.setManaged(true);
//                    tblViewActApproval.setItems(actApprovalData);
                    break;

            }
        });
    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    //populate data from table
//    private void loadVhlApprovalTable() {
//        try {
//            actApprovalData.clear();
//            if (oTrans.loadRsvForApproval()) {
//                for (lnCtr = 1; lnCtr <= oTrans.getReserveCount(); lnCtr++) {
//                    // Iterate over the data and count the approved item
//                    String amountString = oTrans.getInqRsv(lnCtr, "nAmountxx").toString();
//                    // Convert the amount to a decimal value
//                    double amount = Double.parseDouble(amountString);
//                    // Format the decimal value with decimal separators
//                    DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
//                    String formattedAmount = decimalFormat.format(amount);
//                    String res = oTrans.getInqRsv(lnCtr, "cResrvTyp").toString();
//                    if (res.equals("0")) {
//                        res = "Reservation";
//                    }
//                    if (res.equals("1")) {
//                        res = "Deposit";
//                    }
//                    if (res.equals("2")) {
//                        res = "Safeguard Duty";
//                    }
//                    actApprovalData.add(new VehicleSalesApprovalTable(
//                            String.valueOf(lnCtr),
//                            oTrans.getInqRsv(lnCtr, "cTranStat").toString(),
//                            oTrans.getInqRsv(lnCtr, "sTransNox").toString(),
//                            oTrans.getInqRsv(lnCtr, "sReferNox").toString().toUpperCase(),
//                            res.toUpperCase(),
//                            CommonUtils.xsDateShort((Date) oTrans.getInqRsv(lnCtr, "dTransact")),
//                            oTrans.getInqRsv(lnCtr, "sCompnyNm").toString().toUpperCase(),
//                            oTrans.getInqRsv(lnCtr, "sDescript").toString().toUpperCase(),
//                            formattedAmount,
//                            oTrans.getInqRsv(lnCtr, "sSeNamexx").toString().toUpperCase(),
//                            "".toUpperCase()
//                    ));
//                }
//                tblViewActApproval.setItems(actApprovalData);
//                initVhlApprovalTable();
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(VehicleSalesApprovalController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
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
            case "btnActNo": //btn filter for Slip No
//                String filterActNo = txtFieldSearch.getText().trim().toLowerCase();
//                // Initialize the filteredData variable
//                FilteredList<VehicleSalesApprovalTable> filteredTxtFieldActNo = new FilteredList<>(vhlApprovalData);
//                // Apply the filter predicate based on the entered text
//                filteredTxtFieldActNo.setPredicate(clients -> {
//                    if (filterActNo.isEmpty()) {
//                        // No filter text entered, show all data
//                        return true;
//                    } else {
//
//                        String actNo = clients.getTblindex03().toLowerCase();
//                        return actNo.contains(filterActNo);
//                    }
//                });
//                tblViewActApproval.setItems(filteredTxtFieldActNo );
//                if (filteredTxtFieldActNo.isEmpty()) {
//                    ShowMessageFX.Information(null, pxeModuleName, "No record found!");
//                }
                break;
            case "btnActTitle": //btn filter for Customer Name
//                String filterActTitle = txtFieldSearch.getText().trim().toLowerCase();
//                // Initialize the filteredData variable
//                FilteredList<VehicleSalesApprovalTable> filteredTxtFieldActTitle = new FilteredList<>(vhlApprovalData);
//                // Apply the filter predicate based on the entered text
//                filteredTxtFieldActTitle.setPredicate(clients -> {
//                    if (filterActTitle.isEmpty()) {
//                        // No filter text entered, show all data
//                        return true;
//                    } else {
//                        String actTitle = clients.getTblindex20().toLowerCase();
//                        return actTitle.contains(filterActTitle);
//                    }
//                });
//                tblViewActApproval.setItems(filteredTxtFieldActTitle);
//                if (filteredTxtFieldActTitle.isEmpty()) {
//                    ShowMessageFX.Information(null, pxeModuleName, "No record found!");
//                }
                break;
            case "btnDepart": //btn filter for Unit Description
//                String filterDepart = txtFieldSearch.getText().trim().toLowerCase();
//                // Initialize the filteredData variable
//                FilteredList<VehicleSalesApprovalTable> filteredTxtFieldDepart = new FilteredList<>(vhlApprovalData);
//                // Apply the filter predicate based on the entered text
//                filteredTxtFieldDepart.setPredicate(clients -> {
//                    if (filterDepart.isEmpty()) {
//                        // No filter text entered, show all data
//                        return true;
//                    } else {
//                        String dePart = clients.getTblindex23().toLowerCase();
//                        return dePart.contains(filterDepart);
//                    }
//                });
//                tblViewActApproval.setItems(filteredTxtFieldDepart);
//                if (filteredTxtFieldDepart.isEmpty()) {
//                    ShowMessageFX.Information(null, pxeModuleName, "No record found!");
//                }
                break;
            case "btnPerson": //btn filter for Employee Name
//                String filterPerson= txtFieldSearch.getText().trim().toLowerCase();
//                // Initialize the filteredData variable
//                FilteredList<VehicleSalesApprovalTable> filteredTxtFieldPerson = new FilteredList<>(vhlApprovalData);
//                // Apply the filter predicate based on the entered text
//                filteredTxtFieldPerson.setPredicate(clients -> {
//                    if (filterPerson.isEmpty()) {
//                        // No filter text entered, show all data
//                        return true;
//                    } else {
//                        String peRson = clients.getTblindex24().toLowerCase();
//                        return peRson .contains(filterPerson);
//                    }
//                });
//                tblViewActApproval.setItems(filteredTxtFieldPerson);
//                if (filteredTxtFieldPerson.isEmpty()) {
//                    ShowMessageFX.Information(null, pxeModuleName, "No record found!");
//                }
                break;
            case "btnType": //btn filter for comboBox
//                String selectedType = comboType.getValue();
//                if (selectedType == null) {
//                    // No type selected, show all data
//                    tblViewActApproval.setItems(vhlApprovalData);
//                } else {
//                    // Filter data based on selected type
//                    ObservableList<VehicleSalesApprovalTable> filteredCombo = FXCollections.observableArrayList();
//                    for (VehicleSalesApprovalTable actData : vhlApprovalData) {
//                        if (actData.getTblindex12().equals(selectedType)) {
//                            filteredCombo.add(actData);
//                        }
//                    }
//                    tblViewActApproval.setItems(filteredCombo);
//                    if (filteredCombo.isEmpty()) {
//                        ShowMessageFX.Information(null, pxeModuleName, "No record found!");
//                    }
//                }
                break;
            case "btnDate": //btn filter for Activity Date
//                LocalDate filterFromDate = fromDate.getValue();
//                LocalDate filterToDate = toDate.getValue();
//
//                ObservableList<VehicleSalesApprovalTable> filteredDate = FXCollections.observableArrayList();
//                for (VehicleSalesApprovalTable actData : vhlApprovalData) {
//                    LocalDate actDate = LocalDate.parse(actData.getTblindex02());
//
//                    if (filterFromDate == null || actDate.isAfter(filterFromDate.minusDays(1))) {
//                        if (filterToDate == null || actDate.isBefore(filterToDate.plusDays(1))) {
//                            filteredDate.add(actData);
//                        }
//                    }
//                }
//
//                tblViewActApproval.setItems(filteredDate);
//                if (filteredDate.isEmpty()) {
//                    ShowMessageFX.Information(null, pxeModuleName, "No record found!");
//                }
                break;
            case "btnApproved": //btn for approval
//                ObservableList<VehicleSalesApprovalTable> selectedItems = FXCollections.observableArrayList();
//                for (VehicleSalesApprovalTable item : tblVhclApproval.getItems()) {
//                    if (item.getSelect().isSelected()) {
//                        selectedItems.add(item);
//                    }
//                }
//                if (selectedItems.isEmpty()) {
//                    ShowMessageFX.Information(null, pxeModuleName, "No items selected to approve.");
//                } else {
//                    int i = 0;
//                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to approve?")) {
//                        // Call the ApproveReservation() method here
//                        for (VehicleSalesApprovalTable item : selectedItems) {
//                            String fsTransNox = item.getTblindex01(); // Assuming there is a method to retrieve the transaction number
//                            try {
//                                boolean approved = oTrans.ApproveReservation(fsTransNox);
//                                if (approved) {
//                                    i = i + 1;
//                                    ApprovedCount.setText("" + i);
//                                } else {
//                                    // Handle approval failure
//                                    ShowMessageFX.Error(null, pxeModuleName, "Failed to approve reservation.");
//                                }
//                            } catch (SQLException e) {
//                                // Handle SQL exception
//                                ShowMessageFX.Error(null, pxeModuleName, "An error occurred while approving reservation: " + e.getMessage());
//                            }
//                        }
//                        SelectedCount.setText("0");
//                        loadVhlApprovalTable();
//                        ShowMessageFX.Information(null, pxeModuleName, "Reservation approved successfully.");
//                        tblViewActApproval.getItems().removeAll(selectedItems);
//                        tblViewActApproval.refresh();
//                    }
//                }
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
//                loadVhlApprovalTable();
//                SelectedCount.setText("0");
//                ApprovedCount.setText("0");
//                selectAllCheckBox.setSelected(false);
                break;
        }
    }

    private Stage getStage() {
        return (Stage) txtFieldSearch.getScene().getWindow();
    }

//    private void initVhlApprovalTable() {
//        tblRow.setCellValueFactory(new PropertyValueFactory<>("tblRow"));  //Row
//        // Set up listener for "Select All" checkbox
//        tblselected.setCellValueFactory(new PropertyValueFactory<>("select"));
//        tblViewActApproval.getItems().forEach(item -> {
//            CheckBox selectCheckBox = item.getSelect();
//            selectCheckBox.setOnAction(event -> {
//                updateSelectedCount();
//                if (tblViewActApproval.getItems().stream().allMatch(tableItem -> tableItem.getSelect().isSelected())) {
//                    selectAllCheckBox.setSelected(true);
//                } else {
//                    selectAllCheckBox.setSelected(false);
//                }
//            });
//        });
//        selectAllCheckBox.setOnAction(event -> {
//            boolean newValue = selectAllCheckBox.isSelected();
//            tblViewActApproval.getItems().forEach(item -> item.getSelect().setSelected(newValue));
//            updateSelectedCount();
//        });
//        tblindex03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
//        tblindex12.setCellValueFactory(new PropertyValueFactory<>("tblindex12"));
//        tblindex02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
//        tblindex20.setCellValueFactory(new PropertyValueFactory<>("tblindex20"));
//        tblindex23.setCellValueFactory(new PropertyValueFactory<>("tblindex23"));
//        tblindex05.setCellValueFactory(new PropertyValueFactory<>("tblindex05"));
//        tblindex24.setCellValueFactory(new PropertyValueFactory<>("tblindex24"));
//        tblbranch.setCellValueFactory(new PropertyValueFactory<>("tblbranch"));
//    }
}
