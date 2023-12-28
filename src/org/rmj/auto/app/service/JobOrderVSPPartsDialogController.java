/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.auto.app.service;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
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
import org.rmj.auto.app.views.ScreenInterface;
import org.rmj.auto.app.views.unloadForm;
import org.rmj.auto.service.base.JobOrderMaster;

/**
 * FXML Controller class
 *
 * @author User
 */
public class JobOrderVSPPartsDialogController implements Initializable, ScreenInterface {

    private JobOrderMaster oTrans;
    private GRider oApp;
    unloadForm unload = new unloadForm(); //Used in Close Button
    private ObservableList<JobOrderVSPPartsList> partData = FXCollections.observableArrayList();
    private final String pxeModuleName = "Job Order VSP Parts List"; //Form Title {
    private int pnRow = 0;
    private String sTrans = "";
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnClose;
    @FXML
    private TableColumn<JobOrderVSPPartsList, String> tblindexRow;
    @FXML
    private CheckBox selectAllCheckBox;
    @FXML
    private TableColumn<JobOrderVSPPartsList, String> tblindex04;
    @FXML
    private TableView<JobOrderVSPPartsList> tblViewVSPParts;
    @FXML
    private TableColumn<JobOrderVSPPartsList, Boolean> tblSelectVSPParts;
    @FXML
    private TableColumn<JobOrderVSPPartsList, String> tblindex13;
    @FXML
    private TableColumn<JobOrderVSPPartsList, String> tblindex09;
    @FXML
    private TableColumn<JobOrderVSPPartsList, String> tblindex08;
    @FXML
    private TableColumn<JobOrderVSPPartsList, String> tblindex06;
    @FXML
    private TableColumn<JobOrderVSPPartsList, String> tblindex14;
    @FXML
    private TableColumn<JobOrderVSPPartsList, String> tblindex16;

    public void setTrans(String fsValue) {
        sTrans = fsValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnClose.setOnAction(this::cmdButton_Click);
        btnAdd.setOnAction(this::cmdButton_Click);
        loadVSPParts();
        tblViewVSPParts.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewVSPParts.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
    }

    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            case "btnAdd":
                ObservableList<JobOrderVSPPartsList> selectedItems = FXCollections.observableArrayList();
                for (JobOrderVSPPartsList item : tblViewVSPParts.getItems()) {
                    if (item.getSelect().isSelected()) {
                        selectedItems.add(item);
                    }
                }
                if (selectedItems.isEmpty()) {
                    ShowMessageFX.Information(null, pxeModuleName, "No items selected to add.");
                } else {
                    int lnfind = 0;
                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to add?")) {
                        // Call the addTown here
                        for (JobOrderVSPPartsList item : selectedItems) {
                            String fsStockID = item.getTblindex03();
                            String fsCd = item.getTblindex13();
                            String fsDescript = item.getTblindex09();
                            String fsQuantity = item.getTblindex06();
                            String fsAmount = item.getTblindex04();
                            String fsType = item.getTblindexType();
                            String fsTotal = item.getTblindex16();

//                            if (!fsDSCode.isEmpty() && !fsDSCode.equals(sTrans)) {
//                                ShowMessageFX.Error(null, pxeModuleName, "VSP Parts " + fsDescript + " already exist in JO No. " + fsJONox + ". Insert Aborted. ");
//                                return;
//                            }
                            try {
                                boolean fsDest = false;
                                for (int lnCtr = 1; lnCtr <= oTrans.getJOPartsCount(); lnCtr++) {
                                    if (oTrans.getJOPartsDetail(lnCtr, "sDescript").toString().equals(fsDescript)) {
                                        ShowMessageFX.Error(null, pxeModuleName, "Skipping, Failed to add vsp parts, " + fsDescript + " already exist.");
                                        fsDest = true;
                                        break;
                                    }
                                }
                                if (!fsDest) {
                                    lnfind++;
                                    if (oTrans.AddJOParts()) {
                                        DecimalFormat setFormat = new DecimalFormat("###0.00");
                                        oTrans.setJOPartsDetail(oTrans.getJOPartsCount(), 3, fsStockID);
                                        oTrans.setJOPartsDetail(oTrans.getJOPartsCount(), 14, fsCd);
                                        oTrans.setJOPartsDetail(oTrans.getJOPartsCount(), 4, fsDescript);
                                        oTrans.setJOPartsDetail(oTrans.getJOPartsCount(), 11, fsType);
                                        oTrans.setJOPartsDetail(oTrans.getJOPartsCount(), 6, Integer.valueOf(fsQuantity));
                                        oTrans.setJOPartsDetail(oTrans.getJOPartsCount(), 10, setFormat.format(Double.valueOf(fsAmount.replace(",", ""))));
                                        oTrans.setJOPartsDetail(oTrans.getJOPartsCount(), 15, setFormat.format(Double.valueOf(fsTotal.replace(",", ""))));
                                    }
                                    if (!oTrans.checkVSPJOParts(fsStockID, Integer.valueOf(fsQuantity), true, oTrans.getJOPartsCount())) {
                                        ShowMessageFX.Error(null, pxeModuleName, oTrans.getMessage());
                                        oTrans.removeJOParts(oTrans.getJOPartsCount());
                                        return;
                                    }
                                }
                            } catch (SQLException e) {
                                // Handle SQL exception
                                ShowMessageFX.Error(null, pxeModuleName, "An error occurred while adding vsp parts: " + e.getMessage());
                            }
                        }
                        if (lnfind >= 1) {
                            ShowMessageFX.Information(null, pxeModuleName, "Added VSP Parts successfully.");
                        } else {
                            ShowMessageFX.Error(null, pxeModuleName, "Failed to add vsp parts");
                        }
                    }
                    CommonUtils.closeStage(btnAdd);
                }
                break;
        }
    }

    private Stage getStage() {
        return (Stage) tblViewVSPParts.getScene().getWindow();
    }

    public void setObject(JobOrderMaster foValue) {
        oTrans = foValue;
    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    public void setRow(int fnRow) {
        pnRow = fnRow;
    }

    private void loadVSPParts() {
        try {
            /*Populate table*/
            partData.clear();
            if (oTrans.loadVSPParts()) {
                for (int lnCtr = 1; lnCtr <= oTrans.getVSPPartsCount(); lnCtr++) {
                    String amountString = oTrans.getVSPPartsDetail(lnCtr, "nUnitPrce").toString();
                    // Convert the amount to a decimal value
                    String cType = "";
                    switch (oTrans.getVSPPartsDetail(lnCtr, "sChrgeTyp").toString()) {
                        case "0":
                            cType = "FREE OF CHARGE";
                            break;
                        case "1":
                            cType = "CHARGE";
                            break;
                    }
                    DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
                    double amount = Double.parseDouble(amountString);
                    String formattedAmount = decimalFormat.format(amount);
                    int quant = Integer.parseInt(oTrans.getVSPPartsDetail(lnCtr, "nQuantity").toString());
                    double total = quant * amount;
                    String totalAmount = decimalFormat.format(total);
                    partData.add(new JobOrderVSPPartsList(
                            String.valueOf(lnCtr), //ROW
                            oTrans.getVSPPartsDetail(lnCtr, "sStockIDx").toString(),
                            oTrans.getVSPPartsDetail(lnCtr, "sBarCodex").toString(),
                            oTrans.getVSPPartsDetail(lnCtr, "sDescript").toString(),
                            cType,
                            oTrans.getVSPPartsDetail(lnCtr, "nQuantity").toString(),
                            formattedAmount,
                            oTrans.getVSPPartsDetail(lnCtr, "sDSNoxxxx").toString(),
                            oTrans.getVSPPartsDetail(lnCtr, "sDSCodexx").toString(),
                            oTrans.getVSPPartsDetail(lnCtr, "sChrgeTyp").toString(),
                            totalAmount
                    ));
                }
                tblViewVSPParts.setItems(partData);
                initVSPPartsTable();
            }
        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }
    }

    private void initVSPPartsTable() {
        tblindexRow.setCellValueFactory(new PropertyValueFactory<>("tblindexRow"));  //Row
        tblSelectVSPParts.setCellValueFactory(new PropertyValueFactory<>("select"));
        tblViewVSPParts.getItems().forEach(item -> {
            CheckBox selectCheckBox = item.getSelect();
            selectCheckBox.setOnAction(event -> {
                if (tblViewVSPParts.getItems().stream().allMatch(tableItem -> tableItem.getSelect().isSelected())) {
                    selectAllCheckBox.setSelected(true);
                } else {
                    selectAllCheckBox.setSelected(false);
                }
            });
        });
        selectAllCheckBox.setOnAction(event -> {
            boolean newValue = selectAllCheckBox.isSelected();
            tblViewVSPParts.getItems().forEach(item -> item.getSelect().setSelected(newValue));
        });
        tblindex13.setCellValueFactory(new PropertyValueFactory<>("tblindex13"));
        tblindex09.setCellValueFactory(new PropertyValueFactory<>("tblindex09"));
        tblindex08.setCellValueFactory(new PropertyValueFactory<>("tblindex08"));
        tblindex06.setCellValueFactory(new PropertyValueFactory<>("tblindex06"));
        tblindex04.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
        tblindex14.setCellValueFactory(new PropertyValueFactory<>("tblindex14"));
        tblindex16.setCellValueFactory(new PropertyValueFactory<>("tblindex16"));

    }
}
