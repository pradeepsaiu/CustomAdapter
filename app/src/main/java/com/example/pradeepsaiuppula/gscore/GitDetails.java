package com.example.pradeepsaiuppula.gscore;

/**
 * Created by pradeepsaiuppula on 3/13/17.
 */

public class GitDetails {

    String username;
    int ranking;
    int total_stars;

    public GitDetails(String username, int ranking, int total_stars) {
        this.username = username;
        this.ranking = ranking;
        this.total_stars = total_stars;
    }

    public String getUsername() {
        return username;
    }

    public int getRanking() {
        return ranking;
    }

    public int getTotal_stars() {
        return total_stars;
    }
}
