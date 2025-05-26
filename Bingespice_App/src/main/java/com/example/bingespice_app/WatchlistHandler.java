package com.example.bingespice_app;

import java.util.List;
import java.util.Map;

public class WatchlistHandler {

    public static boolean confirmNewWatchlist(String name) {
        return BingespiceDBManager.createNewWatchlist(name, "Personal", Session.getUserID());
    }

    public static boolean confirmUpdateWatchlist(String newName) {
        return BingespiceDBManager.updateWatchlistName(Session.getSelectedWatchlistID(), newName);
    }

    public static boolean confirmDeleteWatchlist() {
        return BingespiceDBManager.deleteWatchlist(Session.getSelectedWatchlistID());
    }

    public static List<Map.Entry<Integer, String>> searchUserWatchlists(){
        return BingespiceDBManager.getUserWatchlists(Session.getUserID());
    }

    public static boolean isContentInWatchlist(int watchlistId, Media media) {
        String dbType = media.getType().equals("movie") ? "Movie" : "Series";
        return BingespiceDBManager.isContentInWatchlist(watchlistId, media.getId(), dbType);
    }

    public static boolean addToWatchlist(int watchlistId, Media media) {
        String dbType = media.getType().equals("movie") ? "Movie" : "Series";
        return BingespiceDBManager.addToWatchlist(watchlistId, media.getId(), dbType);
    }
}
