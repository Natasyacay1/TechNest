package com.example.technest.API;

import android.os.Handler;
import android.os.Looper;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import com.example.technest.Model.Product;
import com.example.technest.Model.ProductResponse;
import retrofit2.Call;
import retrofit2.Response;

public class ProductRepository {

    private final ApiService apiService;
    private final Executor executor;
    private final Handler mainHandler;

    public ProductRepository() {
        // Inisialisasi ApiService menggunakan RetrofitClient yang sudah kita buat
        this.apiService = RetrofitClient.getClient().create(ApiService.class);

        // SPESIFIKASI WAJIB: Membuat 1 Background Thread menggunakan Executor
        // Ini adalah "jalan tol rahasia" agar download data tidak membuat aplikasi macet
        this.executor = Executors.newSingleThreadExecutor();

        // SPESIFIKASI WAJIB: Mempersiapkan Handler untuk kembali ke Main Thread (UI)
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    // Fungsi untuk mengambil data gadget berdasarkan kategori secara asynchronous (Latar Belakang)
    public void getGadgets(String categoryName, ApiCallback callback) {

        // Executor mulai menjalankan tugasnya di background thread
        executor.execute(() -> {
            try {
                // Melakukan request ke API secara sinkron (.execute()) karena kita sudah berada di background
                Call<ProductResponse> call = apiService.getGadgetsByCategory(categoryName);
                Response<ProductResponse> response = call.execute();

                if (response.isSuccessful() && response.body() != null) {
                    List<Product> productList = response.body().getProducts();

                    // Jika sukses, kirim datanya kembali ke Main Thread (UI) via Handler
                    mainHandler.post(() -> callback.onSuccess(productList));
                } else {
                    // Jika server merespon tapi ada error (misal server overload)
                    mainHandler.post(() -> callback.onError("Gagal memuat data dari server."));
                }

            } catch (IOException e) {
                // Jika koneksi internet putus atau tidak ada sinyal
                mainHandler.post(() -> callback.onError("Tidak ada koneksi internet. Periksa jaringan Anda."));
            }
        });
    }
}