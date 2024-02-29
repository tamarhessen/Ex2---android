package com.example.login.facebookdesign;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.example.login.facebookdesign.CommentsAdapter;
import com.example.login.network.WebServiceAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommentsActivity extends AppCompatActivity {

    private List<String> comments;
    private RecyclerView recyclerView;
    private CommentsAdapter adapter;
    private EditText commentEditText;
    private Button addCommentButton;
    private Button deleteButton;
    private String username;
    private Bitmap profilePicBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments);
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
                        username = user.getDisplayName();

                        // Set profile picture
                        String profilePic = user.getProfilePic();
                        if (profilePic != null && !profilePic.equals(MainActivity.defaultPfp)) {
                            byte[] decodedString = Base64.decode(profilePic, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            profilePicBitmap = decodedByte;

                            // Initialize RecyclerView and Adapter with updated profile picture and username
                            recyclerView = findViewById(R.id.commentsRecyclerView);
                            adapter = new CommentsAdapter(CommentsActivity.this, profilePicBitmap, username);
                            recyclerView.setLayoutManager(new LinearLayoutManager(CommentsActivity.this));
                            recyclerView.setAdapter(adapter);

                            // Initialize comments list
                            comments = new ArrayList<>();

                            // Get references to EditText and Add button
                            commentEditText = findViewById(R.id.commentEditText);
                            addCommentButton = findViewById(R.id.addCommentButton);

                            // Set OnClickListener for Add button
                            addCommentButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Add comment to the list
                                    String newComment = commentEditText.getText().toString();
                                    if (!newComment.isEmpty()) {
                                        comments.add(newComment);
                                        adapter.setComments(comments); // Update the RecyclerView
                                        commentEditText.setText("");
                                    }
                                }
                            });
                        }
                    }
                } else {
                    // Handle unsuccessful response
                    // For example, you can display an error message or take appropriate action
                }
            }


            @Override
            public void onFailure(Call<UserCreatePost> call, Throwable t) {

            }
        });
    }

    // No need to populate comments here since it's done in the adapter
}
