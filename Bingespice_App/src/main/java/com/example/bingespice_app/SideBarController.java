package com.example.bingespice_app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import java.io.IOException;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class SideBarController {

    @FXML
    private Text settingsContentText;

    private static final String PREF_USER = "savedUser";
    private static final String PREF_PASS = "savedPass";

    public void loadHomepage(ActionEvent event) throws IOException {
        Parent homepageRoot = FXMLLoader.load(getClass().getResource("Homepage.fxml"));
        Scene currentScene = ((Node) event.getSource()).getScene();
        currentScene.setRoot(homepageRoot);
    }

    public void loadProfile(ActionEvent event) throws IOException {
        Parent profileRoot = FXMLLoader.load(getClass().getResource("Profile.fxml"));
        Scene currentScene = ((Node) event.getSource()).getScene();
        currentScene.setRoot(profileRoot);
    }

    public void loadSettings(ActionEvent event) throws IOException {
        Parent settingsRoot = FXMLLoader.load(getClass().getResource("Settings.fxml"));
        Scene currentScene = ((Node) event.getSource()).getScene();
        currentScene.setRoot(settingsRoot);
    }

    public void loadWatchlist(ActionEvent event) throws IOException {
        Parent watchlistRoot = FXMLLoader.load(getClass().getResource("Watchlist.fxml"));
        Scene currentScene = ((Node) event.getSource()).getScene();
        currentScene.setRoot(watchlistRoot);
    }

    @FXML
    private void handleLogout(ActionEvent event) throws IOException {
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        try {
            // Remove the entire preferences node
            prefs.removeNode();
            prefs.flush(); // Save the changes to the system
        } catch (BackingStoreException e) {
            System.err.println("Failed to remove preferences: " + e.getMessage());
        }

        // Check if the preferences are really gone
        Preferences checkPrefs = Preferences.userNodeForPackage(Main.class);
        String checkUser = checkPrefs.get("savedUser", "default");
        String checkPass = checkPrefs.get("savedPass", "default");
        System.out.println("After logout: user=" + checkUser + ", pass=" + checkPass);

        // Clear the session
        Session.setUsername(null);

        // Switch to the login screen
        Parent loginRoot = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene currentScene = ((Node) event.getSource()).getScene();
        currentScene.setRoot(loginRoot);
    }
}