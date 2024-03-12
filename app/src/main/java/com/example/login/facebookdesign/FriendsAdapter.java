package com.example.login.facebookdesign;
//import static com.example.login.facebookdesign.PostAdapter.formatDateToString;
//import static com.example.login.facebookdesign.PostAdapter.parseDateString;
import static com.example.login.facebookdesign.UsersActivity.setAsImage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.login.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FriendsAdapter extends ArrayAdapter<User> {
    LayoutInflater inflater;
    List<User> userList;

    public FriendsAdapter(Context ctx, List<User> userArrayList) {
        super(ctx, R.layout.custom_list_item, userArrayList);
        this.userList = userArrayList;
        this.inflater = LayoutInflater.from(ctx);
    }

}