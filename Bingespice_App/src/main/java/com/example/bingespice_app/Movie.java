package com.example.bingespice_app;

public class Movie {
    private final int id;
    private final String title;
    private final String posterUrl;
    private final double voteAverage;
    private final String overview;

    public Movie(int id, String title, String posterUrl, double voteAverage, String overview) {
        this.id = id;
        this.title = title;
        this.posterUrl = posterUrl;
        this.voteAverage = voteAverage;
        this.overview = overview;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getPosterUrl() { return posterUrl; }
    public double getVoteAverage() { return voteAverage; }
    public String getOverview() { return overview; }
}