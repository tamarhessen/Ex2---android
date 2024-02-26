package com.example.login.facebookdesign;

import static com.example.login.facebookdesign.MainActivity.SIM;
import static com.example.login.facebookdesign.MainActivity.baseURL;
import static com.example.login.facebookdesign.UsersActivity.token;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.login.network.WebServiceAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsersAPI {
    private MutableLiveData<List<User>> userListData;
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;
    private UserDao userDao;

    public UsersAPI(MutableLiveData<List<User>> userListData, UserDao userDao) {
        this.userListData = userListData;
        retrofit = new Retrofit.Builder().baseUrl(baseURL).
                addConverterFactory(GsonConverterFactory.create()).build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
        this.userDao = userDao;
    }

    public void get(){
        Call<List<User>> call = webServiceAPI.getUsers("Bearer "+ token);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                new Thread(() -> {
                    userDao.deleteAll();
//                     userDao.insert(response.body());
                    List<User> userList = response.body();
                    User[] userArray = userList.toArray(new User[0]);
                    userDao.insert(userArray);
                    userListData.postValue(userList);
                }).start();

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
    }

    public void add(String username,  Context context) {
        OnlyUsername onlyUsername = new OnlyUsername(username);
        Call<UserDataFromAdd> call = webServiceAPI.addPost(onlyUsername,"Bearer "+ token);

        call.enqueue(new Callback<UserDataFromAdd>() {
            @Override
            public void onResponse(Call<UserDataFromAdd> call, Response<UserDataFromAdd> response) {
                if(response.isSuccessful()){
                    List<User> users = userListData.getValue();
                    User user = new User(response.body());
                    users.add(user);
                    new Thread(() -> {
                        userDao.insert(user);
                        userListData.postValue(users);
                    }).start();

                    Toast.makeText(context,"Added "+username,
                            Toast.LENGTH_SHORT).show();
                    Log.d("creating_chat_abcdef", "creating");
                    if(SIM != null) {
                        Log.d("creatingnon_null_abcdef", "creating");
                    }
                    SIM.createChat(0,username);
                    Log.d("created_abcdef", "created");
                } else {
                    //errorData.setErrorString("Invalid username");
                    Toast.makeText(context,"Invalid username",
                            Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<UserDataFromAdd> call, Throwable t) {
                //errorData.setErrorString("Request to server failed");
                Toast.makeText(context,"Request to server failed",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
