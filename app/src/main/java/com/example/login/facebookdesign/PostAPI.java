//package com.example.login.facebookdesign;
//
//import static com.example.login.facebookdesign.MainActivity.baseURL;
//import static com.example.login.facebookdesign.UsersActivity.token;
//
//import androidx.lifecycle.MutableLiveData;
//
//import com.example.login.R;
//import com.example.login.network.WebServiceAPI;
//
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//public class PostAPI {
// private MutableLiveData<List<Post>> postListData;
// private PostDao dao;
// Retrofit retrofit;
// WebServiceAPI webServiceAPI;
//
//    public PostAPI(MutableLiveData<List<Post>> postListData, PostDao postDao) {
//        this.postListData = postListData;
//        retrofit = new Retrofit.Builder().baseUrl(baseURL).
//                addConverterFactory(GsonConverterFactory.create()).build();
//        webServiceAPI = retrofit.create(WebServiceAPI.class);
//        this.dao= postDao;
//    }
//
//         public void get() {
//         Call<List<Post>> call = webServiceAPI.getPosts("Bearer "+ token);
//         call.enqueue(new Callback<List<Post>>() {
// @Override
// public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
//
//                 new Thread(() -> {
//                     dao.clear();
//                     dao.insertList(response.body());
//                     postListData.postValue(dao.get());
//                     }).start();
//                 }
//
//         @Override
// public void onFailure(Call<List<Post>> call, Throwable t) {}
// });
//         }
//}