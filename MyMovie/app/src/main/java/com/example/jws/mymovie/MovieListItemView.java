package com.example.jws.mymovie;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by student on 2019-01-08.
 */

public class MovieListItemView extends LinearLayout{

    TextView tv_movie_title;
    TextView tv_movie_score;

    public MovieListItemView(Context context) {
        super(context);
        init(context);
    }

    public MovieListItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.movie_main_list, this, true);

        tv_movie_score = findViewById(R.id.tv_movie_score);
        tv_movie_title = findViewById(R.id.tv_movie_title);

    }

    public void setTitle(String title) {
        tv_movie_title.setText(title);
    }

    public void setScore(double score) {
        tv_movie_score.setText(Double.toString(score));

    }

}
