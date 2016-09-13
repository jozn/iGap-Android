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
import com.iGap.adapter.ChatRecycleAdapter;
import com.iGap.adapter.G;
import com.iGap.helpers.HelperGetTime;
import com.iGap.instruments.ImageLoader1;
import com.iGap.instruments.SlidingTabsColorsFragment;
import com.iGap.services.TimerServies;


/**
 * 
 * for show list of chats item and update view if change
 *
 */

public class PageMessagingChats extends Fragment {

    private ArrayList<String>          chatroomidarray     = new ArrayList<String>();
    private ArrayList<String>          userchatarray       = new ArrayList<String>();
    private ArrayList<String>          userchatnamearray   = new ArrayList<String>();
    private ArrayList<String>          lastmessagearray    = new ArrayList<String>();
    private ArrayList<String>          lasttimearray       = new ArrayList<String>();
    private ArrayList<String>          soundarray          = new ArrayList<String>();
    private ArrayList<String>          unreadarray         = new ArrayList<String>();
    private ArrayList<String>          activeArarray       = new ArrayList<String>();
    private ArrayList<String>          userchatavatararray = new ArrayList<String>();
    private ArrayList<Boolean>         visibleView         = new ArrayList<Boolean>();

    private String                     day                 = "";
    private String                     month               = "";
    private String                     year                = "";

    private boolean                    onScrollListener;
    private boolean                    reloadListView;
    public static boolean              seenOtherFragment;

    private LinearLayout               lytEmpty;
    private RecyclerView               chatroomslist;
    private RecyclerView.LayoutManager mLayoutManager;
    private ImageLoader1               il;
    private ChatRecycleAdapter         mAdapter;
    private View                       view;
    private Handler                    h1;
    public static Context              mcContext;


    public static PageMessagingChats newInstance(String page) {
        Bundle bundle = new Bundle();
        bundle.putCharSequence("key_page", page);
        PageMessagingChats fragment = new PageMessagingChats();
        fragment.setArguments(bundle);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.page_messaging_chat, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mcContext = getActivity();
        this.view = view;
        initUI();
        loadinfo(getActivity());

    }

    private BroadcastReceiver updateDateType          = new BroadcastReceiver() {

                                                          @Override
                                                          public void onReceive(Context context, Intent intent) {
                                                              String hijri = intent.getStringExtra("hijri");
                                                              mAdapter.changeDateType(hijri);
                                                          }
                                                      };

    private BroadcastReceiver UpdateContactUserAvatar = new BroadcastReceiver() {

                                                          @Override
                                                          public void onReceive(Context context, Intent intent) {

                                                              String userChat = intent.getStringExtra("UserChat");
                                                              updateContactAvatar(userChat);
                                                          }
                                                      };

    private BroadcastReceiver newContactChat          = new BroadcastReceiver() {

                                                          @Override
                                                          public void onReceive(Context context, Intent intent) {

                                                              String name = intent.getStringExtra("name");
                                                              String userchat = intent.getStringExtra("userchat");
                                                              newcontactchat(name, userchat);
                                                          }
                                                      };
    private BroadcastReceiver mMessageReceiver        = new BroadcastReceiver() {

                                                          @Override
                                                          public void onReceive(Context context, Intent intent) {
                                                              loadinfo(context);
                                                          }
                                                      };
    private BroadcastReceiver updateSeenChat          = new BroadcastReceiver() {

                                                          @Override
                                                          public void onReceive(Context context, Intent intent) {

                                                              String uid = intent.getStringExtra("uid");
                                                              updateseen(uid);
                                                          }
                                                      };
    private BroadcastReceiver updateSoundWithUidChat  = new BroadcastReceiver() {

                                                          @Override
                                                          public void onReceive(Context context, Intent intent) {

                                                              String uid = intent.getStringExtra("uid");
                                                              String value = intent.getStringExtra("value");
                                                              updatesoundwithuid(uid, value);
                                                          }
                                                      };
    private BroadcastReceiver cleareWithUidChat       = new BroadcastReceiver() {

                                                          @Override
                                                          public void onReceive(Context context, Intent intent) {

                                                              String uid = intent.getStringExtra("uid");
                                                              clearewithuid(uid);
                                                          }
                                                      };
    private BroadcastReceiver deleteWithUidChat       = new BroadcastReceiver() {

                                                          @Override
                                                          public void onReceive(Context context, Intent intent) {

                                                              String uid = intent.getStringExtra("uid");
                                                              deletewithuid(uid);
                                                          }
                                                      };
    private BroadcastReceiver NewPostChat             = new BroadcastReceiver() {

                                                          @Override
                                                          public void onReceive(Context context, Intent intent) {

                                                              String uid = intent.getStringExtra("uid");
                                                              String lastmsg = intent.getStringExtra("lastmsg");
                                                              String lastdate = intent.getStringExtra("lastdate");
                                                              newpostchat(uid, lastmsg, lastdate);
                                                          }
                                                      };
    private BroadcastReceiver deletedAccountChat      = new BroadcastReceiver() {

                                                          @Override
                                                          public void onReceive(Context context, Intent intent) {

                                                              String uid = intent.getStringExtra("uid");
                                                              String lastmsg = intent.getStringExtra("lastmsg");
                                                              String lastdate = intent.getStringExtra("lastdate");
                                                              newpostchat(uid, lastmsg, lastdate);
                                                          }
                                                      };

    private BroadcastReceiver updateAvatarChat        = new BroadcastReceiver() {

                                                          @Override
                                                          public void onReceive(Context context, Intent intent) {

                                                              String userchat = intent.getStringExtra("userchat");
                                                              String avatar = intent.getStringExtra("avatar");

                                                              mAdapter.updateAvatar(userchat, avatar);
                                                          }
                                                      };

    private BroadcastReceiver NewRoomChat             = new BroadcastReceiver() {

                                                          @Override
                                                          public void onReceive(Context context, Intent intent) {

                                                              String roomid = intent.getStringExtra("roomid");
                                                              String userchat = intent.getStringExtra("userchat");
                                                              String lastmessage = intent.getStringExtra("lastmessage");
                                                              String lasttime = intent.getStringExtra("lasttime");
                                                              String sound = intent.getStringExtra("sound");
                                                              String active = intent.getStringExtra("active");
                                                              String userchatavatar = intent.getStringExtra("userchatavatar");
                                                              newroomchat(roomid, userchat, lastmessage, lasttime, sound, active, userchatavatar);
                                                          }
                                                      };

    private BroadcastReceiver emptyListChat           = new BroadcastReceiver() {

                                                          @Override
                                                          public void onReceive(Context context, Intent intent) {
                                                              showDummy();
                                                          }
                                                      };


    private void loadinfo(Context mContext) {
        try {
            userchatarray.clear();
            lastmessagearray.clear();
            lasttimearray.clear();
            chatroomidarray.clear();
            soundarray.clear();
            userchatavatararray.clear();
            userchatnamearray.clear();
            unreadarray.clear();
            activeArarray.clear();
        }
        catch (Exception e) {}
        int size = G.cmd.getRowCount("Chatrooms");
        if (size == 0) {
            chatroomslist.setVisibility(View.GONE);
            lytEmpty.setVisibility(View.VISIBLE);
        } else {
            if (chatroomslist.getVisibility() == View.GONE) {
                chatroomslist.setVisibility(View.VISIBLE);
                lytEmpty.setVisibility(View.GONE);
            }

            Cursor cursor = G.cmd.selectPageMessagingByTime("Chatrooms", "lasttime");

            while (cursor.moveToNext()) {
                String chatroomid = cursor.getString(cursor.getColumnIndex("id"));
                String userchat = cursor.getString(cursor.getColumnIndex("userchat"));
                String mobile = userchat.split("@")[0];
                String userchatname;
                try {
                    userchatname = G.cmd.namayeshname(1, mobile);
                }
                catch (Exception e) {
                    userchatname = mobile;
                }
                String lastmessage = cursor.getString(cursor.getColumnIndex("lastmessage"));
                String lasttime = cursor.getString(cursor.getColumnIndex("lasttime"));
                String sound = cursor.getString(cursor.getColumnIndex("sound"));
                String userchatavatar = cursor.getString(cursor.getColumnIndex("userchatavatar"));
                String active = cursor.getString(cursor.getColumnIndex("active"));

                int unread = G.cmd.getRowCountunreadchat(chatroomid);

                unreadarray.add(String.valueOf(unread));
                userchatarray.add(userchat);
                lastmessagearray.add(lastmessage);
                lasttimearray.add(lasttime);
                chatroomidarray.add(chatroomid);
                soundarray.add(sound);
                userchatavatararray.add(userchatavatar);
                userchatnamearray.add(userchatname);
                activeArarray.add(active);
            }
            if (visibleView.size() != userchatarray.size()) {

                int ekhtelaf = userchatarray.size() - visibleView.size();

                for (int i = 0; i < ekhtelaf; i++) {
                    visibleView.add(false);
                }
            }

        }
        mAdapter = new ChatRecycleAdapter(chatroomidarray, userchatarray, userchatnamearray, lastmessagearray, lasttimearray, soundarray, unreadarray, activeArarray, userchatavatararray, mContext, year, month, day, il, view);
        chatroomslist.setAdapter(mAdapter);

        try {
            if ( !chatroomidarray.isEmpty()) {
                Runnable fitsOnScreen = new Runnable() {

                    @Override
                    public void run() {
                        int last = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
                        if (last == chatroomslist.getAdapter().getItemCount() - 1 && chatroomslist.getChildAt(last).getBottom() <= chatroomslist.getHeight()) {
                            // It fits!
                            onScrollListener = false;
                        } else {
                            // It doesn't fit...
                            onScrollListener = true;
                        }
                    }
                };

                chatroomslist.post(fitsOnScreen);

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
            G.messagingPageNumber = 2;
            seenOtherFragment = true;
        } else {
            reloadListView = true;
        }
    }


    private void initUI() {
        reloadListView = false;
        il = new ImageLoader1(getActivity(), G.basicAuth);
        chatroomslist = (RecyclerView) view.findViewById(R.id.lv_message_chat);
        chatroomslist.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        chatroomslist.setLayoutManager(mLayoutManager);
        lytEmpty = (LinearLayout) view.findViewById(R.id.lyt_empty);
        il = new ImageLoader1(getActivity(), G.basicAuth);

        TimerServies ts = new TimerServies();
        String currenttime;
        try {

            currenttime = ts.getDateTime();
        }
        catch (Exception e) {
            HelperGetTime helperGetTime = new HelperGetTime();
            currenttime = helperGetTime.getTime();
        }
        String[] splitedtime = currenttime.split("\\s+");
        String date = splitedtime[0];

        String[] splited_date = date.split("-");
        year = splited_date[0];
        month = splited_date[1];
        day = splited_date[2];

        chatroomslist.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (G.messagingPageNumber == 2 && onScrollListener) {

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

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, new IntentFilter("loadinfo"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateSeenChat, new IntentFilter("updateSeenChat"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(cleareWithUidChat, new IntentFilter("cleareWithUidChat"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(deleteWithUidChat, new IntentFilter("deleteWithUidChat"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateSoundWithUidChat, new IntentFilter("updateSoundWithUidChat"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(NewPostChat, new IntentFilter("NewPostChat"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(NewRoomChat, new IntentFilter("NewRoomChat"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(deletedAccountChat, new IntentFilter("deletedAccountChat"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(emptyListChat, new IntentFilter("EMPTY_LIST_CHAT"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateAvatarChat, new IntentFilter("updateAvatarChat"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(newContactChat, new IntentFilter("newContact"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(UpdateContactUserAvatar, new IntentFilter("UpdateContactUserAvatar"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateDateType, new IntentFilter("updateDateType"));
    }


    private void showDummy() {
        chatroomslist.setVisibility(View.GONE);
        lytEmpty.setVisibility(View.VISIBLE);
    }


    public void updateseen(String uid) {
        mAdapter.updateSeen(uid);
    }


    public void clearewithuid(String uid) {
        mAdapter.clearewithuid(uid);
    }


    public void deletewithuid(String uid) {
        mAdapter.deleteWithUid(uid);
    }


    public void updatesoundwithuid(String uid, String value) {
        mAdapter.updateSoundWithUid(uid, value);
    }


    public void newcontactchat(String name, String userchat) {
        mAdapter.newContact(name, userchat);
    }


    public void updateContactAvatar(String userchat) {
        mAdapter.updateContactAvatar(userchat);
    }


    public void newpostchat(String uid, String lastmsg, String lastdate) {
        mAdapter.newPost(uid, lastmsg, lastdate);
    }


    public void newroomchat(String roomid, String userchat, String lastmessage, String lasttime, String sound, String active, String userchatavatar) {
        if (mAdapter.getItemCount() == 0) {
            chatroomslist.setVisibility(View.VISIBLE);
            lytEmpty.setVisibility(View.GONE);
        }
        mAdapter.insert(roomid, userchat, lastmessage, lasttime, sound, active, userchatavatar);
    }


    public void deletedaccount(String uid, String lastmsg, String lastdate) {
        mAdapter.deletedAccount(uid, lastmsg, lastdate);
    }

}
