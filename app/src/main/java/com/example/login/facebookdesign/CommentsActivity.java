package com.example.login.facebookdesign;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.example.login.adapters.CommentsAdapter;

import java.util.ArrayList;
import java.util.List;

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

        // Get profile picture bitmap from CreateAccountActivity
        profilePicBitmap = CreateAccountActivity.profilePictureBitmap;

        // Get username from UserCredentials
        List<UserCredentials.User> userList = UserCredentials.getUsers();
        if (!userList.isEmpty()) {
            username = userList.get(0).getUsername(); // Assuming there is only one user for simplicity
        } else {
            username = "Default User"; // Set a default username if no user is found
        }

        // Initialize RecyclerView and Adapter
        recyclerView = findViewById(R.id.commentsRecyclerView);
        adapter = new CommentsAdapter(this, profilePicBitmap, username); // Provide context, profile picture, and username

        // Set layout manager to RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set adapter to RecyclerView
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

    // No need to populate comments here since it's done in the adapter
}
