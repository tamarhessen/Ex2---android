package com.example.login.facebookdesign;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.example.login.viewModels.PostsViewModel;
import com.example.login.viewModels.UsersViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder>
        implements EditPostDialogFragment.OnPostEditedListener {

    private final LayoutInflater mInflater;
    private static List<Post> posts;
    private String currentUserUsername;
    private String currentDisplayName;
    private final Context mContext;
    private PostsViewModel postsViewModel;
    private String CurrentDisplayName;

    public PostAdapter(Context context, String currentUserUsername,PostsViewModel postsViewModel,String currentDisplayName) {
        mInflater = LayoutInflater.from(context);
        this.currentUserUsername = currentUserUsername; // Initialize the current user's username
        mContext = context; // Initialize the context
        this.postsViewModel=postsViewModel;
        this.currentDisplayName = currentDisplayName;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.post_layout, parent, false);
        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostViewHolder holder, int position) {
        if (posts != null && !posts.isEmpty()) {
            final Post current = posts.get(posts.size() - position - 1);  // Reverse the position
            holder.tvAuthor.setText(current.getCreator());
            holder.tvContent.setText(current.getPostText());

            // Set the Bitmap objects to the ImageViews in the ViewHolder
            holder.ivPic.setImageBitmap(BitmapConverter.stringToBitmap(current.getPostImg
                    ()));
            holder.profilePic.setImageBitmap(BitmapConverter.stringToBitmap(current.getCreatorImg()));

            if (current.getPostImg
                    () != null) {
                holder.ivPic.setVisibility(View.VISIBLE);
                holder.cardView.setVisibility(View.VISIBLE);
            } else {
                holder.ivPic.setVisibility(View.GONE);
                holder.cardView.setVisibility(View.GONE);
            }

            holder.tvLikes.setText(String.valueOf(current.getLikes()));
            holder.shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Start MenuActivity
                    Intent intent = new Intent(mContext, MenuActivity.class);
                    mContext.startActivity(intent);
                }
            });
            holder.likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toggle the liked status
                    current.setLiked(!current.isLiked());

                    // Update the number of likes
                    int currentLikes = current.getLikes();
                    current.setLikes(current.isLiked() ? currentLikes + 1 : currentLikes - 1);
                    holder.tvLikes.setText(String.valueOf(current.getLikes()));
                    if (current.isLiked()) {
                        holder.likeButton.setImageResource(R.drawable.liked);
                    } else {
                        holder.likeButton.setImageResource(R.drawable.not_liked);
                    }
                }
            });
            // Handle click event of the comments button
            // Uncommented code to start CommentsActivity
            holder.commentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Start MenuActivity
                    Intent intent = new Intent(mContext, CommentsActivity.class);
                    mContext.startActivity(intent);
                }
            });

            // Check if the current post matches your username
            if(current.getCreator() != null && current.getCreator().equals(currentDisplayName)) {
                // Show edit and delete buttons
                holder.editButton.setVisibility(View.VISIBLE);
                holder.deleteButton.setVisibility(View.VISIBLE);

                // Set click listeners for edit and delete buttons
                holder.editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Create and show the EditPostDialogFragment
                        EditPostDialogFragment dialogFragment = new EditPostDialogFragment(current, PostAdapter.this,postsViewModel);
                        dialogFragment.show(((AppCompatActivity) mContext).getSupportFragmentManager(), "EditPostDialogFragment");
                    }
                });
                holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int adapterPosition = holder.getAdapterPosition();
                        if (adapterPosition != RecyclerView.NO_POSITION) {
                            // Get the correct post position in the original list
                            int postPosition = posts.size() - adapterPosition - 1;
                            if (postPosition >= 0 && postPosition < posts.size()) {
                                // Remove the post from the list
                                final Post postToDelete = posts.get(postPosition);

                                // Call the delete method in PostsViewModel to delete the post from the server
                                postsViewModel.deletePost(postToDelete); // Assuming getId() returns the ID of the post

                                // Remove the post from the list
                                posts.remove(postPosition);
                                // Notify the adapter that the item is removed
                                notifyItemRemoved(adapterPosition);
                                notifyItemRangeChanged(adapterPosition, getItemCount());
                            }
                        }
                    }
                });

            } else {
                // Hide edit and delete buttons
                holder.editButton.setVisibility(View.GONE);
                holder.deleteButton.setVisibility(View.GONE);
            }

            if (current.getTimestamp() == 0L) {
                holder.time.setText(getCurrentTime()); // Assuming getCurrentTime() returns the current time as a String
            } else {
                String timestampString = Post.convertTimestampToString(current.getTimestamp());
                holder.time.setText(timestampString);
            }

        }
    }

    private String getCurrentTime() {
        long currentTimeMillis = System.currentTimeMillis();
        Date currentTime = new Date(currentTimeMillis);
        // Format the current time using SimpleDateFormat
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return sdf.format(currentTime);
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

    @Override
    public void onPostEdited(Post post) {
        int editedPostIndex = posts.indexOf(post);
        if (editedPostIndex != -1) {
            posts.set(editedPostIndex, post);
            notifyItemChanged(posts.size() - editedPostIndex - 1);
        }
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvAuthor;
        private final TextView tvContent;
        private final ImageView ivPic;
        private final ImageView profilePic;
        private final TextView tvLikes;
        private final ImageView likeButton;
        private final CardView cardView;
        private final Button editButton;
        private final Button deleteButton;
        private final TextView time;
        private final ImageView shareButton;
        private final ImageView commentButton;

        private PostViewHolder(View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvContent = itemView.findViewById(R.id.tvContent);
            ivPic = itemView.findViewById(R.id.ivPic);
            profilePic = itemView.findViewById(R.id.profilePic);
            tvLikes = itemView.findViewById(R.id.tvLikes);
            likeButton = itemView.findViewById(R.id.likeButton);
            commentButton = itemView.findViewById(R.id.commentButton);
            shareButton = itemView.findViewById(R.id.shareButton);
            cardView = itemView.findViewById(R.id.cardView);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            time = itemView.findViewById(R.id.postTime);
        }
    }
}