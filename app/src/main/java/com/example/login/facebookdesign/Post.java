package com.example.login.facebookdesign;

import android.graphics.Bitmap;

import androidx.lifecycle.SavedStateHandle;
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
    private int PostLikes;
    private long timestamp;
    private String PostImg;
    private String CreatorImg;
    private boolean liked;
    private List<String> PeopleLiked;

    private List<String> Comments;

    public Post() {
        // Default constructor
    }

    public Post(String creator, String postText, String pic, int likes,List<String> peopleLiked, String CreatorImg,long timestamp) {
        this.Creator = creator;
        this.PostText = postText;
        this.PostImg
                = pic;
        this.PostLikes = likes;
        this.CreatorImg = CreatorImg;
        this.liked=false;
        Comments = new ArrayList<>();
        this.timestamp = timestamp;
        this.PeopleLiked=peopleLiked;

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
    public List<String> getPeopleLiked() {
        return PeopleLiked;
    }
    public void setPeopleLiked(List<String> peopleLiked){
        this.PeopleLiked=peopleLiked;
    }
    public String getCreator() {
        return Creator;
    }

    public void setCreator(String creator) {
        this.Creator = creator;
    }

    public int getPostLikes() {
        return PostLikes;
    }

    public void setPostLikes(int likes) {
        this.PostLikes = likes;
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

    public List<String> getLikedUsers() {
        // Initialize an empty list to store liked users
        List<String> likedUsers = new ArrayList<>();

        // If the post is liked, add the creator to the list of liked users
        if (liked) {
            likedUsers.add(Creator);
        }

        // Return the list of liked users
        return likedUsers;
    }

}
