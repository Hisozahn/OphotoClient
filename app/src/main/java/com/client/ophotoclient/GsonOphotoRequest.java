package com.client.ophotoclient;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.client.ophotoclient.objects.OphotoMessage;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;

public class GsonOphotoRequest<T extends OphotoMessage> extends OphotoRequest<T> {
    private final Gson gson = new Gson();
    private final Class<T> clazz;

    public GsonOphotoRequest(int method, String url, String requestBody,
                             Class<T> clazz,
                             Response.Listener<T> listener,
                             Response.ErrorListener errorListener) {
        super(method, url, requestBody, listener, errorListener);
        this.clazz = clazz;
    }

    @Override
     protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            T msg = gson.fromJson(json, clazz);
            if (msg.getCode() != 1000) {
                return Response.error(new VolleyError(msg.getMessage()));
            }
            return Response.success(msg, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}
