package com.org.zl.webandroidapp;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static android.view.View.SCROLLBARS_OUTSIDE_OVERLAY;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private TextView android_text;
    private Button button;
    private EditText edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = (WebView) findViewById(R.id.webView);
        button = (Button) findViewById(R.id.button);
        android_text = (TextView) findViewById(R.id.android_text);
        edit = (EditText) findViewById(R.id.edit);



        initData();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.post(new Runnable(){
                    @Override
                    public void run(){
                        String str = edit.getText().toString();
                        webView.loadUrl("javascript:AndroidToWeb('" + str + "')");
                        Log.e("Logs","-----------------------wave--------------------");
                    }
                });
            }
        });

    }

    @SuppressLint("AddJavascriptInterface")
    private void initData() {
        webView.getSettings().setJavaScriptEnabled(true);//设置支持js
        webView.requestFocus();
        webView.setScrollBarStyle(SCROLLBARS_OUTSIDE_OVERLAY);

        webView.addJavascriptInterface(new JavascriptInterface(), "demo");
        webView.loadUrl("file:///android_asset/index.html");//设置加载网页
    }
    class JavascriptInterface{
        @android.webkit.JavascriptInterface
        public void toAndroid(final String order){
            Log.e("Logs","-----------------------"+order);
            webView.post(new Runnable() {
                @Override
                public void run() {
                    android_text.setText(order);
                    android_text.setTextColor(Color.RED);
                }
            });
        }
    }
}