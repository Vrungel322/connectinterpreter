package com.getbewarned.connectinterpreter.ui;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.interfaces.PurchaseView;
import com.getbewarned.connectinterpreter.presenters.PurchasePresenter;

public class PurchaseActivity extends NoStatusBarActivity implements PurchaseView {


    private VideoView videoView;
    private ImageView ivPlayingStopVideo;
    private Button bChooseTariff;
    private RecyclerView rvTariffs;

    private PurchasePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        // presenter
        presenter = new PurchasePresenter(this, this);
        presenter.onCreate(savedInstanceState);

        // toolbar
        ((TextView) findViewById(R.id.tv_toolbar_title)).setText(R.string.tariff_choose);
        ((ImageView) findViewById(R.id.iv_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // video
        setupVideo();

        // tariff list
        rvTariffs = findViewById(R.id.rv_tariffs);
        rvTariffs.setLayoutManager(new LinearLayoutManager(this));
        rvTariffs.setAdapter(presenter.getAdapter());

        // done
        bChooseTariff = findViewById(R.id.b_choose_tariff);
        bChooseTariff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                presenter.getAdapter().selectedItem
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        videoView.start();
    }

    @Override
    public void updateDoneBtn() {
        bChooseTariff.setActivated(presenter.getAdapter().selectedItem != null);
    }

    @Override
    public void errorReceivingTariffs(Error error) {
        Toast.makeText(this, "Error" + error.toString(), Toast.LENGTH_LONG).show();
    }

    private void setupVideo() {
        final Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.please_wait);
        videoView = findViewById(R.id.vv_purchse);
        ivPlayingStopVideo = findViewById(R.id.iv_play_stop);

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                ivPlayingStopVideo.setVisibility(View.VISIBLE);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {

                    if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                        ivPlayingStopVideo.setVisibility(View.GONE);
                        return true;
                    }
                    return false;
                }
            });
        }

        ivPlayingStopVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!videoView.isPlaying()) videoView.start();
            }
        });
        videoView.setVideoURI(video);
    }
}
