package com.example.bingespice_app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class TopBarController {

    @FXML
    private HBox titleBar;

    private double xOffset = 0;
    private double yOffset = 0;

    private boolean isMaximized = false;
    private double prevX, prevY, prevW, prevH;

    @FXML
    private void initialize() {
        titleBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        titleBar.setOnMouseDragged(event -> {
            Stage stage = (Stage) titleBar.getScene().getWindow();

            // Restore if dragging from maximized
            if (isMaximized) {
                Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
                stage.setWidth(prevW);
                stage.setHeight(prevH);
                stage.setX(event.getScreenX() - prevW / 2);
                stage.setY(event.getScreenY() - 10); // Pull it slightly below the top
                isMaximized = false;
            } else {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) titleBar.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleMinimize(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void handleResize(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        if (!isMaximized) {
            // Save current state
            prevX = stage.getX();
            prevY = stage.getY();
            prevW = stage.getWidth();
            prevH = stage.getHeight();

            // Resize to visual bounds
            Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());

            isMaximized = true;
        } else {
            // Restore
            stage.setX(prevX);
            stage.setY(prevY);
            stage.setWidth(prevW);
            stage.setHeight(prevH);
            isMaximized = false;
        }
    }
}
