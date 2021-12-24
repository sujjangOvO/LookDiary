package com.example.lookdiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MappingActivity extends AppCompatActivity {

    final int HAT = 0;
    final int TOP = 1;
    final int PANTS = 2;
    final int DRESS = 3;
    final int SHOES = 4;
    final int ACC = 5;

    String hat=null, top=null, pants=null, dress=null,
            shoes=null, a1=null, a2=null, a3=null; // 최종 저장할 이미지 이름

    String fileName;
    Button btnSave, btnBack;
    ImageView img_hat, img_top, img_pants, img_shoes, img_acc1, img_acc2, img_acc3;
    List<String> filesNameList;
    List<dressName> dressNameList;

    int hatCnt = 100;
    int topCnt = 200;
    int pantCnt = 300;
    int dressCnt = 400;
    int shoesCnt = 500;
    int accCnt = 600;
    // 최대 100개 까지 등록 .. ㅎㅎ 설마 넘게 ..?

    String getCacheDir;
    Boolean acc1, acc2, acc3;

    private AlarmManager alarmManager;
    private NotificationManager notificationManager;

    // 메뉴 등록
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu1,menu);

        SubMenu hat = menu.addSubMenu(HAT,HAT,0,"모자");
        SubMenu top = menu.addSubMenu(TOP,TOP,0,"상의");
        SubMenu pants = menu.addSubMenu(PANTS,PANTS,0,"하의");
        SubMenu dress = menu.addSubMenu(DRESS,DRESS,0,"드레스");
        SubMenu shoes = menu.addSubMenu(SHOES,SHOES,0,"신발");
        SubMenu acc = menu.addSubMenu(ACC,ACC,0,"악세사리");

        File file = new File(getCacheDir);
        File[] files = file.listFiles(); // 해당 폴더의 모든 파일을 File 객체에 담는다.
        filesNameList = new ArrayList<>();
        for(int i=0; i<files.length; i++){
            filesNameList.add(files[i].getName()); // List에 모든 파일의 이름을 넣는다.
        }
        dressNameList = new ArrayList<>(); // dressName 객체 넣어줄 List

        for(int i=0; i<files.length; i++){ // 파일 이름을 보고 type을 분류하여 메뉴에 추가하며, 옷 name으로 추가한다.
            String fileName = filesNameList.get(i);
            String[] fileNames = fileName.split("_");
            //Log.i("Mapping fileName",fileNames[0]);
            //Log.i("Mapping fileName",fileNames[1]);
            if(fileNames[0].equals("모자")){
                hat.add(HAT,hatCnt,0,fileNames[1]);
                dressName dressname = new dressName(hatCnt,fileName);
                dressNameList.add(dressname);
                hatCnt++;
            }
            else if(fileNames[0].equals("상의")){
                top.add(HAT,topCnt,0,fileNames[1]);
                dressName dressname = new dressName(topCnt,fileName);
                dressNameList.add(dressname);
                topCnt++;
            }
            else if(fileNames[0].equals("하의")){
                pants.add(HAT,pantCnt,0,fileNames[1]);
                dressName dressname = new dressName(pantCnt,fileName);
                dressNameList.add(dressname);
                pantCnt++;
            }
            else if(fileNames[0].equals("신발")){
                shoes.add(HAT,shoesCnt,0,fileNames[1]);
                dressName dressname = new dressName(shoesCnt,fileName);
                dressNameList.add(dressname);
                shoesCnt++;
            }
            else if(fileNames[0].equals("원피스")){
                dress.add(HAT,dressCnt,0,fileNames[1]);
                dressName dressname = new dressName(dressCnt,fileName);
                dressNameList.add(dressname);
                dressCnt++;
            }
            else{ // 악세사리
                acc.add(HAT,accCnt,0,fileNames[1]);
                dressName dressname = new dressName(accCnt,fileName);
                dressNameList.add(dressname);
                accCnt++;
            }
        }

        return true;
    }

    // 메뉴 이벤트 처리
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Bitmap bitmap = null;

        if(id >= 100 && id<200){ // 모자
            for(int i=0; i<dressNameList.size(); i++){
                if(id == dressNameList.get(i).getCnt()){
                    bitmap = BitmapFactory.decodeFile(getCacheDir+"/"+dressNameList.get(i).getDressName()); // 내장메모리 파일 가져옴
                    img_hat.setImageBitmap(bitmap); // 가져온 파일로 이미지뷰 설정
                    hat = dressNameList.get(i).getDressName();
                    Log.i("Mapping hat",hat);
                }
            }
        }
        else if(id>=200 && id<300){ // 상의
            for(int i=0; i<dressNameList.size(); i++){
                if(id == dressNameList.get(i).getCnt()){
                    bitmap = BitmapFactory.decodeFile(getCacheDir+"/"+dressNameList.get(i).getDressName()); // 내장메모리 파일 가져옴
                    img_top.setImageBitmap(bitmap); // 가져온 파일로 이미지뷰 설정
                    top = dressNameList.get(i).getDressName();
                    Log.i("Mapping top",top);
                }
            }
        }
        else if(id>=300 && id<400){ // 하의
            for(int i=0; i<dressNameList.size(); i++){
                if(id == dressNameList.get(i).getCnt()){
                    bitmap = BitmapFactory.decodeFile(getCacheDir+"/"+dressNameList.get(i).getDressName()); // 내장메모리 파일 가져옴
                    img_pants.setImageBitmap(bitmap); // 가져온 파일로 이미지뷰 설정
                    pants = dressNameList.get(i).getDressName();
                    Log.i("Mapping pants",pants);
                }
            }
        }
        else if(id>=400 && id<500){ // 드레스
            for(int i=0; i<dressNameList.size(); i++){
                if(id == dressNameList.get(i).getCnt()){
                    bitmap = BitmapFactory.decodeFile(getCacheDir+"/"+dressNameList.get(i).getDressName()); // 내장메모리 파일 가져옴
                    img_top.setImageBitmap(bitmap); // 가져온 파일로 이미지뷰 설정
                    dress = dressNameList.get(i).getDressName();
                    Log.i("Mapping dress",dress);
                }
            }
        }
        else if(id>=500 && id<600){ // 신발
            for(int i=0; i<dressNameList.size(); i++){
                if(id == dressNameList.get(i).getCnt()){
                    bitmap = BitmapFactory.decodeFile(getCacheDir+"/"+dressNameList.get(i).getDressName()); // 내장메모리 파일 가져옴
                    img_shoes.setImageBitmap(bitmap); // 가져온 파일로 이미지뷰 설정
                    shoes = dressNameList.get(i).getDressName();
                }
            }
        }
        else if(id >= 600){ // 악세사리
            for(int i=0; i<dressNameList.size(); i++){
                if(id == dressNameList.get(i).getCnt()){
                    bitmap = BitmapFactory.decodeFile(getCacheDir+"/"+dressNameList.get(i).getDressName()); // 내장메모리 파일 가져옴
                    if(!acc1) {
                        img_acc1.setImageBitmap(bitmap); // 가져온 파일로 이미지뷰 설정
                        a1 = dressNameList.get(i).getDressName();
                        Log.i("Mapping a1",a1);
                        acc1 = true;
                    }
                    else if(acc1 && !acc2){
                        img_acc2.setImageBitmap(bitmap);
                        a2 = dressNameList.get(i).getDressName();
                        Log.i("Mapping a2",a2);
                        acc2 = true;
                    }
                    else if(acc1 && acc2 && !acc3){
                        img_acc3.setImageBitmap(bitmap);
                        a3 = dressNameList.get(i).getDressName();
                        Log.i("Mapping a3",a3);
                        acc3 = true;
                    }
                    else if(acc1 && acc2 && acc3){ // 꽉차면 다시 1번부터
                        img_acc1.setImageBitmap(bitmap);
                        a1 = dressNameList.get(i).getDressName();
                        Log.i("Mapping a1",a1);
                        acc2 = false;
                        acc3 = false;
                    }
                }
            }
        }

        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapping);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnSave = (Button) findViewById(R.id.btnSave);
        img_hat = (ImageView)findViewById(R.id.img_hat);
        img_top = (ImageView)findViewById(R.id.img_top);
        img_pants = (ImageView)findViewById(R.id.img_pants);
        img_shoes = (ImageView)findViewById(R.id.img_shoes);
        img_acc1 = (ImageView)findViewById(R.id.img_acc1);
        img_acc2 = (ImageView)findViewById(R.id.img_acc2);
        img_acc3 = (ImageView)findViewById(R.id.img_acc3);

        acc1 = false;
        acc2 = false;
        acc3 = false;

        getCacheDir =  getCacheDir().getPath(); // 내장메모리 cache 경로, 멤버변수

        Intent intent = getIntent(); // 년_월_일 fileName 정보 받아옴
        fileName = intent.getStringExtra("fileName");

        //Toast.makeText(getApplicationContext(),fileName,Toast.LENGTH_LONG).show();

        // 뒤로가기 버튼
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 경로를 다 저장해서 한번에 불러오는 식으로 해야겠네용 ........
        // 파일이름은 fileName으로 설정.
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 악세사리는 등록 안해도 상관 없으니
                if(hat==null && top==null && pants==null && dress==null && shoes==null) {
                    Toast.makeText(MappingActivity.this, "무엇을 입을지 결정해보세요!", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(dress == null) {
                        LookBook lookBook = new LookBook(hat, top, pants, shoes, a1, a2, a3);
                        add_lookBook(lookBook);
                        Toast.makeText(MappingActivity.this, "룩북을 성공적으로 저장했습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                        LookBook lookBook = new LookBook(hat, dress, pants, shoes, a1, a2, a3);
                        add_lookBook(lookBook);
                        Toast.makeText(MappingActivity.this, "룩북을 성공적으로 저장했습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }
            }
        }); // 리스너 end


    } // onCreate end


    // 룩 내부저장소에 저장
    void add_lookBook(LookBook lookBook){
        try {
            FileOutputStream fileOutputStream = openFileOutput(fileName+".txt", MODE_PRIVATE);
            PrintWriter writer = new PrintWriter(fileOutputStream);
            writer.println(lookBook.get());
            writer.close();

            // 파일이름(날짜)를 파라미터로
            setAlarm(fileName);
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    void setAlarm(String fileName){
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        // 각 알람을 구분하기 위한 dateCode
        String[] tmp = fileName.split("_");
        int dateCode = Integer.parseInt(tmp[0] + tmp[1] + tmp[2]);

        Intent receiverIntent = new Intent(MappingActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MappingActivity.this, dateCode, receiverIntent, 0);

        //저장된 날짜 + 오전8시
        String from = fileName + " 8:00:00";

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd HH:mm:ss");
        Date datetime = null;
        try {
            datetime = dateFormat.parse(from);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(datetime);

        // 알람 등록
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(),pendingIntent);
    }


}