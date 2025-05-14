package com.example.gooder.model;

import java.util.List;

public class ShoppingCart {
    private String shopName;
    //private List<ShoppingCartItem> shoppingCartItems;
    private boolean isChoose;

    public ShoppingCart(String shopName, boolean isChoose){
        this.shopName = shopName;
        this.isChoose = isChoose;
    }

    public String getShopName() {
        return shopName;
    }

    public boolean isChoose() {
        return isChoose;
    }
}
