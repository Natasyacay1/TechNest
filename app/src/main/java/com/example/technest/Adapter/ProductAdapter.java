package com.example.technest.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.technest.Database.CartDatabaseHelper;
import com.example.technest.DetailActivity;
import com.example.technest.Model.Product;
import com.example.technest.R;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        Context context = holder.itemView.getContext();

        holder.tvProductName.setText(product.getTitle() != null ? product.getTitle() : "Unknown");
        holder.tvProductPrice.setText("$" + product.getPrice());
        holder.tvProductRating.setText("⭐ " + product.getRating());

        Glide.with(context)
                .load(product.getThumbnail())
                .placeholder(R.drawable.ic_technest_logo)
                .centerCrop()
                .into(holder.imgProduct);

        // klik item, buka DetailActivity dengan data lengkap
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("product_id", product.getId());
            intent.putExtra("product_title", product.getTitle());
            intent.putExtra("product_price", product.getPrice());
            intent.putExtra("product_desc", product.getDescription());
            intent.putExtra("product_thumbnail", product.getThumbnail());
            intent.putExtra("product_brand", product.getBrand());
            intent.putExtra("product_rating", product.getRating());
            context.startActivity(intent);
        });

        //klik tombol keranjang lalu simpan ke SQLite
        holder.btnAddToCart.setOnClickListener(v -> {
            CartDatabaseHelper db = new CartDatabaseHelper(context);
            db.addToCart(product);
            Toast.makeText(context, product.getTitle() + " ditambahkan ke keranjang!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    public void updateData(List<Product> newProductList) {
        this.productList = newProductList;
        notifyDataSetChanged();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgProduct;
        public TextView tvProductName, tvProductPrice, tvProductRating, btnAddToCart;

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