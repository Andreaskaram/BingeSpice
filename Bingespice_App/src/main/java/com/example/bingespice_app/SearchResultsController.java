package com.example.bingespice_app;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
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
    @FXML private FlowPane mediaFlowPane;
    @FXML private TextField searchField;
    @FXML private Button searchButton;
    @FXML private HBox PopularHBox;
    @FXML private HBox NewArrivalsHBox;
    @FXML private ToggleGroup searchOptionGroup;
    @FXML private RadioButton titleOption, genreOption, actorOption, directorOption;
    private String currentSearchType = "title";
    private SearchHandler searchHandler;
    private TMDBManager tmdbManager;
    @FXML private Label searchResultsTitleLabel;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeSearchHandlers();
        initializeMediaSections();
        restoreSearchType(); // Restore the radio button state
    }
    // Restore the selected search type from the previous screen
    private void restoreSearchType() {
        switch (currentSearchType) {
            case "title" -> searchOptionGroup.selectToggle(titleOption);
            case "genre" -> searchOptionGroup.selectToggle(genreOption);
            case "actor" -> searchOptionGroup.selectToggle(actorOption);
            case "director" -> searchOptionGroup.selectToggle(directorOption);
        }
    }
    // Update the search type when navigating
    public void setSearchType(String searchType) {
        this.currentSearchType = searchType;
        restoreSearchType();
    }
    private void initializeSearchHandlers() {
        searchHandler = new SearchHandler();
        searchButton.disableProperty().bind(searchField.textProperty().length().lessThan(2));
        searchField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) handleSearch(null);
        });
    }

    // Modified initialization to accept search type
    public void initializeWithSearchType(String searchType) {
        currentSearchType = searchType;
        switch (searchType) {
            case "title" -> searchOptionGroup.selectToggle(titleOption);
            case "genre" -> searchOptionGroup.selectToggle(genreOption);
            case "actor" -> searchOptionGroup.selectToggle(actorOption);
            case "director" -> searchOptionGroup.selectToggle(directorOption);
        }
        initialize(null, null); // Trigger normal initialization
    }
    private String getSelectedSearchType() {
        RadioButton selected = (RadioButton) searchOptionGroup.getSelectedToggle();
        if (selected == titleOption) return "title";
        if (selected == genreOption) return "genre";
        if (selected == actorOption) return "actor";
        if (selected == directorOption) return "director";
        return "title"; // default
    }

    private void initializeMediaSections() {
        tmdbManager = new TMDBManager();

        Task<Void> mediaLoadingTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                if (PopularHBox != null) {
                    List<Media> popularMedia = tmdbManager.getPopularMedia();
                    Collections.shuffle(popularMedia);
                    List<Media> recommended = popularMedia.subList(0, Math.min(8, popularMedia.size()));
                    Platform.runLater(() -> PopularHBox.getChildren().clear());
                    loadMediaInBackground(recommended, PopularHBox);
                }

                if (NewArrivalsHBox != null) {
                    List<Media> newReleases = tmdbManager.getNewReleases();
                    Collections.shuffle(newReleases);
                    List<Media> watchNext = newReleases.subList(0, Math.min(8, newReleases.size()));
                    Platform.runLater(() -> NewArrivalsHBox.getChildren().clear());
                    loadMediaInBackground(watchNext, NewArrivalsHBox);
                }
                return null;
            }
        };
        new Thread(mediaLoadingTask).start();
    }

    private void loadMediaInBackground(List<Media> mediaList, HBox container) {
        for (Media media : mediaList) {
            Pane mediaPane = createMediaPane(media);
            Platform.runLater(() -> container.getChildren().add(mediaPane));
        }
    }

    public void setMedia(List<Media> mediaItems) {
        if (mediaFlowPane != null) {
            mediaFlowPane.getChildren().clear();
            for (Media media : mediaItems) {
                mediaFlowPane.getChildren().add(createMediaPane(media));
            }
        }
    }

    private Pane createMediaPane(Media media) {
        Pane pane = new Pane();
        pane.setPrefSize(186, 320);
        pane.setStyle("-fx-background-color: #2C2D40;");
        pane.setOnMouseClicked(event -> handleMediaSelection(media, event)); // Pass event here

        VBox contentBox = new VBox(5);
        contentBox.setAlignment(javafx.geometry.Pos.CENTER);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(185);
        imageView.setFitHeight(232);
        imageView.setPreserveRatio(true);
        String posterUrl = media.getPosterUrl() != null ? media.getPosterUrl() : "http://via.placeholder.com/1080x1580";
        Image image = new Image(posterUrl, true);
        imageView.setImage(image);

        Label titleLabel = new Label(media.getTitle());
        titleLabel.setTextFill(Color.web("#fd6108"));
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(185);
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        titleLabel.setAlignment(javafx.geometry.Pos.CENTER);

        Label typeLabel = new Label(media.getType().equals("movie") ? "Movie" : "TV Series");
        typeLabel.setTextFill(Color.WHITE);
        typeLabel.setFont(new Font("System", 12));
        typeLabel.setAlignment(javafx.geometry.Pos.CENTER);

        contentBox.getChildren().addAll(imageView, titleLabel, typeLabel);
        pane.getChildren().add(contentBox);
        contentBox.layoutXProperty().bind(pane.widthProperty().subtract(contentBox.widthProperty()).divide(2));
        contentBox.layoutYProperty().bind(pane.heightProperty().subtract(contentBox.heightProperty()).divide(2));

        pane.setOnMouseEntered(event -> {
            pane.setStyle("-fx-background-color: #2A2035; -fx-border-color: #2A2035; -fx-border-width: 1px; -fx-border-radius: 5px;");
        });
        pane.setOnMouseExited(event -> {
            pane.setStyle("-fx-background-color: #2C2D40;");
        });

        return pane;
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        String query = searchField.getText().trim();
        if (query.isEmpty()) return;
        String searchType = getSelectedSearchType();
        searchHandler.handleSearch(query, searchField, searchType, searchType);
    }
    // Updated method to include MouseEvent parameter
    private void handleMediaSelection(Media media, MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Selected.fxml"));
            Parent root = loader.load();
            SelectedController controller = loader.getController();
            controller.setMediaDetails(media);
            Node sourceNode = (Node) event.getSource();
            sourceNode.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSearchQuery(String query) {
        searchResultsTitleLabel.setText("Search Results for: " + query);
    }

}
