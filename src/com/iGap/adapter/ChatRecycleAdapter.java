// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.adapter;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.iGap.R;
import com.iGap.Singlechat;
import com.iGap.fragments.PageMessagingChats;
import com.iGap.helpers.HelperDrawAlphabet;
import com.iGap.helpers.HelperGetDataFromOtherApp;
import com.iGap.helpers.HelperGetTime;
import com.iGap.helpers.PageMessagingPopularFunction;
import com.iGap.instruments.ConfirmationDialog;
import com.iGap.instruments.ImageLoader1;
import com.iGap.interfaces.OnColorChangedListenerSelect;


/**
 * 
 * adapter baraye fragmente {@link PageMessagingChats} ke list chatha ra baraye namayesh amade sazi mikonad
 *
 */

public class ChatRecycleAdapter extends RecyclerView.Adapter<ChatRecycleAdapter.ViewHolder> {

    private ArrayList<String>      chatroomidarray;
    private ArrayList<String>      userchatarray;
    private ArrayList<String>      userchatnamearray;
    private ArrayList<String>      lastmessagearray;
    private ArrayList<String>      lasttimearray;
    private ArrayList<String>      soundarray;
    private ArrayList<String>      unreadarray;
    private ArrayList<String>      activeArarray;
    private ArrayList<String>      userchatavatararray;
    private ArrayList<Boolean>     visibleView;

    private int                    lastPosition = 99999;
    private boolean                islong       = false;

    private String                 day;
    private String                 month;
    private String                 year;
    private String                 hijri;

    private ImageLoader1           il;
    private SpannableStringBuilder buildertext;
    private ViewHolder             viewholder;
    private View                   mainView;
    private ConfirmationDialog     cm;
    private Context                mContext;


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView     txourchannel;
        public TextView     txchanneldes;
        public TextView     txticon;
        public TextView     txtname;
        public TextView     txtLastMessage;
        public TextView     txtsound;
        public TextView     txtLastSeen;
        public TextView     txtunread;
        public TextView     txtIconSwip;

        public Button       btnDelete;
        public Button       btnAlarm;
        public Button       btnclearhistory;

        public LinearLayout firstLayout;
        public LinearLayout secondLayout;

        public ImageView    imgAvatar;
        public View         roww;


        public ViewHolder(View row) {
            super(row);
            roww = row;

            //=======layout
            firstLayout = (LinearLayout) row.findViewById(R.id.ll_first_chat);
            secondLayout = (LinearLayout) row.findViewById(R.id.ll_second_chat);

            //=======ImageView
            imgAvatar = (ImageView) row.findViewById(R.id.img_avatar_chat);

            //======TextView
            txticon = (TextView) row.findViewById(R.id.txt_icon_chat);
            txtname = (TextView) row.findViewById(R.id.txt_name_chat);
            txtLastMessage = (TextView) row.findViewById(R.id.txt_last_message_chat);
            txtsound = (TextView) row.findViewById(R.id.txt_sound_chat);
            txtLastSeen = (TextView) row.findViewById(R.id.txt_last_seen_chat);
            txtunread = (TextView) row.findViewById(R.id.txt_unread_chat);
            txourchannel = (TextView) row.findViewById(R.id.tx_our_channel_chat);
            txchanneldes = (TextView) row.findViewById(R.id.tx_channel_des_chat);
            txtIconSwip = (TextView) row.findViewById(R.id.tx_swip_icon_chat);

            //=======Button
            btnAlarm = (Button) row.findViewById(R.id.btn_alarm_chat);
            btnclearhistory = (Button) row.findViewById(R.id.btn_clearhistory_chat);
            btnDelete = (Button) row.findViewById(R.id.btn_delete5_chat);

            //=========TypeFace
            txtIconSwip.setTypeface(G.fontAwesome);
            txticon.setTypeface(G.fontAwesome);
            txtLastMessage.setTypeface(G.robotoLight);
            txtLastSeen.setTypeface(G.robotoLight);
            txtunread.setTypeface(G.robotoLight);
            txourchannel.setTypeface(G.robotoBold);
            txchanneldes.setTypeface(G.robotoLight);
            btnAlarm.setTypeface(G.fontAwesome);
            btnclearhistory.setTypeface(G.fontAwesome);
            btnDelete.setTypeface(G.fontAwesome);

        }
    }


    public ChatRecycleAdapter(ArrayList<String> chatroomidarray1, ArrayList<String> userchatarray1, ArrayList<String> userchatnamearray1, ArrayList<String> lastmessagearray1, ArrayList<String> lasttimearray1
                              , ArrayList<String> soundarray1, ArrayList<String> unreadarray1, ArrayList<String> activeArarray1, ArrayList<String> userchatavatararray1
                              , Context context, String year1, String month1, String day1, ImageLoader1 il1, View view) {
        mContext = context;
        chatroomidarray = chatroomidarray1;
        userchatarray = userchatarray1;
        userchatnamearray = userchatnamearray1;
        lastmessagearray = lastmessagearray1;
        lasttimearray = lasttimearray1;
        soundarray = soundarray1;
        unreadarray = unreadarray1;
        activeArarray = activeArarray1;
        userchatavatararray = userchatavatararray1;
        year = year1;
        month = month1;
        day = day1;
        il = il1;
        hijri = G.hijriDate;
        mainView = view;
        visibleView = new ArrayList<Boolean>();
        for (int i = 0; i < chatroomidarray1.size(); i++) {
            visibleView.add(false);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chats, parent, false);
        viewholder = new ViewHolder(v);
        return viewholder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder h, final int position) {

        if (visibleView.get(position) == true) {
            viewOn(h, position);
        } else {
            viewOff(h, position);
        }

        mainView.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        Boolean finish = true;

                        for (int i = 0; i < visibleView.size(); i++) {
                            if (visibleView.get(i)) {
                                islong = false;
                                finish = false;
                                viewOff(viewholder, i);

                            }
                        }

                        try {
                            if (finish)
                                ((Activity) mContext).finish();
                        }
                        catch (Exception e) {}

                        return true;
                    }
                }
                return false;
            }
        });

        if (soundarray.get(position).equals("0")) {
            h.txtsound.setVisibility(View.GONE);
        } else {
            h.txtsound.setVisibility(View.VISIBLE);
            h.txtsound.setText(R.string.fa_bell);
            h.txtsound.setTypeface(G.fontAwesome);
        }

        if (unreadarray.get(position).equals("0")) {
            h.txtunread.setVisibility(View.INVISIBLE);
        } else {
            h.txtunread.setVisibility(View.VISIBLE);
            h.txtunread.setText(unreadarray.get(position));
        }

        buildertext = parseText(lastmessagearray.get(position));

        h.txtLastMessage.setText(buildertext);
        if (lasttimearray.get(position) != null && !lasttimearray.get(position).isEmpty() && !lasttimearray.get(position).equals("null") && !lasttimearray.get(position).equals("")) {

            h.txtLastSeen.setText(HelperGetTime.getLastSeen(lasttimearray.get(position), year, month, day));

        } else {
            h.txtLastMessage.setText("");
            h.txtLastSeen.setText("");
        }
        h.txtname.setText(userchatnamearray.get(position));

        if (userchatavatararray.get(position) != null && !userchatavatararray.get(position).isEmpty() && !userchatavatararray.get(position).equals("null") && !userchatavatararray.get(position).equals("") && !userchatavatararray.get(position).equals("empty")) {
            //bere vase loade folan
            il.DisplayImage(userchatavatararray.get(position), R.drawable.difaultimage, h.imgAvatar);

        } else {

            HelperDrawAlphabet pf = new HelperDrawAlphabet();
            Bitmap bm = pf.drawAlphabet(mContext, userchatnamearray.get(position), h.imgAvatar);

            h.imgAvatar.setImageBitmap(bm);
        }

        h.txourchannel.setText(userchatnamearray.get(position));
        h.txchanneldes.setText(buildertext);

        if (soundarray.get(position).equals("1")) {
            h.btnAlarm.setText(mContext.getResources().getString(R.string.fa_bell_o));
        }
        else {
            h.btnAlarm.setText(mContext.getResources().getString(R.string.fa_bell));
        }

        h.btnAlarm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (soundarray.get(position).equals("1")) {
                    updatesound(position, "0");
                } else {
                    updatesound(position, "1");
                }

                visibleView.set(position, false);
                islong = false;

            }
        });
        h.btnclearhistory.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                updatePageAll(userchatarray.get(position));
                clearehistory(position, "", "");
                visibleView.set(position, false);
                islong = false;

            }
        });
        h.btnDelete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                cm = new ConfirmationDialog(PageMessagingChats.mcContext, new OnColorChangedListenerSelect() {

                    @Override
                    public void colorChanged(String key, int color) {

                    }


                    @Override
                    public void Confirmation(Boolean result) {
                        if (result) {
                            deleteChatFromAll(userchatarray.get(position));
                            delete(position, h);
                        }
                    }
                });

                cm.showdialog(G.context.getString(R.string.do_you_want_delete_this_chat));
            }
        });

        h.roww.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                if (lastPosition <= visibleView.size()) {
                    if (lastPosition != position && visibleView.get(lastPosition) == true) {
                        islong = false;
                        viewOff(viewholder, lastPosition);
                    }
                }
                viewOn(h, position);
                lastPosition = position;
                viewholder = h;
                return false;
            }
        });
        h.roww.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (islong == true) {
                    if (lastPosition <= visibleView.size()) {
                        if (lastPosition != position && visibleView.get(lastPosition) == true) {
                            islong = false;
                            viewOff(viewholder, lastPosition);
                        }
                    }
                } else {
                    if (HelperGetDataFromOtherApp.hasSharedData) {
                        cm = new ConfirmationDialog(PageMessagingChats.mcContext, new OnColorChangedListenerSelect() {

                            @Override
                            public void colorChanged(String key, int color) {

                            }


                            @Override
                            public void Confirmation(Boolean result) {
                                if (result) {
                                    startSingleChat(position);
                                }
                            }
                        });
                        cm.showdialog(G.context.getString(R.string.share_data_chat));
                    } else {
                        startSingleChat(position);
                    }

                }
            }
        });
    }


    private void startSingleChat(int position) {
        Intent intent = new Intent(mContext, Singlechat.class);
        intent.putExtra("chatroomid", chatroomidarray.get(position));
        intent.putExtra("userchat", userchatarray.get(position));
        intent.putExtra("userchatname", userchatnamearray.get(position));
        intent.putExtra("userchatavatar", userchatavatararray.get(position));
        intent.putExtra("active", activeArarray.get(position));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }


    @Override
    public int getItemCount() {
        return chatroomidarray.size();
    }


    private void viewOn(ViewHolder h, int position) {
        PageMessagingPopularFunction pm = new PageMessagingPopularFunction();
        pm.viewOn(h.firstLayout, h.secondLayout, position);
        visibleView.set(position, true);
        islong = true;
    }


    private void viewOff(ViewHolder h, int position) {
        visibleView.set(position, false);
        PageMessagingPopularFunction pm = new PageMessagingPopularFunction();
        pm.viewOff(h.firstLayout, h.secondLayout, position);
    }


    public void insert(String roomid, String userchat, String lastmessage, String lasttime, String sound, String active, String userchatavatar) {

        int exist = chatroomidarray.indexOf(roomid);
        if (exist == -1) {

            sendOtherItemToBottom();

            chatroomidarray.set(0, roomid);
            userchatarray.set(0, userchat);

            String mobile = userchat.split("@")[0];
            String userchatname;
            try {
                userchatname = G.cmd.namayeshname(1, mobile);
            }
            catch (Exception e) {
                userchatname = mobile;
            }

            userchatnamearray.set(0, userchatname);
            lastmessagearray.set(0, lastmessage);
            lasttimearray.set(0, lasttime);
            soundarray.set(0, sound);
            activeArarray.set(0, active);
            userchatavatararray.set(0, userchatavatar);

            int unread = G.cmd.getRowCountunreadchat(roomid);
            unreadarray.set(0, "" + unread);
            visibleView.set(0, false);
            notif();
        }

    }


    private void sendOtherItemToBottom() {//Insert item to first position

        //====add one item in all array
        chatroomidarray.add("");
        userchatnamearray.add("");
        userchatarray.add("");
        lastmessagearray.add("");
        lasttimearray.add("");
        soundarray.add("");
        activeArarray.add("");
        userchatavatararray.add("");
        unreadarray.add("");
        visibleView.add(false);

        notifyItemInserted((chatroomidarray.size() - 1));

        String mainSaveId = chatroomidarray.get(0);
        String mainSaveName = userchatnamearray.get(0);
        String mainSaveUserChat = userchatarray.get(0);
        String mainSaveAvatarLq = userchatavatararray.get(0);
        String mainSaveLastMsg = lastmessagearray.get(0);
        String mainSaveLastDate = lasttimearray.get(0);
        String mainSaveSound = soundarray.get(0);
        String mainSaveActive = activeArarray.get(0);
        String mainSaveUnread = unreadarray.get(0);

        String saveId = "";
        String saveName = "";
        String saveUserChat = "";
        String saveAvatarLq = "";
        String saveLastMsg = "";
        String saveLastDate = "";
        String saveSound = "";
        String saveActive = "";
        String saveUnread = "";

        for (int i = 1; i < chatroomidarray.size(); i++) {
            //====zakhire item feli

            if (i != chatroomidarray.size()) {
                saveId = chatroomidarray.get(i);
                saveName = userchatnamearray.get(i);
                saveUserChat = userchatarray.get(i);
                saveAvatarLq = userchatavatararray.get(i);
                saveLastMsg = lastmessagearray.get(i);
                saveLastDate = lasttimearray.get(i);
                saveSound = soundarray.get(i);
                saveActive = activeArarray.get(i);
                saveUnread = unreadarray.get(i);
            }

            //====varize item ghabl be item feli 
            chatroomidarray.set(i, mainSaveId);
            userchatnamearray.set(i, mainSaveName);
            userchatarray.set(i, mainSaveUserChat);
            userchatavatararray.set(i, mainSaveAvatarLq);
            lastmessagearray.set(i, mainSaveLastMsg);
            lasttimearray.set(i, mainSaveLastDate);
            soundarray.set(i, mainSaveSound);
            activeArarray.set(i, mainSaveActive);
            unreadarray.set(i, mainSaveUnread);

            if (i != chatroomidarray.size()) {
                //====enteghale item feli ke ghablan zakhire shode bud be mainSave
                mainSaveId = saveId;
                mainSaveName = saveName;
                mainSaveUserChat = saveUserChat;
                mainSaveAvatarLq = saveAvatarLq;
                mainSaveLastMsg = saveLastMsg;
                mainSaveLastDate = saveLastDate;
                mainSaveSound = saveSound;
                mainSaveActive = saveActive;
                mainSaveUnread = saveUnread;
            }
        }
    }


    public void newPost(String uid, String lastmsg, String lastdate) {

        int pos = userchatarray.indexOf(uid);
        if (pos != -1) {
            lasttimearray.set(pos, lastdate);
            lastmessagearray.set(pos, lastmsg);
            int unread = G.cmd.getRowCountunreadchat(chatroomidarray.get(pos));
            unreadarray.set(pos, String.valueOf(unread));

            itemMoveToFirst(pos);
        }

    }


    public void newContact(String name, String userchat) {

        int pos = userchatarray.indexOf(userchat);
        if (pos != -1) {
            userchatnamearray.set(pos, name);
            notifyItemChanged(pos);
        }
    }


    public void updateContactAvatar(String userchat) {
        int pos = userchatarray.indexOf(userchat);
        if (pos != -1) {
            notifyItemChanged(pos);
        }
    }


    private void itemMoveToFirst(int changedPosition) {
        for (int i = changedPosition; i > 0; i--) {

            String firstUid = chatroomidarray.get(i);
            String firstName = userchatnamearray.get(i);
            String firstUserChat = userchatarray.get(i);
            String firstAvatarLq = userchatavatararray.get(i);
            String firstLastMsg = lastmessagearray.get(i);
            String firstLastDate = lasttimearray.get(i);
            String firstSound = soundarray.get(i);
            String firstActive = activeArarray.get(i);
            String firstUnread = unreadarray.get(i);

            String secondUid = chatroomidarray.get(i - 1);
            String secondName = userchatnamearray.get(i - 1);
            String secondUserChat = userchatarray.get(i - 1);
            String secondAvatarLq = userchatavatararray.get(i - 1);
            String secondLastMsg = lastmessagearray.get(i - 1);
            String secondLastDate = lasttimearray.get(i - 1);
            String secondSound = soundarray.get(i - 1);
            String secondActive = activeArarray.get(i - 1);
            String secondUnread = unreadarray.get(i - 1);

            chatroomidarray.set(i, secondUid);
            userchatnamearray.set(i, secondName);
            userchatarray.set(i, secondUserChat);
            userchatavatararray.set(i, secondAvatarLq);
            lastmessagearray.set(i, secondLastMsg);
            lasttimearray.set(i, secondLastDate);
            soundarray.set(i, secondSound);
            activeArarray.set(i, secondActive);
            unreadarray.set(i, secondUnread);

            chatroomidarray.set((i - 1), firstUid);
            userchatnamearray.set((i - 1), firstName);
            userchatarray.set((i - 1), firstUserChat);
            userchatavatararray.set((i - 1), firstAvatarLq);
            lastmessagearray.set((i - 1), firstLastMsg);
            lasttimearray.set((i - 1), firstLastDate);
            soundarray.set((i - 1), firstSound);
            activeArarray.set((i - 1), firstActive);
            unreadarray.set((i - 1), firstUnread);
        }
        notifyDataSetChanged();
    }


    public void updateSoundWithUid(String uid, String value) {

        int pos = userchatarray.indexOf(uid);
        if (pos != -1) {
            G.cmd.updatechatsound(chatroomidarray.get(pos), value);
            soundarray.set(pos, value);
            notifyItemChanged(pos);
        }

    }


    public void deletedAccount(String uid, String lastmsg, String lastdate) {

        int pos = userchatarray.indexOf(uid);
        if (pos != -1) {
            lastmessagearray.set(pos, lastmsg);
            lasttimearray.set(pos, lastdate);
            int unread = G.cmd.getRowCountunreadchat(chatroomidarray.get(pos));
            unreadarray.set(pos, String.valueOf(unread));
            activeArarray.set(pos, "2");
            notifyDataSetChanged();
        }

    }


    public void updateAvatar(String userChat, String avatar) {

        int pos = userchatarray.indexOf(userChat);
        if (pos != -1) {

            userchatavatararray.set(pos, avatar);
            notifyDataSetChanged();
        }

    }


    public void updateSeen(String uid) {

        int pos = userchatarray.indexOf(uid);
        if (pos != -1) {
            int unread = G.cmd.getRowCountunreadchat(chatroomidarray.get(pos));
            unreadarray.set(pos, String.valueOf(unread));
            notifyItemChanged(pos);
        }

    }


    private void updatePageAll(String userchat) {
        Intent intentAll = new Intent("clearHistoryAll");
        intentAll.putExtra("MODEL", "1");
        intentAll.putExtra("UID", userchat);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intentAll);
    }


    private SpannableStringBuilder parseText(String text) {
        PageMessagingPopularFunction pm = new PageMessagingPopularFunction();
        SpannableStringBuilder builder = pm.parseText(text, "PAGE_MESSAGING_LIST", false, mContext, 0);
        return builder;
    }


    private void updatesound(int pos, String value) {

        updateSoundAllFromChat(userchatarray.get(pos), value);
        G.cmd.updatechatsound(chatroomidarray.get(pos), value);
        soundarray.set(pos, value);
        notifyItemChanged(pos);
        islong = false;
        viewOff(viewholder, pos);
    }


    private void deleteChatFromAll(String userchat) {
        Intent intentAll = new Intent("deleteChatFromAll");
        intentAll.putExtra("MODEL", "1");
        intentAll.putExtra("UID", userchat);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intentAll);
    }


    private void updateSoundAllFromChat(String Uid, String Value) {
        Intent intentAll = new Intent("updateSoundAll");
        intentAll.putExtra("MODEL", "1");
        intentAll.putExtra("UID", Uid);
        intentAll.putExtra("VALUE", Value);

        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intentAll);
    }


    private void clearehistory(int pos, String lastMsgDate, String lastMsg) {
        G.cmd.clearchathistory(chatroomidarray.get(pos));
        G.cmd.updatechatrooms(userchatarray.get(pos), lastMsgDate, lastMsg);

        lasttimearray.set(pos, lastMsgDate);
        lastmessagearray.set(pos, lastMsg);
        int unread = G.cmd.getRowCountunreadchat(chatroomidarray.get(pos));
        unreadarray.set(pos, String.valueOf(unread));
        notifyItemChanged(pos);
        islong = false;
        viewOff(viewholder, pos);
    }


    private void delete(int pos, ViewHolder h) {
        G.cmd.deletechatrooms(chatroomidarray.get(pos));
        islong = false;
        viewOff(h, pos);
        chatroomidarray.remove(pos);
        userchatarray.remove(pos);
        userchatnamearray.remove(pos);
        lastmessagearray.remove(pos);
        lasttimearray.remove(pos);
        soundarray.remove(pos);
        unreadarray.remove(pos);
        activeArarray.remove(pos);
        userchatavatararray.remove(pos);
        visibleView.remove(pos);
        lastPosition = 9999;

        if (chatroomidarray.size() == 0) {
            Intent intent = new Intent("EMPTY_LIST_CHAT");
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        }

        notif();
    }


    public void deleteWithUid(String uid) {

        int pos = userchatarray.indexOf(uid);

        if (pos != -1) {
            chatroomidarray.remove(pos);
            userchatarray.remove(pos);
            userchatnamearray.remove(pos);
            lastmessagearray.remove(pos);
            lasttimearray.remove(pos);
            soundarray.remove(pos);
            unreadarray.remove(pos);
            activeArarray.remove(pos);
            userchatavatararray.remove(pos);
            visibleView.remove(pos);

            if (chatroomidarray.size() == 0) {
                Intent intent = new Intent("EMPTY_LIST_CHAT");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
            }

            notif();
        }

    }


    public void clearewithuid(String uid) {

        int pos = userchatarray.indexOf(uid);
        if (pos != -1) {
            lasttimearray.set(pos, "");
            lastmessagearray.set(pos, "");
            notifyItemChanged(pos);
        }

    }


    private void notif() {
        notifyDataSetChanged();
    }


    public void changeDateType(String value) {
        hijri = value;

        for (int i = 0; i < lasttimearray.size(); i++) {

            if (lasttimearray.get(i) != null && !lasttimearray.get(i).isEmpty() && !lasttimearray.get(i).equals("null") && !lasttimearray.get(i).equals("")) {
                String[] splitedrowtime = lasttimearray.get(i).split("\\s+");
                String date = splitedrowtime[0];
                String[] splited_currentpositiondate = date.split("-");
                String currentpositionyear = splited_currentpositiondate[0];
                String currentpositionmonth = splited_currentpositiondate[1];
                String currentpositionday = splited_currentpositiondate[2];
                int intcurrentpositionyear = Integer.parseInt(currentpositionyear);
                int intcurrentpositionmonth = Integer.parseInt(currentpositionmonth);
                int intcurrentpositionday = Integer.parseInt(currentpositionday);
                if (Integer.parseInt(year) > intcurrentpositionyear || Integer.parseInt(month) > intcurrentpositionmonth || Integer.parseInt(day) > intcurrentpositionday) {
                    notifyItemChanged(i);
                }
            }
        }
    }
}