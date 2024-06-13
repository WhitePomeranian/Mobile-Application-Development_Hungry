package com.fcu.hungryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreateQRcode extends AppCompatActivity {
    private static final int REQUEST_WRITE_PERMISSION = 786;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private ImageView img_qrcode;
    private TextView tv_table;
    private EditText et_tablenum;
    private Button bt_storage;
    private Button bt_newcreate;

    private Bitmap bit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_qrcode);

        img_qrcode = findViewById(R.id.img_qrcode);
        tv_table = findViewById(R.id.tv_table);
        et_tablenum = findViewById(R.id.et_tablenum);
        bt_storage = findViewById(R.id.bt_storage);
        bt_newcreate = findViewById(R.id.bt_newcreate);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        ActivityCompat.requestPermissions(CreateQRcode.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        ActivityCompat.requestPermissions(CreateQRcode.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);




        bt_newcreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String table_num = et_tablenum.getText().toString();
                if(table_num != null){
                    tv_table.setText("桌號為: " + table_num);

                    BarcodeEncoder encoder = new BarcodeEncoder();
                    String code = user.getUid() + "@@@" + table_num;
                    try {
                        Toast.makeText(CreateQRcode.this, user.getUid(), Toast.LENGTH_LONG).show();
                        bit = encoder.encodeBitmap(code, BarcodeFormat.QR_CODE,
                                1300, 1300);
                        img_qrcode .setImageBitmap(bit);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        bt_storage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bit != null){
                    saveToGallery();
                }
            }
        });
    }

    private void saveToGallery(){
        Bitmap bitmap = bit;

        FileOutputStream outputStream = null;
        File file = Environment.getExternalStorageDirectory();
        File dir = new File(file.getAbsolutePath() + "/MyPics");
        dir.mkdirs();

        Log.e("file path", String.valueOf(dir));

        String filename = String.format("%d.jpg",System.currentTimeMillis());
        File outFile = new File(dir,filename);
        try{
            outputStream = new FileOutputStream(outFile);
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.e("bitmap", bit.toString());
        bitmap.compress(Bitmap.CompressFormat.JPEG,10,outputStream);
        try{
            outputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            outputStream.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}