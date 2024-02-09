package com.example.login;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class UserCredentials {
    private static List<User> users = new ArrayList<>();

    public static void addUser(String username, String password, Bitmap profilePicture,String displayName) {
        users.add(new User(username, password, profilePicture,displayName));
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
        private String displayName;// New field

        public User(String username, String password, Bitmap profilePicture, String displayName) {
            this.username = username;
            this.password = password;
            this.profilePicture = profilePicture;
            this.displayName=displayName;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public Bitmap getProfilePicturePath() {
            return profilePicture;
        }
        public String getDisplayName(){
            return displayName;
        }
    }
}
