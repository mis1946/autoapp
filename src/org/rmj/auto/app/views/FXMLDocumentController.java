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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.agentfx.callback.IFXML;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.auto.app.bank.BankEntryFormController;
import org.rmj.auto.app.cashiering.InvoiceFormController;
import org.rmj.auto.app.cashiering.VehicleSalesInvoiceFormController;
import org.rmj.auto.app.insurance.InsuranceInformationController;
import org.rmj.auto.app.parts.BinEntryParamController;
import org.rmj.auto.app.parts.BrandEntryParamController;
import org.rmj.auto.app.parts.CategoryEntryParamController;
import org.rmj.auto.app.parts.InvTypeEntryParamController;
import org.rmj.auto.app.parts.InventoryLocationParamController;
import org.rmj.auto.app.parts.ItemEntryFormController;
import org.rmj.auto.app.parts.MeasurementEntryParamController;
import org.rmj.auto.app.parts.SectionEntryParamController;
import org.rmj.auto.app.parts.WareHouseEntryParamController;
import org.rmj.auto.app.sales.InquiryFormController;
import org.rmj.auto.app.sales.SalesAgentFormController;
import org.rmj.auto.app.sales.UnitDeliveryReceiptFormController;
import org.rmj.auto.app.sales.UnitReceivingFormController;
import org.rmj.auto.app.sales.VSPAddOnsApprovalController;
import org.rmj.auto.app.sales.VSPFormController;
import org.rmj.auto.app.sales.VSPPendingPartsRequestController;
import org.rmj.auto.app.sales.VehicleEntryFormController;
import org.rmj.auto.app.sales.VehicleSalesApprovalController;
import org.rmj.auto.app.service.JobOrderFormController;
import org.rmj.auto.app.views.ActivityFormController;
import org.rmj.auto.json.TabsStateManager;

/**
 *
 * @author xurpas
 */
public class FXMLDocumentController implements Initializable, ScreenInterface {

    private GRider oApp;
    private int targetTabIndex = -1;
    private double tabsize;
    private String sSalesInvoiceType = "";
    private String sVehicleInfoType = "";
    private String sJobOrderType = "";
    private String sSalesInfoType = "";

    // Variables to track the window movement
    private double xOffset = 0;
    private double yOffset = 0;
    FXMLMenuParameterForm param = new FXMLMenuParameterForm();
    List<String> tabName = new ArrayList<>();
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
    @FXML
    private MenuItem mnuVhclEntry;
    @FXML
    private MenuItem mnuBank;
    @FXML
    private MenuItem mnuVhclRsrvApp;
    @FXML
    private MenuItem mnuActivity;
    @FXML
    private MenuItem mnuActivityApproval;
    @FXML
    private MenuItem mnuActType;
    @FXML
    private MenuItem mnuItemEntry;
    @FXML
    private MenuItem mnuBinEntry;
    @FXML
    private MenuItem mnuInvLocEntry;
    @FXML
    private MenuItem mnuMeasureEntry;
    @FXML
    private MenuItem mnuSectionEntry;
    @FXML
    private MenuItem mnuWarehsEntry;
    @FXML
    private MenuItem mnuVhclMakeEntry;
    @FXML
    private MenuItem mnuVhclModelEntry;
    @FXML
    private MenuItem mnuVhclTypeEntry;
    @FXML
    private MenuItem mnuVhclColorEntry;
    @FXML
    private MenuItem mnuVhclEngFrmEntry;
    @FXML
    private MenuItem mnuBrandEntry;
    @FXML
    private MenuItem mnuCategoryEntry;
    @FXML
    private MenuItem mnuInvTypeEntry;
    @FXML
    private MenuItem mnuUnitDeliveryReceipt;
    @FXML
    private MenuItem mnuAckReceipt;
    @FXML
    private MenuItem mnuBillingStmt;
    @FXML
    private MenuItem mnuColReceipt;
    @FXML
    private MenuItem mnuOfcReceipt;
    @FXML
    private MenuItem mnuPartsSalesInv;
    @FXML
    private MenuItem mnuVSPEntry;
    @FXML
    private MenuItem mnuVhclSalesInv;
    @FXML
    private MenuItem mnuCustVhclInfo;
    @FXML
    private MenuItem mnuSalesJobOrder;
    @FXML
    private MenuItem mnuSalesPartsRequest;
    @FXML
    private MenuItem mnuServiceJobOrder;
    @FXML
    private MenuItem mnuAddOnsApproval;
    @FXML
    private MenuItem mnuSalesExecutive;
    @FXML
    private Menu menusales1;
    @FXML
    private MenuItem mnuInsurInfo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //Load Main Frame
        setScene(loadAnimateAnchor("FXMLMainScreen.fxml"));
        getTime();

        ResultSet name;
        String lsQuery = "SELECT b.sCompnyNm "
                + " FROM xxxSysUser a"
                + " LEFT JOIN GGC_ISysDBF.Client_Master b"
                + " ON a.sEmployNo  = b.sClientID"
                + " WHERE a.sUserIDxx = " + SQLUtil.toSQL(oApp.getUserID());
        name = oApp.executeQuery(lsQuery);
        try {
            if (name.next()) {
                AppUser.setText(name.getString("sCompnyNm") + " || " + oApp.getBranchName());
                System.setProperty("user.name", name.getString("sCompnyNm"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*USER ACCESS*/
        initMenu();

        // set up the drag and drop listeners on the tab pane
        tabpane.setOnDragDetected(event -> {
            Dragboard db = tabpane.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(tabpane.getSelectionModel().getSelectedItem().getText());
            db.setContent(content);
            event.consume();
        });

        tabpane.setOnDragOver(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
                event.consume();
            }
        });

        tabpane.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                String tabText = db.getString();
                int draggedTabIndex = findTabIndex(tabText);
                //double mouseP , mousePCom;
                double mouseX = event.getX();
                double mouseY = event.getY();
                Bounds headerBounds = tabpane.lookup(".tab-header-area").getBoundsInParent();
                Point2D mouseInScene = tabpane.localToScene(mouseX, mouseY);
                Point2D mouseInHeader = tabpane.sceneToLocal(mouseInScene);
                double tabHeaderHeight = tabpane.lookup(".tab-header-area").getBoundsInParent().getHeight();
                System.out.println("mouseY " + mouseY);
                System.out.println("tabHeaderHeight " + tabHeaderHeight);

//                    mouse is over the tab header area
//                    mouseP = ((mouseInHeader.getX() / headerBounds.getWidth()));
//                    tabsize = tabpane.getTabs().size();
//                    mousePCom = mouseP * tabsize;
//                    targetTabIndex = (int) Math.round(mousePCom) ;
//
//                    double tabWidth = headerBounds.getWidth() / tabpane.getTabs().size();
//                    targetTabIndex = (int) ((mouseX - headerBounds.getMinX()) / tabWidth);
                targetTabIndex = (int) (mouseX / 180);
                System.out.println("targetTabIndex " + targetTabIndex);
                if (mouseY < tabHeaderHeight) {
                    //if (headerBounds.contains(mouseInHeader)) {
                    System.out.println("mouseInHeader.getX() " + mouseInHeader.getX());
                    System.out.println("headerBounds.getWidth() " + headerBounds.getWidth());
                    System.out.println("tabsize " + tabpane.getTabs().size());
                    System.out.println("tabText " + tabText);
                    System.out.println("draggedTabIndex " + draggedTabIndex);

                    if (draggedTabIndex != targetTabIndex) {
                        Tab draggedTab = tabpane.getTabs().remove(draggedTabIndex);
                        if (targetTabIndex > tabpane.getTabs().size()) {
                            targetTabIndex = tabpane.getTabs().size();
                        }
                        tabpane.getTabs().add(targetTabIndex, draggedTab);
                        tabpane.getSelectionModel().select(draggedTab);
                        success = true;

                    }
                    //}
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });

        tabpane.setOnDragDone(event -> {
            event.consume();
        });

        List<String> tabs = new ArrayList<>();
        tabs = TabsStateManager.loadCurrentTab();
        if (tabs.size() > 0) {
            if (ShowMessageFX.YesNo(null, "Automotive Application", "You want to restore unclosed tabs?") == true) {
                for (String tabName : tabs) {
                    triggerMenu(tabName);
                }
            } else {
                for (String tabName : tabs) {
                    TabsStateManager.closeTab(tabName);
                }
                TabsStateManager.saveCurrentTab(new ArrayList<>());
                return;
            }
        }
    }

    private void triggerMenu(String sFormName) {

        switch (sFormName) {
            /*DIRECTORY*/
            case "Activity":
                mnuActivity.fire();
                break;
            case "Activity Approval":
                mnuActivityApproval.fire();
                break;
            case "Customer":
                mnuCustomerInfo.fire();
                break;
            case "Customer Vehicle Information":
                mnuCustVhclInfo.fire();
                break;
            case "Vehicle Sales Information":
                mnuVhclEntry.fire();
                break;
//            case "Supplier":
//                break;
            /*SALES*/
            case "Sales Agent Information":
                mnuSalesAgent.fire();
            case "Sales Executive Information":
                mnuSalesExecutive.fire();
                break;
            case "Vehicle Description":
                mnuVhclDesc.fire();
                break;
//            case "Unit Receiving":
//
//                break;
            case "Inquiry":
                mnuInquiry.fire();
                break;
            case "Vehicle Reservation Approval":
                mnuVhclRsrvApp.fire();
                break;
            case "Unit Delivery Receipt":
                mnuUnitDeliveryReceipt.fire();
                break;
            case "Vehicle Sales Proposal":
                mnuVSPEntry.fire();
                break;
            case "VSP Add Ons Approval":
                mnuAddOnsApproval.fire();
                break;
            case "Sales Job Order Information":
                mnuSalesJobOrder.fire();
                break;
            /*ACCOUNTING*/
            case "Bank":
                mnuBank.fire();
                break;
            /*CASHIERING*/
            case "Acknowledgement Receipt":
                mnuAckReceipt.fire();
                break;
            case "Billing Statement":
                mnuBillingStmt.fire();
                break;
            case "Collection Receipt":
                mnuColReceipt.fire();
                break;
            case "Official Receipt":
                mnuOfcReceipt.fire();
                break;
            case "Parts Sales Invoice":
                mnuPartsSalesInv.fire();
                break;
            case "Vehicle Sales Invoice":
                mnuVhclSalesInv.fire();
                break;
            /*PARTS*/
            case "Item Information":
                mnuItemEntry.fire();
                break;
            case "Sales Parts Request":
                mnuSalesPartsRequest.fire();
                break;
            /*SERVICE*/
            case "Service Job Order Information":
                mnuServiceJobOrder.fire();
                break;
            /*INSURANCE*/
            case "Insurance":
                mnuInsurInfo.fire();
                break;
        }

    }

    private int findTabIndex(String tabText) {
        ObservableList<Tab> tabs = tabpane.getTabs();
        for (int i = 0; i < tabs.size(); i++) {
            if (tabs.get(i).getText().equals(tabText)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    /*LOAD ANIMATE FOR ANCHORPANE MAIN HOME*/
    public AnchorPane loadAnimateAnchor(String fsFormName) {

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
    public void setScene(AnchorPane foPane) {
        workingSpace.getChildren().clear();
        workingSpace.getChildren().add(foPane);
    }

    /*LOAD ANIMATE FOR TABPANE*/
    public TabPane loadAnimate(String fsFormName) {
        //set fxml controller class
        ScreenInterface fxObj = getController(fsFormName);
        fxObj.setGRider(oApp);

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(fxObj.getClass().getResource(fsFormName));
        fxmlLoader.setController(fxObj);

        //Add new tab;
        Tab newTab = new Tab(SetTabTitle(fsFormName));
        newTab.setStyle("-fx-font-weight: bold; -fx-pref-width: 180; -fx-font-size: 10.5px; -fx-font-family: arial;");
        //tabIds.add(fsFormName);
        tabName.add(SetTabTitle(fsFormName));
        // Save the list of tab IDs to the JSON file
        TabsStateManager.saveCurrentTab(tabName);
        try {
            Node content = fxmlLoader.load();
            newTab.setContent(content);
            tabpane.getTabs().add(newTab);
            tabpane.getSelectionModel().select(newTab);
            //newTab.setOnClosed(event -> {
            newTab.setOnCloseRequest(event -> {
                if (ShowMessageFX.YesNo(null, "Close Tab", "Are you sure, do you want to close tab?") == true) {
                    Tabclose();
                    //tabIds.remove(newTab.getText());
                    tabName.remove(newTab.getText());
                    // Save the list of tab IDs to the JSON file
                    TabsStateManager.saveCurrentTab(tabName);
                    TabsStateManager.closeTab(newTab.getText());
                } else {
                    // Cancel the close request
                    event.consume();
                }

            });

            newTab.setOnSelectionChanged(event -> {
                ObservableList<Tab> tabs = tabpane.getTabs();
                for (Tab tab : tabs) {
                    if (tab.getText().equals(newTab.getText())) {
                        tabName.remove(newTab.getText());
                        tabName.add(newTab.getText());
                        // Save the list of tab IDs to the JSON file
                        TabsStateManager.saveCurrentTab(tabName);
                        break;
                    }
                }

            });
            return (TabPane) tabpane;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ScreenInterface getController(String fsValue) {
        switch (fsValue) {
            case "FXMLMainScreen.fxml":
                return new FXMLMainScreenController();
            /*DIRECTORY*/
            case "ActivityForm.fxml":
                return new ActivityFormController();
            case "ActivityApproval.fxml":
                return new ActivityApprovalController();
            case "ActivityTypeAddSource.fxml":
                return new ActivityTypeAddSourceController();
            case "CustomerForm.fxml":
                return new CustomerFormController();
            case "CustomerVehicleInfoForm.fxml":
                return new CustomerVehicleInfoFormController();
//               case "SupplierInfo.fxml":
//                    return new SupplierInfoController();
            /*SALES*/
            case "SalesAgentForm.fxml":
                return new SalesAgentFormController();
            case "VehicleDescriptionForm.fxml":
                return new VehicleDescriptionFormController();
            case "VehicleMakeForm.fxml":
                return new VehicleMakeFormController();
            case "VehicleModelForm.fxml":
                return new VehicleModelFormController();
            case "VehicleTypeForm.fxml":
                return new VehicleTypeFormController();
            case "VehicleColorForm.fxml":
                return new VehicleColorFormController();
            case "VehicleEngineFrameFormatForm.fxml":
                return new VehicleEngineFrameFormatFormController();
            case "VehicleEntryForm.fxml":
                return new VehicleEntryFormController();
            case "UnitReceivingForm.fxml":
                return new UnitReceivingFormController();
            case "InquiryForm.fxml":
                return new InquiryFormController();
            case "VehicleSalesApproval.fxml":
                return new VehicleSalesApprovalController();
            case "BankEntryForm.fxml":
                return new BankEntryFormController();
            case "UnitDeliveryReceiptForm.fxml":
                return new UnitDeliveryReceiptFormController();
            case "VSPForm.fxml":
                return new VSPFormController();
            case "VSPAddOnsApproval.fxml":
                return new VSPAddOnsApprovalController();

            /*PARTS*/
            case "ItemEntryForm.fxml":
                return new ItemEntryFormController();
            case "VSPPendingPartsRequest.fxml":
                return new VSPPendingPartsRequestController();

            /*PARAMETERS*/
            case "InventoryLocationParam.fxml":
                return new InventoryLocationParamController();
            case "BinEntryParam.fxml":
                return new BinEntryParamController();
            case "SectionEntryParam.fxml":
                return new SectionEntryParamController();
            case "WareHouseEntryParam.fxml":
                return new WareHouseEntryParamController();
            case "CategoryEntryParam.fxml":
                return new CategoryEntryParamController();
            case "InvTypeEntryParam.fxml":
                return new InvTypeEntryParamController();
            case "MeasurementEntryParam.fxml":
                return new MeasurementEntryParamController();
            case "BrandEntryParam.fxml":
                return new BrandEntryParamController();
            case "InvoiceForm.fxml":
                return new InvoiceFormController();
            case "VehicleSalesInvoiceForm.fxml":
                return new VehicleSalesInvoiceFormController();
            /*SERVICE*/
            case "JobOrderForm.fxml":
                return new JobOrderFormController();
            /*SERVICE*/
            case "InsuranceInformation.fxml":
                return new InsuranceInformationController();

            default:
                ShowMessageFX.Warning(null, "Warning", "Notify System Admin to Configure Screen Interface for " + fsValue);
                return null;
        }
    }

    //Set tab title
    public String SetTabTitle(String menuaction) {
        switch (menuaction) {
            /*DIRECTORY*/
            case "ActivityForm.fxml":
                return "Activity";
            case "ActivityApproval.fxml":
                return "Activity Approval";
            case "CustomerForm.fxml":
                return "Customer";
            case "CustomerVehicleInfoForm.fxml":
                if (sVehicleInfoType.isEmpty()) {
                    ShowMessageFX.Warning(null, "Warning", "Notify System Admin to Configure Tab Title for " + menuaction);
                    return null;
                }
                return sVehicleInfoType;
            //return "Customer Vehicle Information";
            case "SupplierInfo.fxml":
                return "Supplier";
            /*SALES*/
            case "SalesAgentForm.fxml":
                if (sSalesInfoType.isEmpty()) {
                    ShowMessageFX.Warning(null, "Warning", "Notify System Admin to Configure Tab Title for " + menuaction);
                    return null;
                }
                return sSalesInfoType;
//                return "Sales Agent";
            case "VehicleDescriptionForm.fxml":
                return "Vehicle Description";
            case "VehicleEntryForm.fxml":
                return "Vehicle Information";
            case "UnitReceivingForm.fxml":
                return "Unit Receiving";
            case "InquiryForm.fxml":
                return "Inquiry";
            case "VehicleSalesApproval.fxml":
                return "Vehicle Reservation Approval";
            case "UnitDeliveryReceiptForm.fxml":
                return "Unit Delivery Receipt";
            case "VSPForm.fxml":
                return "Vehicle Sales Proposal";
            case "VSPAddOnsApproval.fxml":
                return "VSP Add Ons Approval";
            /*ACCOUNTING*/
            case "BankEntryForm.fxml":
                return "Bank";
            /*CASHIERING*/
            case "InvoiceForm.fxml":
                if (sSalesInvoiceType.isEmpty()) {
                    ShowMessageFX.Warning(null, "Warning", "Notify System Admin to Configure Tab Title for " + menuaction);
                    return null;
                }
                return sSalesInvoiceType;
            case "VehicleSalesInvoiceForm.fxml":
                return "Vehicle Sales Invoice";
            /*PARTS*/
            case "ItemEntryForm.fxml":
                return "Item Information";
            case "VSPPendingPartsRequest.fxml":
                return "Sales Parts Request";
            /**/
            case "JobOrderForm.fxml":
                if (sJobOrderType.isEmpty()) {
                    ShowMessageFX.Warning(null, "Warning", "Notify System Admin to Configure Tab Title for " + menuaction);
                    return null;
                }
                return sJobOrderType;
            case "InsuranceInformation.fxml":
                return "Insurance";
            default:
                ShowMessageFX.Warning(null, "Warning", "Notify System Admin to Configure Tab Title for " + menuaction);
                return null;
        }
    }

    //Load Main Screen if no tab remain
    public void Tabclose() {
        int tabsize = tabpane.getTabs().size();
        if (tabsize == 1) {
            setScene(loadAnimateAnchor("FXMLMainScreen.fxml"));
        }
    }

    /*SET SCENE FOR WORKPLACE - STACKPANE - TABPANE*/
    public void setScene2(TabPane foPane) {
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
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuSupplierInfoClick(ActionEvent event) {
        String sformname = "SupplierInfo.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    /*SALES*/
    @FXML
    private void mnuSalesJobOrderClick(ActionEvent event) {
        sJobOrderType = "Sales Job Order Information";
        String sformname = "JobOrderForm.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuSalesAgentClick(ActionEvent event) {
        sSalesInfoType = "Sales Agent Information";
        String sformname = "SalesAgentForm.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuSalesExecutiveClick(ActionEvent event) {
        sSalesInfoType = "Sales Executive Information";
        String sformname = "SalesAgentForm.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuUnitRecvClick(ActionEvent event) {
        String sformname = "UnitReceivingForm.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuVhclRsrvAppClick(ActionEvent event) {
        String sformname = "VehicleSalesApproval.fxml";

        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuInquiryClick(ActionEvent event) {
        String sformname = "InquiryForm.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuUnitDeliveryReceiptClick(ActionEvent event) {
        String sformname = "UnitDeliveryReceiptForm.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuVSPEntryClick(ActionEvent event) {
        String sformname = "VSPForm.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuCustVhclInfoClick(ActionEvent event) {
        sVehicleInfoType = "Customer Vehicle Information";
        String sformname = "CustomerVehicleInfoForm.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuVhclEntryClick(ActionEvent event) {
        sVehicleInfoType = "Vehicle Sales Information";
        String sformname = "CustomerVehicleInfoForm.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
//        String sformname = "VehicleEntryForm.fxml";
//        //check tab
//        if (checktabs(SetTabTitle(sformname)) == 1) {
//            setScene2(loadAnimate(sformname));
//        }
    }

    @FXML
    private void mnuAddOnsApprovalClick(ActionEvent event) {
        String sformname = "VSPAddOnsApproval.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    /*VEHICLE DESCRIPTION AND PARAMETERS*/
    @FXML
    public void mnuVhclDescClick(ActionEvent event) {
        String sformname = "VehicleDescriptionForm.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuVhclMakeEntryClicked(ActionEvent event) {
        String sformname = "VehicleMakeForm.fxml";
        param.FXMLMenuParameterForm(getController(sformname), oApp, sformname);
    }

    @FXML
    private void mnuVhclModelEntryClicked(ActionEvent event) {
        String sformname = "VehicleModelForm.fxml";
        param.FXMLMenuParameterForm(getController(sformname), oApp, sformname);
    }

    @FXML
    private void mnuVhclTypeEntryClicked(ActionEvent event) {
        String sformname = "VehicleTypeForm.fxml";
        param.FXMLMenuParameterForm(getController(sformname), oApp, sformname);
    }

    @FXML
    private void mnuVhclColorEntryClicked(ActionEvent event) {
        String sformname = "VehicleColorForm.fxml";
        param.FXMLMenuParameterForm(getController(sformname), oApp, sformname);
    }

    @FXML
    private void mnuVhclEngFrmEntryClicked(ActionEvent event) {
        String sformname = "VehicleEngineFrameFormatForm.fxml";
        param.FXMLMenuParameterForm(getController(sformname), oApp, sformname);
    }

    /*ACCOUNTING*/
    @FXML
    private void mnuBankClick(ActionEvent event) {
        String sformname = "BankEntryForm.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    /*CASHIERING*/
    @FXML
    private void mnuAckReceiptClick(ActionEvent event) {
        sSalesInvoiceType = "Acknowledgement Receipt";
        String sformname = "InvoiceForm.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }

    }

    @FXML
    private void mnuBillingStmtClick(ActionEvent event) {
        sSalesInvoiceType = "Billing Statement";
        String sformname = "InvoiceForm.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuColReceiptClick(ActionEvent event) {
        sSalesInvoiceType = "Collection Receipt";
        String sformname = "InvoiceForm.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuOfcReceiptClick(ActionEvent event) {
        sSalesInvoiceType = "Official Receipt";
        String sformname = "InvoiceForm.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuPartsSalesInvClick(ActionEvent event) {
        sSalesInvoiceType = "Parts Sales Invoice";
        String sformname = "InvoiceForm.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuVhclSalesInvClick(ActionEvent event) {
        String sformname = "VehicleSalesInvoiceForm.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    /*ACTIVITY*/
    @FXML
    private void mnuActivityClick(ActionEvent event) {
        String sformname = "ActivityForm.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuActivityApprovClick(ActionEvent event) {
        String sformname = "ActivityApproval.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuActivityTypeClick(ActionEvent event) {
        String sformname = "ActivityTypeAddSource.fxml";
        param.FXMLMenuParameterForm(getController(sformname), oApp, sformname);

    }

    /*PARTS*/
    @FXML
    private void mnuItemEntryClicked(ActionEvent event) {
        String sformname = "ItemEntryForm.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuSalesPartsRequestClicked(ActionEvent event) {
        String sformname = "VSPPendingPartsRequest.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuBinEntryClicked(ActionEvent event) {
        String sformname = "BinEntryParam.fxml";
        param.FXMLMenuParameterForm(getController(sformname), oApp, sformname);
    }

    @FXML
    private void mnuInvLocEntryClicked(ActionEvent event) {
        String sformname = "InventoryLocationParam.fxml";
        param.FXMLMenuParameterForm(getController(sformname), oApp, sformname);
    }

    @FXML
    private void mnuMeasureEntryClicked(ActionEvent event) {
        String sformname = "MeasurementEntryParam.fxml";
        param.FXMLMenuParameterForm(getController(sformname), oApp, sformname);
    }

    @FXML
    private void mnuSectionEntryClicked(ActionEvent event) {
        String sformname = "SectionEntryParam.fxml";
        param.FXMLMenuParameterForm(getController(sformname), oApp, sformname);
    }

    @FXML
    private void mnuWarehsEntryClicked(ActionEvent event) {
        String sformname = "WareHouseEntryParam.fxml";
        param.FXMLMenuParameterForm(getController(sformname), oApp, sformname);
    }

    @FXML
    private void mnuBrandEntryClicked(ActionEvent event) {
        String sformname = "BrandEntryParam.fxml";
        param.FXMLMenuParameterForm(getController(sformname), oApp, sformname);
    }

    @FXML
    private void mnuCategoryEntryClicked(ActionEvent event) {
        String sformname = "CategoryEntryParam.fxml";
        param.FXMLMenuParameterForm(getController(sformname), oApp, sformname);
    }

    @FXML
    private void mnuInvTypeEntryClicked(ActionEvent event) {
        String sformname = "InvTypeEntryParam.fxml";
        param.FXMLMenuParameterForm(getController(sformname), oApp, sformname);
    }

    /*Service*/
    @FXML
    private void mnuJobOrderClick(ActionEvent event) {
        sJobOrderType = "Service Job Order Information";
        String sformname = "JobOrderForm.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    /*Service*/
    @FXML
    private void mnuInsurInfoClick(ActionEvent event) {
        String sformname = "InsuranceInformation.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    /*SET CURRENT TIME*/
    private void getTime() {
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

            String formattedTime = timeFormat.format(date);
            String formattedDate = dateFormat.format(date);

            DateAndTime.setText(formattedDate + " || " + formattedTime);

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

    public void logout(Stage stage) {

        if (ShowMessageFX.YesNo(null, "Exit", "Are you sure, do you want to close?") == true) {
            if (tabName.size() > 0) {
                for (String tabsName : tabName) {
                    TabsStateManager.closeTab(tabsName);
                }
                TabsStateManager.saveCurrentTab(new ArrayList<>());
            }
            System.out.println("You successfully logged out!");
            stage.close();
        }
    }

    /*USER ACCESS*/
    private void initMenu() {

    }

}
