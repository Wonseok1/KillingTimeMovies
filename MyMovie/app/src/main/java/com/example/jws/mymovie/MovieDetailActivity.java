package com.example.jws.mymovie;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.zip.Inflater;

import javax.microedition.khronos.opengles.GL;

public class MovieDetailActivity extends AppCompatActivity {

    GridView list_movie_similar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();
        int vote_count = intent.getIntExtra("vote_count", -1);
        final int id = intent.getIntExtra("id", -1);
        double vote_average = intent.getDoubleExtra("vote_average", -1);
        final String title = intent.getStringExtra("title");
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
        tv_movie_detail_votecount.setText("총 투표수 : " + String.valueOf(vote_count));
        tv_movie_detail_vote_average.setText("평점 : " + String.valueOf(vote_average));
        tv_movie_detail_release_date.setText("개봉일 : " + release_date);
        tv_movie_detail_original_language.setText("언어 : " + original_language);
        tv_movie_detail__original_title.setText("원제목 : " + original_title);


        if (overview == null) {
            tv_movie_detail_overview.setText("줄거리 없음");
        } else {
            tv_movie_detail_overview.setText(overview);
        }

        Button btn_detail_similar = findViewById(R.id.btn_detail_similar);
        Button btn_detail_internet = findViewById(R.id.btn_detail_internet);

        btn_detail_similar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*String url = "https://api.themoviedb.org/3/movie/{movie_id}/similar?api_key=api_key=e331a939fea1530cdc641ac98d848eee&language=ko-KR&page=1";*/

               /* if (ApiInfo.requestQueue == null) {//requestQueue 생성
                    ApiInfo.requestQueue = Volley.newRequestQueue(getApplicationContext());
                }
                String url = "https://api.themoviedb.org/3/movie/upcoming?api_key=e331a939fea1530cdc641ac98d848eee&language=ko-KR";
                MainActivity mainActivity = new MainActivity();
                MainActivity.MovieListAdapter adapter = new MainActivity.MovieListAdapter(getApplicationContext());
                list_movie_similar.setAdapter(adapter);
                mainActivity.requestMovieList3(url);
*/

            }
        });

        btn_detail_internet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Intent.ACTION_WEB_SEARCH);
                intent1.putExtra(SearchManager.QUERY, "영화 " + title);
                startActivity(intent1);
            }
        });

        list_movie_similar = findViewById(R.id.list_movie_similar);


    }


    /*class MovieListAdapter extends BaseAdapter {

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
            *//*Cursor cursor = database.rawQuery("select id, seen from movie where id ="+item.getId() , null);
            cursor.moveToNext();
            int id = cursor.getInt(0);
            int cursorSeen = cursor.getInt(1);*//*

           *//* list_movie_similar.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //MovieListItem item = items.get(i);
                    //database.execSQL("update movie set seen=1 where id="+item.getId());
                    items.remove(i);
                    adapter.notifyDataSetChanged();

                    return true;
                }
            });*//*

            view.setTitle(item.getTitle());
            view.setImage(item.getBitmap(), getApplicationContext());

            list_movie_similar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
    }*/
}
