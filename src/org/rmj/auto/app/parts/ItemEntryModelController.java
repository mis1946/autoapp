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
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private ItemEntry oTrans;
    private int pnEditMode;
    private final String pxeModuleName = "Item Entry Model";

    @FXML
    private Button btnAdd;
    @FXML
    private Button btnClose;
    @FXML
    private TableView<ItemEntryModelTable> tblVModelList;
    @FXML
    private TableView<ItemEntryModelTable> tblVYear;
    @FXML
    private TableColumn<ItemEntryModelTable, String> tblIndex03_yr;
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
    private ObservableList<ItemEntryModelTable> itemModeldata = FXCollections.observableArrayList();
    private ObservableList<ItemEntryModelTable> itemModelYear = FXCollections.observableArrayList();
    @FXML
    private TableColumn<ItemEntryModelTable, String> tblindexRow;
    @FXML
    private TableColumn<ItemEntryModelTable, String> tblindexRowYear;
    @FXML
    private Button btnFilterMake;
    @FXML
    private Button btnFilterModel;
    @FXML
    private TableColumn tblIndex06_mdl;
    @FXML
    private TableColumn tblIndex07_mdl;
    @FXML
    private TableColumn<ItemEntryModelTable, Boolean> tblindexselectModel;
    @FXML
    private TableColumn<ItemEntryModelTable, Boolean> tblindexselectYear;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oListener = (int fnIndex, Object foValue) -> {
            System.out.println("Set Class Value " + fnIndex + "-->" + foValue);
        };
        oTrans = new ItemEntry(oApp, oApp.getBranchCode(), true);;
        oTrans.setCallback(oListener);
        oTrans.setWithUI(true);
        comboFilter.setItems(cItems);
        btnFilterMake.setOnAction(this::cmdButton_Click);
        btnFilterModel.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click);
        btnAdd.setOnAction(this::cmdButton_Click);
        loadItemModelTable();
        loadItemModelYearTable();
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
                String filterMake = txtSeeks01.getText().trim().toLowerCase();
                FilteredList<ItemEntryModelTable> filteredTxtFieldMake = new FilteredList<>(itemModeldata);
                filteredTxtFieldMake.setPredicate(clients -> {
                    if (filterMake.isEmpty()) {
                        return true;
                    } else {
                        String make = clients.getTblIndex06_mdl().toLowerCase();
                        return make.contains(filterMake);
                    }
                });
                tblVModelList.setItems(filteredTxtFieldMake);
                if (filteredTxtFieldMake.isEmpty()) {
                    ShowMessageFX.Information(null, pxeModuleName, "No record found!");
                }
                break;
            case "btnFilterModel": //btn filter for Slip No
                String filterModel = txtSeeks02.getText().trim().toLowerCase();
                FilteredList<ItemEntryModelTable> filteredTxtFieldModel = new FilteredList<>(itemModeldata);
                filteredTxtFieldModel.setPredicate(clients -> {
                    if (filterModel.isEmpty()) {
                        return true;
                    } else {
                        String make = clients.getTblIndex06_mdl().toLowerCase();
                        return make.contains(filterModel);
                    }
                });
                tblVModelList.setItems(filteredTxtFieldModel);
                if (filteredTxtFieldModel.isEmpty()) {
                    ShowMessageFX.Information(null, pxeModuleName, "No record found!");
                }
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

    public void setObject(ItemEntry foValue) {
        oTrans = foValue;
    }

    private void loadItemModelTable() {
        try {
            itemModeldata.clear(); // Clear the previous data in the list
            if (oTrans.loadInvModel("", false)) {
                for (int lnCtr = 1; lnCtr <= oTrans.getVhclModelCount(); lnCtr++) {

                    System.out.println(oTrans.getVhclModel(lnCtr, "sModelIDx").toString());
                    System.out.println(oTrans.getVhclModel(lnCtr, "sMakeDesc").toString());
                    System.out.println(oTrans.getVhclModel(lnCtr, "sModelDsc").toString());
                    itemModeldata.add(new ItemEntryModelTable(
                            String.valueOf(lnCtr), // ROW
                            "",
                            //                            oTrans.getVhclModel(lnCtr, "sStockIDx").toString(),
                            oTrans.getVhclModel(lnCtr, "sModelIDx").toString(),
                            oTrans.getVhclModel(lnCtr, "sMakeDesc").toString(),
                            oTrans.getVhclModel(lnCtr, "sModelDsc").toString(),
                            ""
                    )
                    );
                }

                // Set the Employeedata list as the items for tblViewEmployee TableView
                tblVModelList.setItems(itemModeldata);

                // Call the initEmployeeTable() method to initialize the TableView columns if needed
                initItemModelTable();
            }
        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }
    }

    private void initItemModelTable() {
        tblindexRow.setCellValueFactory(new PropertyValueFactory<>("tblindexRow"));  //Row
        tblIndex06_mdl.setCellValueFactory(new PropertyValueFactory<>("tblIndex06_mdl"));
        tblIndex07_mdl.setCellValueFactory(new PropertyValueFactory<>("tblIndex07_mdl"));
        // Set up listener for "Select All" checkbox
        tblindexselectModel.setCellValueFactory(new PropertyValueFactory<>("select"));
        tblVModelList.getItems().forEach(item -> {
            CheckBox selectModelAll = item.getSelect();
            selectModelAll.setOnAction(event -> {
                if (tblVModelList.getItems().stream().allMatch(tableItem -> tableItem.getSelect().isSelected())) {
                    selectModelAll.setSelected(true);
                } else {
                    selectModelAll.setSelected(false);
                }
            });
        });
        selectModelAll.setOnAction(event -> {
            boolean newValue = selectModelAll.isSelected();
            tblVModelList.getItems().forEach(item -> item.getSelect().setSelected(newValue));
        });

    }

    private void loadItemModelYearTable() {
        try {
            itemModelYear.clear();
            if (oTrans.loadInvModelYr("", false)) {
                for (int lnCtr = 1; lnCtr <= oTrans.getVhclModelYrCount(); lnCtr++) {
                    itemModelYear.add(new ItemEntryModelTable(
                            "",
                            "",
                            "",
                            "",
                            "",
                            String.valueOf((Integer) oTrans.getVhclModelYr(lnCtr, "nYearModl"))
                    ));
                }

                // Set the Employeedata list as the items for tblViewEmployee TableView
                tblVYear.setItems(itemModelYear);

                // Call the initEmployeeTable() method to initialize the TableView columns if needed
                initItemModelYearTable();
            }
        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }
    }

    private void initItemModelYearTable() {
        // Set up listener for "Select All" checkbox
        tblindexselectYear.setCellValueFactory(new PropertyValueFactory<>("select"));
        tblVYear.getItems().forEach(item -> {
            CheckBox selectYearAll = item.getSelect();
            selectYearAll.setOnAction(event -> {
                if (tblVYear.getItems().stream().allMatch(tableItem -> tableItem.getSelect().isSelected())) {
                    selectYearAll.setSelected(true);
                } else {
                    selectYearAll.setSelected(false);
                }
            });
        });
        selectYearAll.setOnAction(event -> {
            boolean newValue = selectYearAll.isSelected();
            tblVYear.getItems().forEach(item -> item.getSelect().setSelected(newValue));
        });
        tblIndex03_yr.setCellValueFactory(new PropertyValueFactory<>("tblIndex03_yr"));
    }

}
