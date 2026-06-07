package com.example.technest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // --- PERBAIKAN: PINDAHKAN KE SINI (SEBELUM super.onCreate) ---
        SharedPreferences prefs = getSharedPreferences("technest_prefs", MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean("dark_mode", true);
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        // -------------------------------------------------------------

        super.onCreate(savedInstanceState); // super.onCreate sekarang ditaruh setelah set tema
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);
            Intent intent;
            if (isLoggedIn) {
                intent = new Intent(SplashActivity.this, MainActivity.class);
            } else {
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }
            startActivity(intent);
            finish();
        }, 3000);
    }
}