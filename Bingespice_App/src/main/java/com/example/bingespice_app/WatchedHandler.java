package com.example.bingespice_app;

import java.util.List;

public class WatchedHandler {

    public boolean markAsWatched(Media media) {
        //System.out.println(media.getId());
        //System.out.println(Session.getUserID());
        return BingespiceDBManager.updateWatchedCategory(Session.getUserID(), media.getId(), media.getType());
    }

    public boolean deleteFromWatched(Media media) {
        return BingespiceDBManager.removeFromWatched(Session.getUserID(), media.getId());
    }

    public boolean checkIfWatched(Media media) {
        return BingespiceDBManager.checkIfWatched(Session.getUserID(), media.getId());
    }

    public boolean areAllEpisodesWatched(Media media) {
        if (!media.getType().equalsIgnoreCase("tv")) {
            return false;
        }

        TMDBManager tmdbManager = new TMDBManager();
        int totalEpisodes = tmdbManager.getTotalEpisodes(media.getId());
        if (totalEpisodes == 0) return false;

        List<int[]> watchedEpisodes = BingespiceDBManager.checkEpisodeIfWatched(
                Session.getUserID(),
                media.getId()
        );

        return watchedEpisodes.size() >= totalEpisodes;
    }
}