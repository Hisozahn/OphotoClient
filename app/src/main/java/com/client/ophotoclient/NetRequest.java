package com.client.ophotoclient;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.client.ophotoclient.objects.Credentials;

import org.json.JSONObject;

import java.util.HashMap;

public class NetRequest {
    private static final String portalURL = "http://192.168.0.101:8888";
    private static final String singInURL = portalURL + "/auth/login";
    private static final String singUpURL = portalURL + "/auth/create";

    private static Response.ErrorListener getErrorListener(final Response.ErrorListener errorListener,
                                                           final Context context) {
        if (errorListener != null)
            return errorListener;

        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast toast = Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT);
                toast.show();
            }
        };
    }

    public static void signIn(final String user, final String password,
                              final Response.Listener<Credentials> listener,
                              final Response.ErrorListener errorListener,
                              final Context context) {
        JSONObject request = new JSONObject(new HashMap<Object, Object>() {{
            put("user", user);
            put("password", password);
        }});
        System.out.println(request.toString());
        GsonOphotoRequest<Credentials> ophotoRequest = new GsonOphotoRequest<>(Request.Method.POST, singInURL,
                request.toString(), Credentials.class, listener, getErrorListener(errorListener, context));
        NetQueue.getInstance(context).addToRequestQueue(ophotoRequest);
    }

    public static void signUp(final String user, final String password,
                              final Response.Listener<Credentials> listener,
                              final Response.ErrorListener errorListener,
                              final Context context) {
        JSONObject request = new JSONObject(new HashMap<Object, Object>() {{
            put("user", user);
            put("password", password);
        }});
        System.out.println(request.toString());
        GsonOphotoRequest<Credentials> ophotoRequest = new GsonOphotoRequest<>(Request.Method.POST, singUpURL,
                request.toString(), Credentials.class, listener, getErrorListener(errorListener, context));
        NetQueue.getInstance(context).addToRequestQueue(ophotoRequest);
    }
}
