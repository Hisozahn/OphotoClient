package com.client.ophotoclient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.client.ophotoclient.objects.Credentials;

public class SignUpActivity extends AppCompatActivity {
    private EditText mUserName = null;
    private EditText mPassword = null;
    private EditText mRepeatPassword = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mUserName = findViewById(R.id.name_text);
        mPassword = findViewById(R.id.password_text);
        mRepeatPassword = findViewById(R.id.repeat_password_text);

        Intent intent = getIntent();
        if (intent != null) {
            mUserName.setText(intent.getStringExtra(SignInActivity.EXTRA_USERNAME));
        }
    }

    public void onInsteadClick(View view) {
        finish();
    }

    public void onSignUpClick(View view) {
        System.out.println("onSignInClick thread: " + Thread.currentThread().getId());
        final String user = mUserName.getText().toString();
        final String password = mPassword.getText().toString();
        final String repeatPassword = mRepeatPassword.getText().toString();
        final Context context = this;

        if (!password.equals(repeatPassword)) {
            Toast toast = Toast.makeText(context, "Password mismatch", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        NetRequest.signUp(user, password, new Response.Listener<Credentials>() {
            @Override
            public void onResponse(Credentials response) {
                Toast toast = Toast.makeText(context, "Sign up success", Toast.LENGTH_SHORT);
                toast.show();
                finish();
            }
        }, null, context);
    }
}
