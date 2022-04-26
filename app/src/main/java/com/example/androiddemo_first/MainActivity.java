package com.example.androiddemo_first;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button webview_btn= findViewById(R.id.button_webview);
        Button requestdata_btn = findViewById(R.id.button_requestData);
        webview_btn.setOnClickListener(this::onClick);
        requestdata_btn.setOnClickListener(this::onClick);
    }
    public void onClick(View v) {
        if (v.getId() == R.id.button_webview) {
//            跳转至webview界面
            startActivity(new Intent(MainActivity.this,WebViewActivity.class));
        } else{
            //数据请求
            startActivity(new Intent(MainActivity.this,RequestDataActivity.class));

        }
    }
}