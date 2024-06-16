package com.fcu.hungryapp;

import static androidx.browser.customtabs.CustomTabsClient.getPackageName;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ReserveFragment extends Fragment {

    private String userName = "";
    private String userPhone = "";

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference shopInfodatabaseReference;
    private DatabaseReference reverseDatabaseRef;


    Map<String, String> map = new HashMap<>();


    private TextView tvShopName;
    private TextView tvShopAddress;
    private TextView tvShopPhone;

    private Spinner spAdult;
    private Spinner spChild;
    private Spinner spDineDate;
    private Spinner spChair;

    private List<ToggleButton> toggleButtons;

    private Button btnSave;

    public ReserveFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_reverse, container, false);

        tvShopName = rootView.findViewById(R.id.tv_reverse_shop_name);
        tvShopPhone = rootView.findViewById(R.id.tv_reverse_shop_phone);
        tvShopAddress = rootView.findViewById(R.id.tv_reverse_shop_address);
        spAdult = rootView.findViewById(R.id.sp_reserve_meal_time);
        spChild = rootView.findViewById(R.id.sp_child);
        spDineDate = rootView.findViewById(R.id.sp_dine_date);
        spChair = rootView.findViewById(R.id.sp_chair);
        btnSave = rootView.findViewById(R.id.btn_save_reverse);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        shopInfodatabaseReference = FirebaseDatabase.getInstance().getReference("ShopInfo");


        String shop_id = "";
        Bundle bundle = getArguments();
        if (bundle != null) {
            shop_id = bundle.getString(SearchShop.SHOP_ID_VALUE);
        }


        DatabaseReference userRef = database.getReference("users").child(user.getUid());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    userName = dataSnapshot.child("name").getValue(String.class);
                    userPhone = dataSnapshot.child("phone").getValue(String.class);

                    map.put("customer_id", user.getUid());
                    map.put("customer_name", userName);
                    map.put("customer_email", user.getEmail());
                    map.put("customer_phone", userPhone);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        map.put("shop_id", shop_id);


        shopInfodatabaseReference.child(shop_id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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

                    tvShopName.setText(shopName);
                    tvShopPhone.setText(shopPhone);
                    tvShopAddress.setText(shopAddress);

                }
            }
        });

        String[] spinnerAdultItems = {"1位大人", "2位大人", "3位大人", "4位大人", "5位大人", "6位大人", "7位大人", "8位大人"
            , "9位大人", "10位大人"};

        ArrayAdapter<String> adultAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, spinnerAdultItems);

        adultAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spAdult.setAdapter(adultAdapter);

        spAdult.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                map.put("adult", parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String[] spinnerChildItems = {"0位小孩", "1位小孩", "2位小孩", "3位小孩", "4位小孩", "5位小孩"};

        ArrayAdapter<String> childAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, spinnerChildItems);

        childAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spChild.setAdapter(childAdapter);

        spChild.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                map.put("child", parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> dateList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        for (int i = 0; i < 31; i++) {
            dateList.add(sdf.format(calendar.getTime()));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        ArrayAdapter<String> dateAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, dateList);
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDineDate.setAdapter(dateAdapter);

        String[] spinnerChairItems = {"0張", "1張", "2張", "3張", "4張", "5張"};

        ArrayAdapter<String> chairAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, spinnerChairItems);
        chairAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spChair.setAdapter(chairAdapter);

        spChair.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                map.put("chair", parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        toggleButtons = new ArrayList<>();
        for (int hour = 11; hour <= 21; hour++) {
            for (int minute = 0; minute <= 30; minute += 30) {
                String buttonId = "tb_" + String.format("%02d", hour) + String.format("%02d", minute);
                int resId = getResources().getIdentifier(buttonId, "id", getActivity().getPackageName());
                ToggleButton temp = rootView.findViewById(resId);
                String t = String.format("%02d", hour) + ":" + String.format("%02d", minute);
                temp.setText(t);
                temp.setTextOff(t); // 設置為空字符串，不顯示 "OFF"
                temp.setTextOn(t);  // 設置為空字符串，不顯示 "ON"
                toggleButtons.add(rootView.findViewById(resId));
                if(hour == 21) {
                    break;
                }
            }
        }

        for (ToggleButton toggleButton : toggleButtons) {
            toggleButton.setOnClickListener(this::onToggleClicked);
            // 設置初始狀態的文字顏色
            toggleButton.setTextColor(Color.parseColor("#000000"));
            toggleButton.setTypeface(Typeface.create("bold_font", Typeface.BOLD));
            toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        map.put("dine_time", buttonView.getText().toString());
                    } else {

                    }
                }
            });
        }




        Calendar now = Calendar.getInstance();
        int currentHour = now.get(Calendar.HOUR_OF_DAY);
        int currentMinute = now.get(Calendar.MINUTE);

        spDineDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 String selectedDate = dateList.get(position);
                 map.put("dine_date", parent.getItemAtPosition(position).toString());
                 boolean isToday = selectedDate.equals(sdf.format(Calendar.getInstance().getTime()));

                 // 清除舊的按鈕狀態
                 for (ToggleButton toggleButton : toggleButtons) {
                     toggleButton.setEnabled(true);
                     toggleButton.setTextColor(Color.parseColor("#000000"));
                     toggleButton.setTypeface(Typeface.create("bold_font", Typeface.BOLD));
                 }

                 for (int hour = 11; hour <= 21; hour++) {
                     for (int minute = 0; minute <= 30; minute += 30) {
                         String buttonId = "tb_" + String.format("%02d", hour) + String.format("%02d", minute);
                         int resId = getResources().getIdentifier(buttonId, "id", getActivity().getPackageName());
                         ToggleButton temp = rootView.findViewById(resId);
                         String t = String.format("%02d", hour) + ":" + String.format("%02d", minute);
                         temp.setText(t);
                         temp.setTextOff(t); // 設置為空字符串，不顯示 "OFF"
                         temp.setTextOn(t);  // 設置為空字符串，不顯示 "ON"

                         if (isToday) {
                             Calendar allowedTime = Calendar.getInstance();
                             allowedTime.add(Calendar.HOUR_OF_DAY, 3);
                             int allowedHour = allowedTime.get(Calendar.HOUR_OF_DAY);
                             int allowedMinute = allowedTime.get(Calendar.MINUTE);

                             if (hour < allowedHour || (hour == allowedHour && minute < allowedMinute)) {
                                 temp.setEnabled(false);
                                 temp.setTextColor(Color.parseColor("#FFFFFF")); // 設置為灰色
                             } else {
                                 temp.setEnabled(true);
                                 temp.setTextColor(Color.parseColor("#000000"));
                                 temp.setTypeface(Typeface.create("bold_font", Typeface.BOLD));
                             }
                         } else {
                             temp.setEnabled(true);
                             temp.setTextColor(Color.parseColor("#000000"));
                             temp.setTypeface(Typeface.create("bold_font", Typeface.BOLD));
                         }

                         toggleButtons.add(temp);

                         if (hour == 21) {
                             break;
                         }
                     }
                 }
             }

             @Override
             public void onNothingSelected(AdapterView<?> parent) {

             }
        });

        // 手動觸發 onItemSelected 以設置初始狀態
        // 預設選擇當日
        String today = sdf.format(Calendar.getInstance().getTime());
        int todayPosition = dateAdapter.getPosition(today);
        spDineDate.setSelection(todayPosition);
        spDineDate.post(() -> spDineDate.setSelection(todayPosition));

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reverseDatabaseRef = FirebaseDatabase.getInstance().getReference("shop_reverses");
                String reverse_id = reverseDatabaseRef.push().getKey();
                map.put("reverse_id", reverse_id);
                reverseDatabaseRef.child(reverse_id).setValue(map).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Firebase", "Data sent successfully.");
                    } else {
                        Log.e("Firebase", "Data sending failed.", task.getException());
                    }
                });

                Intent intent = new Intent(getActivity(), SearchShop.class);
                intent.putExtra("reverse_success", true);
                startActivity(intent);


            }
        });




        return rootView;
    }

    public void onToggleClicked(View view) {
        ToggleButton clickedButton = (ToggleButton) view;
        if (clickedButton.isChecked()) {
            for (ToggleButton toggleButton : toggleButtons) {
                if (toggleButton != clickedButton) {
                    toggleButton.setChecked(false);
                    if(toggleButton.isEnabled()) {
                        toggleButton.setTextColor(Color.parseColor("#000000"));
                    } else {
                        toggleButton.setTextColor(Color.parseColor("#FFFFFF"));
                    }
                }
            }
            // 設置選中狀態的文字顏色
            clickedButton.setTextColor(Color.parseColor("#4BB2F9"));
        } else {
            // 設置未選中狀態的文字顏色
            clickedButton.setTextColor(Color.parseColor("#000000"));
        }
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
