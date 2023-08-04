/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.parts;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.auto.app.views.ScreenInterface;
import org.rmj.auto.parts.base.ItemEntry;

/**
 * FXML Controller class
 *
 * @author Arsiela to be continued by John Dave 08-01-2023 Date Created:
 * 07-03-2023
 */
public class ItemEntryModelController implements Initializable, ScreenInterface {

    private GRider oApp;
    private MasterCallback oListener;
//    private ItemEntry oTrans;
    private int pnEditMode;
    private final String pxeModuleName = "Item Entry Model";

    @FXML
    private Button btnAdd;
    @FXML
    private Button btnClose;
    @FXML
    private TableView<ItemEntryModelTable> tblVModelList;
    @FXML
    private TableColumn<ItemEntryModelTable, String> tblIndex01;
    @FXML
    private TableColumn<ItemEntryModelTable, String> tblIndex02;
    @FXML
    private TableColumn<ItemEntryModelTable, String> tblIndex04;
    @FXML
    private TableView<ItemEntryModelTable> tblVYear;
    @FXML
    private TableColumn<ItemEntryModelTable, String> tblIndex02_yr;
    @FXML
    private TableColumn tblIndex03_yr;
    @FXML
    private CheckBox selectModelAll;
    @FXML
    private CheckBox selectYearAll;
    @FXML
    private TextField txtSeeks01;
    @FXML
    private TextField txtSeeks02;
    @FXML
    private ComboBox<String> comboFilter;
    ObservableList<String> cItems = FXCollections.observableArrayList("MAKE", "MODEL");
    @FXML
    private TableColumn<ItemEntryModelTable, String> tblindexRow;
    @FXML
    private TableColumn<ItemEntryModelTable, String> tblindexRowYear;
    @FXML
    private Button btnFilterMake;
    @FXML
    private Button btnFilterModel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oListener = (int fnIndex, Object foValue) -> {
            System.out.println("Set Class Value " + fnIndex + "-->" + foValue);
        };
//        oTrans = new PartsMeasure(oApp, oApp.getBranchCode(), true);

//        oTrans.setCallback(oListener);
//        oTrans.setWithUI(true);
        comboFilter.setItems(cItems);
        btnFilterMake.setOnAction(this::cmdButton_Click);
        btnFilterModel.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click);
        btnAdd.setOnAction(this::cmdButton_Click);
//        loadItemModelTable();
    }

    private void cmdButton_Click(ActionEvent event) {

        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            case "btnAdd":
//                ObservableList<ActivityMemberTable> selectedItems = FXCollections.observableArrayList();
//                for (ActivityMemberTable item : tblViewEmployee.getItems()) {
//                    if (item.getSelect().isSelected()) {
//                        selectedItems.add(item);
//                    }
//                }
//                if (selectedItems.isEmpty()) {
//                    ShowMessageFX.Information(null, pxeModuleName, "No items selected to add.");
//                } else {
//                    int i = 0;
//                    int lnfind = 0;
//
//                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to add?")) {
//                        // Call the addTown here
//                        for (ActivityMemberTable item : selectedItems) {
//
//                            String fsEmployID = item.getTblindexMem13();
//                            String fsEmpName = item.getTblindexMem25();
//                            String fsDept = item.getTblindexMem24();
//                            try {
//                                boolean fsEmp = false;
//                                for (int lnCtr = 1; lnCtr <= oTrans.getActMemberCount(); lnCtr++) {
//                                    if (oTrans.getActMember(lnCtr, "sCompnyNm").toString().equals(fsEmpName)
//                                            && oTrans.getActMember(lnCtr, "cOriginal").toString().equals("1")) {
//                                        ShowMessageFX.Error(null, pxeModuleName, "Skipping, Failed to add Employee, " + fsEmpName + " already exist.");
//                                        fsEmp = true;
//                                        break;
//                                    }
//                                }
//                                if (!fsEmp) {
//                                    lnfind++;
//                                    System.out.println(lnfind);
//                                    boolean add = oTrans.addMember(fsEmployID, fsEmpName, fsDept);
//                                }
//                            } catch (SQLException e) {
//                                // Handle SQL exception
//                                ShowMessageFX.Error(null, pxeModuleName, "An error occurred while adding employee: " + e.getMessage());
//                            }
//                        }
//                        if (lnfind >= 1) {
//                            ShowMessageFX.Information(null, pxeModuleName, "Added Employee successfully.");
//                        } else {
//                            ShowMessageFX.Error(null, pxeModuleName, "Failed to add employee");
//                        }
//                        CommonUtils.closeStage(btnAdd);
//                    }
//
//                }
                break;
            case "btnFilterMake": //btn filter for Slip No
//                String filterSlip = txtFieldSearch.getText().trim().toLowerCase();
//                // Initialize the filteredData variable
//                FilteredList<VehicleSalesApprovalTable> filteredTxtFieldSlip = new FilteredList<>(vhlApprovalData);
//                // Apply the filter predicate based on the entered text
//                filteredTxtFieldSlip.setPredicate(clients -> {
//                    if (filterSlip.isEmpty()) {
//                        // No filter text entered, show all data
//                        return true;
//                    } else {
//                        // Filter based on Slip No, Customer Name, Unit Description, and SE Name
//                        String slipNo = clients.getTblindex03().toLowerCase();
//                        return slipNo.contains(filterSlip);
//                    }
//                });
//                tblVhclApproval.setItems(filteredTxtFieldSlip);
//                if (filteredTxtFieldSlip.isEmpty()) {
//                    ShowMessageFX.Information(null, pxeModuleName, "No record found!");
//                }
                break;
            case "btnFilterModel": //btn filter for Slip No
//                String filterSlip = txtFieldSearch.getText().trim().toLowerCase();
//                // Initialize the filteredData variable
//                FilteredList<VehicleSalesApprovalTable> filteredTxtFieldSlip = new FilteredList<>(vhlApprovalData);
//                // Apply the filter predicate based on the entered text
//                filteredTxtFieldSlip.setPredicate(clients -> {
//                    if (filterSlip.isEmpty()) {
//                        // No filter text entered, show all data
//                        return true;
//                    } else {
//                        // Filter based on Slip No, Customer Name, Unit Description, and SE Name
//                        String slipNo = clients.getTblindex03().toLowerCase();
//                        return slipNo.contains(filterSlip);
//                    }
//                });
//                tblVhclApproval.setItems(filteredTxtFieldSlip);
//                if (filteredTxtFieldSlip.isEmpty()) {
//                    ShowMessageFX.Information(null, pxeModuleName, "No record found!");
//                }
                break;
        }
    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private Stage getStage() {
        return (Stage) txtSeeks01.getScene().getWindow();
    }
//
//    public void setObject(EntryModel foValue) {
////        oTrans = foValue;
//    }

    private void loadItemModelTable() {
        initItemModelTable();
    }

    private void initItemModelTable() {

    }

    private void loadItemModelYearTable() {
        initItemModelYearTable();
    }

    private void initItemModelYearTable() {

    }

}
