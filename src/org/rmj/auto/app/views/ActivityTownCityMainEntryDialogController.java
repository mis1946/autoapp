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
public class ActivityTownCityMainEntryDialogController implements Initializable, ScreenInterface {

    private String sProvID;
    private Activity oTrans;
    @FXML
    private Button btnClose;
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnAddTown;
    private GRider oApp;
    private ObservableList<ActivityTownEntryTableList> townCitydata = FXCollections.observableArrayList();
    unloadForm unload = new unloadForm(); //Used in Close Button
    private final String pxeModuleName = "Activity Member Entry"; //Form Title
    @FXML
    private TableColumn<ActivityTownEntryTableList, String> tblRow;
    @FXML
    private TableColumn<ActivityTownEntryTableList, Boolean> tblSelect;
    @FXML
    private TableColumn<ActivityTownEntryTableList, String> tblTown;
    @FXML
    private TableView<ActivityTownEntryTableList> tblViewTown;
    @FXML
    private Label SelectedCount;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnClose.setOnAction(this::cmdButton_Click);
        btnAddTown.setOnAction(this::cmdButton_Click);
        loadTownTable();
        tblViewTown.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewTown.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

    }

    public void setObject(Activity foValue) {
        oTrans = foValue;
    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    public void setProv(String fsValue) {
        sProvID = fsValue;
    }

    private void cmdButton_Click(ActionEvent event) {

        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            case "btnAddTown":
                ObservableList<ActivityTownEntryTableList> selectedItems = FXCollections.observableArrayList();
                for (ActivityTownEntryTableList item : tblViewTown.getItems()) {
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
                        for (ActivityTownEntryTableList item : selectedItems) {
                            String fsTownId = item.getTblindexId01();
                            String fsTownName = item.getTblCity();// Assuming there is a method to retrieve the transaction number
                            try {
                                boolean fsTown = false;
                                for (int lnCtr = 1; lnCtr <= oTrans.getActTownCount(); lnCtr++) {
                                    if (oTrans.getActTown(lnCtr, "sTownName").toString().equals(fsTownName)) {
                                        ShowMessageFX.Error(null, pxeModuleName, "Skipping, Failed to add town, " + fsTownName + " already exist.");
                                        fsTown = true;
                                        break;
                                    }
                                }
                                if (!fsTown) {
                                    lnfind++;
                                    System.out.println(lnfind);
                                    boolean add = oTrans.addActTown(fsTownId, fsTownName);
                                }
                            } catch (SQLException e) {
                                // Handle SQL exception
                                ShowMessageFX.Error(null, pxeModuleName, "An error occurred while adding town: " + e.getMessage());
                            }
                        }
                        if (lnfind >= 1) {
                            ShowMessageFX.Information(null, pxeModuleName, "Added town successfully.");
                        } else {
                            ShowMessageFX.Error(null, pxeModuleName, "Failed to add town");
                        }
                        CommonUtils.closeStage(btnAddTown);
                    }

                }
                break;
        }
    }
    //storing values on bankentrydata

    private void loadTownTable() {
        try {
            /*Populate table*/
            townCitydata.clear();
            if (oTrans.loadTown(sProvID, true)) {
                for (int lnCtr = 1; lnCtr <= oTrans.getTownCount(); lnCtr++) {
                    System.out.println(oTrans.getTown(lnCtr, "sTownName").toString());
                    townCitydata.add(new ActivityTownEntryTableList(
                            String.valueOf(lnCtr), //ROW
                            oTrans.getTown(lnCtr, "sTownIDxx").toString(),
                            oTrans.getTown(lnCtr, "sTownName").toString()
                    ));
                }
                tblViewTown.setItems(townCitydata);
                initTownTable();
            }
        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }
    }

    private Stage getStage() {
        return (Stage) tblViewTown.getScene().getWindow();
    }

    private void updateSelectedCount() {
        ObservableList<ActivityTownEntryTableList> selectedItems = tblViewTown.getItems().filtered(items -> items.getSelect().isSelected());
        int count = selectedItems.size();
        SelectedCount.setText("" + count);
    }

    private void initTownTable() {
        tblRow.setCellValueFactory(new PropertyValueFactory<>("tblRow"));  //Row
        tblSelect.setCellValueFactory(new PropertyValueFactory<>("select"));
        tblViewTown.getItems().forEach(item -> {
            CheckBox selectCheckBox = item.getSelect();
            selectCheckBox.setOnAction(event -> {
                updateSelectedCount();

            });
        });
        tblTown.setCellValueFactory(new PropertyValueFactory<>("tblCity"));

    }
}
