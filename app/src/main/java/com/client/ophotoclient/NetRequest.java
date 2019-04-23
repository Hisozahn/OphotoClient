package com.client.ophotoclient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.client.ophotoclient.objects.Credentials;
import com.client.ophotoclient.objects.ImageResponse;
import com.client.ophotoclient.objects.OphotoMessage;
import com.client.ophotoclient.objects.Post;
import com.client.ophotoclient.objects.PostResponse;
import com.client.ophotoclient.objects.PostsResponse;
import com.client.ophotoclient.objects.UserResponse;

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
    private static final String getPostURL = portalURL + "/get_post";
    private static final String getUserURL = portalURL + "/get_user";
    private static final String getImageURL = portalURL + "/get_image";
    private static final String setUserImageURL = portalURL + "/set_user_image";


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
                                final Response.Listener<PostsResponse> listener,
                                final Response.ErrorListener errorListener,
                                final Context context) {
        JSONObject request = new JSONObject(new HashMap<Object, Object>() {{
            put("token", token);
            put("search_type", type.toString());
        }});
        System.out.println(request.toString());
        GsonOphotoRequest<PostsResponse> ophotoRequest = new GsonOphotoRequest<>(Request.Method.POST, getPostsURL,
                request.toString(), PostsResponse.class, listener, getErrorListener(errorListener, context));
        NetQueue.getInstance(context).addToRequestQueue(ophotoRequest);
    }

    public static void getPost(final String token, final String post_id,
                                final Response.Listener<Post> listener,
                                final Response.ErrorListener errorListener,
                                final Context context) {
        JSONObject request = new JSONObject(new HashMap<Object, Object>() {{
            put("token", token);
            put("post_id", post_id);
        }});
        System.out.println(request.toString());
        GsonOphotoRequest<PostResponse> ophotoRequest = new GsonOphotoRequest<>(Request.Method.POST, getPostURL,
                request.toString(), PostResponse.class, new Response.Listener<PostResponse>() {
            @Override
            public void onResponse(PostResponse response) {
                byte[] decodedString = Base64.decode(response.getImage(), Base64.NO_WRAP);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                Post post = new Post(response.getDescription(), decodedByte);
                listener.onResponse(post);
            }
        }, getErrorListener(errorListener, context));
        NetQueue.getInstance(context).addToRequestQueue(ophotoRequest);
    }

    public static void getUser(final String token, final String userName,
                               final Response.Listener<UserResponse> listener,
                               final Response.ErrorListener errorListener,
                               final Context context) {
        JSONObject request = new JSONObject(new HashMap<Object, Object>() {{
            put("token", token);
            put("name", userName);
        }});
        System.out.println(request.toString());
        GsonOphotoRequest<UserResponse> ophotoRequest = new GsonOphotoRequest<>(Request.Method.POST, getUserURL,
                request.toString(), UserResponse.class, listener, getErrorListener(errorListener, context));
        NetQueue.getInstance(context).addToRequestQueue(ophotoRequest);
    }

    public static void setUserImage(final String token, final Bitmap image,
                               final Response.Listener<OphotoMessage> listener,
                               final Response.ErrorListener errorListener,
                               final Context context) {
        Thread compress = new Thread(new Runnable() {
            @Override
            public void run() {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                final byte[] byteArray = byteArrayOutputStream.toByteArray();
                JSONObject request = new JSONObject(new HashMap<Object, Object>() {{
                    put("token", token);
                    put("image", Base64.encodeToString(byteArray, Base64.NO_WRAP));
                }});
                GsonOphotoRequest<OphotoMessage> ophotoRequest = new GsonOphotoRequest<>(Request.Method.POST, setUserImageURL,
                        request.toString(), OphotoMessage.class, listener, getErrorListener(errorListener, context));
                NetQueue.getInstance(context).addToRequestQueue(ophotoRequest);
            }
        });
        pool.execute(compress);
    }


    public static void getImage(final String token, final String imageId,
                               final Response.Listener<Bitmap> listener,
                               final Response.ErrorListener errorListener,
                               final Context context) {
        JSONObject request = new JSONObject(new HashMap<Object, Object>() {{
            put("token", token);
            put("image_id", imageId);
        }});
        System.out.println(request.toString());
        GsonOphotoRequest<ImageResponse> ophotoRequest = new GsonOphotoRequest<>(Request.Method.POST, getImageURL,
                request.toString(), ImageResponse.class, new Response.Listener<ImageResponse>() {
            @Override
            public void onResponse(ImageResponse response) {
                byte[] decodedString = Base64.decode(response.getImage(), Base64.NO_WRAP);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                listener.onResponse(decodedByte);
            }
        }, getErrorListener(errorListener, context));
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
                image.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
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
