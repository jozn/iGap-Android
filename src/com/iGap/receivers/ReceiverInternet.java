// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.receivers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import com.iGap.adapter.G;
import com.iGap.services.MyService;
import com.iGap.services.TimerServies;


/**
 * 
 * check that device connection to Internet and when connection change it notify igap program
 *
 */

public class ReceiverInternet extends BroadcastReceiver {

    private Context     mContext;
    private NetworkInfo networkInfo;


    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                sendconnectionstate("1");

                ConnectivityManager cm = ((ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE));
                if (cm == null) {
                    G.createConnection = true;
                    return;
                }
                networkInfo = cm.getActiveNetworkInfo();

                if (networkInfo != null) {

                    if (networkInfo.isConnectedOrConnecting()) {
                        sendconnectionstate("2");
                    }

                    if (networkInfo.isRoaming()) {

                    }

                    if (hasActiveInternetConnection()) {
                        sendconnectionstate("4");

                        if ( !isMyServiceRunning(MyService.class)) {

                            if (G.createConnection) {
                                G.createConnection = false;

                                Intent intentServiceManager1 = new Intent(mContext, MyService.class);
                                mContext.startService(intentServiceManager1);
                            } else {

                            }

                        } else {
                            if (G.createConnection) {
                                G.createConnection = false;

                                Intent intentServiceManager1 = new Intent(mContext, MyService.class);
                                mContext.startService(intentServiceManager1);
                            } else {

                            }
                        }

                        if ( !isMyServiceRunning(TimerServies.class)) {
                            Intent intentServiceManager1 = new Intent(mContext, TimerServies.class);
                            mContext.startService(intentServiceManager1);
                        }

                        NetworkInfo[] infoAvailableNetworks = cm.getAllNetworkInfo();
                        if (infoAvailableNetworks != null) {
                            for (NetworkInfo network: infoAvailableNetworks) {

                                if (network.getType() == ConnectivityManager.TYPE_WIFI) {
                                    if (network.isConnected() && network.isAvailable()) {

                                    }
                                }
                                if (network.getType() == ConnectivityManager.TYPE_MOBILE) {
                                    if (network.isConnected() && network.isAvailable()) {

                                    }
                                }
                                if (network.getType() == ConnectivityManager.TYPE_ETHERNET) {
                                    if (network.isConnected() && network.isAvailable()) {

                                    }
                                }
                                if (network.getType() == ConnectivityManager.TYPE_WIMAX) {
                                    if (network.isConnected() && network.isAvailable()) {

                                    }
                                }
                                if (network.getType() == ConnectivityManager.TYPE_BLUETOOTH) {
                                    if (network.isConnected() && network.isAvailable()) {

                                    }
                                }
                            }
                        }
                    } else {
                        sendconnectionstate("1");
                        G.createConnection = true;
                        mContext.stopService(new Intent(mContext, MyService.class));
                    }
                } else {

                    sendconnectionstate("1");
                    G.createConnection = true;

                    mContext.stopService(new Intent(mContext, TimerServies.class));
                }

            }
        });
        thread.start();
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service: manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    public boolean hasActiveInternetConnection() {
        if (networkInfo != null) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(3000);

                urlc.connect();

                return (urlc.getResponseCode() == 200);
            }
            catch (IOException e) {

            }
        } else {

        }
        return false;
    }


    private void sendconnectionstate(String state) { // send to PageMessagingAll
        G.connectionState = state;
        Intent intent = new Intent("connectionstateReceiver");
        intent.putExtra("state", state);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }
}
