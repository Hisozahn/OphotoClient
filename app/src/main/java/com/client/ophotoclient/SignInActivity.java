package com.client.ophotoclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.*;
import com.client.ophotoclient.objects.AuthUser;
import com.client.ophotoclient.objects.Credentials;

import io.realm.Realm;

public class SignInActivity extends AppCompatActivity {
    private EditText mUserName = null;
    private EditText mPassword = null;
    public static final String EXTRA_USERNAME = "com.client.ophotoclient.USERNAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mUserName = findViewById(R.id.name_text);
        mPassword = findViewById(R.id.password_text);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPassword.getText().clear();
        System.out.println("PAUSE SIGN IN");
    }

    public void onSignInClick(View view) {
        System.out.println("onSignInClick thread: " + Thread.currentThread().getId());
        final String user = mUserName.getText().toString();
        final String password = mPassword.getText().toString();
        NetRequest.signIn(user, password, new Response.Listener<Credentials>() {
            @Override
            public void onResponse(Credentials response) {
                onSignInSuccess(user, response);
            }
            }, null, this);
    }

    public void onSignInSuccess(String userName, Credentials response) {
        System.out.println("onSignInSuccess thread: " + Thread.currentThread().getId());
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        final AuthUser oldUser = realm.where(AuthUser.class).findFirst();
        if (oldUser != null)
            oldUser.deleteFromRealm();
        final AuthUser newUser = new AuthUser(userName, response.getToken());
        realm.copyToRealm(newUser);

        realm.commitTransaction();

        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onInsteadClick(View view) {
        System.out.println("INST NAME: " + getComponentName().getClassName());
        Intent intent = new Intent(this, SignUpActivity.class);

        intent.putExtra(EXTRA_USERNAME, mUserName.getText().toString());
        System.out.println("TO SING UP");
        startActivity(intent);
    }
}
