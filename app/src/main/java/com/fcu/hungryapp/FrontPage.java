package com.fcu.hungryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class FrontPage extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private EditText et_user_name;
    private EditText et_user_phone;
    private EditText et_user_email;
    private EditText et_user_password;
    private EditText et_user_check_password;
    private Button bt_logout;
    private Button btn_modify;

    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        et_user_name = findViewById(R.id.et_modify_name);
        et_user_phone = findViewById(R.id.et_modify_phone);
        et_user_email = findViewById(R.id.et_disable_email);
        et_user_email.setFocusable(false);
        et_user_password = findViewById(R.id.et_modify_password);
        et_user_check_password = findViewById(R.id.et_check_modify_password);

        bt_logout = findViewById(R.id.bt_logout);
        btn_modify = findViewById(R.id.btn_modify);

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
                    userEmail = email;

                    et_user_phone.setText(phone);
                    et_user_name.setText(name);
                    et_user_email.setText(email);

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

    public void showConfirmationDialog(View view) {
        new AlertDialog.Builder(this)
                .setTitle("確認")
                .setMessage("確定要更動個人資料嗎？")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String Password = et_user_password.getText().toString();
                        String Confirm_Password = et_user_check_password.getText().toString();
                        String Name = et_user_name.getText().toString();
                        String Phone = et_user_phone.getText().toString();

                        if(Name.isEmpty()){
                            et_user_name.setError("Name require");
                            return;
                        }
                        if(Phone.isEmpty()){
                            et_user_phone.setError("Phone number require");
                            return;
                        }
                        if(!Password.isEmpty() && Password.length()<12){
                            et_user_password.setError("Password should be >= 8 letter");
                            return;
                        }
                        if (!Password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{12,}$")) {
                            et_user_password.setError("Password should contain at least one uppercase, one lowercase, one digit, and one special character");
                            return;
                        }
                        if(!Password.isEmpty() && Confirm_Password.isEmpty()){
                            et_user_check_password.setError("Confirm Password require");
                            return;
                        }
                        if(!Password.isEmpty() && !Confirm_Password.equals(Password)){
                            et_user_check_password.setError("Confirm password not equal password");
                            return;
                        }



                        DatabaseReference userRef = databaseReference.child("users").child(user.getUid());
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("name", Name);
                        updates.put("phone", Phone);


                        userRef.updateChildren(updates)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        if(Password.isEmpty()) {
                                            Intent intent = new Intent(FrontPage.this, SearchShop.class);
                                            intent.putExtra("message", "修改成功！");
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            if(user != null) {
                                                user.updatePassword(Password)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                FirebaseAuth.getInstance().signOut();
                                                                Intent intent = new Intent(FrontPage.this, MainActivity.class);
                                                                intent.putExtra("message", "修改成功！ 請使用新密碼登入~");
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(FrontPage.this, "密碼修改失敗：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(FrontPage.this, "修改失敗：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}

