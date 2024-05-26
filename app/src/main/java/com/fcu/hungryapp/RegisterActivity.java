package com.fcu.hungryapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private TextView tvRegisterLogin;
    private EditText etRegisterName;
    private EditText etRegisterEmail;
    private EditText etRegisterPassword;
    private EditText etRegisterConfirmPassword;
    private EditText etRegisterPhone;
    private Button btnRegister;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch swUsers;
    private TextView tvRegisterName;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private Boolean isShop = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etRegisterName = findViewById(R.id.et_register_name);
        etRegisterEmail = findViewById(R.id.et_register_email);
        etRegisterPassword = findViewById(R.id.et_register_password);
        etRegisterConfirmPassword = findViewById(R.id.et_register_confirm_password);
        etRegisterPhone = findViewById(R.id.et_register_phone);
        btnRegister = findViewById(R.id.btn_register);
        swUsers = findViewById(R.id.sw_register_user);
        tvRegisterName = findViewById(R.id.tv_register_name);
        tvRegisterLogin = findViewById(R.id.tv_register_login);

        //firebase authentication
        firebaseAuth = FirebaseAuth.getInstance();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = etRegisterEmail.getText().toString();
                String Password = etRegisterPassword.getText().toString();
                String Confirm_Password = etRegisterConfirmPassword.getText().toString();
                String Name = etRegisterName.getText().toString();
                String Phone = etRegisterPhone.getText().toString();

                if(Name.isEmpty()){
                    etRegisterName.setError("Name require");
                    return;
                }
                if(Email.isEmpty()){
                    etRegisterEmail.setError("Email require");
                    return;
                }
                if(!Email.endsWith("@gmail.com")){
                    etRegisterEmail.setError("should be email address");
                    return;
                }
                if(Password.isEmpty()){
                    etRegisterPassword.setError("Password require");
                    return;
                }
                if(Password.length()<8){
                    etRegisterPassword.setError("Password should be >= 8 letter");
                    return;
                }
                if(Confirm_Password.isEmpty()){
                    etRegisterConfirmPassword.setError("Confirm Password require");
                    return;
                }
                if(!Confirm_Password.equals(Password)){
                    etRegisterConfirmPassword.setError("Confirm password not equal password");
                    return;
                }
                if(Phone.isEmpty()){
                    etRegisterPhone.setError("Phone number require");
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "User created", Toast.LENGTH_SHORT).show();
                            if(firebaseAuth.getCurrentUser() != null){
                                Toast.makeText(RegisterActivity.this, "writing data", Toast.LENGTH_SHORT).show();
                                write_data(Email, Password, Name, Phone);
                            }

//                            startActivity(new Intent(RegisterActivity.this, FrontPage.class));

                        } else{
                            Toast.makeText(RegisterActivity.this, "Error!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        swUsers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    swUsers.setText("商家");
                    tvRegisterName.setText("商店名稱");
                    etRegisterName.setHint("考魚 (僅能包含英文、數字、底線和中文)");
                    isShop = true;
                } else{
                    swUsers.setText("一般用戶");
                    tvRegisterName.setText("姓名");
                    etRegisterName.setHint("王小明 (僅能包含英文、數字、底線和中文)");
                    isShop = false;
                }
            }
        });

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                finish();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true); // 設置底線
                ds.setColor(Color.parseColor("#4BB2F9"));
            }
        };
        SpannableString spannableString = new SpannableString(tvRegisterLogin.getText().toString());
        spannableString.setSpan(clickableSpan, 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE); // 設置監聽器並包含頭尾兩字
        tvRegisterLogin.setText(spannableString);
        tvRegisterLogin.setMovementMethod(LinkMovementMethod.getInstance()); // 使TextView中的超連結能夠被點擊

    }





    private void write_data(String email, String password, String name, String phone) {

        FirebaseUser user = firebaseAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance();

        DatabaseReference reference = database.getReference("users");


        HashMap<String, Object> map = new HashMap<>();
        map.put("id", user.getUid());
        map.put("name", name);
        map.put("phone", phone);
        map.put("email", email);
        map.put("isShop", isShop);

        reference.child(user.getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "writing success", Toast.LENGTH_LONG).show();
                    if(isShop){
                        startActivity(new Intent(RegisterActivity.this, FrontPage.class));
                    } else{
                        startActivity(new Intent(RegisterActivity.this, FrontPage.class));
                    }

                } else{
                    Toast.makeText(RegisterActivity.this, "Error!!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}