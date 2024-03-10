package com.example.login.facebookdesign;

import static com.example.login.facebookdesign.MainActivity.defaultPfp;

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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.login.R;
import com.example.login.API.WebServiceAPI;
import com.example.login.viewModels.UsersViewModel;

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
    private String token;
    private UsersViewModel usersViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Initialize views
        initViews();
        usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);
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
        displayNameTextView.setText(displayName);
    }

    public static void setAsImage(String strBase64, ImageView imageView) {
        if(strBase64.equals(defaultPfp)){
//            imageView.setImageResource(R.drawable.defaultprofilepic);
        } else {
            byte[] decodedString = Base64.decode(strBase64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageView.setImageBitmap(decodedByte);
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