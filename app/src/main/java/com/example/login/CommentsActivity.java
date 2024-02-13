package com.example.login;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
//import com.example.login.adapters.CommentsAdapter;
import com.example.login.entities.Post;
import java.util.ArrayList;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {

    private List<Post> posts;
    private RecyclerView recyclerView;
//    private CommentsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments);
       EditText commentEditText = findViewById(R.id.commentEditText);

//        // Initialize RecyclerView and Adapter
//        recyclerView = findViewById(R.id.commentsRecyclerView);
//        adapter = new CommentsAdapter();
//
//        // Set layout manager to RecyclerView
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        // Set adapter to RecyclerView
//        recyclerView.setAdapter(adapter);
//
//        // Populate comments list with data
//        populateComments();
    }

    // Method to populate comments list with data
//    private void populateComments() {
//        // Dummy implementation for demonstration
//        List<String> dummyComments = new ArrayList<>();
//        dummyComments.add("This is the first comment.");
//        dummyComments.add("This is the second comment.");
//        dummyComments.add("This is the third comment.");

        // Add comments to the adapter
//        adapter.setComments(dummyComments);
//    }
}
