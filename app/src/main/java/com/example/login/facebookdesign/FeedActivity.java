package com.example.login.facebookdesign;

import static com.example.login.facebookdesign.MainActivity.defaultPfp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Query;
import androidx.room.Room;

import com.example.login.R;
import com.example.login.API.WebServiceAPI;
import com.example.login.dataBase.LocalDB;
import com.example.login.dataBase.PostDB;
import com.example.login.viewModels.PostsViewModel;
import com.example.login.viewModels.UsersViewModel;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FeedActivity extends AppCompatActivity {
    private static final int REQUEST_NEW_POST = 1;
    private static final int EDIT_POST = 2;

    private Button menuButton;
    private Button newPostButton;
    private Button whatsNewButton;
    private RecyclerView lstPosts;

    private PostAdapter adapter;
    private String username;
    private Button profilePictureButton;
    private WebServiceAPI webServiceAPI;
    private byte[] profilePictureByteArray;
    private PostsViewModel postsViewModel;
    private UsersViewModel usersViewModel;
    private String token;
    private String displayName;
    private boolean share=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        initViews();
        PostDB db = Room.databaseBuilder(getApplicationContext(),
                PostDB.class, "post").allowMainThreadQueries().build();
        PostDao postDao = db.postDao();
        // Set up RecyclerView and adapter

        initWebServiceAPI();
        usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);
        // Fetch username and profile picture
        fetchUserData();


        // Set click listeners
        setClickListeners();

        // Load sample posts
//     loadSamplePosts();
        Intent activityIntent = getIntent();
        if (activityIntent != null) {
            token = activityIntent.getStringExtra("Token");
            username = activityIntent.getStringExtra("Username");
            displayName = activityIntent.getStringExtra("Display name");
            // Initialize ViewModel with token
            postsViewModel = new ViewModelProvider(this).get(PostsViewModel.class);
            postsViewModel.setUsername(username);
            postsViewModel.setToken(token);

            if (profilePictureByteArray != null) {
                Bitmap profilePictureBitmap = BitmapFactory.decodeByteArray(profilePictureByteArray, 0, profilePictureByteArray.length);
                postsViewModel.init(username, token, profilePictureBitmap);
            } else {
                postsViewModel.init(username, token, null); // Pass null if profile picture is not available
            }

            // Initialize UsersViewModel

        } else {
            // Handle case where intent is null or token is not provided
            Toast.makeText(this, "Failed to get token", Toast.LENGTH_SHORT).show();
        }

        // Observe changes in posts data
        postsViewModel.getPosts().observe(this, posts -> {
            if (share){
                share=false;
                postsViewModel.refreshPosts();
            }
            if (posts != null && !posts.isEmpty()) {
                adapter.setPosts(posts);
                postDao.clear();
                postDao.insertList(posts);
            }

        });

        fetchAndDisplayPosts();
    }

    private void initViews() {
        profilePictureButton = findViewById(R.id.btn_profile_picture);
        menuButton = findViewById(R.id.menubtn);
        newPostButton = findViewById(R.id.addbtn);
        whatsNewButton = findViewById(R.id.whatsNewButton);
        lstPosts = findViewById(R.id.lstPosts);
    }

    private void setUpRecyclerView() {
        adapter = new PostAdapter(this, username, postsViewModel, displayName);
        lstPosts.setAdapter(adapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));
    }

    private void fetchUserData() {
        Intent activityIntent = getIntent();
        if (activityIntent != null) {
            token = activityIntent.getStringExtra("Token");
            username = activityIntent.getStringExtra("Username");
            usersViewModel.setToken(token);
            usersViewModel.setUserid(username);

            // Trigger the network request to fetch user data
            usersViewModel.getCurrentUser(username, token).observe(this, new Observer<UserCreatePost>() {
                @Override
                public void onChanged(UserCreatePost userCreatePost) {
                    // Display user data or handle the single userCreatePost object as needed
                    // Example: Set the profile picture
                    ImageView profilePictureImageView = findViewById(R.id.image_profile_picture);
                    setAsImage(userCreatePost.getProfilePic(), profilePictureImageView);
                    displayName = userCreatePost.getDisplayName();
                    setUpRecyclerView();
                }
            });
        }
    }


    private void fetchAndDisplayPosts() {
        // Observe changes in posts data
        postsViewModel.getPosts().observe(this, posts -> {
            if (posts != null && !posts.isEmpty()) {
                // Update RecyclerView adapter with fetched posts
                adapter.setPosts(posts);
            } else {
                Toast.makeText(FeedActivity.this, "No posts found", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static void setAsImage(String strBase64, ImageView imageView) {
        if (strBase64.equals(defaultPfp)) {
//            imageView.setImageResource(R.drawable.defaultprofilepic);
        } else {
            byte[] decodedString = Base64.decode(strBase64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageView.setImageBitmap(decodedByte);
        }

    }

    private void initWebServiceAPI() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    private void setClickListeners() {

        menuButton.setOnClickListener(v -> {
            Intent intent = new Intent(FeedActivity.this, MenuActivity.class);

            intent.putExtra("Username", username);
            intent.putExtra("ProfilePicture", profilePictureByteArray);
            intent.putExtra("Token",postsViewModel.getToken());

            startActivity(intent);


        });

        newPostButton.setOnClickListener(v -> startActivityForResult(new Intent(FeedActivity.this, PostActivity.class), REQUEST_NEW_POST));

        whatsNewButton.setOnClickListener(v -> startActivityForResult(new Intent(FeedActivity.this, PostActivity.class), REQUEST_NEW_POST));
        profilePictureButton.setOnClickListener(v -> {
            Intent intent = new Intent(FeedActivity.this, MyProfileActivity.class);
            intent.putExtra("Username", username);
            intent.putExtra("ProfilePicture", profilePictureByteArray);
            intent.putExtra("Token", token);


            startActivity(intent);
        });

    }

    private byte[] compressImage(Bitmap imageBitmap) {
        if(imageBitmap==null){
            return null;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream); // Adjust quality as needed
        return outputStream.toByteArray();
    }


    // This method will be called when returning from NewPostActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_NEW_POST && resultCode == RESULT_OK && data != null) {
            String postText = data.getStringExtra("postText");
            String postImagePath = data.getStringExtra("postImagePath");

            // Call the ViewModel method to fetch user data
            usersViewModel.getCurrentUser(username, token).observe(this, new Observer<UserCreatePost>() {
                @Override
                public void onChanged(UserCreatePost user) {
                    if (user != null) {
                        Log.d("onChanged", "Successfully fetched user data. Display name: " + user.getDisplayName());

                        // Set profile picture
                        String profilePic = user.getProfilePic();
                        if (profilePic != null && !profilePic.equals(defaultPfp)) {
                            byte[] decodedString = Base64.decode(profilePic, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            Bitmap profileImageBitmap = decodedByte;
                            Bitmap postImageBitmap = BitmapFactory.decodeFile(postImagePath);
                            Date currentTime = new Date(System.currentTimeMillis());
                            byte[] compressedPostImage = compressImage(postImageBitmap);
                            String postImage = null;
                            if (compressedPostImage != null) {
                                postImage = Base64.encodeToString(compressedPostImage, Base64.DEFAULT);
                                // Continue processing...
                            } else {
                                // Handle the case where the byte array is null
                                Log.e("FeedActivity", "Compressed post image is null");
                            }
                            String profileImage = BitmapConverter.bitmapToString(profileImageBitmap);
                            Post newPost = new Post(user.getDisplayName(), postText, postImage, 0, null, profileImage, currentTime, username);

                            // Add new post to ViewModel
                            postsViewModel.addPost(newPost);
                            share=true;
                            // Refresh the RecyclerView
                            fetchAndDisplayPosts();
                        }
                    } else {
                        Log.e("onChanged", "Failed to fetch user data.");
                    }
                }
            });
        } else if (requestCode == EDIT_POST && resultCode == RESULT_OK && data != null) {
            // Check if a post was edited
            boolean isPostEdited = data.getBooleanExtra("isPostEdited", false);
            if (isPostEdited) {
                // If a post was edited, refresh the posts in the feed
                fetchAndDisplayPosts();
            }
        }
    }






    private void updateViews(UserCreatePost user) {
        if (user != null) {
            String displayMane= user.getDisplayName();

            // Set profile picture
            String profilePic = user.getProfilePic();
            if (profilePic != null && !profilePic.equals(MainActivity.defaultPfp)) {
                byte[] decodedString = Base64.decode(profilePic, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                Bitmap postImageBitmap = decodedByte;
            }
        }
    }

}