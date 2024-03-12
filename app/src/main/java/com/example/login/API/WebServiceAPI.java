package com.example.login.API;

import com.example.login.facebookdesign.Comment;
import com.example.login.facebookdesign.CommentToSend;
import com.example.login.facebookdesign.FriendsSchema;
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
    Call<Post> createPost(@Path("id") String userId, @Body JsonObject post, @Header("Authorization") String authHeader);
    @GET("posts") // Route for getting posts
    Call<List<Post>> getPosts(@Header("Authorization") String authHeader);

    @GET("users/{id}/posts") // Route for getting posts
    Call<List<Post>> getPostsByUserId(@Path("id") String userId,@Header("Authorization") String authHeader);

    @POST("Users")
    Call<Void> createUser(@Body UserCreatePost userCreatePost);

    @POST("/api/users/{id}/friends")
    Call<Void> askToBeAFriend(@Path("id") String userId, @Header("Authorization") String authHeader);
    @PATCH("users/{id}/friends/{fid}")
    Call<Void> acceptFriendRequest(@Path("id") String userId, @Path("fid") String friendId, @Header("Authorization") String authHeader);
    @DELETE("users/{id}/friends/{fid}") // Route for deleting a post
    Call<Void> deleteFriend(@Path("id") String userId, @Path("fid") String friendId, @Header("Authorization") String authHeader);
    @GET("users/{id}/friends")
    Call<FriendsSchema> getFriends(@Path("id") String id,
                                   @Header("Authorization") String authHeader);

    @POST("Tokens")
    Call<String> getToken(@Body UserCreateToken userCreateToken);

    @GET("Users/{id}")
    Call<UserCreatePost> getUser(@Path("id") String id,
                                 @Header("Authorization") String authHeader);

    @GET("Users")
    Call<List<User>> getUsers(@Header("Authorization") String authHeader);


    @POST("posts/{id}") // Route for liking a post
    Call<Void> likePost(@Path("id") int postId, @Header("Authorization") String authHeader);

    @GET("Posts/{id}/Comments")
    Call<List<Comment>> getComments(@Path("id") int id,
                                    @Header("Authorization") String authHeader);

    @POST("Posts/{id}/Comments")
    Call<Void> postComment(@Path("id") int id,@Body CommentToSend comment,
                           @Header("Authorization") String authHeader);
    @DELETE("users/{id}/posts/{pid}") // Route for deleting a post
    Call<Void> deletePost( @Path("pid") int postId, @Header("Authorization") String authHeader);
    @DELETE("users/{id}") // Route for deleting a post
    Call<Void> deleteUser( @Path("id") String id,
                           @Header("Authorization") String authHeader);
    @PUT("users/{id}/posts/{pid}")
    Call<Void> editPost(@Path("id") String userId, @Path("pid") int postId, @Body JsonObject post, @Header("Authorization") String authHeader);

    @PATCH("users/ws/posts/{pid}")
    Call<Void> editPostPatch(@Path("id") String userId, @Path("pid") int postId, @Body JsonObject post, @Header("Authorization") String authHeader);
    @PUT("users/{id}")
    Call<Void> updateUserByIdPut(@Path("id") String userId, @Body JsonObject userBody, @Header("Authorization") String authHeader);

    @PATCH("users/{id}")
    Call<Void> updateUserByIdPatch(@Path("id") String userId, @Body JsonObject userBody, @Header("Authorization") String authHeader);
    @GET("users/{username}") // Route for getting a user by username
    Call<User> getUserByUsername(@Path("username") String username, @Header("Authorization") String authHeader);

}
