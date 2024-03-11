package com.example.login.viewModels;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.login.facebookdesign.Post;
import com.example.login.repositories.PostsRepository;

import java.util.List;

public class PostsViewModel extends AndroidViewModel {

    private PostsRepository repository;
    private LiveData<List<Post>> posts;
    private String username;
    private String token;
    private MutableLiveData<Boolean> likePostLiveData = new MutableLiveData<>();
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
        posts = repository.getAll();
        return posts;
    }
    public LiveData<List<Post>> getPostsforUserName() {
        posts = repository.getOnlyUser();
        return posts;
    }

    public void addPost(Post post) {
        repository.add(post);
    }

    public void deletePost(Post post) {
        repository.deletePost(post.getId(),token);
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
    public void editPost(Post updatedPost) {
        // Perform the edit post action here, such as making a network request or updating a local database
        // After editing, update the LiveData if necessary
        repository.editPost(updatedPost.getId(),updatedPost,token);
    }
    public void likePost(int postId, String token,Post post){
        repository.likepost(postId,token, post);
        likePostLiveData.postValue(true);
    }
    public LiveData<List<Post>> refreshPosts() {
        posts=repository.refreshPosts();
        return posts;
    }

    public LiveData<Boolean> getLikePostLiveData() {
        return likePostLiveData;
    }
}