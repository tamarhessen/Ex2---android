package com.example.login.facebookdesign;

import static com.example.login.facebookdesign.MainActivity.defaultPfp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.example.login.viewModels.PostsViewModel;
import com.example.login.viewModels.UsersViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private ImageView coverPhotoImageView;
    private ImageView profilePictureImageView;
    private TextView userNameTextView;
    private TextView numOfFriendsTextView;
    private PostsViewModel postsViewModel;
    private Button addFriendButton;
    private Button sentFriendRequest;
    private Button acceptFriend;
    private PostAdapter adapter;
    private Button deleteFriend;
    private Button sendMessage;
    private ImageButton closeButton;
    private RecyclerView friendsRecyclerView;
    private RecyclerView postsRecyclerView;
    private FriendAdapter friendAdapter;
    private String token;
    private String username;
    private String myusername;
    private  List<String> friends;
    private  List<String> pendingList;
    private String currentUsername;
    private UsersViewModel myUserViewModel;
    private static String displayName;
    private List<String> friendList;
    private UsersViewModel usersViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);
        // Initialize views
        coverPhotoImageView = findViewById(R.id.cover_photo);
        profilePictureImageView = findViewById(R.id.profile_picture);
        userNameTextView = findViewById(R.id.user_name);
        numOfFriendsTextView = findViewById(R.id.num_of_friends);
        addFriendButton = findViewById(R.id.btn_add_friend);
        sentFriendRequest = findViewById(R.id.btn_request_sent);
        deleteFriend = findViewById(R.id.btn_delete_friend);
        sendMessage = findViewById(R.id.btn_send_message);
        friendsRecyclerView = findViewById(R.id.recycler_friends);
        postsRecyclerView = findViewById(R.id.recycler_posts);
        closeButton = findViewById(R.id.btn_exit);
        acceptFriend=findViewById(R.id.btn_more);
        usersViewModel = new UsersViewModel();
        myUserViewModel = new UsersViewModel();

        // Set up RecyclerViews

        fetchUserData();
        Intent activityIntent = getIntent();
        if (activityIntent != null) {
            token = activityIntent.getStringExtra("Token");
            username = activityIntent.getStringExtra("Username");
            myusername=activityIntent.getStringExtra("myUsername");
            // Initialize ViewModel with token
            postsViewModel = new ViewModelProvider(this).get(PostsViewModel.class);
            postsViewModel.setUsername(username);
            postsViewModel.setToken(token);



            // Initialize UsersViewModel

        } else {
            // Handle case where intent is null or token is not provided
            Toast.makeText(this, "Failed to get token", Toast.LENGTH_SHORT).show();
        }

        usersViewModel.getFriends().observe(this, new Observer<Pair<List<String>, List<String>>>() {
            @Override
            public void onChanged(Pair<List<String>, List<String>> friendLists) {
                // Update UI with the list of friends
                friends = friendLists.first;
                pendingList = friendLists.second;
                if(friends.contains(myusername)) {
                    // Hide the "Add Friend" button if already friends
                    addFriendButton.setVisibility(View.INVISIBLE);
                    sentFriendRequest.setVisibility(View.INVISIBLE);
                    sendMessage.setVisibility(View.VISIBLE);
                    friendAdapter.setFriends(friends,token,myusername);
                    deleteFriend.setVisibility(View.VISIBLE);
                } else if (pendingList.contains(username)) {
                    // Hide the "Add Friend" button if there is a pending friend request
                    addFriendButton.setVisibility(View.INVISIBLE);
                    sentFriendRequest.setVisibility(View.INVISIBLE);
                    sendMessage.setVisibility(View.INVISIBLE);
                    friendsRecyclerView.setVisibility(View.GONE);
                    deleteFriend.setVisibility(View.INVISIBLE);
                } else {
                    // Show the "Add Friend" button if none of the above conditions are met
                    addFriendButton.setVisibility(View.VISIBLE);
                    sentFriendRequest.setVisibility(View.INVISIBLE);
                    sendMessage.setVisibility(View.INVISIBLE);
                    friendsRecyclerView.setVisibility(View.GONE);
                    deleteFriend.setVisibility(View.INVISIBLE);
                }
                updateNumberOfFriends(friends.size());
            }
        });


        myUserViewModel = new ViewModelProvider(this).get(UsersViewModel.class);
        myUserViewModel.setUserid(myusername);
        myUserViewModel.setToken(token);
        setUpFriendsRecyclerView();
        setUpPostsRecyclerView();
        // Observe changes in pending friend requests for my user
        myUserViewModel.getFriends().observe(this, new Observer<Pair<List<String>, List<String>>>() {
            @Override
            public void onChanged(Pair<List<String>, List<String>> friendLists) {
                // Update UI with the list of friends and pending requests for my user
                friends = friendLists.first;
                pendingList = friendLists.second;
                if (pendingList.contains(username)) {
                    acceptFriend.setVisibility(View.VISIBLE);
                    addFriendButton.setVisibility(View.INVISIBLE);
                } else {
                    acceptFriend.setVisibility(View.INVISIBLE);
                    addFriendButton.setVisibility(View.VISIBLE);
                }
                // Update your UI components with the friends list and pending requests as needed
            }
        });

        // Observe changes in posts data
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Navigate back to the previous activity
            }
        });

        addFriendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                usersViewModel.askFriend();
                addFriendButton.setVisibility(View.INVISIBLE);
                sentFriendRequest.setVisibility(View.VISIBLE);

            }

        });
        acceptFriend.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.d("AcceptFriend", "Username: " + username);
                Log.d("AcceptFriend", "Username: " + myusername);
                usersViewModel.acceptFriend(myusername,username);
               acceptFriend.setVisibility(View.INVISIBLE);
                // Navigate back to the feed activity
                Intent intent = new Intent(ProfileActivity.this, FeedActivity.class);
                intent.putExtra("Token", token);
                intent.putExtra("Username", myusername);

                startActivity(intent);

            }

        });
        deleteFriend.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.d("AcceptFriend", "Username: " + username);
                Log.d("AcceptFriend", "Username: " + myusername);
                usersViewModel.deleteFriend(myusername,username);

                finish();
            }

        });

    }
    private void updateNumberOfFriends(int numberOfFriends) {
        numOfFriendsTextView.setText(String.valueOf(numberOfFriends));
    }
    private void fetchAndDisplayPosts(String currentDisplayName, List<String> friendList, String curretUsername){
        // Observe changes in posts data
        postsViewModel.getPostsforUserName().observe(this, posts -> {
            if (posts != null && !posts.isEmpty() ) {
                // Update RecyclerView adapter with fetched posts
                adapter.setPosts(filterPostsByDisplayName(posts, currentDisplayName, friendList, curretUsername));
            } else {
                Toast.makeText(ProfileActivity.this, "This user is private", Toast.LENGTH_SHORT).show();
                adapter.setPosts(new ArrayList<>());
                postsRecyclerView.setVisibility(View.GONE);
            }
        });
    }
    private List<Post> filterPostsByDisplayName(List<Post> posts, String currentDisplayName, List<String> friendList,String currentUsername) {
        List<Post> filteredPosts = new ArrayList<>();
        for (Post post : posts) {
            if (post.getCreator().equals(currentDisplayName)) {
                filteredPosts.add(post);
            }
        }
        return filteredPosts;
    }
    private void fetchUserData() {
        Intent activityIntent = getIntent();
        if (activityIntent != null) {
            token = activityIntent.getStringExtra("Token");
            username = activityIntent.getStringExtra("Username");
            usersViewModel.setToken(token);
            usersViewModel.setUserid(username);
            myUserViewModel.setUserid(myusername);
            myUserViewModel.setToken(token);

            // Observe user data
            usersViewModel.getCurrentUser(username, token).observe(this, new Observer<UserCreatePost>() {
                @Override
                public void onChanged(UserCreatePost userCreatePost) {
                   friendList = userCreatePost.FriendList;
                   currentUsername = userCreatePost.getUsername();
                    // Display user data or handle the single userCreatePost object as needed
                    // Example: Set the profile picture
                    setProfilePicture(userCreatePost.getProfilePic());
                    setDisplayName(userCreatePost.getDisplayName());
                    displayName=userCreatePost.getDisplayName();

                    fetchAndDisplayPosts(displayName,friendList,currentUsername);
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
        userNameTextView.setText(displayName);
    }
    private void setUpFriendsRecyclerView() {
        // Initialize and set layout manager for friends RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        friendsRecyclerView.setLayoutManager(layoutManager);

        // Create and set adapter for friends RecyclerView
        friendAdapter = new FriendAdapter(this); // <-- Assign it to the field
        friendsRecyclerView.setAdapter(friendAdapter); // <-- Set the adapter

    }
    private void dismissDialogFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag("PendingRequestsDialogFragment");
        if (fragment != null) {
            fragmentManager.beginTransaction().remove(fragment).commit();
        }
    }

    private void setUpPostsRecyclerView() {
        // Initialize and set layout manager for posts RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        postsRecyclerView.setLayoutManager(layoutManager);

        // Create and set adapter for posts RecyclerView
        adapter = new PostAdapter(this, username, postsViewModel, displayName);
        postsRecyclerView.setAdapter(adapter);
    }


}