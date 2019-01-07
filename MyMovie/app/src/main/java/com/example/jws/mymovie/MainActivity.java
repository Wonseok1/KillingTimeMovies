package com.example.jws.mymovie;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    String json;
    int total_pages;
    int a = 1;
    int b = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

                        for (int i = a; b < 3; i++) {
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

       /* for(int i =1; i<total_pages; i++) {
            url = ApiInfo.host + ApiInfo.apikey + ApiInfo.language;
            url += "&page="+i;

            StringRequest request2 = new StringRequest(
                    Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            println("응답받음 -> "+response);

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
        }*/


        println("영화목록요청보냄");

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

                // println("총 투표횟수 : " + vote_count);
                // println("영화 id : " + id);
                //println("점수평균 :" + vote_average);
                println("제목 :" + title);
                // println("관람횟수 : " + popularity);
                //println("원어 : " + original_language);
                //println("줄거리 : " + overview);
                println("개봉일 : " + release_date);
                // println("포스터 경로: "+poster_path);
            }

            //api 출력 형식이 {[{ 이런 식이여서 객체안에 배열안에 객체를 다시 생성하는 식으로로

        } catch (Exception e) {
        }
    }

    public void println(String data) {
        textView.append(data + "\n");
    }

    //영화 정보 가져오는 AsynkTask
    class MovieTask extends AsyncTask<String, Integer, Integer> {//1 doinbackground 2 onprogressupdate 3 postexcute

        @Override
        protected Integer doInBackground(String... strings) {
            //background 에서 동작하는 코드
            //publishProgress(); 실행시 밑의 update 호출됨
            if (ApiInfo.requestQueue == null) {//requestQueue 생성
                ApiInfo.requestQueue = Volley.newRequestQueue(getApplicationContext());
            }
            requestMovieList();
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //중간중간 ui업데이트
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            Toast.makeText(getApplicationContext(), "DB로딩완료", Toast.LENGTH_SHORT).show();
            super.onPostExecute(integer);
        }
    }
}



