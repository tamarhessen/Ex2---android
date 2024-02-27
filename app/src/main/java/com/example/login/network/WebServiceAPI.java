package com.example.login.network;
import android.net.Credentials;

import com.example.login.CommentToSend;
import com.example.login.OnlyUsername;
import com.example.login.User;
import com.example.login.UserCreatePost;
import com.example.login.UserDataFromAdd;
import com.example.login.entities.Post;
import com.example.login.UserCreateToken;
import com.example.login.UserCredentials;
import com.example.login.model.SignUpRequest;

import org.w3c.dom.Comment;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface WebServiceAPI {
    @POST("signup")
    Call<Void> signUp(@Body SignUpRequest request);
    @POST("Users")
    Call<Void> createUser(@Body UserCreatePost userCreatePost);

    @POST("Tokens")
    Call<String> getToken(@Body UserCreateToken userCreateToken);
    @POST("checkCredentials")
    Call<Boolean> checkCredentials(@Body Credentials credentials);

    @GET("Users/{id}")
    Call<Post> getUser(@Path("id") String id, @Header("Authorization") String authHeader);

    @GET("Posts")
    Call<List<UserCredentials>> getUsers(@Header("Authorization") String authHeader);

    @POST("Posts")
    Call<UserDataFromAdd> addPost(@Body OnlyUsername onlyUsername, @Header("Authorization") String authHeader);

    @GET("Posts/{id}/Comments")
    Call<List<Comment>> getComments(@Path("id") int id, @Header("Authorization") String authHeader);

    @POST("Posts/{id}/Comments")
    Call<Void> postComment(@Path("id") int id, @Body CommentToSend comment, @Header("Authorization") String authHeader);
    // Define the method to fetch user data
    @GET("user/data")
    Call<User> getUserData(@Header("Authorization") String token);
}
