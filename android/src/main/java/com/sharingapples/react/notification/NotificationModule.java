package com.sharingapples.react.notification;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Created by rpidanny on 1/25/16.
 */
public class NotificationModule extends ReactContextBaseJavaModule{

    private static final String TAG = "NotificationModule";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    Activity mActivity ;
    ReactContext mContext;

    public NotificationModule(ReactApplicationContext reactContext, Activity activity){
        super(reactContext);
        mActivity = activity;
        mContext = reactContext;
        registerTokenReceived();
        registerNotificationReceived();
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

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(mContext);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(mActivity, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                mActivity.finish();
            }
            return false;
        }
        return true;
    }

    public void sendEvent(String eventName, Object params){
        mContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName,params);

    }

    public void registerTokenReceived(){
        IntentFilter intentFilter = new IntentFilter("PushNotificationRegisteredToken");

        mContext.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String token = intent.getStringExtra("token");
                WritableMap params = Arguments.createMap();
                params.putString("deviceToken", token);
                sendEvent("remoteNotificationsRegistered", params);

            }
        }, intentFilter);
    }

    public void registerNotificationReceived(){
        IntentFilter intentFilter = new IntentFilter("PushNotificationReceived");

        mContext.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String bundleString = convertJSON(intent.getBundleExtra("notification"));
                WritableMap params = Arguments.createMap();
                params.putString("dataJSON",bundleString);
                sendEvent("remoteNotificationReceived",params);
            }
        },intentFilter);
    }

    private String convertJSON(Bundle bundle) {
        JSONObject json = new JSONObject();
        Set<String> keys = bundle.keySet();
        for (String key : keys) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    json.put(key, JSONObject.wrap(bundle.get(key)));
                } else {
                    json.put(key, bundle.get(key));
                }
            } catch(JSONException e) {
                return null;
            }
        }
        return json.toString();
    }

    @ReactMethod
    public void notify(ReadableMap details){
        Bundle bundle = Arguments.toBundle(details);
        new NotificationHelper(mContext).sendNotification(bundle);
    }


    @ReactMethod
    public void schedule(ReadableMap details){
        Bundle bundle = Arguments.toBundle(details);
        double time = bundle.getDouble("time");
        Intent intent = new Intent(mContext,MyBroadcastListener.class);
        intent.putExtra("notification", bundle.getBundle("notification"));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 12232, intent, 0);
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(mContext.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, (long) time, pendingIntent);
    }
/*
    @ReactMethod
    public void scheduleAfter(int sec,ReadableMap details){
        schedule(System.currentTimeMillis() + (sec * 1000),details);
        Toast.makeText(mContext, "Scheduling Notification after " + sec + "seconds", Toast.LENGTH_SHORT).show();
    }
*/
    @ReactMethod
    public void register(String senderID){
        if (checkPlayServices()){
            Intent intent = new Intent(mContext,GCMRegistrationService.class);
            intent.putExtra("senderID", senderID);
            mContext.startService(intent);
        }
    }


}
