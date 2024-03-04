package com.example.login.dataBase;

import android.content.Context; // Import Context class

import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.login.facebookdesign.CommentPostDB;

public class LocalDB {
    public static UserDB userDB;
    public static CommentPostDB commentPostDB;
    private static PostDB postDB;

    // Method to get the PostDao
    public static synchronized PostDB getInstance(Context context) {
        if (postDB == null) {
            postDB = Room.databaseBuilder(context.getApplicationContext(),
                            PostDB.class, "post_database").allowMainThreadQueries().fallbackToDestructiveMigration()
                    .build();
        }
        return postDB;
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Define the necessary SQL statements to migrate the schema
            // For example, you might add a new column to an existing table
            // or create a new table if needed
            database.execSQL("ALTER TABLE post_table ADD COLUMN new_column TEXT");
        }
    };
}


        // Method to get the instance of PostDB