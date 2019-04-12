package com.client.ophotoclient;

import android.app.IntentService;
import android.content.Intent;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class NetworkService extends IntentService {
    public static class Transaction {
        private String action;
        private List<String> requestFields;
        private List<String> responseFields;

        public String getAction() {
            return action;
        }

        public List<String> getRequestFields() {
            return requestFields;
        }

        public List<String> getResponseFields() {
            return responseFields;
        }

        public Transaction(String action, List<String> requestFields, List<String> responseFields) {
            this.action = action;
            this.requestFields = requestFields;
            this.responseFields = responseFields;
        }

    }
    private static final List<String> ackResponse = Arrays.asList("code", "message");
    private static final List<Transaction> supportedTransactions = Arrays.asList(
            new Transaction("sign_in", Arrays.asList("user", "password"), ackResponse),
            new Transaction("sign_up", Arrays.asList("user", "password"), ackResponse),
            new Transaction("sign_in", Arrays.asList("user", "password"), ackResponse),
            new Transaction("sign_in", Arrays.asList("user", "password"), ackResponse));

    public NetworkService() {
        super("com.client.ophotoclient.NetworkService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //Transaction t = intent.getAction();
        System.out.println("Service thread: " + Thread.currentThread().getId());
        // Normally we would do some work here, like download a file.
        // For our sample, we just sleep for 5 seconds.
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            // Restore interrupt status.
//            Thread.currentThread().interrupt();
//        }

    }

}
