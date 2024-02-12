
package com.example.login.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.login.R;
import com.example.login.entities.Post;
import java.util.ArrayList;
import java.util.List;

public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.PostViewHolder> {

    private final LayoutInflater mInflater;
    private List<Post> posts;

    public PostsListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.post_layout, parent, false);
        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        if (posts != null && posts.size() > position) {
            final Post current = posts.get(posts.size() - position - 1); // Adjust position here
            holder.tvAuthor.setText(current.getAuthor());
            holder.tvContent.setText(current.getContent());

            // Set the Bitmap objects to the ImageViews in the ViewHolder
            holder.ivPic.setImageBitmap(current.getPic());
            holder.profilePic.setImageBitmap(current.getProfilepic());

            holder.tvLikes.setText(String.valueOf(current.getLikes()));
            holder.likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toggle the liked status
                    current.setLiked(!current.isLiked());


                    // Update the number of likes
                    int currentLikes = current.getLikes();
                    current.setLikes(current.isLiked() ? currentLikes + 1 : currentLikes - 1);
                    holder.tvLikes.setText(String.valueOf(current.getLikes()));
                }
            });
            List<String> comments = current.getComments();

            if (comments != null) {
                for (String comment : comments) {
                    TextView commentTextView = new TextView(holder.itemView.getContext());
                    commentTextView.setText(comment);
                    holder.commentSection.addView(commentTextView);
                }
            }
            // Bind data to views and set click listeners
            holder.commentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.commentSection.getVisibility() == View.VISIBLE) {
            holder.commentSection.setVisibility(View.GONE);
        } else {
            holder.commentSection.setVisibility(View.VISIBLE);
        }
                }
            });

            holder.addCommentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String newComment = holder.commentEditText.getText().toString().trim();
                    if (!newComment.isEmpty()) {
                        // Get the current post
                        Post currentPost = posts.get(holder.getAdapterPosition());

                        // Add the new comment to the post
                        currentPost.addComment(newComment);

                        // Clear the comment EditText
                        holder.commentEditText.setText("");

                        // Update the UI directly instead of calling notifyDataSetChanged()
                        TextView commentTextView = new TextView(holder.itemView.getContext());
                        commentTextView.setText(newComment);
                        holder.commentSection.addView(commentTextView);
                    }
                }
            });

            holder.editCommentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the current post
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Post currentPost = posts.get(position);

                        // Assuming you have a method in the Post class to edit comments
                        // You can implement this method accordingly
                        int commentIndex = 0;
                        String editedComment = "sdv";
                        currentPost.editComment(commentIndex, editedComment);

                        // Notify the adapter that data has changed
                        notifyItemChanged(position);
                    }
                }
            });

            holder.deleteCommentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the current post
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Post currentPost = posts.get(position);

                        // Assuming you have a method in the Post class to delete comments
                        // You can implement this method accordingly
                        int commentIndex = 0/* specify the index of the comment to be deleted */;
                        currentPost.deleteComment(commentIndex);

                        // Notify the adapter that data has changed
                        notifyItemChanged(position);
                    }
                }
            });






        }
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    public void addPost(Post post) {
        if (posts == null) {
            posts = new ArrayList<>();
        }
        posts.add(post);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return posts != null ? posts.size() : 0;
    }

    public List<Post> getPosts() {
        return posts;
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvAuthor;
        private final TextView tvContent;
        private final ImageView ivPic;
        private final ImageView profilePic;
        private final TextView tvLikes;
        public ImageView likeButton;
        public  ImageView commentButton;
        public LinearLayout commentSection;
        public EditText commentEditText;
        public Button addCommentButton;
        public Button editCommentButton;
        public Button deleteCommentButton;

        private PostViewHolder(View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvContent = itemView.findViewById(R.id.tvContent);
            ivPic = itemView.findViewById(R.id.ivPic);
            profilePic = itemView.findViewById(R.id.profilePic);
            tvLikes = itemView.findViewById(R.id.tvLikes);
            likeButton = itemView.findViewById(R.id.likeButton); // Initialize ImageButton
            commentButton = itemView.findViewById(R.id.commentButton); // Initialize ImageButton
            commentSection = itemView.findViewById(R.id.commentSection);
            commentEditText = itemView.findViewById(R.id.commentEditText);
            addCommentButton = itemView.findViewById(R.id.addCommentButton);
            editCommentButton = itemView.findViewById(R.id.editCommentButton);
            deleteCommentButton = itemView.findViewById(R.id.deleteCommentButton);
        }
    }
}