package com.example.jws.mymovie;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by jws on 2019-01-13.
 */

public class MovieSimilarItemView extends LinearLayout{

    TextView tv_SimilarTitle;
    ImageView iv_movie_poster2;
    Context context;


    public MovieSimilarItemView(Context context) {
        super(context);
    }

    public MovieSimilarItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.movie_main_list, this, true);

        tv_SimilarTitle = findViewById(R.id.tv_movie_title);
        iv_movie_poster2 = findViewById(R.id.iv_movie_poster);

    }

    public void setTitle(String title) {
        tv_SimilarTitle.setText(title);
    }

    public void setImage(String bitmap, Context context) {

        Glide.with(context).load(bitmap).into(iv_movie_poster2);
        //iv_movie_poster.setImageBitmap(bitmap);

    }





}
