/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
 * @author John Dave Aquino date created 8-9-2023
 */
public class ItemEntryExpandModelTableController implements Initializable, ScreenInterface {

    private GRider oApp;
    private MasterCallback oListener;
    private ItemEntry oTrans;
    @FXML
    private Button btnClose;
    @FXML
    private Button btnRemove;
    @FXML
    private TableView<ItemEntryModelTable> tblVModelList;
    @FXML
    private TableColumn<ItemEntryModelTable, String> tblIndex03_yr;
    @FXML
    private CheckBox selectModelAll;
    private ObservableList<ItemEntryModelTable> itemModeldata = FXCollections.observableArrayList();
    @FXML
    private TableColumn<ItemEntryModelTable, String> tblIndex06_mdl;
    @FXML
    private TableColumn<ItemEntryModelTable, String> tblIndex07_mdl;
    @FXML
    private TableColumn<ItemEntryModelTable, Boolean> tblindexselectModel;
    @FXML
    private TableColumn<ItemEntryModelTable, String> tblindexRowModel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        btnClose.setOnAction(this::cmdButton_Click);
        btnRemove.setOnAction(this::cmdButton_Click);
        loadItemModel_YearTable();
    }

    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            case "btnRemove":
                int lnRowModel = 0;
                int lnRowModelYear = 0;

                for (ItemEntryModelTable item : tblVModelList.getItems()) {
                    if (item.getSelect().isSelected()) {
                        if (item.getTblIndex03_yr().equals("")) {
                            lnRowModel++;
                        } else {
                            lnRowModelYear++;
                        }
                    }
                }
                Integer[] lnValueModel = new Integer[lnRowModel];
                Integer[] lnValueModelYear = new Integer[lnRowModelYear];
                lnRowModel = 0;
                lnRowModelYear = 0;
                for (ItemEntryModelTable item : tblVModelList.getItems()) {
                    if (item.getSelect().isSelected()) {

                        if (item.getTblIndex03_yr().equals("")) {
                            lnValueModel[lnRowModel] = Integer.parseInt(item.getTblIndexRow_mdlyr());
                            lnRowModel++;
                        } else {
                            lnValueModelYear[lnRowModelYear] = Integer.parseInt(item.getTblIndexRow_mdlyr());
                            lnRowModelYear++;
                        }
                    }

                }
                oTrans.removeInvModel_Year(lnValueModel, lnValueModelYear);
                loadItemModel_YearTable();
                tblVModelList.refresh();
                break;
        }
    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private Stage getStage() {
        return (Stage) tblVModelList.getScene().getWindow();
    }

    public void setObject(ItemEntry foValue) {
        oTrans = foValue;
    }

    private void loadItemModel_YearTable() {
        try {
            itemModeldata.clear();
            int lnRow = 0;
            for (int lnCtr = 1; lnCtr <= oTrans.getInvModelCount(); lnCtr++) {
                itemModeldata.add(new ItemEntryModelTable(
                        String.valueOf(lnCtr), // ROW
                        "",
                        oTrans.getInvModel(lnCtr, "sModelCde").toString(),
                        oTrans.getInvModel(lnCtr, "sMakeDesc").toString(),
                        oTrans.getInvModel(lnCtr, "sModelDsc").toString(),
                        "",
                        String.valueOf(lnCtr)
                )
                );
            }

            //inv model year
            lnRow = oTrans.getInvModelCount();
            for (int lnCtr = 1; lnCtr <= oTrans.getInvModelYrCount(); lnCtr++) {
                lnRow = lnRow + 1;
                itemModeldata.add(new ItemEntryModelTable(
                        String.valueOf(lnRow), // ROW
                        "",
                        oTrans.getInvModelYr(lnCtr, "sModelCde").toString(),
                        oTrans.getInvModelYr(lnCtr, "sMakeDesc").toString(),
                        oTrans.getInvModelYr(lnCtr, "sModelDsc").toString(),
                        String.valueOf((Integer) oTrans.getInvModelYr(lnCtr, "nYearModl")),
                        String.valueOf(lnCtr)
                )
                );
            }

            tblVModelList.setItems(itemModeldata);
            initItemModelYearTable();
        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }
    }

    private void initItemModelYearTable() {
        tblindexRowModel.setCellValueFactory(new PropertyValueFactory<>("tblindexRow"));
        tblindexselectModel.setCellValueFactory(new PropertyValueFactory<>("select"));
        tblVModelList.getItems().forEach(item -> {
            CheckBox selectCheckBox = item.getSelect();
            selectCheckBox.setOnAction(event -> {
                if (tblVModelList.getItems().stream().allMatch(tableItem -> tableItem.getSelect().isSelected())) {
                    selectModelAll.setSelected(true);
                } else {
                    selectModelAll.setSelected(false);
                }
            });
        });
        selectModelAll.setOnAction(event -> {
            boolean newValue = selectModelAll.isSelected();
            if (!tblVModelList.getItems().isEmpty()) {
                tblVModelList.getItems().forEach(item -> item.getSelect().setSelected(newValue));
            }
        });
        tblIndex06_mdl.setCellValueFactory(new PropertyValueFactory<>("tblIndex06_mdl"));
        tblIndex07_mdl.setCellValueFactory(new PropertyValueFactory<>("tblIndex07_mdl"));
        tblIndex03_yr.setCellValueFactory(new PropertyValueFactory<>("tblIndex03_yr"));
    }

}
