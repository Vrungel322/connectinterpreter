package com.getbewarned.connectinterpreter.ui.compensation;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.ui.NoStatusBarActivity;

/**
 * out:
 * [COMPENSATION_DATA] - [CompensationDataHolder] - get as Serializable
 */
public class CompensationPrepareActivity extends NoStatusBarActivity {
    private static final int RC = 326;
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compensation_prepare);

        ((Button) findViewById(R.id.b_yes)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompensationPrepareActivity.this, CompensationActivity.class);
                startActivityForResult(intent, CompensationActivity.RC);
            }
        });

        ((Button) findViewById(R.id.b_no)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();

            }
        });

        videoView = findViewById(R.id.vv_prepare);
        final Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.please_wait);
        videoView.setVideoURI(video);

    }

    @Override
    protected void onStart() {
        super.onStart();
        videoView.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CompensationActivity.RC) {
//                CompensationDataHolder compensationData = (CompensationDataHolder) data.getSerializableExtra(COMPENSATION_DATA);
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
