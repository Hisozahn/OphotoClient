package com.client.ophotoclient;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.client.ophotoclient.objects.AuthUser;
import com.client.ophotoclient.objects.OphotoMessage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Realm realm = Realm.getDefaultInstance();

    private static final Map<Integer, Fragment> fragments;
    static {
        Map<Integer, Fragment> aMap = new HashMap<>();
        aMap.put(R.id.nav_feed, new FeedFragment());
        aMap.put(R.id.nav_profile, new ProfileFragment());
        aMap.put(R.id.nav_post, new PostFragment());
        aMap.put(R.id.nav_create_post, new CreatePostFragment());
        fragments = Collections.unmodifiableMap(aMap);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    public void openProfile(String user) {
        ProfileFragment fragment = (ProfileFragment) fragments.get(R.id.nav_profile);
        fragment.setUser(user);
        switchFragment(R.id.nav_profile);
    }

    public void openPost(String postId) {
        PostFragment fragment = (PostFragment) fragments.get(R.id.nav_post);
        fragment.setPostId(postId);
        switchFragment(R.id.nav_post);
    }

    private void switchFragment(int id) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        for (Map.Entry<Integer, Fragment> fragment : fragments.entrySet()) {
            String fragTag = fragment.getValue().getClass().getSimpleName();
            boolean isAdded = fragmentManager.findFragmentByTag(fragTag) != null;
            if (id == fragment.getKey()) {
                if (isAdded)
                    fragmentManager.beginTransaction().show(fragment.getValue()).commit();
                else
                    fragmentManager.beginTransaction().add(R.id.container, fragment.getValue(), fragTag).commit();
            } else {
                if (isAdded)
                    fragmentManager.beginTransaction().hide(fragment.getValue()).commit();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_log_out) {
            final AuthUser user = realm.where(AuthUser.class).findFirst();
            NetRequest.logOut(user.getToken(), new Response.Listener<OphotoMessage>() {
                @Override
                public void onResponse(OphotoMessage response) {
                    finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    finish();
                }
            }, getApplicationContext());
        } else if (id == R.id.nav_refresh) {
            FeedFragment feedFragment = (FeedFragment)fragments.get(R.id.nav_feed);
            feedFragment.refresh();
        } else {
            switchFragment(id);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}