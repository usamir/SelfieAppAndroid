package com.samir.selfieapp;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

public class SelfieNotificationReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 1;
    private static final String TAG = "SelfieNotification";

    Intent restartMainActivityIntent;
    PendingIntent contentIntent;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {
        // This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        Log.i(TAG, "On Receive");

        // The Intent to be used when the user clicks on the Notification View
        restartMainActivityIntent = new Intent(context, MainActivity.class);

        // The PendingIntent that wraps the underlying Intent
        contentIntent = PendingIntent.getActivity(context, 0,
                restartMainActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Set notification sound
        Uri alarmSound = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // Build the notification
        Notification.Builder notificationBuilder = new Notification.Builder(
                context).setTicker("It is Selfie Time!!!")
                .setSmallIcon(R.mipmap.ic_launcher).setAutoCancel(true)
                .setContentTitle("Time to take a selfie!")
                .setContentText("open up SelfieApp")
                .setContentIntent(contentIntent).setSound(alarmSound);

        Log.i(TAG, "Get notification manager!");
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Log.i(TAG, "Notify");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mNotificationManager.notify(NOTIFICATION_ID,
                    notificationBuilder.build());
        }

    }
}
