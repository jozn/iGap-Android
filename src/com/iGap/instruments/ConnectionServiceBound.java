package com.iGap.instruments;

import java.util.ArrayList;
import org.json.JSONArray;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.iGap.services.MyService;


/**
 * 
 * bind to main service and get user last seen for a list of user
 *
 */

public class ConnectionServiceBound {

    private MyService         mService;
    private Context           context;

    private ServiceConnection Connection = new ServiceConnection() {

                                             @SuppressWarnings("unchecked")
                                             @Override
                                             public void onServiceConnected(final ComponentName name, final IBinder service) {
                                                 mService = ((LocalBinder<MyService>) service).getService();
                                             }


                                             @Override
                                             public void onServiceDisconnected(final ComponentName name) {
                                                 mService = null;
                                             }
                                         };


    public ConnectionServiceBound(Context context) {

        this.context = context;

        context.bindService(new Intent(context, MyService.class), Connection, Context.BIND_AUTO_CREATE);

    }


    public void closeConnection() {

        try {
            if (mService != null && Connection != null)
                context.unbindService(Connection);
        }
        catch (Exception e) {}
    }


    public void getLastSeen(ArrayList<String> mobile, String className) {

        JSONArray ja = new JSONArray();

        for (int i = 0; i < mobile.size(); i++) {

            ja.put(mobile.get(i));

        }
        try {

            mService.getlastseen(ja, className);
        }
        catch (Exception e) {}

    }

}
