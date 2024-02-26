package com.example.login;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login.network.WebServiceAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsersActivity extends AppCompatActivity {
    ListView listView;
    UserAdapter adapter;
    //    ImageView settings;
    public static String token;
    public static String currentConnectedUsername;
    ImageView pfpCurrentLoggedIn;
    TextView displayNameCurrentLoggedIn;
    private static UsersViewModel viewModel;
    private FloatingActionButton addButton;
    private ImageView menuButton;
    private UserDB userDB;
    private ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if(result.getResultCode()== Activity.RESULT_OK) {
                    Intent data = result.getData();
                    String username = data.getStringExtra("Username");
                    List<User> userList = viewModel.get().getValue();
                    for(User user: userList) {
                        if(user.getUser().getUsername().equals(username)){
                            Toast.makeText(getApplicationContext(),"User already added",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    viewModel.add(username,getApplicationContext());
//                    Toast.makeText(getApplicationContext(),errorData.getErrorString(),
//                            Toast.LENGTH_SHORT).show();

                }
            }
    );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        userDB = Room.databaseBuilder(getApplicationContext(), UserDB.class, "UserDB").build();
        LocalDB.userDB = userDB;
        displayNameCurrentLoggedIn = findViewById(R.id.textViewCurrentLoggedIn);
        pfpCurrentLoggedIn = findViewById(R.id.imageViewCurrentLoggedIn);
        Intent activityIntent = getIntent();
        if(activityIntent!=null){
            token = activityIntent.getStringExtra("Token");
            currentConnectedUsername = activityIntent.getStringExtra("Username");
            getDisplayNameAndProfilePicture();
        }
//        ArrayList<User> users = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            users.add(new User("The pro", R.drawable.roundskunk,"Hi","10:00"));
//        }
//        users.add(new User("The shmo", R.drawable.boatinthewater2,"YO","10:01"));
        viewModel = new ViewModelProvider(this).get(UsersViewModel.class);
        listView = findViewById(R.id.list_view);
        adapter = new UserAdapter(getApplicationContext(),viewModel.get().getValue());
        listView.setAdapter(adapter);
//        settings = findViewById(R.id.settingsButtton);
        addButton = findViewById(R.id.btnAdd);
        menuButton = findViewById(R.id.menuButton);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),FeedActivity.class);
                User u = viewModel.get(position);
                intent.putExtra("username", u.getUser().getUsername());
                intent.putExtra("displayName", u.getUser().getDisplayName());
                intent.putExtra("profilePic",u.getUser().getProfilePic());
                intent.putExtra("id",u.getId());
                startActivity(intent);
            }

        });





    }

    @Override
    public void onResume() {
        super.onResume();
    }
    private void getDisplayNameAndProfilePicture() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:5000/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        WebServiceAPI webServiceAPI = retrofit.create(WebServiceAPI.class);
        Call<UserCreatePost> call = webServiceAPI.getUser(currentConnectedUsername,
                "Bearer "+token);
        call.enqueue(new Callback<UserCreatePost>() {
            @Override
            public void onResponse(Call<UserCreatePost> call, Response<UserCreatePost> response) {
                if(response.isSuccessful()) {
                    UserCreatePost user = response.body();
                    setAsImage(user.getProfilePic(), pfpCurrentLoggedIn);
                    displayNameCurrentLoggedIn.setText(user.getDisplayName());
                }
            }

            @Override
            public void onFailure(Call<UserCreatePost> call, Throwable t) {

            }
        });
    }

    public static void setAsImage(String strBase64, ImageView imageView) {

            imageView.setImageResource(R.drawable.profilePic);

    }
    public static void update(){
        viewModel.refresh();
    }
}
