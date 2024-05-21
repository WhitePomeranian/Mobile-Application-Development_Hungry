package com.fcu.hungryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fcu.hungryapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private TextView tvLoginRegister;

    //Firebase authentication
    public FirebaseAuth auth;
    private EditText et_login_password;
    private EditText et_login_email;
    private Button btn_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_login_email = findViewById(R.id.et_login_email);
        et_login_password = findViewById(R.id.et_login_password);
        btn_login = findViewById(R.id.btn_login);

        tvLoginRegister = findViewById(R.id.tv_login_register);

        //firebase authentication login
        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() != null){
            startActivity(new Intent(MainActivity.this, FrontPage.class));
        }

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = et_login_email.getText().toString();
                String Password = et_login_password.getText().toString();

                if(Email.isEmpty()){
                    et_login_email.setError("Email require");
                    return;
                }
                if(Password.isEmpty()){
                    et_login_password.setError("Password require");
                    return;
                }

                auth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Login...", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, FrontPage.class));

                        } else{
                            Toast.makeText(MainActivity.this, "User not found or Password incorrect", Toast.LENGTH_LONG).show();
                            et_login_password.setText("");
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
}