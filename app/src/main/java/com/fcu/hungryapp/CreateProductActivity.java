package com.fcu.hungryapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class CreateProductActivity extends AppCompatActivity {
    public static final String MEALS_TYPE = "mealtype";

    private Map<String, String> map = new HashMap<>();


    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference MealsDatabaseRef;

    private EditText etClass1;
    private EditText etClass2;
    private EditText etClass3;
    private EditText etClass4;
    private Button btnNextStep;
    
    private Button bt_class1;
    private Button bt_class2;
    private Button bt_class3;
    private Button bt_class4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        etClass1 = findViewById(R.id.et_class1);
        etClass2 = findViewById(R.id.et_class2);
        etClass3 = findViewById(R.id.et_class3);
        etClass4 = findViewById(R.id.et_class4);

        btnNextStep = findViewById(R.id.btn_next_step_add_food);
        
        bt_class1 = findViewById(R.id.bt_class1);
        bt_class2 = findViewById(R.id.bt_class2);
        bt_class3 = findViewById(R.id.bt_class3);
        bt_class4 = findViewById(R.id.bt_class4);




        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String class1 = etClass1.getText().toString();
                String class2 = etClass2.getText().toString();
                String class3 = etClass3.getText().toString();
                String class4 = etClass4.getText().toString();

                if(class1.isEmpty() && class2.isEmpty() && class3.isEmpty() && class4.isEmpty()) {
                    Toast.makeText(CreateProductActivity.this, "至少要有一個餐點類別!", Toast.LENGTH_SHORT).show();
                } else {
                    map.put("class1", class1);
                    map.put("class2", class2);
                    map.put("class3", class3);
                    map.put("class4", class4);

                    map.put("shop_id", user.getUid());

                    MealsDatabaseRef = FirebaseDatabase.getInstance().getReference("Meals_type");
                    MealsDatabaseRef.child(user.getUid()).setValue(map).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("Firebase", "Data sent successfully.");
                        } else {
                            Log.e("Firebase", "Data sending failed.", task.getException());
                        }
                    });
                }
                startActivity(new Intent(CreateProductActivity.this, MerchantActivity.class));
                finish();
            }
        });

        MealsDatabaseRef = FirebaseDatabase.getInstance().getReference("Meals_type");
        MealsDatabaseRef.child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    String meals = String.valueOf(task.getResult().getValue());
                    String class1 = extractValue(meals,"class1");
                    String class2 = extractValue(meals,"class2");
                    String class3 = extractValue(meals,"class3");
                    String class4 = extractValue(meals,"class4");


                    btnNextStep.setText("確認修改");
                    etClass1.setText(class1);
                    etClass2.setText(class2);
                    etClass3.setText(class3);
                    etClass4.setText(class4);

                }
            }
        });

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();
                Intent intent = new Intent(CreateProductActivity.this, CreateProductList.class);

                if(id == R.id.bt_class1){
                    intent.putExtra(MEALS_TYPE, etClass1.getText().toString());
                } else if (id == R.id.bt_class2) {
                    intent.putExtra(MEALS_TYPE, etClass2.getText().toString());
                } else if (id == R.id.bt_class3) {
                    intent.putExtra(MEALS_TYPE, etClass3.getText().toString());
                } else if (id == R.id.bt_class4) {
                    intent.putExtra(MEALS_TYPE, etClass4.getText().toString());
                }
                startActivity(intent);

            }
        };

        bt_class1.setOnClickListener(listener);
        bt_class2.setOnClickListener(listener);
        bt_class3.setOnClickListener(listener);
        bt_class4.setOnClickListener(listener);
        
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