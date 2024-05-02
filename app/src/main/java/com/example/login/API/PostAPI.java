package com.example.login.API;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.login.API.WebServiceAPI;
import com.example.login.facebookdesign.Post;
import com.example.login.facebookdesign.PostDao;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostAPI {
    private MutableLiveData<List<Post>> postListData;
    private PostDao dao;
    private WebServiceAPI webServiceAPI;
    private String token;
    public int UserId;

    public PostAPI(MutableLiveData<List<Post>> postListData, PostDao postDao, String token) {
        this.postListData = postListData;
        this.dao = postDao;
        this.token = token;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/api/")  // Make sure this is set correctly
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void get(String userId) {
        if (token == null) {
            Log.e("PostAPI", "Token is null. Cannot fetch posts.");
            // Handle the case where the token is null (e.g., display an error message)
            return;
        }
        Log.d("PostAPI", "Request Headers get: " + "Bearer " + token);
        Call<List<Post>> call = webServiceAPI.getPosts("Bearer " + token);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()) {
                    List<Post> posts = response.body();
                    if (posts != null) {
                        // Assuming dao is properly initialized, you can insert the data into the database
                        // and update the LiveData
                        new Thread(() -> {
                            // Iterate through the list of posts
                            for (Post post : posts) {
                                // Remove the unwanted string from each post's text
                                if(post.getPostImg()!=null) {
                                    String postText = post.getPostImg();
                                    // Replace "unwantedString" with the string you want to remove
                                    int location = postText.indexOf(",");
                                    postText = postText.substring(location + 1);
                                    // Update the post's text
                                    post.setPostImg(postText);
                                }
                                if(post.getCreatorImg()!=null) {
                                    String postText = post.getCreatorImg();
                                    // Replace "unwantedString" with the string you want to remove
                                    int location = postText.indexOf(",");
                                    postText = postText.substring(location + 1);
                                    // Update the post's text
                                    post.setCreatorImg(postText);
                                }
                                // Check if the current user is in the liked list of the post
                                if (post.getPeopleLiked().contains(userId)) {
                                    post.setLiked(true); // Set the liked status of the post
                                }
                            }
                            // Update the LiveData with the modified list of posts
                            postListData.postValue(posts);
                        }).start();
                    }
                } else {
                    Log.e("PostAPI", "Failed to fetch posts: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                // Handle failure
                Log.e("PostAPI", "Failed to fetch posts: " + t.getMessage());
            }
        });
    }

    public void getpostsuser(String userId) {
        if (token == null) {
            Log.e("PostAPI", "Token is null. Cannot fetch posts.");
            // Handle the case where the token is null (e.g., display an error message)
            return;
        }
        Log.d("PostAPI", "Request Headers get: " + "Bearer " + token);
        Call<List<Post>> call = webServiceAPI.getPostsByUserId(userId,"Bearer " + token);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()) {
                    List<Post> posts = response.body();
                    if (posts != null) {
                        // Assuming dao is properly initialized, you can insert the data into the database
                        // and update the LiveData
                        new Thread(() -> {
                            // Iterate through the list of posts
                            for (Post post : posts) {
                                if(post.getPostImg()!=null) {
                                    String postText = post.getPostImg();
                                    // Replace "unwantedString" with the string you want to remove
                                    int location = postText.indexOf(",");
                                    postText = postText.substring(location + 1);
                                    // Update the post's text
                                    post.setPostImg(postText);
                                }
                                if(post.getCreatorImg()!=null) {
                                    String postText = post.getCreatorImg();
                                    // Replace "unwantedString" with the string you want to remove
                                    int location = postText.indexOf(",");
                                    postText = postText.substring(location + 1);
                                    // Update the post's text
                                    post.setCreatorImg(postText);
                                }
                                // Check if the current user is in the liked list of the post
                                if (post.getPeopleLiked().contains(userId)) {
                                    post.setLiked(true); // Set the liked status of the post
                                }
                            }
                            // Update the LiveData with the modified list of posts
                            postListData.postValue(posts);
                        }).start();
                    }
                    else {postListData.postValue(null);}
                } else {
                    Log.e("PostAPI", "Failed to fetch posts: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                // Handle failure
                Log.e("PostAPI", "Failed to fetch posts: " + t.getMessage());
            }
        });
    }

    public void createPost(String userId, JsonObject jsonBody, String authHeader, Context context) {
        Call<Post> call = webServiceAPI.createPost(userId, jsonBody, "bearer " + authHeader);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()) {
                    // Post created successfully
                    Post createdPost = response.body();
                    if (createdPost != null) {
                        // Notify the listener with the created post
                    } else {
                        Log.e("PostAPI", "Failed to parse created post from response body");
                    }
                } else {
                    // Handle unsuccessful response
                    if (response.code() == 404) {
                        // Post already exists, show a message to the user
                        Log.e("PostAPI", "Post already exists.");
                        Toast.makeText(context, "Couldn't create a post. Try again later", Toast.LENGTH_SHORT).show();
                        // You can notify your UI or any observer here that the post already exists
                    } else {
                        Log.e("PostAPI", "Failed to create post: " + response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                // Log request details
                Log.d("Request Details", "URL: " + call.request().url());
                Log.d("Request Details", "Method: " + call.request().method());
                Log.d("Request Details", "Headers: " + call.request().headers());
                Log.d("Request Details", "Body: " + jsonBody);

                // Handle failure
                Log.e("PostAPI", "Failed to create post: " + t.getMessage());
            }
        });
    }


    // Define an interface for the listener


    public void deletePost(String userId,int postId, String authHeader) {
        Call<Void> call = webServiceAPI.deletePost(postId, "Bearer " + authHeader);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Post deleted successfully
                    Log.d("PostAPI", "Post deleted successfully");
                    // You can perform any additional actions here if needed
                } else {

                    // Handle unsuccessful response
                    Log.e("PostAPI", "Failed to delete post: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
                Log.e("PostAPI", "Failed to delete post: " + t.getMessage());
            }
        });
    }
    public void editPost(String userId,int postId, JsonObject updatedPostData, String authHeader,
                         Context context) {
        Call<Void> call = webServiceAPI.editPost(userId, postId, updatedPostData, "Bearer " + authHeader);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                        // Response body is null even though the request was successful

                        // Post edited successfully
                        Log.d("PostAPI", "Post edited successfully");
                        // You can perform any additional actions here if needed

                } else {
                    if (response.code() == 404) {
                        // Post already exists, show a message to the user
                        Log.e("PostAPI", "Post already exists.");
                        Toast.makeText(context, "Couldn't create a post. Try again later", Toast.LENGTH_SHORT).show();
                        // You can notify your UI or any observer here that the post already exists
                    } else {
                        // Handle unsuccessful response
                        Log.e("PostAPI", "Failed to edit post: " + response.code());
                    }
                }

            }


            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
                Log.e("PostAPI", "Failed to edit post: " + t.getMessage());
            }
        });
    }
    public void likePost(int postId, String authHeader,Post post) {
        Call<Void> call = webServiceAPI.likePost(postId, "Bearer " + authHeader);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    post.setLiked(true);
                    post.setPostLikes(post.getPostLikes() + 1);
                    // Post liked successfully
                    Log.d("PostAPI", "Post liked successfully");
                    // You can perform any additional actions here if needed
                } else {
                    // Handle unsuccessful response
                    Log.e("PostAPI", "Failed to like post: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
                Log.e("PostAPI", "Failed to like post: " + t.getMessage());
            }
        });
    }

}

