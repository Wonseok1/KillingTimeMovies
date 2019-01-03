package com.example.jws.httpex;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView textView;
    Handler handler = new Handler();
    String urlStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlStr = editText.getText().toString();
                RequestThread thread = new RequestThread();
                thread.start();
            }
        });

    }

    class RequestThread extends Thread {
        @Override
        public void run() {


            try {

                URL url = new URL(urlStr);//url 객체 생성
                HttpURLConnection conn = (HttpURLConnection) url.openConnection(); //url객체 리턴

                if (conn != null) {
                    conn.setConnectTimeout(10000);
                    conn.setRequestMethod("GET"); //get 방식으로 요청
                    conn.setDoInput(true);//서버에서 받기
                    conn.setDoOutput(true);//서버로 보내기

                    int resCode = conn.getResponseCode();// 연결


                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String line = null;

                        while (true) {
                            line = reader.readLine();
                            if (line == null) {
                                break;
                            }

                            println(line);
                        }
                        reader.close();
                        conn.disconnect();
                    }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    public void println(final String data) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                textView.append(data + "\n");
            }
        });

    }
}
