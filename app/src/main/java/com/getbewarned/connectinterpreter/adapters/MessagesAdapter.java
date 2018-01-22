package com.getbewarned.connectinterpreter.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.holders.MessageViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by artycake on 1/22/18.
 */

public class MessagesAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    private List<String> messages = new ArrayList<>();

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        String message = messages.get(position);
        holder.updateUI(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void addMessage(String message) {
        messages.add(0, message);
        this.notifyDataSetChanged();
    }
}
