package com.baobomb.watch.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.baobomb.watch.R;
import com.baobomb.watch.activity.TrackingActivity;
import com.baobomb.watch.parse.ParseUtil;

/**
 * Created by baobomb on 2015/10/31.
 */
public class AlarmReceiver extends BroadcastReceiver {

    Context context;
    int i = 100;
    String notiId = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        ParseUtil.checkWatchUpdate(new ParseUtil.OnCheckTimeListener() {
            @Override
            public void onSuccess(long time, String id) {
                compareTime(time, id);
            }

            @Override
            public void onNone() {

            }
        });
    }

    public void compareTime(long time, String Id) {
        long systemTime = System.currentTimeMillis();
        Log.d("Bao", String.valueOf(time));
        Log.d("Bao", String.valueOf(systemTime));
        if (systemTime - time > 3600000) {
            Log.d("Bao", "警告");
            generateNotification(context, "警告", Id, "超過一小時未回報位置");
        }
    }

    private void generateNotification(Context context, String alert, String watchid,
                                      String message) {
        Intent intent = new Intent(context, TrackingActivity.class);
        intent.putExtra("TrackId", watchid);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0);
        NotificationManager mNotifM = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_cast_dark)
                        .setContentTitle(alert)
                        .setVibrate(new long[]{1000, 1000})
                        .setContentText("裝置ID : " + watchid + " : " + message);
        mBuilder.setContentIntent(contentIntent);
        mNotifM.notify(++i, mBuilder.build());
    }

}
