package com.example.login.facebookdesign;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.login.R;
import com.example.login.facebookdesign.BitmapConverter;
import com.example.login.viewModels.UsersViewModel;

import java.io.IOException;

public class EditProfileDialogFragment extends DialogFragment {

    private EditText editTextDisplayName;
    private Button btnSave, btnCancel, btnEditImage;
    private UsersViewModel usersViewModel;
    private Bitmap newProfilePic;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private static final int PICK_IMAGE_REQUEST = 3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_profile_dialog, container, false);
        usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);
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
                openImageChooser();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save changes
                String displayName = editTextDisplayName.getText().toString().trim();
                // Call the edit method of usersViewModel with the updated display name
                usersViewModel.editUser(displayName, BitmapConverter.bitmapToString(newProfilePic));

                // Pass the edited display name to the calling activity/fragment if needed
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
    private void openImageChooser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
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
                        if (cameraIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                        } else {
                            Toast.makeText(requireContext(), "Camera not available", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    // Handle image captured from camera
                    break;
                case PICK_IMAGE_REQUEST:
                    // Handle image picked from gallery
                    if (data != null && data.getData() != null) {
                        // Get the URI of the selected image
                        Uri imageUri = data.getData();
                        try {
                            // Convert the URI to a Bitmap
                            newProfilePic = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
                            // Now you have the Bitmap of the selected image, you can use it as the profile picture
                            // For example, you can display it in an ImageView or save it to a variable
                            // Save the profilePicBitmap to a variable or pass it to a method for further processing
                            // For example:
                            // usersViewModel.updateProfilePicture(profilePicBitmap);
                            // or
                            // saveProfilePicture(profilePicBitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }

    // Interface for communication with the calling fragment/activity
    public interface EditProfileDialogListener {
        void onSaveClicked(String displayName);
    }
}
