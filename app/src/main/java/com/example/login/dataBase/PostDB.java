package com.example.login.dataBase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.login.facebookdesign.Post;
import com.example.login.facebookdesign.PostDao;

@Database(entities = {Post.class}, version = 6)
 public abstract class PostDB extends RoomDatabase {
 public abstract PostDao postDao();
 }