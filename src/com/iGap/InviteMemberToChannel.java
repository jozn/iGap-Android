// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.iGap.adapter.G;
import com.iGap.adapter.customAdapterList;
import com.iGap.helpers.HelperGetTime;
import com.iGap.instruments.ConnectionServiceBound;
import com.iGap.instruments.JSONParser;
import com.iGap.instruments.Utils;


/**
 * 
 * ezafe kardan ozve adi be kanal
 *
 */

public class InviteMemberToChannel extends Activity {

    private ArrayList<String>      contactnameregistered      = new ArrayList<String>();
    private ArrayList<String>      contactmobileregistered    = new ArrayList<String>();
    private ArrayList<String>      contactavatar_lqregistered = new ArrayList<String>();
    private ArrayList<String>      contactavatar_hqregistered = new ArrayList<String>();
    private ArrayList<String>      lastseenregistered         = new ArrayList<String>();
    private ArrayList<String>      uidregistered              = new ArrayList<String>();
    private ArrayList<String>      selectedList               = new ArrayList<String>();
    private ArrayList<Boolean>     check                      = new ArrayList<Boolean>();

    private String                 basicAuth, channeluid;

    private customAdapterList      customAdapterList;
    private ListView               registeredlv;
    private JSONArray              array;
    private TextView               txt_contact;
    private EditText               edt_search;
    private Dialog                 pDialog;
    private ConnectionServiceBound connectionServiceBound;
    private JSONParser             jParser                    = new JSONParser();


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Utils.checkLanguage(this);
        super.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.checkLanguage(this);
        setContentView(R.layout.activity_invite_member_to_channel);

        connectionServiceBound = new ConnectionServiceBound(InviteMemberToChannel.this);
        edt_search = (EditText) findViewById(R.id.edt_search);
        txt_contact = (TextView) findViewById(R.id.textView2);
        txt_contact.setTypeface(G.robotoBold);

        Button btnsearchicon = (Button) findViewById(R.id.btn_searchicon);
        btnsearchicon.setTypeface(G.fontAwesome);
        btnsearchicon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (txt_contact.getVisibility() == View.VISIBLE) {

                    AlphaAnimation alpha = new AlphaAnimation(1, 0);
                    alpha.setDuration(200);
                    txt_contact.setAnimation(alpha);
                    txt_contact.setVisibility(View.GONE);
                    edt_search.setVisibility(View.VISIBLE);

                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInputFromWindow(edt_search.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);

                    edt_search.requestFocus();

                    ScaleAnimation scale = new ScaleAnimation(0, 1f, 1f, 1f, 1, 1f, 1, 1f);
                    scale.setDuration(200);
                    edt_search.setAnimation(scale);

                } else {
                    ScaleAnimation scale = new ScaleAnimation(1f, 0, 1f, 1f, 1, 1f, 1, 1f);
                    scale.setDuration(50);
                    edt_search.setAnimation(scale);
                    edt_search.setVisibility(View.GONE);
                    edt_search.clearFocus();
                    txt_contact.setVisibility(View.VISIBLE);
                    AlphaAnimation alpha = new AlphaAnimation(0, 1);
                    alpha.setDuration(200);
                    alpha.setStartOffset(100);
                    txt_contact.setAnimation(alpha);

                }

            }
        });

        Button btnback = (Button) findViewById(R.id.btn_back);
        btnback.setTypeface(G.fontAwesome);

        Button btnSend = (Button) findViewById(R.id.button1);
        btnSend.setTypeface(G.fontAwesome);

        String add = getString(R.string.add_en);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {// allcaps is for api 14 and upper
            btnSend.setAllCaps(false);
        } else {
            add = add.substring(0, 1).toUpperCase() + add.substring(1).toLowerCase();
        }
        btnSend.setText(add);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            channeluid = extras.getString("channeluid");
        }

        edt_search.addTextChangedListener(new TextWatcher() {

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

        try {
            Cursor cu = G.cmd.getallContacts("1");
            if (cu != null) {
                while (cu.moveToNext()) {

                    contactnameregistered.add(cu.getString(1));
                    contactmobileregistered.add(cu.getString(2));
                    contactavatar_lqregistered.add(cu.getString(4));
                    contactavatar_hqregistered.add(cu.getString(5));
                    lastseenregistered.add(cu.getString(6));
                    uidregistered.add(cu.getString(7));
                    check.add(false);

                }
                cu.close();
            }
        }
        catch (Exception e1) {}

        registeredlv = (ListView) findViewById(R.id.registeredlv);

        try {
            if (check.size() > 0) {
                customAdapterList = new customAdapterList(InviteMemberToChannel.this, contactavatar_lqregistered, contactnameregistered, check, lastseenregistered);
                registeredlv.setAdapter(customAdapterList);
            }
        }
        catch (Exception e) {}

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                connectionServiceBound.getLastSeen(contactmobileregistered, "InviteMemberToChannel");

            }
        }, 2000);

        array = new JSONArray();

        registeredlv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Boolean state = customAdapterList.checks.get(position);
                customAdapterList.checks.set(position, !state);
                customAdapterList.animationState = position;
                customAdapterList.notifyDataSetChanged();

            }
        });

        Button btnaddmember = (Button) findViewById(R.id.button1);
        btnaddmember.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                selectedList.clear();

                if (check.size() > 0) {
                    for (int j = 0; j < customAdapterList.checks.size(); j++) {
                        if (customAdapterList.checks.get(j) == true) {
                            array.put(contactmobileregistered.get(j));
                        }
                    }
                    new SendNumbers().execute();
                }
            }
        });

    }


    private void search(String name) {

        ArrayList<String> names = new ArrayList<String>();
        ArrayList<String> avatar_lq = new ArrayList<String>();
        ArrayList<Boolean> checks = new ArrayList<Boolean>();

        try {
            if (name != null) {
                if (name.trim().length() > 0) {
                    String s = name.trim().toLowerCase();
                    for (int i = 0; i < contactnameregistered.size(); i++) {

                        if (contactnameregistered.get(i).trim().toLowerCase().startsWith(s)) {
                            names.add(contactnameregistered.get(i));
                            avatar_lq.add(contactavatar_lqregistered.get(i));
                            checks.add(check.get(i));
                        }
                    }
                    customAdapterList = new customAdapterList(InviteMemberToChannel.this, avatar_lq, names, checks, lastseenregistered);
                    registeredlv.setAdapter(customAdapterList);

                } else {
                    customAdapterList = new customAdapterList(InviteMemberToChannel.this, contactavatar_lqregistered, contactnameregistered, check, lastseenregistered);
                    registeredlv.setAdapter(customAdapterList);
                }

            } else {
                customAdapterList = new customAdapterList(InviteMemberToChannel.this, contactavatar_lqregistered, contactnameregistered, check, lastseenregistered);
                registeredlv.setAdapter(customAdapterList);
            }
        }
        catch (Exception e) {}

    }


    @Override
    protected void onDestroy() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        Utils.checkLanguage(this);
        LocalBroadcastManager.getInstance(InviteMemberToChannel.this).registerReceiver(UpdateLastInviteMemberToChannel, new IntentFilter("UpdateLastInviteMemberToChannel"));
        super.onResume();
        G.appIsShowing = true;
    }


    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(InviteMemberToChannel.this).unregisterReceiver(UpdateLastInviteMemberToChannel);
        connectionServiceBound.closeConnection();
        super.onPause();
        G.appIsShowing = false;
    }

    private BroadcastReceiver UpdateLastInviteMemberToChannel = new BroadcastReceiver() {

                                                                  @Override
                                                                  public void onReceive(Context context, Intent intent) {

                                                                      String lastSeen = intent.getStringExtra("lastSeen");
                                                                      try {
                                                                          JSONObject json = new JSONObject(lastSeen);

                                                                          for (int i = 0; i < contactmobileregistered.size(); i++) {
                                                                              String lasts = json.get(contactmobileregistered.get(i)).toString();

                                                                              lasts = HelperGetTime.getStringTime(lasts, G.cmd);

                                                                              lastseenregistered.set(i, lasts);
                                                                              G.cmd.setLastSeen(contactmobileregistered.get(i), lasts);

                                                                          }

                                                                          runOnUiThread(new Runnable() {

                                                                              public void run() {

                                                                                  customAdapterList.notifyDataSetChanged();
                                                                              }
                                                                          });

                                                                      }
                                                                      catch (JSONException e) {

                                                                      }

                                                                  }
                                                              };


    class SendNumbers extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new Dialog(InviteMemberToChannel.this);
            pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(pDialog.getWindow().getAttributes());
            layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            pDialog.getWindow().setAttributes(layoutParams);
            pDialog.setCancelable(false);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
                pDialog.setContentView(R.layout.dialog_wait);
            } else {
                pDialog.setContentView(R.layout.dialog_wait_simple);
            }
            pDialog.show();
        }


        @Override
        protected String doInBackground(String... args) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                JSONObject jsonobj = jParser.getJSONFromUrl(G.addmembertochannel + channeluid + "/invite", params, "BODY", basicAuth, array.toString());

                try {
                    String statuscode = jsonobj.getString("statuscode");
                    String jsonstring = jsonobj.getString("json");

                    JSONObject json = new JSONObject(jsonstring);

                    Boolean success = json.getBoolean("success");

                    if (success) {

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(InviteMemberToChannel.this, getString(R.string.invite_successful_en), Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });

                    } else {

                        if (statuscode.equals("403")) {
                            String errorStatus = "";
                            JSONObject result = json.getJSONObject("result");
                            errorStatus = result.getString("errorStatus");
                            if (errorStatus.equals("1")) {
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        Toast.makeText(InviteMemberToChannel.this, getString(R.string.channel_deleted), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else if (errorStatus.equals("2")) {
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        Toast.makeText(InviteMemberToChannel.this, getString(R.string.channel_closed), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else if (errorStatus.equals("3")) {
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        Toast.makeText(InviteMemberToChannel.this, getString(R.string.channel_invite_member_limitation), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else if (errorStatus.equals("4")) {
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        Toast.makeText(InviteMemberToChannel.this, getString(R.string.channel_invite_member_limitation), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        Toast.makeText(InviteMemberToChannel.this, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        } else {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(InviteMemberToChannel.this, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
                catch (final JSONException e) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(InviteMemberToChannel.this, getString(R.string.Error_occurred_en) + " : " + e, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            catch (final Exception e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(InviteMemberToChannel.this, getString(R.string.internet_connection_problem_en) + " : " + e, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            return null;
        }


        @Override
        protected void onPostExecute(String file_url) {

            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }

        }
    }

}
