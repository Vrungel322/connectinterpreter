package com.getbewarned.connectinterpreter.holders;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.getbewarned.connectinterpreter.R;

/**
 * Created by artycake on 1/22/18.
 */

public class MessageViewHolder extends RecyclerView.ViewHolder {
    private final TextView message;

    public MessageViewHolder(View itemView) {
        super(itemView);
        message = itemView.findViewById(R.id.message);
    }

    public void updateUI(String text) {
        message.setText(text);
    }
}
