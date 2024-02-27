package com.example.login.network;
import com.example.login.facebookdesign.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PostService {

    @POST("posts")
    Call<Void> createPost(@Body Post post);
    @GET("posts") // Adjust the endpoint URL accordingly
    Call<List<Post>> getPosts();
}