package com.example.jws.mymovie;

import com.android.volley.RequestQueue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 http://api.themoviedb.org/3/discover/movie?api_key=e331a939fea1530cdc641ac98d848eee
 https://api.themoviedb.org/3/movie/upcoming?api_key=e331a939fea1530cdc641ac98d848eee&language=ko-KR&page=1
 &language=ko-KR&page=1&primary_release_date.gte=2018-01-01&primary_release_date.lte=2018-01-31&sort_by=primary_release_date.asc";
 //http://api.themoviedb.org/3/discover/movie?api_key=e331a939fea1530cdc641ac98d848eee&language=ko-KR&page=1&primary_release_date.lte=2018-01-31&sort_by=primary_release_date.asc&region=KR
 https://image.tmdb.org/t/p/w500/kqjL17yufvn9OVLyXYpvtyrFfak.jpg
 * Created by jws on 2019-01-06.
 */

public class ApiInfo {
    public static RequestQueue requestQueue;
    public static String host = "http://api.themoviedb.org/3/discover/movie?";
    public static String apikey = "api_key=e331a939fea1530cdc641ac98d848eee";
    public static String language = "&language=ko-KR";
    public static String page = "&page=";
    public static String release_date_gte="&primary_release_date.gte=";
    public static String release_date_lte="&primary_release_date.lte=";
    public static String sortasc = "&sort_by=primary_release_date.asc";
    public static String sortdesc = "&sort_by=primary_release_date.desc";
    public static String region = "&region=KR";


    public static Date date = new Date(System.currentTimeMillis());
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    public static String getTime = sdf.format(date);
    public static String getTime3Month = sdf.format(month(date, -3));

    public static Date month(Date date, int months) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }


}
