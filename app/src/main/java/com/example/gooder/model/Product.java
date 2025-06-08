package com.example.gooder.model;

public class Product {
    private Long amount;
    private String category;
    private String city;
    private String description;
    private String imageURL;
    private String name;
    private Long price;
    private String id;
    private String method;



    public Product(String id, String name, String imageURL, String method, Long price, String city, Long amount, String category, String description) {
        this.amount = amount;
        this.category = category;
        this.city = city;
        this.description = description;
        this.imageURL = imageURL;
        this.name = name;
        this.price = price;
        this.id = id;
        this.method = method;

    }

    public String getId() { return id; }
    public String getName() { return  name; }
    public String getImageURL() { return imageURL; }
    public String getMethod() { return method; }
    public Long getPrice() { return price; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }



}
