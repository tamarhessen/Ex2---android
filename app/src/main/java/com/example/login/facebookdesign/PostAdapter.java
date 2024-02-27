package com.example.login.facebookdesign;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.example.login.network.RetrofitClient.BASE_URL;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.example.login.facebookdesign.MenuActivity;
import com.example.login.facebookdesign.EditPostDialogFragment;

import com.example.login.facebookdesign.Post;
import com.example.login.network.WebServiceAPI;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder>
        implements EditPostDialogFragment.OnPostEditedListener {


    private final LayoutInflater mInflater;
    private static List<Post> posts;
    private String currentUserUsername;
    public static String token;
    private final Context mContext;

    public PostAdapter(Context context, String currentUserUsername) {
        mInflater = LayoutInflater.from(context);
        this.currentUserUsername = currentUserUsername; // Initialize the current user's username
        mContext = context; // Initialize the context
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
            holder.tvAuthor.setText(current.getAuthor());
            holder.tvContent.setText(current.getContent());

            // Set the Bitmap objects to the ImageViews in the ViewHolder
            holder.ivPic.setImageBitmap(current.getPic());
            holder.profilePic.setImageBitmap(current.getProfilepic());

            if (current.getPic() != null) {
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
            if (current.getAuthor().equals(currentUserUsername)) {
                // Show edit and delete buttons
                holder.editButton.setVisibility(View.VISIBLE);
                holder.deleteButton.setVisibility(View.VISIBLE);

                // Set click listeners for edit and delete buttons
                holder.editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Create and show the EditPostDialogFragment
                        EditPostDialogFragment dialogFragment = new EditPostDialogFragment(current, PostAdapter.this);
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
                                posts.remove(postPosition);
                                // Notify the adapter that the item is removed
                                notifyItemRemoved(adapterPosition);
                                notifyItemRangeChanged(adapterPosition, getItemCount()); // Update any items that come after the removed one
                            }
                        }
                    }
                });

            } else {
                // Hide edit and delete buttons
                holder.editButton.setVisibility(View.GONE);
                holder.deleteButton.setVisibility(View.GONE);
            }

            if (current.getTimestamp().equals("")) {
                holder.time.setText(getCurrentTime());
            } else {
                holder.time.setText(current.getTimestamp());
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
    // Method to add a new post to the server
    // Method to add a new post to the server
//    private void addPostToServer(OnlyUsername onlyUsername) {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL) // Replace BASE_URL with your server's base URL
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        WebServiceAPI webServiceAPI = retrofit.create(WebServiceAPI.class); // Define WebServiceAPI interface according to your server's API
//
//        // Make a POST request to your server to add the post
//        Call<Void> call = webServiceAPI.addPost(onlyUsername, "dd"); // Define addPost method in your WebServiceAPI interface
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (response.isSuccessful()) {
//                    // Post added successfully
//                    Log.d(TAG, "Post added successfully");
//                } else {
//                    // Handle error
//                    Log.e(TAG, "Failed to add post: " + response.message());
//                }
//            }

//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                // Handle failure
//                Log.e(TAG, "Failed to add post: " + t.getMessage());
//            }
//        });




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
