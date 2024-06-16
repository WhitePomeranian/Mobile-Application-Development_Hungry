package com.fcu.hungryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class CreateProductList extends AppCompatActivity {
    public static final String PRODUCT_INFO = "productinfo";
    public static final String PRODUCT_TYPE = "producttype";

    private String type;
    private Boolean noData = false;

    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Meals_list");
    final private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private FirebaseAuth auth;
    private FirebaseUser user;

    private ListView lv_product;
    private FloatingActionButton fab_add_product;
    private Button bt_done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product_list);

        lv_product = findViewById(R.id.lv_product);
        fab_add_product = findViewById(R.id.fab_add_product);
        bt_done = findViewById(R.id.bt_done);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        Intent intent = getIntent();
        type = intent.getStringExtra(CreateProductActivity.MEALS_TYPE);

        List<ProductInfo> product_list = new ArrayList<>();
        List<String> key_position = new ArrayList<>();

        databaseReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        String key = dataSnapshot.getKey();
                        ProductInfo info = dataSnapshot.getValue(ProductInfo.class);

                        if(info != null){
                            if(info.gettype().equals(type)){
                                product_list.add(info);
                                key_position.add(key);
                            }
                        }
                    }
                    ProductInfoAdapter adapter = new ProductInfoAdapter(CreateProductList.this, product_list);
                    lv_product.setAdapter(adapter);
                } else{
                    noData = true;
                    Toast.makeText(CreateProductList.this, "No data found", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        lv_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ProductInfo info = product_list.get(i);

                String send_info = "{image=" + info.getProduct_image() + ", name="
                        + info.getProduct_name() + ", price=" + info.getProduct_price()
                        + ", describe=" + info.getProduct_describe()
                        + ", type=" + info.gettype() + "}";

                Intent intent2 = new Intent(CreateProductList.this, CreateProductDetail.class);
                intent2.putExtra(PRODUCT_INFO, key_position.get(i));
                startActivity(intent2);
                finish();

            }
        });

        fab_add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(CreateProductList.this, CreateProductDetail.class);
                intent3.putExtra(PRODUCT_TYPE, type);
                startActivity(intent3);
                finish();
            }
        });

        bt_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateProductList.this, CreateProductActivity.class));
                finish();
            }
        });

    }
}