package com.example.jws.mymovie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import javax.microedition.khronos.opengles.GL;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();
        int vote_count = intent.getIntExtra("vote_count",-1);
        String id = intent.getStringExtra("id");
        double vote_average = intent.getDoubleExtra("vote_average",-1);
        String title = intent.getStringExtra("title");
        String bitmap = intent.getStringExtra("bitmap");
        String original_language = intent.getStringExtra("original_language");
        String overview = intent.getStringExtra("overview");
        String release_date = intent.getStringExtra("release_date");
        String original_title = intent.getStringExtra("original_title");

        ImageView iv_movie_detail = findViewById(R.id.iv_movie_detail);
        Glide.with(this).load(bitmap).into(iv_movie_detail);


        TextView tv_movie_detail_title = findViewById(R.id.tv_movie_detail_title);
        TextView tv_movie_detail_votecount = findViewById(R.id.tv_movie_detail_votecount);
        TextView tv_movie_detail_vote_average = findViewById(R.id.tv_movie_detail_vote_average);
        TextView tv_movie_detail_original_language = findViewById(R.id.tv_movie_detail_original_language);
        TextView tv_movie_detail_release_date = findViewById(R.id.tv_movie_detail_release_date);
        TextView tv_movie_detail__original_title = findViewById(R.id.tv_movie_detail__original_title);
        TextView tv_movie_detail_overview = findViewById(R.id.tv_movie_detail_overview);


        tv_movie_detail_title.setText(title);
        tv_movie_detail_votecount.setText("총 투표수 : "+ String.valueOf(vote_count));
        tv_movie_detail_vote_average.setText("평점 : "+ String.valueOf(vote_average));
        tv_movie_detail_release_date.setText("개봉일 : "+release_date);
        tv_movie_detail_original_language.setText("언어 : "+original_language);
        tv_movie_detail__original_title.setText("원제목 : "+original_title);



        if (overview == null) {
            tv_movie_detail_overview.setText("줄거리 없음");
        } else {
            tv_movie_detail_overview.setText(overview);
        }


    }
}
