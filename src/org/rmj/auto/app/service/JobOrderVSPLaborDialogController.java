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
public class JobOrderVSPLaborDialogController implements Initializable, ScreenInterface {

    private JobOrderMaster oTrans;
    private String sTrans;
    private GRider oApp;
    unloadForm unload = new unloadForm(); //Used in Close Button
    private ObservableList<JobOrderVSPLaborList> laborData = FXCollections.observableArrayList();
    private final String pxeModuleName = "Job Order VSP Labor List"; //Form Title {
    private int pnRow = 0;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnClose;
    @FXML
    private TableColumn<JobOrderVSPLaborList, String> tblindexRow;
    @FXML
    private CheckBox selectAllCheckBox;
    @FXML
    private TableView<JobOrderVSPLaborList> tblViewVSPLabor;
    @FXML
    private TableColumn<JobOrderVSPLaborList, Boolean> tblSelectVSPLabor;
    @FXML
    private TableColumn<JobOrderVSPLaborList, String> tblindex07;
    @FXML
    private TableColumn<JobOrderVSPLaborList, String> tblindex05;
    @FXML
    private TableColumn<JobOrderVSPLaborList, String> tblindex04;
    @FXML
    private TableColumn<JobOrderVSPLaborList, String> tblindex11;

    private Stage getStage() {
        return (Stage) tblViewVSPLabor.getScene().getWindow();
    }

    public void setObject(JobOrderMaster foValue) {
        oTrans = foValue;
    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    public void setTrans(String fsValue) {
        sTrans = fsValue;
    }

    public void setRow(int fnRow) {
        pnRow = fnRow;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnClose.setOnAction(this::cmdButton_Click);
        btnAdd.setOnAction(this::cmdButton_Click);
        loadVSPLabor();

        tblViewVSPLabor.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewVSPLabor.lookup("TableHeaderRow");
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
                ObservableList<JobOrderVSPLaborList> selectedItems = FXCollections.observableArrayList();
                for (JobOrderVSPLaborList item : tblViewVSPLabor.getItems()) {
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
                        for (JobOrderVSPLaborList item : selectedItems) {

                            String fsDSCode = item.getTblindex12();
                            String fsCd = item.getTblindex03();
                            String fsDescript = item.getTblindex07();
                            String fsType = item.getTblindex05();
                            String fsAmount = item.getTblindex04();
                            String fsJONox = item.getTblindex11();

                            if (!fsDSCode.isEmpty() && !fsDSCode.equals(sTrans)) {
                                ShowMessageFX.Error(null, pxeModuleName, "VSP Labor " + fsDescript + " already exist in JO No. " + fsJONox + ". Insert Aborted. ");
                                return;
                            }
                            try {
                                boolean fsDest = false;
                                for (int lnCtr = 1; lnCtr <= oTrans.getJOLaborCount(); lnCtr++) {
                                    if (oTrans.getJOLaborDetail(lnCtr, "sLaborDsc").toString().equals(fsDescript)) {
                                        ShowMessageFX.Error(null, pxeModuleName, "Skipping, Failed to add vsp labor, " + fsDescript + " already exist.");
                                        fsDest = true;
                                        break;
                                    }
                                }
                                if (!fsDest) {
                                    lnfind++;
                                    DecimalFormat setFormat = new DecimalFormat("###0.00");
                                    if (oTrans.addJOLabor()) {
                                        oTrans.setJOLaborDetail(oTrans.getJOLaborCount(), 4, fsCd);
                                        oTrans.setJOLaborDetail(oTrans.getJOLaborCount(), 10, fsDescript);
                                        oTrans.setJOLaborDetail(oTrans.getJOLaborCount(), 3, fsType);
                                        oTrans.setJOLaborDetail(oTrans.getJOLaborCount(), 6, setFormat.format(Double.valueOf(fsAmount.replace(",", ""))));
                                    }
                                }
                            } catch (SQLException e) {
                                // Handle SQL exception
                                ShowMessageFX.Error(null, pxeModuleName, "An error occurred while adding vsp labor: " + e.getMessage());
                            }
                        }
                        if (lnfind >= 1) {
                            ShowMessageFX.Information(null, pxeModuleName, "Added VSP Labor successfully.");
                        } else {
                            ShowMessageFX.Error(null, pxeModuleName, "Failed to add vsp labor");
                        }
                    }
                    CommonUtils.closeStage(btnAdd);
                }
                break;
        }
    }

    private void loadVSPLabor() {
        try {
            /*Populate table*/
            laborData.clear();
            if (oTrans.loadVSPLabor()) {
                for (int lnCtr = 1; lnCtr <= oTrans.getVSPLaborCount(); lnCtr++) {
                    String amountString = oTrans.getVSPLaborDetail(lnCtr, "nLaborAmt").toString();
                    // Convert the amount to a decimal value

                    String cType = "";
                    switch (oTrans.getVSPLaborDetail(lnCtr, "sChrgeTyp").toString()) {
                        case "0":
                            cType = "FREE OF CHARGE";
                            break;
                        case "1":
                            cType = "CHARGE";
                            break;
                    }
                    double amount = Double.parseDouble(amountString);
                    DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
                    String formattedAmount = decimalFormat.format(amount);
                    laborData.add(new JobOrderVSPLaborList(
                            String.valueOf(lnCtr), //ROW
                            oTrans.getVSPLaborDetail(lnCtr, "sLaborCde").toString(),
                            oTrans.getVSPLaborDetail(lnCtr, "sLaborDsc").toString(),
                            cType,
                            formattedAmount,
                            oTrans.getVSPLaborDetail(lnCtr, "sDSNoxxxx").toString(),
                            oTrans.getVSPLaborDetail(lnCtr, "sDSCodexx").toString()
                    ));
                }
                tblViewVSPLabor.setItems(laborData);
                initVSPLaborTable();
            }
        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }
    }

    private void initVSPLaborTable() {
        tblindexRow.setCellValueFactory(new PropertyValueFactory<>("tblindexRow"));  //Row
        tblSelectVSPLabor.setCellValueFactory(new PropertyValueFactory<>("select"));
        tblViewVSPLabor.getItems().forEach(item -> {
            CheckBox selectCheckBox = item.getSelect();
            selectCheckBox.setOnAction(event -> {
                if (tblViewVSPLabor.getItems().stream().allMatch(tableItem -> tableItem.getSelect().isSelected())) {
                    selectAllCheckBox.setSelected(true);
                } else {
                    selectAllCheckBox.setSelected(false);
                }
            });
        });
        selectAllCheckBox.setOnAction(event -> {
            boolean newValue = selectAllCheckBox.isSelected();
            tblViewVSPLabor.getItems().forEach(item -> item.getSelect().setSelected(newValue));
        });
        tblindex07.setCellValueFactory(new PropertyValueFactory<>("tblindex07"));
        tblindex05.setCellValueFactory(new PropertyValueFactory<>("tblindex05"));
        tblindex04.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
        tblindex11.setCellValueFactory(new PropertyValueFactory<>("tblindex11"));
    }
}
