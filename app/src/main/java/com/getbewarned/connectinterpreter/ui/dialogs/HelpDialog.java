package com.getbewarned.connectinterpreter.ui.dialogs;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.analytics.Analytics;
import com.getbewarned.connectinterpreter.analytics.Events;

public class HelpDialog extends NoBackgroundDialog {

    private static final String SUPPORT_NUMBER = "79998070637";
    private static final String SUPPORT_TG = "79998070637";
    public static final String TAG = "HelpDialog";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_help, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);

        view.findViewById(R.id.iv_close_help).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
            }
        });

        view.findViewById(R.id.ll_telegram).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                telegram();
            }
        });

        view.findViewById(R.id.ll_viber).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viber();
            }
        });

        view.findViewById(R.id.ll_whatsapp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whatsApp();
            }
        });

        final VideoView videoView = view.findViewById(R.id.vv_help);
        final Uri video = Uri.parse("android.resource://" + getContext().getPackageName() + "/" + R.raw.help);
        videoView.setVideoURI(video);
        videoView.start();
    }

    private void telegram() {
        String tgKey = "Moi_Rzhya";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=" + tgKey));
        intent.setPackage("org.telegram.messenger");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/" + tgKey)));
        }
    }

    private void viber() {
        // viber opens with delay
        try {
            String viberNumber = "79998070637"; // no need "+" sign
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("viber://contact?number=" + viberNumber)));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this.getContext(), "Viber not installed", Toast.LENGTH_SHORT).show();
        }
    }

    private void whatsApp() {
        try {
            String whatsAppNumber = "+" + SUPPORT_NUMBER;
            Intent intentWhatsapp = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + whatsAppNumber));
            intentWhatsapp.setPackage("com.whatsapp");
            startActivity(intentWhatsapp);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this.getContext(), "WhatsApp not installed", Toast.LENGTH_SHORT).show();
        }
    }
}
