package com.example.mortuza.testgooglefb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Main2Activity extends AppCompatActivity {
    TextView smsNumber, smsBody,Totalm;
    Button sendSms;
    String Base64;
    String PhoneNumber;
    String message;
    String TotalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        Getdata();

        smsNumber = (TextView) findViewById(R.id.sendnumber);
        smsBody = (TextView) findViewById(R.id.sendtext);
        sendSms = (Button) findViewById(R.id.btnSend);
        Totalm=(TextView)findViewById(R.id.amount);
        Totalm.setText(TotalAmount);

        sendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneNumber = smsNumber.getText().toString();
                message = smsBody.getText().toString();
                VolleySend();

            }
        });


    }

    public void VolleySend() {
        StringRequest request = new StringRequest(Request.Method.POST, "https://api.infobip.com/sms/1/text/single", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                try {
//                    JSONObject jo = new JSONObject(response);
//                    JSONArray array = jo.getJSONArray("messages");
                Log.d("response", response);
                ToastShow(response);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

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

            @Override
            public byte[] getBody() throws AuthFailureError {
                String data = "{\"to\":\"" + PhoneNumber + "\",\"from\":\"FreeSms\",\"text\":\"" + message + "\"}";
                return data.getBytes();
            }
        };
        TestVolly.getInstance().addToRequest(request);
    }

    public void ToastShow(String ToastText) {
        Toast.makeText(getApplicationContext(), ToastText, Toast.LENGTH_LONG).show();
    }

    public void Getdata() {
        Intent i = getIntent();
        Bundle b = i.getExtras();
        if (b != null) {
            Base64 = b.getString("basecode");
            TotalAmount =b.getString("blnc");
        }
    }
}
