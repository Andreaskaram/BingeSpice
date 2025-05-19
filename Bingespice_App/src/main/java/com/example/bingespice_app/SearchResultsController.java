package com.example.bingespice_app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.List;

public class SearchResultsController {
    @FXML
    private ListView<Media> moviesListView;

    public void setMedia(List<Media> mediaItems) {
        moviesListView.getItems().setAll(mediaItems);
        moviesListView.setCellFactory(listView -> new MediaListCell());
    }

    private static class MediaListCell extends ListCell<Media> {
        private final ImageView imageView = new ImageView();
        private final Label titleLabel = new Label();
        private final Label typeLabel = new Label();
        private final VBox vbox = new VBox(imageView, titleLabel, typeLabel);

        public MediaListCell() {
            imageView.setFitWidth(100);
            imageView.setFitHeight(150);
            titleLabel.setTextFill(Color.web("#fd6108")); // Title color as specified
            titleLabel.setWrapText(true);
            titleLabel.setMaxWidth(100);
            typeLabel.setTextFill(Color.WHITE); // Type label in white for contrast
            setStyle("-fx-background-color: #2C2D40;"); // Cell background as specified
            setOnMouseClicked(event -> {
                Media media = getItem();
                if (media != null) {
                    System.out.println("Selected " + media.getType() + ": " + media.getTitle());
                    // TODO: Load media details screen
                }
            });
        }

        @Override
        protected void updateItem(Media media, boolean empty) {
            super.updateItem(media, empty);
            if (empty || media == null) {
                setGraphic(null);
            } else {
                imageView.setImage(media.getPosterUrl() != null ? new Image(media.getPosterUrl()) : new Image("http://via.placeholder.com/1080x1580"));
                titleLabel.setText(media.getTitle());
                typeLabel.setText(media.getType().equals("movie") ? "Movie" : "TV Series");
                setGraphic(vbox);
            }
        }
    }

    // Navigation methods (assuming they exist in TopBar.fxml or SideBar.fxml)
    @FXML
    private void loadHomepage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Homepage.fxml"));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }

    @FXML
    private void loadSettings(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Settings.fxml"));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }

    @FXML
    private void loadProfile(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Profile.fxml"));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }
}