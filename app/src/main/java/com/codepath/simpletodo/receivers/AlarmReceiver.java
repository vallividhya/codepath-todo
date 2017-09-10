package com.codepath.simpletodo.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.codepath.simpletodo.services.NotificationService;

/**
 * Created by vidhya on 9/8/17.
 */

public class AlarmReceiver extends BroadcastReceiver {

    public  static final int REQUEST_CODE = 12;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, NotificationService.class);

        context.startService(i);
    }
}
