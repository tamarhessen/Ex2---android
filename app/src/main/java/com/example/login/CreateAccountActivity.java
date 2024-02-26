package com.example.login;

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

import com.example.login.UserCreatePost;
import com.example.login.network.WebServiceAPI;
import com.example.login.network.RetrofitClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateAccountActivity extends Activity {

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
        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
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
        profilePictureBitmap = ((BitmapDrawable) imageViewProfilePicture.getDrawable()).getBitmap();
        String profilePic = bitmapToBase64(profilePictureBitmap);
        Log.d("CreateAccountActivity", profilePic);

        // Clear any previous error messages
        clearErrors();

        // Validate input
        if (!validateInput(password, confirmPassword, pictureUploaded, agreeTerms)) {
            return; // Validation failed
        }
        UserCreateToken userCreateToken = new UserCreateToken(username, password);
        // Create an instance of UserCreatePost and populate it with data
        UserCreatePost userCreatePost = new UserCreatePost(username, password, displayName, profilePic);

        // Create an instance of WebServiceAPI
        WebServiceAPI webServiceAPI = retrofit.create(WebServiceAPI.class);

        // Make network request to create a user
        Call<Void> call = webServiceAPI.createUser(userCreatePost);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Display success message
                    Toast.makeText(CreateAccountActivity.this, "Sign-up successful", Toast.LENGTH_SHORT).show();
                    // Proceed to login activity
                    Intent intent = new Intent(CreateAccountActivity.this, LogInActivity.class);
                    startActivity(intent);
                    finish(); // Optional: finish the current activity to remove it from the back stack
                } else {
                    // Handle error
                    Toast.makeText(CreateAccountActivity.this, "Failed to create user", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
                Toast.makeText(CreateAccountActivity.this, "Network request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap,100,100,false);
        bitmap1.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }


    private boolean validateInput(String password, String confirmPassword, boolean pictureUploaded, boolean agreeTerms) {
        // Validate password
        if (password.length() < 8) {
            editTextPassword.setError("Password must be at least 8 characters long");
            editTextPassword.requestFocus();
            return false;
        }
        if (!password.matches(".*[a-zA-Z].*")) {
            editTextPassword.setError("Password must contain at least one letter");
            editTextPassword.requestFocus();
            return false;
        }
        if (!password.matches(".*\\d.*")) {
            editTextPassword.setError("Password must contain at least one digit");
            editTextPassword.requestFocus();
            return false;
        }

        // Validate confirm password
        if (!confirmPassword.equals(password)) {
            editTextConfirmPassword.setError("Passwords do not match");
            editTextConfirmPassword.requestFocus();
            return false;
        }

        // Validate display name
        if (displayName.isEmpty()) {
            editTextDisplayName.setError("Display name is required");
            editTextDisplayName.requestFocus();
            return false;
        }

        // Validate profile picture
        if (!pictureUploaded) {
            Toast.makeText(CreateAccountActivity.this, "Please upload a profile picture", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate terms and conditions agreement
        if (!agreeTerms) {
            Toast.makeText(CreateAccountActivity.this, "Please agree to the terms and conditions", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true; // All validations passed
    }

    // Method to clear error messages
    private void clearErrors() {
        editTextPassword.setError(null);
        editTextConfirmPassword.setError(null);
        editTextDisplayName.setError(null);
    }
}
