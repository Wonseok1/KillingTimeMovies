package com.example.jws.mymovie;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    String json;
    int total_pages;
    int a = 1;
    int b = 0;
    Bitmap bitmap;

    MovieListAdapter adapter;
    GridView movie_main_list;

    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        movie_main_list = findViewById(R.id.movie_main_list);
        adapter = new MovieListAdapter();
        movie_main_list.setAdapter(adapter);

        //DB 오픈 및 테이블 생성

        database = openOrCreateDatabase("movie", MODE_PRIVATE, null);//DB오픈
        if (database != null) {
            database.execSQL("CREATE TABLE IF NOT EXISTS movieinfo (id integer PRIMARY KEY, title text)");
            database.execSQL("CREATE TABLE IF NOT EXISTS imginfo(id integer PRIMARY KEY, img blob)");
        }

        MovieTask task = new MovieTask();
        task.execute();



        textView = findViewById(R.id.textView);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b = 0;
                if (ApiInfo.requestQueue == null) {//requestQueue 생성
                    ApiInfo.requestQueue = Volley.newRequestQueue(getApplicationContext());
                }
                requestMovieList();
            }
        });

    }

    class MovieListAdapter extends BaseAdapter {

        ArrayList<MovieListItem> items = new ArrayList<MovieListItem>();


        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        public void addItem(MovieListItem item) {
            items.add(item);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MovieListItemView view= null;

            if (convertView == null) {
                view = new MovieListItemView(getApplicationContext());
            } else {
                view = (MovieListItemView) convertView;
            }

            MovieListItem item = items.get(position);
            view.setTitle(item.getTitle());
            view.setScore(item.getVote_average());
            view.setImage(item.getBitmap());

            return view;
        }
    }

    //영화 정보 가져오는 AsynkTask
    class MovieTask extends AsyncTask<Void, Void, Void> {//1 doinbackground 2 onprogressupdate 3 postexcute


        @Override
        protected Void doInBackground(Void... voids) {
            //background 에서 동작하는 코드
            //publishProgress(); 실행시 밑의 update 호출됨
            if (ApiInfo.requestQueue == null) {//requestQueue 생성
                ApiInfo.requestQueue = Volley.newRequestQueue(getApplicationContext());
            }
            requestMovieList();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {



            Toast.makeText(getApplicationContext(), "DB로딩완료", Toast.LENGTH_SHORT).show();
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            adapter.notifyDataSetChanged();
        }


    }




    public void requestMovieList() {
        String url = ApiInfo.host + ApiInfo.apikey + ApiInfo.language;
        url += "&page=1";
        url +=ApiInfo.release_date_lte + ApiInfo.getTime + ApiInfo.sortdesc;//최신 개봉영화

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        println(ApiInfo.getTime);

                        //processResponse(response);

                        for (int i = a; b < 2; i++) {
                            String url = ApiInfo.host + ApiInfo.apikey + ApiInfo.language ;
                            url += "&page=" + i;
                            url += ApiInfo.release_date_lte + ApiInfo.getTime + ApiInfo.sortdesc;
                            b++;
                            a++;

                            StringRequest request2 = new StringRequest(
                                    Request.Method.GET,
                                    url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            processResponse(response);
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            println("에러발생 ->" + error.getMessage());
                                        }
                                    }
                            );

                            request2.setShouldCache(false);
                            ApiInfo.requestQueue.add(request2);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        println("에러발생 ->" + error.getMessage());
                    }
                }
        );

        request.setShouldCache(false);
        ApiInfo.requestQueue.add(request);

        println("영화목록요청보냄");

    }

    public byte[] bitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public void processResponse(String response) {
        json = response; //받아온 전체 데이터
        try {
            //검색시 전체정보 파싱
            JSONObject root = new JSONObject(json);
            int page = root.getInt("page");
            int total_results = root.getInt("total_results");
            total_pages = root.getInt("total_pages");

            JSONArray movie_list = root.getJSONArray("results"); //데이터중에 []안의 내용

            //영화 검색결과 파싱
            JSONArray movie_info = new JSONArray(movie_list.toString());

            println("현재페이지 : " + page);
            //println("총 검색 영화수 : " + total_results);
            //println("총페이지 : " + total_pages);
            //println("현재 페이지 영화수 :"+movie_list.length());





           for (int i = 0; i < total_pages; i++) {
                //영화 상세정보 파싱
                JSONObject movie_detail = new JSONObject(movie_info.getString(i));
                int vote_count = movie_detail.getInt("vote_count");
                int id = movie_detail.getInt("id");
                double vote_average = movie_detail.getDouble("vote_average");
                String title = movie_detail.getString("title");
                int popularity = movie_detail.getInt("popularity");
                String poster_path = movie_detail.getString("poster_path");
                String original_language = movie_detail.getString("original_language");
                String overview = movie_detail.getString("overview");
                String release_date = movie_detail.getString("release_date");

                final String img_url = "https://image.tmdb.org/t/p/w500/"+poster_path;




                new Thread(){
                    @Override
                    public void run() {
                        try {
                            URL url = new URL(img_url);
                            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                        } catch (Exception e) {

                        }
                    }
                }.start();


                //DB Insert
               if (database != null) {
                   String sql = "insert into movieinfo(id, title) values(?, ?)";
                   Object[] params = {id, title};
                   database.execSQL(sql, params);

                   String sql2 = "insert into imginfo(id, img) values(?, ?)";
                   Object[] params2 = {id, bitmapToByte(bitmap)};
                   database.execSQL(sql2, params2);
               }



                adapter.addItem(new MovieListItem(title, vote_average, bitmap));//
                adapter.notifyDataSetChanged();
                // println("총 투표횟수 : " + vote_count);
                // println("영화 id : " + id);
                //println("점수평균 :" + vote_average);
                println("제목 :" + title);
                // println("관람횟수 : " + popularity);
                //println("원어 : " + original_language);
                //println("줄거리 : " + overview);
                println("개봉일 : " + release_date);
                println("포스터 경로: "+poster_path);

            }



            //api 출력 형식이 {[{ 이런 식이여서 객체안에 배열안에 객체를 다시 생성하는 식으로로

        } catch (Exception e) {
        }
    }

    public void println(String data) {
        textView.append(data + "\n");
    }
    //textView 에 출력

    //class MovieInfoTask extends  AsyncTask<>
/*
    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String urlStr;
        private ImageView imageView;

        public ImageLoadTask(String urlStr, ImageView imageView) {
            this.urlStr = urlStr;
            this.imageView = imageView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {

            Bitmap bitmap = null;

            try {
                URL url = new URL(urlStr);
                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (Exception e) {
            }
            return bitmap;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
            imageView.invalidate();
        }

    }*/


}



