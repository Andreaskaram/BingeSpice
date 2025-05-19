package com.example.bingespice_app;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;

public class SearchResultsController {
    @FXML
    private ListView<Movie> moviesListView;

    public void setMovies(List<Movie> movies) {
        moviesListView.getItems().setAll(movies);
        moviesListView.setCellFactory(listView -> new MovieListCell());
    }

    private static class MovieListCell extends ListCell<Movie> {
        private final ImageView imageView = new ImageView();
        private final Text titleText = new Text();
        private final VBox vbox = new VBox(imageView, titleText);

        public MovieListCell() {
            imageView.setFitWidth(100);
            imageView.setFitHeight(150);
            titleText.setWrappingWidth(100);
            setOnMouseClicked(event -> {
                Movie movie = getItem();
                if (movie != null) {
                    System.out.println("Selected movie: " + movie.getTitle());
                    // TODO: Load movie details screen
                }
            });
        }

        @Override
        protected void updateItem(Movie movie, boolean empty) {
            super.updateItem(movie, empty);
            if (empty || movie == null) {
                setGraphic(null);
            } else {
                imageView.setImage(movie.getPosterUrl() != null ? new Image(movie.getPosterUrl()) : new Image("http://via.placeholder.com/1080x1580"));
                titleText.setText(movie.getTitle());
                setGraphic(vbox);
            }
        }
    }
}