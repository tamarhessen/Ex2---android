package com.example.login.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

//import com.example.login.facebookdesign.LocalDB;
import com.example.login.facebookdesign.User;
import com.example.login.facebookdesign.UserDB;
import com.example.login.facebookdesign.UserDao;
import com.example.login.facebookdesign.UsersAPI;

import java.util.LinkedList;
import java.util.List;

public class UsersRepository {
    private UserListData userListData;
    private UsersAPI api;
    private UserDao userDao;

    public UsersRepository() {
//        UserDB userDB = LocalDB.userDB;
//        userDao = userDB.userDao();
//        userListData = new UserListData();
//        api = new UsersAPI(userListData, userDao);
    }

    class UserListData extends MutableLiveData<List<User>> {
        public UserListData() {
            super();
            setValue(new LinkedList<>());
        }

        @Override
        protected void onActive() {
            super.onActive();
            new Thread(() -> {
                userListData.postValue(userDao.index());
            }).start();
            api.get();


        }
    }

    public LiveData<List<User>> getAll(){
        return userListData;
    }

    public void add(String username,Context context){
        api.add(username, context);
    }
    public void refresh() {
        api.get();
    }
}
