package com.getbewarned.connectinterpreter.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

import com.getbewarned.connectinterpreter.R;

public class HelpActivity extends NoStatusBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        findViewById(R.id.iv_close_help).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.ll_telegram).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                telegram();
            }
        });

        findViewById(R.id.ll_viber).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viber();
            }
        });

        findViewById(R.id.ll_whatsapp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whatsApp();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        final VideoView videoView = findViewById(R.id.vv_help);
        final Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.help);
        videoView.setVideoURI(video);
        videoView.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    void telegram() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=Serg1906"));
        intent.setPackage("org.telegram.messenger");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/Serg1906")));
        }
    }

    void viber() {
        // viber opens with delay
        try {
            String viberNumber = "79031140903"; // no need "+" sign
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("viber://contact?number=" + viberNumber)));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Viber not installed", Toast.LENGTH_SHORT).show();
        }
    }

    void whatsApp() {
        try {
            String whatsAppNumber = "+79031140903";
            Intent intentWhatsapp = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + whatsAppNumber));
            intentWhatsapp.setPackage("com.whatsapp");
            startActivity(intentWhatsapp);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "WhatsApp not installed", Toast.LENGTH_SHORT).show();
        }
    }
}
