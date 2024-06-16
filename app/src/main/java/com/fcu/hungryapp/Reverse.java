package com.fcu.hungryapp;

public class Reverse {

    private String adult;
    private String chair;
    private String child;
    private String customer_email;
    private String customer_id;
    private String customer_name;
    private String customer_phone;
    private String dine_date;
    private String dine_time;
    private String reverse_id;
    private String shop_id;

    public Reverse() {
    }

    public Reverse(String adult, String chair, String child, String customer_email, String customer_id, String customer_name, String customer_phone, String dine_date, String dine_time, String reverse_id, String shop_id) {
        this.adult = adult;
        this.chair = chair;
        this.child = child;
        this.customer_email = customer_email;
        this.customer_id = customer_id;
        this.customer_name = customer_name;
        this.customer_phone = customer_phone;
        this.dine_date = dine_date;
        this.dine_time = dine_time;
        this.reverse_id = reverse_id;
        this.shop_id = shop_id;
    }

    public String getDine_date() {
        return dine_date;
    }

    public void setDine_date(String dine_date) {
        this.dine_date = dine_date;
    }

    public String getAdult() {
        return adult;
    }

    public void setAdult(String adult) {
        this.adult = adult;
    }

    public String getChair() {
        return chair;
    }

    public void setChair(String chair) {
        this.chair = chair;
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    public String getDine_time() {
        return dine_time;
    }

    public void setDine_time(String dine_time) {
        this.dine_time = dine_time;
    }

    public String getReverse_id() {
        return reverse_id;
    }

    public void setReverse_id(String reverse_id) {
        this.reverse_id = reverse_id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }
}
