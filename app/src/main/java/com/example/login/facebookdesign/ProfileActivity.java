package com.example.login.facebookdesign;

import static com.example.login.facebookdesign.MainActivity.defaultPfp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.example.login.viewModels.PostsViewModel;
import com.example.login.viewModels.UsersViewModel;

public class ProfileActivity extends AppCompatActivity {

    private ImageView coverPhotoImageView;
    private ImageView profilePictureImageView;
    private TextView userNameTextView;
    private TextView numOfFriendsTextView;
    private PostsViewModel postsViewModel;
    private Button addFriendButton;
    private PostAdapter adapter;
    private Button sendMessageButton;
    private RecyclerView friendsRecyclerView;
    private RecyclerView postsRecyclerView;
    private String token;
    private String username;
    private String displayName;
    private UsersViewModel usersViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);

        // Initialize views
        coverPhotoImageView = findViewById(R.id.cover_photo);
        profilePictureImageView = findViewById(R.id.profile_picture);
        userNameTextView = findViewById(R.id.user_name);
        numOfFriendsTextView = findViewById(R.id.num_of_friends);
        addFriendButton = findViewById(R.id.btn_add_friend);
        sendMessageButton = findViewById(R.id.btn_send_message);
        friendsRecyclerView = findViewById(R.id.recycler_friends);
        postsRecyclerView = findViewById(R.id.recycler_posts);
        usersViewModel = new UsersViewModel();
        // Set up RecyclerViews
        setUpFriendsRecyclerView();
        setUpPostsRecyclerView();
fetchUserData();
        Intent activityIntent = getIntent();
        if (activityIntent != null) {
            token = activityIntent.getStringExtra("Token");
            username = activityIntent.getStringExtra("Username");

            // Initialize ViewModel with token
            postsViewModel = new ViewModelProvider(this).get(PostsViewModel.class);
            postsViewModel.setUsername(username);
            postsViewModel.setToken(token);



            // Initialize UsersViewModel

        } else {
            // Handle case where intent is null or token is not provided
            Toast.makeText(this, "Failed to get token", Toast.LENGTH_SHORT).show();
        }

        // Observe changes in posts data
        postsViewModel.getPosts().observe(this, posts -> {
            if (posts != null && !posts.isEmpty()) {
                adapter.setPosts(posts);
            }
        });

fetchAndDisplayPosts();
        // Add more setup code as needed
    }
    private void fetchUserData() {
        Intent activityIntent = getIntent();
        if (activityIntent != null) {
            token = activityIntent.getStringExtra("Token");
            username = activityIntent.getStringExtra("Username");
            usersViewModel.setToken(token);
            usersViewModel.setUserid(username);

            // Observe user data
            usersViewModel.getCurrentUser(username, token).observe(this, new Observer<UserCreatePost>() {
                @Override
                public void onChanged(UserCreatePost userCreatePost) {
                    // Display user data or handle the single userCreatePost object as needed
                    // Example: Set the profile picture
                    setProfilePicture(userCreatePost.getProfilePic());
                    setDisplayName(userCreatePost.getDisplayName());
                    displayName=userCreatePost.getDisplayName();
                }
            });
        }
    }
    private void setProfilePicture(String profilePicBase64) {
        if (profilePicBase64 != null && !profilePicBase64.equals(defaultPfp)) {
            byte[] decodedString = Base64.decode(profilePicBase64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            profilePictureImageView.setImageBitmap(decodedByte);
        } else {
            // Set a default profile picture if profilePicBase64 is null or defaultPfp
            profilePictureImageView.setImageResource(R.drawable.facebook_logo);
        }
    }

    private void setDisplayName(String displayName) {
        userNameTextView.setText(displayName);
    }
    private void setUpFriendsRecyclerView() {
        // Initialize and set layout manager for friends RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        friendsRecyclerView.setLayoutManager(layoutManager);
//
//        // Create and set adapter for friends RecyclerView
//        FriendsAdapter friendsAdapter = new FriendsAdapter(/* pass necessary parameters */);
//        friendsRecyclerView.setAdapter(friendsAdapter);
    }

    private void setUpPostsRecyclerView() {
        // Initialize and set layout manager for posts RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        postsRecyclerView.setLayoutManager(layoutManager);

        // Create and set adapter for posts RecyclerView
        adapter = new PostAdapter(this, username, postsViewModel, displayName);
        postsRecyclerView.setAdapter(adapter);
    }


    private void fetchAndDisplayPosts() {
        // Observe changes in posts data
        postsViewModel.getPostsforUserName().observe(this, posts -> {
            if (posts != null && !posts.isEmpty()) {
                // Update RecyclerView adapter with fetched posts
                adapter.setPosts(posts);
            } else {
                Toast.makeText(ProfileActivity .this, "No posts found", Toast.LENGTH_SHORT).show();
            }
        });
    }

}