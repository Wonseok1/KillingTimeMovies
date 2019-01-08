package com.example.jws.mymovie;

/**
 * Created by student on 2019-01-08.
 */

public class MovieListItem {
    String title;
    double vote_average;

    public MovieListItem(String title, double vote_average) {
        this.title = title;
        this.vote_average = vote_average;
    }

    public String getTitle() {
        return title;
    }

    public double getVote_average() {
        return vote_average;
    }

}
