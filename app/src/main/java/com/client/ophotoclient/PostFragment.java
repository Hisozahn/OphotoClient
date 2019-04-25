package com.client.ophotoclient;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.client.ophotoclient.objects.AuthUser;
import com.client.ophotoclient.objects.LikesResponse;
import com.client.ophotoclient.objects.OphotoMessage;
import com.client.ophotoclient.objects.Post;

import io.realm.Realm;

public class PostFragment extends Fragment {
    private Realm realm = Realm.getDefaultInstance();
    private ImageView image;
    private TextView description;
    private TextView userName;
    private TextView likes;
    private Button likeButton;
    private String postId = null;

    public void setPostId(String postId) {
        this.postId = postId;
    }

    private void refresh() {
        final String token = realm.where(AuthUser.class).findFirst().getToken();
        final String currentUser = realm.where(AuthUser.class).findFirst().getName();
        NetRequest.getPost(token, postId, new Response.Listener<Post>() {
            @Override
            public void onResponse(Post response) {
                image.setImageBitmap(response.getImage());
                userName.setText(response.getUserName());
                description.setText(response.getDescription());

                NetRequest.getPostLikes(token, postId, new Response.Listener<LikesResponse>() {
                    @Override
                    public void onResponse(LikesResponse response) {
                        likes.setText(getString(R.string.likes) + ": " +  response.getLikes().size());
                        final boolean userLikes = response.getLikes().contains(currentUser);
                        likeButton.setText(userLikes ? "Unlike" : "Like");
                        likeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                NetRequest.likePost(token, postId, userLikes ? "0" : "1", new Response.Listener<OphotoMessage>() {
                                    @Override
                                    public void onResponse(OphotoMessage response) {
                                        System.out.println("User like changed");
                                        refresh();
                                    }
                                }, null, getContext());
                            }
                        });
                    }
                }, null, getContext());
            }
        }, null, getContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.post_fragment, container, false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden)
            refresh();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        image = getActivity().findViewById(R.id.post_image);
        description = getActivity().findViewById(R.id.post_description);
        userName = getActivity().findViewById(R.id.post_user);
        likeButton = getActivity().findViewById(R.id.post_like_button);
        likes = getActivity().findViewById(R.id.post_likes);

        if (postId != null)
            refresh();
    }
}
