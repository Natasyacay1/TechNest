package com.example.technest;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.technest.Database.CartDatabaseHelper;

public class CheckoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        String title = getIntent().getStringExtra("product_title");
        double price = getIntent().getDoubleExtra("product_price", 0);

        TextView tvOrderTitle = findViewById(R.id.tv_order_title);
        TextView tvOrderPrice = findViewById(R.id.tv_order_price);
        TextView tvTotalPrice = findViewById(R.id.tv_total_price);
        RadioGroup rgPayment = findViewById(R.id.rg_payment);
        Button btnBayar = findViewById(R.id.btn_bayar);
        ImageButton btnBack = findViewById(R.id.btn_checkout_back);

        long hargaRupiah = (long)(price * 16000);
        long ongkir = 25000;
        long total = hargaRupiah + ongkir;
        tvOrderTitle.setText(title);
        tvOrderPrice.setText("Rp " + String.format("%,d", hargaRupiah));
        tvTotalPrice.setText("Rp " + String.format("%,d", total));
        btnBack.setOnClickListener(v -> finish());

        btnBayar.setOnClickListener(v -> {
            int selectedId = rgPayment.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Pilih metode pembayaran dulu!", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton selected = findViewById(selectedId);
            String metode = selected.getText().toString();

            CartDatabaseHelper db = new CartDatabaseHelper(this);
            db.clearCart();

            Toast.makeText(this, "Pembayaran via " + metode + " berhasil! 🎉", Toast.LENGTH_LONG).show();
            android.content.Intent intent = new android.content.Intent(this, MainActivity.class);
            intent.setFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }
}