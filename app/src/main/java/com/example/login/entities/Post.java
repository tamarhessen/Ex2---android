package com.example.login.entities;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Post {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String author;
    private String content;
    private int likes;
    private Bitmap pic;
    private Bitmap profilepic;
    private boolean liked;
    private long timestamp;
    private List<String> comments;

    public Post() {
        // Default constructor
    }

    public Post(String author, String content, Bitmap pic, int likes, Bitmap profilepic,long timestamp) {
        this.author = author;
        this.content = content;
        this.pic = pic;
        this.likes = likes;
        this.profilepic = profilepic;
        this.liked=false;
        comments = new ArrayList<>();
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public Bitmap getPic() {
        
        return pic;
    }

    public void setPic(Bitmap pic) {
        this.pic = pic;
    }

    public Bitmap getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(Bitmap profilepic) {
        this.profilepic = profilepic;
    }



    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public String getTimestamp() {
        return convertTimestampToString(timestamp);
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public static String convertTimestampToString(long timestamp) {
        // Convert the timestamp to a Date object
        Date date = new Date(timestamp);

        // Format the date using SimpleDateFormat
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a\ndd, MMM, yyyy", Locale.getDefault());
        return sdf.format(date);
    }



    public void addComment(String newComment) {
        comments.add(newComment);
    }

    public void editComment(int index, String editedComment) {
        if (index >= 0 && index < comments.size()) {
            comments.set(index, editedComment);
        }
    }

    public void deleteComment(int index) {
        if (index >= 0 && index < comments.size()) {
            comments.remove(index);
        }
    }


    public List<String> getComments() {
        return comments;
    }
}

