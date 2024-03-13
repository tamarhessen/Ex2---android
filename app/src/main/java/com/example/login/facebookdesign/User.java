package com.example.login.facebookdesign;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "user")
@TypeConverters({UserNoPasswordConverter.class, CommentDetailsConventor.class})
public class User {

    @PrimaryKey(autoGenerate = false)
    private int id;
    private String username; // Change to String type
    private UserNoPassword user;
    public CommentDetails lastComment;
    public String password;
    @TypeConverters(ListStringConverter.class)
    public List<String> FriendList;
    @TypeConverters(ListStringConverter.class)
    public List<String> PendingList;
    private String profilePic;

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public User(int id, String username, UserNoPassword user, CommentDetails lastComment, String
            password) {
        this.id = id;
        this.username = username;
        this.user = user;
        this.lastComment = lastComment;
        this.password=password;
        this.FriendList = new ArrayList<>();
        this.PendingList = new ArrayList<>();
    }

    public List<String> getPendingList() {
        return PendingList;
    }

    public void setPendingList(List<String> pendingList) {
        PendingList = pendingList;
    }

    public User(UserDataFromAdd userDataFromAdd) {
        this.id = userDataFromAdd.getId();
        this.user = userDataFromAdd.getUser();
        this.lastComment = null;
    }

    public List<String> getFriendList() {
        return FriendList;
    }

    public void setFriendList(List<String> friendList) {
        FriendList = friendList;
    }
    public void addFriend(String friend) {
    FriendList.add(friend);
}
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username; // Add getter method for username
    }

    public UserNoPassword getUser() {
        return user;
    }

    public CommentDetails getLastComment() {
        return lastComment;
    }

    // Static nested class for user details without password
    public static class UserNoPassword {
        private String username;
        private String displayName;
        private Bitmap profilePic;

        public UserNoPassword(String username, String displayName, Bitmap profilePic) {
            this.username = username;
            this.displayName = displayName;
            this.profilePic = profilePic;
        }

        // Getter and setter methods
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }


        public Bitmap getProfilePic() {
            return profilePic;
        }

        public void setProfilePic(Bitmap profilePic) {
            this.profilePic = profilePic;
        }
    }

    // Static nested class for comment details
    public static class CommentDetails {
        private int id;
        private String created;
        private String content;

        public CommentDetails(int id, String created, String content) {
            this.id = id;
            this.created = created;
            this.content = content;
        }

        // Getter and setter methods
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
