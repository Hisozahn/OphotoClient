package com.client.ophotoclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.*;
import com.android.volley.toolbox.*;

import org.json.JSONException;
import org.json.JSONObject;

public class SignInActivity extends AppCompatActivity {
    private EditText mUserName = null;
    private EditText mPassword = null;
    public static final String EXTRA_USERNAME = "com.client.ophotoclient.USERNAME";
    public static final String SIGN_IN_NET_TAG = "SignInNet";

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
        NetQueue.getInstance(this).getRequestQueue().cancelAll(SIGN_IN_NET_TAG);
        System.out.println("PAUSE SIGN IN");
    }

    public void onSignInClick(View view) {
        System.out.println("onSignInClick thread: " + Thread.currentThread().getId());
        String user = mUserName.getText().toString();
        String password = mPassword.getText().toString();
        NetRequest.signIn(user, password, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                onSignInResponse(response);
            }
            }, null, this);
    }

    public void onSignInResponse (JSONObject response) {
        System.out.println("onSignInResponse thread: " + Thread.currentThread().getId());
        int code;
        String message;
        try {
            code = response.getInt("code");
            message = response.getString("message");
        }
        catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        if (code != 1000) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Authentication failure: " + message, Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        Intent intent = new Intent(SignInActivity.this, FeedActivity.class);
        intent.putExtra(EXTRA_USERNAME, mUserName.getText().toString());
        startActivity(intent);
    }

    public void onInsteadClick(View view) {
        System.out.println("OnInsteadClick thread: " + Thread.currentThread().getId());
        Intent sintent = new Intent(this, NetworkService.class);
        startService(sintent);

        System.out.println("INST NAME: " + getComponentName().getClassName());
        Intent intent = new Intent(this, SignUpActivity.class);

        intent.putExtra(EXTRA_USERNAME, mUserName.getText().toString());
        System.out.println("TO SING UP");
        startActivity(intent);
    }
}
