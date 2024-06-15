package com.fcu.hungryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.DataOutputStream;

public class MerchantActivity extends AppCompatActivity {

    private Button btnReservationList;
    private Button btnOrderDetails;
    private Button btncreateqrcode;
    private Button btncreateproduct;
    private Button btnShopInfo;

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
        btnShopInfo = findViewById(R.id.btn_shop_info);

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
                    Intent intent = new Intent(MerchantActivity.this, CreateQRcode.class);
                    startActivity(intent);
                } else if (id == R.id.btn_create_product) {
//                    Intent intent = new Intent(MerchantActivity.this, CreateProduct.class);
//                    startActivity(intent);
                } else if(id == R.id.btn_shop_info) {
                    Intent intent = new Intent(MerchantActivity.this, CreateShopActivity.class);
                    startActivity(intent);
                }
            }
        };

        btnReservationList.setOnClickListener(listener);
        btnOrderDetails.setOnClickListener(listener);
        btncreateqrcode.setOnClickListener(listener);
        btncreateproduct.setOnClickListener(listener);
        btnShopInfo.setOnClickListener(listener);
    }
}