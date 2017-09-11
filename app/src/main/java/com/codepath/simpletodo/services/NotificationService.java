package com.codepath.simpletodo.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.activities.MainActivity;
import com.codepath.simpletodo.helpers.DatabaseHelper;

/**
 * Created by vidhya on 9/8/17.
 */

public class NotificationService extends IntentService {
    private static final int NOTIFICATION_ID = 123;

    public NotificationService() {
        super("Get It Done: Notification Service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        showNotification();
        Log.i("GID NotificationService", "Service Running");
    }

    private void showNotification() {
        CharSequence title = "Get it done";
        int icon = R.drawable.ic_stat_name;
        CharSequence text = "GEt these items done today";
        long time = System.currentTimeMillis();
        String channelId = "channel1";

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_CANCEL_CURRENT);

        String[] items = getItemsDueToday();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(icon)
                .setContentTitle("Add items to your Get It Done list")
                .setContentIntent(contentIntent)
                .setAutoCancel(true);

        if (items.length > 0) {
            builder.setContentTitle("You have some items due today");

            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            inboxStyle.setBigContentTitle("Items due today:");
            for (int i = 0; i < items.length; i++) {
                inboxStyle.addLine((i+1) + ". " + items[i]);
            }
            builder.setStyle(inboxStyle);
        } else {
            builder.setContentTitle("Add items to your 'Get It Done' list");
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }

    private String[] getItemsDueToday() {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        String[]items = dbHelper.getToDoItemsDueToday();
        return items;
    }
}
