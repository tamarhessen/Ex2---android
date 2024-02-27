package com.example.login.facebookdesign;

import android.content.Context; // Import Context class

import androidx.room.Room;

public class LocalDB {
    public static UserDB userDB;
    public static CommentPostDB commentPostDB;
    private static PostDB postDB;

    // Method to get the PostDao
    public static synchronized PostDB getInstance(Context context) {
        if (postDB == null) {
            postDB = Room.databaseBuilder(context.getApplicationContext(),
                            PostDB.class, "post_database")
                    .build();
        }
        return postDB;
    }



    // Method to get the instance of PostDB

}
