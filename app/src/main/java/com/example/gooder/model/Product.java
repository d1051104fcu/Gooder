package com.example.gooder.model;

public class Product {
    private String title;
    private String imageUrl;
    private String method;
    private Long price;

    public Product(String title, String imageUrl, String method, Long price) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.method = method;
        this.price = price;
    }

    public String getTitle() { return title; }
    public String getImageUrl() { return imageUrl; }
    public String getMethod() { return method; }
    public Long getPrice() { return price; }



}
