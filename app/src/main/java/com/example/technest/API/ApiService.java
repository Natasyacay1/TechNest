package com.example.technest.API;

import com.example.technest.Model.ProductResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

    @GET("products/category/smartphones?limit=20")
    Call<ProductResponse> getAllProducts();

    // Dibuat satu parameter saja agar pas dengan baris ProductRepository kamu
    @GET("products/category/{category}?limit=20")
    Call<ProductResponse> getProductsByCategory(@Path("category") String category);
}