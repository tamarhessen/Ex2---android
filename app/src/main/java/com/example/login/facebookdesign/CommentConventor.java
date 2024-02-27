package com.example.login.facebookdesign;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class CommentConventor {
    @TypeConverter
    public String fromCommentList(List<Comment> commentList) {
        if (commentList == null) {
            return null;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<Comment>>() {}.getType();
        return gson.toJson(commentList, type);
    }

    @TypeConverter
    public List<Comment> toCommentList(String commentListString) {
        if (commentListString == null) {
            return null;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<Comment>>() {}.getType();
        return gson.fromJson(commentListString, type);
    }
}

