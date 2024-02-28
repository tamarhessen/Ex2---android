package com.example.login.facebookdesign;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.example.login.network.WebServiceAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyProfileActivity  extends AppCompatActivity {

    private ImageView profilePictureImageView;
    private TextView displayNameTextView;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile_page);
        profilePictureImageView = findViewById(R.id.profile_picture);
        displayNameTextView = findViewById(R.id.user_name);

        // Assuming you have initialized RecyclerViews for friends and posts
        RecyclerView recyclerViewFriends = findViewById(R.id.recycler_friends);
        RecyclerView recyclerViewPosts = findViewById(R.id.recycler_posts);

        // Set layout manager for RecyclerViews
        recyclerViewFriends.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPosts.setLayoutManager(new LinearLayoutManager(this));
        // Fetch user data
        fetchUserData();
//        // Create adapters for RecyclerViews and set them
//        FriendsAdapter friendsAdapter = new FriendsAdapter(/* pass data here if needed */);
//        recyclerViewFriends.setAdapter(friendsAdapter);

//        PostAdapter postsAdapter = new PostAdapter(/* pass data here if needed */);
//        recyclerViewPosts.setAdapter(postsAdapter);

        // Add more initialization code as needed for buttons, text views, etc.
    }
    private void fetchUserData() {
        Intent activityIntent = getIntent();
        if (activityIntent != null) {
            // Check if username is passed from the previous activity
            username = activityIntent.getStringExtra("Username");
        }

        // Fetch user data from server if username is available
        if (username != null) {
            Retrofit retrofit = new Retrofit.Builder().baseUrl(MainActivity.baseURL)
                    .addConverterFactory(GsonConverterFactory.create()).build();
            WebServiceAPI webServiceAPI = retrofit.create(WebServiceAPI.class);
            Call<UserCreatePost> call = webServiceAPI.getUser(username,
                    "Bearer " + activityIntent.getStringExtra("Token"));
            call.enqueue(new Callback<UserCreatePost>() {
                @Override
                public void onResponse(Call<UserCreatePost> call, Response<UserCreatePost> response) {
                    if (response.isSuccessful()) {
                        UserCreatePost user = response.body();
                        Log.d("MenuActivity", "User data fetched successfully: " + user.toString());
                        // Update views with fetched data
                        updateViews(user);
                    } else {
                        Log.e("MenuActivity", "Failed to fetch user data: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<UserCreatePost> call, Throwable t) {
                    Log.e("MenuActivity", "Error fetching user data: " + t.getMessage());
                }
            });
        }
    }

    private void updateViews(UserCreatePost user) {
        if (user != null) {
            // Set display name
            displayNameTextView.setText(user.getDisplayName());

            // Set profile picture
            String profilePic = user.getProfilePic();
            if (profilePic != null && !profilePic.equals(MainActivity.defaultPfp)) {
                byte[] decodedString = Base64.decode(profilePic, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                profilePictureImageView.setImageBitmap(decodedByte);
            }
        }
    }

}
