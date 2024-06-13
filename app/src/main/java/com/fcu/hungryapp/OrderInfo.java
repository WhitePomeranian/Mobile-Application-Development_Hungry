package com.fcu.hungryapp;

public class OrderInfo {
    private String name;
    private String order_num;
    private String phone;
    private String email;
    private String order_note;
    private String order_time;
    public OrderInfo(){
    }
    public OrderInfo(String name, String order_num, String phone, String email, String order_note, String order_time) {
        this.name = name;
        this.order_num = order_num;
        this.phone = phone;
        this.email = email;
        this.order_note = order_note;
        this.order_time = order_time;
    }

    public String getName() {
        return name;
    }

    public String getOrder_num() {
        return order_num;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getOrder_note() {
        return order_note;
    }

    public String getOrder_time() {
        return order_time;
    }
}
