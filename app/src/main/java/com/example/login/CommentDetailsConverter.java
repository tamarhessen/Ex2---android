package com.example.login;

import androidx.room.TypeConverter;
import com.google.gson.Gson;

public class CommentDetailsConverter {

    @TypeConverter
    public static User.CommentDetails fromString(String value) {
        return new Gson().fromJson(value, User.CommentDetails.class);
    }

    @TypeConverter
    public static String toString(User.CommentDetails commentDetails) {
        return new Gson().toJson(commentDetails);
    }
}

