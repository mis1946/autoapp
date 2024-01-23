/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.sales;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.auto.app.views.CancelForm;
import org.rmj.auto.app.views.ScreenInterface;
import org.rmj.auto.app.views.TextFieldAnimationUtil;
import org.rmj.auto.app.views.unloadForm;
import org.rmj.auto.sales.base.SalesAgentExecutiveMaster;
import org.rmj.auto.sales.base.SalesAgentInfo;

/**
 * FXML Controller class
 * DATE CREATED: 03-29-2023
 * @author Arsiela
 */
public class SalesAgentFormController implements Initializable, ScreenInterface  {
    private GRider oApp;
    private MasterCallback oListener;
    private SalesAgentExecutiveMaster oTrans;
    //private SalesAgentInfo oTrans;

    unloadForm unload = new unloadForm(); //Used in Close Button
    TextFieldAnimationUtil txtFieldAnimation = new TextFieldAnimationUtil();

    private String pxeModuleName = "Sales Agent Information"; //Form Title
    private int pnEditMode;//Modifying fields
    private int pnRow = -1;
    private int oldPnRow = -1;
    private int lnCtr = 0;
    private int pagecounter;

    private String oldTransNo = "";
    private String TransNo = "";
    private String sName = "";
    private boolean bisAgent = true;
    
    /*populate tables List*/
    private ObservableList<SalesAgentTableList> datalist = FXCollections.observableArrayList();
    private FilteredList<SalesAgentTableList> filteredData;
    private static final int ROWS_PER_PAGE = 50;
      
    private ObservableList<SalesAgentTableList> transactionlist = FXCollections.observableArrayList();
    
    @FXML
    private Pagination pagination;
    @FXML
    private TableView tblAgent;
    @FXML
    private TableColumn listIndexrw;
    @FXML
    private TableColumn listIndex01;
    @FXML
    private TableColumn listIndex02;
    @FXML
    private TableColumn listIndex03;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnClose;
    @FXML
    private Button btnDeactivate;
    @FXML
    private TableView tblTransaction;
    @FXML
    private TableColumn refIndexrw;
    @FXML
    private TableColumn refIndex01;
    @FXML
    private TableColumn refIndex02;
    @FXML
    private TableColumn refIndex03;
    @FXML
    private TableColumn refIndex04;
    @FXML
    private TableColumn refIndex05;
    @FXML
    private TableColumn refIndex06;
    @FXML
    private TableColumn refIndex07;
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private TextField textSeek01;
    @FXML
    private TextField textSeek02;
    @FXML
    private Label lblStatus;
    @FXML
    private TextField txtField06;
    @FXML
    private TextField txtField07;
    @FXML
    private TextField txtField09;
    @FXML
    private TextField txtField08;
    @FXML
    private TextArea textArea10;
    @FXML
    private Label lblFormTitle;
    @FXML
    private Label lblID;
    @FXML
    private Label lblName;
    @FXML
    private Label lblNameInfo;
    @FXML
    private FontAwesomeIconView ActIconNm;
    @FXML
    private TableColumn<?, ?> refIndex08;
    @FXML
    private Label txtLabel08;
    @FXML
    private Button btnDisapprove;
    @FXML
    private FontAwesomeIconView ActIconNm1;

    private Stage getStage(){
         return (Stage) btnSave.getScene().getWindow();
    }
     
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oListener = (int fnIndex, Object foValue) -> {
            System.out.println("Set Class Value "  + fnIndex + "-->" + foValue);
        };

//        oTrans = new SalesAgentInfo(oApp, oApp.getBranchCode(), true); 
//        oTrans.setCallback(oListener);
//        oTrans.setWithUI(true);
        
        oTrans = new SalesAgentExecutiveMaster(oApp, oApp.getBranchCode(), true); 
        oTrans.setCallback(oListener);
        oTrans.setWithUI(true);
        
        Platform.runLater(() -> {
            String parentTabTitle = getParentTabTitle();
            lblFormTitle.setText(parentTabTitle);
            if (parentTabTitle != null && parentTabTitle.contains("EXECUTIVE")) {
                oTrans.setFormType(false);
                bisAgent = false;
                sName = "Executive";
            } else {
                oTrans.setFormType(true);
                bisAgent = true;
                sName = "Agent";
            }
            txtField08.setVisible(bisAgent);
            txtLabel08.setVisible(bisAgent);
            lblID.setText(sName.toUpperCase() + " ID");
            lblName.setText(sName.toUpperCase() + " NAME");
            lblNameInfo.setText(sName + " Name");
            
            //Populate table
            loadAgentListTable();
            pagination.setPageFactory(this::createPage);
        });
        
        initTransactionTable();
        
        btnDisapprove.setVisible(false);
        btnDisapprove.setManaged(false);
        btnDeactivate.setVisible(false); 
        btnDeactivate.setManaged(false);
        
        setCapsLockBehavior(textSeek01);
        setCapsLockBehavior(textSeek02);
        setCapsLockBehavior(txtField06);
        setCapsLockBehavior(txtField07);
        setCapsLockBehavior(txtField08);
        setCapsLockBehavior(txtField09);
        setCapsLockBehavior(textArea10);
        
        //Key Pressed Event
        txtField06.setOnKeyPressed(this::txtField_KeyPressed); 
        
        txtField06.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
            } else {
                txtField07.clear();
                txtField08.clear();
                txtField09.clear();
                textArea10.clear();
            }
        });
        
        txtFieldAnimation.addRequiredFieldListener(txtField06);
        
        //Button Click Event
        btnAdd.setOnAction(this::cmdButton_Click);
        btnEdit.setOnAction(this::cmdButton_Click); 
        btnSave.setOnAction(this::cmdButton_Click); 
        btnClose.setOnAction(this::cmdButton_Click); 
        btnDisapprove.setOnAction(this::cmdButton_Click); 
        btnDeactivate.setOnAction(this::cmdButton_Click); 

        /*Clear Fields*/
        clearFields();

        pnEditMode = EditMode.UNKNOWN;
        initButton(pnEditMode);
        
//        Platform.runLater(() -> {
//            if(oTrans.loadState()){
//                pnEditMode = oTrans.getEditMode();
//                loadFields();
//                initButton(pnEditMode);
//            }else {
//                if(oTrans.getMessage().isEmpty()){
//                }else{
//                    ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
//                }
//            }
//        });
        
    }
    
    private String getParentTabTitle() {
        if (AnchorMain != null) {
            Node parent = AnchorMain.getParent();
            if (parent != null) {
                Parent tabContentParent = parent.getParent();
                if (tabContentParent instanceof TabPane) {
                    TabPane tabPane = (TabPane) tabContentParent;
                    Tab tab = findTabByContent(tabPane, AnchorMain);
                    if (tab != null) {
                        pxeModuleName = tab.getText();
                        return tab.getText().toUpperCase();
                    }
                }
            }
        }
        return null; // No parent Tab found
    }

    private Tab findTabByContent(TabPane tabPane, Node content) {
        for (Tab tab : tabPane.getTabs()) {
            if (tab.getContent() == content) {
                return tab;
            }
        }
        return null;
    }

    @Override
    public void setGRider(GRider foValue) {
         oApp = foValue;
    }
    
    private static void setCapsLockBehavior(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (textField.getText() != null) {
                textField.setText(newValue.toUpperCase());
            }
        });
    }
    
    private static void setCapsLockBehavior(TextArea textArea) {
        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (textArea.getText() != null) {
                textArea.setText(newValue.toUpperCase());
            }
        });
    }
     
     private void cmdButton_Click(ActionEvent event) {
        try {
            String lsButton = ((Button)event.getSource()).getId();
            String lsState = "";
            switch (lsButton){
                case "btnAdd": //create new Entry
                    clearFields();
                    if (oTrans.NewRecord()) {
                        loadFields();
                        transactionlist.clear();
                        pnEditMode = oTrans.getEditMode();
                    } else {
                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                    }
                    break;
                case "btnEdit": //modify entry
                    if (oTrans.UpdateRecord()) {
                        pnEditMode = oTrans.getEditMode();
                    } else {
                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                    }
                    break;
                case "btnDisapprove":
                case "btnDeactivate":
                    
                    if(((String) oTrans.getMaster(2)).equals("0") || ((String) oTrans.getMaster(2)).equals("2")){
                        if(lsButton.equals("btnDisapprove")){
                            lsState = "Disapprove";
                        } else {
                            if(bisAgent){
                                lsState = "Approve";
                            } else {
                                lsState = "Activate";
                            }
                        }
                       
                    } else {
                        lsState = "Deactivate";
                    }
                    
                    if(ShowMessageFX.OkayCancel(null, "Close Tab", "Are you sure you want to "+lsState+" this "+sName+"?") == true){
                    } else {
                        return;
                    }
                    
                    lsState = "";
                    if (oTrans.UpdateRecord()) {
                        CancelForm cancelform = new CancelForm();
                        
                        if(((String) oTrans.getMaster(2)).equals("0") || ((String) oTrans.getMaster(2)).equals("2")){
                            if(lsButton.equals("btnDisapprove")){
                                if (cancelform.loadCancelWindow(oApp, (String) oTrans.getMaster(1), (String) oTrans.getMaster(1), sName.toUpperCase())) {
                                } else {
                                    ShowMessageFX.Warning(null, pxeModuleName, "Error while disapproving " + sName + ".");
                                    return;
                                }
                                oTrans.setMaster(2, "2");
                                lsState = "disapproved";
                            } else {
                                oTrans.setMaster(2, "1");
                                lsState = "activated";
                            }
                            
                        } else {
                            if (cancelform.loadCancelWindow(oApp, (String) oTrans.getMaster(1), (String) oTrans.getMaster(1), sName.toUpperCase())) {
                            } else {
                                ShowMessageFX.Warning(null, pxeModuleName, "Error while deactivating " + sName + ".");
                                return;
                            }
                            oTrans.setMaster(2, "0");
                            lsState = "deactivated";
                        }
                        
                    } else {
                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                    }
                case "btnSave": //Proceed Saving
                    if(lsButton.equals("btnSave")){
                        if(ShowMessageFX.OkayCancel(null, "Close Tab", "Are you sure you want to save this entry?") == true){
                            lsState = "save";
                        } else {
                            return;
                        }
                    }
                    
                    if (oTrans.SaveRecord()) {
                       ShowMessageFX.Information(getStage(), "Transaction "+lsState+" successfully.", pxeModuleName, null);
                       loadAgentListTable();
                       pagination.setPageFactory(this::createPage);
                       getSelectedItem((String) oTrans.getMaster(1));

                       pnEditMode = oTrans.getEditMode();
                    } else {
                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", "Error while saving "+sName+" Information");
                    }
                    break;                        
                case "btnClose": //close tab
                    if(ShowMessageFX.OkayCancel(null, "Close Tab", "Are you sure you want to close this Tab?") == true){
                        if (unload != null) {
                             unload.unloadForm(AnchorMain, oApp, pxeModuleName);
                        }else {
                             ShowMessageFX.Warning(null, "Warning", "Please notify the system administrator to configure the null value at the close button.");    
                        }
                        break;
                    } else
                        return;
            }
            initButton(pnEditMode);  
        
        } catch (SQLException ex) {
            Logger.getLogger(SalesAgentFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
     
     //use for creating new page on pagination
    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, datalist.size());
        if (datalist.size() > 0) {
            tblAgent.setItems(FXCollections.observableArrayList(datalist.subList(fromIndex, toIndex)));
        }
        return tblAgent;

    }

    //storing values on table
    private void loadAgentListTable() {
        try {
            /*Populate table*/
            datalist.clear();
            if (oTrans.loadList()) {
                for (lnCtr = 1; lnCtr <= oTrans.getDetailCount(); lnCtr++) {
                    datalist.add(new SalesAgentTableList(
                            String.valueOf(lnCtr), //ROW
                            oTrans.getDetail(lnCtr, "sClientID").toString(), //
                            oTrans.getDetail(lnCtr, "cRecdStat").toString(), //
                            oTrans.getDetail(lnCtr, "sLastName").toString(), //
                            oTrans.getDetail(lnCtr, "sFrstName").toString(), //
                            oTrans.getDetail(lnCtr, "sMiddName").toString(), //
                            oTrans.getDetail(lnCtr, "sCompnyNm").toString().toUpperCase(), //
                            oTrans.getDetail(lnCtr, "sMobileNo").toString(), //
                            oTrans.getDetail(lnCtr, "sAccountx").toString(), //
                            oTrans.getDetail(lnCtr, "sEmailAdd").toString(), //
                            oTrans.getDetail(lnCtr, "sAddressx").toString().toUpperCase(), //
                            oTrans.getDetail(lnCtr, "cClientTp").toString() //  
                    ));
                }
                initListTable();
            }
            loadTab();

        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }
    }

    /*populate Table*/
    private void initListTable() {
        listIndexrw.setCellValueFactory(new PropertyValueFactory<>("tblindexrw"));
        listIndex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
        listIndex02.setCellValueFactory(new PropertyValueFactory<>("tblindex06"));
        listIndex03.setCellValueFactory(new PropertyValueFactory<>("tblindex10"));
        
        listIndex01.setText(sName + " ID");
        listIndex02.setText(sName + " Name");
        
        tblAgent.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblAgent.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

        filteredData = new FilteredList<>(datalist, b -> true);
        autoSearch(textSeek01);
        autoSearch(textSeek02);
        // 3. Wrap the FilteredList in a SortedList.
        SortedList<SalesAgentTableList> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(tblAgent.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tblAgent.setItems(sortedData);
        tblAgent.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblAgent.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
            header.setDisable(true);
        });

    }

    private void autoSearch(TextField txtField) {
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        txtField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(clients -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare order no. and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();
                switch (lnIndex) {
                    case 1:
                        if (lnIndex == 1) {
                            return (clients.getTblindex01().toLowerCase().contains(lowerCaseFilter)); // Does not match.
                        } else {
                            return (clients.getTblindex01().toLowerCase().contains(lowerCaseFilter)); // Does not match.
                        }
                    case 2:
                        if (lnIndex == 2) {
                            return (clients.getTblindex06().toLowerCase().contains(lowerCaseFilter)); // Does not match.
                        } else {
                            return (clients.getTblindex06().toLowerCase().contains(lowerCaseFilter)); // Does not match.
                        }
                    default:
                        return true;
                }
            });

            changeTableView(0, ROWS_PER_PAGE);
        });
        loadTab();
    }

    private void loadTab() {
        int totalPage = (int) (Math.ceil(datalist.size() * 1.0 / ROWS_PER_PAGE));
        pagination.setPageCount(totalPage);
        pagination.setCurrentPageIndex(0);
        changeTableView(0, ROWS_PER_PAGE);
        pagination.currentPageIndexProperty().addListener(
                (observable, oldValue, newValue) -> changeTableView(newValue.intValue(), ROWS_PER_PAGE));

    }

    private void changeTableView(int index, int limit) {
        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, datalist.size());

        int minIndex = Math.min(toIndex, filteredData.size());
        SortedList<SalesAgentTableList> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(tblAgent.comparatorProperty());
        tblAgent.setItems(sortedData);
    }

    /*Populate Fields after clicking row in table*/
    @FXML
    private void tblAgent_Clicked(MouseEvent event) {
        pnRow = tblAgent.getSelectionModel().getSelectedIndex();
        
        if(pnRow < 0){
            return;
        }
        
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            if (ShowMessageFX.OkayCancel(null, pxeModuleName, "You have unsaved data, are you sure you want to continue?") == true) {
            } else {
                return;
            }
        }

        pagecounter = pnRow + pagination.getCurrentPageIndex() * ROWS_PER_PAGE;
        if (pagecounter >= 0) {
            if (event.getClickCount() > 0) {
                getSelectedItem(filteredData.get(pagecounter).getTblindex01()); //Populate field based on selected Item
                tblAgent.setOnKeyReleased((KeyEvent t) -> {
                    KeyCode key = t.getCode();
                    switch (key) {
                        case DOWN:
                            pnRow = tblAgent.getSelectionModel().getSelectedIndex();
                            pagecounter = pnRow + pagination.getCurrentPageIndex() * ROWS_PER_PAGE;
                            if (pagecounter == tblAgent.getItems().size()) {
                                pagecounter = tblAgent.getItems().size();
                                getSelectedItem(filteredData.get(pagecounter).getTblindex01());
                            } else {
                                int y = 1;
                                pnRow = pnRow + y;
                                getSelectedItem(filteredData.get(pagecounter).getTblindex01());
                            }
                            break;
                        case UP:
                            pnRow = tblAgent.getSelectionModel().getSelectedIndex();
                            pagecounter = pnRow + pagination.getCurrentPageIndex() * ROWS_PER_PAGE;
                            getSelectedItem(filteredData.get(pagecounter).getTblindex01());
                            break;
                        default:
                            return;
                    }
                });
            }
            pnEditMode = EditMode.READY;
            initButton(pnEditMode);
        }
    }

    /*Populate Text Field Based on selected address in table*/
    private void getSelectedItem(String TransNo) {
        oldTransNo = TransNo;
        clearFields();
        if (oTrans.OpenRecord(TransNo)) {
            loadFields();
            loadTransactionsList();
        }
        oldPnRow = pagecounter;
    }
    
    private void initTransactionTable(){
        refIndexrw.setCellValueFactory(new PropertyValueFactory<>("tblindexrw"));
        refIndex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
        refIndex02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
        refIndex03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
        refIndex04.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
        refIndex05.setCellValueFactory(new PropertyValueFactory<>("tblindex05"));
        refIndex06.setCellValueFactory(new PropertyValueFactory<>("tblindex06"));
        refIndex07.setCellValueFactory(new PropertyValueFactory<>("tblindex07"));
        
        if(bisAgent){
            refIndex08.setCellValueFactory(new PropertyValueFactory<>("tblindex09"));
            refIndex08.setText("Sales Executive Name");
        } else {
            refIndex08.setCellValueFactory(new PropertyValueFactory<>("tblindex08"));
            refIndex08.setText("Sales Agent Name");
        }
        
        tblTransaction.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblTransaction.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
           // header.setDisable(true);
        });
        
        tblTransaction.setItems(transactionlist);
    
    }
    
    /*Populate table*/
    private void loadTransactionsList(){
        try {
            String lsCSPlateNo = "";
            String lsVSPDate = "";
            String lsUDRDate = "";
            transactionlist.clear();
            if (oTrans.loadTransactions()) {
                for (int lnCtr = 1; lnCtr <= oTrans.getVSPTransCount(); lnCtr++) {
                    
                    if(oTrans.getVSPTransDetail(lnCtr, "sPlateNox").toString().isEmpty()){
                        lsCSPlateNo = oTrans.getVSPTransDetail(lnCtr, "sCSNoxxxx").toString();
                    } else {
                        lsCSPlateNo = oTrans.getVSPTransDetail(lnCtr, "sPlateNox").toString();
                    }
                    
                    if(oTrans.getVSPTransDetail(lnCtr, "dTransact") != null){
                        lsVSPDate =  CommonUtils.xsDateShort((Date) oTrans.getVSPTransDetail(lnCtr, "dTransact"));
                    }
                    
                    if(oTrans.getVSPTransDetail(lnCtr, "dUDRDatex") != null ){
                        lsUDRDate = CommonUtils.xsDateShort((Date) oTrans.getVSPTransDetail(lnCtr, "dUDRDatex"));
                    }
                    transactionlist.add(new SalesAgentTableList(
                            String.valueOf(lnCtr), //ROW
                            lsVSPDate, //
                            oTrans.getVSPTransDetail(lnCtr, "sVSPNOxxx").toString(), //
                            oTrans.getVSPTransDetail(lnCtr, "sBuyCltNm").toString(), //
                            lsCSPlateNo, //
                            oTrans.getVSPTransDetail(lnCtr, "sDescript").toString(), //
                            lsUDRDate,
                            oTrans.getVSPTransDetail(lnCtr, "sUDRNoxxx").toString(), //
                            oTrans.getVSPTransDetail(lnCtr, "sSalesAgn").toString(), //
                            oTrans.getVSPTransDetail(lnCtr, "sSaleExNm").toString(), //
                            "", //
                            ""
                    ));
                    
                    lsCSPlateNo = "";
                    lsVSPDate = "";
                    lsUDRDate = "";
                }
                initTransactionTable();
            }

        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }
    
    }
 
    private void loadFields() {
        try {
            txtField06.setText((String) oTrans.getMaster(6));
            txtField07.setText((String) oTrans.getMaster(7));
            txtField08.setText((String) oTrans.getMaster(8));
            txtField09.setText((String) oTrans.getMaster(9));
            textArea10.setText(oTrans.getMaster(10).toString());
            lblStatus.setText("");
            
        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }
    }

    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(((TextField) event.getSource()).getId().substring(8, 10));

        try {
            switch (event.getCode()) {
                case F3:
                case TAB:
                case ENTER:
                    switch (lnIndex) {
                        case 6: 
                            if (oTrans.searchPerson(txtField06.getText())) {
                                loadFields();
                            } else {
                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                            }
                            break;
                    }
                    break;
            }
        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }

        switch (event.getCode()) {
            case ENTER:
            case DOWN:
                CommonUtils.SetNextFocus(txtField);
                break;
            case UP:
                CommonUtils.SetPreviousFocus(txtField);
        }

    }
     
    /*Enabling / Disabling Fields*/
    private void initButton(int fnValue){
        pnRow = 0;
        /* NOTE:
             lbShow (FALSE)= invisible
             !lbShow (TRUE)= visible
        */
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        //if lbShow = false hide btn          
        btnEdit.setVisible(false); 
        btnEdit.setManaged(false);
//        btnDeactivate.setVisible(false); 
//        btnDeactivate.setManaged(false);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);
        
        btnDeactivate.setVisible(false);
        btnDeactivate.setManaged(false);
        btnDisapprove.setVisible(false);
        btnDisapprove.setManaged(false);
        if(pnEditMode == EditMode.READY){
            try {
                switch((String) oTrans.getMaster(2)){
                    case "0": //Inactive / For approval
                        btnDeactivate.setVisible(true);
                        btnDeactivate.setManaged(true);
                        if(bisAgent){
                            lblStatus.setText("FOR APPROVAL");
                            ActIconNm.setIcon(FontAwesomeIcon.CHECK_CIRCLE);
                            btnDeactivate.setText("Approve");
                            btnDisapprove.setVisible(true);
                            btnDisapprove.setManaged(true);
                        } else {
                            lblStatus.setText("INACTIVE");
                            ActIconNm.setIcon(FontAwesomeIcon.PLUS_CIRCLE);
                            btnDeactivate.setText("Activate");
                        }
                        
                        break;
                    case "1": //Active / Approved
                        lblStatus.setText("ACTIVE");
                        btnDeactivate.setText("Deactivate");
                        ActIconNm.setIcon(FontAwesomeIcon.MINUS_CIRCLE);
                        btnDisapprove.setVisible(false);
                        btnDisapprove.setManaged(false);
                        btnDeactivate.setVisible(true);
                        btnDeactivate.setManaged(true);
                        break;
                    case "2": //Disapprove
                        if(bisAgent){
                            lblStatus.setText("DIS-APPROVE");
                            btnDeactivate.setText("Activate");
                            ActIconNm.setIcon(FontAwesomeIcon.PLUS_CIRCLE);
                            btnDeactivate.setVisible(true);
                            btnDeactivate.setManaged(true);
                            btnDisapprove.setVisible(false);
                            btnDisapprove.setManaged(false);
                        }
                        break;
                }
            } catch (SQLException ex) {
                Logger.getLogger(SalesAgentFormController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
//        if (fnValue == EditMode.READY) { //show edit if user clicked save / browse
////            btnEdit.setVisible(true); 
////            btnEdit.setManaged(true);
//              
//            btnDeactivate.setVisible(true); 
//            btnDeactivate.setManaged(true);
//        }
        
        txtField06.setDisable(true);
        
        if(fnValue == EditMode.ADDNEW){
            txtField06.setDisable(false);
        }
    }

    /*Clear Fields*/
    public void clearFields(){
        pnRow = 0;
        /*clear tables*/
        txtFieldAnimation.removeShakeAnimation(txtField06, txtFieldAnimation.shakeTextField(txtField07), "required-field");
        
        txtField06.clear();
        txtField07.clear();
        txtField08.clear();
        txtField09.clear();
        textArea10.clear();
    }

     
}
