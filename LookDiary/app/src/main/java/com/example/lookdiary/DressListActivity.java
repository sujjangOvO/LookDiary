package com.example.lookdiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DressListActivity extends AppCompatActivity {

    RecyclerView recycler;
    ArrayList<String> filePaths;
    ArrayList<Dress> dressList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dress_list);
        recycler = (RecyclerView) findViewById(R.id.recycler);

        dressList = new ArrayList<>();
        File file = new File(getFilesDir().getPath());
        File[] files = file.listFiles(); // 모든 파일 가져오기
        filePaths = new ArrayList<>();

        if(files != null) {
            for (int i = 0; i < files.length; i++) {
                if(String.valueOf(files[i]).contains("dress_")) filePaths.add(files[i].getName()); // 모든 파일 이름중에서 dress_ 파일만 가져오기
                //Log.i("DressList", filePaths.get(i));
            }
        }
        else{
            Toast.makeText(DressListActivity.this, "등록된 정보가 없습니다.", Toast.LENGTH_SHORT).show();
        }

        // 리사이클러뷰에 표시할 데이터 리스트 생성
        for(int i=0; i<filePaths.size(); i++){
            try {
                FileInputStream fis = openFileInput(filePaths.get(i));
                InputStreamReader inputStreamReader = new InputStreamReader(fis);
                BufferedReader reader = new BufferedReader(inputStreamReader);

                String name = reader.readLine();
                String cost = reader.readLine();
                String brande = reader.readLine();
                String size = reader.readLine();
                String type = reader.readLine();

                fis.close();

                /*
                Log.i("DressList 이름",name);
                Log.i("DressList 가격",cost);
                Log.i("DressList 브랜드",brande);
                Log.i("DressList 크기",size);
                Log.i("DressList 타입",type);*/

                Dress dress = new Dress(name, cost, brande, size, type);
                dressList.add(dress);
            }
            catch(FileNotFoundException e){
                e.printStackTrace();
            }
            catch(IOException e){ // readLine 예외문 처리
                e.printStackTrace();
            }
        }



        // 리사이클러뷰에 레이아웃 매니저 객체 지정
        recycler.setLayoutManager(new LinearLayoutManager(DressListActivity.this));
        // 리사이클러뷰에 CustomAdapter 객체 지정
        CustomAdapter customAdapter = new CustomAdapter(dressList); // 데이터 넘겨줌
        recycler.setAdapter(customAdapter); // 어댑터 설정





    }
}