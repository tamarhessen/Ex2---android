package com.example.login.facebookdesign;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CommentPostDao {

    @Query("SELECT * FROM commentPost")
    List<CommentPost> index();

    @Query("SELECT * FROM commentPost WHERE postId = :id")
    CommentPost get(int id);

    @Insert
    void insert(CommentPost... commentPosts);

    @Update
    void update(CommentPost... commentPosts);
    @Update
    void delete(CommentPost... commentPosts);

    @Query("DELETE FROM commentPost")
    void deleteAll();

}