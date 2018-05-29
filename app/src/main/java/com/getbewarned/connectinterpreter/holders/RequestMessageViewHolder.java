package com.getbewarned.connectinterpreter.holders;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.models.RequestMessage;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class RequestMessageViewHolder extends RecyclerView.ViewHolder {

    private View spaceLeft;
    private View spaceRight;
    private TextView text;
    private View imageWrapper;
    private ImageView image;
    private ImageView videoIndicator;

    public RequestMessageViewHolder(View itemView) {
        super(itemView);
        spaceLeft = itemView.findViewById(R.id.space_left);
        spaceRight = itemView.findViewById(R.id.space_right);
        text = itemView.findViewById(R.id.text);
        imageWrapper = itemView.findViewById(R.id.image_wrapper);
        image = itemView.findViewById(R.id.image);
        videoIndicator = itemView.findViewById(R.id.video_indicator);
    }

    public void updateUI(RequestMessage message) {
        if (message.getAuthor().equals(RequestMessage.SELF)) {
            spaceLeft.setVisibility(View.GONE);
            spaceRight.setVisibility(View.VISIBLE);
        } else {
            spaceLeft.setVisibility(View.VISIBLE);
            spaceRight.setVisibility(View.GONE);
        }

        if (message.getType().equals("text")) {
            text.setVisibility(View.VISIBLE);
            text.setText(message.getContent());
        } else {
            text.setVisibility(View.GONE);
        }

        if (message.getType().equals("video") || message.getType().equals("image")) {
            imageWrapper.setVisibility(View.VISIBLE);
            Picasso instance = Picasso.get();
            instance.setLoggingEnabled(true);
            instance
                    .load(message.getThumbnail())
                    .placeholder(R.drawable.blue_gradient)
                    .into(image);
            if (message.getType().equals("video")) {
                videoIndicator.setVisibility(View.VISIBLE);
                image.setAlpha(0.7f);
            } else {
                videoIndicator.setVisibility(View.GONE);
                image.setAlpha(1f);
            }
        } else {
            imageWrapper.setVisibility(View.GONE);
        }

    }


}
