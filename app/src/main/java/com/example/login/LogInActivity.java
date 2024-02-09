package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class LogInActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button createButton;
    private Button loginButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        createButton = findViewById(R.id.create_btn);
        loginButton = findViewById(R.id.loginbtn);
        Bitmap profilePictureBitmap = getIntent().getParcelableExtra("profilePictureBitmap");


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

                // Check if the credentials exist
                boolean credentialsExist = checkCredentials(username, password);

                if (credentialsExist) {
                    // Credentials exist, proceed to login
                    Toast.makeText(LogInActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    // Start the FeedActivity
                    Intent intent = new Intent(LogInActivity.this, FeedActivity.class);

                    startActivity(intent);
                    finish();
                } else {
                    // Credentials do not exist, show an error message
                    Toast.makeText(LogInActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
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
