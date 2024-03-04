package com.example.login.API;

import com.example.login.facebookdesign.Comment;
import com.example.login.facebookdesign.CommentToSend;
import com.example.login.facebookdesign.OnlyUsername;
import com.example.login.facebookdesign.Post;
import com.example.login.facebookdesign.User;
import com.example.login.facebookdesign.UserCreatePost;
import com.example.login.facebookdesign.UserCreateToken;
import com.example.login.facebookdesign.UserDataFromAdd;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface WebServiceAPI {



    @POST("posts") // Route for creating a new post
    Call<UserDataFromAdd> addPost(@Body OnlyUsername onlyUsername,
                                  @Header("Authorization") String authHeader);
    @POST("users/{id}/posts")
    Call<Void> createPost(@Path("id") String userId, @Body JsonObject post, @Header("Authorization") String authHeader);
    @GET("posts") // Route for getting posts
    Call<List<Post>> getPosts(@Header("Authorization") String authHeader);

    @POST("Users")
    Call<Void> createUser(@Body UserCreatePost userCreatePost);

    @POST("Tokens")
    Call<String> getToken(@Body UserCreateToken userCreateToken);

    @GET("Users/{id}")
    Call<UserCreatePost> getUser(@Path("id") String id,
                                 @Header("Authorization") String authHeader);

    @GET("Users")
    Call<List<User>> getUsers(@Header("Authorization") String authHeader);



    @GET("Posts/{id}/Comments")
    Call<List<Comment>> getComments(@Path("id") int id,
                                    @Header("Authorization") String authHeader);

    @POST("Posts/{id}/Comments")
    Call<Void> postComment(@Path("id") int id,@Body CommentToSend comment,
                           @Header("Authorization") String authHeader);
    @DELETE("users/{id}/posts/{pid}") // Route for deleting a post
    Call<Void> deletePost( @Path("pid") int postId, @Header("Authorization") String authHeader);
    @PUT("users/{id}/posts/{pid}") // Route for editing a post (using PUT method)
    Call<Void> editPost(@Path("id") String userId, @Path("pid") int postId, @Body JsonObject post, @Header("Authorization") String authHeader);
    @PATCH("users/{id}/posts/{pid}") // Route for editing a post (using PATCH method)
    Call<Void> editPostPatch(@Path("id") String userId, @Path("pid") int postId, @Body JsonObject post, @Header("Authorization") String authHeader);


}
