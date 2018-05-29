package com.getbewarned.connectinterpreter.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.models.Request;
import com.getbewarned.connectinterpreter.presenters.RequestPresenter;
import com.getbewarned.connectinterpreter.ui.RequestActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import io.realm.Realm;

/**
 * Created by artycake on 1/17/18.
 */

public class FBMessagingService extends FirebaseMessagingService {

    private static final int NOTIFICATION_ID = 17;

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
            if (remoteMessage.getData().containsKey("request_id")) {
                Realm.init(this);
                Realm realm = Realm.getDefaultInstance();
                long id = Long.parseLong(remoteMessage.getData().get("request_id"));
                Request request = realm.where(Request.class).equalTo("id", id).findFirst();
                if (request != null) {

                    if (remoteMessage.getData().containsKey("status")) {
                        realm.beginTransaction();
                        request.setStatus(remoteMessage.getData().get("status"));
                        realm.commitTransaction();
                    } else {
                        realm.beginTransaction();
                        request.setUnreadCount(request.getUnreadCount() + 1);
                        realm.commitTransaction();
                    }

                    Intent intent = new Intent(getApplicationInfo().packageName + ".UPDATE_REQUEST_MESSAGES");
                    intent.putExtra(RequestPresenter.REQUEST_ID, request.getId());
                    sendBroadcast(intent);

                    Intent openIntent = new Intent(this, RequestActivity.class);
                    openIntent.putExtra(RequestPresenter.REQUEST_ID, request.getId());
                    openIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    PendingIntent notificationIntent = PendingIntent.getActivity(this, 0, openIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    notif.setContentIntent(notificationIntent);

                }
            }
        }

        if (remoteMessage.getNotification() != null) {
            Log.d("NOTIF", "Message Notification Body: " + remoteMessage.getNotification().getBody());
            notif.setContentTitle(remoteMessage.getNotification().getTitle());
            String text = remoteMessage.getNotification().getBody();
            notif.setContentText(text);
            notif.setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(text));
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID, notif.build());
        }
    }
}


