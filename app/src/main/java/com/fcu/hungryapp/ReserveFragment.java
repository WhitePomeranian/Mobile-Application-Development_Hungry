package com.fcu.hungryapp;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReserveFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference shopInfodatabaseReference;


    private TextView tvShopName;
    private TextView tvShopAddress;
    private TextView tvShopPhone;

    private Spinner spAdult;


    public ReserveFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_reverse, container, false);

        tvShopName = rootView.findViewById(R.id.tv_reverse_shop_name);
        tvShopPhone = rootView.findViewById(R.id.tv_reverse_shop_phone);
        spAdult = rootView.findViewById(R.id.sp_adult);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        shopInfodatabaseReference = FirebaseDatabase.getInstance().getReference("shopInfo");

//        shopInfodatabaseReference.child(SeatActivity.getShop_id()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (task.isSuccessful()) {
//                    String user_info = String.valueOf(task.getResult().getValue());

//                    String shopName = extractValue(user_info, "name");
//                    String shopPhone = extractValue(user_info, "phone");
//
//                    tvShopName.setText(shopName);
//                    tvShopPhone.setText(shopPhone);
//
//                }
//            }
//        });

        String[] spinnerAdultItems = {"1位大人", "2位大人", "3位大人", "4位大人", "5位大人", "6位大人", "7位大人", "8位大人"
            , "9位大人", "10位大人"};

        ArrayAdapter<String> adultAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, spinnerAdultItems);

        adultAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spAdult.setAdapter(adultAdapter);
        spAdult.setBackgroundColor(Color.parseColor("#D3D3D3"));

        return rootView;
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
