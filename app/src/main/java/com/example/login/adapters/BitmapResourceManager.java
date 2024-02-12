package com.example.login.adapters;

import android.graphics.Bitmap;

import java.util.HashMap;

public class BitmapResourceManager {
    private static final HashMap<Integer, Bitmap> bitmapMap = new HashMap<>();
    private static int nextId = 1;

    public static int storeBitmap(Bitmap bitmap) {
        int id = nextId++;
        bitmapMap.put(id, bitmap);
        return id;
    }

    public static Bitmap getBitmap(int id) {
        return bitmapMap.get(id);
    }
}
