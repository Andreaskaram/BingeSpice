package com.example.bingespice_app;

/**
 * Simple session holder for storing current user information in-memory.
 */
public class Session {
    private static String username;

    /**
     * Set the currently logged-in username.
     */
    public static void setUsername(String user) {
        username = user;
    }

    /**
     * Get the currently logged-in username.
     */
    public static String getUsername() {
        return username;
    }
}