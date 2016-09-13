// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.TextView;
import com.iGap.adapter.G;
import com.iGap.instruments.Utils;


/**
 * namayesh layeh update barnameh be karbar
 *
 */
public class SplashUpdate extends Activity {

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Utils.checkLanguage(this);
        super.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.checkLanguage(this);
        setContentView(R.layout.activity_splash_update);
        initUi();
    }


    private void initUi() {
        TextView txt_str_i = (TextView) findViewById(R.id.aso_app_name_tx);
        txt_str_i.setTypeface(G.neuroplp);

        TextView txt_str_gap = (TextView) findViewById(R.id.aso_app_name_tx_2);
        txt_str_gap.setTypeface(G.neuroplp);
    }


    @Override
    protected void onResume() {
        Utils.checkLanguage(this);
        super.onResume();
        G.appIsShowing = true;
    }


    @Override
    protected void onPause() {
        super.onPause();
        G.appIsShowing = false;
    }
}
