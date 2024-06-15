package com.fcu.hungryapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class SeatActivity extends AppCompatActivity {
//    private Spinner spin_all_timing;
//    private TextView tv_choose;
//    private TextView tv_getname;
//    private TextView tv_getmail;
//    private TextView tv_getphonenumber;
//    private EditText et_num;
//    private EditText et_note;
//    private Button bt_submit;
//    private String shop_id;
//    private String order_time;
//    private String userinfo;
//    private String phone;
//    private String name;
//    private String email;
//    private Boolean check_spinner = false;
    public String shop_id;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;

    public int order_count;

    private TabLayout tlMain;
    private ViewPager2 vp2Main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_seat);


        Intent intent = getIntent();
        String shopId = intent.getStringExtra(SearchShop.SHOP_ID_VALUE);


        tlMain = findViewById(R.id.tl_main);
        vp2Main = findViewById(R.id.vp2_main);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, shopId);
        vp2Main.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(tlMain, vp2Main,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        View tabView = LayoutInflater.from(SeatActivity.this).inflate(R.layout.tab_item, null);
                        TextView tabText = tabView.findViewById(R.id.tv_tab_item_title);
                        switch (position) {
                            case 0:
                                tabText.setText("訂位");
                                break;
                            case 1:
                                tabText.setText("訂餐");
                                break;
                        }
                        tab.setCustomView(tabView);
                    }
                }).attach();



//        spin_all_timing = findViewById(R.id.spin_all_timing);
//        tv_choose = findViewById(R.id.tv_choose);
//        tv_getname = findViewById(R.id.tv_getname);
//        tv_getmail = findViewById(R.id.tv_getmail);
//        tv_getphonenumber = findViewById(R.id.tv_getPhoneNumber);
//        et_num = findViewById(R.id.et_num);
//        et_note = findViewById(R.id.et_note);
//        bt_submit = findViewById(R.id.bt_submit);
//
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        ArrayAdapter<CharSequence> adapter =
//                ArrayAdapter.createFromResource(SeatActivity.this, R.array.seat, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spin_all_timing.setAdapter(adapter);
//
//        Bundle bundle = getIntent().getExtras();
//        if(bundle != null){
//            shop_id = bundle.getString(SearchShop.SHOP_ID_VALUE);
//            Toast.makeText(SeatActivity.this, shop_id, Toast.LENGTH_LONG).show();
//        }
//
//        auth = FirebaseAuth.getInstance();
//        user = auth.getCurrentUser();
//        database = FirebaseDatabase.getInstance();
//        DatabaseReference reference = database.getReference("ShopInfo");
//        DatabaseReference userReferencce = database.getReference("users");
//
//
//        reference.child(shop_id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if(task.isSuccessful()){
//                    String shop_info = String.valueOf(task.getResult().getValue());
//                    Log.e("shopinfo", shop_info);
//                    String Shop_name = extractValue(shop_info, "name");
//
//                    tv_choose.setText(Shop_name + "選擇時段");
//                }
//            }
//        });
//
//        userReferencce.child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if(task != null){
//                    String user_info = String.valueOf(task.getResult().getValue());
//
//                    phone = extractValue(user_info, "phone");
//                    name = extractValue(user_info, "name");
//                    email = extractValue(user_info, "email");
//
//                    tv_getphonenumber.setText(phone);
//                    tv_getname.setText(name);
//                    tv_getmail.setText(email);
//                }
//            }
//        });
//
//        spin_all_timing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if(position == 0){
//                    bt_submit.setVisibility(View.GONE);
//                    return;
//                }
//                order_time = parent.getItemAtPosition(position).toString();
//                check_spinner = true;
//
//                if(et_num.getText().toString().length() != 0){
//                    Log.e("edittext", String.valueOf(et_num.getText().toString().length()));
//                    bt_submit.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                bt_submit.setVisibility(View.GONE);
//            }
//        });
//
//        et_num.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if(et_num.getText().toString().length() != 0 && check_spinner){
//                    bt_submit.setVisibility(View.VISIBLE);
//                } else{
//                    bt_submit.setVisibility(View.GONE);
//                }
//            }
//        });
//
//        bt_submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String order_num;
//                String order_note = "";
//
//                order_num = et_num.getText().toString();
//                if(et_note.getText().toString() != ""){
//                    order_note = et_note.getText().toString();
//                }
//
//                uploadToFirebase(order_time, phone, name, email, order_num, order_note);
//
//            }
//        });
//    }
//
//    private void uploadToFirebase(String orderTime, String phone, String name, String email, String orderNum, String orderNote) {
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("order_time", orderTime);
//        map.put("phone", phone);
//        map.put("name", name);
//        map.put("email", email);
//        map.put("order_num", orderNum);
//        map.put("order_note", orderNote);
//
//        DatabaseReference checkReference = database.getReference("OrderInfo").child(shop_id);
//        DatabaseReference orderReference = database.getReference("OrderInfo");
//        order_count = 1;
//        checkReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                DataSnapshot dataSnapshot = task.getResult();
//                if (dataSnapshot.exists()) {
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        String key = snapshot.getKey();
//                        Log.e("first key order", "Key:" + key + "order" + String.valueOf(order_count));
//                        if(String.valueOf(order_count).equals(key)){
//                            order_count++;
//                            Log.e("key order", "Key:" + key + "order" + String.valueOf(order_count));
//                        }
//                    }
//                    Log.e("orderReference...", String.valueOf(order_count));
//                    orderReference.child(shop_id).child(String.valueOf(order_count)).setValue(map);
//                    Toast.makeText(SeatActivity.this, "order success", Toast.LENGTH_LONG).show();
//
//                    Intent intent = new Intent(SeatActivity.this, MerchantActivity.class);//finish order page(Thank for order)
//                    startActivity(intent);
//                    finish();
//                } else {
//                    Log.d("FirebaseData", "No data found");
//                    orderReference.child(shop_id).child("1").setValue(map);
//                    Toast.makeText(SeatActivity.this, "order success", Toast.LENGTH_LONG).show();
//
//                    Intent intent = new Intent(SeatActivity.this, MerchantActivity.class);//finish order page(Thank for order)
//                    startActivity(intent);
//                    finish();
//                }
//            }
//        });
    }
    public String getShop_id(){
        return shop_id;
    }
}