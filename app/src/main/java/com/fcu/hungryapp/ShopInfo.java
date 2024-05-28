package com.fcu.hungryapp;

import android.net.Uri;

public class ShopInfo {
    private String image;
    private String name;
    private String start_time;
    private String end_time;

    public ShopInfo() {
    }

    public ShopInfo(String image, String name, String start_time, String end_time) {
        this.image = image;
        this.name = name;
        this.start_time = start_time;
        this.end_time = end_time;
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
}
