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
 * braye ezafe kardan ozve be yek groh
 *
 */

public class AddMemberToGroup extends Activity {

    private ArrayList<String>      contactnameregistered      = new ArrayList<String>();
    private ArrayList<String>      contactmobileregistered    = new ArrayList<String>();
    private ArrayList<String>      contactavatar_lqregistered = new ArrayList<String>();
    private ArrayList<String>      contactavatar_hqregistered = new ArrayList<String>();
    private ArrayList<String>      lastseenregistered         = new ArrayList<String>();
    private ArrayList<String>      uidregistered              = new ArrayList<String>();
    private ArrayList<Boolean>     check                      = new ArrayList<Boolean>();

    private customAdapterList      customAdapterList;
    private ListView               registeredlv;
    private JSONArray              array;
    private String                 gchmembership, id, name, groupdesc, active;
    private TextView               txt_contact;
    private ConnectionServiceBound connectionServiceBound;
    private Dialog                 pDialog;
    private int                    failedNumbers, numberOfMembers;
    private JSONParser             jParser                    = new JSONParser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.checkLanguage(this);
        setContentView(R.layout.activity_add_member_to_gruop);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getString("id");
            name = extras.getString("name");
            groupdesc = extras.getString("groupdesc");
            gchmembership = extras.getString("gchmembership");
        }

        connectionServiceBound = new ConnectionServiceBound(AddMemberToGroup.this);

        active = G.cmd.selectActive(10, id);

        final EditText edtsearch = (EditText) findViewById(R.id.edt_search);

        edtsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search(s.toString());
            }


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}


            @Override
            public void afterTextChanged(Editable s) {}
        });

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

        Button btnback = (Button) findViewById(R.id.btn_back);
        btnback.setTypeface(G.fontAwesome);

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
            }
            cu.close();
        }
        catch (Exception e1) {}

        registeredlv = (ListView) findViewById(R.id.registeredlv);

        try {
            if (check.size() > 0) {
                customAdapterList = new customAdapterList(AddMemberToGroup.this, contactavatar_lqregistered, contactnameregistered, check, lastseenregistered);
                registeredlv.setAdapter(customAdapterList);
            }
        }
        catch (Exception e) {}

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                connectionServiceBound.getLastSeen(contactmobileregistered, "AddMemberToGroup");

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

        Button btnaddmember = (Button) findViewById(R.id.button_add_member);
        String add = getString(R.string.add_en);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {// allcaps is for api 14 and upper
            btnaddmember.setAllCaps(false);
        } else {
            add = add.substring(0, 1).toUpperCase() + add.substring(1).toLowerCase();
        }
        btnaddmember.setText(add);

        btnaddmember.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                numberOfMembers = 0;

                if (check.size() > 0) {
                    for (int j = 0; j < customAdapterList.checks.size(); j++) {
                        if (customAdapterList.checks.get(j) == true) {
                            numberOfMembers++;
                            array.put(contactmobileregistered.get(j));
                        }
                    }

                    new LoadAllgoroh().execute();

                } else {
                    Toast.makeText(AddMemberToGroup.this, R.string.dont_have_registered_en, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Utils.checkLanguage(this);
        super.onConfigurationChanged(newConfig);
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
                    customAdapterList = new customAdapterList(AddMemberToGroup.this, avatar_lq, names, checks, lastseenregistered);
                    registeredlv.setAdapter(customAdapterList);

                } else {
                    customAdapterList = new customAdapterList(AddMemberToGroup.this, contactavatar_lqregistered, contactnameregistered, check, lastseenregistered);
                    registeredlv.setAdapter(customAdapterList);
                }

            } else {
                customAdapterList = new customAdapterList(AddMemberToGroup.this, contactavatar_lqregistered, contactnameregistered, check, lastseenregistered);
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
        LocalBroadcastManager.getInstance(AddMemberToGroup.this).registerReceiver(UpdateLastSeenAddMemberToGroup, new IntentFilter("UpdateLastSeenAddMemberToGroup"));
        super.onResume();
        G.appIsShowing = true;
    }


    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(AddMemberToGroup.this).unregisterReceiver(UpdateLastSeenAddMemberToGroup);
        connectionServiceBound.closeConnection();
        super.onPause();
        G.appIsShowing = false;
    }

    private BroadcastReceiver UpdateLastSeenAddMemberToGroup = new BroadcastReceiver() {

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


    class LoadAllgoroh extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new Dialog(AddMemberToGroup.this);
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

                String groupid = id.split("@")[0];

                JSONObject jsonobj = jParser.getJSONFromUrl(G.addmembertogroupchat + groupid + "/members", params, "BODY", G.basicAuth, array.toString());

                try {
                    String statuscode = jsonobj.getString("statuscode");
                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);

                    Boolean success = json.getBoolean("success");

                    if (success) {
                        JSONObject result = json.getJSONObject("result");
                        JSONArray faild = result.getJSONArray("failed");
                        failedNumbers = faild.length();

                    } else {

                        if (statuscode.equals("403")) {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(AddMemberToGroup.this, getString(R.string.chatroom_is_full_en), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(AddMemberToGroup.this, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                }
                catch (JSONException e) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(AddMemberToGroup.this, getString(R.string.internet_connection_problem_en), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            catch (Exception e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(AddMemberToGroup.this, getString(R.string.internet_connection_problem_en), Toast.LENGTH_SHORT).show();
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

            int membres = numberOfMembers - failedNumbers;

            if (membres < 1)
                membres = 1;

            G.cmd.updateGroupsMembers(id, membres + "");

            Intent intent = new Intent(AddMemberToGroup.this, GroupChat.class);
            intent.putExtra("gchid", id);
            intent.putExtra("gchname", name);
            intent.putExtra("gchavatar", "");
            intent.putExtra("gchdescription", groupdesc);
            intent.putExtra("gchmembership", gchmembership);
            intent.putExtra("gchactive", active);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
}
