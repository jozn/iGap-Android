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
import com.iGap.GroupChat;
import com.iGap.R;
import com.iGap.fragments.PageMessagingChats;
import com.iGap.fragments.PageMessagingGroups;
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
 * adapter for use in recycle view in page group that show a list of item
 *
 */

public class GroupRecycleAdapter extends RecyclerView.Adapter<GroupRecycleAdapter.ViewHolder> {

    private Context                mContext;

    private ArrayList<String>      gchatidarray;
    private ArrayList<String>      gchatnamearray;
    private ArrayList<String>      gchatmembershiparray;
    private ArrayList<String>      gchatlastmessagearray;
    private ArrayList<String>      gchatlastdatearray;
    private ArrayList<String>      gchatdescriptionarray;
    private ArrayList<String>      gchatavatararray;
    private ArrayList<String>      gchatavatararrayHq;
    private ArrayList<String>      gchatsoundarray;
    private ArrayList<String>      gchatunreadarray;
    private ArrayList<String>      gchatactivearray;

    private ArrayList<Boolean>     visibleView;

    private String                 day;
    private String                 month;
    private String                 year;
    private String                 hijri;
    private String                 username;

    private int                    lastPosition = 99999;
    private boolean                islong       = false;

    private ImageLoader1           il;
    private SpannableStringBuilder buildertext;
    private ViewHolder             viewholder;
    private View                   mainView;

    private ConfirmationDialog     cm;


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView     txtChannelIcon;
        public TextView     txourchannel;
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

        public ImageView    img_avatar;

        public Button       btnDelete;
        public Button       btnAlarm;
        public Button       btnclearhistory;

        public LinearLayout firstLayout;
        public LinearLayout secondLayout;

        public View         roww;


        public ViewHolder(View row) {
            super(row);
            roww = row;

            //=======layout
            firstLayout = (LinearLayout) row.findViewById(R.id.ll_first_group);
            secondLayout = (LinearLayout) row.findViewById(R.id.ll_second_group);

            //=======ImageView
            img_avatar = (ImageView) row.findViewById(R.id.img_avatar_group);

            //======TextView
            txticon = (TextView) row.findViewById(R.id.txt_icon_group);
            txtname = (TextView) row.findViewById(R.id.txt_name_group);
            txtmembers = (TextView) row.findViewById(R.id.txt_members_group);
            txtdesc = (TextView) row.findViewById(R.id.txt_desc_group);
            txtlast_message = (TextView) row.findViewById(R.id.txt_last_message_group);
            txtsound = (TextView) row.findViewById(R.id.txt_sound_group);
            txtlast_seen = (TextView) row.findViewById(R.id.txt_last_seen_group);
            txtunread = (TextView) row.findViewById(R.id.txt_unread_group);
            txourchannel = (TextView) row.findViewById(R.id.tx_our_channel_group);
            txchanneldes = (TextView) row.findViewById(R.id.tx_channel_des_group);
            txfirstpersent = (TextView) row.findViewById(R.id.tx_first_persent_group);
            txtChannelIcon = (TextView) row.findViewById(R.id.tx_channel_icon_group);

            //=======Button
            btnAlarm = (Button) row.findViewById(R.id.btn_alarm_group);
            btnclearhistory = (Button) row.findViewById(R.id.btn_clearhistory_group);
            btnDelete = (Button) row.findViewById(R.id.btn_delete5_group);

            //=========TypeFace
            txticon.setTypeface(G.fontAwesome);
            txtlast_message.setTypeface(G.robotoLight);
            txtlast_seen.setTypeface(G.robotoLight);
            txtunread.setTypeface(G.robotoLight);
            txourchannel.setTypeface(G.robotoBold);
            txchanneldes.setTypeface(G.robotoLight);
            txfirstpersent.setTypeface(G.robotoLight);
            txtChannelIcon.setTypeface(G.fontAwesome);
            btnAlarm.setTypeface(G.fontAwesome);
            btnclearhistory.setTypeface(G.fontAwesome);
            btnDelete.setTypeface(G.fontAwesome);
        }
    }


    public GroupRecycleAdapter(ArrayList<String> gchatidarray1, ArrayList<String> gchatnamearray1, ArrayList<String> gchatmembershiparray1, ArrayList<String> gchatlastmessagearray1, ArrayList<String> gchatlastdatearray1
                               , ArrayList<String> gchatdescriptionarray1, ArrayList<String> gchatavatararray1, ArrayList<String> gchatavatararrayHq1, ArrayList<String> gchatsoundarray1, ArrayList<String> gchatunreadarray1, ArrayList<String> gchatactivearray1
                               , Context context, String year1, String month1, String day1, ImageLoader1 il1, View view) {
        mContext = context;
        gchatidarray = gchatidarray1;
        gchatnamearray = gchatnamearray1;
        gchatmembershiparray = gchatmembershiparray1;
        gchatlastmessagearray = gchatlastmessagearray1;
        gchatlastdatearray = gchatlastdatearray1;
        gchatdescriptionarray = gchatdescriptionarray1;
        gchatavatararray = gchatavatararray1;
        gchatavatararrayHq = gchatavatararrayHq1;
        gchatsoundarray = gchatsoundarray1;
        gchatunreadarray = gchatunreadarray1;
        gchatactivearray = gchatactivearray1;
        year = year1;
        month = month1;
        day = day1;
        il = il1;

        hijri = G.cmd.getsetting(13);

        mainView = view;
        visibleView = new ArrayList<Boolean>();
        for (int i = 0; i < gchatidarray.size(); i++) {
            visibleView.add(false);
        }
        username = G.cmd.namayesh4(1, "info");
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_groups, parent, false);
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

        if (gchatsoundarray.get(position).equals("0")) {

            h.txtsound.setVisibility(View.GONE);

        } else {

            h.txtsound.setVisibility(View.VISIBLE);
            h.txtsound.setText(R.string.fa_bell);
            h.txtsound.setTypeface(G.fontAwesome);

        }

        if (gchatunreadarray.get(position).equals("0")) {

            h.txtunread.setVisibility(View.INVISIBLE);

        } else {

            h.txtunread.setVisibility(View.VISIBLE);
            h.txtunread.setText(gchatunreadarray.get(position));

        }

        String numberOfMembers = G.cmd.selectNumberOfMembers(gchatidarray.get(position));
        h.txtmembers.setText(numberOfMembers + "/5k");

        if (G.SelectedLanguage.equals("fa")) {
            h.txtmembers.setText(HelperGetTime.stringNumberToPersianNumberFormat(h.txtmembers.getText().toString()));
        }

        buildertext = parseText(gchatlastmessagearray.get(position));

        h.txtlast_message.setText(buildertext);

        if (gchatlastdatearray.get(position) != null && !gchatlastdatearray.get(position).isEmpty() && !gchatlastdatearray.get(position).equals("null") && !gchatlastdatearray.get(position).equals("")) {

            h.txtlast_seen.setText(HelperGetTime.getLastSeen(gchatlastdatearray.get(position), year, month, day));

        } else {
            h.txtlast_message.setText("");
            h.txtlast_seen.setText("");
        }
        h.txtname.setText(gchatnamearray.get(position));
        h.txtdesc.setText(gchatdescriptionarray.get(position));

        if (gchatavatararray.get(position) != null && !gchatavatararray.get(position).isEmpty() && !gchatavatararray.get(position).equals("null") && !gchatavatararray.get(position).equals("") && !gchatavatararray.get(position).equals("empty")) {

            il.DisplayImage(gchatavatararray.get(position), R.drawable.difaultimage, h.img_avatar);

        } else {

            HelperDrawAlphabet pf = new HelperDrawAlphabet();
            Bitmap bm = pf.drawAlphabet(mContext, gchatnamearray.get(position), h.img_avatar);

            h.img_avatar.setImageBitmap(bm);
        }

        h.txourchannel.setText(gchatnamearray.get(position));
        h.txchanneldes.setText(gchatdescriptionarray.get(position));
        h.txfirstpersent.setText(numberOfMembers + "/5k");
        h.txtChannelIcon.setTypeface(G.fontAwesome);

        if (gchatsoundarray.get(position).equals("1")) {
            h.btnAlarm.setText(mContext.getResources().getString(R.string.fa_bell_o));
        }
        else {
            h.btnAlarm.setText(mContext.getResources().getString(R.string.fa_bell));
        }

        h.btnAlarm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (gchatsoundarray.get(position).equals("1")) {
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
                updateclearcashdAllFromGroupChat(gchatidarray.get(position));
                clearehistory(position, "", "");
                visibleView.set(position, false);
                islong = false;
            }
        });
        h.btnDelete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ConfirmationDialog cm = new ConfirmationDialog(PageMessagingGroups.mcContext, new OnColorChangedListenerSelect() {

                    @Override
                    public void colorChanged(String key, int color) {

                    }


                    @Override
                    public void Confirmation(Boolean result) {
                        if (result) {

                            if (mContext == null)
                                mContext = G.context;

                            GroupAdapter GA = new GroupAdapter(mContext);

                            GA.deleteGroup(gchatidarray.get(position), username, gchatmembershiparray.get(position), new OnDeleteComplete() {

                                @Override
                                public void deleteComplete(Boolean result) {
                                    if (result) {
                                        deleteGroupFromAll(gchatidarray.get(position));
                                        delete(position, h);
                                    }
                                }
                            });
                        }
                    }
                });
                cm.showdialog(G.context.getString(R.string.do_you_want_delete_this_group));
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
                                    startGroupChat(position);
                                }
                            }
                        });
                        cm.showdialog(G.context.getString(R.string.share_data_group));
                    } else {
                        startGroupChat(position);
                    }
                }
            }
        });
    }


    private void startGroupChat(int position) {
        Intent intent = new Intent(mContext, GroupChat.class);
        intent.putExtra("gchid", gchatidarray.get(position));
        intent.putExtra("gchname", gchatnamearray.get(position));
        intent.putExtra("gchavatar", gchatavatararray.get(position));
        intent.putExtra("gchavatarHq", gchatavatararrayHq.get(position));
        intent.putExtra("gchdescription", gchatdescriptionarray.get(position));
        intent.putExtra("gchmembership", gchatmembershiparray.get(position));
        intent.putExtra("gchactive", gchatactivearray.get(position));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }


    @Override
    public int getItemCount() {
        return gchatidarray.size();
    }


    private void viewOn(ViewHolder h, int position) {
        PageMessagingPopularFunction pm = new PageMessagingPopularFunction();
        pm.viewOn(h.firstLayout, h.secondLayout, position);
        islong = true;
        visibleView.set(position, true);
    }


    private void viewOff(ViewHolder h, int position) {
        visibleView.set(position, false);
        PageMessagingPopularFunction pm = new PageMessagingPopularFunction();
        pm.viewOff(h.firstLayout, h.secondLayout, position);
    }


    public void insert(String uid, String name, String description, String avatar_lq, String avatar_hq, String lastmsg, String lastdate, String membership) {

        int exist = gchatidarray.indexOf(uid);

        if (exist == -1) {

            sendOtherItemToBottom();

            gchatidarray.set(0, uid);
            gchatnamearray.set(0, name);
            gchatdescriptionarray.set(0, description);
            gchatavatararray.set(0, avatar_lq);
            gchatavatararrayHq.set(0, avatar_hq);
            gchatlastmessagearray.set(0, lastmsg);
            gchatlastdatearray.set(0, lastdate);
            gchatmembershiparray.set(0, membership);
            gchatsoundarray.set(0, "0");
            gchatactivearray.set(0, "1");
            int unread = G.cmd.getRowCountunreadgroupchat(uid);
            gchatunreadarray.set(0, String.valueOf(unread));
            visibleView.set(0, false);
            notif();
        }
    }


    private void sendOtherItemToBottom() {//Insert item to first position

        //====add one item in all array
        gchatidarray.add("");
        gchatnamearray.add("");
        gchatdescriptionarray.add("");
        gchatavatararray.add("");
        gchatavatararrayHq.add("");
        gchatlastmessagearray.add("");
        gchatlastdatearray.add("");
        gchatmembershiparray.add("");
        gchatsoundarray.add("");
        gchatactivearray.add("");
        gchatunreadarray.add("");
        visibleView.add(false);

        notifyItemInserted((gchatidarray.size() - 1));

        String mainSaveId = gchatidarray.get(0);
        String mainSaveName = gchatnamearray.get(0);
        String mainSaveDesc = gchatdescriptionarray.get(0);
        String mainSaveAvatarLq = gchatavatararray.get(0);
        String mainSaveAvatarHq = gchatavatararrayHq.get(0);
        String mainSaveLastMsg = gchatlastmessagearray.get(0);
        String mainSaveLastDate = gchatlastdatearray.get(0);
        String mainSaveSound = gchatsoundarray.get(0);
        String mainSaveMemberShip = gchatmembershiparray.get(0);
        String mainSaveActive = gchatactivearray.get(0);
        String mainSaveUnread = gchatunreadarray.get(0);

        String saveId = "";
        String saveName = "";
        String saveDesc = "";
        String saveAvatarLq = "";
        String saveAvatarHq = "";
        String saveLastMsg = "";
        String saveLastDate = "";
        String saveSound = "";
        String saveMemberShip = "";
        String saveActive = "";
        String saveUnread = "";

        for (int i = 1; i < gchatidarray.size(); i++) {
            //====zakhire item feli

            if (i != gchatidarray.size()) {
                saveId = gchatidarray.get(i);
                saveName = gchatnamearray.get(i);
                saveDesc = gchatdescriptionarray.get(i);
                saveAvatarLq = gchatavatararray.get(i);
                saveAvatarHq = gchatavatararrayHq.get(i);
                saveLastMsg = gchatlastmessagearray.get(i);
                saveLastDate = gchatlastdatearray.get(i);
                saveSound = gchatsoundarray.get(i);
                saveMemberShip = gchatmembershiparray.get(i);
                saveActive = gchatactivearray.get(i);
                saveUnread = gchatunreadarray.get(i);
            }

            //====varize item ghabl be item feli 
            gchatidarray.set(i, mainSaveId);
            gchatnamearray.set(i, mainSaveName);
            gchatdescriptionarray.set(i, mainSaveDesc);
            gchatavatararray.set(i, mainSaveAvatarLq);
            gchatavatararrayHq.set(i, mainSaveAvatarHq);
            gchatlastmessagearray.set(i, mainSaveLastMsg);
            gchatlastdatearray.set(i, mainSaveLastDate);
            gchatsoundarray.set(i, mainSaveSound);
            gchatmembershiparray.set(i, mainSaveMemberShip);
            gchatactivearray.set(i, mainSaveActive);
            gchatunreadarray.set(i, mainSaveUnread);

            if (i != gchatidarray.size()) {
                //====enteghale item feli ke ghablan zakhire shode bud be mainSave
                mainSaveId = saveId;
                mainSaveName = saveName;
                mainSaveDesc = saveDesc;
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


    public void newPost(String uid, String lastmsg, String lastdate) {

        int pos = gchatidarray.indexOf(uid);
        if (pos != -1) {
            gchatlastmessagearray.set(pos, lastmsg);
            gchatlastdatearray.set(pos, lastdate);
            int unread = G.cmd.getRowCountunreadgroupchat(uid);
            gchatunreadarray.set(pos, String.valueOf(unread));

            itemMoveToFirst(pos);
        }

    }


    private void itemMoveToFirst(int changedPosition) {
        for (int i = changedPosition; i > 0; i--) {

            String firstUid = gchatidarray.get(i);
            String firstName = gchatnamearray.get(i);
            String firstDesc = gchatdescriptionarray.get(i);
            String firstAvatarLq = gchatavatararray.get(i);
            String firstAvatarHq = gchatavatararrayHq.get(i);
            String firstLastMsg = gchatlastmessagearray.get(i);
            String firstLastDate = gchatlastdatearray.get(i);
            String firstSound = gchatsoundarray.get(i);
            String firstMemberShip = gchatmembershiparray.get(i);
            String firstActive = gchatactivearray.get(i);
            String firstUnread = gchatunreadarray.get(i);

            String secondUid = gchatidarray.get(i - 1);
            String secondName = gchatnamearray.get(i - 1);
            String secondDesc = gchatdescriptionarray.get(i - 1);
            String secondAvatarLq = gchatavatararray.get(i - 1);
            String secondAvatarHq = gchatavatararrayHq.get(i - 1);
            String secondLastMsg = gchatlastmessagearray.get(i - 1);
            String secondLastDate = gchatlastdatearray.get(i - 1);
            String secondSound = gchatsoundarray.get(i - 1);
            String secondMemberShip = gchatmembershiparray.get(i - 1);
            String secondActive = gchatactivearray.get(i - 1);
            String secondUnread = gchatunreadarray.get(i - 1);

            gchatidarray.set(i, secondUid);
            gchatnamearray.set(i, secondName);
            gchatdescriptionarray.set(i, secondDesc);
            gchatavatararray.set(i, secondAvatarLq);
            gchatavatararrayHq.set(i, secondAvatarHq);
            gchatlastmessagearray.set(i, secondLastMsg);
            gchatlastdatearray.set(i, secondLastDate);
            gchatsoundarray.set(i, secondSound);
            gchatmembershiparray.set(i, secondMemberShip);
            gchatactivearray.set(i, secondActive);
            gchatunreadarray.set(i, secondUnread);

            gchatidarray.set((i - 1), firstUid);
            gchatnamearray.set((i - 1), firstName);
            gchatdescriptionarray.set((i - 1), firstDesc);
            gchatavatararray.set((i - 1), firstAvatarLq);
            gchatavatararrayHq.set((i - 1), firstAvatarHq);
            gchatlastmessagearray.set((i - 1), firstLastMsg);
            gchatlastdatearray.set((i - 1), firstLastDate);
            gchatsoundarray.set((i - 1), firstSound);
            gchatmembershiparray.set((i - 1), firstMemberShip);
            gchatactivearray.set((i - 1), firstActive);
            gchatunreadarray.set((i - 1), firstUnread);
        }
        notifyDataSetChanged();
    }


    public void updateSoundWithUid(String uid, String value) {
        int pos = gchatidarray.indexOf(uid);
        if (pos != -1) {
            G.cmd.updatechannelsound(gchatidarray.get(pos), value);
            gchatsoundarray.set(pos, value);
            notifyItemChanged(pos);
        }

    }


    public void deleteWithUid(String uid) {
        int pos = gchatidarray.indexOf(uid);
        if (pos != -1) {
            gchatidarray.remove(pos);
            gchatnamearray.remove(pos);
            gchatmembershiparray.remove(pos);
            gchatlastmessagearray.remove(pos);
            gchatlastdatearray.remove(pos);
            gchatdescriptionarray.remove(pos);
            gchatavatararray.remove(pos);
            gchatavatararrayHq.remove(pos);
            gchatsoundarray.remove(pos);
            gchatunreadarray.remove(pos);
            gchatactivearray.remove(pos);
            visibleView.remove(pos);

            lastPosition = 9999;

            notif();
        }

    }


    public void kiked(String uid, String lastmsg, String lastdate) {
        int pos = gchatidarray.indexOf(uid);
        if (pos != -1) {
            gchatlastmessagearray.set(pos, lastmsg);
            gchatlastdatearray.set(pos, lastdate);
            int unread = G.cmd.getRowCountunreadgroupchat(uid);
            gchatunreadarray.set(pos, String.valueOf(unread));
            gchatactivearray.set(pos, "2");
            notifyDataSetChanged();
        }
    }


    public void updateSeen(String uid) {
        int pos = gchatidarray.indexOf(uid);
        if (pos != -1) {
            int unread = G.cmd.getRowCountunreadgroupchat(uid);
            gchatunreadarray.set(pos, String.valueOf(unread));
            notifyItemChanged(pos);
        }

    }


    private SpannableStringBuilder parseText(String text) {
        PageMessagingPopularFunction pm = new PageMessagingPopularFunction();
        SpannableStringBuilder builder = pm.parseText(text, "PAGE_MESSAGING_LIST", false, mContext, 0);
        return builder;
    }


    private void updatesound(int pos, String value) {
        updateSoundAllFromGroupChat(gchatidarray.get(pos), value);

        G.cmd.updatesound(gchatidarray.get(pos), value);
        gchatsoundarray.set(pos, value);
        notifyItemChanged(pos);
        islong = false;
        viewOff(viewholder, pos);
    }


    private void updateSoundAllFromGroupChat(String GroupChatid, String Value) {
        Intent intentAll = new Intent("updateSoundAll");
        intentAll.putExtra("MODEL", "2");
        intentAll.putExtra("UID", GroupChatid);
        intentAll.putExtra("VALUE", Value);

        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intentAll);
    }


    private void clearehistory(int pos, String lastMsgDate, String lastMsg) {

        if (gchatidarray.get(pos) != null && !gchatidarray.get(pos).isEmpty() && !gchatidarray.get(pos).equals("null") && !gchatidarray.get(pos).equals("") && !gchatidarray.get(pos).equals("empty")) {
            G.cmd.cleargroupchathistory(gchatidarray.get(pos));
            G.cmd.updategroupchatrooms(gchatidarray.get(pos), lastMsgDate, lastMsg);

            gchatlastdatearray.set(pos, lastMsgDate);
            gchatlastmessagearray.set(pos, lastMsg);
            int unread = G.cmd.getRowCountunreadgroupchat(gchatidarray.get(pos));
            gchatunreadarray.set(pos, String.valueOf(unread));
            notifyItemChanged(pos);
        }
        islong = false;
        viewOff(viewholder, pos);
    }


    private void updateclearcashdAllFromGroupChat(String GroupChatid) {
        Intent intentAll = new Intent("clearHistoryAll");
        intentAll.putExtra("MODEL", "2");
        intentAll.putExtra("UID", GroupChatid);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intentAll);
    }


    public void clearehistorywithuid(String uid) {

        int pos = gchatidarray.indexOf(uid);
        if (pos != -1) {
            gchatlastdatearray.set(pos, "");
            gchatlastmessagearray.set(pos, "");
            notifyItemChanged(pos);
        }

    }


    private void deleteGroupFromAll(String groupChat) {
        Intent intentAll = new Intent("deleteChatFromAll");
        intentAll.putExtra("MODEL", "2");
        intentAll.putExtra("UID", groupChat);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intentAll);
    }


    private void delete(int pos, ViewHolder h) {
        islong = false;
        viewOff(h, pos);
        gchatidarray.remove(pos);
        gchatnamearray.remove(pos);
        gchatmembershiparray.remove(pos);
        gchatlastmessagearray.remove(pos);
        gchatlastdatearray.remove(pos);
        gchatdescriptionarray.remove(pos);
        gchatavatararray.remove(pos);
        gchatavatararrayHq.remove(pos);
        gchatsoundarray.remove(pos);
        gchatunreadarray.remove(pos);
        gchatactivearray.remove(pos);
        visibleView.remove(pos);

        lastPosition = 9999;

        if (gchatidarray.size() == 0) {
            Intent intent = new Intent("EMPTY_LIST_GROUP");
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        }

        notif();
    }


    private void notif() {
        notifyDataSetChanged();
    }


    public void updateAvatar(String uid, String avatarlq) {
        int pos = gchatidarray.indexOf(uid);
        if (pos != -1) {
            gchatavatararray.set(pos, avatarlq);
            notifyItemChanged(pos);
        }

    }


    public void updateItemgroup(String uid, String name, String desc, String avatarLq, String avatarHq, String memberShip) {

        int pos = gchatidarray.indexOf(uid);
        if (pos != -1) {

            gchatnamearray.set(pos, name);
            gchatdescriptionarray.set(pos, desc);
            gchatavatararray.set(pos, avatarLq);
            gchatavatararrayHq.set(pos, avatarHq);
            gchatmembershiparray.set(pos, memberShip);

            notifyItemChanged(pos);
        }
    }


    public void activeItemgroup(String uid, String active, String LastMessage, String lastDate) {

        int pos = gchatidarray.indexOf(uid);
        if (pos != -1) {

            gchatactivearray.set(pos, active);
            gchatlastmessagearray.set(pos, LastMessage);
            gchatlastdatearray.set(pos, lastDate);

            notifyItemChanged(pos);
        }
    }


    public void updateGroupMembersNumber(String groupID) {
        int pos = gchatidarray.indexOf(groupID);
        if (pos != -1) {
            notifyItemChanged(pos);
        }
    }


    public void changeDateType(String value) {
        hijri = value;

        for (int i = 0; i < gchatlastdatearray.size(); i++) {
            if (gchatlastdatearray.get(i) != null && !gchatlastdatearray.get(i).isEmpty() && !gchatlastdatearray.get(i).equals("null") && !gchatlastdatearray.get(i).equals("")) {
                String[] splitedrowtime = gchatlastdatearray.get(i).split("\\s+");
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
