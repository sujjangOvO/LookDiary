package com.example.lookdiary;

import static android.content.ContentValues.TAG;
import static android.os.Environment.DIRECTORY_DCIM;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DressRegisterActivity extends AppCompatActivity {

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_CAMERA = 2;

    private final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STROAGE = 1001;

    Button btnSave, btnBack, btn_imgUpload;
    ImageView img;
    EditText edt_brand, edt_cost, edt_size, edt_name;
    Spinner spinner;

    String mediaPath;
    Bitmap bitmap;
    private File tempFile;
    Uri mImageCaptureUri;
    File tmpFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dress_register);
        setTitle("옷 등록하기");


        // 권한 확인
        int permissioncheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        //권한이 없을 때 :
        if(permissioncheck != PackageManager.PERMISSION_GRANTED) {
            //Toast.makeText(this, "권한 승인이 필요합니다.", Toast.LENGTH_LONG).show();
            //사용자가 거부를 한 경우
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "외부 저장소 접근을 위해 저장소 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
            }
            //최초 요청
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STROAGE);
                Toast.makeText(this, "외부 저장소 접근을 위해 저장소 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
            }
        }


        btnBack = (Button) findViewById(R.id.btnBack);
        btnSave = (Button) findViewById(R.id.btnSave);
        spinner = (Spinner) findViewById(R.id.spinner);
        btn_imgUpload = (Button) findViewById(R.id.btn_imgUpload);
        edt_brand = (EditText) findViewById(R.id.edt_brand);
        edt_cost = (EditText) findViewById(R.id.edt_cost);
        edt_size = (EditText) findViewById(R.id.edt_size);
        edt_name = (EditText) findViewById(R.id.edt_name);
        img = (ImageView) findViewById(R.id.img);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,R.array.type, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // 뒤로가기 버튼
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 저장하기 버튼
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!nullCheck(edt_brand) || !nullCheck(edt_cost) || !nullCheck(edt_size) || !nullCheck(edt_name)){
                    Toast.makeText(getApplicationContext(), "빈칸을 확인해 주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    String type = spinner.getSelectedItem().toString();
                    String brande = edt_brand.getText().toString();
                    String cost = edt_cost.getText().toString();
                    String size = edt_size.getText().toString();
                    String name = edt_name.getText().toString();

                    Dress dress = new Dress(name,cost,brande,size,type);
                    String dressFileName = "dress_"+name;
                    add_dress(dress,dressFileName); // 옷 정보 저장

                    String ImgFilePath = getCacheDir().getAbsolutePath() + "/" + type + "_" + name + "_img";
                    try { // 이미지 파일 저장

                        // from ImgView to Bitmap
                        img.setDrawingCacheEnabled(true);
                        bitmap = img.getDrawingCache();

                        // Save file from Bitmap
                        File f = new File(ImgFilePath);
                        FileOutputStream fos = new FileOutputStream(f);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        fos.flush();
                        fos.close();

                        Toast.makeText(getApplicationContext(), "저장 완료", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    catch (Exception e){
                        Toast.makeText(getApplicationContext(), "file error", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

        btn_imgUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                // 사진촬영
                DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doTakePhotoAction();
                    }
                };

                // 앨범선택
                DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doTakeAlbumAction();
                    }
                };

                //취소
                DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                };


                AlertDialog.Builder dig = new AlertDialog.Builder(DressRegisterActivity.this);

                dig.setTitle("업로드할 이미지 선택");
                dig.setPositiveButton("사진촬영", cameraListener);
                dig.setNeutralButton("앨범선택", albumListener);
                dig.setNegativeButton("취소", cancelListener);
                dig.show();

            }
        });

    } // onCrate end

    // 옷 정보 내부저장소에 작성
    void add_dress(Dress dress,String fileName){
        try {
            FileOutputStream fileOutputStream = openFileOutput(fileName, MODE_PRIVATE);
            PrintWriter writer = new PrintWriter(fileOutputStream);
            writer.println(dress.get());
            writer.close();
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }

    }

    //권한에 대한 응답이 있을때 작동
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STROAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "권한이 허가되어 있습니다.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "아직 승인받지 않았습니다.", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            Toast.makeText(this,"사진 업로드 실패",Toast.LENGTH_SHORT).show();
            return;
        }

        Bundle extras = data.getExtras();
        //mImageCaptureUri = Uri.parse(extras.getString("test"));

        switch (requestCode) {

            case CROP_FROM_CAMERA : { // 이미지 크롭
                // 크롭이 된 이후의 이미지를 넘겨 받습니다.
                // 이미지뷰에 이미지를 보여준다거나 부가적인 작업 이후에
                // 임시 파일을 삭제합니다.

                if(extras != null)
                {
                    Bitmap photo = extras.getParcelable("data");
                    img.setImageBitmap(photo);
                }

                // 임시 파일 삭제
                File f = new File(mImageCaptureUri.getPath());
                if(f.exists())
                {
                    f.delete();
                }

                break;
            }

            case PICK_FROM_ALBUM : {
                mImageCaptureUri = data.getData();

                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");

                intent.putExtra("outputX", 90);
                intent.putExtra("outputY", 90);
                intent.putExtra("aspectX", 1.5);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_CAMERA);

                break;
            }

            case PICK_FROM_CAMERA: {
                // 카메라
                // 이미지를 가져온 이후의 리사이즈할 이미지 크기를 결정합니다.
                // 이후에 이미지 크롭 어플리케이션을 호출하게 됩니다.

                
                // 가장 최근에 찍은 사진의 경로를 딴다
                final String[] imageColumns = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA };
                final String imageOrderBy = MediaStore.Images.Media._ID + " DESC";
                Cursor imageCursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageColumns, null, null, imageOrderBy);
                imageCursor.moveToFirst();
                do {
                    String fullPath = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    if (fullPath.contains("DCIM")) {
                        //--last image from camera --
                        mImageCaptureUri = Uri.parse(fullPath); // 결과 값
                        //Toast.makeText(this, mImageCaptureUri.toString(), Toast.LENGTH_LONG).show();
                        break;
                    }
                }
                while (imageCursor.moveToNext());
                
                // -------------------------------------------------------------------------------

                // Absolute Path(/sdcard/~~~~~~~/XXXX.jpg) -> content:// path
                Cursor cursor = getContentResolver().query( MediaStore.Images.Media.EXTERNAL_CONTENT_URI , new String[] {
                        MediaStore.Images.Media._ID } , MediaStore.Images.Media.DATA + "=? " , new String[] { mImageCaptureUri.toString() }, null);
                cursor.moveToFirst();
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                mImageCaptureUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI , Integer.toString(id));

                // ----------------------------------------------------------------------------

                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");

                intent.putExtra("outputX", 90);
                intent.putExtra("outputY", 90);
                intent.putExtra("aspectX", 1.5);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_CAMERA);


            }

        }
    }



    public void doTakeAlbumAction(){ // 앨범에서 이미지 가져오기
        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent,PICK_FROM_ALBUM );
    }

    public void doTakePhotoAction(){ //카메라 촬영

        int permissionCheck = ContextCompat.checkSelfPermission(DressRegisterActivity.this,Manifest.permission.CAMERA);
        if(permissionCheck == PackageManager.PERMISSION_DENIED){ // 카메라 권한x
            ActivityCompat.requestPermissions(DressRegisterActivity.this,new String[]{Manifest.permission.CAMERA},100);
        }
        else {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
            //mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
            //mImageCaptureUri = FileProvider.getUriForFile(this, "", new File(getCacheDir(), url));

            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, getCacheDir().getAbsolutePath() + url);

            tmpFile = (new File(getCacheDir().getAbsolutePath()  + url));

            startActivityForResult(intent, PICK_FROM_CAMERA);

        }

    }



    // 권한 확인
    public void checkPermission(){
        String temp = "";
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            temp += Manifest.permission.READ_EXTERNAL_STORAGE+ " ";
        }

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE+" ";
        }
        //파일 쓰기, 읽기 권한 확인

        if(!TextUtils.isEmpty(temp)){ // 권한 요청
            ActivityCompat.requestPermissions(this,temp.trim().split(" "),101);
        }
        else{
            // 모든 권한 허용 상태
        }
    }

    public boolean nullCheck(EditText editText){
        if(editText.getText().toString().replace(" ", "").equals(""))
            return false;
        else return true;
    }
}