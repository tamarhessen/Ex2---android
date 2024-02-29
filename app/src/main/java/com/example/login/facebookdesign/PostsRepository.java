package com.example.login.facebookdesign;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.LinkedList;
import java.util.List;

public class PostsRepository {
    private PostListData postListData;
    private PostAPI api;
    private PostDao postDao;

    public PostsRepository() {
        PostDB postDB = LocalDB.postDB;
        postDao = postDB.postDao();
        postListData = new PostListData();
        api = new PostAPI(postListData, postDao);
    }

    class PostListData extends MutableLiveData<List<Post>> {
        public PostListData() {
            super();
            setValue(new LinkedList<>());
        }

        @Override
        protected void onActive() {
            super.onActive();
            new Thread(() -> {
                postListData.postValue(postDao.index());
            }).start();
            api.get();


        }
    }

    public LiveData<List<Post>> getAll() {
        return postListData;
    }
    public void add(String username,Context context){
        api.add(username, context);
    }
    public void refresh() {
        api.get();
    }
}
