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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
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
import com.iGap.instruments.Utils;


/**
 * 
 * namayesh list contact haye register shodeh karbar
 *
 */

public class ContactList extends Activity {

    private ArrayList<Item>        listItem = new ArrayList<>();
    private ListView               registeredlv;
    private EditText               edtsearch;
    private ImageLoader1           il;
    private TextView               txt_contact;
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
        setContentView(R.layout.activity_contact_list);

        connectionServiceBound = new ConnectionServiceBound(ContactList.this);
        il = new ImageLoader1(ContactList.this, G.basicAuth);

        Button btnsearchicon = (Button) findViewById(R.id.btn_searchicon);
        btnsearchicon.setTypeface(G.fontAwesome);

        Button btnback = (Button) findViewById(R.id.btn_back);
        btnback.setTypeface(G.fontAwesome);

        txt_contact = (TextView) findViewById(R.id.textView2);
        txt_contact.setTypeface(G.robotoBold);

        edtsearch = (EditText) findViewById(R.id.edt_search);

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

        try {
            Cursor cu = G.cmd.getallContacts("1");
            if (cu != null) {
                while (cu.moveToNext()) {

                    Item item = new Item();
                    item.name = cu.getString(1);
                    item.mobile = cu.getString(2);
                    item.avatar_lq = cu.getString(4);
                    item.avatar_hq = cu.getString(5);
                    item.lastSeen = cu.getString(6);
                    item.uid = cu.getString(7);

                    listItem.add(item);

                }
                cu.close();
            }
        }
        catch (Exception e1) {}

        registeredlv = (ListView) findViewById(R.id.registeredlv);

        try {
            if (listItem.size() > 0)
                registeredlv.setAdapter(new AA(listItem));
        }
        catch (Exception e) {}

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                ArrayList<String> s = new ArrayList<>();
                for (int i = 0; i < listItem.size(); i++) {
                    s.add(listItem.get(i).mobile);
                }

                connectionServiceBound.getLastSeen(s, "ContactList");

            }
        }, 2000);

    }


    @Override
    protected void onResume() {
        Utils.checkLanguage(this);
        LocalBroadcastManager.getInstance(ContactList.this).registerReceiver(notifyAddContacts, new IntentFilter("notifyAddContacts"));
        LocalBroadcastManager.getInstance(ContactList.this).registerReceiver(UpdateLastContactList, new IntentFilter("UpdateLastContactList"));
        super.onResume();
        G.appIsShowing = true;
    }


    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(ContactList.this).unregisterReceiver(notifyAddContacts);
        LocalBroadcastManager.getInstance(ContactList.this).unregisterReceiver(UpdateLastContactList);

        try {
            connectionServiceBound.closeConnection();
        }
        catch (Exception e) {
            Log.e(" contact list   error  ", e.toString());
        }

        super.onPause();
        G.appIsShowing = false;
    }

    private BroadcastReceiver notifyAddContacts     = new BroadcastReceiver() {

                                                        @Override
                                                        public void onReceive(Context context, Intent intent) {

                                                            finish();
                                                            startActivity(getIntent());
                                                        }
                                                    };

    private BroadcastReceiver UpdateLastContactList = new BroadcastReceiver() {

                                                        @Override
                                                        public void onReceive(Context context, Intent intent) {

                                                            String lastSeen = intent.getStringExtra("lastSeen");
                                                            try {
                                                                JSONObject json = new JSONObject(lastSeen);

                                                                for (int i = 0; i < listItem.size(); i++) {
                                                                    String lasts = json.get(listItem.get(i).mobile).toString();

                                                                    lasts = HelperGetTime.getStringTime(lasts, G.cmd);

                                                                    listItem.get(i).lastSeen = lasts;
                                                                    G.cmd.setLastSeen(listItem.get(i).mobile, lasts);
                                                                }

                                                                runOnUiThread(new Runnable() {

                                                                    public void run() {

                                                                        ((BaseAdapter) registeredlv.getAdapter()).notifyDataSetChanged();
                                                                    }
                                                                });

                                                            }
                                                            catch (NullPointerException | JSONException e) {
                                                                e.printStackTrace();
                                                            }

                                                        }
                                                    };


    private class Item {

        String name;
        String mobile;
        String avatar_lq;
        String avatar_hq;
        String lastSeen;
        String uid;
    }


    private void search(String name) {

        try {
            ArrayList<Item> subList = new ArrayList<>();

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


    class AA extends ArrayAdapter<Item> {

        private ArrayList<Item> list;


        public AA(ArrayList<Item> list) {
            super(ContactList.this, R.layout.list_item_allcontacts, list);
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
            ImageView img_avatar = (ImageView) row.findViewById(R.id.img_avatar);

            txtname.setText(list.get(position).name);
            txtdesc.setText(list.get(position).mobile);
            txtlastseen.setText(list.get(position).lastSeen);

            row.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ContactList.this, UserProfile.class);
                    intent.putExtra("chatroomid", "empty");
                    intent.putExtra("userchat", list.get(position).mobile + "@igap.im");
                    intent.putExtra("userchatname", list.get(position).name);
                    intent.putExtra("userchatavatar", list.get(position).avatar_lq);
                    startActivity(intent);
                    finish();

                }
            });

            if (list.get(position).avatar_lq != null && !list.get(position).avatar_lq.isEmpty() && !list.get(position).avatar_lq.equals("null") && !list.get(position).avatar_lq.equals("")) {
                il.DisplayImage(list.get(position).avatar_lq, R.drawable.difaultimage, img_avatar);
            } else {
                if (list.get(position).name != null)
                    if (list.get(position).name.length() > 0) {

                        HelperDrawAlphabet pf = new HelperDrawAlphabet();
                        Bitmap bm = pf.drawAlphabet(ContactList.this, list.get(position).name, img_avatar);
                        img_avatar.setImageBitmap(bm);

                    } else {
                        Bitmap bm = G.utileProg.drawAlphabetOnPicture(img_avatar.getLayoutParams().width, "", "");
                        img_avatar.setImageBitmap(bm);
                    }
            }

            return (row);
        }
    }

}
