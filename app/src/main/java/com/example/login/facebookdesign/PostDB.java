package com.example.login.facebookdesign;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Post.class}, version = 1)
@TypeConverters(PostConverter.class) // Apply the type converter here
public abstract class PostDB extends RoomDatabase {
 public abstract PostDao postDao();
}
