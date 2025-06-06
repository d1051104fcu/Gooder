package com.example.gooder.model;

public class Product {
    private String id;
    private String title;
    private String imageUrl;
    private String method;
    private Long price;
    private String city;

    public Product(String id, String title, String imageUrl, String method, Long price, String city) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.method = method;
        this.price = price;
        this.city = city;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getImageUrl() { return imageUrl; }
    public String getMethod() { return method; }
    public Long getPrice() { return price; }



}
