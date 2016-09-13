// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.iGap.adapter.G;
import com.iGap.helpers.HelperCheckUpadte;
import com.iGap.instruments.CheckDatabase;
import com.iGap.instruments.JSONParser;
import com.iGap.instruments.Utils;
import com.iGap.instruments.database;
import com.iGap.services.ContactServies;
import com.iGap.services.MyService;
import com.iGap.services.PublicService;
import com.iGap.services.SplashService;
import com.iGap.services.TimerServies;


/**
 * 
 * aghaz barname az ien class hast va braye chek kardan nasb barnameh va update barnameh va update sqlite estafadeh mishavad
 *
 */

public class Splash extends Activity {

    private static final String TAG_ISOCODE = "isoCode";
    private String              countryCode, countryIsoCode, countryName;
    private TextView            txtAppName, txtAppName2;
    private Handler             handler;
    private JSONParser          jParser     = new JSONParser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            String s = bundle.getString("isclose");

            if (s != null)
                if (s.equals("close")) {
                    G.cmd = new database(Splash.this);
                    G.cmd.useable();
                    G.cmd.open();
                    CheckDatabase.checkDb(Splash.this);
                    finish();
                    return;
                }
        }

        cancelNotification();
        handler = new Handler();

        if (G.language.equals("0")) {
            G.SelectedLanguage = "en";
        } else if (G.language.equals("1")) {
            G.SelectedLanguage = "fa";
        }

        Utils.checkLanguage(this);

        setContentView(R.layout.activity_splash);

        String needUpdate = HelperCheckUpadte.checkUpdate();

        if (needUpdate.equals("1")) { // niaz be update
            Intent intent = new Intent(Splash.this, SplashUpdate.class);
            startActivity(intent);
            finish();
        } else {
            startApp();
        }

        Tracker t = ((G) getApplication()).getTracker(G.TrackerName.APP_TRACKER);
        t.setScreenName("Splash");
        t.send(new HitBuilders.AppViewBuilder().build());
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Utils.checkLanguage(this);
        super.onConfigurationChanged(newConfig);
    }


    private void startApp() {

        G.cmd.deleteDuplicates();

        Intent intentServiceManager = new Intent(Splash.this, TimerServies.class);
        startService(intentServiceManager);

        Intent intent = new Intent(Splash.this, ContactServies.class);
        startService(intent);

        txtAppName = (TextView) findViewById(R.id.app_name_tx);
        txtAppName.setTypeface(G.neuroplp);
        txtAppName2 = (TextView) findViewById(R.id.app_name_tx_2);
        txtAppName2.setTypeface(G.neuroplp);

        TextView limitlesstx = (TextView) findViewById(R.id.limitless_tx);
        limitlesstx.setTypeface(G.robotoRegular);

        TextView companynametx = (TextView) findViewById(R.id.company_name_tx);
        companynametx.setTypeface(G.robotoLight);
        txtAppName2 = (TextView) findViewById(R.id.app_name_tx_2);
        txtAppName2.setTypeface(G.neuroplp);

        ImageView imv_app_icon = (ImageView) findViewById(R.id.app_icon_img);

        TranslateAnimation animation1 = new TranslateAnimation(0, 0, -(int) getResources().getDimension(R.dimen.dp300), 0);
        animation1.setDuration(1500); // duartion in ms
        animation1.setFillAfter(false);

        imv_app_icon.setAnimation(animation1);

        TranslateAnimation animation2 = new TranslateAnimation((int) getResources().getDimension(R.dimen.dp300), 0, 0, 0);
        animation2.setDuration(1500); // duartion in ms
        animation2.setFillAfter(false);

        txtAppName.setAnimation(animation2);
        txtAppName2.setAnimation(animation2);
        companynametx.setAnimation(animation2);

        TranslateAnimation animation3 = new TranslateAnimation( -(int) getResources().getDimension(R.dimen.dp300), 0, 0, 0);
        animation3.setDuration(1500); // duartion in ms
        animation3.setFillAfter(false);

        limitlesstx.setAnimation(animation3);

        int islogin = G.cmd.getRowCount("info");

        if (islogin == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                Log.i("LOG", "detectlocation");
                new detectlocation().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                new detectlocation().execute();
            }
        } else {
            startService(new Intent(G.context, MyService.class));
            startService(new Intent(G.context, SplashService.class));
            startService(new Intent(G.context, PublicService.class));
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {

                    Intent intent2 = new Intent(Splash.this, MainActivity.class);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent2);
                    finish();

                }
            }, 3000);

        }
    }


    public static void cancelNotification() {
        String ns = G.context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) G.context.getSystemService(ns);
        nMgr.cancel(G.NOTIFICATION_ID);
    }


    //send request to detect device location
    class detectlocation extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            Log.i("LOG", "detectlocation 1");
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                JSONObject jsonobj = jParser.getJSONFromUrl(G.detectlocation, params, "GET", "0", null);
                try {
                    Log.i("LOG", "detectlocation 2");
                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);

                    Boolean success = json.getBoolean(G.TAG_SUCCESS);
                    Log.i("LOG", "detectlocation 3 success : " + success);
                    if (success == true) {

                        JSONObject result = json.getJSONObject("result");
                        countryIsoCode = result.getString(TAG_ISOCODE);
                        try {
                            countryName = G.cmd.getCountryInfo(3, "Countries", countryIsoCode);
                            countryCode = G.cmd.getCountryInfo(1, "Countries", countryIsoCode);
                        }
                        catch (Exception e) {}

                        handler.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                Intent intent = new Intent(Splash.this, SelectingLanguage.class);
                                intent.putExtra("countryCode", countryCode);
                                intent.putExtra("countryIsoCode", countryIsoCode);
                                intent.putExtra("countryName", countryName);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        }, 5000);

                    } else {
                        Intent intent = new Intent(Splash.this, SelectingLanguage.class);
                        intent.putExtra("countryCode", "");
                        intent.putExtra("countryIsoCode", "");
                        intent.putExtra("countryName", "");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                }
                catch (JSONException e) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(G.context, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            catch (Exception e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(G.context, getString(R.string.internet_connection_problem_en), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            return null;
        }

    }


    @Override
    protected void onResume() {
        Utils.checkLanguage(this);
        super.onResume();
        G.appIsShowing = true;
    }


    @Override
    protected void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(Splash.this).reportActivityStart(this);
    }


    @Override
    protected void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(Splash.this).reportActivityStop(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        G.appIsShowing = false;
    }

}
