package com.getbewarned.connectinterpreter.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
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
        videoView.setMediaController(new MediaController(this));
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
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=BeWarned"));
        intent.setPackage("org.telegram.messenger");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/BeWarned")));
        }
    }

    void viber() {
        try {
            String viberNumber = "";
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("viber://contact?number=" + viberNumber));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Viber not installed", Toast.LENGTH_SHORT).show();
        }
    }

    void whatsApp() {
        // group
        try {
            String whatsAppGroupLink = "";
            Intent intentWhatsapp = new Intent(Intent.ACTION_VIEW);
            String url = "https://chat.whatsapp.com/" + whatsAppGroupLink;
            intentWhatsapp.setData(Uri.parse(url));
            intentWhatsapp.setPackage("com.whatsapp");
            startActivity(intentWhatsapp);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "WhatsApp not installed", Toast.LENGTH_SHORT).show();
        }

        // contact
//        String contact = "+00 9876543210"; // use country code with your phone number
//        String url = "https://api.whatsapp.com/send?phone=" + contact;
//        try {
//            PackageManager pm = getPackageManager();
//            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
//            Intent i = new Intent(Intent.ACTION_VIEW);
//            i.setData(Uri.parse(url));
//            startActivity(i);
//        } catch (PackageManager.NameNotFoundException e) {
//            Toast.makeText(getApplicationContext(), "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//        }
    }
}

//Viber:
//this for contact number:
//
//        Intent intent = new Intent("android.intent.action.VIEW",
//        Android.Net.Uri.Parse("viber://contact?number=contactnumber"));
//        Context.StartActivity(intent);
//        this for public account chat:
//
//        Intent intent = new Intent("android.intent.action.VIEW",
//        Android.Net.Uri.Parse("viber://pa?chatURI=publicaccounturi"));
//        Context.StartActivity(intent);
//        this for public account info page:
//
//        Intent intent = new Intent("android.intent.action.VIEW",
//        Android.Net.Uri.Parse("viber://pa/info?uri=publicaccounturi"));
//        Context.StartActivity(intent);
