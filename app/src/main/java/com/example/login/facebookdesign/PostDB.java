package com.example.login.facebookdesign;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Post.class,CommentPost.class}, version = 1)
public abstract class PostDB extends RoomDatabase {
    public abstract PostDao postDao();

    public abstract CommentPostDao commentPostDao();
}

