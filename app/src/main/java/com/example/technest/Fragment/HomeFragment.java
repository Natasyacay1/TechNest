package com.example.technest.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.technest.Adapter.ProductAdapter;
import com.example.technest.API.ApiCallback;
import com.example.technest.API.ProductRepository;
import com.example.technest.Model.Product;
import com.example.technest.R;

import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView rvProducts;
    private ProgressBar progressBar;
    private ProductRepository productRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 1. Inisialisasi komponen UI dari fragment_home.xml
        rvProducts = view.findViewById(R.id.rv_products);
        progressBar = view.findViewById(R.id.progress_bar);

        rvProducts.setLayoutManager(new LinearLayoutManager(getContext()));

        // 2. Inisialisasi Repository untuk penarikan data via Retrofit
        productRepository = new ProductRepository();

        // 3. Ambil data smartphone riil dari internet
        loadGadgetData();

        return view;
    }

    private void loadGadgetData() {
        progressBar.setVisibility(View.VISIBLE);

        // Mengambil kategori "smartphones" dari API DummyJSON
        productRepository.getGadgets("smartphones", new ApiCallback() {
            @Override
            public void onSuccess(List<Product> products) {
                progressBar.setVisibility(View.GONE);

                // Pasang data asli dari internet ke Adapter
                ProductAdapter adapter = new ProductAdapter(products);
                rvProducts.setAdapter(adapter);

                // Logika klik produk (untuk persiapan pindah ke halaman detail nanti)
                adapter.setOnItemClickListener(product -> {
                    Toast.makeText(getContext(), "Kamu memilih: " + product.getTitle(), Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onError(String errorMessage) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Gagal memuat data: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }
}