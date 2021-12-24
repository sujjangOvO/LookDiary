package com.example.lookdiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity {

    Button btnBack, btnMapping, btnLookBook;
    CalendarView calendarView;
    EditText edt;

    String fileName, path;
    int cYear, cMonth, cDay;

    @Override
    protected void onResume() {
        super.onResume();
        checkFile(fileName); // 옷 매칭 후 다시 그려질 때 함수 호출
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnMapping = (Button) findViewById(R.id.btnMapping);
        btnLookBook = (Button) findViewById(R.id.btnLookBook);
        calendarView = (CalendarView) findViewById(R.id.calendarView);
        edt = (EditText) findViewById(R.id.edt);

        Calendar calendar = Calendar.getInstance();
        cYear = calendar.get(Calendar.YEAR);
        cMonth = calendar.get(Calendar.MONTH);
        cDay = calendar.get(Calendar.DAY_OF_MONTH);
        fileName = cYear +"_"+ (cMonth+1) +"_" +cDay;
        checkFile(fileName);

        // 뒤로가기 버튼
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 옷 매칭하기 버튼
        btnMapping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MappingActivity.class);
                intent.putExtra("fileName",fileName); // 옷매칭 정보를 등록하기 위해 해당 날짜 정보 넘겨줌
                startActivity(intent);
            }
        });


        // 캘린더뷰 리스너
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                cYear = year;
                cMonth = month;
                cDay = dayOfMonth;
                fileName = cYear +"_"+ (cMonth+1) +"_" +cDay;
                checkFile(fileName);
            }
        });

        // 룩북보기 버튼
        btnLookBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CheckLookBook.class);
                intent.putExtra("fileName",fileName);
                startActivity(intent);
            }
        });




    } // onCreate

    void checkFile(String fileName){
        String path = getFilesDir().getPath()+"/"+fileName+".txt"; // 내부저장소 파일 경로
        //Log.i("Calendar path",path);
        if(new File(path).exists()){ // 해당 날짜의 파일이 존재 하는 경우
            //Log.i("Calendar","Exists");
            edt.setText("옷 매칭 정보 존재");
            btnLookBook.setEnabled(true);
        }
        else{ // 해당 날짜의 파일이 존재 하지 않는 경우
            //Log.i("Calendar","nonExists");
            edt.setText("옷 매칭 정보 없음");
            btnLookBook.setEnabled(false);
        }
    }

}