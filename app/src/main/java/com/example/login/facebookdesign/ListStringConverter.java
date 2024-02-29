package com.example.login.facebookdesign;

import androidx.room.TypeConverter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListStringConverter {

    @TypeConverter
    public static List<String> fromString(String value) {
        // Convert the String representation back to a List of Strings
        return value == null ? new ArrayList<>() : Arrays.asList(value.split(","));
    }

    @TypeConverter
    public static String fromList(List<String> list) {
        // Convert the List of Strings to a String representation
        StringBuilder sb = new StringBuilder();
        for (String item : list) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(item);
        }
        return sb.toString();
    }
}
