package com.getbewarned.connectinterpreter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.UiUtils;
import com.getbewarned.connectinterpreter.analytics.Analytics;
import com.getbewarned.connectinterpreter.analytics.Events;
import com.getbewarned.connectinterpreter.ui.dialogs.HelpDialog;
import com.getbewarned.connectinterpreter.ui.requests.RequestsActivity;

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
                Intent intent = new Intent(ActionsMenuActivity.this, ProfileActivityV2.class);
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
                getSupportFragmentManager().beginTransaction().add(new HelpDialog(), "HelpDialog").commitAllowingStateLoss();
                Analytics.getInstance().trackEvent(Events.EVENT_ACTION_HELP);
            }
        });

        findViewById(R.id.cl_news).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActionsMenuActivity.this, NewsActivity.class);
                startActivity(intent);
            }
        });
//        findViewById(R.id.cl_qr).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ActionsMenuActivity.this, QrScannerActivity.class);
//                startActivity(intent);
//            }
//        });

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

        // set shadows
        UiUtils.actionActionsScreen(findViewById(R.id.iv_requests));
        UiUtils.actionActionsScreen(findViewById(R.id.iv_help));
        UiUtils.actionActionsScreen(findViewById(R.id.iv_news));
        UiUtils.actionActionsScreen(findViewById(R.id.iv_share));
//        UiUtils.actionActionsScreen(findViewById(R.id.iv_qr));

        Analytics.getInstance().trackEvent(Events.EVENT_MAIN_MENU_OPENED);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
