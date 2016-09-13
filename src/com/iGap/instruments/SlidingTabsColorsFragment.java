// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.instruments;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.iGap.AddMemberToGroup;
import com.iGap.Channel;
import com.iGap.R;
import com.iGap.SelectContactSingle;
import com.iGap.adapter.ChannelAdapter;
import com.iGap.adapter.G;
import com.iGap.fragments.PageMessagingAll;
import com.iGap.fragments.PageMessagingChanneles;
import com.iGap.fragments.PageMessagingChats;
import com.iGap.fragments.PageMessagingGroups;
import com.iGap.helpers.HelperAnimation;
import com.iGap.helpers.HelperGetTime;
import com.iGap.interfaces.OnComplet;
import com.iGap.services.MyService;
import com.iGap.services.TimerServies;


/**
 * 
 * adapter view pager for load page messaging all and chat and channel and group
 *
 */

public class SlidingTabsColorsFragment extends Fragment {

    private SlidingTabLayout        mSlidingTabLayout;
    private ViewPager               mViewPager;
    private ArrayList<Fragment>     pages       = new ArrayList<Fragment>();

    //Channel
    public static Button            btnCreate;
    private Typeface                fontAwesome;
    private Dialog                  dialog;
    private EditText                edtchannelName, edtchannelDesc, edtchannelUid;
    private String                  channelName, channelDesc, channelUid, basicAuth;
    private JSONParser              jParser     = new JSONParser();
    private TimerServies            ts;
    private database                db;

    //Groups
    private Dialog                  pDialog;
    private EditText                edtgroupName, edtgroupDesc;
    private String                  groupname, groupdesc, time;
    private MyService               mService;
    private boolean                 mBounded;

    private Animation               slide_down, slide_up;

    private static final String     TAG         = "SlidingTabsColorsFragment";
    private final ServiceConnection mConnection = new ServiceConnection() {

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pages.add(PageMessagingAll.newInstance("all"));
        pages.add(PageMessagingChats.newInstance("chats"));
        pages.add(PageMessagingGroups.newInstance("groups"));
        pages.add(PageMessagingChanneles.newInstance("chaneles"));

        defineDialog();
    }


    private void defineDialog() {
        pDialog = new Dialog(getActivity());
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
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sample, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        doBindService();
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        HelperAnimation.helperAnimation(mViewPager, 2);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(new SampleFragmentPagerAdapter(getChildFragmentManager()));

        ts = new TimerServies();
        db = new database(getActivity());
        db.open();
        basicAuth = db.namayesh4(3, "info");
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);

        btnCreate = (Button) view.findViewById(R.id.btn_create);
        mSlidingTabLayout.setViewPager(mViewPager, btnCreate);

        slide_down = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down_animation);
        slide_up = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up_animtion);
        fontAwesome = Typeface.createFromAsset(getActivity().getAssets(), "fonts/fontawesome-webfont.ttf");

        btnCreate.setTypeface(fontAwesome);
        btnCreate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (G.messagingPageNumber == 1) {

                    //Do Nothing

                } else if (G.messagingPageNumber == 2) {

                    Intent intent = new Intent(getActivity(), SelectContactSingle.class);
                    startActivity(intent);

                } else if (G.messagingPageNumber == 3) {

                    //new HelperDialog(getActivity()).dialogcreategroup(mService);
                    Log.i("LOG", "Group 1");
                    dialogcreategroup();

                } else if (G.messagingPageNumber == 4) {

                    //new HelperDialog(getActivity()).dialogcreatechannels();
                    dialogcreatechannels();

                }
            }
        });

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(hideButton, new IntentFilter("HIDE_BUTTON"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(showButton, new IntentFilter("SHOW_BUTTON"));

    }

    private BroadcastReceiver hideButton = new BroadcastReceiver() {

                                             @Override
                                             public void onReceive(Context context, Intent intent) {
                                                 if (G.showCreateButton) {
                                                     G.showCreateButton = false;
                                                     btnCreate.startAnimation(slide_down);
                                                     btnCreate.setVisibility(View.GONE);
                                                 }
                                             }
                                         };
    private BroadcastReceiver showButton = new BroadcastReceiver() {

                                             @Override
                                             public void onReceive(Context context, Intent intent) {
                                                 if ( !G.showCreateButton) {
                                                     G.showCreateButton = true;
                                                     btnCreate.startAnimation(slide_up);
                                                     btnCreate.setVisibility(View.VISIBLE);
                                                 }
                                             }
                                         };


    class SampleFragmentPagerAdapter extends FragmentPagerAdapter {

        SampleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int i) {

            return pages.get(i);
        }


        @Override
        public int getCount() {
            return pages.size();
        }

    }


    //=========================================== (Create For Channel)
    @SuppressWarnings("deprecation")
    private void dialogcreatechannels() {
        dialog = new Dialog(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_create_channels, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(dialogView);
        Typeface RobotoLight = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");
        Typeface RobotoBold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/RobotoBold.ttf");
        edtchannelName = (EditText) dialogView.findViewById(R.id.edt_channelName);
        edtchannelDesc = (EditText) dialogView.findViewById(R.id.edt_channelDesc);
        edtchannelUid = (EditText) dialogView.findViewById(R.id.edt_channelUid);
        TextView txt_channel = (TextView) dialogView.findViewById(R.id.txt_channel);
        txt_channel.setTypeface(RobotoBold);

        TextView txt_channel_text = (TextView) dialogView.findViewById(R.id.txt_channel_text);
        txt_channel_text.setTypeface(RobotoLight);

        TextView txt_channel_name = (TextView) dialogView.findViewById(R.id.txt_channel_name);
        txt_channel_name.setTypeface(RobotoBold);

        TextView txt_channel_des = (TextView) dialogView.findViewById(R.id.txt_channel_des);
        txt_channel_des.setTypeface(RobotoBold);

        TextView txt_channel_nick_name = (TextView) dialogView.findViewById(R.id.txt_channel_nick_name);
        txt_channel_nick_name.setTypeface(RobotoBold);
        Button btncreate = (Button) dialogView.findViewById(R.id.btn_create);
        btncreate.setTypeface(RobotoBold);
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
                            Toast.makeText(getActivity(), getString(R.string.please_enter_unique_identifier_en), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getActivity(), getString(R.string.please_enter_channel_description_en), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), getString(R.string.please_enter_channel_name_en), Toast.LENGTH_SHORT).show();
                }
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());

        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
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
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                        }
                    });
                }
                else {

                    String currenttime;
                    try {

                        currenttime = ts.getDateTime();
                    }
                    catch (Exception e) {
                        HelperGetTime helperGetTime = new HelperGetTime();
                        currenttime = helperGetTime.getTime();
                    }

                    //yani sakhte shod
                    db.addchannel(channelUid, channelName, channelDesc, "1", "", "", "you created this channel", currenttime, "0", "1");
                    db.addchannelhistory(channelUid, "first", "you created this channel", currenttime, "100", "", "1", "0", "1", "0", "");
                    NewChannel(channelUid, channelName, channelDesc, "1", "", "you created this channel", currenttime, "1");
                    Intent intent = new Intent(getActivity(), Channel.class);
                    intent.putExtra("channeluid", channelUid);
                    intent.putExtra("channelName", channelName);
                    intent.putExtra("channelDesc", channelDesc);
                    intent.putExtra("channelavatarlq", "");
                    intent.putExtra("channelmembership", "1");
                    intent.putExtra("channelmembersnumber", "1");
                    intent.putExtra("channelactive", "1");
                    startActivity(intent);

                    Intent intent2 = new Intent("loadChannelInfo");
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent2);

                }
            }
        });
    }


    //================================================================(Create For Groups)
    @SuppressWarnings("deprecation")
    private void dialogcreategroup() {
        dialog = new Dialog(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_create_group, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(dialogView);

        Typeface RobotoBold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/RobotoBold.ttf");
        Typeface RobotoLight = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");

        edtgroupName = (EditText) dialogView.findViewById(R.id.edt_groupName);
        edtgroupDesc = (EditText) dialogView.findViewById(R.id.edt_groupDesc);

        TextView txtgroup = (TextView) dialogView.findViewById(R.id.txt_group);
        txtgroup.setTypeface(RobotoBold);

        TextView txt_group_name = (TextView) dialogView.findViewById(R.id.txt_group_name);
        txt_group_name.setTypeface(RobotoBold);

        TextView txt_group_text = (TextView) dialogView.findViewById(R.id.txt_group_text);
        txt_group_text.setTypeface(RobotoLight);

        TextView txt_group_des = (TextView) dialogView.findViewById(R.id.txt_group_des);
        txt_group_des.setTypeface(RobotoBold);

        Button btncreate = (Button) dialogView.findViewById(R.id.btn_create);
        btncreate.setTypeface(RobotoBold);
        Log.i("LOG", "Group 2");
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

                        try {

                            time = ts.getDateTime();
                        }
                        catch (Exception e) {

                            HelperGetTime helperGetTime = new HelperGetTime();
                            time = helperGetTime.getTime();

                        }
                        Log.i("LOG", "Group 3");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            new creategroup().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } else {
                            new creategroup().execute();
                        }

                    } else {
                        Toast.makeText(getActivity(), getString(R.string.please_enter_group_description_en), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), getString(R.string.please_enter_channel_name_en), Toast.LENGTH_SHORT).show();
                }

            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());

        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
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
            Log.i("LOG", "Group 4");
            try {
                Log.i("LOG", "Group 5");
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("name", groupname));
                params.add(new BasicNameValuePair("description", groupdesc));

                JSONObject jsonobj = jParser.getJSONFromUrl(G.creategroupchat, params, "POST", basicAuth, null);
                Log.i("LOG", "Group 6");

                try {

                    String statuscode = jsonobj.getString("statuscode");
                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);
                    Boolean success = json.getBoolean(G.TAG_SUCCESS);
                    Log.i("LOG", "Group 7");
                    if (success == true) {

                        JSONObject result = json.getJSONObject("result");
                        String id = result.getString("id") + "@group.igap.im";
                        mService.joinroom(id, groupname, groupdesc, "", "", "You've created this group", time, "1");
                        db.Addgroupchat(id, groupname, "1", "1", "", "", "You've created this group", time, groupdesc);

                        String username = db.namayesh4(1, "info");
                        db.addgroupchathistory("", id, "empty", "You Created this group", "1", time, "2", "empty", "empty", "100", null, null, null, username, "", "", "");

                        Intent intent = new Intent(getActivity(), AddMemberToGroup.class);
                        intent.putExtra("id", id);
                        intent.putExtra("name", groupname);
                        intent.putExtra("groupdesc", groupdesc);
                        intent.putExtra("gchmembership", "1");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    } else {

                        if (statuscode.equals("400")) {
                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), getString(R.string.illegal_characters), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                }
                catch (JSONException e) {
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), getString(R.string.internet_connection_problem_en), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            catch (Exception e) {
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), getString(R.string.internet_connection_problem_en), Toast.LENGTH_SHORT).show();
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


    void doBindService() {
        getActivity().bindService(new Intent(getActivity(), MyService.class), mConnection, Context.BIND_AUTO_CREATE);
    }


    void doUnbindService() {
        if (mBounded) {
            getActivity().unbindService(mConnection);
        }
    }


    @Override
    public void onDestroyView() {
        doUnbindService();

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        super.onDestroyView();
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
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
        NewChannelSendToAll("3", uid, name, description, membersnumbers, avatar_lq, lastmsg, lastdate, membership);
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
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intentAll);
    }

}