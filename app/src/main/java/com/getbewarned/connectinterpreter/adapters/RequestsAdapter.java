package com.getbewarned.connectinterpreter.adapters;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.holders.RequestViewHolder;
import com.getbewarned.connectinterpreter.models.Request;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class RequestsAdapter extends RealmRecyclerViewAdapter<Request, RequestViewHolder> {

    public interface OnRequestSelected {
        void onRequestSelected(Request request);
    }

    private OnRequestSelected onRequestSelected;

    public RequestsAdapter(@Nullable OrderedRealmCollection<Request> data, boolean autoUpdate) {
        super(data, autoUpdate);
        setHasStableIds(true);
    }

    @Override
    public RequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_request, parent, false);
        return new RequestViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(RequestViewHolder holder, int position) {
        final Request request = getItem(position);
        holder.updateUI(request);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onRequestSelected != null) {
                    onRequestSelected.onRequestSelected(request);
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    public void setOnRequestSelected(OnRequestSelected onRequestSelected) {
        this.onRequestSelected = onRequestSelected;
    }
}
