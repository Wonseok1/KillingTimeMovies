package com.example.jws.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textView, textView2;
    EditText editText, editText2, editText3, editText4, editText5;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        editText = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        editText4 = findViewById(R.id.editText4);
        editText5 = findViewById(R.id.editText5);



        Button button = findViewById(R.id.button);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        Button button4 = findViewById(R.id.button4);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String databaseName = editText.getText().toString();
                openDatabase(databaseName);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tableName = editText2.getText().toString();
                createTable(tableName);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editText3.getText().toString().trim();
                String ageStr = editText4.getText().toString().trim();
                String mobile = editText5.getText().toString().trim();

                int age = -1;
                try {
                    age = Integer.parseInt(ageStr);
                    insertData(name, age, mobile);
                } catch (Exception e) {
                }
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tableName = editText2.getText().toString();
                selectData(tableName);
            }
        });
    }



    //데이터 베이스 생성
    public void openDatabase(String databaseName) {
        println("openDatabase 호출됨.");
        database = openOrCreateDatabase(databaseName, MODE_PRIVATE, null);
        if (database != null) {
            println("데이터베이스 오픈됨.");
        }
    }

    public void createTable(String tableName) {
        println("createTable() 호출됨");

        if (database != null) {
            String sql = "create table " + tableName + " (_id integer PRIMARY KEY autoincrement, name text, age integer, mobile text)";
            database.execSQL(sql);
            println("테이블 생성됨");
        } else {
            println("먼저 데이터 베이스를 오픈하세요.");
        }
    }

    public void insertData(String name, int age, String mobile) {
        println("insertDate() 호출됨");

        if (database != null) {
            String sql = "insert into customer(name, age, mobile) values(?, ?, ?)";
            Object[] params = {name, age, mobile};
            println("데이터 베이스 insert 됨");

            database.execSQL(sql, params);
        } else {
            println("먼저 데이터 베이스를 오픈하세요");
        }
    }

    public void selectData(String tableName) {
        println("selectData()호출됨");

        if (database != null) {
            String sql = "select name, age, mobile from "+tableName;
            Cursor cursor = database.rawQuery(sql, null);//리턴값이 있는경우
            println("조회된 데이터의 개수 : "+ cursor.getCount());

            for(int i = 0; i<cursor.getCount(); i++) {
                cursor.moveToNext();
                String name = cursor.getString(0);
                int age = cursor.getInt(1);
                String mobile = cursor.getString(2);

                println("#"+i+1 +" -> "+name+ ", "+age+ ", "+mobile);
            }
        }
    }

    public void println(String data) {
        textView.append(data+"\n");
    }
}

