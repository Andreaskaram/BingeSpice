package com.example.bingespice_app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import java.io.IOException;
import javafx.application.Platform;

public class Controller {
    @FXML
    private Text settingsContentText;

    @FXML
    private MenuButton searchOptionsMenuButton;



    @FXML
    private void handleSearchOption(ActionEvent event) {
        MenuItem selectedItem = (MenuItem) event.getSource();
        searchOptionsMenuButton.setText(selectedItem.getText());
    }

    @FXML
    private void handleSettingsButton(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        settingsContentText.setText(clickedButton.getText());

        switch (clickedButton.getText()) {
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