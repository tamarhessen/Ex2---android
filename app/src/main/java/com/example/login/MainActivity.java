package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Start the LoginActivity when the MainActivity is created
        Intent intent = new Intent(MainActivity.this, LogInActivity.class);
        startActivity(intent);
        // Finish the MainActivity to prevent it from appearing in the back stack
        finish();
    }
}
