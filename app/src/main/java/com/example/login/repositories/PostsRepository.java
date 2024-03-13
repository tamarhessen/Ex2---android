package com.example.login.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.login.dataBase.LocalDB;
import com.example.login.facebookdesign.Post;
import com.example.login.API.PostAPI;
import com.example.login.dataBase.PostDB;
import com.example.login.facebookdesign.PostDao;
import com.google.gson.JsonObject;

import java.util.List;

public class PostsRepository {
    private PostDao dao;
    private MutableLiveData<List<Post>> postListData;
    private PostAPI postAPI;
    private String token;
    private String userId;

    public PostsRepository(Context context) {
        postListData = new MutableLiveData<>();
        // Initialize dao
        PostDB db = LocalDB.getInstance(context);
        dao = db.postDao();

        // Initialize PostAPI after dao is initialized

    }

    public void setTokenAndId(String token,String userId) {
        this.token = token;
        this.userId = userId;
        postAPI = new PostAPI(postListData, dao, token);
    }

    public LiveData<List<Post>> getAll() {
        // Make API call to fetch posts
        postAPI.get(userId);

        // Return LiveData
        return postListData;
    }
    public LiveData<List<Post>> getOnlyUser() {
        // Make API call to fetch posts
        postAPI.getpostsuser(userId);

        // Return LiveData
        return postListData;
    }

    public void add(Post post) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("postText", post.getPostText());
  //      jsonObject.addProperty("CreatorUsername" , post.getCreatorUsername());
        if(post.getPostImg()!=null) {
            jsonObject.addProperty("postImg", "data:image/jpeg;charset=utf-8;base64,"+post.getPostImg

                    ());
        }
        else {
            jsonObject.addProperty("postImg", "");
        }

        postAPI.createPost(userId, jsonObject, token);
        dao.insert(post);
    }

    public void deletePost(int postId, String authHeader) {
        postAPI.deletePost(userId,postId, authHeader);
        //dao.delete(post);
    }

    public void reload() {
        // Refresh the data if needed
    }


    public void editPost(int postId,Post updatedPost,String authHeader) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("postText", updatedPost.getPostText());
    //    jsonObject.addProperty("CreatorUsername" , updatedPost.getCreatorUsername());
        jsonObject.addProperty("postImg", "data:image/jpeg;charset=utf-8;base64,"+updatedPost.getPostImg
                ());
        postAPI.editPost(userId,postId,jsonObject,authHeader);
        dao.update(updatedPost);

    }
    public void likepost(int postId,String token,Post post){
        postAPI.likePost(postId,token, post);
    }

    public LiveData<List<Post>> refreshPosts() {
        postAPI.get(userId);

        // Return LiveData
        return postListData;

    }
}
