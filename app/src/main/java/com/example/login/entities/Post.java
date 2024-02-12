package com.example.login.entities;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

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

    public Post() {
        // Default constructor
    }

    public Post(String author, String content, Bitmap pic, int likes, Bitmap profilepic) {
        this.author = author;
        this.content = content;
        this.pic = pic;
        this.likes = likes;
        this.profilepic = profilepic;
        this.liked=false;
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
}

