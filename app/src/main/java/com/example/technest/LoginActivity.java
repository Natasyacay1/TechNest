package com.example.technest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvGoToRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmailLogin);
        etPassword = findViewById(R.id.etPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        tvGoToRegister = findViewById(R.id.tvGoToRegister);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                etEmail.setError("Email tidak boleh kosong");
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

            // Cek apakah email & password cocok dengan data yang tersimpan
            SharedPreferences prefs = getSharedPreferences("technest_prefs", MODE_PRIVATE);
            String savedEmail = prefs.getString("user_email", "");
            String savedPassword = prefs.getString("user_password", "");

            if (email.equals(savedEmail) && password.equals(savedPassword)) {
                // Login berhasil
                prefs.edit().putBoolean("is_logged_in", true).apply();
                Toast.makeText(this, "Selamat datang kembali!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Email atau kata sandi salah!", Toast.LENGTH_SHORT).show();
            }
        });

        tvGoToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}