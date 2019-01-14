package com.example.jws.mymovie;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.zip.Inflater;

import javax.microedition.khronos.opengles.GL;

public class MovieDetailActivity extends AppCompatActivity {

    GridView list_movie_similar;

    MovieListAdapter adapter;

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

        String num = String.valueOf(vote_average/2);
        float num2 = Float.valueOf(num);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setRating(num2);

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
                //String url = "https://api.themoviedb.org/3/movie/{movie_id}/similar?api_key=api_key=e331a939fea1530cdc641ac98d848eee&language=ko-KR&page=1";
                if (ApiInfo.requestQueue == null) {//requestQueue 생성
                    ApiInfo.requestQueue = Volley.newRequestQueue(getApplicationContext());
                }
                //String url = "https://api.themoviedb.org/3/movie/upcoming?api_key=e331a939fea1530cdc641ac98d848eee&language=ko-KR";

                String url = "https://api.themoviedb.org/3/movie/"+id+"/similar?api_key=e331a939fea1530cdc641ac98d848eee&language=ko-KR&page=1";
                //String url = "https://api.themoviedb.org/3/movie/"+id+"/recommendations?api_key=e331a939fea1530cdc641ac98d848eee&language=ko-KR&page=1";
                //https://api.themoviedb.org/3/movie/{movie_id}/similar?api_key=<<api_key>>&language=en-US&page=1


                adapter = new MovieListAdapter();
                list_movie_similar.setAdapter(adapter);

                requestMovieList3(url);
                //setGridViewHeightBasedOnChildren(list_movie_similar, 10);
                //setListViewHeightBasedOnChildren(list_movie_similar);
                adapter.notifyDataSetChanged();
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

    public void setGridViewHeightBasedOnChildren(GridView gridView, int columns) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int items = listAdapter.getCount();
        int rows = 0;

        View listItem = listAdapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();

        float x = 1;
        if( items > columns ){
            x = items/columns;
            rows = (int) (x + 1);
            totalHeight *= rows;
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);

    }

    public void requestMovieList3(String url) {
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
                    }
                }
        );
        request.setShouldCache(false);
        ApiInfo.requestQueue.add(request);
    }

    public void processResponse(String response) {
        String json = response; //받아온 전체 데이터
        try {
            //검색시 전체정보 파싱
            JSONObject root = new JSONObject(json);
            int page = root.getInt("page");
            int total_results = root.getInt("total_results");
            int total_pages = root.getInt("total_pages");

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
                //insertData(id, 0);
                //db저장 하고 받아와서 어댑터에 저장

                adapter.addItem(new MovieListItem(vote_count, id, vote_average, title, img_url, original_language, overview, release_date, original_title));//
                adapter.notifyDataSetChanged();
            }

        } catch (Exception e) {
        }
    }

    public static void setListViewHeightBasedOnChildren(GridView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getHeight() * (listAdapter.getCount() - 1));//listView.getDividerHeight()
        listView.setLayoutParams(params);
        listView.requestLayout();
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

            /*list_movie_similar.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //MovieListItem item = items.get(i);
                    //database.execSQL("update movie set seen=1 where id="+item.getId());
                    items.remove(i);
                    adapter.notifyDataSetChanged();

                    return true;
               }
            });*/

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
    }
}
