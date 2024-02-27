package com.example.login;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.login.adapters.PostsListAdapter;
import com.example.login.entities.Post;
import com.example.login.network.RetrofitClient;
import com.example.login.network.PostService;
import com.example.login.network.WebServiceAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedActivity extends AppCompatActivity {

    private static final int REQUEST_NEW_POST = 1;
    private Button menuButton;
    private Button newPostButton;
    private RecyclerView lstPosts;
    private PostsListAdapter adapter;
    private String username;
    private PostService postService;
    private String currentUserUsername;
    private UserViewModel userViewModel;
    private WebServiceAPI webServiceAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        webServiceAPI = RetrofitClient.getClient().create(WebServiceAPI.class);

        postService = RetrofitClient.getClient().create(PostService.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        // Initialize views
        menuButton = findViewById(R.id.menubtn);
        newPostButton = findViewById(R.id.addbtn);
        lstPosts = findViewById(R.id.lstPosts);
        ImageView profilePictureImageView = findViewById(R.id.image_profile_picture);
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refreshLayout);
        currentUserUsername = username;
        // Initialize adapter
        adapter = new PostsListAdapter(this, username);
        lstPosts.setAdapter(adapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));
        String token = getIntent().getStringExtra("Token");
        // Fetch posts from server
        fetchPosts();
        fetchUserData(token);

        // Set click listeners
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FeedActivity.this, MenuActivity.class));
            }
        });
        userViewModel.getUserLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    // Update UI with user data
                    User.UserNoPassword userNoPassword = user.getUser();
                    if (userNoPassword != null) {
                        String profilePictureBase64 = userNoPassword.getProfilePic();
                        Bitmap profilePictureBitmap = decodeBase64ToBitmap(profilePictureBase64);
                        ImageView profilePictureImageView = findViewById(R.id.image_profile_picture);
                        profilePictureImageView.setImageBitmap(profilePictureBitmap);
                    }
                }
            }
        });


        newPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(FeedActivity.this, NewPostActivity.class), REQUEST_NEW_POST);
            }
        });

        // Handle swipe-to-refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchPosts();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    // Method to fetch posts from the server
    private void fetchPosts() {
        Call<List<Post>> call = postService.getPosts();
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()) {
                    List<Post> posts = response.body();
                    adapter.setPosts(posts);
                } else {
                    // Handle error
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                // Handle failure
            }
        });
    }

    // Method to handle the result from NewPostActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_NEW_POST && resultCode == RESULT_OK && data != null) {
            // Retrieve post data from NewPostActivity
            String postText = data.getStringExtra("postText");
            Bitmap postImageBitmap = (Bitmap) data.getParcelableExtra("postImageBitmap");

            // Create a new post on the server
            createPost(postText, postImageBitmap);
        }
    }

    // Method to create a new post on the server
    private void createPost(String postText, Bitmap postImageBitmap) {
        // Assuming you have the current user's username stored in a variable called username
        Post newPost = new Post(currentUserUsername, postText, postImageBitmap, 0, null, System.currentTimeMillis());
        Call<Void> call = postService.createPost(newPost);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Post created successfully
                    // Fetch posts again to update the list
                    fetchPosts();
                } else {
                    // Handle error
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
            }
        });
    }
    public Bitmap decodeBase64ToBitmap(String base64String) {
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
    private void fetchUserData(String token) {
        // Make a network request to fetch user data using the token
        // Assuming you have a method in your WebServiceAPI to fetch user data
        // Pass the token as a parameter
        webServiceAPI.getUserData(token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    // User data fetched successfully
                    User user = response.body();

                    // Update UI with user data
                    if (user != null) {
                        User.UserNoPassword userNoPassword = user.getUser();
                        if (userNoPassword != null) {
                            String profilePictureBase64 = userNoPassword.getProfilePic();
                            Bitmap profilePictureBitmap = decodeBase64ToBitmap(profilePictureBase64);
                            ImageView profilePictureImageView = findViewById(R.id.image_profile_picture);
                            profilePictureImageView.setImageBitmap(profilePictureBitmap);
                        }
                    }
                } else {
                    // Handle unsuccessful response
                    // Show an error message or retry logic
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Handle network failure
                // Show an error message or retry logic
            }
        });
    }


}
