package com.example.login;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    private SwitchCompat darkModeSwitch;
    private ImageView backButton;

    private Button applyButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        applyButton = findViewById(R.id.applyButton);
        Intent intent = getIntent();
        if(intent!=null){
            int flag = intent.getIntExtra("flag",0);
            if(flag==0){
                applyButton.setVisibility(View.GONE);
            } else {
            }
        }
        darkModeSwitch = findViewById(R.id.darkModeSwitch);
        backButton = findViewById(R.id.backButton2);
        // Load the current state of dark mode from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        boolean isDarkModeEnabled = sharedPreferences.getBoolean("dark_mode_enabled", false);
        darkModeSwitch.setChecked(isDarkModeEnabled);

        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Save the state of dark mode to shared preferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("dark_mode_enabled", isChecked);
                editor.apply();

                // Apply the selected mode immediately
                applyDarkMode(isChecked);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        applyButton.setOnClickListener(v -> {
        });

    }

    public static void applyDarkMode(boolean isEnabled) {
        if (isEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
