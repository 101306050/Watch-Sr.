package com.baobomb.watch.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.baobomb.watch.R;
import com.baobomb.watch.activity.MainActivity;
import com.baobomb.watch.parse.ParseUtil;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by baobomb on 2015/10/31.
 */
public class CustomReceiver extends ParsePushBroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        String notificationTitle = null;
        String notificationWatchID = null;
        String notificationMessage = null;
        String notificationLat = null;
        String notificationLng = null;
        try {
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
            notificationTitle = json.getString("title");
            if (notificationTitle != null && notificationTitle.equals("通知")) {
                notificationWatchID = json.getString("watchid");
                notificationLat = json.getString("lat");
                notificationLng = json.getString("lng");
            } else {
                notificationWatchID = json.getString("watchid");
                notificationMessage = json.getString("message");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (notificationTitle != null && notificationTitle.equals("通知")) {
            final String id = notificationWatchID;
            ParseUtil.checkPosition(notificationWatchID, notificationLat, notificationLng, new ParseUtil.OnRestrictCheckListener() {
                @Override
                public void onOut(double distance) {
                    generateNotification(context, "警告", id, "超出限制活動範圍" + distance + "公尺");
                }

                @Override
                public void onIn() {

                }

                @Override
                public void onUnknow() {

                }
            });
        } else {
            generateNotification(context, notificationTitle, notificationWatchID, notificationMessage);
        }
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

