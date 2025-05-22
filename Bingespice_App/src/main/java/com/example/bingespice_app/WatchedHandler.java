package com.example.bingespice_app;

public class WatchedHandler {

    public boolean markAsWatched(Media media) {
        System.out.println(media.getId());
        System.out.println(Session.getUserID());
        return BingespiceDBManager.updateWatchedCategory(Session.getUserID(), media.getId(), media.getType());
    }

    public boolean deleteFromWatched(Media media) {
        return BingespiceDBManager.removeFromWatched(Session.getUserID(), media.getId());
    }

    public boolean checkIfWatched(Media media) {
        return BingespiceDBManager.checkIfWatched(Session.getUserID(), media.getId());
    }

}
