package com.example.bingespice_app;

public class Media {
    private final int id;
    private final String title;
    private final String posterUrl;
    private final double voteAverage;
    private final String overview;
    private final String type;

    public Media(int id, String title, String posterUrl, double voteAverage, String overview, String type) {
        this.id = id;
        this.title = title;
        this.posterUrl = posterUrl;
        this.voteAverage = voteAverage;
        this.overview = overview;
        this.type = type;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getPosterUrl() { return posterUrl; }
    public double getVoteAverage() { return voteAverage; }
    public String getOverview() { return overview; }
    public String getType() { return type; }
}
