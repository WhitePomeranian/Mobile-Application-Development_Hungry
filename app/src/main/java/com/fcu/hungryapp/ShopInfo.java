package com.fcu.hungryapp;

import android.net.Uri;

public class ShopInfo {
    private String image;
    private String name;
    private String start_time;
    private String end_time;
    private String shop_id;

    public ShopInfo() {
    }

    public ShopInfo(String image, String name, String start_time, String end_time, String shop_id) {
        this.image = image;
        this.name = name;
        this.start_time = start_time;
        this.end_time = end_time;
        this.shop_id = shop_id;
    }
    public Uri getImage() {
        return Uri.parse(image);
    }

    public String getName() {
        return name;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }
    public String getShop_id(){
        return shop_id;
    }
}
