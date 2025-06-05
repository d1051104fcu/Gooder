package com.example.gooder.model;

import java.util.List;

public class ShoppingCartShop {
    private final String shopName;
    final private List<ShoppingCartItem> shoppingCartItemList;
    private boolean isChoose, isExpand;

    public ShoppingCartShop(String shopName, List<ShoppingCartItem> shoppingCartItem){
        this.shopName = shopName;
        this.shoppingCartItemList = shoppingCartItem;
        this.isChoose = true;
        this.isExpand = true;
    }

    public String getShopName() {
        return shopName;
    }
    public List<ShoppingCartItem> getShoppingCartItemList() {
        return shoppingCartItemList;
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
