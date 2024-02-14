package com.example.login.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;

import java.util.ArrayList;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

    private List<String> comments;
    private String username;
    private Bitmap profilePictureBitmap;
    private Context context; // Added for AlertDialog

    // Constructor
    public CommentsAdapter(Context context, Bitmap profilePictureBitmap, String username) {
        this.context = context;
        this.profilePictureBitmap = profilePictureBitmap;
        this.username = username+":";
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
        Button deleteButton;
        Button editButton;
        TextView userNameTextView;
        ImageView profileImageView;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewComment = itemView.findViewById(R.id.commentTextView);
            userNameTextView = itemView.findViewById(R.id.displayNameTextView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            editButton = itemView.findViewById(R.id.editButton);
        }
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_comment, parent, false);
        return new CommentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, final int position) {
        final String comment = comments.get(position);
        holder.textViewComment.setText(comment);
        holder.userNameTextView.setText(username);
        holder.profileImageView.setImageBitmap(profilePictureBitmap);

        // Set click listener for the delete button
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remove the comment from the list
                comments.remove(position);
                // Notify adapter that item is removed
                notifyItemRemoved(position);
            }
        });

        // Set click listener for the edit button
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    // Method to show an edit dialog
    private void showEditDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Comment");

        // Set up the input
        final EditText input = new EditText(context);
        input.setText(comments.get(position));
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String editedComment = input.getText().toString();
                comments.set(position, editedComment);
                notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
