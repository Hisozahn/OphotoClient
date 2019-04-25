package com.client.ophotoclient;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.client.ophotoclient.objects.Post;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<Post> posts;
    private ItemClickListener mClickListener;

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
    public void setPost(Post post, int position) {
        Post p = posts.get(position);
        p.setImage(post.getImage());
        p.setDescription(post.getDescription());
        p.setUserName(post.getUserName());
        posts.set(position, p);
    }
    public void setPostUserImage(Bitmap image, int position) {
        Post post = posts.get(position);
        post.setUserImage(image);
        posts.set(position, post);
    }
    public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        ImageView userImage;
        TextView userName;
        ImageView image;
        TextView description;
        public PostViewHolder(View row) {
            super(row);
            image = row.findViewById(R.id.image);
            description = row.findViewById(R.id.description);
            userImage = row.findViewById(R.id.user_image);
            userName = row.findViewById(R.id.user_name);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(posts.get(getAdapterPosition()));
        }
    }

    public PostAdapter(@NonNull List<Post> posts) {
        this.posts = posts;
    }

    // Create new views (invoked by the layout manager)
    @Override
    @NonNull
    public PostAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_row_layout, parent, false);
        PostViewHolder vh = new PostViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.image.setImageBitmap(posts.get(position).getImage());
        holder.description.setText(posts.get(position).getDescription());
        holder.userImage.setImageBitmap(posts.get(position).getUserImage());
        holder.userName.setText(posts.get(position).getUserName());
    }

    Post getItem(int id) {
        return posts.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return posts.size();
    }
    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(Post post);
    }
}