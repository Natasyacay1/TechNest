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

    public void setProducts(List<Product> products) { this.products = products; }
    public void setTotal(int total) { this.total = total; }
}