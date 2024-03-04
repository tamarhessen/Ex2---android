package com.example.login.facebookdesign;

import static com.example.login.facebookdesign.MainActivity.baseURL;
import static com.example.login.facebookdesign.MainActivity.defaultPfp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.login.JsonParser;
import com.example.login.R;
import com.example.login.facebookdesign.CreateAccountActivity;
import com.example.login.facebookdesign.PostAdapter;
import com.example.login.facebookdesign.Post;
import com.example.login.network.WebServiceAPI;

import java.io.ByteArrayOutputStream;
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

    private PostAdapter adapter;
    private String username;
    private Button profilePictureButton;
    private WebServiceAPI webServiceAPI;
    private byte[] profilePictureByteArray;
    private PostsViewModel postsViewModel;
    private String token;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        initViews();
        // Set up RecyclerView and adapter

        initWebServiceAPI();
        // Fetch username and profile picture
        fetchUserData();


        // Set click listeners
        setClickListeners();

        // Load sample posts
   //     loadSamplePosts();
        Intent activityIntent = getIntent();
        if (activityIntent != null) {
            token = activityIntent.getStringExtra("Token");
            username = activityIntent.getStringExtra("Username");

            // Initialize ViewModel with token
            postsViewModel = new ViewModelProvider(this).get(PostsViewModel.class);
            postsViewModel.setUsername(username);
            postsViewModel.setToken(token);

            if (profilePictureByteArray != null) {
                Bitmap profilePictureBitmap = BitmapFactory.decodeByteArray(profilePictureByteArray, 0, profilePictureByteArray.length);
                postsViewModel.init(username, token, profilePictureBitmap);
            } else {
                postsViewModel.init(username, token, null); // Pass null if profile picture is not available
            }
        } else {
            // Handle case where intent is null or token is not provided
            Toast.makeText(this, "Failed to get token", Toast.LENGTH_SHORT).show();
        }

        // Observe changes in posts data
        postsViewModel.getPosts().observe(this, posts -> {
            if (posts != null && !posts.isEmpty()) {
                adapter.setPosts(posts);
            }
        });
        setUpRecyclerView();
        fetchAndDisplayPosts();
    }


    private void initViews() {
        profilePictureButton = findViewById(R.id.btn_profile_picture);
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
            String token = activityIntent.getStringExtra("Token");
            username = activityIntent.getStringExtra("Username");

            // Initialize the webServiceAPI object
            initWebServiceAPI();

            Call<UserCreatePost> call = webServiceAPI.getUser(username, "Bearer " + token);
            call.enqueue(new Callback<UserCreatePost>() {
                @Override
                public void onResponse(Call<UserCreatePost> call, Response<UserCreatePost> response) {
                    if (response.isSuccessful()) {
                        UserCreatePost user = response.body();
                        ImageView profilePictureImageView = findViewById(R.id.image_profile_picture);

                        if (user != null) {
                            setAsImage(user.getProfilePic(), profilePictureImageView);
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserCreatePost> call, Throwable t) {
                    // Handle failure
                    Toast.makeText(FeedActivity.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void fetchAndDisplayPosts() {
        // Observe changes in posts data
        postsViewModel.getPosts().observe(this, posts -> {
            if (posts != null && !posts.isEmpty()) {
                // Update RecyclerView adapter with fetched posts
                adapter.setPosts(posts);
            } else {
                Toast.makeText(FeedActivity.this, "No posts found", Toast.LENGTH_SHORT).show();
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
    private void initWebServiceAPI() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }
    private void setClickListeners() {

        menuButton.setOnClickListener(v -> {
            Intent intent = new Intent(FeedActivity.this, MenuActivity.class);
            intent.putExtra("Username", username);
            intent.putExtra("ProfilePicture", profilePictureByteArray); // Pass the profile picture byte array
            startActivity(intent);


        });

        newPostButton.setOnClickListener(v -> startActivityForResult(new Intent(FeedActivity.this, PostActivity.class), REQUEST_NEW_POST));

        whatsNewButton.setOnClickListener(v -> startActivityForResult(new Intent(FeedActivity.this, PostActivity.class), REQUEST_NEW_POST));
        profilePictureButton.setOnClickListener(v -> {
            Intent intent = new Intent(FeedActivity.this,MyProfileActivity.class);
            intent.putExtra("Username", username);
            intent.putExtra("ProfilePicture", profilePictureByteArray); // Pass the profile picture byte array
            startActivity(intent);


        });
    }

//    private void loadSamplePosts() {
//        List<Post> posts = JsonParser.parseJson(this);
//        adapter.setPosts(posts);
//    }


    // This method will be called when returning from NewPostActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap profileImageBitmap = null;
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_NEW_POST && resultCode == RESULT_OK && data != null) {
            String postText = data.getStringExtra("postText");
            String postImagePath = data.getStringExtra("postImagePath");
            Intent activityIntent = getIntent();

            username = activityIntent.getStringExtra("Username");
            Retrofit retrofit = new Retrofit.Builder().baseUrl(MainActivity.baseURL)
                    .addConverterFactory(GsonConverterFactory.create()).build();
            WebServiceAPI webServiceAPI = retrofit.create(WebServiceAPI.class);
            Call<UserCreatePost> call = webServiceAPI.getUser(username,
                    "Bearer " + activityIntent.getStringExtra("Token"));
            call.enqueue(new Callback<UserCreatePost>() {
                @Override
                public void onResponse(Call<UserCreatePost> call, Response<UserCreatePost> response) {
                    if (response.isSuccessful()) {
                        UserCreatePost user = response.body();
                        // Update views with fetched data
                        if (user != null) {
                            Log.d("onResponse", "Successfully fetched user data. Display name: " + username);
                            String displayMane= user.getDisplayName();

                            // Set profile picture
                            String profilePic = user.getProfilePic();
                            if (profilePic != null && !profilePic.equals(MainActivity.defaultPfp)) {
                                byte[] decodedString = Base64.decode(profilePic, Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                Bitmap profileImageBitmap = decodedByte;
                                Bitmap postImageBitmap = BitmapFactory.decodeFile(postImagePath);
                                long currentTimeMillis = System.currentTimeMillis();
                                String postImage = BitmapConverter.bitmapToString(postImageBitmap);
                                String profileImage = BitmapConverter.bitmapToString(profileImageBitmap);
                                Post newPost = new Post(displayMane, postText, postImage, 0, profileImage,currentTimeMillis);
                                postsViewModel.addPost(newPost);
                                // Add the new post to the adapter
                                adapter.addPost(newPost);
                            }
                        }
                    } else {
                        Log.e("onResponse", "Failed to fetch user data. Error code: " + response.code());
                    }
                }

                @Override

                public void onFailure(Call<UserCreatePost> call, Throwable t) {
                    Log.e("onFailure", "Failed to fetch user data. Error message: " + t.getMessage());

                }
            });

        }
    }
    private Bitmap decodeProfilePicture(String profilePic) {
        if (profilePic == null || profilePic.equals(MainActivity.defaultPfp)) {
            // Return default profile picture or handle accordingly
            return null;
        } else {
            byte[] decodedString = Base64.decode(profilePic, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        }
    }



    private void updateViews(UserCreatePost user) {
        if (user != null) {
            String displayMane= user.getDisplayName();

            // Set profile picture
            String profilePic = user.getProfilePic();
            if (profilePic != null && !profilePic.equals(MainActivity.defaultPfp)) {
                byte[] decodedString = Base64.decode(profilePic, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                Bitmap postImageBitmap = decodedByte;
            }
        }
    }

}