package com.example.mortuza.testgooglefb;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Mortuza on 11/11/2015.
 */
public class TestVolly extends Application {
    private RequestQueue requestQueue;
    private static TestVolly instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

    }

    public static synchronized TestVolly getInstance() {
        return instance;

    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;

    }

    public void addToRequest(Request req) {
        getRequestQueue().add(req);


    }
}
