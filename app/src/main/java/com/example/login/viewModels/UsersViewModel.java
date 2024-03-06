package com.example.login.viewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.login.facebookdesign.User;
import com.example.login.facebookdesign.UserCreatePost;
import com.example.login.facebookdesign.UserCredentials;
import com.example.login.facebookdesign.UserDao;
import com.example.login.repositories.UsersRepository;

import java.util.List;

public class UsersViewModel extends ViewModel {
    private UsersRepository repository;
    private LiveData<List<User>> users;
    private String userid;
    private String token;
    public UsersViewModel() {
        repository = new UsersRepository(userid);
        users=repository.getAll();
    }
    public UsersViewModel(UserDao userDao,String userid) {
        repository = new UsersRepository(userDao,userid);
        users = repository.getAll();
    }
    public LiveData<UserCreatePost> getCurrentUser(String userid,String token) {
        return repository.getCurrentUser(userid,token);
    }
    public LiveData<List<User>> getAll() {
        return users;
    }

    public User getAll(int position) {
        return users.getValue().get(position);
    }

    public void refresh() {
        repository.refresh(token);
    }

    public void add(UserCreatePost userCreatePost, Context context) {
        repository.add(userCreatePost, context);
    }

    public void setToken(String token) {
        this.token=token;
    }
    public void setUserid(String userid){
        this.userid=userid;
    }
}
