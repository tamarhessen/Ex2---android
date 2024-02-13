package com.example.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments);

        // Initialize RecyclerView and Adapter
        recyclerView = findViewById(R.id.commentsRecyclerView);
        adapter = new CommentsAdapter();

        // Set layout manager to RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set adapter to RecyclerView
        recyclerView.setAdapter(adapter);

        // Initialize comments list
        comments = new ArrayList<>();

        // Populate comments list with data
        populateComments();

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
                    commentEditText.setText(""); // Clear the EditText
                }
            }
        });


    }

    // Method to populate comments list with data
    private void populateComments() {
        // Add comments to the adapter
        adapter.setComments(comments);
    }
}
