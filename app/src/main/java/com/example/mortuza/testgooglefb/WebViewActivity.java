package com.example.mortuza.testgooglefb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class WebViewActivity extends AppCompatActivity {

    WebView view;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        view=(WebView)findViewById(R.id.webview);
        view.loadUrl("https://accounts.infobip.com/signup");
    }
}
