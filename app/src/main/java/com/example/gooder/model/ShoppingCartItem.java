package com.example.gooder.model;

public class ShoppingCartItem {
    private String name;
    private String price;
    private int imgId;
    private int count;
    private boolean isChoose;

    public ShoppingCartItem(String name, String price, int imgId, int count, boolean isChoose){
        this.name = name;
        this.price = price;
        this.imgId = imgId;
        this.count = count;
        this.isChoose = isChoose;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public int getImgId() {
        return imgId;
    }

    public int getCount() {
        return count;
    }

    public boolean isChoose() {
        return isChoose;
    }
}
