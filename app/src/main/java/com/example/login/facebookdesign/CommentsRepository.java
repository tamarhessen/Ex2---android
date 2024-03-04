package com.example.login.facebookdesign;

import static com.example.login.facebookdesign.UsersActivity.currentConnectedUsername;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import com.example.login.dataBase.LocalDB;
import com.example.login.dataBase.UserDB;

public class CommentsRepository {
    private CommentListData commentListData;
    private CommentAPI api;
    private int id = -1;
    private CommentPostDao commentPostDao;


    public CommentsRepository(int id){
        UserDB userDB = LocalDB.userDB;
        commentPostDao = userDB.commentPostDao();

        commentListData = new CommentListData();
        api = new CommentAPI(commentListData, commentPostDao);
        this.id = id;
    }

    public void add(CommentToSend comment) {
//        api.add(id,message);
        // Get the current time
        Calendar currentTime = Calendar.getInstance();

        // Extract the hour and minute values
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        // Format the hour and minute values as strings
        String formattedHour = String.format(Locale.getDefault(), "%02d", hour);
        String formattedMinute = String.format(Locale.getDefault(), "%02d", minute);

        // Construct the formatted time string
        String formattedTime = formattedHour + ":" + formattedMinute;
        Comment realComment= new Comment(comment.getComment(),
                formattedTime,new OnlyUsername(currentConnectedUsername));
        List<Comment> commentList1 = commentListData.getValue();
        commentList1.add(realComment);
        commentListData.setValue(commentList1);
        api.add(id,comment, commentList1);


//        // Add the new message to the local database
//        MessageChat messageChat = messageChatDao.get(id);
//        if (messageChat != null) {
//            messageChat.getListMessage().add(realMessage);
//            messageChatDao.update(messageChat);
//        }
    }

    class CommentListData extends MutableLiveData<List<Comment>> {
        public CommentListData(){
            super();
            setValue(new LinkedList<>());
        }

        @Override
        protected void onActive() {
            super.onActive();

            new Thread(() -> {
//                messageListData.postValue(messageChatDao.index());
                //List<Message> messages = messageChatDao.index();
                CommentPost commentPost = commentPostDao.get(id);
                if(commentPost!=null) {
                    commentListData.postValue(commentPost.getListComment());
                }
            }).start();

            api.get(id);
        }
    }
    public void refresh() {
        Log.d("abcde_refresh", String.valueOf(id));
        api.get(id);
    }
    public LiveData<List<Comment>> getAll() {return commentListData;}
}
