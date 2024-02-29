package com.example.login.facebookdesign;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class UserDataFromAdd {
    private int id;
    private User.UserNoPassword user;

    public UserDataFromAdd(int id, User.UserNoPassword user) {
        this.id = id;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User.UserNoPassword getUser() {
        return user;
    }

    public void setUser(User.UserNoPassword user) {
        this.user = user;
    }

    public String getDisplayName() {
        return this.user.getDisplayName();
    }

    public String getProfilePic() {
        return bitmapToString(this.user.getProfilePic());
    }
    public static String bitmapToString(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] byteArray = outputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}
