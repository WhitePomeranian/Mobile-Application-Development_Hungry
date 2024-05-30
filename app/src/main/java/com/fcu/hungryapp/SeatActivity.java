package com.fcu.hungryapp;

import static com.fcu.hungryapp.FrontPage.extractValue;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SeatActivity extends AppCompatActivity {
    private TextView tv_choose;
    private TextView tv_getname;
    private TextView tv_getmail;
    private TextView tv_getphonenumber;
    private String shop_id;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_seat);

        tv_choose = findViewById(R.id.tv_choose);
        tv_getname = findViewById(R.id.tv_getname);
        tv_getmail = findViewById(R.id.tv_getmail);
        tv_getphonenumber = findViewById(R.id.tv_getPhoneNumber);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            shop_id = bundle.getString(SearchShop.SHOP_ID_VALUE);
//            Toast.makeText(SeatActivity.this, shop_id, Toast.LENGTH_LONG).show();
        }

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("ShopInfo");
        DatabaseReference userReferencce = database.getReference("users");

        reference.child(shop_id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    String shop_info = String.valueOf(task.getResult().getValue());
                    Log.e("shopinfo", shop_info);
                    String Shop_name = extractValue(shop_info, "name");

                    tv_choose.setText(Shop_name + "選擇時段");
                }
            }
        });

        userReferencce.child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task != null){
                    String user_info = String.valueOf(task.getResult().getValue());

                    String phone = extractValue(user_info, "phone");
                    String name = extractValue(user_info, "name");
                    String email = extractValue(user_info, "email");

                    tv_getphonenumber.setText(phone);
                    tv_getname.setText(name);
                    tv_getmail.setText(email);
                }
            }
        });
    }
}