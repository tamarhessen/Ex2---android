package com.example.login.facebookdesign;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.ArrayList;
import java.util.List;


public class Friends{

    @PrimaryKey(autoGenerate = false)

    private String username; // Change to String type
    private User.UserNoPassword user;

    @TypeConverters(ListStringConverter.class)
    public List<String> FriendList;
    @TypeConverters(ListStringConverter.class)
    public List<String> PendingList;

    public Friends(String username, User.UserNoPassword user, List<String> FriendList, List<String> PendingList) {

        this.username = username;
        this.user = user;
        this.FriendList = new ArrayList<>();
        this.PendingList = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User.UserNoPassword getUser() {
        return user;
    }

    public void setUser(User.UserNoPassword user) {
        this.user = user;
    }

    public List<String> getFriendList() {
        return FriendList;
    }

    public void setFriendList(List<String> friendList) {
        FriendList = friendList;
    }

    public List<String> getPendingList() {
        return PendingList;
    }

    public void setPendingList(List<String> pendingList) {
        PendingList = pendingList;
    }



}
