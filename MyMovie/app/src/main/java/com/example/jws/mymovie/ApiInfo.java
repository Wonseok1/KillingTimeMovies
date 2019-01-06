package com.example.jws.mymovie;

import com.android.volley.RequestQueue;

/**
 * Created by jws on 2019-01-06.
 */

public class ApiInfo {
    public static RequestQueue requestQueue;
    public static String host = "http://api.themoviedb.org/3/discover/movie?";
    public static String apikey = "api_key=e331a939fea1530cdc641ac98d848eee";
    public static String language = "&language=ko-KR";
}
