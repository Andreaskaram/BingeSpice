package com.example.bingespice_app;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

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
    @FXML private Accordion seasonsAccordion;
    @FXML private Button removeFromWatchedButton;
    @FXML private Button markAsWatchedButton;

    private TMDBManager tmdbManager;
    private Media selectedMedia;
    private JSONObject selectedJson;
    private Map<Integer, List<String>> seasonEpisodesMap = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tmdbManager = new TMDBManager();
    }

    public void setMediaDetails(Media media) {
        this.selectedMedia = media;
        try {
            JSONObject details = tmdbManager.getMediaDetails(media.getId(), media.getType());
            this.selectedJson = details;

            titleLabel.setText(media.getTitle());
            overviewLabel.setText(media.getOverview());
            ratingLabel.setText(String.format("★ %.1f/10", media.getVoteAverage()));
            typeLabel.setText(media.getType().equalsIgnoreCase("movie") ? "Movie" : "TV Series");

            if (media.getType().equalsIgnoreCase("tv")) {
                fetchSeriesSeasons();
                updateSeasonAccordion();
            }
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

    private void fetchSeriesSeasons() {
        if (!selectedMedia.getType().equalsIgnoreCase("tv")) {
            return;
        }

        int seasonCount = selectedJson.optInt("number_of_seasons", 0);
        if (seasonCount == 0) {
            return;
        }

        seasonEpisodesMap.clear();

        for (int i = 1; i <= seasonCount; i++) {
            try {
                System.out.println("trying to fetch season " + i);
                JSONObject seasonDetails = tmdbManager.getSeasonDetails(selectedMedia.getId(), i);
                System.out.println("found season " + i);

                JSONArray episodesArray = seasonDetails.optJSONArray("episodes");
                if (episodesArray != null) {
                    List<String> episodeNames = new ArrayList<>();
                    for (int j = 0; j < episodesArray.length(); j++) {
                        JSONObject episode = episodesArray.getJSONObject(j);
                        String episodeName = episode.optString("name", "Unnamed Episode");
                        int episodeNumber = episode.optInt("episode_number", j + 1);
                        System.out.println("Season " + i + " - Episode " + episodeNumber + ": " + episodeName);
                        episodeNames.add("Episode " + episodeNumber + ": " + episodeName);
                    }
                    seasonEpisodesMap.put(i, episodeNames);
                }
            } catch (Exception e) {
                System.err.println("Failed to fetch details for Season " + i);
                e.printStackTrace();
            }
        }
    }
    private void updateSeasonAccordion() {
        seasonsAccordion.getPanes().clear();
        seasonsAccordion.setVisible(true);

        for (Map.Entry<Integer, List<String>> entry : seasonEpisodesMap.entrySet()) {
            int seasonNumber = entry.getKey();
            List<String> episodes = entry.getValue();

            TitledPane seasonPane = newTitledPane(seasonNumber);
            VBox episodesBox = createEpisodesBox(episodes);
            seasonPane.setContent(episodesBox);
            seasonsAccordion.getPanes().add(seasonPane);
        }
    }

    private TitledPane newTitledPane(int seasonNumber) {
        TitledPane pane = new TitledPane();
        pane.setText("Season " + seasonNumber);
        pane.setStyle("-fx-text-fill: #FD6108; -fx-font-weight: bold; -fx-font-size: 14px;");
        return pane;
    }

    private VBox createEpisodesBox(List<String> episodes) {
        VBox episodesBox = new VBox(3); // Reduced spacing
        episodesBox.setPadding(new Insets(5, 10, 10, 20)); // Compact padding

        for (String episode : episodes) {
            HBox episodeRow = createEpisodeRow(episode);
            episodesBox.getChildren().add(episodeRow);
        }

        return episodesBox;
    }

    private HBox createEpisodeRow(String episode) {
        HBox row = new HBox(8); // Reduced spacing
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(2, 0, 2, 0)); // Compact vertical padding

        // Episode label styling
        Label episodeLabel = new Label(episode);
        episodeLabel.setStyle("-fx-text-fill: #FD6108; -fx-font-size: 13px;");
        episodeLabel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(episodeLabel, Priority.ALWAYS);

        // Compact toggle button for "Mark as Watched"
        ToggleButton markAsWatchedButton = createCompactButton();

        row.getChildren().addAll(episodeLabel, markAsWatchedButton);
        return row;
    }

    private ToggleButton createCompactButton() {
        ToggleButton button = new ToggleButton("✓");
        button.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-text-fill: #FD6108; " +
                        "-fx-font-size: 12px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 2 6; " +  // More compact
                        "-fx-border-color: #FD6108; " +
                        "-fx-border-radius: 3; " +
                        "-fx-background-radius: 3; " +
                        "-fx-border-width: 1.5;"
        );

        button.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                // Selected state: orange background, blue checkmark
                button.setStyle(
                        "-fx-background-color: #FD6108; " +
                                "-fx-text-fill: #0033cc; " + // Blue checkmark color
                                "-fx-font-size: 12px; " +
                                "-fx-font-weight: bold; " +
                                "-fx-padding: 2 6; " +
                                "-fx-border-color: #FD6108; " +
                                "-fx-border-radius: 3; " +
                                "-fx-background-radius: 3; " +
                                "-fx-border-width: 1.5;"
                );
            } else {
                // Normal state: transparent background, orange checkmark
                button.setStyle(
                        "-fx-background-color: transparent; " +
                                "-fx-text-fill: #FD6108; " +
                                "-fx-font-size: 12px; " +
                                "-fx-font-weight: bold; " +
                                "-fx-padding: 2 6; " +
                                "-fx-border-color: #FD6108; " +
                                "-fx-border-radius: 3; " +
                                "-fx-background-radius: 3; " +
                                "-fx-border-width: 1.5;"
                );
            }
        });

        // Hover effects should be updated based on the selected state
        button.setOnMouseEntered(e -> {
            if (!button.isSelected()) {
                button.setStyle(
                        "-fx-background-color: rgba(253, 97, 8, 0.1); " + // Slight orange tint
                                "-fx-text-fill: #FD6108; " +
                                "-fx-font-size: 12px; " +
                                "-fx-font-weight: bold; " +
                                "-fx-padding: 2 6; " +
                                "-fx-border-color: #FD6108; " +
                                "-fx-border-radius: 3; " +
                                "-fx-background-radius: 3; " +
                                "-fx-border-width: 1.5;"
                );
            }
        });

        button.setOnMouseExited(e -> {
            if (!button.isSelected()) {
                button.setStyle(
                        "-fx-background-color: transparent; " +
                                "-fx-text-fill: #FD6108; " +
                                "-fx-font-size: 12px; " +
                                "-fx-font-weight: bold; " +
                                "-fx-padding: 2 6; " +
                                "-fx-border-color: #FD6108; " +
                                "-fx-border-radius: 3; " +
                                "-fx-background-radius: 3; " +
                                "-fx-border-width: 1.5;"
                );
            }
        });

        return button;
    }

}