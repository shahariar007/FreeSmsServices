package com.example.mortuza.testgooglefb;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Main2Activity extends AppCompatActivity {
    EditText smsNumber, smsBody;
    TextView Totalm, charcnt;
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

        smsNumber = (EditText) findViewById(R.id.sendnumber);
        smsBody = (EditText) findViewById(R.id.sendtext);
        sendSms = (Button) findViewById(R.id.btnSend);
        Totalm = (TextView) findViewById(R.id.amount);
        charcnt = (TextView) findViewById(R.id.charCount);

        smsBody.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                charcnt.setText("Word:" + String.valueOf(s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        Totalm.setText(TotalAmount);

        sendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneNumber = smsNumber.getText().toString();
                message = smsBody.getText().toString();
                VolleySend();
                CheckAccount();

            }
        });


    }


    public void VolleySend() {
        StringRequest request = new StringRequest(Request.Method.POST, "https://api.infobip.com/sms/1/text/single", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent intent = new Intent(getApplicationContext(), NotifyServices.class);
                intent.putExtra("coded", Base64);
                intent.putExtra("Number", PhoneNumber);

                getApplication().startService(intent);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastShow("check");
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
            TotalAmount = b.getString("blnc");
        } else Base64 = null;
    }

    public void CheckAccount() {
        StringRequest request = new StringRequest(Request.Method.GET, "https://api.infobip.com/account/1/balance", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo = new JSONObject(response);
                    String balance = jo.get("balance").toString();
                    String Currency = jo.get("currency").toString();
                    String totalAmount = balance + " " + Currency;
                    Totalm.setText(totalAmount);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                YoYo.with(Techniques.Wobble).delay(700).playOn(findViewById(R.id.uname));
                YoYo.with(Techniques.Shake).delay(700).playOn(findViewById(R.id.upass));
                ToastShow("Login Fail");
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
        TestVolly.getInstance().addToRequest(request);

    }
}

