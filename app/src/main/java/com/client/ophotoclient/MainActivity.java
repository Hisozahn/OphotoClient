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

    private static final Map<Fragment, Integer> fragments;
    static {
        Map<Fragment, Integer> aMap = new HashMap<>();
        aMap.put(new FeedFragment(), R.id.nav_feed);
        aMap.put(new ProfileFragment(), R.id.nav_profile);
        aMap.put(new CreatePostFragment(), R.id.nav_create_post);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();

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
        }
        for (Map.Entry<Fragment, Integer> fragment : fragments.entrySet()) {
            String fragTag = fragment.getKey().getClass().getSimpleName();
            boolean isAdded = fragmentManager.findFragmentByTag(fragTag) != null;
            if (id == fragment.getValue()) {
                if (isAdded)
                    fragmentManager.beginTransaction().show(fragment.getKey()).commit();
                else
                    fragmentManager.beginTransaction().add(R.id.container, fragment.getKey(), fragTag).commit();
            } else {
                if (isAdded)
                    fragmentManager.beginTransaction().hide(fragment.getKey()).commit();
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}