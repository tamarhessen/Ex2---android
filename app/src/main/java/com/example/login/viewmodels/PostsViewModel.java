package com.example.login.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.login.entities.Post;
import com.example.login.JsonParser; // Import your JSON parser here

import java.util.ArrayList;
import java.util.List;

public class PostsViewModel extends ViewModel {
    private MutableLiveData<List<Post>> postsLiveData;

    public LiveData<List<Post>> getPosts(Context context) {
        if (postsLiveData == null) {
            postsLiveData = new MutableLiveData<>();
            // Initialize the list only if it's null
            List<Post> posts = loadPostsFromJson(context); // Load posts from JSON here
            postsLiveData.setValue(posts);
        }
        return postsLiveData;
    }

    private List<Post> loadPostsFromJson(Context context) {
        // Use your JSON parser to load posts from JSON file
        return JsonParser.parseJson(context); // Load posts from JSON using your parser
    }

    public void addPost(Post post) {
        // Retrieve the current list of posts
        List<Post> currentPosts = postsLiveData.getValue();
        if (currentPosts == null) {
            currentPosts = new ArrayList<>();
        }
        // Add the new post
        currentPosts.add(post);
        // Update the LiveData
        postsLiveData.setValue(currentPosts);
    }

    public void removePost(Post post) {
        // Retrieve the current list of posts
        List<Post> currentPosts = postsLiveData.getValue();
        if (currentPosts != null) {
            // Remove the specified post
            currentPosts.remove(post);
            // Update the LiveData
            postsLiveData.setValue(currentPosts);
        }
    }
}
