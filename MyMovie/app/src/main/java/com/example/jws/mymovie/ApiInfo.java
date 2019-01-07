package com.example.jws.mymovie;

import com.android.volley.RequestQueue;

/**
 http://api.themoviedb.org/3/discover/movie?api_key=e331a939fea1530cdc641ac98d848eee
 &language=ko-KR&page=1&primary_release_date.gte=2018-01-01&primary_release_date.lte=2018-01-31&sort_by=primary_release_date.asc";
 * Created by jws on 2019-01-06.
 */

public class ApiInfo {
    public static RequestQueue requestQueue;
    public static String host = "http://api.themoviedb.org/3/discover/movie?";
    public static String apikey = "api_key=e331a939fea1530cdc641ac98d848eee";
    public static String language = "&language=ko-KR";
    public static String page = "&page=";
    public static String release_date_gte="&primary_release_date.gte=";
    public static String release_date_asc="&primary_release_date.lte=";
    public static String sort = "&sort_by=";
}
