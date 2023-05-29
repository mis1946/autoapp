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
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.auto.app.bank.BankEntryTableList;
import org.rmj.auto.clients.base.Activity;
import org.rmj.auto.sales.base.InquiryFollowUp;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class TownCityMainEntryDialogController implements Initializable, ScreenInterface {

    private Activity oTransTown;
    @FXML
    private Button btnClose;
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnAddTown;
    private GRider oApp;
    private ObservableList<TownEntryTableList> townCitydata = FXCollections.observableArrayList();
    unloadForm unload = new unloadForm(); //Used in Close Button
    private final String pxeModuleName = "ActivityMemberEntryDialogController"; //Form Title
    @FXML
    private TableColumn<TownEntryTableList, String> tblRow;
    @FXML
    private TableColumn<TownEntryTableList, Boolean> tblSelect;
    @FXML
    private TableColumn<TownEntryTableList, String> tblTown;
    @FXML
    private TableView tblViewTown;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        btnClose.setOnAction(this::cmdButton_Click);
        btnAddTown.setOnAction(this::cmdButton_Click);
        loadTownTable();
    }

    public void setObject(Activity foValue) {
        oTransTown = foValue;
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
        }
    }

    //storing values on bankentrydata
    private void loadTownTable() {
        try {
            /*Populate table*/
            townCitydata.clear();
            if (oTransTown.loadTown("", true)) {
                for (int lnCtr = 1; lnCtr <= oTransTown.getTownCount(); lnCtr++) {
                    System.out.println(oTransTown.getTown(lnCtr, "sTownName").toString());
                    townCitydata.add(new TownEntryTableList(
                            String.valueOf(lnCtr), //ROW
                            oTransTown.getTown(lnCtr, "sTownName").toString()
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

    private void initTownTable() {
        tblRow.setCellValueFactory(new PropertyValueFactory<>("tblRow"));  //Row
        tblSelect.setCellValueFactory(new PropertyValueFactory<>("select"));
        tblTown.setCellValueFactory(new PropertyValueFactory<>("tblTown")); // sBankName

    }
}
