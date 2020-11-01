package com.getbewarned.connectinterpreter.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.models.RequestMessage;
import com.squareup.picasso.Picasso;

public class RequestMessageViewHolder extends RecyclerView.ViewHolder {

    private final TextView text;
    private final View imageWrapper;
    private final ImageView image;
    private final ImageView videoIndicator;

    public RequestMessageViewHolder(View itemView) {
        super(itemView);
        text = itemView.findViewById(R.id.text);
        imageWrapper = itemView.findViewById(R.id.image_wrapper);
        image = itemView.findViewById(R.id.image);
        videoIndicator = itemView.findViewById(R.id.video_indicator);
    }

    public void updateUI(RequestMessage message) {


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
