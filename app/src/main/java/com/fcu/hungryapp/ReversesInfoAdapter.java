package com.fcu.hungryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Map;

public class ReversesInfoAdapter extends ArrayAdapter<Reverse> {

    private Context mContext;
    private List<Reverse> mData;

    public ReversesInfoAdapter(Context context, List<Reverse> data) {
        super(context, R.layout.reverses_info_layout, data);
        this.mContext = context;
        this.mData = data;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
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


        return listItem;
    }
}
