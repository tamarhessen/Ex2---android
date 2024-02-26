package com.example.login;

import android.app.Application;
import com.example.login.network.RetrofitClient;

import retrofit2.Retrofit;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Retrofit in the Application class
        // Obtain the Retrofit instance
        Retrofit retrofit = RetrofitClient.getClient();

    }
}
