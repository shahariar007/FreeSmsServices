package com.example.mortuza.testgooglefb;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by Mortuza on 11/15/2015.
 */
public class InternetConnection {
    Context context;

    public InternetConnection(Context context) {
        this.context = context;


    }
    public boolean Net()
    {
        ConnectivityManager cn = (ConnectivityManager)context.getSystemService(context.getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=cn.getActiveNetworkInfo();
        boolean isconnected=networkInfo!=null && networkInfo.isConnectedOrConnecting();
        return isconnected;

    }


}
