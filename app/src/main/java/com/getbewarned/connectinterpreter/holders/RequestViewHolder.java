package com.getbewarned.connectinterpreter.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.models.Request;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class RequestViewHolder extends RecyclerView.ViewHolder {

    private TextView name;
    private TextView updated;
    private TextView unread;
    private TextView status;
    private List<Locale> nonEnglishLocales = Arrays.asList(new Locale("ru","RU"), new Locale("uk","UA"));


    public RequestViewHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.name);
        updated = itemView.findViewById(R.id.updated);
        unread = itemView.findViewById(R.id.unread);
        status = itemView.findViewById(R.id.status);
    }

    public void updateUI(Request request) {
        name.setText(request.getName());
        status.setText(getStatusString(request.getStatus()));
        if (request.getUnreadCount() > 0) {
            unread.setVisibility(View.VISIBLE);
            unread.setText(String.valueOf(request.getUnreadCount()));
        } else {
            unread.setVisibility(View.INVISIBLE);
        }
        DateFormat dateFormat;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        if (request.getUpdated().before(calendar.getTime())) {
            dateFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT);
        } else {
            if (nonEnglishLocales.contains(Locale.getDefault())) {
                dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            } else {
                dateFormat = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
            }
        }
        updated.setText(dateFormat.format(request.getUpdated()));

    }

    private String getStatusString(String status) {
        switch (status) {
            case "new":
                return itemView.getContext().getString(R.string.status_new);
            case "assigned":
                return itemView.getContext().getString(R.string.status_assigned);
            case "closed":
                return itemView.getContext().getString(R.string.status_closed);
            default:
                return "";
        }
    }
}
