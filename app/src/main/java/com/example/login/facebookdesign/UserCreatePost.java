package com.example.login.facebookdesign;

import android.graphics.Bitmap;

import androidx.room.TypeConverters;

import java.util.List;

public class UserCreatePost {
    private String username,password,displayName,profilePic;
    @TypeConverters(ListStringConverter.class)
    public List<String> FriendList;
    @TypeConverters(ListStringConverter.class)
    public List<String> PendingList;

    public List<String> getPendingList() {
        return PendingList;
    }

    public List<String> getFriendList() {
        return FriendList;
    }

    public void setFriendList(List<String> friendList) {
        FriendList = friendList;
    }

    public void setPendingList(List<String> pendingList) {
        PendingList = pendingList;
    }

    public UserCreatePost(String username, String password, String displayName, String profilePic) {
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.profilePic = profilePic;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getProfilePic() {
        return profilePic;
    }
}
