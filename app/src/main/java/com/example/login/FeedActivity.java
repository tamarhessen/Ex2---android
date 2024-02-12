package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.adapters.PostsListAdapter;
import com.example.login.entities.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {
    private Button menuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        // Find the menu button and set click listener
        menuButton = findViewById(R.id.menubtn);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the MenuActivity when the button is clicked
                Intent intent = new Intent(FeedActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });

        // Set up RecyclerView and adapter
        RecyclerView lstPosts = findViewById(R.id.lstPosts);
        final PostsListAdapter adapter = new PostsListAdapter(this);
        lstPosts.setAdapter(adapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));

        // Create sample posts
        List<Post> posts = new ArrayList<>();
        posts.add(new Post("Hila123", "Look at the beautiful sea", R.drawable.pic1,200,R.drawable.img_1));
        posts.add(new Post("Hila123", "Look at the beautiful sea", R.drawable.pic1,300,R.drawable.img_1));
        posts.add(new Post("Hila123", "Look at the beautiful sea", R.drawable.pic1,200,R.drawable.img_1));

        // Pass the list of posts to the adapter
        adapter.setPosts(posts);

    }

//    private List<Post> loadPostsFromJson() {
//        List<Post> posts = new ArrayList<>();
//        try {
//            // Load JSON data from file
//            InputStream inputStream = getAssets().open("posts.json");
//            int size = inputStream.available();
//            byte[] buffer = new byte[size];
//            inputStream.read(buffer);
//            inputStream.close();
//            String json = new String(buffer, StandardCharsets.UTF_8);
//
//            // Parse JSON array
//            // Parse JSON array
//            JSONArray jsonArray = new JSONArray(json);
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                String author = jsonObject.getString("author");
//                String content = jsonObject.getString("content");
//                int pic = jsonObject.getInt("pic");
//                int likes = jsonObject.getInt("likes");
//                int profilePic = jsonObject.getInt("profilepic");
//                Post post = new Post(author, content, pic, likes, profilePic);
//                posts.add(post);
//            }
//
//        } catch (IOException | JSONException e) {
//            e.printStackTrace();
//        }
//        return posts;
//    }
}
