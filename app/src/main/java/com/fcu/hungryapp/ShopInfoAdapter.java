package com.fcu.hungryapp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ShopInfoAdapter extends BaseAdapter {
    private Context context;
    private List<ShopInfo> lsShop;

    public ShopInfoAdapter(Context context, List<ShopInfo> lsShop){
        this.context = context;
        this.lsShop = lsShop;
    }

    @Override
    public int getCount() {
        return lsShop.size();
    }

    @Override
    public Object getItem(int position) {
        return lsShop.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.shop_info_layout, parent, false);
        }

        ImageView img_shop = convertView.findViewById(R.id.img_shop);
        TextView tv_shopname = convertView.findViewById(R.id.tv_shopname);
        TextView tv_time = convertView.findViewById(R.id.tv_time);

        ShopInfo info = lsShop.get(position);
        Glide.with(context)
                .load(Uri.parse(info.getImage().toString()))
                .into(img_shop);
//        img_shop.setImageURI(Uri.parse("https://firebasestorage.googleapis.com/v0/b/hungryapp-d2994.appspot.com/o/1716904596386.jpg?alt=media&token=29fbc9a7-e45f-45ad-bf7c-3be620ecf5d7"));
        tv_shopname.setText(info.getName());
        tv_time.setText("營業時間" + info.getStart_time() + "~" + info.getEnd_time());

        return convertView;
    }
}
