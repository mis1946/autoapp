/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.auto.app.views;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.auto.app.sales.VehicleSalesApprovalTable;
import org.rmj.auto.clients.base.Activity;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class ActivityMemberEntryDialogController implements Initializable, ScreenInterface {

    private GRider oApp;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnClose;
    private Activity oTrans;
    private ObservableList<ActivityMemberTable> Employeedata = FXCollections.observableArrayList();
    private FilteredList<ActivityMemberTable> filteredData;
    private ObservableList<ActivityMemberTable> Departdata = FXCollections.observableArrayList();
    unloadForm unload = new unloadForm(); //Used in Close Button
    private final String pxeModuleName = "ActivityMemberEntryDialogController"; //Form Title
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private TableView<ActivityMemberTable> tblViewDepart;
    @FXML
    private TableView<ActivityMemberTable> tblViewEmployee;
    @FXML
    private TableColumn<ActivityMemberTable, String> tblindex24;
    @FXML
    private TableColumn<ActivityMemberTable, String> tblindexRow;
    @FXML
    private TableColumn<ActivityMemberTable, Boolean> tblselect;
    @FXML
    private TableColumn<ActivityMemberTable, String> tblindex25;
    @FXML
    private CheckBox selectAll;
    @FXML
    private Label SelectedCount;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        btnClose.setOnAction(this::cmdButton_Click);
        btnAdd.setOnAction(this::cmdButton_Click);
        loadDepartTable();
        tblViewDepart.setOnMouseClicked(event -> {
            ActivityMemberTable selectedDepartment = tblViewDepart.getSelectionModel().getSelectedItem();
            if (selectedDepartment != null) {
                String departmentID = selectedDepartment.getTblindex14();
                loadEmployeeTable(departmentID);
            }
        });

    }

    private Stage getStage() {
        return (Stage) tblViewDepart.getScene().getWindow();
    }

    public void setObject(Activity foValue) {
        oTrans = foValue;
    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private void cmdButton_Click(ActionEvent event) {

        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            case "btnAdd":
                ObservableList<ActivityMemberTable> selectedItems = tblViewEmployee.getSelectionModel().getSelectedItems();
                if (selectedItems.isEmpty()) {
                    ShowMessageFX.Information(null, pxeModuleName, "No items selected to add");
                } else {
                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to add Employee?")) {
                        int i = 0;
                        for (ActivityMemberTable item : selectedItems) {
                            String fsEmpId = item.getTblindex14();
                            String fsEmpName = item.getTblindex25();
                            String fsDeptName = item.getTblindex24();
                            try {
                                boolean add = oTrans.addMember(fsEmpId, fsEmpName, fsDeptName);
                                if (add) {
                                    i++;
                                } else {
                                    // Handle approval failure
                                    ShowMessageFX.Error(null, pxeModuleName, "Failed to add Employee.");
                                }
                            } catch (SQLException e) {
                                // Handle SQL exception
                                ShowMessageFX.Error(null, pxeModuleName, "An error occurred while adding Employee: " + e.getMessage());
                            }
                        }

                        tblViewEmployee.getSelectionModel().clearSelection();
                        SelectedCount.setText("0");
                        ShowMessageFX.Information(null, pxeModuleName, i + " Employee(s) added successfully.");
                        tblViewEmployee.refresh();
                    }
                }
                break;
        }
    }

    //storing values on bankentrydata
    private void loadEmployeeTable(String departmentID) {
        try {
            Employeedata.clear(); // Clear the previous data in the list

            // Load employee data using oTrans.loadEmployee() method
            // Pass the necessary parameters for loading employee data
            String fsValue = departmentID; // Use the selected department's ID as fsValue
            boolean fbLoadEmp = true; // Set the fbLoadEmp to true to load employees
            if (oTrans.loadEmployee(fsValue, fbLoadEmp)) {
                for (int lnCtr = 1; lnCtr <= oTrans.getEmpCount(); lnCtr++) {
                    Employeedata.add(new ActivityMemberTable(
                            String.valueOf(lnCtr), // ROW
                            "", // Replace with the appropriate value for the second column
                            "", // Replace with the appropriate value for the third column
                            "", // Replace with the appropriate value for the fourth column
                            oTrans.getEmployee(lnCtr, "sCompnyNm").toString(), // Fifth column (Department)
                            oTrans.getEmployee(lnCtr, "sEmployID").toString() // Replace with the appropriate value for the sixth column
                    ));
                }

                // Set the Employeedata list as the items for tblViewEmployee TableView
                tblViewEmployee.setItems(Employeedata);

                // Call the initEmployeeTable() method to initialize the TableView columns if needed
                initEmployeeTable();
            }
        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }
    }

    private void updateSelectedCount() {
        ObservableList<ActivityMemberTable> selectedItems = tblViewEmployee.getItems().filtered(items -> items.getSelect().isSelected());
        int count = selectedItems.size();
        SelectedCount.setText("" + count);
    }

    private void initEmployeeTable() {
        tblindexRow.setCellValueFactory(new PropertyValueFactory<>("tblindexRow"));  //Row
        tblselect.setCellValueFactory(new PropertyValueFactory<>("select"));

        tblViewEmployee.getItems().forEach(item -> {
            CheckBox selectCheckBox = item.getSelect();
            selectCheckBox.setOnAction(event -> {
                updateSelectedCount();
                if (tblViewEmployee.getItems().stream().allMatch(tableItem -> tableItem.getSelect().isSelected())) {
                    selectAll.setSelected(true);
                } else {
                    selectAll.setSelected(false);
                }
            });
        });
        selectAll.setOnAction(event -> {
            boolean newValue = selectAll.isSelected();
            tblViewEmployee.getItems().forEach(item -> item.getSelect().setSelected(newValue));
            updateSelectedCount();
        });
        tblindex25.setCellValueFactory(new PropertyValueFactory<>("tblindex25"));
    }

    //storing values on bankentrydata
    private void loadDepartTable() {
        try {
            /*Populate table*/
            Departdata.clear();

            if (oTrans.loadDepartment()) {
                for (int lnCtr = 1; lnCtr <= oTrans.getDeptCount(); lnCtr++) {
                    Departdata.add(new ActivityMemberTable(
                            String.valueOf(lnCtr), //ROW
                            "",
                            oTrans.getDepartment(lnCtr, "sDeptName").toString(),
                            oTrans.getDepartment(lnCtr, "sDeptIDxx").toString(),
                            "",
                            ""
                    ));
                }
                tblViewDepart.setItems(Departdata);
                initDepartTable();
            }
        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }
    }

    private void initDepartTable() {

        tblindex24.setCellValueFactory(new PropertyValueFactory<>("tblindex24"));
    }

}
