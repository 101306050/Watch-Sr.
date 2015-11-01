package com.baobomb.watch.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.baobomb.watch.R;
import com.baobomb.watch.activity.MainActivity;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by baobomb on 2015/10/31.
 */
public class CustomReceiver extends ParsePushBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String notificationTitle = null;
        String notificationWatchID = null;
        String notificationMessage = null;
        try {
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
            String alert = json.getString("alert");
            JSONObject jsonObject = new JSONObject(alert);
            notificationTitle = jsonObject.getString("title");
            notificationWatchID = jsonObject.getString("watchid");
            notificationMessage = jsonObject.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        generateNotification(context, notificationTitle, notificationWatchID, notificationMessage);
    }

    private void generateNotification(Context context, String alert, String watchid,
                                      String message) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("TrackId", watchid);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0);
        NotificationManager mNotifM = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_cast_dark)
                        .setContentTitle(alert)
                        .setVibrate(new long[]{1000, 1000})
                        .setContentText(watchid + " : " + message);
        mBuilder.setContentIntent(contentIntent);
        mNotifM.notify(2, mBuilder.build());
    }
}

