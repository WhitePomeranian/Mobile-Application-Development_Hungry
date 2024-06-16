package com.fcu.hungryapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Map;

public class ReversesInfoAdapter extends ArrayAdapter<Reverse> {

    public interface OnItemRemovedListener {
        void onItemRemoved();
    }

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference reverseDatabaseRef;

    private Context mContext;
    private List<Reverse> mData;
    private OnItemRemovedListener listener;

    public ReversesInfoAdapter(Context context, List<Reverse> data, OnItemRemovedListener listener) {
        super(context, R.layout.reverses_info_layout, data);
        this.mContext = context;
        this.mData = data;
        this.listener = listener;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.reverses_info_layout, parent, false);
        }


        // 獲取列表項中的各個TextView
        TextView tvName = listItem.findViewById(R.id.tv_reverses_name);
        TextView tvEmail = listItem.findViewById(R.id.tv_reverses_email);
        TextView tvPhone = listItem.findViewById(R.id.tv_reverses_phone);
        TextView tvPeople = listItem.findViewById(R.id.tv_reverses_people);
        TextView tvChair = listItem.findViewById(R.id.tv_reverses_chair);
        Button btnRemove = listItem.findViewById(R.id.btn_remove_reverse);

        // 根據位置設置數據
        Reverse item = mData.get(position);

        String s;
        s = "姓名: " + item.getCustomer_name();
        tvName.setText(s);  // 假設這裡的item是標題，實際情況根據你的數據來設置
        s = "信箱: " + item.getCustomer_email();
        tvEmail.setText(s);
        s = "電話: " + item.getCustomer_phone();
        tvPhone.setText(s);
        String adult = item.getAdult();
        String child = item.getChild();
        s = adult.charAt(0) + "大" + child.charAt(0) + "小";
        tvPeople.setText(s);
        char chair = item.getChair().charAt(0);

        if(chair == '0') {
            s = "備註: 不須兒童椅";
        } else {
            s = "備註: 需要兒童椅 " + chair + " 張";
        }
        tvChair.setText(s);

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reverseDatabaseRef = FirebaseDatabase.getInstance().getReference("shop_reverses");

                reverseDatabaseRef.child(item.getReverse_id())
                        .removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // 移除成功
                                Toast.makeText(mContext.getApplicationContext(), "該筆訂位資訊已成功移除", Toast.LENGTH_SHORT).show();
                                if (listener != null) {
                                    listener.onItemRemoved();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // 移除失敗
                                Toast.makeText(mContext.getApplicationContext(), "移除資料失敗：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });


        return listItem;
    }
}
