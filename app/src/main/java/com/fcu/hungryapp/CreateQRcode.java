package com.fcu.hungryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
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
import com.google.firebase.auth.TotpSecret;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class CreateQRcode extends AppCompatActivity {
    private static final int REQUEST_WRITE_PERMISSION = 1;
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
                } else{
                    tv_table.setText("無桌號");

                    BarcodeEncoder encoder = new BarcodeEncoder();
                    String code = user.getUid();
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
                    if(ContextCompat.checkSelfPermission(CreateQRcode.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        saveImage();
                    } else{
                        ActivityCompat.requestPermissions(CreateQRcode.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_WRITE_PERMISSION);
                        ActivityCompat.requestPermissions(CreateQRcode.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_WRITE_PERMISSION);

                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_WRITE_PERMISSION){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                saveImage();
            } else{

            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void saveImage(){

        Uri image;
        ContentResolver contentResolver = getContentResolver();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            image = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        } else{
            image = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, System.currentTimeMillis() + ".jpg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "images/*");

        Uri uri = contentResolver.insert(image, contentValues);

        try{
            Bitmap bitmap = bit;

            OutputStream outputStream = contentResolver.openOutputStream(Objects.requireNonNull(uri));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            Objects.requireNonNull(outputStream);

            Toast.makeText(CreateQRcode.this, "Save success", Toast.LENGTH_LONG).show();
        } catch (Exception e){
            Toast.makeText(CreateQRcode.this, "Save does not success", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}

/*
    private static final int REQUEST_WRITE_PERMISSION = 1;
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
                } else{
                    tv_table.setText("無桌號");

                    BarcodeEncoder encoder = new BarcodeEncoder();
                    String code = user.getUid();
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
                    if(ContextCompat.checkSelfPermission(CreateQRcode.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        saveImage();
                    } else{
                        ActivityCompat.requestPermissions(CreateQRcode.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_WRITE_PERMISSION);
                        ActivityCompat.requestPermissions(CreateQRcode.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_WRITE_PERMISSION);

                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_WRITE_PERMISSION){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                saveImage();
            } else{

            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void saveImage(){

        Uri image;
        ContentResolver contentResolver = getContentResolver();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            image = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        } else{
            image = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, System.currentTimeMillis() + ".jpg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "images/*");

        Uri uri = contentResolver.insert(image, contentValues);

        try{
            Bitmap bitmap = bit;

            OutputStream outputStream = contentResolver.openOutputStream(Objects.requireNonNull(uri));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            Objects.requireNonNull(outputStream);

            Toast.makeText(CreateQRcode.this, "Save success", Toast.LENGTH_LONG).show();
        } catch (Exception e){
            Toast.makeText(CreateQRcode.this, "Save does not success", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
 */

