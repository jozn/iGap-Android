// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
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
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
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
import com.iGap.adapter.CustomPagerAdapter;
import com.iGap.adapter.DrawableManager;
import com.iGap.adapter.DrawableManagerDialog;
import com.iGap.adapter.G;
import com.iGap.adapter.GroupAdapter;
import com.iGap.adapter.GroupChatRecycleAdapter;
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
import com.iGap.instruments.ConfirmationDialog;
import com.iGap.instruments.ImageLoader1;
import com.iGap.instruments.JSONParser;
import com.iGap.instruments.LocalBinder;
import com.iGap.instruments.SlidingTabLayoutemoji;
import com.iGap.instruments.SoftKeyboard;
import com.iGap.instruments.Utils;
import com.iGap.instruments.explorer;
import com.iGap.instruments.myPaint;
import com.iGap.interfaces.OnColorChangedListenerSelect;
import com.iGap.interfaces.OnComplet;
import com.iGap.interfaces.OnDeleteComplete;
import com.iGap.services.MyService;
import com.iGap.services.TimerServies;


/**
 * 
 * namayesh list grouh he va ghabeliyet entekhab har groh va shorue chat dar grouh
 *
 */

public class GroupChat extends Activity {

    //************************* ArrayLists
    private ArrayList<String>          filemimearray           = new ArrayList<String>();
    private ArrayList<String>          idarray                 = new ArrayList<String>();
    private ArrayList<String>          msgarray                = new ArrayList<String>();
    private ArrayList<String>          statusarray             = new ArrayList<String>();
    private ArrayList<String>          msgtimearray            = new ArrayList<String>();
    private ArrayList<String>          msgtypearray            = new ArrayList<String>();
    private ArrayList<String>          msgidarray              = new ArrayList<String>();
    private ArrayList<String>          msgsenderarray          = new ArrayList<String>();
    private ArrayList<String>          msgsenderavatararray    = new ArrayList<String>();
    private ArrayList<String>          usernames               = new ArrayList<String>();
    private ArrayList<String>          filehasharray           = new ArrayList<String>();
    private ArrayList<String>          typearray               = new ArrayList<String>();
    private ArrayList<String>          replyfilehasharray      = new ArrayList<String>();
    private ArrayList<String>          replymessagearray       = new ArrayList<String>();
    private ArrayList<String>          replyfromarray          = new ArrayList<String>();

    private ArrayList<Boolean>         selectedItem            = new ArrayList<Boolean>();
    private ArrayList<Boolean>         runUploaderThread       = new ArrayList<Boolean>();
    private ArrayList<Boolean>         runDownloaderThread     = new ArrayList<Boolean>();
    private ArrayList<Boolean>         runDownloaderListener   = new ArrayList<Boolean>();
    private ArrayList<Boolean>         runUploaderListener     = new ArrayList<Boolean>();

    private ArrayList<Integer>         hashPositions           = new ArrayList<Integer>();

    //************************* private values

    private int                        clickHashPos;
    private int                        newPosBottom;
    private int                        itemCount;
    private int                        selectCounter;
    private int                        statusCode;
    private int                        lastX;
    private int                        leftPading;
    private int                        messagecash;
    private int                        newMsgPosition          = -1;
    private int                        lastHighlightPosition   = -1;
    private int                        offset                  = 0;
    private int                        countOffset             = 0;
    private int                        beforOffset             = 0;
    private int                        newPos                  = 0;
    private int                        secend                  = 0;
    private int                        minute                  = 0;
    private int                        Allmoving               = 0;
    private int                        DistanceToCancel        = 130;

    private String                     hashtakText;
    private String                     hashtakBeforeScrollBottom;
    private String                     imageFileName, imageFileNameWithoutCrop, filePathWithoutCrop;
    private String                     crop;
    private String                     gchactive;
    private String                     sendbyenter;
    private String                     ffrom;
    private String                     copytext;
    private String                     avatar;
    private String                     gchmembership;
    private String                     username;
    private String                     outputFile;
    private String                     newfilePath;
    private String                     gchdescription;
    private String                     gchid;
    private String                     gchavatarHq;
    private String                     gchavatar;
    private String                     selectedImagePath;
    private String                     gchatmsg;
    private String                     gchname;
    private String                     Upname;
    private String                     type;
    private String                     filehash;
    private String                     basicAuth;
    private String                     fileurl;
    private String                     filethumb;
    private String                     filemime                = "";
    private String                     replymessage            = "";
    private String                     replyfilehash           = "";
    private String                     replyfrom               = "";
    private String                     itemTag                 = "";
    private String                     month                   = "";
    private String                     day                     = "";
    private String                     year                    = "";
    private String                     filePath                = "";
    private String                     lastHashtakID           = "";

    private boolean                    mBounded;
    private boolean                    hashtakSearch           = false;
    private boolean                    loadBottomAllow         = false;
    private boolean                    needCrop                = false;
    private boolean                    showDefaultImage        = false;
    private boolean                    showDefaultImageCapture = false;
    private boolean                    canStop                 = false;
    private boolean                    selectItemVisible       = false;
    private boolean                    state                   = false;
    private boolean                    cansel                  = false;
    private boolean                    inActivity              = false;
    private boolean                    firstLoad               = true;
    private boolean                    loadBottom              = true;

    //************************* private variables

    private Button                     btnmicicon;
    private Button                     btnDrawerForward;
    private Button                     btnDrawerFiles;
    private Button                     btnDrawerReplay;
    private Button                     btnDrawerTrash;
    private Button                     voiceIcon;
    private Button                     btnCancelSend;
    private Button                     btnSendFile;
    private Button                     btnUp;
    private Button                     btnDown;
    private Button                     btnCancel;

    private TextView                   txtedturls;
    private TextView                   txtuserarrowright;
    private TextView                   txtforwardfrom;
    private TextView                   usernaveclose;
    private TextView                   txtforwardmsg;
    private TextView                   txtDrawerCounter;
    private TextView                   attachIcon;
    private TextView                   smaileIcon;
    private TextView                   txt_slide_to_cancel;
    private TextView                   txtTimeRecord;
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

    private LinearLayout               layoutreplay;
    private LinearLayout               layout1;
    private LinearLayout               layout2;
    private LinearLayout               layout3;
    private LinearLayout               mLayout_smile;
    private LinearLayout               sideDrawer;
    private LinearLayout               lytRoot;
    private LinearLayout               llVoice;
    private LinearLayout               layout_music;
    private LinearLayout               lytHash;
    private LinearLayout               lytMainHeader;

    private boolean                    isOpened                = false;
    private boolean                    smileOpen               = false;
    private boolean                    smileClicked            = false;
    private boolean                    sendMessage             = false;
    private boolean                    edtTouch                = false;
    private boolean                    btnSendClicked          = false;
    private boolean                    load                    = false;
    private boolean                    scrollToOffset          = false;

    private EditText                   edtchat;

    private Dialog                     di;
    private Dialog                     dialogSelectMedia;
    private ImageView                  imgPicRecord;
    private ImageView                  useravatar;
    private RecyclerView               groupchatlist;
    private RecyclerView.LayoutManager mLayoutManager;
    private DrawableManager            dm;
    private DrawableManagerDialog      dmDialog;
    private ImageLoader1               imgLoader1;
    private ImageLoader1               il;
    private ProgressDialog             pDialogg;
    private SpannableStringBuilder     buildertext;
    private SlidingTabLayoutemoji      mSlidingTabLayout;
    private ViewPager                  mViewPager;
    private LocationManager            locManager;
    private Uri                        fileUri;
    private InputMethodManager         imm                     = null;
    private static File                mediaFile;
    private PopupWindow                popUp;
    private GroupAdapter               GA;
    private TimerServies               currentTime;
    private MediaRecorder              mediaRecorder;
    private TimerTask                  timertask;
    private Timer                      timer, secendTimer;
    private SoftKeyboard               softKeyboard;
    private GroupChatRecycleAdapter    mAdapter;
    private View                       view;
    private MyService                  mService;
    private HelperGetTime              helperGetTime;
    public static MyService            staticGroupService;
    private static final int           progress_bar_type       = 0, MEDIA_TYPE_IMAGE = 1, REQUEST_CAMERA = 0;
    private JSONParser                 jParser                 = new JSONParser();

    private final ServiceConnection    mConnection             = new ServiceConnection() {

                                                                   @SuppressWarnings("unchecked")
                                                                   @Override
                                                                   public void onServiceConnected(final ComponentName name, final IBinder service) {
                                                                       mService = ((LocalBinder<MyService>) service).getService();
                                                                       mBounded = true;
                                                                       staticGroupService = mService;
                                                                   }


                                                                   @Override
                                                                   public void onServiceDisconnected(final ComponentName name) {
                                                                       mService = null;
                                                                       mBounded = false;
                                                                   }
                                                               };


    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.checkLanguage(this);
        setContentView(R.layout.activity_group_chat);
        helperGetTime = new HelperGetTime();
        inActivity = true;
        view = this.findViewById(android.R.id.content);
        G.hashSearchType = 2;

        hashtakSearch = false;
        G.hashtakSearch = false;

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
                    int toppestHashtakOffset = G.cmd.selectOffsetOfIDFromTop("groupchathistory", "groupchatroom_id = '" + gchid + "'", toppestHashtakID);

                    int idOffset = G.cmd.selectOffsetOfID("groupchathistory", "groupchatroom_id = '" + gchid + "'", idarray.get(0));

                    String firstNewHashtakID = G.cmd.selectHashtakPosition1("groupchathistory", "groupchatroom_id = '" + gchid + "'", (idOffset + 1) + "", gchid, hashtakText);
                    int offset = G.cmd.selectOffsetOfIDFromTop("groupchathistory", "groupchatroom_id = '" + gchid + "'", firstNewHashtakID);

                    if (offset == -1) {
                        Toast.makeText(GroupChat.this, "not exist more!", Toast.LENGTH_SHORT).show();
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
                    int idOffset = G.cmd.selectOffsetOfIDFromTop("groupchathistory", "groupchatroom_id = '" + gchid + "'", idarray.get(idarray.size() - 1));
                    String firstNewHashtakID = G.cmd.selectHashtakPositionToBottom("groupchathistory", "groupchatroom_id = '" + gchid + "'", (idOffset + 1) + "", gchid, hashtakText);

                    if (firstNewHashtakID.equals("-1")) {
                        Toast.makeText(GroupChat.this, "not exist more!", Toast.LENGTH_SHORT).show();
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
                mAdapter.changeHeader();
            }
        });

        dm = new DrawableManager(GroupChat.this);
        dmDialog = new DrawableManagerDialog(GroupChat.this);
        currentTime = new TimerServies();

        crop = G.cmd.getsetting(14);

        locManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        doBindService();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            gchid = extras.getString("gchid");
            G.mainGroupID = gchid;
            gchname = extras.getString("gchname");
            gchavatar = extras.getString("gchavatar");
            gchavatarHq = extras.getString("gchavatarHq");
            gchdescription = extras.getString("gchdescription");
            gchmembership = extras.getString("gchmembership");
            gchactive = extras.getString("gchactive");
        }

        basicAuth = G.cmd.namayesh4(3, "info");
        username = G.cmd.namayesh4(1, "info");
        avatar = G.cmd.namayesh4(4, "info");
        sendbyenter = G.cmd.getsetting(6);
        txtedturls = new TextView(GroupChat.this);
        GA = new GroupAdapter(GroupChat.this);
        if (avatar != null && !avatar.isEmpty() && !avatar.equals("null") && !avatar.equals("")) {} else {
            avatar = "empty";
        }
        imgLoader1 = new ImageLoader1(GroupChat.this, basicAuth);
        TimerServies ts = new TimerServies();
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
        il = new ImageLoader1(GroupChat.this, basicAuth);

        btnUp.setTypeface(G.fontAwesome);
        btnDown.setTypeface(G.fontAwesome);
        btnCancel.setTypeface(G.fontAwesome);

        layout_music = (LinearLayout) findViewById(R.id.ll_music_chat);
        lytRoot = (LinearLayout) findViewById(R.id.lyt_root);
        attachIcon = (TextView) findViewById(R.id.attach_icon1);
        smaileIcon = (TextView) findViewById(R.id.smaile_icon);
        txtDrawerCounter = (TextView) findViewById(R.id.txt_drawer_counter);
        txt_slide_to_cancel = (TextView) findViewById(R.id.txt_slideto_cancel);
        txtTimeRecord = (TextView) findViewById(R.id.txt_time_record);
        Button backIcon = (Button) findViewById(R.id.back_icon);
        Button navIcon = (Button) findViewById(R.id.nav_icon);
        TextView usernametx = (TextView) findViewById(R.id.user_name_tx);

        llVoice = (LinearLayout) findViewById(R.id.ll_voice);
        voiceIcon = (Button) findViewById(R.id.voice_icon);
        btnDrawerTrash = (Button) findViewById(R.id.btn_drawer_trash);
        btnDrawerForward = (Button) findViewById(R.id.btn_drawer_forward);
        btnDrawerFiles = (Button) findViewById(R.id.btn_drawer_files);
        btnDrawerReplay = (Button) findViewById(R.id.btn_drawer_replay);
        btnmicicon = (Button) findViewById(R.id.btn_mic_icon);

        final Button btnsend = (Button) findViewById(R.id.btn_send);
        Button btnDeleteGroup = (Button) findViewById(R.id.btn_delete_group);

        btnCancelSend = (Button) findViewById(R.id.btn_cancel_send_group);
        btnCancelSend.setTypeface(G.fontAwesome);

        btnSendFile = (Button) findViewById(R.id.btn_send_file_group);
        btnSendFile.setTypeface(G.fontAwesome);

        groupchatlist = (RecyclerView) findViewById(R.id.groupchatlist);
        groupchatlist.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(GroupChat.this);
        groupchatlist.setLayoutManager(mLayoutManager);
        imgPicRecord = (ImageView) findViewById(R.id.img_pic_record);
        useravatar = (ImageView) findViewById(R.id.user_avatar);

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

        groupchatlist.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                loadBottomAllow = true;
                return false;
            }
        });

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

        edtchat = (EditText) findViewById(R.id.edt_chat);

        String mess = G.cmd.getLastMessage(gchid, "2");
        if (mess.length() > 0) {
            PageMessagingPopularFunction pm = new PageMessagingPopularFunction();
            edtchat.setText(pm.parseText(mess, "CHAT_LIST", false, GroupChat.this, 0));
        }

        LinearLayout llgroupinfo = (LinearLayout) findViewById(R.id.ll_groupinfo);
        sideDrawer = (LinearLayout) findViewById(R.id.Side_drawer);
        mLayout_smile = (LinearLayout) findViewById(R.id.layout_smile);
        layout1 = (LinearLayout) findViewById(R.id.layout1);
        layout2 = (LinearLayout) findViewById(R.id.layout2);
        layout3 = (LinearLayout) findViewById(R.id.layout3);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mSlidingTabLayout = (SlidingTabLayoutemoji) findViewById(R.id.sliding_tabs);

        btnDrawerTrash.setTypeface(G.fontAwesome);
        btnDrawerForward.setTypeface(G.fontAwesome);
        btnDrawerFiles.setTypeface(G.fontAwesome);
        btnDrawerReplay.setTypeface(G.fontAwesome);
        smaileIcon.setTypeface(G.fontAwesome);
        attachIcon.setTypeface(G.fontAwesome);
        voiceIcon.setTypeface(G.fontAwesome);
        navIcon.setTypeface(G.fontAwesome);
        usernametx.setText(gchname);
        usernametx.setTypeface(G.robotoBold);
        btnsend.setTypeface(G.fontAwesome);
        backIcon.setTypeface(G.fontAwesome);
        btnmicicon.setTypeface(G.fontAwesome);

        mViewPager.setAdapter(new CustomPagerAdapter(this, edtchat));
        mSlidingTabLayout.setViewPager(mViewPager);

        LayoutParams params = mLayout_smile.getLayoutParams();
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        params.height = (display.getHeight() * 3) / 10;
        mLayout_smile.setLayoutParams(params);

        layoutreplay = (LinearLayout) findViewById(R.id.layout_replay);
        txtuserarrowright = (TextView) findViewById(R.id.user_arrow_right);
        usernaveclose = (TextView) findViewById(R.id.user_nave_close);
        txtforwardfrom = (TextView) findViewById(R.id.txt_forward_from);
        txtforwardmsg = (TextView) findViewById(R.id.txt_forwardmsg);
        txtuserarrowright.setTypeface(G.fontAwesome);
        usernaveclose.setTypeface(G.fontAwesome);

        LinearLayout lytDeleteGroup = (LinearLayout) findViewById(R.id.lyt_delete_group);

        btnDeleteGroup.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                G.cmd.deletegroupchatrooms(gchid);
                Intent intentGroup = new Intent("loadMessageingGroup");
                Intent intentAll = new Intent("loadall");
                LocalBroadcastManager.getInstance(GroupChat.this).sendBroadcast(intentGroup);
                LocalBroadcastManager.getInstance(GroupChat.this).sendBroadcast(intentAll);
                finish();
            }
        });

        if (gchactive.equals("2")) {
            layout1.setVisibility(View.GONE);
            lytDeleteGroup.setVisibility(View.VISIBLE);
        }

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
                    if (ffrom != null && !ffrom.isEmpty() && !ffrom.equals("null") && !ffrom.equals("")) {
                        ffrom = ffrom + "," + name;
                    } else {
                        ffrom = name;
                    }
                }

                txtforwardfrom.setText(ffrom);
                txtforwardmsg.setText(messagecash + getString(R.string.forwarded_messages_en));
            }

        }

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

        backIcon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                G.mainGroupID = "";
                G.visibleGroupID = "";
                View view = GroupChat.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                finish();
            }
        });

        useravatar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupChat.this, GroupProfile.class);
                intent.putExtra("gchid", gchid);
                intent.putExtra("gchname", gchname);
                intent.putExtra("gchavatar", gchavatar);
                intent.putExtra("gchavatarHq", gchavatarHq);
                intent.putExtra("gchdescription", gchdescription);
                intent.putExtra("gchmembership", gchmembership);
                intent.putExtra("gchactive", gchactive);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
        llgroupinfo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupChat.this, GroupProfile.class);
                intent.putExtra("gchid", gchid);
                intent.putExtra("gchname", gchname);
                intent.putExtra("gchavatar", gchavatar);
                intent.putExtra("gchavatarHq", gchavatarHq);
                intent.putExtra("gchdescription", gchdescription);
                intent.putExtra("gchmembership", gchmembership);
                intent.putExtra("gchactive", gchactive);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        attachIcon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                initDialog();

            }
        });

        btnsend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (G.hashtakSearch) {
                    mAdapter.resethighlight();
                    mAdapter.changeHeader();
                }

                type = "1";
                if (messagecash == 0) {

                    gchatmsg = edtchat.getText().toString();
                    if (gchatmsg != null && !gchatmsg.isEmpty() && !gchatmsg.equals("null") && !gchatmsg.equals("")) {

                        if (smileOpen) {
                            closeLayoutSmile();
                            smileOpen = false;
                        }

                        edtchat.getText().clear();

                        mService.sendgroupmessage(filemime, gchid, gchatmsg, avatar, type, null, null, null, replymessage, replyfilehash, replyfrom, "0", null);
                        layoutreplay.setVisibility(View.GONE);
                        replymessage = "";
                        replyfilehash = "";
                        replyfrom = "";

                    } else {
                        Toast.makeText(GroupChat.this, getString(R.string.your_message_is_empty_en), Toast.LENGTH_SHORT).show();
                    }

                } else {

                    gchatmsg = edtchat.getText().toString();
                    if (gchatmsg != null && !gchatmsg.isEmpty() && !gchatmsg.equals("null") && !gchatmsg.equals("")) {

                        if (smileOpen) {
                            closeLayoutSmile();
                            smileOpen = false;
                        }

                        if (closingkeybord() == true) {
                            mService.sendgroupmessage(filemime, gchid, gchatmsg, avatar, type, null, null, null, replymessage, replyfilehash, replyfrom, "0", null);
                            layoutreplay.setVisibility(View.GONE);
                            replymessage = "";
                            replyfilehash = "";
                            replyfrom = "";
                        }

                    }

                    Cursor cursor = G.cmd.selectMessagingCache();
                    while (cursor.moveToNext()) {
                        //String forwardfrom = cursor.getString(cursor.getColumnIndex("forwardfrom"));
                        String msg = cursor.getString(cursor.getColumnIndex("msg"));
                        String filehash = cursor.getString(cursor.getColumnIndex("filehash"));
                        String fileurl = cursor.getString(cursor.getColumnIndex("fileurl"));
                        String filethumbnail = cursor.getString(cursor.getColumnIndex("filethumbnail"));
                        String type = cursor.getString(cursor.getColumnIndex("type"));
                        String filemime = cursor.getString(cursor.getColumnIndex("filemime"));

                        if (filehash != null && !filehash.isEmpty() && !filehash.equals("null") && !filehash.equals("")) {
                            String filepathh = "", status;
                            status = G.cmd.getfile(4, filehash);

                            try {
                                filepathh = G.cmd.getfile(6, filehash);

                                if (filepathh != null && !filepathh.isEmpty() && !filepathh.equals("null") && !filepathh.equals("")) {

                                } else {
                                    filepathh = "";
                                }

                            }
                            catch (Exception e) {
                                filepathh = "";
                            }

                            G.cmd.Addfiles1(filehash, fileurl, filethumbnail, status, filepathh);

                            mService.sendgroupmessage(filemime, gchid, msg, avatar, type, filehash, fileurl, filethumbnail, replymessage, replyfilehash, replyfrom, "0", null);
                        } else {
                            mService.sendgroupmessage(filemime, gchid, msg, avatar, type, filehash, fileurl, filethumbnail, replymessage, replyfilehash, replyfrom, "0", null);

                        }
                    }
                    cursor.close();

                    layoutreplay.setVisibility(View.GONE);
                    G.cmd.clearmessagecash();
                }

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
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }


            @Override
            public void afterTextChanged(Editable s) {

                // namayesh  etellate site ke adress an dar edittext vared shodeh ast

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
                                                    //String icon = jo.getString("icon");

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
                        //String icon = G.cmd.getwebsiteinfo(url, 4);

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
        navIcon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                popupoptions(v);
            }
        });

        edtchat.setOnKeyListener(new OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button

                return false;
            }
        });

        btnDrawerTrash.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                mAdapter.deletetrash();

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

                        G.cmd.AddMessagecash(msgsenderarray.get(i), msgarray.get(i), filehasharray.get(i), fileurl, filethumbnail, typearray.get(i), filemimearray.get(i));

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

        btnDrawerReplay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                layoutreplay.setVisibility(View.VISIBLE);
                for (int i = 0; i < selectedItem.size(); i++) {

                    if (selectedItem.get(i) == true) {

                        replymessage = msgarray.get(i);
                        replyfilehash = filehasharray.get(i);
                        replyfrom = msgsenderarray.get(i);
                        buildertext = parseText(replymessage, false);
                        txtforwardfrom.setText(replyfrom);
                        txtforwardmsg.setText(buildertext, BufferType.SPANNABLE);

                        softKeyboard.openSoftKeyboard();
                        edtchat.requestFocus();

                    }
                }
                UnVisibleDrawer();
                selectedItem.clear();
                if (selectedItem.size() != msgarray.size()) {
                    int ekhtelaf = msgarray.size() - selectedItem.size();
                    for (int i = 0; i < ekhtelaf; i++) {
                        selectedItem.add(false);
                    }
                }
                loadchathistory();
            }
        });
        loadchathistory();
        setuserimage();

        LocalBroadcastManager.getInstance(GroupChat.this).registerReceiver(SetAvatarChangeReceiver, new IntentFilter("SetAvatarChange"));
        LocalBroadcastManager.getInstance(GroupChat.this).registerReceiver(NewPostGroupChat, new IntentFilter("NewPostGroupChat"));
        LocalBroadcastManager.getInstance(GroupChat.this).registerReceiver(newPostGroupChatFromSender, new IntentFilter("newPostGroupChatFromSender"));
        LocalBroadcastManager.getInstance(GroupChat.this).registerReceiver(UpdateIdfileGroupChat, new IntentFilter("UpdateIdfileGroupChat"));
        LocalBroadcastManager.getInstance(GroupChat.this).registerReceiver(UpdateStatusfileGroupChat, new IntentFilter("UpdateStatusfileGroupChat"));
        LocalBroadcastManager.getInstance(GroupChat.this).registerReceiver(UpdateMessageIDGroupChat, new IntentFilter("UpdateMessageIDGroupChat"));
        LocalBroadcastManager.getInstance(GroupChat.this).registerReceiver(UpdateFileMessageIDGroupChat, new IntentFilter("UpdateFileMessageIDGroupChat"));
        LocalBroadcastManager.getInstance(GroupChat.this).registerReceiver(updateViewAfterResendGroupChat, new IntentFilter("updateViewAfterResendGroupChat"));
        LocalBroadcastManager.getInstance(GroupChat.this).registerReceiver(LoadGroupChat, new IntentFilter("LoadGroupChat"));
        LocalBroadcastManager.getInstance(GroupChat.this).registerReceiver(hashSearchGroupChat, new IntentFilter("hashSearchGroupChat"));
        LocalBroadcastManager.getInstance(GroupChat.this).registerReceiver(changeHeaderGroupChat, new IntentFilter("changeHeaderGroupChat"));
        LocalBroadcastManager.getInstance(GroupChat.this).registerReceiver(LoadBottomGroupChat, new IntentFilter("LoadBottomGroupChat"));

        Handler h = new Handler();

        h.postDelayed(new Runnable() {

            @Override
            public void run() {
                G.visibleGroupID = gchid;
            }
        }, 1000);

        if (HelperGetDataFromOtherApp.hasSharedData) {
            HelperGetDataFromOtherApp.hasSharedData = false;

            G.HANDLER.postDelayed(new Runnable() { // set delay to prepare createchattest

                @Override
                public void run() {

                    if (HelperGetDataFromOtherApp.messageType == FileType.message) {

                        type = "1";
                        gchatmsg = HelperGetDataFromOtherApp.message;

                        mService.sendgroupmessage(filemime, gchid, gchatmsg, avatar, type, null, null, null, replymessage, replyfilehash, replyfrom, "0", null);

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


    private boolean closingkeybord() {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                edtchat.getText().clear();
                edtchat.clearFocus();
                View view = GroupChat.this.getCurrentFocus();
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


    private void setuserimage() {
        if (gchavatar != null && !gchavatar.isEmpty() && !gchavatar.equals("null") && !gchavatar.equals("") && !gchavatar.equals("empty")) {
            int loader = R.drawable.difaultimage;
            imgLoader1.DisplayImage(gchavatar, loader, useravatar);

        } else {
            HelperDrawAlphabet pf = new HelperDrawAlphabet();
            Bitmap bm = pf.drawAlphabet(GroupChat.this, gchname, useravatar);
            useravatar.setImageBitmap(bm);

        }
    }


    /**
     * 
     * send message by Enter event
     * 
     */
    private void sendByEnter() {
        if (sendbyenter.equals("1")) {

            if (G.hashtakSearch) {
                mAdapter.resethighlight();
                mAdapter.changeHeader();
            }

            if (((LinearLayout) findViewById(R.id.layout_add_text_group)).getVisibility() == View.VISIBLE) {
                ((Button) findViewById(R.id.btn_send_file_group)).performClick();
                return;
            }

            type = "1";
            if (messagecash == 0) {

                gchatmsg = edtchat.getText().toString();
                if (gchatmsg != null && !gchatmsg.isEmpty() && !gchatmsg.equals("null") && !gchatmsg.equals("")) {

                    if (smileOpen) {
                        closeLayoutSmile();
                        smileOpen = false;
                    }

                    edtchat.getText().clear();

                    mService.sendgroupmessage(filemime, gchid, gchatmsg, avatar, type, null, null, null, replymessage, replyfilehash, replyfrom, "0", null);
                    layoutreplay.setVisibility(View.GONE);
                    replymessage = "";
                    replyfilehash = "";
                    replyfrom = "";

                } else {
                    Toast.makeText(GroupChat.this, getString(R.string.your_message_is_empty_en), Toast.LENGTH_SHORT).show();
                }

            } else {

                gchatmsg = edtchat.getText().toString();
                if (gchatmsg != null && !gchatmsg.isEmpty() && !gchatmsg.equals("null") && !gchatmsg.equals("")) {

                    if (smileOpen) {
                        closeLayoutSmile();
                        smileOpen = false;
                    }

                    if (closingkeybord() == true) {
                        mService.sendgroupmessage(filemime, gchid, gchatmsg, avatar, type, null, null, null, replymessage, replyfilehash, replyfrom, "0", null);
                        layoutreplay.setVisibility(View.GONE);
                        replymessage = "";
                        replyfilehash = "";
                        replyfrom = "";
                    }

                }

                Cursor cursor = G.cmd.selectMessagingCache();
                while (cursor.moveToNext()) {
                    //String forwardfrom = cursor.getString(cursor.getColumnIndex("forwardfrom"));
                    String msg = cursor.getString(cursor.getColumnIndex("msg"));
                    String filehash = cursor.getString(cursor.getColumnIndex("filehash"));
                    String fileurl = cursor.getString(cursor.getColumnIndex("fileurl"));
                    String filethumbnail = cursor.getString(cursor.getColumnIndex("filethumbnail"));
                    String type = cursor.getString(cursor.getColumnIndex("type"));

                    if (filehash != null && !filehash.isEmpty() && !filehash.equals("null") && !filehash.equals("")) {
                        String filepathh = "", status;
                        status = G.cmd.getfile(4, filehash);

                        try {
                            filepathh = G.cmd.getfile(6, filehash);

                            if (filepathh != null && !filepathh.isEmpty() && !filepathh.equals("null") && !filepathh.equals("")) {

                            } else {
                                filepathh = "";
                            }

                        }
                        catch (Exception e) {
                            filepathh = "";
                        }

                        G.cmd.Addfiles1(filehash, fileurl, filethumbnail, status, filepathh);

                        mService.sendgroupmessage(filemime, gchid, msg, avatar, type, filehash, fileurl, filethumbnail, replymessage, replyfilehash, replyfrom, "0", null);
                    } else {
                        mService.sendgroupmessage(filemime, gchid, msg, avatar, type, filehash, fileurl, filethumbnail, replymessage, replyfilehash, replyfrom, "0", null);

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


    private void UnVisibleDrawer() {
        G.longPressItem = false;
        selectItemVisible = false;
        selectCounter = 0;
        sideDrawer.setVisibility(View.GONE);
        mAdapter.invisibleDrawer();
    }

    private BroadcastReceiver SetAvatarChangeReceiver        = new BroadcastReceiver() {

                                                                 @Override
                                                                 public void onReceive(Context context, Intent intent) {

                                                                     String newAvatar = intent.getStringExtra("newAvatar");

                                                                     if (newAvatar != null && !newAvatar.isEmpty() && !newAvatar.equals("null") && !newAvatar.equals("")) {
                                                                         il.DisplayImage(newAvatar, R.drawable.difaultimage, useravatar);
                                                                     } else {
                                                                         String name = gchname;
                                                                         name = name.trim();

                                                                         String[] splited = name.split("\\s+");

                                                                         int size = splited.length;
                                                                         String charfirst, mname;

                                                                         if (size == 1) {
                                                                             charfirst = splited[0].trim();
                                                                             mname = charfirst.substring(0, 1);
                                                                         } else {
                                                                             charfirst = splited[0].trim();
                                                                             String charlast = splited[size - 1].trim();
                                                                             mname = charfirst.substring(0, 1) + charlast.substring(0, 1);
                                                                         }
                                                                         Upname = mname.toUpperCase();
                                                                         Bitmap bm = G.utileProg.drawAlphabetOnPicture(useravatar.getLayoutParams().width, Upname, "#ffffff");
                                                                         useravatar.setImageBitmap(bm);
                                                                     }
                                                                 }
                                                             };
    private BroadcastReceiver NewPostGroupChat               = new BroadcastReceiver() {

                                                                 @Override
                                                                 public void onReceive(Context context, Intent intent) {

                                                                     String id = intent.getStringExtra("id");
                                                                     String msg = intent.getStringExtra("msg");
                                                                     String status = intent.getStringExtra("status");
                                                                     String msgtime = intent.getStringExtra("msgtime");
                                                                     String msgtype = intent.getStringExtra("msgtype");
                                                                     String username = intent.getStringExtra("username");
                                                                     String filehash = intent.getStringExtra("filehash");
                                                                     String replyfilehash = intent.getStringExtra("replyfilehash");
                                                                     String replymessage = intent.getStringExtra("replymessage");
                                                                     String replyfrom = intent.getStringExtra("replyfrom");
                                                                     String type = intent.getStringExtra("type");
                                                                     String msgsender = intent.getStringExtra("msgsender");
                                                                     String msgsenderavatar = intent.getStringExtra("msgsenderavatar");
                                                                     String msgid = intent.getStringExtra("msgid");
                                                                     String filemime = intent.getStringExtra("filemime");
                                                                     G.cmd.updategroupmsgstatus(msgid, "5");
                                                                     updateseen(gchid);

                                                                     mAdapter.newPost(filemime, id, msg, status, msgtime, msgtype, username, filehash, replyfilehash, replymessage, replyfrom, type, msgsender, msgsenderavatar, msgid);
                                                                     groupchatlist.scrollToPosition(mAdapter.getItemCount() - 1);
                                                                 }
                                                             };

    private BroadcastReceiver newPostGroupChatFromSender     = new BroadcastReceiver() {

                                                                 @Override
                                                                 public void onReceive(Context context, Intent intent) {

                                                                     String id = intent.getStringExtra("id");
                                                                     String msg = intent.getStringExtra("msg");
                                                                     String status = intent.getStringExtra("status");
                                                                     String msgtime = intent.getStringExtra("msgtime");
                                                                     String msgtype = intent.getStringExtra("msgtype");
                                                                     String username = intent.getStringExtra("username");
                                                                     String filehash = intent.getStringExtra("filehash");
                                                                     String replyfilehash = intent.getStringExtra("replyfilehash");
                                                                     String replymessage = intent.getStringExtra("replymessage");
                                                                     String replyfrom = intent.getStringExtra("replyfrom");
                                                                     String type = intent.getStringExtra("type");
                                                                     String msgsender = intent.getStringExtra("msgsender");
                                                                     String msgsenderavatar = intent.getStringExtra("msgsenderavatar");
                                                                     String msgid = intent.getStringExtra("msgid");
                                                                     String filemime = intent.getStringExtra("filemime");
                                                                     mAdapter.newPost(filemime, id, msg, status, msgtime, msgtype, username, filehash, replyfilehash, replymessage, replyfrom, type, msgsender, msgsenderavatar, msgid);
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

    private BroadcastReceiver UpdateStatusfileGroupChat      = new BroadcastReceiver() {

                                                                 @Override
                                                                 public void onReceive(Context context, Intent intent) {

                                                                     String status = intent.getStringExtra("status");
                                                                     String msgid = intent.getStringExtra("msgid");
                                                                     mAdapter.updatefilestatus(msgid, status);

                                                                     int getItemCount = ((LinearLayoutManager) mLayoutManager).getItemCount();
                                                                     int getChildCount = ((LinearLayoutManager) mLayoutManager).getChildCount();

                                                                     if (getItemCount == (getChildCount + 1)) { // agar item haye mojud kamtar az andazeye safhe bud
                                                                         //((LinearLayoutManager) mLayoutManager).setStackFromEnd(false); // enteghal be entehaye safhe anjam nashavad
                                                                     } else {
                                                                         ((LinearLayoutManager) mLayoutManager).setStackFromEnd(true); // enteghal be entehaye safhe
                                                                     }
                                                                 }
                                                             };
    private BroadcastReceiver UpdateIdfileGroupChat          = new BroadcastReceiver() {

                                                                 @Override
                                                                 public void onReceive(Context context, Intent intent) {
                                                                     String filehash = intent.getStringExtra("filehash");
                                                                     String msgid = intent.getStringExtra("msgid");

                                                                     mAdapter.updatefileid(filehash, msgid);
                                                                 }
                                                             };

    private BroadcastReceiver UpdateMessageIDGroupChat       = new BroadcastReceiver() {

                                                                 @Override
                                                                 public void onReceive(Context context, Intent intent) {

                                                                     String newMsgID = intent.getStringExtra("newMsgID");
                                                                     String oldMsgID = intent.getStringExtra("oldMsgID");
                                                                     mAdapter.updateMessageID(newMsgID, oldMsgID);
                                                                 }
                                                             };

    private BroadcastReceiver UpdateFileMessageIDGroupChat   = new BroadcastReceiver() {

                                                                 @Override
                                                                 public void onReceive(Context context, Intent intent) {

                                                                     String newMsgID = intent.getStringExtra("newMsgID");
                                                                     String fileHash = intent.getStringExtra("fileHash");
                                                                     mAdapter.updateFileMessageID(newMsgID, fileHash);
                                                                 }
                                                             };

    private BroadcastReceiver updateViewAfterResendGroupChat = new BroadcastReceiver() {

                                                                 @Override
                                                                 public void onReceive(Context context, Intent intent) {
                                                                     loadchathistory();
                                                                 }
                                                             };

    private BroadcastReceiver LoadGroupChat                  = new BroadcastReceiver() {

                                                                 @Override
                                                                 public void onReceive(Context context, Intent intent) {
                                                                     scrollToOffset = true;
                                                                     loadchathistory();
                                                                 }
                                                             };

    private BroadcastReceiver hashSearchGroupChat            = new BroadcastReceiver() {

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

    private BroadcastReceiver changeHeaderGroupChat          = new BroadcastReceiver() {

                                                                 @Override
                                                                 public void onReceive(Context context, Intent intent) {

                                                                     changeHeader();

                                                                 }
                                                             };

    private BroadcastReceiver LoadBottomGroupChat            = new BroadcastReceiver() {

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


    private SpannableStringBuilder parseText(String text, Boolean withLink) {
        PageMessagingPopularFunction pm = new PageMessagingPopularFunction();
        SpannableStringBuilder builder = pm.parseText(text, "CHAT_LIST", withLink, GroupChat.this, 0);
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
                G.mainGroupID = "";
                G.visibleGroupID = "";

                G.cmd.setLastMessage(gchid, "2", edtchat.getText().toString());

                if (G.hashtakSearch) {
                    mAdapter.resethighlight();
                    mAdapter.changeHeader();
                    return;
                }

                super.onBackPressed();
            }
        }
    }


    private void loadchathistoryHashtak(int value, String firstNewHashtakID) {
        String hashtakOffset = (value - 30) + "";

        clearArray();

        int size = G.cmd.getgroupchathistoryRowCount("groupchathistory", gchid);

        if (size != 0) {

            int count = G.cmd.selectHistorySize("groupchathistory", "groupchatroom_id = '" + gchid + "'");
            Cursor cursorGroupChat = G.cmd.selectLimitOffset("groupchathistory", "groupchatroom_id = '" + gchid + "'", count + "", hashtakOffset);

            readCursor(cursorGroupChat);

            if (inActivity == true) {
                sendseen();
            }

            if (selectedItem.size() != msgidarray.size()) {

                int ekhtelaf = msgidarray.size() - selectedItem.size();

                for (int i = 0; i < ekhtelaf; i++) {
                    selectedItem.add(false);
                }
            }
            newPos = idarray.indexOf(firstNewHashtakID);
        }

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Handler h = new Handler();
                h.postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        mAdapter.setmservice(mService);
                    }
                }, 1000);
                groupchatlist.setVisibility(View.VISIBLE);
                mAdapter = new GroupChatRecycleAdapter(filemimearray, idarray, msgarray, statusarray, msgtimearray, msgtypearray, usernames, selectedItem, typearray, filehasharray, replyfilehasharray, replymessagearray, replyfromarray, runUploaderThread, runDownloaderThread, runDownloaderListener, runUploaderListener, msgsenderarray, msgsenderavatararray, msgidarray, GroupChat.this, year, month, day, il, view, dm, dmDialog, G.HANDLER, replymessage, replyfilehash, replyfrom, mService, selectItemVisible, btnDrawerReplay, txtDrawerCounter, sideDrawer, gchname, gchid, smileClicked, countOffset, load, loadBottom, layout_music);
                groupchatlist.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                groupchatlist.scrollToPosition(newPos);
            }
        });
    }


    private void loadchathistoryHashtakLimit(int value, String firstNewHashtakID) {
        String hashtakOffset = (value - 50) + "";

        clearArray();

        int size = G.cmd.getgroupchathistoryRowCount("groupchathistory", gchid);

        if (size != 0) {
            Cursor cursorGroupChat = G.cmd.selectLimitOffset("groupchathistory", "groupchatroom_id = '" + gchid + "'", 100 + "", hashtakOffset);
            readCursor(cursorGroupChat);

            if (inActivity == true) {
                sendseen();
            }

            if (selectedItem.size() != msgidarray.size()) {

                int ekhtelaf = msgidarray.size() - selectedItem.size();

                for (int i = 0; i < ekhtelaf; i++) {
                    selectedItem.add(false);
                }
            }
            newPos = idarray.indexOf(firstNewHashtakID);
        }

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Handler h = new Handler();
                h.postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        mAdapter.setmservice(mService);
                    }
                }, 1000);
                groupchatlist.setVisibility(View.VISIBLE);
                mAdapter = new GroupChatRecycleAdapter(filemimearray, idarray, msgarray, statusarray, msgtimearray, msgtypearray, usernames, selectedItem, typearray, filehasharray, replyfilehasharray, replymessagearray, replyfromarray, runUploaderThread, runDownloaderThread, runDownloaderListener, runUploaderListener, msgsenderarray, msgsenderavatararray, msgidarray, GroupChat.this, year, month, day, il, view, dm, dmDialog, G.HANDLER, replymessage, replyfilehash, replyfrom, mService, selectItemVisible, btnDrawerReplay, txtDrawerCounter, sideDrawer, gchname, gchid, smileClicked, countOffset, load, loadBottom, layout_music);
                groupchatlist.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                groupchatlist.scrollToPosition(newPos);
            }
        });
    }


    private void loadchathistoryToBottom() {

        if (hashtakSearch) {

            int size = G.cmd.getgroupchathistoryRowCount("groupchathistory", gchid);

            if (size != 0) {

                String firstID = "";
                groupchatlist.setVisibility(View.VISIBLE);

                Cursor cursorGroupChat;

                String lastListID = idarray.get(0);
                String lastListIDSetPos = idarray.get(idarray.size() - 1);

                clearArray();

                int offsetOfLast = G.cmd.selectOffsetOfIDFromTop("groupchathistory", "groupchatroom_id = '" + gchid + "'", lastListID);
                cursorGroupChat = G.cmd.selectLimitOffset("groupchathistory", "groupchatroom_id = '" + gchid + "'", "-1", offsetOfLast + "");

                readCursor(cursorGroupChat);

                if (inActivity == true) {
                    sendseen();
                }

                if (selectedItem.size() != msgidarray.size()) {

                    int ekhtelaf = msgidarray.size() - selectedItem.size();

                    for (int i = 0; i < ekhtelaf; i++) {
                        selectedItem.add(false);
                    }
                }

                beforOffset = idarray.indexOf(firstID); // ba avalin id mojud dar safhe ghable az load ba scroll makan payam ra migirim ta bad az load be haman makan beravim                
                newPosBottom = idarray.indexOf(lastListIDSetPos);
            }

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {

                        @Override
                        public void run() {

                            mAdapter.setmservice(mService);
                        }
                    }, 1000);
                    groupchatlist.setVisibility(View.VISIBLE);
                    mAdapter = new GroupChatRecycleAdapter(filemimearray, idarray, msgarray, statusarray, msgtimearray, msgtypearray, usernames, selectedItem, typearray, filehasharray, replyfilehasharray, replymessagearray, replyfromarray, runUploaderThread, runDownloaderThread, runDownloaderListener, runUploaderListener, msgsenderarray, msgsenderavatararray, msgidarray, GroupChat.this, year, month, day, il, view, dm, dmDialog, G.HANDLER, replymessage, replyfilehash, replyfrom, mService, selectItemVisible, btnDrawerReplay, txtDrawerCounter, sideDrawer, gchname, gchid, smileClicked, countOffset, load, loadBottom, layout_music);
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


    private void loadchathistory() {

        countOffset++;

        int size = G.cmd.getgroupchathistoryRowCount("groupchathistory", gchid);

        if (size != 0) {

            String firstID = "";

            if ( !firstLoad) {
                if (idarray.size() > 0) {
                    firstID = idarray.get(0);
                }
            }

            if (hashtakSearch) {
                lastHashtakID = idarray.get(lastHighlightPosition); // idye balatarin hashtak ra ghabl az load list migirim 
            }

            clearArray();

            String newMessagePosition = "5";
            boolean detectNewMessagePos = true;

            Cursor cursorGroupChat;

            if (firstLoad) {
                firstLoad = false;
                load = true;
                cursorGroupChat = G.cmd.selectHistoryByMessageID("groupchathistory", "groupchatroom_id = '" + gchid + "'", 30);

            } else {
                mAdapter.resetSelectedItem();

                int idOffset = G.cmd.selectOffsetOfIDFromTop("groupchathistory", "groupchatroom_id = '" + gchid + "'", firstID);

                String loadOffset = "0";
                load = false;
                if (idOffset - 30 > 0) {
                    loadOffset = (idOffset - 30) + "";
                    load = true;
                }

                cursorGroupChat = G.cmd.selectLimitOffset("groupchathistory", "groupchatroom_id = '" + gchid + "'", "-1", loadOffset + "");
            }

            while (cursorGroupChat.moveToNext()) {
                String chatid = cursorGroupChat.getString(cursorGroupChat.getColumnIndex("id"));
                String msgid = cursorGroupChat.getString(cursorGroupChat.getColumnIndex("msg_id"));
                String msg = cursorGroupChat.getString(cursorGroupChat.getColumnIndex("msg"));
                String status = cursorGroupChat.getString(cursorGroupChat.getColumnIndex("status"));
                String msgtime = cursorGroupChat.getString(cursorGroupChat.getColumnIndex("msg_time"));
                String msgtype = cursorGroupChat.getString(cursorGroupChat.getColumnIndex("msg_type"));
                String msgsender = cursorGroupChat.getString(cursorGroupChat.getColumnIndex("msg_sender"));
                String msgsenderavatar = cursorGroupChat.getString(cursorGroupChat.getColumnIndex("msg_sender_avatar"));
                String type = cursorGroupChat.getString(cursorGroupChat.getColumnIndex("type"));
                String filehash = cursorGroupChat.getString(cursorGroupChat.getColumnIndex("filehash"));
                String username = cursorGroupChat.getString(cursorGroupChat.getColumnIndex("username"));
                String replyfilehash = cursorGroupChat.getString(cursorGroupChat.getColumnIndex("replyfilehash"));
                String replymessage = cursorGroupChat.getString(cursorGroupChat.getColumnIndex("replymessage"));
                String replyfrom = cursorGroupChat.getString(cursorGroupChat.getColumnIndex("replyfrom"));
                String filemime = cursorGroupChat.getString(cursorGroupChat.getColumnIndex("filemime"));

                if (detectNewMessagePos) { // raftan be payame jadid dar ebtedaye vorud be safhe
                    if ( !status.equals("1") && !status.equals("2") && !status.equals("3")) { // agar ma ferestandeye payam nabudim
                        if ( !newMessagePosition.equals(status) && status.equals("4")) {
                            detectNewMessagePos = false;
                            newMsgPosition = cursorGroupChat.getPosition();
                        }
                        newMessagePosition = status;
                    }
                }

                idarray.add(chatid);
                msgarray.add(msg);
                statusarray.add(status);
                msgtimearray.add(msgtime);
                msgtypearray.add(msgtype);
                msgidarray.add(msgid);
                filemimearray.add(filemime);
                msgsenderarray.add(msgsender);
                msgsenderavatararray.add(msgsenderavatar);
                usernames.add(username);
                filehasharray.add(filehash);
                typearray.add(type);

                replyfilehasharray.add(replyfilehash);
                replymessagearray.add(replymessage);
                replyfromarray.add(replyfrom);

                runUploaderThread.add(true);
                runDownloaderThread.add(true);
                runDownloaderListener.add(true);
                runUploaderListener.add(true);
            }
            cursorGroupChat.close();

            if (inActivity == true) {
                sendseen();
            }

            if (selectedItem.size() != msgidarray.size()) {

                int ekhtelaf = msgidarray.size() - selectedItem.size();

                for (int i = 0; i < ekhtelaf; i++) {
                    selectedItem.add(false);
                }
            }

            beforOffset = idarray.indexOf(firstID); // ba avalin id mojud dar safhe ghable az load ba scroll makan payam ra migirim ta bad az load be haman makan beravim
        } else {

            //==========zamani ke clear chat mikonim arayeha ra khali mikonim ta bad az notify kardan safhe khali shavad
            clearArray();
        }

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Handler h = new Handler();
                h.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mAdapter.setmservice(mService);
                    }
                }, 1000);

                groupchatlist.setVisibility(View.VISIBLE);
                mAdapter = new GroupChatRecycleAdapter(filemimearray, idarray, msgarray, statusarray, msgtimearray, msgtypearray, usernames, selectedItem, typearray, filehasharray, replyfilehasharray, replymessagearray, replyfromarray, runUploaderThread, runDownloaderThread, runDownloaderListener, runUploaderListener, msgsenderarray, msgsenderavatararray, msgidarray, GroupChat.this, year, month, day, il, view, dm, dmDialog, G.HANDLER, replymessage, replyfilehash, replyfrom, mService, selectItemVisible, btnDrawerReplay, txtDrawerCounter, sideDrawer, gchname, gchid, smileClicked, countOffset, load, loadBottom, layout_music);
                groupchatlist.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                if (scrollToOffset) {
                    groupchatlist.scrollToPosition(beforOffset);
                } else {
                    if (newMsgPosition == -1) {
                        groupchatlist.scrollToPosition(mAdapter.getItemCount() - 1);
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


    private void readCursor(Cursor cursorGroupChat) {
        while (cursorGroupChat.moveToNext()) {
            String chatid = cursorGroupChat.getString(cursorGroupChat.getColumnIndex("id"));
            String msgid = cursorGroupChat.getString(cursorGroupChat.getColumnIndex("msg_id"));
            String msg = cursorGroupChat.getString(cursorGroupChat.getColumnIndex("msg"));
            String status = cursorGroupChat.getString(cursorGroupChat.getColumnIndex("status"));
            String msgtime = cursorGroupChat.getString(cursorGroupChat.getColumnIndex("msg_time"));
            String msgtype = cursorGroupChat.getString(cursorGroupChat.getColumnIndex("msg_type"));
            String msgsender = cursorGroupChat.getString(cursorGroupChat.getColumnIndex("msg_sender"));
            String msgsenderavatar = cursorGroupChat.getString(cursorGroupChat.getColumnIndex("msg_sender_avatar"));
            String type = cursorGroupChat.getString(cursorGroupChat.getColumnIndex("type"));
            String filehash = cursorGroupChat.getString(cursorGroupChat.getColumnIndex("filehash"));
            String username = cursorGroupChat.getString(cursorGroupChat.getColumnIndex("username"));
            String replyfilehash = cursorGroupChat.getString(cursorGroupChat.getColumnIndex("replyfilehash"));
            String replymessage = cursorGroupChat.getString(cursorGroupChat.getColumnIndex("replymessage"));
            String replyfrom = cursorGroupChat.getString(cursorGroupChat.getColumnIndex("replyfrom"));
            String filemime = cursorGroupChat.getString(cursorGroupChat.getColumnIndex("filemime"));

            idarray.add(chatid);
            msgarray.add(msg);
            statusarray.add(status);
            msgtimearray.add(msgtime);
            msgtypearray.add(msgtype);
            msgidarray.add(msgid);
            filemimearray.add(filemime);
            msgsenderarray.add(msgsender);
            msgsenderavatararray.add(msgsenderavatar);
            usernames.add(username);
            filehasharray.add(filehash);
            typearray.add(type);

            replyfilehasharray.add(replyfilehash);
            replymessagearray.add(replymessage);
            replyfromarray.add(replyfrom);

            runUploaderThread.add(true);
            runDownloaderThread.add(true);
            runDownloaderListener.add(true);
            runUploaderListener.add(true);
        }
        cursorGroupChat.close();
    }


    private void clearArray() {
        try {
            idarray.clear();
            msgarray.clear();
            statusarray.clear();
            msgtimearray.clear();
            msgtypearray.clear();
            msgsenderarray.clear();
            msgsenderavatararray.clear();
            usernames.clear();
            msgidarray.clear();
            filehasharray.clear();
            typearray.clear();
            replyfilehasharray.clear();
            replymessagearray.clear();
            replyfromarray.clear();
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

                Cursor cursorUnread = G.cmd.selectUnreadMessage("groupchathistory", "groupchatroom_id ='" + gchid + "'");
                while (cursorUnread.moveToNext()) {
                    String msgID = cursorUnread.getString(cursorUnread.getColumnIndex("msg_id"));
                    String msgStatus = cursorUnread.getString(cursorUnread.getColumnIndex("status"));
                    String msgType = cursorUnread.getString(cursorUnread.getColumnIndex("msg_type"));

                    if (msgType.equals("1")) { // payamhaye daryafti
                        if ( !msgStatus.equals("5")) { // payamhaye nakhande (seen = 5 , unseen = 4)
                            if (msgID != null && !msgID.isEmpty() && !msgID.equals("null") && !msgID.equals("")) {
                                G.cmd.updategroupmsgstatus(msgID, "5");
                            }
                        }
                    }
                }

                updateseen(gchid);

                HelperComputeUnread.unreadMessageCount();
            }
        });

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

        doUnbindService();

    }


    void doBindService() {
        bindService(new Intent(this, MyService.class), mConnection, Context.BIND_AUTO_CREATE);
    }


    void doUnbindService() {
        if (mBounded) {
            unbindService(mConnection);
        }
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
            gchatmsg = "";
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
                                Toast.makeText(GroupChat.this, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
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

                    G.cmd.Addfiles1(fileHash, fileUrl, fileThumb, "5", filePath);
                    mService.sendgroupmessage(fileMime, gchid, "", avatar, fileType, fileHash, fileUrl, fileThumb, "", "", "", "0", null);

                } else {
                    preparationFileUplaod(filePath, fileHash, fileMime, fileType);
                }
            }

            super.onPostExecute(result);
        }
    }


    private void preparationFileUplaod(String filePath, String fileHash, String fileMime, String fileType) {

        G.cmd.Addfiles1(fileHash, null, null, "3", filePath);

        String time = G.helperGetTime.getTime();
        String senderType = "2"; // 2 means that we are message sender 
        String status = "0";

        G.cmd.addgroupchathistory(fileMime, gchid, null, "", status, time, senderType, G.name, G.avatarLQ, fileType, fileHash, null, null, G.username, "", "", "");
        G.cmd.updategroupchatrooms(gchid, "", time);

        String id = G.cmd.selectLastGroupChatHistoryID(); // akharin id ke ba ezafe shodane payame jadid be dast miayad ra migirim
        mAdapter.newPost(fileMime, id, "", status, time, senderType, G.name, fileHash, "", "", "", fileType, G.username, G.avatarLQ, null);
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
                    Toast.makeText(GroupChat.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            if (showDefaultImageCapture) {
                showDefaultImageCapture = false;
                try {
                    filePath = fileUri.getPath();
                    String filename = mediaFile.getName();
                    gchatmsg = "";
                    try {
                        getsha(filePath, filename, true);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                catch (Exception e) {
                    Toast.makeText(GroupChat.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
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
                    gchatmsg = "";
                    try {
                        getsha(filePath, imageFileName, true);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

                case REQUEST_CAMERA:

                    if (crop.equals("0")) { // disable

                        type = "2";

                        try {
                            filePath = fileUri.getPath();
                            String filename = mediaFile.getName();
                            gchatmsg = "";
                            try {
                                getsha(filePath, filename, true);
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        catch (Exception e) {
                            Toast.makeText(GroupChat.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
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
                                @SuppressWarnings("deprecation") Cursor cursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
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
                                        Toast.makeText(GroupChat.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(GroupChat.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            });

                            if (needCrop) { // agar tasvir gif nabashad
                                gchatmsg = "";
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
                                            Toast.makeText(GroupChat.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(GroupChat.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(GroupChat.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(GroupChat.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
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
                                gchatmsg = "";
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
                                Toast.makeText(GroupChat.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(GroupChat.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
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
                            gchatmsg = "Contact Information : " + "\n" + "Name : " + name + "\n" + "Number : " + number;
                            mService.sendgroupmessage(filemime, gchid, gchatmsg, avatar, type, null, null, null, replymessage, replyfilehash, replyfrom, "0", null);
                            layoutreplay.setVisibility(View.GONE);
                            replymessage = "";
                            replyfilehash = "";
                            replyfrom = "";
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
                                gchatmsg = "";
                                try {
                                    getsha(filePath, filename, false);
                                }
                                catch (Exception e) {

                                    e.printStackTrace();
                                }

                            }
                            catch (Exception e) {
                                Toast.makeText(GroupChat.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
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


    class chechfileexist extends AsyncTask<String, String, String> {

        boolean exists;


        @Override
        protected String doInBackground(String... args) {

            String resend = args[0];
            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();

                JSONObject jsonobj = jParser.getJSONFromUrl(G.checkfileexist + filehash, params, "GET", basicAuth, null);

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

                            if (exists) {
                                fileurl = c.getString("url");
                                filethumb = c.getString("thumbnailLq");
                            }
                        }
                    }
                }
                catch (JSONException e) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(GroupChat.this, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            catch (Exception e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(GroupChat.this, getString(R.string.internet_connection_problem_en), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            return resend;
        }


        @Override
        protected void onPostExecute(String result) {

            if (result.equals("1")) { // Resend

                if (exists == true) {
                    G.cmd.updatefilestatus(filehash, 5);
                    mService.sendgroupmessage(filemime, gchid, gchatmsg, avatar, type, filehash, fileurl, filethumb, replymessage, replyfilehash, replyfrom, "1", null);
                } else {
                    G.cmd.updatefilestatus(filehash, 3);
                    loadchathistory();
                }

            } else {
                if (exists == true) {

                    G.cmd.Addfiles1(filehash, fileurl, filethumb, "5", filePath);

                    mService.sendgroupmessage(filemime, gchid, gchatmsg, avatar, type, filehash, fileurl, filethumb, replymessage, replyfilehash, replyfrom, "0", null);
                    layoutreplay.setVisibility(View.GONE);
                    replymessage = "";
                    replyfilehash = "";
                    replyfrom = "";
                } else {
                    alertdialogupfile();
                }
            }

            super.onPostExecute(result);
        }

    }


    private void alertdialogupfile() {

        G.cmd.Addfiles1(filehash, null, null, "3", filePath);

        String currentTimea;
        try {
            currentTimea = currentTime.getDateTime();
        }
        catch (Exception e) {
            currentTimea = helperGetTime.getTime();
        }

        String time = currentTimea;
        String typee = "2";
        String status = "0";
        String NAME = G.cmd.namayesh4(6, "info");
        String avatar = G.cmd.namayesh4(4, "info");
        String username = G.cmd.namayesh4(1, "info");
        G.cmd.addgroupchathistory(filemime, gchid, null, gchatmsg, status, time, typee, NAME, avatar, type, filehash, null, null, username, replymessage, replyfrom, replyfilehash);
        G.cmd.updategroupchatrooms(gchid, gchatmsg, time);

        String id = G.cmd.selectLastGroupChatHistoryID(); // akharin id ke ba ezafe shodane payame jadid be dast miayad ra migirim
        mAdapter.newPost(filemime, id, gchatmsg, status, time, typee, NAME, filehash, replyfilehash, replymessage, replyfrom, type, username, avatar, null);
    }


    private void initDialog() {

        dialogSelectMedia = new Dialog(GroupChat.this);
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

                Intent intent = new Intent(GroupChat.this, myPaint.class);
                startActivityForResult(intent, G.request_code_paint);
            }
        });

    }


    void pictureByCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }


    void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/* video/*");
        startActivityForResult(intent, G.request_code_PICK_IMAGE);
    }


    void videoByCamera() {

        PackageManager packageManager = getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) == false) {
            Toast.makeText(GroupChat.this, getString(R.string.device_dosenot_camera_en), Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(GroupChat.this, explorer.class);
        startActivityForResult(intent, G.request_code_FILE);
    }


    void pickContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(intent, G.request_code_cantact_phone);
    }

    Boolean        sendPosition = false;
    ProgressDialog pd;


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

                    gchatmsg = "My Position is : " + "\n" + "Latitude : " + String.valueOf(location.getLatitude()) + "\n" + "Longitude : " + String.valueOf(location.getLongitude());
                    mService.sendgroupmessage(filemime, gchid, gchatmsg, avatar, type, null, null, null, replymessage, replyfilehash, replyfrom, "0", null);
                }
                else {
                    sendPosition = true;
                    pd = new ProgressDialog(GroupChat.this);
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

                                                  type = "6";
                                                  gchatmsg = "My Position is : " + "\n" + "Latitude : " + String.valueOf(location.getLatitude()) + "\n" + "Longitude : " + String.valueOf(location.getLongitude());
                                                  mService.sendgroupmessage(filemime, gchid, gchatmsg, avatar, type, null, null, null, replymessage, replyfilehash, replyfrom, "0", null);
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
            gchatmsg = "";
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
        @SuppressWarnings("resource") FileInputStream fis = new FileInputStream(datafile);
        byte[] dataBytes = new byte[1024];

        int nread = 0;

        while ((nread = fis.read(dataBytes)) != -1) {
            md.update(dataBytes, 0, nread);
        }

        byte[] mdbytes = md.digest();

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
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        new chechfileexist().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "0");
                    } else {
                        new chechfileexist().execute("0");
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
        View mView = layoutInflater.inflate(R.layout.popup_group_chat, null);

        Button btnClearHistory = (Button) mView.findViewById(R.id.btn_clear_history);

        popUp = new PopupWindow(GroupChat.this);
        popUp.setContentView(mView);
        popUp.setWindowLayoutMode(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        popUp.setHeight(1);
        popUp.setWidth(1);
        popUp.setTouchable(true);
        popUp.setFocusable(false);
        popUp.setOutsideTouchable(true);
        popUp.setBackgroundDrawable(new BitmapDrawable());
        popUp.showAtLocation(v, Gravity.NO_GRAVITY, location[0], location[1]);

        Button btnleftgroup = (Button) mView.findViewById(R.id.btn_left_group);
        Button btngroupinfo = (Button) mView.findViewById(R.id.btn_group_info);
        Button btnaddmember = (Button) mView.findViewById(R.id.btn_add_member);
        Button btneditegroup = (Button) mView.findViewById(R.id.btn_edite_group);

        String editGroup = getString(R.string.edit_group_en);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {// allcaps is for api 14 and upper
            btneditegroup.setAllCaps(false);
        } else {
            editGroup = editGroup.substring(0, 1).toUpperCase() + editGroup.substring(1, 5).toLowerCase() + editGroup.substring(5, 6).toUpperCase() + editGroup.substring(7).toLowerCase();
        }
        btneditegroup.setText(editGroup);

        String leftGroup = getString(R.string.left_group_en);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {// allcaps is for api 14 and upper
            btnleftgroup.setAllCaps(false);
        } else {
            leftGroup = leftGroup.substring(0, 1).toUpperCase() + leftGroup.substring(1, 5).toLowerCase() + leftGroup.substring(5, 6).toUpperCase() + leftGroup.substring(7).toLowerCase();
        }
        btnleftgroup.setText(leftGroup);

        String clearChat = getString(R.string.clear_history_en);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {// allcaps is for api 14 and upper
            btnClearHistory.setAllCaps(false);
        } else {
            clearChat = clearChat.substring(0, 1).toUpperCase() + clearChat.substring(1, 6).toLowerCase() + clearChat.substring(6, 7).toUpperCase() + clearChat.substring(7).toLowerCase();
        }
        btnClearHistory.setText(clearChat);

        String groupInfo = getString(R.string.group_info_en);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {// allcaps is for api 14 and upper
            btngroupinfo.setAllCaps(false);
        } else {
            groupInfo = groupInfo.substring(0, 1).toUpperCase() + groupInfo.substring(1, 6).toLowerCase() + groupInfo.substring(6, 7).toUpperCase() + groupInfo.substring(7).toLowerCase();
        }
        btngroupinfo.setText(groupInfo);

        String addMember = getString(R.string.add_member_en);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {// allcaps is for api 14 and upper
            btnaddmember.setAllCaps(false);
        } else {
            addMember = addMember.substring(0, 1).toUpperCase() + addMember.substring(1, 4).toLowerCase() + addMember.substring(4, 5).toUpperCase() + addMember.substring(5).toLowerCase();
        }
        btnaddmember.setText(addMember);

        if ( !gchactive.equals("1")) {

            btnaddmember.setVisibility(View.GONE);
        }
        btngroupinfo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (popUp != null && popUp.isShowing()) {
                    popUp.dismiss();
                }

                Intent intent = new Intent(GroupChat.this, GroupProfile.class);
                intent.putExtra("gchid", gchid);
                intent.putExtra("gchname", gchname);
                intent.putExtra("gchavatar", gchavatar);
                intent.putExtra("gchavatarHq", gchavatarHq);
                intent.putExtra("gchdescription", gchdescription);
                intent.putExtra("gchmembership", gchmembership);
                intent.putExtra("gchactive", gchactive);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
        btnleftgroup.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (popUp != null && popUp.isShowing()) {
                    popUp.dismiss();
                }

                ConfirmationDialog cm = new ConfirmationDialog(GroupChat.this, new OnColorChangedListenerSelect() {

                    @Override
                    public void colorChanged(String key, int color) {

                    }


                    @Override
                    public void Confirmation(Boolean result) {
                        if (result) {
                            GA.deleteGroup(gchid, username, gchmembership, new OnDeleteComplete() {

                                @Override
                                public void deleteComplete(Boolean result) {
                                    if (result) {
                                        Intent intent = new Intent(GroupChat.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        }
                    }
                });
                cm.showdialog(getString(R.string.do_you_want_delete_this_group));
            }
        });
        btnaddmember.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (gchactive.equals("1")) {
                    Intent intent = new Intent(GroupChat.this, AddMemberToGroup.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("id", gchid);
                    intent.putExtra("name", gchname);
                    intent.putExtra("groupdesc", gchdescription);
                    intent.putExtra("gchmembership", gchmembership);
                    startActivity(intent);
                }

            }
        });

        btnClearHistory.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                G.cmd.cleargroupchathistory(gchid);
                G.cmd.updategroupchatrooms(gchid, "", "");
                mAdapter.clearHistory();

                loadchathistory();

                if (popUp != null && popUp.isShowing()) {
                    popUp.dismiss();
                }
                clearhistorywithuid(gchid);
            }
        });

        if (gchmembership == null) {
            gchmembership = G.cmd.selectMembership(gchid);

            if (gchmembership != null) {
                if ( !gchmembership.equals("1")) {
                    btneditegroup.setVisibility(View.GONE);
                }
            } else {
                btneditegroup.setVisibility(View.GONE);
            }
        }
        btneditegroup.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (popUp != null && popUp.isShowing()) {
                    popUp.dismiss();
                }
                Intent intent = new Intent(GroupChat.this, EditGroup.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("groupuid", gchid);
                intent.putExtra("groupName", gchname);
                intent.putExtra("groupDesc", gchdescription);
                intent.putExtra("groupavatarlq", gchavatar);
                intent.putExtra("groupavatarhq", gchavatarHq);
                intent.putExtra("groupmembership", gchmembership);
                startActivity(intent);
                finish();

            }
        });
    }


    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialogg = new ProgressDialog(this);
                pDialogg.setMessage(getString(R.string.load_information_please_wait_en));
                pDialogg.setIndeterminate(false);
                pDialogg.setMax(100);
                pDialogg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialogg.setCancelable(true);
                pDialogg.show();
                return pDialogg;
            default:
                return null;
        }
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
        } else {
            if (canStop) {

                try {
                    String fileName = HelperString.getFileName(outputFile);
                    String filename = "record_" + fileName;
                    gchatmsg = "";
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
    public void onConfigurationChanged(Configuration newConfig) {
        Utils.checkLanguage(this);
        super.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onStop() {
        super.onStop();

        inActivity = false;

    }


    private void openLayoutAddText(String filename, Bitmap bit, final OnColorChangedListenerSelect l) {

        final LinearLayout layout = (LinearLayout) findViewById(R.id.layout_add_text_group);
        layout.setVisibility(View.VISIBLE);

        TextView txtFileName = ((TextView) findViewById(R.id.txt_file_name_group));
        txtFileName.setText(filename);

        ImageView iv = (ImageView) findViewById(R.id.imageView_file_pic_group);
        iv.setImageBitmap(bit);

        llVoice.setVisibility(View.GONE);
        findViewById(R.id.btn_send).setVisibility(View.GONE);

        Button btnSendWithText = (Button) findViewById(R.id.btn_send_file_group);
        btnSendWithText.setTypeface(G.fontAwesome);
        btnSendWithText.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String msgedt = edtchat.getText().toString();
                if (msgedt != null && !msgedt.isEmpty() && !msgedt.equals("null") && !msgedt.equals("")) {
                    gchatmsg = edtchat.getText().toString();
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

        findViewById(R.id.btn_cancel_send_group).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                layout.setVisibility(View.GONE);
                l.colorChanged("cancel", 0);
                closingkeybord();
                llVoice.setVisibility(View.VISIBLE);
                findViewById(R.id.btn_send).setVisibility(View.VISIBLE);
            }
        });

    }


    public static MyService getService() {
        return staticGroupService;
    }


    private void clearhistorywithuid(String uid) {
        Intent intent = new Intent("clearHistoryWithUid");
        intent.putExtra("uid", uid);
        LocalBroadcastManager.getInstance(GroupChat.this).sendBroadcast(intent);
        //=======Update Last Message In Page All
        Intent intentAll = new Intent("clearHistoryAll");
        intentAll.putExtra("MODEL", "2");
        intentAll.putExtra("UID", uid);
        LocalBroadcastManager.getInstance(GroupChat.this).sendBroadcast(intentAll);
    }


    private void updateseen(String uid) {
        Intent intent = new Intent("updateSeenGroup");
        intent.putExtra("uid", uid);
        LocalBroadcastManager.getInstance(GroupChat.this).sendBroadcast(intent);
        //=========Seen For Page All
        Intent intentAll = new Intent("updateSeenAll");
        intentAll.putExtra("MODEL", "2");
        intentAll.putExtra("UID", gchid);
        LocalBroadcastManager.getInstance(GroupChat.this).sendBroadcast(intentAll);
    }

}
