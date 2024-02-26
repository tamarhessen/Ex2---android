package com.example.login.facebookdesign;

import static com.example.login.facebookdesign.SettingsActivity.applyDarkMode;
import static com.google.firebase.messaging.Constants.TAG;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;



public class MainActivity extends AppCompatActivity {
    public static SocketIOManager SIM = new SocketIOManager();

    //private static final int PERMISSION_REQUEST_CODE = 123;
//    private EditText usernameEditText, passwordEditText, passwordValidationEditText, displayNameEditText;
//    private TextView errors;
//    private ImageView profilePictureImageView;
//    private Uri selectedImageUri = null;

    public static String defaultPfp = "https://images-na.ssl-images-amazon.com/images/I/51e6kpkyuIL._AC_SX466_.jpg";
    //    private Bitmap bitmap;
    public static String baseURL = "http://10.0.2.2:5000/api/";
//    private ActivityResultLauncher<Intent> imagePickerLauncher;
//    private final ActivityResultLauncher<String[]> imagePickerLauncher =
//            registerForActivityResult(new ActivityResultContracts.OpenDocument(), result -> {
//                if (result != null) {
//                    selectedImageUri = result;
//                    try {
//                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
//                        profilePictureImageView.setVisibility(View.VISIBLE);
//                        profilePictureImageView.setImageBitmap(bitmap);
//                    } catch (IOException e) {
//                        Log.e("MainActivity", "Error loading image: " + e.getMessage());
//                    }
//                }
//            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        createNotificationChannel();
        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        boolean isDarkModeEnabled = sharedPreferences.getBoolean("dark_mode_enabled", false);
        applyDarkMode(isDarkModeEnabled);
        SharedPreferences sharedPreferences1 = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_main);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful()) {
                            // Get new FCM registration token
                            String token = task.getResult();

                            // Log and toast
                            String username = sharedPreferences1.getString("username", "");
                            String msg = getString(R.string.msg_token_fmt, token);
                            Log.d(TAG, msg);
                            SIM.sendToken(token, username);
                            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                        } else {
                            // Handle failure
                            Exception exception = task.getException();
                            Log.w(TAG, "Fetching FCM registration token failed", exception);
                            Toast.makeText(MainActivity.this, "Failed to retrieve FCM registration token", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
//        FirebaseMessaging.getInstance().getToken()
//                .addOnCompleteListener(new OnCompleteListener<String>() {
//                    @Override
//                    public void onComplete(@NonNull Task<String> task) {
//                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
//                            return;
//                        }
//
//                        // Get new FCM registration token
//                        String token = task.getResult();
//
//                        // Log and toast
//                        String username = sharedPreferences1.getString("username", "");
//                        String msg = getString(R.string.msg_token_fmt, token);
//                        Log.d(TAG, msg);
//                        SIM.sendToken(token,username);
//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
//                    }
//                });

        if(sharedPreferences1.contains("username")){
            String username = sharedPreferences1.getString("username", "");
            String tokenNow = sharedPreferences1.getString("token", "");
            Intent intent = new Intent(getApplicationContext(),UsersActivity.class);
            intent.putExtra("Token",tokenNow);
            intent.putExtra("Username",username);
            SIM.connect(username);
//            SIM.logIn(username);
            startActivity(intent);
        } else {
            if(LocalDB.userDB != null){
                new Thread(() ->{
                    LocalDB.userDB.clearAllTables();
                }).start();
            }
            Intent intent = new Intent(getApplicationContext(), CreateAccountActivity.class);
            startActivity(intent);
        }
        finish();

//        usernameEditText = findViewById(R.id.usernameEditText);
//        passwordEditText = findViewById(R.id.passwordEditText);
//        passwordValidationEditText = findViewById(R.id.passwordValidationEditText);
//        displayNameEditText = findViewById(R.id.displayNameEditText);
//        profilePictureImageView = findViewById(R.id.profilePictureImageView);
//        errors = findViewById(R.id.errorMessage);
//        Button profilePictureButton = findViewById(R.id.profilePictureButton);
//        Button signUpButton = findViewById(R.id.signUpButton);
//        Button alreadyRegistered = findViewById(R.id.alreadySignedIn);
//
////        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
////                (ActivityResultCallback<Uri>) result -> {
////                    if (result != null) {
////                        selectedImageUri = result;
////                        try {
////                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
////                            profilePictureImageView.setVisibility(View.VISIBLE);
////                            profilePictureImageView.setImageBitmap(bitmap);
////                        } catch (IOException e) {
////                            Log.e("MainActivity", "Error loading image: " + e.getMessage());
////                        }
////                    }
////                });
//
//
//
//        profilePictureButton.setOnClickListener(v -> {
//            selectProfilePicture();
//        });
//
//        signUpButton.setOnClickListener(v -> signUp());
//
//        alreadyRegistered.setOnClickListener(v -> {
//            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//            startActivity(intent);
//        });


    }
//    private String bitmapToBase64(Bitmap bitmap) {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap,100,100,false);
//        bitmap1.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
//        byte[] imageBytes = byteArrayOutputStream.toByteArray();
//        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
//    }


//    private void selectProfilePicture() {
//        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("image/*");
//        imagePickerLauncher.launch(new String[]{"image/*"});
//    }


    //    private void signUp(){
//        String username = usernameEditText.getText().toString().trim();
//        String password = passwordEditText.getText().toString().trim();
//        String passwordValidation = passwordValidationEditText.getText().toString().trim();
//        String displayName = displayNameEditText.getText().toString().trim();
//
//        // Perform validation checks
//        if (TextUtils.isEmpty(username)) {
//            usernameEditText.setError("Please enter a username");
//            return;
//        }
//
//        if (TextUtils.isEmpty(password)) {
//            passwordEditText.setError("Please enter a password");
//            return;
//        }
//
//        if (!password.equals(passwordValidation)) {
//            passwordValidationEditText.setError("Password does not match");
//            return;
//        }
//
//        if (TextUtils.isEmpty(displayName)) {
//            displayNameEditText.setError("Please enter a display name");
//            return;
//        }
//        if (displayName.length() > 10) {
//            displayNameEditText.setError("Display name must be up to 10 characters");
//            return;
//        }
//        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseURL)
//                .addConverterFactory(GsonConverterFactory.create()).build();
//        WebServiceAPI webServiceAPI = retrofit.create(WebServiceAPI.class);
//        String pfp=defaultPfp;
//        if(selectedImageUri!=null){
//            pfp=bitmapToBase64(bitmap);
//        }
//        UserCreatePost userCreatePost = new UserCreatePost(username,password,displayName,pfp);
//        Call<Void> call = webServiceAPI.createUser(userCreatePost);
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if(response.isSuccessful()){
//                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
//                    startActivity(intent);
//                } else {
//                    errors.setText(R.string.username_already_taken_or_error);
//                    errors.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                errors.setText(R.string.error_with_request_to_server);
//                errors.setVisibility(View.VISIBLE);
//
//            }
//        });
//
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (requestCode == PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                selectProfilePicture();
//            } else {
//                Log.d("MainActivity", "Permission denied");
//            }
//        }
//    }
    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
