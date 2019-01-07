package com.example.student.listex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ExAdapter adapter;
    EditText editText, editText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.listView);
        editText = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);

        adapter = new ExAdapter();

        adapter.addItem(new ExItem("소녀시대1", "010-1000-1000", R.drawable.ic_launcher_background));
        adapter.addItem(new ExItem("소녀시대2", "010-2000-1000", R.drawable.ic_launcher_background));
        adapter.addItem(new ExItem("소녀시대3", "010-3000-1000", R.drawable.ic_launcher_background));
        adapter.addItem(new ExItem("소녀시대4", "010-4000-1000", R.drawable.ic_launcher_background));
        adapter.addItem(new ExItem("소녀시대5", "010-5000-1000", R.drawable.ic_launcher_background));


        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ExItem item = (ExItem) adapter.getItem(position);
                Toast.makeText(getApplicationContext(), "선택: " + item.getName(), Toast.LENGTH_SHORT).show();
            }
        });


        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText.getText().toString();
                String mobile = editText2.getText().toString();

                adapter.addItem(new ExItem(name, mobile, R.drawable.ic_launcher_background));
                adapter.notifyDataSetChanged();//리스트뷰 갱신하라고 알려줌
            }
        });
    }

    class ExAdapter extends BaseAdapter {
        ArrayList<ExItem> items = new ArrayList<ExItem>();

        @Override
        public int getCount() {//아이템 몇개니?
            return items.size();
        }

        @Override
        public Object getItem(int position) {//몇번째 데이터니?
            return items.get(position);
        }

        public void addItem(ExItem item) {
            items.add(item);
        }

        @Override
        public long getItemId(int position) { //id라는 값이 혹시 있으면 내놔
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ExItemView view = null;
            if (convertView == null) {
                view = new ExItemView(getApplicationContext());
            } else {
                view = (ExItemView) convertView;
            }
            ExItem item = items.get(position);
            view.setName(item.getName());
            view.setMobile(item.getMobile());
            view.setImage(item.getResId());

            return view;
        }
    }

}
