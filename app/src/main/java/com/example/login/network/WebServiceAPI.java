package com.example.login.network;

import com.example.login.facebookdesign.Comment;
import com.example.login.facebookdesign.CommentToSend;
import com.example.login.facebookdesign.OnlyUsername;
import com.example.login.facebookdesign.Post;
import com.example.login.facebookdesign.User;
import com.example.login.facebookdesign.UserCreatePost;
import com.example.login.facebookdesign.UserCreateToken;
import com.example.login.facebookdesign.UserDataFromAdd;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface WebServiceAPI {


    // Modified method for adding a post with an image
    @Multipart
    @POST("Posts")
    Call<Post> addPostWithImage(@Part("post") Post post,
                                           @Part MultipartBody.Part image,
                                           @Header("Authorization") String authHeader);
    @POST("Posts")
    Call<Post> addPost(@Body Post post,
                       @Header("Authorization") String authHeader);


    @POST("Users")
    Call<Void> createUser(@Body UserCreatePost userCreatePost);

    @POST("Tokens")
    Call<String> getToken(@Body UserCreateToken userCreateToken);

    @GET("Users/{id}")
    Call<UserCreatePost> getUser(@Path("id") String id,
                                 @Header("Authorization") String authHeader);

    @GET("Chats")
    Call<List<User>> getUsers(@Header("Authorization") String authHeader);



    @GET("Posts/{id}/Comments")
    Call<List<Comment>> getComments(@Path("id") int id,
                                    @Header("Authorization") String authHeader);

    @POST("Posts/{id}/Comments")
    Call<Void> postComment(@Path("id") int id,@Body CommentToSend comment,
                           @Header("Authorization") String authHeader);

    @GET("Posts")
    Call<List<Post>> getPosts(@Header("Authorization") String authHeader);

}