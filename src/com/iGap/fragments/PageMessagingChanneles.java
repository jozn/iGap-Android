// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.fragments;

import java.util.ArrayList;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.iGap.R;
import com.iGap.adapter.G;
import com.iGap.adapter.ChannelListsAdapter;
import com.iGap.instruments.ImageLoader1;
import com.iGap.instruments.SlidingTabsColorsFragment;
import com.iGap.services.TimerServies;


/**
 * 
 * for show list of channel item and update view if change
 *
 */

public class PageMessagingChanneles extends Fragment {

    private ArrayList<String>          channeluidarray           = new ArrayList<String>();
    private ArrayList<String>          channelnamearray          = new ArrayList<String>();
    private ArrayList<String>          channeldescriptionarray   = new ArrayList<String>();
    private ArrayList<String>          channelmembersnumberarray = new ArrayList<String>();
    private ArrayList<String>          channelavatar_lqarray     = new ArrayList<String>();
    private ArrayList<String>          channelavatar_hqarray     = new ArrayList<String>();
    private ArrayList<String>          channellastmessagearray   = new ArrayList<String>();
    private ArrayList<String>          channellastdatearray      = new ArrayList<String>();
    private ArrayList<String>          channelsoundarray         = new ArrayList<String>();
    private ArrayList<String>          channelmembershiparray    = new ArrayList<String>();
    private ArrayList<String>          channelunreadarray        = new ArrayList<String>();
    private ArrayList<String>          channelactivearray        = new ArrayList<String>();
    private ArrayList<Boolean>         visibleView               = new ArrayList<Boolean>();

    private String                     currentTimea;
    private String                     day                       = "";
    private String                     month                     = "";
    private String                     year                      = "";

    private RecyclerView               lvmessagechannels;
    private RecyclerView.LayoutManager mLayoutManager;
    private ChannelListsAdapter                  mAdapter;

    private LinearLayout               lytEmpty;
    private ImageLoader1               il;
    private boolean                    onScrollListener;
    private View                       view;
    private Handler                    h1;
    public static Context              mcContext;


    public static PageMessagingChanneles newInstance(String page) {
        Bundle bundle = new Bundle();
        bundle.putCharSequence("key_page", page);
        PageMessagingChanneles fragment = new PageMessagingChanneles();
        fragment.setArguments(bundle);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.page_messaging_channeles, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mcContext = getActivity();
        this.view = view;

        initUI();
        loadinfo(getActivity());

    }

    private BroadcastReceiver mMessageReceiver               = new BroadcastReceiver() {

                                                                 @Override
                                                                 public void onReceive(Context context, Intent intent) {
                                                                     loadinfo(context);
                                                                 }
                                                             };
    private BroadcastReceiver channelDeleted                 = new BroadcastReceiver() {

                                                                 @Override
                                                                 public void onReceive(Context context, Intent intent) {

                                                                     String uid = intent.getStringExtra("uid");
                                                                     String lastmsg = intent.getStringExtra("lastmsg");
                                                                     String lastdate = intent.getStringExtra("lastdate");
                                                                     deletedchannel(uid, lastmsg, lastdate);
                                                                 }
                                                             };
    private BroadcastReceiver updateAvatarChannel            = new BroadcastReceiver() {

                                                                 @Override
                                                                 public void onReceive(Context context, Intent intent) {

                                                                     String uid = intent.getStringExtra("uid");
                                                                     String avatarlq = intent.getStringExtra("avatarlq");
                                                                     updateavatar(uid, avatarlq);
                                                                 }
                                                             };
    private BroadcastReceiver updateSound                    = new BroadcastReceiver() {

                                                                 @Override
                                                                 public void onReceive(Context context, Intent intent) {

                                                                     String uid = intent.getStringExtra("uid");
                                                                     String value = intent.getStringExtra("value");
                                                                     updatesoundwithuid(uid, value);
                                                                 }
                                                             };
    private BroadcastReceiver deleteWithUid                  = new BroadcastReceiver() {

                                                                 @Override
                                                                 public void onReceive(Context context, Intent intent) {
                                                                     String uid = intent.getStringExtra("uid");
                                                                     deletewithuid(uid);
                                                                 }
                                                             };
    private BroadcastReceiver newPostChannel                 = new BroadcastReceiver() {

                                                                 @Override
                                                                 public void onReceive(Context context, Intent intent) {

                                                                     String uid = intent.getStringExtra("uid");
                                                                     String lastmsg = intent.getStringExtra("lastmsg");
                                                                     String lastdate = intent.getStringExtra("lastdate");
                                                                     newpostchannel(uid, lastmsg, lastdate);
                                                                 }
                                                             };
    private BroadcastReceiver addNewChannel                  = new BroadcastReceiver() {

                                                                 @Override
                                                                 public void onReceive(Context context, Intent intent) {
                                                                     String uid = intent.getStringExtra("uid");
                                                                     String name = intent.getStringExtra("name");
                                                                     String description = intent.getStringExtra("description");
                                                                     String membersnumbers = intent.getStringExtra("membersnumbers");
                                                                     String avatar_lq = intent.getStringExtra("avatar_lq");
                                                                     String avatar_hq = intent.getStringExtra("avatar_hq");
                                                                     String lastmsg = intent.getStringExtra("lastmsg");
                                                                     String lastdate = intent.getStringExtra("lastdate");
                                                                     String membership = intent.getStringExtra("membership");
                                                                     addNewChannel(uid, name, description, membersnumbers, avatar_lq, avatar_hq, lastmsg, lastdate, membership);
                                                                 }
                                                             };
    private BroadcastReceiver updateSeen                     = new BroadcastReceiver() {

                                                                 @Override
                                                                 public void onReceive(Context context, Intent intent) {
                                                                     String uid = intent.getStringExtra("uid");
                                                                     updateseen(uid);
                                                                 }
                                                             };

    private BroadcastReceiver emptyListChannel               = new BroadcastReceiver() {

                                                                 @Override
                                                                 public void onReceive(Context context, Intent intent) {
                                                                     showDummy();
                                                                 }
                                                             };

    private BroadcastReceiver updateChannelItem              = new BroadcastReceiver() {

                                                                 @Override
                                                                 public void onReceive(Context context, Intent intent) {

                                                                     String uid = intent.getStringExtra("uid");
                                                                     String name = intent.getStringExtra("name");
                                                                     String description = intent.getStringExtra("description");
                                                                     String membersnumbers = intent.getStringExtra("membersnumbers");
                                                                     String avatar_lq = intent.getStringExtra("avatar_lq");
                                                                     String avatar_hq = intent.getStringExtra("avatar_hq");
                                                                     String membership = intent.getStringExtra("membership");

                                                                     mAdapter.updateChannelItem(uid, name, description, membersnumbers, avatar_lq, avatar_hq, membership);
                                                                 }
                                                             };

    private BroadcastReceiver ActiveItemChannel              = new BroadcastReceiver() {

                                                                 @Override
                                                                 public void onReceive(Context context, Intent intent) {

                                                                     String uid = intent.getStringExtra("uid");
                                                                     String active = intent.getStringExtra("active");
                                                                     String lastmsg = intent.getStringExtra("lastmsg");
                                                                     String lastdate = intent.getStringExtra("lastdate");

                                                                     mAdapter.activeItemChannel(uid, active, lastmsg, lastdate);
                                                                 }
                                                             };

    private BroadcastReceiver ChangeRolePageMessagingChannel = new BroadcastReceiver() {

                                                                 @Override
                                                                 public void onReceive(Context context, Intent intent) {

                                                                     String uid = intent.getStringExtra("UID");
                                                                     String membership = intent.getStringExtra("ROLE");
                                                                     mAdapter.updateMemberShip(uid, membership);
                                                                 }
                                                             };

    private BroadcastReceiver updateDateType                 = new BroadcastReceiver() {

                                                                 @Override
                                                                 public void onReceive(Context context, Intent intent) {
                                                                     String hijri = intent.getStringExtra("hijri");
                                                                     mAdapter.changeDateType(hijri);
                                                                 }
                                                             };


    private void initUI() {
        il = new ImageLoader1(getActivity(), G.basicAuth);
        lytEmpty = (LinearLayout) view.findViewById(R.id.lyt_empty);
        lvmessagechannels = (RecyclerView) view.findViewById(R.id.lv_message_channels);
        lvmessagechannels.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        lvmessagechannels.setLayoutManager(mLayoutManager);
        TimerServies ts = new TimerServies();
        try {

            currentTimea = ts.getDateTime();
        }
        catch (Exception e) {
            currentTimea = G.helperGetTime.getTime();
        }
        String[] splitedtime = currentTimea.split("\\s+");
        String date = splitedtime[0];

        String[] splited_date = date.split("-");
        year = splited_date[0];
        month = splited_date[1];
        day = splited_date[2];

        lvmessagechannels.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (G.messagingPageNumber == 4 && onScrollListener) {

                    if (dy > 0) {
                        if (G.showButton) { // agar button namayesh dade mishavad
                            G.showButton = false;
                            Intent intent = new Intent("HIDE_BUTTON");
                            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                        }
                    } else {
                        if ( !G.showButton) { // agar button namayesh dade nemishavad
                            G.showButton = true;
                            Intent intent = new Intent("SHOW_BUTTON");
                            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                        }
                    }
                }
            }


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, new IntentFilter("loadChannelInfo"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(addNewChannel, new IntentFilter("addNewChannel"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(newPostChannel, new IntentFilter("newPostChannelList"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateSound, new IntentFilter("updateSound"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(deleteWithUid, new IntentFilter("deleteWithUid"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(channelDeleted, new IntentFilter("channelDeleted"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateSeen, new IntentFilter("updateSeen"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateAvatarChannel, new IntentFilter("updateAvatarChannel"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(emptyListChannel, new IntentFilter("EMPTY_LIST_CHANNEL"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateChannelItem, new IntentFilter("updateChannelItem"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(ActiveItemChannel, new IntentFilter("ActiveItemChannel"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(ChangeRolePageMessagingChannel, new IntentFilter("ChangeRolePageMessagingChannel"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateDateType, new IntentFilter("updateDateType"));
    }


    private void loadinfo(Context context) {

        try {
            channeluidarray.clear();
            channelnamearray.clear();
            channeldescriptionarray.clear();
            channelmembersnumberarray.clear();
            channelavatar_lqarray.clear();
            channelavatar_hqarray.clear();
            channellastmessagearray.clear();
            channellastdatearray.clear();
            channelsoundarray.clear();
            channelmembershiparray.clear();
            channelactivearray.clear();
            channelunreadarray.clear();

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        int size = G.cmd.getRowCount("channels");
        if (size == 0) {
            lvmessagechannels.setVisibility(View.GONE);
            lytEmpty.setVisibility(View.VISIBLE);
        } else {
            if (lvmessagechannels.getVisibility() == View.GONE) {
                lvmessagechannels.setVisibility(View.VISIBLE);
                lytEmpty.setVisibility(View.GONE);
            }
        }

        Cursor cursor = G.cmd.selectPageMessagingByTime("channels", "lastdate");

        while (cursor.moveToNext()) {
            String channeluid = cursor.getString(cursor.getColumnIndex("uid"));
            String channelname = cursor.getString(cursor.getColumnIndex("name"));
            String channeldescription = cursor.getString(cursor.getColumnIndex("description"));
            String channelmembersnumber = cursor.getString(cursor.getColumnIndex("membersnumber"));
            String channelavatar_lq = cursor.getString(cursor.getColumnIndex("avatar_lq"));
            String channelavatar_hq = cursor.getString(cursor.getColumnIndex("avatar_hq"));
            String channellastmessage = cursor.getString(cursor.getColumnIndex("lastmessage"));
            String channellastdate = cursor.getString(cursor.getColumnIndex("lastdate"));
            String channelsound = cursor.getString(cursor.getColumnIndex("sound"));
            String channelmembership = cursor.getString(cursor.getColumnIndex("membership"));
            String channelActive = cursor.getString(cursor.getColumnIndex("active"));
            int unread = G.cmd.getchannelunreadsize(channeluid);

            channeluidarray.add(channeluid);
            channelnamearray.add(channelname);
            channeldescriptionarray.add(channeldescription);
            channelmembersnumberarray.add(channelmembersnumber);
            channelavatar_lqarray.add(channelavatar_lq);
            channelavatar_hqarray.add(channelavatar_hq);
            channellastmessagearray.add(channellastmessage);
            channellastdatearray.add(channellastdate);
            channelsoundarray.add(channelsound);
            channelmembershiparray.add(channelmembership);
            channelactivearray.add(channelActive);
            channelunreadarray.add(String.valueOf(unread));
        }
        if (visibleView.size() != channeluidarray.size()) {

            int ekhtelaf = channeluidarray.size() - visibleView.size();

            for (int i = 0; i < ekhtelaf; i++) {
                visibleView.add(false);
            }
        }

        Context cc = getActivity();
        if (cc == null)
            cc = G.context;

        mAdapter = new ChannelListsAdapter(channeluidarray, channelnamearray, channeldescriptionarray, channelmembersnumberarray, channelavatar_lqarray, channelavatar_hqarray, channellastmessagearray, channellastdatearray, channelsoundarray, channelmembershiparray, channelactivearray, channelunreadarray, cc, year, month, day, il, view);
        lvmessagechannels.setAdapter(mAdapter);

        try {
            if ( !channeluidarray.isEmpty()) {
                Runnable fitsOnScreen = new Runnable() {

                    @Override
                    public void run() {
                        int last = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
                        if (last == lvmessagechannels.getAdapter().getItemCount() - 1 && lvmessagechannels.getChildAt(last).getBottom() <= lvmessagechannels.getHeight()) {
                            onScrollListener = false;
                        } else {
                            onScrollListener = true;
                        }
                    }
                };

                lvmessagechannels.post(fitsOnScreen);
            }
        }
        catch (Exception e) {
            onScrollListener = true;
        }
    }


    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            if (SlidingTabsColorsFragment.btnCreate != null) {
                G.showButton = true;
                G.showCreateButton = true;
                SlidingTabsColorsFragment.btnCreate.setVisibility(View.VISIBLE);
                SlidingTabsColorsFragment.btnCreate.setBackgroundResource(R.drawable.circle_shadow);
                SlidingTabsColorsFragment.btnCreate.setText(getString(R.string.fa_plusa));
            }
            G.messagingPageNumber = 4;
        }
    }


    private void showDummy() {
        lvmessagechannels.setVisibility(View.GONE);
        lytEmpty.setVisibility(View.VISIBLE);
    }


    public void addNewChannel(String uid, String name, String description, String membersnumbers, String avatar_lq, String avatar_hq, String lastmsg, String lastdate, String membership) {
        if (mAdapter.getItemCount() == 0) {
            lvmessagechannels.setVisibility(View.VISIBLE);
            lytEmpty.setVisibility(View.GONE);
        }
        mAdapter.insert(uid, name, description, membersnumbers, avatar_lq, avatar_hq, lastmsg, lastdate, membership);
    }


    public void newpostchannel(String uid, String lastmsg, String lastdate) {
        mAdapter.newPost(uid, lastmsg, lastdate);
    }


    public void updatesoundwithuid(String uid, String value) {
        mAdapter.updateSoundWithUid(uid, value);
    }


    public void deletewithuid(String uid) {
        mAdapter.deleteWithUid(uid);
    }


    public void deletedchannel(String uid, String lastmsg, String lastdate) {
        mAdapter.deletedChannel(uid, lastmsg, lastdate);
    }


    public void updateseen(String uid) {
        mAdapter.updateSeen(uid);
    }


    public void updateavatar(String uid, String avatarlq) {
        mAdapter.updateAvatar(uid, avatarlq);
    }
}