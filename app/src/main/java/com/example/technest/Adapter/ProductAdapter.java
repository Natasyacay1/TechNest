package com.example.technest.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.technest.R;
import java.util.List;

// Sesuaikan 'Product' dengan nama model class data produk milikmu
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList; // Ganti 'Product' sesuai nama kelas modelmu jika berbeda

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        // Bind data nama produk
        holder.tvProductName.setText(product.getTitle()); // atau product.getName() sesuai modelmu

        // Bind data harga produk
        holder.tvProductPrice.setText("$" + product.getPrice());

        // Bind data rating jika ada
        holder.tvProductRating.setText(String.valueOf(product.getRating()));

        // Untuk gambar, jika kamu menggunakan library Glide, kodenya seperti ini:
        // Glide.with(holder.itemView.getContext()).load(product.getThumbnail()).into(holder.imgProduct);
        // Kalau pakai Picasso:
        // Picasso.get().load(product.getThumbnail()).into(holder.imgProduct);
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    // KUNCI UTAMA NYA ADA DI SINI (Menyesuaikan dengan ID Baru di item_product.xml)
    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvProductName, tvProductPrice, tvProductRating, btnAddToCart;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.img_product);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvProductPrice = itemView.findViewById(R.id.tv_product_price);
            tvProductRating = itemView.findViewById(R.id.tv_product_rating);
            btnAddToCart = itemView.findViewById(R.id.btn_add_to_cart);
        }
    }
}