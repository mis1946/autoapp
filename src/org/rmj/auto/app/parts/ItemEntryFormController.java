/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.parts;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.auto.app.views.VehicleDescriptionTableList;
import org.rmj.auto.app.views.unloadForm;

/**
 * FXML Controller class
 *
 * @author Arsiela
 * Date Created: 06-27-2023
 */
public class ItemEntryFormController implements Initializable {
    
    private GRider oApp;
    private MasterCallback oListener;
    //private VehicleColor oTrans;
    private int pnEditMode;
    private final String pxeModuleName = "Item Entry";
    
    unloadForm unload = new unloadForm(); //Used in Close Button
    private String oldTransNo = "";
    private String sTransNo = "";
    private int lnRow;
    private int lnCtr;
    
    private int pnRow = -1;
    private int oldPnRow = -1;
    private int pagecounter;
    private double xOffset = 0;
    private double yOffset = 0;
    
    private ObservableList<ItemEntryTableList> itemdata = FXCollections.observableArrayList();
    private FilteredList<ItemEntryTableList> filteredData;
    private static final int ROWS_PER_PAGE = 50;
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Pagination pagination;
    @FXML
    private TableView tblItemList;
    @FXML
    private TableColumn tblindex01;
    @FXML
    private TableColumn tblindex02;
    @FXML
    private TableColumn tblindex03;
    @FXML
    private TableColumn tblindex04;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnClose;
    @FXML
    private Button btnBrandName;
    @FXML
    private Button btnCategory;
    @FXML
    private Button btnInvType;
    @FXML
    private Button btnMeasurement;
    @FXML
    private Button btnLocation;
    @FXML
    private Button btnSupsDel;
    @FXML
    private Button btnSupsAdd;
    @FXML
    private Button btnModelAdd;
    @FXML
    private Button btnModelDel;
    @FXML
    private Button btnCapture;
    @FXML
    private Button btnUpload;
    @FXML
    private ImageView imgPartsPic;
    @FXML
    private TextField txtSeeks01;
    
    private Stage getStage() {
        return (Stage) txtSeeks01.getScene().getWindow();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oListener = (int fnIndex, Object foValue) -> {
            System.out.println("Set Class Value " + fnIndex + "-->" + foValue);
        };

//        oTrans = new VehicleColor(oApp, oApp.getBranchCode(), true); //Initialize ClientMaster
//        oTrans.setCallback(oListener);
//        oTrans.setWithUI(true);
//        loadItemList();

        //Button SetOnAction using cmdButton_Click() method
        btnClose.setOnAction(this::cmdButton_Click);
        btnAdd.setOnAction(this::cmdButton_Click);
        btnEdit.setOnAction(this::cmdButton_Click);
        btnSave.setOnAction(this::cmdButton_Click);

        pnEditMode = EditMode.UNKNOWN;
        initbutton(pnEditMode);
    }    
    
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
    
    //use for creating new page on pagination 
    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, itemdata.size());
        if(itemdata.size()>0){
           tblItemList.setItems(FXCollections.observableArrayList(itemdata.subList(fromIndex, toIndex))); 
        }
        return tblItemList;

    }

    private void cmdButton_Click(ActionEvent event) {
//        try {
            String lsButton = ((Button) event.getSource()).getId();
            switch (lsButton) {
                case "btnAdd":
//                    if (oTrans.NewRecord()) {
//                        pnEditMode = oTrans.getEditMode();
//                    } else {
//                        ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
//                        return;
//                    }
                    break;
                case "btnEdit":
//                    if (oTrans.UpdateRecord()) {
//                        pnEditMode = oTrans.getEditMode();
//                    } else {
//                        ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
//                        return;
//                    }
                    break;
                case "btnSave":
//                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to save?")) {
//                    } else {
//                        return;
//                    }
//
//                    if (oTrans.SaveRecord()) {
//                        ShowMessageFX.Information(null, pxeModuleName, "Vehicle Make save sucessfully.");
//                        loadItemsList();
//
//                        getSelectedItem();
//                        pnEditMode = oTrans.getEditMode();
//                    } else {
//                        ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
//                        return;
//                    }
                    break;

                case "btnClose":
                    if (ShowMessageFX.OkayCancel(null, "Close Tab", "Are you sure you want to close this Tab?") == true) {
                        if (unload != null) {
                            unload.unloadForm(AnchorMain, oApp, "Customer");
                        } else {
                            ShowMessageFX.Warning(getStage(), "Please notify the system administrator to configure the null value at the close button.", "Warning", pxeModuleName);
                        }
                        break;
                    } else {
                        return;
                    }

                default:
                    ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                    break;

            }

            initbutton(pnEditMode);
//        } catch (SQLException ex) {
//            Logger.getLogger(ItemEntryFormController.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public void loadVehicleParameterList() {
//        try {
//            /*Populate table*/
//            itemdata.clear();
//            String sRecStat = "";
//            if (oTrans.LoadList()) {
//                for (lnCtr = 1; lnCtr <= oTrans.getItemCount(); lnCtr++) {
//                    itemdata.add(new ItemEntryTableList(
//                            String.valueOf(lnCtr) //Row
//                    ));
//                }
//                initItemList();
//            } else {
//                ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
//                return;
//            }
//        } catch (SQLException e) {
//            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
//        }

    }

    private void initItemList() {
        boolean lbShow = (pnEditMode == EditMode.READY || pnEditMode == EditMode.UPDATE);
        tblindex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
        tblindex02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
        tblindex03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));

        tblItemList.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblItemList.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

        tblItemList.setItems(itemdata);
    }

    //Populate Text Field Based on selected transaction in table
    private void getSelectedItem(String TransNo) {
//        oldTransNo = TransNo;
//        if (oTrans.OpenRecord(TransNo)) {
//            if (itemdata.get(lnRow).getTblindex03().equals("Y")) {
//                pnEditMode = oTrans.getEditMode();
//            } else {
//                pnEditMode = EditMode.UNKNOWN;
//            }
//
//        }
//        initbutton(pnEditMode);
    }
    
    private void autoSearch(TextField txtField){
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        boolean fsCode = true;
        txtField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(clients-> {
            // If filter text is empty, display all persons.
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }
            // Compare order no. and last name of every person with filter text.
            String lowerCaseFilter = newValue.toLowerCase();
            switch (lnIndex){
                    case 2:
                        if(lnIndex == 2){
//                            return (clients.getTblindex10().toLowerCase().contains(lowerCaseFilter)); // Does not match.   
                        }else {
//                            return (clients.getTblindex10().toLowerCase().contains(lowerCaseFilter)); // Does not match.
                        }   
                    default:
                    return true;            
            }
            });

        changeTableView(0, ROWS_PER_PAGE);
      });
      loadTab();
    } 
     
    private void loadTab(){
        int totalPage = (int) (Math.ceil(itemdata.size() * 1.0 / ROWS_PER_PAGE));
        pagination.setPageCount(totalPage);
        pagination.setCurrentPageIndex(0);
        changeTableView(0, ROWS_PER_PAGE);
        pagination.currentPageIndexProperty().addListener(
                (observable, oldValue, newValue) -> changeTableView(newValue.intValue(), ROWS_PER_PAGE));

    } 
     
    private void changeTableView(int index, int limit) {
        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, itemdata.size());

        int minIndex = Math.min(toIndex, filteredData.size());
        SortedList<ItemEntryTableList> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(tblItemList.comparatorProperty());
        tblItemList.setItems(sortedData); 
    }

     @FXML
    private void tblVhclEntryList_Clicked(MouseEvent event) {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
             if(ShowMessageFX.OkayCancel(null, pxeModuleName, "You have unsaved data, are you sure you want to continue?") == true){   
            } else
                return;
        }

        pnRow = tblItemList.getSelectionModel().getSelectedIndex(); 
        pagecounter = pnRow + pagination.getCurrentPageIndex() * ROWS_PER_PAGE;
        if (pagecounter >= 0){
            if(event.getClickCount() > 0){
//                getSelectedItem(filteredData.get(pagecounter).getTblindex11()); //Populate field based on selected Item
                tblItemList.setOnKeyReleased((KeyEvent t)-> {
                    KeyCode key = t.getCode();
                    switch (key){
                        case DOWN:
                            pnRow = tblItemList.getSelectionModel().getSelectedIndex();
                            pagecounter = pnRow + pagination.getCurrentPageIndex() * ROWS_PER_PAGE;
                            if (pagecounter == tblItemList.getItems().size()) {
                                pagecounter = tblItemList.getItems().size();
//                                getSelectedItem(filteredData.get(pagecounter).getTblindex11());
                            }else {
                               int y = 1;
                              pnRow = pnRow + y;
//                                getSelectedItem(filteredData.get(pagecounter).getTblindex11());
                            }
                            break;
                        case UP:
                            pnRow = tblItemList.getSelectionModel().getSelectedIndex();
                            pagecounter = pnRow + pagination.getCurrentPageIndex() * ROWS_PER_PAGE;
//                            getSelectedItem(filteredData.get(pagecounter).getTblindex11());
                            break;
                        default:
                          return; 
                    }
                });
            } 
            pnEditMode = EditMode.READY;
            initbutton(pnEditMode);  
        }     
    }
    
    /*Set TextField Value to Master Class*/
    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {
//        try {
//            TextField txtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
//            int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
//            String lsValue = txtField.getText();
//
//            if (lsValue == null) {
//                return;
//            }
//            if (!nv) {
//                /*Lost Focus*/
//                switch (lnIndex) {
//                    case 2:
//                        oTrans.setMaster(lnIndex, lsValue); //Handle Encoded Value
//                        break;
//                }
//
//            } else {
//                txtField.selectAll();
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(ItemEntryFormController.class.getName()).log(Level.SEVERE, null, ex);
//        }
    };

    private void initbutton(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

        btnEdit.setDisable(true);
        btnSave.setDisable(!lbShow);
        btnAdd.setDisable(lbShow);

        if (fnValue == EditMode.READY) {
            btnEdit.setDisable(false);
        }
    }

    
}
