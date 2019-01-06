package com.example.jws.mymovie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    String json;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestMovieList();
            }
        });

        if (ApiInfo.requestQueue == null) {//requestQueue 생성
            ApiInfo.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
    }

    public void requestMovieList(){
        String url = ApiInfo.host + ApiInfo.apikey + ApiInfo.language;
        url += "&page=1";

        StringRequest request = new StringRequest(
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

        request.setShouldCache(false);
        ApiInfo.requestQueue.add(request);
        println("영화목록요청보냄");

    }

   /* public void processResponse(String response) {
        Gson gson = new Gson();

        //ResponseInfo info = gson.fromJson(response, ResponseInfo.class);
        MovieList movieList = gson.fromJson(response, MovieList.class);
        println("영화 갯수 : " + movieList.result.size());
    }*/

    public void processResponse(String response) {
        json = response; //받아온 전체 데이터
        try {
            JSONObject root = new JSONObject(json);
            int page = root.getInt("page");
            println("페이지수 : "+page);

            JSONArray movie_list = root.getJSONArray("results"); //데이터중에 []안의 내용
            println("영화수 :"+movie_list.length());
            String movie_list2 = movie_list.toString();
            println(movie_list2);

            JSONArray movie_info = new JSONArray(movie_list2);
            String first = movie_info.getString(0);
            JSONObject movie1 = new JSONObject(first);


            String img = movie1.getString("poster_path");
            println("포스터 경로: "+img);

            //api 출력 형식이 {[{ 이런 식이여서 객체안에 배열안에 객체를 다시 생성하는 식으로로

        }catch (Exception e) {

        }
    }


    public void println(String data) {
        textView.append(data + "\n");
    }
}
