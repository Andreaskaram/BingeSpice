package com.example.bingespice_app;

import java.sql.Timestamp;

public class Notification {
    private final String content;
    private final Timestamp date;
    private final String status;

    public Notification(String content, Timestamp date, String status) {
        this.content = content;
        this.date = date;
        this.status = status;
    }

    public String getContent() { return content; }
    public Timestamp getDate() { return date; }
    public String getStatus() { return status; }
}