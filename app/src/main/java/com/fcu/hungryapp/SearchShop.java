package com.fcu.hungryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class SearchShop extends AppCompatActivity {
    public static final String SHOP_ID_VALUE = "shopgetvaluecheck";
    private Uri imgURI;
    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ShopInfo");
    final private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;

    private ListView lv_shop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_shop);

        lv_shop = findViewById(R.id.lv_shop);

        List<ShopInfo> shops = new ArrayList<>();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        ShopInfo info = dataSnapshot.getValue(ShopInfo.class);

                        if(info != null){
                            Log.d("check info", info.getImage().toString());
                            shops.add(info);
                        }
                    }
                } else{
                    Toast.makeText(SearchShop.this, "No data found", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ShopInfoAdapter adapter = new ShopInfoAdapter(SearchShop.this, shops);
        lv_shop.setAdapter(adapter);

        lv_shop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShopInfo select = shops.get(position);
                String shop_id = select.getShop_id();

                Intent intent = new Intent(SearchShop.this, SeatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(SHOP_ID_VALUE, shop_id);
                intent.putExtras(bundle);

                startActivity(intent);
                finish();
            }
        });
    }
}