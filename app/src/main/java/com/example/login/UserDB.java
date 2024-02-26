package com.example.login;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {User.class,MessageChat.class}, version = 1)
public abstract class UserDB extends RoomDatabase {
    public abstract UserDao userDao();

    public abstract MessageChatDao messageChatDao();
}

