package com.example.bingespice_app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Homepage.fxml"));
        Scene scene = new Scene(root);

        // Add CSS for proper resize handling
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        // Set initial size and position
        primaryStage.setWidth(1280);
        primaryStage.setHeight(720);
        primaryStage.centerOnScreen();

        // Configure stage
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/Logo.png")));

        // Add resize listener
        ResizeHelper.addResizeListener(primaryStage);

        // Maximization listener
        primaryStage.maximizedProperty().addListener((obs, wasMaximized, isNowMaximized) -> {
            javafx.geometry.Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            if (isNowMaximized && (screenBounds.getWidth() < 1280 || screenBounds.getHeight() < 720)) {
                primaryStage.setMaximized(false);
                primaryStage.setWidth(Math.min(1280, screenBounds.getWidth()));
                primaryStage.setHeight(Math.min(720, screenBounds.getHeight()));
                primaryStage.centerOnScreen();
            }
        });

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}