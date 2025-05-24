package com.example.bingespice_app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class WatchlistController implements Initializable {
    @FXML private FlowPane watchedMoviesFlowPane;
    @FXML private FlowPane watchedSeriesFlowPane;
    @FXML private Button watchedButton;
    @FXML private Text moviesText;
    @FXML private Text seriesText;

    private TMDBManager tmdbManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tmdbManager = new TMDBManager();
        watchedButton.setOnAction(event -> loadWatchedContent());
    }

    private void loadWatchedContent() {
        List<Media> watchedMovies = new ArrayList<>();
        List<Media> watchedSeries = new ArrayList<>();

        String sql = "SELECT ContentID, Type FROM WatchedMoviesSeries WHERE UserID = ?";
        try (Connection conn = BingespiceDBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, Session.getUserID());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int contentId = rs.getInt("ContentID");
                String type = rs.getString("Type");
                String mediaType = type.equals("Series") ? "tv" : "movie";

                try {
                    JSONObject details = tmdbManager.getMediaDetails(contentId, mediaType);

                    String title = mediaType.equals("movie") ?
                            details.getString("title") :
                            details.getString("name");

                    String posterPath = details.optString("poster_path");
                    String posterUrl = posterPath.isEmpty() ?
                            "http://via.placeholder.com/1080x1580" :
                            "https://image.tmdb.org/t/p/w154" + posterPath;

                    Media media = new Media(
                            contentId,
                            title,
                            posterUrl,
                            details.getDouble("vote_average"),
                            details.getString("overview"),
                            mediaType
                    );

                    if (type.equals("Series")) {
                        watchedSeries.add(media);
                    } else {
                        watchedMovies.add(media);
                    }
                } catch (Exception e) {
                    System.err.println("Error fetching details for ID " + contentId + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }

            // Clear existing content
            watchedMoviesFlowPane.getChildren().clear();
            watchedSeriesFlowPane.getChildren().clear();

            // Update headers with colons
            moviesText.setText("Movies Watched:");
            seriesText.setText("Series Watched:");

            // Add movies
            for (Media movie : watchedMovies) {
                watchedMoviesFlowPane.getChildren().add(createMediaPane(movie));
            }

            // Add series
            for (Media series : watchedSeries) {
                watchedSeriesFlowPane.getChildren().add(createMediaPane(series));
            }

        } catch (Exception e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Pane createMediaPane(Media media) {
        Pane pane = new Pane();
        pane.setPrefSize(186, 320);
        pane.setStyle("-fx-background-color: #2C2D40;");
        pane.setOnMouseClicked(event -> handleMediaSelection(media, event));

        VBox contentBox = new VBox(5);
        contentBox.setAlignment(javafx.geometry.Pos.CENTER);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(185);
        imageView.setFitHeight(232);
        imageView.setPreserveRatio(true);

        // Load image asynchronously
        Image image = new Image(media.getPosterUrl(), true);
        imageView.setImage(image);

        Label titleLabel = new Label(media.getTitle());
        titleLabel.setTextFill(javafx.scene.paint.Color.web("#fd6108"));
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(185);
        titleLabel.setFont(javafx.scene.text.Font.font("System", javafx.scene.text.FontWeight.BOLD, 14));
        titleLabel.setAlignment(javafx.geometry.Pos.CENTER);

        Label typeLabel = new Label(media.getType().equals("movie") ? "Movie" : "TV Series");
        typeLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        typeLabel.setFont(new javafx.scene.text.Font("System", 12));
        typeLabel.setAlignment(javafx.geometry.Pos.CENTER);

        contentBox.getChildren().addAll(imageView, titleLabel, typeLabel);
        pane.getChildren().add(contentBox);

        // Center the content
        contentBox.layoutXProperty().bind(pane.widthProperty().subtract(contentBox.widthProperty()).divide(2));
        contentBox.layoutYProperty().bind(pane.heightProperty().subtract(contentBox.heightProperty()).divide(2));

        // Hover effects
        pane.setOnMouseEntered(event -> {
            pane.setStyle("-fx-background-color: #2A2035; -fx-border-color: #2A2035; -fx-border-width: 1px; -fx-border-radius: 5px;");
        });
        pane.setOnMouseExited(event -> {
            pane.setStyle("-fx-background-color: #2C2D40;");
        });

        return pane;
    }

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

    @FXML
    private void handleCreateWatchlistButton(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("WatchlistEditor.fxml"));
        Parent editorRoot = loader.load();

        // Now get the controller and pass arguments
        CreateEditWatchlistScreen controller = loader.getController();
        controller.setParameters("Create"); // This method must be defined in your controller

        // Switch scene
        Scene currentScene = ((Node) event.getSource()).getScene();
        currentScene.setRoot(editorRoot);
    }

    @FXML
    private void handleEditWatchlistButton(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("WatchlistEditor.fxml"));
        Parent editorRoot = loader.load();

        // Now get the controller and pass arguments
        CreateEditWatchlistScreen controller = loader.getController();
        controller.setParameters("Edit"); // This method must be defined in your controller

        // Switch scene
        Scene currentScene = ((Node) event.getSource()).getScene();
        currentScene.setRoot(editorRoot);
    }
}