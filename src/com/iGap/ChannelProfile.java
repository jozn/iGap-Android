// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap;

import java.io.File;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.SwitchCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.iGap.adapter.ChannelAdapter;
import com.iGap.adapter.DrawableManagerDialog;
import com.iGap.adapter.G;
import com.iGap.customviews.ImageSquareProgressBarDialog;
import com.iGap.helpers.HelperCalculateMembers;
import com.iGap.helpers.HelperDrawAlphabet;
import com.iGap.helpers.HelperString;
import com.iGap.instruments.ConfirmationDialog;
import com.iGap.instruments.ImageLoader1;
import com.iGap.instruments.ImageLoaderProfile;
import com.iGap.instruments.ImageLoaderProfileLq;
import com.iGap.instruments.Utils;
import com.iGap.interfaces.OnColorChangedListenerSelect;
import com.iGap.interfaces.OnDeleteComplete;
import com.iGap.interfaces.OnDownloadListener;
import com.iGap.interfaces.OnLoadPhotoListener;


/**
 * 
 * namayesh va tanzimat etelate kanal
 */

public class ChannelProfile extends Activity {

    private Button                btnBack;
    private Button                btnNav;

    private TextView              txtListUl;
    private TextView              txtNotifi;
    private TextView              txtShared;

    private String                sound;
    private String                channeluid;
    private String                channelName;
    private String                channelDesc;
    private String                channelAvatarLq;
    private String                channelAvatarHq;
    private String                channelMembership;
    private String                channelMembersNumber;
    private String                channelActive;

    private PopupWindow           popUp;
    private ImageView             imgAvatar;
    private ImageLoader1          il;
    private Dialog                dialog;
    private ChannelAdapter        ChA;
    private ImageLoaderProfileLq  imageLoaderProfileLq;
    private DrawableManagerDialog dmDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.checkLanguage(this);
        setContentView(R.layout.activity_channel_profile);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            channeluid = extras.getString("channeluid");
            channelName = extras.getString("channelName");
            channelDesc = extras.getString("channelDesc");
            channelAvatarLq = extras.getString("channelAvatarLq");
            channelAvatarHq = extras.getString("channelAvatarHq");
            channelMembership = extras.getString("channelMembership");
            channelMembersNumber = extras.getString("channelMembersNumber");
            channelActive = extras.getString("channelActive");
        }

        sound = G.cmd.getchannelssound(channeluid);
        il = new ImageLoader1(ChannelProfile.this, G.basicAuth);
        ChA = new ChannelAdapter(ChannelProfile.this);
        imageLoaderProfileLq = new ImageLoaderProfileLq(ChannelProfile.this, G.basicAuth);
        dmDialog = new DrawableManagerDialog(ChannelProfile.this);

        intiUi();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Utils.checkLanguage(this);
        super.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        popupoptions(findViewById(R.id.btn_nav_ch));
        return false;
    }


    private void intiUi() {

        final SwitchCompat switchNotification = (SwitchCompat) findViewById(R.id.switch_notification);
        if (sound.equals("0")) {
            switchNotification.setChecked(true);
        } else {
            switchNotification.setChecked(false);
        }

        switchNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                int isexist = G.cmd.isChannelExist("channels", channeluid);
                if (isexist != 0) {
                    if (sound.equals("1")) {
                        G.cmd.updatechannelsound(channeluid, "0");
                        sound = "0";
                    } else {
                        G.cmd.updatechannelsound(channeluid, "1");
                        sound = "1";
                    }

                    // ===Send Sound State To PageAll
                    Intent intentAll = new Intent("updateSoundAll");
                    intentAll.putExtra("MODEL", "3");
                    intentAll.putExtra("UID", channeluid);
                    intentAll.putExtra("VALUE", sound);
                    LocalBroadcastManager.getInstance(ChannelProfile.this).sendBroadcast(intentAll);

                    // ===Send Sound State To PageAll
                    Intent intentChannel = new Intent("updateSound"); // ====in PageMessagingChannel
                    intentChannel.putExtra("uid", channeluid);
                    intentChannel.putExtra("value", sound);
                    LocalBroadcastManager.getInstance(ChannelProfile.this).sendBroadcast(intentChannel);

                    // ===Send To Channel
                    Intent intentToChannel = new Intent("updateSoundInChannel"); // ====in Channel
                    intentToChannel.putExtra("uid", channeluid);
                    intentToChannel.putExtra("value", sound);
                    LocalBroadcastManager.getInstance(ChannelProfile.this).sendBroadcast(intentToChannel);

                } else {
                    Toast.makeText(ChannelProfile.this, getString(R.string.you_are_not_member_ofthis_channel_en), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBack = (Button) findViewById(R.id.btn_back_ch);
        btnBack.setTypeface(G.fontAwesome);
        btnNav = (Button) findViewById(R.id.btn_nav_ch);
        btnNav.setTypeface(G.fontAwesome);
        imgAvatar = (ImageView) findViewById(R.id.img_user_icon_ch);

        LinearLayout lldeletechannel = (LinearLayout) findViewById(R.id.ll_delete_channel);

        TextView txtchannlename1 = (TextView) findViewById(R.id.txt_channle_name1);
        txtchannlename1.setTypeface(G.robotoBold);
        txtchannlename1.setText(channelName);

        TextView txtchannledes = (TextView) findViewById(R.id.txt_channle_des);
        txtchannledes.setTypeface(G.robotoLight);
        txtchannledes.setText(channelDesc);

        TextView txtuserchannlememberch = (TextView) findViewById(R.id.txt_user_channle_member_ch);
        txtuserchannlememberch.setTypeface(G.robotoLight);

        TextView txtChannleLinkCh = (TextView) findViewById(R.id.txt_channle_link_ch);
        txtChannleLinkCh.setTypeface(G.robotoLight);
        txtChannleLinkCh.setText("@" + channeluid);
        TextView txtNotificationCh = (TextView) findViewById(R.id.txt_notification_ch);
        txtNotificationCh.setTypeface(G.robotoLight);

        TextView txtPuzzleIcon_ch = (TextView) findViewById(R.id.txt_shared);
        txtPuzzleIcon_ch.setTypeface(G.robotoLight);

        TextView txtLeaveCh = (TextView) findViewById(R.id.txt_leave_ch);
        txtLeaveCh.setTypeface(G.robotoLight);

        LinearLayout layoutSharedMedia = (LinearLayout) findViewById(R.id.cp_ll_shared_media);
        layoutSharedMedia.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChannelProfile.this, SharedMedia.class);
                intent.putExtra("type", "3");
                intent.putExtra("uid", channeluid);
                intent.putExtra("basicAuth", G.basicAuth);
                intent.putExtra("name", channelName);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        if (channelMembersNumber != null && !channelMembersNumber.isEmpty() && !channelMembersNumber.equals("null") && !channelMembersNumber.equals("")) {
            HelperCalculateMembers cp = new HelperCalculateMembers();
            String members = cp.calculateMembers(channelMembersNumber);

            txtuserchannlememberch.setText(members);

        } else {
            txtuserchannlememberch.setText("< " + getString(R.string.number_of_member_en) + " > ");
        }

        txtListUl = (TextView) findViewById(R.id.txt_leave_icon_ch);
        txtListUl.setTypeface(G.fontAwesome);

        txtNotifi = (TextView) findViewById(R.id.txt_bell_icon_ch);
        txtNotifi.setTypeface(G.fontAwesome);

        txtShared = (TextView) findViewById(R.id.txt_puzzle_icon_ch);
        txtShared.setTypeface(G.fontAwesome);

        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });
        if (channelAvatarLq != null && !channelAvatarLq.isEmpty() && !channelAvatarLq.equals("null") && !channelAvatarLq.equals("")) {
            il.DisplayImage(channelAvatarHq, R.drawable.difaultimage, imgAvatar);
        } else {
            HelperDrawAlphabet pf = new HelperDrawAlphabet();
            Bitmap bm = pf.drawAlphabet(ChannelProfile.this, channelName, imgAvatar);
            imgAvatar.setImageBitmap(bm);

        }

        btnNav.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                popupoptions(v);

            }
        });
        lldeletechannel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ConfirmationDialog cm = new ConfirmationDialog(ChannelProfile.this, new OnColorChangedListenerSelect() {

                    @Override
                    public void colorChanged(String key, int color) {}


                    @Override
                    public void Confirmation(Boolean result) {
                        if (result) {
                            String userState = G.cmd.selectUserChannelState(channeluid);
                            ChA.deleteChannel(userState, channeluid, new OnDeleteComplete() {

                                @Override
                                public void deleteComplete(Boolean result) {
                                    if (result) {

                                        Intent intent = new Intent(ChannelProfile.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    }

                                }
                            });
                        }
                    }
                });
                cm.showdialog(getString(R.string.do_you_want_delete_this_channel));

            }
        });

        imgAvatar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (channelAvatarLq == null || channelAvatarLq.isEmpty() || channelAvatarLq.equals("null") || channelAvatarLq.equals("")) {
                    Toast.makeText(ChannelProfile.this, getString(R.string.channel_avatar_not_set_yet_en), Toast.LENGTH_SHORT).show();
                } else {
                    dialogimageview();
                }
            }
        });

    }


    @SuppressWarnings("deprecation")
    private void popupoptions(View v) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);

        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View mView = layoutInflater.inflate(R.layout.popup_channel, null);

        Button btnInviteAdmin = (Button) mView.findViewById(R.id.btn_channel_invite_admin);
        btnInviteAdmin.setVisibility(View.GONE);

        Button btnInviteMember = (Button) mView.findViewById(R.id.btn_channel_invite_member);
        btnInviteMember.setVisibility(View.GONE);

        Button btnClearHistory = (Button) mView.findViewById(R.id.btn_channel_clear_history);
        btnClearHistory.setVisibility(View.GONE);

        Button btnchannelinfo = (Button) mView.findViewById(R.id.btn_channel_info);
        btnchannelinfo.setVisibility(View.GONE);

        Button btneditchannel = (Button) mView.findViewById(R.id.btn_edit_channel);
        btneditchannel.setVisibility(View.GONE);

        String edtiChannel = getString(R.string.edit_en);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) { // allcaps is for api 14 and upper
            btneditchannel.setAllCaps(false);
        } else {
            edtiChannel = edtiChannel.substring(0, 1).toUpperCase() + edtiChannel.substring(1).toLowerCase();
        }
        btneditchannel.setText(edtiChannel);

        String inviteAdmin = getString(R.string.invite_admin_en);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {// allcaps is for api 14 and upper
            btnInviteAdmin.setAllCaps(false);
        } else {
            inviteAdmin = inviteAdmin.substring(0, 1).toUpperCase() + inviteAdmin.substring(1, 7).toLowerCase() + inviteAdmin.substring(7, 8).toUpperCase() + inviteAdmin.substring(8).toLowerCase();
        }
        btnInviteAdmin.setText(inviteAdmin);

        String inviteMember = getString(R.string.invite_member_en);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {// allcaps is for api 14 and upper

            btnInviteMember.setAllCaps(false);
        } else {
            inviteMember = inviteMember.substring(0, 1).toUpperCase() + inviteMember.substring(1, 7).toLowerCase() + inviteMember.substring(7, 8).toUpperCase() + inviteMember.substring(8).toLowerCase();
        }
        btnInviteMember.setText(inviteMember);

        if (channelMembership.equals("1")) {
            btneditchannel.setVisibility(View.VISIBLE);
            btnInviteAdmin.setVisibility(View.VISIBLE);
        }

        if (channelActive.equals("1")) {
            btnInviteMember.setVisibility(View.VISIBLE);

        }

        popUp = new PopupWindow(ChannelProfile.this);
        popUp.setContentView(mView);
        popUp.setWindowLayoutMode(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popUp.setHeight(1);
        popUp.setWidth(1);
        popUp.setTouchable(true);
        popUp.setFocusable(false);
        popUp.setOutsideTouchable(true);
        popUp.setBackgroundDrawable(new BitmapDrawable());
        popUp.showAtLocation(v, Gravity.NO_GRAVITY, location[0], location[1]);

        btneditchannel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (popUp != null && popUp.isShowing()) {
                    popUp.dismiss();
                }

                Intent intent = new Intent(ChannelProfile.this, EditChannel.class);
                intent.putExtra("channeluid", channeluid);
                intent.putExtra("channelName", channelName);
                intent.putExtra("channelDesc", channelDesc);
                intent.putExtra("channelAvatarLq", channelAvatarLq);
                intent.putExtra("channelAvatarHq", channelAvatarHq);
                intent.putExtra("channelMembership", channelMembership);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        btnInviteAdmin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (popUp != null && popUp.isShowing()) {
                    popUp.dismiss();
                }

                Intent intent = new Intent(ChannelProfile.this, InviteAdmin.class);
                intent.putExtra("channeluid", channeluid);
                intent.putExtra("channelName", channelName);
                intent.putExtra("channelDesc", channelDesc);
                intent.putExtra("channelAvatarLq", channelAvatarLq);
                intent.putExtra("channelMembership", channelMembership);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        btnInviteMember.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (popUp != null && popUp.isShowing()) {
                    popUp.dismiss();
                }

                Intent intent = new Intent(ChannelProfile.this, InviteMemberToChannel.class);
                intent.putExtra("channeluid", channeluid);
                intent.putExtra("channelName", channelName);
                intent.putExtra("channelDesc", channelDesc);
                intent.putExtra("channelAvatarLq", channelAvatarLq);
                intent.putExtra("channelMembership", channelMembership);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (popUp != null && popUp.isShowing()) {
            popUp.dismiss();
        }

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

    }


    private void dialogimageview() {

        dialog = new Dialog(ChannelProfile.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_imageview, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(dialogView);

        Button btnBack = (Button) dialogView.findViewById(R.id.btn_back);
        btnBack.setTypeface(G.fontAwesome);

        Button btnSetting = (Button) dialogView.findViewById(R.id.btn_setting);
        btnSetting.setTypeface(G.fontAwesome);

        Button btnShare = (Button) dialogView.findViewById(R.id.btn_share);
        btnShare.setTypeface(G.fontAwesome);

        TextView txtOf = (TextView) dialogView.findViewById(R.id.txt_of);
        txtOf.setTypeface(G.robotoBold);

        TextView txtUserName = (TextView) dialogView.findViewById(R.id.txt_user_name);
        txtUserName.setTypeface(G.robotoBold);

        TextView txtSendDate = (TextView) dialogView.findViewById(R.id.txt_send_date);
        txtSendDate.setTypeface(G.robotoLight);

        final LinearLayout llTop = (LinearLayout) dialogView.findViewById(R.id.ll_top);

        final LinearLayout llBottom = (LinearLayout) dialogView.findViewById(R.id.ll_bottom);

        final ImageSquareProgressBarDialog imgDialogImage = (ImageSquareProgressBarDialog) dialogView.findViewById(R.id.img_dialog_image);

        btnSetting.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                popupoptionsImage(v, imgDialogImage);
            }
        });

        imgDialogImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (llBottom.getVisibility() == View.VISIBLE) {
                    AlphaAnimation animation1 = new AlphaAnimation(1, 0);
                    animation1.setDuration(400);
                    llTop.setAnimation(animation1);
                    llBottom.setAnimation(animation1);

                    llTop.setVisibility(ViewGroup.GONE);
                    llBottom.setVisibility(ViewGroup.GONE);

                } else {

                    AlphaAnimation animation1 = new AlphaAnimation(0, 1);
                    animation1.setDuration(400);
                    llTop.setAnimation(animation1);
                    llBottom.setAnimation(animation1);
                    llTop.setVisibility(ViewGroup.VISIBLE);
                    llBottom.setVisibility(ViewGroup.VISIBLE);
                }
            }
        });

        final OnDownloadListener listener = new OnDownloadListener() {

            @Override
            public void onProgressDownload(final int percent, int downloadedSize, int fileSize, boolean completeDownload) {

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        imgDialogImage.setProgress(percent);
                    }
                });
            }
        };
        String fileName = HelperString.getFileName(channelAvatarHq);
        final String filePath = G.DIR_TEMP + "/" + channeluid + "_" + fileName;
        String profileImagePath = G.DIR_DIALOG + "/" + channeluid + "_" + fileName;
        File imgFile = new File(profileImagePath);

        if (imgFile.exists()) { // agar file ghblan download shode namayesh bede
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imgDialogImage.setImageBitmap(bitmap);
        } else {
            int loader = R.drawable.difaultimage;
            OnLoadPhotoListener loadPhotoListener = new OnLoadPhotoListener() {

                @Override
                public void onLoadPhotoListener() {
                    final ImageLoaderProfile imgLoaderProfile = new ImageLoaderProfile(ChannelProfile.this, channelAvatarHq, G.basicAuth, filePath, listener, dmDialog, imgDialogImage);
                    imgLoaderProfile.getBitmap();
                }
            };
            imageLoaderProfileLq.DisplayImage(channelAvatarLq, loader, imgDialogImage, loadPhotoListener);
        }

        txtSendDate.setVisibility(View.GONE);
        txtUserName.setText(channelName);
        txtOf.setVisibility(View.GONE);
        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }

            }
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }


    @SuppressWarnings("deprecation")
    private void popupoptionsImage(View v, final ImageSquareProgressBarDialog imageView) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);

        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View mView = layoutInflater.inflate(R.layout.popup_image_dialog, null);
        popUp = new PopupWindow(ChannelProfile.this);
        popUp.setContentView(mView);
        popUp.setWindowLayoutMode(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popUp.setHeight(1);
        popUp.setWidth(1);
        popUp.setTouchable(true);
        popUp.setFocusable(false);
        popUp.setOutsideTouchable(true);
        popUp.setBackgroundDrawable(new BitmapDrawable());
        popUp.showAtLocation(v, Gravity.NO_GRAVITY, location[0], location[1]);

        Button btndeleteavatar = (Button) mView.findViewById(R.id.btn_delete_avatar);
        btndeleteavatar.setVisibility(View.GONE);

        Button btnSaveAvatar = (Button) mView.findViewById(R.id.btn_save_avatar);
        btnSaveAvatar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                try {

                    if (Utils.SavePicToDownLoadFolder((Bitmap) imageView.getTag()))
                        Toast.makeText(getApplicationContext(), getString(R.string.picture_saved_en), Toast.LENGTH_SHORT).show();

                }
                catch (Exception e) {

                }

            }
        });

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
