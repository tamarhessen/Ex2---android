package com.example.login.facebookdesign;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.login.R;
import com.example.login.network.WebServiceAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MenuActivity extends AppCompatActivity {
    private Button homeButton;
    private Button logoutButton;
    private Button settingsButton;
    private ImageView profilePictureImageView;
    private TextView displayNameTextView;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Initialize views
        initViews();

        // Fetch user data
        fetchUserData();

        // Set click listeners for buttons
        setClickListeners();
    }

    private void initViews() {
        profilePictureImageView = findViewById(R.id.image_profile_picture);
        homeButton = findViewById(R.id.homeimg);
        logoutButton = findViewById(R.id.logOutBtn);
        settingsButton = findViewById(R.id.settings);
        displayNameTextView = findViewById(R.id.displayName);
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

    private void setClickListeners() {
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Navigate back to the previous activity
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the LogInActivity when the logout button is clicked
                Intent intent = new Intent(MenuActivity.this, LogInActivity.class);
                startActivity(intent);
                // Finish the current activity if needed
                finish();
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the SettingsActivity when the settings button is clicked
                Intent intent = new Intent(MenuActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }
}
