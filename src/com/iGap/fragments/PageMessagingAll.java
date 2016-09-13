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
import android.graphics.Color;
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
import com.iGap.adapter.AllRecycleAdapter;
import com.iGap.adapter.AllType;
import com.iGap.adapter.G;
import com.iGap.instruments.ImageLoader1;
import com.iGap.instruments.SlidingTabsColorsFragment;
import com.iGap.services.TimerServies;


/**
 * 
 * for show list of all item contain group and chat and channel and update view if change
 *
 */

public class PageMessagingAll extends Fragment {

    private String                     currentTimea;
    private String                     day         = "";
    private String                     month       = "";
    private String                     year        = "";

    private ArrayList<myItem>          list        = new ArrayList<myItem>();
    private ArrayList<Boolean>         visibleView = new ArrayList<Boolean>();
    private ArrayList<AllType>         allitem     = new ArrayList<AllType>();

    private boolean                    onScrollListener;

    private TimerServies               ts;
    private RecyclerView               allmessagelist;
    private ImageLoader1               il;
    private LinearLayout               lytEmpty;
    private AllRecycleAdapter          mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View                       view;
    private Handler                    h1;
    private Context                    mcContext;


    public static PageMessagingAll newInstance(String page) {
        Bundle bundle = new Bundle();
        bundle.putCharSequence("key_page", page);
        PageMessagingAll fragment = new PageMessagingAll();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.page_messaging_all, container, false);
        return fragmentView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mcContext = getActivity();
        this.view = view;
        initUI();
    }


    private void initUI() {

        il = new ImageLoader1(getActivity(), G.basicAuth);
        ts = new TimerServies();
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
        lytEmpty = (LinearLayout) view.findViewById(R.id.lyt_empty);
        allmessagelist = (RecyclerView) view.findViewById(R.id.all_message_list);
        allmessagelist.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        allmessagelist.setLayoutManager(mLayoutManager);
        loadall(getActivity());

        try {
            if ( !allitem.isEmpty()) {
                Runnable fitsOnScreen = new Runnable() {

                    @Override
                    public void run() {
                        int last = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
                        if (last == allmessagelist.getAdapter().getItemCount() - 1 && allmessagelist.getChildAt(last).getBottom() <= allmessagelist.getHeight()) {
                            // It fits!
                            onScrollListener = false;
                        }
                        else {
                            // It doesn't fit...
                            onScrollListener = true;
                        }
                    }
                };

                allmessagelist.post(fitsOnScreen);
            }
        }
        catch (Exception e) {
            onScrollListener = true;
        }

        allmessagelist.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (G.messagingPageNumber == 1 && onScrollListener) {
                    int lastVisiblePosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
                    if (lastVisiblePosition == (recyclerView.getAdapter().getItemCount() - 1)) {

                        SlidingTabsColorsFragment.btnCreate.setVisibility(View.GONE);
                        Intent intent = new Intent("HIDE_BUTTON");
                        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);

                    } else {
                        SlidingTabsColorsFragment.btnCreate.setVisibility(View.VISIBLE);
                        Intent intent = new Intent("SHOW_BUTTON");
                        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, new IntentFilter("loadall"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(addNewInAll, new IntentFilter("addNewInAll"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(newPostAll, new IntentFilter("newPostAll"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateSoundAll, new IntentFilter("updateSoundAll"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(deletedFromAll, new IntentFilter("deletedFromAll"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateSeenAll, new IntentFilter("updateSeenAll"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(clearHistoryAll, new IntentFilter("clearHistoryAll"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(deleteChatFromAll, new IntentFilter("deleteChatFromAll"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateAvatarAll, new IntentFilter("updateAvatarAll"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(emptyListAll, new IntentFilter("EMPTY_LIST_ALL"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(kikedFromGroupaAll, new IntentFilter("kikedFromGroupaAll"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateItemInPageAll, new IntentFilter("updateItemInPageAll"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(ActiveItemGroupAll, new IntentFilter("ActiveItemGroupAll"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateMembersNumberToAll, new IntentFilter("updateMembersNumberToAll"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(ChangeRolePageMessagingAll, new IntentFilter("ChangeRolePageMessagingAll"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(newContactAll, new IntentFilter("newContact"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateDateType, new IntentFilter("updateDateType"));
    }

    //==========================Define Broadcasts Start

    private BroadcastReceiver updateDateType             = new BroadcastReceiver() {

                                                             @Override
                                                             public void onReceive(Context context, Intent intent) {
                                                                 String hijri = intent.getStringExtra("hijri");
                                                                 mAdapter.changeDateType(hijri);
                                                             }
                                                         };

    private BroadcastReceiver newContactAll              = new BroadcastReceiver() {

                                                             @Override
                                                             public void onReceive(Context context, Intent intent) {

                                                                 String name = intent.getStringExtra("name");
                                                                 String userchat = intent.getStringExtra("userchat");
                                                                 newcontactall(name, userchat);
                                                             }
                                                         };
    private BroadcastReceiver ChangeRolePageMessagingAll = new BroadcastReceiver() {

                                                             @Override
                                                             public void onReceive(Context context, Intent intent) {

                                                                 String model = intent.getStringExtra("MODEL");
                                                                 String uid = intent.getStringExtra("UID");
                                                                 String role = intent.getStringExtra("ROLE");

                                                                 mAdapter.updateMemberShip(model, uid, role);

                                                             }
                                                         };

    private BroadcastReceiver updateMembersNumberToAll   = new BroadcastReceiver() {

                                                             @Override
                                                             public void onReceive(Context context, Intent intent) {

                                                                 String model = intent.getStringExtra("MODEL");
                                                                 String id = intent.getStringExtra("ID");
                                                                 String numberOfMembers = intent.getStringExtra("NUMBER_OF_MEMBER");

                                                                 mAdapter.updateMembersNumber(model, id, numberOfMembers);

                                                             }
                                                         };

    private BroadcastReceiver ActiveItemGroupAll         = new BroadcastReceiver() {

                                                             @Override
                                                             public void onReceive(Context context, Intent intent) {

                                                                 String model = intent.getStringExtra("MODEL");
                                                                 String uid = intent.getStringExtra("uid");
                                                                 String active = intent.getStringExtra("active");
                                                                 String lastmsg = intent.getStringExtra("lastmsg");
                                                                 String lastdate = intent.getStringExtra("lastdate");

                                                                 mAdapter.updateActiveItem(model, uid, active, lastmsg, lastdate);

                                                             }
                                                         };

    private BroadcastReceiver updateItemInPageAll        = new BroadcastReceiver() {

                                                             @Override
                                                             public void onReceive(Context context, Intent intent) {

                                                                 Bundle extras = intent.getExtras();
                                                                 String model = extras.getString("MODEL");

                                                                 if (model.equals("3")) { // update item Channel
                                                                     String uid = extras.getString("UID");
                                                                     String name = extras.getString("NAME");
                                                                     String desc = extras.getString("DESC");
                                                                     String membersNumber = extras.getString("MEMBERS_NUMBER");
                                                                     String memberShip = extras.getString("MEMBER_SHIP");
                                                                     String avatarLq = extras.getString("AVATAR_LQ");
                                                                     String avatarHq = extras.getString("AVATAR_HQ");

                                                                     mAdapter.updateItemChannel(model, uid, name, desc, membersNumber, avatarLq, avatarHq, memberShip);

                                                                 }

                                                                 else if (model.equals("2")) { // update item Group
                                                                     String uid = extras.getString("UID");
                                                                     String name = extras.getString("NAME");
                                                                     String desc = extras.getString("DESC");
                                                                     String memberShip = extras.getString("MEMBER_SHIP");
                                                                     String avatarLq = extras.getString("AVATAR_LQ");
                                                                     String avatarHq = extras.getString("AVATAR_HQ");

                                                                     mAdapter.updateItemgroup(model, uid, name, desc, avatarLq, avatarHq, memberShip);
                                                                 }

                                                             }
                                                         };

    private BroadcastReceiver kikedFromGroupaAll         = new BroadcastReceiver() {

                                                             @Override
                                                             public void onReceive(Context context, Intent intent) {

                                                                 String model = intent.getStringExtra("MODEL");
                                                                 String uid = intent.getStringExtra("UID");
                                                                 String lastmsg = intent.getStringExtra("LAST_MSG");
                                                                 String lastdate = intent.getStringExtra("LAST_DATE");

                                                                 mAdapter.kikedFromGroup(model, uid, lastdate, lastmsg);
                                                             }
                                                         };

    private BroadcastReceiver mMessageReceiver           = new BroadcastReceiver() {

                                                             @Override
                                                             public void onReceive(Context context, Intent intent) {

                                                                 loadall(context);
                                                             }
                                                         };

    private BroadcastReceiver addNewInAll                = new BroadcastReceiver() {

                                                             @Override
                                                             public void onReceive(Context context, Intent intent) {

                                                                 Bundle extras = intent.getExtras();
                                                                 String model = extras.getString("MODEL");
                                                                 if (model.equals("3")) { // Add New Channel
                                                                     String uid = extras.getString("UID");
                                                                     String name = extras.getString("NAME");
                                                                     String desc = extras.getString("DESC");
                                                                     String membersNumber = extras.getString("MEMBERS_NUMBER");
                                                                     String memberShip = extras.getString("MEMBER_SHIP");
                                                                     String avatarLq = extras.getString("AVATAR_LQ");
                                                                     String avatarHq = extras.getString("AVATAR_HQ");
                                                                     String lastMsg = extras.getString("LAST_MSG");
                                                                     String lastDate = extras.getString("LAST_DATE");
                                                                     addNewInAll(model, uid, name, desc, membersNumber, memberShip, avatarLq, avatarHq, lastMsg, lastDate);
                                                                 } else if (model.equals("2")) { // Add New Group
                                                                     String uid = extras.getString("UID");
                                                                     String name = extras.getString("NAME");
                                                                     String desc = extras.getString("DESC");
                                                                     String memberShip = extras.getString("MEMBERSHIP");
                                                                     String avatarLq = extras.getString("AVATAR_LQ");
                                                                     String avatarHq = extras.getString("AVATAR_HQ");
                                                                     String lastMsg = extras.getString("LAST_MSG");
                                                                     String lastDate = extras.getString("LAST_DATE");
                                                                     addNewInAll(model, uid, name, desc, "", memberShip, avatarLq, avatarHq, lastMsg, lastDate);
                                                                 } else if (model.equals("1")) { // Add New Chat
                                                                     String roomID = extras.getString("ROOM_ID");
                                                                     String userChat = extras.getString("USERCHAT");
                                                                     String desc = extras.getString("DESC");
                                                                     String userAvatar = extras.getString("USERCHAT_AVATAR");
                                                                     String lastMsg = extras.getString("LAST_MSG");
                                                                     String lastDate = extras.getString("LAST_DATE");
                                                                     addNewInAll(model, roomID, userChat, desc, "", "", userAvatar, "", lastMsg, lastDate);
                                                                 }
                                                             }
                                                         };

    private BroadcastReceiver newPostAll                 = new BroadcastReceiver() {

                                                             @Override
                                                             public void onReceive(Context context, Intent intent) {

                                                                 Bundle extras = intent.getExtras(); // Channel and Group have eqaul params
                                                                 String model = extras.getString("MODEL");
                                                                 String uid = extras.getString("UID");
                                                                 String lastMsg = extras.getString("LAST_MSG");
                                                                 String lastDate = extras.getString("LAST_DATE");
                                                                 newPostAll(model, uid, lastMsg, lastDate);
                                                             }
                                                         };
    private BroadcastReceiver updateSoundAll             = new BroadcastReceiver() {

                                                             @Override
                                                             public void onReceive(Context context, Intent intent) {

                                                                 Bundle extars = intent.getExtras(); // Channel and Group have equal params
                                                                 String model = extars.getString("MODEL");
                                                                 String uid = extars.getString("UID");
                                                                 String value = extars.getString("VALUE");
                                                                 updateSoundAll(model, uid, value);
                                                             }
                                                         };

    private BroadcastReceiver deletedFromAll             = new BroadcastReceiver() {

                                                             @Override
                                                             public void onReceive(Context context, Intent intent) {

                                                                 Bundle extras = intent.getExtras();
                                                                 String model = extras.getString("MODEL");
                                                                 String uid = extras.getString("UID");
                                                                 String lastMsg = extras.getString("LAST_MSG");
                                                                 String lastDate = extras.getString("LAST_DATE");
                                                                 deletedFromAll(model, uid, lastMsg, lastDate);
                                                             }
                                                         };
    private BroadcastReceiver updateSeenAll              = new BroadcastReceiver() {

                                                             @Override
                                                             public void onReceive(Context context, Intent intent) {

                                                                 Bundle extars = intent.getExtras();
                                                                 String model = extars.getString("MODEL");
                                                                 String uid = extars.getString("UID");
                                                                 updateSeenAll(model, uid);
                                                             }
                                                         };

    private BroadcastReceiver clearHistoryAll            = new BroadcastReceiver() {

                                                             @Override
                                                             public void onReceive(Context context, Intent intent) {

                                                                 Bundle extars = intent.getExtras();
                                                                 String model = extars.getString("MODEL");
                                                                 String uid = extars.getString("UID");
                                                                 clearHistoryAll(model, uid);
                                                             }
                                                         };
    private BroadcastReceiver updateAvatarAll            = new BroadcastReceiver() {

                                                             @Override
                                                             public void onReceive(Context context, Intent intent) {

                                                                 Bundle extars = intent.getExtras();
                                                                 String model = extars.getString("MODEL");
                                                                 String avatar = extars.getString("AVATAR");
                                                                 String uid = extars.getString("UID");
                                                                 updateAvatarAll(uid, model, avatar);
                                                             }
                                                         };

    private BroadcastReceiver deleteChatFromAll          = new BroadcastReceiver() {

                                                             @Override
                                                             public void onReceive(Context context, Intent intent) {
                                                                 Bundle extars = intent.getExtras();
                                                                 String model = extars.getString("MODEL");
                                                                 String uid = extars.getString("UID");
                                                                 deleteChatFromAll(model, uid);
                                                             }
                                                         };

    private BroadcastReceiver emptyListAll               = new BroadcastReceiver() {

                                                             @Override
                                                             public void onReceive(Context context, Intent intent) {
                                                                 showDummy();
                                                             }
                                                         };


    //=========================Define Broadcasts End

    private void loadall(Context mContext) {

        try {
            list.clear();
            allitem.clear();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Cursor channels = G.cmd.getTableRow("channels", "lastdate");
        Cursor Chatrooms = G.cmd.getTableRow("Chatrooms", "lasttime");
        Cursor groupchatrooms = G.cmd.getTableRow("groupchatrooms", "lastdate");

        getDataFromTable(channels, Chatrooms, groupchatrooms);

        channels.moveToFirst();
        Chatrooms.moveToFirst();
        groupchatrooms.moveToFirst();

        if (list.size() == 0) {
            allmessagelist.setVisibility(View.GONE);
            lytEmpty.setVisibility(View.VISIBLE);
        } else {
            if (allmessagelist.getVisibility() == View.GONE) {
                allmessagelist.setVisibility(View.VISIBLE);
                lytEmpty.setVisibility(View.GONE);
            }

            for (int i = 0; i < list.size(); i++) {

                if (list.get(i).name.equals("channels")) {

                    AllType mitem = new AllType();

                    mitem.model = "3";
                    mitem.id = channels.getString(0);
                    mitem.uid = channels.getString(1);
                    mitem.name = channels.getString(2);
                    mitem.description = channels.getString(3);
                    mitem.membersnumber = channels.getString(4);
                    mitem.avatar_lq = channels.getString(5);
                    mitem.avatar_hq = channels.getString(6);
                    mitem.lastmessage = channels.getString(7);
                    mitem.lastdate = channels.getString(8);
                    mitem.sound = channels.getString(9);
                    mitem.membership = channels.getString(10);
                    mitem.active = channels.getString(11);

                    allitem.add(mitem);

                    channels.moveToNext();
                }
                else if (list.get(i).name.equals("Chatrooms")) {

                    AllType mitem = new AllType();

                    mitem.model = "1";
                    mitem.id = Chatrooms.getString(0);
                    mitem.userchat = Chatrooms.getString(1);
                    mitem.lastmessage = Chatrooms.getString(2);
                    mitem.lastdate = Chatrooms.getString(3);
                    mitem.sound = Chatrooms.getString(4);
                    mitem.userchatavatar = Chatrooms.getString(5);
                    mitem.active = Chatrooms.getString(6);

                    String mobile = Chatrooms.getString(1).split("@")[0];
                    String userchatname;
                    try {
                        userchatname = G.cmd.namayeshname(1, mobile);
                    }
                    catch (Exception e) {
                        userchatname = mobile;
                    }
                    mitem.name = userchatname;

                    allitem.add(mitem);

                    Chatrooms.moveToNext();
                }
                else if (list.get(i).name.equals("groupchatrooms")) {

                    AllType mitem = new AllType();

                    mitem.model = "2";

                    mitem.id = groupchatrooms.getString(0);
                    mitem.groupchatid = groupchatrooms.getString(1);
                    mitem.name = groupchatrooms.getString(2);
                    mitem.membership = groupchatrooms.getString(3);
                    mitem.membersnumber = groupchatrooms.getString(4);
                    mitem.groupavatar = groupchatrooms.getString(5);
                    mitem.lastmessage = groupchatrooms.getString(6);
                    mitem.lastdate = groupchatrooms.getString(7);
                    mitem.description = groupchatrooms.getString(8);
                    mitem.sound = groupchatrooms.getString(9);
                    mitem.active = groupchatrooms.getString(10);
                    mitem.avatar_hq = groupchatrooms.getString(11);

                    allitem.add(mitem);
                    groupchatrooms.moveToNext();
                }
            }
            //Chatrooms.close();
            //channels.close();
            //groupchatrooms.close();

            if (visibleView.size() != list.size()) {

                int ekhtelaf = list.size() - visibleView.size();

                for (int i = 0; i < ekhtelaf; i++) {
                    visibleView.add(false);
                }
            }

        }
        mAdapter = new AllRecycleAdapter(allitem, mContext, year, month, day, il, view);
        allmessagelist.setAdapter(mAdapter);
    }


    public class myItem {

        String name;
        String date;
    }


    private void getDataFromTable(Cursor channels, Cursor Chatrooms, Cursor groupchatrooms) {

        int channelsCount = channels.getCount();
        int ChatroomsCount = Chatrooms.getCount();
        int groupchatroomsCount = groupchatrooms.getCount();

        for (int i = 0; i < channelsCount; i++) {
            channels.moveToPosition(i);
            insertToArry("channels", channels.getString(8));
        }

        for (int i = 0; i < ChatroomsCount; i++) {

            Chatrooms.moveToPosition(i);
            insertToArry("Chatrooms", Chatrooms.getString(3));
        }

        for (int i = 0; i < groupchatroomsCount; i++) {

            groupchatrooms.moveToPosition(i);
            insertToArry("groupchatrooms", groupchatrooms.getString(7));

        }

    }


    private void insertToArry(String name, String date) {
        myItem item = new myItem();
        item.name = name;
        item.date = date;
        int size = list.size();

        for (int i = 0; i < size; i++) {

            if (list.get(i).date.compareTo(date) < 0) {

                list.add(i, item);
                return;
            }
        }
        list.add(item);
    }


    private void addNewInAll(String model, String uid, String name, String desc, String membersNumber, String memberShip, String avatarLq, String avatarHq, String lastMsg, String lastDate) {
        if (mAdapter.getItemCount() == 0) {
            allmessagelist.setVisibility(View.VISIBLE);
            lytEmpty.setVisibility(View.GONE);
        }
        mAdapter.insert(uid, name, desc, membersNumber, avatarLq, avatarHq, lastMsg, lastDate, memberShip, model);
    }


    private void newPostAll(String model, String uid, String lastMsg, String lastDate) {
        mAdapter.newPost(model, uid, lastMsg, lastDate);
    }


    private void updateSoundAll(String model, String uid, String value) {
        mAdapter.updateSoundWithUid(model, uid, value);
    }


    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            SlidingTabsColorsFragment.btnCreate.setBackgroundColor(Color.parseColor("#00ffffff"));
            SlidingTabsColorsFragment.btnCreate.setText("");
        }
    }


    private void showDummy() {
        allmessagelist.setVisibility(View.GONE);
        lytEmpty.setVisibility(View.VISIBLE);
    }


    private void deletedFromAll(String model, String uid, String lastMsg, String lastDate) {
        mAdapter.deletedChannelOrGroupOrChat(model, uid, lastMsg, lastDate);
    }


    private void updateSeenAll(String model, String uid) {
        mAdapter.updateSeen(model, uid);
    }


    private void clearHistoryAll(String model, String uid) {
        mAdapter.clearhistorywithuid(model, uid);
    }


    private void updateAvatarAll(String uid, String model, String avatar) {
        mAdapter.updateavatar(uid, model, avatar);
    }


    private void deleteChatFromAll(String model, String uid) {
        mAdapter.deleteChatFromAll(model, uid);
    }


    public void newcontactall(String name, String userchat) {
        mAdapter.newContact(name, userchat);
    }
}
