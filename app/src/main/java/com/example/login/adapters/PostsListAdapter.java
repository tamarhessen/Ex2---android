//package com.example.login.adapters;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import com.example.login.R;
//import com.example.login.entities.Post;
//
//import java.text.BreakIterator;
//import java.util.ArrayList;
//import java.util.List;
//
//public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.PostViewHolder> {
//
//    private final LayoutInflater mInflater;
//    private List<Post> posts;
//
//    public PostsListAdapter(Context context) {
//        mInflater = LayoutInflater.from(context);
//    }
//
//    @NonNull
//    @Override
//    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View itemView = mInflater.inflate(R.layout.post_layout, parent, false);
//        return new PostViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
//        if (posts != null && posts.size() > position) {
//            final Post current = posts.get(posts.size() - position - 1); // Adjust position here
//            holder.tvAuthor.setText(current.getAuthor());
//            holder.tvContent.setText(current.getContent());
//
//            // Set the Bitmap objects to the ImageViews in the ViewHolder
//            holder.ivPic.setImageBitmap(current.getPic());
//            holder.profilePic.setImageBitmap(current.getProfilepic());
//
//            holder.tvLikes.setText(String.valueOf(current.getLikes()));
//            holder.likeButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // Toggle the liked status
//                    current.setLiked(!current.isLiked());
//
//                    // Update the number of likes
//                    int currentLikes = current.getLikes();
//                    current.setLikes(current.isLiked() ? currentLikes + 1 : currentLikes - 1);
//                    holder.tvLikes.setText(String.valueOf(current.getLikes()));
//                }
//            });
//
//            // Handle comment button click
//            holder.commentButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    toggleCommentBox(holder.commentBox);
//                }
//            });
//
//            // Handle adding comment
//            holder.addCommentButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String comment = holder.commentEditText.getText().toString();
//                    // Handle adding comment logic here
//                    // For example, you can add the comment to the current post object
//                    // Then update the UI accordingly
//                    // After adding comment, hide the comment box
//                    toggleCommentBox(holder.commentBox);
//                }
//            });
//        }
//    }
//
//    // Method to toggle visibility of comment box
//    private void toggleCommentBox(View commentBox) {
//        if (commentBox.getVisibility() == View.VISIBLE) {
//            commentBox.setVisibility(View.GONE);
//        } else {
//            commentBox.setVisibility(View.VISIBLE);
//        }
//    }
//    public void setPosts(List<Post> posts) {
//        this.posts = posts;
//        notifyDataSetChanged();
//    }
//
//    public void addPost(Post post) {
//        if (posts == null) {
//            posts = new ArrayList<>();
//        }
//        posts.add(post);
//        notifyDataSetChanged();
//    }
//
//    @Override
//    public int getItemCount() {
//        return posts != null ? posts.size() : 0;
//    }
//
//    public List<Post> getPosts() {
//        return posts;
//    }
//
//    static class PostViewHolder extends RecyclerView.ViewHolder {
//        private final TextView tvAuthor;
//        private final TextView tvContent;
//        private final ImageView ivPic;
//        private final ImageView profilePic;
//        private final TextView tvLikes;
//        private final ImageView likeButton;
//        public View commentButton;
//        public View commentBox;
//        public TextView commentEditText;
//        public View addCommentButton;
//
//        private PostViewHolder(View itemView) {
//            super(itemView);
//            tvAuthor = itemView.findViewById(R.id.tvAuthor);
//            tvContent = itemView.findViewById(R.id.tvContent);
//            ivPic = itemView.findViewById(R.id.ivPic);
//            profilePic = itemView.findViewById(R.id.profilePic);
//            tvLikes = itemView.findViewById(R.id.tvLikes);
//            likeButton = itemView.findViewById(R.id.likeButton);
//            commentButton=itemView.findViewById(R.id.commentButton);
//            commentBox=itemView.findViewById(R.id.commentBox);
//            commentEditText=itemView.findViewById(R.id.commentEditText);
//
//
//        }
//    }
//}
package com.example.login.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        private final ImageView likeButton;

        private PostViewHolder(View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvContent = itemView.findViewById(R.id.tvContent);
            ivPic = itemView.findViewById(R.id.ivPic);
            profilePic = itemView.findViewById(R.id.profilePic);
            tvLikes = itemView.findViewById(R.id.tvLikes);
            likeButton = itemView.findViewById(R.id.likeButton);
        }
    }
}