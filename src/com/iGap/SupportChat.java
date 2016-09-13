// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap;

import java.util.ArrayList;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.iGap.adapter.G;
import com.iGap.instruments.LocalBinder;
import com.iGap.instruments.SupportRecycleAdapter;
import com.iGap.instruments.Utils;
import com.iGap.services.MyService;
import com.iGap.services.TimerServies;


/**
 * 
 * braye chat ba modiriat va tolid konandegan barnameh
 *
 */

public class SupportChat extends Activity {

    private ArrayList<String>          msgarray     = new ArrayList<String>();
    private ArrayList<String>          statusarray  = new ArrayList<String>();
    private ArrayList<String>          msgtimearray = new ArrayList<String>();
    private ArrayList<String>          msgtypearray = new ArrayList<String>();
    private ArrayList<String>          msgidarray   = new ArrayList<String>();

    private String                     chatmsg;
    private String                     userchat;
    private String                     day          = "";
    private String                     month        = "";
    private String                     year         = "";

    private boolean                    inActivity   = false, mBounded;

    private Button                     backIcon, navIcon;
    private EditText                   edtchat;
    private MyService                  mService;
    private RecyclerView               singlechatlist;
    private RecyclerView.LayoutManager mLayoutManager;
    private PopupWindow                popUp;
    private Handler                    handler;
    private SupportRecycleAdapter      mAdapter;
    private InputMethodManager         imm          = null;
    private TimerServies               ts           = new TimerServies();

    private final ServiceConnection    mConnection  = new ServiceConnection() {

                                                        @SuppressWarnings("unchecked")
                                                        @Override
                                                        public void onServiceConnected(final ComponentName name, final IBinder service) {
                                                            mService = ((LocalBinder<MyService>) service).getService();
                                                            mBounded = true;
                                                        }


                                                        @Override
                                                        public void onServiceDisconnected(final ComponentName name) {
                                                            mService = null;
                                                            mBounded = false;
                                                        }
                                                    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.checkLanguage(this);
        setContentView(R.layout.support_chat);
        inActivity = true;

        doBindService();

        userchat = "support@igap.im";
        G.mainUserChat = userchat;

        handler = new Handler();

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                mService.createchattest(userchat);
            }
        }, 1000);

        String currenttime;
        try {
            currenttime = ts.getDateTime();
        }
        catch (Exception e) {
            currenttime = G.helperGetTime.getTime();
        }
        String[] splitedtime = currenttime.split("\\s+");
        String date = splitedtime[0];
        String[] splited_date = date.split("-");
        year = splited_date[0];
        month = splited_date[1];
        day = splited_date[2];

        backIcon = (Button) findViewById(R.id.back_icon);

        navIcon = (Button) findViewById(R.id.nav_icon);

        TextView usernametx = (TextView) findViewById(R.id.user_name_tx);
        usernametx.setTypeface(G.robotoBold);

        Button btnsend = (Button) findViewById(R.id.btn_send);

        edtchat = (EditText) findViewById(R.id.edt_chat);

        singlechatlist = (RecyclerView) findViewById(R.id.singlechatlist);
        singlechatlist.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(SupportChat.this);
        singlechatlist.setLayoutManager(mLayoutManager);

        backIcon.setTypeface(G.fontAwesome);
        navIcon.setTypeface(G.fontAwesome);

        btnsend.setTypeface(G.fontAwesome);

        backIcon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                View view = SupportChat.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                finish();
            }
        });

        btnsend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                chatmsg = edtchat.getText().toString();

                if (chatmsg != null && !chatmsg.isEmpty() && !chatmsg.equals("null") && !chatmsg.equals("")) {

                    if (closingkeybord() == true) {

                        mService.sendsupport(chatmsg);
                    }

                } else {
                    Toast.makeText(SupportChat.this, getString(R.string.your_message_is_empty_en), Toast.LENGTH_SHORT).show();
                }

            }
        });

        loadchathistory();
        LocalBroadcastManager.getInstance(SupportChat.this).registerReceiver(mMessageReceiver, new IntentFilter("loadchathistory"));
        LocalBroadcastManager.getInstance(SupportChat.this).registerReceiver(NewPostSupport, new IntentFilter("NewPostSupport"));
    }

    private BroadcastReceiver NewPostSupport = new BroadcastReceiver() {

                                                 @Override
                                                 public void onReceive(Context context, Intent intent) {

                                                     String msg = intent.getStringExtra("msg");
                                                     String status = intent.getStringExtra("status");
                                                     String msgtime = intent.getStringExtra("msgtime");
                                                     String msgtype = intent.getStringExtra("msgtype");
                                                     String msgid = intent.getStringExtra("msgid");

                                                     mAdapter.newPost(msg, status, msgtime, msgtype, msgid);
                                                     singlechatlist.scrollToPosition(msgarray.size() - 1);
                                                 }
                                             };


    private boolean closingkeybord() {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                edtchat.getText().clear();
                edtchat.clearFocus();
                View view = SupportChat.this.getCurrentFocus();
                if (view != null) {
                    imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });

        if (imm.isAcceptingText()) {
            return false;
        } else {
            return true;
        }

    }


    private void loadchathistory() {

        int size = G.cmd.getsupporthistoryRowCount();

        if (size != 0) {

            try {
                msgarray.clear();
                statusarray.clear();
                msgtimearray.clear();
                msgtypearray.clear();
                msgidarray.clear();
            }
            catch (Exception e) {}
            singlechatlist.setVisibility(View.VISIBLE);

            Cursor cursorSingleChat = G.cmd.selectSupportHistory();

            while (cursorSingleChat.moveToNext()) {
                String msgid = cursorSingleChat.getString(cursorSingleChat.getColumnIndex("msg_id"));
                String msg = cursorSingleChat.getString(cursorSingleChat.getColumnIndex("msg"));
                String status = cursorSingleChat.getString(cursorSingleChat.getColumnIndex("status"));
                String msgtime = cursorSingleChat.getString(cursorSingleChat.getColumnIndex("msg_time"));
                String msgtype = cursorSingleChat.getString(cursorSingleChat.getColumnIndex("msg_type"));
                msgarray.add(msg);
                statusarray.add(status);
                msgtimearray.add(msgtime);
                msgtypearray.add(msgtype);
                msgidarray.add(msgid);

            }
            cursorSingleChat.close();
        }

        if (inActivity) {
            sendseen();
        }

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                mAdapter = new SupportRecycleAdapter(msgarray, statusarray, msgtimearray, msgtypearray, msgidarray, SupportChat.this, year, month, day);
                singlechatlist.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                singlechatlist.scrollToPosition(msgarray.size() - 1);

            }
        });
    }


    private void sendseen() {

        handler.post(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < msgtypearray.size(); i++) {
                    if (msgtypearray.get(i).equals("1")) {
                        if ( !statusarray.get(i).equals("5")) {
                            if (msgidarray.get(i) != null && !msgidarray.get(i).isEmpty() && !msgidarray.get(i).equals("null") && !msgidarray.get(i).equals("")) {
                                G.cmd.updatemsgstatusSupport(msgidarray.get(i), "5");
                            }
                        }
                    }
                }

                if (userchat != null) {
                    Intent intentAll = new Intent("updateSeenSupportChat"); // send to MainActivity
                    LocalBroadcastManager.getInstance(SupportChat.this).sendBroadcast(intentAll);
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (popUp != null && popUp.isShowing()) {
            popUp.dismiss();
        }
        doUnbindService();

    }


    void doBindService() {
        bindService(new Intent(this, MyService.class), mConnection, Context.BIND_AUTO_CREATE);

    }


    void doUnbindService() {
        if (mBounded) {
            unbindService(mConnection);
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

                                                   @Override
                                                   public void onReceive(Context context, Intent intent) {
                                                       loadchathistory();
                                                   }
                                               };


    @Override
    protected void onResume() {
        Utils.checkLanguage(this);
        super.onResume();
        inActivity = true;
        G.appIsShowing = true;
    }


    @Override
    protected void onPause() {
        super.onPause();
        inActivity = false;
        G.appIsShowing = false;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Utils.checkLanguage(this);
        super.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onStart() {
        super.onStart();
        inActivity = true;
    }


    @Override
    protected void onStop() {
        super.onStop();
        inActivity = false;
    }
}
