package com.example.login.facebookdesign;

import static com.example.login.facebookdesign.MainActivity.baseURL;
import static com.example.login.facebookdesign.MainActivity.defaultPfp;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login.R;
import com.example.login.network.WebServiceAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

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
//        displayNameCurrentLoggedIn = findViewById(R.id.textViewCurrentLoggedIn);
//        pfpCurrentLoggedIn = findViewById(R.id.imageViewCurrentLoggedIn);
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
//        listView = findViewById(R.id.list_view);
        adapter = new UserAdapter(getApplicationContext(),viewModel.get().getValue());
        listView.setAdapter(adapter);
//        settings = findViewById(R.id.settingsButtton);
        addButton = findViewById(R.id.btnAdd);
//        menuButton = findViewById(R.id.menuButton);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),PostActivity.class);
                User u = viewModel.get(position);
                intent.putExtra("username", u.getUser().getUsername());
                intent.putExtra("displayName", u.getUser().getDisplayName());
                intent.putExtra("profilePic",u.getUser().getProfilePic());
                intent.putExtra("id",u.getId());
                startActivity(intent);
            }

        });
//        settings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
//                startActivity(intent);
//            }
//        });
        addButton.setOnClickListener(v -> {

            Intent intent = new Intent(getApplicationContext(), AddUserActivity.class);
            launcher.launch(intent);
            viewModel.refresh();
        });
        viewModel.get().observe(this, users -> {
            adapter = new UserAdapter(getApplicationContext(),users);
            listView.setAdapter(adapter);
        });

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(), v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_main, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemId = item.getItemId();

                        if (itemId == R.id.settingsItem) {
                            // Handle settings button click
                            Intent intent = new Intent(getApplicationContext(),
                                    SettingsActivity.class);
                            startActivity(intent);
                            // TODO: Add your code here to handle the settings action
                            return true;
                        } else if (itemId == R.id.logoutItem) {
                            // Handle logout button click
                            // Clear the saved user information during logout
                            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.remove("username"); // Remove the saved username
                            editor.remove("token"); // Remove the saved token
                            editor.apply();
                            new Thread(()->{
                                LocalDB.userDB.clearAllTables();
                            }).start();
                            Intent intent = new Intent(getApplicationContext(),
                                    LogInActivity.class);
                            startActivity(intent);
                            // TODO: Add your code here to handle the logout action
                            return true;
                        } else {
                            return false;
                        }
                    }
                });

                popupMenu.show();
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
    }
    private void getDisplayNameAndProfilePicture() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        WebServiceAPI webServiceAPI = retrofit.create(WebServiceAPI.class);
        Call<UserCreatePost> call = webServiceAPI.getUser(currentConnectedUsername,
                "Bearer "+token);
        call.enqueue(new Callback<UserCreatePost>() {
            @Override
            public void onResponse(Call<UserCreatePost> call, Response<UserCreatePost> response) {
                if(response.isSuccessful()) {
                    UserCreatePost user = response.body();
//                    setAsImage(user.getProfilePic(), pfpCurrentLoggedIn);
                    displayNameCurrentLoggedIn.setText(user.getDisplayName());
                }
            }

            @Override
            public void onFailure(Call<UserCreatePost> call, Throwable t) {

            }
        });
    }

    public static void setAsImage(String strBase64, ImageView imageView) {
        if(strBase64.equals(defaultPfp)){
//            imageView.setImageResource(R.drawable.defaultprofilepic);
        } else {
            byte[] decodedString = Base64.decode(strBase64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageView.setImageBitmap(decodedByte);
        }
    }
    public static void update(){
        viewModel.refresh();
    }
    public static String getToken() {
        return token;
    }
}
