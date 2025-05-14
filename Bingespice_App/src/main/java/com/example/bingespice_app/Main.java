package com.example.bingespice_app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Homepage.fxml"));
        Scene scene = new Scene(root);

        // Set a default size before maximizing
        primaryStage.setWidth(1280);
        primaryStage.setHeight(720);
        // Set the stage icon
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/Logo.png")));
        // Optional: center the window before maximizing
        primaryStage.centerOnScreen();

        // Add listener to handle when maximized is changed
        primaryStage.maximizedProperty().addListener((obs, wasMaximized, isNowMaximized) -> {
            if (isNowMaximized) {
                // You can check screen bounds and ensure it doesn't exceed a max
                javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
                if (screenBounds.getWidth() < 1280 || screenBounds.getHeight() < 720) {
                    // If the screen is too small, don't maximize; instead, set default size
                    primaryStage.setMaximized(false);
                    primaryStage.setWidth(1280);
                    primaryStage.setHeight(720);
                    primaryStage.centerOnScreen();
                }
            }
        });

        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}