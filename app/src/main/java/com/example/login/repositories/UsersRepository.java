package com.example.login.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.login.facebookdesign.User;
import com.example.login.facebookdesign.UserCreatePost;
import com.example.login.facebookdesign.UserDao;
import com.example.login.API.UsersAPI;

import java.util.LinkedList;
import java.util.List;

public class UsersRepository {
    private UserListData userListData;
    private UsersAPI api;
    private UserDao userDao;
    private  String userId;
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


    // Custom LiveData class to hold list of users
    class UserListData extends MutableLiveData<List<User>> {
        public UserListData() {
            super();
            setValue(new LinkedList<>());
        }
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
        api.add(userCreatePost, context);
    }

    // Method to refresh the list of users
    public void refresh(String token) {
        // Trigger the API call to refresh users
        api.get(userId,token);
    }
}
