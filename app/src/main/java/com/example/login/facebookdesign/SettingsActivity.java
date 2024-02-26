package com.example.login.facebookdesign;


import static com.example.login.facebookdesign.MainActivity.baseURL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.login.R;

public class SettingsActivity extends AppCompatActivity {

    private SwitchCompat darkModeSwitch;
    private ImageView backButton;
    private EditText serverBaseUrlEditText;
    private TextView instructionSettings;
    private Button applyButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
//        serverBaseUrlEditText = findViewById(R.id.serverBaseUrlEditText);
//        applyButton = findViewById(R.id.applyButton);
//        instructionSettings = findViewById(R.id.instructionSettings);
        Intent intent = getIntent();
        if(intent!=null){
            int flag = intent.getIntExtra("flag",0);
            if(flag==0){
                serverBaseUrlEditText.setVisibility(View.GONE);
                applyButton.setVisibility(View.GONE);
                instructionSettings.setText(R.string.changing_base_url_not_permitted);
            } else {
                serverBaseUrlEditText.setText(baseURL);
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
            baseURL = serverBaseUrlEditText.getText().toString();
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
