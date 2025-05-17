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
    private void handleSettingsButton(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        settingsContentText.setText(clickedButton.getText());

        switch(clickedButton.getText()) {
            case "Account Settings":
                settingsContentText.setText("Account Settings");
                break;
            case "Privacy Settings":
                settingsContentText.setText("Privacy Settings");
                break;
            case "App Settings":
                settingsContentText.setText("App Settings");
                break;
            case "Notifications":
                settingsContentText.setText("Notification settings");
                break;
            case "Support":
                settingsContentText.setText("Support");
                break;
        }
    }

    @FXML
    public void handleLogin(ActionEvent event) throws IOException {
        Parent loginRoot = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Stage loginStage = new Stage();
        loginStage.setScene(new Scene(loginRoot));
        loginStage.setTitle("Login");
        loginStage.show();
    }
}
