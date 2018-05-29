package com.getbewarned.connectinterpreter.adapters;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.holders.RequestMessageViewHolder;
import com.getbewarned.connectinterpreter.models.RequestMessage;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class RequestMessagesAdapter extends RealmRecyclerViewAdapter<RequestMessage, RequestMessageViewHolder> {

    private ItemClickListener itemClickListener;

    public RequestMessagesAdapter(@Nullable OrderedRealmCollection<RequestMessage> data, boolean autoUpdate) {
        super(data, autoUpdate);
    }

    @Override
    public RequestMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_request_message, parent, false);
        return new RequestMessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RequestMessageViewHolder holder, int position) {
        RequestMessage message = getItem(position);
        holder.updateUI(message);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null) {
                    itemClickListener.onItemSelected(getItem(holder.getAdapterPosition()));
                }
            }
        });
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemSelected(RequestMessage message);
    }
}
