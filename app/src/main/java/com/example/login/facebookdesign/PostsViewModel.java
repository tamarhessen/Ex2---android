package com.example.login.facebookdesign;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.login.facebookdesign.Post;

import java.util.List;

public class PostsViewModel extends AndroidViewModel {

    private PostsRepository repository;
    private LiveData<List<Post>> posts;

    public PostsViewModel(@NonNull Application application) {
        super(application);
        repository = new PostsRepository(application.getApplicationContext());
        posts = repository.getAll();
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
}
