package com.example.technest.Model;

import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("price")
    private double price;

    @SerializedName("rating")
    private double rating;

    @SerializedName("brand")
    private String brand;

    @SerializedName("category")
    private String category;

    @SerializedName("thumbnail")
    private String thumbnail;

    private boolean isSelected = false;

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public double getRating() { return rating; }
    public String getBrand() { return brand; }
    public String getCategory() { return category; }
    public String getThumbnail() { return thumbnail; }

    public boolean isSelected() {
        return isSelected;
    }

    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(double price) { this.price = price; }
    public void setRating(double rating) { this.rating = rating; }
    public void setBrand(String brand) { this.brand = brand; }
    public void setCategory(String category) { this.category = category; }
    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }
    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }
}