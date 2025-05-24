package com.example.bingespice_app;

public class WatchlistHandler {
    public static boolean confirmNewWatchlist(String name, String type, int UserId) {
        return BingespiceDBManager.createNewWatchlist(name, type, UserId);
    }
}
