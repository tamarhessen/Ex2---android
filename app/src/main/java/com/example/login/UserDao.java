package com.example.login;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user LIMIT 1")
    LiveData<User> getUserLiveData();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);
}
