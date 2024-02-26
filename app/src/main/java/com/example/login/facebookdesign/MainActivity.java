package com.example.login.facebookdesign;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login.R;
import com.example.login.network.WebServiceAPI;
import com.example.login.network.RetrofitClient;

import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private WebServiceAPI webServiceAPI;
    public static SocketIOManager SIM = new SocketIOManager();

    //private static final int PERMISSION_REQUEST_CODE = 123;
//    private EditText usernameEditText, passwordEditText, passwordValidationEditText, displayNameEditText;
//    private TextView errors;
//    private ImageView profilePictureImageView;
//    private Uri selectedImageUri = null;

    public static String defaultPfp = "https://images-na.ssl-images-amazon.com/images/I/51e6kpkyuIL._AC_SX466_.jpg";
    //    private Bitmap bitmap;
    public static String baseURL = "http://10.0.2.2:5000/api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Retrofit
        Retrofit retrofit = RetrofitClient.getClient();

        // Create an instance of the API interface
        webServiceAPI = retrofit.create(WebServiceAPI.class);


        // Start the LoginActivity when the MainActivity is created
        Intent intent = new Intent(MainActivity.this, LogInActivity.class);
        startActivity(intent);
        // Finish the MainActivity to prevent it from appearing in the back stack
        finish();
    }
}