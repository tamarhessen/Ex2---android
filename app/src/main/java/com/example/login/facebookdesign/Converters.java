package com.example.login.facebookdesign;
import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class Converters {
    @TypeConverter
    public static List<User> fromString(String value) {
        Type listType = new TypeToken<List<User>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(List<User> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}