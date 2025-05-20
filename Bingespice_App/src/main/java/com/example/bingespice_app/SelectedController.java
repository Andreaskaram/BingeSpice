package com.example.bingespice_app;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    private TMDBManager tmdbManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tmdbManager = new TMDBManager();
    }

    public void setMediaDetails(Media media) {
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}