package com.example.login.facebookdesign;

import android.graphics.Bitmap;

public class UserCreatePost {
    private String username,password,displayName,profilePic;

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
