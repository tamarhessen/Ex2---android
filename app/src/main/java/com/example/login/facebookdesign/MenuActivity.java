package com.example.login.facebookdesign;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login.R;

import java.util.List;

public class MenuActivity extends AppCompatActivity {
    private Button homeButton;
    private Button logout;

    private Button settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ImageView profilePictureImageView = findViewById(R.id.image_profile_picture);
        Bitmap profilePictureBitmap = CreateAccountActivity.profilePictureBitmap;
        Button homeButton = findViewById(R.id.homeimg);
        Button logOutButton = findViewById(R.id.logOutBtn);
        Button settingsButton = findViewById(R.id.settings);
        profilePictureImageView.setImageBitmap(profilePictureBitmap);
        List<UserCredentials.User> userList = UserCredentials.getUsers();
        TextView displayNameTextView = findViewById(R.id.displayName);
        String userDisplayName="";
        for (UserCredentials.User user : userList) {
            String username = user.getUsername();
            userDisplayName = user.getDisplayName();
            // Perform any operations with userDisplayName if needed
        }

// Now you can use the displayName TextView
// For example, you can set its text using setText method
        if (displayNameTextView != null) {
            displayNameTextView.setText(userDisplayName);
        }
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Navigate back to the previous activity
            }
        });


        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the FeedActivity when the home button is clicked
                Intent intent = new Intent(MenuActivity.this, LogInActivity.class);
                startActivity(intent);
                // Finish the current activity if needed
                finish();
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the FeedActivity when the home button is clicked
                Intent intent = new Intent(MenuActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        // Perform any additional initialization or setup here
    }
}