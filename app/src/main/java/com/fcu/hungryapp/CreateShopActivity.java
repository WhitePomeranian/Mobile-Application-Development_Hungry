package com.fcu.hungryapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class CreateShopActivity extends AppCompatActivity {
    private ImageView img_shopcreate;
    private EditText et_shopname;
    private Spinner sp_start;
    private Spinner sp_end;
    private Button bt_create;

    private Uri imgURI;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ShopInfo");
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;

    private String start_time, end_time = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shop);

        img_shopcreate = findViewById(R.id.img_shopcreate);
        et_shopname = findViewById(R.id.et_shopname);
        sp_start = findViewById(R.id.sp_start);
        sp_end= findViewById(R.id.sp_end);
        bt_create = findViewById(R.id.bt_create);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

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

        bt_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_shopname.getText().toString();

                if(imgURI != null && start_time != null && end_time != null && name != null){
                    uploadToFirebase(imgURI, start_time, end_time, name);
                } else{
                    Toast.makeText(CreateShopActivity.this, "Please select image", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void uploadToFirebase(Uri Uri, String start_time, String end_time, String name) {
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