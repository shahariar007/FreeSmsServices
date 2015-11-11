package com.example.mortuza.testgooglefb;

import android.content.Intent;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView uName, upass;
    Button btnlog;
    String baseCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uName = (TextView) findViewById(R.id.uname);
        upass = (TextView) findViewById(R.id.upass);
        btnlog = (Button) findViewById(R.id.btnlogin);
        btnlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String base64 = uName.getText().toString() + ":" + upass.getText().toString();
                Log.d("dsad", base64);
                try {
                    byte[] data = base64.getBytes("UTF-8");
                    baseCode = "Basic " + Base64.encodeToString(data, Base64.DEFAULT);
                    Log.d("rrdsad", baseCode);
                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();
                }

               CheckAccount();

            }

        });


    }

    public void ToastShow(String ToastText) {
        Toast.makeText(getApplicationContext(), ToastText, Toast.LENGTH_LONG).show();
    }
     public void CheckAccount()
     {
         StringRequest request = new StringRequest(Request.Method.GET, "https://api.infobip.com/account/1/balance", new Response.Listener<String>() {
             @Override
             public void onResponse(String response) {
                 try {
                     JSONObject jo=new JSONObject(response);
                     String balance=jo.get("balance").toString();
                     String Currency=jo.get("currency").toString();
                     String totalAmount=balance+" "+Currency;
                     if(Double.parseDouble(balance)>0.0)
                     {
                         ToastShow("Login Successfully");
                         Intent intent=new Intent(MainActivity.this,Main2Activity.class);
                         intent.putExtra("blnc",totalAmount);
                         intent.putExtra("basecode",baseCode);
                         startActivity(intent);
                      }
                     else ToastShow("UR BALANCE IS 0.So Create New Account");

                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
             }
         }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
                 ToastShow("ERROR");
                 error.printStackTrace();
             }
         }) {
             @Override
             public Map<String, String> getHeaders() throws AuthFailureError {
                 HashMap<String, String> hashMap = new HashMap<>();
                 hashMap.put("Host", "api.infobip.com");
                 hashMap.put("Authorization", baseCode);
                 hashMap.put("Content-Type", "application/json");
                 hashMap.put("Accept", "application/json");
                 return hashMap;
             }
         };
         TestVolly.getInstance().addToRequest(request);
     }

}
