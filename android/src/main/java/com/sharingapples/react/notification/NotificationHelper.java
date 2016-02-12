package com.sharingapples.react.notification;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by rpidanny on 1/26/16.
 * helper class for sending local notifications
 */
public class NotificationHelper {

    private Context mContext;
    private static final String TAG = "NotificationHelper";

    public NotificationHelper(Context context){
        mContext = context;
    }

    public void sendNotification(Bundle bundle){
        Intent notificationIntent = new Intent(Intent.ACTION_VIEW);
        notificationIntent.setData(Uri.parse("arrow://test1"));

        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, 0);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(mContext)
                .setContentTitle(bundle.getString("title"))
                .setContentText(bundle.getString("message"))
                .setTicker(bundle.getString("ticker"))
                .setSmallIcon(android.R.mipmap.sym_def_app_icon)
                .setContentIntent(pendingIntent)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        int notificationID;

        String notificationIDString = bundle.getString("id");
        if(notificationIDString!=null){
            notificationID = Integer.parseInt(notificationIDString);
        }else{
            notificationID = (int) System.currentTimeMillis();
        }
        if(bundle.getString("group")!=null){
            notification.setGroup(bundle.getString("group"));
        }
        Notification info = notification.build();
        info.defaults |= Notification.DEFAULT_VIBRATE;
        info.defaults |= Notification.DEFAULT_SOUND;
        info.defaults |= Notification.DEFAULT_LIGHTS;

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID, info);
    }
}
