package com.example.jws.mymovie;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

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
    Bitmap bitmap;
    int Movie_tab = 1;
    EditText editText;

    int a = 1;
    int b = 0;
    int a1 = 1;
    int b1 = 0;
    int i1;
    int i;
    int i2;
    int a2 = 1;
    int b2 = 0;


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
        editText = findViewById(R.id.editText);

        MovieTask task = new MovieTask();
        task.execute();

        //textView = findViewById(R.id.textView);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //무비 탭이 0이면 requestMovieList 1을 실행
                //1이면 2를 실행( 인기영화)
                //최신영화로 가면 a랑 b초기화되고 다시
                b = 0;

                if (ApiInfo.requestQueue == null) {//requestQueue 생성
                    ApiInfo.requestQueue = Volley.newRequestQueue(getApplicationContext());
                }

                if (Movie_tab == 0) {
                    //requestMovieList();

                    for (i = a; b < 2; i++) {
                        b++;
                        a++;
                        String url = ApiInfo.host + ApiInfo.apikey + ApiInfo.language;
                        url += "&page=" + i;
                        url += ApiInfo.release_date_lte + ApiInfo.getTime + ApiInfo.sortdesc;
                        requestMovieList3(url);
                    }

                } else if (Movie_tab == 1) {
                    b1 = 0;
                    //requestMovieList2();

                    for (i1 = a1; b1 < 2; i1++) {
                        b1++;
                        a1++;
                        String url = "https://api.themoviedb.org/3/movie/upcoming?api_key=e331a939fea1530cdc641ac98d848eee&language=ko-KR";
                        url += "&page=" + i1;
                        requestMovieList3(url);
                    }
                } else if (Movie_tab == 2) {
                    b2 = 0;
                    for (i2 = a2; b2 < 2; i2++) {
                        b2++;
                        a2++;
                        String search;
                        search = editText.getText().toString();
                        String url = "https://api.themoviedb.org/3/search/movie?api_key=e331a939fea1530cdc641ac98d848eee&query=" + search;
                        url += "&language=ko-KR&page=" + i2;

                        requestMovieList3(url);
                    }
                }

            }
        });

        Button btn_famous = findViewById(R.id.btn_famous);
        btn_famous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Movie_tab = 1;

                //어댑터 목록 삭제제
                adapter = new MovieListAdapter();
                movie_main_list.setAdapter(adapter);
                a1 = 1;
                b1 = 0;

                /*if (ApiInfo.requestQueue == null) {//requestQueue 생성
                    ApiInfo.requestQueue = Volley.newRequestQueue(getApplicationContext());
                }*/

                for (i1 = a1; b1 < 2; i1++) {
                    b1++;
                    a1++;
                    String url = "https://api.themoviedb.org/3/movie/upcoming?api_key=e331a939fea1530cdc641ac98d848eee&language=ko-KR";
                    url += "&page=" + i1;
                    requestMovieList3(url);
                }
                //requestMovieList2();
            }
        });

        Button btn_latest = findViewById(R.id.btn_latest);
        btn_latest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Movie_tab = 0;
                adapter = new MovieListAdapter();
                movie_main_list.setAdapter(adapter);
                a = 1;
                b = 0;

                for (i = a; b < 2; i++) {
                    b++;
                    a++;
                    String url = ApiInfo.host + ApiInfo.apikey + ApiInfo.language;
                    url += "&page=" + i;
                    url += ApiInfo.release_date_lte + ApiInfo.getTime + ApiInfo.sortdesc;
                    requestMovieList3(url);
                }

                //requestMovieList();
            }
        });

        Button btn_search = findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter = new MovieListAdapter();
                movie_main_list.setAdapter(adapter);
                Movie_tab = 2;
                a2 = 1;
                b2 = 0;
                String search;
                search = editText.getText().toString();
                //https://api.themoviedb.org/3/search/movie?api_key=<your key>&query=<영화제목>&language=ko-KR&page=1
                for (i2 = a2; b2 < 2; i2++) {
                    b2++;
                    a2++;
                    String url = "https://api.themoviedb.org/3/search/movie?api_key=e331a939fea1530cdc641ac98d848eee&query=" + search;
                    url += "&language=ko-KR&page=" + i2;

                    requestMovieList3(url);
                }
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
            MovieListItemView view = null;

            if (convertView == null) {
                view = new MovieListItemView(getApplicationContext());
            } else {
                view = (MovieListItemView) convertView;
            }

            final MovieListItem item = items.get(position);
            view.setTitle(item.getTitle());
            // view.setScore(item.getVote_average());
            view.setImage(item.getBitmap(), getApplicationContext());

            movie_main_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(), MovieDetailActivity.class);
                    MovieListItem item2 = items.get(position);

                    intent.putExtra("vote_count", item2.getVote_count());
                    intent.putExtra("id", item2.getId());
                    intent.putExtra("vote_average", item2.getVote_average());
                    intent.putExtra("title", item2.getTitle());
                    intent.putExtra("original_title", item2.getOriginal_title());
                    intent.putExtra("bitmap", item2.getBitmap());
                    intent.putExtra("original_language", item2.getOriginal_language());
                    intent.putExtra("overview", item2.getOverview());
                    intent.putExtra("release_date", item2.getRelease_date());
                    getApplicationContext().startActivity(intent);
                }
            });
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
            //requestMovieList2();
            for (i1 = a1; b1 < 2; i1++) {
                b1++;
                String url = "https://api.themoviedb.org/3/movie/upcoming?api_key=e331a939fea1530cdc641ac98d848eee&language=ko-KR";
                url += "&page=" + i1;
                requestMovieList3(url);
            }
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


    public void requestMovieList3(String url) {
        //https://api.themoviedb.org/3/movie/upcoming?api_key=e331a939fea1530cdc641ac98d848eee&language=ko-KR&page=1
        //String url = "https://api.themoviedb.org/3/movie/upcoming?api_key=e331a939fea1530cdc641ac98d848eee&language=ko-KR&page=1";
        StringRequest request = new StringRequest(
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
                        //println("에러발생 ->" + error.getMessage());
                    }
                }
        );
        request.setShouldCache(false);
        ApiInfo.requestQueue.add(request);
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

            //println("현재페이지 : " + page);
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
                String original_title = movie_detail.getString("original_title");

                final String img_url = "https://image.tmdb.org/t/p/w500/" + poster_path;

                adapter.addItem(new MovieListItem(vote_count, id, vote_average, title, img_url, original_language, overview, release_date, original_title));//
                adapter.notifyDataSetChanged();
            }
            //api 출력 형식이 {[{ 이런 식이여서 객체안에 배열안에 객체를 다시 생성하는 식으로로
        } catch (Exception e) {
        }
    }

    public void println(String data) {
        textView.append(data + "\n");
    }
    //textView 에 출력

    /*public void requestMovieList() {
        String url = ApiInfo.host + ApiInfo.apikey + ApiInfo.language;
        url += "&page=1";
        url += ApiInfo.release_date_lte + ApiInfo.getTime + ApiInfo.sortdesc;//최신 개봉영화

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        for (int i = a; b < 2; i++) {
                            String url = ApiInfo.host + ApiInfo.apikey + ApiInfo.language;
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
                                            //println("에러발생 ->" + error.getMessage());
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
                        //println("에러발생 ->" + error.getMessage());
                    }
                }
        );

        request.setShouldCache(false);
        ApiInfo.requestQueue.add(request);

        //println("영화목록요청보냄");

    }

    public void requestMovieList2() {
        //https://api.themoviedb.org/3/movie/upcoming?api_key=e331a939fea1530cdc641ac98d848eee&language=ko-KR&page=1
        String url = "https://api.themoviedb.org/3/movie/upcoming?api_key=e331a939fea1530cdc641ac98d848eee&language=ko-KR&page=1";

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //println(ApiInfo.getTime);

                        //processResponse(response);

                        for (int i = a1; b1 < 2; i++) {
                            String url = "https://api.themoviedb.org/3/movie/upcoming?api_key=e331a939fea1530cdc641ac98d848eee&language=ko-KR";
                            url += "&page=" + i;
                            //url += ApiInfo.release_date_lte + ApiInfo.getTime + ApiInfo.sortdesc;
                            b1++;
                            a1++;

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
                                            //println("에러발생 ->" + error.getMessage());
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
                        //println("에러발생 ->" + error.getMessage());
                    }
                }
        );

        request.setShouldCache(false);
        ApiInfo.requestQueue.add(request);

        //println("영화목록요청보냄");
    }*/


}





