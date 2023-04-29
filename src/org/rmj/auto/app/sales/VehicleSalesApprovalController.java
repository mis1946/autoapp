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
    @FXML
    private Button btnFilter1; // For filtering with txtField
    @FXML
    private Button btnFilter2; // For Filteriing with Combo Box
    @FXML
    private Button btnFilter3; // For Filteriing with Date

     
    ObservableList<VehicleSalesApprovalTable> slipDataList = FXCollections.observableArrayList();
    // Create sample SlipData objects
    VehicleSalesApprovalTable slipData1 = new VehicleSalesApprovalTable("1","001", "Reservation","2023-04-20", "John Doe",
        "Unit A", "1000.0", "SE 1", "Branch 1");
    VehicleSalesApprovalTable slipData2 = new VehicleSalesApprovalTable("2","002", "Deposit","2023-02-20","Jane Smith",
        "Unit B", "2000.0", "SE 2", "Branch 2");
     VehicleSalesApprovalTable slipData3 = new VehicleSalesApprovalTable("3","003", "Reservation","2023-07-20","Dave Smith",
        "Unit C", "3000.0", "SE 3", "Branch 3");
   
   
  

    
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        
        btnFilter3.setVisible(false);
        btnFilter3.setManaged(false);
        btnFilter1.setVisible(false);
        btnFilter1.setManaged(false);
        btnFilter2.setVisible(false);
        btnFilter2.setManaged(false);
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
        btnFilter1.setOnAction(this::cmdButton_Click);
        btnFilter2.setOnAction(this::cmdButton_Click);
        btnFilter3.setOnAction(this::cmdButton_Click);
        btnRefresh.setOnAction(this::cmdButton_Click);

        
    }    
   
//    private LocalDate parseDate(String dateString) {
//            String[] parts = dateString.split(",");
//            int year = Integer.parseInt(parts[0].trim());
//            int month = Integer.parseInt(parts[1].trim());
//            int day = Integer.parseInt(parts[2].trim());
//
//            return LocalDate.of(year, month, day);
//        }
//            
            
            
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
        case "btnFilter1": //btn filter for txtfield
            
                String filterText = txtFieldSearch.getText().trim().toLowerCase();

                 // Initialize the filteredData variable
                     FilteredList<VehicleSalesApprovalTable> filteredTxtField = new FilteredList<>(slipDataList);

                     // Apply the filter predicate based on the entered text
                     filteredTxtField.setPredicate(slipData -> {
                         if (filterText.isEmpty()) {
                             // No filter text entered, show all data
                             return true;
                         } else {
                             // Filter based on Slip No, Customer Name, Unit Description, and SE Name
                             String slipNo = slipData.getTblslipNo().toLowerCase();
                             String customerName = slipData.getTblcustomerName().toLowerCase();
                             String unitDescription = slipData.getTblunitDescription().toLowerCase();
                             String seName = slipData.getTblseName().toLowerCase();

                             return slipNo.contains(filterText)
                                     || customerName.contains(filterText)
                                     || unitDescription.contains(filterText)
                                     || seName.contains(filterText);
                         }
                     });

                 tblVhclApproval.setItems(filteredTxtField);
        break;
        case "btnFilter2": //btn filter for comboBox
         String selectedType = comboType.getValue();
                    if (selectedType == null) {
                        // No type selected, show all data
                        tblVhclApproval.setItems(slipDataList);
                    } else {
                        // Filter data based on selected type
                        ObservableList<VehicleSalesApprovalTable> filteredCombo = FXCollections.observableArrayList();
                        for (VehicleSalesApprovalTable slipData : slipDataList) {
                            if (slipData.getTbltype().equals(selectedType)) {
                                filteredCombo.add(slipData);
                            }
                        }
                        tblVhclApproval.setItems(filteredCombo);
                    }
        
        break;
        case "btnFilter3":
                btnFilter3.setOnAction(clients -> {
                    LocalDate filterFromDate = fromDate.getValue();
                    LocalDate filterToDate = toDate.getValue();

                    ObservableList<VehicleSalesApprovalTable> filteredDate = FXCollections.observableArrayList();
                    for (VehicleSalesApprovalTable slipData : slipDataList) {
                        LocalDate slipDate = LocalDate.parse(slipData.getTblslipDate());

                        if (filterFromDate == null || slipDate.isAfter(filterFromDate.minusDays(1))) {
                            if (filterToDate == null || slipDate.isBefore(filterToDate.plusDays(1))) {
                                filteredDate.add(slipData);
                            }
                        }
                    }

                    tblVhclApproval.setItems(filteredDate);
                });
        break;   
        case "btnApproved":
                    btnApproved.setOnAction(clients -> {
                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to approve?")) {

                    ObservableList<VehicleSalesApprovalTable> selectedItems = FXCollections.observableArrayList();
                        
                    for (VehicleSalesApprovalTable item : tblVhclApproval.getItems()) {
                        if (item.getSelect().isSelected()) {
                            selectedItems.add(item);
                        }
                    }

                    if (selectedItems.isEmpty()) {
                         ShowMessageFX.Information(null, pxeModuleName, "No items selected to approve.");
                     } else {
                         // Perform approval logic here
                         tblVhclApproval.getItems().removeAll(selectedItems);
                         tblVhclApproval.refresh();
                     }
                 }
             });
        break;
        case "btnRefresh":

                // Clear the combo box selection
                comboType.getSelectionModel().clearSelection();

                // Clear the text field
                txtFieldSearch.clear();

                // Clear the date picker values
                fromDate.setValue(null);
                toDate.setValue(null);

                // Set the table items back to the original data
                tblVhclApproval.setItems(slipDataList);

            break;
            
//       
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
            
            btnFilter1.setVisible(false);
            btnFilter1.setManaged(false);
            btnFilter2.setVisible(false);
            btnFilter2.setManaged(false);
            btnFilter3.setVisible(false);
            btnFilter3.setManaged(false);
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
                    txtFieldSearch.setText("");
                    txtFieldSearch.setVisible(true);
                    txtFieldSearch.setManaged(true);
                    btnFilter1.setVisible(true);
                    btnFilter1.setManaged(true);
                    tblVhclApproval.setItems(slipDataList);
                    break;
                case "Advance Slip Date":
                    btnFilter3.setVisible(true);
                    btnFilter3.setManaged(true);
                    lFrom.setVisible(true);
                    lFrom.setManaged(true);
                    fromDate.setVisible(true);
                    fromDate.setManaged(true);
                    lTo.setVisible(true);
                    lTo.setManaged(true);
                    toDate.setVisible(true);
                    toDate.setManaged(true);
                    tblVhclApproval.setItems(slipDataList); 
                    break;
                case "Advances Type":
                    comboType.setVisible(true);
                    comboType.setManaged(true);
                    btnFilter2.setVisible(true);
                    btnFilter2.setManaged(true);
                    tblVhclApproval.setItems(slipDataList);
                    break;
                case "Customer Name":
                    txtFieldSearch.setText("");
                    txtFieldSearch.setVisible(true);
                    txtFieldSearch.setManaged(true);
                    btnFilter1.setVisible(true);
                    btnFilter1.setManaged(true);
                    tblVhclApproval.setItems(slipDataList);
       
                    break;
                case "Employee Name":
                    txtFieldSearch.setText("");
                    txtFieldSearch.setVisible(true);
                    txtFieldSearch.setManaged(true);
                    btnFilter1.setVisible(true);
                    btnFilter1.setManaged(true);
                    tblVhclApproval.setItems(slipDataList);
              
                    break;
                case "Unit Description":
                    txtFieldSearch.setText("");
                    txtFieldSearch.setVisible(true);
                    txtFieldSearch.setManaged(true);
                    btnFilter1.setVisible(true);
                    btnFilter1.setManaged(true);
                    tblVhclApproval.setItems(slipDataList);
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
