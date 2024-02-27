package com.example.login.model;
import android.app.Application;
import com.example.login.facebookdesign.UsersRepository;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.login.User;


public class UserViewModel extends AndroidViewModel {

    private UsersRepository userRepository;

    // LiveData to hold the user data
    private LiveData<User> userLiveData;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UsersRepository(application);
        userLiveData = userRepository.getUserLiveData();
    }

    // Method to get the LiveData containing user data
    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }
}
