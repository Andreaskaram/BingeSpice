package com.example.bingespice_app;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ResourceBundle;

public class SelectedController implements Initializable {
    @FXML private ImageView posterImageView;
    @FXML private Label titleLabel;
    @FXML private Label overviewLabel;
    @FXML private Label ratingLabel;
    @FXML private Label typeLabel;
    @FXML private Label genresLabel;
    @FXML private Label releaseDateLabel;
    @FXML private Label actorsLabel;
    @FXML private Label directorsLabel;
    @FXML private Label runtimeLabel;
    @FXML private Label seasonsLabel;

    @FXML private Button removeFromWatchedButton;
    @FXML private Button markAsWatchedButton;

    private TMDBManager tmdbManager;
    private Media selectedMedia;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tmdbManager = new TMDBManager();
    }

    public void setMediaDetails(Media media) {
        this.selectedMedia = media;
        try {
            JSONObject details = tmdbManager.getMediaDetails(media.getId(), media.getType());

            titleLabel.setText(media.getTitle());
            overviewLabel.setText(media.getOverview());
            ratingLabel.setText(String.format("★ %.1f/10", media.getVoteAverage()));
            typeLabel.setText(media.getType().equalsIgnoreCase("movie") ? "Movie" : "TV Series");

            if (media.getPosterUrl() != null) {
                posterImageView.setImage(new Image(media.getPosterUrl(), true));
            }

            JSONArray genres = details.getJSONArray("genres");
            StringBuilder genresText = new StringBuilder();
            for (int i = 0; i < genres.length(); i++) {
                genresText.append(genres.getJSONObject(i).getString("name"));
                if (i < genres.length() - 1) genresText.append(", ");
            }
            genresLabel.setText(genresText.toString());

            String date = details.optString(media.getType().equals("movie") ? "release_date" : "first_air_date", "N/A");
            releaseDateLabel.setText(media.getType().equals("movie") ? "Release Date: " + date : "First Air Date: " + date);
            actorsLabel.setText("Actors: " + details.optString("actors", "N/A"));
            directorsLabel.setText("Director(s): " + details.optString("directors", "N/A"));

            if (media.getType().equalsIgnoreCase("movie")) {
                int runtime = details.optInt("runtime", 0);
                runtimeLabel.setText("Duration: " + runtime + " mins");
                seasonsLabel.setVisible(false); // Hide for movies
            } else {
                runtimeLabel.setText("Episodes: " + details.optInt("episodes", 0));
                seasonsLabel.setText("Seasons: " + details.optInt("seasons", 0));
                seasonsLabel.setVisible(true);
            }
            WatchedHandler watched = new WatchedHandler();
            boolean watchedStatus = watched.checkIfWatched(selectedMedia);
            if (watchedStatus) {
                setRemoveFromWatchedButton();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMarkAsWatched() {
        WatchedHandler watched = new WatchedHandler();
        boolean success = watched.markAsWatched(selectedMedia);
        if(success) {
            setRemoveFromWatchedButton();
        } else {
            System.out.println("Error marking as watched");
        }
    }

    @FXML
    private void handleRemoveFromWatched() {
        System.out.println("handleRemoveFromWatched");
        WatchedHandler watched = new WatchedHandler();
        boolean success = watched.deleteFromWatched(selectedMedia);
        if(success) {
            setMarkAsWatchedButton();
        } else {
            System.out.println("Error deleting from watched");
        }
    }

    private void setMarkAsWatchedButton() {
        removeFromWatchedButton.setVisible(false);
        removeFromWatchedButton.setDisable(true);
        markAsWatchedButton.setVisible(true);
        markAsWatchedButton.setDisable(false);
    }

    private void setRemoveFromWatchedButton() {
        markAsWatchedButton.setVisible(false);
        markAsWatchedButton.setDisable(true);
        removeFromWatchedButton.setVisible(true);
        removeFromWatchedButton.setDisable(false);
    }
}