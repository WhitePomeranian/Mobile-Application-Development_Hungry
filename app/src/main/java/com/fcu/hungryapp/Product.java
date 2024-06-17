package com.fcu.hungryapp;

public class Product {

    private String product_name;
    private String product_image;
    private String product_price;
    private String product_describe;
    private String type;

    private Product() {

    }

    public Product(String product_name, String product_image, String product_price, String product_describe, String type) {
        this.product_name = product_name;
        this.product_image = product_image;
        this.product_price = product_price;
        this.product_describe = product_describe;
        this.type = type;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_describe() {
        return product_describe;
    }

    public void setProduct_describe(String product_describe) {
        this.product_describe = product_describe;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
