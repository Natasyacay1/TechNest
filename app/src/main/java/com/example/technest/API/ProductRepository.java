package com.example.technest.API;

import com.example.technest.Model.Product;
import com.example.technest.Model.ProductResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import retrofit2.Response;

public class ProductRepository {

    private final ApiService apiService;
    private final Executor executor;

    public ProductRepository() {
        this.apiService = RetrofitClient.getApiService();
        this.executor = Executors.newSingleThreadExecutor();
    }

    // Gabungkan smartphone laptop tablet untuk tombol "Semua"
    public void getAllProducts(ApiCallback<ProductResponse> callback) {
        executor.execute(() -> {
            try {
                Response<ProductResponse> smartphoneRes = apiService.getSmartphones().execute();
                Response<ProductResponse> laptopRes = apiService.getLaptops().execute();

                List<Product> combined = new ArrayList<>();

                if (smartphoneRes.isSuccessful() && smartphoneRes.body() != null) {
                    combined.addAll(smartphoneRes.body().getProducts());
                }
                if (laptopRes.isSuccessful() && laptopRes.body() != null) {
                    combined.addAll(laptopRes.body().getProducts());
                }

                ProductResponse result = new ProductResponse();
                result.setProducts(combined);
                callback.onSuccess(result);

            } catch (Exception e) {
                callback.onFailure(e);
            }
        });
    }

    //gilter berdasarkan kategori
    public void getByCategory(String category, ApiCallback<ProductResponse> callback) {
        executor.execute(() -> {
            try {
                Response<ProductResponse> response = apiService.getProductsByCategory(category).execute();
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Exception("Gagal: " + response.code()));
                }
            } catch (Exception e) {
                callback.onFailure(e);
            }
        });
    }

    //pencarian produk
    public void searchProducts(String query, ApiCallback<ProductResponse> callback) {
        executor.execute(() -> {
            try {
                Response<ProductResponse> response = apiService.searchProducts(query).execute();
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Exception("Gagal: " + response.code()));
                }
            } catch (Exception e) {
                callback.onFailure(e);
            }
        });
    }
}