package com.example.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.wifi.hotspot2.pps.Credential;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class CreateAccountActivity extends Activity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;

    private EditText editTextUsername;
    private EditText editTextPassword;
    private CheckBox checkBoxTermsConditions;
    private Button buttonSignUp;
    private Button buttonSelectImage;
    private EditText editTextConfirmPassword;
    private EditText editTextDisplayName;
    private ImageView imageViewProfilePicture;

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

        // Set click listener for the Sign Up button
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
        String displayName = editTextDisplayName.getText().toString().trim();
        boolean agreeTerms = checkBoxTermsConditions.isChecked();

        // Clear any previous error messages
        clearErrors();
        if (UserCredentials.isUsernameExists(username)) {
            // Username already exists, show an error message
            editTextUsername.setError("Username already exists");
            editTextUsername.requestFocus();
            return;
        }
        // Validate user input
        if (username.isEmpty()) {
            editTextUsername.setError("Username is required");
            editTextUsername.requestFocus();
            return;
        }

        // Validate password
        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length() < 8) {
            editTextPassword.setError("Password must be at least 8 characters long");
            editTextPassword.requestFocus();
            return;
        }
        if (!password.matches(".*[a-zA-Z].*")) {
            editTextPassword.setError("Password must contain at least one letter");
            editTextPassword.requestFocus();
            return;
        }
        if (!password.matches(".*\\d.*")) {
            editTextPassword.setError("Password must contain at least one digit");
            editTextPassword.requestFocus();
            return;
        }

        // Validate confirm password
        if (confirmPassword.isEmpty()) {
            editTextConfirmPassword.setError("Confirm Password is required");
            editTextConfirmPassword.requestFocus();
            return;
        }
        if (!confirmPassword.equals(password)) {
            editTextConfirmPassword.setError("Passwords do not match");
            editTextConfirmPassword.requestFocus();
            return;
        }

        // Validate display name
        if (displayName.isEmpty()) {
            editTextDisplayName.setError("Display name is required");
            editTextDisplayName.requestFocus();
            return;
        }

        if (!agreeTerms) {
            Toast.makeText(CreateAccountActivity.this, "Please agree to the terms and conditions", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add user to the global list
        UserCredentials.addUser(username, password);

        // Display success message
        Toast.makeText(CreateAccountActivity.this, "Sign-up successful", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(CreateAccountActivity.this,LogInActivity.class);
        startActivity(intent);
        finish(); // Optional: finish the current activity to remove it from the back stack
    }

    // Method to clear error messages
    private void clearErrors() {
        editTextUsername.setError(null);
        editTextPassword.setError(null);
        editTextConfirmPassword.setError(null);
        editTextDisplayName.setError(null);
    }
}
