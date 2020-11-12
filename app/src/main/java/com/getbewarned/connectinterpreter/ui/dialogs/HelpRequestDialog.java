package com.getbewarned.connectinterpreter.ui.dialogs;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.getbewarned.connectinterpreter.R;

public class HelpRequestDialog extends NoBackgroundDialog {
    public static final String TAG = "HelpRequestDialog";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_help_request, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.tv_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
            }
        });

        final VideoView videoView = view.findViewById(R.id.vv_help_requests);
        final Uri video = Uri.parse("android.resource://" + getContext().getPackageName() + "/" + R.raw.text_video_translate);
        videoView.setVideoURI(video);
        videoView.start();
    }

}
