/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.sales;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
 * @author Arsiela Date Created: 12-29-2023 to be add functionality on
 * controller by Dave
 */
public class VSPPartsRequestController implements Initializable, ScreenInterface {

    private GRider oApp;
    private VehicleSalesProposalMaster oTrans;
    private MasterCallback oListener;
    private double xOffset = 0;
    private double yOffset = 0;
    private boolean pbState = true;
    private Boolean lbrDsc;
    private String psOrigDsc = "";
    private String sTrans = "";
    private String psStockID = "";
    private String psJO = "";
    private int pnRow = -1;
    unloadForm unload = new unloadForm(); //Used in Close Button
    private final String pxeModuleName = "Parts Request"; //Form Title
    private ObservableList<VSPTablePartList> partData = FXCollections.observableArrayList();
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnClose;
    @FXML
    private Button btnRefresh;
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
    private TableColumn<VSPTablePartList, String> tblRow;
    @FXML
    private TableColumn<VSPTablePartList, String> tblindex09_Part;
    @FXML
    private TableColumn<VSPTablePartList, String> tblindex14_Part;
    @FXML
    private TableColumn<VSPTablePartList, String> tblindex17_Part;
    @FXML
    private TableColumn<VSPTablePartList, String> tblindex06_Part;
    @FXML
    private TableColumn<VSPTablePartList, String> tblindex08_Part;
    @FXML
    private TableView<VSPTablePartList> tblViewPartRequest;

    /**
     * Initializes the controller class.
     */
    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    public void setObject(VehicleSalesProposalMaster foValue) {
        oTrans = foValue;
    }

    public void setRow(int fnRow) {
        pnRow = fnRow;
    }

    public void isAdditional(boolean additional) {
        additional = additional;
    }

    public void setState(boolean fbValue) {
        pbState = fbValue;
    }

    public void setOrigDsc(String fsValue) {
        psOrigDsc = fsValue;
    }

    public void setStockID(String fsValue) {
        psStockID = fsValue;
    }

    public void setJO(String fsValue) {
        psJO = fsValue;
    }

    public void setTrans(String fsValue) {
        sTrans = fsValue;
    }

    public void setLbrDsc(Boolean fbValue) {
        lbrDsc = fbValue;
    }

    private Stage getStage() {
        return null;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTrans = new VehicleSalesProposalMaster(oApp, oApp.getBranchCode(), true); //Initialize ClientMaster
        oTrans.setCallback(oListener);
        oTrans.setWithUI(true);
        loadVSPFields();
        btnClose.setOnAction(this::cmdButton_Click);
        btnRefresh.setOnAction(this::cmdButton_Click);

        tblViewPartRequest.setOnMouseClicked(this::tblParts_Clicked);

    }

    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnRefresh":
                loadVSPFields();
                break;
            case "btnClose":
                loadVSPFields();
                CommonUtils.closeStage(btnClose);
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;
        }

    }

    private void loadVSPFields() {
        if (oTrans.OpenRecord(sTrans)) {
            try {
                if (oTrans.UpdateRecord()) {
                    txtField68.setText(oTrans.getMaster(68).toString().toUpperCase());
                    textArea69.setText((String) oTrans.getMaster(69).toString().toUpperCase());
                    txtField97.setText((String) oTrans.getMaster(97).toString().toUpperCase());
                    txtField75.setText((String) oTrans.getMaster(75).toString().toUpperCase());
                    textArea70.setText((String) oTrans.getMaster(70).toString().toUpperCase());
                    txtField71.setText((String) oTrans.getMaster(71).toString().toUpperCase());
                    txtField72.setText((String) oTrans.getMaster(72).toString().toUpperCase());
                    txtField73.setText((String) oTrans.getMaster(73).toString().toUpperCase());
                    txtField74.setText((String) oTrans.getMaster(74).toString().toUpperCase());
                    loadTableParts();
                } else {
                    ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                    return;
                }
            } catch (SQLException ex) {
                Logger.getLogger(VSPPartsRequestController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void loadTableParts() {
        try {
            /*Populate table*/
            partData.clear();
            for (int lnCtr = 1; lnCtr <= oTrans.getVSPPartsCount(); lnCtr++) {
                String cType = "";
                switch (oTrans.getVSPPartsDetail(lnCtr, "sChrgeTyp").toString()) {
                    case "0":
                        cType = "FREE OF CHARGE";
                        break;
                    case "1":
                        cType = "CHARGE";
                        break;
                }
                String partDesc2 = "";
                if (!oTrans.getVSPPartsDetail(lnCtr, "sPartDesc").toString().isEmpty()) {
                    partDesc2 = oTrans.getVSPPartsDetail(lnCtr, "sPartDesc").toString();
                }
                String partDesc = (String) oTrans.getVSPPartsDetail(lnCtr, "sDescript");
                int quant = Integer.parseInt(oTrans.getVSPPartsDetail(lnCtr, "nQuantity").toString());
                partData.add(new VSPTablePartList(
                        String.valueOf(lnCtr), //ROW
                        oTrans.getVSPPartsDetail(lnCtr, "sTransNox").toString(),
                        oTrans.getVSPPartsDetail(lnCtr, "sStockIDx").toString(),
                        oTrans.getVSPPartsDetail(lnCtr, "sBarCodex").toString(),
                        partDesc.toUpperCase(),
                        cType.toUpperCase(),
                        oTrans.getVSPPartsDetail(lnCtr, "nQuantity").toString(),
                        "",
                        oTrans.getVSPPartsDetail(lnCtr, "sDSNoxxxx").toString(),
                        "",
                        oTrans.getVSPPartsDetail(lnCtr, "sPartDesc").toString().toUpperCase()
                ));

            }
            tblViewPartRequest.setItems(partData);
            initTableParts();
        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }

    }

    private void initTableParts() {
        tblRow.setCellValueFactory(new PropertyValueFactory<VSPTablePartList, String>("tblPartsRow"));
        tblindex09_Part.setCellValueFactory(new PropertyValueFactory<VSPTablePartList, String>("tblindex09_Part"));
        tblindex14_Part.setCellValueFactory(new PropertyValueFactory<VSPTablePartList, String>("tblindex14_Part"));
        tblindex17_Part.setCellValueFactory(new PropertyValueFactory<VSPTablePartList, String>("tblindex17_Part"));
        tblindex06_Part.setCellValueFactory(new PropertyValueFactory<VSPTablePartList, String>("tblindex06_Part"));
        tblindex08_Part.setCellValueFactory(new PropertyValueFactory<VSPTablePartList, String>("tblindex08_Part"));
    }

    private void tblParts_Clicked(MouseEvent event) {
        pnRow = tblViewPartRequest.getSelectionModel().getSelectedIndex() + 1;
        if (pnRow == 0) {
            return;
        }

        if (event.getClickCount() == 2) {
            try {
                loadPartsAdditionalDialog(pnRow, false, false);
            } catch (IOException ex) {
                Logger.getLogger(VSPFormController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    private void loadPartsAdditionalDialog(Integer fnRow, boolean isWithLbDsc, boolean isAdd) throws IOException {
        /**
         * if state = true : ADD else if state = false : UPDATE *
         */
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("VSPPartsDialog.fxml"));

            VSPPartsDialogController loControl = new VSPPartsDialogController();
            loControl.setGRider(oApp);
            loControl.setObject(oTrans);
            loControl.setState(isAdd);
            fxmlLoader.setController(loControl);
            loControl.setRow(fnRow);
            loControl.setRequest(true);
            loControl.setOrigDsc((String) oTrans.getVSPPartsDetail(fnRow, 9));
            loControl.setStockID((String) oTrans.getVSPPartsDetail(fnRow, 3));
            loControl.setJO((String) oTrans.getVSPPartsDetail(fnRow, 11));
            loControl.setLbrDsc(isWithLbDsc);
            //load the main interface
            Parent parent = fxmlLoader.load();

            parent.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
            });

            parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    stage.setX(event.getScreenX() - xOffset);
                    stage.setY(event.getScreenY() - yOffset);
                }
            });

            //set the main interface as the scene/*
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("");
            stage.showAndWait();
            loadVSPFields();

        } catch (IOException e) {
            e.printStackTrace();
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);

        } catch (SQLException ex) {
            Logger.getLogger(VSPFormController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

}
