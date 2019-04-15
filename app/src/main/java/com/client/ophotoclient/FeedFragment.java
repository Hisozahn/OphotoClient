package com.client.ophotoclient;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.client.ophotoclient.objects.Post;

import io.realm.Realm;

public class FeedFragment extends ListFragment {
    private Realm realm = Realm.getDefaultInstance();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.feed_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("Feed frag created");
        Post[] values = new Post[] {
                new Post("Item 1", "Some"),
                new Post("Item 2", "Thing"),
                new Post("Item 3", "Inter"),
                new Post("Item 4", "Est"), };
        // use your custom layout
        PostArrayAdapter adapter = new PostArrayAdapter(getContext(), values);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        System.out.println("Clicked " + v.toString());
        Post item = (Post) getListAdapter().getItem(position);
        Toast.makeText(getContext(), item.getDescription() + " selected", Toast.LENGTH_LONG).show();
    }
}
