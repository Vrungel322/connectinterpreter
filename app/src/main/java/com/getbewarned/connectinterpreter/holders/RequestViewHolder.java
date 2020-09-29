package com.getbewarned.connectinterpreter.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.models.Request;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
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
        // name
        name.setText(request.getName());
        // badge
        status.setText(getStatusString(request.getStatus()));
        if (request.getStatus().equals("new")){
            status.setTextColor(itemView.getResources().getColor(R.color.blue_new_ui));
            status.setBackgroundResource(R.drawable.request_new_list_item);
        }
        if (request.getStatus().equals("assigned")){
//            todo set valid colors
            status.setTextColor(itemView.getResources().getColor(android.R.color.holo_orange_dark));
            status.setBackgroundResource(R.drawable.request_in_progress_list_item);
        }
        if (request.getStatus().equals("closed")){
            status.setTextColor(itemView.getResources().getColor(android.R.color.black));
            status.setBackgroundResource(R.drawable.request_closed_list_item);
        }

        if (request.getUnreadCount() > 0) {
            unread.setVisibility(View.VISIBLE);
            unread.setText(String.valueOf(request.getUnreadCount()));
        } else {
            unread.setVisibility(View.INVISIBLE);
        }
        // date
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
