package com.example.jws.apiex;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyRecyclerViewAdapter adapter;
    ArrayList<Movie> movieList;
    Button bt_movie_add;
    TextView textView;
    int total_pages;
    int b = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        bt_movie_add = findViewById(R.id.bt_movie_add);
        movieList = new ArrayList<Movie>();
        textView = findViewById(R.id.textView);

        //Asynctask - OKHttp
        MyAsyncTask mAsyncTask = new MyAsyncTask();
        mAsyncTask.execute();

        MovieTask movieTask = new MovieTask();
        movieTask.execute();


        //LayoutManager
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
    }

    public class MyAsyncTask extends AsyncTask<String, Void, Movie[]> {
        //로딩중 표시
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("\t로딩중...");
            //show dialog
            progressDialog.show();
        }

        @Override
        protected Movie[] doInBackground(String... strings) {
            //페이지 출력 반복문 필요

            OkHttpClient client = new OkHttpClient();
            String url1 = ApiInfo.host + ApiInfo.apikey + ApiInfo.language;
            url1 += "&page=1" + ApiInfo.release_date_lte + ApiInfo.getTime + ApiInfo.sortdesc;

            Request request = new Request.Builder()
                    .url(url1)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                Gson gson = new GsonBuilder().create();
                JsonParser parser = new JsonParser();


                JsonElement rootObject = parser.parse(response.body().charStream())
                        .getAsJsonObject().get("results");
                Movie[] posts = gson.fromJson(rootObject, Movie[].class);
                return posts;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Movie[] result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            //ArrayList에 차례대로 집어 넣는다.
            if (result.length > 0) {
                for (Movie p : result) {
                    movieList.add(p);
                }
            }

            //어답터 설정
            adapter = new MyRecyclerViewAdapter(MainActivity.this, movieList);
            recyclerView.setAdapter(adapter);
        }


    }
    //AsynkTask를 하나 더 만들어서 do in background 의 결과값을 스트링으로 받아서 받은걸 파싱한다.

    public class MovieTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();
            String url1 = ApiInfo.host + ApiInfo.apikey + ApiInfo.language + "&page=1" + ApiInfo.release_date_lte + ApiInfo.getTime + ApiInfo.sortdesc;
            Request request = new Request.Builder()
                    .url(url1)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
                //api 요청문 전체

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("api :", s.toString());
            textView.setText(s.toString());
            try {
                JSONObject root = new JSONObject(s.toString());
                int page = root.getInt("page");
                int total_results = root.getInt("total_results");
                total_pages = root.getInt("total_pages");
            } catch (Exception e) {

            }

        }

    }

}