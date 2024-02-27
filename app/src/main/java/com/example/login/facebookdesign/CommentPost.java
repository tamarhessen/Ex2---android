package com.example.login.facebookdesign;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.LinkedList;
import java.util.List;

@Entity(tableName = "commentPost")
@TypeConverters(CommentConventor.class)
public class CommentPost {
    @PrimaryKey(autoGenerate = false)
    private int postId;
    private List<Comment> listComment;
    @Ignore
    public CommentPost(int postId, List<Comment> listComment) {
        this.postId = postId;
        this.listComment = listComment;
    }
    public CommentPost(int postId) {
        this.postId = postId;
        this.listComment = new LinkedList<>();
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public List<Comment> getListComment() {
        return listComment;
    }

    public void setListComment(List<Comment> listComment) {
        this.listComment = listComment;
    }
}
