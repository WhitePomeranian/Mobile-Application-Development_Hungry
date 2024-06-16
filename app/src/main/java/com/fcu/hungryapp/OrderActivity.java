package com.fcu.hungryapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderActivity extends AppCompatActivity {
    private String qrcode_info;
    private String shop_id;
    private String table_num;
    private TextView tv_shoptitle;
    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ShopInfo");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order);

        tv_shoptitle = findViewById(R.id.tv_shoptitle);

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

                    tv_shoptitle.setText(table_num + "(" + info.getName() + ")" + "取餐時間:");
                } else{
                    Toast.makeText(OrderActivity.this, "No data found", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

/*
    private String qrcode_info;
    private String shop_id;
    private String table_num;
    private TextView tv_shoptitle;
    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ShopInfo");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order);

        tv_shoptitle = findViewById(R.id.tv_shoptitle);

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

                    tv_shoptitle.setText(table_num + "(" + info.getName() + ")" + "取餐時間:");
                } else{
                    Toast.makeText(OrderActivity.this, "No data found", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
 */