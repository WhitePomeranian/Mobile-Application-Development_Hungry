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

public class ProductInfoAdapter extends BaseAdapter {

    private Context context;
    private List<ProductInfo> lsProduct;

    public ProductInfoAdapter(Context context, List<ProductInfo> lsProduct){
        this.context = context;
        this.lsProduct = lsProduct;
    }

    @Override
    public int getCount() {
        return lsProduct.size();
    }

    @Override
    public Object getItem(int i) {
        return lsProduct.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.product_info_layout, viewGroup, false);
        }

        ImageView img_product_list = view.findViewById(R.id.img_product_list);
        TextView tv_product_name = view.findViewById(R.id.tv_product_name);
        TextView tv_product_price = view.findViewById(R.id.tv_product_price);
        TextView tv_product_describe = view.findViewById(R.id.tv_product_describe);
        TextView tv_type = view.findViewById(R.id.tv_type);

        ProductInfo info = lsProduct.get(i);
        Glide.with(context)
                .load(Uri.parse(info.getProduct_image().toString()))
                .into(img_product_list);

        tv_product_name.setText(info.getProduct_name());
        tv_product_price.setText("$" + info.getProduct_price());
        tv_product_describe.setText(info.getProduct_describe());
        tv_type.setText(info.gettype());

        return view;
    }
}
