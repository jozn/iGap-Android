// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.adapter;

import java.util.ArrayList;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.iGap.Channel;
import com.iGap.GroupChat;
import com.iGap.MainActivity;
import com.iGap.R;
import com.iGap.Singlechat;
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
 * adapter baraye fragmente {@link com.iGap.fragments.PageMessagingAll} ke list hameye chatha , grouh ha va kanalha ra namayesh midahad
 *
 */

public class AllRecycleAdapter extends RecyclerView.Adapter<AllRecycleAdapter.ViewHolder> {

    private ArrayList<AllType>     allitem;
    private ArrayList<Boolean>     visibleView;
    private ArrayList<Boolean>     isLongArray;

    private String                 day;
    private String                 month;
    private String                 year;
    private String                 hijri;
    private String                 username;

    private int                    lastPosition = 99999;
    private boolean                islong       = false;

    private Context                mContext;
    private ViewHolder             viewholder;
    private View                   mainView;
    private ImageLoader1           il;
    private SpannableStringBuilder buildertext;
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

            // =======layout
            firstLayout = (LinearLayout) row.findViewById(R.id.ll_first);
            secondLayout = (LinearLayout) row.findViewById(R.id.ll_second);
            // =======ImageView
            img_avatar = (ImageView) row.findViewById(R.id.img_avatar);

            // ======TextView

            txticon = (TextView) row.findViewById(R.id.txt_icon);
            txtname = (TextView) row.findViewById(R.id.txt_name);
            txtmembers = (TextView) row.findViewById(R.id.txt_members);
            txtdesc = (TextView) row.findViewById(R.id.txt_desc);
            txtlast_message = (TextView) row.findViewById(R.id.txt_last_message);
            txtsound = (TextView) row.findViewById(R.id.txt_sound);
            txtlast_seen = (TextView) row.findViewById(R.id.txt_last_seen);
            txtunread = (TextView) row.findViewById(R.id.txt_unread);
            txourchannel = (TextView) row.findViewById(R.id.tx_our_channel);
            txchanneldes = (TextView) row.findViewById(R.id.tx_channel_des);
            txfirstpersent = (TextView) row.findViewById(R.id.tx_first_persent);
            txtChannelIcon = (TextView) row.findViewById(R.id.tx_channel_icon);

            // =======Button
            btnAlarm = (Button) row.findViewById(R.id.btn_alarm);
            btnclearhistory = (Button) row.findViewById(R.id.btn_clearhistory);
            btnDelete = (Button) row.findViewById(R.id.btn_delete5);

            // =========TypeFace
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


    public AllRecycleAdapter(ArrayList<AllType> allitem1, Context context, String year1, String month1, String day1, ImageLoader1 il1, View view) {
        mContext = context;
        allitem = allitem1;
        year = year1;
        month = month1;
        day = day1;
        il = il1;

        mainView = view;
        visibleView = new ArrayList<Boolean>();
        isLongArray = new ArrayList<Boolean>();
        for (int i = 0; i < allitem.size(); i++) {
            visibleView.add(false);
            isLongArray.add(false);
        }

        username = G.username;
        hijri = G.hijriDate;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item, parent, false);
        viewholder = new ViewHolder(v);
        return viewholder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder h, final int position) {

        if (isLongArray.get(position) == true) {
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
                        catch (Exception e) {
                            e.printStackTrace();
                        }

                        return true;
                    }
                }
                return false;
            }
        });

        h.txtdesc.setText(allitem.get(position).description);

        if (allitem.get(position).model.equals("3")) { // channel set info

            h.txtdesc.setVisibility(View.VISIBLE);
            h.txtdesc.setText(allitem.get(position).description);

            HelperCalculateMembers cp = new HelperCalculateMembers();
            String members = cp.calculateMembers(allitem.get(position).membersnumber);

            h.txfirstpersent.setVisibility(View.VISIBLE);
            h.txfirstpersent.setText(members);
            h.txticon.setText(R.string.fa_rss);
            h.txticon.setTypeface(G.fontAwesome);
            h.txtChannelIcon.setText(R.string.fa_rss);
            h.txtChannelIcon.setTypeface(G.fontAwesome);

            int unread = G.cmd.getchannelunreadsize(allitem.get(position).uid);

            if (unread == 0) {
                h.txtunread.setVisibility(View.INVISIBLE);
            } else {
                h.txtunread.setVisibility(View.VISIBLE);
                h.txtunread.setText(String.valueOf(unread));
            }
            if (allitem.get(position).sound.equals("0")) {
                h.txtsound.setVisibility(View.GONE);
            } else {
                h.txtsound.setVisibility(View.VISIBLE);
                h.txtsound.setText(R.string.fa_bell);
                h.txtsound.setTypeface(G.fontAwesome);
            }
            if (allitem.get(position).avatar_lq != null && !allitem.get(position).avatar_lq.isEmpty() && !allitem.get(position).avatar_lq.equals("null") && !allitem.get(position).avatar_lq.equals("") && !allitem.get(position).avatar_lq.equals("empty")) {
                il.DisplayImage(allitem.get(position).avatar_lq, R.drawable.difaultimage, h.img_avatar);
            } else {
                HelperDrawAlphabet pf = new HelperDrawAlphabet();
                Bitmap bm = pf.drawAlphabet(mContext, allitem.get(position).name, h.img_avatar);
                h.img_avatar.setImageBitmap(bm);

            }
            if (allitem.get(position).lastdate != null && !allitem.get(position).lastdate.isEmpty() && !allitem.get(position).lastdate.equals("null") && !allitem.get(position).lastdate.equals("")) {

                h.txtlast_seen.setText(HelperGetTime.getLastSeen(allitem.get(position).lastdate, year, month, day));

            } else {
                h.txtlast_message.setText("");
                h.txtlast_seen.setText("");
            }
            buildertext = parseText(allitem.get(position).lastmessage);
            h.txtlast_message.setText(buildertext);
            h.txtname.setText(allitem.get(position).name);
            h.txourchannel.setText(allitem.get(position).name);
            h.txourchannel.setTypeface(G.robotoBold);
            h.txchanneldes.setText(allitem.get(position).description);

        } else if (allitem.get(position).model.equals("1")) { // singleChat set info

            h.txtdesc.setVisibility(View.GONE);

            int unread = G.cmd.getRowCountunreadchat(allitem.get(position).id);

            if (unread == 0) {
                h.txtunread.setVisibility(View.INVISIBLE);
            } else {
                h.txtunread.setVisibility(View.VISIBLE);
                h.txtunread.setText(String.valueOf(unread));
            }
            h.txfirstpersent.setVisibility(View.GONE);
            h.txticon.setText(R.string.fa_user);
            h.txticon.setTypeface(G.fontAwesome);
            h.txtChannelIcon.setText(R.string.fa_user);
            h.txtChannelIcon.setTypeface(G.fontAwesome);

            if (allitem.get(position).sound.equals("0")) {
                h.txtsound.setVisibility(View.GONE);
            } else {
                h.txtsound.setVisibility(View.VISIBLE);
                h.txtsound.setText(R.string.fa_bell);
                h.txtsound.setTypeface(G.fontAwesome);
            }

            if (allitem.get(position).userchatavatar != null && !allitem.get(position).userchatavatar.isEmpty() && !allitem.get(position).userchatavatar.equals("null") && !allitem.get(position).userchatavatar.equals("") && !allitem.get(position).userchatavatar.equals("empty")) {
                il.DisplayImage(allitem.get(position).userchatavatar,
                        R.drawable.difaultimage, h.img_avatar);
            } else {
                HelperDrawAlphabet pf = new HelperDrawAlphabet();
                Bitmap bm = pf.drawAlphabet(mContext, allitem.get(position).name, h.img_avatar);

                h.img_avatar.setImageBitmap(bm);

            }
            if (allitem.get(position).lastdate != null && !allitem.get(position).lastdate.isEmpty() && !allitem.get(position).lastdate.equals("null") && !allitem.get(position).lastdate.equals("")) {

                h.txtlast_seen.setText(HelperGetTime.getLastSeen(allitem.get(position).lastdate, year, month, day));

            } else {
                h.txtlast_message.setText("");
                h.txtlast_seen.setText("");
            }
            buildertext = parseText(allitem.get(position).lastmessage);
            h.txtlast_message.setText(buildertext);
            h.txtname.setText(allitem.get(position).name);
            h.txourchannel.setText(allitem.get(position).name);
            h.txchanneldes.setText(buildertext);

        } else if (allitem.get(position).model.equals("2")) { // GroupChat set info

            h.txtdesc.setVisibility(View.VISIBLE);
            h.txtdesc.setText(allitem.get(position).description);

            int unread = G.cmd.getRowCountunreadgroupchat(allitem.get(position).groupchatid);
            if (unread == 0) {
                h.txtunread.setVisibility(View.INVISIBLE);
            } else {
                h.txtunread.setVisibility(View.VISIBLE);
                h.txtunread.setText(String.valueOf(unread));
            }

            h.txfirstpersent.setVisibility(View.VISIBLE);
            h.txfirstpersent.setText(allitem.get(position).membersnumber + "/5k");
            h.txticon.setText(R.string.fa_group);
            h.txticon.setTypeface(G.fontAwesome);
            h.txtChannelIcon.setText(R.string.fa_group);
            h.txtChannelIcon.setTypeface(G.fontAwesome);

            if (allitem.get(position).sound.equals("0")) {
                h.txtsound.setVisibility(View.GONE);
            } else {
                h.txtsound.setVisibility(View.VISIBLE);
                h.txtsound.setText(R.string.fa_bell);
                h.txtsound.setTypeface(G.fontAwesome);
            }
            if (allitem.get(position).groupavatar != null && !allitem.get(position).groupavatar.isEmpty() && !allitem.get(position).groupavatar.equals("null") && !allitem.get(position).groupavatar.equals("") && !allitem.get(position).groupavatar.equals("empty")) {
                il.DisplayImage(allitem.get(position).groupavatar, R.drawable.difaultimage, h.img_avatar);
            } else {
                HelperDrawAlphabet pf = new HelperDrawAlphabet();
                Bitmap bm = pf.drawAlphabet(mContext, allitem.get(position).name, h.img_avatar);
                h.img_avatar.setImageBitmap(bm);

            }
            if (allitem.get(position).lastdate != null && !allitem.get(position).lastdate.isEmpty() && !allitem.get(position).lastdate.equals("null") && !allitem.get(position).lastdate.equals("")) {

                h.txtlast_seen.setText(HelperGetTime.getLastSeen(allitem.get(position).lastdate, year, month, day));

            } else {
                h.txtlast_message.setText("");
                h.txtlast_seen.setText("");
            }

            if (allitem.get(position).lastmessage != null) {
                buildertext = parseText(allitem.get(position).lastmessage);
                h.txtlast_message.setText(buildertext);
            } else {
                h.txtlast_message.setText("");
            }
            h.txtname.setText(allitem.get(position).name);
            h.txourchannel.setText(allitem.get(position).name);
            h.txchanneldes.setText(allitem.get(position).description);
        }

        if (allitem.get(position).model.equals("1")) { // singleChat

            h.txtmembers.setVisibility(View.GONE);

        } else if (allitem.get(position).model.equals("2")) { // Group

            String numberOfMembers = G.cmd.selectNumberOfMembers(allitem.get(position).groupchatid);
            h.txtmembers.setVisibility(View.VISIBLE);
            h.txtmembers.setText(numberOfMembers + "/5k");

        } else if (allitem.get(position).model.equals("3")) { // Channel
            h.txtmembers.setVisibility(View.VISIBLE);
            HelperCalculateMembers cp = new HelperCalculateMembers();
            String members = cp.calculateMembers(allitem.get(position).membersnumber);

            h.txtmembers.setText(members);
        }

        if (G.SelectedLanguage.equals("fa")) {
            h.txtmembers.setText(HelperGetTime.stringNumberToPersianNumberFormat(h.txtmembers.getText().toString()));
        }

        if (allitem.get(position).sound.equals("1")) {
            h.btnAlarm.setText(mContext.getResources().getString(R.string.fa_bell_o));
        } else {
            h.btnAlarm.setText(mContext.getResources().getString(R.string.fa_bell));
        }

        h.btnAlarm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (allitem.get(position).model.equals("1")) {
                    String value;
                    if (allitem.get(position).sound.equals("1")) {
                        updatesound(position, "0", 1);
                        value = "0";
                    } else {
                        updatesound(position, "1", 1);
                        value = "1";
                    }

                    Intent intent = new Intent("updateSoundWithUidChat");
                    intent.putExtra("uid", allitem.get(position).userchat);
                    intent.putExtra("value", value);
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(
                            intent);

                } else if (allitem.get(position).model.equals("2")) {
                    if (allitem.get(position).sound.equals("1")) {
                        updatesound(position, "0", 2);
                    } else {
                        updatesound(position, "1", 2);
                    }

                } else if (allitem.get(position).model.equals("3")) {
                    if (allitem.get(position).sound.equals("1")) {
                        updatesound(position, "0", 3);
                    } else {
                        updatesound(position, "1", 3);
                    }
                }

                isLongArray.set(position, false);
                islong = false;

            }
        });
        h.btnclearhistory.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (allitem.get(position).model.equals("1")) {
                    clearehistory(position, "", "", 1);
                } else if (allitem.get(position).model.equals("2")) {
                    clearehistory(position, "", "", 2);
                } else if (allitem.get(position).model.equals("3")) {
                    clearehistory(position, "", "", 3);
                }

                isLongArray.set(position, false);
                islong = false;
            }
        });

        h.btnDelete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (allitem.get(position).model.equals("1")) {
                    if (mContext == null) {
                        mContext = G.context;
                    }
                    cm = new ConfirmationDialog(PageMessagingChats.mcContext, new OnColorChangedListenerSelect() {

                        @Override
                        public void colorChanged(String key, int color) {

                        }


                        @Override
                        public void Confirmation(Boolean result) {
                            if (result) {
                                G.cmd.deletechatrooms(allitem.get(position).id);
                                Intent intent = new Intent("deleteWithUidChat");
                                intent.putExtra("uid", allitem.get(position).userchat);
                                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                                delete(position, h);
                            }
                        }
                    });

                } else if (allitem.get(position).model.equals("2")) {

                    if (mContext == null) {
                        mContext = G.context;
                    }

                    cm = new ConfirmationDialog(PageMessagingChats.mcContext, new OnColorChangedListenerSelect() {

                        @Override
                        public void colorChanged(String key, int color) {

                        }


                        @Override
                        public void Confirmation(Boolean result) {
                            if (result) {
                                if (mContext == null) {
                                    mContext = G.context;
                                }
                                GroupAdapter GA = new GroupAdapter(mContext);

                                GA.deleteGroup(allitem.get(position).groupchatid, username, allitem.get(position).membership, new OnDeleteComplete() {

                                    @Override
                                    public void deleteComplete(Boolean result) {
                                        if (result) {
                                            Intent intent = new Intent("deleteWithUidGroup");
                                            intent.putExtra("uid", allitem.get(position).groupchatid);
                                            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

                                            delete(position, h);
                                        }
                                    }
                                });
                            }
                        }
                    });

                } else if (allitem.get(position).model.equals("3")) {

                    final String uid = allitem.get(position).uid;
                    final String userState = allitem.get(position).membership;
                    if (mContext == null) {
                        mContext = G.context;
                    }

                    cm = new ConfirmationDialog(PageMessagingChats.mcContext, new OnColorChangedListenerSelect() {

                        @Override
                        public void colorChanged(String key, int color) {

                        }


                        @Override
                        public void Confirmation(Boolean result) {
                            if (result) {
                                ChannelAdapter ChA;

                                ChA = new ChannelAdapter(MainActivity.publicContext);

                                ChA.deleteChannel(userState, uid, new OnDeleteComplete() {

                                    @Override
                                    public void deleteComplete(Boolean result) {
                                        if (result) {
                                            Intent intent = new Intent("deleteWithUid");
                                            intent.putExtra("uid", uid);
                                            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

                                            delete(position, h);
                                        }

                                    }
                                });
                            }
                        }
                    });
                }

                if (allitem.get(position).model.equals("1")) {
                    cm.showdialog(G.context.getString(R.string.do_you_want_delete_this_chat));
                } else if (allitem.get(position).model.equals("2")) {
                    cm.showdialog(G.context.getString(R.string.do_you_want_delete_this_group));
                } else if (allitem.get(position).model.equals("3")) {
                    cm.showdialog(G.context.getString(R.string.do_you_want_delete_this_channel));
                }

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
                                    startChats(position);
                                }
                            }
                        });

                        if (allitem.get(position).model.equals("1")) {
                            cm.showdialog(G.context.getString(R.string.share_data_chat));
                        } else if (allitem.get(position).model.equals("2")) {
                            cm.showdialog(G.context.getString(R.string.share_data_group));
                        } else if (allitem.get(position).model.equals("3")) {
                            cm.showdialog(G.context.getString(R.string.share_data_channel));
                        }

                    } else {
                        startChats(position);
                    }
                }
            }
        });
    }


    private void startChats(int position) {
        if (allitem.get(position).model.equals("1")) {

            Intent intent = new Intent(mContext, Singlechat.class);
            intent.putExtra("chatroomid", allitem.get(position).id);
            intent.putExtra("userchat", allitem.get(position).userchat);
            intent.putExtra("userchatname", allitem.get(position).name);
            intent.putExtra("userchatavatar", allitem.get(position).userchatavatar);
            intent.putExtra("active", allitem.get(position).active);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);

        } else if (allitem.get(position).model.equals("2")) {

            Intent intent = new Intent(mContext, GroupChat.class);
            intent.putExtra("gchid", allitem.get(position).groupchatid);
            intent.putExtra("gchname", allitem.get(position).name);
            intent.putExtra("gchavatar", allitem.get(position).groupavatar);
            intent.putExtra("gchavatarHq", allitem.get(position).avatar_hq);
            intent.putExtra("gchdescription", allitem.get(position).description);
            intent.putExtra("gchmembership", allitem.get(position).membership);
            intent.putExtra("gchactive", allitem.get(position).active);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);

        } else if (allitem.get(position).model.equals("3")) {

            Intent intent = new Intent(mContext, Channel.class);
            intent.putExtra("channeluid", allitem.get(position).uid);
            intent.putExtra("channelName", allitem.get(position).name);
            intent.putExtra("channelavatarlq", allitem.get(position).avatar_lq);
            intent.putExtra("channelavatarhq", allitem.get(position).avatar_hq);
            intent.putExtra("channelmembership", allitem.get(position).membership);
            intent.putExtra("channelDesc", allitem.get(position).description);
            intent.putExtra("channelmembersnumber", allitem.get(position).membersnumber);
            intent.putExtra("channelactive", allitem.get(position).active);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);

        }
    }


    @Override
    public int getItemCount() {
        return allitem.size();
    }


    private void showdialog(final OnColorChangedListenerSelect listener) {

        final Dialog di = new Dialog(mContext);
        di.requestWindowFeature(Window.FEATURE_NO_TITLE);
        di.setContentView(R.layout.dialog_exit_prompt);
        di.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        di.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        TextView tvMessage = (TextView) di.findViewById(R.id.textView_message_prompt);
        tvMessage.setText(R.string.are_you_sure_en);
        tvMessage.setTypeface(G.robotoBold);
        Button tvYes = (Button) di.findViewById(R.id.btnView_yes);
        tvYes.setTypeface(G.robotoBold);
        Button tvNo = (Button) di.findViewById(R.id.btnView_no);
        tvNo.setTypeface(G.robotoBold);

        tvYes.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (di != null && di.isShowing()) {
                    di.dismiss();
                }

                listener.Confirmation(true);

            }
        });
        tvNo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (di != null && di.isShowing()) {
                    di.dismiss();
                }

                listener.Confirmation(false);

            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(di.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        di.getWindow().setAttributes(lp);

        di.show();
    }


    private void viewOn(ViewHolder h, int position) {
        PageMessagingPopularFunction pm = new PageMessagingPopularFunction();
        pm.viewOn(h.firstLayout, h.secondLayout, position);
        islong = true;
        visibleView.set(position, true);
        isLongArray.set(position, true);
    }


    private void viewOff(ViewHolder h, int position) {
        visibleView.set(position, false);
        isLongArray.set(position, false);

        PageMessagingPopularFunction pm = new PageMessagingPopularFunction();
        pm.viewOff(h.firstLayout, h.secondLayout, position);
    }


    public void insert(String uid, String name, String description, String membersnumbers, String avatar_lq, String avatar_hq, String lastmsg, String lastdate, String membership, String model) {

        if (model.equals("3")) { // Add New Channel

            for (int i = 0; i < allitem.size(); i++) {
                if (allitem.get(i).uid != null) {
                    if (allitem.get(i).uid.equals(uid)) {
                        return; // if this channel uid exist don't add again and
                        // exit from this method
                    }
                }
            }

            sendOtherItemToBottom();

            int unread = G.cmd.getchannelunreadsize(uid);
            AllType item = new AllType();
            item.uid = uid;
            item.name = name;
            item.description = description;
            item.membersnumber = membersnumbers;
            item.avatar_lq = avatar_lq;
            item.avatar_hq = avatar_hq;
            item.lastmessage = lastmsg;
            item.lastdate = lastdate;
            item.membership = membership;
            item.sound = "0";
            item.active = "1";
            item.unread = unread + "";
            item.model = "3";

            isLongArray.add(false);

            allitem.set(0, item);

        } else if (model.equals("2")) { // Add New Group

            for (int i = 0; i < allitem.size(); i++) {
                if (allitem.get(i).uid != null) {
                    if (allitem.get(i).uid.equals(uid)) {

                        return; // if this group uid exist don't add again and exist from this method
                    }
                }
            }
            sendOtherItemToBottom();

            int unread = G.cmd.getRowCountunreadgroupchat(uid);
            AllType item = new AllType();
            item.groupchatid = uid;
            item.name = name;
            item.description = description;
            item.groupavatar = avatar_lq;
            item.avatar_hq = avatar_hq;
            item.lastmessage = lastmsg;
            item.lastdate = lastdate;
            item.membership = membership;
            item.sound = "0";
            item.active = "1";
            item.unread = unread + "";
            item.model = "2";
            item.membersnumber = "1";

            isLongArray.add(false);

            allitem.set(0, item);
        } else if (model.equals("1")) { // Add New SingleChat
            for (int i = 0; i < allitem.size(); i++) {
                if (allitem.get(i).userchat != null) {
                    if (allitem.get(i).userchat.equals(name)) {

                        return; // if this userchat exist don't add again and exit from this method
                    }
                }
            }

            sendOtherItemToBottom();
            int unread = G.cmd.selectUnreadCountSingleChat(uid);
            AllType item = new AllType();
            item.id = uid;
            String mobile = name.split("@")[0];
            String userchatname;
            try {
                userchatname = G.cmd.namayeshname(1, mobile);
            }
            catch (Exception e) {
                userchatname = mobile;
            }
            item.id = uid;
            item.name = userchatname;
            item.userchat = name;
            item.userchatavatar = avatar_lq;
            item.lastmessage = lastmsg;
            item.lastdate = lastdate;
            item.description = description;
            item.sound = "0";
            item.active = "1";
            item.unread = unread + "";
            item.model = "1";

            isLongArray.add(false);

            allitem.set(0, item);
        }
        visibleView.add(false);
        notif();
    }


    /**
     * 
     * Insert new item to first position
     * 
     */

    private void sendOtherItemToBottom() {

        // ====add one item in all array
        AllType item = new AllType();
        allitem.add(item);
        visibleView.add(false);

        notifyItemInserted((allitem.size() - 1));
        AllType mainSaveItem = allitem.get(0);
        AllType saveItem = new AllType();

        for (int i = 1; i < allitem.size(); i++) {
            // ====zakhire item feli
            if (i != allitem.size()) {
                saveItem = allitem.get(i);
            }

            // ====varize item ghabl be item feli
            allitem.set(i, mainSaveItem);

            if (i != allitem.size()) {
                // ====enteghale item feli ke ghablan zakhire shode bud be
                // mainSave
                mainSaveItem = saveItem;
            }
        }
    }


    public void newPost(String model, String uid, String lastmsg, String lastdate) {

        int pos = getPostition(uid, model);
        if (pos != -1) {
            int unread = 0;
            if (model.equals("1")) {
                unread = G.cmd.selectUnreadCountSingleChat(uid);
            } else if (model.equals("2")) {
                unread = G.cmd.selectUnreadCountGroup(uid);
            } else if (model.equals("3")) {
                unread = G.cmd.getchannelunreadsize(uid);
            }
            allitem.get(pos).lastmessage = lastmsg;
            allitem.get(pos).lastdate = lastdate;
            allitem.get(pos).unread = unread + "";

            itemMoveToFirst(pos);
        }
    }


    /**
     * 
     * move item from current position to first item
     * 
     * @param changedPosition item position before move to first
     */

    private void itemMoveToFirst(int changedPosition) {
        for (int i = changedPosition; i > 0; i--) {
            AllType firstItem = allitem.get(i);
            AllType secondItem = allitem.get(i - 1);
            allitem.set(i, secondItem);
            allitem.set((i - 1), firstItem);
        }
        notifyDataSetChanged();
    }


    public void updateSoundWithUid(String model, String uid, String value) {
        int pos = getPostition(uid, model);
        if (pos != -1) {
            allitem.get(pos).sound = value;
            notifyItemChanged(pos);
        }
    }


    public void deletedChannelOrGroupOrChat(String model, String uid, String lastmsg, String lastdate) {
        int pos = getPostition(uid, model);
        if (pos != -1) {
            int unread = G.cmd.getchannelunreadsize(uid);
            allitem.get(pos).lastmessage = lastmsg;
            allitem.get(pos).lastdate = lastdate;
            allitem.get(pos).unread = unread + "";
            allitem.get(pos).active = "2";
            itemMoveToFirst(pos);
        }
    }


    public void updateSeen(String model, String uid) {
        int pos = getPostition(uid, model);
        if (pos != -1) {
            int unread = 0;
            if (model.equals("1")) {
                unread = G.cmd.selectUnreadCountSingleChat(uid);
            } else if (model.equals("2")) {
                unread = G.cmd.selectUnreadCountGroup(uid);
            } else if (model.equals("3")) {
                unread = G.cmd.getchannelunreadsize(uid);
            }

            allitem.get(pos).unread = unread + "";
            notifyItemChanged(pos);
        }
    }


    public void clearhistorywithuid(String model, String uid) {
        int pos = getPostition(uid, model);
        if (pos != -1) {
            allitem.get(pos).lastdate = "";
            allitem.get(pos).lastmessage = "";
            int unread = 0;
            if (model.equals("1")) {
                unread = G.cmd.selectUnreadCountSingleChat(uid);
            } else if (model.equals("2")) {
                unread = G.cmd.selectUnreadCountGroup(uid);
            } else if (model.equals("3")) {
                unread = G.cmd.getchannelunreadsize(uid);
            }

            allitem.get(pos).unread = unread + "";
            notifyItemChanged(pos);
        }
    }


    public void kikedFromGroup(String model, String uid, String lastDate, String LastMessage) {

        int pos = getPostition(uid, model);
        if (pos != -1) {
            allitem.get(pos).lastdate = lastDate;
            allitem.get(pos).lastmessage = LastMessage;
            int unread = 0;
            if (model.equals("1")) {
                unread = G.cmd.selectUnreadCountSingleChat(uid);
            } else if (model.equals("2")) {
                unread = G.cmd.selectUnreadCountGroup(uid);
            } else if (model.equals("3")) {
                unread = G.cmd.getchannelunreadsize(uid);
            }

            allitem.get(pos).unread = unread + "";
            notifyItemChanged(pos);
        }

    }


    public void updateActiveItem(String model, String uid, String active, String LastMessage, String lastDate) {
        int pos = getPostition(uid, model);
        if (pos != -1) {
            allitem.get(pos).active = active;
            allitem.get(pos).lastmessage = LastMessage;
            allitem.get(pos).lastdate = lastDate;
            notifyItemChanged(pos);

        }
    }


    public void updateItemChannel(String model, String uid, String name, String desc, String membersNumber, String avatarLq, String avatarHq, String memberShip) {

        int pos = getPostition(uid, model);
        if (pos != -1) {
            allitem.get(pos).uid = uid;
            allitem.get(pos).name = name;
            allitem.get(pos).description = desc;
            allitem.get(pos).membersnumber = membersNumber;
            allitem.get(pos).avatar_lq = avatarLq;
            allitem.get(pos).avatar_hq = avatarHq;
            allitem.get(pos).membership = memberShip;

            notifyItemChanged(pos);
        }
    }


    public void updateItemgroup(String model, String uid, String name, String desc, String avatarLq, String avatarHq, String memberShip) {

        int pos = getPostition(uid, model);
        if (pos != -1) {
            allitem.get(pos).uid = uid;
            allitem.get(pos).name = name;
            allitem.get(pos).description = desc;
            allitem.get(pos).groupavatar = avatarLq;
            allitem.get(pos).avatar_hq = avatarHq;
            allitem.get(pos).membership = memberShip;

            notifyItemChanged(pos);
        }
    }


    public void updateavatar(String uid, String model, String avatar) {
        int pos = getPostition(uid, model);
        if (pos != -1) {
            allitem.get(pos).avatar_lq = avatar;

            notifyItemChanged(pos);
        }
    }


    public void deleteChatFromAll(String model, String uid) { // should run move to first  method for this code
        int pos = getPostition(uid, model);
        if (pos != -1) {
            if (allitem.size() == 1) { // bad az delete shodane in item list khali mishavad va bayad adamak namaysh dade shavad.
                Intent intent = new Intent("EMPTY_LIST_ALL");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
            }
            allitem.remove(pos);
            notifyDataSetChanged();
        }
    }


    public void updateMembersNumber(String model, String id, String numberOfMembers) {

        int pos = getPostition(id, model);

        if (pos != -1) {
            allitem.get(pos).membersnumber = numberOfMembers;
            notifyItemChanged(pos);
        }
    }


    public void newContact(String name, String userchat) {

        int pos = getPostition(userchat, "1");
        if (pos != -1) {
            allitem.get(pos).name = name;
            notifyItemChanged(pos);
        }
    }


    private SpannableStringBuilder parseText(String text) {
        PageMessagingPopularFunction pm = new PageMessagingPopularFunction();
        SpannableStringBuilder builder = pm.parseText(text, "PAGE_MESSAGING_LIST", false, mContext, 0);
        return builder;
    }


    private void updatesound(int pos, String value, int model) {

        if (model == 1) {
            G.cmd.updatechatsound(allitem.get(pos).id, value);
        } else if (model == 2) {
            G.cmd.updatesound(allitem.get(pos).groupchatid, value);

            Intent intent = new Intent("updateSoundGroup");
            intent.putExtra("uid", allitem.get(pos).groupchatid);
            intent.putExtra("value", value);
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

        } else if (model == 3) {
            G.cmd.updatechannelsound(allitem.get(pos).uid, value);

            Intent intent = new Intent("updateSound");
            intent.putExtra("uid", allitem.get(pos).uid);
            intent.putExtra("value", value);
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        }
        islong = false;
        allitem.get(pos).sound = value;
        notifyItemChanged(pos);
        islong = false;
        viewOff(viewholder, pos);
    }


    private void clearehistory(int pos, String lastMsgDate, String lastMsg, int model) {

        if (model == 1) {
            G.cmd.clearchathistory(allitem.get(pos).id);
            G.cmd.updatechatrooms(allitem.get(pos).userchat, lastMsgDate, lastMsg);

            Intent intent = new Intent("cleareWithUidChat");
            intent.putExtra("uid", allitem.get(pos).userchat);
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

            allitem.get(pos).lastdate = lastMsgDate;
            allitem.get(pos).lastmessage = lastMsg;

        } else if (model == 2) {
            G.cmd.cleargroupchathistory(allitem.get(pos).groupchatid);
            G.cmd.updategroupchatrooms(allitem.get(pos).groupchatid, lastMsgDate,
                    lastMsg);

            Intent intent = new Intent("clearHistoryWithUid");
            intent.putExtra("uid", allitem.get(pos).groupchatid);
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

            allitem.get(pos).lastdate = lastMsgDate;
            allitem.get(pos).lastmessage = lastMsg;

        } else if (model == 3) {
            G.cmd.clearChannelHistory(allitem.get(pos).uid);
        }

        notifyItemChanged(pos);
        islong = false;
        viewOff(viewholder, pos);
    }


    public void updateMemberShip(String model, String uid, String role) {
        int pos = getPostition(uid, model);
        if (pos != -1) {
            allitem.get(pos).membership = role;
            notifyItemChanged(pos);
        }
    }


    private void delete(int pos, ViewHolder h) {
        islong = false;
        viewOff(h, pos);
        allitem.remove(pos);
        visibleView.remove(pos);
        lastPosition = 9999;
        if (allitem.size() == 0) {
            Intent intent = new Intent("EMPTY_LIST_ALL");
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        }
        notif();
    }


    private void notif() {
        notifyDataSetChanged();
    }


    /**
     * 
     * detect item position with model and id
     * 
     * @param uid id for item depends on that is singleChat , GroupChat or Channel
     * @param model 1 (singleChat) or 2 (GroupChat) or 3 (Channel)
     * @return
     */

    private int getPostition(String uid, String model) {
        int pos = -1;

        if (model.equals("1")) { // SingleChat should change this state code
            for (int i = 0; i < allitem.size(); i++) {
                if (allitem.get(i).userchat != null) {
                    if (allitem.get(i).userchat.equals(uid)) {
                        pos = i;
                        break;
                    }
                }
            }
        } else if (model.equals("2")) { // Group
            for (int i = 0; i < allitem.size(); i++) {
                if (allitem.get(i).groupchatid != null) {

                    if (allitem.get(i).groupchatid.equals(uid)) {
                        pos = i;
                        break;
                    }
                }
            }
        } else if (model.equals("3")) { // Channel
            for (int i = 0; i < allitem.size(); i++) {
                if (allitem.get(i).uid != null) {
                    if (allitem.get(i).uid.equals(uid)) {
                        pos = i;
                        break;
                    }
                }
            }
        }

        return pos;
    }


    public void changeDateType(String value) {
        hijri = value;

        for (int i = 0; i < allitem.size(); i++) {
            if (allitem.get(i).lastdate != null && !allitem.get(i).lastdate.isEmpty() && !allitem.get(i).lastdate.equals("null") && !allitem.get(i).lastdate.equals("")) {
                String[] splitedrowtime = allitem.get(i).lastdate.split("\\s+");
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
