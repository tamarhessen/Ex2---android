package com.example.login.facebookdesign;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.login.repositories.UsersRepository;

import java.util.List;

public class UsersViewModel extends ViewModel {
    private UsersRepository repository;
    private LiveData<List<User>> users;

    public UsersViewModel(){
        repository = new UsersRepository();
        users=repository.getAll();
    }

    public LiveData<List<User>> get() {return users;}

    public User get(int position) {
        return users.getValue().get(position);
    }
    public void refresh() {repository.refresh();}
    public void add(String username, Context context) {
        repository.add(username,context);
    }
}
