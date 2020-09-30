package com.getbewarned.connectinterpreter.ui.requests;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.ui.NoStatusBarActivity;

public class HelpRequestActivity extends NoStatusBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_request);

        findViewById(R.id.tv_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        final VideoView videoView = findViewById(R.id.vv_help_requests);
        videoView.setMediaController(new MediaController(this));
        final Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.please_wait);
        videoView.setVideoURI(video);
        videoView.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
