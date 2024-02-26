package com.example.login.facebookdesign;

import android.graphics.Bitmap;

import com.example.login.facebookdesign.SignUpRequest;

import java.util.ArrayList;
import java.util.List;

public class UserCredentials {
    private static List<User> users = new ArrayList<>();

    public static void addUser(SignUpRequest signUpRequest, Bitmap profilePicture) {
        users.add(new User(signUpRequest.getUsername(), signUpRequest.getPassword(), profilePicture, signUpRequest.getDisplayName()));
    }

    public static boolean checkCredentials(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true; // Credentials match
            }
        }
        return false; // Credentials do not match
    }

    public static List<User> getUsers() {
        return users;
    }

    public static boolean isUsernameExists(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }


    static class User {
        private String username;
        private String password;
        private Bitmap profilePicture;
        private String displayName;

        public User(String username, String password, Bitmap profilePicture, String displayName) {
            this.username = username;
            this.password = password;
            this.profilePicture = profilePicture;
            this.displayName = displayName;
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
    }
}
