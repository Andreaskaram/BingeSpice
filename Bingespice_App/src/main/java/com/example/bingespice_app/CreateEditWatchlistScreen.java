package com.example.bingespice_app;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CreateEditWatchlistScreen {

    @FXML private Label modeLabel;
    @FXML private TextField watchlistName;
    @FXML private Button confirmButton;
    @FXML private Button deleteButton;
    @FXML private Button confirmChangesButton;
    @FXML private Label errorLabel;

    private String viewMode;

    public void setParameters(String viewMode){
        this.viewMode = viewMode;
        toggleButton();
    }

    //Button when creating new watchlist
    @FXML
    private void handleConfirmButton() {
        String watchlistName = this.watchlistName.getText().trim();
        if(watchlistName.isEmpty()){
            System.out.println("Watchlist name cannot be empty");
            errorLabel.setText("Watchlist name cannot be empty");
            errorLabel.setVisible(true);
            return;
        }
        boolean success = WatchlistHandler.confirmNewWatchlist(watchlistName);
        if (success) {
            System.out.println("Watchlist created");
            errorLabel.setText("Watchlist created!");
            errorLabel.setVisible(true);
            errorLabel.setStyle("-fx-text-fill: #00FF00;");
            confirmButton.setDisable(true);
        } else {
            System.out.println("Watchlist not created");
            errorLabel.setText("Error creating watchlist");
            errorLabel.setVisible(true);
        }
    }

    //Button when editing watchlist name
    @FXML private void handleConfirmChangesButton() {
        String watchlistName = this.watchlistName.getText().trim();
        if(watchlistName.isEmpty()){
            System.out.println("Watchlist name cannot be empty");
            errorLabel.setText("Watchlist name cannot be empty");
            errorLabel.setVisible(true);
            return;
        }
        boolean success = WatchlistHandler.confirmUpdateWatchlist(watchlistName);
        if (success) {
            errorLabel.setText("Watchlist Updated!");
            errorLabel.setVisible(true);
            errorLabel.setStyle("-fx-text-fill: #00FF00;");
            confirmChangesButton.setDisable(true);
            deleteButton.setDisable(true);
        } else {
            System.out.println("Watchlist not created");
            errorLabel.setText("Error updating watchlist");
            errorLabel.setVisible(true);
        }
    }

    @FXML
    private void handleDeleteButton() {
        boolean success = WatchlistHandler.confirmDeleteWatchlist();
        if (success) {
            errorLabel.setText("Watchlist deleted successfully!");
            errorLabel.setVisible(true);
            errorLabel.setStyle("-fx-text-fill: #00FF00;");
            confirmChangesButton.setDisable(true);
            deleteButton.setDisable(true);
        } else {
            errorLabel.setText("Error updating watchlist");
            errorLabel.setVisible(true);
        }
    }

    private void toggleButton(){
        if(this.viewMode.equals("Edit")){
            confirmButton.setDisable(true);
            confirmButton.setVisible(false);
            confirmChangesButton.setDisable(false);
            confirmChangesButton.setVisible(true);
            deleteButton.setVisible(true);
            deleteButton.setDisable(false);

            modeLabel.setText("Rename or Delete Watchlist");
            watchlistName.setPromptText("Rename selected watchlist: " + Session.getSelectedWatchlistName());
            if(Session.getSelectedWatchlistID() == 0){
                errorLabel.setText("No watchlist selected!");
                errorLabel.setVisible(true);
                confirmChangesButton.setDisable(true);
                deleteButton.setDisable(true);
            }
        }
    }
}
