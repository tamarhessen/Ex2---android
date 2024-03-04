package com.example.login.facebookdesign;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.login.repositories.PostsRepository;

import java.util.List;

public class PostsViewModel extends AndroidViewModel {

    private PostsRepository repository;
    private LiveData<List<Post>> posts;
    private String username;
    private String token;
    private Bitmap profilePicture;

    public PostsViewModel(@NonNull Application application) {
        super(application);
        repository = new PostsRepository(application.getApplicationContext());
    }

    public void setToken(String token) {
        repository.setTokenAndId(token,username);
        posts = repository.getAll();
    }

    public void setPosts(LiveData<List<Post>> posts) {
        this.posts = posts;
    }

    public LiveData<List<Post>> getPosts() {
        return posts;
    }

    public void addPost(Post post) {
        repository.add(post);
    }

    public void deletePost(Post post) {
        repository.delete(post);
    }

    public void reloadPosts() {
        repository.reload();
    }

    public void init(String username, String token, Bitmap profilePicture) {
        this.username = username;
        this.token = token;
        this.profilePicture = profilePicture;
        // You can perform any necessary initialization here with the username, token, and profile picture
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public void setUsername(String username){
        this.username=username;
    }
}
