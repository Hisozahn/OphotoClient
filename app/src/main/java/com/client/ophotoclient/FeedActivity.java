package com.client.ophotoclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class FeedActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        System.out.println("CREATE FEED");
        TextView textView = findViewById(R.id.textView);
        Intent intent = getIntent();
        if (intent != null) {
            textView.append(" of " + intent.getStringExtra(SignInActivity.EXTRA_USERNAME));
        }
    }
}
