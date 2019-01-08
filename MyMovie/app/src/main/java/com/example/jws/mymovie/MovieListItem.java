package com.example.jws.mymovie;

import android.graphics.Bitmap;

/**
 * Created by student on 2019-01-08.
 */

public class MovieListItem {
    String title;
    double vote_average;
    Bitmap bitmap;

    public MovieListItem(String title, double vote_average, Bitmap bitmap) {//
        this.title = title;
        this.vote_average = vote_average;
        this.bitmap = bitmap;
    }

    public String getTitle() {
        return title;
    }

    public double getVote_average() {
        return vote_average;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
