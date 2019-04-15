package com.client.ophotoclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.client.ophotoclient.objects.AuthUser;

import io.realm.Realm;

public class ProfileFragment extends Fragment {
    private Realm realm = Realm.getDefaultInstance();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("Profile frag created");
        final AuthUser user = realm.where(AuthUser.class).findFirst();
        if (getActivity() == null)
            return;
        TextView textView = getActivity().findViewById(R.id.textView);
        textView.append(" of " + (user != null ? user.getName() : "Unknown"));
    }
}
