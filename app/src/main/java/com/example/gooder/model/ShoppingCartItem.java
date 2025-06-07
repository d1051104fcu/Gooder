package com.example.gooder.model;

import com.example.gooder.R;

import java.util.Objects;

public class ShoppingCartItem {
    private final String ShoppingCartId;
    private final String name;
    private final int price;
    private String imgId = String.valueOf(R.drawable.not_found);
    private int count;
    private boolean isChoose;

    public ShoppingCartItem(String ShoppingCartId, String name, int price, String imgId, int count){
        this.ShoppingCartId = ShoppingCartId;this.name = name;
        this.price = price;
        if (!imgId.isEmpty()){
            this.imgId = imgId;
        }
        this.count = count;
        this.isChoose = true;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImgId() {
        return imgId;
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

    public String getShoppingCartId() {
        return ShoppingCartId;
    }
}
