package com.fcu.hungryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class OrderInfoAdapter extends BaseAdapter {
    private Context context;
    private List<OrderInfo> lsorder;

    public OrderInfoAdapter(Context context, List<OrderInfo> lsorder) {
        this.context = context;
        this.lsorder = lsorder;
    }


    @Override
    public int getCount() {
        return lsorder.size();
    }

    @Override
    public Object getItem(int position) {
        return lsorder.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.order_info, parent, false);
        }

        TextView tv_ordername = convertView.findViewById(R.id.tv_ordername);
        TextView tv_ordernum = convertView.findViewById(R.id.tv_ordernum);
        TextView tv_orderphone = convertView.findViewById(R.id.tv_orderphone);
        TextView tv_orderemail = convertView.findViewById(R.id.tv_orderemail);
        TextView tv_ordernote = convertView.findViewById(R.id.tv_ordernote);
        TextView tv_ordertime = convertView.findViewById(R.id.tv_ordertime);

        OrderInfo info = lsorder.get(position);
        tv_ordername.setText(info.getName());
        tv_ordernum.setText(info.getOrder_num());
        tv_orderphone.setText(info.getPhone());
        tv_orderemail.setText(info.getEmail());
        tv_ordernote.setText(info.getOrder_note());
        tv_ordertime.setText(info.getOrder_time());

        return convertView;
    }
}
