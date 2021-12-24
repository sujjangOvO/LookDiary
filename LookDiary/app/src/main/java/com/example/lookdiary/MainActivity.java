package com.example.lookdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnReg,btnCalendar,btnDress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCalendar = (Button) findViewById(R.id.btnCalendar);
        btnReg = (Button) findViewById(R.id.btnReg);
        btnDress = (Button) findViewById(R.id.btnDress);

        // 캘린더뷰 버튼
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CalendarActivity.class);
                startActivity(intent);
            }
        });

        // 옷 등록하기 버튼
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),DressRegisterActivity.class);
                startActivity(intent);
            }
        });

        // 옷 정보 확인 버튼
        btnDress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),DressListActivity.class);
                startActivity(intent);
            }
        });

    }
}