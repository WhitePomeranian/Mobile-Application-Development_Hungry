package com.fcu.hungryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FrontPage extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private TextView tv_user_name;
    private TextView tv_user_phone;
    private TextView tv_user_email;
    private Button bt_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        tv_user_name = findViewById(R.id.tv_user_name);
        tv_user_phone = findViewById(R.id.tv_user_phone);
        tv_user_email = findViewById(R.id.tv_user_email);

        bt_logout = findViewById(R.id.bt_logout);

        bt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        DatabaseReference reference = database.getReference("users");

        reference.child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    String user_info = String.valueOf(task.getResult().getValue());

                    String phone = extractValue(user_info, "phone");
                    String name = extractValue(user_info, "name");
                    String email = extractValue(user_info, "email");

                    tv_user_phone.setText("Phone: " + phone);
                    tv_user_name.setText("Name: " + name);
                    tv_user_email.setText("Email: " + email);

                }
                else {
                    Toast.makeText(FrontPage.this, "Get user info Error!", Toast.LENGTH_LONG).show();
                }
            }
        });



    }

    public void logout(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(FrontPage.this, MainActivity.class));
        finish();
    }

    private static String extractValue(String input, String key) {
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