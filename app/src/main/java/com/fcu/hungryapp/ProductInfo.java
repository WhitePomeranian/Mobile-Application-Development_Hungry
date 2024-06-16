package com.fcu.hungryapp;

public class ProductInfo {
    private String product_image;
    private String product_name;
    private String product_price;
    private String product_describe;
    private String type;

    public ProductInfo() {
    }

    public ProductInfo(String product_image, String product_name, String product_price, String product_describe, String type) {
        this.product_image = product_image;
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_describe = product_describe;
        this.type = type;
    }

    public String getProduct_image() {
        return product_image;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_price() {
        return product_price;
    }

    public String getProduct_describe() {
        return product_describe;
    }

    public String gettype() {
        return type;
    }
}
