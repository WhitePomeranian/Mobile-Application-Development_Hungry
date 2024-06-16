package com.fcu.hungryapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class CreateShopActivity extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ShopInfo");
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;



    private ImageView img_shopcreate;
    private EditText et_shopname;
    private EditText et_shopPhone;
    private EditText et_shopAddress;
    private Spinner sp_start;
    private Spinner sp_end;
    private Button btn_save;
    private Button btn_person_info;

    private Uri imgURI;


    private String start_time, end_time = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shop);

        img_shopcreate = findViewById(R.id.img_shopcreate);
        et_shopname = findViewById(R.id.et_shopname);
        et_shopPhone = findViewById(R.id.et_shop_info_phone);
        et_shopAddress = findViewById(R.id.et_shopInfo_address);
        sp_start = findViewById(R.id.sp_start);
        sp_end= findViewById(R.id.sp_end);
        btn_save = findViewById(R.id.btn_shop_info_save);
        btn_person_info = findViewById(R.id.btn_shop_info_personal_info);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        // query放在editText
        String shopId = user.getUid();
        DatabaseReference shopRef = database.getReference("ShopInfo").child(shopId);
        shopRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String address = dataSnapshot.child("address").getValue(String.class);
                    String phone = dataSnapshot.child("phone").getValue(String.class);
                    String startTime = dataSnapshot.child("start_time").getValue(String.class);
                    String endTime = dataSnapshot.child("end_time").getValue(String.class);
                    String imageUrl = dataSnapshot.child("image").getValue(String.class);

                    et_shopname.setText(name);
                    et_shopAddress.setText(address);
                    et_shopPhone.setText(phone);

                    String[] timeArray = getResources().getStringArray(R.array.time_array);
                    for(int i = 0; i < timeArray.length; i++) {
                        if(startTime.equals(timeArray[i])) {
                            sp_start.setSelection(i);
                        }
                        if(endTime.equals(timeArray[i])) {
                            sp_end.setSelection(i);
                        }
                    }

                    Glide.with(CreateShopActivity.this).load(imageUrl).into(img_shopcreate);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CreateShopActivity.this, "Failed to load shop data.", Toast.LENGTH_SHORT).show();
            }
        });
        //

        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(CreateShopActivity.this, R.array.time_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_start.setAdapter(adapter);
        sp_end.setAdapter(adapter);

        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String time = parent.getItemAtPosition(position).toString();
                if (parent.getId() == R.id.sp_start) {
                    start_time = time;
                } else if (parent.getId() == R.id.sp_end) {
                    end_time = time;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        sp_start.setOnItemSelectedListener(listener);
        sp_end.setOnItemSelectedListener(listener);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if(o.getResultCode() == Activity.RESULT_OK){
                            Intent data = o.getData();
                            imgURI = data.getData();
                            img_shopcreate.setImageURI(imgURI);
                        } else{
                            Toast.makeText(CreateShopActivity.this, "No image selected", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );

        img_shopcreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent img_select = new Intent();
                img_select.setAction(Intent.ACTION_GET_CONTENT);
                img_select.setType("image/*");
                activityResultLauncher.launch(img_select);

                img_shopcreate.setOnClickListener(null);
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_shopname.getText().toString();
                String address = et_shopAddress.getText().toString();
                String phone = et_shopPhone.getText().toString();

                if(start_time.equals("請選擇") || end_time.equals("請選擇")) {
                    Toast.makeText(CreateShopActivity.this, "Please select business hours", Toast.LENGTH_LONG).show();
                }

                if(imgURI != null && start_time != null && end_time != null && name != null && address != null && phone != null){
                    uploadToFirebase(imgURI, start_time, end_time, name, address, phone);
                } else{
                    Toast.makeText(CreateShopActivity.this, "Please select image and fill all entry", Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_person_info.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(CreateShopActivity.this, FrontPage.class);
                startActivity(intent);
            }
        });
    }

    private void uploadToFirebase(Uri Uri, String start_time, String end_time, String name, String address, String phone) {
        StorageReference imageReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(Uri));

        imageReference.putFile(Uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("image", uri.toString());
                        map.put("start_time", start_time);
                        map.put("end_time", end_time);
                        map.put("name", name);
                        map.put("shop_id",user.getUid());
                        map.put("address", address);
                        map.put("phone", phone);

                        databaseReference.child(user.getUid()).setValue(map);
                        Toast.makeText(CreateShopActivity.this, "Uploaded", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(CreateShopActivity.this, MerchantActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }

    private String getFileExtension(Uri fileUri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(fileUri));
    }
}
