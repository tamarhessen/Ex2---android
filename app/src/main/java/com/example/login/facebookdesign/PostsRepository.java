package com.example.login.facebookdesign;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.LinkedList;
import java.util.List;

public class PostsRepository {
    private PostDao dao;
    private PostListData postListData;
    private PostAPI api;
    private Context context;

    public PostsRepository(Context context) {
        this.context = context;
        PostDB db = LocalDB.getInstance(context);
        dao = db.postDao();
        postListData = new PostListData();
        api = new PostAPI(postListData, dao);
    }

    public LiveData<List<Post>> getAll() {
        return postListData;
    }

    public void add(Post post) {
        postListData.add(post);
    }

    public void delete(Post post) {
        postListData.delete(post);
    }

    public void reload() {
        postListData.reload();
    }

    class PostListData extends MutableLiveData<List<Post>> {
        public PostListData() {
            super(new LinkedList<>());
        }

        public void add(Post post) {
            List<Post> posts = getValue();
            if (posts != null) {
                posts.add(post);
                setValue(posts);
            }
        }

        public void delete(Post post) {
            List<Post> posts = getValue();
            if (posts != null) {
                posts.remove(post);
                setValue(posts);
            }
        }

        public void reload() {
            // You can implement the logic to reload posts here
        }
    }
}