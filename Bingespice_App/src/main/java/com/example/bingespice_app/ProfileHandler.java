package com.example.bingespice_app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import java.io.ByteArrayInputStream;

import javafx.application.Platform;
import javafx.scene.image.ImageView;

public class ProfileHandler {
    @FXML private Text settingsContentText;
    @FXML private MenuButton searchOptionsMenuButton;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField genderField;
    @FXML private TextField countryField;
    @FXML private TextField usernameField;
    @FXML private ImageView profileImageView;
    @FXML private Button editProfileButton;
    @FXML private Button cancelButton;
    @FXML private Button applyButton;

    private User originalUser;

    @FXML
    public void initialize() {
        loadUserDetails();
    }

    private void loadUserDetails() {
        String username = Session.getUsername();
        if (username != null && !username.isEmpty()) {
            User user = BingespiceDBManager.getUserData(username);
            if (user != null) {
                Platform.runLater(() -> {
                    firstNameField.setText(user.getFirstName());
                    lastNameField.setText(user.getLastName());
                    usernameField.setText(username);
                    emailField.setText(user.getEmail());
                    genderField.setText(user.getGender());
                    countryField.setText(user.getCountry());

                    byte[] imageData = user.getProfilePicture();
                    if (imageData != null) {
                        Image image = new Image(new ByteArrayInputStream(imageData));
                        profileImageView.setImage(image);
                    } else {
                        Image defaultImage = new Image(
                                getClass().getResourceAsStream("/profile-user.png")
                        );
                        profileImageView.setImage(defaultImage);
                    }
                });
            }
        }
    }

    @FXML
    private void handleEditProfile(ActionEvent event) {
        originalUser = BingespiceDBManager.getUserData(Session.getUsername());
        setEditable(true);
        editProfileButton.setVisible(false);
        cancelButton.setVisible(true);
        applyButton.setVisible(true);
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        if (originalUser != null) {
            firstNameField.setText(originalUser.getFirstName());
            lastNameField.setText(originalUser.getLastName());
            emailField.setText(originalUser.getEmail());
            genderField.setText(originalUser.getGender());
            countryField.setText(originalUser.getCountry());
        }
        setEditable(false);
        editProfileButton.setVisible(true);
        cancelButton.setVisible(false);
        applyButton.setVisible(false);
    }

    @FXML
    private void handleApply(ActionEvent event) {
        boolean success = BingespiceDBManager.updateUser(
                Session.getUsername(),
                usernameField.getText(),
                firstNameField.getText(),
                lastNameField.getText(),
                emailField.getText(),
                genderField.getText(),
                countryField.getText()
        );

        if (success) {

            Session.setUsername(usernameField.getText());
            setEditable(false);
            editProfileButton.setVisible(true);
            cancelButton.setVisible(false);
            applyButton.setVisible(false);
            loadUserDetails();
        }
    }

    private void setEditable(boolean editable) {
        firstNameField.setEditable(editable);
        lastNameField.setEditable(editable);
        emailField.setEditable(editable);
        genderField.setEditable(editable);
        countryField.setEditable(editable);
        usernameField.setEditable(editable);

        String editableStyle = "-fx-border-color: #fd6108; -fx-background-color: transparent; -fx-text-fill: #ffffff;";
        String nonEditableStyle = "-fx-border-color: transparent; -fx-background-color: transparent; -fx-text-fill: #ffffff;";

        if (editable) {
            firstNameField.setStyle(editableStyle);
            lastNameField.setStyle(editableStyle);
            emailField.setStyle(editableStyle);
            genderField.setStyle(editableStyle);
            countryField.setStyle(editableStyle);
            usernameField.setStyle(editableStyle);
        } else {
            firstNameField.setStyle(nonEditableStyle);
            lastNameField.setStyle(nonEditableStyle);
            emailField.setStyle(nonEditableStyle);
            genderField.setStyle(nonEditableStyle);
            countryField.setStyle(nonEditableStyle);
            usernameField.setStyle(nonEditableStyle);
        }
    }


    @FXML
    private void handleSettingsButton(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        settingsContentText.setText(clickedButton.getText());
        switch (clickedButton.getText()) {
            case "Account Settings": settingsContentText.setText("Account Settings"); break;
            case "Privacy Settings": settingsContentText.setText("Privacy Settings"); break;
            case "App Settings": settingsContentText.setText("App Settings"); break;
            case "Notifications": settingsContentText.setText("Notification settings"); break;
            case "Support": settingsContentText.setText("Support"); break;
        }
    }
}