package com.example.login.facebookdesign;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.login.R;
import com.example.login.API.WebServiceAPI;
import com.example.login.viewModels.UsersViewModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateAccountActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    public static Bitmap profilePictureBitmap;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private CheckBox checkBoxTermsConditions;
    private Button buttonSignUp;
    private Button buttonSelectImage;
    private EditText editTextConfirmPassword;
    private EditText editTextDisplayName;
    private ImageView imageViewProfilePicture;
    private String displayName;
    private Retrofit retrofit;
    private UsersViewModel usersViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_account);

        // Initialize views
        editTextUsername = findViewById(R.id.edit_username);
        editTextPassword = findViewById(R.id.edit_password);
        editTextConfirmPassword = findViewById(R.id.edit_confirm_password);
        editTextDisplayName = findViewById(R.id.edit_display_name);
        checkBoxTermsConditions = findViewById(R.id.check_terms_conditions);
        buttonSignUp = findViewById(R.id.btn_sign_up);
        buttonSelectImage = findViewById(R.id.btn_select_image);
        imageViewProfilePicture = findViewById(R.id.image_profile_picture);
        usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);
        editTextUsername = findViewById(R.id.edit_username);
        editTextPassword = findViewById(R.id.edit_password);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        // Set click listener for uploading image
        buttonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });
    }

    private void openImageChooser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an option");
        builder.setItems(new CharSequence[]{"Choose from Gallery", "Take Photo"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        // Choose from Gallery
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
                        break;
                    case 1:
                        // Take Photo
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                        } else {
                            Toast.makeText(CreateAccountActivity.this, "Camera not available", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageViewProfilePicture.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageViewProfilePicture.setImageBitmap(imageBitmap);
        }
    }

    private void signUp() {
        // Get user input
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();
        displayName = editTextDisplayName.getText().toString().trim();
        boolean agreeTerms = checkBoxTermsConditions.isChecked();
        boolean pictureUploaded = (imageViewProfilePicture.getDrawable() != null);

        // Clear any previous error messages
        clearErrors();

        // Validate input
        if (!validateInput(username,password, confirmPassword, pictureUploaded, agreeTerms)) {
            return; // Validation failed
        }

        // Check if all fields are filled in
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || displayName.isEmpty() || !agreeTerms || !pictureUploaded) {
            // Show a toast or error message indicating that all fields are required
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if image is uploaded before retrieving it
        Bitmap profilePictureBitmap = null;
        if (pictureUploaded) {
            profilePictureBitmap = ((BitmapDrawable) imageViewProfilePicture.getDrawable()).getBitmap();
        }

        // Convert bitmap to base64 string
        String profilePic = bitmapToBase64(profilePictureBitmap);
        Log.d("CreateAccountActivity", profilePic);

        // Sign up using ViewModel
        UserCreatePost userCreatePost = new UserCreatePost(username, password, displayName, profilePic);
        usersViewModel.add(userCreatePost, this);
    }






    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap,100,100,false);
        bitmap1.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }


    private boolean validateInput(String username,String password, String confirmPassword, boolean pictureUploaded, boolean agreeTerms) {
        boolean isValid = true;

        if (username.isEmpty()) {
            editTextUsername.setError("Username is required");
            isValid = false;
        } else {
            editTextUsername.setError(null); // Clear the error if validation passes
        }
        // Validate password
        if (password.length() < 8 || !password.matches(".*[a-zA-Z].*") || !password.matches(".*\\d.*")) {
            editTextPassword.setError("Password must be at least 8 characters long, contain at least one letter, and at least one digit");
            isValid = false;
        } else {
            editTextPassword.setError(null); // Clear the error if validation passes
        }

        // Validate confirm password
        if (!confirmPassword.equals(password)) {
            editTextConfirmPassword.setError("Passwords do not match");
            isValid = false;
        } else {
            editTextConfirmPassword.setError(null); // Clear the error if validation passes
        }

        // Validate display name
        if (displayName.isEmpty()) {
            editTextDisplayName.setError("Display name is required");
            isValid = false;
        } else {
            editTextDisplayName.setError(null); // Clear the error if validation passes
        }

        // Clear errors for picture and terms if validation passes
        // Validate profile picture
        if (!pictureUploaded) {
            findViewById(R.id.error_profile_picture).setVisibility(View.VISIBLE);
            isValid = false;
        } else {
            findViewById(R.id.error_profile_picture).setVisibility(View.GONE);
        }

        if (agreeTerms) {
            checkBoxTermsConditions.setError(null);
        } else {
            checkBoxTermsConditions.setError("Please agree to the terms and conditions");
            isValid = false;
        }

        return isValid; // Return whether all validations passed
    }


    // Method to clear error messages
    private void clearErrors() {
        editTextPassword.setError(null);
        editTextConfirmPassword.setError(null);
        editTextDisplayName.setError(null);
        editTextUsername.setError(null);
    }
}