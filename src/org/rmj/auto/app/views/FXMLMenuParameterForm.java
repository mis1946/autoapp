/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.views;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.ShowMessageFX;

/**
 * FXMLMenuParameterForm is a class that represents a form window for displaying parameters using JavaFX.
 * @author Arsiela 
 * Date Created: 06-21-2023
 */
public class FXMLMenuParameterForm {
    // Variables to track the window movement
    private double xOffset = 0;
    private double yOffset = 0;
    
    /**
     * Opens the parameter window.
     * 
     * @param fsiController The controller implementing the ScreenInterface interface.
     * @param oApp The GRider object.
     * @param fsFxml The path to the FXML file for the parameter form.
     */
    public void FXMLMenuParameterForm(ScreenInterface fsiController, GRider oApp, String fsFxml) {
        try {
            Stage stage = new Stage();
            ScreenInterface fxObj = fsiController;
            
            FXMLLoader fxmlLoader = new FXMLLoader();
            //fxmlLoader.setLocation(getClass().getResource(fsFxml));
            //fxmlLoader.setController(fxObj);
            //fxObj.setGRider(oApp);
            
            // Get the class of any class in the desired package to retrieve the package information
            Class<?> clazz = fxObj.getClass();
            // Construct the correct path to the FXML file using the package information
            String fxmlPath = clazz.getPackage().getName().replace(".", "/") + "/" + fsFxml;
            // Load the FXML file using the constructed path
            fxmlLoader.setLocation(clazz.getClassLoader().getResource(fxmlPath));
            fxmlLoader.setController(fxObj);
            fxObj.setGRider(oApp);
            
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
            ShowMessageFX.Warning(e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }
}
