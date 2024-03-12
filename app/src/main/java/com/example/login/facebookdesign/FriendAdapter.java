package com.example.login.facebookdesign;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;

import java.util.ArrayList;
import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    private Context context;
    private List<String> friendDetailsList;

    public FriendAdapter(Context context) {
        this.context = context;
        this.friendDetailsList = new ArrayList<>();
    }


    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_friend, parent, false);
        return new FriendViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        String detail = friendDetailsList.get(position);
        holder.bind(detail);
    }

    @Override
    public int getItemCount() {
        return friendDetailsList.size();
    }

    class FriendViewHolder extends RecyclerView.ViewHolder {
        private Button usernameButton;

        FriendViewHolder(View itemView) {
            super(itemView);
            usernameButton = itemView.findViewById(R.id.friend_name_btn);
        }

        void bind(String detail) {
            usernameButton.setText(detail);
        }
    }
}

