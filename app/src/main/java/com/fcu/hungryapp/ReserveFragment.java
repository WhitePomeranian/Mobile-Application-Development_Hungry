package com.fcu.hungryapp;

import static androidx.browser.customtabs.CustomTabsClient.getPackageName;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ReserveFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference shopInfodatabaseReference;


    private TextView tvShopName;
    private TextView tvShopAddress;
    private TextView tvShopPhone;

    private Spinner spAdult;
    private Spinner spChild;
    private Spinner spDineDate;
    private Spinner spChair;

    private List<ToggleButton> toggleButtons;

    public ReserveFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_reverse, container, false);

        tvShopName = rootView.findViewById(R.id.tv_reverse_shop_name);
        tvShopPhone = rootView.findViewById(R.id.tv_reverse_shop_phone);
        spAdult = rootView.findViewById(R.id.sp_adult);
        spChild = rootView.findViewById(R.id.sp_child);
        spDineDate = rootView.findViewById(R.id.sp_dine_date);
        spChair = rootView.findViewById(R.id.sp_chair);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        shopInfodatabaseReference = FirebaseDatabase.getInstance().getReference("shopInfo");
        // 拿不到shop_id
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

        String[] spinnerChildItems = {"0位小孩", "1位小孩", "2位小孩", "3位小孩", "4位小孩", "5位小孩"};

        ArrayAdapter<String> childAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, spinnerChildItems);

        childAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spChild.setAdapter(childAdapter);

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
        }

        return rootView;
    }

    public void onToggleClicked(View view) {
        ToggleButton clickedButton = (ToggleButton) view;
        if (clickedButton.isChecked()) {
            for (ToggleButton toggleButton : toggleButtons) {
                if (toggleButton != clickedButton) {
                    toggleButton.setChecked(false);
                    toggleButton.setTextColor(Color.parseColor("#000000"));
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
