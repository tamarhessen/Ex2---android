package com.example.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.login.network.WebServiceAPI;
import com.example.login.network.RetrofitClient;

import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private WebServiceAPI webServiceAPI;
    private AppDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Retrofit
        Retrofit retrofit = RetrofitClient.getClient();

        // Create an instance of the API interface
        webServiceAPI = retrofit.create(WebServiceAPI.class);

        // Initialize Room database
        db = Room.databaseBuilder(getApplicationContext(),
                        AppDB.class, "app-database")
                .fallbackToDestructiveMigration()
                .build();

        // Start the LoginActivity when the MainActivity is created
        Intent intent = new Intent(MainActivity.this, LogInActivity.class);
        startActivity(intent);

        // Finish the MainActivity to prevent it from appearing in the back stack
        finish();
    }
}
