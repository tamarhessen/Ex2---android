package com.example.login.facebookdesign;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class PostsViewModel extends ViewModel {
    private PostsRepository repository;
    private LiveData<List<Post>> posts;

    public PostsViewModel(){
        repository = new PostsRepository();
        posts = repository.getAll();
    }

    public LiveData<List<Post>> get() {return posts;}

    public Post get(int position) {
        return posts.getValue().get(position);
    }
    public void refresh() {repository.refresh();}
    public void add(String username, Context context) {
        repository.add(username,context);
    }
}
