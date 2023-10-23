/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.auto.app.views;

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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.auto.clients.base.Activity;

/**
 * FXML Controller class
 *
 * @author User
 */
public class ActivityVehicleDialogController implements Initializable, ScreenInterface {

    private Activity oTrans;
    private GRider oApp;
    unloadForm unload = new unloadForm(); //Used in Close Button
    private ObservableList<ActivityVchlEntryTable> actVhclModelData = FXCollections.observableArrayList();
    private final String pxeModuleName = "Activity Vehicle Entry"; //Form Title
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnClose;
    @FXML
    private Label SelectedCount;
    @FXML
    private TableColumn<ActivityVchlEntryTable, Boolean> tblSelectActVhcl;
    @FXML
    private CheckBox selectAllCheckBox;
    @FXML
    private TableColumn<ActivityVchlEntryTable, String> tblDescript;
    @FXML
    private TableColumn<ActivityVchlEntryTable, String> tblindexRow;
    @FXML
    private TableView<ActivityVchlEntryTable> tblViewActVchl;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        btnClose.setOnAction(this::cmdButton_Click);
        btnAdd.setOnAction(this::cmdButton_Click);
        loadActVhclModelTable();
    }

    private Stage getStage() {
        return (Stage) tblViewActVchl.getScene().getWindow();
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
                ObservableList<ActivityVchlEntryTable> selectedItems = FXCollections.observableArrayList();
                for (ActivityVchlEntryTable item : tblViewActVchl.getItems()) {
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
                        for (ActivityVchlEntryTable item : selectedItems) {
                            String fsSerialID = item.getTblSerial03();
                            String fsDescript = item.getTblDescription04();
                            String fsCSNoxxxx = item.getTblCs04();// Assuming there is a method to retrieve the transaction number
                            try {
                                boolean fsDest = false;
                                for (int lnCtr = 1; lnCtr <= oTrans.getActVehicleCount(); lnCtr++) {
                                    if (oTrans.getActVehicle(lnCtr, "sDescript").toString().equals(fsDescript)) {
                                        ShowMessageFX.Error(null, pxeModuleName, "Skipping, Failed to add vehicle model, " + fsDescript + " already exist.");
                                        fsDest = true;
                                        break;
                                        //return;
                                    }
                                }
                                if (!fsDest) {
                                    lnfind++;
                                    boolean add = oTrans.addActVehicle(fsSerialID, fsDescript, fsCSNoxxxx);
                                }
                            } catch (SQLException e) {
                                // Handle SQL exception
                                ShowMessageFX.Error(null, pxeModuleName, "An error occurred while adding vehicle model: " + e.getMessage());
                            }
                        }
                        if (lnfind >= 1) {
                            ShowMessageFX.Information(null, pxeModuleName, "Added vehicle successfully.");
                        } else {
                            ShowMessageFX.Error(null, pxeModuleName, "Failed to add vehicle");
                        }
                    }
                    CommonUtils.closeStage(btnAdd);
                }
                break;
        }
    }

    private void loadActVhclModelTable() {
        try {
            /*Populate table*/
            actVhclModelData.clear();
            if (oTrans.loadActVehicle("", false)) {
                System.out.println("copunt" + oTrans.getVehicleCount());
                for (int lnCtr = 1; lnCtr <= oTrans.getVehicleCount(); lnCtr++) {
                    actVhclModelData.add(new ActivityVchlEntryTable(
                            String.valueOf(lnCtr), //ROW
                            oTrans.getVehicle(lnCtr, "sSerialID").toString(),
                            oTrans.getVehicle(lnCtr, "sDescript").toString(),
                            oTrans.getVehicle(lnCtr, "sCSNoxxxx").toString()
                    ));
                }
                tblViewActVchl.setItems(actVhclModelData);
                initActVhclModelTable();
            }
        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }
    }

    private void updateSelectedCount() {
        ObservableList<ActivityVchlEntryTable> selectedItems = tblViewActVchl.getItems().filtered(items -> items.getSelect().isSelected());
        int count = selectedItems.size();
        SelectedCount.setText("" + count);
    }

    private void initActVhclModelTable() {
        tblindexRow.setCellValueFactory(new PropertyValueFactory<>("tblRow"));  //Row
        tblSelectActVhcl.setCellValueFactory(new PropertyValueFactory<>("select"));
        tblViewActVchl.getItems().forEach(item -> {
            CheckBox selectCheckBox = item.getSelect();
            selectCheckBox.setOnAction(event -> {
                updateSelectedCount();
                if (tblViewActVchl.getItems().stream().allMatch(tableItem -> tableItem.getSelect().isSelected())) {
                    selectAllCheckBox.setSelected(true);
                } else {
                    selectAllCheckBox.setSelected(false);
                }
            });
        });
        selectAllCheckBox.setOnAction(event -> {
            boolean newValue = selectAllCheckBox.isSelected();
            tblViewActVchl.getItems().forEach(item -> item.getSelect().setSelected(newValue));
            updateSelectedCount();
        });
        tblDescript.setCellValueFactory(new PropertyValueFactory<>("tblDescription04"));

    }

}
