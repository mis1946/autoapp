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
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
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
import javafx.scene.control.CheckBox;
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
import javafx.util.Duration;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.auto.app.bank.BankEntryFormController;
import org.rmj.auto.app.views.ActivityMemberTable;
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
    private ObservableList<ItemEntryModelTable> itemModeldata = FXCollections.observableArrayList();
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
    private TextField txtField33;
    @FXML
    private TextField txtField34;
    @FXML
    private TableColumn<ItemEntryTableList, String> tblindex33;
    @FXML
    private TextField txtField32;
    ObservableList<String> cItems = FXCollections.observableArrayList("PART NUMBER", "DESCRIPTION");
    @FXML
    private ComboBox<String> comboFilter;
    @FXML
    private TextField txtSeeks02;
    @FXML
    private TextField txtField02;
    @FXML
    private TextField txtField37;
    @FXML
    private TableColumn<ItemEntryModelTable, Boolean> tblModelSelect;
    @FXML
    private CheckBox selectAllModelCheckBox;
    @FXML
    private TableColumn<ItemEntryModelTable, String> tblIndex06_mdl;
    @FXML
    private TableColumn<ItemEntryModelTable, String> tblIndex07_mdl;
    @FXML
    private TableColumn<ItemEntryModelTable, String> tblIndex03_yr;
    @FXML
    private TableColumn<ItemEntryModelTable, String> tblindexModelRow;
    @FXML
    private TableView<ItemEntryModelTable> tblModelView;
    @FXML
    private Button btnModelExpand;

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
        comboFilter.setItems(cItems);
        btnClose.setOnAction(this::cmdButton_Click);
        btnAdd.setOnAction(this::cmdButton_Click);
        btnEdit.setOnAction(this::cmdButton_Click);
        btnCancel.setOnAction(this::cmdButton_Click);
        btnSave.setOnAction(this::cmdButton_Click);

        tblItemList.setOnMouseClicked(this::tblItemEntry_Clicked);

        btnBrandName.setOnAction(this::cmdButton_Click);
        btnCategory.setOnAction(this::cmdButton_Click);
        btnInvType.setOnAction(this::cmdButton_Click);
        btnMeasurement.setOnAction(this::cmdButton_Click);
        btnSupsAdd.setOnAction(this::cmdButton_Click);
        btnSupsDel.setOnAction(this::cmdButton_Click);
        btnModelAdd.setOnAction(this::cmdButton_Click);
        btnModelDel.setOnAction(this::cmdButton_Click);
        btnModelExpand.setOnAction(this::cmdButton_Click);
        btnCapture.setOnAction(this::cmdButton_Click);
        btnUpload.setOnAction(this::cmdButton_Click);
        btnLoadPhoto.setOnAction(this::cmdButton_Click);

        setCapsLockBehavior(txtField01);
        setCapsLockBehavior(txtField02);
        setCapsLockBehavior(txtField32);
        setCapsLockBehavior(txtField03);
        setCapsLockBehavior(txtField04);
        setCapsLockBehavior(txtField13);
        setCapsLockBehavior(txtField12);
        setCapsLockBehavior(txtField33);
        setCapsLockBehavior(txtField34);

        txtField01.focusedProperty().addListener(txtField_Focus);
        txtField02.focusedProperty().addListener(txtField_Focus);
        txtField32.focusedProperty().addListener(txtField_Focus);
        txtField03.focusedProperty().addListener(txtField_Focus);
        txtField04.focusedProperty().addListener(txtField_Focus);
        txtField13.focusedProperty().addListener(txtField_Focus);
        txtField12.focusedProperty().addListener(txtField_Focus);
        txtField33.focusedProperty().addListener(txtField_Focus);
        txtField34.focusedProperty().addListener(txtField_Focus);

        txtField32.setOnKeyPressed(this::txtField_KeyPressed);
        txtField12.setOnKeyPressed(this::txtField_KeyPressed);
        txtField33.setOnKeyPressed(this::txtField_KeyPressed);
        txtField34.setOnKeyPressed(this::txtField_KeyPressed);
        pnEditMode = EditMode.UNKNOWN;
        initButton(pnEditMode);
        addRequiredFieldListener(txtField02);
        addRequiredFieldListener(txtField32);
        addRequiredFieldListener(txtField03);
        addRequiredFieldListener(txtField12);
        addRequiredFieldListener(txtField33);
        addRequiredFieldListener(txtField34);
        txtField12.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                txtField33.clear();
            }
        });

        pagination.setPageFactory(this::createPage);
        initCombo();

    }

    private void addRequiredFieldListener(TextField textField) {
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && textField.getText().isEmpty()) {
                shakeTextField(textField);
                textField.getStyleClass().add("required-field");
            } else {
                textField.getStyleClass().remove("required-field");
            }
        });
    }

    private void shakeTextField(TextField textField) {
        Timeline timeline = new Timeline();
        double originalX = textField.getTranslateX();

        // Add keyframes for the animation
        KeyFrame keyFrame1 = new KeyFrame(Duration.millis(0), new KeyValue(textField.translateXProperty(), 0));
        KeyFrame keyFrame2 = new KeyFrame(Duration.millis(100), new KeyValue(textField.translateXProperty(), -5));
        KeyFrame keyFrame3 = new KeyFrame(Duration.millis(200), new KeyValue(textField.translateXProperty(), 5));
        KeyFrame keyFrame4 = new KeyFrame(Duration.millis(300), new KeyValue(textField.translateXProperty(), -5));
        KeyFrame keyFrame5 = new KeyFrame(Duration.millis(400), new KeyValue(textField.translateXProperty(), 5));
        KeyFrame keyFrame6 = new KeyFrame(Duration.millis(500), new KeyValue(textField.translateXProperty(), -5));
        KeyFrame keyFrame7 = new KeyFrame(Duration.millis(600), new KeyValue(textField.translateXProperty(), 5));
        KeyFrame keyFrame8 = new KeyFrame(Duration.millis(700), new KeyValue(textField.translateXProperty(), originalX));

        // Add keyframes to the timeline
        timeline.getKeyFrames().addAll(
                keyFrame1, keyFrame2, keyFrame3, keyFrame4, keyFrame5, keyFrame6, keyFrame7, keyFrame8
        );

        // Play the animation
        timeline.play();
    }

    public void initCombo() {
        comboFilter.setOnAction(e -> {
            String selectedFilter = comboFilter.getSelectionModel().getSelectedItem();
            txtSeeks01.setVisible(false);
            txtSeeks01.setManaged(false);
            txtSeeks02.setVisible(false);
            txtSeeks02.setManaged(false);
            switch (selectedFilter) {
                case "PART NUMBER":
                    txtSeeks01.setText("");
                    txtSeeks01.setVisible(true);
                    txtSeeks01.setManaged(true);
                    tblItemList.setItems(itemdata);
                    break;
                case "DESCRIPTION":
                    txtSeeks02.setText("");
                    txtSeeks02.setVisible(true);
                    txtSeeks02.setManaged(true);
                    tblItemList.setItems(itemdata);
                    break;
                default:
                    System.out.println("INVALID OPERATOR!");
                    break;
            }
        });
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
                loadBrandDialog();
                break;
            case "btnInvType":
                loadInvTypeDialog();
                break;
            case "btnCategory":
                loadCategoryDialog();
                break;
            case "btnMeasurement":
                loadMeasurementDialog();
                break;
            case "btnSupsDel":

                break;
            case "btnSupsAdd":
                break;
            case "btnModelAdd":
                loadItemModelDialog();
                break;
            case "btnModelDel":
                int lnRowModel = 0;
                int lnRowModelYear = 0;

                for (ItemEntryModelTable item : tblModelView.getItems()) {
                    if (item.getSelect().isSelected()) {
                        if (item.getTblIndex03_yr().equals("")) {
                            lnRowModel++;
                        } else {
                            lnRowModelYear++;
                        }
                    }
                }
                Integer[] lnValueModel = new Integer[lnRowModel];
                Integer[] lnValueModelYear = new Integer[lnRowModelYear];
                lnRowModel = 0;
                lnRowModelYear = 0;
                for (ItemEntryModelTable item : tblModelView.getItems()) {
                    if (item.getSelect().isSelected()) {

                        if (item.getTblIndex03_yr().equals("")) {
                            lnValueModel[lnRowModel] = Integer.parseInt(item.getTblIndexRow_mdlyr());
                            lnRowModel++;
                        } else {
                            lnValueModelYear[lnRowModelYear] = Integer.parseInt(item.getTblIndexRow_mdlyr());
                            lnRowModelYear++;
                        }
                    }

                }
                oTrans.removeInvModel_Year(lnValueModel, lnValueModelYear);
                loadItemModelTable();
                tblModelView.refresh();
                break;
            case "btnModelExpand":
                loadItemModelExpandDialog();
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

                if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
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

                        try {
                            oTrans.setMaster(26, psFileUrl);
                        } catch (SQLException ex) {
                            Logger.getLogger(ItemEntryFormController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                break;
            case "btnLoadPhoto":
                if (!psFileUrl.isEmpty()) {
                    loadPhotoWindow();
                }
                break;
            case "btnAdd":

                if (oTrans.NewRecord()) {
                    clearFields();
                    pnEditMode = oTrans.getEditMode();
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
                    return;
                }
                break;
            case "btnEdit":
                if (oTrans.UpdateRecord()) {
                    pnEditMode = oTrans.getEditMode();
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
                    return;
                }
                break;
            case "btnCancel":
                if (ShowMessageFX.OkayCancel(getStage(), "Are you sure you want to cancel?", pxeModuleName, null) == true) {
                    clearFields();
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnSave":
                //Validate before saving
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to save?") == true) {
                    if (txtField02.getText().trim().equals("")) {
                        ShowMessageFX.Warning(getStage(), "Please enter a valid value for Part Number", "Warning", null);
                        txtField02.requestFocus();
                        return;
                    }
                    if (txtField32.getText().trim().equals("")) {
                        ShowMessageFX.Warning(getStage(), "Please enter a value for Brand Name.", "Warning", null);
                        txtField32.requestFocus();
                        return;
                    }
                    if (txtField03.getText().trim().equals("")) {
                        ShowMessageFX.Warning(getStage(), "Please enter a valid value for Description.", "Warning", null);
                        txtField03.requestFocus();
                        return;
                    }
                    if (txtField04.getText().trim().equals("")) {
                        ShowMessageFX.Warning(getStage(), "Please enter a value for Brief Description.", "Warning", null);
                        txtField04.requestFocus();
                        return;
                    }
//                    if (txtField13.getText().trim().equals("")) {
//                        ShowMessageFX.Warning(getStage(), "Please enter a value for Unit Price.", "Warning", null);
//                        txtField13.requestFocus();
//                        return;
//                    }
                    if (txtField12.getText().trim().equals("")) {
                        ShowMessageFX.Warning(getStage(), "Please enter a value for Inventory Type", "Warning", null);
                        txtField12.requestFocus();
                        return;
                    }
                    if (txtField33.getText().trim().equals("")) {
                        ShowMessageFX.Warning(getStage(), "Please enter a value for Category.", "Warning", null);
                        txtField33.requestFocus();
                        return;
                    }
                    if (txtField34.getText().trim().equals("")) {
                        ShowMessageFX.Warning(getStage(), "Please enter a value for Measurement.", "Warning", null);
                        txtField34.requestFocus();
                        return;
                    }
                    //Proceed Saving
                    if (oTrans.SaveRecord()) {
                        ShowMessageFX.Information(getStage(), "Transaction save successfully.", pxeModuleName, null);

                        loadItemInformationField();
                        loadItemModelTable();
                        loadItemList();
                        try {
                            getSelectedItem((String) oTrans.getMaster(1));
                        } catch (SQLException ex) {
                            Logger.getLogger(BankEntryFormController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        pnEditMode = oTrans.getEditMode();
                    } else {
                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", "Error while saving Item Information");
                        return;
                    }
                }
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
        initButton(pnEditMode);
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
                            oTrans.getDetail(lnCtr, "sStockIDx").toString(),
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
        autoSearch(txtSeeks02);
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

    //Populate Text Field Based on selected transaction in table
    private void getSelectedItem(String TransNo) {
        oldTransNo = TransNo;
        if (oTrans.OpenRecord(TransNo)) {
            clearFields();
            loadItemInformationField();
            loadItemModelTable();

        }
        oldPnRow = pagecounter;

    }

    @FXML
    private void tblItemEntry_Clicked(MouseEvent event) {
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
                getSelectedItem(filteredData.get(pagecounter).getTblindex01()); //Populate field based on selected Item

                tblItemList.setOnKeyReleased((KeyEvent t) -> {
                    KeyCode key = t.getCode();
                    switch (key) {
                        case DOWN:
                            pnRow = tblItemList.getSelectionModel().getSelectedIndex();
                            pagecounter = pnRow + pagination.getCurrentPageIndex() * ROWS_PER_PAGE;
                            if (pagecounter == tblItemList.getItems().size()) {
                                pagecounter = tblItemList.getItems().size();
                                getSelectedItem(filteredData.get(pagecounter).getTblindex01());
                            } else {
                                int y = 1;
                                pnRow = pnRow + y;
                                getSelectedItem(filteredData.get(pagecounter).getTblindex01());
                            }
                            break;
                        case UP:
                            pnRow = tblItemList.getSelectionModel().getSelectedIndex();
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

    private void autoSearch(TextField txtField) {
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));

        txtField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(clients -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare the appropriate field of every client with the filter text.
                String lowerCaseFilter = newValue.toLowerCase();
                switch (lnIndex) {
                    case 1:
                        return clients.getTblindex02().toLowerCase().contains(lowerCaseFilter);
                    case 2:
                        return clients.getTblindex03().toLowerCase().contains(lowerCaseFilter);
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
                                txtField32.setText((String) oTrans.getMaster(32));
                            } else {
                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                            }
                            break;
                        case "txtField12":
                            if (oTrans.searchInvType(lsValue)) {
                                txtField12.setText((String) oTrans.getMaster(12));
                                txtField33.clear();
                            } else {
                                txtField12.clear();
                                txtField12.requestFocus();
                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                            }
                            break;
                        case "txtField33":
                            if (oTrans.getMaster(12).toString().isEmpty()) {
                                ShowMessageFX.Warning(getStage(), "Please select inventory type first", "Warning", null);
                                return;
                            }
                            if (oTrans.searchInvCategory(lsValue, oTrans.getMaster(12).toString())) {
                                txtField33.setText((String) oTrans.getMaster(33));
                            } else {
                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                            }
                            break;
                        case "txtField34":
                            if (oTrans.searchMeasure(lsValue)) {
                                txtField34.setText((String) oTrans.getMaster(34));
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

    //parameter
    private void loadBrandDialog() {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("BrandEntryParam.fxml"));

            BrandEntryParamController loControl = new BrandEntryParamController();
            loControl.setGRider(oApp);
//            loControl.setObject(oTrans);
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

            //set the main interface as the scene/*
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

    private void loadInvTypeDialog() {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("InvTypeEntryParam.fxml"));

            InvTypeEntryParamController loControl = new InvTypeEntryParamController();
            loControl.setGRider(oApp);
//            loControl.setObject(oTrans);
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

            //set the main interface as the scene/*
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

    private void loadCategoryDialog() {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("CategoryEntryParam.fxml"));

            CategoryEntryParamController loControl = new CategoryEntryParamController();
            loControl.setGRider(oApp);
//            loControl.setObject(oTrans);
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

            //set the main interface as the scene/*
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

    private void loadMeasurementDialog() {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("MeasurementEntryParam.fxml"));

            MeasurementEntryParamController loControl = new MeasurementEntryParamController();
            loControl.setGRider(oApp);
//            loControl.setObject(oTrans);
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

            //set the main interface as the scene/*
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

    private void loadItemModelExpandDialog() {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("ItemEntryExpandModelTable.fxml"));
            ItemEntryExpandModelTableController loControl = new ItemEntryExpandModelTableController();
            loControl.setGRider(oApp);
            loControl.setObject(oTrans);
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

            //set the main interface as the scene/*
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("");
            stage.showAndWait();
            loadItemModelTable();

        } catch (IOException e) {
            e.printStackTrace();
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }

    }

    private void loadItemModelDialog() {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("ItemEntryModel.fxml"));
            ItemEntryModelController loControl = new ItemEntryModelController();
            loControl.setGRider(oApp);
            loControl.setObject(oTrans);
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

            //set the main interface as the scene/*
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("");
            stage.showAndWait();
            loadItemModelTable();

        } catch (IOException e) {
            e.printStackTrace();
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }

    private void loadItemModelTable() {
        try {
            itemModeldata.clear(); // Clear the previous data in the list
            // if (oTrans.loadInvModel_year(oTrans.getMaster(1).toString())) {
            //inv model
            int lnRow = 0;
            for (int lnCtr = 1; lnCtr <= oTrans.getInvModelCount(); lnCtr++) {
                itemModeldata.add(new ItemEntryModelTable(
                        String.valueOf(lnCtr), // ROW
                        "",
                        oTrans.getInvModel(lnCtr, "sModelCde").toString(),
                        oTrans.getInvModel(lnCtr, "sMakeDesc").toString(),
                        oTrans.getInvModel(lnCtr, "sModelDsc").toString(),
                        "",
                        String.valueOf(lnCtr) // ROW
                )
                );
            }

            //inv model year
            lnRow = oTrans.getInvModelCount();
            for (int lnCtr = 1; lnCtr <= oTrans.getInvModelYrCount(); lnCtr++) {
                lnRow = lnRow + 1;
                itemModeldata.add(new ItemEntryModelTable(
                        String.valueOf(lnRow), // ROW
                        "",
                        oTrans.getInvModelYr(lnCtr, "sModelCde").toString(),
                        oTrans.getInvModelYr(lnCtr, "sMakeDesc").toString(),
                        oTrans.getInvModelYr(lnCtr, "sModelDsc").toString(),
                        String.valueOf((Integer) oTrans.getInvModelYr(lnCtr, "nYearModl")),
                        String.valueOf(lnCtr) // ROW
                )
                );
            }

            tblModelView.setItems(itemModeldata);
            initItemModelTable();
            //}
        } catch (SQLException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }
    }

    private void initItemModelTable() {
        tblindexModelRow.setCellValueFactory(new PropertyValueFactory<>("tblindexRow"));
        tblModelSelect.setCellValueFactory(new PropertyValueFactory<>("select"));
        tblModelView.getItems().forEach(item -> {
            CheckBox selectCheckBox = item.getSelect();
            selectCheckBox.setOnAction(event -> {
                if (tblModelView.getItems().stream().allMatch(tableItem -> tableItem.getSelect().isSelected())) {
                    selectAllModelCheckBox.setSelected(true);
                } else {
                    selectAllModelCheckBox.setSelected(false);
                }
            });
        });
        selectAllModelCheckBox.setOnAction(event -> {
            boolean newValue = selectAllModelCheckBox.isSelected();
            if (!tblModelView.getItems().isEmpty()) {
                tblModelView.getItems().forEach(item -> item.getSelect().setSelected(newValue));
            }
        });
        tblIndex06_mdl.setCellValueFactory(new PropertyValueFactory<>("tblIndex06_mdl"));
        tblIndex07_mdl.setCellValueFactory(new PropertyValueFactory<>("tblIndex07_mdl"));
        tblIndex03_yr.setCellValueFactory(new PropertyValueFactory<>("tblIndex03_yr"));
    }

    private void loadItemInformationField() {

        String unitPrice = "0.00";
        try {
            txtField01.setText((String) oTrans.getMaster(1));
            txtField02.setText((String) oTrans.getMaster(2));
            txtField32.setText((String) oTrans.getMaster(32));
            txtField03.setText((String) oTrans.getMaster(3));
            txtField04.setText((String) oTrans.getMaster(4));
//            if (!String.valueOf((BigDecimal) oTrans.getMaster(13)).equals("null")) {
//                unitPrice = String.valueOf((BigDecimal) oTrans.getMaster(13));
//            } // Format the double value with 2 decimal places
            txtField13.setText(unitPrice);
            txtField12.setText((String) oTrans.getMaster(12));
            txtField33.setText((String) oTrans.getMaster(33));
            txtField37.setText((String) oTrans.getMaster(37));
            txtField34.setText((String) oTrans.getMaster(34));

            String imageFilePath = oTrans.getMaster(26).toString();
            String imageName = imageFilePath.substring(imageFilePath.lastIndexOf('/') + 1);
            if (imageFilePath == null || imageFilePath.isEmpty()) {
                Image NoImage = new Image("file:D:/GGC_SEG_Folder-Java/autoapp/src/org/rmj/auto/app/images/no-image-available.png");
                imgPartsPic.setImage(NoImage);
            } else {
                psFileUrl = imageFilePath;
                psFileName = imageName;
                pimage = new Image(imageFilePath);
                Image image = new Image(imageFilePath);
                imgPartsPic.setImage(image);
            }
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
                    case 3:
                    case 4:
                    case 12:
                    case 33:
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
        txtField02.getStyleClass().remove("required-field");
        txtField03.getStyleClass().remove("required-field");
        txtField32.getStyleClass().remove("required-field");
        txtField12.getStyleClass().remove("required-field");
        txtField33.getStyleClass().remove("required-field");
        txtField34.getStyleClass().remove("required-field");
        Image imageError = new Image("file:D:/GGC_SEG_Folder-Java/autoapp/src/org/rmj/auto/app/images/no-image-available.png");
        imgPartsPic.setImage(imageError);
        txtField01.setText("");
        txtField02.setText("");
        txtField03.setText("");
        txtField04.setText("");
        txtField32.setText("");
        txtField33.setText("");
        txtField34.setText("");
        txtField37.setText("");
        txtField12.setText("");
        itemModeldata.clear();
    }

    private void initButton(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        btnBrandName.setVisible(lbShow);
        btnCategory.setVisible(lbShow);
        btnInvType.setVisible(lbShow);
        btnMeasurement.setVisible(lbShow);
        txtField01.setDisable(true);
        txtField02.setDisable(!lbShow);
        txtField03.setDisable(!lbShow);
        txtField32.setDisable(!lbShow);
        txtField04.setDisable(!lbShow);
        txtField13.setDisable(true);
        txtField12.setDisable(!lbShow);
        txtField33.setDisable(!lbShow);
        txtField34.setDisable(!lbShow);
        txtField37.setDisable(true);
        btnSupsAdd.setDisable(!lbShow);
        btnSupsDel.setDisable(!lbShow);
        btnModelAdd.setDisable(!lbShow);
        btnModelDel.setDisable(!lbShow);
        btnModelExpand.setDisable(!lbShow);
        btnCapture.setDisable(!lbShow);
        btnUpload.setDisable(!lbShow);
        tblSupersede.setDisable(!lbShow);
        tblModelView.setDisable(!lbShow);
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
        if (fnValue == EditMode.UPDATE) {
            txtField02.setDisable(true);

        }
    }

}
