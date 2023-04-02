package com.example.stage2poche.entities;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

public class NotificationReceiver extends BroadcastReceiver {

    public static final String NOTIFICATION_ID = "notification_id";
    public static final String NOTIFICATION = "notification";


    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        Bundle extras = intent.getExtras();
        if (extras != null) {
            Notification notification = extras.getParcelable(NOTIFICATION, Notification.class);
            int id = extras.getInt(NOTIFICATION_ID, 0);
            Log.d("NotificationDebug", "Message received for offer ID: " + id);
            if(notification != null){
                notificationManager.notify(id, notification);
                Log.d("NotificationDebug", "Notification displayed for offerid : " + id);
            }
        }

    }
}