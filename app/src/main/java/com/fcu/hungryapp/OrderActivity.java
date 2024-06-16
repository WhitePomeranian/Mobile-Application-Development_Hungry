package com.fcu.hungryapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;

    private String qrcode_info;
    private String shop_id;
    private String table_num;
    private TextView tv_shoptitle;
    private TextView tv_name;
    private TextView tv_phone;
    private ListView lv_indoor_product;

    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ShopInfo");
    final private DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users");
    final private DatabaseReference productReference = FirebaseDatabase.getInstance().getReference("Meals_list");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order);

        tv_shoptitle = findViewById(R.id.tv_shoptitle);
        tv_name = findViewById(R.id.tv_name);
        tv_phone = findViewById(R.id.tv_phone);
        lv_indoor_product = findViewById(R.id.lv_indoor_product);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        List<ProductInfo> ls_product = new ArrayList<>();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            qrcode_info = bundle.getString(QRcode_scanner.QRCODE_ID_VALUE);
            int index = qrcode_info.indexOf("@@@");
            if (index == -1) {
                shop_id = qrcode_info;
                table_num = "無桌號";
            } else {
                shop_id = qrcode_info.substring(0, index);
                table_num = qrcode_info.substring(index+3);
                Log.d("shop", shop_id);
                Log.d("table", table_num);
            }
        }

        databaseReference.child(shop_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    ShopInfo info = snapshot.getValue(ShopInfo.class);

                    tv_shoptitle.setText(table_num + "(" + info.getName() + ")");
                } else{
                    Toast.makeText(OrderActivity.this, "No data found", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String user_info = snapshot.getValue().toString();

                    String phone = extractValue(user_info, "phone");
                    String name = extractValue(user_info, "name");

                    tv_name.setText(name);
                    tv_phone.setText(phone);

                } else{
                    Toast.makeText(OrderActivity.this, "No data found", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        productReference.child(shop_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        ProductInfo info = dataSnapshot.getValue(ProductInfo.class);

                        if(info != null){
                            ls_product.add(info);
                        }
                    }
                    ProductInfoAdapter adapter = new ProductInfoAdapter(OrderActivity.this, ls_product);
                    lv_indoor_product.setAdapter(adapter);
                } else{
                    Toast.makeText(OrderActivity.this, "No data found", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    static String extractValue(String input, String key) {
        String keyWithEquals = key + "=";
        int startIndex = input.indexOf(keyWithEquals);
        if (startIndex == -1) {
            return null;
        }
        startIndex += keyWithEquals.length();
        int endIndex = input.indexOf(',', startIndex);
        if (endIndex == -1) {
            endIndex = input.indexOf('}', startIndex);
        }
        return input.substring(startIndex, endIndex).trim();
    }

}