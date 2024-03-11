package com.example.login.viewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
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

    private MutableLiveData<Boolean> loginResult = new MutableLiveData<>();
    private String token;

    public UsersViewModel() {
        repository = new UsersRepository(userid);
        users = repository.getAll();
    }

    public UsersViewModel(UserDao userDao, String userid) {
        repository = new UsersRepository(userDao, userid);
        users = repository.getAll();
    }

    public LiveData<UserCreatePost> getCurrentUser(String userid, String token) {
        return repository.getCurrentUser(userid, token);
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

    public void editUser(String displayName, String base64EncodedImage) {
        repository.editUser(userid, displayName, base64EncodedImage, token);
    }
    public void deleteuser() {
        repository.deleteUser(userid, token);
    }
    public void askFriend() {
        repository.askFriend(userid, token);
    }
    public void getFriends() {
        repository.getFriends(userid, token);
    }
    public void acceptFriend(String userid,String friendid) {
        repository.acceptFriend(userid, friendid, token);
    }
    public void deleteFriend(String userid,String friendid) {
        repository.deleteFriend(userid, friendid, token);
    }

    public String getUserid() {
        return this.userid;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }


    public void login(String username, String password) {
        this.repository=new UsersRepository(username);
        this.repository.login(username, password, new UsersRepository.LoginCallback() {
            @Override
            public void onLoginSuccess(String token) {
                // Handle successful login, maybe store token or navigate to another screen
                loginResult.postValue(true);
            }

            @Override
            public void onLoginError(String errorMessage) {
                // Handle login error, maybe show an error message to the user
                loginResult.postValue(false);
            }
        });
    }

    private void saveToken(String token) {
        this.token = token;
    }

    // Method to observe the login result
    public LiveData<Boolean> getLoginResult() {
        return loginResult;
    }
}
