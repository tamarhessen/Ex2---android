package com.example.login.facebookdesign;//package com.example.whatsappdesign;

import static com.example.login.facebookdesign.MainActivity.SIM;
import static com.example.login.facebookdesign.MainActivity.baseURL;
import static com.example.login.facebookdesign.UsersActivity.currentConnectedUsername;
import static com.example.login.facebookdesign.UsersActivity.token;

import static java.util.Collections.reverse;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.login.facebookdesign.CommentPostDao;
import com.example.login.facebookdesign.CommentPost;
import com.example.login.network.WebServiceAPI;

public class CommentAPI {
    private MutableLiveData<List<Comment>> commentListData;
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;

    private CommentPostDao commentPostDao;

    public CommentAPI(MutableLiveData<List<Comment>> commentListData, CommentPostDao commentPostDao) {
        this.commentListData = commentListData;
        retrofit = new Retrofit.Builder().baseUrl(baseURL).
                addConverterFactory(GsonConverterFactory.create()).build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
        this.commentPostDao = commentPostDao;
    }

    public void get(int id) {
        Call<List<Comment>> call = webServiceAPI.getComments(id,"Bearer "+ token);
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                new Thread(()->{
                    List<Comment> comments = response.body();
                    reverse(comments);
                    // Retrieve the existing MessageChat object with the given chatId
                    CommentPost commentPost = commentPostDao.get(id);

                    if (commentPost == null) {
                        // Create a new MessageChat object if it doesn't exist
                        commentPost = new CommentPost(id);
                        commentPost.setListComment(comments);
                        Log.d("API", String.valueOf(id));
                        commentPostDao.insert(commentPost);
                    } else {
                        // Update the list of messages
                        commentPost.setListComment(comments);

                        // Insert MessageChat object in the database
                        commentPostDao.update(commentPost);
                    }
                   commentListData.postValue(comments);
                }).start();

            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {

            }
        });
    }

    public void add(int id,CommentToSend comment, List<Comment> commentList1) {
        Call<Void> call = webServiceAPI.postComment(id,comment,"Bearer "+token);
        SIM.sendMessage(id, PostActivity.talkingUser, comment.getComment());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                new Thread(()->{
//                    // Get the current time
//                    Calendar currentTime = Calendar.getInstance();
//
//                    // Extract the hour and minute values
//                    int hour = currentTime.get(Calendar.HOUR_OF_DAY);
//                    int minute = currentTime.get(Calendar.MINUTE);
//
//                    // Format the hour and minute values as strings
//                    String formattedHour = String.format(Locale.getDefault(), "%02d", hour);
//                    String formattedMinute = String.format(Locale.getDefault(), "%02d", minute);
//
//                    // Construct the formatted time string
//                    String formattedTime = formattedHour + ":" + formattedMinute;
//                    Message realMessage = new Message(message.getMsg(),
//                            formattedTime,new OnlyUsername(currentConnectedUsername));
//                    List<Message> messageList1 = messageListData.getValue();
//                    messageList1.add(realMessage);
                    CommentPost commentPost = commentPostDao.get(id);
                    commentPost.setListComment(commentList1);
                    commentPostDao.update(commentPost);
//                    messageListData.postValue(messageList1);
                }).start();

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
}

