package com.fcu.hungryapp;

import static com.fcu.hungryapp.FrontPage.extractValue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private TextView tvLoginRegister;

    //Firebase authentication
    public FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private EditText etLoginPassword;
    private EditText etLoginEmail;
    private Button btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String message = getIntent().getStringExtra("message");
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

        etLoginEmail = findViewById(R.id.et_login_email);
        etLoginPassword = findViewById(R.id.et_login_password);
        btnLogin = findViewById(R.id.btn_login);

        tvLoginRegister = findViewById(R.id.tv_login_register);

        //firebase authentication login
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        if(user != null){
            CheckUserType();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = etLoginEmail.getText().toString();
                String Password = etLoginPassword.getText().toString();

                if(Email.isEmpty()){
                    etLoginEmail.setError("Email require");
                    return;
                }
                if(Password.isEmpty()){
                    etLoginPassword.setError("Password require");
                    return;
                }

                auth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Login...", Toast.LENGTH_SHORT).show();
                            user = auth.getCurrentUser();
                            if(user != null){
                              CheckUserType();
                            }

                        } else{
                            Toast.makeText(MainActivity.this, "User not found or Password incorrect", Toast.LENGTH_LONG).show();
                            etLoginPassword.setText("");
                        }
                    }
                });
            }
        });

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true); // 設置底線
                ds.setColor(Color.parseColor("#4BB2F9"));
            }
        };
        SpannableString spannableString = new SpannableString(tvLoginRegister.getText().toString());
        spannableString.setSpan(clickableSpan, 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE); // 設置監聽器並包含頭尾兩字
        tvLoginRegister.setText(spannableString);
        tvLoginRegister.setMovementMethod(LinkMovementMethod.getInstance()); // 使TextView中的超連結能夠被點擊

    }

    private void CheckUserType() {
        DatabaseReference reference = database.getReference("users");
        reference.child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    String user_info = String.valueOf(task.getResult().getValue());
                    Log.e("user info check", user_info);
                    String isShop = extractValue(user_info, "isShop");

                    if(isShop.equals("true")){
                        startActivity(new Intent(MainActivity.this, MerchantActivity.class));
//                        finish();
                    } else{
                        startActivity(new Intent(MainActivity.this, SearchShop.class));
                        finish();
                    }
                } else{
                    Toast.makeText(MainActivity.this, "Get user info Error!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}