package com.example.login.facebookdesign;

import androidx.room.TypeConverter;
import com.google.gson.Gson;

public class UserNoPasswordConverter {

    @TypeConverter
    public static User.UserNoPassword fromString(String value) {
        return new Gson().fromJson(value, User.UserNoPassword.class);
    }

    @TypeConverter
    public static String toString(User.UserNoPassword userNoPassword) {
        return new Gson().toJson(userNoPassword);
    }
}
