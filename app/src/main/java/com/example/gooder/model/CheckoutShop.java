package com.example.gooder.model;

import java.util.List;

public class CheckoutShop {
    private final String shopName;
    private String tradeMode;
    private String address;
    private final List<CheckoutItem> checkoutItemList;

    public CheckoutShop(String shopName, List<CheckoutItem> checkoutItemList){
        this.shopName = shopName;
        this.tradeMode = "超商";
        this.address = "";
        this.checkoutItemList = checkoutItemList;
    }

    public String getShopName() {
        return shopName;
    }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getTradeMode() { return tradeMode; }
    public void setTradeMode(String tradeMode) { this.tradeMode = tradeMode; }

    public List<CheckoutItem> getCheckoutItemList() {
        return checkoutItemList;
    }

}
