package com.example.bingespice_app;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class TMDBManager {
    private static final String API_KEY = "3f409933ce626e2ff4882fe91883da5e";
    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String IMG_URL = "https://image.tmdb.org/t/p/w500";
    private static final String SEARCH_URL = BASE_URL + "/search/movie?api_key=" + API_KEY;
    private static final String DISCOVER_URL = BASE_URL + "/discover/movie?sort_by=popularity.desc&api_key=" + API_KEY;

    private final HttpClient httpClient;

    public TMDBManager() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public List<Movie> searchMovies(String query) throws Exception {
        String url = SEARCH_URL + "&query=" + query;
        return fetchMovies(url);
    }

    public List<Movie> getPopularMovies() throws Exception {
        return fetchMovies(DISCOVER_URL);
    }

    private List<Movie> fetchMovies(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject json = new JSONObject(response.body());
        JSONArray results = json.getJSONArray("results");
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < results.length(); i++) {
            JSONObject movieJson = results.getJSONObject(i);
            String posterPath = movieJson.isNull("poster_path") ? null : IMG_URL + movieJson.getString("poster_path");
            Movie movie = new Movie(
                    movieJson.getInt("id"),
                    movieJson.getString("title"),
                    posterPath,
                    movieJson.getDouble("vote_average"),
                    movieJson.getString("overview")
            );
            movies.add(movie);
        }
        return movies;
    }
}