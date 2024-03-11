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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.login.R;
import com.example.login.facebookdesign.BitmapConverter;
import com.example.login.viewModels.UsersViewModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EditProfileDialogFragment extends DialogFragment {

    // Define keys for arguments
    private static final String ARG_USERNAME = "username";
    private static final String ARG_TOKEN = "token";
    private static final String currentDisplayName = "CurrentDisplayName";
    private static final String ARG_PROFILE_IMAGE = "profilepic";
    private static String profilePicBase64;

    // Method to create a new instance of EditProfileDialogFragment with arguments
    public static EditProfileDialogFragment newInstance(String username, String token, String displayName,String profilePic) {
        EditProfileDialogFragment fragment = new EditProfileDialogFragment();
        Bundle args = new Bundle();
        args.putString("Username", username);
        args.putString("Token", token);
        args.putString("CurrentDisplayName", currentDisplayName);
        args.putString("ProfilePicBase64", profilePicBase64);
        fragment.setArguments(args);
        return fragment;
    }

    // Rest of the class implementation remains the same..


    private EditText editTextDisplayName;
    private Button btnSave, btnCancel;
    private UsersViewModel usersViewModel;
    private Bitmap newProfilePic;
    private ImageButton btnEditImage;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PICK_IMAGE_REQUEST = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_profile_dialog, container, false);
        usersViewModel = new ViewModelProvider(requireActivity()).get(UsersViewModel.class);

        // Initialize views
        editTextDisplayName = view.findViewById(R.id.edit_text_display_name);
        btnSave = view.findViewById(R.id.btn_save);
        btnCancel = view.findViewById(R.id.btn_cancel);
        btnEditImage = view.findViewById(R.id.btn_profile_picture);

        // Retrieve the display name from arguments and set it to the editTextDisplayName
        assert getArguments() != null;
        String displayName = getArguments().getString("Enter new display name");
        editTextDisplayName.setText(displayName);

        // Set click listeners
        btnEditImage.setOnClickListener(v -> openImageChooser());
        btnSave.setOnClickListener(v -> saveChanges());
        btnCancel.setOnClickListener(v -> dismiss());

        return view;
    }

    // Method to open camera or gallery
    private void openImageChooser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Choose an option");
        builder.setItems(new CharSequence[]{"Choose from Gallery", "Take Photo"}, (dialog, which) -> {
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
        });
        builder.show();
    }
    private byte[] compressImage(Bitmap imageBitmap) {
        if(imageBitmap==null){
            return null;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream); // Adjust quality as needed
        return outputStream.toByteArray();
    }
    private void saveChanges() {
        String displayName = editTextDisplayName.getText().toString().trim();
        if (displayName.isEmpty()) {
            // Display a toast message indicating that display name cannot be empty
            Toast.makeText(requireContext(), "Display name cannot be empty", Toast.LENGTH_SHORT).show();
            return; // Exit the method early
        }

        // Check if a new profile picture is selected
        if (newProfilePic != null) {
            // If a new profile picture is selected, convert it to base64 string
            byte[] compressedImage = compressImage(newProfilePic);
            String profilePicBase64 = BitmapConverter.bitmapToString(BitmapConverter.toBitmap(compressedImage));
            // Call the editUser method of the UsersViewModel with updated display name and profile picture
            usersViewModel.editUser(displayName, profilePicBase64);
        } else {
            // If no new profile picture is selected, call the editUser method with only the updated display name
            usersViewModel.editUser(displayName,"");
        }

        // Dismiss the dialog
        dismiss();
        Intent intent = new Intent(requireContext(),LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear the back stack
        startActivity(intent);
        requireActivity().finish();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    // Handle image captured from camera
                    if (data != null && data.getExtras() != null) {
                        newProfilePic = (Bitmap) data.getExtras().get("data");
                        // Set the new profile picture to the ImageView
                        btnEditImage.setImageBitmap(newProfilePic);
                    }
                    break;
                case PICK_IMAGE_REQUEST:
                    // Handle image picked from gallery
                    if (data != null && data.getData() != null) {
                        // Get the URI of the selected image
                        Uri imageUri = data.getData();
                        try {
                            // Convert the URI to a Bitmap
                            newProfilePic = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
                            // Set the new profile picture to the ImageView
                            btnEditImage.setImageBitmap(newProfilePic);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }

}
