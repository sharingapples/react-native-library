package com.sharingapples.react.notification;

/**
 * Created by rpidanny on 1/26/16.
 */

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

public class GCMListenerService extends GcmListenerService {

    @Override
    public void onMessageReceived(String from,Bundle bundle){
        Bundle b = new Bundle();
        b.putString("title","GCM Message");
        b.putString("message","Message Received");
        new NotificationHelper(this).sendNotification(b);
    }
}
