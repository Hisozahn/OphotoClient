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
import android.widget.EditText;

import com.android.volley.Response;
import com.client.ophotoclient.objects.AuthUser;
import com.client.ophotoclient.objects.UserResponse;
import com.client.ophotoclient.objects.UserRow;
import com.client.ophotoclient.objects.UsersResponse;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class SearchFragment extends Fragment {
    private Realm realm = Realm.getDefaultInstance();
    private SearchAdapter mAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_fragment, container, false);
    }

    public void search(NetRequest.UserType type) {
        if (mAdapter == null)
            return;
        final String token = realm.where(AuthUser.class).findFirst().getToken();
        NetRequest.findUsers(token, getSearchQuery(), type, new Response.Listener<UsersResponse>() {
            @Override
            public void onResponse(UsersResponse response) {
                List<UserRow> users = new ArrayList<>();
                int i = 0;
                List<String> names = response.getUsers();
                if (names != null) {
                    for (String name : names) {
                        users.add(new UserRow(name, null));
                        final int finalIndex = i;
                        NetRequest.getUser(token, name, new Response.Listener<UserResponse>() {
                            @Override
                            public void onResponse(UserResponse response) {
                                mAdapter.setUserImage(response.getImage(), finalIndex);
                                mAdapter.notifyDataSetChanged();
                            }
                        }, null, getContext());
                        i++;
                    }
                }
                mAdapter.setUsers(users);
                mAdapter.notifyDataSetChanged();
            }
        }, null, getContext());
    }

    private String getSearchQuery() {
        EditText query = getActivity().findViewById(R.id.search_query);
        return query.getText().toString();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecyclerView recyclerView = getActivity().findViewById(R.id.search_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);

        getActivity().findViewById(R.id.search_all_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(NetRequest.UserType.ALL);
            }
        });
        getActivity().findViewById(R.id.search_followers_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(NetRequest.UserType.FOLLOWERS);
            }
        });
        getActivity().findViewById(R.id.search_following_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(NetRequest.UserType.FOLLOWING);
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new SearchAdapter(new ArrayList<UserRow>());
        mAdapter.setClickListener(new SearchAdapter.ItemClickListener() {
            @Override
            public void onItemClick(String user) {
                ((MainActivity)getActivity()).openProfile(user);
            }
        });
        System.out.println("Feed frag created");

    }
}
