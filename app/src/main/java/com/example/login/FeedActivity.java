package com.example.login;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.login.adapters.PostsListAdapter;
import com.example.login.entities.Post;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {
    private static final int REQUEST_NEW_POST = 1;
    private Button menuButton;
    private Button newPostButton;
    private Button whatsNewButton;
    private RecyclerView lstPosts;
    private PostsListAdapter adapter;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        // Find the menu button and set click listener
        menuButton = findViewById(R.id.menubtn);
        newPostButton = findViewById(R.id.addbtn);
        whatsNewButton = findViewById(R.id.whatsNewButton);
        UserViewModel userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                // Assuming you have a method to retrieve the profile picture from the User object
                byte[] profilePictureData = user.getProfilePictureData();
                if (profilePictureData != null) {
                    // Convert byte array to Bitmap
                    Bitmap profilePictureBitmap = BitmapFactory.decodeByteArray(profilePictureData, 0, profilePictureData.length);
                    // Display the profile picture in ImageView
                    ImageView profilePictureImageView = findViewById(R.id.image_profile_picture);
                    profilePictureImageView.setImageBitmap(profilePictureBitmap);
                }
            }
        });
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refreshLayout);

        // Disable swipe-to-refresh functionality
        swipeRefreshLayout.setEnabled(false);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the MenuActivity when the button is clicked
                Intent intent = new Intent(FeedActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });
        newPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeedActivity.this, NewPostActivity.class);
                startActivityForResult(intent, REQUEST_NEW_POST); // Start activity for result
            }
        });
        whatsNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeedActivity.this, NewPostActivity.class);
                startActivityForResult(intent, REQUEST_NEW_POST); // Start activity for result
            }
        });

        // Set up RecyclerView and adapter
        lstPosts = findViewById(R.id.lstPosts);
        adapter = new PostsListAdapter(this, username);
        lstPosts.setAdapter(adapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));


        // Create sample posts
        // Load posts from JSON file and pass them to the adapter

        List<Post> posts = JsonParser.parseJson(this);


        // Pass the list of posts to the adapter
        adapter.setPosts(posts);

    }

    // This method will be called when returning from NewPostActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_NEW_POST && resultCode == RESULT_OK && data != null) {
            // Retrieve post data from NewPostActivity
            String postText = data.getStringExtra("postText");
            String postImagePath = data.getStringExtra("postImagePath");
            Bitmap profileImageBitmap = CreateAccountActivity.profilePictureBitmap; // Assuming you have already stored the profile picture bitmap
            Bitmap postImageBitmap = BitmapFactory.decodeFile(postImagePath);
            long currentTimeMillis = System.currentTimeMillis();
            // Create a new Post object
            Post newPost = new Post(username, postText, postImageBitmap, 0, profileImageBitmap,currentTimeMillis);

            // Add the new post to the adapter
            adapter.addPost(newPost);
        }
    }



}