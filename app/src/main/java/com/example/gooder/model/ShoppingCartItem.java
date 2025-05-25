package com.example.gooder.model;

public class ShoppingCartItem {
    private String name;
    private String price;
    private int imgId;
    private int count;
    private boolean isChoose;

    public ShoppingCartItem(String name, String price, int imgId, int count){
        this.name = name;
        this.price = price;
        this.imgId = imgId;
        this.count = count;
        this.isChoose = false;
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
    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }

    public boolean isChoose() {
        return isChoose;
    }
    public void setIsChoose(boolean isChoose) {
        this.isChoose = isChoose;
    }
}
