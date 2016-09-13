// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap;

import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.iGap.adapter.G;
import com.iGap.helpers.HelperDrawAlphabet;
import com.iGap.helpers.HelperGetTime;
import com.iGap.instruments.ConnectionServiceBound;
import com.iGap.instruments.ImageLoader1;
import com.iGap.instruments.UtileProg;
import com.iGap.instruments.Utils;


/**
 * 
 * namayesh list contact haye register shodeh karbar
 *
 */
public class SelectContactSingle extends Activity {

    private ArrayList<Item>        listItem = new ArrayList<Item>();
    private ListView               registeredlv;
    private EditText               edtsearch;
    private TextView               txt_contact;
    private ImageLoader1           il;
    private UtileProg              utileProg;
    private ConnectionServiceBound connectionServiceBound;


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Utils.checkLanguage(this);
        super.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.checkLanguage(this);
        setContentView(R.layout.activity_select_contact_single);

        utileProg = new UtileProg(this);
        il = new ImageLoader1(SelectContactSingle.this, G.basicAuth);
        connectionServiceBound = new ConnectionServiceBound(SelectContactSingle.this);

        Button btnsearchicon = (Button) findViewById(R.id.btn_searchicon);
        btnsearchicon.setTypeface(G.fontAwesome);

        Button btnback = (Button) findViewById(R.id.btn_back);

        btnback.setTypeface(G.fontAwesome);

        txt_contact = (TextView) findViewById(R.id.textView2);
        txt_contact.setTypeface(G.robotoBold);

        edtsearch = (EditText) findViewById(R.id.edt_search);
        edtsearch.setVisibility(View.GONE);

        edtsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                search(s.toString());

            }


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnback.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });

        btnsearchicon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (txt_contact.getVisibility() == View.VISIBLE) {

                    AlphaAnimation alpha = new AlphaAnimation(1, 0);
                    alpha.setDuration(200);
                    txt_contact.setAnimation(alpha);
                    txt_contact.setVisibility(View.GONE);
                    edtsearch.setVisibility(View.VISIBLE);
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInputFromWindow(edtsearch.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                    edtsearch.requestFocus();

                    ScaleAnimation scale = new ScaleAnimation(0, 1f, 1f, 1f, 1, 1f, 1, 1f);
                    scale.setDuration(200);
                    edtsearch.setAnimation(scale);

                } else {
                    ScaleAnimation scale = new ScaleAnimation(1f, 0, 1f, 1f, 1, 1f, 1, 1f);
                    scale.setDuration(50);
                    edtsearch.setAnimation(scale);
                    edtsearch.setVisibility(View.GONE);
                    edtsearch.clearFocus();
                    txt_contact.setVisibility(View.VISIBLE);
                    AlphaAnimation alpha = new AlphaAnimation(0, 1);
                    alpha.setDuration(200);
                    alpha.setStartOffset(100);
                    txt_contact.setAnimation(alpha);
                }

            }
        });

        try {
            Cursor cu = G.cmd.getallContacts("1");
            if (cu != null) {
                while (cu.moveToNext()) {

                    Item item = new Item();

                    item.name = cu.getString(1);
                    item.mobile = cu.getString(2);
                    item.avatar_lq = cu.getString(4);
                    item.avatar_hq = cu.getString(5);
                    item.lastseen = cu.getString(6);
                    item.uid = cu.getString(7);

                    listItem.add(item);
                }
                cu.close();
            }
        }
        catch (Exception e1) {}

        registeredlv = (ListView) findViewById(R.id.registeredlv);

        registeredlv.setAdapter(new AA(listItem));

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                ArrayList<String> s = new ArrayList<String>();
                for (int i = 0; i < listItem.size(); i++) {
                    s.add(listItem.get(i).mobile);
                }

                connectionServiceBound.getLastSeen(s, "SelectContactSingle");

            }
        }, 2000);

        registeredlv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(SelectContactSingle.this, Singlechat.class);

                intent.putExtra("userchat", listItem.get(position).mobile + "@igap.im"); //=== /igap
                intent.putExtra("userchatname", listItem.get(position).name);
                intent.putExtra("userchatavatar", listItem.get(position).avatar_lq);
                intent.putExtra("chatroomid", "");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

    }


    @Override
    protected void onResume() {
        Utils.checkLanguage(this);
        LocalBroadcastManager.getInstance(SelectContactSingle.this).registerReceiver(notifyAddContacts, new IntentFilter("notifyAddContacts"));
        LocalBroadcastManager.getInstance(SelectContactSingle.this).registerReceiver(UpdateLastSeenSelectContactSingle, new IntentFilter("UpdateLastSeenSelectContactSingle"));
        super.onResume();
        G.appIsShowing = true;
    }


    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(SelectContactSingle.this).unregisterReceiver(notifyAddContacts);
        LocalBroadcastManager.getInstance(SelectContactSingle.this).unregisterReceiver(UpdateLastSeenSelectContactSingle);
        connectionServiceBound.closeConnection();
        super.onPause();
        G.appIsShowing = false;
    }

    private BroadcastReceiver notifyAddContacts                 = new BroadcastReceiver() {

                                                                    @Override
                                                                    public void onReceive(Context context, Intent intent) {

                                                                        finish();
                                                                        startActivity(getIntent());
                                                                    }
                                                                };

    private BroadcastReceiver UpdateLastSeenSelectContactSingle = new BroadcastReceiver() {

                                                                    @Override
                                                                    public void onReceive(Context context, Intent intent) {

                                                                        String lastSeen = intent.getStringExtra("lastSeen");
                                                                        try {
                                                                            JSONObject json = new JSONObject(lastSeen);

                                                                            for (int i = 0; i < listItem.size(); i++) {
                                                                                String lasts = json.get(listItem.get(i).mobile).toString();

                                                                                lasts = HelperGetTime.getStringTime(lasts, G.cmd);

                                                                                listItem.get(i).lastseen = lasts;
                                                                                G.cmd.setLastSeen(listItem.get(i).mobile, lasts);
                                                                            }

                                                                            runOnUiThread(new Runnable() {

                                                                                public void run() {

                                                                                    ((BaseAdapter) registeredlv.getAdapter()).notifyDataSetChanged();
                                                                                }
                                                                            });

                                                                        }
                                                                        catch (JSONException e) {

                                                                        }

                                                                    }
                                                                };


    class AA extends ArrayAdapter<Item> {

        private ArrayList<Item> list;


        public AA(ArrayList<Item> list) {
            super(SelectContactSingle.this, R.layout.list_item_allcontacts, list);
            this.list = list;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater in = getLayoutInflater();
            View row = in.inflate(R.layout.list_item_allcontacts, parent, false);

            TextView txtcontacticon = (TextView) row.findViewById(R.id.txt_contacticon);
            txtcontacticon.setTypeface(G.fontAwesome);

            TextView txtname = (TextView) row.findViewById(R.id.txt_name);
            txtname.setTypeface(G.robotoBold);

            TextView txtdesc = (TextView) row.findViewById(R.id.txt_member_des);
            txtdesc.setTypeface(G.robotoLight);

            TextView txtlastseen = (TextView) row.findViewById(R.id.txt_last_seen);
            txtlastseen.setTypeface(G.robotoLight);

            ImageView chbselected = (ImageView) row.findViewById(R.id.imageView_checkbox_selected);
            chbselected.setVisibility(View.GONE);
            final ImageView img_avatar = (ImageView) row.findViewById(R.id.img_avatar);

            row.findViewById(R.id.ll_checkbox_selected).setVisibility(View.INVISIBLE);

            txtname.setText(list.get(position).name);
            txtlastseen.setText(list.get(position).lastseen);
            txtdesc.setText(list.get(position).mobile);

            if (list.get(position).avatar_lq != null && !list.get(position).avatar_lq.isEmpty() && !list.get(position).avatar_lq.equals("null") && !list.get(position).avatar_lq.equals("")) {
                il.DisplayImage(list.get(position).avatar_lq, R.drawable.difaultimage, img_avatar);
            } else {
                if (list.get(position).name != null && !list.get(position).name.isEmpty() && !list.get(position).name.equals("null") && !list.get(position).name.equals("")) {
                    HelperDrawAlphabet pf = new HelperDrawAlphabet();
                    Bitmap bm = pf.drawAlphabet(SelectContactSingle.this, list.get(position).name, img_avatar);
                    img_avatar.setImageBitmap(bm);

                } else {
                    Bitmap bm = utileProg.drawAlphabetOnPicture(img_avatar.getLayoutParams().width, "", "");
                    img_avatar.setImageBitmap(bm);
                }
            }

            return (row);
        }
    }


    private class Item {

        String name;
        String mobile;
        String avatar_lq;
        String avatar_hq;
        String lastseen;
        String uid;

    }


    private void search(String name) {

        try {
            ArrayList<Item> subList = new ArrayList<Item>();

            if (name != null) {
                if (name.trim().length() > 0) {
                    String s = name.trim().toLowerCase();
                    for (int i = 0; i < listItem.size(); i++) {

                        if (listItem.get(i).name.trim().toLowerCase().startsWith(s)) {
                            subList.add(listItem.get(i));
                        }
                    }
                    registeredlv.setAdapter(new AA(subList));

                } else {
                    registeredlv.setAdapter(new AA(listItem));
                }

            } else {
                registeredlv.setAdapter(new AA(listItem));
            }
        }
        catch (Exception e) {}

    }

}
