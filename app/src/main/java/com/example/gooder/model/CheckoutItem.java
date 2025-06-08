package com.example.gooder.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.gooder.R;

public class CheckoutItem implements Parcelable {
    private final String ShoppingCartId;
    private final String productId;
    private final String name;
    private final int price;
    private String imgId = String.valueOf(R.drawable.not_found);;
    private final int count;

    public CheckoutItem(String ShoppingCartId, String productId, String name, int price, String imgId, int count){
        this.ShoppingCartId = ShoppingCartId;
        this.productId = productId;
        this.name = name;
        this.price = price;
        if (!imgId.isEmpty()){
            this.imgId = imgId;
        }
        this.count = count;
    }

    protected CheckoutItem(Parcel in) {
        ShoppingCartId = in.readString();
        productId = in.readString();
        name = in.readString();
        price = in.readInt();
        imgId = in.readString();
        count = in.readInt();
    }

    public static final Creator<CheckoutItem> CREATOR = new Creator<CheckoutItem>() {
        @Override
        public CheckoutItem createFromParcel(Parcel in) {
            return new CheckoutItem(in);
        }

        @Override
        public CheckoutItem[] newArray(int size) {
            return new CheckoutItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(ShoppingCartId);
        dest.writeString(productId);
        dest.writeString(name);
        dest.writeInt(price);
        dest.writeString(imgId);
        dest.writeInt(count);
    }

    public String getShoppingCartId() {
        return ShoppingCartId;
    }

    public String getProductId() {
        return productId;
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
}
