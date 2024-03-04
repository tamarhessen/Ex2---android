package com.example.login.facebookdesign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login.R;
import com.example.login.API.WebServiceAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LogInActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button createButton;
    private Button loginButton;
    private String displayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        createButton = findViewById(R.id.create_btn);
        loginButton = findViewById(R.id.loginbtn);

        // Set click listener for the "Create Account" button
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the CreateAccountActivity when the button is clicked
                Intent intent = new Intent(LogInActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });

        // Set click listener for the "Login" button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the entered username and password
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();
                Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:5000/api/")
                        .addConverterFactory(GsonConverterFactory.create(gson)).build();
                WebServiceAPI webServiceAPI = retrofit.create(WebServiceAPI.class);
                UserCreateToken userCreateToken = new UserCreateToken(username, password);
                Call<String> call = webServiceAPI.getToken(userCreateToken);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            Intent intent = new Intent(getApplicationContext(), FeedActivity.class);
                            String tokenNow = response.body();
                            intent.putExtra("Token", tokenNow);
                            intent.putExtra("Username", username);
                            intent.putExtra("Display name", displayName);

                            // Save the user information during login
                            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("username", username); // Store the username
                            editor.putString("token", tokenNow); // Store the token
                            editor.apply();
                            startActivity(intent);
                        } else {
                            Toast.makeText(LogInActivity.this, "Username or password incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(LogInActivity.this, "Network request failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // Handle "Enter" key press on the password field
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == KeyEvent.KEYCODE_ENTER) {
                    // Perform login action
                    loginButton.performClick(); // Trigger the "Login" button click
                    return true;
                }
                return false;
            }
        });
    }

    private boolean checkCredentials(String username, String password) {
        // Get the list of users from UserCredentials class
        List<UserCredentials.User> userList = UserCredentials.getUsers();

        // Check if the credentials exist in the user list
        for (UserCredentials.User user : userList) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true; // Credentials match
            }
        }
        return false; // Credentials do not match
    }
}