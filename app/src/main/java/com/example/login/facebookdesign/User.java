package com.example.login.facebookdesign;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "user")
@TypeConverters({UserNoPasswordConverter.class, CommentDetailsConventor.class})
public class User {

    @PrimaryKey(autoGenerate = false)
    private int id;
    private UserNoPassword user;
    private CommentDetails lastComment;

    public User(int id, UserNoPassword user, CommentDetails lastComment) {
        this.id = id;
        this.user = user;
        this.lastComment = lastComment;
    }

    public User(UserDataFromAdd userDataFromAdd) {
        this.id = userDataFromAdd.getId();
        this.user = userDataFromAdd.getUser();
        this.lastComment = null;
    }

    public int getId() {
        return id;
    }

    public UserNoPassword getUser() {
        return user;
    }

    public CommentDetails getLastComment() {
        return lastComment;
    }

    class UserNoPassword {
        private String username;
        private String displayName;
        private Bitmap profilePic;


        public UserNoPassword(String username, String displayName, Bitmap profilePic) {
            this.username = username;
            this.displayName = displayName;
            this.profilePic = profilePic;
        }

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

    class CommentDetails{
        private int id;
        private String created;
        private String content;

        public CommentDetails(int id, String created, String content) {
            this.id = id;
            this.created = created;
            this.content = content;
        }

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
