package com.sharingapples.react.notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;


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
    public void notify(ReadableMap details){
        Bundle bundle = Arguments.toBundle(details);
        new NotificationHelper(getReactApplicationContext()).sendNotification(bundle);
    }


    @ReactMethod
    public void schedule(double time,ReadableMap details){
        Bundle bundle = Arguments.toBundle(details);
        Intent intent = new Intent(getReactApplicationContext(),MyBroadcastListener.class);
        intent.putExtra("notification",bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getReactApplicationContext(), 12232, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getReactApplicationContext().getSystemService(getReactApplicationContext().ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, (long)time, pendingIntent);
    }

    @ReactMethod
    public void scheduleAfter(int sec,ReadableMap details){
        schedule(System.currentTimeMillis() + (sec * 1000),details);
        Toast.makeText(getReactApplicationContext(), "Scheduling Notification after " + sec + "seconds", Toast.LENGTH_SHORT).show();
    }
}
