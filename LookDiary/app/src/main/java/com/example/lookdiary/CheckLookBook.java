package com.example.lookdiary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class CheckLookBook extends AppCompatActivity {

    String fileName, getCacheDir, filePath;
    Button btnDelete,btnBack;
    ImageView img_hat, img_top, img_pants, img_shoes, img_acc1, img_acc2, img_acc3;
    String[] imgs = {"hat", "top", "pants", "shoes", "acc1", "acc2", "acc3"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_look_book);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnBack.setOnClickListener(clickListener);
        btnDelete.setOnClickListener(clickListener);

        img_hat = (ImageView)findViewById(R.id.img_hat);
        img_top = (ImageView)findViewById(R.id.img_top);
        img_pants = (ImageView)findViewById(R.id.img_pants);
        img_shoes = (ImageView)findViewById(R.id.img_shoes);
        img_acc1 = (ImageView)findViewById(R.id.img_acc1);
        img_acc2 = (ImageView)findViewById(R.id.img_acc2);
        img_acc3 = (ImageView)findViewById(R.id.img_acc3);

        Intent intent = getIntent(); // 년_월_일 fileName 정보 받아옴
        fileName = intent.getStringExtra("fileName");
        String[] lookbook = new String[7]; // hat, top, pants, shoes, acc1, acc2, acc3 (dress는 top과 통일)

        getCacheDir = getCacheDir().getPath()+"/"; // 내장 메모리 cache 경로 => 이미지를 읽기 위한 경로
        //filePath = getFilesDir().getPath()+"/"+fileName+".txt"; // 접근할 파일 경로 => 읽을 파일 경로
        filePath = fileName+".txt";
        Log.i("CheckLookBook",filePath);


        try {
            // oepnFileInput은 하위 디렉토리 /files에 있는 파일을 읽기 모드로 오픈
            FileInputStream fileInputStream = openFileInput(filePath); // 파일 경로를 써주는게 아니고 파일 이름을 써줘야함.
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            for(int i=0; i<lookbook.length; i++) {
                String buffer = bufferedReader.readLine();
                if (buffer.equals("null")) {
                    lookbook[i] = "null";
                }
                else {
                    Log.i("CheckLookBook",buffer);
                    lookbook[i] = buffer;
                }
            }

            fileInputStream.close();
            inputStreamReader.close();
            bufferedReader.close();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch(IOException e){ // readLine 예외문 처리
            e.printStackTrace();
        }


        //for(int i=0; i<lookbook.length; i++) Log.i("CheckLookBook ",lookbook[i]);

        readImg(lookbook);

    }

    // null 아닌것만 읽어서 캐시메모리로부터 이미지 읽어오기.
    void readImg(String[] lookbook){
        Bitmap bitmap;
        String imgPath;
        for(int i=0; i<lookbook.length; i++){
            //Log.i("Check look",lookbook[i]);
            String look = lookbook[i];
            if(!(look.equals("null"))){
                switch (imgs[i]){
                    case "hat":
                        imgPath = getCacheDir+lookbook[i];
                        bitmap = BitmapFactory.decodeFile(imgPath);
                        img_hat.setImageBitmap(bitmap);
                        break;
                    case "top":
                        imgPath = getCacheDir+lookbook[i];
                        bitmap = BitmapFactory.decodeFile(imgPath);
                        img_top.setImageBitmap(bitmap);
                        break;
                    case "pants":
                        imgPath = getCacheDir+lookbook[i];
                        bitmap = BitmapFactory.decodeFile(imgPath);
                        img_pants.setImageBitmap(bitmap);
                        break;
                    case "shoes":
                        imgPath = getCacheDir+lookbook[i];
                        bitmap = BitmapFactory.decodeFile(imgPath);
                        img_shoes.setImageBitmap(bitmap);;
                        break;
                    case "acc1":
                        imgPath = getCacheDir+lookbook[i];
                        bitmap = BitmapFactory.decodeFile(imgPath);
                        img_acc1.setImageBitmap(bitmap);
                        break;
                    case "acc2":
                        imgPath = getCacheDir+lookbook[i];
                        bitmap = BitmapFactory.decodeFile(imgPath);
                        img_acc2.setImageBitmap(bitmap);
                        break;
                    case "acc3":
                        imgPath = getCacheDir+lookbook[i];
                        bitmap = BitmapFactory.decodeFile(imgPath);
                        img_acc3.setImageBitmap(bitmap);
                        break;
                }
            }
        }
    }

    // 클릭리스너
    View.OnClickListener clickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnBack:
                    finish();
                    break;
                case R.id.btnDelete: // 삭제하기 버튼 ( 구현 필요 )
                    AlertDialog.Builder dlg = new AlertDialog.Builder(CheckLookBook.this);
                    dlg.setTitle("확인 메시지");
                    dlg.setMessage("해당 룩북을 삭제하시겠습니까?");
                    dlg.setNegativeButton("no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    dlg.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String path = getFilesDir().getPath()+"/"+fileName+".txt"; // 내부저장소 경로
                            //Log.i("CheckLookBook path",path);
                            File file = new File(path);
                            if(file.exists()){
                                file.delete();
                                Toast.makeText(CheckLookBook.this,"성공적으로 삭제하였습니다.",Toast.LENGTH_SHORT).show();

                                cancelAlarm(fileName); // 파일뿐만 아니라 알람도 같이 삭제 해준다.
                                finish();
                            }
                            else{
                                Toast.makeText(CheckLookBook.this,"삭제 할 파일이 없습니다.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    dlg.show();
                    break;
            }
        }
    };

    void cancelAlarm(String fileName){
        // 각 알람을 구분하기 위한 dateCode
        String[] tmp = fileName.split("_");
        int dateCode = Integer.parseInt(tmp[0] + tmp[1] + tmp[2]);

        Intent receiverIntent = new Intent(CheckLookBook.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(CheckLookBook.this, dateCode, receiverIntent, 0);

        pendingIntent.cancel();

    }

}