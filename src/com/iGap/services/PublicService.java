package com.iGap.services;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.iGap.instruments.LocalBinder;


/**
 * 
 * this service check myService class is running and if need run myService class again
 *
 */

public class PublicService extends Service {

    private MyService         mService;
    private boolean           isBound             = false;
    public static Boolean     stopService         = false;
    private Timer             timer;

    private ServiceConnection MyServiceConnection = new ServiceConnection() {

                                                      @SuppressWarnings("unchecked")
                                                      @Override
                                                      public void onServiceConnected(final ComponentName name, final IBinder service) {
                                                          mService = ((LocalBinder<MyService>) service).getService();
                                                          isBound = true;

                                                      }


                                                      @Override
                                                      public void onServiceDisconnected(final ComponentName name) {
                                                          mService = null;
                                                          isBound = false;

                                                      }
                                                  };


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {

        try {
            bindToService();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        loop();

        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }


    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void StopService() {

        try {
            try {
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
            }
            catch (Exception e1) {
                e1.printStackTrace();
            }

            try {
                if (mService != null) {

                    mService.stopSelf();
                }
            }
            catch (Exception e) {}

            stopSelf();

        }
        catch (Exception e) {}

    }


    private void bindToService() {

        bindService(new Intent(PublicService.this, MyService.class), MyServiceConnection, Context.BIND_AUTO_CREATE);
    }


    private void loop() {

        timer = new Timer();

        timer.schedule(new TimerTask() {

            @Override
            public void run() {

                checkState();

            }
        }, 2000, 10000);

    }


    private void checkState() {

        try {

            if (stopService) {

                StopService();
                return;
            }

            if (isInternetConnect()) {
                if (isMyServiceRunning(MyService.class)) {
                    if (isSocketConnect()) {
                        //nothing to do
                    }
                    else {
                        openSocket();
                    }

                    if ( !isBound)
                        bindToService();

                }
                else {
                    runMyService();
                }

            }
            else {
                if (isMyServiceRunning(MyService.class)) {}
                else {}

            }
        }
        catch (Exception e) {}

    }


    private void runMyService() {

        startService(new Intent(getApplicationContext(), MyService.class));
    }


    private Boolean isInternetConnect() {

        try {
            HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
            urlc.setRequestProperty("User-Agent", "Test");
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(3000);
            urlc.connect();

            return (urlc.getResponseCode() == 200);
        }
        catch (IOException e) {}

        return false;
    }


    private Boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service: manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    private Boolean isSocketConnect() {

        return mService.getConnection();

    }


    private void openSocket() {
        mService.startsocket();

    }
}
