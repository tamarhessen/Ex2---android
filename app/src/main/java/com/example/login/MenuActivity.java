package com.example.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MenuActivity extends AppCompatActivity {
    private Button homeButton;
    private Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ImageView profilePictureImageView = findViewById(R.id.image_profile_picture);
        Bitmap profilePictureBitmap = CreateAccountActivity.profilePictureBitmap;
        Button homeButton = findViewById(R.id.homeimg);
        Button logOutButton = findViewById(R.id.logOutBtn);
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
                // Start the FeedActivity when the home button is clicked
                Intent intent = new Intent(MenuActivity.this, FeedActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Add this line to clear the activity stack
                startActivity(intent);
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

        // Perform any additional initialization or setup here
    }
}