package com.example.login.facebookdesign;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class CommentsViewModel extends ViewModel {

    private CommentsRepository repository;
    private LiveData<List<Comment>> comments;
    private int id;

    public CommentsViewModel() {

    }

    public void init(int id){
        repository = new CommentsRepository(id);
        comments = repository.getAll();
    }

    public LiveData<List<Comment>> get() {return comments;}
    public void refresh() {
        Log.d("refresh2_abcde", "refresh2");
        repository.refresh();
    }
    public void add(CommentToSend comment) {
        repository.add(comment);
    }
}
