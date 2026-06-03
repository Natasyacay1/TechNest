package com.example.technest.API;

import android.os.Handler;
import android.os.Looper;

import com.example.technest.Model.ProductResponse;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepository {

    private final ApiService apiService;
    private final Executor executor;
    private final Handler mainThreadHandler; // Mengamankan pengiriman data ke UI thread

    public ProductRepository() {
        // Memastikan koneksi ke Service & Executor tunggal
        this.apiService = RetrofitClient.getApiService();
        this.executor = Executors.newSingleThreadExecutor();
        this.mainThreadHandler = new Handler(Looper.getMainLooper());
    }

    public void getAllProducts(final ApiCallback<ProductResponse> callback) {
        executor.execute(() -> {
            Call<ProductResponse> call = apiService.getAllProducts();
            call.enqueue(new Callback<ProductResponse>() {
                @Override
                public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        final ProductResponse productResponse = response.body();
                        // Pastikan callback sukses dijalankan di Main Thread agar UI tidak crash
                        mainThreadHandler.post(() -> callback.onSuccess(productResponse));
                    } else {
                        final Exception exception = new Exception("Response gagal: " + response.code());
                        mainThreadHandler.post(() -> callback.onFailure(exception));
                    }
                }

                @Override
                public void onFailure(Call<ProductResponse> call, final Throwable t) {
                    mainThreadHandler.post(() -> callback.onFailure(t));
                }
            });
        });
    }

    public void getByCategory(String category, final ApiCallback<ProductResponse> callback) {
        executor.execute(() -> {
            Call<ProductResponse> call = apiService.getProductsByCategory(category);
            call.enqueue(new Callback<ProductResponse>() {
                @Override
                public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        final ProductResponse productResponse = response.body();
                        // Dorong data sukses ke UI Thread
                        mainThreadHandler.post(() -> callback.onSuccess(productResponse));
                    } else {
                        final Exception exception = new Exception("Gagal: " + response.code());
                        mainThreadHandler.post(() -> callback.onFailure(exception));
                    }
                }

                @Override
                public void onFailure(Call<ProductResponse> call, final Throwable t) {
                    mainThreadHandler.post(() -> callback.onFailure(t));
                }
            });
        });
    }
}