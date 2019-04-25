package com.client.ophotoclient;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.client.ophotoclient.objects.UserRow;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.UserViewHolder> {
    private List<UserRow> users;
    private ItemClickListener mClickListener;

    public void setUsers(List<UserRow> users) {
        this.users = users;
    }
    public void setUserImage(Bitmap image, int position) {
        UserRow row = users.get(position);
        row.setImage(image);
        users.set(position, row);
    }
    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        ImageView userImage;
        TextView userName;
        public UserViewHolder(View row) {
            super(row);
            userImage = row.findViewById(R.id.user_image);
            userName = row.findViewById(R.id.user_name);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(users.get(getAdapterPosition()).getName());
        }
    }

    public SearchAdapter(@NonNull List<UserRow> users) {
        this.users = users;
    }

    // Create new views (invoked by the layout manager)
    @Override
    @NonNull
    public SearchAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_row_layout, parent, false);
        UserViewHolder vh = new UserViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.userImage.setImageBitmap(users.get(position).getImage());
        holder.userName.setText(users.get(position).getName());
    }

    UserRow getItem(int id) {
        return users.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return users.size();
    }
    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(String name);
    }
}