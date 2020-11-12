package com.ycbjie.ycscrollpager;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ycbjie.ycscrollpager.list.TestListActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TestListActivity.class);
                intent.putExtra("type",0);
                startActivity(intent);
            }
        });
        findViewById(R.id.tv_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TestListActivity.class);
                intent.putExtra("type",1);
                startActivity(intent);
            }
        });

        findViewById(R.id.tv_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TestListActivity.class);
                intent.putExtra("type",2);
                startActivity(intent);
            }
        });

        findViewById(R.id.tv_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TestListActivity.class);
                intent.putExtra("type",3);
                startActivity(intent);
            }
        });
        findViewById(R.id.tv_5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TestListActivity.class);
                intent.putExtra("type",4);
                startActivity(intent);
            }
        });
    }


}
