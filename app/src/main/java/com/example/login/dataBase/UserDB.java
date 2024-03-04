package com.example.login.dataBase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.login.facebookdesign.CommentPost;
import com.example.login.facebookdesign.CommentPostDao;
import com.example.login.facebookdesign.User;
import com.example.login.facebookdesign.UserDao;

@Database(entities = {User.class, CommentPost.class}, version = 1)
public abstract class UserDB extends RoomDatabase {
    public abstract UserDao userDao();

    public abstract CommentPostDao commentPostDao();
}

