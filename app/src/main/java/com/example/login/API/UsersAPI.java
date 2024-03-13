package com.example.login.API;

import static com.example.login.facebookdesign.MainActivity.SIM;
import static com.example.login.facebookdesign.MainActivity.baseURL;
import static com.example.login.facebookdesign.UsersActivity.token;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.login.API.WebServiceAPI;
import com.example.login.facebookdesign.CreateAccountActivity;
import com.example.login.facebookdesign.FeedActivity;
import com.example.login.facebookdesign.FriendsSchema;
import com.example.login.facebookdesign.LogInActivity;
import com.example.login.facebookdesign.OnlyUsername;
import com.example.login.facebookdesign.User;
import com.example.login.facebookdesign.UserCreatePost;
import com.example.login.facebookdesign.UserCreateToken;
import com.example.login.facebookdesign.UserDao;
import com.example.login.facebookdesign.UserDataFromAdd;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsersAPI {
    private MutableLiveData<List<User>> userListData;
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;
    private UserDao userDao;
    private static Gson gson;

    public UsersAPI(MutableLiveData<List<User>> userListData, UserDao userDao) {
        this.userListData = userListData;
        retrofit = new Retrofit.Builder().baseUrl(baseURL).
                addConverterFactory(GsonConverterFactory.create()).build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
        this.userDao = userDao;
        gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/api/")  // Make sure this is set correctly
                .addConverterFactory(GsonConverterFactory.create(gson)) // Pass the custom Gson instance
                .build();


        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }


    public LiveData<UserCreatePost> get(String username, String token) {
        MutableLiveData<UserCreatePost> userLiveData = new MutableLiveData<>();

        Call<UserCreatePost> call = webServiceAPI.getUser(username, "Bearer " + token);
        call.enqueue(new Callback<UserCreatePost>() {
            @Override
            public void onResponse(Call<UserCreatePost> call, Response<UserCreatePost> response) {
                if (response.isSuccessful()) {
                    UserCreatePost user = response.body();
                    if (user != null) {
                        String postText = user.getProfilePic();
                        // Replace "unwantedString" with the string you want to remove
                        int location = postText.indexOf(",");
                        postText = postText.substring(location + 1);
                        // Update the post's text

                        user.setProfilePic(postText);
                        // Update LiveData with the fetched user
                        userLiveData.setValue(user);
                    } else {
                        Log.e("UsersAPI", "Response body is null");
                        // Optionally, you can handle the case where response body is null
                    }
                } else {
                    Log.e("UsersAPI", "Response not successful: " + response.code());
                    // Optionally, you can handle the case where response is not successful
                }
            }

            @Override
            public void onFailure(Call<UserCreatePost> call, Throwable t) {
                // Log the error for debugging purposes
                Log.e("UsersAPI", "Failed to fetch user: " + t.getMessage());
                // Optionally, you can also update LiveData with an empty user or show an error message to the user
            }
        });

        return userLiveData;
    }


    public void add(UserCreatePost userCreatePost, Context context) {
        // Create an instance of UserCreatePost and populate it with data

        // Initialize WebServiceAPi

        // Make network request to create a user
        Call<Void> call = webServiceAPI.createUser(userCreatePost);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Display success message
                    Toast.makeText(context, "Sign-up successful", Toast.LENGTH_SHORT).show();
                    // Proceed to login activity
                    Intent intent = new Intent(context, LogInActivity.class);
                    intent.putExtra("display name", userCreatePost.getDisplayName());
                    context.startActivity(intent);
                    if (context instanceof Activity) {
                        ((Activity) context).finish();
                    }// Optional: finish the current activity to remove it from the back stack
                } else {
                    // Handle error
                    Toast.makeText(context, "Username is already taken", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
                Toast.makeText(context, "Network request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void editUser(String userId, String displayName, String base64EncodedImage, String token) {
        JsonObject userBody = new JsonObject();
        userBody.addProperty("displayName", displayName);
        userBody.addProperty("profilePic", "data:image/jpeg;charset=utf-8;base64,"+base64EncodedImage);

        // Call updateUserByIdPatch or updateUserByIdPut based on your preference
        Call<Void> call = webServiceAPI.updateUserByIdPatch(userId, userBody, "Bearer " + token);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Display success message or handle the response as needed
                } else {
                    // Handle unsuccessful response
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
            }
        });
    }
    public void deleteUser(String userId, String token) {
        JsonObject userBody = new JsonObject();


        // Call updateUserByIdPatch or updateUserByIdPut based on your preference
        Call<Void> call = webServiceAPI.deleteUser(userId, "Bearer " + token);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Display success message or handle the response as needed
                } else {
                    // Handle unsuccessful response
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
            }
        });
    }

    public void askFriend(String userId, String token) {
        JsonObject userBody = new JsonObject();


        // Call updateUserByIdPatch or updateUserByIdPut based on your preference
        Call<Void> call = webServiceAPI.askToBeAFriend(userId, "Bearer " + token);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Display success message or handle the response as needed
                } else {
                    // Handle unsuccessful response
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
            }
        });
    }
    public void acceptFriend(String userId,String friendId, String token) {
        JsonObject userBody = new JsonObject();


        // Call updateUserByIdPatch or updateUserByIdPut based on your preference
        Call<Void> call = webServiceAPI.acceptFriendRequest(userId, friendId,"Bearer " + token);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Display success message or handle the response as needed
                } else {
                    // Handle unsuccessful response
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
            }
        });
    }
    public LiveData<Pair<List<String>, List<String>>> getFriends(String username, String token) {
        MutableLiveData<Pair<List<String>, List<String>>> friendsLiveData = new MutableLiveData<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<FriendsSchema> response = webServiceAPI.getFriends(username, "Bearer " + token).execute();
                    if (response.isSuccessful()) {
                        FriendsSchema friendsSchema = response.body();
                        if (friendsSchema != null) {
                            List<String> friendList = friendsSchema.getFriendList();
                            List<String> pendingList = friendsSchema.getPendingList();
                            // Update LiveData with the fetched lists
                            friendsLiveData.postValue(new Pair<>(friendList, pendingList));
                        } else {
                            Log.e("UsersAPI", "Response body is null");
                            // Optionally, you can handle the case where response body is null
                        }
                    } else {
                        Log.e("UsersAPI", "Response not successful: " + response.code());
                        // Optionally, you can handle the case where response is not successful
                    }
                } catch (IOException e) {
                    // Log the error for debugging purposes
                    Log.e("UsersAPI", "Failed to fetch friends: " + e.getMessage());
                    // Optionally, you can also update LiveData with empty lists or show an error message to the user
                }
            }
        }).start();

        return friendsLiveData;
    }

    public void deleteFriend(String userId,String friendId, String token) {
        JsonObject userBody = new JsonObject();


        // Call updateUserByIdPatch or updateUserByIdPut based on your preference
        Call<Void> call = webServiceAPI.deleteFriend(userId, friendId,"Bearer " + token);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Display success message or handle the response as needed
                } else {
                    // Handle unsuccessful response
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
            }
        });
    }


    public void createToken(Context context, String username, String password, TokenCallback callback) {
        UserCreateToken userCreateToken = new UserCreateToken(username, password);
        Call<String> call = webServiceAPI.getToken(userCreateToken);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String token = response.body();
                    if (token != null) {
                        callback.onTokenGenerated(token);
                    } else {
                        Toast.makeText(context, "Username or password incorrect", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Username or password incorrect", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context, "Network request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public interface TokenCallback {
        void onTokenGenerated(String token);
    }
    public LiveData<User> getUserByUsername(String username, String token) {
        MutableLiveData<User> userLiveData = new MutableLiveData<>();

        Call<User> call = webServiceAPI.getUserByUsername(username, "Bearer " + token);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    if (user != null) {
                        String postText = user.getProfilePic();
                        // Replace "unwantedString" with the string you want to remove
                        int location = postText.indexOf(",");
                        postText = postText.substring(location + 1);
                        // Update the post's text

                        user.setProfilePic(postText);
                        // Update LiveData with the fetched user
                        userLiveData.setValue(user);
                    } else {
                        Log.e("UsersAPI", "Response body is null");
                        // Optionally, you can handle the case where response body is null
                    }
                } else {
                    Log.e("UsersAPI", "Response not successful: " + response.code());
                    // Optionally, you can handle the case where response is not successful
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Log the error for debugging purposes
                Log.e("UsersAPI", "Failed to fetch user by username: " + t.getMessage());
                // Update LiveData with null value to indicate failure
                userLiveData.setValue(null);
            }
        });

        return userLiveData;
    }

}


