package com.client.ophotoclient;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class NetQueue {
    private static NetQueue instance;
    private RequestQueue requestQueue;
    private static Context ctx;

    private NetQueue(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized NetQueue getInstance(Context context) {
        if (instance == null) {
            instance = new NetQueue(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}