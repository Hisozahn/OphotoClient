package com.client.ophotoclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class SignUpActivity extends AppCompatActivity {
    private EditText user_name = null;
    private EditText password = null;
    private EditText repeat_password = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        user_name = findViewById(R.id.name_text);
        password = findViewById(R.id.password_text);
        repeat_password = findViewById(R.id.repeat_password_text);

        Intent intent = getIntent();
        if (intent != null) {
            user_name.setText(intent.getStringExtra(SignInActivity.EXTRA_USERNAME));
        }
    }

    public void onInsteadClick(View view) {
        /*Intent intent = new Intent(this, SignInActivity.class);

        startActivity(intent);*/
        finish();
    }


    public void onSignUpClick(View view) {
    }
}
