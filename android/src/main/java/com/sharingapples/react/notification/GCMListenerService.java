package com.sharingapples.react.notification;

/**
 * Created by rpidanny on 1/26/16.
 */

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

public class GCMListenerService extends GcmListenerService {

    @Override
    public void onMessageReceived(String from,Bundle bundle){
        new NotificationHelper(this).sendNotification(bundle);
    }
}
