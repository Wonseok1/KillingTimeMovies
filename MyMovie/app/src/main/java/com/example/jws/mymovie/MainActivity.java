package com.example.jws.mymovie;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.content.ContextCompat;
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
    int a1 = 1;
    int a2 = 1;
    int a3 = 1;
    int a4 = 1;

    int b = 0;
    int b1 = 0;
    int b2 = 0;
    int b3 = 0;
    int b4 = 0;

    int i;
    int i1;
    int i2;
    int i3;
    int i4;

    Button btn_famous;
    Button btn_latest;
    Button btn_search;
    Button btn_killing;
    Button btn_popular;
    Button btn_top_rating;

    MovieListAdapter adapter;
    GridView movie_main_list;

    SQLiteDatabase database;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, LoadingActivity.class);
        startActivity(intent);
        MainActivity.context = getApplicationContext();

        movie_main_list = findViewById(R.id.movie_main_list);
        adapter = new MovieListAdapter();
        movie_main_list.setAdapter(adapter);
        editText = findViewById(R.id.editText);


        String tableName = "movie";
        database = openOrCreateDatabase(tableName, MODE_PRIVATE, null);
        database.execSQL("drop table movie"); //나중에 바꿔야돼
        database.execSQL("create table if not exists " + tableName + " (id integer, seen integer)");


        MovieTask task = new MovieTask();
        task.execute();


        final Handler handler = new Handler();

        //textView = findViewById(R.id.textView);
        final Button button = findViewById(R.id.button);
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
                } else if (Movie_tab == 3) {
                    b3 = 0;
                    for (i3 = a3; b3 < 2; i3++) {
                        b3++;
                        a3++;
                        String url = ApiInfo.host + ApiInfo.apikey + ApiInfo.language;
                        url += "&page=" + i3;
                        url += ApiInfo.release_date_lte + ApiInfo.getTime3Month + ApiInfo.sortdesc;
                        requestMovieList3(url);
                    }
                } else if (Movie_tab == 4) {
                    Toast.makeText(getApplicationContext(), "화제의 영화는 현재 언론의 화제가 되는 20개의 영화만 보여줍니다.", Toast.LENGTH_LONG).show();
                } else if (Movie_tab==5) {
                    b4=0;
                    for (i4 = a4; b4 < 2; i4++) {
                        b4++;
                        a4++;
                        String url = "https://api.themoviedb.org/3/movie/top_rated?"+ ApiInfo.apikey + ApiInfo.language + "&page="+i4;
                        requestMovieList3(url);
                    }
                }


            }
        });

        btn_famous = findViewById(R.id.btn_famous);
        btn_famous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Movie_tab = 1;

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                btn_famous.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_selected2));
                                btn_latest.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_not_clicked));
                                btn_search.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_not_clicked));
                                btn_killing.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_not_clicked));
                                btn_top_rating.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_not_clicked));
                            }
                        });
                    }
                }).start();

                //어댑터 목록 삭제제
                adapter = new MovieListAdapter();
                movie_main_list.setAdapter(adapter);
                a1 = 1;
                b1 = 0;

                for (i1 = a1; b1 < 2; i1++) {
                    b1++;
                    a1++;
                    String url = "https://api.themoviedb.org/3/movie/upcoming?api_key=e331a939fea1530cdc641ac98d848eee&language=ko-KR";
                    url += "&page=" + i1;
                    requestMovieList3(url);
                }
            }
        });

        btn_latest = findViewById(R.id.btn_latest);
        btn_latest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                btn_famous.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_not_clicked));
                                btn_latest.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_selected2));
                                btn_search.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_not_clicked));
                                btn_killing.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_not_clicked));
                                btn_top_rating.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_not_clicked));
                            }
                        });
                    }
                }).start();

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
            }
        });

        btn_search = findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                btn_famous.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_not_clicked));
                                btn_latest.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_not_clicked));
                                btn_search.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_selected2));
                                btn_killing.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_not_clicked));
                                btn_top_rating.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_not_clicked));
                            }
                        });
                    }
                }).start();

                adapter = new MovieListAdapter();
                movie_main_list.setAdapter(adapter);
                Movie_tab = 2;
                a2 = 1;
                b2 = 0;
                String search;
                search = editText.getText().toString();
                //https://api.themoviedb.org/3/search/movie?api_key=<your key>&query=<search>&language=ko-KR&page=1
                for (i2 = a2; b2 < 2; i2++) {
                    b2++;
                    a2++;
                    String url = "https://api.themoviedb.org/3/search/movie?api_key=e331a939fea1530cdc641ac98d848eee&query=" + search;
                    url += "&language=ko-KR&page=" + i2;
                    requestMovieList3(url);
                }
            }
        });

        btn_killing = findViewById(R.id.btn_killing);
        btn_killing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Movie_tab = 3;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                btn_famous.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_not_clicked));
                                btn_latest.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_not_clicked));
                                btn_search.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_not_clicked));
                                btn_killing.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_selected2));
                                btn_popular.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_not_clicked));
                                btn_top_rating.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_not_clicked));
                            }
                        });
                    }
                }).start();

                adapter = new MovieListAdapter();
                movie_main_list.setAdapter(adapter);
                a3 = 1;
                b3 = 0;

                for (i3 = a3; b3 < 2; i3++) {
                    b3++;
                    a3++;
                    String url = ApiInfo.host + ApiInfo.apikey + ApiInfo.language;
                    url += "&page=" + i3;
                    url += ApiInfo.release_date_lte + ApiInfo.getTime3Month + ApiInfo.sortdesc;
                    requestMovieList3(url);
                }
            }
        });

        btn_popular = findViewById(R.id.btn_popular);
        btn_popular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Movie_tab = 4;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                btn_famous.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_not_clicked));
                                btn_latest.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_not_clicked));
                                btn_search.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_not_clicked));
                                btn_killing.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_not_clicked));
                                btn_popular.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_selected2));
                                btn_top_rating.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_not_clicked));
                            }
                        });
                    }
                }).start();

                adapter = new MovieListAdapter();
                movie_main_list.setAdapter(adapter);

                String url = ApiInfo.host + ApiInfo.apikey + ApiInfo.language + "&sort_by=popularity.desc";
                requestMovieList3(url);

            }
        });

        btn_top_rating = findViewById(R.id.btn_top_rating);
        btn_top_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Movie_tab = 5;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                btn_famous.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_not_clicked));
                                btn_latest.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_not_clicked));
                                btn_search.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_not_clicked));
                                btn_killing.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_not_clicked));
                                btn_popular.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_not_clicked));
                                btn_top_rating.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_selected2));
                            }
                        });
                    }
                }).start();


                adapter = new MovieListAdapter();
                movie_main_list.setAdapter(adapter);

                a4 = 1;
                b4 = 0;

                for (i4 = a4; b4 < 2; i4++) {
                    b4++;
                    a4++;
                    String url = "https://api.themoviedb.org/3/movie/top_rated?"+ ApiInfo.apikey + ApiInfo.language + "&page="+i4;

                    requestMovieList3(url);
                }
                //https://api.themoviedb.org/3/movie/top_rated?api_key=<<api_key>>&language=en-US&page=1
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
            //여기에 db로 if문 생성
            /*Cursor cursor = database.rawQuery("select id, seen from movie where id ="+item.getId() , null);
            cursor.moveToNext();
            int id = cursor.getInt(0);
            int cursorSeen = cursor.getInt(1);*/

            movie_main_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    MovieListItem item = items.get(i);
                    database.execSQL("update movie set seen=1 where id="+item.getId());
                    items.remove(i);
                    adapter.notifyDataSetChanged();

                    return true;
                }
            });

                view.setTitle(item.getTitle());
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


            //seen 이 1이되면 안보이게 하는 쿼리를 짜야한다.
        }
    }

    //영화 정보 가져오는 AsynkTask
    public class MovieTask extends AsyncTask<Void, Void, Void> {//1 doinbackground 2 onprogressupdate 3 postexcute

        ProgressDialog loadingDialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loadingDialog.setMessage("영화정보 로딩중입니다..");
            loadingDialog.show();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            //background 에서 동작하는 코드
            //publishProgress(); 실행시 밑의 update 호출됨
            if (ApiInfo.requestQueue == null) {//requestQueue 생성
                ApiInfo.requestQueue = Volley.newRequestQueue(getApplicationContext());
            }
            //requestMovieList2();
            for (i1 = a1; b1 < 4; i1++) {
                b1++;
                a1++;
                String url = "https://api.themoviedb.org/3/movie/upcoming?api_key=e331a939fea1530cdc641ac98d848eee&language=ko-KR";
                url += "&page=" + i1;
                requestMovieList3(url);

            }
            loadingDialog.show();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(getApplicationContext(), "DB로딩완료", Toast.LENGTH_SHORT).show();
            super.onPostExecute(aVoid);
            loadingDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            adapter.notifyDataSetChanged();
        }


    }

   /* public void similarMovie() {
        String url = "http://api.themoviedb.org/3/discover/movie/" + id + "/similar" + ApiInfo.apikey + ApiInfo.language + ApiInfo.page;
    }
*/

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


            //api 출력 형식이 {[{ 이런 식이여서 객체안에 배열안에 객체를 다시 생성하는 식으로로
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
                insertData(id, 0);
                //db저장 하고 받아와서 어댑터에 저장

                Cursor cursor = database.rawQuery("select id, seen from movie where id ="+id , null);
                cursor.moveToNext();
                int cursorSeen = cursor.getInt(1);

                if (cursorSeen == 0) {
                    adapter.addItem(new MovieListItem(vote_count, id, vote_average, title, img_url, original_language, overview, release_date, original_title));//
                    adapter.notifyDataSetChanged();
                }

                /*adapter.addItem(new MovieListItem(vote_count, id, vote_average, title, img_url, original_language, overview, release_date, original_title));//
                adapter.notifyDataSetChanged();*/



            }

        } catch (Exception e) {
        }
    }

    public void println(String data) {
        textView.append(data + "\n");
    }

    public void insertData(int id, int seen) {
        if (database != null) {
            String sql = "insert into movie(id, seen) values(?,?)";
            Object[] params = {id, seen};
            database.execSQL(sql, params);
        }
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





