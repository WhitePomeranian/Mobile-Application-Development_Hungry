package com.fcu.hungryapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PersonalReverseAdapter extends ArrayAdapter<Reverse> {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference reverseDatabaseRef;

    private Context mContext;
    private List<Reverse> mData;
    private ReversesInfoAdapter.OnItemRemovedListener listener;

    public PersonalReverseAdapter(Context context, List<Reverse> data) {
        super(context, R.layout.personal_reverse_layout, data);
        this.mContext = context;
        this.mData = data;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();


        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.personal_reverse_layout, parent, false);
        }


        // 獲取列表項中的各個TextView
        TextView tvShopName = listItem.findViewById(R.id.tv_personal_reverse_shop_name);
        TextView tvShopAddress = listItem.findViewById(R.id.tv_personal_reverse_shop_address);
        TextView tvShopPhone = listItem.findViewById(R.id.tv_personal_reverse_shop_phone);
        TextView tvShopDineTime = listItem.findViewById(R.id.tv_personal_reverse_shop_dine_time);
        TextView tvShopPeople = listItem.findViewById(R.id.tv_personal_reverse_shop_people);
        TextView tvShopChair = listItem.findViewById(R.id.tv_personal_reverse_shop_chair);

        Reverse item = mData.get(position);
        String s = "";
        String adult = item.getAdult();
        String child = item.getChild();
        s = adult.charAt(0) + "大" + child.charAt(0) + "小";
        tvShopPeople.setText(s);
        s = item.getDine_date() + " " + item.getDine_time();
        tvShopDineTime.setText(s);
        char chair = item.getChair().charAt(0);
        if(chair == '0') {
            s = "備註: 不須兒童椅";
        } else {
            s = "備註: 需要兒童椅 " + chair + " 張";
        }
        tvShopChair.setText(s);

        DatabaseReference shopInfoDatabaseRef = FirebaseDatabase.getInstance().getReference("ShopInfo");
        Query query = shopInfoDatabaseRef.orderByKey().equalTo(item.getShop_id());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // 獲取商家的名稱、地址和電話
                    String name = snapshot.child(item.getShop_id()).child("name").getValue(String.class);
                    String address = snapshot.child(item.getShop_id()).child("address").getValue(String.class);
                    String phone = snapshot.child(item.getShop_id()).child("phone").getValue(String.class);

                    String s;
                    s = "商家名稱: " + name;
                    tvShopName.setText(s);
                    s = "商家地址: " + address;
                    tvShopAddress.setText(s);
                    s = "商家電話: " + phone;
                    tvShopPhone.setText(s);
                } else {
                    Log.d("ShopInfo", "商家ID不存在");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return listItem;
    }
}
