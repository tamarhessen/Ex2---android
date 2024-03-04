package com.example.login.facebookdesign;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import java.util.ArrayList;
import java.util.List;

@Entity
@TypeConverters({ListStringConverter.class, BitmapConverter.class})
public class Post {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String Creator;
    private String PostText;
    private int likes;
    private long timestamp;
    private String PostImg;
    private String CreatorImg;
    private boolean liked;
    private List<String> Comments;

    public Post() {
        // Default constructor
    }

    public Post(String creator, String postText, String pic, int likes, String CreatorImg,long timestamp) {
        this.Creator = creator;
        this.PostText = postText;
        this.PostImg
                = pic;
        this.likes = likes;
        this.CreatorImg = CreatorImg;
        this.liked=false;
        Comments = new ArrayList<>();
        this.timestamp = timestamp;
        this.id=0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPostText() {
        return PostText;
    }

    public void setPostText(String postText) {
        this.PostText = postText;
    }

    public String getCreator() {
        return Creator;
    }

    public void setCreator(String creator) {
        this.Creator = creator;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getPostImg
            () {
        return PostImg
                ;
    }

    public void setPostImg
            (String pic) {
        this.PostImg
                = pic;
    }

    public String getCreatorImg() {
        return CreatorImg;
    }

    public void setCreatorImg(String CreatorImg) {
        this.CreatorImg = CreatorImg;
    }



    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public long getTimestamp() {
        return timestamp;
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
        Comments.add(newComment);
    }

    public void editComment(int index, String editedComment) {
        if (index >= 0 && index < Comments.size()) {
            Comments.set(index, editedComment);
        }
    }

    public void deleteComment(int index) {
        if (index >= 0 && index < Comments.size()) {
           Comments.remove(index);
        }
    }


    public List<String> getComments() {
        return Comments;
    }

    public void setComments(List<String> comments) {
        this.Comments = comments;
    }

}
