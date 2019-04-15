package com.client.ophotoclient;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.client.ophotoclient.objects.Post;

public class PostArrayAdapter extends ArrayAdapter<Post> {
    private final Post[] posts;
    private final Context context;

    public PostArrayAdapter(@NonNull Context context, @NonNull Post[] objects) {
        super(context, R.layout.row_layout, objects);
        this.posts = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = convertView;
        // reuse views
        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            rowView = inflater.inflate(R.layout.row_layout, null);
        }

        // fill data
        Post post = posts[position];
        TextView textLeft = rowView.findViewById(R.id.icon);
        textLeft.setText(post.getDescription());

        TextView textRight = rowView.findViewById(R.id.label);
        textRight.setText(post.getImage());


        return rowView;
    }
}
