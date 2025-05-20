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
import java.util.Comparator;
import java.util.List;

public class TMDBManager {
    private static final String API_KEY = "1cf50e6248dc270629e802686245c2c8";
    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String IMG_URL = "https://image.tmdb.org/t/p/w154"; // smaller size here
    private static final String SEARCH_MOVIE_URL = BASE_URL + "/search/movie?api_key=" + API_KEY;
    private static final String SEARCH_TV_URL = BASE_URL + "/search/tv?api_key=" + API_KEY;
    private static final String DISCOVER_MOVIE_URL = BASE_URL + "/discover/movie?sort_by=popularity.desc&api_key=" + API_KEY;
    private static final String DISCOVER_TV_URL = BASE_URL + "/discover/tv?sort_by=popularity.desc&api_key=" + API_KEY;
    private static final String NOW_PLAYING_MOVIE_URL = BASE_URL + "/movie/now_playing?api_key=" + API_KEY;
    private static final String AIRING_TODAY_TV_URL = BASE_URL + "/tv/airing_today?api_key=" + API_KEY;

    private final HttpClient httpClient;

    public TMDBManager() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public List<Media> searchMedia(String query) throws Exception {
        List<Media> mediaList = new ArrayList<>();
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString()).replace("+", "%20");

        String movieUrl = SEARCH_MOVIE_URL + "&query=" + encodedQuery;
        mediaList.addAll(fetchMedia(movieUrl, "movie"));

        String tvUrl = SEARCH_TV_URL + "&query=" + encodedQuery;
        mediaList.addAll(fetchMedia(tvUrl, "tv"));

        mediaList.sort(Comparator.comparingDouble(Media::getVoteAverage).reversed());
        return mediaList;
    }

    public List<Media> getNewReleases() throws Exception {
        List<Media> mediaList = new ArrayList<>();
        mediaList.addAll(fetchMedia(NOW_PLAYING_MOVIE_URL, "movie"));
        mediaList.addAll(fetchMedia(AIRING_TODAY_TV_URL, "tv"));
        return mediaList;
    }

    public List<Media> getPopularMedia() throws Exception {
        List<Media> mediaList = new ArrayList<>();
        mediaList.addAll(fetchMedia(DISCOVER_MOVIE_URL, "movie"));
        mediaList.addAll(fetchMedia(DISCOVER_TV_URL, "tv"));
        mediaList.sort(Comparator.comparingDouble(Media::getVoteAverage).reversed());
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

    public JSONObject getMediaDetails(int id, String type) throws Exception {
        String url = BASE_URL + "/" + type + "/" + id + "?api_key=" + API_KEY;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return new JSONObject(response.body());
    }
}