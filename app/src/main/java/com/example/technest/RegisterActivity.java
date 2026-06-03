package com.example.technest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText etNama, etEmail, etPassword, etKonfirmasi;
    private Button btnDaftar;
    private TextView tvGoToLogin;
    private ImageButton btnKembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etNama = findViewById(R.id.etNama);
        etEmail = findViewById(R.id.etEmailDaftar);
        etPassword = findViewById(R.id.etPasswordDaftar);
        etKonfirmasi = findViewById(R.id.etKonfirmasiPassword);
        btnDaftar = findViewById(R.id.btnDaftar);
        tvGoToLogin = findViewById(R.id.tvGoToLogin);
        btnKembali = findViewById(R.id.btnKembali);

        btnKembali.setOnClickListener(v -> finish());

        btnDaftar.setOnClickListener(v -> {
            String nama = etNama.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String konfirmasi = etKonfirmasi.getText().toString().trim();

            if (TextUtils.isEmpty(nama)) {
                etNama.setError("Nama tidak boleh kosong");
                return;
            }
            if (TextUtils.isEmpty(email)) {
                etEmail.setError("Email tidak boleh kosong");
                return;
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.setError("Format email tidak valid");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                etPassword.setError("Kata sandi tidak boleh kosong");
                return;
            }
            if (password.length() < 6) {
                etPassword.setError("Kata sandi minimal 6 karakter");
                return;
            }
            if (!password.equals(konfirmasi)) {
                etKonfirmasi.setError("Kata sandi tidak cocok");
                return;
            }

            // Simpan data user ke SharedPreferences
            SharedPreferences prefs = getSharedPreferences("technest_prefs", MODE_PRIVATE);
            prefs.edit()
                    .putString("user_nama", nama)
                    .putString("user_email", email)
                    .putString("user_password", password)
                    .apply();

            Toast.makeText(this, "Pendaftaran berhasil! Silakan masuk.", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        tvGoToLogin.setOnClickListener(v -> finish());
    }
}