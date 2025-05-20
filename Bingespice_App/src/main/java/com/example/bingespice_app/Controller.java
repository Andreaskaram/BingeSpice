package com.example.bingespice_app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.text.Text;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import javafx.application.Platform;
import javafx.scene.image.ImageView;

public class Controller {
    @FXML
    private Text settingsContentText;

    @FXML
    private MenuButton searchOptionsMenuButton;

    @FXML private Text firstNameText;
    @FXML private Text lastNameText;
    @FXML private Text emailText;
    @FXML private Text genderText;
    @FXML private Text countryText;
    @FXML private Text usernameText;
    @FXML private ImageView profileImageView;
    @FXML private Button editProfileButton;



    @FXML
    public void initialize() {
        // Load user details when the profile view is loaded
        loadUserDetails();
    }

    private void loadUserDetails() {
        String username = Session.getUsername();
        if (username != null && !username.isEmpty()) {
            User user = BingespiceDBManager.getUserDetails(username);
            if (user != null) {
                // Update the UI elements with user data
                Platform.runLater(() -> {
                    firstNameText.setText(user.getFirstName());
                    lastNameText.setText(user.getLastName());
                    usernameText.setText(username);
                    emailText.setText(user.getEmail());
                    genderText.setText(user.getGender());
                    countryText.setText(user.getCountry());
                    byte[] imageData = user.getProfilePicture();
                    if (imageData != null) {
                        Image image = new Image(new ByteArrayInputStream(imageData));
                        profileImageView.setImage(image);
                    }
                    else {
                        // Load default image from resources
                        Image defaultImage = new Image(
                                getClass().getResourceAsStream("/profile-user.png")
                        );
                        profileImageView.setImage(defaultImage);

                        // You can also load a profile image here if you have one in the DB
                        // profileImageView.setImage(...);
                    }
                });
            }
        }
    }
    @FXML
    private void handleEditProfile(ActionEvent event) {
        // Implement edit profile functionality here
        System.out.println("Edit profile button clicked");
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