package com.example.bingespice_app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button; // Added
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField; // Added
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.text.Text;


import java.io.IOException;

public class Controller {

    @FXML
    private Text settingsContentText;

    @FXML
    private MenuButton searchOptionsMenuButton;

    @FXML
    private TextField searchField; // Now recognized

    @FXML
    private Button searchButton; // Now recognized

    public void initialize() {
        searchButton.disableProperty().bind(searchField.textProperty().length().lessThan(2));
    }

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

    public void loadSearch_Results_Selected(ActionEvent event) throws IOException {

        if (searchField.getText().length() < 2) {
            return;
        }


        Parent homepageRoot = FXMLLoader.load(getClass().getResource("Search Results_Selected.fxml"));
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
    private void handleSearchOption(ActionEvent event) {
        MenuItem selectedItem = (MenuItem) event.getSource();
        searchOptionsMenuButton.setText(selectedItem.getText());
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
}
