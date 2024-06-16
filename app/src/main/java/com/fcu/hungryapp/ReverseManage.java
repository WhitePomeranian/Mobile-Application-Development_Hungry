package com.fcu.hungryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ReverseManage extends AppCompatActivity implements ReversesInfoAdapter.OnItemRemovedListener {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference reverseDatabaseRef;

    private Spinner spReverseDate;
    private ListView lvShopReverses;

    private List<Reverse> dataList = new ArrayList<>();

    private String selectDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reverse_manage);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        spReverseDate = findViewById(R.id.sp_reverse_date);
        lvShopReverses = findViewById(R.id.lv_shop_reverses);


        List<String> dateList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        for (int i = 0; i < 31; i++) {
            dateList.add(sdf.format(calendar.getTime()));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        ArrayAdapter<String> dateAdapter = new ArrayAdapter<>(ReverseManage.this, android.R.layout.simple_spinner_item, dateList);
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spReverseDate.setAdapter(dateAdapter);

        spReverseDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectDay = parent.getItemAtPosition(position).toString();
                queryReverse();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spReverseDate.setSelection(0);
        spReverseDate.post(() -> spReverseDate.setSelection(0));
        selectDay = spReverseDate.getItemAtPosition(0).toString();


    }

    private void queryReverse() {
        reverseDatabaseRef = FirebaseDatabase.getInstance().getReference("shop_reverses");

        Query query = reverseDatabaseRef.orderByChild("shop_id").equalTo(user.getUid());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList = new ArrayList<>();
                for (DataSnapshot a_snapshot : snapshot.getChildren()) {
                    Reverse reverse = a_snapshot.getValue(Reverse.class);
                    if(reverse.getDine_date().equals(selectDay)) {
                        dataList.add(reverse);
                    }

                }

                ReversesInfoAdapter adapter = new ReversesInfoAdapter(ReverseManage.this, dataList, ReverseManage.this);
                lvShopReverses.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemRemoved() {
        queryReverse();
    }
}