package org.rmj.auto.app.sales;

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
import org.rmj.auto.app.views.ScreenInterface;
import org.rmj.auto.app.views.unloadForm;
import org.rmj.auto.sales.base.InquiryProcess;

/**
 * Vehicle Sales Approval Controller class
 *
 * @author John Dave
 */
public class VehicleSalesApprovalController implements Initializable, ScreenInterface {

    private GRider oApp;
    private InquiryProcess oTrans;
    private MasterCallback oListener;
    private int lnCtr = 0;
    private final String pxeModuleName = "Vehicle Reservation Approval"; //Form Title

    unloadForm unload = new unloadForm(); //Used in Close Button

    @FXML
    private AnchorPane AnchorMain;
    ObservableList<String> cFilter = FXCollections.observableArrayList("Advance Slip Date", "Advances Slip No.", "Advances Type",
            "Customer Name", "Employee Name", "Unit Description");

    ObservableList<String> cType = FXCollections.observableArrayList("RESERVATION", "DEPOSIT", "SAFEGUARD DUTY");
    private ObservableList<VehicleSalesApprovalTable> vhlApprovalData = FXCollections.observableArrayList();
    @FXML
    private Button btnApproved;
    @FXML
    private Button btnRefresh;
    @FXML
    private Button btnClose;
    @FXML
    private TextField txtFieldSearch;
    @FXML
    private ComboBox<String> comboFilter;
    @FXML
    private ComboBox<String> comboType;
    @FXML
    private DatePicker fromDate;
    @FXML
    private DatePicker toDate;
    @FXML
    private Label lFrom;
    @FXML
    private Label lTo;
    @FXML
    private Label ApprovedCount;
    @FXML
    private Label SelectedCount;
    @FXML
    private TableView<VehicleSalesApprovalTable> tblVhclApproval;
    @FXML
    private TableColumn<VehicleSalesApprovalTable, String> tblRow;
    @FXML
    private TableColumn<VehicleSalesApprovalTable, Boolean> tblselected;
    @FXML
    private TableColumn<VehicleSalesApprovalTable, String> tblindex03; //sTransNox
    @FXML
    private TableColumn<VehicleSalesApprovalTable, String> tblindex12; //cResrvTyp
    @FXML
    private TableColumn<VehicleSalesApprovalTable, LocalDate> tblindex02; //dTransact
    @FXML
    private TableColumn<VehicleSalesApprovalTable, String> tblindex20; //sCompnyNm
    @FXML
    private TableColumn<VehicleSalesApprovalTable, String> tblindex23; //sDescript
    @FXML
    private TableColumn<VehicleSalesApprovalTable, String> tblindex05; //nAmountxx
    @FXML
    private TableColumn<VehicleSalesApprovalTable, String> tblindex24; //sSeNamexx
    @FXML
    private TableColumn<VehicleSalesApprovalTable, String> tblbranch;
    @FXML
    private CheckBox selectAllCheckBox;
    @FXML
    private Button btnFilterType;
    @FXML
    private Button btnFilterDate;
    @FXML
    private Button btnFilterSlip;
    @FXML
    private Button btnFilterCustomer;
    @FXML
    private Button btnFilterUnit;
    @FXML
    private Button btnFilterEmployee;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTrans = new InquiryProcess(oApp, oApp.getBranchCode(), true); //Initialize ClientMaster
        oTrans.setCallback(oListener);
        oTrans.setWithUI(true);
        initOtherUtils();
        initCombo();
        loadVhlApprovalTable();
        //Buttons
        tblRow.setSortable(false);
        tblselected.setSortable(false);
        tblindex03.setSortable(false);
        tblindex20.setSortable(false);
        tblindex12.setSortable(false);
        tblindex23.setSortable(false);
        tblindex05.setSortable(false);
        tblindex24.setSortable(false);
        tblindex02.setSortable(false);
        tblbranch.setSortable(false);
        btnClose.setOnAction(this::cmdButton_Click);
        btnFilterSlip.setOnAction(this::cmdButton_Click);
        btnFilterType.setOnAction(this::cmdButton_Click);
        btnFilterDate.setOnAction(this::cmdButton_Click);
        btnFilterCustomer.setOnAction(this::cmdButton_Click);
        btnFilterUnit.setOnAction(this::cmdButton_Click);
        btnFilterEmployee.setOnAction(this::cmdButton_Click);
        btnApproved.setOnAction(this::cmdButton_Click);
        btnRefresh.setOnAction(this::cmdButton_Click);
        tblVhclApproval.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblVhclApproval.lookup("TableHeaderRow");
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
        btnFilterDate.setVisible(false);
        btnFilterDate.setManaged(false);
        btnFilterSlip.setVisible(false);
        btnFilterSlip.setManaged(false);
        btnFilterCustomer.setVisible(false);
        btnFilterCustomer.setManaged(false);
        btnFilterUnit.setVisible(false);
        btnFilterUnit.setManaged(false);
        btnFilterEmployee.setVisible(false);
        btnFilterEmployee.setManaged(false);
        btnFilterType.setVisible(false);
        btnFilterType.setManaged(false);
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
            btnFilterDate.setVisible(false);
            btnFilterDate.setManaged(false);
            btnFilterSlip.setVisible(false);
            btnFilterSlip.setManaged(false);
            btnFilterCustomer.setVisible(false);
            btnFilterCustomer.setManaged(false);
            btnFilterUnit.setVisible(false);
            btnFilterUnit.setManaged(false);
            btnFilterEmployee.setVisible(false);
            btnFilterEmployee.setManaged(false);
            btnFilterType.setVisible(false);
            btnFilterType.setManaged(false);
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
                case "Advances Slip No.":
                    txtFieldSearch.setText("");
                    txtFieldSearch.setVisible(true);
                    txtFieldSearch.setManaged(true);
                    btnFilterSlip.setVisible(true);
                    btnFilterSlip.setManaged(true);
                    tblVhclApproval.setItems(vhlApprovalData);

                    break;
                case "Advance Slip Date":
                    btnFilterDate.setVisible(true);
                    btnFilterDate.setManaged(true);
                    lFrom.setVisible(true);
                    lFrom.setManaged(true);

                    fromDate.setVisible(true);
                    fromDate.setManaged(true);
                    lTo.setVisible(true);
                    lTo.setManaged(true);
                    toDate.setVisible(true);
                    toDate.setManaged(true);
                    tblVhclApproval.setItems(vhlApprovalData);
                    break;
                case "Advances Type":
                    comboType.setVisible(true);
                    comboType.setManaged(true);
                    btnFilterType.setVisible(true);
                    btnFilterType.setManaged(true);
                    tblVhclApproval.setItems(vhlApprovalData);
                    break;
                case "Customer Name":
                    txtFieldSearch.setText("");
                    txtFieldSearch.setVisible(true);
                    txtFieldSearch.setManaged(true);
                    btnFilterCustomer.setVisible(true);
                    btnFilterCustomer.setManaged(true);
                    tblVhclApproval.setItems(vhlApprovalData);

                    break;
                case "Employee Name":
                    txtFieldSearch.setText("");
                    txtFieldSearch.setVisible(true);
                    txtFieldSearch.setManaged(true);
                    btnFilterEmployee.setVisible(true);
                    btnFilterEmployee.setManaged(true);
                    tblVhclApproval.setItems(vhlApprovalData);

                    break;
                case "Unit Description":
                    txtFieldSearch.setText("");
                    txtFieldSearch.setVisible(true);
                    txtFieldSearch.setManaged(true);
                    btnFilterUnit.setVisible(true);
                    btnFilterUnit.setManaged(true);
                    tblVhclApproval.setItems(vhlApprovalData);
                    break;
            }
        });
    }

    //populate data from table
    private void loadVhlApprovalTable() {
        try {
            vhlApprovalData.clear();
            if (oTrans.loadRsvForApproval()) {
                for (lnCtr = 1; lnCtr <= oTrans.getReserveCount(); lnCtr++) {
                    // Iterate over the data and count the approved item
                    String amountString = oTrans.getInqRsv(lnCtr, "nAmountxx").toString();
                    // Convert the amount to a decimal value
                    double amount = Double.parseDouble(amountString);
                    // Format the decimal value with decimal separators
                    DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
                    String formattedAmount = decimalFormat.format(amount);
                    String res = oTrans.getInqRsv(lnCtr, "cResrvTyp").toString();
                    if (res.equals("0")) {
                        res = "Reservation";
                    }
                    if (res.equals("1")) {
                        res = "Deposit";
                    }
                    if (res.equals("2")) {
                        res = "Safeguard Duty";
                    }
                    vhlApprovalData.add(new VehicleSalesApprovalTable(
                            String.valueOf(lnCtr),
                            oTrans.getInqRsv(lnCtr, "cTranStat").toString(),
                            oTrans.getInqRsv(lnCtr, "sTransNox").toString(),
                            oTrans.getInqRsv(lnCtr, "sReferNox").toString().toUpperCase(),
                            res.toUpperCase(),
                            CommonUtils.xsDateShort((Date) oTrans.getInqRsv(lnCtr, "dTransact")),
                            oTrans.getInqRsv(lnCtr, "sCompnyNm").toString().toUpperCase(),
                            oTrans.getInqRsv(lnCtr, "sDescript").toString().toUpperCase(),
                            formattedAmount,
                            oTrans.getInqRsv(lnCtr, "sSeNamexx").toString().toUpperCase(),
                            oTrans.getInqRsv(lnCtr, "sBranchNm").toString().toUpperCase()
                    ));
                }
                tblVhclApproval.setItems(vhlApprovalData);
                initVhlApprovalTable();
            }
        } catch (SQLException ex) {
            Logger.getLogger(VehicleSalesApprovalController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            case "btnFilterSlip": //btn filter for Slip No
                String filterSlip = txtFieldSearch.getText().trim().toLowerCase();
                // Initialize the filteredData variable
                FilteredList<VehicleSalesApprovalTable> filteredTxtFieldSlip = new FilteredList<>(vhlApprovalData);
                // Apply the filter predicate based on the entered text
                filteredTxtFieldSlip.setPredicate(clients -> {
                    if (filterSlip.isEmpty()) {
                        // No filter text entered, show all data
                        return true;
                    } else {
                        // Filter based on Slip No, Customer Name, Unit Description, and SE Name
                        String slipNo = clients.getTblindex03().toLowerCase();
                        return slipNo.contains(filterSlip);
                    }
                });
                tblVhclApproval.setItems(filteredTxtFieldSlip);
                if (filteredTxtFieldSlip.isEmpty()) {
                    ShowMessageFX.Information(null, pxeModuleName, "No record found!");
                }
                break;
            case "btnFilterCustomer": //btn filter for Customer Name
                String filterCustomer = txtFieldSearch.getText().trim().toLowerCase();
                // Initialize the filteredData variable
                FilteredList<VehicleSalesApprovalTable> filteredTxtFieldCustomer = new FilteredList<>(vhlApprovalData);
                // Apply the filter predicate based on the entered text
                filteredTxtFieldCustomer.setPredicate(clients -> {
                    if (filterCustomer.isEmpty()) {
                        // No filter text entered, show all data
                        return true;
                    } else {
                        String customerName = clients.getTblindex20().toLowerCase();
                        return customerName.contains(filterCustomer);
                    }
                });
                tblVhclApproval.setItems(filteredTxtFieldCustomer);
                if (filteredTxtFieldCustomer.isEmpty()) {
                    ShowMessageFX.Information(null, pxeModuleName, "No record found!");
                }
                break;
            case "btnFilterUnit": //btn filter for Unit Description
                String filterUnit = txtFieldSearch.getText().trim().toLowerCase();
                // Initialize the filteredData variable
                FilteredList<VehicleSalesApprovalTable> filteredTxtFieldUnit = new FilteredList<>(vhlApprovalData);
                // Apply the filter predicate based on the entered text
                filteredTxtFieldUnit.setPredicate(clients -> {
                    if (filterUnit.isEmpty()) {
                        // No filter text entered, show all data
                        return true;
                    } else {
                        String unitDescription = clients.getTblindex23().toLowerCase();
                        return unitDescription.contains(filterUnit);
                    }
                });
                tblVhclApproval.setItems(filteredTxtFieldUnit);
                if (filteredTxtFieldUnit.isEmpty()) {
                    ShowMessageFX.Information(null, pxeModuleName, "No record found!");
                }
                break;
            case "btnFilterEmployee": //btn filter for Employee Name
                String filterEmployee = txtFieldSearch.getText().trim().toLowerCase();
                // Initialize the filteredData variable
                FilteredList<VehicleSalesApprovalTable> filteredTxtFieldEmployee = new FilteredList<>(vhlApprovalData);
                // Apply the filter predicate based on the entered text
                filteredTxtFieldEmployee.setPredicate(clients -> {
                    if (filterEmployee.isEmpty()) {
                        // No filter text entered, show all data
                        return true;
                    } else {
                        String seName = clients.getTblindex24().toLowerCase();
                        return seName.contains(filterEmployee);
                    }
                });
                tblVhclApproval.setItems(filteredTxtFieldEmployee);
                if (filteredTxtFieldEmployee.isEmpty()) {
                    ShowMessageFX.Information(null, pxeModuleName, "No record found!");
                }
                break;
            case "btnFilterType": //btn filter for comboBox
                String selectedType = comboType.getValue();
                if (selectedType == null) {
                    // No type selected, show all data
                    tblVhclApproval.setItems(vhlApprovalData);
                } else {
                    // Filter data based on selected type
                    ObservableList<VehicleSalesApprovalTable> filteredCombo = FXCollections.observableArrayList();
                    for (VehicleSalesApprovalTable slipData : vhlApprovalData) {
                        if (slipData.getTblindex12().equals(selectedType)) {
                            filteredCombo.add(slipData);
                        }
                    }
                    tblVhclApproval.setItems(filteredCombo);
                    if (filteredCombo.isEmpty()) {
                        ShowMessageFX.Information(null, pxeModuleName, "No record found!");
                    }
                }
                break;
            case "btnFilterDate": //btn filter for Slip Date
                LocalDate filterFromDate = fromDate.getValue();
                LocalDate filterToDate = toDate.getValue();

                ObservableList<VehicleSalesApprovalTable> filteredDate = FXCollections.observableArrayList();
                for (VehicleSalesApprovalTable slipData : vhlApprovalData) {
                    LocalDate slipDate = LocalDate.parse(slipData.getTblindex02());

                    if (filterFromDate == null || slipDate.isAfter(filterFromDate.minusDays(1))) {
                        if (filterToDate == null || slipDate.isBefore(filterToDate.plusDays(1))) {
                            filteredDate.add(slipData);
                        }
                    }
                }

                tblVhclApproval.setItems(filteredDate);
                if (filteredDate.isEmpty()) {
                    ShowMessageFX.Information(null, pxeModuleName, "No record found!");
                }
                break;
            case "btnApproved": //btn for approval
                ObservableList<VehicleSalesApprovalTable> selectedItems = FXCollections.observableArrayList();
                for (VehicleSalesApprovalTable item : tblVhclApproval.getItems()) {
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
                        for (VehicleSalesApprovalTable item : selectedItems) {
                            String fsTransNox = item.getTblindex01();
                            String fsRow = item.getTblRow();
                            int fsRowInt = Integer.parseInt(fsRow);// Assuming there is a method to retrieve the transaction number
                            try {
                                boolean approved = oTrans.ApproveReservation(fsTransNox, fsRowInt);
                                if (approved) {
                                    i = i + 1;
                                    ApprovedCount.setText("" + i);
                                } else {
                                    // Handle approval failure
                                    ShowMessageFX.Error(null, pxeModuleName, "Failed to approve reservation.");
                                    loadVhlApprovalTable();
                                    selectAllCheckBox.setSelected(false);
                                    return;
                                }
                            } catch (SQLException e) {
                                // Handle SQL exception
                                ShowMessageFX.Error(null, pxeModuleName, "An error occurred while approving reservation: " + e.getMessage());
                            }
                        }
                        SelectedCount.setText("0");
                        ShowMessageFX.Information(null, pxeModuleName, "Reservation approved successfully.");
                        loadVhlApprovalTable();
                        selectAllCheckBox.setSelected(false);
                        tblVhclApproval.getItems().removeAll(selectedItems);
                        tblVhclApproval.refresh();
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
                loadVhlApprovalTable();
                SelectedCount.setText("0");
                ApprovedCount.setText("0");
                selectAllCheckBox.setSelected(false);
                break;
        }
    }

    private Stage getStage() {
        return (Stage) txtFieldSearch.getScene().getWindow();
    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private void updateSelectedCount() {
        ObservableList<VehicleSalesApprovalTable> selectedItems = tblVhclApproval.getItems().filtered(items -> items.getSelect().isSelected());
        int count = selectedItems.size();
        SelectedCount.setText("" + count);
    }

    private void initVhlApprovalTable() {
        tblRow.setCellValueFactory(new PropertyValueFactory<>("tblRow"));  //Row
        // Set up listener for "Select All" checkbox
        tblselected.setCellValueFactory(new PropertyValueFactory<>("select"));
        tblVhclApproval.getItems().forEach(item -> {
            CheckBox selectCheckBox = item.getSelect();
            selectCheckBox.setOnAction(event -> {
                updateSelectedCount();
                if (tblVhclApproval.getItems().stream().allMatch(tableItem -> tableItem.getSelect().isSelected())) {
                    selectAllCheckBox.setSelected(true);
                } else {
                    selectAllCheckBox.setSelected(false);
                }
            });
        });
        selectAllCheckBox.setOnAction(event -> {
            boolean newValue = selectAllCheckBox.isSelected();
            tblVhclApproval.getItems().forEach(item -> item.getSelect().setSelected(newValue));
            updateSelectedCount();
        });
        tblindex03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
        tblindex12.setCellValueFactory(new PropertyValueFactory<>("tblindex12"));
        tblindex02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
        tblindex20.setCellValueFactory(new PropertyValueFactory<>("tblindex20"));
        tblindex23.setCellValueFactory(new PropertyValueFactory<>("tblindex23"));
        tblindex05.setCellValueFactory(new PropertyValueFactory<>("tblindex05"));
        tblindex24.setCellValueFactory(new PropertyValueFactory<>("tblindex24"));
        tblbranch.setCellValueFactory(new PropertyValueFactory<>("tblbranch"));
    }
}
