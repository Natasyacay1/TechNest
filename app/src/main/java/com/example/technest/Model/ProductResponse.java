package com.example.technest.Model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ProductResponse {

    @SerializedName("products")
    private List<Product> products;

    @SerializedName("total")
    private int total;

    public List<Product> getProducts() { return products; }
    public int getTotal() { return total; }
}