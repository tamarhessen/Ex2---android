package com.example.login.repositories;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.login.facebookdesign.FriendsSchema;
import com.example.login.facebookdesign.User;
import com.example.login.facebookdesign.UserCreatePost;
import com.example.login.facebookdesign.UserDao;
import com.example.login.API.UsersAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersRepository {
    private UserListData userListData;
    private UsersAPI api;
    private UserDao userDao;
    private  String userId;
    private String token;
    public UsersRepository(String userid) {
        // Initialize repository with default values or null references
        this.userDao = null; // You may need to handle this case in your repository methods
        this.userListData = new UserListData();
        this.api = new UsersAPI(userListData, userDao);
        this.userId=userid;
    }
    public UsersRepository(UserDao userDao,String userId) {
        this.userDao = userDao;
        this.userId=userId;
        userListData = new UserListData();
        // Initialize UsersAPI with userListData and userDao
        api = new UsersAPI(userListData, userDao);
    }

    public  LiveData<UserCreatePost> getCurrentUser(String userid,String token) {
        return api.get(userid,token);

    }

    public void editUser(String userId, String displayName, String base64EncodedImage, String token) {
        api.editUser(userId,displayName,base64EncodedImage,token);
    }
    public void deleteUser(String userId, String token) {
        api.deleteUser(userId,token);
    }
    public void askFriend(String userId, String token) {
        api.askFriend(userId,token);
    }



    public void acceptFriend(String userId,String friendId, String token) {
        Log.d("sss", "Username: " + userId);
        api.acceptFriend(userId,friendId,token);
    }
    public void deleteFriend(String userId,String friendId, String token) {
        Log.d("sss", "Username: " + userId);
        api.deleteFriend(userId,friendId,token);
    }

    public LiveData<Pair<List<String>, List<String>>>
    getFriends(String username, String token) {
        return api.getFriends(username,token);
    }

    public interface FriendsCallback {
        void onSuccess(Pair<List<String>, List<String>> data);

        void onError(String errorMessage);
    }



    // Custom LiveData class to hold list of users
    class UserListData extends MutableLiveData<List<User>> {
        public UserListData() {
            super();
            setValue(new LinkedList<>());
        }
    }

    public LiveData<String> login(Context context, String username, String password) {
        MutableLiveData<String> tokenLiveData = new MutableLiveData<>();

        api.createToken(context, username, password, new UsersAPI.TokenCallback() {
            @Override
            public void onTokenGenerated(String token) {
                if (token != null) {
                    tokenLiveData.postValue(token);
                } else {
                    Toast.makeText(context, "Username or password incorrect", Toast.LENGTH_SHORT).show();
                    tokenLiveData.postValue(null);
                }
            }
        });

        return tokenLiveData;
    }

    private void fetchUserByUsername(String username, String token, LoginCallback callback) {
        api.getUserByUsername(username, token).observeForever(new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    // User found, pass token to the callback indicating successful login
                    callback.onLoginSuccess(token);
                } else {
                    // User not found, pass error message to the callback
                    callback.onLoginError("User does not exist");
                }
            }
        });
    }




    public interface LoginCallback {
        void onLoginSuccess(String token);
        void onLoginError(String errorMessage);
    }
    // Method to fetch all users
    public LiveData<List<User>> getAll() {
        // Trigger the API call to fetch users
        //api.get(userId,);
        // Return LiveData
        return userListData;
    }

    // Method to add a new user
    public void add(UserCreatePost userCreatePost, Context context) {
        userCreatePost.setProfilePic("data:image/jpeg;charset=utf-8;base64,"+userCreatePost.getProfilePic());
        api.add(userCreatePost, context);
    }

    // Method to refresh the list of users
    public void refresh(String token) {
        // Trigger the API call to refresh users
        api.get(userId,token);
    }
}
