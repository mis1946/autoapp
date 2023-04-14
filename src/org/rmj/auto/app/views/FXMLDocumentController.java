/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package org.rmj.auto.app.views;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.auto.app.sales.InquiryFormController;
import org.rmj.auto.app.sales.SalesAgentFormController;
import org.rmj.auto.app.sales.UnitReceivingFormController;

/**
 *
 * @author xurpas
 */
public class FXMLDocumentController implements Initializable, ScreenInterface {
    
     private GRider oApp;
     
     @FXML
     private Label AppUser;
     @FXML
     private Pane view;
     @FXML
     private Label DateAndTime;
     @FXML
     public StackPane workingSpace;
     @FXML
     private Pane btnClose;
     @FXML
     private Pane btnMin;
     @FXML
     private MenuItem mnuSupplierInfo;
     @FXML
     private TabPane tabpane;
     @FXML
     private MenuItem mnuCustomerInfo;
     @FXML
     private MenuItem mnuVhclDesc;
     @FXML
     private Menu menusales;
     @FXML
     private MenuItem mnuInquiry;
     @FXML
     private MenuItem mnuSalesAgent;
     @FXML
     private MenuItem mnuUnitRecv;
     
     @Override
     public void initialize(URL url, ResourceBundle rb) {
          // TODO
          //Load Main Frame
          setScene(loadAnimateAnchor("FXMLMainScreen.fxml"));
          getTime();

          ResultSet name;
          String lsQuery = "SELECT b.sCompnyNm " +
                            " FROM xxxSysUser a" +
                            " LEFT JOIN GGC_ISysDBF.Client_Master b" +  
                                " ON a.sEmployNo  = b.sClientID" +
                            " WHERE a.sUserIDxx = " + SQLUtil.toSQL(oApp.getUserID());
          name = oApp.executeQuery(lsQuery);
          try {
            if(name.next()){
                AppUser.setText(name.getString("sCompnyNm") + " || " + oApp.getBranchName());
                System.setProperty("user.name", name.getString("sCompnyNm"));   
            }             
          } catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
          }
          /*USER ACCESS*/
          initMenu();
          
     }  

     //@Override
     public void setGRider(GRider foValue) {
          oApp = foValue;
     }
     
     /*LOAD ANIMATE FOR ANCHORPANE MAIN HOME*/
     public AnchorPane loadAnimateAnchor(String fsFormName){

          ScreenInterface fxObj = getController(fsFormName);
          fxObj.setGRider(oApp);

          FXMLLoader fxmlLoader = new FXMLLoader();
          fxmlLoader.setLocation(fxObj.getClass().getResource(fsFormName));
          fxmlLoader.setController(fxObj);  

          AnchorPane root;
          try {
              root = (AnchorPane) fxmlLoader.load();
              FadeTransition ft = new FadeTransition(Duration.millis(1500));
              ft.setNode(root);
              ft.setFromValue(1);
              ft.setToValue(1);
              ft.setCycleCount(1);
              ft.setAutoReverse(false);
              ft.play();
              return root;
          } catch (IOException ex) {
              System.err.println(ex.getMessage());
          }

          return null;
     }
     
     /*SET SCENE FOR WORKPLACE - STACKPANE - ANCHORPANE*/
     public void setScene(AnchorPane foPane){
          workingSpace.getChildren().clear();
          workingSpace.getChildren().add(foPane);
     }
     
     /*LOAD ANIMATE FOR TABPANE*/
     public TabPane loadAnimate(String fsFormName){
          //set fxml controller class
          ScreenInterface fxObj = getController(fsFormName);
          fxObj.setGRider(oApp); 

          FXMLLoader fxmlLoader = new FXMLLoader();
          fxmlLoader.setLocation(fxObj.getClass().getResource(fsFormName));
          fxmlLoader.setController(fxObj); 
          
          //Add new tab;
          Tab newTab = new Tab(SetTabTitle(fsFormName));
          newTab.setStyle("-fx-font-weight: bold;");
          try {
               Node content = fxmlLoader.load();
               newTab.setContent(content);
               tabpane.getTabs().add(newTab);
               tabpane.getSelectionModel().select(newTab);
               //newTab.setOnClosed(event -> {
               newTab.setOnCloseRequest(event -> {
                    
                    if(ShowMessageFX.YesNo(null, "Close Tab", "Are you sure, do you want to close tab?") == true){
                         Tabclose();
                    } else {
                        // Cancel the close request
                        event.consume();
                    }
  
               });
               return  (TabPane) tabpane;
          } catch (IOException e) {
               e.printStackTrace();
          }
          
          return null;
     }
    
     public ScreenInterface getController(String fsValue){
          switch (fsValue){
               case "FXMLMainScreen.fxml":
                    return new FXMLMainScreenController();
               case "CustomerForm.fxml":
                    return new CustomerFormController();
//               case "SupplierInfo.fxml":
//                    return new SupplierInfoController();
               case "SalesAgentForm.fxml":
                    return new SalesAgentFormController();
               case "VehicleDescriptionForm.fxml":
                    return new VehicleDescriptionFormController();
               case "UnitReceivingForm.fxml":
                    return new UnitReceivingFormController();
               case "InquiryForm.fxml":
                    return new InquiryFormController();
               
               default:
                    ShowMessageFX.Warning(null, "Warning", "Notify System Admin to Configure Screen Interface for " +fsValue);
                    return null;
          }
     }
     
     //Set tab title
     public String SetTabTitle(String menuaction){
          switch (menuaction){
          case "CustomerForm.fxml":
               return "Customer";
          case "SupplierInfo.fxml":
               return "Supplier";
          case "SalesAgentForm.fxml":
               return "Sales Agent";
          case "VehicleDescriptionForm.fxml":
               return "Vehicle Description";
          case "UnitReceivingForm.fxml":
               return "Unit Receiving";
          case "InquiryForm.fxml":
               return "Inquiry Information";
          default:
               ShowMessageFX.Warning(null, "Warning", "Notify System Admin to Configure Tab Title for " +menuaction);
               return null;
          }
     }
     
     //Load Main Screen if no tab remain 
     public void Tabclose(){
          int tabsize = tabpane.getTabs().size();
          if (tabsize == 1) {
               setScene(loadAnimateAnchor("FXMLMainScreen.fxml"));   
          }
     }
     
     /*SET SCENE FOR WORKPLACE - STACKPANE - TABPANE*/
     public void setScene2(TabPane foPane){
          workingSpace.getChildren().clear();
          workingSpace.getChildren().add(foPane);
     }
     
     /*Check opened tab*/
     public int checktabs(String tabtitle) {
          for (Tab tab : tabpane.getTabs()) {
               if (tab.getText().equals(tabtitle)) {
                    tabpane.getSelectionModel().select(tab);
                    return 0;
               }
          }
          return 1;
     }
     
     public TabPane getTabPane() {
           //return (TabPane) workingSpace.getChildren().add(tabpane);
          workingSpace.getChildren().clear();
          workingSpace.getChildren().add((TabPane) tabpane);
           //return (TabPane) workingSpace.getChildren().get(0);
           return (TabPane) workingSpace.lookup("#tabpane");
     }
     
     public MenuItem getMenuItem() {
          return mnuVhclDesc;
     }
     
     public Menu getMenu() {
          return menusales;
      }
     
     public StackPane getStactPane() {
          return workingSpace;
     }
     
     /*MENU ACTIONS OPENING FXML's*/  
     @FXML
     private void mnuCustomerInfoClick(ActionEvent event) {
          String sformname = "CustomerForm.fxml";
          //check tab
          if (checktabs(SetTabTitle(sformname)) == 1 ) {
               setScene2(loadAnimate(sformname));
          }
     }
     
     @FXML
     private void mnuSupplierInfoClick(ActionEvent event) {
          String sformname = "SupplierInfo.fxml";
          //check tab
          if (checktabs(SetTabTitle(sformname)) == 1 ) {
               setScene2(loadAnimate(sformname));
          }
     }
     
     @FXML
     private void mnuSalesAgentClick(ActionEvent event) {
          String sformname = "SalesAgentForm.fxml";
          //check tab
          if (checktabs(SetTabTitle(sformname)) == 1 ) {
               setScene2(loadAnimate(sformname));
          }
     }
     
     @FXML
     public void mnuVhclDescClick(ActionEvent event) {
          String sformname = "VehicleDescriptionForm.fxml";
          //check tab
          if (checktabs(SetTabTitle(sformname)) == 1 ) {
               setScene2(loadAnimate(sformname));
          }
     }
     
     @FXML
     private void mnuUnitRecvClick(ActionEvent event) {
          String sformname = "UnitReceivingForm.fxml";
          //check tab
          if (checktabs(SetTabTitle(sformname)) == 1 ) {
               setScene2(loadAnimate(sformname));
          }
     }
     
     @FXML
     private void mnuInquiryClick(ActionEvent event) {
          String sformname = "InquiryForm.fxml";
          //check tab
          if (checktabs(SetTabTitle(sformname)) == 1 ) {
               setScene2(loadAnimate(sformname));
          }
     }
       
     /*SET CURRENT TIME*/
     private void getTime(){
          Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {            
          Calendar cal = Calendar.getInstance();
          int second = cal.get(Calendar.SECOND);        
          String temp = "" + second;

          Date date = new Date();
          String strTimeFormat = "hh:mm:";
          String strDateFormat = "MMMM dd, yyyy";
          String secondFormat = "ss";

          DateFormat timeFormat = new SimpleDateFormat(strTimeFormat + secondFormat);
          DateFormat dateFormat = new SimpleDateFormat(strDateFormat);

          String formattedTime= timeFormat.format(date);
          String formattedDate= dateFormat.format(date);

          DateAndTime.setText(formattedDate+ " || " + formattedTime);

          }),
           new KeyFrame(Duration.seconds(1))
          );

          clock.setCycleCount(Animation.INDEFINITE);
          clock.play();
     }

     @FXML
     private void handleButtonMinimizeClick(MouseEvent event) {
          Stage stage = (Stage) btnMin.getScene().getWindow();
          stage.setIconified(true);
     }

     @FXML
     private void handleButtonCloseClick(MouseEvent event) {
          Stage stage = (Stage) btnClose.getScene().getWindow();
          //stage.close();
          //Close Stage
          event.consume();
          logout(stage);
     }    
     //close whole application
     public void logout(Stage stage){

          if(ShowMessageFX.YesNo(null, "Exit", "Are you sure, do you want to close?") == true){
               System.out.println("You successfully logged out!");
               stage.close();
          }
     }

     /*USER ACCESS*/
     private void initMenu(){
     
     }

     

     

  
    
}
