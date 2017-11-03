package xyz.bnayagrawal.android.icsapp.internet;

import android.app.Activity;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by binay on 11/3/2017.
 */

public class VolleyGet {
    private iVolleyCallback volleyCallback;
    private Activity activity;
    private String url;
    private String token;

    public VolleyGet(iVolleyCallback context, Activity activity, String url, String token) {
        this.url = url;
        this.token = token;
        this.activity = activity;
        this.volleyCallback = context;
    }

    public void fetchData() {
        if(token == null) {
            volleyCallback.onResponseError("Null token received");
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(activity);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        volleyCallback.onResponseReceived(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error == null || error.networkResponse == null) {
                    volleyCallback.onResponseError("Error fetching data! ");
                    return;
                }

                final String statusCode = String.valueOf(error.networkResponse.statusCode);
                try {
                    volleyCallback.onResponseError(new String(error.networkResponse.data,"UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    // exception
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "bearer " + token);
                return headers;
            }
        };
        queue.add(stringRequest);
    }
}
