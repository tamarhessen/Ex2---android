package com.example.login.facebookdesign;

import androidx.room.TypeConverter;

import com.example.login.facebookdesign.Post;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class PostConverter {
    // Convert a List<Post> to a String for database storage
    @TypeConverter
    public static String fromPostList(List<Post> postList) {
        if (postList == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Post>>() {}.getType();
        return gson.toJson(postList, type);
    }

    // Convert a String from the database to a List<Post>
    @TypeConverter
    public static List<Post> toPostList(String postListString) {
        if (postListString == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Post>>() {}.getType();
        return gson.fromJson(postListString, type);
    }
}
