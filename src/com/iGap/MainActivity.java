// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.iGap.adapter.ChannelAdapter;
import com.iGap.adapter.G;
import com.iGap.fragments.PageCall;
import com.iGap.fragments.PageMap;
import com.iGap.fragments.PageMessagingChats;
import com.iGap.fragments.PageTimeline;
import com.iGap.helpers.HelperGetDataFromOtherApp;
import com.iGap.helpers.HelperGetTime;
import com.iGap.instruments.JSONParser;
import com.iGap.instruments.LocalBinder;
import com.iGap.instruments.SampleActivityBase;
import com.iGap.instruments.SlidingTabsColorsFragment;
import com.iGap.instruments.Utils;
import com.iGap.interfaces.OnComplet;
import com.iGap.services.MyService;
import com.iGap.services.SplashService;
import com.iGap.services.TimerServies;


/**
 * 
 * class asly barnameh baraye namayesh list chat va grouh va kanal va menu
 *
 */

@SuppressWarnings("deprecation")
public class MainActivity extends SampleActivityBase {

    private String                  channelName;
    private String                  channelUid;
    private String                  channelDesc;
    private String                  time;
    private String                  groupdesc;
    private String                  groupname;
    private String                  iGapMessenger       = "";
    private String                  strColorNotActive   = "#f2b33d";
    private String                  strColorActive      = "#2bbfbd";

    private boolean                 mBounded;

    private Button                  btn_messaging, btn_timeline, btn_map, btn_call, btn_menu;
    private TextView                txtchatwhihsupportcount;
    private LinearLayout            drawer;

    public static Context           publicContext;

    private MyService               mService;
    private DrawerLayout            mDrawerLayout;
    private ActionBarDrawerToggle   mDrawerToggle;
    private Dialog                  dialog;
    private TimerServies            ts;
    private Dialog                  pDialog;
    private HelperGetTime           helperGetTime;
    private JSONParser              jParser             = new JSONParser();

    private final ServiceConnection mConnection         = new ServiceConnection() {

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

    private BroadcastReceiver       reciverDismisDialog = new BroadcastReceiver() {

                                                            @Override
                                                            public void onReceive(Context context, Intent intent) {

                                                                if (pDialog != null && pDialog.isShowing()) {
                                                                    pDialog.dismiss();
                                                                }

                                                                LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(reciverDismisDialog);

                                                            }
                                                        };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HelperGetDataFromOtherApp getData = new HelperGetDataFromOtherApp(getIntent());
        if ( !getData.continueClass()) {
            Intent intent2 = new Intent(G.context, Splash.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            G.context.startActivity(intent2);
            finish();
        }

        Utils.checkLanguage(this);
        setContentView(R.layout.activity_main);

        G.hashtakSearch = false;
        publicContext = MainActivity.this;

        String firstTime = getIntent().getStringExtra("firstTime");

        if (firstTime != null) {
            startService(new Intent(MainActivity.this, SplashService.class));
        }

        cancelNotification();

        helperGetTime = new HelperGetTime();

        pDialog = new Dialog(MainActivity.this);
        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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

        initDrawer();

        if ( !isMyServiceRunning(MyService.class)) {
            startService(new Intent(this, MyService.class));
        } else {

        }

        doBindService();

        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(reciverDismisDialog, new IntentFilter("Dismis_dialog"));

        pDialog.show();

        new Thread(new Runnable() {

            @Override
            public void run() {

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                SlidingTabsColorsFragment fragment_messaging = new SlidingTabsColorsFragment();

                transaction.replace(R.id.fr_fragments, fragment_messaging);
                transaction.commitAllowingStateLoss();

                PageMessagingChats.seenOtherFragment = false;

            }
        }).start();

        //**********************************************
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer = (LinearLayout) findViewById(R.id.ll_drawr);
        btn_messaging = (Button) findViewById(R.id.btn_messaging);
        btn_messaging.setTypeface(G.fontAwesome);
        iGapMessenger = getString(R.string.igap_messager_en);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {// allcaps is for api 14 and upper
            btn_messaging.setAllCaps(false);
        } else {
            iGapMessenger = iGapMessenger.substring(0, 1).toLowerCase() + iGapMessenger.substring(1, 2).toUpperCase() + iGapMessenger.substring(2, 5).toLowerCase() + iGapMessenger.substring(5, 6).toUpperCase() + iGapMessenger.substring(6).toLowerCase();
        }

        //   iGapMessenger = getString(R.string.fa_commenting_oww) + " " + iGapMessenger;

        btn_messaging.setText(iGapMessenger);

        btn_messaging.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                PageMessagingChats.seenOtherFragment = false;
                setAllButtonColor();
                btn_messaging.setBackgroundColor(Color.parseColor(strColorActive));
                findViewById(R.id.view_messaging).setVisibility(View.GONE);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                SlidingTabsColorsFragment fragment_messaging = new SlidingTabsColorsFragment();

                transaction.replace(R.id.fr_fragments, fragment_messaging);
                transaction.commit();

            }
        });

        btn_timeline = (Button) findViewById(R.id.btn_timeline);
        btn_timeline.setVisibility(View.GONE);
        btn_timeline.setTypeface(G.robotoLight);
        btn_timeline.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                PageMessagingChats.seenOtherFragment = false;
                setAllButtonColor();
                btn_timeline.setBackgroundColor(Color.parseColor(strColorActive));
                findViewById(R.id.view_timeline).setVisibility(View.GONE);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                PageTimeline fragment_Timeline = new PageTimeline();
                transaction.replace(R.id.fr_fragments, fragment_Timeline);
                transaction.commit();
            }
        });

        btn_map = (Button) findViewById(R.id.btn_map);
        btn_map.setVisibility(View.GONE);
        btn_map.setTypeface(G.robotoLight);
        btn_map.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                PageMessagingChats.seenOtherFragment = false;
                setAllButtonColor();
                btn_map.setBackgroundColor(Color.parseColor(strColorActive));
                findViewById(R.id.view_map).setVisibility(View.GONE);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                PageMap fragment_map = new PageMap();
                transaction.replace(R.id.fr_fragments, fragment_map);
                transaction.commit();
            }
        });

        btn_call = (Button) findViewById(R.id.btn_call);
        btn_call.setVisibility(View.GONE);
        btn_call.setTypeface(G.robotoLight);
        btn_call.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                PageMessagingChats.seenOtherFragment = false;
                setAllButtonColor();
                btn_call.setBackgroundColor(Color.parseColor(strColorActive));
                findViewById(R.id.view_call).setVisibility(View.GONE);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                PageCall fragment_call = new PageCall();
                transaction.replace(R.id.fr_fragments, fragment_call);
                transaction.commit();
            }
        });

        btn_menu = (Button) findViewById(R.id.btn_menu);
        btn_menu.setTypeface(G.fontAwesome);

        btn_menu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(drawer)) {
                    mDrawerLayout.closeDrawer(drawer);
                } else {

                    mDrawerLayout.openDrawer(drawer);
                }

            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.drawer, R.string.open_en, R.string.close_en) {

            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                supportInvalidateOptionsMenu(); // creates call to
            }


            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                supportInvalidateOptionsMenu(); // creates call to
            }
        };

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                if (hasActiveInternetConnection()) {
                    G.connectionState = "4";
                } else {
                    G.connectionState = "1";
                }
            }
        });
        thread.start();

        if (G.connectionState.equals("1")) {
            btn_messaging.setText(getString(R.string.waiting_for_network_en));
        } else if (G.connectionState.equals("2")) {
            btn_messaging.setText(getString(R.string.connecting_en));
        } else if (G.connectionState.equals("3")) {
            btn_messaging.setText(getString(R.string.updating_en));
        } else if (G.connectionState.equals("3")) {
            btn_messaging.setText(iGapMessenger);
        } else {
            btn_messaging.setText(iGapMessenger);
        }

        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(connectionstateReceiver, new IntentFilter("connectionstateReceiver"));
        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(updateSeenSupportChat, new IntentFilter("updateSeenSupportChat"));

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 100)
            {
                Intent i = getIntent();
                finish();
                startActivity(i);
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        try {
            if (mDrawerLayout.isDrawerOpen(drawer)) {
                mDrawerLayout.closeDrawer(drawer);
            } else {

                mDrawerLayout.openDrawer(drawer);
            }
        }
        catch (Exception e) {}

        return false;
    }


    public boolean hasActiveInternetConnection() {
        ConnectivityManager cm = ((ConnectivityManager) G.context.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(3000);

                urlc.connect();

                return (urlc.getResponseCode() == 200);
            }
            catch (IOException e) {

            }
        } else {

        }
        return false;
    }

    private BroadcastReceiver connectionstateReceiver = new BroadcastReceiver() {

                                                          @Override
                                                          public void onReceive(Context context, Intent intent) {
                                                              final String state = intent.getStringExtra("state");
                                                              runOnUiThread(new Runnable() {

                                                                  public void run() {

                                                                      if (state.equals("1")) {
                                                                          btn_messaging.setText(getString(R.string.waiting_for_network_en));
                                                                      } else if (state.equals("2")) {
                                                                          btn_messaging.setText(getString(R.string.connecting_en));
                                                                      } else if (state.equals("3")) {
                                                                          btn_messaging.setText(getString(R.string.updating_en));
                                                                      } else if (state.equals("4")) {
                                                                          btn_messaging.setText(iGapMessenger);
                                                                      } else {
                                                                          btn_messaging.setText(iGapMessenger);
                                                                      }

                                                                  }
                                                              });
                                                          }
                                                      };

    private BroadcastReceiver updateSeenSupportChat   = new BroadcastReceiver() {

                                                          @Override
                                                          public void onReceive(Context context, Intent intent) {
                                                              runOnUiThread(new Runnable() {

                                                                  @Override
                                                                  public void run() {
                                                                      updateUnread();
                                                                  }
                                                              });
                                                          }
                                                      };


    private void updateUnread() {
        int unread = G.cmd.supportunreadcount();
        if (unread == 0) {
            txtchatwhihsupportcount.setVisibility(View.GONE);
        } else {
            txtchatwhihsupportcount.setVisibility(View.VISIBLE);
            txtchatwhihsupportcount.setText("" + unread);
        }
    }


    public static void cancelNotification() {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) G.context.getSystemService(ns);
        nMgr.cancel(G.NOTIFICATION_ID);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Utils.checkLanguage(this);
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    private void setAllButtonColor() {

        btn_messaging.setBackgroundColor(Color.parseColor(strColorNotActive));
        btn_timeline.setBackgroundColor(Color.parseColor(strColorNotActive));
        btn_map.setBackgroundColor(Color.parseColor(strColorNotActive));
        btn_call.setBackgroundColor(Color.parseColor(strColorNotActive));

        findViewById(R.id.view_messaging).setVisibility(View.VISIBLE);
        findViewById(R.id.view_map).setVisibility(View.VISIBLE);
        findViewById(R.id.view_timeline).setVisibility(View.VISIBLE);
        findViewById(R.id.view_call).setVisibility(View.VISIBLE);

    }


    private void initDrawer() {

        TextView txtDashboard = (TextView) findViewById(R.id.txt_dashboard);
        txtDashboard.setTypeface(G.robotoBold);

        TextView txtCreateChate = (TextView) findViewById(R.id.txt_create_chate);
        txtCreateChate.setTypeface(G.robotoLight);

        TextView txtCreateGroup = (TextView) findViewById(R.id.txt_create_group);
        txtCreateGroup.setTypeface(G.robotoLight);

        TextView txtCreateChanel = (TextView) findViewById(R.id.txt_create_chanel);
        txtCreateChanel.setTypeface(G.robotoLight);

        TextView txtContactList = (TextView) findViewById(R.id.txt_contact_list);
        txtContactList.setTypeface(G.robotoLight);

        TextView txtInviteFriend = (TextView) findViewById(R.id.txt_invite_friend);
        txtInviteFriend.setTypeface(G.robotoLight);

        TextView txtSetting = (TextView) findViewById(R.id.txt_setting);
        txtSetting.setTypeface(G.robotoLight);

        TextView txtPrivacy = (TextView) findViewById(R.id.txt_privacy);
        txtPrivacy.setTypeface(G.robotoLight);

        TextView txtFaq = (TextView) findViewById(R.id.txt_faq);
        txtFaq.setTypeface(G.robotoLight);

        TextView txtsearchchanel = (TextView) findViewById(R.id.txt_search_chanel);
        txtsearchchanel.setTypeface(G.robotoLight);

        TextView txtChatWithSupport = (TextView) findViewById(R.id.txt_chat_whih_support);
        txtChatWithSupport.setTypeface(G.robotoLight);

        txtchatwhihsupportcount = (TextView) findViewById(R.id.txt_chat_whih_support_count);
        txtchatwhihsupportcount.setTypeface(G.robotoLight);

        int unread = G.cmd.supportunreadcount();
        if (unread == 0) {
            txtchatwhihsupportcount.setVisibility(View.GONE);
        } else {
            txtchatwhihsupportcount.setText("" + unread);
        }

        TextView txtIconChatWithSupport = (TextView) findViewById(R.id.txt_icon_chat_whih_support);
        txtIconChatWithSupport.setTypeface(G.fontAwesome);

        LinearLayout llChatWithSupport = (LinearLayout) findViewById(R.id.ll_drawer_chat_whih_support);
        llChatWithSupport.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                mDrawerLayout.closeDrawer(drawer);
                Intent intent = new Intent(MainActivity.this, SupportChat.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        TextView txtDashboardIcon = (TextView) findViewById(R.id.txt_dashboard_icon);
        txtDashboardIcon.setTypeface(G.fontAwesome);

        TextView txtCreateChatIcon = (TextView) findViewById(R.id.txt_create_chat_icon);
        txtCreateChatIcon.setTypeface(G.fontAwesome);

        TextView txtCreateGroupIcon = (TextView) findViewById(R.id.txt_crate_group_icon);
        txtCreateGroupIcon.setTypeface(G.fontAwesome);

        TextView txtCreateChanleIcon = (TextView) findViewById(R.id.txt_create_chanel_icon);
        txtCreateChanleIcon.setTypeface(G.fontAwesome);

        TextView txtContactListIcon = (TextView) findViewById(R.id.txt_contact_icon);
        txtContactListIcon.setTypeface(G.fontAwesome);

        TextView txtInviteFriendIcon = (TextView) findViewById(R.id.txt_invite_friend_icon);
        txtInviteFriendIcon.setTypeface(G.fontAwesome);

        TextView txtSettingIcon = (TextView) findViewById(R.id.txt_setting_icon);
        txtSettingIcon.setTypeface(G.fontAwesome);

        TextView txtPrivacyIcon = (TextView) findViewById(R.id.txt_privacy_icon);
        txtPrivacyIcon.setTypeface(G.fontAwesome);

        TextView txtFaqIcon = (TextView) findViewById(R.id.txt_faq_icon);
        txtFaqIcon.setTypeface(G.fontAwesome);

        TextView txtsearchchanelicon = (TextView) findViewById(R.id.txt_search_chanel_icon);
        txtsearchchanelicon.setTypeface(G.fontAwesome);

        LinearLayout lldrawercreatechat = (LinearLayout) findViewById(R.id.ll_drawer_createchat);
        LinearLayout lldrawercreategroup = (LinearLayout) findViewById(R.id.ll_drawer_creategroup);
        LinearLayout lldrawercreatechannel = (LinearLayout) findViewById(R.id.ll_drawer_createchannel);
        LinearLayout lldrawercontactlist = (LinearLayout) findViewById(R.id.ll_drawer_contactlist);
        LinearLayout lldrawerinvitefriends = (LinearLayout) findViewById(R.id.ll_drawer_invitefriends);
        LinearLayout lldrawersetting = (LinearLayout) findViewById(R.id.ll_drawer_setting);
        LinearLayout lldrawersearchchannel = (LinearLayout) findViewById(R.id.ll_drawer_searchchannel);

        LinearLayout lldrawerprivacy = (LinearLayout) findViewById(R.id.ll_drawer_privacy);

        LinearLayout lldrawerfaq = (LinearLayout) findViewById(R.id.ll_drawer_faq);

        lldrawercreatechat.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(drawer);
                Intent intent = new Intent(MainActivity.this, SelectContactSingle.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        lldrawercreategroup.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(drawer);
                //new HelperDialog(MainActivity.this).dialogcreategroup(mService);
                dialogcreategroup();
            }
        });

        lldrawercreatechannel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(drawer);
                //new HelperDialog(MainActivity.this).dialogcreatechannels();
                dialogcreatechannels();
            }
        });

        lldrawercontactlist.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(drawer);
                Intent intent = new Intent(MainActivity.this, ContactList.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        lldrawerinvitefriends.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(drawer);
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey Join iGap : https://www.igap.im/iGap.apk");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        lldrawersetting.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(drawer);
                Intent intent = new Intent(MainActivity.this, Setting.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, 100);

            }
        });
        lldrawersearchchannel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(drawer);
                Intent intent = new Intent(MainActivity.this, ChannelSearch.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
        lldrawerprivacy.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(drawer);
                Intent intent = new Intent(MainActivity.this, Privacy.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        lldrawerfaq.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(drawer);

            }
        });

    }


    public static boolean tryActivityIntent(Context context, Intent activityIntent) {
        try {
            if (activityIntent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(activityIntent);
                return true;
            }
        }
        catch (SecurityException e) {
            return false;
        }
        return false;
    }


    @Override
    protected void onDestroy() {

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }

        super.onDestroy();
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


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }


    private void dialogcreategroup() {
        dialog = new Dialog(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_create_group, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(dialogView);
        ts = new TimerServies();

        final EditText edtgroupName = (EditText) dialogView.findViewById(R.id.edt_groupName);
        final EditText edtgroupDesc = (EditText) dialogView.findViewById(R.id.edt_groupDesc);

        TextView txtgroup = (TextView) dialogView.findViewById(R.id.txt_group);
        txtgroup.setTypeface(G.robotoBold);

        TextView txt_group_name = (TextView) dialogView.findViewById(R.id.txt_group_name);
        txt_group_name.setTypeface(G.robotoBold);

        TextView txt_group_text = (TextView) dialogView.findViewById(R.id.txt_group_text);
        txt_group_text.setTypeface(G.robotoLight);

        TextView txt_group_des = (TextView) dialogView.findViewById(R.id.txt_group_des);
        txt_group_des.setTypeface(G.robotoBold);

        Button btncreate = (Button) dialogView.findViewById(R.id.btn_create);
        btncreate.setTypeface(G.robotoBold);
        btncreate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                groupdesc = edtgroupDesc.getText().toString();
                groupname = edtgroupName.getText().toString();

                if (groupname != null && !groupname.isEmpty() && !groupname.equals("null") && !groupname.equals("")) {
                    if (groupdesc != null && !groupdesc.isEmpty() && !groupdesc.equals("null") && !groupdesc.equals("")) {

                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }

                        String currentTimea;
                        try {
                            currentTimea = ts.getDateTime();
                        }
                        catch (Exception e) {
                            currentTimea = helperGetTime.getTime();
                        }
                        time = currentTimea;
                        new creategroup().execute();

                    } else {
                        Toast.makeText(MainActivity.this, getString(R.string.please_enter_group_description_en), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.please_enter_group_name_en), Toast.LENGTH_SHORT).show();
                }

            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        lp.width = (display.getWidth());
        dialog.getWindow().setAttributes(lp);

        dialog.show();
    }


    class creategroup extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog.show();
        }


        @Override
        protected String doInBackground(String... args) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("name", groupname));
                params.add(new BasicNameValuePair("description", groupdesc));

                JSONObject jsonobj = jParser.getJSONFromUrl(G.creategroupchat, params, "POST", G.basicAuth, null);

                try {

                    String statuscode = jsonobj.getString("statuscode");
                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);
                    Boolean success = json.getBoolean(G.TAG_SUCCESS);

                    if (success == true) {

                        JSONObject result = json.getJSONObject("result");
                        String id = result.getString("id") + "@group.igap.im";

                        mService.joinroom(id, groupname, groupdesc, "", "", "You've created this group", time, "1");
                        G.cmd.Addgroupchat(id, groupname, "1", "1", "", "", "You've created this group", time, groupdesc);
                        G.cmd.addgroupchathistory("", id, "empty", "You Created this group", "1", time, "2", "empty", "empty", "100", null, null, null, G.username, "", "", "");
                        NewChannelSendToAll("2", id, groupname, groupdesc, "1", "", "You Created this group", time, "1");

                        Intent intent = new Intent(MainActivity.this, AddMemberToGroup.class);
                        intent.putExtra("id", id);
                        intent.putExtra("name", groupname);
                        intent.putExtra("groupdesc", groupdesc);
                        intent.putExtra("gchmembership", "1");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    } else {

                        if (statuscode.equals("400")) {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, getString(R.string.illegal_characters), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                }
                catch (JSONException e) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            catch (Exception e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
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


    private void dialogcreatechannels() {
        final Dialog dialog = new Dialog(MainActivity.this);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_create_channels, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(dialogView);

        final EditText edtchannelName = (EditText) dialogView.findViewById(R.id.edt_channelName);
        final EditText edtchannelDesc = (EditText) dialogView.findViewById(R.id.edt_channelDesc);
        final EditText edtchannelUid = (EditText) dialogView.findViewById(R.id.edt_channelUid);
        TextView txt_channel = (TextView) dialogView.findViewById(R.id.txt_channel);
        txt_channel.setTypeface(G.robotoBold);

        TextView txt_channel_text = (TextView) dialogView.findViewById(R.id.txt_channel_text);
        txt_channel_text.setTypeface(G.robotoBold);

        TextView txt_channel_name = (TextView) dialogView.findViewById(R.id.txt_channel_name);
        txt_channel_name.setTypeface(G.robotoBold);

        TextView txt_channel_des = (TextView) dialogView.findViewById(R.id.txt_channel_des);
        txt_channel_des.setTypeface(G.robotoBold);

        TextView txt_channel_nick_name = (TextView) dialogView.findViewById(R.id.txt_channel_nick_name);
        txt_channel_nick_name.setTypeface(G.robotoBold);
        Button btncreate = (Button) dialogView.findViewById(R.id.btn_create);
        btncreate.setTypeface(G.robotoBold);
        btncreate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                channelName = edtchannelName.getText().toString();
                channelDesc = edtchannelDesc.getText().toString();
                channelUid = edtchannelUid.getText().toString();

                if (channelName != null && !channelName.isEmpty() && !channelName.equals("null") && !channelName.equals("")) {
                    if (channelDesc != null && !channelDesc.isEmpty() && !channelDesc.equals("null") && !channelDesc.equals("")) {
                        if (channelUid != null && !channelUid.isEmpty() && !channelUid.equals("null") && !channelUid.equals("")) {

                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }

                            pDialog.show();
                            createChannel();

                        } else {
                            Toast.makeText(MainActivity.this, getString(R.string.please_enter_channel_uid_en), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(MainActivity.this, getString(R.string.please_enter_channel_description_en), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.please_enter_channel_name_en), Toast.LENGTH_SHORT).show();
                }
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        lp.width = (display.getWidth());
        dialog.getWindow().setAttributes(lp);

        dialog.show();
    }


    private void createChannel() {
        new ChannelAdapter(G.context).createChannel(channelUid, channelName, channelDesc, new OnComplet() {

            @Override
            public void complet(Boolean result, final String message) {

                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }

                if (result == false) {

                    runOnUiThread(new Runnable() {

                        public void run() {
                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {

                    String currentTimea;
                    try {
                        currentTimea = ts.getDateTime();
                    }
                    catch (Exception e) {
                        currentTimea = helperGetTime.getTime();
                    }
                    String currenttime = currentTimea;

                    //yani sakhte shod
                    G.cmd.addchannel(channelUid, channelName, channelDesc, "1", "", "", "you created this channel", currenttime, "0", "1");
                    G.cmd.addchannelhistory(channelUid, "first", "you created this channel", currenttime, "1", "", "1", "0", "1", "0", "");
                    NewChannel(channelUid, channelName, channelDesc, "1", "", "you created this channel", currenttime, "1");
                    NewChannelSendToAll("3", channelUid, channelName, channelDesc, "1", "", "you created this channel", currenttime, "1");
                    Intent intent = new Intent(MainActivity.this, Channel.class);
                    intent.putExtra("channeluid", channelUid);
                    intent.putExtra("channelName", channelName);
                    intent.putExtra("channelDesc", channelDesc);
                    intent.putExtra("channelavatarlq", "");
                    intent.putExtra("channelmembership", "1");
                    intent.putExtra("channelmembersnumber", "1");
                    intent.putExtra("channelactive", "1");
                    startActivity(intent);

                }
            }
        });
    }


    private void NewChannel(String uid, String name, String description, String membersnumbers, String avatar_lq, String lastmsg, String lastdate, String membership) {
        Intent intent = new Intent("addNewChannel");
        intent.putExtra("uid", uid);
        intent.putExtra("name", name);
        intent.putExtra("description", description);
        intent.putExtra("avatar_lq", avatar_lq);
        intent.putExtra("membership", membership);
        intent.putExtra("membersnumbers", membersnumbers);
        intent.putExtra("lastmsg", lastmsg);
        intent.putExtra("lastdate", lastdate);
        LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(intent);
    }


    private void NewChannelSendToAll(String model, String uid, String name, String description, String membersnumbers, String avatar_lq, String lastmsg, String lastdate, String membership) {

        Intent intentAll = new Intent("addNewInAll");
        intentAll.putExtra("MODEL", model); // 3 = channel
        intentAll.putExtra("UID", uid);
        intentAll.putExtra("NAME", name);
        intentAll.putExtra("DESC", description);
        intentAll.putExtra("MEMBERS_NUMBER", membersnumbers);
        intentAll.putExtra("MEMBER_SHIP", membership);
        intentAll.putExtra("AVATAR_LQ", avatar_lq);
        intentAll.putExtra("LAST_MSG", lastmsg);
        intentAll.putExtra("LAST_DATE", lastdate);
        LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(intentAll);
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service: manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
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
