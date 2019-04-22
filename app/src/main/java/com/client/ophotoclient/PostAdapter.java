package com.client.ophotoclient;

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

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public void addPost(@NonNull Post post) {
        posts.add(post);
    }
    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
    public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        ImageView image;
        TextView description;
        public PostViewHolder(View row) {
            super(row);
            image = row.findViewById(R.id.image);
            description = row.findViewById(R.id.description);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());
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
                .inflate(R.layout.row_layout, parent, false);
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
        void onItemClick(View view, int position);
    }
}