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
    private TextField searchField;

    @FXML
    private Button searchButton;

    private SearchHandler searchHandler;

    public void initialize() {
        searchHandler = new SearchHandler();
        searchButton.disableProperty().bind(searchField.textProperty().length().lessThan(2));

        searchField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                loadSearch_Results_Selected(null);
            }
        });
    }

    public void loadSearch_Results_Selected(ActionEvent event) {
        String searchTerm = searchField.getText();
        if (searchTerm.length() < 2) {
            return;
        }
        searchHandler.handleSearch(searchTerm, event != null ? (Node) event.getSource() : searchField);
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