package com.example.technest.API;

import com.example.technest.Model.ProductResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    // Menangkap kategori gadget secara dinamis
    @GET("products/category/{category_name}")
    Call<ProductResponse> getGadgetsByCategory(@Path("category_name") String categoryName);
}