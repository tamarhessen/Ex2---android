package com.example.login;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.login.facebookdesign.BitmapConverter;
import com.example.login.facebookdesign.Post;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JsonParser {

    public static List<Post> parseJson(Context context) {
        List<Post> posts = new ArrayList<>();

        try {
            // Load JSON data from file
            InputStream inputStream = context.getAssets().open("posts.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer);

            // Parse JSON array
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String author = jsonObject.getString("author");
                String content = jsonObject.getString("content");
                String imageDrawableName = jsonObject.getString("imageDrawableId");
                int imageDrawableId = context.getResources().getIdentifier(imageDrawableName, "drawable", context.getPackageName());
                Bitmap pic = BitmapFactory.decodeResource(context.getResources(), imageDrawableId);
                String picString = BitmapConverter.bitmapToString(pic);
                int likes = jsonObject.getInt("likes");
                String profileDrawableName = jsonObject.getString("profileDrawableId");
                int profileDrawableId = context.getResources().getIdentifier(profileDrawableName, "drawable", context.getPackageName());
                Bitmap profilePic = BitmapFactory.decodeResource(context.getResources(), profileDrawableId);
                String profilePicString = BitmapConverter.bitmapToString(profilePic);
                long timestamp = jsonObject.getLong("timestamp");
                Post post = new Post(author, content, picString, likes, profilePicString,timestamp);
                posts.add(post);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return posts;
    }



    // Method to load Bitmap from drawable resource
    private static Bitmap loadBitmapFromDrawable(Context context, String drawableName) {
        int drawableId = context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());
        return BitmapFactory.decodeResource(context.getResources(), drawableId);
    }
}
