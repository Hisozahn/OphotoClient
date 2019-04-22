package com.client.ophotoclient;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.selection.SelectionTracker;

import com.android.volley.Response;
import com.client.ophotoclient.objects.AuthUser;
import com.client.ophotoclient.objects.Post;
import com.client.ophotoclient.objects.PostResponse;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class FeedFragment extends Fragment {
    private Realm realm = Realm.getDefaultInstance();

    private PostAdapter mAdapter;
    private SelectionTracker mTracker;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.feed_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final AuthUser user = realm.where(AuthUser.class).findFirst();
        NetRequest.getPosts(user.getToken(), NetRequest.PostType.FOLLOWING, new Response.Listener<PostResponse>() {
            @Override
            public void onResponse(PostResponse response) {
                List<Post> posts = new ArrayList<>();
                for (String id : response.getPosts()) {
                    System.out.println("Received: " + id);
                    posts.add(new Post(id, null));
                }
                mAdapter.setPosts(posts);
                mAdapter.notifyDataSetChanged();
            }
        }, null, getContext());

        RecyclerView recyclerView = getActivity().findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // specify an adapter (see also next example)

        recyclerView.setAdapter(mAdapter);
//        mTracker = new SelectionTracker.Builder<>(
//                "image", recyclerView,
//                new StableIdKeyProvider(recyclerView),
//                new PostDetailsLookup(recyclerView),
//                StorageStrategy.createLongStorage()).withOnItemActivatedListener(new OnItemActivatedListener<Long>() {
//            @Override
//            public boolean onItemActivated(@NonNull ItemDetailsLookup.ItemDetails<Long> item, @NonNull MotionEvent e) {
//                System.out.println(item.getPosition());
//                return true;
//            }
//        }).build();
//        mTracker.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new PostAdapter(new ArrayList<Post>());
        System.out.println("Feed frag created");

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
//        mTracker.onSaveInstanceState(outState);
    }

}
