package com.example.bingespice_app;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TMDBManager {
    private static final String API_KEY = "1cf50e6248dc270629e802686245c2c8";
    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String IMG_URL = "https://image.tmdb.org/t/p/w500";
    private static final String SEARCH_MOVIE_URL = BASE_URL + "/search/movie?api_key=" + API_KEY;
    private static final String SEARCH_TV_URL = BASE_URL + "/search/tv?api_key=" + API_KEY;
    private static final String DISCOVER_MOVIE_URL = BASE_URL + "/discover/movie?sort_by=popularity.desc&api_key=" + API_KEY;
    private static final String DISCOVER_TV_URL = BASE_URL + "/discover/tv?sort_by=popularity.desc&api_key=" + API_KEY;

    private final HttpClient httpClient;

    public TMDBManager() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public List<Media> searchMedia(String query) throws Exception {
        List<Media> mediaList = new ArrayList<>();

        // Encode the query to handle spaces and special characters
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString()).replace("+", "%20");

        // Search for movies
        String movieUrl = SEARCH_MOVIE_URL + "&query=" + encodedQuery;
        mediaList.addAll(fetchMedia(movieUrl, "movie"));

        // Search for TV series
        String tvUrl = SEARCH_TV_URL + "&query=" + encodedQuery;
        mediaList.addAll(fetchMedia(tvUrl, "tv"));

        return mediaList;
    }

    public List<Media> getPopularMedia() throws Exception {
        List<Media> mediaList = new ArrayList<>();

        // Get popular movies
        mediaList.addAll(fetchMedia(DISCOVER_MOVIE_URL, "movie"));

        // Get popular TV series
        mediaList.addAll(fetchMedia(DISCOVER_TV_URL, "tv"));

        return mediaList;
    }

    private List<Media> fetchMedia(String url, String type) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject json = new JSONObject(response.body());
        JSONArray results = json.getJSONArray("results");
        List<Media> mediaItems = new ArrayList<>();
        for (int i = 0; i < results.length(); i++) {
            JSONObject mediaJson = results.getJSONObject(i);
            String title = type.equals("movie") ? mediaJson.getString("title") : mediaJson.getString("name");
            String posterPath = mediaJson.isNull("poster_path") ? null : IMG_URL + mediaJson.getString("poster_path");
            Media media = new Media(
                    mediaJson.getInt("id"),
                    title,
                    posterPath,
                    mediaJson.getDouble("vote_average"),
                    mediaJson.getString("overview"),
                    type
            );
            mediaItems.add(media);
        }
        return mediaItems;
    }
}