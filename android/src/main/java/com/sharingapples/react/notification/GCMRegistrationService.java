package com.sharingapples.react.notification;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

/**
 * Created by rpidanny on 1/26/16.
 */
public class GCMRegistrationService extends IntentService {

    private static final String TAG = "PushNotification";
    private static final String[] TOPICS = {"global"};

    public GCMRegistrationService(){
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String SenderID = intent.getStringExtra("senderID");
        try{
            InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
            String token = instanceID.getToken(SenderID, GoogleCloudMessaging.INSTANCE_ID_SCOPE,null);

            sendTokenToServer(token);
            sendRegistrationToken(token);

            sharedPreferences.edit().putBoolean(GCMPreferences.SENT_TOKEN_TO_SERVER,true).apply();
        }catch (Exception e){
            sharedPreferences.edit().putBoolean(GCMPreferences.SENT_TOKEN_TO_SERVER,false).apply();
            e.printStackTrace();
        }
    }

    private void sendRegistrationToken(String token){
        Intent intent = new Intent(GCMPreferences.REGISTRATION_COMPLETE);
        intent.putExtra("token",token);
        sendBroadcast(intent);
    }

    private void sendTokenToServer(String token){

    }
}
