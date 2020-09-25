package com.getbewarned.connectinterpreter.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.getbewarned.connectinterpreter.R;

public class ActionsMenuActivity extends NoStatusBarActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actions_menu);

        findViewById(R.id.iv_menu_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.iv_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActionsMenuActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.cl_requests).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActionsMenuActivity.this, RequestsActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.cl_help).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://interpreter.getbw.me/help");
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });

        findViewById(R.id.cl_news).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActionsMenuActivity.this, NewsActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.cl_qr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActionsMenuActivity.this, QrScannerActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.cl_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String share = getString(R.string.share_text);
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, share);
                startActivity(Intent.createChooser(shareIntent, getString(R.string.drawer_share)));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
