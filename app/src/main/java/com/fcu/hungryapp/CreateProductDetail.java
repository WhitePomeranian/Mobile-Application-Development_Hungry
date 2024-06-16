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
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class CreateProductDetail extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Meals_list");
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;

    private ImageView img_product;
    private EditText et_product_name;
    private EditText et_product_price;
    private EditText et_product_describe;
    private Button bt_done_edit;
    private Button bt_delete;

    private Uri imgURI;
    public int count;
    private Boolean isEdit = false;
    private String key;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product_detail);

        img_product = findViewById(R.id.img_product);

        et_product_name = findViewById(R.id.et_product_name);
        et_product_price = findViewById(R.id.et_product_price);
        et_product_describe = findViewById(R.id.et_product_describe);

        bt_done_edit = findViewById(R.id.bt_done_edit);
        bt_delete = findViewById(R.id.bt_delete);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        Intent intent5 = getIntent();
        key = intent5.getStringExtra(CreateProductList.PRODUCT_INFO);
        if(key != null){

            bt_delete.setVisibility(View.VISIBLE);

            Log.d("key check send", key);
            DatabaseReference editreference = database.getReference("Meals_list").child(user.getUid()).child(key);
            editreference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        isEdit = true;

                        ProductInfo info = snapshot.getValue(ProductInfo.class);

                        Glide.with(CreateProductDetail.this)
                                .load(Uri.parse(info.getProduct_image().toString()))
                                .into(img_product);
                        imgURI = Uri.parse(info.getProduct_image());

                        et_product_name.setText(info.getProduct_name());
                        et_product_price.setText(info.getProduct_price());
                        et_product_describe.setText(info.getProduct_describe());

                        type = info.gettype();
                    } else{
                        Toast.makeText(CreateProductDetail.this, "No data found", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }




        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if(o.getResultCode() == Activity.RESULT_OK){
                            Intent data = o.getData();
                            imgURI = data.getData();
                            img_product.setImageURI(imgURI);
                        } else{
                            Toast.makeText(CreateProductDetail.this, "No image selected", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );

        img_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isEdit){
                    Intent img_select = new Intent();
                    img_select.setAction(Intent.ACTION_GET_CONTENT);
                    img_select.setType("image/*");
                    activityResultLauncher.launch(img_select);
                }
            }
        });

        bt_done_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_product_name.getText().toString() == null || et_product_price.getText().toString() == null){
                    Toast.makeText(CreateProductDetail.this, "請輸入商品名稱及價錢", Toast.LENGTH_LONG).show();
                } else{
                    String name = et_product_name.getText().toString();
                    String price = et_product_price.getText().toString();
                    String describe = et_product_describe.getText().toString();
                    if(imgURI == null){
                        imgURI = Uri.parse("android.resource://com.fcu.hungryapp/drawable/title.png");
                    }

                    if(isEdit){
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("product_image", imgURI.toString());
                        map.put("product_name", name);
                        map.put("product_price", price);
                        map.put("product_describe", describe);
                        map.put("type",type);
                        databaseReference.child(user.getUid()).child(key).setValue(map);
//                        Intent intent = new Intent(CreateProductDetail.this, CreateProductActivity.class);
//                        startActivity(intent);
                        finish();
                    } else{
                        Intent intent = getIntent();
                        type = intent.getStringExtra(CreateProductList.PRODUCT_TYPE);
                        uploadToFirebase(imgURI, name, price, describe, type);
                    }

                }
            }
        });

        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child(user.getUid()).child(key).removeValue();
                finish();
            }
        });


    }

    private void uploadToFirebase(Uri Uri, String name, String price, String describe, String type) {

        StorageReference imageReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(Uri));
        imageReference.putFile(Uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("product_image", uri.toString());
                        map.put("product_name", name);
                        map.put("product_price", price);
                        map.put("product_describe", describe);
                        map.put("type",type);


                        count = 1;
                        DatabaseReference checkReference = database.getReference("Meals_list").child(user.getUid());
                        checkReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                DataSnapshot datasnapshot = task.getResult();
                                if(datasnapshot.exists()){
                                    for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                                        String key = snapshot.getKey();
                                        Log.e("first key order", "Key:" + key + "order" + String.valueOf(count));
                                        if(String.valueOf(count).equals(key)){
                                            count++;
                                            Log.e("key order", "Key:" + key + "order" + String.valueOf(count));
                                        }
                                    }
                                    Log.e("orderReference...", String.valueOf(count));
                                    databaseReference.child(user.getUid()).child(String.valueOf(count)).setValue(map);
                                } else{
                                    Log.d("FirebaseData", "No data found");
                                    databaseReference.child(user.getUid()).child("1").setValue(map);
                                }
                            }
                        });


//                        Intent intent = new Intent(CreateProductDetail.this, CreateProductActivity.class);
//                        startActivity(intent);
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