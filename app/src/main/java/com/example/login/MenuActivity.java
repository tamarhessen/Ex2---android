package com.example.login;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ImageView profilePictureImageView = findViewById(R.id.image_profile_picture);
        Bitmap profilePictureBitmap = CreateAccountActivity.profilePictureBitmap;
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



        // Perform any additional initialization or setup here
    }
}