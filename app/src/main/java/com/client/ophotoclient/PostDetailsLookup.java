package com.client.ophotoclient;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.selection.ItemDetailsLookup;


public class PostDetailsLookup extends ItemDetailsLookup<Long> {
    private final RecyclerView mRecyclerView;

    PostDetailsLookup(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    public ItemDetails<Long> getItemDetails(@NonNull MotionEvent e) {
        View view = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
        if (view != null) {
            final RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(view);
            if (holder instanceof PostAdapter.PostViewHolder) {
                return new ItemDetails<Long>() {
                    @Override
                    public int getPosition() {
                        return holder.getAdapterPosition();
                    }

                    @Nullable
                    @Override
                    public Long getSelectionKey() {
                        return null;
                    }
                };
            }
        }
        return null;
    }
}
