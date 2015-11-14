package com.example.mortuza.testgooglefb;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mortuza on 11/12/2015.
 */
public class NotifyServices extends Service {
    String Base64, PhoneNumber;
    IBinder binder = new Binder();

    @Override
    public void onCreate() {
        Log.d("services ", "create");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {


        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("onstartCommand", "dsa");
        Bundle dl = intent.getExtras();
        Base64 = dl.getString("coded");
        PhoneNumber = dl.getString("PhoneNumber");
        GettingNotification();
        return Service.START_NOT_STICKY;
    }

    public void GettingNotification() {

        StringRequest request = new StringRequest(Request.Method.GET, "https://api.infobip.com/sms/1/reports", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONObject js = new JSONObject(response);
                    JSONArray ja = js.getJSONArray("results");
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = ja.getJSONObject(i);
                        String number = jo.getString("to").toString();
                        JSONObject joPrice = jo.getJSONObject("price");
                        String price = joPrice.getString("pricePerMessage").toString() + " EUR";
                        JSONObject joDel = jo.getJSONObject("status");
                        String joDeli = joDel.getString("groupName").toString();
                        //ToastShow("Number" + number + " cost: " + price + " Delivary " + joDeli);
                        if (joDeli.equals("DELIVERED")) {

                            NotificationShow(number, price, joDeli);
                        } else {
                            NotificationShow(number, price, joDeli);
                            ToastShow("DELIVERED fail");
                        }

                    }


                    //ToastShow(response);
                    Log.d("dsad", ja.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastShow("Error");
                error.printStackTrace();
            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put("Host", "api.infobip.com");
                hashMap.put("Authorization", Base64);
                hashMap.put("Content-Type", "application/json");
                hashMap.put("Accept", "application/json");


                return hashMap;

            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        TestVolly.getInstance().addToRequest(request);
    }

    public void ToastShow(String st) {
        Toast.makeText(getApplicationContext(), st, Toast.LENGTH_LONG).show();
    }

    public void NotificationShow(String Number, String Cost, String Status) {
        int notificationid = 0;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.sms_launcher);
        builder.setContentTitle("Sms Delivery Info");
        builder.setAutoCancel(true);
        builder.setContentText("To:" + Number + "\n Cost:" + Cost);
        builder.setContentText("Status:" + Status);
        Intent intent=new Intent(this,Main2Activity.class);
        intent.putExtra("ncode",Base64);
        PendingIntent pendingIntent=PendingIntent.getActivity(getApplication(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        builder.build().flags |= Notification.FLAG_AUTO_CANCEL |Notification.FLAG_SHOW_LIGHTS;
        notificationManager.notify(notificationid, builder.build());




    }


}
