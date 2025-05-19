package com.example.bingespice_app;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class HomepageController implements Initializable {

    @FXML
    private HBox recommendedHBox; // HBox for the "Recommended" section

    @FXML
    private HBox watchNextHBox;   // HBox for the "Watch Next" section

    private TMDBManager tmdbManager; // Class to handle TMDB API calls

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tmdbManager = new TMDBManager(); // Initialize the API manager
        loadRecommendedMedia();          // Load "Recommended" section
        loadWatchNextMedia();            // Load "Watch Next" section
    }

    /** Loads up to 10 random items into the "Recommended" section */
    private void loadRecommendedMedia() {
        try {
            List<Media> popularMedia = tmdbManager.getPopularMedia();
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
            List<Media> newReleases = tmdbManager.getNewReleases();
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

    /** Creates a Pane for a single media item, styled like the search results */
    private Pane createMediaPane(Media media) {
        Pane pane = new Pane();
        pane.setPrefSize(186, 320);
        pane.setStyle("-fx-background-color: #2C2D40;");

        VBox contentBox = new VBox(5);
        contentBox.setAlignment(Pos.CENTER);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(185);
        imageView.setFitHeight(232);
        imageView.setPreserveRatio(true);
        imageView.setImage(media.getPosterUrl() != null ? new Image(media.getPosterUrl()) : new Image("http://via.placeholder.com/1080x1580"));

        Label titleLabel = new Label(media.getTitle());
        titleLabel.setTextFill(Color.web("#fd6108"));
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(185);
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        titleLabel.setAlignment(Pos.CENTER);

        Label typeLabel = new Label(media.getType().equals("movie") ? "Movie" : "TV Series");
        typeLabel.setTextFill(Color.WHITE);
        typeLabel.setFont(Font.font("System", 12));
        typeLabel.setAlignment(Pos.CENTER);
        contentBox.getChildren().addAll(imageView, titleLabel, typeLabel);
        pane.getChildren().add(contentBox);

        // Center the contentBox within the pane
        contentBox.layoutXProperty().bind(pane.widthProperty().subtract(contentBox.widthProperty()).divide(2));
        contentBox.layoutYProperty().bind(pane.heightProperty().subtract(contentBox.heightProperty()).divide(2));

        // Add hover effect
        pane.setOnMouseEntered(event -> {
            pane.setStyle("-fx-background-color: #2A2035; -fx-border-color: #2A2035; -fx-border-width: 1px; -fx-border-radius: 5px;");
        });
        pane.setOnMouseExited(event -> {
            pane.setStyle("-fx-background-color: #2C2D40;");
        });

        return pane;
    }
}