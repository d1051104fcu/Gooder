package com.example.gooder.model;

import java.util.List;

public class ShoppingCartShop {
    private final String shopName, sellerId;
    final private List<ShoppingCartItem> shoppingCartItemList;
    private boolean isChoose, isExpand;

    public ShoppingCartShop(String shopName, String sellerId, List<ShoppingCartItem> shoppingCartItem){
        this.shopName = shopName;
        this.sellerId = sellerId;
        this.shoppingCartItemList = shoppingCartItem;
        this.isChoose = true;
        this.isExpand = true;
    }

    public String getShopName() {
        return shopName;
    }

    public String getSellerId() {
        return sellerId;
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
