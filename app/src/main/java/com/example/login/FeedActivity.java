package com.example.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FeedActivity extends AppCompatActivity {

    // Assuming you have references to the ImageView and LinearLayout
    ImageView userPostPicture;
    LinearLayout userPostText; // Change from TextView to LinearLayout

    private Button menuButton;
    private Button addPostButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        // Initialize the views
        userPostText = findViewById(R.id.userPostText); // Change to LinearLayout
        menuButton = findViewById(R.id.menubtn);
        addPostButton=findViewById(R.id.addbtn);
        ImageView profilePictureImageView = findViewById(R.id.image_profile_picture);
        Bitmap profilePictureBitmap = CreateAccountActivity.profilePictureBitmap;
        profilePictureImageView.setImageBitmap(profilePictureBitmap);

        // Find the ImageView in your activity


// Assuming profilePictureBitmap is the Bitmap you want to set
// Set the bitmap to the ImageView


        // Set click listener for the "Create Account" button
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the MenuActivity when the button is clicked
                Intent intent = new Intent(FeedActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });
        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the MenuActivity when the button is clicked
                Intent intent = new Intent(FeedActivity.this, AddPostActivity.class);
                startActivity(intent);
                submitNewPost(profilePictureBitmap);

            }
        });
        // Example: If user submits a new post

    }

    // Method to handle when the user submits a new post
    private void submitNewPost(Bitmap profilePictureBitmap) {
        // Inflate the post layout
        View postView = getLayoutInflater().inflate(R.layout.posts, null);

        // Find the ImageView and TextView in the post layout
        ImageView postImageView = postView.findViewById(R.id.userPostPicture);
        TextView postTextView = postView.findViewById(R.id.usernameText);

        // Set the image and text
        postImageView.setImageBitmap(profilePictureBitmap);
        postTextView.setText("User's dynamically generated post text");

        // Add the post layout to the userPostText layout
        userPostText.addView(postView);
    }
}
