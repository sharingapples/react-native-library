package com.sharingapples.react.notification;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by rpidanny on 1/26/16.
 */
public class GCMInstanceIDListenerService extends InstanceIDListenerService {

    private static final String TAG = "InstanceIDLS";

    @Override
    public void onTokenRefresh(){
        Intent intent = new Intent(this,GCMRegistrationService.class);
        startService(intent);
    }
}
