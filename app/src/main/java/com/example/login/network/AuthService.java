package com.example.login.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AuthService {

    @FormUrlEncoded
    @POST("login") // Adjust the endpoint URL accordingly
    Call<Void> login(@Field("username") String username, @Field("password") String password);
}
