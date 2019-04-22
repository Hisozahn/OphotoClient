package com.client.ophotoclient;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.client.ophotoclient.objects.Credentials;
import com.client.ophotoclient.objects.OphotoMessage;
import com.client.ophotoclient.objects.PostResponse;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetRequest {
    public static enum PostType {
        FOLLOWING,
        FOLLOWERS,
        SELF
    }

    private static final String portalURL = "http://192.168.0.101:8888";
    private static final String singInURL = portalURL + "/auth/login";
    private static final String singUpURL = portalURL + "/auth/create";
    private static final String logOutURL = portalURL + "/auth/logout";
    private static final String publishPostURL = portalURL + "/post";
    private static final String getPostsURL = portalURL + "/get_posts";

    private static final int MAX_T = 3;

    private static ExecutorService pool = Executors.newFixedThreadPool(MAX_T);

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

    public static void logOut(final String token,
                              final Response.Listener<OphotoMessage> listener,
                              final Response.ErrorListener errorListener,
                              final Context context) {
        JSONObject request = new JSONObject(new HashMap<Object, Object>() {{
            put("token", token);
        }});
        System.out.println(request.toString());
        GsonOphotoRequest<OphotoMessage> ophotoRequest = new GsonOphotoRequest<>(Request.Method.POST, logOutURL,
                request.toString(), OphotoMessage.class, listener, getErrorListener(errorListener, context));
        NetQueue.getInstance(context).addToRequestQueue(ophotoRequest);
    }

    public static void getPosts(final String token, final PostType type,
                                final Response.Listener<PostResponse> listener,
                                final Response.ErrorListener errorListener,
                                final Context context) {
        JSONObject request = new JSONObject(new HashMap<Object, Object>() {{
            put("token", token);
            put("search_type", type.toString());
        }});
        System.out.println(request.toString());
        GsonOphotoRequest<PostResponse> ophotoRequest = new GsonOphotoRequest<>(Request.Method.POST, getPostsURL,
                request.toString(), PostResponse.class, listener, getErrorListener(errorListener, context));
        NetQueue.getInstance(context).addToRequestQueue(ophotoRequest);
    }

    public static void post(final String token, final Bitmap image, final String description,
                            final Response.Listener<OphotoMessage> listener,
                            final Response.ErrorListener errorListener,
                            final Context context) {
        System.out.println("Post start: " + System.currentTimeMillis());
        Thread compress = new Thread(new Runnable() {
            @Override
            public void run() {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 0, byteArrayOutputStream);
                final byte[] byteArray = byteArrayOutputStream.toByteArray();
                JSONObject request = new JSONObject(new HashMap<Object, Object>() {{
                    put("token", token);
                    put("description", description);
                    put("image", Base64.encodeToString(byteArray, Base64.NO_WRAP));
                }});
                GsonOphotoRequest<OphotoMessage> ophotoRequest = new GsonOphotoRequest<>(Request.Method.POST, publishPostURL,
                        request.toString(), OphotoMessage.class, listener, getErrorListener(errorListener, context));
                NetQueue.getInstance(context).addToRequestQueue(ophotoRequest);
            }
        });
        pool.execute(compress);
        System.out.println("Post finish: " + System.currentTimeMillis());
    }
}
