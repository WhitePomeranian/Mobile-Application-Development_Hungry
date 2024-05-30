package com.fcu.hungryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MerchantActivity extends AppCompatActivity {

    private Button btnReservationList;
    private Button btnOrderDetails;

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

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                if(id == R.id.btn_reservation_list) {
                    Intent intent = new Intent(MerchantActivity.this, BookingInfoActivity.class);
                    startActivity(intent);
                } else if(id == R.id.btn_order_details) {
                    Intent intent = new Intent(MerchantActivity.this, OrderDetailsActivity.class);
                    startActivity(intent);
                }
            }
        };

        btnReservationList.setOnClickListener(listener);
        btnOrderDetails.setOnClickListener(listener);
    }
}