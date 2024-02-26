package com.example.login.facebookdesign;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.net.URISyntaxException;

public class SocketIOManager {
    private Socket socket;
    private int x = 0;
    private String id;
    public int activeChat;
    public SocketIOManager() {
        try {
            // Connect to the server
            socket = IO.socket("http://10.0.2.2:9000");
            socket.connect();
            socket.on("connected", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d("abcde", "connected");

                    JSONObject data = (JSONObject)args[0];
                    try {
                        id = data.getString("socket");
                        Log.d("abcde", "got socket");

                    } catch (JSONException e) {
                        Log.d("abcde", "socket exception");

                        throw new RuntimeException(e);
                    }
//here the data is in JSON Format
                    //Toast.makeText(MainActivity.this, data.toString(), Toast.LENGTH_SHORT).show();
                }
            });
//            // Listen for events
            socket.on("receive_message", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    Log.d("abcde", "receuive_message");

                    JSONObject message = (JSONObject) args[0];
                    // refresh
                    upd(message);
                    // Handle the received message
                }
            });

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    private void upd(JSONObject message){

        //pull chats from server.
        Log.d("abcde", String.valueOf(x));
        x++;
        try {
            if (message.getString("post_id").equals("0")){
                Log.d("new_post_abcde", String.valueOf(x));
                new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    UsersActivity.update();
                }).start();
                // new chat update screen.
            } else {
                Log.d("new_message_abcde", String.valueOf(x));
                new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    Log.d("abcde", "going to update");

                    PostActivity.update();
                    Log.d("abcde", "PostActivity updated");

                    UsersActivity.update();
                    Log.d("abcde", "PostActivity updated");

                }).start();

                // new message update chats.
            }
        } catch (JSONException e) {
            Log.d("not_updated_abcde", String.valueOf(x));
            throw new RuntimeException(e);
        }
    }
    public void sendToken(String token, String username){
        JSONObject messageObject = new JSONObject();
        try {
            messageObject.put("token", token);
            messageObject.put("username", username);
            socket.emit("using_phone", messageObject);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    public void logIn(String username){
        JSONObject messageObject = new JSONObject();
        try {
            Log.d("zxcv_create", "Loging_in");
            Log.d("zxcv_my_socket", id);
            Log.d("zxcv_username", username);
            messageObject.put("socket", id);
            messageObject.put("username", username);
            socket.emit("logged_in", messageObject);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    public void createChat(int activeId,String username){
        JSONObject messageObject = new JSONObject();
        try {
            Log.d("zxcv_create", "creating_post");
            Log.d("zxcv_socket", id);
            messageObject.put("post_id", activeId);
            messageObject.put("socket", id);
            messageObject.put("username", username);
            messageObject.put("comment", "new post");
            socket.emit("send_message", messageObject);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendMessage(int activeId, String username, String comment){
        JSONObject messageObject = new JSONObject();
        try {
            messageObject.put("post_id", activeId);
            messageObject.put("socket", id);
            messageObject.put("username", username);
            messageObject.put("comment", comment);
            socket.emit("send_message", messageObject);
            Log.d("debug_rtyui", "sent message");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void disconnect() {
        Log.d("debug_rtyui", "disconnect()");

        if (socket != null) {
            socket.disconnect();
        }
    }
    public void connect(String username) {
        socket.disconnect();
        socket.connect();
        logIn(username);
    }
}
