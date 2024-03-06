package com.example.login.facebookdesign;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.login.R;

public class EditProfileDialogFragment extends DialogFragment {

    private EditText editTextDisplayName;
    private Button btnSave, btnCancel, btnEditImage;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_profile_dialog, container, false);

        // Initialize views
        editTextDisplayName = view.findViewById(R.id.edit_text_display_name);
        btnSave = view.findViewById(R.id.btn_save);
        btnCancel = view.findViewById(R.id.btn_cancel);
        btnEditImage = view.findViewById(R.id.btn_profile_picture);

        // Set click listeners
        editTextDisplayName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open dialog or activity to edit display name
            }
        });

        btnEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open camera or gallery to change profile picture
                openImagePicker();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save changes
                String displayName = editTextDisplayName.getText().toString().trim();
                // Pass the edited display name to the calling activity/fragment
                if (getTargetFragment() != null) {
                    ((EditProfileDialogListener) getTargetFragment()).onSaveClicked(displayName);
                }
                dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cancel editing
                dismiss();
            }
        });

        return view;
    }

    // Method to open camera or gallery
    private void openImagePicker() {
        // Open camera
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

        // Open gallery
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhotoIntent.setType("image/*");
        startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_PICK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                // Handle image captured from camera
            } else if (requestCode == REQUEST_IMAGE_PICK) {
                // Handle image picked from gallery
            }
        }
    }

    // Interface for communication with the calling fragment/activity
    public interface EditProfileDialogListener {
        void onSaveClicked(String displayName);
    }
}