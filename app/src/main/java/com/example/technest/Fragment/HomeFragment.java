package com.example.technest.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.technest.API.ApiCallback;
import com.example.technest.API.ProductRepository;
import com.example.technest.Adapter.ProductAdapter;
import com.example.technest.Database.CartDatabaseHelper;
import com.example.technest.Model.Product;
import com.example.technest.Model.ProductResponse;
import com.example.technest.R;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView rvProducts;
    private ProgressBar progressBar;
    private ImageButton btnRefresh;
    private TextView btnSemua, btnSmartphone, btnLaptop, btnTablet, tvViewAll;
    private EditText searchBar;
    private ProductAdapter productAdapter;
    private ProductRepository productRepository;
    private CartDatabaseHelper dbHelper;

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
        btnTablet = view.findViewById(R.id.btnTablet);
        tvViewAll = view.findViewById(R.id.tv_view_all);
        searchBar = view.findViewById(R.id.search_bar);

        productAdapter = new ProductAdapter(new ArrayList<>());
        rvProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvProducts.setNestedScrollingEnabled(false);
        rvProducts.setHasFixedSize(false);
        rvProducts.setAdapter(productAdapter);

        productRepository = new ProductRepository();
        dbHelper = new CartDatabaseHelper(getContext());

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (query.isEmpty()) {
                    loadAllProducts();
                } else {
                    searchProducts(query);
                }
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        btnSemua.setOnClickListener(v -> {
            setActiveTab(btnSemua);
            searchBar.setText("");
            loadAllProducts();
        });

        btnSmartphone.setOnClickListener(v -> {
            setActiveTab(btnSmartphone);
            searchBar.setText("");
            loadByCategory("smartphones");
        });

        btnLaptop.setOnClickListener(v -> {
            setActiveTab(btnLaptop);
            searchBar.setText("");
            loadByCategory("laptops");
        });

        btnTablet.setOnClickListener(v -> {
            setActiveTab(btnTablet);
            searchBar.setText("");
            loadByCategory("tablets");
        });

        if (tvViewAll != null) {
            tvViewAll.setOnClickListener(v -> {
                setActiveTab(btnSemua);
                searchBar.setText("");
                loadAllProducts();
            });
        }

        btnRefresh.setOnClickListener(v -> {
            btnRefresh.setVisibility(View.GONE);
            searchBar.setText("");
            loadAllProducts();
        });

        setActiveTab(btnSemua);
        loadAllProducts();
    }

    private void setActiveTab(TextView activeTab) {
        TextView[] tabs = {btnSemua, btnSmartphone, btnLaptop, btnTablet};
        for (TextView tab : tabs) {
            tab.setBackgroundTintList(android.content.res.ColorStateList.valueOf(
                    android.graphics.Color.parseColor("#12161A")));
            tab.setTextColor(android.graphics.Color.parseColor("#626D77"));
        }
        activeTab.setBackgroundTintList(android.content.res.ColorStateList.valueOf(
                android.graphics.Color.parseColor("#00E5FF")));
        activeTab.setTextColor(android.graphics.Color.parseColor("#0B0D0F"));
    }

    private void searchProducts(String query) {
        progressBar.setVisibility(View.VISIBLE);
        btnRefresh.setVisibility(View.GONE);

        productRepository.searchProducts(query, new ApiCallback<ProductResponse>() {
            @Override
            public void onSuccess(ProductResponse response) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    if (response != null && response.getProducts() != null
                            && !response.getProducts().isEmpty()) {
                        productAdapter.updateData(response.getProducts());
                    } else {
                        productAdapter.updateData(new ArrayList<>());
                        Toast.makeText(getContext(), "Produk tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Throwable t) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    btnRefresh.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "Gagal mencari produk!", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void loadAllProducts() {
        progressBar.setVisibility(View.VISIBLE);
        btnRefresh.setVisibility(View.GONE);

        productRepository.getAllProducts(new ApiCallback<ProductResponse>() {
            @Override
            public void onSuccess(ProductResponse response) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    if (response != null && response.getProducts() != null) {
                        List<Product> products = response.getProducts();
                        productAdapter.updateData(products);
                        // Simpan ke cache SQLite untuk mode offline
                        dbHelper.saveProductsToCache(products);
                    } else {
                        Toast.makeText(getContext(), "Tidak ada produk ditemukan", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Throwable t) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    btnRefresh.setVisibility(View.VISIBLE);
                    // Load dari cache SQLite saat offline
                    List<Product> cachedProducts = dbHelper.getCachedProducts();
                    if (!cachedProducts.isEmpty()) {
                        productAdapter.updateData(cachedProducts);
                        Toast.makeText(getContext(), "Mode offline - menampilkan data tersimpan", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Gagal memuat data, cek koneksi!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
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
                    }
                });
            }

            @Override
            public void onFailure(Throwable t) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    btnRefresh.setVisibility(View.VISIBLE);

                    //mbil semua cache lalu saring sesuai kategori yang diklik
                    List<Product> allCachedProducts = dbHelper.getCachedProducts();
                    List<Product> filteredCache = new ArrayList<>();

                    for (Product product : allCachedProducts) {
                        if (product.getCategory() != null && product.getCategory().equalsIgnoreCase(category)) {
                            filteredCache.add(product);
                        }
                    }

                    if (!filteredCache.isEmpty()) {
                        productAdapter.updateData(filteredCache);
                        Toast.makeText(getContext(), "Mode offline - menampilkan cache kategori " + category, Toast.LENGTH_SHORT).show();
                    } else {
                        productAdapter.updateData(new ArrayList<>());
                        Toast.makeText(getContext(), "Gagal memuat data, tidak ada cache untuk kategori ini!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}