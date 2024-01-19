/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.auto.app.sales;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.auto.app.views.ScreenInterface;
import org.rmj.auto.app.views.unloadForm;
import org.rmj.auto.sales.base.VehicleSalesProposalMaster;

/**
 * FXML Controller class
 *
 * @author User
 */
public class VSPAddOnsApprovalController implements Initializable, ScreenInterface {

    private GRider oApp;
    private VehicleSalesProposalMaster oTrans;
    private MasterCallback oListener;
    private int pnRow = -1;
    private double xOffset = 0;
    private double yOffset = 0;
    public int pnEditMode;//Modifying fields
    private int lnCtr = 0;
    unloadForm unload = new unloadForm(); //Used in Close Button
    private final String pxeModuleName = "VSP Add Ons Approval"; //Form Title
    private ObservableList<VSPTableLaborList> laborData = FXCollections.observableArrayList();
    private ObservableList<VSPTablePartList> partData = FXCollections.observableArrayList();
    DecimalFormat formatToSendDecimal = new DecimalFormat("###0.00"); // to set format on database
    DecimalFormat decimalFormat = new DecimalFormat("#,##0.00"); //to set format on textfield
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnClose;
    @FXML
    private Button btnBrowse;
    @FXML
    private TextField txtField68;
    @FXML
    private TextField txtField75;
    @FXML
    private TextField txtField97;
    @FXML
    private TextArea textArea69;
    @FXML
    private TextField txtField71;
    @FXML
    private TextField txtField72;
    @FXML
    private TextField txtField73;
    @FXML
    private TextField txtField74;
    @FXML
    private TextArea textArea70;
    @FXML
    private TextField txtField21;
    @FXML
    private TabPane tabPCustCont;
    @FXML
    private TableView<VSPTableLaborList> tblViewLabor;
    @FXML
    private TableColumn<VSPTableLaborList, String> tblLaborRow;
    @FXML
    private TableColumn<VSPTableLaborList, String> tblindex07_Labor;
    @FXML
    private TableColumn<VSPTableLaborList, String> tblindex05_Labor;
    @FXML
    private TableColumn<VSPTableLaborList, String> tblindex04_Labor;
    @FXML
    private TableColumn<VSPTableLaborList, String> tblindex08_Labor;
    @FXML
    private TableColumn<VSPTableLaborList, String> tblindex11_Labor;
    @FXML
    private TableView<VSPTablePartList> tblViewParts;
    @FXML
    private TableColumn<VSPTablePartList, String> tblPartsRow;
    @FXML
    private TableColumn<VSPTablePartList, String> tblindex14_Part;
    @FXML
    private TableColumn<VSPTablePartList, String> tblindex09_Part;
    @FXML
    private TableColumn<VSPTablePartList, String> tblindex08_Part;
    @FXML
    private TableColumn<VSPTablePartList, String> tblindex06_Part;
    @FXML
    private TableColumn<VSPTablePartList, String> tblindex04_Part;
    @FXML
    private TableColumn<VSPTablePartList, String> tblindexTotAmnt;
    @FXML
    private TableColumn<VSPTablePartList, String> tblindex11_Part;
    @FXML
    private Button btnApprove;
    @FXML
    private Label lblVSPApprovalStatus;
    @FXML
    private TableColumn<VSPTableLaborList, String> tblLaborSelect;
    @FXML
    private CheckBox selectAllLabor;
    @FXML
    private TableColumn<VSPTablePartList, String> tblPartsSelect;
    @FXML
    private CheckBox selectAllParts;
    @FXML
    private Tab tabLabor;
    @FXML
    private Tab tabParts;
    @FXML
    private TextField txtField13;
    @FXML
    private TextField txtField14;
    @FXML
    private TableColumn<VSPTableLaborList, String> tblindex14_Labor;
    @FXML
    private TableColumn<VSPTablePartList, String> tblindex20_Part;
    @FXML
    private TextField txtField03;
    @FXML
    private TextField txtField02;

    /**
     * Initializes the controller class.
     */
    private Stage getStage() {
        return (Stage) btnClose.getScene().getWindow();
    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oListener = (int fnIndex, Object foValue) -> {
            System.out.println("Set Class Value " + fnIndex + "-->" + foValue);
        };
        oTrans = new VehicleSalesProposalMaster(oApp, oApp.getBranchCode(), true); //Initialize VehicleSalesProposalMaster
        oTrans.setCallback(oListener);
        oTrans.setWithUI(true);

        //Initialize buttons
        initCmdButton();
        oTrans.setFormType(false);
        tblViewLabor.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewLabor.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

        tblViewParts.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewParts.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

        pnEditMode = EditMode.UNKNOWN;
        initButton(pnEditMode);
    }

    private void initCmdButton() {
        btnClose.setOnAction(this::cmdButton_Click);
        btnBrowse.setOnAction(this::cmdButton_Click);
        btnApprove.setOnAction(this::cmdButton_Click);
    }

    private void cmdButton_Click(ActionEvent event) {
        try {
            String lsButton = ((Button) event.getSource()).getId();
            switch (lsButton) {
                case "btnBrowse":
                    try {
                    if (oTrans.searchRecord()) {
                        loadVSPApprovalField();
                        loadTableLabor();
                        loadTableParts();
                        pnEditMode = EditMode.READY;
                        initButton(pnEditMode);
                    } else {
                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                        clearFields();
                        laborData.clear();
                        partData.clear();
                        pnEditMode = EditMode.UNKNOWN;

                    }
                } catch (SQLException ex) {
                    Logger.getLogger(VSPAddOnsApprovalController.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
                break;
                case "btnApprove":
                    ObservableList<VSPTableLaborList> selectedLaborItems = FXCollections.observableArrayList();
                    ObservableList<VSPTablePartList> selectedPartsItems = FXCollections.observableArrayList();
                    for (VSPTableLaborList itemLabor : tblViewLabor.getItems()) {
                        if (itemLabor.getSelect().isSelected()) {
                            selectedLaborItems.add(itemLabor);
                        }
                    }
                    for (VSPTablePartList itemParts : tblViewParts.getItems()) {
                        if (itemParts.getSelect().isSelected()) {
                            selectedPartsItems.add(itemParts);
                        }
                    }
                    if (selectedLaborItems.isEmpty() && selectedPartsItems.isEmpty()) {
                        ShowMessageFX.Information(null, pxeModuleName, "No items selected to approve.");
                    } else {
                        int i = 0;
                        if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to approve?")) {
                            for (VSPTableLaborList itemLabor : selectedLaborItems) {
                                String fsLaborRow = itemLabor.getTblLaborRow();
                                int fsRowInt = Integer.parseInt(fsLaborRow);// Assuming there is a method to retrieve the transaction number
                                boolean approvedLabor = oTrans.vspAddOnsApproval(fsRowInt, true); // Handle SQL exception
                                String fsLaborApprovedBy = itemLabor.getTblindex12_Labor();
                                if (!fsLaborApprovedBy.isEmpty()) {
                                    ShowMessageFX.Error(null, pxeModuleName, "Failed to approve labor, the item(s) already approved.");
                                    loadTableLabor();
                                    selectAllLabor.setSelected(false);
                                    return;
                                } else {
                                    if (approvedLabor) {
                                        i = i + 1;
                                    } else {
                                        ShowMessageFX.Error(null, pxeModuleName, "Failed to approve labor.");
                                        return;
                                    }

                                }
                            }
                            for (VSPTablePartList itemParts : selectedPartsItems) {
                                String fsPartsRow = itemParts.getTblPartsRow();
                                int fsRowIntParts = Integer.parseInt(fsPartsRow);
                                boolean approvedParts = oTrans.vspAddOnsApproval(fsRowIntParts, false); // Handle SQL exception
                                String fsPartsApprovedBy = itemParts.getTblindex18_Part();
                                if (!fsPartsApprovedBy.isEmpty()) {
                                    ShowMessageFX.Error(null, pxeModuleName, "Failed to approve parts, the item(s) already approved.");
                                    loadTableParts();
                                    selectAllParts.setSelected(false);
                                    return;
                                } else {
                                    if (approvedParts) {
                                        i = i + 1;
                                    } else {
                                        ShowMessageFX.Error(null, pxeModuleName, "Failed to approve parts.");
                                        return;
                                    }
                                }
                            }
                            loadTableLabor();
                            loadTableParts();
                            selectAllParts.setSelected(false);
                            selectAllLabor.setSelected(false);
                            ShowMessageFX.Information(null, pxeModuleName, i + " item(s) approved successfully.");
                            tblViewLabor.getItems().removeAll(selectedLaborItems);
                            tblViewLabor.refresh();
                            tblViewParts.getItems().removeAll(selectedPartsItems);
                            tblViewParts.refresh();
                        }
                    }
                    if (oTrans.OpenRecord(oTrans.getMaster(1).toString())) {
                        loadTableLabor();
                        loadTableParts();
                        tblViewLabor.refresh();
                        tblViewParts.refresh();
                    }
                    break;
                case "btnClose":
                    if (ShowMessageFX.OkayCancel(null, "Close Tab", "Are you sure you want to close this Tab?")) {
                        if (unload != null) {
                            unload.unloadForm(AnchorMain, oApp, pxeModuleName);
                        } else {
                            ShowMessageFX.Warning(null, "Warning", "Please notify the system administrator to configure the null value at the close button.");
                        }
                    }
                    break;
                default:
                    break;
            }
            initButton(pnEditMode);
        } catch (SQLException ex) {
            Logger.getLogger(VSPAddOnsApprovalController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private boolean loadVSPApprovalField() throws SQLException {
        try {
            if (!oTrans.computeAmount()) {
                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                return false;
            }
            txtField03.setText((String) oTrans.getMaster(3));
            txtField02.setText(CommonUtils.xsDateMedium((Date) oTrans.getMaster(2)));
            txtField68.setText(oTrans.getMaster(68).toString().toUpperCase());
            textArea69.setText(oTrans.getMaster(69).toString().toUpperCase());
            txtField97.setText(oTrans.getMaster(97).toString().toUpperCase());
            txtField75.setText(oTrans.getMaster(75).toString().toUpperCase());
            textArea70.setText(oTrans.getMaster(70).toString().toUpperCase());
            txtField71.setText(oTrans.getMaster(71).toString().toUpperCase());
            txtField72.setText(oTrans.getMaster(72).toString().toUpperCase());
            txtField73.setText(oTrans.getMaster(73).toString().toUpperCase());
            txtField74.setText(oTrans.getMaster(74).toString().toUpperCase());

            txtField13.setText(String.valueOf(decimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMaster(13))))));
            txtField14.setText(String.valueOf(decimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMaster(14))))));

            String slaborAmount = oTrans.getMaster(13).toString();
            double nlabotAmounts = Double.parseDouble(slaborAmount);
            String spartsAmount = oTrans.getMaster(14).toString();
            double npartsAmounts = Double.parseDouble(spartsAmount);
            double sum = nlabotAmounts + npartsAmounts;
            txtField21.setText(String.valueOf(decimalFormat.format(Double.parseDouble(String.valueOf(sum)))));

            if (oTrans.getMaster(61).toString().contains("0")) {
                lblVSPApprovalStatus.setText("Cancelled");
            } else {
                lblVSPApprovalStatus.setText("Active");
            }
            loadTableParts();
            loadTableLabor();
        } catch (SQLException ex) {
            Logger.getLogger(VSPAddOnsApprovalController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;

    }

    private void loadTableLabor() {
        try {
            /*Populate table*/
            laborData.clear();
            boolean bAdditional = false;
            boolean fbPurchaseType = false;
            for (int lnCtr = 1; lnCtr <= oTrans.getVSPLaborCount(); lnCtr++) {
                String cType = "";
                switch (oTrans.getVSPLaborDetail(lnCtr, "sChrgeTyp").toString()) {
                    case "0":
                        fbPurchaseType = true;
                        break;
                    case "1":
                        fbPurchaseType = false;
                        break;
                }
                bAdditional = oTrans.getVSPLaborDetail(lnCtr, "cAddtlxxx").toString().equals("1");
                String amountString = oTrans.getVSPLaborDetail(lnCtr, "nLaborAmt").toString();
                // Convert the amount to a decimal value
                double amount = Double.parseDouble(amountString);
                String formattedAmount = decimalFormat.format(amount);
                laborData.add(new VSPTableLaborList(
                        String.valueOf(lnCtr), //ROW
                        oTrans.getVSPLaborDetail(lnCtr, "sTransNox").toString().toUpperCase(),
                        oTrans.getVSPLaborDetail(lnCtr, "sLaborCde").toString().toUpperCase(),
                        oTrans.getVSPLaborDetail(lnCtr, "sLaborDsc").toString().toUpperCase(),
                        cType,
                        formattedAmount,
                        "",
                        "",
                        "",
                        "",
                        oTrans.getVSPLaborDetail(lnCtr, "sDSNoxxxx").toString().toUpperCase(),
                        oTrans.getVSPLaborDetail(lnCtr, "sApprovBy").toString().toUpperCase(),
                        oTrans.getVSPLaborDetail(lnCtr, "sApproved").toString().toUpperCase(),
                        bAdditional,
                        fbPurchaseType
                ));
                bAdditional = false;
                fbPurchaseType = false;
            }

            tblViewLabor.setItems(laborData);
            initTableLabor();
        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }
    }

    private void initTableLabor() {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            tblViewLabor.setEditable(true);
        } else {
            tblViewLabor.setEditable(false);
        }

        tblLaborRow.setCellValueFactory(new PropertyValueFactory<VSPTableLaborList, String>("tblLaborRow"));
        tblLaborSelect.setCellValueFactory(new PropertyValueFactory<VSPTableLaborList, String>("select"));
        tblViewLabor.getItems().forEach(item -> {
            CheckBox selectCheckBox = item.getSelect();
            selectCheckBox.setOnAction(event -> {
                if (tblViewLabor.getItems().stream().allMatch(tableItem -> tableItem.getSelect().isSelected())) {
                    selectAllLabor.setSelected(true);
                } else {
                    selectAllLabor.setSelected(false);
                }
            });
        });
        selectAllLabor.setOnAction(event -> {
            boolean newValue = selectAllLabor.isSelected();
            tblViewLabor.getItems().forEach(item -> item.getSelect().setSelected(newValue));
        });
        tblindex07_Labor.setCellValueFactory(new PropertyValueFactory<VSPTableLaborList, String>("tblindex07_Labor"));
        tblindex05_Labor.setCellValueFactory(new PropertyValueFactory<VSPTableLaborList, String>("FreeOrNot"));
        tblindex04_Labor.setCellValueFactory(new PropertyValueFactory<VSPTableLaborList, String>("tblindex04_Labor"));
        tblindex11_Labor.setCellValueFactory(new PropertyValueFactory<VSPTableLaborList, String>("tblindex11_Labor"));
        tblindex14_Labor.setCellValueFactory(new PropertyValueFactory<VSPTableLaborList, String>("tblindex14_Labor"));
        tblindex08_Labor.setCellValueFactory(new PropertyValueFactory<>("addOrNot"));

    }

    private void loadTableParts() {
        try {

            /*Populate table*/
            partData.clear();
            boolean fbPurchaseType = false;
            for (int lnCtr = 1; lnCtr <= oTrans.getVSPPartsCount(); lnCtr++) {
                String cType = "";
                switch (oTrans.getVSPPartsDetail(lnCtr, "sChrgeTyp").toString()) {
                    case "0":
                        fbPurchaseType = true;
                        break;
                    case "1":
                        fbPurchaseType = false;
                        break;
                }
                String amountString = oTrans.getVSPPartsDetail(lnCtr, "nUnitPrce").toString();
                double amount = Double.parseDouble(amountString);
                String formattedAmount = decimalFormat.format(amount);
                String partDesc = (String) oTrans.getVSPPartsDetail(lnCtr, "sDescript");
                int quant = Integer.parseInt(oTrans.getVSPPartsDetail(lnCtr, "nQuantity").toString());
                double total = quant * amount;
                String totalAmount = decimalFormat.format(total);

                partData.add(new VSPTablePartList(
                        String.valueOf(lnCtr), //ROW
                        oTrans.getVSPPartsDetail(lnCtr, "sTransNox").toString().toUpperCase(),
                        oTrans.getVSPPartsDetail(lnCtr, "sStockIDx").toString().toUpperCase(),
                        oTrans.getVSPPartsDetail(lnCtr, "sBarCodex").toString().toUpperCase(),
                        partDesc.toUpperCase(),
                        cType,
                        oTrans.getVSPPartsDetail(lnCtr, "nQuantity").toString(),
                        formattedAmount,
                        oTrans.getVSPPartsDetail(lnCtr, "sDSNoxxxx").toString().toUpperCase(),
                        oTrans.getVSPPartsDetail(lnCtr, "sApprovBy").toString().toUpperCase(),
                        totalAmount,
                        "",
                        oTrans.getVSPPartsDetail(lnCtr, "sApproved").toString().toUpperCase(),
                        fbPurchaseType
                ));
                fbPurchaseType = false;

            }

            tblViewParts.setItems(partData);
            initTableParts();
        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }

    }

    private void initTableParts() {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            tblViewParts.setEditable(true);
        } else {
            tblViewParts.setEditable(false);
        }
        tblPartsRow.setCellValueFactory(new PropertyValueFactory<VSPTablePartList, String>("tblPartsRow"));
        tblPartsSelect.setCellValueFactory(new PropertyValueFactory<VSPTablePartList, String>("select"));
        tblViewParts.getItems().forEach(item -> {
            CheckBox selectCheckBox = item.getSelect();
            selectCheckBox.setOnAction(event -> {
                if (tblViewParts.getItems().stream().allMatch(tableItem -> tableItem.getSelect().isSelected())) {
                    selectAllParts.setSelected(true);
                } else {
                    selectAllParts.setSelected(false);
                }
            });
        });
        selectAllParts.setOnAction(event -> {
            boolean newValue = selectAllParts.isSelected();
            tblViewParts.getItems().forEach(item -> item.getSelect().setSelected(newValue));
        });
        tblindex14_Part.setCellValueFactory(new PropertyValueFactory<VSPTablePartList, String>("tblindex14_Part"));
        tblindex09_Part.setCellValueFactory(new PropertyValueFactory<VSPTablePartList, String>("tblindex09_Part"));
        tblindex08_Part.setCellValueFactory(new PropertyValueFactory<VSPTablePartList, String>("FreeOrNot"));
        tblindex06_Part.setCellValueFactory(new PropertyValueFactory<VSPTablePartList, String>("tblindex06_Part"));
        tblindex04_Part.setCellValueFactory(new PropertyValueFactory<VSPTablePartList, String>("tblindex04_Part"));
        tblindex11_Part.setCellValueFactory(new PropertyValueFactory<VSPTablePartList, String>("tblindex11_Part"));
        tblindex20_Part.setCellValueFactory(new PropertyValueFactory<VSPTablePartList, String>("tblindex20_Part"));

        tblindexTotAmnt.setCellValueFactory(new PropertyValueFactory<VSPTablePartList, String>("tblindexTotAmnt"));
    }

    private void initButton(int fnValue) {
        btnApprove.setVisible(false);
        btnApprove.setManaged(false);
        tblLaborSelect.setVisible(false);
        tblPartsSelect.setVisible(false);
        selectAllParts.setVisible(false);
        selectAllLabor.setVisible(false);
        if (fnValue == EditMode.READY) {
            try {
                if (((String) oTrans.getMaster(61)).equals("0")) {
                    btnApprove.setVisible(false);
                    btnApprove.setManaged(false);
                    tblLaborSelect.setVisible(false);
                    tblPartsSelect.setVisible(false);
                } else {
                    btnApprove.setVisible(true);
                    btnApprove.setManaged(true);
                    tblLaborSelect.setVisible(true);
                    tblPartsSelect.setVisible(true);
                    selectAllParts.setVisible(true);
                    selectAllLabor.setVisible(true);
                }
            } catch (SQLException ex) {
                Logger.getLogger(VSPAddOnsApprovalController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void clearFields() {
        laborData.clear();
        partData.clear();
        txtField03.setText("");
        txtField02.setText("");
        txtField68.setText("");
        textArea69.setText("");
        txtField97.setText("");
        txtField75.setText("");
        textArea70.setText("");
        txtField71.setText("");
        txtField72.setText("");
        txtField73.setText("");
        txtField74.setText("");
        lblVSPApprovalStatus.setText("");
        txtField13.setText("0.00");
        txtField14.setText("0.00");
        txtField21.setText("0.00");
        lblVSPApprovalStatus.setText("");

    }
}
