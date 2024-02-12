package com.example.login.entities;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.login.R;

@Entity

public class Post {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String author;

    private String content;
   private int likes;
    private int pic;
    private int profilepic;
    private boolean liked;

    public Post() {
      this.pic = R.drawable.pic1;
    }
    public Post(String author, String content, int pic, int likes, int profilepic) {
        this.author = author;
        this.content = content;
        this.pic = pic;
        this.likes=likes;
        this.profilepic=profilepic;
        this.liked=false;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public int getLikes() {
        return likes;
    }

    public int getPic() {
        return pic;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(int profilepic) {
        this.profilepic = profilepic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }


    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}