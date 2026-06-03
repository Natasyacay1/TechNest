package com.example.technest.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.technest.API.ApiCallback;
import com.example.technest.API.ProductRepository;
import com.example.technest.Adapter.ProductAdapter;
import com.example.technest.Model.ProductResponse;
import com.example.technest.R;

public class HomeFragment extends Fragment {

    private RecyclerView rvProducts;
    private ProgressBar progressBar;
    private Button btnRefresh;
    private Button btnSemua, btnSmartphone, btnLaptop;
    private ProductAdapter productAdapter;
    private ProductRepository productRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvProducts = view.findViewById(R.id.rv_products);
        progressBar = view.findViewById(R.id.progress_bar);
        btnRefresh = view.findViewById(R.id.btn_refresh);
        btnSemua = view.findViewById(R.id.btnSemuaProduk);
        btnSmartphone = view.findViewById(R.id.btnSmartphone);
        btnLaptop = view.findViewById(R.id.btnLaptop);

        rvProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));
        productAdapter = new ProductAdapter();
        rvProducts.setAdapter(productAdapter);

        productRepository = new ProductRepository();

        btnSemua.setOnClickListener(v -> {
            setActiveButton(btnSemua);
            loadByCategory("smartphones");
        });

        btnSmartphone.setOnClickListener(v -> {
            setActiveButton(btnSmartphone);
            loadByCategory("smartphones");
        });

        btnLaptop.setOnClickListener(v -> {
            setActiveButton(btnLaptop);
            loadByCategory("laptops");
        });

        btnRefresh.setOnClickListener(v -> loadByCategory("smartphones"));

        loadByCategory("smartphones");
    }

    private void setActiveButton(Button activeBtn) {
        btnSemua.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#1A1D21")));
        btnSemua.setTextColor(android.graphics.Color.parseColor("#888888"));
        btnSmartphone.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#1A1D21")));
        btnSmartphone.setTextColor(android.graphics.Color.parseColor("#888888"));
        btnLaptop.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#1A1D21")));
        btnLaptop.setTextColor(android.graphics.Color.parseColor("#888888"));

        activeBtn.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#00BCD4")));
        activeBtn.setTextColor(android.graphics.Color.parseColor("#0F1113"));
    }

    private void loadByCategory(String category) {
        progressBar.setVisibility(View.VISIBLE);
        btnRefresh.setVisibility(View.GONE);

        productRepository.getByCategory(category, new ApiCallback<ProductResponse>() {
            @Override
            public void onSuccess(ProductResponse response) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    if (response != null && response.getProducts() != null) {
                        productAdapter.updateData(response.getProducts());
                    } else {
                        Toast.makeText(getContext(), "Tidak ada produk ditemukan", Toast.LENGTH_SHORT).show();
                        btnRefresh.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onFailure(Throwable t) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    btnRefresh.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "Gagal memuat data, cek koneksi internet!", Toast.LENGTH_LONG).show();
                });
            }
        });
    }
}