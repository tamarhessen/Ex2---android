package com.example.login.facebookdesign;

import static com.example.login.facebookdesign.MainActivity.defaultPfp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.example.login.API.WebServiceAPI;
import com.example.login.viewModels.PostsViewModel;
import com.example.login.viewModels.UsersViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyProfileActivity extends AppCompatActivity {

    private ImageView profilePictureImageView;
    private TextView displayNameTextView;
    private UsersViewModel usersViewModel;
    private String username;
    private String token;
    private PostsViewModel postsViewModel;
    private String displayName;
    private RecyclerView postsRecyclerView;
    private TextView numOfFriendsTextView;
    private RecyclerView friendsRecyclerView;
    private PostAdapter postAdapter;
    private FriendAdapter friendAdapter;
    private Button editProfile;
    private  List<String> friends;
    private List<String> pendingRequests;
    private ImageButton exitButton;
    private Button deleteUser;
    private Button friendRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile_page);
        // Initialize views
        profilePictureImageView = findViewById(R.id.profile_picture);
        displayNameTextView = findViewById(R.id.user_name);
        editProfile = findViewById(R.id.btn_edit_profile);
        exitButton = findViewById(R.id.btn_exit);
        postsRecyclerView = findViewById(R.id.recycler_posts);
        friendsRecyclerView=findViewById(R.id.recycler_friends);
        deleteUser = findViewById(R.id.btn_delete_user);
        friendRequests = findViewById(R.id.btn_more);
        numOfFriendsTextView = findViewById(R.id.num_of_friends);
        // Initialize UsersViewModel
        usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);

        fetchUserData();


        usersViewModel.getFriends().observe(this, new Observer<Pair<List<String>, List<String>>>() {
            @Override
            public void onChanged(Pair<List<String>, List<String>> friendLists) {
                // Update UI with the list of friends
                friends = friendLists.first;
                pendingRequests = friendLists.second;
                friendAdapter.setFriends(friends,token,username);

                updateNumberOfFriends(friends.size());
            }
        });
        Intent activityIntent = getIntent();
        if (activityIntent != null) {
            token = activityIntent.getStringExtra("Token");
            username = activityIntent.getStringExtra("Username");
            // Initialize ViewModel with token
            postsViewModel = new ViewModelProvider(this).get(PostsViewModel.class);
            postsViewModel.setUsername(username);
            postsViewModel.setToken(token);
        }
        else {
            // Handle case where intent is null or token is not provided
            Toast.makeText(this, "Failed to get token", Toast.LENGTH_SHORT).show();
        }
        setUpPostsRecyclerView();
        setUpFriendsRecyclerView();
        // Observe changes in posts data
        postsViewModel.getPosts().observe(this, posts -> {
            if (posts != null && !posts.isEmpty()) {
                postAdapter.setPosts(posts);
            }
        });
        // Set click listener for edit button
        editProfile.setOnClickListener(v -> {
            // Open edit profile dialog fragment
            openEditProfileDialog();
        });
        deleteUser.setOnClickListener(v -> {
            // Open edit profile dialog fragment
            deleteUser();
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Navigate back to the previous activity
            }
        });
        friendRequests.setOnClickListener(v -> {
            // Create a new instance of the dialog fragment
            PendingRequestsDialogFragment dialogFragment = new PendingRequestsDialogFragment(pendingRequests,token,username);
            dialogFragment.show(getSupportFragmentManager(), "PendingRequestsDialogFragment");

        });



        // Retrieve the profile picture byte array from Intent extras
        byte[] profilePictureByteArray = getIntent().getByteArrayExtra("ProfilePicture");
        if (profilePictureByteArray != null) {
            // Convert the byte array to Bitmap and set it to the profile picture ImageView
            Bitmap bitmap = BitmapFactory.decodeByteArray(profilePictureByteArray, 0, profilePictureByteArray.length);
            profilePictureImageView.setImageBitmap(bitmap);
        }
    }

    private void setUpFriendsRecyclerView() {
        // Inside onCreate method or any appropriate method

// Initialize RecyclerView for friends
        friendsRecyclerView = findViewById(R.id.recycler_friends);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        friendsRecyclerView.setLayoutManager(layoutManager);

// Create and set adapter for friends RecyclerView
        friendAdapter = new FriendAdapter(this); // Pass your list of friend details here
        friendsRecyclerView.setAdapter(friendAdapter);

    }

    private void fetchAndDisplayPosts(String currentDisplayName) {
        // Observe changes in posts data
        postsViewModel.getPostsforUserName().observe(this, posts -> {
            if (posts != null && !posts.isEmpty()) {
                // Update RecyclerView adapter with fetched posts
                postAdapter.setPosts(filterPostsByDisplayName(posts,currentDisplayName));
            } else {
                Toast.makeText(MyProfileActivity .this, "No posts found", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private List<Post> filterPostsByDisplayName(List<Post> posts, String currentDisplayName) {
        List<Post> filteredPosts = new ArrayList<>();
        for (Post post : posts) {
            if (post.getCreator().equals(currentDisplayName)) {
                filteredPosts.add(post);
            }
        }
        return filteredPosts;
    }
    private void deleteUser() {
        // Call the deleteUser method in the ViewModel
        usersViewModel.deleteuser();

        // Navigate back to the login screen
        Intent intent = new Intent(MyProfileActivity.this, LogInActivity.class);
        startActivity(intent);
        finish(); // Optional: Finish the current activity to prevent going back to it when pressing the back button
    }



    private void setUpPostsRecyclerView() {
        // Initialize and set layout manager for posts RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        postsRecyclerView.setLayoutManager(layoutManager);

        // Create and set adapter for posts RecyclerView
        postAdapter = new PostAdapter(this, username, postsViewModel, displayName);
        postsRecyclerView.setAdapter(postAdapter);
    }
    private void openEditProfileDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        // Retrieve user data
        String username = getIntent().getStringExtra("Username");
        String token = getIntent().getStringExtra("Token");
        String displayName = getIntent().getStringExtra("DisplayName");
        String profilePicBase64 = getIntent().getStringExtra("ProfilePicBase64"); // Assuming you have a key for profile picture
        Bitmap profilePicBitmap = null;
        if (profilePicBase64 != null) {
            byte[] decodedString = Base64.decode(profilePicBase64, Base64.DEFAULT);
            profilePicBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        }
        // Create an instance of EditProfileDialogFragment with arguments
        EditProfileDialogFragment editProfileDialogFragment = EditProfileDialogFragment.newInstance(username, token, displayName, BitmapConverter.bitmapToString(profilePicBitmap));
        // Show the dialog
        editProfileDialogFragment.show(fragmentManager, "EditProfileDialogFragment");

    }
    private void updateNumberOfFriends(int numberOfFriends) {
        numOfFriendsTextView.setText(String.valueOf(numberOfFriends));
    }
    private void fetchUserData() {
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
                    // Display user data or handle the single userCreatePost object as needed
                    // Example: Set the profile picture
                    setProfilePicture(userCreatePost.getProfilePic());
                    setDisplayName(userCreatePost.getDisplayName());

                    // Fetch and display posts after setting display name
                    fetchAndDisplayPosts(userCreatePost.getDisplayName());

                }
            });

        }
    }

    private void setProfilePicture(String profilePicBase64) {
        if (profilePicBase64 != null && !profilePicBase64.equals(defaultPfp)) {
            byte[] decodedString = Base64.decode(profilePicBase64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            profilePictureImageView.setImageBitmap(decodedByte);
        } else {
            // Set a default profile picture if profilePicBase64 is null or defaultPfp
            profilePictureImageView.setImageResource(R.drawable.facebook_logo);
        }
    }

    private void setDisplayName(String displayName) {
        displayNameTextView.setText(displayName);
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

    private void updateViews(UserCreatePost user) {
        // Set display name
        displayNameTextView.setText(user.getDisplayName());

        // Set profile picture
        String profilePic = user.getProfilePic();
        if (profilePic != null && !profilePic.equals(MainActivity.defaultPfp)) {
            byte[] decodedString = Base64.decode(profilePic, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            profilePictureImageView.setImageBitmap(decodedByte);
        }
    }
}
