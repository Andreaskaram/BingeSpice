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
    public List<Media> searchByGenre(String genreQuery) throws Exception {
        List<Media> results = new ArrayList<>();

        // Search for movies
        String movieGenreId = getGenreId(genreQuery, "movie");
        if (!movieGenreId.isEmpty()) {
            String movieUrl = BASE_URL + "/discover/movie?api_key=" + API_KEY
                    + "&with_genres=" + movieGenreId
                    + "&sort_by=vote_average.desc"; // Sort by rating
            results.addAll(fetchMedia(movieUrl, "movie"));
        }

        // Search for TV shows
        String tvGenreId = getGenreId(genreQuery, "tv");
        if (!tvGenreId.isEmpty()) {
            String tvUrl = BASE_URL + "/discover/tv?api_key=" + API_KEY
                    + "&with_genres=" + tvGenreId
                    + "&sort_by=vote_average.desc"; // Sort by rating
            results.addAll(fetchMedia(tvUrl, "tv"));
        }

        results.sort(Comparator.comparingDouble(Media::getVoteAverage).reversed());
        return results;
    }


    public List<Media> searchByActor(String actorName) throws Exception {
        int actorId = searchPersonId(actorName);
        String url = BASE_URL + "/discover/movie?api_key=" + API_KEY + "&with_cast=" + actorId;
        return fetchMedia(url, "movie");
    }

    public List<Media> searchByDirector(String directorName) throws Exception {
        int directorId = searchPersonId(directorName);
        String url = BASE_URL + "/discover/movie?api_key=" + API_KEY + "&with_crew=" + directorId;
        return fetchMedia(url, "movie");
    }

    // Helper methods
    private int searchPersonId(String name) throws Exception {
        String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8);
        String url = BASE_URL + "/search/person?api_key=" + API_KEY + "&query=" + encodedName;
        HttpResponse<String> response = httpClient.send(
                HttpRequest.newBuilder().uri(URI.create(url)).build(),
                HttpResponse.BodyHandlers.ofString()
        );
        JSONObject json = new JSONObject(response.body());
        return json.getJSONArray("results").getJSONObject(0).getInt("id");
    }



    private String getGenreId(String genreName, String mediaType) throws Exception {
        String url = BASE_URL + "/genre/" + mediaType + "/list?api_key=" + API_KEY;
        try {
            HttpResponse<String> response = httpClient.send(
                    HttpRequest.newBuilder().uri(URI.create(url)).build(),
                    HttpResponse.BodyHandlers.ofString()
            );
            JSONArray genres = new JSONObject(response.body()).getJSONArray("genres");
            for (int i = 0; i < genres.length(); i++) {
                JSONObject genre = genres.getJSONObject(i);
                if (genre.getString("name").equalsIgnoreCase(genreName)) {
                    return genre.getString("id");
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching genre ID for " + genreName + ": " + e.getMessage());
        }
        return "";
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
        String url = BASE_URL + "/" + type + "/" + id + "?api_key=" + API_KEY + "&append_to_response=credits";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject json = new JSONObject(response.body());

        // Extract actors (top 3) and directors
        JSONArray credits = json.getJSONObject("credits").getJSONArray("cast");
        JSONArray crew = json.getJSONObject("credits").getJSONArray("crew");
        StringBuilder actors = new StringBuilder();
        StringBuilder directors = new StringBuilder();

        for (int i = 0; i < Math.min(3, credits.length()); i++) {
            actors.append(credits.getJSONObject(i).getString("name"));
            if (i < Math.min(3, credits.length()) - 1) actors.append(", ");
        }

        for (int i = 0; i < crew.length(); i++) {
            JSONObject member = crew.getJSONObject(i);
            if (member.getString("job").equalsIgnoreCase("Director")) {
                directors.append(member.getString("name")).append(", ");
            }
        }
        if (directors.length() > 0) directors.setLength(directors.length() - 2); // Remove trailing comma

        // Add extracted data to the JSON
        json.put("actors", actors.toString());
        json.put("directors", directors.toString());

        // Add runtime/duration (movies) or seasons/episodes (TV)
        if (type.equals("movie")) {
            json.put("runtime", json.optInt("runtime", 0));
        } else {
            json.put("seasons", json.optInt("number_of_seasons", 0));
            json.put("episodes", json.optInt("number_of_episodes", 0));
        }

        return json;
    }
}
