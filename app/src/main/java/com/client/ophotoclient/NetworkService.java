package com.client.ophotoclient;

import android.app.IntentService;
import android.content.Intent;

public class NetworkService extends IntentService {
    public NetworkService() {
        super("com.client.ophotoclient.NetworkService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Normally we would do some work here, like download a file.
        // For our sample, we just sleep for 5 seconds.
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // Restore interrupt status.
            Thread.currentThread().interrupt();
        }
    }
}
