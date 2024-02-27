package com.example.login;

import android.app.Application;

import androidx.lifecycle.LiveData;

public class UserRepository {

    private UserDao userDao;
    private LiveData<User> userLiveData;

    public UserRepository(Application application) {
        AppDB db = AppDB.getInstance(application);
        userDao = db.userDao();
        userLiveData = userDao.getUserLiveData();
    }

    // Method to get LiveData containing user data
    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }


}
