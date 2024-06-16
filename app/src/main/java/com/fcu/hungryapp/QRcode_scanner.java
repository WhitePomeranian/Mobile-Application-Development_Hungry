package com.fcu.hungryapp;

import android.Manifest;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class QRcode_scanner extends AppCompatActivity {
    public static final String QRCODE_ID_VALUE = "qrcodegetvaluecheck";
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private SurfaceView sv_qrcode;
    private CameraSource camera;
    private TextView tv_test;
    private boolean isProcessingQRCode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scanner);

        getPermissionCamera();

        sv_qrcode = findViewById(R.id.sv_qrcode);
        tv_test = findViewById(R.id.tv_test);

        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE).build();

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(@NonNull Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcodes = detections.getDetectedItems();

                if(qrcodes.size() != 0 && !isProcessingQRCode){
                    isProcessingQRCode = true;
                    String shop_id = qrcodes.valueAt(0).displayValue;

                    Intent intent = new Intent(QRcode_scanner.this, OrderActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(QRCODE_ID_VALUE, shop_id);
                    intent.putExtras(bundle);

                    startActivity(intent);
                    finish();
                }
            }
        });

        camera = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(500,500)
                .setAutoFocusEnabled(true)
                .build();

        sv_qrcode.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED)
                    return;
                try {
                    camera.start(holder);
                } catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                camera.stop();
            }
        });
    }

    public void getPermissionCamera() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle("需要相機權限")
                    .setMessage("需要相機權限才能掃描 QR Code，請授予相機權限")
                    .setPositiveButton("OK", (dialog, which) -> {
                                ActivityCompat.requestPermissions(QRcode_scanner.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                            }
                    )
                    .show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "已取得相機權限", Toast.LENGTH_SHORT).show();

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;

                    }
                    try {
                        camera.start(sv_qrcode.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } else {
                    Toast.makeText(this, "未取得相機權限", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}

/*
    public static final String QRCODE_ID_VALUE = "qrcodegetvaluecheck";
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private SurfaceView sv_qrcode;
    private CameraSource camera;
    private TextView tv_test;
    private boolean isProcessingQRCode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scanner);

        getPermissionCamera();

        sv_qrcode = findViewById(R.id.sv_qrcode);
        tv_test = findViewById(R.id.tv_test);

        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE).build();

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(@NonNull Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcodes = detections.getDetectedItems();

                if(qrcodes.size() != 0 && !isProcessingQRCode){
                    isProcessingQRCode = true;
                    String shop_id = qrcodes.valueAt(0).displayValue;

                    Intent intent = new Intent(QRcode_scanner.this, OrderActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(QRCODE_ID_VALUE, shop_id);
                    intent.putExtras(bundle);

                    startActivity(intent);
                    finish();
                }
            }
        });

        camera = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(500,500)
                .setAutoFocusEnabled(true)
                .build();

        sv_qrcode.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED)
                    return;
                try {
                    camera.start(holder);
                } catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                camera.stop();
            }
        });
    }

    public void getPermissionCamera() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle("需要相機權限")
                    .setMessage("需要相機權限才能掃描 QR Code，請授予相機權限")
                    .setPositiveButton("OK", (dialog, which) -> {
                                ActivityCompat.requestPermissions(QRcode_scanner.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                            }
                    )
                    .show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "已取得相機權限", Toast.LENGTH_SHORT).show();

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;

                    }
                    try {
                        camera.start(sv_qrcode.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } else {
                    Toast.makeText(this, "未取得相機權限", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
 */