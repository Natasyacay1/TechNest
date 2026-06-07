package com.example.technest.API;

import com.example.technest.Model.ProductResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("products/category/smartphones?limit=20")
    Call<ProductResponse> getSmartphones();

    @GET("products/category/laptops?limit=20")
    Call<ProductResponse> getLaptops();

    @GET("products/category/tablets?limit=20")
    Call<ProductResponse> getTablets();

    @GET("products/category/mobile-accessories?limit=20")
    Call<ProductResponse> getAksesoris();

    @GET("products/category/{category}?limit=20")
    Call<ProductResponse> getProductsByCategory(@Path("category") String category);

    @GET("products/search")
    Call<ProductResponse> searchProducts(@Query("q") String query);
}