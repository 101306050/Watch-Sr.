package com.baobomb.watch.alarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Created by baobomb on 2015/10/31.
 */
public class Alarm {
    PendingIntent pi = null;
    long time = 0;
    AlarmManager am = null;

    public Alarm(Activity activity, String BC_ACTION) {
        am = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent();
        intent.setAction(BC_ACTION);
        pi = PendingIntent.getBroadcast(activity, 0, intent, 0);
        time = System.currentTimeMillis();
    }

    public void start() {
        if (am != null && time != 0 && pi != null) {
            am.setRepeating(AlarmManager.RTC_WAKEUP, time, 60 * 60 * 1000, pi);
        }
    }
}
