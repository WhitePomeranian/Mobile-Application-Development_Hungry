package com.fcu.hungryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {

//    public interface OnItemAddListener {
//        void onItemAdd();
//    }
//
//    public interface OnItemMinorListener {
//        void onItemMinor();
//    }

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference reverseDatabaseRef;

    private Context mContext;
    private List<Product> mData;

//    private ProductAdapter.OnItemAddListener addListener;
//
//    private ProductAdapter.OnItemMinorListener minorListener;

    public ProductAdapter(Context context, List<Product> data) {
        super(context, R.layout.orders_layout, data);
        this.mContext = context;
        this.mData = data;
//        this.addListener = addListener;
//        this.minorListener = minorListener;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.orders_layout, parent, false);
        }


        TextView tvName = listItem.findViewById(R.id.tv_myproduct_name);
        TextView tvPrice = listItem.findViewById(R.id.tv_myproduct_price);
        ImageView ivProduct = listItem.findViewById(R.id.iv_product);
        EditText etNum = listItem.findViewById(R.id.et_product_num);
        Button btnAdd = listItem.findViewById(R.id.btn_add);
        Button btnMinor = listItem.findViewById(R.id.btn_minor);

        // 根據位置設置數據
        Product item = mData.get(position);

        tvName.setText(item.getProduct_name());
        String s = "$ " + item.getProduct_price();
        tvPrice.setText(s);
        Glide.with(listItem).load(item.getProduct_image()).into(ivProduct);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(etNum.getText().toString());
                if(num != 10) {
                    num++;
                    etNum.setText(String.valueOf(num));
                }
            }
        });

        btnMinor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(etNum.getText().toString());
                if(num != 0) {
                    num--;
                    etNum.setText(String.valueOf(num));
                }
            }
        });


        return listItem;
    }
}
