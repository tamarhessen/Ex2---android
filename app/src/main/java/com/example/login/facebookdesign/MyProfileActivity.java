package com.example.login.facebookdesign;

import static com.example.login.facebookdesign.MainActivity.defaultPfp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
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

public class MyProfileActivity extends AppCompatActivity {

    private ImageView profilePictureImageView;
    private TextView displayNameTextView;
    private UsersViewModel usersViewModel;
    private String username;
    private String token;
    private String displayName;
    private Button editProfile;
    private ImageButton exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile_page);

        // Initialize views
        profilePictureImageView = findViewById(R.id.profile_picture);
        displayNameTextView = findViewById(R.id.user_name);
        editProfile = findViewById(R.id.btn_edit_profile);
        exitButton = findViewById(R.id.btn_exit);

        // Initialize UsersViewModel
        usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);

        // Set click listener for edit button
        editProfile.setOnClickListener(v -> {
            // Open edit profile dialog fragment
            openEditProfileDialog();
        });
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Navigate back to the previous activity
            }
        });


        // Fetch user data
        fetchUserData();

        // Retrieve the profile picture byte array from Intent extras
        byte[] profilePictureByteArray = getIntent().getByteArrayExtra("ProfilePicture");
        if (profilePictureByteArray != null) {
            // Convert the byte array to Bitmap and set it to the profile picture ImageView
            Bitmap bitmap = BitmapFactory.decodeByteArray(profilePictureByteArray, 0, profilePictureByteArray.length);
            profilePictureImageView.setImageBitmap(bitmap);
        }
    }

    private void openEditProfileDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        // Retrieve user data
        String username = getIntent().getStringExtra("Username");
        String token = getIntent().getStringExtra("Token");
        String displayName = getIntent().getStringExtra("DisplayName");
        String profilePicBase64 = getIntent().getStringExtra("ProfilePicBase64"); // Assuming you have a key for profile picture
        Bitmap profilePicBitmap = null;
        if (profilePicBase64 != null) {
            byte[] decodedString = Base64.decode(profilePicBase64, Base64.DEFAULT);
            profilePicBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        }
        // Create an instance of EditProfileDialogFragment with arguments
        EditProfileDialogFragment editProfileDialogFragment = EditProfileDialogFragment.newInstance(username, token, displayName, BitmapConverter.bitmapToString(profilePicBitmap));
        // Show the dialog
        editProfileDialogFragment.show(fragmentManager, "EditProfileDialogFragment");

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
