package com.example.gooder.model;

import java.util.List;

public class ShoppingCart {
    private String shopName;
    private List<ShoppingCartItem> shoppingCartItems;
    private boolean isChoose, isExpand;

    public ShoppingCart(String shopName, List<ShoppingCartItem> shoppingCartItem){
        this.shopName = shopName;
        this.shoppingCartItems = shoppingCartItem;
        this.isChoose = false;
        this.isExpand = true;
    }

    public String getShopName() {
        return shopName;
    }
    public List<ShoppingCartItem> getShoppingCartItems() {
        return shoppingCartItems;
    }

    public boolean isChoose() {
        return isChoose;
    }
    public void setIsChoose(boolean isChoose) {
        this.isChoose = isChoose;
    }

    public boolean isExpand() {
        return isExpand;
    }
    public void setIsExpand(boolean isExpand) {
        this.isExpand = isExpand;
    }
}
