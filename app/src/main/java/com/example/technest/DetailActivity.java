package com.example.technest;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.technest.Database.CartDatabaseHelper;
import com.example.technest.Model.Product;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Ambil data dari Intent
        int id = getIntent().getIntExtra("product_id", 0);
        String title = getIntent().getStringExtra("product_title");
        double price = getIntent().getDoubleExtra("product_price", 0);
        String desc = getIntent().getStringExtra("product_desc");
        String thumbnail = getIntent().getStringExtra("product_thumbnail");
        String brand = getIntent().getStringExtra("product_brand");
        double rating = getIntent().getDoubleExtra("product_rating", 0);

        // Bind ke view
        ImageView imgProduct = findViewById(R.id.img_detail);
        TextView tvTitle = findViewById(R.id.tv_detail_title);
        TextView tvBrand = findViewById(R.id.tv_detail_brand);
        TextView tvPrice = findViewById(R.id.tv_detail_price);
        TextView tvRating = findViewById(R.id.tv_detail_rating);
        TextView tvDesc = findViewById(R.id.tv_detail_desc);
        Button btnAddCart = findViewById(R.id.btn_detail_add_cart);
        Button btnBuyNow = findViewById(R.id.btn_detail_buy_now);
        ImageButton btnBack = findViewById(R.id.btn_detail_back);

        // Set data
        tvTitle.setText(title);
        tvBrand.setText(brand != null ? brand.toUpperCase() : "");
        tvPrice.setText("Rp " + String.format("%,.0f", price * 16000));
        tvRating.setText("⭐ " + rating);
        tvDesc.setText(desc);

        Glide.with(this)
                .load(thumbnail)
                .placeholder(R.drawable.ic_technest_logo)
                .centerCrop()
                .into(imgProduct);

        btnBack.setOnClickListener(v -> finish());

        // Tambah ke keranjang
        btnAddCart.setOnClickListener(v -> {
            Product product = new Product();
            product.setId(id);
            product.setTitle(title);
            product.setPrice(price);
            product.setThumbnail(thumbnail);

            CartDatabaseHelper db = new CartDatabaseHelper(this);
            db.addToCart(product);
            Toast.makeText(this, title + " ditambahkan ke keranjang!", Toast.LENGTH_SHORT).show();
        });

        // Beli sekarang
        btnBuyNow.setOnClickListener(v -> {
            Product product = new Product();
            product.setId(id);
            product.setTitle(title);
            product.setPrice(price);
            product.setThumbnail(thumbnail);

            CartDatabaseHelper db = new CartDatabaseHelper(this);
            db.addToCart(product);

            // Pindah ke CheckoutActivity
            android.content.Intent intent = new android.content.Intent(this, CheckoutActivity.class);
            intent.putExtra("product_title", title);
            intent.putExtra("product_price", price);
            intent.putExtra("product_thumbnail", thumbnail);
            startActivity(intent);
        });
    }
}