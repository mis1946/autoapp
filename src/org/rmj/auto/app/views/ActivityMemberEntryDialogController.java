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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
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
    private ObservableList<ActivityMemberTable> Departdata = FXCollections.observableArrayList();
    unloadForm unload = new unloadForm(); //Used in Close Button
    private final String pxeModuleName = "Activity Member Entry"; //Form Title
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
    private CheckBox selectAll;
    @FXML
    private Label SelectedCount;
    @FXML
    private CheckBox selectAllEmployee;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        btnClose.setOnAction(this::cmdButton_Click);
        btnAdd.setOnAction(this::cmdButton_Click);
        loadDepartTable();

        initTablePropert();
        tblViewDepart.setOnMouseClicked(event -> {
            ActivityMemberTable selectedDepartment = tblViewDepart.getSelectionModel().getSelectedItem();
            if (selectedDepartment != null) {
                String departmentID = selectedDepartment.getTblindexMem14();

                loadEmployeeTable(departmentID);
            }
        });

    }

    private void initTablePropert() {
        tblViewDepart.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewDepart.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        tblViewEmployee.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewEmployee.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
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
                ObservableList<ActivityMemberTable> selectedItems = FXCollections.observableArrayList();
                for (ActivityMemberTable item : tblViewEmployee.getItems()) {
                    if (item.getSelect().isSelected()) {
                        selectedItems.add(item);
                    }
                }
                if (selectedItems.isEmpty()) {
                    ShowMessageFX.Information(null, pxeModuleName, "No items selected to add.");
                } else {
                    int i = 0;
                    int lnfind = 0;

                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to add?")) {
                        // Call the addTown here
                        for (ActivityMemberTable item : selectedItems) {

                            String fsEmployID = item.getTblindexMem13();
                            String fsEmpName = item.getTblindexMem25();
                            String fsDept = item.getTblindexMem24();
                            try {
                                boolean fsEmp = false;
                                for (int lnCtr = 1; lnCtr <= oTrans.getActMemberCount(); lnCtr++) {
                                    if (oTrans.getActMember(lnCtr, "sCompnyNm").toString().equals(fsEmpName)
                                            && oTrans.getActMember(lnCtr, "cOriginal").toString().equals("1")) {
                                        ShowMessageFX.Error(null, pxeModuleName, "Skipping, Failed to add Employee, " + fsEmpName + " already exist.");
                                        fsEmp = true;
                                        break;
                                    }
                                }
                                if (!fsEmp) {
                                    lnfind++;
                                    System.out.println(lnfind);
                                    boolean add = oTrans.addMember(fsEmployID, fsEmpName, fsDept);
                                }
                            } catch (SQLException e) {
                                // Handle SQL exception
                                ShowMessageFX.Error(null, pxeModuleName, "An error occurred while adding employee: " + e.getMessage());
                            }
                        }
                        if (lnfind >= 1) {
                            ShowMessageFX.Information(null, pxeModuleName, "Added Employee successfully.");
                        } else {
                            ShowMessageFX.Error(null, pxeModuleName, "Failed to add employee");
                        }
                        CommonUtils.closeStage(btnAdd);
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
                            oTrans.getEmployee(lnCtr, "sDeptName").toString(),
                            oTrans.getEmployee(lnCtr, "sDeptIDxx").toString(),
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
                    selectAllEmployee.setSelected(true);
                } else {
                    selectAllEmployee.setSelected(false);
                }
            });
        });
        selectAllEmployee.setOnAction(event -> {
            boolean newValue = selectAllEmployee.isSelected();
            tblViewEmployee.getItems().forEach(item -> item.getSelect().setSelected(newValue));
            updateSelectedCount();
        });
        tblindex25.setCellValueFactory(new PropertyValueFactory<>("tblindexMem25"));
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

        tblindex24.setCellValueFactory(new PropertyValueFactory<>("tblindexMem24"));
    }

}
