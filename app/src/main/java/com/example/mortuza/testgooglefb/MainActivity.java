package com.example.mortuza.testgooglefb;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Looper;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

public class MainActivity extends AppCompatActivity {
    TextView uName, upass, sign, freesmstexte;
    Button btnlog;
    String baseCode;
    CheckBox ckbox;
    public static final String MYPREFERENCES = "preferkey";
    public static final String USERNAME = "usernamekey";
    public static final String USERPASS = "userpasskey";
    String url = "If you not registered  <a href=\"https://accounts.infobip.com/signup\">Click here</a>";
    InternetConnection connection;
    Dialog dialog;
    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InternetCnnection();
        uName = (TextView) findViewById(R.id.uname);
        upass = (TextView) findViewById(R.id.upass);
        btnlog = (Button) findViewById(R.id.btnlogin);
        ckbox = (CheckBox) findViewById(R.id.rememberpass);
        sign = (TextView) findViewById(R.id.signin);
        freesmstexte = (TextView) findViewById(R.id.freesmstext);
        YoYo.with(Techniques.StandUp).delay(500).duration(5000).playOn(findViewById(R.id.freesmstext));
        preferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
        uName.setText(preferences.getString("usernamekey", ""));
        upass.setText(preferences.getString("userpasskey", ""));

        sign.setMovementMethod(LinkMovementMethod.getInstance());
        sign.setText(Html.fromHtml(url));
        connection = new InternetConnection(getApplicationContext());

        if (uName.getText().length() == 0 && upass.getText().length() == 0) ckbox.setChecked(false);
        else ckbox.setChecked(true);


        btnlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String base64 = uName.getText().toString() + ":" + upass.getText().toString();
                Log.d("dsad", base64);
                try {
                    byte[] data = base64.getBytes("UTF-8");
                    baseCode = "Basic " + Base64.encodeToString(data, Base64.DEFAULT);
                    Log.d("BaseCode", baseCode);
                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();
                }
                if (ckbox.isChecked() == true) {
                    Log.d("checkox", "dasdasda");
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(USERNAME, uName.getText().toString());
                    editor.putString(USERPASS, upass.getText().toString());
                    editor.commit();

                } else {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(USERNAME, "");
                    editor.putString(USERPASS, "");
                    editor.commit();

                }
                if (!connection.Net()) {
                    ToastShow("Need Internet Connection ");
                } else CheckAccount();


            }

        });


    }

    public void ToastShow(String ToastText) {
        Toast.makeText(getApplicationContext(), ToastText, Toast.LENGTH_SHORT).show();
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
                    if (Double.parseDouble(balance) > 0.0) {
                        ToastShow("Login Successfully");
                        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                        intent.putExtra("blnc", totalAmount);
                        intent.putExtra("basecode", baseCode);
                        startActivity(intent);
                        finish();
                    } else ToastShow("UR BALANCE IS 0.Please Create New Account");

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
                hashMap.put("Authorization", baseCode);
                hashMap.put("Content-Type", "application/json");
                hashMap.put("Accept", "application/json");
                return hashMap;
            }
        };
        TestVolly.getInstance().addToRequest(request);
    }

    public void InternetCnnection() {

        connection = new InternetConnection(getApplicationContext());
        if (connection.Net()) {

        } else {
            Timer tm = new Timer();
            tm.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DialogShow();
                        }
                    });


                }
            }, 3000);

        }


    }

    public void DialogShow() {
        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.internetdialog);
        dialog.setTitle("Turn On Internet Connection");
        Button wifi = (Button) dialog.findViewById(R.id.wifi);
        Button dataConnection = (Button) dialog.findViewById(R.id.data);
        Button btncancel = (Button) dialog.findViewById(R.id.can);
        wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        dataConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }

        });
        dialog.show();
    }

}
