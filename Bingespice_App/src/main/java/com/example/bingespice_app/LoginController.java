package com.example.bingespice_app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.prefs.Preferences;

/**
 * Controller for handling user login interactions.
 */
public class LoginController {

    public void loadSignUp(ActionEvent event) throws IOException {
        Parent homepageRoot = FXMLLoader.load(getClass().getResource("SignUp.fxml"));
        Scene currentScene = ((Node) event.getSource()).getScene();
        currentScene.setRoot(homepageRoot);
    }

    public void loadLogin(ActionEvent event) throws IOException {
        Parent homepageRoot = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene currentScene = ((Node) event.getSource()).getScene();
        currentScene.setRoot(homepageRoot);
    }
    // Login
    @FXML private TextField loginUsername;      // Username input field
    @FXML private PasswordField loginPassword;  // Password input field
    @FXML private CheckBox rememberMe;          // "Remember Me" checkbox
    @FXML private Text loginError;

    // Sign Up
    @FXML private TextField signupFirstName;
    @FXML private TextField signupLastName;
    @FXML private TextField signupUsername;
    @FXML private TextField signupEmail;
    @FXML private TextField signupCountry;
    @FXML private TextField signupPassword;
    @FXML private RadioButton maleRadio;
    @FXML private RadioButton femaleRadio;
    @FXML private ToggleGroup genderToggleGroup;
    @FXML private Text signupError;
    @FXML private ImageView profileImageView;
    private byte[] profileImageData;

    // Preference keys (should match Main.java)
    private static final String PREF_USER = "savedUser";
    private static final String PREF_PASS = "savedPass";

    /**
     * Called when the user presses the Login button.
     * @param event ActionEvent from the button click
     */
    @FXML
    private void handleLoginAttempt(ActionEvent event) {
        String user = loginUsername.getText();   // Get username text
        String pass = loginPassword.getText();   // Get password text

        // Validate credentials (currently hardcoded)
        if (!checkCredentials(user, pass)) {
            loginError.setText("Invalid username or password");
            loginError.setVisible(true);
            return; // Abort login
        }

        // If "Remember Me" is checked, store credentials in preferences
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        if (rememberMe.isSelected()) {
            prefs.put(PREF_USER, user);
            prefs.put(PREF_PASS, pass);
        } else {
            // Remove any saved prefs if unchecked
            prefs.remove(PREF_USER);
            prefs.remove(PREF_PASS);
        }

        // Store logged-in user in session
        Session.setUsername(user);

        try {
            // Load the homepage interface
            Parent root = FXMLLoader.load(getClass().getResource("Homepage.fxml"));
            // Swap the current scene root (no new window)
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (Exception e) {
            e.printStackTrace(); // Log any errors
        }
    }

    @FXML
    private void handleSignupAttempt(ActionEvent event) {
        System.out.println("Signup attempt");

        String firstName = signupFirstName.getText().trim();
        String lastName = signupLastName.getText().trim();
        String username = signupUsername.getText().trim();
        String password = signupPassword.getText().trim();
        String email = signupEmail.getText().trim();
        String country = signupCountry.getText().trim();

        // Validate all required fields
        if(username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            System.out.println("Username or password is empty");
            signupError.setText("Username and Passsword are required");
            signupError.setVisible(true);
            return;
        }

        String selectedGender = getSelectedGender();
        String errorMsg = BingespiceDBManager.signup(username, firstName, lastName, email,
                password, selectedGender, country, profileImageData);

        if(errorMsg != null){
            signupError.setText(errorMsg);
            signupError.setVisible(true);
            System.out.println(errorMsg);
        } else{
            signupError.setText("Signup successful! Please login");
            signupError.setStyle("-fx-fill: green;");
            signupError.setVisible(true);
            System.out.println("Signup successful");
        }
    }

    /**
     * Hardcoded credential check (replace with real auth logic later).
     * @param username input username
     * @param password input password
     * @return true if credentials match
     */
    private boolean checkCredentials(String username, String password) {
        return BingespiceDBManager.login(username, password);
    }

    private String getSelectedGender() {
        RadioButton selectedRadio = (RadioButton) genderToggleGroup.getSelectedToggle();
        return selectedRadio != null ? selectedRadio.getText() : null;
    }

    @FXML
    private void handleImageUpload(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                // Check file size first
                if(file.length() > 1_048_576) { // 1MB
                    signupError.setText("Image too large (max 1MB)");
                    signupError.setVisible(true);
                    return;
                }

                profileImageData = Files.readAllBytes(file.toPath());
                Image image = new Image(file.toURI().toString());
                profileImageView.setImage(image);

            } catch (IOException e) {
                e.printStackTrace();
                signupError.setText("Error loading image");
                signupError.setVisible(true);
            }
        }
    }
}