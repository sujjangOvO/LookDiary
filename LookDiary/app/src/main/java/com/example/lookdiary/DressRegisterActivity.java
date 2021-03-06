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
        setTitle("??? ????????????");


        // ?????? ??????
        int permissioncheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        //????????? ?????? ??? :
        if(permissioncheck != PackageManager.PERMISSION_GRANTED) {
            //Toast.makeText(this, "?????? ????????? ???????????????.", Toast.LENGTH_LONG).show();
            //???????????? ????????? ??? ??????
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "?????? ????????? ????????? ?????? ????????? ?????? ????????? ???????????????.", Toast.LENGTH_LONG).show();
            }
            //?????? ??????
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STROAGE);
                Toast.makeText(this, "?????? ????????? ????????? ?????? ????????? ?????? ????????? ???????????????.", Toast.LENGTH_LONG).show();
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

        // ???????????? ??????
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // ???????????? ??????
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!nullCheck(edt_brand) || !nullCheck(edt_cost) || !nullCheck(edt_size) || !nullCheck(edt_name)){
                    Toast.makeText(getApplicationContext(), "????????? ????????? ?????????.", Toast.LENGTH_SHORT).show();
                }
                else{
                    String type = spinner.getSelectedItem().toString();
                    String brande = edt_brand.getText().toString();
                    String cost = edt_cost.getText().toString();
                    String size = edt_size.getText().toString();
                    String name = edt_name.getText().toString();

                    Dress dress = new Dress(name,cost,brande,size,type);
                    String dressFileName = "dress_"+name;
                    add_dress(dress,dressFileName); // ??? ?????? ??????

                    String ImgFilePath = getCacheDir().getAbsolutePath() + "/" + type + "_" + name + "_img";
                    try { // ????????? ?????? ??????

                        // from ImgView to Bitmap
                        img.setDrawingCacheEnabled(true);
                        bitmap = img.getDrawingCache();

                        // Save file from Bitmap
                        File f = new File(ImgFilePath);
                        FileOutputStream fos = new FileOutputStream(f);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        fos.flush();
                        fos.close();

                        Toast.makeText(getApplicationContext(), "?????? ??????", Toast.LENGTH_SHORT).show();
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
                // ????????????
                DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doTakePhotoAction();
                    }
                };

                // ????????????
                DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doTakeAlbumAction();
                    }
                };

                //??????
                DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                };


                AlertDialog.Builder dig = new AlertDialog.Builder(DressRegisterActivity.this);

                dig.setTitle("???????????? ????????? ??????");
                dig.setPositiveButton("????????????", cameraListener);
                dig.setNeutralButton("????????????", albumListener);
                dig.setNegativeButton("??????", cancelListener);
                dig.show();

            }
        });

    } // onCrate end

    // ??? ?????? ?????????????????? ??????
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

    //????????? ?????? ????????? ????????? ??????
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STROAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "????????? ???????????? ????????????.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "?????? ???????????? ???????????????.", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            Toast.makeText(this,"?????? ????????? ??????",Toast.LENGTH_SHORT).show();
            return;
        }

        Bundle extras = data.getExtras();
        //mImageCaptureUri = Uri.parse(extras.getString("test"));

        switch (requestCode) {

            case CROP_FROM_CAMERA : { // ????????? ??????
                // ????????? ??? ????????? ???????????? ?????? ????????????.
                // ??????????????? ???????????? ?????????????????? ???????????? ?????? ?????????
                // ?????? ????????? ???????????????.

                if(extras != null)
                {
                    Bitmap photo = extras.getParcelable("data");
                    img.setImageBitmap(photo);
                }

                // ?????? ?????? ??????
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
                // ?????????
                // ???????????? ????????? ????????? ??????????????? ????????? ????????? ???????????????.
                // ????????? ????????? ?????? ????????????????????? ???????????? ?????????.

                
                // ?????? ????????? ?????? ????????? ????????? ??????
                final String[] imageColumns = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA };
                final String imageOrderBy = MediaStore.Images.Media._ID + " DESC";
                Cursor imageCursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageColumns, null, null, imageOrderBy);
                imageCursor.moveToFirst();
                do {
                    String fullPath = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    if (fullPath.contains("DCIM")) {
                        //--last image from camera --
                        mImageCaptureUri = Uri.parse(fullPath); // ?????? ???
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



    public void doTakeAlbumAction(){ // ???????????? ????????? ????????????
        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent,PICK_FROM_ALBUM );
    }

    public void doTakePhotoAction(){ //????????? ??????

        int permissionCheck = ContextCompat.checkSelfPermission(DressRegisterActivity.this,Manifest.permission.CAMERA);
        if(permissionCheck == PackageManager.PERMISSION_DENIED){ // ????????? ??????x
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



    // ?????? ??????
    public void checkPermission(){
        String temp = "";
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            temp += Manifest.permission.READ_EXTERNAL_STORAGE+ " ";
        }

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE+" ";
        }
        //?????? ??????, ?????? ?????? ??????

        if(!TextUtils.isEmpty(temp)){ // ?????? ??????
            ActivityCompat.requestPermissions(this,temp.trim().split(" "),101);
        }
        else{
            // ?????? ?????? ?????? ??????
        }
    }

    public boolean nullCheck(EditText editText){
        if(editText.getText().toString().replace(" ", "").equals(""))
            return false;
        else return true;
    }
}