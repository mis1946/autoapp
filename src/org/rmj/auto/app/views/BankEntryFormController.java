/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.auto.app.views;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;

/**
 * FXML Controller class
 *
 * @author User
 */
public class BankEntryFormController implements Initializable, ScreenInterface{
    private GRider oApp;
//  private BankEntry oTrans;
    unloadForm unload = new unloadForm(); //Object for closing form
    private final String pxeModuleName = "Bank Entry"; //Form Title
    private MasterCallback oListener;
    
    
    private int pnEditMode;//Modifying fields
    private int pnRow = -1;
    private int oldPnRow = -1;
    private int lnCtr = 0;
    private int pagecounter;
    
    
    private static final int ROWS_PER_PAGE = 50;
    
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private TextField txtField02;
    @FXML
    private Pagination pagination;
    @FXML
    private TableView<?> tblBankEntry;
    @FXML
    private TableColumn<?, ?> tblindex01;
    @FXML
    private TableColumn<?, ?> tblindex02;
    @FXML
    private TableColumn<?, ?> tblindex03;
    @FXML
    private TableColumn<?, ?> tblindex04;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnClose;
    @FXML
    private TextField txtField03;
    @FXML
    private TextField txtField04;
    @FXML
    private TextField txtField06;
    @FXML
    private TextField txtField05;
    @FXML
    private TextField txtField08;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
          
        oListener = (int fnIndex, Object foValue) -> {
               System.out.println("Set Class Value "  + fnIndex + "-->" + foValue);
          };
        
//          oTrans = new VehicleDescription(oApp, oApp.getBranchCode(), true); //Initialize ClientMaster
//          oTrans.setCallback(oListener);
//          oTrans.setWithUI(true);


//          /*Set Focus to set Value to Class*/
//          txtField02.focusedProperty().addListener(txtField_Focus); // sBankName
//          txtField03.focusedProperty().addListener(txtField_Focus); // sBankCode
//          txtField06.focusedProperty().addListener(txtField_Focus); // sBranchxx no database
//          txtField16.focusedProperty().addListener(txtField_Focus); // sBrgyNamexx
//          txtField06.focusedProperty().addListener(txtField_Focus); // sTownNamexx
//          txtField03.focusedProperty().addListener(txtField_Focus); // sProvName
//          txtField07.focusedProperty().addListener(txtField_Focus); // sZipCode
//          txtField04.focusedProperty().addListener(txtField_Focus); // sContactP
//          txtField08.focusedProperty().addListener(txtField_Focus); // sTelNoxxx
//          txtField09.focusedProperty().addListener(txtField_Focus); // sFaxNoxx
    
//          txtField02.setOnKeyPressed(this::txtField_KeyPressed); // sBankNamexx
//          txtField03.setOnKeyPressed(this::txtField_KeyPressed); // sBrgyNamexx
//          txtField04.setOnKeyPressed(this::txtField_KeyPressed); // sTownNamexx
//          txtField04.setOnKeyPressed(this::txtField_KeyPressed); // sProvNamexx
          
            //Button Click Event
            btnAdd.setOnAction(this::cmdButton_Click);
            btnEdit.setOnAction(this::cmdButton_Click); 
            btnSave.setOnAction(this::cmdButton_Click); 
            btnClose.setOnAction(this::cmdButton_Click); 
            
            /*Clear Fields*/
//            clearFields();
//
//            pnEditMode = EditMode.UNKNOWN;
//            initButton(pnEditMode); 
     
    }    

//     @FXML
//     private void tblBankEntry_Clicked(MouseEvent event) {
//          if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
//               if(ShowMessageFX.OkayCancel(null, pxeModuleName, "You have unsaved data, are you sure you want to continue?") == true){   
//              } else
//                  return;
//          }
//          
//          pnRow = tblVehicleDesc.getSelectionModel().getSelectedIndex(); 
//          pagecounter = pnRow + pagination.getCurrentPageIndex() * ROWS_PER_PAGE;
//          if (pagecounter >= 0){
//               if(event.getClickCount() > 0){
//                    getSelectedItem(filteredData.get(pagecounter).getTblindex11()); //Populate field based on selected Item
//
//                    tblVehicleDesc.setOnKeyReleased((KeyEvent t)-> {
//                        KeyCode key = t.getCode();
//                        switch (key){
//                            case DOWN:
//                                pnRow = tblVehicleDesc.getSelectionModel().getSelectedIndex();
//                                pagecounter = pnRow + pagination.getCurrentPageIndex() * ROWS_PER_PAGE;
//                                if (pagecounter == tblVehicleDesc.getItems().size()) {
//                                    pagecounter = tblVehicleDesc.getItems().size();
//                                    getSelectedItem(filteredData.get(pagecounter).getTblindex11());
//                                }else {
//                                   int y = 1;
//                                  pnRow = pnRow + y;
//                                    getSelectedItem(filteredData.get(pagecounter).getTblindex11());
//                                }
//                                break;
//                            case UP:
//                                pnRow = tblVehicleDesc.getSelectionModel().getSelectedIndex();
//                                pagecounter = pnRow + pagination.getCurrentPageIndex() * ROWS_PER_PAGE;
//                                getSelectedItem(filteredData.get(pagecounter).getTblindex11());
//                                break;
//                            default:
//                              return; 
//                      }
//                    });
//               } 
//               pnEditMode = EditMode.READY;
//               initButton(pnEditMode);  
//          }     
//     }
    
      
  private void cmdButton_Click(ActionEvent event) {
          String lsButton = ((Button)event.getSource()).getId();
          switch(lsButton){
            case "btnAdd":
                break;
            case "btnEdit":
                break;
            case "btnSave":
                break;
            case "btnClose": //close tab
                         if(ShowMessageFX.OkayCancel(null, "Close Tab", "Are you sure, do you want to close tab?") == true){
                               if (unload != null) {
                                    unload.unloadForm(AnchorMain, oApp, pxeModuleName);
                               }else {
                                    ShowMessageFX.Warning(null, "Warning", "Notify System Admin to Configure Null value at close button.");    
                               }
                               break;
                               }else
                           return;
            }
        }
  
    @Override
    public void setGRider(GRider foValue) {
       
    }
    
//    private void loadTab(){
//                int totalPage = (int) (Math.ceil(bankentrydata.size() * 1.0 / ROWS_PER_PAGE));
//                pagination.setPageCount(totalPage);
//                pagination.setCurrentPageIndex(0);
//                changeTableView(0, ROWS_PER_PAGE);
//                pagination.currentPageIndexProperty().addListener(
//                        (observable, oldValue, newValue) -> changeTableView(newValue.intValue(), ROWS_PER_PAGE));
//      
//     } 
    
//      private void changeTableView(int index, int limit) {
//          int fromIndex = index * limit;
//          int toIndex = Math.min(fromIndex + limit, vhcldescdata.size());
//
//          int minIndex = Math.min(toIndex, filteredData.size());
//          SortedList<BankEntryTableList> sortedData = new SortedList<>(
//                  FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
//          sortedData.comparatorProperty().bind(tblBankEntry.comparatorProperty());
//          tblBankEntry.setItems(sortedData); 
//     }
     
//    
//      //use for creating new page on pagination 
//     private Node createPage(int pageIndex) {
//          int fromIndex = pageIndex * ROWS_PER_PAGE;
//          int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, vhcldescdata.size());
//          if(bnkentrydata.size()>0){
//             tblVehicleDesc.setItems(FXCollections.observableArrayList(vhcldescdata.subList(fromIndex, toIndex))); 
//          }
//          return tblVehicleDesc;
//          
//     }
    
    
     /*Populate Text Field Based on selected address in table*/
//     private void getSelectedItem(String TransNo){
//        oldTransNo = TransNo;
//          if (oTrans.OpenRecord(TransNo)){
//               //txtField02.setText(bankentrydata.get(pagecounter).getTblindex10()); //Description 
//               txtField03.setText(bankentrydata.get(pagecounter).getTblindex02()); //Make
//               txtField04.setText(bankentrydata.get(pagecounter).getTblindex09()); //Color
//               txtField06.setText(bankentrydata.get(pagecounter).getTblindex06()); //Type
//               txtField08.setText(bankentrydata.get(pagecounter).getTblindex04()); //Year
//          }
//        
//     }
//     private void loadBankEntryField(){
//          try {
//               txtField03.setText((String) oTrans.getMaster(15));
//               txtField04.setText((String) oTrans.getMaster(16));
//               txtField05.setText((String) oTrans.getMaster(17));
//               txtField06.setText((String) oTrans.getMaster(18));
//               txtField08.setText(oTrans.getMaster(8).toString());
//               
//               
//          } catch (SQLException e) {
//               ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
//          }
//     }
    
    
    
//     /*Set TextField Value to Master Class*/
//     final ChangeListener<? super Boolean> txtField_Focus = (o,ov,nv)->{
//          try{
//            TextField txtField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
//            int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
//            String lsValue = txtField.getText();
//            
//            if (lsValue == null) return;
//            if(!nv){ /*Lost Focus*/
//                    switch (lnIndex){
//                         case 3: //sMakeIDxx
//                              oTrans.setMaster(15, lsValue); //Handle Encoded Value
//                              break;
//                         case 4: //sModelIDx
//                              oTrans.setMaster(16, lsValue); //Handle Encoded Value
//                              break;
//                         case 5: //sColorIDx
//                              oTrans.setMaster(17, lsValue); //Handle Encoded Value
//                              break;
//                         case 6: //sTypeIDxx
//                              oTrans.setMaster(18, lsValue); //Handle Encoded Value
//                              break;
//                         case 8: //nYearModl
//                              if (lsValue.trim().equals("")){
//                                   oTrans.setMaster(lnIndex,  0); //Handle Encoded Value     
//                              } else {
//                                   oTrans.setMaster(lnIndex,  Integer.parseInt(lsValue)); //Handle Encoded Value
//                              }
//                              break;
//                        
//                    }
//                
//            } else
//               txtField.selectAll();
//          } catch (SQLException ex) {
//            Logger.getLogger(VehicleDescriptionFormController.class.getName()).log(Level.SEVERE, null, ex);
//          }
//     };
//      private void txtField_KeyPressed(KeyEvent event){
//          TextField txtField = (TextField)event.getSource();
//          int lnIndex = Integer.parseInt(((TextField)event.getSource()).getId().substring(8,10));
//          
//          try{
//               switch (event.getCode()){
//                    case F3:
//                         switch (lnIndex){ 
//                              case 3: //BankName
//                                   if (oTrans.searchBankName(txtField03.getText())){
//                                        loadBankEntryField();
//                                   } else 
//                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
//                              
//                              break;
//                              case 4: //HouseNo
//                                   if (oTrans.searchHouseNo(txtField04.getText())){
//                                       loadBankEntryField();
//                                   } else 
//                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
//                              
//                              break;
//                              
//                              case 5: //Municipality
//                                   if (oTrans.searchMunicipality(txtField05.getText())){
//                                       loadBankEntryField();
//                                   } else 
//                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
//           
//                              break;
//                              
//                              case 6: //Province
//                                   if (oTrans.searchProvince(txtField06.getText())){
//                                        loadBankEntryField();
//                                        pnEditMode = oTrans.getEditMode();
//                                   } else 
//                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
//                              break;     
//                         } 
//                    break;
//                    case TAB:
//                    case ENTER:
//                         switch (lnIndex){ 
//                                  case 3: //BankName
//                                   if (oTrans.searchBankName(txtField03.getText())){
//                                        loadBankEntryField();
//                                   } else 
//                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
//                              
//                              break;
//                              case 4: //HouseNo
//                                   if (oTrans.searchHouseNo(txtField04.getText())){
//                                       loadBankEntryField();
//                                   } else 
//                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
//                              
//                              break;
//                              
//                              case 5: //Municipality
//                                   if (oTrans.searchMunicipality(txtField05.getText())){
//                                       loadBankEntryField();
//                                   } else 
//                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
//           
//                              break;
//                              
//                              case 6: //Province
//                                   if (oTrans.searchProvince(txtField06.getText())){
//                                        loadBankEntryField();
//                                        pnEditMode = oTrans.getEditMode();
//                                   } else 
//                                       ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
//                              break; 
//                         } 
//                         break;
//               }
//          }catch(SQLException e){
//                ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
//          }
//          
//          switch (event.getCode()){
//          case ENTER:
//          case DOWN:
//              CommonUtils.SetNextFocus(txtField);
//              break;
//          case UP:
//              CommonUtils.SetPreviousFocus(txtField);
//          }
//          
//     }
    
//        
//            /*Enabling / Disabling Fields*/
//            private void initButton(int fnValue){
//             pnRow = 0;
//             /* NOTE:
//                  lbShow (FALSE)= invisible
//                  !lbShow (TRUE)= visible
//             */
//             boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
//
//             /*Vehicle Description*/
//             txtField03.setDisable(!lbShow); // sMakeIDxx
//             txtField04.setDisable(!lbShow); // sModelIDx
//             txtField06.setDisable(!lbShow); // sColorIDx
//             txtField05.setDisable(!lbShow); // sTypeIDxx
//             txtField08.setDisable(!lbShow); // nYearModl
//
//             btnAdd.setVisible(!lbShow);
//             btnAdd.setManaged(!lbShow);
//             //if lbShow = false hide btn          
//             btnEdit.setVisible(false); 
//             btnEdit.setManaged(false);
//             btnSave.setVisible(lbShow);
//             btnSave.setManaged(lbShow);
//
//             if (fnValue == EditMode.READY) { //show edit if user clicked save / browse
//                  btnEdit.setVisible(true); 
//                  btnEdit.setManaged(true);
//             }
//        }
      
//          /*Clear Fields*/
//          public void clearFields(){
//          pnRow = 0;
//          /*clear tables*/
//          
//          txtField02.clear(); // sDescript
//          txtField03.clear(); // sMakeIDxx
//          txtField04.clear(); // sModelIDx
//          txtField06.clear(); // sColorIDx
//          txtField05.clear(); // sTypeIDxx
//          txtField08.clear(); // nYearModl

    
    
}
