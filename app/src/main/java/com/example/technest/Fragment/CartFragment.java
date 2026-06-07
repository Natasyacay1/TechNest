package com.example.technest.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.technest.Adapter.CartAdapter;
import com.example.technest.CheckoutActivity;
import com.example.technest.Database.CartDatabaseHelper;
import com.example.technest.Model.Product;
import com.example.technest.R;
import java.util.List;
import java.util.Locale;

public class CartFragment extends Fragment {

    private RecyclerView rvCart;
    private LinearLayout layoutEmptyState, layoutCartSummary;
    private TextView tvTotalHarga;
    private AppCompatButton btnCheckout;
    private CartDatabaseHelper dbHelper;
    private CartAdapter cartAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvCart = view.findViewById(R.id.rv_cart);
        layoutEmptyState = view.findViewById(R.id.layout_empty_state);
        layoutCartSummary = view.findViewById(R.id.layout_cart_summary);
        tvTotalHarga = view.findViewById(R.id.tv_total_harga);
        btnCheckout = view.findViewById(R.id.btn_checkout);

        dbHelper = new CartDatabaseHelper(getContext());

        cartAdapter = new CartAdapter(null, (product, position) -> {
            // Hapus dari database
            dbHelper.deleteCartItem(product.getId());
            Toast.makeText(getContext(), product.getTitle() + " dihapus dari keranjang", Toast.LENGTH_SHORT).show();
            loadCartData();
        });

        rvCart.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCart.setAdapter(cartAdapter);

        btnCheckout.setOnClickListener(v -> {
            List<Product> cartList = dbHelper.getAllCartItems();
            if (cartList != null && !cartList.isEmpty()) {
                double total = 0;
                for (Product p : cartList) total += p.getPrice();

                Intent intent = new Intent(getActivity(), CheckoutActivity.class);
                intent.putExtra("product_title", cartList.size() + " produk");
                intent.putExtra("product_price", total);
                startActivity(intent);
            }
        });

        loadCartData();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCartData();
    }

    public void loadCartData() {
        if (dbHelper == null) return;

        List<Product> cartList = dbHelper.getAllCartItems();
        double totalBelanja = 0;

        if (cartList != null && !cartList.isEmpty()) {
            for (Product product : cartList) {
                if (product != null) totalBelanja += product.getPrice();
            }
        }

        if (cartList == null || cartList.isEmpty()) {
            layoutEmptyState.setVisibility(View.VISIBLE);
            rvCart.setVisibility(View.GONE);
            layoutCartSummary.setVisibility(View.GONE);
        } else {
            layoutEmptyState.setVisibility(View.GONE);
            rvCart.setVisibility(View.VISIBLE);
            layoutCartSummary.setVisibility(View.VISIBLE);
            cartAdapter.updateData(cartList);
            tvTotalHarga.setText(String.format(Locale.US, "$%.2f", totalBelanja));
        }
    }
}