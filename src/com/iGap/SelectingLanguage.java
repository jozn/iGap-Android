// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.
package com.iGap;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import com.iGap.adapter.G;
import com.iGap.instruments.Utils;


/**
 * 
 * entekhab zabane barnameh tavasot karbar
 *
 */
public class SelectingLanguage extends Activity {

    private Spinner  spselectlang;
    private String   countryCode = null, countryIsoCode = null, countryName = null;
    private TextView txtGenerIcon;
    private TextView txtAppName;
    private TextView txtAppName2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        G.SelectedLanguage = "en";
        Utils.checkLanguage(this);
        setContentView(R.layout.activity_selecting_language);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            try {
                countryCode = extras.getString("countryCode");
                countryIsoCode = extras.getString("countryIsoCode");
                countryName = extras.getString("countryName");
            }
            catch (Exception e) {}

        }

        txtGenerIcon = (TextView) findViewById(R.id.txt_gener_icon);
        txtGenerIcon.setTypeface(G.fontAwesome);

        TextView txt_pl_select = (TextView) findViewById(R.id.txt_pl_select);
        txt_pl_select.setTypeface(G.robotoLight);

        TextView txt_select_langg = (TextView) findViewById(R.id.txt_select_langg);
        txt_select_langg.setTypeface(G.robotoBold);

        TextView limitless_tx = (TextView) findViewById(R.id.limitless_tx);
        limitless_tx.setTypeface(G.robotoBold);

        TextView company_name_tx = (TextView) findViewById(R.id.company_name_tx);
        company_name_tx.setTypeface(G.robotoLight);

        txtAppName = (TextView) findViewById(R.id.app_name_tx);
        txtAppName.setTypeface(G.neuroplp);

        txtAppName2 = (TextView) findViewById(R.id.app_name_tx_2);
        txtAppName2.setTypeface(G.neuroplp);

        Button btnselect = (Button) findViewById(R.id.language_btn);
        btnselect.setTypeface(G.robotoLight);

        spselectlang = (Spinner) findViewById(R.id.select_language_sp);
        ArrayList<String> list = new ArrayList<String>();
        list.add("English");
        list.add("فارسی");

        spselectlang.setAdapter(new myAdapter(list));

        btnselect.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String lang = spselectlang.getSelectedItem().toString();
                if (lang.equals("English")) {
                    G.cmd.updatelang(0);
                    G.SelectedLanguage = "en";
                } else {
                    G.cmd.updatelang(1);
                    G.SelectedLanguage = "fa";
                }

                Intent intent = new Intent(G.context, Register.class);
                intent.putExtra("countryCode", countryCode);
                intent.putExtra("countryIsoCode", countryIsoCode);
                intent.putExtra("countryName", countryName);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        txtGenerIcon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                spselectlang.performClick();

            }
        });
    }


    private class myAdapter extends BaseAdapter {

        private ArrayList<String> list;
        LayoutInflater            mInflater;


        public myAdapter(ArrayList<String> list) {
            this.list = list;
            mInflater = (LayoutInflater) G.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public int getCount() {

            return list.size();
        }


        @Override
        public Object getItem(int position) {

            return list.get(position);
        }


        @Override
        public long getItemId(int position) {

            return 0;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = null;

            view = mInflater.inflate(R.layout.sp_item, null);
            TextView txt = (TextView) view.findViewById(R.id.sp_text);
            txt.setText(list.get(position));
            return view;

        }
    }


    @Override
    protected void onResume() {
        Utils.checkLanguage(this);
        super.onResume();
        G.appIsShowing = true;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Utils.checkLanguage(this);
        super.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onPause() {
        super.onPause();
        G.appIsShowing = false;
    }
}
