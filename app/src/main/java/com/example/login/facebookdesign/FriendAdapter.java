package com.example.login.facebookdesign;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;

import com.example.login.R;

import java.util.ArrayList;
import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    private Context context;
    private List<String> friendDetailsList;
    String token;
    String username;

    public FriendAdapter(Context context) {
        this.context = context;
        this.friendDetailsList = new ArrayList<>();
    }
    public void setFriends(List<String> friends) {
        this.friendDetailsList.clear();
        this.friendDetailsList.addAll(friends);
        notifyDataSetChanged();
    }
    public void setFriends(List<String> friends,String token,String username) {
        this.friendDetailsList.clear();
        this.friendDetailsList.addAll(friends);
        this.token=token;
        this.username=username;
        notifyDataSetChanged();

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
            usernameButton.setOnClickListener(v -> {
                // Get the username of the friend
                String selectedUsername = friendDetailsList.get(getAdapterPosition());

                // Navigate to the profile page of the selected user
                Intent intent = new Intent(context, ProfileActivity.class);

                intent.putExtra("Username",selectedUsername);
                intent.putExtra("myUsername",username);
                intent.putExtra("Token",token);

                context.startActivity(intent);
            });
        }

        void bind(String detail) {
            usernameButton.setText(detail);
        }
    }
}
