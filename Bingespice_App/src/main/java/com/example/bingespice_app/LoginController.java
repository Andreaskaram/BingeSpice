package com.example.bingespice_app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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

    @FXML private TextField loginUsername;      // Username input field
    @FXML private PasswordField loginPassword;  // Password input field
    @FXML private CheckBox rememberMe;          // "Remember Me" checkbox
    @FXML private Text loginError;              // Error message display

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
}