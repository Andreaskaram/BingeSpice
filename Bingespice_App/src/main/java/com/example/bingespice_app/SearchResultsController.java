package com.example.bingespice_app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class SearchResultsController implements Initializable {

    @FXML
    private FlowPane mediaFlowPane;

    @FXML
    private TextField searchField;

    private SearchHandler searchHandler;

    @FXML
    private HBox recommendedHBox; // HBox for the "Recommended" section

    @FXML
    private HBox watchNextHBox;   // HBox for the "Watch Next" section

    @FXML
    private TMDBManager TMDBManager;

    @FXML
    private Button searchButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchHandler = new SearchHandler();
        searchButton.disableProperty().bind(searchField.textProperty().length().lessThan(2));
        // Add Enter key listener to searchField
        searchField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleSearch(null); // Trigger handleSearch with null event
            }
        });

        TMDBManager = new TMDBManager(); // Initialize the API manager
        loadRecommendedMedia();          // Load "Recommended" section
        loadWatchNextMedia();            // Load "Watch Next" section
    }

    public void setMedia(List<Media> mediaItems) {
        mediaFlowPane.getChildren().clear(); // Clear existing children
        for (Media media : mediaItems) {
            Pane mediaPane = createMediaPane(media);
            mediaFlowPane.getChildren().add(mediaPane);
        }
    }

    /** Loads up to 10 random items into the "Recommended" section */
    private void loadRecommendedMedia() {
        try {
            List<Media> popularMedia = TMDBManager.getPopularMedia();
            Collections.shuffle(popularMedia); // Shuffle to get random items
            List<Media> recommended = popularMedia.subList(0, Math.min(8, popularMedia.size()));
            for (Media media : recommended) {
                Pane mediaPane = createMediaPane(media);
                recommendedHBox.getChildren().add(mediaPane);
            }

        } catch (Exception e) {
            e.printStackTrace(); // Handle any errors
        }
    }

    /** Loads up to 10 random items into the "Watch Next" section */
    private void loadWatchNextMedia() {
        try {
            List<Media> newReleases = TMDBManager.getNewReleases();
            Collections.shuffle(newReleases); // Shuffle to get random items
            List<Media> watchNext = newReleases.subList(0, Math.min(8, newReleases.size()));
            for (Media media : watchNext) {
                Pane mediaPane = createMediaPane(media);
                watchNextHBox.getChildren().add(mediaPane);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Handle any errors
        }
    }


    private Pane createMediaPane(Media media) {
        Pane pane = new Pane();
        pane.setPrefSize(186, 320); // Height accommodates type label
        pane.setStyle("-fx-background-color: #2C2D40;");

        VBox contentBox = new VBox(5); // VBox with 5px spacing between elements
        contentBox.setAlignment(javafx.geometry.Pos.CENTER); // Center children within VBox

        ImageView imageView = new ImageView();
        imageView.setFitWidth(185);
        imageView.setFitHeight(232);
        imageView.setPreserveRatio(true);
        imageView.setImage(media.getPosterUrl() != null ? new Image(media.getPosterUrl()) : new Image("http://via.placeholder.com/1080x1580"));

        Label titleLabel = new Label(media.getTitle());
        titleLabel.setTextFill(Color.web("#fd6108"));
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(185);
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 14)); // Smaller size (14px) with bold
        titleLabel.setAlignment(javafx.geometry.Pos.CENTER);

        Label typeLabel = new Label(media.getType().equals("movie") ? "Movie" : "TV Series");
        typeLabel.setTextFill(Color.WHITE);
        typeLabel.setFont(new Font("System", 12));
        typeLabel.setAlignment(javafx.geometry.Pos.CENTER);

        contentBox.getChildren().addAll(imageView, titleLabel, typeLabel);

        // Center the VBox within the Pane
        pane.getChildren().add(contentBox);
        contentBox.layoutXProperty().bind(pane.widthProperty().subtract(contentBox.widthProperty()).divide(2));
        contentBox.layoutYProperty().bind(pane.heightProperty().subtract(contentBox.heightProperty()).divide(2));

        // Add hover effect for a subtle border
        pane.setOnMouseEntered(event -> {
            pane.setStyle("-fx-background-color: #2A2035; -fx-border-color: #2A2035; -fx-border-width: 1px; -fx-border-radius: 5px;");
        });
        pane.setOnMouseExited(event -> {
            pane.setStyle("-fx-background-color: #2C2D40;"); // Remove border on exit
        });

        return pane;
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        String query = searchField.getText();
        if (query == null || query.trim().isEmpty() || query.length() < 2) {
            return;
        }
        searchHandler.handleSearch(query, searchField);
    }

    @FXML
    private void handleSearchOption(ActionEvent event) {
        // Placeholder for handling search options (e.g., Title, Genre, Actor, Director)
        // Implement logic if needed to filter search results based on selected option
    }

    // Navigation methods (assuming they exist in TopBar.fxml or SideBar.fxml)
    @FXML
    private void loadHomepage() throws IOException {
        // Implementation as needed
    }

    @FXML
    private void loadSettings() throws IOException {
        // Implementation as needed
    }

    @FXML
    private void loadProfile() throws IOException {
        // Implementation as needed
    }
}