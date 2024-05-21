package com.fcu.hungryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.databind.jsontype.impl.MinimalClassNameIdResolver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private TextView tvRegisterLogin;
    private EditText etRegisterName;
    private EditText etRegisterEmail;
    private EditText etRegisterPassword;
    private EditText et_register_confirm_password;
    private EditText etRegisterPhone;
    private Button btnRegister;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etRegisterName = findViewById(R.id.et_register_name);
        etRegisterEmail = findViewById(R.id.et_register_email);
        etRegisterPassword = findViewById(R.id.et_register_password);
        et_register_confirm_password = findViewById(R.id.et_register_confirm_password);
        etRegisterPhone = findViewById(R.id.et_register_phone);
        btnRegister = findViewById(R.id.btn_register);

        //firebase authentication
        firebaseAuth = FirebaseAuth.getInstance();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = etRegisterEmail.getText().toString();
                String Password = etRegisterPassword.getText().toString();
                String Confirm_Password = et_register_confirm_password.getText().toString();
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
                    et_register_confirm_password.setError("Confirm Password require");
                    return;
                }
                if(!Confirm_Password.equals(Password)){
                    et_register_confirm_password.setError("Confirm password not equal password");
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

        reference.child(user.getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "writing success", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(RegisterActivity.this, FrontPage.class));
                } else{
                    Toast.makeText(RegisterActivity.this, "Error!!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}