// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;
import com.iGap.Crop.Crop;
import com.iGap.adapter.ChannelAdapter;
import com.iGap.adapter.ChannelRecycleAdapter;
import com.iGap.adapter.CustomPagerAdapter;
import com.iGap.adapter.DrawableManager;
import com.iGap.adapter.DrawableManagerDialog;
import com.iGap.adapter.G;
import com.iGap.adapter.StructCheckFileExist;
import com.iGap.helpers.HelperComoressImage;
import com.iGap.helpers.HelperComputeUnread;
import com.iGap.helpers.HelperDrawAlphabet;
import com.iGap.helpers.HelperFetchWebsiteData;
import com.iGap.helpers.HelperGetDataFromOtherApp;
import com.iGap.helpers.HelperGetDataFromOtherApp.FileType;
import com.iGap.helpers.HelperGetTime;
import com.iGap.helpers.HelperString;
import com.iGap.helpers.PageMessagingPopularFunction;
import com.iGap.instruments.Config;
import com.iGap.instruments.ImageLoader1;
import com.iGap.instruments.JSONParser;
import com.iGap.instruments.SlidingTabLayoutemoji;
import com.iGap.instruments.SoftKeyboard;
import com.iGap.instruments.Utils;
import com.iGap.instruments.explorer;
import com.iGap.instruments.myPaint;
import com.iGap.interfaces.OnColorChangedListenerSelect;
import com.iGap.interfaces.OnComplet;
import com.iGap.services.TimerServies;


/**
 * 
 * braye namayesh list payamhaye karbar dar kanal
 *
 */

public class Channel extends Activity {

    //************************* ArrayLists
    private ArrayList<String>          idarray                 = new ArrayList<String>();
    private ArrayList<String>          msgarray                = new ArrayList<String>();
    private ArrayList<String>          msgtimearray            = new ArrayList<String>();
    private ArrayList<String>          msgtypearray            = new ArrayList<String>();
    private ArrayList<String>          msgidarray              = new ArrayList<String>();
    private ArrayList<String>          msgseenarray            = new ArrayList<String>();
    private ArrayList<String>          msgviewarray            = new ArrayList<String>();
    private ArrayList<String>          msgsender               = new ArrayList<String>();
    private ArrayList<String>          msgStatusArray          = new ArrayList<String>();
    private ArrayList<String>          filehasharray           = new ArrayList<String>();
    private ArrayList<String>          filemimearray           = new ArrayList<String>();

    private ArrayList<Boolean>         selectedItem            = new ArrayList<Boolean>();
    private ArrayList<Boolean>         runUploaderThread       = new ArrayList<Boolean>();
    private ArrayList<Boolean>         runDownloaderThread     = new ArrayList<Boolean>();
    private ArrayList<Boolean>         runDownloaderListener   = new ArrayList<Boolean>();
    private ArrayList<Boolean>         runUploaderListener     = new ArrayList<Boolean>();

    private ArrayList<Integer>         hashPositions           = new ArrayList<Integer>();

    //************************* private values
    private int                        selectCounter;
    private int                        lastX;
    private int                        messagecash;
    private int                        leftPading;
    private int                        clickHashPos;
    private int                        newPosBottom;
    private int                        lastPosition;
    private int                        newMsgPosition          = -1;
    private int                        lastHighlightPosition   = -1;
    private int                        newPos                  = 0;
    private int                        beforOffset             = 0;
    private int                        secend                  = 0;
    private int                        minute                  = 0;
    private int                        Allmoving               = 0;
    private int                        DistanceToCancel        = 130;
    public static int                  visiblePosition         = -1;
    public static final int            MEDIA_TYPE_IMAGE        = 1;

    private String                     crop;
    private String                     channelAvatarLq;
    private String                     channelAvatarHq;
    private String                     channelMembership;
    private String                     channelMembersNumber;
    private String                     channeluid;
    private String                     selectedImagePath;
    private String                     channelmsg;
    private String                     channelName;
    private String                     channelDesc;
    private String                     type;
    private String                     filehash;
    private String                     channelActive;
    private String                     copytext;
    private String                     sendbyenter;
    private String                     forwardFrom;
    private String                     hashtakText;
    private String                     imageFileNameWithoutCrop;
    private String                     filePathWithoutCrop;
    private String                     hashtakBeforeScrollBottom;
    private String                     imageFileName;
    private String                     channelMuteState;
    private String                     lastFileMessageID;
    private String                     outputFile;
    private String                     filemime                = "";
    private String                     day                     = "";
    private String                     month                   = "";
    private String                     year                    = "";
    private String                     filePath                = "";
    private String                     lastHashtakID           = "";
    private String                     itemTag                 = "";

    private boolean                    loadTop;
    private boolean                    canStop                 = false;
    private boolean                    cansel                  = false;
    private boolean                    state                   = false;
    private boolean                    selectItemVisible       = false;
    private boolean                    inActivity              = false;
    private boolean                    smileClicked            = false;
    private boolean                    sendMessage             = false;
    private boolean                    edtTouch                = false;
    private boolean                    btnSendClicked          = false;
    private boolean                    isOpened                = false;
    private boolean                    sendPosition            = false;
    private boolean                    smileOpen               = false;
    private boolean                    hashtakSearch           = false;
    private boolean                    load                    = false;
    private boolean                    scrollToOffset          = false;
    private boolean                    loadBottomAllow         = false;
    private boolean                    showDefaultImage        = false;
    private boolean                    showDefaultImageCapture = false;
    private boolean                    needCrop                = false;
    private boolean                    iSender                 = false;
    private boolean                    isText                  = false;
    private boolean                    firstLoad               = true;
    private boolean                    loadBottom              = true;

    //************************* private variables & elements

    private Button                     btnMic;
    private Button                     btnDrawerFiles;
    private Button                     btnDrawerForward;
    private Button                     voiceIcon;
    private Button                     btnCancelSend;
    private Button                     btnSendFile;
    private Button                     btnUp;
    private Button                     btnDown;
    private Button                     btnCancel;
    private Button                     btnMuteChannel;

    private TextView                   txtedturls;
    private TextView                   txtuserarrowright;
    private TextView                   usernaveclose;
    private TextView                   txtforwardfrom;
    private TextView                   txtforwardmsg;
    private TextView                   txt_slide_to_cancel;
    private TextView                   txtTimeRecord;
    private TextView                   txtDrawerCounter;
    private TextView                   attachIcon;
    private TextView                   smaileIcon;
    private TextView                   tvDown;
    private TextView                   tvCamera;
    private TextView                   tvSience;
    private TextView                   tvVideo;
    private TextView                   tvMusic;
    private TextView                   tvPhone;
    private TextView                   tvFolder;
    private TextView                   tvPosition;
    private TextView                   tvPaint;
    private TextView                   txtHash;

    private EditText                   edtchat;

    private ImageView                  useravatar;
    private ImageView                  imgPicRecord;

    private LinearLayout               layout1;
    private LinearLayout               layout2;
    private LinearLayout               layout3;
    private LinearLayout               layout_music;
    private LinearLayout               lytMainHeader;
    private LinearLayout               lytHash;
    private LinearLayout               llVoice;
    private LinearLayout               layoutreplay;
    private LinearLayout               sideDrawer;
    private LinearLayout               mLayout_smile;
    private LinearLayout               lytRoot;
    private LinearLayout               lytMuteChannel;

    private SpannableStringBuilder     buildertext;
    private Dialog                     di;
    private SlidingTabLayoutemoji      mSlidingTabLayout;
    private ViewPager                  mViewPager;
    private LocationManager            locManager;
    private Dialog                     dialogSelectMedia;
    private ChannelRecycleAdapter      mAdapter;
    private PopupWindow                popUp;
    private TimerTask                  timertask;
    private Timer                      timer, secendTimer;
    private Uri                        fileUri;
    private MediaRecorder              mediaRecorder;
    private DrawableManager            dm;
    private InputMethodManager         imm                     = null;
    private SoftKeyboard               softKeyboard;
    private ImageLoader1               il;
    private ProgressDialog             pd;
    private View                       view;
    private ChannelAdapter             ca;
    private HelperGetTime              helperGetTime;
    private DrawableManagerDialog      dmDialog;
    private RecyclerView               groupchatlist;
    private RecyclerView.LayoutManager mLayoutManager;
    private static File                mediaFile;
    private JSONParser                 jParser                 = new JSONParser();
    private MediaPlayer                mp                      = new MediaPlayer();
    private TimerServies               timeService             = new TimerServies();


    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.checkLanguage(this);
        setContentView(R.layout.activity_channel);
        helperGetTime = G.helperGetTime;
        inActivity = true;
        ca = new ChannelAdapter(Channel.this);
        G.hashSearchType = 3;

        view = this.findViewById(android.R.id.content);

        lytHash = (LinearLayout) findViewById(R.id.lyt_hash);
        lytMainHeader = (LinearLayout) findViewById(R.id.lyt_main_header);

        btnUp = (Button) findViewById(R.id.btn_up);
        btnDown = (Button) findViewById(R.id.btn_down);
        btnCancel = (Button) findViewById(R.id.btn_cancel);

        txtHash = (TextView) findViewById(R.id.txt_hash);

        btnUp.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                clickHashPos--;
                if ((clickHashPos) >= 0) {
                    int highlightPos = hashPositions.get(clickHashPos);
                    int clearHighlightPos = hashPositions.get(clickHashPos + 1);
                    groupchatlist.scrollToPosition(highlightPos);

                    mAdapter.clearHighlightLayoutBackground(clearHighlightPos);
                    mAdapter.highlightLayoutBackground(highlightPos);

                    lastHighlightPosition = highlightPos;
                } else {
                    clickHashPos++;

                    int toppestHashtakPos = hashPositions.get(0); // first(toppest) hashtak pos
                    String toppestHashtakID = idarray.get(toppestHashtakPos);
                    int toppestHashtakOffset = G.cmd.selectOffsetOfIDFromTop("channelhistory", "channeluid = '" + channeluid + "'", toppestHashtakID);

                    int idOffset = G.cmd.selectOffsetOfID("channelhistory", "channeluid = '" + channeluid + "'", idarray.get(0));

                    String firstNewHashtakID = G.cmd.selectHashtakPosition1("channelhistory", "channeluid = '" + channeluid + "'", (idOffset + 1) + "", channeluid, hashtakText);
                    int offset = G.cmd.selectOffsetOfIDFromTop("channelhistory", "channeluid = '" + channeluid + "'", firstNewHashtakID); // new hashtak offset

                    if (offset == -1) {
                        Toast.makeText(Channel.this, "not exist more!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (toppestHashtakOffset - offset > 70) {
                            loadchathistoryHashtakLimit(offset, firstNewHashtakID);
                            reloadHashtakSearch(firstNewHashtakID);
                        } else {
                            loadchathistoryHashtak(offset, firstNewHashtakID);
                            reloadHashtakSearch(firstNewHashtakID);
                        }
                    }
                }
            }
        });

        btnDown.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                clickHashPos++;

                if ((clickHashPos) < hashPositions.size()) {
                    int highlightPos = hashPositions.get(clickHashPos);
                    int clearHighlightPos = hashPositions.get(clickHashPos - 1);
                    groupchatlist.scrollToPosition(highlightPos);

                    mAdapter.clearHighlightLayoutBackground(clearHighlightPos);
                    mAdapter.highlightLayoutBackground(highlightPos);

                    lastHighlightPosition = highlightPos;

                } else {
                    clickHashPos--;
                    int idOffset = G.cmd.selectOffsetOfIDFromTop("channelhistory", "channeluid = '" + channeluid + "'", idarray.get(idarray.size() - 1));
                    String firstNewHashtakID = G.cmd.selectHashtakPositionToBottom("channelhistory", "channeluid = '" + channeluid + "'", (idOffset + 1) + "", channeluid, hashtakText);

                    if (firstNewHashtakID.equals("-1")) {
                        Toast.makeText(Channel.this, "not exist more!", Toast.LENGTH_SHORT).show();
                    } else {
                        loadchathistoryHashtak(idOffset, firstNewHashtakID);
                        reloadHashtakSearch(firstNewHashtakID);
                    }
                }
            }
        });

        btnCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mAdapter.resethighlight();
                changeHeader();
            }
        });

        crop = G.cmd.getsetting(14);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            channeluid = extras.getString("channeluid");
            G.mainChannelUid = channeluid;
            channelName = extras.getString("channelName");
            channelDesc = extras.getString("channelDesc");
            channelAvatarLq = extras.getString("channelavatarlq");
            channelAvatarHq = extras.getString("channelavatarhq");
            channelMembership = extras.getString("channelmembership");
            channelMembersNumber = extras.getString("channelmembersnumber");
            channelActive = extras.getString("channelactive");
        }

        locManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        sendbyenter = G.cmd.getsetting(6);
        txtedturls = new TextView(Channel.this);
        il = new ImageLoader1(Channel.this, G.basicAuth);
        final TimerServies ts = new TimerServies();
        String currentTime;
        try {
            currentTime = ts.getDateTime();
        }
        catch (Exception e) {

            currentTime = helperGetTime.getTime();
        }

        String[] splitedtime = currentTime.split("\\s+");
        String date = splitedtime[0];

        String[] splited_date = date.split("-");
        year = splited_date[0];
        month = splited_date[1];
        day = splited_date[2];

        btnUp.setTypeface(G.fontAwesome);
        btnDown.setTypeface(G.fontAwesome);
        btnCancel.setTypeface(G.fontAwesome);

        Button backIcon = (Button) findViewById(R.id.back_icon);
        backIcon.setTypeface(G.fontAwesome);
        Button navIcon = (Button) findViewById(R.id.nav_icon);
        navIcon.setTypeface(G.fontAwesome);
        TextView usernametx = (TextView) findViewById(R.id.user_name_tx);
        usernametx.setTypeface(G.robotoBold);
        txtDrawerCounter = (TextView) findViewById(R.id.txt_drawer_counter);
        attachIcon = (TextView) findViewById(R.id.attach_icon1);
        attachIcon.setTypeface(G.fontAwesome);
        smaileIcon = (TextView) findViewById(R.id.smaile_icon);
        smaileIcon.setTypeface(G.fontAwesome);
        txtTimeRecord = (TextView) findViewById(R.id.txt_time_record);
        txt_slide_to_cancel = (TextView) findViewById(R.id.txt_slideto_cancel);

        layout_music = (LinearLayout) findViewById(R.id.ll_music_chat);
        final Button btnsend = (Button) findViewById(R.id.btn_send);
        btnsend.setTypeface(G.fontAwesome);
        voiceIcon = (Button) findViewById(R.id.voice_icon);
        voiceIcon.setTypeface(G.fontAwesome);
        btnDrawerForward = (Button) findViewById(R.id.btn_drawer_forward);
        btnDrawerForward.setTypeface(G.fontAwesome);
        btnDrawerFiles = (Button) findViewById(R.id.btn_drawer_files);
        btnDrawerFiles.setTypeface(G.fontAwesome);
        btnMic = (Button) findViewById(R.id.voice_icon);

        Button btnmicicon = (Button) findViewById(R.id.btn_mic_icon);
        btnmicicon.setTypeface(G.fontAwesome);

        llVoice = (LinearLayout) findViewById(R.id.ll_voice);
        LinearLayout llgroupinfo = (LinearLayout) findViewById(R.id.ll_groupinfo);
        sideDrawer = (LinearLayout) findViewById(R.id.Side_drawer);
        layout1 = (LinearLayout) findViewById(R.id.layout1);
        layout2 = (LinearLayout) findViewById(R.id.layout2);
        layout3 = (LinearLayout) findViewById(R.id.layout3);
        mLayout_smile = (LinearLayout) findViewById(R.id.layout_smile);
        lytRoot = (LinearLayout) findViewById(R.id.lyt_root);

        useravatar = (ImageView) findViewById(R.id.user_avatar);
        imgPicRecord = (ImageView) findViewById(R.id.img_pic_record);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        groupchatlist = (RecyclerView) findViewById(R.id.groupchatlist);
        groupchatlist.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(Channel.this);
        groupchatlist.setLayoutManager(mLayoutManager);

        groupchatlist.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                loadBottomAllow = true;
                return false;
            }
        });

        groupchatlist.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { // go to bottom
                    scrollToOffset = false;
                }
            }


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        edtchat = (EditText) findViewById(R.id.edt_chat);

        String mess = G.cmd.getLastMessage(channeluid, "3");
        if (mess.length() > 0) {
            PageMessagingPopularFunction pm = new PageMessagingPopularFunction();
            edtchat.setText(pm.parseText(mess, "CHAT_LIST", false, Channel.this, 0));
        }

        layoutreplay = (LinearLayout) findViewById(R.id.layout_replay);
        txtuserarrowright = (TextView) findViewById(R.id.user_arrow_right);
        txtuserarrowright.setTypeface(G.fontAwesome);
        usernaveclose = (TextView) findViewById(R.id.user_nave_close);
        usernaveclose.setTypeface(G.fontAwesome);
        txtforwardfrom = (TextView) findViewById(R.id.txt_forward_from);
        txtforwardmsg = (TextView) findViewById(R.id.txt_forwardmsg);

        btnCancelSend = (Button) findViewById(R.id.btn_cancel_send_channel);
        btnCancelSend.setTypeface(G.fontAwesome);

        LinearLayout lytDeleteChannel = (LinearLayout) findViewById(R.id.lyt_delete_channel);
        Button btnDeleteChannel = (Button) findViewById(R.id.btn_delete_channel);

        lytMuteChannel = (LinearLayout) findViewById(R.id.lyt_mute_channel);
        btnMuteChannel = (Button) findViewById(R.id.btn_mute_channel);

        channelMuteState = G.cmd.selectField("channels", "uid = '" + channeluid + "'", 9);

        if (channelActive.equals("2")) {
            layout1.setVisibility(View.GONE);
            attachIcon.setVisibility(View.GONE);
            lytDeleteChannel.setVisibility(View.VISIBLE);
        } else {
            if (channelMembership.equals("0")) {
                layout1.setVisibility(View.GONE);
                attachIcon.setVisibility(View.GONE);
                lytMuteChannel.setVisibility(View.VISIBLE);
            }

            if (channelMuteState.equals("0")) { // not Mute
                btnMuteChannel.setText(getString(R.string.mute_channel_en));
            } else { // Mute
                btnMuteChannel.setText(getString(R.string.unmute_channel_en));
            }
        }

        btnMuteChannel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (channelMuteState.equals("1")) { // do unMute
                    G.cmd.updatechannelsound(channeluid, "0");
                    btnMuteChannel.setText(getString(R.string.mute_channel_en));
                    channelMuteState = "0";
                    updatesoundwithuid(channeluid, "0");
                } else { // do Mute
                    G.cmd.updatechannelsound(channeluid, "1");
                    btnMuteChannel.setText(getString(R.string.unmute_channel_en));
                    channelMuteState = "1";
                    updatesoundwithuid(channeluid, "1");
                }
                Intent intentAll = new Intent("loadall");
                LocalBroadcastManager.getInstance(Channel.this).sendBroadcast(intentAll);
            }
        });

        btnDeleteChannel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                G.cmd.deletechannel(channeluid);
                deletewithuid(channeluid);
                Intent intentAll = new Intent("loadall");
                LocalBroadcastManager.getInstance(Channel.this).sendBroadcast(intentAll);
                finish();
            }
        });

        messagecash = G.cmd.getRowCount("messagingcache");

        if (messagecash != 0) {
            layoutreplay.setVisibility(View.VISIBLE);

            if (messagecash == 1) {
                buildertext = parseText(G.cmd.getmessagcash(0, 2), false);
                txtforwardfrom.setText(G.cmd.getmessagcash(0, 1));

                txtforwardmsg.setText(buildertext, BufferType.SPANNABLE);

            } else {
                int size = G.cmd.getRowCountmessagecash();
                for (int i = 0; i < size; i++) {
                    String name = G.cmd.getmessagcashfrom(i, 1);
                    if (forwardFrom != null && !forwardFrom.isEmpty() && !forwardFrom.equals("null") && !forwardFrom.equals("")) {
                        forwardFrom = forwardFrom + "," + name;
                    } else {
                        forwardFrom = name;
                    }
                }

                txtforwardfrom.setText(forwardFrom);
                txtforwardmsg.setText(messagecash + getString(R.string.forwarded_messages_en));
            }

        }

        InputMethodManager im = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        softKeyboard = new SoftKeyboard(lytRoot, im);
        softKeyboard.setSoftKeyboardCallback(new SoftKeyboard.SoftKeyboardChanged() {

            @Override
            public void onSoftKeyboardHide() {

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (smileOpen) {
                            llVoice.setVisibility(View.GONE);
                        } else {
                            llVoice.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }


            @Override
            public void onSoftKeyboardShow() {

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        llVoice.setVisibility(View.GONE);
                    }
                });
            }
        });

        usernametx.setText(channelName);

        mViewPager.setAdapter(new CustomPagerAdapter(this, edtchat));

        dm = new DrawableManager(Channel.this);
        dmDialog = new DrawableManagerDialog(Channel.this);
        mediaRecorder = new MediaRecorder();
        mp = new MediaPlayer();

        mSlidingTabLayout = (SlidingTabLayoutemoji) findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);

        LayoutParams params = mLayout_smile.getLayoutParams();
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        params.height = (display.getHeight() * 3) / 10;
        mLayout_smile.setLayoutParams(params);

        backIcon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                G.mainChannelUid = "";
                View view = Channel.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                finish();
            }
        });

        navIcon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                popupoptions(v);

            }
        });
        useravatar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Channel.this, ChannelProfile.class);
                intent.putExtra("channeluid", channeluid);
                intent.putExtra("channelName", channelName);
                intent.putExtra("channelDesc", channelDesc);
                intent.putExtra("channelAvatarLq", channelAvatarLq);
                intent.putExtra("channelAvatarHq", channelAvatarHq);
                intent.putExtra("channelMembership", channelMembership);
                intent.putExtra("channelMembersNumber", channelMembersNumber);
                intent.putExtra("channelActive", channelActive);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
        llgroupinfo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Channel.this, ChannelProfile.class);
                intent.putExtra("channeluid", channeluid);
                intent.putExtra("channelName", channelName);
                intent.putExtra("channelDesc", channelDesc);
                intent.putExtra("channelAvatarLq", channelAvatarLq);
                intent.putExtra("channelAvatarHq", channelAvatarHq);
                intent.putExtra("channelMembership", channelMembership);
                intent.putExtra("channelMembersNumber", channelMembersNumber);
                intent.putExtra("channelActive", channelActive);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
        if (channelAvatarLq != null && !channelAvatarLq.isEmpty() && !channelAvatarLq.equals("null") && !channelAvatarLq.equals("") && !channelAvatarLq.equals("empty")) {
            il.DisplayImage(channelAvatarLq, R.drawable.difaultimage, useravatar);
        } else {
            HelperDrawAlphabet pf = new HelperDrawAlphabet();
            Bitmap bm = pf.drawAlphabet(Channel.this, channelName, useravatar);
            useravatar.setImageBitmap(bm);

        }
        attachIcon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                initDialog();

            }
        });

        voiceIcon.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View arg0) {

                itemTag = "ivVoice";
                layout1.setVisibility(View.GONE);
                layout2.setVisibility(View.VISIBLE);

                startVoiceRecord();

                return false;
            }
        });
        edtchat.setOnKeyListener(new OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button

                if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {

                    SpannableStringBuilder builder = new SpannableStringBuilder(edtchat.getText());
                    int end = edtchat.getSelectionStart();

                    if (end > 5) {
                        String text = edtchat.getText().toString().substring(0, end);

                        if (text.endsWith("].png")) {
                            int start = -1;

                            for (int i = end - 4; i >= 0; i--) {
                                if (text.charAt(i) == '[') {
                                    start = i;
                                    break;
                                }
                            }

                            if (start >= 0) {
                                builder.delete(start, end);
                                edtchat.setText(builder);
                                edtchat.setSelection(start);
                                return true;
                            }
                        }
                    }
                }

                return false;
            }
        });

        btnsend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (G.hashtakSearch) {
                    mAdapter.resethighlight();
                    changeHeader();
                }

                type = "1";
                if (messagecash == 0) {

                    channelmsg = edtchat.getText().toString();
                    if (channelmsg != null && !channelmsg.isEmpty() && !channelmsg.equals("null") && !channelmsg.equals("")) {

                        if (smileOpen) {
                            closeLayoutSmile();
                            smileOpen = false;
                        }

                        edtchat.getText().clear();
                        String currentTime = null;
                        try {
                            currentTime = timeService.getDateTime();
                        }
                        catch (Exception e) {

                            currentTime = helperGetTime.getTime();
                        }

                        currentTime = helperGetTime.getTime();

                        G.cmd.addchannelhistory(channeluid, null, channelmsg, currentTime, "1", null, "1", "0", "1", "1", "");
                        String lastMessageID = G.cmd.selectChannelLastMessageID(channeluid);
                        //                        newChannelMessage("", channeluid, channelmsg, "1", currentTime, "1", "0", "", "", "", "", "1", null, lastMessageID);
                        newChannelMessage("", lastMessageID, channelmsg, "1", currentTime, "1", "0", "", "", "", "", "1", null, lastMessageID);

                        iSender = true;
                        new SendChannelMessage().execute("0", currentTime, channelmsg, lastMessageID, "0");

                        int getItemCount = ((LinearLayoutManager) mLayoutManager).getItemCount();
                        int getChildCount = ((LinearLayoutManager) mLayoutManager).getChildCount();

                        if (getItemCount == (getChildCount + 1)) { // agar item haye mojud kamtar az andazeye safhe bud
                            //((LinearLayoutManager) mLayoutManager).setStackFromEnd(false); // enteghal be entehaye safhe anjam nashavad
                        } else {
                            ((LinearLayoutManager) mLayoutManager).setStackFromEnd(true); // enteghal be entehaye safhe
                        }
                    } else {
                        Toast.makeText(Channel.this, getString(R.string.pleas_write_message_en), Toast.LENGTH_SHORT).show();
                    }

                } else {

                    channelmsg = edtchat.getText().toString();
                    if (channelmsg != null && !channelmsg.isEmpty() && !channelmsg.equals("null") && !channelmsg.equals("")) {

                        if (smileOpen) {
                            closeLayoutSmile();
                            smileOpen = false;
                        }

                        if (closingkeybord() == true) {
                            String currentTime = null;
                            try {
                                currentTime = timeService.getDateTime();
                            }
                            catch (Exception e) {

                                currentTime = helperGetTime.getTime();
                            }
                            currentTime = helperGetTime.getTime();

                            G.cmd.addchannelhistory(channeluid, null, channelmsg, currentTime, "1", null, "1", "0", "1", "1", "");
                            String lastMessageID = G.cmd.selectChannelLastMessageID(channeluid);
                            newChannelMessage("", lastMessageID, channelmsg, "1", currentTime, "1", "0", "", "", "", "", "1", null, lastMessageID);
                            iSender = true;
                            new SendChannelMessage().execute("0", currentTime, channelmsg, lastMessageID, "0");
                            int getItemCount = ((LinearLayoutManager) mLayoutManager).getItemCount();
                            int getChildCount = ((LinearLayoutManager) mLayoutManager).getChildCount();

                            if (getItemCount == (getChildCount + 1)) { // agar item haye mojud kamtar az andazeye safhe bud
                                //((LinearLayoutManager) mLayoutManager).setStackFromEnd(false); // enteghal be entehaye safhe anjam nashavad
                            } else {
                                ((LinearLayoutManager) mLayoutManager).setStackFromEnd(true); // enteghal be entehaye safhe
                            }
                        }

                    }

                    Cursor cursor = G.cmd.selectMessagingCache();
                    while (cursor.moveToNext()) {
                        //String forwardfrom = cursor.getString(cursor.getColumnIndex("forwardfrom"));
                        //String fileUrl = cursor.getString(cursor.getColumnIndex("fileurl"));
                        //String filethumbnail = cursor.getString(cursor.getColumnIndex("filethumbnail"));
                        String msg = cursor.getString(cursor.getColumnIndex("msg"));
                        String fileHash = cursor.getString(cursor.getColumnIndex("filehash"));
                        String fileType = cursor.getString(cursor.getColumnIndex("type"));
                        String filemime = cursor.getString(cursor.getColumnIndex("filemime"));

                        if (fileHash != null && !fileHash.isEmpty() && !fileHash.equals("null") && !fileHash.equals("")) {

                            String filepathh = "", status;
                            status = G.cmd.getfile(4, fileHash);

                            try {
                                filepathh = G.cmd.getfile(6, fileHash);

                                if (filepathh != null && !filepathh.isEmpty() && !filepathh.equals("null") && !filepathh.equals("")) {

                                } else {
                                    filepathh = "";
                                }

                            }
                            catch (Exception e) {
                                filepathh = "";
                            }

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                new chechfileexist().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, fileHash, fileType, msg, status, "0", filemime);
                            } else {
                                new chechfileexist().execute(fileHash, fileType, msg, status, "0", filemime);
                            }

                        } else {
                            String time;
                            try {
                                time = ts.getDateTime();
                            }
                            catch (Exception e) {
                                time = helperGetTime.getTime();
                            }
                            G.cmd.addchannelhistory(channeluid, null, msg, time, "1", null, "1", "0", "1", "1", filemime);
                            String lastMessageID = G.cmd.selectChannelLastMessageID(channeluid);
                            newChannelMessage("", lastMessageID, msg, "1", time, "1", "0", "", "", "", "", "1", null, lastMessageID);

                            iSender = true;
                            new SendChannelMessage().execute("0", time, msg, lastMessageID, "0");
                        }
                    }
                    cursor.close();

                    layoutreplay.setVisibility(View.GONE);
                    G.cmd.clearmessagecash();

                }
            }
        });

        mp.setOnCompletionListener(new OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer myp) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(Channel.this, getString(R.string.Compelete_Sound_en), Toast.LENGTH_SHORT).show();
                        //mp.release();
                        mp.stop();
                        mp.reset();
                    }
                });
            }
        });

        edtchat.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                llVoice.setVisibility(View.GONE);

                UnVisibleDrawer();
                selectedItem.clear();
                if (selectedItem.size() != idarray.size()) {

                    int ekhtelaf = idarray.size() - selectedItem.size();

                    for (int i = 0; i < ekhtelaf; i++) {
                        selectedItem.add(false);
                    }
                }

                mAdapter.notifyDataSetChanged();

                softKeyboard.openSoftKeyboard();
                edtchat.requestFocus();
                if (smileOpen) {

                    edtchat.getText().insert((edtchat.getText().length()), " ");

                    edtchat.setSelection(edtchat.getText().length());

                    closeLayoutSmile();
                    smileOpen = false;
                }

                G.HANDLER.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        groupchatlist.scrollToPosition(mAdapter.getItemCount() - 1);
                    }
                }, 500);

                return false;
            }
        });
        edtchat.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().endsWith("\n")) {
                    sendByEnter();
                }

                if (edtchat.getText().length() > 0) {

                    final int sdk = Build.VERSION.SDK_INT;
                    if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                        btnsend.setBackgroundDrawable(getResources().getDrawable(R.drawable.oval_green));
                    } else {
                        btnsend.setBackground(getResources().getDrawable(R.drawable.oval_green));
                    }
                }
                else {
                    final int sdk = Build.VERSION.SDK_INT;
                    if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                        btnsend.setBackgroundDrawable(getResources().getDrawable(R.drawable.oval_gray));
                    } else {
                        btnsend.setBackground(getResources().getDrawable(R.drawable.oval_gray));
                    }
                }

                if (s.length() == 0) {}
            }


            @Override
            public void beforeTextChanged(CharSequence s, int starts, int count, int after) {

                if (count - after == 1) {

                    try {
                        SpannableStringBuilder builder = new SpannableStringBuilder(edtchat.getText());
                        int end = edtchat.getSelectionStart();

                        if (end > 5) {
                            String text = edtchat.getText().toString().substring(0, end);

                            if (text.endsWith("].png")) {
                                int start = -1;

                                for (int i = end - 4; i >= 0; i--) {
                                    if (text.charAt(i) == '[') {
                                        start = i;
                                        break;
                                    }
                                }

                                if (start >= 0) {
                                    builder.delete(start, end);
                                    edtchat.setText(builder);
                                    edtchat.setSelection(start);
                                }
                            }
                        }
                    }
                    catch (Exception e1) {}
                }
            }


            @Override
            public void afterTextChanged(Editable s) {

                //namayesh etellate site ke adress an dar edittext vared shodeh ast

                txtedturls.setAutoLinkMask(Linkify.WEB_URLS);
                txtedturls.setText(s);
                URLSpan[] urlSpans = txtedturls.getUrls();
                if (urlSpans.length > 0) {
                    final String url = urlSpans[0].getURL().toString();
                    int exist = G.cmd.iswebsiteexist(url);
                    if (exist == 0) {

                        Thread thread = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                new HelperFetchWebsiteData(new OnComplet() {

                                    @Override
                                    public void complet(Boolean result, final String message) {

                                        runOnUiThread(new Runnable() {

                                            @Override
                                            public void run() {
                                                JSONObject jo;
                                                try {
                                                    jo = new JSONObject(message);
                                                    String title = jo.getString("title");
                                                    String description = jo.getString("description");

                                                    if (title != null && !title.isEmpty() && !title.equals("") && !title.equals("Domain not yet activated")) {
                                                        layoutreplay.setVisibility(View.VISIBLE);
                                                        txtforwardfrom.setText(title);
                                                        txtforwardmsg.setText(description);
                                                    }

                                                }
                                                catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                                    }
                                }).execute(url);

                            }
                        });
                        thread.start();
                    } else {
                        String title = G.cmd.getwebsiteinfo(url, 2);
                        String description = G.cmd.getwebsiteinfo(url, 3);

                        if (title != null && !title.isEmpty() && !title.equals("") && !title.equals("Domain not yet activated")) {
                            layoutreplay.setVisibility(View.VISIBLE);
                            txtforwardfrom.setText(title);
                            txtforwardmsg.setText(description);
                        }
                    }

                }
            }
        });

        edtchat.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    edtchat.setHint("");
                } else {
                    edtchat.setHint(getString(R.string.type_message_en));
                }
            }
        });

        smaileIcon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                type = "1";

                smileClicked = true;
                if (smileOpen) {

                    softKeyboard.openSoftKeyboard();
                    closeLayoutSmile();
                    smileOpen = false;
                } else {
                    softKeyboard.closeSoftKeyboard();
                    openLayoutSmile();

                    smileOpen = true;
                }

            }
        });

        btnDrawerFiles.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip;
                clip = ClipData.newPlainText("", "");
                clipboard.setPrimaryClip(clip);

                for (int i = 0; i < selectedItem.size(); i++) {

                    if (selectedItem.get(i) == true) {
                        if (copytext != null && !copytext.isEmpty() && !copytext.equals("null") && !copytext.equals("")) {
                            copytext = copytext + "\n" + msgarray.get(i);
                        } else {
                            copytext = msgarray.get(i);
                        }
                    }

                }

                clip = ClipData.newPlainText("iGap", copytext);
                clipboard.setPrimaryClip(clip);

                UnVisibleDrawer();
                selectedItem.clear();
                if (selectedItem.size() != idarray.size()) {

                    int ekhtelaf = idarray.size() - selectedItem.size();

                    for (int i = 0; i < ekhtelaf; i++) {
                        selectedItem.add(false);
                    }
                }
                loadchathistory();

            }
        });

        btnDrawerForward.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                G.cmd.clearmessagecash();
                for (int i = 0; i < selectedItem.size(); i++) {

                    if (selectedItem.get(i) == true) {

                        String fileurl;
                        String filethumbnail;
                        try {
                            fileurl = G.cmd.getfile(2, filehasharray.get(i));
                        }
                        catch (Exception e) {
                            fileurl = "";
                        }

                        try {
                            filethumbnail = G.cmd.getfile(3, filehasharray.get(i));
                        }
                        catch (Exception e) {
                            filethumbnail = "";
                        }

                        G.cmd.AddMessagecash(channelName, msgarray.get(i), filehasharray.get(i), fileurl, filethumbnail, msgtypearray.get(i), filemimearray.get(i));
                    }

                }
                finish();
            }
        });

        usernaveclose.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                layoutreplay.setVisibility(View.GONE);
                G.cmd.clearmessagecash();

            }
        });

        LocalBroadcastManager.getInstance(Channel.this).registerReceiver(mMessageReceiver, new IntentFilter("loadChannelInfo"));
        LocalBroadcastManager.getInstance(Channel.this).registerReceiver(SetAvatarChangeReceiver, new IntentFilter("SetAvatarChange"));
        LocalBroadcastManager.getInstance(Channel.this).registerReceiver(sendMessageChannel, new IntentFilter("sendMessageChannel"));
        LocalBroadcastManager.getInstance(Channel.this).registerReceiver(NewPostChannel, new IntentFilter("NewPostChannel"));
        LocalBroadcastManager.getInstance(Channel.this).registerReceiver(ChangeRole, new IntentFilter("ChangeRole"));
        LocalBroadcastManager.getInstance(Channel.this).registerReceiver(hashSearchChannel, new IntentFilter("hashSearchChannel"));
        LocalBroadcastManager.getInstance(Channel.this).registerReceiver(changeHeaderChannel, new IntentFilter("changeHeaderChannel"));

        LocalBroadcastManager.getInstance(Channel.this).registerReceiver(LoadChannel, new IntentFilter("LoadChannel"));
        LocalBroadcastManager.getInstance(Channel.this).registerReceiver(LoadBottomChannel, new IntentFilter("LoadBottomChannel"));

        LocalBroadcastManager.getInstance(Channel.this).registerReceiver(updateSoundInChannel, new IntentFilter("updateSoundInChannel"));

        int size = G.cmd.getchannelhistorysize(channeluid);
        lastPosition = (size - 1);
        loadchathistory();
        if (channelActive.equals("1")) {
            if (size == 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    new GetChannelMessage().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "", "3");
                } else {
                    new GetChannelMessage().execute("", "3");
                }
            } else {

                if (msgidarray.size() > 1) {
                    lastPosition = (msgidarray.size() - 1);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        new GetChannelMessage().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, msgidarray.get((msgidarray.size() - 1)), "2");
                    } else {
                        new GetChannelMessage().execute(msgidarray.get((msgidarray.size() - 1)), "2");
                    }

                }

            }

            if (msgidarray.size() > 0) {
                if (msgidarray.get(0) != null) {
                    if (size == 1 && msgidarray.get(0).equals("ChannelInvite")) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            new GetChannelMessage().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "", "3");
                        } else {
                            new GetChannelMessage().execute("", "3");
                        }
                    }
                }
            }
        }

        if (HelperGetDataFromOtherApp.hasSharedData) {
            HelperGetDataFromOtherApp.hasSharedData = false;

            G.HANDLER.postDelayed(new Runnable() { // set delay to prepare createchattest

                @Override
                public void run() {

                    if (HelperGetDataFromOtherApp.messageType == FileType.message) {

                        type = "1";
                        channelmsg = HelperGetDataFromOtherApp.message;

                        String currentTime = G.helperGetTime.getTime();

                        currentTime = helperGetTime.getTime();

                        G.cmd.addchannelhistory(channeluid, null, channelmsg, currentTime, "1", null, "1", "0", "1", "1", "");
                        String lastMessageID = G.cmd.selectChannelLastMessageID(channeluid);
                        newChannelMessage("", lastMessageID, channelmsg, "1", currentTime, "1", "0", "", "", "", "", "1", null, lastMessageID);

                        iSender = true;
                        new SendChannelMessage().execute("0", currentTime, channelmsg, lastMessageID, "0");

                    } else if (HelperGetDataFromOtherApp.messageType == FileType.image) {

                        preprationSendShareMessage("2", true);

                    } else if (HelperGetDataFromOtherApp.messageType == FileType.video) {

                        preprationSendShareMessage("3", false);

                    } else if (HelperGetDataFromOtherApp.messageType == FileType.audio) {

                        preprationSendShareMessage("4", false);

                    } else if (HelperGetDataFromOtherApp.messageType == FileType.file) {

                        for (int i = 0; i < HelperGetDataFromOtherApp.messageFileAddress.size(); i++) {

                            FileType fileType = HelperGetDataFromOtherApp.fileTypeArray.get(i);

                            if (fileType == FileType.image) {

                                preprationSendSingleShareMessage(HelperGetDataFromOtherApp.messageFileAddress.get(i), "2", true);

                            } else if (fileType == FileType.video) {

                                preprationSendSingleShareMessage(HelperGetDataFromOtherApp.messageFileAddress.get(i), "3", false);

                            } else if (fileType == FileType.audio) {

                                preprationSendSingleShareMessage(HelperGetDataFromOtherApp.messageFileAddress.get(i), "4", false);

                            } else if (fileType == FileType.file) {

                                getMultipleFileInfo(HelperGetDataFromOtherApp.messageFileAddress.get(i));

                            }

                        }
                    }
                }
            }, 2000);
        }
    }


    private void preprationSendSingleShareMessage(Uri uri, String type, boolean isImage) {
        getMultipleUri(uri, type, isImage);
    }


    private void preprationSendShareMessage(String type, boolean isImage) {
        for (int i = 0; i < HelperGetDataFromOtherApp.messageFileAddress.size(); i++) {
            getMultipleUri(HelperGetDataFromOtherApp.messageFileAddress.get(i), type, isImage);
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Utils.checkLanguage(this);
        super.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        popupoptions(findViewById(R.id.nav_icon));

        return false;
    }


    private void reloadHashtakSearch(String firstNewHashtakID) {
        hashtakSearch = true;
        G.hashtakSearch = true;

        lytMainHeader.setVisibility(View.GONE);
        lytHash.setVisibility(View.VISIBLE);
        hashPositions.clear();
        hashPositions = mAdapter.hashSearch(hashtakText); // positionha ie ke hashtak darand

        int newPos = idarray.indexOf(firstNewHashtakID);
        clickHashPos = hashPositions.indexOf(newPos); // positione ke dar arraye hashtak dar an bayad beravim

        mAdapter.highlightLayoutBackground(newPos);
        lastHighlightPosition = newPos;
    }


    private void changeHeader() {
        hashtakSearch = false;
        G.hashtakSearch = false;
        lytMainHeader.setVisibility(View.VISIBLE);
        lytHash.setVisibility(View.GONE);
    }

    private BroadcastReceiver updateSoundInChannel    = new BroadcastReceiver() {

                                                          @Override
                                                          public void onReceive(Context context, Intent intent) {
                                                              String uid = intent.getStringExtra("uid");
                                                              String value = intent.getStringExtra("value");

                                                              channelMuteState = value;
                                                              if (uid.equals(channeluid)) {
                                                                  if (channelMuteState.equals("0")) { // not Mute
                                                                      btnMuteChannel.setText(getString(R.string.mute_channel_en));
                                                                  } else { // Mute
                                                                      btnMuteChannel.setText(getString(R.string.unmute_channel_en));
                                                                  }
                                                              }
                                                          }
                                                      };
    private BroadcastReceiver LoadChannel             = new BroadcastReceiver() {

                                                          @Override
                                                          public void onReceive(Context context, Intent intent) {
                                                              scrollToOffset = true;
                                                              loadchathistory();
                                                          }
                                                      };

    private BroadcastReceiver LoadBottomChannel       = new BroadcastReceiver() {

                                                          @Override
                                                          public void onReceive(Context context, Intent intent) {
                                                              if (hashtakSearch) {
                                                                  hashtakBeforeScrollBottom = idarray.get(lastHighlightPosition); // idye balatarin hashtak ra ghabl az load list migirim 
                                                              }

                                                              if (loadBottomAllow) {
                                                                  loadchathistoryToBottom();
                                                              }
                                                          }
                                                      };

    private BroadcastReceiver changeHeaderChannel     = new BroadcastReceiver() {

                                                          @Override
                                                          public void onReceive(Context context, Intent intent) {

                                                              changeHeader();

                                                          }
                                                      };
    private BroadcastReceiver hashSearchChannel       = new BroadcastReceiver() {

                                                          @Override
                                                          public void onReceive(Context context, Intent intent) {
                                                              hashtakSearch = true;

                                                              G.hashtakSearch = true;

                                                              lytMainHeader.setVisibility(View.GONE);
                                                              lytHash.setVisibility(View.VISIBLE);

                                                              Bundle extras = intent.getExtras();
                                                              String text = extras.getString("TEXT");
                                                              hashtakText = text;
                                                              int position = extras.getInt("POSITION"); // position click shode ruye hashtak

                                                              txtHash.setText("#" + text);

                                                              hashPositions.clear();

                                                              hashPositions = mAdapter.hashSearch(text); // positionha ie ke hashtak darand
                                                              clickHashPos = hashPositions.indexOf(position); // positione click shode chandomin khaneye in araye hast
                                                              mAdapter.highlightLayoutBackground(position);
                                                              if (lastHighlightPosition != -1) {
                                                                  mAdapter.clearHighlightLayoutBackground(lastHighlightPosition);
                                                                  if (lastHighlightPosition == position) {
                                                                      mAdapter.highlightLayoutBackground(position);
                                                                  }
                                                              }
                                                              lastHighlightPosition = position;
                                                          }
                                                      };

    private BroadcastReceiver ChangeRole              = new BroadcastReceiver() {

                                                          @Override
                                                          public void onReceive(Context context, Intent intent) {

                                                              Bundle extras = intent.getExtras();
                                                              String role = extras.getString("ROLE");

                                                              changeRole(role);
                                                          }
                                                      };

    private BroadcastReceiver sendMessageChannel      = new BroadcastReceiver() {

                                                          @Override
                                                          public void onReceive(Context context, Intent intent) {

                                                              Bundle extras = intent.getExtras();
                                                              String fileHash = extras.getString("FILE_HASH");

                                                              sendChannelMessage(fileHash);
                                                          }
                                                      };

    private BroadcastReceiver NewPostChannel          = new BroadcastReceiver() {

                                                          @Override
                                                          public void onReceive(Context context, Intent intent) {

                                                              Bundle extras = intent.getExtras();
                                                              String channelUid = extras.getString("channel_uid");
                                                              String msg = extras.getString("msg");
                                                              String status = extras.getString("status");
                                                              String msgtime = extras.getString("msgtime");
                                                              String msgtype = extras.getString("msgtype");
                                                              String view = extras.getString("view");
                                                              String filehash = extras.getString("filehash");
                                                              String replyfilehash = extras.getString("replyfilehash");
                                                              String replymessage = extras.getString("replymessage");
                                                              String replyfrom = extras.getString("replyfrom");
                                                              String msgsender = extras.getString("msgsender");
                                                              String msgid = extras.getString("msgid");
                                                              String filemime = extras.getString("filemime");
                                                              newChannelMessage(filemime, channelUid, msg, status, msgtime, msgtype, view, filehash, replyfilehash, replymessage, replyfrom, msgsender, msgid, "");
                                                              groupchatlist.scrollToPosition(mAdapter.getItemCount() - 1);

                                                              int getItemCount = ((LinearLayoutManager) mLayoutManager).getItemCount();
                                                              int getChildCount = ((LinearLayoutManager) mLayoutManager).getChildCount();

                                                              if (getItemCount == (getChildCount + 1)) { // agar item haye mojud kamtar az andazeye safhe bud
                                                                  //((LinearLayoutManager) mLayoutManager).setStackFromEnd(false); // enteghal be entehaye safhe anjam nashavad
                                                              } else {
                                                                  ((LinearLayoutManager) mLayoutManager).setStackFromEnd(true); // enteghal be entehaye safhe
                                                              }
                                                          }
                                                      };

    private BroadcastReceiver SetAvatarChangeReceiver = new BroadcastReceiver() {

                                                          @Override
                                                          public void onReceive(Context context, Intent intent) {
                                                              String newAvatar = intent.getStringExtra("newAvatar");

                                                              if (newAvatar != null && !newAvatar.isEmpty() && !newAvatar.equals("null") && !newAvatar.equals("")) {
                                                                  il.DisplayImage(newAvatar, R.drawable.difaultimage, useravatar);
                                                              } else {

                                                                  HelperDrawAlphabet pf = new HelperDrawAlphabet();
                                                                  Bitmap bm = pf.drawAlphabet(Channel.this, channelName, useravatar);
                                                                  useravatar.setImageBitmap(bm);
                                                              }
                                                          }
                                                      };


    private void sendChannelMessage(String filehash) {

        ca.SendChannelMessageFromUploader(channeluid, filehash, "", "", new OnComplet() {

            @Override
            public void complet(Boolean result, String servertime) {
                iSender = false;
                isText = false;
                channelNewPost(channeluid, "", servertime);
                loadchathistory();

            }
        });
    }


    private void changeRole(String role) {
        if (role.equals("0")) {
            layout1.setVisibility(View.GONE);
            lytMuteChannel.setVisibility(View.VISIBLE);
        } else {
            layout1.setVisibility(View.VISIBLE);
            lytMuteChannel.setVisibility(View.GONE);
        }
    }


    /**
     * 
     * send message by Enter event
     * 
     */

    private void sendByEnter() {
        if (G.sendByEnter.equals("1")) {

            if (G.hashtakSearch) {
                mAdapter.resethighlight();
                changeHeader();
            }

            if (((LinearLayout) findViewById(R.id.layout_add_text_channel)).getVisibility() == View.VISIBLE) {
                ((Button) findViewById(R.id.btn_send_file_channel)).performClick();
                return;
            }

            type = "1";
            if (messagecash == 0) {

                channelmsg = edtchat.getText().toString();
                if (channelmsg != null && !channelmsg.isEmpty() && !channelmsg.equals("null") && !channelmsg.equals("")) {

                    if (smileOpen) {
                        closeLayoutSmile();
                        smileOpen = false;
                    }

                    edtchat.getText().clear();
                    String currentTime = null;
                    try {
                        currentTime = timeService.getDateTime();
                    }
                    catch (Exception e) {

                        currentTime = helperGetTime.getTime();
                    }

                    currentTime = helperGetTime.getTime();

                    G.cmd.addchannelhistory(channeluid, null, channelmsg, currentTime, "1", null, "1", "0", "1", "1", "");
                    String lastMessageID = G.cmd.selectChannelLastMessageID(channeluid);
                    newChannelMessage("", lastMessageID, channelmsg, "1", currentTime, "1", "0", "", "", "", "", "1", null, lastMessageID);

                    iSender = true;
                    new SendChannelMessage().execute("0", currentTime, channelmsg, lastMessageID, "0");

                    int getItemCount = ((LinearLayoutManager) mLayoutManager).getItemCount();
                    int getChildCount = ((LinearLayoutManager) mLayoutManager).getChildCount();

                    if (getItemCount == (getChildCount + 1)) { // agar item haye mojud kamtar az andazeye safhe bud
                        //((LinearLayoutManager) mLayoutManager).setStackFromEnd(false); // enteghal be entehaye safhe anjam nashavad
                    } else {
                        ((LinearLayoutManager) mLayoutManager).setStackFromEnd(true); // enteghal be entehaye safhe
                    }

                    ((LinearLayoutManager) mLayoutManager).setStackFromEnd(true);

                } else {
                    Toast.makeText(Channel.this, getString(R.string.pleas_write_message_en), Toast.LENGTH_SHORT).show();
                }

            } else {

                channelmsg = edtchat.getText().toString();
                if (channelmsg != null && !channelmsg.isEmpty() && !channelmsg.equals("null") && !channelmsg.equals("")) {

                    if (smileOpen) {
                        closeLayoutSmile();
                        smileOpen = false;
                    }

                    if (closingkeybord() == true) {
                        String currentTime = null;
                        try {
                            currentTime = timeService.getDateTime();
                        }
                        catch (Exception e) {

                            currentTime = helperGetTime.getTime();
                        }

                        currentTime = helperGetTime.getTime();

                        G.cmd.addchannelhistory(channeluid, null, channelmsg, currentTime, "1", null, "1", "0", "1", "1", "");
                        String lastMessageID = G.cmd.selectChannelLastMessageID(channeluid);
                        newChannelMessage("", lastMessageID, channelmsg, "1", currentTime, "1", "0", "", "", "", "", "1", null, lastMessageID);
                        iSender = true;
                        new SendChannelMessage().execute("0", currentTime, channelmsg, lastMessageID, "0");

                        int getItemCount = ((LinearLayoutManager) mLayoutManager).getItemCount();
                        int getChildCount = ((LinearLayoutManager) mLayoutManager).getChildCount();

                        if (getItemCount == (getChildCount + 1)) { // agar item haye mojud kamtar az andazeye safhe bud
                            //((LinearLayoutManager) mLayoutManager).setStackFromEnd(false); // enteghal be entehaye safhe anjam nashavad
                        } else {
                            ((LinearLayoutManager) mLayoutManager).setStackFromEnd(true); // enteghal be entehaye safhe
                        }
                    }

                }

                Cursor cursor = G.cmd.selectMessagingCache();
                while (cursor.moveToNext()) {
                    //String forwardfrom = cursor.getString(cursor.getColumnIndex("forwardfrom"));
                    //String fileUrl = cursor.getString(cursor.getColumnIndex("fileurl"));
                    //String filethumbnail = cursor.getString(cursor.getColumnIndex("filethumbnail"));
                    String msg = cursor.getString(cursor.getColumnIndex("msg"));
                    String fileHash = cursor.getString(cursor.getColumnIndex("filehash"));
                    String fileType = cursor.getString(cursor.getColumnIndex("type"));
                    String filemime = cursor.getString(cursor.getColumnIndex("filemime"));

                    if (fileHash != null && !fileHash.isEmpty() && !fileHash.equals("null") && !fileHash.equals("")) {

                        String filepathh = "", status;
                        status = G.cmd.getfile(4, fileHash);

                        try {
                            filepathh = G.cmd.getfile(6, fileHash);

                            if (filepathh != null && !filepathh.isEmpty() && !filepathh.equals("null") && !filepathh.equals("")) {

                            } else {
                                filepathh = "";
                            }

                        }
                        catch (Exception e) {
                            filepathh = "";
                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            new chechfileexist().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, fileHash, fileType, msg, status, "0", filemime);
                        } else {
                            new chechfileexist().execute(fileHash, fileType, msg, status, "0", filemime);
                        }

                    } else {

                        String time = G.helperGetTime.getTime();
                        G.cmd.addchannelhistory(channeluid, null, msg, time, "1", null, "1", "0", "1", "1", filemime);
                        String lastMessageID = G.cmd.selectChannelLastMessageID(channeluid);
                        newChannelMessage("", lastMessageID, msg, "1", time, "1", "0", "", "", "", "", "1", null, lastMessageID);

                        iSender = true;
                        new SendChannelMessage().execute("0", time, msg, lastMessageID, "0");
                    }
                }
                cursor.close();

                layoutreplay.setVisibility(View.GONE);
                G.cmd.clearmessagecash();
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && selectCounter > 0) {

            UnVisibleDrawer();
            selectedItem.clear();
            if (selectedItem.size() != idarray.size()) {

                int ekhtelaf = idarray.size() - selectedItem.size();

                for (int i = 0; i < ekhtelaf; i++) {
                    selectedItem.add(false);
                }
            }
            mAdapter.notifyDataSetChanged();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private boolean closingkeybord() {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                edtchat.getText().clear();
                edtchat.clearFocus();
                View view = Channel.this.getCurrentFocus();
                if (view != null) {
                    imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });

        if (imm.isAcceptingText()) {
            return false;
        } else {
            return true;
        }

    }


    private void UnVisibleDrawer() {
        G.longPressItem = false;
        selectItemVisible = false;
        selectCounter = 0;
        sideDrawer.setVisibility(View.GONE);
        mAdapter.invisibleDrawer();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

                                                   @Override
                                                   public void onReceive(Context context, Intent intent) {
                                                       loadchathistory();
                                                   }
                                               };


    private void openLayoutSmile() {

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                smaileIcon.setTag("on");
                mLayout_smile.setVisibility(LinearLayout.VISIBLE);
                llVoice.setVisibility(View.GONE);
            }
        }, 250);

    }


    private void closeLayoutSmile() {
        smaileIcon.setTag(null);
        mLayout_smile.setVisibility(LinearLayout.GONE);
        llVoice.setVisibility(View.VISIBLE);
    }


    private SpannableStringBuilder parseText(String text, boolean withLink) {
        PageMessagingPopularFunction pm = new PageMessagingPopularFunction();
        SpannableStringBuilder builder = pm.parseText(text, "CHAT_LIST", withLink, Channel.this, 0);
        return builder;
    }


    @Override
    public void onBackPressed() {
        if (G.longPressItem) {
            G.longPressItem = false;
            mAdapter.invisibleDrawer();
        } else {
            if (mLayout_smile.getVisibility() == View.VISIBLE) {
                closeLayoutSmile();
                return;
            }
            else {
                G.mainChannelUid = "";

                G.cmd.setLastMessage(channeluid, "3", edtchat.getText().toString());

                if (G.hashtakSearch) {
                    mAdapter.resethighlight();
                    changeHeader();
                    return;
                }

                super.onBackPressed();
            }
        }
    }


    private void sendChannelMessageToActivity() {
        Intent intent = new Intent("loadChannelInfo");
        LocalBroadcastManager.getInstance(Channel.this).sendBroadcast(intent);
    }


    private void loadchathistoryHashtak(int value, String firstNewHashtakID) {
        String hashtakOffset = (value - 50) + "";

        int size = G.cmd.getchannelhistorysize(channeluid);

        if (size != 0) {

            clearArray();

            groupchatlist.setVisibility(View.VISIBLE);

            Cursor cursorChannel = G.cmd.selectLimitOffset("channelhistory", "channeluid = '" + channeluid + "'", -1 + "", hashtakOffset);
            readCursor(cursorChannel);

            if (selectedItem.size() != msgidarray.size()) {

                int ekhtelaf = msgidarray.size() - selectedItem.size();

                for (int i = 0; i < ekhtelaf; i++) {
                    selectedItem.add(false);
                }
            }
            if (inActivity == true) {
                sendseen();
            }

            newPos = idarray.indexOf(firstNewHashtakID);

        } else {
            groupchatlist.setVisibility(View.INVISIBLE);
        }
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                groupchatlist.invalidate();
                mAdapter = new ChannelRecycleAdapter(filemimearray, idarray, msgarray, msgseenarray, msgStatusArray, msgtimearray, msgtypearray, msgidarray, selectedItem, filehasharray, msgviewarray, msgsender, runUploaderThread, runDownloaderThread, runDownloaderListener, runUploaderListener, Channel.this, year, month, day, il, view, dm, dmDialog, G.HANDLER, selectItemVisible, txtDrawerCounter, sideDrawer, lastFileMessageID, smileClicked, channelName, channeluid, channelActive, layout_music, load, loadBottom);
                groupchatlist.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                groupchatlist.scrollToPosition(newPos);
            }
        });
    }


    private void loadchathistoryHashtakLimit(int value, String firstNewHashtakID) {
        String hashtakOffset = (value - 50) + "";

        int size = G.cmd.getchannelhistorysize(channeluid);

        if (size != 0) {

            clearArray();

            groupchatlist.setVisibility(View.VISIBLE);

            Cursor cursorChannel = G.cmd.selectLimitOffset("channelhistory", "channeluid = '" + channeluid + "'", 100 + "", hashtakOffset);
            readCursor(cursorChannel);

            if (selectedItem.size() != msgidarray.size()) {

                int ekhtelaf = msgidarray.size() - selectedItem.size();

                for (int i = 0; i < ekhtelaf; i++) {
                    selectedItem.add(false);
                }
            }
            if (inActivity == true) {
                sendseen();
            }
            newPos = idarray.indexOf(firstNewHashtakID);

        } else {
            groupchatlist.setVisibility(View.INVISIBLE);
        }
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                groupchatlist.invalidate();
                mAdapter = new ChannelRecycleAdapter(filemimearray, idarray, msgarray, msgseenarray, msgStatusArray, msgtimearray, msgtypearray, msgidarray, selectedItem, filehasharray, msgviewarray, msgsender, runUploaderThread, runDownloaderThread, runDownloaderListener, runUploaderListener, Channel.this, year, month, day, il, view, dm, dmDialog, G.HANDLER, selectItemVisible, txtDrawerCounter, sideDrawer, lastFileMessageID, smileClicked, channelName, channeluid, channelActive, layout_music, true, true);
                groupchatlist.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                groupchatlist.scrollToPosition(newPos);
            }
        });
    }


    private void loadchathistoryToBottom() {

        if (hashtakSearch) {

            int size = G.cmd.getchannelhistorysize(channeluid);

            if (size != 0) {

                String lastListID = idarray.get(0);
                String lastListIDSetPos = idarray.get(idarray.size() - 1);

                int offsetOfLast = G.cmd.selectOffsetOfIDFromTop("channelhistory", "channeluid = '" + channeluid + "'", lastListID);

                clearArray();

                groupchatlist.setVisibility(View.VISIBLE);

                Cursor cursorChannel;
                cursorChannel = G.cmd.selectLimitOffset("channelhistory", "channeluid = '" + channeluid + "'", "-1", offsetOfLast + "");

                readCursor(cursorChannel);

                if (selectedItem.size() != msgidarray.size()) {

                    int ekhtelaf = msgidarray.size() - selectedItem.size();

                    for (int i = 0; i < ekhtelaf; i++) {
                        selectedItem.add(false);
                    }
                }
                if (inActivity == true) {
                    sendseen();
                }

                newPosBottom = idarray.indexOf(lastListIDSetPos);

            } else {
                groupchatlist.setVisibility(View.INVISIBLE);
            }
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    groupchatlist.invalidate();
                    mAdapter = new ChannelRecycleAdapter(filemimearray, idarray, msgarray, msgseenarray, msgStatusArray, msgtimearray, msgtypearray, msgidarray, selectedItem, filehasharray, msgviewarray, msgsender, runUploaderThread, runDownloaderThread, runDownloaderListener, runUploaderListener, Channel.this, year, month, day, il, view, dm, dmDialog, G.HANDLER, selectItemVisible, txtDrawerCounter, sideDrawer, lastFileMessageID, smileClicked, channelName, channeluid, channelActive, layout_music, load, loadBottom);
                    groupchatlist.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();

                    if (loadBottom) {
                        loadBottom = false;
                    }
                    groupchatlist.scrollToPosition(newPosBottom);

                    if (hashtakSearch) {
                        reloadHashtakSearch(hashtakBeforeScrollBottom);
                    }
                }
            });
        }
    }

    private String toppestID = null;


    private void loadchathistory() {
        G.cmd.deleteChannelMessageDuplicates();

        int size = G.cmd.getchannelhistorysize(channeluid);

        if (size != 0) {

            String firstID = "";

            if ( !firstLoad) {
                firstID = idarray.get(0);
            }

            if (hashtakSearch) {
                lastHashtakID = idarray.get(lastHighlightPosition); // idye balatarin hashtak ra ghabl az load list migirim 
            }

            clearArray();

            groupchatlist.setVisibility(View.VISIBLE);

            Cursor cursorChannel;
            if (iSender) {
                iSender = false;
                load = true;
                cursorChannel = G.cmd.selectHistoryByTime("channelhistory", "channeluid = '" + channeluid + "'", "30");
            } else if (loadTop) {
                loadTop = false;
                cursorChannel = G.cmd.selectHistoryByTime("channelhistory", "channeluid = '" + channeluid + "'", "-1");
            } else {

                if (firstLoad) {
                    firstLoad = false;
                    load = true;

                    cursorChannel = G.cmd.selectHistoryByTime("channelhistory", "channeluid = '" + channeluid + "'", "30");
                } else {

                    mAdapter.resetSelectedItem();

                    int idOffset = G.cmd.selectOffsetOfIDFromTopOrderByTime("channelhistory", "channeluid = '" + channeluid + "'", firstID);

                    String loadOffset = "0";
                    load = false;
                    if (idOffset - 10 > 0) {
                        loadOffset = (idOffset - 10) + "";
                        load = true;
                    }

                    cursorChannel = null;
                    if (idOffset == 0) { // agar balatar az in payam dar history vojud nadasht az server bekhan
                        if (toppestID != null) {
                            loadTop = true;
                            new GetChannelMessage().execute(toppestID + "", "1");
                        }
                    }
                    cursorChannel = G.cmd.selectLimitOffsetOrderByTime("channelhistory", "channeluid = '" + channeluid + "'", "-1", loadOffset + "");

                }
            }

            String newMessagePosition = "1";
            boolean detectNewMessagePos = true;
            visiblePosition = -1;

            while (cursorChannel.moveToNext()) {
                String chatid = cursorChannel.getString(cursorChannel.getColumnIndex("id"));
                String msgid = cursorChannel.getString(cursorChannel.getColumnIndex("msg_id"));
                String msg = cursorChannel.getString(cursorChannel.getColumnIndex("msg"));
                String msgtime = cursorChannel.getString(cursorChannel.getColumnIndex("msg_time"));
                String msgtype = cursorChannel.getString(cursorChannel.getColumnIndex("msg_type"));
                String filehash = cursorChannel.getString(cursorChannel.getColumnIndex("filehash"));
                String seen = cursorChannel.getString(cursorChannel.getColumnIndex("seen"));
                String view = cursorChannel.getString(cursorChannel.getColumnIndex("view"));
                String sender = cursorChannel.getString(cursorChannel.getColumnIndex("sender"));
                String status = cursorChannel.getString(cursorChannel.getColumnIndex("status"));
                String filemime = cursorChannel.getString(cursorChannel.getColumnIndex("filemime"));

                if (detectNewMessagePos) {
                    if ( !newMessagePosition.equals(seen) && seen.equals("0")) {
                        detectNewMessagePos = false;
                        newMsgPosition = cursorChannel.getPosition();
                        visiblePosition = cursorChannel.getPosition();
                    }
                    newMessagePosition = seen;
                }

                idarray.add(chatid);
                msgarray.add(msg);
                msgtimearray.add(msgtime);
                msgtypearray.add(msgtype);
                msgidarray.add(msgid);
                msgsender.add(sender);
                filehasharray.add(filehash);
                msgseenarray.add(seen);
                msgviewarray.add(view);
                msgStatusArray.add(status);
                filemimearray.add(filemime);
                runUploaderThread.add(true);
                runDownloaderThread.add(true);
                runDownloaderListener.add(true);
                runUploaderListener.add(true);

            }
            cursorChannel.close();

            if ( !msgidarray.isEmpty()) {
                toppestID = msgidarray.get(0);
            }
            if (selectedItem.size() != msgidarray.size()) {

                int ekhtelaf = msgidarray.size() - selectedItem.size();

                for (int i = 0; i < ekhtelaf; i++) {
                    selectedItem.add(false);
                }
            }
            if (inActivity == true) {
                sendseen();
            }

            beforOffset = idarray.indexOf(firstID); // ba avalin id mojud dar safhe ghable az load ba scroll makan payam ra migirim ta bad az load be haman makan beravim

        } else {
            groupchatlist.setVisibility(View.INVISIBLE);
        }
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                groupchatlist.invalidate();
                mAdapter = new ChannelRecycleAdapter(filemimearray, idarray, msgarray, msgseenarray, msgStatusArray, msgtimearray, msgtypearray, msgidarray, selectedItem, filehasharray, msgviewarray, msgsender, runUploaderThread, runDownloaderThread, runDownloaderListener, runUploaderListener, Channel.this, year, month, day, il, view, dm, dmDialog, G.HANDLER, selectItemVisible, txtDrawerCounter, sideDrawer, lastFileMessageID, smileClicked, channelName, channeluid, channelActive, layout_music, load, loadBottom);
                groupchatlist.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                if (scrollToOffset) {
                    groupchatlist.scrollToPosition(beforOffset);
                } else {
                    if (newMsgPosition == -1) {
                        groupchatlist.scrollToPosition(msgarray.size() - 1);
                    } else { // agar pyame jadid dasht be makane an payam miravad
                        groupchatlist.scrollToPosition(newMsgPosition);
                    }
                }

                if (hashtakSearch) {
                    reloadHashtakSearch(lastHashtakID);
                }
            }
        });
    }


    private void readCursor(Cursor cursorChannel) {
        while (cursorChannel.moveToNext()) {
            String chatid = cursorChannel.getString(cursorChannel.getColumnIndex("id"));
            String msgid = cursorChannel.getString(cursorChannel.getColumnIndex("msg_id"));
            String msg = cursorChannel.getString(cursorChannel.getColumnIndex("msg"));
            String msgtime = cursorChannel.getString(cursorChannel.getColumnIndex("msg_time"));
            String msgtype = cursorChannel.getString(cursorChannel.getColumnIndex("msg_type"));
            String filehash = cursorChannel.getString(cursorChannel.getColumnIndex("filehash"));
            String seen = cursorChannel.getString(cursorChannel.getColumnIndex("seen"));
            String view = cursorChannel.getString(cursorChannel.getColumnIndex("view"));
            String sender = cursorChannel.getString(cursorChannel.getColumnIndex("sender"));
            String status = cursorChannel.getString(cursorChannel.getColumnIndex("status"));
            String filemime = cursorChannel.getString(cursorChannel.getColumnIndex("filemime"));

            idarray.add(chatid);
            msgarray.add(msg);
            msgtimearray.add(msgtime);
            msgtypearray.add(msgtype);
            msgidarray.add(msgid);
            msgsender.add(sender);
            filehasharray.add(filehash);
            msgseenarray.add(seen);
            msgviewarray.add(view);
            msgStatusArray.add(status);
            filemimearray.add(filemime);
            runUploaderThread.add(true);
            runDownloaderThread.add(true);
            runDownloaderListener.add(true);
            runUploaderListener.add(true);
        }
        cursorChannel.close();
    }


    private void clearArray() {
        try {
            idarray.clear();
            msgarray.clear();
            msgtimearray.clear();
            msgtypearray.clear();
            msgseenarray.clear();
            msgviewarray.clear();
            msgsender.clear();
            msgidarray.clear();
            filehasharray.clear();
            msgStatusArray.clear();
            filemimearray.clear();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void sendseen() {

        G.HANDLER.post(new Runnable() {

            @Override
            public void run() {

                Cursor cursorUnread = G.cmd.selectUnreadMessageChannel("channelhistory", "channeluid ='" + channeluid + "'");
                while (cursorUnread.moveToNext()) {
                    String msgID = cursorUnread.getString(cursorUnread.getColumnIndex("msg_id"));
                    String seen = cursorUnread.getString(cursorUnread.getColumnIndex("seen"));
                    String sender = cursorUnread.getString(cursorUnread.getColumnIndex("sender"));

                    if ( !sender.equals("1")) { // payamhaye daryafti (agar ma ferestande bashim ==> sender == 1)
                        if ( !seen.equals("1")) { // payamhaye nakhande (seen = 1 , unseen = 0)
                            if (msgID != null && !msgID.isEmpty() && !msgID.equals("null") && !msgID.equals("")) {
                                G.cmd.updateChannelMessageStatus(channeluid, "1", msgID);
                            }
                        }
                    }
                }

                updateseen(channeluid);

                HelperComputeUnread.unreadMessageCount();
            }
        });

    }


    private boolean getFilePath(Intent data) {
        Uri selectedImageUri = data.getData();
        String[] proj = null;
        Cursor cursor = getContentResolver().query(selectedImageUri, proj, null, null, null);

        String filename = "";
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            int columnIndexname = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
            filename = cursor.getString(columnIndexname);
            imageFileNameWithoutCrop = filename;
            channelmsg = "";
            selectedImagePath = cursor.getString(column_index);
        }
        filePathWithoutCrop = selectedImagePath;
        File imgFile = new File(filePathWithoutCrop);
        int index = imgFile.getName().lastIndexOf('.') + 1;
        filemime = imgFile.getName().substring(index).toLowerCase();
        if (filemime.equals("gif")) {
            return false;
        }
        return true;
    }


    private void beginCrop(Uri source, Uri destination) {
        Crop.of(source, destination).asSquare().start(this);
    }


    //*********************** Multipel Send Message Start

    /**
     * 
     * prepreation file info for send to server
     * 
     * @param uri
     */

    private void getMultipleFileInfo(Uri uri) {

        File myFile = new File(uri.getPath());
        String filePath = myFile.getAbsolutePath();
        File imgFile = new File(filePath);
        int index = imgFile.getName().lastIndexOf('.') + 1;
        String fileMime = imgFile.getName().substring(index).toLowerCase();

        File file = new File(filePath);
        if (file.exists()) {
            getMultipleSha(filePath, fileMime, "7", false);
        }
    }


    private void getMultipleUri(Uri uri, String fileType, boolean isImage) {
        getMultipleFilePath(uri, fileType, isImage);
    }


    private void getMultipleFilePath(Uri uri, String fileType, boolean isImage) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        String filePath = "";
        String fileMime = "";

        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            filePath = cursor.getString(column_index);
        }

        File imgFile = new File(filePath);
        int index = imgFile.getName().lastIndexOf('.') + 1;
        fileMime = imgFile.getName().substring(index).toLowerCase();
        try {
            getMultipleSha(filePath, fileMime, fileType, isImage);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getMultipleSha(String filePath, String fileMime, String fileType, boolean isImage) {
        if (isImage) { // filePath is for Image so should compress image before upload
            filePath = HelperComoressImage.checkAndCompressImage(filePath);
        }

        String fileHash = fileHashFromFilePath(filePath);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new CheckMultipleFileExist().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, filePath, fileHash, fileMime, fileType);
        } else {
            new CheckMultipleFileExist().execute(filePath, fileHash, fileMime, fileType);
        }
    }


    /**
     * 
     * get source and create hash
     * 
     * @param source filePath
     * @return a string that detection from input filePath
     */

    private String fileHashFromFilePath(String source) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA1");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(source);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] dataBytes = new byte[1024];

        int nread = 0;

        try {
            while ((nread = fileInputStream.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        byte[] mdbytes = md.digest();

        if (fileInputStream != null) {
            try {
                fileInputStream.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < mdbytes.length; i++) {
            sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        String hash = sb.toString();
        return hash;
    }


    class CheckMultipleFileExist extends AsyncTask<String, String, StructCheckFileExist> {

        @Override
        protected StructCheckFileExist doInBackground(String... args) {
            String filePath = args[0];
            String fileHash = args[1];
            String fileMime = args[2];
            String fileType = args[3];

            StructCheckFileExist struct = null;

            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                JSONObject jsonobj = jParser.getJSONFromUrl(G.checkfileexist + fileHash, params, "GET", G.basicAuth, null);

                try {
                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);
                    boolean success = json.getBoolean(G.TAG_SUCCESS);

                    if (success) {
                        JSONArray result = json.getJSONArray(G.TAG_RESULT);
                        struct = new StructCheckFileExist();

                        for (int i = 0; i < result.length(); i++) {
                            JSONObject c = result.getJSONObject(i);

                            boolean exists = c.getBoolean("exists");

                            String url = "";
                            String thumb = "";

                            if (exists) {
                                url = c.getString("url");
                                thumb = c.getString("thumbnailLq");
                            }

                            struct.fileExist = exists;
                            struct.filePath = filePath;
                            struct.fileHash = fileHash;
                            struct.fileMime = fileMime;
                            struct.fileType = fileType;
                            struct.fileUrl = url;
                            struct.fileThumb = thumb;
                        }
                    } else {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(Channel.this, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return struct;
        }


        @Override
        protected void onPostExecute(StructCheckFileExist result) {

            if (result != null) { // zamani ke success false basha StructCheckFileExist meghdar dehi nashode ast banabarin null hast

                boolean fileExist = result.fileExist;

                String filePath = result.filePath;
                String fileHash = result.fileHash;
                String fileMime = result.fileMime;
                String fileType = result.fileType;
                String fileUrl = result.fileUrl;
                String fileThumb = result.fileThumb;

                if (fileExist == true) {
                    G.cmd.Addfiles1(fileHash, fileUrl, fileThumb, "", filePath);
                    String currentTime = G.helperGetTime.getTime();
                    iSender = true;
                    G.cmd.addchannelhistory(channeluid, null, "", currentTime, fileType, fileHash, "1", "0", "1", "1", fileMime);
                    String lastDbID = G.cmd.selectChannelLastMessageID(channeluid);

                    ca.SendChannelMessageFromUploader(channeluid, fileHash, lastDbID, "", new OnComplet() {

                        @Override
                        public void complet(Boolean result, String servertime) {

                            iSender = false;
                            isText = false;
                            channelNewPost(channeluid, "", servertime);
                            loadchathistory();

                        }
                    });

                    G.cmd.updatechannels(channeluid, "", currentTime);
                    loadchathistory();
                } else {
                    preparationFileUplaod(filePath, fileHash, fileMime, fileType);
                }

                /*
                if (fileExist == true) {

                    G.cmd.Addfiles1(fileHash, fileUrl, fileThumb, "5", filePath);
                    mService.sendgroupmessage(fileMime, gchid, "", avatar, fileType, fileHash, fileUrl, fileThumb, "", "", "", "0", null);

                } else {
                    preparationFileUplaod(filePath, fileHash, fileMime, fileType, fileUrl, fileThumb);
                }
                */
            }

            super.onPostExecute(result);
        }
    }


    private void preparationFileUplaod(String filePath, String fileHash, String fileMime, String fileType) {

        String currentTime = G.helperGetTime.getTime();

        G.cmd.Addfiles1(fileHash, null, null, "3", filePath);
        G.cmd.addchannelhistory(channeluid, null, "", currentTime, fileType, fileHash, "1", "0", "1", "1", fileMime);

        lastFileMessageID = G.cmd.selectChannelLastMessageID(channeluid);
        iSender = true;
        isText = false;
        loadchathistory();
    }


    //*********************** Multipel Send Message End

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (dialogSelectMedia != null && dialogSelectMedia.isShowing()) {
            dialogSelectMedia.dismiss();
        }

        if (requestCode == G.request_code_position && locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getPosition();
        }

        if (resultCode == Activity.RESULT_CANCELED) {
            if (showDefaultImage) {
                showDefaultImage = false;
                filePath = filePathWithoutCrop;

                try {
                    getsha(filePath, imageFileNameWithoutCrop, true);
                }
                catch (Exception e) {
                    Toast.makeText(Channel.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            if (showDefaultImageCapture) {
                showDefaultImageCapture = false;
                try {
                    filePath = fileUri.getPath();
                    String filename = mediaFile.getName();
                    channelmsg = "";
                    try {
                        getsha(filePath, filename, true);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                catch (Exception e) {
                    Toast.makeText(Channel.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }

        if (resultCode == Activity.RESULT_OK) {

            switch (requestCode) {
                case Crop.REQUEST_CROP:
                    showDefaultImage = false;
                    showDefaultImageCapture = false;
                    type = "2";
                    channelmsg = "";
                    try {
                        getsha(filePath, imageFileName, true);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

                case G.request_code_TAKE_PICTURE:

                    if (crop.equals("0")) { // disable

                        type = "2";

                        try {
                            filePath = fileUri.getPath();
                            String filename = mediaFile.getName();
                            channelmsg = "";
                            try {
                                getsha(filePath, filename, true);
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        catch (Exception e) {
                            Toast.makeText(Channel.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
                            return;
                        }

                    } else {

                        type = "2";
                        imageFileName = "" + System.currentTimeMillis();
                        filePath = G.DIR_CROP + "/" + imageFileName;
                        Uri destinationCapture = Uri.fromFile(new File(filePath));
                        beginCrop(fileUri, destinationCapture);
                        showDefaultImageCapture = true;

                    }

                    break;

                case G.request_code_PICK_IMAGE:

                    Uri selectedImageUri = data.getData();
                    if (selectedImageUri.toString().contains("images")) {

                        if (crop.equals("0")) { // disable

                            try {
                                String[] projection = { MediaStore.Images.Media.DATA };
                                @SuppressWarnings({ "deprecation" }) Cursor cursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
                                int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                                cursor.moveToLast();
                                String imagePath = cursor.getString(column_index_data);
                                BitmapFactory.decodeFile(imagePath);
                            }
                            catch (IllegalArgumentException e1) {
                                e1.printStackTrace();
                            }

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    type = "2";

                                    try {
                                        geturi(data, true);
                                    }
                                    catch (Exception e) {
                                        Toast.makeText(Channel.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            });

                        } else {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    type = "2";

                                    try {
                                        needCrop = getFilePath(data);
                                        showDefaultImage = true;
                                    }
                                    catch (Exception e) {
                                        Toast.makeText(Channel.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            });

                            if (needCrop) { // agar tasvir gif nabashad
                                channelmsg = "";
                                type = "2";
                                imageFileName = "" + System.currentTimeMillis();
                                filePath = G.DIR_CROP + "/" + imageFileName;
                                Uri destination = Uri.fromFile(new File(filePath));
                                beginCrop(data.getData(), destination);
                            } else {
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        type = "2";

                                        try {
                                            geturi(data, true);
                                        }
                                        catch (Exception e) {
                                            Toast.makeText(Channel.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }
                                });
                            }
                        }

                    } else if (selectedImageUri.toString().contains("video")) {

                        type = "3";
                        try {
                            geturi(data, false);

                        }
                        catch (Exception e) {
                            Toast.makeText(Channel.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }

                    break;

                case G.request_code_VIDEO_CAPTURED:
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            type = "3";
                            try {
                                geturi(data, false);

                            }
                            catch (Exception e) {
                                Toast.makeText(Channel.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    });
                    break;
                case G.request_code_audi:
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            type = "4";
                            try {
                                geturi(data, false);

                            }
                            catch (Exception e) {
                                Toast.makeText(Channel.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    });
                    break;
                case G.request_code_FILE:
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            type = "7";

                            try {
                                Uri datauri = data.getData();
                                File myFile = new File(datauri.getPath());
                                filePath = myFile.getAbsolutePath();
                                String filename = myFile.getName();
                                channelmsg = "";
                                File imgFile = new File(filePath);
                                int index = imgFile.getName().lastIndexOf('.') + 1;
                                filemime = imgFile.getName().substring(index).toLowerCase();

                                try {
                                    getsha(filePath, filename, false);
                                }
                                catch (Exception e) {

                                    e.printStackTrace();
                                }

                            }
                            catch (Exception e) {
                                Toast.makeText(Channel.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
                                return;
                            }

                        }
                    });
                    break;
                case G.request_code_cantact_phone:
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            type = "5";

                            try {
                                filePath = data.getData().toString();
                            }
                            catch (Exception e) {
                                Toast.makeText(Channel.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
                                return;
                            }

                            Uri uri = Uri.parse(filePath);
                            String[] projection = new String[]{ ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER };
                            Cursor people = getContentResolver().query(uri, projection, null, null, null);
                            int indexName = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                            int indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                            people.moveToFirst();
                            String name = people.getString(indexName);
                            String number = people.getString(indexNumber);
                            people.close();
                            channelmsg = "Contact Information : " + "\n" + "Name : " + name + "\n" + "Number : " + number;

                            String currentTime = null;
                            try {
                                currentTime = timeService.getDateTime();
                            }
                            catch (Exception e) {

                                currentTime = helperGetTime.getTime();
                            }

                            currentTime = helperGetTime.getTime();

                            G.cmd.addchannelhistory(channeluid, null, channelmsg, currentTime, "1", null, "1", "0", "1", "1", "");
                            String lastMessageID = G.cmd.selectChannelLastMessageID(channeluid);
                            newChannelMessage("", lastMessageID, channelmsg, "1", currentTime, "1", "0", "", "", "", "", "1", null, lastMessageID);

                            iSender = true;
                            new SendChannelMessage().execute("0", currentTime, channelmsg, lastMessageID, "0");
                            loadchathistory();
                        }
                    });
                    break;

                case G.request_code_paint:
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            type = "2";

                            try {
                                filePath = data.getData().toString();

                                File ff = new File(filePath);
                                String filename = ff.getName();
                                channelmsg = "";
                                try {
                                    getsha(filePath, filename, false);
                                }
                                catch (Exception e) {

                                    e.printStackTrace();
                                }

                            }
                            catch (Exception e) {
                                Toast.makeText(Channel.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
                                return;
                            }

                        }
                    });
                    break;

                default:
                    break;
            }
        }
    }


    public class StructSendChannelMessage {

        public boolean success;
        public String  message;
        public String  serverTime;
        public String  lastMsgID;
    }


    class SendChannelMessage extends AsyncTask<String, String, StructSendChannelMessage> {

        @Override
        protected StructSendChannelMessage doInBackground(String... args) {
            StructSendChannelMessage structChannel = new StructSendChannelMessage();
            try {

                jParser = new JSONParser();

                String channelMessageType = args[0];
                //String channelTime = args[1];
                String channelMessage = args[2];
                String channelType = "";
                String channelFilehash = "";
                String channelLastMessageID = "";
                String channelResend = "";
                String serverTime = "";

                structChannel.message = channelMessage;

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                if (channelMessageType.equals("0")) { // text message

                    channelLastMessageID = args[3];
                    channelResend = args[4];
                    structChannel.lastMsgID = channelLastMessageID;

                    isText = true;
                    params.add(new BasicNameValuePair("text", channelMessage));

                } else if (channelMessageType.equals("1")) { // file message

                    isText = false;
                    channelType = args[3];
                    channelFilehash = args[4];

                    params.add(new BasicNameValuePair("hash", channelFilehash));

                }
                JSONObject jsonobj = jParser.getJSONFromUrl(G.createchannel + "/" + channeluid + "/posts", params, "POST", G.basicAuth, null);
                boolean messageSuccess = false;

                try {
                    String statuscode = jsonobj.getString("statuscode");
                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);

                    messageSuccess = json.getBoolean(G.TAG_SUCCESS);
                    structChannel.success = messageSuccess;

                    if (messageSuccess == true) {

                        JSONObject result = json.getJSONObject("result");
                        String id = result.getString("id");
                        serverTime = result.getString("createAt");
                        serverTime = HelperGetTime.convertWithSingleTime(serverTime, G.utcMillis);
                        structChannel.serverTime = serverTime;

                        if (channelMessageType.equals("0")) { // text message

                            if (channelResend.equals("0")) { // send message
                                G.cmd.updateChannelIdAndTime(channeluid, id, serverTime, "2", channelLastMessageID);
                            } else if (channelResend.equals("1")) { // Resend Message
                                G.cmd.updateChannelMessageInResend(channeluid, id, serverTime, "2", channelLastMessageID);
                            }

                        } else { // file message

                            int isMsgExist = G.cmd.isChannelMessageExist(id);

                            if (isMsgExist == 0) {
                                G.cmd.addchannelhistory(channeluid, id, channelMessage, serverTime, channelType, channelFilehash, "1", "0", "1", "1", "");
                            } else {

                            }

                        }
                        G.cmd.updatechannels(channeluid, channelMessage, serverTime);
                    } else {

                        if (statuscode.equals("400")) {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(Channel.this, getString(R.string.illegal_characters), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else if (statuscode.equals("403")) {
                            String errorStatus = "";
                            JSONObject result = json.getJSONObject("result");
                            errorStatus = result.getString("errorStatus");
                            if (errorStatus.equals("1")) {
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        Toast.makeText(Channel.this, getString(R.string.channel_deleted), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else if (errorStatus.equals("2")) {
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        Toast.makeText(Channel.this, getString(R.string.channel_closed), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        Toast.makeText(Channel.this, getString(R.string.message_error1_en), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } else {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(Channel.this, getString(R.string.message_error1_en), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                }
                catch (JSONException e) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(Channel.this, getString(R.string.message_error1_en), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            catch (Exception e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(Channel.this, getString(R.string.message_error2_en), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            return structChannel;
        }


        @Override
        protected void onPostExecute(StructSendChannelMessage result) {
            boolean messageSuccess = result.success;
            String serverTime = result.serverTime;
            String channelMessage = result.message;
            String lastMsgID = result.lastMsgID;

            if (messageSuccess == true) {
                iSender = true;
                channelNewPost(channeluid, channelMessage, serverTime);
                updateMessageStatus(lastMsgID, serverTime);
                groupchatlist.scrollToPosition(mAdapter.getItemCount() - 1);
            }
        }
    }


    private class GetChannelMessage extends AsyncTask<String, String, String> {

        private boolean success1;
        private int     resultSize, position;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... args) {
            try {

                String msgID = args[0];
                String item = args[1];

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                JSONObject jsonobj;
                if (item.equals("1")) {
                    position = 1;

                    jsonobj = jParser.getJSONFromUrl(G.createchannel + "/" + channeluid + "/posts?first=" + msgID, params, "GET", G.basicAuth, null);

                } else if (item.equals("2")) {
                    position = 2;
                    jsonobj = jParser.getJSONFromUrl(G.createchannel + "/" + channeluid + "/posts?last=" + msgID, params, "GET", G.basicAuth, null);

                } else {

                    position = 3;
                    jsonobj = jParser.getJSONFromUrl(G.createchannel + "/" + channeluid + "/posts?", params, "GET", G.basicAuth, null);

                }

                try {
                    jsonobj.getString("statuscode");
                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);

                    success1 = json.getBoolean(G.TAG_SUCCESS);
                    if (success1 == true) {

                        JSONArray result = json.getJSONArray("result");

                        resultSize = result.length();

                        for (int i = 0; i < result.length(); i++) {
                            JSONObject response = result.getJSONObject(i);
                            String id = response.getString("id");
                            String hash = response.getString("hash");
                            String thumbLq = response.getString("thumbnailLq");
                            //String thumbHq = response.getString("thumbnailHq");
                            String url = response.getString("url");
                            String extention = response.getString("extention");
                            String text = response.getString("text");
                            String visit = response.getString("visit");
                            String addDate = response.getString("addDate");
                            String addTime = response.getString("addTime");

                            try {
                                text = URLDecoder.decode(text, "UTF-8");
                                addTime = URLDecoder.decode(addTime, "UTF-8");
                            }
                            catch (UnsupportedEncodingException e) {

                                e.printStackTrace();
                            }

                            String mainTime = HelperGetTime.convertTime(addDate, addTime, G.utcMillis);

                            String ext;

                            if (hash == null || hash.equals("null")) {
                                G.cmd.addchannelhistory(channeluid, id, text, mainTime, "1", null, "1", visit, "0", "0", "");

                            } else {

                                if (extention.equals("jpg") || extention.equals("gif") || extention.equals("JPEG")) {
                                    ext = "2";
                                } else if (extention.equals("mp4") || extention.equals("3gp")) {
                                    ext = "3";
                                } else if (extention.equals("mp3")) {
                                    ext = "4";
                                } else {
                                    ext = "7";
                                }

                                if (text == null || text.equals("null") || text.equals("")) {
                                    // just file

                                    G.cmd.addchannelhistory(channeluid, id, text, mainTime, ext, hash, "1", visit, "0", "0", extention);
                                    G.cmd.Addfiles(hash, url, thumbLq, "0");
                                } else {
                                    // file with text
                                    G.cmd.addchannelhistory(channeluid, id, text, mainTime, ext, hash, "1", visit, "0", "0", extention);
                                    G.cmd.Addfiles(hash, url, thumbLq, "0");
                                }
                            }

                            if (i == (result.length() - 1)) {

                            }
                        }

                        if (resultSize > 0) {
                            JSONObject response = result.getJSONObject((resultSize - 1));
                            String text = response.getString("text");
                            String addDate = response.getString("addDate");
                            String addTime = response.getString("addTime");

                            try {
                                text = URLDecoder.decode(text, "UTF-8");
                                addTime = URLDecoder.decode(addTime, "UTF-8");
                            }
                            catch (UnsupportedEncodingException e) {

                                e.printStackTrace();
                            }

                            String mainTime = HelperGetTime.convertTime(addDate, addTime, G.utcMillis);
                            G.cmd.updatechannels(channeluid, text, mainTime);
                        }

                    } else {
                        String errorStatus = "";
                        JSONObject result = json.getJSONObject("result");
                        errorStatus = result.getString("errorStatus");
                        if (errorStatus.equals("1")) {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(Channel.this, getString(R.string.channel_deleted), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else if (errorStatus.equals("2")) {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(Channel.this, getString(R.string.channel_closed), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(Channel.this, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                }
                catch (final JSONException e) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(Channel.this, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            catch (final Exception e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(Channel.this, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            return null;
        }


        @Override
        protected void onPostExecute(String file_url) {

            if (position == 1) {
                lastPosition = +resultSize;
            } else if (position == 2) {
                //lastPosition = resultSize;
            } else {
                lastPosition = resultSize;
            }

            if (success1 == true && resultSize > 0) {

                iSender = false;
                sendchatMessageToActivity(Channel.this);
                loadchathistory();
            }
        }
    }


    private class GetChannelMessageForViewer extends AsyncTask<String, String, String> {

        private boolean success1;
        private int     resultSize, position;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... args) {
            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                JSONObject jsonobj;
                position = 3;
                jsonobj = jParser.getJSONFromUrl(G.createchannel + "/" + channeluid + "/posts?", params, "GET", G.basicAuth, null);

                try {
                    jsonobj.getString("statuscode");
                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);

                    success1 = json.getBoolean(G.TAG_SUCCESS);

                    if (success1 == true) {

                        JSONArray result = json.getJSONArray("result");

                        resultSize = result.length();

                        ArrayList<String> ids = new ArrayList<String>();
                        ArrayList<String> hashs = new ArrayList<String>();
                        ArrayList<String> extentions = new ArrayList<String>();
                        ArrayList<String> texts = new ArrayList<String>();
                        ArrayList<String> visits = new ArrayList<String>();
                        ArrayList<String> mainTimes = new ArrayList<String>();
                        ArrayList<String> msgTypes = new ArrayList<String>();

                        for (int i = 0; i < result.length(); i++) {
                            JSONObject response = result.getJSONObject(i);
                            String id = response.getString("id");
                            String hash = response.getString("hash");
                            String thumbLq = response.getString("thumbnailLq");
                            //String thumbHq = response.getString("thumbnailHq");
                            String url = response.getString("url");
                            String extention = response.getString("extention");
                            String text = response.getString("text");
                            String visit = response.getString("visit");
                            String addDate = response.getString("addDate");
                            String addTime = response.getString("addTime");

                            try {
                                text = URLDecoder.decode(text, "UTF-8");
                                addTime = URLDecoder.decode(addTime, "UTF-8");
                            }
                            catch (UnsupportedEncodingException e) {

                                e.printStackTrace();
                            }

                            String mainTime = HelperGetTime.convertTime(addDate, addTime, G.utcMillis);

                            String ext;
                            if (extention.equals("jpg") || extention.equals("gif") || extention.equals("JPEG")) {
                                ext = "2";
                            } else if (extention.equals("mp4") || extention.equals("3gp")) {
                                ext = "3";
                            } else if (extention.equals("mp3")) {
                                ext = "4";
                            } else {
                                ext = "7";
                            }

                            if (hash == null || hash.equals("null")) {
                                ext = "1";
                                hash = null;
                            } else {
                                G.cmd.Addfiles(hash, url, thumbLq, "0");
                            }

                            ids.add(id);
                            hashs.add(hash);
                            extentions.add(extention);
                            texts.add(text);
                            visits.add(visit);
                            mainTimes.add(mainTime);
                            msgTypes.add(ext);
                        }

                        mAdapter.getChannelPostForViewer(ids, hashs, extentions, texts, visits, mainTimes, msgTypes);

                    } else {

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(Channel.this, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
                catch (final JSONException e) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(Channel.this, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            catch (final Exception e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(Channel.this, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            return null;
        }


        @Override
        protected void onPostExecute(String file_url) {

        }
    }


    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }


    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Config.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if ( !mediaStorageDir.exists()) {
            if ( !mediaStorageDir.mkdirs()) {

                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (popUp != null && popUp.isShowing()) {
            popUp.dismiss();
        }

        if (dialogSelectMedia != null && dialogSelectMedia.isShowing()) {
            dialogSelectMedia.dismiss();
        }

        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }

        if (di != null && di.isShowing()) {
            di.dismiss();
        }

    }


    public class StructCheckFile {

        public boolean exists;
        public String  fileHash;
        public String  fileUrl;
        public String  fileThumb;
        public String  fileType;
        public String  message;
        public String  status;
        public String  resend;
        public String  idInDb;
        public String  filemime;
    }


    class chechfileexist extends AsyncTask<String, String, StructCheckFile> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected StructCheckFile doInBackground(String... args) {

            boolean exists;
            String fileHash = args[0];
            String fileType = args[1];
            String channelMsg = args[2];
            String status = args[3];
            String resend = args[4];
            String filemime = args[5];
            String fileUrl;
            String fileThumb;
            StructCheckFile struct = new StructCheckFile();
            if (resend.equals("1")) {
                struct.idInDb = args[5];
            }
            struct.resend = resend;
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                JSONObject jsonobj;
                jsonobj = jParser.getJSONFromUrl(G.checkfileexist + fileHash, params, "GET", G.basicAuth, null);
                try {
                    jsonobj.getString("statuscode");
                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);
                    Boolean success = json.getBoolean(G.TAG_SUCCESS);

                    if (success) {
                        JSONArray result = json.getJSONArray(G.TAG_RESULT);

                        for (int i = 0; i < result.length(); i++) {
                            JSONObject c = result.getJSONObject(i);

                            exists = c.getBoolean("exists");

                            struct.fileHash = fileHash;
                            struct.message = channelMsg;
                            struct.fileType = fileType;
                            struct.status = status;
                            struct.exists = exists;
                            struct.filemime = filemime;

                            if (exists) {
                                fileUrl = c.getString("url");
                                fileThumb = c.getString("thumbnailLq");

                                struct.fileUrl = fileUrl;
                                struct.fileThumb = fileThumb;
                            }
                        }
                    } else {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(Channel.this, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
                catch (JSONException e) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(Channel.this, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            catch (Exception e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(Channel.this, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            return struct;
        }


        @Override
        protected void onPostExecute(StructCheckFile result) {

            boolean exists = result.exists;
            String fileHash = result.fileHash;
            String fileThumb = result.fileThumb;
            String fileUrl = result.fileUrl;
            String fileType = result.fileType;
            String status = result.status;
            String filemime = result.filemime;
            final String message1 = result.message;

            try {
                if (message1 == null) {
                    channelmsg = URLEncoder.encode("", "UTF-8");
                } else {
                    channelmsg = URLEncoder.encode(message1, "UTF-8");
                }
            }
            catch (UnsupportedEncodingException e1) {

                channelmsg = message1;
                e1.printStackTrace();
            }

            String resend = result.resend;
            String idInDb = result.idInDb;

            if (resend.equals("1")) { //Resend

                if (exists == true) {

                    G.cmd.updatefilestatus(fileHash, 5);

                    ca.SendChannelMessageFromUploader(channeluid, fileHash, idInDb, channelmsg, new OnComplet() {

                        @Override
                        public void complet(Boolean result, String servertime) {

                            iSender = false;
                            isText = false;
                            channelNewPost(channeluid, message1, servertime);
                            loadchathistory();
                        }
                    });
                } else {

                    lastFileMessageID = idInDb;

                    G.cmd.updatefilestatus(fileHash, 3);
                    loadchathistory();
                }

            } else { // Send
                if (exists == true) {
                    G.cmd.Addfiles1(fileHash, fileUrl, fileThumb, status, filePath);
                    String currentTime;
                    try {
                        currentTime = timeService.getDateTime();
                    }
                    catch (Exception e) {
                        currentTime = helperGetTime.getTime();
                    }
                    iSender = true;
                    G.cmd.addchannelhistory(channeluid, null, message1, currentTime, fileType, fileHash, "1", "0", "1", "1", filemime);
                    String lastDbID = G.cmd.selectChannelLastMessageID(channeluid);

                    ca.SendChannelMessageFromUploader(channeluid, fileHash, lastDbID, channelmsg, new OnComplet() {

                        @Override
                        public void complet(Boolean result, String servertime) {

                            iSender = false;
                            isText = false;
                            channelNewPost(channeluid, channelmsg, servertime);
                            loadchathistory();

                        }
                    });

                    G.cmd.updatechannels(channeluid, message1, currentTime);
                    loadchathistory();
                } else {
                    alertdialogupfile(fileHash, message1, fileType, filemime);
                }
            }

            super.onPostExecute(result);
        }
    }


    private void alertdialogupfile(String filehash, String msg, String type, String filemime) {

        if (type == null) {
            type = this.type;
        }

        if (filehash == null) {
            filehash = this.filehash;
        }

        if (msg == null) {
            msg = this.channelmsg;
        }

        G.cmd.Addfiles1(filehash, null, null, "3", filePath);
        String currentTime;
        try {
            currentTime = timeService.getDateTime();
        }
        catch (Exception e) {
            currentTime = helperGetTime.getTime();
        }

        G.cmd.addchannelhistory(channeluid, null, msg, currentTime, type, filehash, "1", "0", "1", "1", filemime);

        lastFileMessageID = G.cmd.selectChannelLastMessageID(channeluid);
        iSender = true;
        isText = false;
        loadchathistory();
    }


    private void initDialog() {

        dialogSelectMedia = new Dialog(Channel.this);
        dialogSelectMedia.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSelectMedia.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogSelectMedia.setContentView(R.layout.dialog_select_media);
        dialogSelectMedia.setCancelable(true);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialogSelectMedia.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.TOP;
        dialogSelectMedia.show();
        dialogSelectMedia.getWindow().setAttributes(layoutParams);

        tvDown = (TextView) dialogSelectMedia.findViewById(R.id.textView_down);
        tvDown.setTypeface(G.fontAwesome);
        tvDown.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (dialogSelectMedia != null)
                    if (dialogSelectMedia.isShowing())
                        dialogSelectMedia.cancel();
            }
        });

        tvCamera = (TextView) dialogSelectMedia.findViewById(R.id.textView_camera);
        tvCamera.setTypeface(G.fontAwesome);
        tvCamera.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                pictureByCamera();
            }
        });

        tvSience = (TextView) dialogSelectMedia.findViewById(R.id.textView_sience);
        tvSience.setTypeface(G.fontAwesome);
        tvSience.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                openGallery();
            }
        });

        tvVideo = (TextView) dialogSelectMedia.findViewById(R.id.textView_video);
        tvVideo.setTypeface(G.fontAwesome);
        tvVideo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                videoByCamera();
            }
        });

        tvMusic = (TextView) dialogSelectMedia.findViewById(R.id.textView_music);
        tvMusic.setTypeface(G.fontAwesome);
        tvMusic.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                audio();
            }
        });

        tvPhone = (TextView) dialogSelectMedia.findViewById(R.id.textView_phone);
        tvPhone.setTypeface(G.fontAwesome);
        tvPhone.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                pickContact();
            }
        });

        tvFolder = (TextView) dialogSelectMedia.findViewById(R.id.textView_folder);
        tvFolder.setTypeface(G.fontAwesome);
        tvFolder.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                file();
            }
        });

        tvPosition = (TextView) dialogSelectMedia.findViewById(R.id.textView_position);
        tvPosition.setTypeface(G.fontAwesome);
        tvPosition.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (dialogSelectMedia != null && dialogSelectMedia.isShowing()) {
                    dialogSelectMedia.dismiss();
                }

                getPosition();
            }
        });

        tvPaint = (TextView) dialogSelectMedia.findViewById(R.id.textView_paint5);
        tvPaint.setTypeface(G.fontAwesome);
        tvPaint.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(Channel.this, myPaint.class);
                startActivityForResult(intent, G.request_code_paint);
            }
        });

    }


    void pictureByCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, G.request_code_TAKE_PICTURE);
    }


    void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/* video/*");
        startActivityForResult(intent, G.request_code_PICK_IMAGE);
    }


    void videoByCamera() {

        PackageManager packageManager = getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) == false) {
            Toast.makeText(Channel.this, getString(R.string.device_dosenot_camera_en), Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, G.request_code_VIDEO_CAPTURED);
    }


    void audio() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setData(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, G.request_code_audi);
    }


    void file() {
        Intent intent = new Intent(Channel.this, explorer.class);
        startActivityForResult(intent, G.request_code_FILE);
    }


    void pickContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(intent, G.request_code_cantact_phone);
    }


    /**
     * be dast avardan mokhtast gps karbar
     */

    private void getPosition() {

        try {
            if ( !locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showSettingsAlert();
            }
            else {
                locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

                Location location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location == null) {
                    location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
                if (location != null)
                {
                    location.getLatitude();
                    location.getLongitude();

                    type = "6";
                    String currentTime = null;
                    try {
                        currentTime = timeService.getDateTime();
                    }
                    catch (Exception e) {

                        currentTime = helperGetTime.getTime();
                    }

                    channelmsg = "My Position is : " + "\n" + "Latitude : " + String.valueOf(location.getLatitude()) + "\n" + "Longitude : " + String.valueOf(location.getLongitude());
                    G.cmd.addchannelhistory(channeluid, null, channelmsg, currentTime, "1", null, "1", "0", "1", "1", "");
                    String lastMessageID = G.cmd.selectChannelLastMessageID(channeluid);
                    newChannelMessage("", lastMessageID, channelmsg, "1", currentTime, "1", "0", "", "", "", "", "1", null, lastMessageID);
                    new SendChannelMessage().execute("0", currentTime, channelmsg, lastMessageID, "0");
                    loadchathistory();
                }
                else {
                    sendPosition = true;
                    pd = new ProgressDialog(Channel.this);
                    pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    pd.setMessage(getString(R.string.just_wait_en));
                    pd.setIndeterminate(false);
                    pd.setCancelable(true);
                    pd.show();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    LocationListener locationListener = new LocationListener() {

                                          @Override
                                          public void onStatusChanged(String provider, int status, Bundle extras) {

                                          }


                                          @Override
                                          public void onProviderEnabled(String provider) {

                                          }


                                          @Override
                                          public void onProviderDisabled(String provider) {

                                          }


                                          @Override
                                          public void onLocationChanged(Location location) {

                                              if (sendPosition) {
                                                  sendPosition = false;

                                                  if (pd != null && pd.isShowing()) {
                                                      pd.dismiss();
                                                  }

                                                  location.getLatitude();
                                                  location.getLongitude();
                                                  String chatmsg = "My Position is : " + "\n" + "Latitude : " + String.valueOf(location.getLatitude()) + "\n" + "Longitude : " + String.valueOf(location.getLongitude());
                                                  type = "6";
                                                  String currentTime = null;
                                                  try {
                                                      currentTime = timeService.getDateTime();
                                                  }
                                                  catch (Exception e) {

                                                      currentTime = helperGetTime.getTime();
                                                  }

                                                  G.cmd.addchannelhistory(channeluid, null, chatmsg, currentTime, "1", null, "1", "0", "1", "1", "");
                                                  String lastMessageID = G.cmd.selectChannelLastMessageID(channeluid);
                                                  newChannelMessage("", lastMessageID, chatmsg, "1", currentTime, "1", "0", "", "", "", "", "1", null, lastMessageID);
                                                  new SendChannelMessage().execute("0", currentTime, chatmsg, lastMessageID, "0");
                                                  loadchathistory();
                                              }

                                              locManager.removeUpdates(locationListener);
                                          }
                                      };


    void showSettingsAlert() {

        di = new Dialog(this);
        di.requestWindowFeature(Window.FEATURE_NO_TITLE);
        di.setContentView(R.layout.dialog_exit_prompt);
        di.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        di.show();

        TextView tvMessage = (TextView) di.findViewById(R.id.textView_message_prompt);
        tvMessage.setTypeface(G.robotoBold);
        tvMessage.setText(getString(R.string.doyou_wantto_turnon_gps_en));

        Button tvYes = (Button) di.findViewById(R.id.btnView_yes);
        tvYes.setTypeface(G.robotoBold);
        tvYes.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                di.cancel();
                turnOnGps();
            }
        });

        Button tvNo = (Button) di.findViewById(R.id.btnView_no);
        tvNo.setTypeface(G.robotoBold);
        tvNo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (di != null && di.isShowing()) {
                    di.dismiss();
                }
            }
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(di.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        di.getWindow().setAttributes(lp);
        di.show();
    }


    void turnOnGps() {
        startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), G.request_code_position);
    }


    private void geturi(Intent data, boolean isImage) {

        Uri selectedImageUri = data.getData();
        String[] proj = null;
        Cursor cursor = getContentResolver().query(selectedImageUri, proj, null, null, null);
        String filename = "";
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            int columnIndexname = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
            filename = cursor.getString(columnIndexname);
            channelmsg = "";
            selectedImagePath = cursor.getString(column_index);
        }
        filePath = selectedImagePath;
        File imgFile = new File(filePath);
        int index = imgFile.getName().lastIndexOf('.') + 1;
        filemime = imgFile.getName().substring(index).toLowerCase();
        try {
            getsha(filePath, filename, isImage);
        }
        catch (Exception e) {

            e.printStackTrace();
        }
    }


    private void getsha(String datafile, String filename, boolean isImage) throws Exception {

        if (isImage) { // filePath is for Image so should compress image before upload
            datafile = HelperComoressImage.checkAndCompressImage(datafile);
            filePath = datafile;
        }

        MessageDigest md = MessageDigest.getInstance("SHA1");
        FileInputStream fis = new FileInputStream(datafile);
        byte[] dataBytes = new byte[1024];

        int nread = 0;

        while ((nread = fis.read(dataBytes)) != -1) {
            md.update(dataBytes, 0, nread);
        }

        byte[] mdbytes = md.digest();

        if (fis != null) {
            try {
                fis.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        //convert the byte to hex format
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < mdbytes.length; i++) {
            sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        filehash = sb.toString();

        openLayoutAddText(filename, null, new OnColorChangedListenerSelect() {

            @Override
            public void colorChanged(String msg, int state) {

                if (state == 1) {

                    try {
                        G.HANDLER.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                groupchatlist.scrollToPosition(mAdapter.getItemCount() - 1);
                            }
                        }, 1000);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    //                    String edttext = edtchat.getText().toString();
                    //
                    //                    if (edttext != null && !edttext.isEmpty() && !edttext.equals("null") && !edttext.equals("")) {
                    //                        channelmsg = edttext;
                    //                    } else {
                    //                        channelmsg = "";
                    //                    }
                    //                    edtchat.setText("");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        new chechfileexist().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, filehash, type, channelmsg, "5", "0", filemime);
                    } else {
                        new chechfileexist().execute(filehash, type, channelmsg, "5", "0", filemime);
                    }
                }

            }


            @Override
            public void Confirmation(Boolean result) {

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
        Button btnInviteMember = (Button) mView.findViewById(R.id.btn_channel_invite_member);
        Button btnClearHistory = (Button) mView.findViewById(R.id.btn_channel_clear_history);
        btnClearHistory.setText(getString(R.string.clear_chsh_en));
        Button btneditchannel = (Button) mView.findViewById(R.id.btn_edit_channel);
        Button btnchannelinfo = (Button) mView.findViewById(R.id.btn_channel_info);

        String edtiChannel = getString(R.string.edit_en);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {// allcaps is for api 14 and upper
            btneditchannel.setAllCaps(false);
        } else {
            edtiChannel = edtiChannel.substring(0, 1).toUpperCase() + edtiChannel.substring(1).toLowerCase();
        }
        btneditchannel.setText(edtiChannel);

        String channelInfo = getString(R.string.Channel_info_en);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {// allcaps is for api 14 and upper
            btnchannelinfo.setAllCaps(false);
        } else {
            channelInfo = channelInfo.substring(0, 1).toUpperCase() + channelInfo.substring(1, 8).toLowerCase() + channelInfo.substring(8, 9).toUpperCase() + channelInfo.substring(9).toLowerCase();
        }
        btnchannelinfo.setText(channelInfo);

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

        String clearChat = getString(R.string.clear_chsh_en);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {// allcaps is for api 14 and upper
            btnClearHistory.setAllCaps(false);
        } else {
            clearChat = clearChat.substring(0, 1).toUpperCase() + clearChat.substring(1, 6).toLowerCase() + clearChat.substring(6, 7).toUpperCase() + clearChat.substring(7).toLowerCase();
        }
        btnClearHistory.setText(clearChat);

        if (channelMembership.equals("0")) { // User is member
            btneditchannel.setVisibility(View.GONE);
            btnInviteAdmin.setVisibility(View.GONE);
            btnInviteMember.setVisibility(View.GONE);
        } else if (channelMembership.equals("2")) {
            btneditchannel.setVisibility(View.GONE);
            btnInviteAdmin.setVisibility(View.GONE);
        }

        popUp = new PopupWindow(Channel.this);
        popUp.setContentView(mView);
        popUp.setWindowLayoutMode(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        popUp.setHeight(1);
        popUp.setWidth(1);
        popUp.setTouchable(true);
        popUp.setFocusable(false);
        popUp.setOutsideTouchable(true);
        popUp.setBackgroundDrawable(new BitmapDrawable());
        popUp.showAtLocation(v, Gravity.NO_GRAVITY, location[0], location[1]);

        btnchannelinfo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (popUp != null && popUp.isShowing()) {
                    popUp.dismiss();
                }

                Intent intent = new Intent(Channel.this, ChannelProfile.class);
                intent.putExtra("channeluid", channeluid);
                intent.putExtra("channelName", channelName);
                intent.putExtra("channelDesc", channelDesc);
                intent.putExtra("channelAvatarLq", channelAvatarLq);
                intent.putExtra("channelAvatarHq", channelAvatarHq);
                intent.putExtra("channelMembership", channelMembership);
                intent.putExtra("channelMembersNumber", channelMembersNumber);
                intent.putExtra("channelActive", channelActive);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        btneditchannel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (popUp != null && popUp.isShowing()) {
                    popUp.dismiss();
                }

                Intent intent = new Intent(Channel.this, EditChannel.class);
                intent.putExtra("channeluid", channeluid);
                intent.putExtra("channelName", channelName);
                intent.putExtra("channelDesc", channelDesc);
                intent.putExtra("channelAvatarLq", channelAvatarLq);
                intent.putExtra("channelAvatarHq", channelAvatarHq);
                intent.putExtra("channelMembership", channelMembership);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        btnClearHistory.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                G.cmd.clearChannelHistory(channeluid);
                loadchathistory();

                if (popUp != null && popUp.isShowing()) {
                    popUp.dismiss();
                }

                sendChannelMessageToActivity();

            }
        });

        btnInviteAdmin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (popUp != null && popUp.isShowing()) {
                    popUp.dismiss();
                }
                Intent intent = new Intent(Channel.this, InviteAdmin.class);
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
                Intent intent = new Intent(Channel.this, InviteMemberToChannel.class);
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


    public static class Item {

        public String fileHash;
        public String fileType;
        public String fileUrl;
        public String fileThumb;
        public String filePath;
    }


    public void stopVoiceRecord() {
        if (null != mediaRecorder) {
            try {
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
            }
            catch (IllegalStateException e) {}
        }
    }


    private void startRecording() {
        final long currentTime = System.currentTimeMillis();
        outputFile = G.DIR_RECORD + "/" + currentTime + ".mp3";

        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(outputFile);
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        }
        catch (IllegalStateException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void startVoiceRecord() {
        canStop = false;
        startRecording();
        timertask = new TimerTask() {

            @Override
            public void run() {
                if (state) {

                    imgPicRecord.post(new Runnable() {

                        @Override
                        public void run() {
                            imgPicRecord.setImageResource(R.drawable.circle_white);
                            state = false;
                        }
                    });

                }
                else {
                    imgPicRecord.post(new Runnable() {

                        @Override
                        public void run() {
                            imgPicRecord.setImageResource(R.drawable.circle_red);
                            state = true;
                        }
                    });
                }
            }
        };

        if (timer == null) {
            timer = new Timer();
            timer.schedule(timertask, 100, 300);
        }

        if (secendTimer == null) {

            secendTimer = new Timer();
            secendTimer.schedule(new TimerTask() {

                @Override
                public void run() {

                    secend++;
                    if (secend >= 60) {
                        minute++;
                        secend %= 60;
                    }
                    if (minute >= 60) {
                        minute %= 60;
                    }
                    if (secend >= 1) {
                        canStop = true;
                    }

                    txtTimeRecord.post(new Runnable() {

                        @Override
                        public void run() {
                            String s = "";
                            if (minute < 10)
                                s += "0" + minute;
                            else
                                s += minute;
                            s += ":";
                            if (secend < 10)
                                s += "0" + secend;
                            else
                                s += secend;

                            txtTimeRecord.setText(s);
                        }
                    });
                }
            }, 1000, 1000);
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startMoving((int) event.getX());
                break;
            case MotionEvent.ACTION_MOVE:
                if (itemTag.equals("ivVoice"))
                    moveing((int) event.getX());
                break;
            case MotionEvent.ACTION_UP:
                if (itemTag.equals("ivVoice"))
                    reset();
                break;
        }

        return super.dispatchTouchEvent(event);
    }


    private void startMoving(int x) {
        leftPading = layout3.getPaddingRight();
        lastX = x;
        cansel = false;
    }


    private void moveing(int x) {
        int i = lastX - x;

        if (i > 0 || Allmoving > 0) {
            Allmoving += i;
            txt_slide_to_cancel.setAlpha(((float) (DistanceToCancel - Allmoving) / DistanceToCancel));
            layout3.setPadding(0, 0, layout3.getPaddingRight() + i, 0);
            lastX = x;

            if (Allmoving >= DistanceToCancel) {
                cansel = true;
                reset();
            }
        }
    }


    private void reset() {
        layout3.setPadding(0, 0, leftPading, 0);
        txt_slide_to_cancel.setAlpha(1);
        Allmoving = 0;
        itemTag = "";
        layout1.setVisibility(View.VISIBLE);
        layout2.setVisibility(View.GONE);

        if (timertask != null) {
            timertask.cancel();
            timertask = null;
        }
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        if (secendTimer != null) {
            secendTimer.cancel();
            secendTimer.purge();
            secendTimer = null;
        }

        secend = 0;
        minute = 0;
        txtTimeRecord.setText("00:00");

        if (canStop) {
            stopVoiceRecord();
        }

        if (cansel) {
            Toast.makeText(getBaseContext(), getString(R.string.cansel_en), Toast.LENGTH_SHORT).show();
        }
        else {
            if (canStop) {
                try {
                    String fileName = HelperString.getFileName(outputFile);
                    String filename = "record_" + fileName;
                    channelmsg = "";
                    filePath = outputFile;
                    type = "8";
                    getsha(filePath, filename, false);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }


    private static void sendchatMessageToActivity(Context mContext) {
        Intent intent = new Intent("loadChannelInfo");
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

        Intent intent1 = new Intent("loadall");
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent1);
    }


    @Override
    protected void onStart() {
        super.onStart();

        inActivity = true;

    }


    @Override
    protected void onResume() {
        Utils.checkLanguage(this);
        super.onResume();
        inActivity = true;
        G.appIsShowing = true;
    }


    @Override
    protected void onPause() {
        super.onPause();
        inActivity = false;
        G.appIsShowing = false;
    }


    @Override
    protected void onStop() {
        super.onStop();

        inActivity = false;

    }


    private void openLayoutAddText(String filename, Bitmap bit, final OnColorChangedListenerSelect l) {

        final LinearLayout layout = (LinearLayout) findViewById(R.id.layout_add_text_channel);
        layout.setVisibility(View.VISIBLE);

        TextView txtFileName = ((TextView) findViewById(R.id.txt_file_name_channel));
        txtFileName.setText(filename);

        ImageView iv = (ImageView) findViewById(R.id.imageView_file_pic_channel);
        iv.setImageBitmap(bit);

        llVoice.setVisibility(View.GONE);
        findViewById(R.id.btn_send).setVisibility(View.GONE);
        Button btnSendWithText = (Button) findViewById(R.id.btn_send_file_channel);
        btnSendWithText.setTypeface(G.fontAwesome);
        btnSendWithText.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String msgedt = edtchat.getText().toString();
                if (msgedt != null && !msgedt.isEmpty() && !msgedt.equals("null") && !msgedt.equals("")) {
                    channelmsg = edtchat.getText().toString();
                    edtchat.setText("");

                }
                closingkeybord();

                if (layoutreplay.getVisibility() == view.VISIBLE) {

                    layoutreplay.setVisibility(View.GONE);
                }

                l.colorChanged("ok", 1);

                layout.setVisibility(View.GONE);
                llVoice.setVisibility(View.VISIBLE);
                findViewById(R.id.btn_send).setVisibility(View.VISIBLE);
            }
        });

        findViewById(R.id.btn_cancel_send_channel).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                layout.setVisibility(View.GONE);
                closingkeybord();
                l.colorChanged("cancel", 0);

                llVoice.setVisibility(View.VISIBLE);
                findViewById(R.id.btn_send).setVisibility(View.VISIBLE);
            }
        });

    }


    private void channelNewPost(String uid, String lastmsg, String lastdate) {
        Intent intent = new Intent("newPostChannelList");
        intent.putExtra("uid", uid);
        intent.putExtra("lastmsg", lastmsg);
        intent.putExtra("lastdate", lastdate);
        LocalBroadcastManager.getInstance(Channel.this).sendBroadcast(intent);

        //===Send New Post To PageAll
        Intent intentAll = new Intent("newPostAll");
        intentAll.putExtra("MODEL", "3"); // 3 = channel
        intentAll.putExtra("UID", uid);
        intentAll.putExtra("LAST_MSG", lastmsg);
        intentAll.putExtra("LAST_DATE", lastdate);
        LocalBroadcastManager.getInstance(Channel.this).sendBroadcast(intentAll);
    }


    private void updatesoundwithuid(String uid, String value) {
        Intent intent = new Intent("updateSound");
        intent.putExtra("uid", uid);
        intent.putExtra("value", value);
        LocalBroadcastManager.getInstance(Channel.this).sendBroadcast(intent);

        //===Send Sound State To PageAll
        Intent intentAll = new Intent("updateSoundAll");
        intentAll.putExtra("UID", channeluid);
        intentAll.putExtra("VALUE", value);
        intentAll.putExtra("MODEL", "3"); // 3 = channel
        LocalBroadcastManager.getInstance(Channel.this).sendBroadcast(intentAll);

    }


    private void deletewithuid(String uid) {
        Intent intent = new Intent("deleteWithUid");
        intent.putExtra("uid", uid);
        LocalBroadcastManager.getInstance(Channel.this).sendBroadcast(intent);
    }


    private void updateseen(String uid) {

        Intent intent = new Intent("updateSeen");
        intent.putExtra("uid", uid);
        LocalBroadcastManager.getInstance(Channel.this).sendBroadcast(intent);

        Intent intentAll = new Intent("updateSeenAll");
        intentAll.putExtra("MODEL", "3");
        intentAll.putExtra("UID", uid);
        LocalBroadcastManager.getInstance(Channel.this).sendBroadcast(intentAll);

    }


    private void newChannelMessage(String mime, String databaseID, String msg, String status, String msgtime, String msgtype, String view, String filehash, String replyfilehash, String replymessage, String replyfrom, String msgsender, String msgid, String lastMessageID) {
        mAdapter.newPost(mime, databaseID, msg, status, msgtime, msgtype, view, filehash, replyfilehash, replymessage, replyfrom, msgsender, msgid, lastMessageID);
    }


    private void updateMessageStatus(String msgID, String serverTime) {
        mAdapter.updateMessageStatus(msgID, serverTime);
    }
}
