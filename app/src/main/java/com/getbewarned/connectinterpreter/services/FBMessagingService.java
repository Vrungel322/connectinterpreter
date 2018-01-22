package com.getbewarned.connectinterpreter.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.getbewarned.connectinterpreter.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by artycake on 1/17/18.
 */

public class FBMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("NOTIF", "From: " + remoteMessage.getFrom());
        NotificationCompat.Builder notif = new NotificationCompat.Builder(this);
        notif.setSmallIcon(R.drawable.connect_interpreter_icon_blank);
        notif.setAutoCancel(true);
        if (remoteMessage.getData().size() > 0) {
            Log.d("NOTIF", "Message data payload: " + remoteMessage.getData());

            if (remoteMessage.getData().containsKey("link")) {
                Intent notificationIntent = new Intent(Intent.ACTION_VIEW);

                notificationIntent.setData(Uri.parse(remoteMessage.getData().get("link")));
                PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
                notif.setContentIntent(contentIntent);
            }
            notif.setContentTitle(remoteMessage.getData().get("title"));
            String text = remoteMessage.getData().get("text");
            notif.setContentText(text);
            notif.setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(text));
        }

        if (remoteMessage.getNotification() != null) {
            Log.d("NOTIF", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(836, notif.build());

    }
}


