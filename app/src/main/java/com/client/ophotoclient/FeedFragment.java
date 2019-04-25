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

import com.android.volley.Response;
import com.client.ophotoclient.objects.AuthUser;
import com.client.ophotoclient.objects.Post;
import com.client.ophotoclient.objects.PostsResponse;
import com.client.ophotoclient.objects.UserResponse;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class FeedFragment extends Fragment {
    private Realm realm = Realm.getDefaultInstance();
    private PostAdapter mAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.feed_fragment, container, false);
    }

    public void refresh() {
        if (mAdapter == null)
            return;
        final String token = realm.where(AuthUser.class).findFirst().getToken();
        NetRequest.getPosts(token, NetRequest.PostType.ALL, new Response.Listener<PostsResponse>() {
            @Override
            public void onResponse(PostsResponse response) {
                List<Post> posts = new ArrayList<>();
                int i = 0;
                List<String> postIds = response.getPosts();
                if (postIds != null) {
                    for (String id : postIds) {
                        posts.add(new Post(id, null, null, "No user"));
                        final int finalIndex = i;
                        NetRequest.getPost(token, id, new Response.Listener<Post>() {
                            @Override
                            public void onResponse(Post response) {
                                mAdapter.setPost(response, finalIndex);
                                mAdapter.notifyDataSetChanged();
                                NetRequest.getUser(token, response.getUserName(), new Response.Listener<UserResponse>() {
                                    @Override
                                    public void onResponse(UserResponse response) {
                                        mAdapter.setPostUserImage(response.getImage(), finalIndex);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                }, null, getContext());
                            }
                        }, null, getContext());
                        i++;
                    }
                }
                mAdapter.setPosts(posts);
                mAdapter.notifyDataSetChanged();
            }
        }, null, getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refresh();

        RecyclerView recyclerView = getActivity().findViewById(R.id.feed_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new PostAdapter(new ArrayList<Post>());
        mAdapter.setClickListener(new PostAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Post post) {
                ((MainActivity)getActivity()).openPost(post.getId());
            }
        });
        System.out.println("Feed frag created");

    }
}
