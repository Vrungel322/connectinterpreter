package com.getbewarned.connectinterpreter.models;

import android.content.Context;

import com.getbewarned.connectinterpreter.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by artycake on 1/16/18.
 */

public class HumanDate {
    private String date;

    public HumanDate(Context context, long timestamp) {
        Date date = new Date(timestamp * 1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat(context.getString(R.string.date_format), Locale.getDefault());
        this.date = dateFormat.format(date);
    }

    public String getDate() {
        return date;
    }
}
