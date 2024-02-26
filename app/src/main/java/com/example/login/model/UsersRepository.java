package com.example.login.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {User.class,CommentPost.class}, version = 1)
public abstract class UserDB extends RoomDatabase {
    public abstract UserDao userDao();

    public abstract CommentPostDao commentPostDao();
}