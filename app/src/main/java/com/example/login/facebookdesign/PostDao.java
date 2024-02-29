package com.example.login.facebookdesign;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import java.util.List;

@Dao
public interface PostDao {

    @Query("SELECT * FROM post")
    List<Post> index();

    @Query("SELECT * FROM post WHERE id = :id")
    Post get(int id);

    @Insert
    void insert(Post... posts);

    @Update
    void update(Post... posts);

    @Query("DELETE FROM post")
    void deleteAll();
    @Query("DELETE FROM post")
    void clear();

    @Insert
    void insertList(List<Post> posts);

}
