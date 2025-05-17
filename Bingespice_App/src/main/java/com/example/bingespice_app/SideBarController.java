package com.example.bingespice_app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class SideBarController {

    @FXML
    private Text settingsContentText;

    public void loadHomepage(ActionEvent event) throws IOException {
        Parent homepageRoot = FXMLLoader.load(getClass().getResource("Homepage.fxml"));
        Scene currentScene = ((Node) event.getSource()).getScene();
        currentScene.setRoot(homepageRoot); // Directly update the scene's root
    }

    public void loadProfile(ActionEvent event) throws IOException {
        Parent homepageRoot = FXMLLoader.load(getClass().getResource("Profile.fxml"));
        Scene currentScene = ((Node) event.getSource()).getScene();
        currentScene.setRoot(homepageRoot); // Directly update the scene's root


    }

    public void loadSettings(ActionEvent event) throws IOException {
        Parent homepageRoot = FXMLLoader.load(getClass().getResource("Settings.fxml"));
        Scene currentScene = ((Node) event.getSource()).getScene();
        currentScene.setRoot(homepageRoot); // Directly update the scene's root

    }

    public void loadWatchlist(ActionEvent event) throws IOException {
        Parent homepageRoot = FXMLLoader.load(getClass().getResource("Watchlist.fxml"));
        Scene currentScene = ((Node) event.getSource()).getScene();
        currentScene.setRoot(homepageRoot); // Directly update the scene's root

    }

    @FXML
    public void handleLogin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent loginRoot = loader.load();

        Stage loginStage = new Stage();
        loginStage.setScene(new Scene(loginRoot));
        loginStage.setTitle("Login");

        // Set fixed size
        loginStage.setWidth(350);
        loginStage.setHeight(400);
        loginStage.setResizable(false);

        // 🔥 Remove OS-native bar
        loginStage.initStyle(javafx.stage.StageStyle.UNDECORATED);

        // Optional modal
        loginStage.initOwner(((Node) event.getSource()).getScene().getWindow());
        loginStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        loginStage.centerOnScreen();

        loginStage.show();
    }
}
