package com.example.gooder.model;

public class OrderItem {
    private String product_id;
    private String name;
    private int price;
    private int amount;

    public OrderItem(String product_id, String name, int price, int amount) {
        this.product_id = product_id;
        this.name = name;
        this.price = price;
        this.amount = amount;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }
}
