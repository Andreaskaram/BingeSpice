package com.example.bingespice_app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.util.prefs.Preferences;

/**
 * Entry point for the BingeSpice JavaFX application.
 * Handles initial scene loading and window configuration.
 */
public class Main extends Application {
    // Preference keys for storing user credentials
    private static final String PREF_USER = "savedUser";
    private static final String PREF_USER_ID = "savedUserID";
    private static final String PREF_PASS = "savedPass";

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Retrieve stored preferences (if any)
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        String savedUser = prefs.get(PREF_USER, "");
        String savedUserID = prefs.get(PREF_USER_ID, "");
        String savedPass = prefs.get(PREF_PASS, "");

        Parent root;
        // If credentials exist, auto-login to homepage
        if (!savedUser.isEmpty() && !savedPass.isEmpty()) {
            // Store in session for use in other controllers
            Session.setUsername(savedUser);
            int savedUserIdint = Integer.parseInt(savedUserID);
            Session.setUserID(savedUserIdint);
            root = FXMLLoader.load(getClass().getResource("Homepage.fxml"));
        } else {
            // Else, show login screen first
            root = FXMLLoader.load(getClass().getResource("login.fxml"));
        }

        // Create scene and apply CSS
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        // Set default window size and center on screen
        primaryStage.setWidth(1280);
        primaryStage.setHeight(720);
        primaryStage.centerOnScreen();

        // Remove window decorations for custom styling
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(scene);

        // Set application icon
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/Logo.png")));

        // Enable window resizing handlers
        ResizeHelper.addResizeListener(primaryStage);

        // Custom maximize behavior: don't exceed minimum designed resolution
        primaryStage.maximizedProperty().addListener((obs, wasMax, isMax) -> {
            javafx.geometry.Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
            if (isMax && (bounds.getWidth() < 1280 || bounds.getHeight() < 720)) {
                primaryStage.setMaximized(false);
                primaryStage.setWidth(Math.min(1280, bounds.getWidth()));
                primaryStage.setHeight(Math.min(720, bounds.getHeight()));
                primaryStage.centerOnScreen();
            }
        });

        // Display the primary stage
        primaryStage.show();
    }

    /**
     * Standard main method to launch the JavaFX application.
     */
    public static void main(String[] args) {
        launch();
    }
}