package com.example.gooder.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class CheckoutShop implements Parcelable {
    private final String shopName;
    private String tradeMode;
    private int freight;
    private String address;
    private final List<CheckoutItem> checkoutItemList;

    public CheckoutShop(String shopName, List<CheckoutItem> checkoutItemList){
        this.shopName = shopName;
        this.tradeMode = TradeMode.getDefaultTradeMode();
        this.freight = TradeMode.getDefaultFright();
        this.address = "";
        this.checkoutItemList = checkoutItemList;
    }

    protected CheckoutShop(Parcel in) {
        shopName = in.readString();
        checkoutItemList = in.createTypedArrayList(CheckoutItem.CREATOR);
    }

    public static final Creator<CheckoutShop> CREATOR = new Creator<CheckoutShop>() {
        @Override
        public CheckoutShop createFromParcel(Parcel in) {
            return new CheckoutShop(in);
        }

        @Override
        public CheckoutShop[] newArray(int size) {
            return new CheckoutShop[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(shopName);
        dest.writeTypedList(checkoutItemList);
    }

    public String getShopName() {
        return shopName;
    }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getTradeMode() { return tradeMode; }
    public void setTradeMode(String tradeMode) { this.tradeMode = tradeMode; }

    public int getFreight() { return freight; }
    public void setFreight(int freight) { this.freight = freight; }

    public List<CheckoutItem> getCheckoutItemList() {
        return checkoutItemList;
    }
}
