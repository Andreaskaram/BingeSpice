package com.example.bingespice_app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class LoginController {

    @FXML
    private TextField loginUsername;

    @FXML
    private PasswordField loginPassword;

    @FXML
    private Text loginError;

    @FXML
    private void handleLoginAttempt(ActionEvent event) {
        // Simple login check (replace with your actual logic)
        boolean isValid = checkCredentials(loginUsername.getText(), loginPassword.getText());
        if (!isValid) {
            loginError.setText("Invalid username or password");
            loginError.setVisible(true);
        } else {
            try {
                // Load Homepage.fxml after successful login
                Parent root = FXMLLoader.load(getClass().getResource("Homepage.fxml"));
                Scene currentScene = ((Node) event.getSource()).getScene();
                currentScene.setRoot(root); // Replace the scene content, keeping the window size
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkCredentials(String username, String password) {
        // Placeholder: Replace with actual database or logic check
        return "admin".equals(username) && "password".equals(password);
    }
}