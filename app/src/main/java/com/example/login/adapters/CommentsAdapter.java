package com.example.login.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.example.login.entities.Post;

import java.util.ArrayList;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

    private List<String> comments;

    // Constructor
    public CommentsAdapter() {
        this.comments = new ArrayList<>();
    }

    // Method to set the list of comments
    public void setComments(List<String> comments) {
        this.comments = comments;
        notifyDataSetChanged(); // Notify RecyclerView that data has changed
    }

    // ViewHolder class
    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView textViewComment;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewComment = itemView.findViewById(R.id.textViewComment); // Adjust to your layout's TextView ID
        }
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_comment, parent, false);
        return new CommentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        String comment = comments.get(position);
        holder.textViewComment.setText(comment);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
}
