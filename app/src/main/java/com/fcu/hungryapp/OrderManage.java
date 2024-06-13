package com.fcu.hungryapp;

import static com.fcu.hungryapp.FrontPage.extractValue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderManage extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseUser user;
    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("OrderInfo");
    private ListView lv_orderready;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_manage);

        lv_orderready = findViewById(R.id.lv_orderready);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        List<OrderInfo> orders = new ArrayList<>();

        databaseReference.child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot dataSnapshot = task.getResult();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String key = snapshot.getKey();
                        OrderInfo value = snapshot.getValue(OrderInfo.class);
                        Log.e("snapshot", key + ",," + value);

                        orders.add(value);
                    }
                    OrderInfoAdapter adapter = new OrderInfoAdapter(OrderManage.this, orders);
                    lv_orderready.setAdapter(adapter);
                }
            }
        });
    }

}