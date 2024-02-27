package com.example.login.facebookdesign;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {CommentPost.class}, version = 1)
public abstract class CommentPostDB extends RoomDatabase {
    public abstract CommentPostDao commentPostDao();
}

