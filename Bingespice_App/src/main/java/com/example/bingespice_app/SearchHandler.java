package com.example.bingespice_app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;

import java.io.IOException;
import java.util.List;

public class SearchHandler {
    private final TMDBManager tmdbManager;

    public SearchHandler() {
        this.tmdbManager = new TMDBManager();
    }

    public void handleSearch(String query, Node sourceNode) {
        if (query == null || query.length() < 2) {
            return;
        }

        try {
            List<Movie> movies = tmdbManager.searchMovies(query);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Search Results_Selected.fxml"));
            Parent root = loader.load();
            SearchResultsController controller = loader.getController();
            controller.setMovies(movies);
            Scene currentScene = sourceNode.getScene();
            currentScene.setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: Show error alert to user
        }
    }
}