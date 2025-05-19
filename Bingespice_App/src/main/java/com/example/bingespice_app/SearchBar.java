package com.example.bingespice_app;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class SearchBar extends HBox {
    private final TextField searchField;
    private final Button searchButton;

    public SearchBar() {
        searchField = new TextField();
        searchField.setPromptText("Search movies...");
        searchButton = new Button("Search");
        searchButton.disableProperty().bind(searchField.textProperty().length().lessThan(2));
        this.getChildren().addAll(searchField, searchButton);
    }

    public TextField getSearchField() {
        return searchField;
    }

    public Button getSearchButton() {
        return searchButton;
    }
}