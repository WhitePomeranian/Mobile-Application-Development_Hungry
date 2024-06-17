package com.fcu.hungryapp;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private TextView tvBookingShopName;
    private TextView tvBookingShopAddress;
    private TextView tvBookingShopPhone;
    private Spinner spBookingTime;


    private ToggleButton tbClass1;
    private ToggleButton tbClass2;
    private ToggleButton tbClass3;
    private ToggleButton tbClass4;

    private TextView tvClass1;
    private TextView tvClass2;
    private TextView tvClass3;
    private TextView tvClass4;


    private View viewc2;
    private View viewc3;
    private View viewc4;
    private View vhc2;
    private View vhc3;
    private View vhc4;

    private ListView lv1;
    private ListView lv2;
    private ListView lv3;
    private ListView lv4;


    private String shop_id = "";

    private List<Product> dataList1 = new ArrayList<>();
    private List<Product> dataList2 = new ArrayList<>();
    private List<Product> dataList3 = new ArrayList<>();
    private List<Product> dataList4 = new ArrayList<>();

    public OrderFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_order, container, false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();


        tvBookingShopName = rootView.findViewById(R.id.tv_booking_shop_name);
        tvBookingShopAddress = rootView.findViewById(R.id.tv_booking_shop_address);
        tvBookingShopPhone = rootView.findViewById(R.id.tv_booking_shop_phone);

        spBookingTime = rootView.findViewById(R.id.sp_booking_time);

        tbClass1 = rootView.findViewById(R.id.tb_class1);
        tbClass2 = rootView.findViewById(R.id.tb_class2);
        tbClass3 = rootView.findViewById(R.id.tb_class3);
        tbClass4 = rootView.findViewById(R.id.tb_class4);

        tvClass1 = rootView.findViewById(R.id.tv_class1);
        tvClass2 = rootView.findViewById(R.id.tv_class2);
        tvClass3 = rootView.findViewById(R.id.tv_class3);
        tvClass4 = rootView.findViewById(R.id.tv_class4);


        viewc2 = rootView.findViewById(R.id.viewc2);
        viewc3 = rootView.findViewById(R.id.viewc3);
        viewc4 = rootView.findViewById(R.id.viewc4);

        vhc2 = rootView.findViewById(R.id.vhc2);
        vhc3 = rootView.findViewById(R.id.vhc3);
        vhc4 = rootView.findViewById(R.id.vhc4);

        lv1 = rootView.findViewById(R.id.lv_class1);
        lv2 = rootView.findViewById(R.id.lv_class2);
        lv3 = rootView.findViewById(R.id.lv_class3);
        lv4 = rootView.findViewById(R.id.lv_class4);


        Bundle bundle = getArguments();
        if (bundle != null) {
            shop_id = bundle.getString(SearchShop.SHOP_ID_VALUE);
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("ShopInfo");

        databaseReference.child(shop_id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    String user_info = String.valueOf(task.getResult().getValue());

                    String shopName = extractValue(user_info, "name");
                    String shopPhone = extractValue(user_info, "phone");
                    String shopAddress = extractValue(user_info, "address");

                    shopPhone = "Tel: " + shopPhone;
                    shopAddress = "地址: " + shopAddress;

                    tvBookingShopName.setText(shopName);
                    tvBookingShopPhone.setText(shopPhone);
                    tvBookingShopAddress.setText(shopAddress);
                }
            }
        });

        String[] spinnerTimeItems = new String[21];
        int index = 0;

        for (int hour = 11; hour <= 21; hour++) {
            for (int minute = 0; minute <= 30; minute += 30) {
                String t = String.format("%02d", hour) + ":" + String.format("%02d", minute);
                spinnerTimeItems[index++] = t;
                if(hour == 21) {
                    break;
                }
            }
        }

        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, spinnerTimeItems);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBookingTime.setAdapter(timeAdapter);


        tbClass1.setTextColor(Color.parseColor("#000000"));
        tbClass1.setTypeface(Typeface.create("bold_font", Typeface.BOLD));
        tbClass2.setTextColor(Color.parseColor("#000000"));
        tbClass2.setTypeface(Typeface.create("bold_font", Typeface.BOLD));
        tbClass3.setTextColor(Color.parseColor("#000000"));
        tbClass3.setTypeface(Typeface.create("bold_font", Typeface.BOLD));
        tbClass4.setTextColor(Color.parseColor("#000000"));
        tbClass4.setTypeface(Typeface.create("bold_font", Typeface.BOLD));


        databaseReference = FirebaseDatabase.getInstance().getReference("Meals_type");
        Query query = databaseReference.orderByKey().equalTo(shop_id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String class1 = snapshot.child(shop_id).child("class1").getValue(String.class);
                    String class2 = snapshot.child(shop_id).child("class2").getValue(String.class);
                    String class3 = snapshot.child(shop_id).child("class3").getValue(String.class);
                    String class4 = snapshot.child(shop_id).child("class4").getValue(String.class);


                    tvClass1.setText(class1);
                    tbClass1.setText(class1);
                    tbClass1.setTextOff(class1);
                    tbClass1.setTextOn(class1);
                    databaseReference = FirebaseDatabase.getInstance().getReference("Meals_list");
                    Query query = databaseReference
                            .orderByKey()
                            .equalTo(shop_id);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            dataList1 = new ArrayList<>();

                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                // 遍歷每個產品
                                for (DataSnapshot productSnapshot : childSnapshot.getChildren()) {
                                    // 將每個產品轉換為 Product 物件
                                    Product product = productSnapshot.getValue(Product.class);
                                    if (product != null && product.getType().equals(class1)) {
                                        dataList1.add(product);
                                    } else if(product != null && product.getType().equals(class2)) {
                                        dataList2.add(product);
                                    } else if(product != null && product.getType().equals(class3)) {
                                        dataList3.add(product);
                                    } else if(product != null && product.getType().equals(class4)) {
                                        dataList4.add(product);
                                    }
                                }
                            }

                            ProductAdapter adapter = new ProductAdapter(getActivity(), dataList1);
                            lv1.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                    if(class2.isEmpty()) {
                        tbClass2.setVisibility(View.INVISIBLE);
                        tvClass2.setVisibility(View.INVISIBLE);
                        viewc2.setVisibility(View.INVISIBLE);
                        vhc2.setVisibility(View.INVISIBLE);
                    } else {
                        tvClass2.setText(class2);
                        tbClass2.setText(class2);
                        tbClass2.setTextOff(class2);
                        tbClass2.setTextOn(class2);

                        ProductAdapter adapter = new ProductAdapter(getActivity(), dataList2);
                        lv2.setAdapter(adapter);


                    }

                    if(class3.isEmpty()) {
                        tbClass3.setVisibility(View.INVISIBLE);
                        tvClass3.setVisibility(View.INVISIBLE);
                        viewc3.setVisibility(View.INVISIBLE);
                        vhc3.setVisibility(View.INVISIBLE);

                    } else {
                        tvClass3.setText(class3);
                        tbClass3.setText(class3);
                        tbClass3.setTextOff(class3);
                        tbClass3.setTextOn(class3);

                        ProductAdapter adapter = new ProductAdapter(getActivity(), dataList3);
                        lv3.setAdapter(adapter);
                    }


                    if(class4.isEmpty()) {
                        tbClass4.setVisibility(View.INVISIBLE);
                        tvClass4.setVisibility(View.INVISIBLE);
                        viewc4.setVisibility(View.INVISIBLE);
                        vhc4.setVisibility(View.INVISIBLE);
                    } else {
                        tvClass4.setText(class4);
                        tbClass4.setText(class4);
                        tbClass4.setTextOff(class4);
                        tbClass4.setTextOn(class4);

                        ProductAdapter adapter = new ProductAdapter(getActivity(), dataList4);
                        lv4.setAdapter(adapter);
                    }


                } else {
                    Log.d("Firebase", "UserID不存在");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });








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
