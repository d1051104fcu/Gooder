package com.example.gooder.model;

import java.sql.Timestamp;

public class Order {
    private String buyer_id, seller_id;
    private String tradeMode, address;
    private int freight, total_price;
    private int state;
    private Timestamp time;

    public Order() {
    }

    public Order(String buyer_id, String seller_id, String tradeMode, String address, int freight, int total_price, Timestamp time) {
        this.buyer_id = buyer_id;
        this.seller_id = seller_id;
        this.tradeMode = tradeMode;
        this.address = address;
        this.freight = freight;
        this.total_price = total_price;
        this.time = time;
        this.state = 0;
    }

    public String getBuyer_id() {
        return buyer_id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public String getTradeMode() {
        return tradeMode;
    }

    public String getAddress() {
        return address;
    }

    public int getFreight() {
        return freight;
    }

    public int getTotal_price() {
        return total_price;
    }

    public Timestamp getTime() {
        return time;
    }

    public int getState() {
        return state;
    }

}
