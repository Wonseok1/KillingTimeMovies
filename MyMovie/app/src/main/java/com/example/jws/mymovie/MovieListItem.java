package com.example.jws.mymovie;

/**
 * Created by student on 2019-01-08.
 */

 /*   int vote_count = movie_detail.getInt("vote_count");
    int id = movie_detail.getInt("id");
    double vote_average = movie_detail.getDouble("vote_average");
    String title = movie_detail.getString("title");
    int popularity = movie_detail.getInt("popularity");
    String poster_path = movie_detail.getString("poster_path");
    String original_language = movie_detail.getString("original_language");
    String overview = movie_detail.getString("overview");
    String release_date = movie_detail.getString("release_date");*/

public class MovieListItem {
    int vote_count;
    int id;
    double vote_average;
    String title;
    String bitmap;
    String original_language;
    String original_title;
    String overview;
    String release_date;

    public MovieListItem(int vote_count, int id, double vote_average, String title, String bitmap, String original_language, String overview, String release_date, String original_title) {
        this.vote_count = vote_count;
        this.id = id;
        this.vote_average = vote_average;
        this.title = title;
        this.bitmap = bitmap;
        this.original_language = original_language;
        this.overview = overview;
        this.release_date = release_date;
        this.original_title = original_title;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getTitle() {
        return title;
    }

    public double getVote_average() {
        return vote_average;
    }

    public String getBitmap() {
        return bitmap;
    }

    public void setBitmap(String bitmap) {
        this.bitmap = bitmap;
    }
}


