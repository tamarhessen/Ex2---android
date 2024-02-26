package com.example.login;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.login.adapters.PostsListAdapter;
import com.example.login.entities.Post;
import com.example.login.network.RetrofitClient;
import com.example.login.network.PostService;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        postService = RetrofitClient.getClient().create(PostService.class);

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

        // Fetch posts from server
        fetchPosts();

        // Set click listeners
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FeedActivity.this, MenuActivity.class));
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
}
