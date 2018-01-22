package com.getbewarned.connectinterpreter.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.getbewarned.connectinterpreter.R;

/**
 * Created by artycake on 1/22/18.
 */

public class MessageViewHolder extends RecyclerView.ViewHolder {
    private TextView message;

    public MessageViewHolder(View itemView) {
        super(itemView);
        message = itemView.findViewById(R.id.message);
    }

    public void updateUI(String text) {
        message.setText(text);
    }
}
