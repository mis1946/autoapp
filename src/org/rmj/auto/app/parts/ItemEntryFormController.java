/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.parts;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.F3;
import static javafx.scene.input.KeyCode.TAB;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.auto.app.views.ScreenInterface;
import org.rmj.auto.app.views.unloadForm;
import org.rmj.auto.parts.base.ItemEntry;
//import static org.rmj.webcamfx.ui.CameraType.Webcam;
//import org.rmj.webcamfx.ui.WebCamFX;
//import org.rmj.webcamfx.ui.Webcam;

/**
 * FXML Controller class
 *
 * @author Arsiela to be continued by John Dave Date Created: 06-27-2023
 */
public class ItemEntryFormController implements Initializable, ScreenInterface {

    private GRider oApp;
    private MasterCallback oListener;
    private ItemEntry oTrans;
    private int pnEditMode;
    private final String pxeModuleName = "Item Entry";

    unloadForm unload = new unloadForm(); //Used in Close Button
    private String oldTransNo = "";
    private String sTransNo = "";
    private int lnRow;
    private int lnCtr;
    private Image pimage;
    private String psFileName = "";
    private String psFileUrl = "";

    private int pnRow = -1;
    private int oldPnRow = -1;
    private int pagecounter;
    private double xOffset = 0;
    private double yOffset = 0;

    private ObservableList<ItemEntryTableList> itemdata = FXCollections.observableArrayList();
    private ObservableList<ItemEntryTableList> supersededata = FXCollections.observableArrayList();
    private ObservableList<ItemEntryTableList> modeldata = FXCollections.observableArrayList();
    private FilteredList<ItemEntryTableList> filteredData;

    private static final int ROWS_PER_PAGE = 50;
//    WebCamFX webcam = new WebCamFX(); //Open Camera
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Pagination pagination;
    @FXML
    private TableView<ItemEntryTableList> tblItemList;
    @FXML
    private TableColumn<ItemEntryTableList, String> tblindexRow;
    @FXML
    private TableColumn<ItemEntryTableList, String> tblindex02;
    @FXML
    private TableColumn<ItemEntryTableList, String> tblindex03;
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
    @FXML
    private TableView tblSupersede;
    @FXML
    private TableColumn tblindex01_sups;
    @FXML
    private TableColumn tblindex02_sups;
    @FXML
    private TableColumn tblindex03_sups;
    @FXML
    private TableColumn tblindex04_sups;
    @FXML
    private TableView tblModel;
    @FXML
    private TableColumn tblindex01_model;
    @FXML
    private TableColumn tblindex02_model;
    @FXML
    private TableColumn tblindex03_model;
    @FXML
    private TableColumn tblindex04_model;
    @FXML
    private Button btnLoadPhoto;
    @FXML
    private TextField txtField12;
    @FXML
    private TextField txtField01;
    @FXML
    private TextField txtField03;
    @FXML
    private TextField txtField04;
    @FXML
    private TextField txtField13;
    @FXML
    private TextField textField02;
    @FXML
    private TextField txtField33;
    @FXML
    private TextField txtField34;
    @FXML
    private TableColumn<ItemEntryTableList, String> tblindex33;
    @FXML
    private TextField txtField32;
    @FXML
    private ComboBox comboBox01;
    ObservableList<String> cItems = FXCollections.observableArrayList("Part Number", "Description");

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

        oTrans = new ItemEntry(oApp, oApp.getBranchCode(), true); //Initialize ClientMaster
        oTrans.setCallback(oListener);
        oTrans.setWithUI(true);
        loadItemList();
        comboBox01.setItems(cItems);
        btnClose.setOnAction(this::cmdButton_Click);
        btnAdd.setOnAction(this::cmdButton_Click);
        btnEdit.setOnAction(this::cmdButton_Click);
        btnCancel.setOnAction(this::cmdButton_Click);
        btnSave.setOnAction(this::cmdButton_Click);

        btnBrandName.setOnAction(this::cmdButton_Click);
        btnCategory.setOnAction(this::cmdButton_Click);
        btnInvType.setOnAction(this::cmdButton_Click);
        btnMeasurement.setOnAction(this::cmdButton_Click);
        btnLocation.setOnAction(this::cmdButton_Click);
        btnSupsAdd.setOnAction(this::cmdButton_Click);
        btnSupsDel.setOnAction(this::cmdButton_Click);
        btnModelAdd.setOnAction(this::cmdButton_Click);
        btnModelDel.setOnAction(this::cmdButton_Click);
        btnCapture.setOnAction(this::cmdButton_Click);
        btnUpload.setOnAction(this::cmdButton_Click);
        btnLoadPhoto.setOnAction(this::cmdButton_Click);

        txtField33.focusedProperty().addListener(txtField_Focus);
        txtField12.focusedProperty().addListener(txtField_Focus);
        txtField33.focusedProperty().addListener(txtField_Focus);
        txtField34.focusedProperty().addListener(txtField_Focus);

        txtField33.setOnKeyPressed(this::txtField_KeyPressed);
        txtField12.setOnKeyPressed(this::txtField_KeyPressed);
        txtField33.setOnKeyPressed(this::txtField_KeyPressed);
        txtField34.setOnKeyPressed(this::txtField_KeyPressed);
        pnEditMode = EditMode.UNKNOWN;
        initbutton(pnEditMode);

        txtField12.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && txtField12.getText().isEmpty()) {
                txtField12.getStyleClass().add("required-field");
            } else {
                txtField12.getStyleClass().remove("required-field");
            }
        });
        txtField34.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && txtField34.getText().isEmpty()) {
                txtField34.getStyleClass().add("required-field");
            } else {
                txtField34.getStyleClass().remove("required-field");
            }
        });
        pagination.setPageFactory(this::createPage);
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
        if (itemdata.size() > 0) {
            tblItemList.setItems(FXCollections.observableArrayList(itemdata.subList(fromIndex, toIndex)));
        }
        return tblItemList;

    }

    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnBrandName":
                break;
            case "btnInvType":
                break;
            case "btnCategory":
                break;
            case "btnLocation":
                break;
            case "btnMeasurement":
                break;
            case "btnSupsDel":
                break;
            case "btnSupsAdd":
                break;
            case "btnModelAdd":
                break;
            case "btnModelDel":
                break;
            case "btnCapture":
//                    try {
//                        Stage stage = new Stage();
//                        webcam.start(stage);
//                        captureImage();
//                    } catch (Exception ex) {
//                        Logger.getLogger(ItemEntryFormController.class.getName()).log(Level.SEVERE, null, ex);
//                    }
                break;
            case "btnUpload":
                FileChooser fileChooser = new FileChooser();
                // Set the title and extension filters if desired
                fileChooser.setTitle("Select Image File");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
                // Show the file chooser dialog
                File selectedFile = fileChooser.showOpenDialog(btnUpload.getScene().getWindow());
                if (selectedFile != null) {
                    // Load the selected image file
                    Image image = new Image(selectedFile.toURI().toString());
                    imgPartsPic.setImage(image);

                    psFileUrl = selectedFile.toURI().toString();
                    psFileName = selectedFile.getName();
                    pimage = new Image(selectedFile.toURI().toString());
                }
                break;
            case "btnLoadPhoto":
                if (!psFileUrl.isEmpty()) {
                    loadPhotoWindow();
                }
                break;
            case "btnAdd":
                if (oTrans.NewRecord()) {
                    pnEditMode = oTrans.getEditMode();
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
                    return;
                }
                break;
            case "btnEdit":
//                    if (oTrans.UpdateRecord()) {
//                        pnEditMode = oTrans.getEditMode();
//                    } else {
//                        ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
//                        return;
//                    }
                break;
            case "btnCancel":
                if (ShowMessageFX.OkayCancel(getStage(), "Are you sure you want to cancel?", pxeModuleName, null) == true) {
                    clearFields();
                    pnEditMode = EditMode.UNKNOWN;
                }
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
                        unload.unloadForm(AnchorMain, oApp, pxeModuleName);
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
    }

    /*OPEN PHOTO WINDOW*/
    private void loadPhotoWindow() {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("ItemPhoto.fxml"));

            ItemPhotoController loControl = new ItemPhotoController();
            loControl.setGRider(oApp);
            loControl.setPicName(psFileName);
            loControl.setPicUrl(psFileUrl);
            fxmlLoader.setController(loControl);

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

            //set the main interface as the scene
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("");
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }

    }

    /*TODO*/
 /*OPEN CAMERA*/
    private void captureImage() {
//        if (webcam.isOpen()) {
//            try {
//                // Capture image from webcam
//                BufferedImage bufferedImage = webcam.getImage();
//
//                // Convert BufferedImage to JavaFX Image
//                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
//
//                // Display the captured image in ImageView
//                imgPartsPic.setImage(image);
//
//                // Save the captured image to a file (optional)
//                // File outputFile = new File("captured_image.png");
//                // ImageIO.write(bufferedImage, "png", outputFile);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

    public void loadItemList() {
        try {
            /*Populate table*/
            itemdata.clear();
            String sRecStat = "";
            if (oTrans.LoadMasterList()) {
                for (lnCtr = 1; lnCtr <= oTrans.getMasterDetailCount(); lnCtr++) {
                    itemdata.add(new ItemEntryTableList(
                            String.valueOf(lnCtr), //Row
                            "",
                            oTrans.getDetail(lnCtr, "sBarCodex").toString(),
                            "",
                            oTrans.getDetail(lnCtr, "sDescript").toString(),
                            "",
                            "",
                            "",
                            oTrans.getDetail(lnCtr, "sCategNme").toString(),
                            ""
                    ));
                }
                initItemList();
            } else {
                ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
                return;
            }
        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }

    }

    private void initItemList() {
        tblindexRow.setCellValueFactory(new PropertyValueFactory<>("tblindexRow"));
        tblindex02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
        tblindex03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
        tblindex33.setCellValueFactory(new PropertyValueFactory<>("tblindex33"));

        filteredData = new FilteredList<>(itemdata, b -> true);
        autoSearch(txtSeeks01);
        SortedList<ItemEntryTableList> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblItemList.comparatorProperty());
        tblItemList.setItems(sortedData);

        tblItemList.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblItemList.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
            header.setDisable(true);
        });

        tblItemList.setItems(itemdata);
    }

    public void loadSupersedeList() {
//        try {
//            /*Populate table*/
//            supersededata.clear();
//            String sRecStat = "";
//            if (oTrans.LoadList()) {
//                for (lnCtr = 1; lnCtr <= oTrans.getItemCount(); lnCtr++) {
//                    itemdata.add(new ItemEntryTableList(
//                            String.valueOf(lnCtr) //Row
//                    ));
//                }
//                initSupersedeList();
//            } else {
//                ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
//                return;
//            }
//        } catch (SQLException e) {
//            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
//        }

    }

    private void initSupersedeList() {
        boolean lbShow = (pnEditMode == EditMode.READY || pnEditMode == EditMode.UPDATE);
        tblindex01_sups.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
        tblindex02_sups.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
        tblindex03_sups.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
        tblindex04_sups.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));

        tblSupersede.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblSupersede.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

        tblSupersede.setItems(supersededata);
    }

    public void loadModelList() {
//        try {
//            /*Populate table*/
//            modeldata.clear();
//            String sRecStat = "";
//            if (oTrans.LoadList()) {
//                for (lnCtr = 1; lnCtr <= oTrans.getItemCount(); lnCtr++) {
//                    itemdata.add(new ItemEntryTableList(
//                            String.valueOf(lnCtr) //Row
//                    ));
//                }
//                initModelList();
//            } else {
//                ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
//                return;
//            }
//        } catch (SQLException e) {
//            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
//        }

    }

    private void initModelList() {
        boolean lbShow = (pnEditMode == EditMode.READY || pnEditMode == EditMode.UPDATE);
        tblindex01_model.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
        tblindex02_model.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
        tblindex03_model.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
        tblindex04_model.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));

        tblModel.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblModel.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

        tblModel.setItems(modeldata);
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
                            return (clients.getTblindex02().toLowerCase().contains(lowerCaseFilter)); // Does not match.
                        } else {
                            return (clients.getTblindex02().toLowerCase().contains(lowerCaseFilter)); // Does not match.
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
            if (ShowMessageFX.OkayCancel(null, pxeModuleName, "You have unsaved data, are you sure you want to continue?") == true) {
            } else {
                return;
            }
        }

        pnRow = tblItemList.getSelectionModel().getSelectedIndex();
        pagecounter = pnRow + pagination.getCurrentPageIndex() * ROWS_PER_PAGE;
        if (pagecounter >= 0) {
            if (event.getClickCount() > 0) {
//                getSelectedItem(filteredData.get(pagecounter).getTblindex11()); //Populate field based on selected Item
                tblItemList.setOnKeyReleased((KeyEvent t) -> {
                    KeyCode key = t.getCode();
                    switch (key) {
                        case DOWN:
                            pnRow = tblItemList.getSelectionModel().getSelectedIndex();
                            pagecounter = pnRow + pagination.getCurrentPageIndex() * ROWS_PER_PAGE;
                            if (pagecounter == tblItemList.getItems().size()) {
                                pagecounter = tblItemList.getItems().size();
//                                getSelectedItem(filteredData.get(pagecounter).getTblindex11());
                            } else {
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

    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(((TextField) event.getSource()).getId().substring(8, 10));
        String txtFieldID = ((TextField) event.getSource()).getId();
        String lsValue = txtField.getText();

        try {
            switch (event.getCode()) {
                case F3:
                case TAB:
                case ENTER:
                    switch (txtFieldID) {
                        case "txtField32":
                            if (oTrans.searchBrand(lsValue)) {
                                loadItemInformationField();
                            } else {
                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                            }
                            break;
                        case "txtField12":
                            if (oTrans.searchInvType(lsValue)) {
                                loadItemInformationField();
                            } else {
                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                            }
                            break;
                        case "txtField33":
                            if (oTrans.getMaster(12).toString().isEmpty()) {
                                ShowMessageFX.Warning(getStage(), "Please select inventory type first", "Warning", null);
                                return;
                            }
                            if (oTrans.searchInvCategory(lsValue, oTrans.getMaster(12).toString())) {
                                loadItemInformationField();
                            } else {
                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                            }
                            break;
                        case "txtField34":
                            if (oTrans.searchMeasure(lsValue)) {
                                loadItemInformationField();
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

    private void loadItemInformationField() {
        try {
            txtField32.setText((String) oTrans.getMaster(32));
            txtField33.setText((String) oTrans.getMaster(33));
            txtField12.setText((String) oTrans.getMaster(12));
            txtField34.setText((String) oTrans.getMaster(34));
        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }
    }
    /*Set TextField Value to Master Class*/
    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {
        try {
            TextField txtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
            int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
            String lsValue = txtField.getText();

            if (lsValue == null) {
                return;
            }
            if (!nv) {
                /*Lost Focus*/
                switch (lnIndex) {
                    case 2:
                    case 32:
                    case 12:
                    case 5:
                    case 34:
                        oTrans.setMaster(lnIndex, lsValue); //Handle Encoded Value
                        break;
                }

            } else {
                txtField.selectAll();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ItemEntryFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    };

    private void clearFields() {
        txtField01.setText("");
        txtField03.setText("");
        txtField04.setText("");
        txtField32.setText("");
        txtField33.setText("");
        txtField34.setText("");
        txtField12.setText("");
    }

    private void initbutton(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

        btnBrandName.setVisible(lbShow);
        btnCategory.setVisible(lbShow);
        btnInvType.setVisible(lbShow);
        btnMeasurement.setVisible(lbShow);
        btnLocation.setVisible(lbShow);

        txtField01.setDisable(!lbShow);
        txtField03.setDisable(!lbShow);
        txtField04.setDisable(!lbShow);
        txtField32.setDisable(!lbShow);
        txtField33.setDisable(!lbShow);
        txtField34.setDisable(!lbShow);
        txtField12.setDisable(!lbShow);
        btnSupsAdd.setDisable(!lbShow);
        btnSupsDel.setDisable(!lbShow);
        btnModelAdd.setDisable(!lbShow);
        btnModelDel.setDisable(!lbShow);
        //btnCapture.setVisible(lbShow);
        //btnUpload.setVisible(lbShow);

        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        btnEdit.setVisible(false);
        btnEdit.setManaged(false);
        btnCancel.setVisible(lbShow);
        btnCancel.setManaged(lbShow);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);

        if (fnValue == EditMode.READY) {
            btnEdit.setVisible(true);
            btnEdit.setManaged(true);
        }
    }

}