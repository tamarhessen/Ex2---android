package com.example.login;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class}, version = 3) // Incremented version number
public abstract class AppDB extends RoomDatabase {
    private static AppDB instance;

    public abstract UserDao userDao();

    public static synchronized AppDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDB.class, "app_database")
                    .fallbackToDestructiveMigration() // Handle migration by dropping existing data
                    .build();
        }
        return instance;
    }
}
