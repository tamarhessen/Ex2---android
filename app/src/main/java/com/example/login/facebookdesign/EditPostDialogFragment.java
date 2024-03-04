package com.example.login.facebookdesign;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.login.R;
import com.example.login.facebookdesign.Post;

import java.io.IOException;

public class EditPostDialogFragment extends DialogFragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Post post;
    private EditText editText;
    private ImageView imageView;
    private OnPostEditedListener onPostEditedListener;

    public EditPostDialogFragment(Post post, OnPostEditedListener listener) {
        this.post = post;
        this.onPostEditedListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_post_dialog, container, false);
        editText = view.findViewById(R.id.contentEditText);
        imageView = view.findViewById(R.id.postImageView);

        // Populate the fields with the current post details
        editText.setText(post.getPostText());

        imageView.setImageBitmap(BitmapConverter.stringToBitmap(post.getPostImg
                ()));

        // Set click listener for the image view to allow changing the picture
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });

        // Handle the save button click to update the post details
        Button saveButton = view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update the post details based on the user's input
                String updatedContent = editText.getText().toString();
                // Update the post content
                post.setPostText(updatedContent);
                // Notify the listener that the post is edited
                onPostEditedListener.onPostEdited(post);
                // Dismiss the dialog
                dismiss();
            }
        });
        Button chooseImageButton = view.findViewById(R.id.chooseImageButton);
        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });

        return view;
    }

    private void choosePicture() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            try {
                // Set the chosen image to the image view
                imageView.setImageURI(imageUri);
                // Update the post's picture
                post.setPostImg
                        (BitmapConverter.bitmapToString(MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public interface OnPostEditedListener {
        void onPostEdited(Post post);
    }
}
