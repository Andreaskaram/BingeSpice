package com.example.bingespice_app;

/**
 * Simple session holder for storing current user information in-memory.
 */
public class Session {
    private static String username;
    private static int UserID;
    private static int selectedWatchlistID;
    private static String selectedWatchlistName;

    /**
     * Set the currently logged-in username.
     */
    public static void setUsername(String user) {
        username = user;
    }

    public static void setUserID(int id) {
        UserID = id;
    }

    public static void setSelectedWatchlist(int id, String name) {
        selectedWatchlistID = id;
        selectedWatchlistName = name;

    }

    public static void clearSelectedWatchlist() {
        selectedWatchlistID = 0;
        selectedWatchlistName = null;
    }

    /**
     * Get the currently logged-in username.
     */
    public static String getUsername() {
        return username;
    }

    public static int getUserID() { return UserID; }

    public static int getSelectedWatchlistID() { return selectedWatchlistID; }

    public static String getSelectedWatchlistName() { return selectedWatchlistName; }
}