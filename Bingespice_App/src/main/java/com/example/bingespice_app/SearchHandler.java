package com.example.bingespice_app;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;
import java.io.IOException;
import java.util.List;

public class SearchHandler {
    private final TMDBManager tmdbManager;

    public SearchHandler() {
        this.tmdbManager = new TMDBManager();
    }

    public void handleSearch(String query, Node sourceNode, String searchType, String persistSearchType) {
        Task<Void> searchTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try {
                    List<Media> mediaItems = switch (searchType) {
                        case "title" -> tmdbManager.searchMedia(query);
                        case "genre" -> tmdbManager.searchByGenre(query);
                        case "actor" -> tmdbManager.searchByActor(query);
                        case "director" -> tmdbManager.searchByDirector(query);
                        default -> tmdbManager.searchMedia(query);
                    };
                    System.out.println("Fetched " + mediaItems.size() + " items"); // Debug log

                    Platform.runLater(() -> {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("Search Results_Selected.fxml"));
                            Parent root = loader.load();
                            SearchResultsController controller = loader.getController();
                            controller.setSearchType(persistSearchType);
                            controller.setMedia(mediaItems);
                            controller.setSearchQuery(query);
                            sourceNode.getScene().setRoot(root);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                } catch (Exception e) {
                    System.err.println("Search failed: " + e.getMessage()); // Catch-all error
                    e.printStackTrace();
                }
                return null;
            }
        };
        new Thread(searchTask).start();
    }
}