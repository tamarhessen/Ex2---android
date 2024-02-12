package com.example.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class FeedActivity extends AppCompatActivity {

    private static final int REQUEST_NEW_POST = 1;
    TextView usernameTextView;
    LinearLayout userPostText; // Change from TextView to LinearLayout

    private Button menuButton;
    private Button addPostButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        // Initialize the views
        userPostText = findViewById(R.id.userPostText);
        menuButton = findViewById(R.id.menubtn);
        addPostButton = findViewById(R.id.addbtn);
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
                // Start NewPostActivity for result
                Intent intent = new Intent(FeedActivity.this, NewPostActivity.class);
                startActivityForResult(intent, REQUEST_NEW_POST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the result is from NewPostActivity and it was successful
        if (requestCode == REQUEST_NEW_POST && resultCode == RESULT_OK) {
            // Retrieve the post text and image bitmap from the result intent
            String postText = data.getStringExtra("postText");
            Bitmap postImageBitmap = data.getParcelableExtra("postImageBitmap");

            // Call the submitNewPost method to add the new post to the feed
            submitNewPost(postText, postImageBitmap);
        }
    }
    public boolean isBitmapEmpty(Bitmap bitmap) {
        return bitmap == null || bitmap.getWidth() == 0 || bitmap.getHeight() == 0;
    }

    private void submitNewPost(String postText, Bitmap postImageBitmap) {
        // Inflate the appropriate post layout based on whether the post has an image or not
        View postView;
        ImageView postImageView = null;
        if (!isBitmapEmpty(postImageBitmap)) {
            postView = getLayoutInflater().inflate(R.layout.photo_posts, null);
            postImageView = postView.findViewById(R.id.userPostPicture);
        } else {
            postView = getLayoutInflater().inflate(R.layout.writing_posts, null);
        }

        // Find the views in the post layout
        ImageView profilePic = postView.findViewById(R.id.profilePic);
        TextView postTextView = postView.findViewById(R.id.userPostText);
        TextView usernameTextView = postView.findViewById(R.id.usernameText);
        TextView usernameCommentTextView = postView.findViewById(R.id.usernameCaption);

        // Set profile picture bitmap if available
        Bitmap profilePictureBitmap = CreateAccountActivity.profilePictureBitmap;
        if (profilePictureBitmap != null) {
            profilePic.setImageBitmap(profilePictureBitmap);
        }

        // Find usernameText within postView
        String usernameText = "";
        List<UserCredentials.User> userList = UserCredentials.getUsers();
        for (UserCredentials.User user : userList) {
            String username = user.getUsername();
            usernameTextView.setText(username);
            username = username + ": ";
            usernameCommentTextView.setText(username);
            // Set username to usernameTextView
        }

        // Set post image and text

        postTextView.setText(postText);

        // Add the post layout to the userPostContainer layout
        LinearLayout userPostContainer = findViewById(R.id.postsContainer); // Find userPostContainer in the main layout
        userPostContainer.addView(postView); // Add the new post at the top of the feed
    }


}