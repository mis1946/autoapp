/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.parts;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
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
    private Button btnFilterMake;
    @FXML
    private Button btnFilterModel;
    @FXML
    private TableColumn<ItemEntryModelTable, String> tblIndex06_mdl;
    @FXML
    private TableColumn<ItemEntryModelTable, String> tblIndex07_mdl;
    @FXML
    private TableColumn<ItemEntryModelTable, Boolean> tblindexselectModel;
    @FXML
    private TableColumn<ItemEntryModelTable, Boolean> tblindexselectYear;
    @FXML
    private CheckBox chckNoYear;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        comboFilter.setItems(cItems);

        txtSeeks01.setOnKeyPressed(this::txtField_KeyPressed);
        txtSeeks02.setOnKeyPressed(this::txtField_KeyPressed);

        btnClose.setOnAction(this::cmdButton_Click);
        btnAdd.setOnAction(this::cmdButton_Click);
        btnFilterMake.setOnAction(this::cmdButton_Click);
        btnFilterModel.setOnAction(this::cmdButton_Click);

        CheckNoYear();
        loadItemModelTable();
        loadItemModelYearTable();
        initCombo();
    }

    private void CheckNoYear() {
        chckNoYear.setOnAction(event -> {
            if (chckNoYear.isSelected()) {
                tblVYear.setDisable(true);

            } else {
                tblVYear.setDisable(false);
            }
        });
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

    private void txtField_KeyPressed(KeyEvent event) {
        String txtFieldID = ((TextField) event.getSource()).getId();

        switch (event.getCode()) {
            case ENTER:
                switch (txtFieldID) {
                    case "txtSeeks01":
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
                    case "txtSeeks02":
                        String filterModel = txtSeeks02.getText().trim().toLowerCase();
                        FilteredList<ItemEntryModelTable> filteredTxtFieldModel = new FilteredList<>(itemModeldata);
                        filteredTxtFieldModel.setPredicate(clients -> {
                            if (filterModel.isEmpty()) {
                                return true;
                            } else {
                                String make = clients.getTblIndex07_mdl().toLowerCase();
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
    }

    private void cmdButton_Click(ActionEvent event) {

        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            case "btnAdd":
                ObservableList<ItemEntryModelTable> selectedItemsModel = FXCollections.observableArrayList();
                ObservableList<ItemEntryModelTable> selectedItemsYear = FXCollections.observableArrayList();

                // Check if any items are selected in tblVModelList
                boolean isAnyModelItemSelected = false;
                boolean isAnyYearItemSelected = false;
                for (ItemEntryModelTable item : tblVModelList.getItems()) {
                    if (item.getSelect().isSelected()) {
                        selectedItemsModel.add(item);
                        isAnyModelItemSelected = true;
                    }
                }

                for (ItemEntryModelTable item : tblVYear.getItems()) {
                    if (item.getSelect().isSelected()) {
                        selectedItemsYear.add(item);
                        isAnyYearItemSelected = true;
                    }
                }

                if (!isAnyModelItemSelected && !isAnyYearItemSelected) {
                    ShowMessageFX.Information(null, pxeModuleName, "No items selected to add.");
                } else if (!chckNoYear.isSelected() && !isAnyYearItemSelected) {
                    ShowMessageFX.Information(null, pxeModuleName, "Please either check the \"No Year Model\" checkbox or select items in the table for model years.");
                } else {
                    int lnfind = 0;
                    boolean add = false;
                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to add?")) {
                        if (chckNoYear.isSelected()) {
                            for (ItemEntryModelTable item : selectedItemsModel) {
                                String fsMakeDesc = item.getTblIndex06_mdl();
                                String fsModelDesc = item.getTblIndex07_mdl();
                                String fsModelCode = item.getTblindex02();
                                add = oTrans.addInvModel_Year(fsModelCode, fsModelDesc, fsMakeDesc, 0, true);
                                if (!add) {
                                    ShowMessageFX.Error(null, pxeModuleName, oTrans.getMessage());
                                    if (oTrans.getMessage().equals("You cannot add other vehicle model")) {
                                        return;
                                    }
                                } else {
                                    lnfind++;
                                }

                            }

                        } else {
                            //Inv Model Year
                            for (ItemEntryModelTable item : selectedItemsModel) {

                                String fsMakeDesc = item.getTblIndex06_mdl();
                                String fsModelDesc = item.getTblIndex07_mdl();
                                String fsModelCode = item.getTblindex02();

                                for (ItemEntryModelTable items : selectedItemsYear) {
                                    String Year = items.getTblIndex03_yr();

                                    try {
                                        int yearAsInt = Integer.parseInt(Year);
                                        add = oTrans.addInvModel_Year(fsModelCode, fsModelDesc, fsMakeDesc, yearAsInt, false);
                                        if (!add) {
                                            ShowMessageFX.Error(null, pxeModuleName, oTrans.getMessage());
                                            if (oTrans.getMessage().equals("You cannot add other vehicle model")) {
                                                return;
                                            }
                                        } else {
                                            lnfind++;
                                        }

                                    } catch (NumberFormatException e) {
                                        // Handle the case where Year is not a valid integer
                                        System.out.println("Invalid year value: " + Year);
                                        // You might want to log or display an error message here
                                    }
                                }

                            }
                        }
                        if (add || lnfind >= 1) {
                            ShowMessageFX.Information(null, pxeModuleName, "Added Vehicle Model successfully.");
                            CommonUtils.closeStage(btnAdd);
                        } else {
                            ShowMessageFX.Error(null, pxeModuleName, "Failed to add vehicle model");
                        }

                    }
                }
                break;

            case "btnFilterMake":
                System.out.println("IM MAKE");
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
            case "btnFilterModel":
                System.out.println("IM MODEL");
                String filterModel = txtSeeks02.getText().trim().toLowerCase();
                FilteredList<ItemEntryModelTable> filteredTxtFieldModel = new FilteredList<>(itemModeldata);
                filteredTxtFieldModel.setPredicate(clients -> {
                    if (filterModel.isEmpty()) {
                        return true;
                    } else {
                        String make = clients.getTblIndex07_mdl().toLowerCase();
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

    private void loadItemModelTable() {
        try {
            itemModeldata.clear(); // Clear the previous data in the list
            if (oTrans.loadVhclModel()) {
                for (int lnCtr = 1; lnCtr <= oTrans.getVhclModelCount(); lnCtr++) {
                    itemModeldata.add(new ItemEntryModelTable(
                            String.valueOf(lnCtr), // ROW
                            "",
                            oTrans.getVhclModel(lnCtr, "sModelIDx").toString(),
                            oTrans.getVhclModel(lnCtr, "sMakeDesc").toString(),
                            oTrans.getVhclModel(lnCtr, "sModelDsc").toString(),
                            "",
                            ""
                    )
                    );
                }

                tblVModelList.setItems(itemModeldata);

                initItemModelTable();
            }
        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }
    }

    @SuppressWarnings("unchecked")
    private void initItemModelTable() {

        tblIndex06_mdl.setCellValueFactory(new PropertyValueFactory<>("tblIndex06_mdl"));
        tblIndex07_mdl.setCellValueFactory(new PropertyValueFactory<>("tblIndex07_mdl"));
        // Set up listener for "Select All" checkbox
        tblindexselectModel.setCellValueFactory(new PropertyValueFactory<>("select"));

        tblVModelList.getItems().forEach(new Consumer<ItemEntryModelTable>() {
            @Override
            public void accept(ItemEntryModelTable item) {
                CheckBox selectCheckBoxModel = item.getSelect();
                selectCheckBoxModel.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        // Check if any checkbox is selected
                        boolean anySelected = false;
                        for (ItemEntryModelTable itemModel : tblVModelList.getItems()) {
                            if (itemModel.getSelect().isSelected()) {
                                if (itemModel.getTblIndex07_mdl().equals("COMMON")) {
                                    tblVYear.setDisable(true);
                                    chckNoYear.setSelected(true);
                                    chckNoYear.setVisible(false);

                                    for (ItemEntryModelTable itemModel2 : tblVModelList.getItems()) {
                                        if (itemModel2.getSelect().isSelected()) {
                                            if (!itemModel2.getTblIndex07_mdl().equals("COMMON")) {
                                                itemModel2.getSelect().setSelected(false);
                                            }
                                        }
                                    }
                                } else {
                                    tblVYear.setDisable(false);
                                    chckNoYear.setSelected(false);
                                    chckNoYear.setVisible(true);
                                    anySelected = true;
                                }
                            }
                        }

                        if (!chckNoYear.isSelected()) {
                            tblVYear.setDisable(false);
                            chckNoYear.setVisible(true);
                            chckNoYear.setSelected(false);
                        }

                        // Check if all checkboxes are selected and update selectModelAll accordingly
                        boolean allSelected = tblVModelList.getItems().stream()
                                .allMatch(new Predicate<ItemEntryModelTable>() {
                                    @Override
                                    public boolean test(ItemEntryModelTable tableItem) {
                                        return tableItem.getSelect().isSelected();
                                    }
                                });
                        selectModelAll.setSelected(allSelected);
                        tblVYear.setDisable(!anySelected);
                        chckNoYear.setVisible(anySelected);
                        chckNoYear.setSelected(!anySelected);

                    }
                });
            }
        });

        selectModelAll.setOnAction(event -> {
            boolean selectAll = selectModelAll.isSelected();

            // Check/uncheck all items' checkboxes
            tblVModelList.getItems().forEach(new Consumer<ItemEntryModelTable>() {
                @Override
                public void accept(ItemEntryModelTable item) {
                    if (item.getTblIndex07_mdl().equals("COMMON")) {
                        item.getSelect().setSelected(false);
                        chckNoYear.setVisible(true);
                    } else {
                        item.getSelect().setSelected(selectAll);
                    }

                }
            });

            // Enable/disable tableViewYear and unselect checkboxNoYear accordingly
            if (selectAll) {
                tblVYear.setDisable(false);
                chckNoYear.setVisible(true);
                chckNoYear.setSelected(false);
            } else {
                tblVYear.setDisable(true);
                chckNoYear.setVisible(false);
                chckNoYear.setSelected(true);
            }
        });
    }

    private void loadItemModelYearTable() {
        try {
            itemModelYear.clear();
            if (oTrans.loadVhclModelYr()) {
                for (int lnCtr = 1; lnCtr <= oTrans.getVhclModelYrCount(); lnCtr++) {
                    itemModelYear.add(new ItemEntryModelTable(
                            String.valueOf(lnCtr),
                            "",
                            "",
                            "",
                            "",
                            String.valueOf((Integer) oTrans.getVhclModelYr(lnCtr, "nYearModl")),
                            ""
                    ));
                }

                tblVYear.setItems(itemModelYear);
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
            CheckBox selectCheckBoxYear = item.getSelect();
            selectCheckBoxYear.setOnAction(event -> {
                if (tblVYear.isDisabled()) {
                    selectCheckBoxYear.setSelected(false); // Unselect the checkbox
                } else if (tblVYear.getItems().stream().allMatch(tableItem -> tableItem.getSelect().isSelected())) {
                    selectYearAll.setSelected(true);
                } else {
                    selectYearAll.setSelected(false);
                }
            });
        });
        selectYearAll.setOnAction(event -> {
            boolean selectedAll = selectYearAll.isSelected();
            tblVYear.getItems().forEach(item -> item.getSelect().setSelected(selectedAll));
        });

        tblVYear.disabledProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) { // If tblVYear is disabled
                // Unselect all checkboxes
                tblVYear.getItems().forEach(item -> item.getSelect().setSelected(false));
                // Unselect the "Select All" checkbox
                selectYearAll.setSelected(false);
            }
        });

        tblIndex03_yr.setCellValueFactory(new PropertyValueFactory<>("tblIndex03_yr"));
    }

    public void initCombo() {
        tblVYear.setDisable(true);
        chckNoYear.setSelected(true);
        chckNoYear.setVisible(false);

        comboFilter.setOnAction(e -> {
            String selectedFilter = comboFilter.getSelectionModel().getSelectedItem();
            txtSeeks01.setVisible(false);
            txtSeeks01.setManaged(false);
            txtSeeks02.setVisible(false);
            txtSeeks02.setManaged(false);
            btnFilterMake.setVisible(false);
            btnFilterMake.setManaged(false);
            btnFilterModel.setVisible(false);
            btnFilterModel.setManaged(false);

            // Show relevant controls based on selected filter
            switch (selectedFilter) {
                case "MAKE":
                    txtSeeks01.setText("");
                    txtSeeks01.setVisible(true);
                    txtSeeks01.setManaged(true);
                    btnFilterMake.setVisible(true);
                    btnFilterMake.setManaged(true);
                    tblVModelList.setItems(itemModeldata);

                    break;
                case "MODEL":
                    txtSeeks02.setText("");
                    txtSeeks02.setVisible(true);
                    txtSeeks02.setManaged(true);
                    btnFilterModel.setVisible(true);
                    btnFilterModel.setManaged(true);
                    tblVModelList.setItems(itemModeldata);
                    break;
            }
        });
    }
}
