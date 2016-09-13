// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.iGap.services.TimerServies;


/**
 * 
 * Start time service
 *
 */

public class Reciver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, TimerServies.class);
        context.startService(i);
    }
}
