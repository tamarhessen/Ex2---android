package com.example.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddPostActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 1;

    private ImageView selectedImageView;
    private EditText editText;
    private Button selectImageButton;
    private Button postButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        selectedImageView = findViewById(R.id.postImageView);
        editText = findViewById(R.id.postEditText);
        selectImageButton = findViewById(R.id.selectImageButton);
        postButton = findViewById(R.id.submitButton);

        selectImageButton.setOnClickListener(v -> openGallery());

        postButton.setOnClickListener(v -> postContent());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    private void postContent() {
        // Retrieve the entered text from the EditText
        String text = editText.getText().toString();

        // Perform actions to post the content (e.g., upload to server)

        // Set the result intent to send back to FeedActivity
        int imageResource = R.drawable.leaf_post; // Change this to the actual image resource
        String postText = text; // Use the retrieved text

        Intent resultIntent = new Intent();
        resultIntent.putExtra("imageResource", imageResource);
        resultIntent.putExtra("postText", postText);
        setResult(RESULT_OK, resultIntent);

        // Finish the activity
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            // Handle the selected image URI
            Uri imageUri = data.getData();
            Intent resultIntent = new Intent();
            resultIntent.putExtra("imageUri", imageUri.toString());
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }
}
