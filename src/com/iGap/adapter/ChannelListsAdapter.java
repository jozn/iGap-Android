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
import com.iGap.Channel;
import com.iGap.R;
import com.iGap.fragments.PageMessagingChanneles;
import com.iGap.fragments.PageMessagingChats;
import com.iGap.helpers.HelperCalculateMembers;
import com.iGap.helpers.HelperDrawAlphabet;
import com.iGap.helpers.HelperGetDataFromOtherApp;
import com.iGap.helpers.HelperGetTime;
import com.iGap.helpers.PageMessagingPopularFunction;
import com.iGap.instruments.ConfirmationDialog;
import com.iGap.instruments.ImageLoader1;
import com.iGap.interfaces.OnColorChangedListenerSelect;
import com.iGap.interfaces.OnDeleteComplete;


/**
 * 
 * adapter for use in recycle view in page channel that show a list of chat item
 *
 */

public class ChannelListsAdapter extends RecyclerView.Adapter<ChannelListsAdapter.ViewHolder> {

    private ArrayList<String>      channeluidarray;
    private ArrayList<String>      channelnamearray;
    private ArrayList<String>      channeldescriptionarray;
    private ArrayList<String>      channelmembersnumberarray;
    private ArrayList<String>      channelavatar_lqarray;
    private ArrayList<String>      channelavatar_hqarray;
    private ArrayList<String>      channellastmessagearray;
    private ArrayList<String>      channellastdatearray;
    private ArrayList<String>      channelsoundarray;
    private ArrayList<String>      channelmembershiparray;
    private ArrayList<String>      channelactivearray;
    private ArrayList<String>      channelunreadarray;
    private ArrayList<String>      channelMsgIdArray;
    private ArrayList<Boolean>     visibleView;

    private String                 hijri;
    private String                 day;
    private String                 month;
    private String                 year;

    private int                    lastPosition = 99999;
    private boolean                islong       = false;
    private boolean                first        = true;

    private ImageLoader1           il;
    private SpannableStringBuilder buildertext;
    private ViewHolder             viewholder;
    private View                   mainView;
    private ChannelAdapter         ChA;
    private Context                mContext;

    private ConfirmationDialog     cm;


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView     txourchannel;
        public TextView     txtChannelIcon;
        public TextView     txfirstpersent;
        public TextView     txchanneldes;
        public TextView     txticon;
        public TextView     txtname;
        public TextView     txtmembers;
        public TextView     txtdesc;
        public TextView     txtlast_message;
        public TextView     txtsound;
        public TextView     txtlast_seen;
        public TextView     txtunread;

        public Button       btnDelete;
        public Button       btnAlarm;
        public Button       btnclearhistory;

        public LinearLayout firstLayout;
        public LinearLayout secondLayout;

        public ImageView    img_avatar;
        public View         row;


        public ViewHolder(View v) {
            super(v);
            row = v;

            //=======layout
            firstLayout = (LinearLayout) row.findViewById(R.id.ll_first_channel);
            secondLayout = (LinearLayout) row.findViewById(R.id.ll_second_channel);
            //=======ImageView
            img_avatar = (ImageView) row.findViewById(R.id.img_avatar_channel);

            //======TextView
            txticon = (TextView) row.findViewById(R.id.txt_icon_channel);
            txtname = (TextView) row.findViewById(R.id.txt_name_channel);
            txtmembers = (TextView) row.findViewById(R.id.txt_members_channel);
            txtdesc = (TextView) row.findViewById(R.id.txt_desc_channel);
            txtlast_message = (TextView) row.findViewById(R.id.txt_last_message_channel);
            txtsound = (TextView) row.findViewById(R.id.txt_sound_channel);
            txtlast_seen = (TextView) row.findViewById(R.id.txt_last_seen_channel);
            txtunread = (TextView) row.findViewById(R.id.txt_unread_channel);
            txchanneldes = (TextView) row.findViewById(R.id.tx_channel_des_channel);
            txfirstpersent = (TextView) row.findViewById(R.id.tx_first_persent_channel);
            txtChannelIcon = (TextView) row.findViewById(R.id.tx_channel_icon_channel);
            txourchannel = (TextView) row.findViewById(R.id.tx_our_channel_channel);

            //=======Button
            btnAlarm = (Button) row.findViewById(R.id.btn_alarm_channel);
            btnclearhistory = (Button) row.findViewById(R.id.btn_clearhistory_channel);
            btnDelete = (Button) row.findViewById(R.id.btn_delete5_channel);

            //=========TypeFace
            txtlast_message.setTypeface(G.robotoLight);
            txtlast_seen.setTypeface(G.robotoLight);
            txtunread.setTypeface(G.robotoLight);
            txchanneldes.setTypeface(G.robotoLight);
            txfirstpersent.setTypeface(G.robotoLight);
            btnAlarm.setTypeface(G.fontAwesome);
            btnclearhistory.setTypeface(G.fontAwesome);
            btnDelete.setTypeface(G.fontAwesome);
            txticon.setTypeface(G.fontAwesome);
            txtChannelIcon.setTypeface(G.fontAwesome);

        }
    }


    public ChannelListsAdapter(ArrayList<String> channeluidarray1, ArrayList<String> channelnamearray1, ArrayList<String> channeldescriptionarray1, ArrayList<String> channelmembersnumberarray1, ArrayList<String> channelavatar_lqarray1, ArrayList<String> channelavatar_hqarray1
                               , ArrayList<String> channellastmessagearray1, ArrayList<String> channellastdatearray1, ArrayList<String> channelsoundarray1, ArrayList<String> channelmembershiparray1
                               , ArrayList<String> channelactivearray1, ArrayList<String> channelunreadarray1, Context context, String year1, String month1, String day1, ImageLoader1 il1, View view) {
        mContext = context;
        channeluidarray = channeluidarray1;
        channelnamearray = channelnamearray1;
        channeldescriptionarray = channeldescriptionarray1;
        channelmembersnumberarray = channelmembersnumberarray1;
        channelavatar_lqarray = channelavatar_lqarray1;
        channelavatar_hqarray = channelavatar_hqarray1;
        channellastmessagearray = channellastmessagearray1;
        channellastdatearray = channellastdatearray1;
        channelsoundarray = channelsoundarray1;
        channelmembershiparray = channelmembershiparray1;
        channelactivearray = channelactivearray1;
        channelunreadarray = channelunreadarray1;
        year = year1;
        month = month1;
        day = day1;
        il = il1;

        hijri = G.hijriDate;

        visibleView = new ArrayList<Boolean>();
        channelMsgIdArray = new ArrayList<String>();
        for (int i = 0; i < channeluidarray.size(); i++) {
            visibleView.add(false);
            channelMsgIdArray.add(""); // zamani ke payame jadid daryaft shod msgID zakhire shode va newPost tekrar nemishavad
        }
        mainView = view;
        ChA = new ChannelAdapter(mContext);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_channels, parent, false);
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
                                viewOff(viewholder, lastPosition);
                                break;
                            }
                        }

                        try {
                            if (finish)
                                ((Activity) mContext).finish();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }

                        return true;
                    }
                }
                return false;
            }
        });

        h.txtdesc.setText(channeldescriptionarray.get(position));

        HelperCalculateMembers cp = new HelperCalculateMembers();
        String members = cp.calculateMembers(channelmembersnumberarray.get(position));

        h.txtmembers.setText(members);
        if (channelunreadarray.get(position).equals("0")) {
            h.txtunread.setVisibility(View.INVISIBLE);
        } else {
            h.txtunread.setVisibility(View.VISIBLE);
            h.txtunread.setText(channelunreadarray.get(position));
        }

        if (channellastdatearray.get(position) != null && !channellastdatearray.get(position).isEmpty() && !channellastdatearray.get(position).equals("null") && !channellastdatearray.get(position).equals("")) {

            h.txtlast_seen.setText(HelperGetTime.getLastSeen(channellastdatearray.get(position), year, month, day));

        } else {
            h.txtlast_seen.setText("");
        }

        if (channelsoundarray.get(position).equals("0")) {
            h.txtsound.setVisibility(View.GONE);
        } else {
            h.txtsound.setVisibility(View.VISIBLE);
            h.txtsound.setText(R.string.fa_bell);
            h.txtsound.setTypeface(G.fontAwesome);
        }

        if (channelavatar_lqarray.get(position) != null && !channelavatar_lqarray.get(position).isEmpty() && !channelavatar_lqarray.get(position).equals("null") && !channelavatar_lqarray.get(position).equals("")) {
            il.DisplayImage(channelavatar_lqarray.get(position), R.drawable.difaultimage, h.img_avatar);
        } else {
            HelperDrawAlphabet pf = new HelperDrawAlphabet();
            Bitmap bm = pf.drawAlphabet(mContext, channelnamearray.get(position), h.img_avatar);
            h.img_avatar.setImageBitmap(bm);

        }
        buildertext = parseText(channellastmessagearray.get(position));
        h.txtlast_message.setText(buildertext);
        h.txtname.setText(channelnamearray.get(position));
        h.txourchannel.setText(channelnamearray.get(position));
        h.txchanneldes.setText(channeldescriptionarray.get(position));
        h.txfirstpersent.setText(members);

        if (channelsoundarray.get(position).equals("1")) {
            h.btnAlarm.setText(mContext.getResources().getString(R.string.fa_bell_o));
        }
        else {
            h.btnAlarm.setText(mContext.getResources().getString(R.string.fa_bell));
        }

        h.btnAlarm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (channelsoundarray.get(position).equals("1")) {
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
                clearehistory(position, "", "");
                visibleView.set(position, false);
                islong = false;
            }
        });
        h.btnDelete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                final String uid = channeluidarray.get(position);
                final String userState = G.cmd.selectUserChannelState(uid);
                if (userState != null) {
                    ConfirmationDialog cm = new ConfirmationDialog(PageMessagingChanneles.mcContext, new OnColorChangedListenerSelect() {

                        @Override
                        public void colorChanged(String key, int color) {}


                        @Override
                        public void Confirmation(Boolean result) {
                            if (result) {
                                ChA.deleteChannel(userState, uid, new OnDeleteComplete() {

                                    @Override
                                    public void deleteComplete(Boolean result) {
                                        if (result) {
                                            deleteChannelFromAll(channeluidarray.get(position));
                                            delete(position, h);
                                        }

                                    }
                                });
                            }
                        }
                    });
                    cm.showdialog(G.context.getString(R.string.do_you_want_delete_this_channel));
                }
            }
        });

        h.row.setOnLongClickListener(new OnLongClickListener() {

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
        h.row.setOnClickListener(new OnClickListener() {

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
                                    startChannel(position);
                                }
                            }
                        });
                        cm.showdialog(G.context.getString(R.string.share_data_channel));
                    } else {
                        startChannel(position);
                    }

                }
            }
        });

    }


    private void startChannel(int position) {
        Intent intent = new Intent(mContext, Channel.class);
        intent.putExtra("channeluid", channeluidarray.get(position));
        intent.putExtra("channelName", channelnamearray.get(position));
        intent.putExtra("channelavatarlq", channelavatar_lqarray.get(position));
        intent.putExtra("channelavatarhq", channelavatar_hqarray.get(position));
        intent.putExtra("channelmembership", channelmembershiparray.get(position));
        intent.putExtra("channelDesc", channeldescriptionarray.get(position));
        intent.putExtra("channelmembersnumber", channelmembersnumberarray.get(position));
        intent.putExtra("channelactive", channelactivearray.get(position));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        Intent intent = new Intent("Dismis_dialog");
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        super.onAttachedToRecyclerView(recyclerView);
    }


    @Override
    public int getItemCount() {
        return channeluidarray.size();
    }


    private SpannableStringBuilder parseText(String text) {
        PageMessagingPopularFunction pm = new PageMessagingPopularFunction();
        SpannableStringBuilder builder = pm.parseText(text, "PAGE_MESSAGING_LIST", false, mContext, 0);
        return builder;
    }


    private void updatesound(int pos, String value) {
        updateSoundAllFromGroupChannel(channeluidarray.get(pos), value);

        G.cmd.updatechannelsound(channeluidarray.get(pos), value);
        channelsoundarray.set(pos, value);
        notifyItemChanged(pos);
        islong = false;
        viewOff(viewholder, pos);
    }


    private void updateSoundAllFromGroupChannel(String channelUid, String Value) {
        Intent intentAll = new Intent("updateSoundAll");
        intentAll.putExtra("MODEL", "3");
        intentAll.putExtra("UID", channelUid);
        intentAll.putExtra("VALUE", Value);

        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intentAll);
    }


    private void clearehistory(int pos, String lastMsgDate, String lastMsg) {
        G.cmd.clearChannelHistory(channeluidarray.get(pos));

        updateSeen(channeluidarray.get(pos));

        notifyItemChanged(pos);
        islong = false;
        viewOff(viewholder, pos);
    }


    private void viewOn(ViewHolder h, int position) {
        PageMessagingPopularFunction pm = new PageMessagingPopularFunction();
        pm.viewOn(h.firstLayout, h.secondLayout, position);
        visibleView.set(position, true);
        islong = true;

    }


    private void viewOff(ViewHolder h, int position) {
        visibleView.set(position, false);
        //islong = false;
        PageMessagingPopularFunction pm = new PageMessagingPopularFunction();
        pm.viewOff(h.firstLayout, h.secondLayout, position);
    }


    private void delete(int pos, ViewHolder h) {
        islong = false;
        viewOff(h, pos);
        channeluidarray.remove(pos);
        channelnamearray.remove(pos);
        channeldescriptionarray.remove(pos);
        channelmembersnumberarray.remove(pos);
        channelavatar_lqarray.remove(pos);
        channelavatar_hqarray.remove(pos);
        channellastmessagearray.remove(pos);
        channellastdatearray.remove(pos);
        channelsoundarray.remove(pos);
        channelmembershiparray.remove(pos);
        channelactivearray.remove(pos);
        channelunreadarray.remove(pos);
        visibleView.remove(pos);

        lastPosition = 9999;

        if (channeluidarray.size() == 0) {
            Intent intent = new Intent("EMPTY_LIST_CHANNEL");
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        }

        notif();
    }


    private void notif() {
        notifyDataSetChanged();
    }


    public void insert(String uid, String name, String description, String membersnumbers, String avatar_lq, String avatar_hq, String lastmsg, String lastdate, String membership) {

        int exist = channeluidarray.indexOf(uid);

        if (exist == -1) { // -1 means this uid not exist in channeluidarray and this channel should add to this array

            sendOtherItemToBottom();

            channeluidarray.set(0, uid);
            channelnamearray.set(0, name);
            channeldescriptionarray.set(0, description);
            channelmembersnumberarray.set(0, membersnumbers);
            channelavatar_lqarray.set(0, avatar_lq);
            channelavatar_hqarray.set(0, avatar_hq);
            channellastmessagearray.set(0, lastmsg);
            channellastdatearray.set(0, lastdate);
            channelsoundarray.set(0, "0");
            channelmembershiparray.set(0, membership);
            channelactivearray.set(0, "1");
            int unread = G.cmd.getchannelunreadsize(uid);
            channelunreadarray.set(0, String.valueOf(unread));
            visibleView.set(0, false);
            notifyDataSetChanged();
        }
    }


    private void sendOtherItemToBottom() {//Insert item to first position

        //====add one item in all array
        channeluidarray.add("");
        channelnamearray.add("");
        channeldescriptionarray.add("");
        channelmembersnumberarray.add("");
        channelavatar_lqarray.add("");
        channelavatar_hqarray.add("");
        channellastmessagearray.add("");
        channellastdatearray.add("");
        channelsoundarray.add("");
        channelmembershiparray.add("");
        channelactivearray.add("");
        channelunreadarray.add("");
        visibleView.add(false);

        notifyItemInserted((channelunreadarray.size() - 1));

        String mainSaveUid = channeluidarray.get(0);
        String mainSaveName = channelnamearray.get(0);
        String mainSaveDesc = channeldescriptionarray.get(0);
        String mainSaveMembersNumber = channelmembersnumberarray.get(0);
        String mainSaveAvatarLq = channelavatar_lqarray.get(0);
        String mainSaveAvatarHq = channelavatar_hqarray.get(0);
        String mainSaveLastMsg = channellastmessagearray.get(0);
        String mainSaveLastDate = channellastdatearray.get(0);
        String mainSaveSound = channelsoundarray.get(0);
        String mainSaveMemberShip = channelmembershiparray.get(0);
        String mainSaveActive = channelactivearray.get(0);
        String mainSaveUnread = channelunreadarray.get(0);

        String saveUid = "";
        String saveName = "";
        String saveDesc = "";
        String saveMembersNumber = "";
        String saveAvatarLq = "";
        String saveAvatarHq = "";
        String saveLastMsg = "";
        String saveLastDate = "";
        String saveSound = "";
        String saveMemberShip = "";
        String saveActive = "";
        String saveUnread = "";

        for (int i = 1; i < channeluidarray.size(); i++) {
            //====zakhire item feli

            if (i != channeluidarray.size()) {
                saveUid = channeluidarray.get(i);
                saveName = channelnamearray.get(i);
                saveDesc = channeldescriptionarray.get(i);
                saveMembersNumber = channelmembersnumberarray.get(i);
                saveAvatarLq = channelavatar_lqarray.get(i);
                saveAvatarHq = channelavatar_hqarray.get(i);
                saveLastMsg = channellastmessagearray.get(i);
                saveLastDate = channellastdatearray.get(i);
                saveSound = channelsoundarray.get(i);
                saveMemberShip = channelmembershiparray.get(i);
                saveActive = channelactivearray.get(i);
                saveUnread = channelunreadarray.get(i);
            }

            //====varize item ghabl be item feli 
            channeluidarray.set(i, mainSaveUid);
            channelnamearray.set(i, mainSaveName);
            channeldescriptionarray.set(i, mainSaveDesc);
            channelmembersnumberarray.set(i, mainSaveMembersNumber);
            channelavatar_lqarray.set(i, mainSaveAvatarLq);
            channelavatar_hqarray.set(i, mainSaveAvatarHq);
            channellastmessagearray.set(i, mainSaveLastMsg);
            channellastdatearray.set(i, mainSaveLastDate);
            channelsoundarray.set(i, mainSaveSound);
            channelmembershiparray.set(i, mainSaveMemberShip);
            channelactivearray.set(i, mainSaveActive);
            channelunreadarray.set(i, mainSaveUnread);

            if (i != channeluidarray.size()) {
                //====enteghale item feli ke ghablan zakhire shode bud be mainSave
                mainSaveUid = saveUid;
                mainSaveName = saveName;
                mainSaveDesc = saveDesc;
                mainSaveMembersNumber = saveMembersNumber;
                mainSaveAvatarLq = saveAvatarLq;
                mainSaveAvatarHq = saveAvatarHq;
                mainSaveLastMsg = saveLastMsg;
                mainSaveLastDate = saveLastDate;
                mainSaveSound = saveSound;
                mainSaveMemberShip = saveMemberShip;
                mainSaveActive = saveActive;
                mainSaveUnread = saveUnread;
                //notifyItemMoved((i - 1), i);
            }
        }
    }


    private void deleteChannelFromAll(String ChannelUid) {
        Intent intentAll = new Intent("deleteChatFromAll");
        intentAll.putExtra("MODEL", "3");
        intentAll.putExtra("UID", ChannelUid);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intentAll);
    }


    public void newPost(String uid, String lastmsg, String lastdate) {

        final int pos = channeluidarray.indexOf(uid);

        if (pos != -1) {

            // agar in id vojud nadasht , ezafe kon
            //channelMsgIdArray.set(pos, msgID);
            channellastmessagearray.set(pos, lastmsg);
            channellastdatearray.set(pos, lastdate);
            int unread = G.cmd.getchannelunreadsize(uid);
            channelunreadarray.set(pos, String.valueOf(unread));
            itemMoveToFirst(pos);

        }
    }


    private void itemMoveToFirst(int changedPosition) {
        for (int i = changedPosition; i > 0; i--) {

            String firstUid = channeluidarray.get(i);
            String firstName = channelnamearray.get(i);
            String firstDesc = channeldescriptionarray.get(i);
            String firstMembersNumber = channelmembersnumberarray.get(i);
            String firstAvatarLq = channelavatar_lqarray.get(i);
            String firstAvatarHq = channelavatar_hqarray.get(i);
            String firstLastMsg = channellastmessagearray.get(i);
            String firstLastDate = channellastdatearray.get(i);
            String firstSound = channelsoundarray.get(i);
            String firstMemberShip = channelmembershiparray.get(i);
            String firstActive = channelactivearray.get(i);
            String firstUnread = channelunreadarray.get(i);

            String secondUid = channeluidarray.get(i - 1);
            String secondName = channelnamearray.get(i - 1);
            String secondDesc = channeldescriptionarray.get(i - 1);
            String secondMembersNumber = channelmembersnumberarray.get(i - 1);
            String secondAvatarLq = channelavatar_lqarray.get(i - 1);
            String secondAvatarHq = channelavatar_hqarray.get(i - 1);
            String secondLastMsg = channellastmessagearray.get(i - 1);
            String secondLastDate = channellastdatearray.get(i - 1);
            String secondSound = channelsoundarray.get(i - 1);
            String secondMemberShip = channelmembershiparray.get(i - 1);
            String secondActive = channelactivearray.get(i - 1);
            String secondUnread = channelunreadarray.get(i - 1);

            channeluidarray.set(i, secondUid);
            channelnamearray.set(i, secondName);
            channeldescriptionarray.set(i, secondDesc);
            channelmembersnumberarray.set(i, secondMembersNumber);
            channelavatar_lqarray.set(i, secondAvatarLq);
            channelavatar_hqarray.set(i, secondAvatarHq);
            channellastmessagearray.set(i, secondLastMsg);
            channellastdatearray.set(i, secondLastDate);
            channelsoundarray.set(i, secondSound);
            channelmembershiparray.set(i, secondMemberShip);
            channelactivearray.set(i, secondActive);
            channelunreadarray.set(i, secondUnread);

            channeluidarray.set((i - 1), firstUid);
            channelnamearray.set((i - 1), firstName);
            channeldescriptionarray.set((i - 1), firstDesc);
            channelmembersnumberarray.set((i - 1), firstMembersNumber);
            channelavatar_lqarray.set((i - 1), firstAvatarLq);
            channelavatar_hqarray.set((i - 1), firstAvatarHq);
            channellastmessagearray.set((i - 1), firstLastMsg);
            channellastdatearray.set((i - 1), firstLastDate);
            channelsoundarray.set((i - 1), firstSound);
            channelmembershiparray.set((i - 1), firstMemberShip);
            channelactivearray.set((i - 1), firstActive);
            channelunreadarray.set((i - 1), firstUnread);
        }
        notifyDataSetChanged();
    }


    public void updateSoundWithUid(String uid, String value) {

        int pos = channeluidarray.indexOf(uid);
        if (pos != -1) {
            G.cmd.updatechannelsound(channeluidarray.get(pos), value);
            channelsoundarray.set(pos, value);
            notifyItemChanged(pos);
        }

    }


    public void deleteWithUid(String uid) {

        int pos = channeluidarray.indexOf(uid);

        if (pos != -1) {
            channeluidarray.remove(pos);
            channelnamearray.remove(pos);
            channeldescriptionarray.remove(pos);
            channelmembersnumberarray.remove(pos);
            channelavatar_lqarray.remove(pos);
            channelavatar_hqarray.remove(pos);
            channellastmessagearray.remove(pos);
            channellastdatearray.remove(pos);
            channelsoundarray.remove(pos);
            channelmembershiparray.remove(pos);
            channelactivearray.remove(pos);
            channelunreadarray.remove(pos);
            visibleView.remove(pos);
            notif();
        }

    }


    public void deletedChannel(String uid, String lastmsg, String lastdate) {

        int pos = channeluidarray.indexOf(uid);
        if (pos != -1) {
            channellastmessagearray.set(pos, lastmsg);
            channellastdatearray.set(pos, lastdate);
            int unread = G.cmd.getchannelunreadsize(uid);
            channelunreadarray.set(pos, String.valueOf(unread));
            channelactivearray.set(pos, "2");
            itemMoveToFirst(pos);
        }
    }


    public void updateSeen(String uid) {

        int pos = channeluidarray.indexOf(uid);
        if (pos != -1) {
            int unread = G.cmd.getchannelunreadsize(uid);
            channelunreadarray.set(pos, String.valueOf(unread));
            notifyItemChanged(pos);
        }

    }


    public void updateAvatar(String uid, String avatarlq) {

        int pos = channeluidarray.indexOf(uid);
        if (pos != -1) {
            channelavatar_lqarray.set(pos, avatarlq);
            notifyItemChanged(pos);
        }

    }


    public void updateChannelItem(String uid, String name, String description, String membersnumbers, String avatar_lq, String avatar_hq, String membership) {

        int pos = channeluidarray.indexOf(uid);
        if (pos != -1) {
            channelnamearray.set(pos, name);
            channeldescriptionarray.set(pos, description);
            channelmembersnumberarray.set(pos, membersnumbers);
            channelavatar_lqarray.set(pos, avatar_lq);
            channelavatar_hqarray.set(pos, avatar_hq);
            channelmembershiparray.set(pos, membership);

            notifyItemChanged(pos);
        }
    }


    public void activeItemChannel(String uid, String active, String LastMessage, String lastDate) {

        int pos = channeluidarray.indexOf(uid);
        if (pos != -1) {

            channelactivearray.set(pos, active);
            channellastmessagearray.set(pos, LastMessage);
            channellastdatearray.set(pos, lastDate);

            notifyItemChanged(pos);
        }
    }


    public void updateMemberShip(String uid, String membership) {
        int pos = channeluidarray.indexOf(uid);
        if (pos != -1) {

            channelmembershiparray.set(pos, membership);
            notifyItemChanged(pos);
        }
    }


    public void changeDateType(String value) {
        hijri = value;

        for (int i = 0; i < channellastdatearray.size(); i++) {

            if (channellastdatearray.get(i) != null && !channellastdatearray.get(i).isEmpty() && !channellastdatearray.get(i).equals("null") && !channellastdatearray.get(i).equals("")) {
                String[] splitedrowtime = channellastdatearray.get(i).split("\\s+");
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
