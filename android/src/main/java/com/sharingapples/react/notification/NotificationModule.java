package com.sharingapples.react.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by rpidanny on 1/25/16.
 */
public class NotificationModule extends ReactContextBaseJavaModule{


    public NotificationModule(ReactApplicationContext reactContext){
        super(reactContext);
    }
    @Override
    public String getName(){
        return "NotificationAndroid";
    }

    @Override
    public Map<String, Object> getConstants(){
        final Map<String, Object> constants = new HashMap<>();
        //put constants
        return constants;
    }

    @ReactMethod
    public void notify(String notificationTitle, String notificationMessage){
        NotifyCall(getReactApplicationContext(),notificationTitle,notificationMessage);
    }

    private void NotifyCall(ReactContext context,String notificationTitle, String notificationMessage){

        Intent notificationIntent = new Intent(Intent.ACTION_VIEW);
        notificationIntent.setData(Uri.parse("arrow://arrow"));

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        Notification notification = new Notification.Builder(context)
                .setSmallIcon(android.R.mipmap.sym_def_app_icon)
                .setContentTitle(notificationTitle)
                .setContentText(notificationMessage)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}
