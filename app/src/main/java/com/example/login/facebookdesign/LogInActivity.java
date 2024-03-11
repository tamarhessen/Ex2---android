package com.example.login.facebookdesign;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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
import com.example.login.viewModels.UsersViewModel;

import java.util.List;

public class LogInActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button createButton;
    private Button loginButton;
    private String displayName;
    private UsersViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        createButton = findViewById(R.id.create_btn);
        loginButton = findViewById(R.id.loginbtn);

        // Initialize the ViewModel
        userViewModel = new ViewModelProvider(this).get(UsersViewModel.class);

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
        // Set click listener for the "Login" button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                // Call the login method in UserViewModel with the Context parameter
                userViewModel.login(LogInActivity.this, username, password).observe(LogInActivity.this, new Observer<String>() {
                    @Override
                    public void onChanged(String token) {
                        if (token != null) {
                            // Login successful, start FeedActivity
                            Intent intent = new Intent(getApplicationContext(), FeedActivity.class);
                            intent.putExtra("Token", token);
                            intent.putExtra("Username", username);
                            intent.putExtra("Display name", displayName);

                            // Save the user information during login
                            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("username", username); // Store the username
                            editor.putString("token", token); // Store the token
                            editor.apply();

                            startActivity(intent);
                        } else {
                            // Login failed
                            Toast.makeText(LogInActivity.this, "Username or password incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        // Observe the login result in the activity
        userViewModel.getLoginResult().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean loginSuccessful) {
                if (loginSuccessful) {
                    // Login successful, start FeedActivity
                    Intent intent = new Intent(getApplicationContext(), FeedActivity.class);
                    intent.putExtra("Token", userViewModel.getToken());
                    intent.putExtra("Username", usernameEditText.getText().toString().trim());
                    intent.putExtra("Display name", displayName);

                    // Save the user information during login
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", usernameEditText.getText().toString().trim()); // Store the username
                    editor.putString("token", userViewModel.getToken()); // Store the token
                    editor.apply();

                    startActivity(intent);
                } else {
                    // Login failed
                    Toast.makeText(LogInActivity.this, "Username or password incorrect", Toast.LENGTH_SHORT).show();
                }
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
}
