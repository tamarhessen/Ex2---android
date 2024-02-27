package com.example.login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class UserViewModel extends AndroidViewModel {

    private UserRepository userRepository;

    // LiveData to hold the user data
    private LiveData<User> userLiveData;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        userLiveData = userRepository.getUserLiveData();
    }

    // Method to get the LiveData containing user data
    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }


}
