package com.example.login;

import static com.example.login.SettingsActivity.applyDarkMode;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        boolean isDarkModeEnabled = sharedPreferences.getBoolean("dark_mode_enabled", false);
        applyDarkMode(isDarkModeEnabled);
        // Start the LoginActivity when the MainActivity is created
        Intent intent = new Intent(MainActivity.this, LogInActivity.class);
        startActivity(intent);
        // Finish the MainActivity to prevent it from appearing in the back stack
        finish();
    }
}
