package com.example.login.facebookdesign;

import static com.example.login.facebookdesign.MainActivity.baseURL;
import static com.example.login.facebookdesign.MainActivity.defaultPfp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.login.JsonParser;
import com.example.login.R;
import com.example.login.facebookdesign.CreateAccountActivity;
import com.example.login.facebookdesign.PostAdapter;
import com.example.login.facebookdesign.Post;
import com.example.login.network.WebServiceAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FeedActivity extends AppCompatActivity {
    private static final int REQUEST_NEW_POST = 1;
    private Button menuButton;
    private Button newPostButton;
    private Button whatsNewButton;
    private RecyclerView lstPosts;
    private UserDB userDB;
    private PostAdapter adapter;
    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        // Initialize views
        initViews();

        // Set up RecyclerView and adapter
        setUpRecyclerView();

        // Fetch username and profile picture
        fetchUserData();

        // Set click listeners
        setClickListeners();

        // Load sample posts
        loadSamplePosts();
    }

    private void initViews() {
        menuButton = findViewById(R.id.menubtn);
        newPostButton = findViewById(R.id.addbtn);
        whatsNewButton = findViewById(R.id.whatsNewButton);
        lstPosts = findViewById(R.id.lstPosts);
    }

    private void setUpRecyclerView() {
        adapter = new PostAdapter(this, username);
        lstPosts.setAdapter(adapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));
    }

    private void fetchUserData() {
        Intent activityIntent = getIntent();
        if (activityIntent != null) {
            username = activityIntent.getStringExtra("Username");
            byte[] profilePictureByteArray = activityIntent.getByteArrayExtra("ProfilePicture");
            if (profilePictureByteArray != null) {
                Bitmap profilePictureBitmap = BitmapFactory.decodeByteArray(profilePictureByteArray, 0, profilePictureByteArray.length);
                ImageView profilePictureImageView = findViewById(R.id.image_profile_picture);
                profilePictureImageView.setImageBitmap(profilePictureBitmap);
            }
        }
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        WebServiceAPI webServiceAPI = retrofit.create(WebServiceAPI.class);
        Call<UserCreatePost> call = webServiceAPI.getUser(username,
                "Bearer "+activityIntent.getStringExtra("Token"));
        call.enqueue(new Callback<UserCreatePost>() {
            @Override
            public void onResponse(Call<UserCreatePost> call, Response<UserCreatePost> response) {
                if(response.isSuccessful()) {
                    UserCreatePost user = response.body();
                    ImageView profilePictureImageView = findViewById(R.id.image_profile_picture);

setAsImage(user.getProfilePic(), profilePictureImageView);
                }
            }

            @Override
            public void onFailure(Call<UserCreatePost> call, Throwable t) {

            }
        });
    }

    public static void setAsImage(String strBase64, ImageView imageView) {
        if(strBase64.equals(defaultPfp)){
//            imageView.setImageResource(R.drawable.defaultprofilepic);
        } else {
            byte[] decodedString = Base64.decode(strBase64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageView.setImageBitmap(decodedByte);
        }

    }

    private void setClickListeners() {
        menuButton.setOnClickListener(v -> startActivity(new Intent(FeedActivity.this, MenuActivity.class)));

        newPostButton.setOnClickListener(v -> startActivityForResult(new Intent(FeedActivity.this, PostActivity.class), REQUEST_NEW_POST));

        whatsNewButton.setOnClickListener(v -> startActivityForResult(new Intent(FeedActivity.this, PostActivity.class), REQUEST_NEW_POST));
    }

    private void loadSamplePosts() {
        List<Post> posts = JsonParser.parseJson(this);
        adapter.setPosts(posts);
    }

    // This method will be called when returning from NewPostActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_NEW_POST && resultCode == RESULT_OK && data != null) {
            String postText = data.getStringExtra("postText");
            String postImagePath = data.getStringExtra("postImagePath");
            Bitmap profileImageBitmap = CreateAccountActivity.profilePictureBitmap;
            Bitmap postImageBitmap = BitmapFactory.decodeFile(postImagePath);
            long currentTimeMillis = System.currentTimeMillis();
            Post newPost = new Post(username, postText, postImageBitmap, 0, profileImageBitmap, currentTimeMillis);
            adapter.addPost(newPost);
        }
    }
}
