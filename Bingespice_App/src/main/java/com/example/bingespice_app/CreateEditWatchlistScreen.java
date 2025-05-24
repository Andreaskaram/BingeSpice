package com.example.bingespice_app;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CreateEditWatchlistScreen {

    @FXML private Label modeLabel;

    private String viewMode;

    public void setParameters(String viewMode){
        this.viewMode = viewMode;
        modeLabel.setText(viewMode);
    }

    private void setCreateMode() {
        String viewMode = this.viewMode;

        if(viewMode.equals("Create")){
            modeLabel.setText("Create New Watchlist");
        } else if(viewMode.equals("Edit")){
            modeLabel.setText("Edit Watchlist");
        }
    }

    @FXML
    private void confirmCreateWatchlist() {
        boolean success = WatchlistHandler.confirmNewWatchlist("Mhtsos", "Personal", 1);
        if (success) {
            System.out.println("Watchlist created");
        } else {
            System.out.println("Watchlist not created");
        }
    }
}
