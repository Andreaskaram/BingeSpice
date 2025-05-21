package com.example.bingespice_app;

/**
 * Simple session holder for storing current user information in-memory.
 */
public class Session {
    private static String username;
    private static int UserID;

    /**
     * Set the currently logged-in username.
     */
    public static void setUsername(String user) {
        username = user;
    }

    public static void setUserID(int id) {
        UserID = id;
    }

    /**
     * Get the currently logged-in username.
     */
    public static String getUsername() {
        return username;
    }

    public static int getUserID() {
        return UserID;
    }
}