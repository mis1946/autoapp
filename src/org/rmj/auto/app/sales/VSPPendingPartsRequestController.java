package org.rmj.auto.app.sales;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.auto.app.views.ScreenInterface;
import org.rmj.auto.app.views.unloadForm;
import org.rmj.auto.sales.base.VehicleSalesProposalMaster;

/**
 * FXML Controller class
 *
 * @author User
 */
public class VSPPendingPartsRequestController implements Initializable, ScreenInterface {

    private GRider oApp;
    private VehicleSalesProposalMaster oTrans;
    private MasterCallback oListener;
    private int lnCtr = 0;
    unloadForm unload = new unloadForm(); //Used in Close Button
    private final String pxeModuleName = "Sales Parts Request"; //Form Title
    private ObservableList<VSPTableMasterList> vspdata = FXCollections.observableArrayList();
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnClose;
    @FXML
    private Label lFrom;
    @FXML
    private DatePicker fromDate;
    @FXML
    private Label lTo;
    @FXML
    private DatePicker toDate;
    @FXML
    private TableColumn<VSPTableMasterList, String> tblindex02;
    @FXML
    private TableColumn<VSPTableMasterList, String> tblindex03;
    @FXML
    private TableColumn<VSPTableMasterList, String> tblindex68;
    @FXML
    private TableColumn<VSPTableMasterList, String> tblindex71;
    @FXML
    private TableColumn<VSPTableMasterList, String> tblindex72;
    @FXML
    private TableColumn<VSPTableMasterList, String> tblindex70;
    @FXML
    private TableColumn<VSPTableMasterList, String> tblindex61;
    @FXML
    private TableView<VSPTableMasterList> tblVhclPartsRequest;
    @FXML
    private TableColumn<VSPTableMasterList, String> tblVSPMaster_Row;
    @FXML
    private TableColumn<VSPTableMasterList, String> tblindex75;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTrans = new VehicleSalesProposalMaster(oApp, oApp.getBranchCode(), true); //Initialize ClientMaster
        oTrans.setCallback(oListener);
        oTrans.setWithUI(true);
//
//        loadVhlPartsRequestTable();
        // TODO
        fromDate.setValue(strToDate(CommonUtils.xsDateShort((Date) oApp.getServerDate())));
        toDate.setValue(strToDate(CommonUtils.xsDateShort((Date) oApp.getServerDate())));
        fromDate.setOnAction(event -> {
            LocalDate filterFromDate = fromDate.getValue();
            String psFromDate = filterFromDate.toString();
            LocalDate filterToDate = toDate.getValue();
            String psToDate = filterToDate.toString();
            loadVhlPartsRequestTable(psFromDate, psToDate);
        });

        toDate.setOnAction(event -> {
            LocalDate filterFromDate = fromDate.getValue();
            String psFromDate = filterFromDate.toString();
            LocalDate filterToDate = toDate.getValue();
            String psToDate = filterToDate.toString();
            loadVhlPartsRequestTable(psFromDate, psToDate);
        });
        btnClose.setOnAction(this::cmdButton_Click);
        tblVhclPartsRequest.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblVhclPartsRequest.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    //Date Formatter
    private LocalDate strToDate(String val) {
        DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(val, date_formatter);
        return localDate;
    }

    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();

        switch (lsButton) {
            case "btnClose": //close tab
                if (ShowMessageFX.OkayCancel(null, "Close Tab", "Are you sure, do you want to close tab?") == true) {
                    if (unload != null) {
                        unload.unloadForm(AnchorMain, oApp, pxeModuleName);
                    } else {
                        ShowMessageFX.Warning(null, "Warning", "Notify System Admin to Configure Null value at close button.");
                    }
                } else {
                    return;
                }
                break;
        }
    }

    private Stage getStage() {
//        return (Stage) txtFieldSearch.getScene().getWindow();
        return null;
    }

    private void loadVhlPartsRequestTable(String fromDate, String toDate) {
        try {
            vspdata.clear();
            if (oTrans.LoadVSPlist(fromDate, toDate)) {
                for (lnCtr = 1; lnCtr <= oTrans.getDetailCount(); lnCtr++) {
                    String cancelDisplay = "";
                    if (oTrans.getDetail(lnCtr, "sCancelld").toString().equals("1")) {
                        cancelDisplay = "Y";
                    } else {
                        cancelDisplay = "N";
                    }
                    vspdata.add(new VSPTableMasterList(
                            String.valueOf(lnCtr),
                            "",
                            oTrans.getDetail(lnCtr, "dTransact").toString(),
                            oTrans.getDetail(lnCtr, "sVSPNOxxx").toString().toUpperCase(), //vspNo
                            "", //dDelvryDt
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            " ",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            cancelDisplay,
                            oTrans.getDetail(lnCtr, "sCompnyNm").toString().toUpperCase(),
                            "",
                            "",
                            oTrans.getDetail(lnCtr, "sDescript").toString().toUpperCase(),
                            oTrans.getDetail(lnCtr, "sCSNoxxxx").toString().toUpperCase(),
                            oTrans.getDetail(lnCtr, "sPlateNox").toString().toUpperCase(),
                            oTrans.getDetail(lnCtr, "sFrameNox").toString().toUpperCase(),
                            oTrans.getDetail(lnCtr, "sEngineNo").toString().toUpperCase(),
                            oTrans.getDetail(lnCtr, "sSalesExe").toString().toUpperCase(),
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",//Branch Name
                            "",
                            "",
                            "",
                            oTrans.getDetail(lnCtr, "sDSNoxxxx").toString(),
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            ""// Branch Address
                    )
                    );

                }
                tblVhclPartsRequest.setItems(vspdata);
                initVhlPartsRequestTable();
            }
        } catch (SQLException ex) {
            Logger.getLogger(VSPPendingPartsRequestController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initVhlPartsRequestTable() {
        tblVSPMaster_Row.setCellValueFactory(new PropertyValueFactory<>("tblVSPMaster_Row"));  //Row
        // Set up listener for "Select All" checkbox
        tblindex03.setCellValueFactory(new PropertyValueFactory<>("tblindex03_Master"));
        tblindex02.setCellValueFactory(new PropertyValueFactory<>("tblindex02_Master"));
        tblindex68.setCellValueFactory(new PropertyValueFactory<>("tblindex68_Master"));
        tblindex71.setCellValueFactory(new PropertyValueFactory<>("tblindex71_Master"));
        tblindex72.setCellValueFactory(new PropertyValueFactory<>("tblindex72_Master"));
        tblindex70.setCellValueFactory(new PropertyValueFactory<>("tblindex70_Master"));
        tblindex75.setCellValueFactory(new PropertyValueFactory<>("tblindex75_Master"));
        tblindex61.setCellValueFactory(new PropertyValueFactory<>("tblindex61_Master"));
    }
}
