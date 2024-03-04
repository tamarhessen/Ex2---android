package com.example.login.facebookdesign;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Post.class}, version = 4)
 public abstract class PostDB extends RoomDatabase {
 public abstract PostDao postDao();
 }