package com.example.gooder.model;

import com.example.gooder.R;

public class CheckoutItem {
    private final String name;
    private final int price;
    private int imgId = R.drawable.not_found;;
    private final int count;

    public CheckoutItem(String name, int price, int imgId, int count){
        this.name = name;
        this.price = price;
        if (imgId != 0){
            this.imgId = imgId;
        }
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getImgId() {
        return imgId;
    }

    public int getCount() {
        return count;
    }
}
