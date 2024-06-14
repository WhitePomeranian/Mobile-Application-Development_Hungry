package com.fcu.hungryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.service.controls.actions.FloatAction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
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

public class SearchShop extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {



    public static final String SHOP_ID_VALUE = "shopgetvaluecheck";
    private Uri imgURI;
    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ShopInfo");
    final private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;

    private ListView lvShop;

    // drawer
    private DrawerLayout drawerLayout;
    NavigationView nvDrawer;
    private Toolbar tbSearchShop;

    //floating action button
    private FloatingActionButton fab_camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_shop);



        lvShop = findViewById(R.id.lv_shop);
        fab_camera = findViewById(R.id.fab_camera_bt);

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
                    ShopInfoAdapter adapter = new ShopInfoAdapter(SearchShop.this, shops);
                    lvShop.setAdapter(adapter);
                } else{
                    Toast.makeText(SearchShop.this, "No data found", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        lvShop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShopInfo select = shops.get(position);
                String shop_id = select.getShop_id();

                Intent intent = new Intent(SearchShop.this, SeatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(SHOP_ID_VALUE, shop_id);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });



        // drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        tbSearchShop = findViewById(R.id.tb_search_shop);
        nvDrawer = findViewById(R.id.nv_drawer);
        nvDrawer.setItemIconTintList(null);
        nvDrawer.bringToFront();
        setSupportActionBar(tbSearchShop);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(SearchShop.this, drawerLayout, tbSearchShop, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        nvDrawer.setNavigationItemSelectedListener(this);

//        nvDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(MenuItem item) {
//                switch (item.getItemId()) {
//                    case 1000069:
//                        startActivity(new Intent(SearchShop.this, FrontPage.class));
//                        drawerLayout.closeDrawer(GravityCompat.START);
//                        return true;
//                    case 1000022:
//                        startActivity(new Intent(SearchShop.this, QRcode_scanner.class));
//                        drawerLayout.closeDrawer(GravityCompat.START);
//                        return true;
//                }
//                return false;
//            }
//        });

        fab_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start activity to scan QRCode.
                Intent intent = new Intent(SearchShop.this, QRcode_scanner.class);
                startActivity(intent);
            }
        });
    }

    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.nav_personal_info) {
            Intent intent = new Intent(SearchShop.this, PersonalInfoActivity.class);
            startActivity(intent);
            return true;
        } else if(id == R.id.nav_orders) {
            Intent intent = new Intent(SearchShop.this, YourOrdersActivity.class);
            startActivity(intent);
            return true;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }
}