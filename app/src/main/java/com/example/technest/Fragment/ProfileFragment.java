package com.example.technest.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import com.example.technest.LoginActivity;
import com.example.technest.R;

public class ProfileFragment extends Fragment {

    private SwitchCompat switchTheme;
    private SharedPreferences prefs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        switchTheme = view.findViewById(R.id.switch_theme);
        TextView tvNama = view.findViewById(R.id.tv_profile_nama);
        TextView tvEmail = view.findViewById(R.id.tv_profile_email);
        Button btnLogout = view.findViewById(R.id.btn_logout);

        prefs = requireActivity().getSharedPreferences("technest_prefs", requireActivity().MODE_PRIVATE);
        String nama = prefs.getString("user_nama", "TechNest User");
        String email = prefs.getString("user_email", "user@technest.com");
        tvNama.setText(nama);
        tvEmail.setText(email);

        // Ambil status dark mode (default true sesuai kodingan aslimu)
        boolean isDarkMode = prefs.getBoolean("dark_mode", true);

        // FIX: Set status saklar SEBELUM listener dipasang agar tidak bentrok saat recreate halaman
        // Jika isDarkMode = true, maka saklar otomatis menyala (checked)
        switchTheme.setChecked(isDarkMode);

        // FIX: Logika diselaraskan (Checked = Gelap, Unchecked = Terang)
        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (getContext() == null || getActivity() == null) return;

            if (isChecked) {
                // Saklar ON -> Aktifkan Mode Gelap
                prefs.edit().putBoolean("dark_mode", true).apply();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                Toast.makeText(getContext(), "Mode Gelap aktif", Toast.LENGTH_SHORT).show();
            } else {
                // Saklar OFF -> Aktifkan Mode Terang
                prefs.edit().putBoolean("dark_mode", false).apply();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                Toast.makeText(getContext(), "Mode Terang aktif", Toast.LENGTH_SHORT).show();
            }
        });

        btnLogout.setOnClickListener(v -> {
            prefs.edit().putBoolean("is_logged_in", false).apply();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}