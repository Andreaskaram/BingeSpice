package com.example.bingespice_app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.io.IOException;
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

        String firstName = signupFirstName.getText();
        String lastName = signupLastName.getText();
        String username = signupUsername.getText();
        String password = signupPassword.getText();
        String email = signupEmail.getText();
        String country = signupCountry.getText();

        String selectedGender = getSelectedGender();
        if(username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            System.out.println("Username or password is empty");
            signupError.setText("Username or password is empty");
            signupError.setVisible(true);

        } else {
            String errorMsg = BingespiceDBManager.signup(username, firstName, lastName, email,
                    password, selectedGender, country);
            if(errorMsg!= null){
                signupError.setText(errorMsg);
                signupError.setVisible(true);
            }
        }
    }

    /**
     * Hardcoded credential check (replace with real auth logic later).
     * @param username input username
     * @param password input password
     * @return true if credentials match
     */
    private boolean checkCredentials(String username, String password) {
        // Only allows admin/admin for now
        return "admin".equals(username) && "admin".equals(password);
    }

    private String getSelectedGender() {
        RadioButton selectedRadio = (RadioButton) genderToggleGroup.getSelectedToggle();
        return selectedRadio != null ? selectedRadio.getText() : null;
    }
}