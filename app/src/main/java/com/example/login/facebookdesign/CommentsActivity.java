package com.example.login.facebookdesign;

import static com.example.login.facebookdesign.MainActivity.defaultPfp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.facebookdesign.CommentAdapter;
import com.example.login.R;
import com.example.login.viewModels.UsersViewModel;

import java.util.ArrayList;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {

    private List<String> comments;
    private RecyclerView recyclerView;
    private CommentAdapter adapter;
private String token;
    private EditText commentEditText;
    private Button addCommentButton;
    private UsersViewModel usersViewModel;
    private Button deleteButton;
    private String username;
    private String displayName;
    private Bitmap profilePicBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments);

        usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);
        Intent activityIntent = getIntent();
        if (activityIntent != null) {
            token = activityIntent.getStringExtra("Token");
            username = activityIntent.getStringExtra("Username");
            usersViewModel.setToken(token);
            usersViewModel.setUserid(username);

            // Observe user data
            usersViewModel.getCurrentUser(username, token).observe(this, new Observer<UserCreatePost>() {
                @Override
                public void onChanged(UserCreatePost userCreatePost) {

                    String profilePicBase64 = userCreatePost.getProfilePic();
                    profilePicBitmap = decodeBase64ToBitmap(profilePicBase64);
                    displayName = userCreatePost.getDisplayName();
                    Log.d("CommentsActivity", "DisplayName: " + displayName);
                    // Initialize RecyclerView and Adapter
                    recyclerView = findViewById(R.id.commentsRecyclerView);
                    adapter = new CommentAdapter(CommentsActivity.this, profilePicBitmap, displayName); // Provide context, profile picture, and username

                    // Set layout manager to RecyclerView
                    recyclerView.setLayoutManager(new LinearLayoutManager(CommentsActivity.this));

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
            });
        }


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

    private Bitmap decodeBase64ToBitmap(String base64String) {
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }


}