package com.example.jws.volleyex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest();
            }
        });
        if (AppHelper.requestQueue ==null) {
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

    }

    public void sendRequest() {
        //String url = "http://www.google.co.kr";
        String url = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json?key=430156241533f1d058c603178cc3ca0e&targetDt=20120101";

        StringRequest request = new StringRequest(
                Request.Method.GET,// get / post 중 어떤방식?
                url, //받아올 url
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        println("응답 -> "+response);

                        //processResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        println("에러"+ error.getMessage());
                    }
                }
        ){
            //post방식으로 넣고싶을때는 이렇게 중괄호 하나 더 생성하면 Request  라는 메소드 안에 메서드를 재정의 가능

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                return  params;
            }
        };

        request.setShouldCache(false);//이전결과가 있더라도 새로 요청을 해서 응답을 보여주게 함
        AppHelper.requestQueue.add(request);
        println("요청 보냄");
    }

    public void processResponse(String response) {//gson사용
        Gson gson = new Gson();
        MovieList movieList = gson.fromJson(response, MovieList.class);

        if (movieList != null) {
            int countMovie = movieList.boxofficeResult.dailyBoxOfficeList.size();
            println("박스 오피스 타입"+movieList.boxofficeResult.boxofficeType);
            println("응답받은 영화 갯수 :" +countMovie);
        }
    }

    public void println(String data) {
        textView.append(data + "\n");

    }
}
