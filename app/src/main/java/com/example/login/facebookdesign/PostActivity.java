package com.example.login.facebookdesign;
import static com.example.login.facebookdesign.UsersActivity.currentConnectedUsername;
import static com.example.login.facebookdesign.UsersActivity.setAsImage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.login.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PostActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;

    private ImageView postImageView;
    private static CommentsViewModel viewModel;
    private EditText postEditText;
    private Button selectImageButton;
    private Button submitButton;
    public static String talkingUser;
    private int id;
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
                            Toast.makeText(PostActivity.this, "Camera not available", Toast.LENGTH_SHORT).show();
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
                postImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            postImageView.setImageBitmap(imageBitmap);
        }
    }


    // Method to save bitmap to a file and return the file path
    // Method to save bitmap to a file and return the file path
    private String saveBitmapToFile(Bitmap bitmap) {
        // Create a file to save the bitmap
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "post_image.jpg");

        try {
            // Compress the bitmap and save it to the file
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Return the file path
        return file.getAbsolutePath();
    }

    // Method to submit the post
    private void submitPost() {
        // Get the post text from EditText
        String postText = postEditText.getText().toString();

        // Get the post image bitmap from ImageView
        Bitmap postImageBitmap = null;
        Drawable drawable = postImageView.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            postImageBitmap = ((BitmapDrawable) drawable).getBitmap();
        }

        // Check if both post text and image are available
        if (!TextUtils.isEmpty(postText) || postImageBitmap != null) {
            if (postImageBitmap != null) {
                // Save the post image bitmap to a file
                String imagePath = saveBitmapToFile(postImageBitmap);
                // Pass the post text and image file path back to the calling activity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("postText", postText);
                resultIntent.putExtra("postImagePath", imagePath);
                // Set the result to indicate successful submission
                setResult(RESULT_OK, resultIntent);
            } else {
                // If no image is selected, pass only the post text
                Intent resultIntent = new Intent();
                resultIntent.putExtra("postText", postText);
                setResult(RESULT_OK, resultIntent);
            }
            finish(); // Finish the activity
        } else {
            // Show a toast message if either post text or image is missing
            Toast.makeText(PostActivity.this, "Please select an image and write something", Toast.LENGTH_SHORT).show();
        }
    }

    public static void update(){
        Log.d("update_abcde","update");
        if (viewModel != null) {
            viewModel.refresh();
        }
    }
}
