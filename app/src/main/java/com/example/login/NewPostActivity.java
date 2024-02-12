package com.example.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class NewPostActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;

    private ImageView postImageView;
    private EditText postEditText;
    private Button selectImageButton;
    private Button submitButton;

    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        // Initialize views
        postImageView = findViewById(R.id.postImageView);
        postEditText = findViewById(R.id.postEditText);
        selectImageButton = findViewById(R.id.selectImageButton);
        submitButton = findViewById(R.id.submitButton);

        // Set click listener for selecting image
        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        // Set click listener for submitting the post
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });
    }

    // Method to open image chooser dialog
    private void openImageChooser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an option");
        builder.setItems(new CharSequence[]{"Take Photo"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        // Take Photo
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                        } else {
                            Toast.makeText(NewPostActivity.this, "Camera not available", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        });
        builder.show();
    }

    // Method to capture image from camera
    private void captureImage() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(NewPostActivity.this, "Camera not available", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to handle the result of image capture
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && data != null && data.getExtras() != null) {
                handleCapturedImage(data);
            }
        }
    }

    // Method to handle the captured image from camera
    private void handleCapturedImage(Intent data) {
        // Get the captured image as a Bitmap
        Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
        if (imageBitmap != null) {
            // Set the captured image bitmap to the profile picture bitmap static variable
            CreateAccountActivity.profilePictureBitmap = imageBitmap;

            // Optionally, you can set the captured image bitmap to the image view for preview
            postImageView.setImageBitmap(imageBitmap);
            postImageView.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(NewPostActivity.this, "Error: Failed to capture image", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to submit the post
    private void submitPost() {
        // Get the post text from EditText
        String postText = postEditText.getText().toString();

        // Get the post image bitmap from ImageView
        Bitmap postImageBitmap = null;
        if (postImageView.getDrawable() != null) {
            postImageBitmap = ((BitmapDrawable) postImageView.getDrawable()).getBitmap();
        }

        // Check if both post text and image are available
        if (!TextUtils.isEmpty(postText) && postImageBitmap != null) {
            // Pass the post text and image bitmap back to the calling activity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("postText", postText);
            resultIntent.putExtra("postImageBitmap", postImageBitmap);

            // Set the result to indicate successful submission
            setResult(RESULT_OK, resultIntent);
            finish(); // Finish the activity
        } else {
            // Show a toast message if either post text or image is missing
            Toast.makeText(NewPostActivity.this, "Please select an image and write something", Toast.LENGTH_SHORT).show();
        }
    }
}