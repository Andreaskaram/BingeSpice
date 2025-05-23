package com.example.bingespice_app;

import java.util.List;

public class NotificationsHandler {
    public static List<Notification> getNotifications(int userId) {
        return BingespiceDBManager.getNotifications(userId);
    }
    public static boolean deleteNotifications(int userId) {
        return BingespiceDBManager.deleteNotifications(userId);
    }

}