package com.getbewarned.connectinterpreter.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.getbewarned.connectinterpreter.R;

public class ImagePreviewActivity extends AppCompatActivity {

    public static Intent getInstance(Context context, String imageUrl) {
        Intent intent = new Intent(context, ImagePreviewActivity.class);
        intent.putExtra("imageUrl", imageUrl);
        return intent;
    }

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
        webView = findViewById(R.id.web);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.loadUrl(getIntent().getStringExtra("imageUrl"));

    }
}
