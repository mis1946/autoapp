/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.auto.app.sales;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.auto.app.bank.BankEntryTableList;
import org.rmj.auto.app.views.ScreenInterface;
import org.rmj.auto.app.views.unloadForm;

/**
 * Vehicle Sales Approval Controller class
 *
 * @author John Dave
 */
public class VehicleSalesApprovalController implements Initializable,ScreenInterface {
    private GRider oApp;
    
    private final String pxeModuleName = "Vehicle Reservation Approval"; //Form Title
    
    unloadForm unload = new unloadForm(); //Used in Close Button
    
    @FXML
    private AnchorPane AnchorMain;
    
    
    ObservableList<String> cFilter = FXCollections.observableArrayList("Advance Slip Date", "Advances Slip No.", "Advances Type",
                                                                       "Customer Name","Employee Name","Unit Description");
        
    ObservableList<String> cType = FXCollections.observableArrayList("Reservation","Deposit");
    
    private ObservableList<VehicleSalesApprovalTable> vhlApprovalData = FXCollections.observableArrayList();
    private FilteredList<VehicleSalesApprovalTable> filteredData;
    @FXML
    private Button btnApproved;
    @FXML
    private Button btnFilter;
    @FXML
    private Button btnRefresh;
    @FXML
    private Button btnClose;
    @FXML
    private TextField txtFieldSearch;
    @FXML
    private ComboBox<String> comboFilter;
    @FXML
    private ComboBox<String> comboType;
    @FXML
    private DatePicker fromDate;
    @FXML
    private DatePicker toDate;
    @FXML
    private Label lFrom;
    @FXML
    private Label lTo;
    @FXML
    private TableView<VehicleSalesApprovalTable> tblVhclApproval;
    @FXML
    private TableColumn <VehicleSalesApprovalTable, String>tblRow;
    @FXML
    private TableColumn<VehicleSalesApprovalTable, Boolean> tblselected;
    @FXML
    private TableColumn<VehicleSalesApprovalTable, String> tblslipNo;
    @FXML
    private TableColumn<VehicleSalesApprovalTable, String> tbltype;
    @FXML
    private TableColumn<VehicleSalesApprovalTable, String> tblcustomerName;
    @FXML
    private TableColumn<VehicleSalesApprovalTable, String> tblunitDescription;
    @FXML
    private TableColumn<VehicleSalesApprovalTable, String> tblamount;
    @FXML
    private TableColumn<VehicleSalesApprovalTable, String> tblseName;
    @FXML
    private TableColumn<VehicleSalesApprovalTable, LocalDate> tblbranch;
    @FXML
    private CheckBox selectAllCheckBox;
    @FXML
    private TableColumn<VehicleSalesApprovalTable, String> tblslipDate;

     
    ObservableList<VehicleSalesApprovalTable> slipDataList = FXCollections.observableArrayList();
    // Create sample SlipData objects
    VehicleSalesApprovalTable slipData1 = new VehicleSalesApprovalTable("1","001", "Reservation","2023, 1, 20", "John Doe",
        "Unit A", "1000.0", "SE 1", "Branch 1");
    VehicleSalesApprovalTable slipData2 = new VehicleSalesApprovalTable("2","002", "Deposit","2023, 7, 29","Jane Smith",
        "Unit B", "2000.0", "SE 2", "Branch 2");
     VehicleSalesApprovalTable slipData3 = new VehicleSalesApprovalTable("3","003", "Reservation","2023, 4, 28","Dave Smith",
        "Unit C", "3000.0", "SE 3", "Branch 3");
  

    
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        
        filteredData = new FilteredList<>(tblVhclApproval.getItems());
       
        txtFieldSearch.setVisible(false);
        txtFieldSearch.setManaged(false);
        lFrom.setManaged(false);
        lFrom.setVisible(false);
        fromDate.setVisible(false);
        fromDate.setManaged(false);
        lTo.setVisible(false);
        lTo.setManaged(false);
        toDate.setVisible(false);
        toDate.setManaged(false);
        comboType.setVisible(false);
        comboType.setManaged(false);
            
        initCombo();
        comboFilter.setItems(cFilter);
        comboType.setItems(cType);
        //cmdButtons_click properties
        
        slipDataList.addAll(slipData1, slipData2, slipData3);
        loadVhlApprovalTable();
        tblVhclApproval.setItems(slipDataList);
        btnClose.setOnAction(this::cmdButton_Click);
        btnApproved.setOnAction(this::cmdButton_Click);
        btnFilter.setOnAction(this::cmdButton_Click);

        
    }    
   
        private void loadVhlApprovalTable(){
             //try {
                  /*Populate table*/
             
                 vhlApprovalData.clear();
                 
                       for (int i = 1; i <= 5; i++){
          
                          
                            vhlApprovalData.add(new VehicleSalesApprovalTable(
                            String.valueOf(i), //ROW
                            "",
                                    "", 
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    ""

                            ));
                       }
                       initVhlApprovalTable();

                
               
//          } catch (SQLException e) {
//               ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
//          }
     }
    
    private void cmdButton_Click(ActionEvent event) {
    String lsButton = ((Button)event.getSource()).getId();
    switch(lsButton){
        case "btnClose": //close tab
            if(ShowMessageFX.OkayCancel(null, "Close Tab", "Are you sure, do you want to close tab?") == true){
                if (unload != null) {
                    unload.unloadForm(AnchorMain, oApp, pxeModuleName);
                }else {
                    ShowMessageFX.Warning(null, "Warning", "Notify System Admin to Configure Null value at close button.");    
                }
            } else {
                return;
            }
            break;
        case "btnFilter":
                System.out.println("FILTER!");
                String selectedValue = comboType.getValue();

                // Apply the filter predicate based on the selected value
                filteredData.setPredicate(clients -> {
                    if (selectedValue == null || selectedValue.isEmpty()) {
                        // No filter selected, show all data
                        return true;
                    } else {
                        // Filter based on selected value (e.g., "Reservation" or "Deposit")
                        return clients.getTbltype().equalsIgnoreCase(selectedValue);
                    }
                });

          tblVhclApproval.setItems(filteredData);
    
        break;
            
        case "btnApproved":
                System.out.println("THIS IS APPROVED BUTTON!");
            break;
//        case "btnFilter": //filter table
//            // Retrieve filter criteria from text fields, combo box, and date pickers
//            String customerFilter = txtFieldSearch.getText().trim().toLowerCase();
//            String unitFilter = txtFieldSearch.getText().trim().toLowerCase();
//            String typeFilter = comboType.getValue();
//            LocalDate fromDateFilter = fromDate.getValue();
//            LocalDate toDateFilter = toDate.getValue();
//  
//            // Create a list to hold the filtered orders
//            List<Order> filteredOrders = new ArrayList<>();
//
//            // Filter the orders based on the criteria
//            for (Order order : allOrders) {
//                boolean customerMatch = order.getCustomer().toLowerCase().contains(customerFilter);
//                boolean unitMatch = order.getUnit().toLowerCase().contains(unitFilter);
//                boolean typeMatch = typeFilter == null || typeFilter.isEmpty() || order.getType().equals(typeFilter);
//                boolean dateMatch = true;
//                if (fromDateFilter != null) {
//                    if (!toDateFilter.isEmpty()) {
//                        dateMatch = order.getDate().compareTo(fromDateFilter) >= 0 && order.getDate().compareTo(toDateFilter) <= 0;
//                    } else {
//                        dateMatch = order.getDate().compareTo(fromDateFilter) >= 0;
//                    }
//                } else if (!toDateFilter.isEmpty()) {
//                    dateMatch = order.getDate().compareTo(toDateFilter) <= 0;
//                }
//                if (customerMatch && unitMatch && typeMatch && dateMatch) {
//                    filteredOrders.add(order);
//                }
//            }
//
//            // Update the table view with the filtered data
//            tableView.setItems(filteredOrders);
//            break;
    }
}
    private Stage getStage(){
          return (Stage) txtFieldSearch.getScene().getWindow();
     }
    
    public void initCombo(){
        comboFilter.setOnAction(e -> {
            String selectedFilter = comboFilter.getSelectionModel().getSelectedItem();

            // Hide all controls first  
            txtFieldSearch.setVisible(false);
            txtFieldSearch.setManaged(false);
            lFrom.setManaged(false);
            lFrom.setVisible(false);
            fromDate.setVisible(false);
            fromDate.setManaged(false);
            lTo.setVisible(false);
            lTo.setManaged(false);
            toDate.setVisible(false);
            toDate.setManaged(false);
            comboType.setVisible(false);
            comboType.setManaged(false);

            // Show relevant controls based on selected filter
            switch (selectedFilter) {
                case "Advances Slip No.":
                    txtFieldSearch.setVisible(true);
                    txtFieldSearch.setManaged(true);
                    break;
                case "Advance Slip Date":
                    lFrom.setVisible(true);
                    lFrom.setManaged(true);
                    fromDate.setVisible(true);
                    fromDate.setManaged(true);
                    lTo.setVisible(true);
                    lTo.setManaged(true);
                    toDate.setVisible(true);
                    toDate.setManaged(true);
                    break;
                case "Advances Type":
                    comboType.setVisible(true);
                    comboType.setManaged(true);
                    break;
                case "Customer Name":
                    txtFieldSearch.setVisible(true);
                    txtFieldSearch.setManaged(true);
                    break;
                case "Employee Name":
                    txtFieldSearch.setVisible(true);
                    txtFieldSearch.setManaged(true);
                    break;
                case "Unit Description":
                    txtFieldSearch.setVisible(true);
                    txtFieldSearch.setManaged(true);
                    break;
            }
        });
    }

 
    @Override
    public void setGRider(GRider foValue) {
          oApp = foValue;
     }    
 
      private void initVhlApprovalTable() {
          tblRow.setCellValueFactory(new PropertyValueFactory<>("tblRow"));  //Row
          tblselected.setCellValueFactory(new PropertyValueFactory<>("select"));
          selectAllCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>(){
              @Override
              public void changed(javafx.beans.value.ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    ObservableList<VehicleSalesApprovalTable> items = tblVhclApproval.getItems();
                     
                    for(VehicleSalesApprovalTable item : items){
                        {
                            if(selectAllCheckBox.isSelected()){
                                item.getSelect().setSelected(true);
                            }
                            else{
                                item.getSelect().setSelected(false);
                            }
                        }
                    }
              }
          });
          
          tblslipNo.setCellValueFactory(new PropertyValueFactory<>("tblslipNo")); 
          tbltype.setCellValueFactory(new PropertyValueFactory<>("tbltype")); 
          tblslipDate.setCellValueFactory(new PropertyValueFactory<>("tblslipDate"));
    
          tblcustomerName.setCellValueFactory(new PropertyValueFactory<>("tblcustomerName")); 
          tblunitDescription.setCellValueFactory(new PropertyValueFactory<>("tblunitDescription")); 
          tblamount.setCellValueFactory(new PropertyValueFactory<>("tblamount")); 
          tblseName.setCellValueFactory(new PropertyValueFactory<>("tblseName")); 
          tblbranch.setCellValueFactory(new PropertyValueFactory<>("tblbranch"));
          
      }
}
