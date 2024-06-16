package com.fcu.hungryapp;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchQRcode extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_qrcode);

        private Button btnReservationList;
        private Button btnOrderDetails;
        private Button btncreateqrcode;
        private Button btncreateproduct;


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
                    if(ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA)
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
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                return;
            }

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)) {
                new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setTitle("需要相機權限")
                        .setMessage("需要相機權限才能掃描 QR Code，請授予相機權限")
                        .setPositiveButton("OK", (dialog, which) -> {
                                    ActivityCompat.requestPermissions(QRcode_scanner.this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                                }
                        )
                        .show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            switch (requestCode) {
                case REQUEST_CAMERA_PERMISSION:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "已取得相機權限", Toast.LENGTH_SHORT).show();

                        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
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

        @Override
        public int getCount() {
            return lsorder.size();
        }

        @Override
        public Object getItem(int position) {
            return lsorder.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.order_info, parent, false);
            }

            TextView tv_ordername = convertView.findViewById(R.id.tv_ordername);
            TextView tv_ordernum = convertView.findViewById(R.id.tv_ordernum);
            TextView tv_orderphone = convertView.findViewById(R.id.tv_orderphone);
            TextView tv_orderemail = convertView.findViewById(R.id.tv_orderemail);
            TextView tv_ordernote = convertView.findViewById(R.id.tv_ordernote);
            TextView tv_ordertime = convertView.findViewById(R.id.tv_ordertime);

            OrderInfo info = lsorder.get(position);
            tv_ordername.setText(info.getName());
            tv_ordernum.setText(info.getOrder_num());
            tv_orderphone.setText(info.getPhone());
            tv_orderemail.setText(info.getEmail());
            tv_ordernote.setText(info.getOrder_note());
            tv_ordertime.setText(info.getOrder_time());

            return convertView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_merchant);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            btnReservationList = findViewById(R.id.btn_reservation_list);
            btnOrderDetails = findViewById(R.id.btn_order_details);
            btncreateqrcode = findViewById(R.id.btn_create_qrcode);
            btncreateproduct = findViewById(R.id.btn_create_product);

            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = v.getId();
                    if(id == R.id.btn_reservation_list) {
                        Intent intent = new Intent(MerchantActivity.this, OrderManage.class);
                        startActivity(intent);
                    } else if(id == R.id.btn_order_details) {
                        Toast.makeText(MerchantActivity.this, "no function yet", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(MerchantActivity.this, OrderActivity.class);
//                    startActivity(intent);
                    } else if (id == R.id.btn_create_qrcode) {
                        Intent intent = new Intent(MerchantActivity.this, com.fcu.hungryapp.CreateQRcode.class);
                        startActivity(intent);
                    } else if (id == R.id.btn_create_product) {
//                    Intent intent = new Intent(MerchantActivity.this, CreateProduct.class);
//                    startActivity(intent);
                    }
                }
            };

            btnReservationList.setOnClickListener(listener);
            btnOrderDetails.setOnClickListener(listener);
            btncreateqrcode.setOnClickListener(listener);
            btncreateproduct.setOnClickListener(listener);
        }
    }

        public class MainActivity extends AppCompatActivity {

            private TextView tvLoginRegister;

            //Firebase authentication
            public FirebaseAuth auth;
            private FirebaseUser user;
            private FirebaseDatabase database;
            private EditText etLoginPassword;
            private EditText etLoginEmail;
            private Button btnLogin;


            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

                String message = getIntent().getStringExtra("message");
                if (message != null) {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                }

                etLoginEmail = findViewById(R.id.et_login_email);
                etLoginPassword = findViewById(R.id.et_login_password);
                btnLogin = findViewById(R.id.btn_login);

                tvLoginRegister = findViewById(R.id.tv_login_register);

                //firebase authentication login
                auth = FirebaseAuth.getInstance();
                user = auth.getCurrentUser();
                database = FirebaseDatabase.getInstance();

                if(user != null){
                    CheckUserType();
                }

                btnLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String Email = etLoginEmail.getText().toString();
                        String Password = etLoginPassword.getText().toString();

                        if(Email.isEmpty()){
                            etLoginEmail.setError("Email require");
                            return;
                        }
                        if(Password.isEmpty()){
                            etLoginPassword.setError("Password require");
                            return;
                        }

                        auth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(MainActivity.this, "Login...", Toast.LENGTH_SHORT).show();
                                    user = auth.getCurrentUser();
                                    if(user != null){
                                        CheckUserType();
                                    }

                                } else{
                                    Toast.makeText(MainActivity.this, "User not found or Password incorrect", Toast.LENGTH_LONG).show();
                                    etLoginPassword.setText("");
                                }
                            }
                        });
                    }
                });

                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setUnderlineText(true); // 設置底線
                        ds.setColor(Color.parseColor("#4BB2F9"));
                    }
                };
                SpannableString spannableString = new SpannableString(tvLoginRegister.getText().toString());
                spannableString.setSpan(clickableSpan, 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE); // 設置監聽器並包含頭尾兩字
                tvLoginRegister.setText(spannableString);
                tvLoginRegister.setMovementMethod(LinkMovementMethod.getInstance()); // 使TextView中的超連結能夠被點擊

            }

            private void CheckUserType() {
                DatabaseReference reference = database.getReference("users");
                reference.child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()){
                            String user_info = String.valueOf(task.getResult().getValue());
                            Log.e("user info check", user_info);
                            String isShop = extractValue(user_info, "isShop");

                            if(isShop.equals("true")){
                                startActivity(new Intent(MainActivity.this, MerchantActivity.class));
//                        finish();
                            } else{
                                startActivity(new Intent(MainActivity.this, SearchShop.class));
                                finish();
                            }
                        } else{
                            Toast.makeText(MainActivity.this, "Get user info Error!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }


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

        public static final String SHOP_ID_VALUE = "shopgetvaluecheck";
        private Uri imgURI;
        final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ShopInfo");
        final private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        private FirebaseAuth auth;
        private FirebaseUser user;
        private FirebaseDatabase database;

        private ListView lvShop;

        // drawer
        private DrawerLayout drawerLayout;
        NavigationView nvDrawer;
        private Toolbar tbSearchShop;

        //floating action button
        private FloatingActionButton fab_camera;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_search_shop);

            String message = getIntent().getStringExtra("message");
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }



            lvShop = findViewById(R.id.lv_shop);
            fab_camera = findViewById(R.id.fab_camera_bt);

            List<ShopInfo> shops = new ArrayList<>();

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                            ShopInfo info = dataSnapshot.getValue(ShopInfo.class);

                            if(info != null){
                                Log.d("check info", info.getImage().toString());
                                shops.add(info);
                            }
                        }
                        ShopInfoAdapter adapter = new ShopInfoAdapter(SearchShop.this, shops);
                        lvShop.setAdapter(adapter);
                    } else{
                        Toast.makeText(SearchShop.this, "No data found", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            lvShop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ShopInfo select = shops.get(position);
                    String shop_id = select.getShop_id();
                    Intent intent = new Intent(SearchShop.this, SeatActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(SHOP_ID_VALUE, shop_id);
                    intent.putExtras(bundle);

                    startActivity(intent);
                }
            });



            // drawer
            drawerLayout = findViewById(R.id.drawer_layout);
            tbSearchShop = findViewById(R.id.tb_search_shop);
            nvDrawer = findViewById(R.id.nv_drawer);
            nvDrawer.setItemIconTintList(null);
            nvDrawer.bringToFront();
            setSupportActionBar(tbSearchShop);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(SearchShop.this, drawerLayout, tbSearchShop, R.string.drawer_open, R.string.drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            nvDrawer.setNavigationItemSelectedListener(this);

            fab_camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //start activity to scan QRCode.
                    Intent intent = new Intent(SearchShop.this, QRcode_scanner.class);
                    startActivity(intent);
                }
            });
        }

        public void onBackPressed() {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }

        public boolean onNavigationItemSelected(MenuItem item) {
            int id = item.getItemId();
            if(id == R.id.nav_personal_info) {
                Intent intent = new Intent(SearchShop.this, FrontPage.class);
                startActivity(intent);
                return true;
            } else if(id == R.id.nav_orders) {
                Intent intent = new Intent(SearchShop.this, QRcode_scanner.class);
                startActivity(intent);
                return true;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        }



    }
}