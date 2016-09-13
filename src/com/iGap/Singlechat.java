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
import android.content.ComponentName;
import android.content.ContentProviderOperation;
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
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
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
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Log;
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
import com.iGap.adapter.SingleChatRecycleAdapter;
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
import com.iGap.instruments.multyMyPaint;
import com.iGap.instruments.myPaint;
import com.iGap.interfaces.OnColorChangedListenerSelect;
import com.iGap.interfaces.OnComplet;
import com.iGap.services.MyService;
import com.iGap.services.TimerServies;


/**
 * 
 * namayesh list chat ha va aghaz chat jadid
 *
 */

public class Singlechat extends Activity {

    //************************* ArrayLists
    private ArrayList<String>          filemimearray           = new ArrayList<String>();
    private ArrayList<String>          chatidarray             = new ArrayList<String>();
    private ArrayList<String>          msgarray                = new ArrayList<String>();
    private ArrayList<String>          statusarray             = new ArrayList<String>();
    private ArrayList<String>          msgtimearray            = new ArrayList<String>();
    private ArrayList<String>          msgtypearray            = new ArrayList<String>();
    private ArrayList<String>          msgidarray              = new ArrayList<String>();
    private ArrayList<String>          typearray               = new ArrayList<String>();
    private ArrayList<String>          filehasharray           = new ArrayList<String>();
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

    private int                        messagecash;
    private int                        statusCode;
    private int                        selectCounter;
    private int                        iscontact;
    private int                        leftPading;
    private int                        lastX;
    private int                        clickHashPos;
    private int                        newPosBottom;
    private int                        newMsgPosition          = -1;
    private int                        lastHighlightPosition   = -1;
    private int                        counter                 = 0;
    private int                        secend                  = 0;
    private int                        minute                  = 0;
    private int                        Allmoving               = 0;
    private int                        countOffset             = 0;
    private int                        beforOffset             = 0;
    private int                        newPos                  = 0;
    private int                        DistanceToCancel        = 130;
    public static final int            progress_bar_type       = 10;
    public static final int            MEDIA_TYPE_IMAGE        = 20;

    private String                     mobile;
    private String                     active;
    private String                     forwardFrom;
    private String                     copytext;
    private String                     outputFile;
    private String                     newfilePath;
    private String                     type;
    private String                     chatroomid;
    private String                     chatmsg;
    private String                     userchat;
    private String                     selectedImagePath;
    private String                     filehash;
    private String                     userchatname;
    private String                     userchatavatar;
    private String                     fileurl;
    private String                     filethumb;
    private String                     hashtakText;
    private String                     hashtakBeforeScrollBottom;
    private String                     imageFileName;
    private String                     imageFileNameWithoutCrop;
    private String                     filePathWithoutCrop;
    private String                     lastHashtakID           = "";
    private String                     itemTag                 = "";
    private String                     filemime                = "";
    private String                     replymessage            = "";
    private String                     replyfilehash           = "";
    private String                     replyfrom               = "";
    private String                     day                     = "";
    private String                     month                   = "";
    private String                     year                    = "";
    private String                     filePath                = "0";

    private boolean                    mBounded;
    private boolean                    canStop                 = false;
    private boolean                    selectItemVisible       = false;
    private boolean                    state                   = false;
    private boolean                    cansel                  = false;
    private boolean                    inActivity              = false;
    private boolean                    isigap                  = false;
    private boolean                    smileOpen               = false;
    private boolean                    smileClicked            = false;
    private boolean                    load                    = false;
    private boolean                    scrollToOffset          = false;
    private boolean                    hashtakSearch           = false;
    private boolean                    loadBottomAllow         = false;
    private boolean                    showDefaultImage        = false;
    private boolean                    showDefaultImageCapture = false;
    private boolean                    needCrop                = false;
    private boolean                    isdouble                = true;
    private boolean                    firstLoad               = true;
    private boolean                    loadBottom              = true;
    private boolean                    firsttime               = true;
    public static boolean              showPaintAlert          = true;

    //************************* private variables & elements

    private Button                     btnDrawerTrash;
    private Button                     btnDrawerFiles;
    private Button                     btnDrawerForward;
    private Button                     btnDrawerReplay;
    private Button                     btnMic;
    private Button                     backIcon;
    private Button                     navIcon;
    private Button                     btnCancelSend;
    private Button                     btnUp;
    private Button                     btnDown;
    private Button                     btnCancel;

    private TextView                   usernaveclose;
    private TextView                   txtuserarrowright;
    private TextView                   txtforwardmsg;
    private TextView                   txtforwardfrom;
    private TextView                   userlasttimetx;
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
    private TextView                   txtDrawerCounter;
    private TextView                   tvPaint;
    private TextView                   txt_slide_to_cancel;
    private TextView                   txtTimeRecord;
    private TextView                   usernametx;
    private TextView                   txtedturls;
    private TextView                   txtHash;

    private LinearLayout               lytReplay;
    private LinearLayout               sideDrawer;
    private LinearLayout               mLayout_smile;
    private LinearLayout               layout1;
    private LinearLayout               layout2;
    private LinearLayout               layout3;
    private LinearLayout               lytRoot;
    private LinearLayout               llVoice;
    private LinearLayout               layout_music;
    private LinearLayout               lytHash;
    private LinearLayout               lytMainHeader;
    public static LinearLayout         ll_iscontact;

    private ImageView                  imgPicRecord;
    private ImageView                  useravatar;
    private EditText                   edtchat;

    private Dialog                     dialog;
    private Dialog                     pDialog;
    private Dialog                     dialogSelectMedia;
    private Handler                    handlerdd;
    private Handler                    handler2;
    private Handler                    handler;
    private TimerTask                  timertask;
    private Timer                      timer;
    private Timer                      secendTimer;
    private MyService                  mService;
    private ProgressDialog             pDialogg;
    private SpannableStringBuilder     buildertext;
    private RecyclerView               singlechatlist;
    private RecyclerView.LayoutManager mLayoutManager;
    private PopupWindow                popUp;
    private InputMethodManager         imm                     = null;
    private static File                mediaFile;
    private Uri                        fileUri;
    private LocationManager            locManager;
    private ImageLoader1               il;
    private SlidingTabLayoutemoji      mSlidingTabLayout;
    private ViewPager                  mViewPager;
    private MediaRecorder              mediaRecorder;
    private DrawableManager            dm;
    private SoftKeyboard               softKeyboard;
    private Runnable                   runnable;
    private SingleChatRecycleAdapter   mAdapter;
    private View                       view;
    private DrawableManagerDialog      dmDialog;
    public static MyService            staticSingleService;

    private MediaPlayer                mp                      = new MediaPlayer();
    private JSONParser                 jParser                 = new JSONParser();
    private TimerServies               ts                      = new TimerServies();

    private final ServiceConnection    mConnection             = new ServiceConnection() {

                                                                   @SuppressWarnings("unchecked")
                                                                   @Override
                                                                   public void onServiceConnected(final ComponentName name, final IBinder service) {
                                                                       mService = ((LocalBinder<MyService>) service).getService();
                                                                       mBounded = true;
                                                                       staticSingleService = mService;
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
        setContentView(R.layout.activity_singlechat);
        view = this.findViewById(android.R.id.content);
        inActivity = true;
        G.hashSearchType = 1;

        hashtakSearch = false;
        G.hashtakSearch = false;

        doBindService();

        lytHash = (LinearLayout) findViewById(R.id.lyt_hash);
        lytMainHeader = (LinearLayout) findViewById(R.id.lyt_main_header);

        btnUp = (Button) findViewById(R.id.btn_up);
        btnDown = (Button) findViewById(R.id.btn_down);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        Button btnmultipaint = (Button) findViewById(R.id.btn_multipaint);

        btnmultipaint.setVisibility(View.GONE); // button hide (for disable this action)

        btnmultipaint.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                firsttime = false;
                counter = 0;

                //=================Show Sun dialog

                pDialog = new Dialog(Singlechat.this);
                pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                pDialog.setCancelable(false);
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
                    pDialog.setContentView(R.layout.dialog_wait);
                } else {
                    pDialog.setContentView(R.layout.dialog_wait_simple);
                }
                pDialog.show();

                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(pDialog.getWindow().getAttributes());
                layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
                pDialog.getWindow().setAttributes(layoutParams);

                counter();
                mService.sendpaintrequest();
            }
        });
        txtHash = (TextView) findViewById(R.id.txt_hash);

        btnUp.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                clickHashPos--;
                if ((clickHashPos) >= 0) {
                    int highlightPos = hashPositions.get(clickHashPos);
                    int clearHighlightPos = hashPositions.get(clickHashPos + 1);
                    singlechatlist.scrollToPosition(highlightPos);

                    mAdapter.clearHighlightLayoutBackground(clearHighlightPos);
                    mAdapter.highlightLayoutBackground(highlightPos);

                    lastHighlightPosition = highlightPos;
                } else {
                    clickHashPos++;

                    int toppestHashtakPos = hashPositions.get(0); // first(toppest) hashtak pos
                    String toppestHashtakID = chatidarray.get(toppestHashtakPos);
                    int toppestHashtakOffset = G.cmd.selectOffsetOfIDFromTop("chathistory", "chatroom_id = '" + chatroomid + "'", toppestHashtakID);

                    int idOffset = G.cmd.selectOffsetOfID("chathistory", "chatroom_id = '" + chatroomid + "'", chatidarray.get(0));

                    String firstNewHashtakID = G.cmd.selectHashtakPosition1("chathistory", "chatroom_id = '" + chatroomid + "'", (idOffset + 1) + "", chatroomid, hashtakText);
                    int offset = G.cmd.selectOffsetOfIDFromTop("chathistory", "chatroom_id = '" + chatroomid + "'", firstNewHashtakID); // new hashtak offset

                    if (offset == -1) {
                        Toast.makeText(Singlechat.this, "not exist more!", Toast.LENGTH_SHORT).show();
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
                    singlechatlist.scrollToPosition(highlightPos);

                    mAdapter.clearHighlightLayoutBackground(clearHighlightPos);
                    mAdapter.highlightLayoutBackground(highlightPos);

                    lastHighlightPosition = highlightPos;

                } else {
                    clickHashPos--;
                    int idOffset = G.cmd.selectOffsetOfIDFromTop("chathistory", "chatroom_id = '" + chatroomid + "'", chatidarray.get(chatidarray.size() - 1));
                    String firstNewHashtakID = G.cmd.selectHashtakPositionToBottom("chathistory", "chatroom_id = '" + chatroomid + "'", (idOffset + 1) + "", chatroomid, hashtakText);

                    if (firstNewHashtakID.equals("-1")) {
                        Toast.makeText(Singlechat.this, "not exist more!", Toast.LENGTH_SHORT).show();
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

        locManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            chatroomid = extras.getString("chatroomid");
            if (extras.getString("userchat") != null) {
                userchat = extras.getString("userchat");
                G.mainUserChat = userchat;
                userchatname = extras.getString("userchatname");
                userchatavatar = extras.getString("userchatavatar");
                active = extras.getString("active");
            }

        }
        mobile = userchat.split("@")[0];
        if (userchatname.equals("igap")) {
            isigap = true;
        }

        handler = new Handler();
        handler2 = new Handler();
        txtedturls = new TextView(Singlechat.this);
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                mService.createchattest(userchat);

                getlastseeninfo(mobile);
            }
        }, 1500);

        String currenttime;
        try {
            currenttime = ts.getDateTime();
        }
        catch (Exception e) {
            currenttime = G.helperGetTime.getTime();
        }
        String[] splitedtime = currenttime.split("\\s+");
        String date = splitedtime[0];
        String[] splited_date = date.split("-");
        year = splited_date[0];
        month = splited_date[1];
        day = splited_date[2];

        il = new ImageLoader1(Singlechat.this, G.basicAuth);
        dm = new DrawableManager(Singlechat.this);
        dmDialog = new DrawableManagerDialog(Singlechat.this);

        btnUp.setTypeface(G.fontAwesome);
        btnDown.setTypeface(G.fontAwesome);
        btnCancel.setTypeface(G.fontAwesome);
        btnmultipaint.setTypeface(G.fontAwesome);
        ll_iscontact = (LinearLayout) findViewById(R.id.ll_iscontact);
        Button addContect = (Button) findViewById(R.id.add_contect_btn);
        addContect.setTypeface(G.robotoBold);
        layout_music = (LinearLayout) findViewById(R.id.ll_music_chat);
        lytReplay = (LinearLayout) findViewById(R.id.layout_replay);
        txtuserarrowright = (TextView) findViewById(R.id.user_arrow_right);
        usernaveclose = (TextView) findViewById(R.id.user_nave_close);
        txtforwardfrom = (TextView) findViewById(R.id.txt_forward_from);
        txtforwardmsg = (TextView) findViewById(R.id.txt_forwardmsg);
        txtuserarrowright.setTypeface(G.fontAwesome);
        usernaveclose.setTypeface(G.fontAwesome);

        messagecash = G.cmd.getRowCount("messagingcache");

        if (messagecash != 0) {
            lytReplay.setVisibility(View.VISIBLE);
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

        Button blockContact = (Button) findViewById(R.id.block_contact_btn);
        blockContact.setTypeface(G.robotoBold);

        addContect.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Singlechat.this, AddNewContact.class);
                intent.putExtra("MOBILE", mobile);
                intent.putExtra("userchatavatar", userchatavatar);
                startActivity(intent);
                ll_iscontact.setVisibility(View.GONE);
            }
        });
        blockContact.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                int mobileExist = G.cmd.isMobileExist(mobile);

                if (mobileExist == 1) {
                    new BlockContact().execute(mobile, userchatname);
                    ll_iscontact.setVisibility(View.GONE);
                } else {
                    addContactForBlock(mobile);
                }
            }
        });

        backIcon = (Button) findViewById(R.id.back_icon);
        txtDrawerCounter = (TextView) findViewById(R.id.txt_drawer_counter);
        userlasttimetx = (TextView) findViewById(R.id.user_last_time_tx);
        userlasttimetx.setTypeface(G.robotoLight);
        userlasttimetx.setText(G.cmd.getLastSeen(userchat.split("@")[0]));

        navIcon = (Button) findViewById(R.id.nav_icon);
        attachIcon = (TextView) findViewById(R.id.attach_icon1);
        smaileIcon = (TextView) findViewById(R.id.smaile_icon);
        txt_slide_to_cancel = (TextView) findViewById(R.id.txt_slideto_cancel);
        txtTimeRecord = (TextView) findViewById(R.id.txt_time_record);
        usernametx = (TextView) findViewById(R.id.user_name_tx);
        usernametx.setTypeface(G.robotoBold);

        sideDrawer = (LinearLayout) findViewById(R.id.Side_drawer);
        LinearLayout lluserinfo = (LinearLayout) findViewById(R.id.ll_user_info);
        layout1 = (LinearLayout) findViewById(R.id.layout1);
        layout2 = (LinearLayout) findViewById(R.id.layout2);
        layout3 = (LinearLayout) findViewById(R.id.layout3);
        lytRoot = (LinearLayout) findViewById(R.id.lyt_root);
        mLayout_smile = (LinearLayout) findViewById(R.id.layout_smile);

        btnDrawerTrash = (Button) findViewById(R.id.btn_drawer_trash);
        btnDrawerFiles = (Button) findViewById(R.id.btn_drawer_files);
        btnDrawerReplay = (Button) findViewById(R.id.btn_drawer_replay);
        btnDrawerForward = (Button) findViewById(R.id.btn_drawer_forward);
        btnMic = (Button) findViewById(R.id.voice_icon);
        Button btn_mic_icon = (Button) findViewById(R.id.btn_mic_icon);
        final Button btnsend = (Button) findViewById(R.id.btn_send);

        btnCancelSend = (Button) findViewById(R.id.btn_cancel_send);
        btnCancelSend.setTypeface(G.fontAwesome);

        imgPicRecord = (ImageView) findViewById(R.id.img_pic_record);
        useravatar = (ImageView) findViewById(R.id.user_avatar);

        LinearLayout lytDeleteChat = (LinearLayout) findViewById(R.id.lyt_delete_chat);
        Button btnDeleteChat = (Button) findViewById(R.id.btn_delete_chat);

        try {
            if (active.equals("2")) {
                layout1.setVisibility(View.GONE);
                lytDeleteChat.setVisibility(View.VISIBLE);
            }
        }
        catch (Exception e1) {

            e1.printStackTrace();
        }

        btnDeleteChat.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                G.cmd.deletechatrooms(chatroomid);
                sendchatMessageToActivity(Singlechat.this);
                finish();
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

        llVoice = (LinearLayout) findViewById(R.id.ll_voice);

        edtchat = (EditText) findViewById(R.id.edt_chat);

        String mess = G.cmd.getLastMessage(chatroomid, "1");
        if (mess.length() > 0) {
            PageMessagingPopularFunction pm = new PageMessagingPopularFunction();
            edtchat.setText(pm.parseText(mess, "CHAT_LIST", false, Singlechat.this, 0));
        }

        singlechatlist = (RecyclerView) findViewById(R.id.singlechatlist);
        singlechatlist.setHasFixedSize(true);
        singlechatlist.setItemViewCacheSize(5);
        mLayoutManager = new LinearLayoutManager(Singlechat.this);
        singlechatlist.setLayoutManager(mLayoutManager);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mSlidingTabLayout = (SlidingTabLayoutemoji) findViewById(R.id.sliding_tabs);

        btnDrawerTrash.setTypeface(G.fontAwesome);
        backIcon.setTypeface(G.fontAwesome);
        btnDrawerForward.setTypeface(G.fontAwesome);
        btnDrawerFiles.setTypeface(G.fontAwesome);
        btnDrawerReplay.setTypeface(G.fontAwesome);
        smaileIcon.setTypeface(G.fontAwesome);
        attachIcon.setTypeface(G.fontAwesome);
        navIcon.setTypeface(G.fontAwesome);
        btnMic.setTypeface(G.fontAwesome);
        btn_mic_icon.setTypeface(G.fontAwesome);
        btnsend.setTypeface(G.fontAwesome);

        usernametx.setText(userchatname);

        mViewPager.setAdapter(new CustomPagerAdapter(this, edtchat));
        mSlidingTabLayout.setViewPager(mViewPager);
        LayoutParams params = mLayout_smile.getLayoutParams();
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        params.height = (display.getHeight() * 3) / 10;
        mLayout_smile.setLayoutParams(params);

        singlechatlist.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                loadBottomAllow = true;
                return false;
            }
        });

        btnMic.setOnLongClickListener(new OnLongClickListener() {

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
                G.mainUserChat = "";
                G.visibleUserChat = "";
                View view = Singlechat.this.getCurrentFocus();
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
                if ( !isigap) {
                    Intent intent = new Intent(Singlechat.this, UserProfile.class);
                    intent.putExtra("userchatname", userchatname);
                    intent.putExtra("userchat", userchat);
                    intent.putExtra("userchatavatar", userchatavatar);
                    intent.putExtra("chatroomid", chatroomid);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

            }
        });
        lluserinfo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if ( !isigap) {
                    Intent intent = new Intent(Singlechat.this, UserProfile.class);
                    intent.putExtra("userchatname", userchatname);
                    intent.putExtra("userchat", userchat);
                    intent.putExtra("userchatavatar", userchatavatar);
                    intent.putExtra("chatroomid", chatroomid);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

            }
        });
        attachIcon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                initDialog();

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
        edtchat.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, final int start, int before, int count) {

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

                if (start == 0) {
                    try {
                        mService.setchatstate("1", userchat);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (runnable != null) {

                    handler2.removeCallbacks(runnable);
                }
                runnable = new Runnable() {

                    @Override
                    public void run() {

                        mService.setchatstate("5", userchat);
                    }
                };
                handler2.postDelayed(runnable, 3000);
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
                                                        lytReplay.setVisibility(View.VISIBLE);
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
                            lytReplay.setVisibility(View.VISIBLE);
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
                    mService.setchatstate("5", userchat);
                }
                singlechatlist.scrollToPosition(mAdapter.getItemCount() - 1);
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

                    chatmsg = edtchat.getText().toString();

                    if (chatmsg != null && !chatmsg.isEmpty() && !chatmsg.equals("null") && !chatmsg.equals("")) {

                        if (smileOpen) {

                            closeLayoutSmile();
                            smileOpen = false;
                        }

                        edtchat.getText().clear();

                        mService.SendMessageOrder(filemime, userchat, chatmsg, type, null, null, null, replymessage, replyfilehash, replyfrom, "0", null);

                        lytReplay.setVisibility(View.GONE);
                        replymessage = "";
                        replyfilehash = "";
                        replyfrom = "";

                    } else {
                        Toast.makeText(Singlechat.this, getString(R.string.your_message_is_empty_en), Toast.LENGTH_SHORT).show();
                    }
                } else {

                    chatmsg = edtchat.getText().toString();

                    if (chatmsg != null && !chatmsg.isEmpty() && !chatmsg.equals("null") && !chatmsg.equals("")) {

                        if (smileOpen) {

                            closeLayoutSmile();
                            smileOpen = false;
                        }

                        if (closingkeybord() == true) {

                            mService.SendMessageOrder(filemime, userchat, chatmsg, type, null, null, null, replymessage, replyfilehash, replyfrom, "0", null);

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
                            String currenttime, filepathh = "", status;
                            status = G.cmd.getfile(4, filehash);
                            try {

                                currenttime = ts.getDateTime();
                            }
                            catch (Exception e) {
                                currenttime = G.helperGetTime.getTime();
                            }

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
                            String id = G.cmd.addchathistory(filemime, chatroomid, chatmsg, status, currenttime, "2", null, type, filehash, fileurl, filethumbnail, replymessage, replyfilehash, replyfrom);
                            mAdapter.newPost(filemime, id, chatmsg, "0", currenttime, "2", null, filehash, replyfilehash, replymessage, replyfrom, type);
                            mService.SendMessageOrder(filemime, userchat, msg, type, filehash, fileurl, filethumbnail, replymessage, replyfilehash, replyfrom, "0", null);
                        } else {
                            mService.SendMessageOrder(filemime, userchat, msg, type, filehash, fileurl, filethumbnail, replymessage, replyfilehash, replyfrom, "0", null);
                        }
                    }
                    cursor.close();
                    lytReplay.setVisibility(View.GONE);
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
                        Toast.makeText(Singlechat.this, getString(R.string.Compelete_Sound_en), Toast.LENGTH_SHORT).show();
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
                if (selectedItem.size() != chatidarray.size()) {
                    int ekhtelaf = chatidarray.size() - selectedItem.size();
                    for (int i = 0; i < ekhtelaf; i++) {
                        selectedItem.add(false);
                    }
                }

                mAdapter.notifyDataSetChanged();

                singlechatlist.scrollToPosition(mAdapter.getItemCount() - 1);

                softKeyboard.openSoftKeyboard();
                edtchat.requestFocus();
                if (smileOpen) {
                    edtchat.getText().insert((edtchat.getText().length()), " ");

                    edtchat.setSelection(edtchat.getText().length());

                    closeLayoutSmile();
                    smileOpen = false;
                }

                handlerdd = new Handler();
                handlerdd.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        singlechatlist.scrollToPosition(mAdapter.getItemCount() - 1);

                    }
                }, 500);

                return false;
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
                if (selectedItem.size() != chatidarray.size()) {
                    int ekhtelaf = chatidarray.size() - selectedItem.size();
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

                String from;
                G.cmd.clearmessagecash();
                for (int i = 0; i < selectedItem.size(); i++) {

                    if (selectedItem.get(i) == true) {

                        if (msgtypearray.get(i).equals("1")) {
                            from = userchatname;
                        } else {
                            from = G.username;
                        }

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
                        G.cmd.AddMessagecash(from, msgarray.get(i), filehasharray.get(i), fileurl, filethumbnail, typearray.get(i), filemimearray.get(i));
                    }
                }
                finish();
            }
        });

        usernaveclose.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                lytReplay.setVisibility(View.GONE);
                G.cmd.clearmessagecash();

            }
        });
        setuserimage();

        btnDrawerReplay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                lytReplay.setVisibility(View.VISIBLE);
                for (int i = 0; i < selectedItem.size(); i++) {

                    if (selectedItem.get(i) == true) {

                        replymessage = msgarray.get(i);
                        replyfilehash = filehasharray.get(i);

                        if (msgtypearray.get(i).equals("2")) {
                            replyfrom = G.username;
                        } else {
                            replyfrom = userchatname;
                        }
                        buildertext = parseText(replymessage, false);
                        txtforwardfrom.setText(replyfrom);
                        txtforwardmsg.setText(buildertext, BufferType.SPANNABLE);
                        softKeyboard.openSoftKeyboard();
                        edtchat.requestFocus();
                    }
                }

                UnVisibleDrawer();
                selectedItem.clear();
                if (selectedItem.size() != chatidarray.size()) {
                    int ekhtelaf = chatidarray.size() - selectedItem.size();
                    for (int i = 0; i < ekhtelaf; i++) {
                        selectedItem.add(false);
                    }
                }
                loadchathistory();
            }
        });

        iscontact = G.cmd.iscontact(mobile);

        if (iscontact != 0) {
            ll_iscontact.setVisibility(View.GONE);
        }
        if (isigap == true) {
            layout1.setVisibility(View.GONE);
            ll_iscontact.setVisibility(View.GONE);
            lytReplay.setVisibility(View.GONE);
        }
        loadchathistory();
        LocalBroadcastManager.getInstance(Singlechat.this).registerReceiver(paintrequestbroad, new IntentFilter("paintrequestbroad"));
        LocalBroadcastManager.getInstance(Singlechat.this).registerReceiver(mMessageReceiver, new IntentFilter("loadchathistory"));
        LocalBroadcastManager.getInstance(Singlechat.this).registerReceiver(mMessageReceiver1, new IntentFilter("setuserstate"));
        LocalBroadcastManager.getInstance(Singlechat.this).registerReceiver(newPostSingleChat, new IntentFilter("newPostSingleChat"));
        LocalBroadcastManager.getInstance(Singlechat.this).registerReceiver(updateStatusfile, new IntentFilter("updateStatusfileSingleChat"));
        LocalBroadcastManager.getInstance(Singlechat.this).registerReceiver(updateSeenSingleChat, new IntentFilter("updateSeenSingleChat"));
        LocalBroadcastManager.getInstance(Singlechat.this).registerReceiver(UpdateFileMessageIDSingleChat, new IntentFilter("UpdateFileMessageIDSingleChat"));
        LocalBroadcastManager.getInstance(Singlechat.this).registerReceiver(UpdateMessageIDSingleChat, new IntentFilter("UpdateMessageIDSingleChat"));
        LocalBroadcastManager.getInstance(Singlechat.this).registerReceiver(sendNewPostSingleChat, new IntentFilter("sendNewPostSingleChat"));
        LocalBroadcastManager.getInstance(Singlechat.this).registerReceiver(UpdateLastSeenSingleChat, new IntentFilter("UpdateLastSeenSingleChat"));
        LocalBroadcastManager.getInstance(Singlechat.this).registerReceiver(updateViewAfterResendSingleChat, new IntentFilter("updateViewAfterResendSingleChat"));
        LocalBroadcastManager.getInstance(Singlechat.this).registerReceiver(newContact, new IntentFilter("newContact"));
        LocalBroadcastManager.getInstance(Singlechat.this).registerReceiver(LoadSingleChat, new IntentFilter("LoadSingleChat"));
        LocalBroadcastManager.getInstance(Singlechat.this).registerReceiver(hashSearchSingleChat, new IntentFilter("hashSearchSingleChat"));
        LocalBroadcastManager.getInstance(Singlechat.this).registerReceiver(changeHeaderSingleChat, new IntentFilter("changeHeaderSingleChat"));
        LocalBroadcastManager.getInstance(Singlechat.this).registerReceiver(LoadBottomSingleChat, new IntentFilter("LoadBottomSingleChat"));

        Handler h = new Handler();
        h.postDelayed(new Runnable() {

            @Override
            public void run() {
                G.visibleUserChat = userchat;
            }
        }, 1000);

        if (HelperGetDataFromOtherApp.hasSharedData) {
            HelperGetDataFromOtherApp.hasSharedData = false;

            G.HANDLER.postDelayed(new Runnable() { // set delay to prepare createchattest

                @Override
                public void run() {

                    if (HelperGetDataFromOtherApp.messageType == FileType.message) {

                        type = "1";
                        chatmsg = HelperGetDataFromOtherApp.message;

                        mService.SendMessageOrder(filemime, userchat, chatmsg, type, null, null, null, replymessage, replyfilehash, replyfrom, "0", null);

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


    private void counter() {
        Handler h = new Handler();
        h.postDelayed(new Runnable() {

            @Override
            public void run() {

                firsttime = true;
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                    Toast.makeText(Singlechat.this, "No response . . .", Toast.LENGTH_SHORT).show();
                }
            }
        }, 10000);
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

        int newPos = chatidarray.indexOf(firstNewHashtakID);
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


    private void getlastseeninfo(final String mobile) {

        new Runnable() {

            @Override
            public void run() {

                JSONArray ja = new JSONArray();
                ja.put(mobile);
                mService.getlastseen(ja, "Singlechat");
                G.HANDLER.postDelayed(this, 10000);
            }
        }.run();
    }

    private BroadcastReceiver paintrequestbroad               = new BroadcastReceiver() {

                                                                  @Override
                                                                  public void onReceive(Context context, Intent intent) {
                                                                      if (showPaintAlert) {
                                                                          showPaintAlert = false;

                                                                          if (isdouble) {

                                                                              if (firsttime) {

                                                                                  showdialog(new OnColorChangedListenerSelect() {

                                                                                      @Override
                                                                                      public void colorChanged(String key, int color) {

                                                                                      }


                                                                                      @Override
                                                                                      public void Confirmation(Boolean result) {
                                                                                          if (result) {

                                                                                              if (firsttime) {
                                                                                                  mService.sendpaintrequest();
                                                                                              }
                                                                                              Intent intent = new Intent(Singlechat.this, multyMyPaint.class);
                                                                                              startActivity(intent);

                                                                                          }
                                                                                      }
                                                                                  });

                                                                              } else {
                                                                                  if (pDialog != null && pDialog.isShowing()) {
                                                                                      pDialog.dismiss();
                                                                                  }
                                                                                  Intent intent1 = new Intent(Singlechat.this, multyMyPaint.class);
                                                                                  startActivity(intent1);
                                                                              }

                                                                          }
                                                                          isdouble = false;
                                                                          Handler handler = new Handler();
                                                                          handler.postDelayed(new Runnable() {

                                                                              @Override
                                                                              public void run() {
                                                                                  isdouble = true;
                                                                              }
                                                                          }, 1000);
                                                                      }

                                                                  }
                                                              };

    private BroadcastReceiver LoadBottomSingleChat            = new BroadcastReceiver() {

                                                                  @Override
                                                                  public void onReceive(Context context, Intent intent) {
                                                                      if (hashtakSearch) {
                                                                          hashtakBeforeScrollBottom = chatidarray.get(lastHighlightPosition); // idye balatarin hashtak ra ghabl az load list migirim 
                                                                      }

                                                                      if (loadBottomAllow) {
                                                                          loadchathistoryToBottom();
                                                                      }
                                                                  }
                                                              };

    private BroadcastReceiver changeHeaderSingleChat          = new BroadcastReceiver() {

                                                                  @Override
                                                                  public void onReceive(Context context, Intent intent) {
                                                                      changeHeader();
                                                                  }
                                                              };
    private BroadcastReceiver hashSearchSingleChat            = new BroadcastReceiver() {

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

    private BroadcastReceiver LoadSingleChat                  = new BroadcastReceiver() {

                                                                  @Override
                                                                  public void onReceive(Context context, Intent intent) {
                                                                      scrollToOffset = true;
                                                                      loadchathistory();

                                                                  }
                                                              };

    private BroadcastReceiver newContact                      = new BroadcastReceiver() {

                                                                  @Override
                                                                  public void onReceive(Context context, Intent intent) {

                                                                      final String name = intent.getStringExtra("name");
                                                                      userchatname = name;
                                                                      runOnUiThread(new Runnable() {

                                                                          @Override
                                                                          public void run() {
                                                                              usernametx.setText(name);
                                                                          }
                                                                      });

                                                                  }
                                                              };

    private BroadcastReceiver updateViewAfterResendSingleChat = new BroadcastReceiver() {

                                                                  @Override
                                                                  public void onReceive(Context context, Intent intent) {

                                                                      loadchathistory();
                                                                  }
                                                              };

    private BroadcastReceiver UpdateLastSeenSingleChat        = new BroadcastReceiver() {

                                                                  @Override
                                                                  public void onReceive(Context context, Intent intent) {

                                                                      String lastSeen = intent.getStringExtra("lastSeen");
                                                                      try {
                                                                          JSONObject jo = new JSONObject(lastSeen);

                                                                          String lasts = jo.get(mobile).toString();

                                                                          lasts = HelperGetTime.getStringTime(lasts, G.cmd);

                                                                          userlasttimetx.setText(lasts);
                                                                          G.cmd.setLastSeen(mobile, lasts);

                                                                      }
                                                                      catch (JSONException e) {

                                                                          e.printStackTrace();
                                                                      }

                                                                  }
                                                              };

    private BroadcastReceiver sendNewPostSingleChat           = new BroadcastReceiver() {

                                                                  @Override
                                                                  public void onReceive(Context context, Intent intent) {

                                                                      String id = intent.getStringExtra("id");
                                                                      String msg = intent.getStringExtra("msg");
                                                                      String status = intent.getStringExtra("status");
                                                                      String msgtime = intent.getStringExtra("msgtime");
                                                                      String msgtype = intent.getStringExtra("msgtype");
                                                                      String msgid = intent.getStringExtra("msgid");
                                                                      String filehash = intent.getStringExtra("filehash");
                                                                      String replyfilehash = intent.getStringExtra("replyfilehash");
                                                                      String replymessage = intent.getStringExtra("replymessage");
                                                                      String replyfrom = intent.getStringExtra("replyfrom");
                                                                      String type = intent.getStringExtra("type");
                                                                      String filemim = intent.getStringExtra("filemime");

                                                                      mAdapter.newPost(filemim, id, msg, status, msgtime, msgtype, msgid, filehash, replyfilehash, replymessage, replyfrom, type);
                                                                      singlechatlist.scrollToPosition(mAdapter.getItemCount() - 1);

                                                                      int getItemCount = ((LinearLayoutManager) mLayoutManager).getItemCount();
                                                                      int getChildCount = ((LinearLayoutManager) mLayoutManager).getChildCount();

                                                                      if (getItemCount == (getChildCount + 1)) { // agar item haye mojud kamtar az andazeye safhe bud
                                                                          //((LinearLayoutManager) mLayoutManager).setStackFromEnd(false); // enteghal be entehaye safhe anjam nashavad
                                                                      } else {
                                                                          ((LinearLayoutManager) mLayoutManager).setStackFromEnd(true); // enteghal be entehaye safhe
                                                                      }
                                                                  }
                                                              };

    private BroadcastReceiver newPostSingleChat               = new BroadcastReceiver() {

                                                                  @Override
                                                                  public void onReceive(Context context, Intent intent) {

                                                                      String id = intent.getStringExtra("id");
                                                                      String msg = intent.getStringExtra("msg");
                                                                      String status = intent.getStringExtra("status");
                                                                      String msgtime = intent.getStringExtra("msgtime");
                                                                      String msgtype = intent.getStringExtra("msgtype");
                                                                      String msgid = intent.getStringExtra("msgid");
                                                                      String filehash = intent.getStringExtra("filehash");
                                                                      String replyfilehash = intent.getStringExtra("replyfilehash");
                                                                      String replymessage = intent.getStringExtra("replymessage");
                                                                      String replyfrom = intent.getStringExtra("replyfrom");
                                                                      String type = intent.getStringExtra("type");
                                                                      String filemim = intent.getStringExtra("filemime");
                                                                      G.cmd.updatemsgstatus(msgid, "5");
                                                                      mService.sendseen(userchat, msgid);

                                                                      mAdapter.newPost(filemim, id, msg, status, msgtime, msgtype, msgid, filehash, replyfilehash, replymessage, replyfrom, type);
                                                                      singlechatlist.scrollToPosition(mAdapter.getItemCount() - 1);

                                                                      int getItemCount = ((LinearLayoutManager) mLayoutManager).getItemCount();
                                                                      int getChildCount = ((LinearLayoutManager) mLayoutManager).getChildCount();

                                                                      if (getItemCount == (getChildCount + 1)) { // agar item haye mojud kamtar az andazeye safhe bud
                                                                          //((LinearLayoutManager) mLayoutManager).setStackFromEnd(false); // enteghal be entehaye safhe anjam nashavad
                                                                      } else {
                                                                          ((LinearLayoutManager) mLayoutManager).setStackFromEnd(true); // enteghal be entehaye safhe
                                                                      }
                                                                  }
                                                              };
    private BroadcastReceiver updateStatusfile                = new BroadcastReceiver() {

                                                                  @Override
                                                                  public void onReceive(Context context, Intent intent) {

                                                                      String filehash = intent.getStringExtra("filehash");
                                                                      String msgid = intent.getStringExtra("msgid");
                                                                      String status = intent.getStringExtra("status");
                                                                      mAdapter.updateStatusfile(filehash, msgid, status);
                                                                  }
                                                              };

    private BroadcastReceiver updateSeenSingleChat            = new BroadcastReceiver() {

                                                                  @Override
                                                                  public void onReceive(Context context, Intent intent) {

                                                                      String msgid = intent.getStringExtra("msgid");
                                                                      String status = intent.getStringExtra("status");
                                                                      mAdapter.updateSeen(msgid, status);
                                                                  }
                                                              };

    private BroadcastReceiver UpdateMessageIDSingleChat       = new BroadcastReceiver() {

                                                                  @Override
                                                                  public void onReceive(Context context, Intent intent) {

                                                                      String newMsgID = intent.getStringExtra("newMsgID");
                                                                      String oldMsgID = intent.getStringExtra("oldMsgID");
                                                                      mAdapter.updateMessageID(newMsgID, oldMsgID);
                                                                  }
                                                              };
    private BroadcastReceiver UpdateFileMessageIDSingleChat   = new BroadcastReceiver() {

                                                                  @Override
                                                                  public void onReceive(Context context, Intent intent) {

                                                                      String fileHash = intent.getStringExtra("fileHash");
                                                                      String newMsgID = intent.getStringExtra("newMsgID");
                                                                      mAdapter.updateFileMessageID(fileHash, newMsgID);
                                                                  }
                                                              };


    private void addContactForBlock(String mobile) {

        String DisplayName = mobile;
        String MobileNumber = mobile;

        if (DisplayName != null & !DisplayName.equals("")) {

            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

            ops.add(ContentProviderOperation.newInsert(
                    ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                    .build());

            //------------------------------------------------------ Names
            if (DisplayName != null) {
                ops.add(ContentProviderOperation.newInsert(
                        ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, DisplayName).build());
            }

            //------------------------------------------------------ Mobile Number                     
            if (MobileNumber != null) {
                ops.add(ContentProviderOperation.
                        newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, MobileNumber)
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                        .build());
            }

            try {
                getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);

                new BlockContact().execute(mobile, mobile);
                ll_iscontact.setVisibility(View.GONE);
            }
            catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(Singlechat.this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    private boolean closingkeybord() {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                edtchat.getText().clear();
                edtchat.clearFocus();
                View view = Singlechat.this.getCurrentFocus();
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
        if (userchatavatar != null && !userchatavatar.isEmpty() && !userchatavatar.equals("null") && !userchatavatar.equals("") && !userchatavatar.equals("empty")) {
            int loader = R.drawable.difaultimage;
            il.DisplayImage(userchatavatar, loader, useravatar);
        } else {
            HelperDrawAlphabet pf = new HelperDrawAlphabet();
            Bitmap bm = pf.drawAlphabet(Singlechat.this, userchatname, useravatar);
            useravatar.setImageBitmap(bm);
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
                mAdapter.changeHeader();
            }

            if (((LinearLayout) findViewById(R.id.layout_add_text)).getVisibility() == View.VISIBLE) {
                ((Button) findViewById(R.id.btn_send_file)).performClick();
                return;
            }

            type = "1";
            if (messagecash == 0) {

                chatmsg = edtchat.getText().toString();

                if (chatmsg != null && !chatmsg.isEmpty() && !chatmsg.equals("null") && !chatmsg.equals("")) {

                    if (smileOpen) {

                        closeLayoutSmile();
                        smileOpen = false;
                    }

                    edtchat.getText().clear();
                    mService.SendMessageOrder(filemime, userchat, chatmsg, type, null, null, null, replymessage, replyfilehash, replyfrom, "0", null);

                    lytReplay.setVisibility(View.GONE);
                    replymessage = "";
                    replyfilehash = "";
                    replyfrom = "";

                } else {
                    Toast.makeText(Singlechat.this, getString(R.string.your_message_is_empty_en), Toast.LENGTH_SHORT).show();
                }
            } else {

                chatmsg = edtchat.getText().toString();

                if (chatmsg != null && !chatmsg.isEmpty() && !chatmsg.equals("null") && !chatmsg.equals("")) {

                    if (smileOpen) {

                        closeLayoutSmile();
                        smileOpen = false;
                    }

                    if (closingkeybord() == true) {

                        mService.SendMessageOrder(filemime, userchat, chatmsg, type, null, null, null, replymessage, replyfilehash, replyfrom, "0", null);

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
                        String currenttime, filepathh = "", status;
                        status = G.cmd.getfile(4, filehash);
                        try {

                            currenttime = ts.getDateTime();
                        }
                        catch (Exception e) {
                            currenttime = G.helperGetTime.getTime();
                        }

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
                        String id = G.cmd.addchathistory(filemime, chatroomid, chatmsg, status, currenttime, "2", null, type, filehash, fileurl, filethumbnail, replymessage, replyfilehash, replyfrom);
                        mAdapter.newPost(filemime, id, chatmsg, "0", currenttime, "2", null, filehash, replyfilehash, replymessage, replyfrom, type);
                        mService.SendMessageOrder(filemime, userchat, msg, type, filehash, fileurl, filethumbnail, replymessage, replyfilehash, replyfrom, "0", null);
                    } else {
                        mService.SendMessageOrder(filemime, userchat, msg, type, filehash, fileurl, filethumbnail, replymessage, replyfilehash, replyfrom, "0", null);
                    }
                }
                cursor.close();
                lytReplay.setVisibility(View.GONE);
                G.cmd.clearmessagecash();
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        smileClicked = false;
        selectItemVisible = false;
        if (keyCode == KeyEvent.KEYCODE_BACK && selectCounter > 0) {

            UnVisibleDrawer();
            selectedItem.clear();
            if (selectedItem.size() != chatidarray.size()) {
                int ekhtelaf = chatidarray.size() - selectedItem.size();
                for (int i = 0; i < ekhtelaf; i++) {
                    selectedItem.add(false);
                }
            }
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


    private SpannableStringBuilder parseText(String text, Boolean withLink) {
        PageMessagingPopularFunction pm = new PageMessagingPopularFunction();
        SpannableStringBuilder builder = pm.parseText(text, "CHAT_LIST", withLink, Singlechat.this, 0);
        return builder;
    }


    private void openLayoutSmile() {

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                mLayout_smile.setVisibility(LinearLayout.VISIBLE);
                llVoice.setVisibility(View.GONE);
            }
        }, 250);

    }


    private void closeLayoutSmile() {
        mLayout_smile.setVisibility(LinearLayout.GONE);
        llVoice.setVisibility(View.VISIBLE);
    }


    @Override
    public void onBackPressed() {

        if (G.longPressItem) {
            G.longPressItem = false;
            mAdapter.invisibleDrawer();
        } else {
            smileClicked = false;

            if (mLayout_smile.getVisibility() == View.VISIBLE) {
                smileOpen = false;
                closeLayoutSmile();
                return;
            }
            G.mainUserChat = "";
            G.visibleUserChat = "";

            G.cmd.setLastMessage(chatroomid, "1", edtchat.getText().toString());

            if (G.hashtakSearch) {
                mAdapter.resethighlight();
                mAdapter.changeHeader();
                return;
            }

            super.onBackPressed();
        }
    }


    private void setuserstate(String mode) {

        if (mode.equals("1")) {
            userlasttimetx.setText(getString(R.string.typing_en));
        } else if (mode.equals("2")) {
            userlasttimetx.setText(getString(R.string.recording_voice_en));
        } else if (mode.equals("3")) {
            userlasttimetx.setText(getString(R.string.sending_photo_en));
        } else if (mode.equals("4")) {
            userlasttimetx.setText(getString(R.string.sending_file_en));
        } else {
            userlasttimetx.setText(G.cmd.getLastSeen(userchat.split("@")[0]));
        }
    }


    private void loadchathistoryHashtak(int value, String firstNewHashtakID) {
        String hashtakOffset = (value - 30) + "";

        if (chatroomid != null && !chatroomid.isEmpty() && !chatroomid.equals("null") && !chatroomid.equals("")) {} else {
            try {
                chatroomid = G.cmd.namayeshchatroomid(userchat);
            }
            catch (Exception e) {}
        }
        int size = G.cmd.getchathistoryRowCount("chathistory", chatroomid);

        if (size != 0) {

            clearArray();

            singlechatlist.setVisibility(View.VISIBLE);

            int count = G.cmd.selectHistorySize("chathistory", "chatroom_id = '" + chatroomid + "'");

            Cursor cursorSingleChat = G.cmd.selectLimitOffset("chathistory", "chatroom_id = '" + chatroomid + "'", count + "", hashtakOffset);
            readCursor(cursorSingleChat);

            if (selectedItem.size() != chatidarray.size()) {

                int ekhtelaf = chatidarray.size() - selectedItem.size();

                for (int i = 0; i < ekhtelaf; i++) {
                    selectedItem.add(false);
                }
            }
            if (inActivity == true) {
                sendseen();
            }

            newPos = chatidarray.indexOf(firstNewHashtakID);
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
                }, 3000);

                mAdapter = new SingleChatRecycleAdapter(filemimearray, chatidarray, msgarray, statusarray, msgtimearray, msgtypearray, msgidarray, selectedItem, typearray, filehasharray, replyfilehasharray, replymessagearray, replyfromarray, runUploaderThread, runDownloaderThread, runDownloaderListener, runUploaderListener, Singlechat.this,
                        year, month, day, il, view, dm, dmDialog, handler, replymessage, replyfilehash, replyfrom,
                        userchat, mService, selectItemVisible, btnDrawerReplay, txtDrawerCounter, sideDrawer, userchatname, chatroomid, smileClicked, countOffset, load, loadBottom, layout_music);
                singlechatlist.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                singlechatlist.scrollToPosition(newPos);
            }
        });
    }


    private void loadchathistory() {

        countOffset++;

        if (chatroomid != null && !chatroomid.isEmpty() && !chatroomid.equals("null") && !chatroomid.equals("")) {} else {
            try {
                chatroomid = G.cmd.namayeshchatroomid(userchat);
            }
            catch (Exception e) {}

        }
        int size = G.cmd.getchathistoryRowCount("chathistory", chatroomid);

        if (size != 0) {

            String firstID = "";

            if ( !firstLoad) {
                if (chatidarray.size() > 0) {
                    firstID = chatidarray.get(0);
                }
            }

            if (hashtakSearch) {
                lastHashtakID = chatidarray.get(lastHighlightPosition); // idye balatarin hashtak ra ghabl az load list migirim 
            }

            clearArray();

            singlechatlist.setVisibility(View.VISIBLE);

            String newMessagePosition = "5";
            boolean detectNewMessagePos = true;

            Cursor cursorSingleChat;

            if (firstLoad) {
                firstLoad = false;
                load = true;
                cursorSingleChat = G.cmd.selectHistoryByMessageID("chathistory", "chatroom_id = '" + chatroomid + "'", 30);
            } else {
                mAdapter.resetSelectedItem();

                int idOffset = G.cmd.selectOffsetOfIDFromTop("chathistory", "chatroom_id = '" + chatroomid + "'", firstID);

                String loadOffset = "0";
                load = false;
                if (idOffset - 30 > 0) {
                    loadOffset = (idOffset - 30) + "";
                    load = true;
                }

                cursorSingleChat = G.cmd.selectLimitOffset("chathistory", "chatroom_id = '" + chatroomid + "'", "-1", loadOffset + "");

            }

            while (cursorSingleChat.moveToNext()) {
                String chatid = cursorSingleChat.getString(cursorSingleChat.getColumnIndex("id"));
                String msgid = cursorSingleChat.getString(cursorSingleChat.getColumnIndex("msg_id"));
                String msg = cursorSingleChat.getString(cursorSingleChat.getColumnIndex("msg"));
                String status = cursorSingleChat.getString(cursorSingleChat.getColumnIndex("status"));
                String msgtime = cursorSingleChat.getString(cursorSingleChat.getColumnIndex("msg_time"));
                String msgtype = cursorSingleChat.getString(cursorSingleChat.getColumnIndex("msg_type"));
                String type = cursorSingleChat.getString(cursorSingleChat.getColumnIndex("type"));
                String filehash = cursorSingleChat.getString(cursorSingleChat.getColumnIndex("filehash"));
                String replyfilehash = cursorSingleChat.getString(cursorSingleChat.getColumnIndex("replyfilehash"));
                String replymessage = cursorSingleChat.getString(cursorSingleChat.getColumnIndex("replymessage"));
                String replyfrom = cursorSingleChat.getString(cursorSingleChat.getColumnIndex("replyfrom"));
                String filemime = cursorSingleChat.getString(cursorSingleChat.getColumnIndex("filemime"));

                if (detectNewMessagePos) {
                    if ( !status.equals("1") && !status.equals("2") && !status.equals("3")) { // agar ma ferestandeye payam nabudim
                        if ( !newMessagePosition.equals(status) && status.equals("4")) {
                            detectNewMessagePos = false;
                            newMsgPosition = cursorSingleChat.getPosition();
                        }
                        newMessagePosition = status;
                    }
                }

                chatidarray.add(chatid);
                msgarray.add(msg);
                statusarray.add(status);
                msgtimearray.add(msgtime);
                msgtypearray.add(msgtype);
                msgidarray.add(msgid);
                typearray.add(type);
                filehasharray.add(filehash);
                replyfilehasharray.add(replyfilehash);
                replymessagearray.add(replymessage);
                replyfromarray.add(replyfrom);
                runUploaderThread.add(true);
                runDownloaderThread.add(true);
                runDownloaderListener.add(true);
                runUploaderListener.add(true);
                filemimearray.add(filemime);

            }
            cursorSingleChat.close();

            if (selectedItem.size() != chatidarray.size()) {

                int ekhtelaf = chatidarray.size() - selectedItem.size();

                for (int i = 0; i < ekhtelaf; i++) {
                    selectedItem.add(false);
                }
            }

            if (inActivity == true) {
                sendseen();
            }

            beforOffset = chatidarray.indexOf(firstID); // ba avalin id mojud dar safhe ghable az load ba scroll makan payam ra migirim ta bad az load be haman makan beravim                

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
                }, 3000);

                mAdapter = new SingleChatRecycleAdapter(filemimearray, chatidarray, msgarray, statusarray, msgtimearray, msgtypearray, msgidarray, selectedItem, typearray, filehasharray, replyfilehasharray, replymessagearray, replyfromarray, runUploaderThread, runDownloaderThread, runDownloaderListener, runUploaderListener, Singlechat.this,
                        year, month, day, il, view, dm, dmDialog, handler, replymessage, replyfilehash, replyfrom,
                        userchat, mService, selectItemVisible, btnDrawerReplay, txtDrawerCounter, sideDrawer, userchatname, chatroomid, smileClicked, countOffset, load, loadBottom, layout_music);
                singlechatlist.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

                if (scrollToOffset) {
                    singlechatlist.scrollToPosition(beforOffset);
                } else {
                    if (newMsgPosition == -1) {
                        singlechatlist.scrollToPosition(mAdapter.getItemCount() - 1);
                    } else { // agar pyame jadid dasht be makane an payam miravad
                        singlechatlist.scrollToPosition(newMsgPosition);
                    }
                }

                if (hashtakSearch) {
                    reloadHashtakSearch(lastHashtakID);
                }
            }
        });
    }


    private void sendseen() {

        handler.post(new Runnable() {

            @Override
            public void run() {

                Cursor cursorUnread = G.cmd.selectUnreadMessage("chathistory", "chatroom_id ='" + chatroomid + "'");
                while (cursorUnread.moveToNext()) {
                    String msgID = cursorUnread.getString(cursorUnread.getColumnIndex("msg_id"));
                    String msgStatus = cursorUnread.getString(cursorUnread.getColumnIndex("status"));
                    String msgType = cursorUnread.getString(cursorUnread.getColumnIndex("msg_type"));

                    if (msgType.equals("1")) { // payamhaye daryafti
                        if ( !msgStatus.equals("5")) { // payamhaye nakhande (seen = 4 , unseen = 5)
                            if (msgID != null && !msgID.isEmpty() && !msgID.equals("null") && !msgID.equals("")) {
                                G.cmd.updatemsgstatus(msgID, "5");
                                mService.sendseen(userchat, msgID);
                            }
                        }
                    }
                }

                try {
                    if (userchat != null) {
                        Intent intentAll = new Intent("updateSeenAll");
                        intentAll.putExtra("MODEL", "1");
                        intentAll.putExtra("UID", userchat);
                        LocalBroadcastManager.getInstance(Singlechat.this).sendBroadcast(intentAll);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                HelperComputeUnread.unreadMessageCount();
            }
        });
    }


    private void loadchathistoryHashtakLimit(int value, String firstNewHashtakID) {
        String hashtakOffset = (value - 50) + "";

        if (chatroomid != null && !chatroomid.isEmpty() && !chatroomid.equals("null") && !chatroomid.equals("")) {} else {
            try {
                chatroomid = G.cmd.namayeshchatroomid(userchat);
            }
            catch (Exception e) {}
        }
        int size = G.cmd.getchathistoryRowCount("chathistory", chatroomid);

        if (size != 0) {

            clearArray();

            singlechatlist.setVisibility(View.VISIBLE);

            Cursor cursorSingleChat = G.cmd.selectLimitOffset("chathistory", "chatroom_id = '" + chatroomid + "'", 100 + "", hashtakOffset);
            readCursor(cursorSingleChat);

            if (selectedItem.size() != chatidarray.size()) {

                int ekhtelaf = chatidarray.size() - selectedItem.size();

                for (int i = 0; i < ekhtelaf; i++) {
                    selectedItem.add(false);
                }
            }
            if (inActivity == true) {
                sendseen();
            }

            newPos = chatidarray.indexOf(firstNewHashtakID);
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
                }, 3000);

                mAdapter = new SingleChatRecycleAdapter(filemimearray, chatidarray, msgarray, statusarray, msgtimearray, msgtypearray, msgidarray, selectedItem, typearray, filehasharray, replyfilehasharray, replymessagearray, replyfromarray, runUploaderThread, runDownloaderThread, runDownloaderListener, runUploaderListener, Singlechat.this,
                        year, month, day, il, view, dm, dmDialog, handler, replymessage, replyfilehash, replyfrom,
                        userchat, mService, selectItemVisible, btnDrawerReplay, txtDrawerCounter, sideDrawer, userchatname, chatroomid, smileClicked, countOffset, true, true, layout_music);
                singlechatlist.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                singlechatlist.scrollToPosition(newPos);
            }
        });
    }


    private void loadchathistoryToBottom() {

        if (hashtakSearch) {

            if (chatroomid != null && !chatroomid.isEmpty() && !chatroomid.equals("null") && !chatroomid.equals("")) {} else {
                try {
                    chatroomid = G.cmd.namayeshchatroomid(userchat);
                }
                catch (Exception e) {}

            }
            int size = G.cmd.getchathistoryRowCount("chathistory", chatroomid);

            if (size != 0) {

                singlechatlist.setVisibility(View.VISIBLE);

                Cursor cursorSingleChat;

                String lastListID = chatidarray.get(0);
                String lastListIDSetPos = chatidarray.get(chatidarray.size() - 1);

                int offsetOfLast = G.cmd.selectOffsetOfIDFromTop("chathistory", "chatroom_id = '" + chatroomid + "'", lastListID);

                clearArray();

                cursorSingleChat = G.cmd.selectLimitOffset("chathistory", "chatroom_id = '" + chatroomid + "'", "-1", offsetOfLast + "");
                readCursor(cursorSingleChat);

                if (selectedItem.size() != chatidarray.size()) {

                    int ekhtelaf = chatidarray.size() - selectedItem.size();

                    for (int i = 0; i < ekhtelaf; i++) {
                        selectedItem.add(false);
                    }
                }

                if (inActivity == true) {
                    sendseen();
                }

                newPosBottom = chatidarray.indexOf(lastListIDSetPos); // ba avalin id mojud dar safhe ghable az load ba scroll makan payam ra migirim ta bad az load be haman makan beravim                
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
                    }, 3000);

                    mAdapter = new SingleChatRecycleAdapter(filemimearray, chatidarray, msgarray, statusarray, msgtimearray, msgtypearray, msgidarray, selectedItem, typearray, filehasharray, replyfilehasharray, replymessagearray, replyfromarray, runUploaderThread, runDownloaderThread, runDownloaderListener, runUploaderListener, Singlechat.this,
                            year, month, day, il, view, dm, dmDialog, handler, replymessage, replyfilehash, replyfrom,
                            userchat, mService, selectItemVisible, btnDrawerReplay, txtDrawerCounter, sideDrawer, userchatname, chatroomid, smileClicked, countOffset, load, loadBottom, layout_music);
                    singlechatlist.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();

                    if (loadBottom) {
                        loadBottom = false;
                    }

                    singlechatlist.scrollToPosition(newPosBottom);

                    if (hashtakSearch) {
                        reloadHashtakSearch(hashtakBeforeScrollBottom);
                    }
                }
            });
        }
    }


    private void readCursor(Cursor cursorSingleChat) {
        while (cursorSingleChat.moveToNext()) {
            String chatid = cursorSingleChat.getString(cursorSingleChat.getColumnIndex("id"));
            String msgid = cursorSingleChat.getString(cursorSingleChat.getColumnIndex("msg_id"));
            String msg = cursorSingleChat.getString(cursorSingleChat.getColumnIndex("msg"));
            String status = cursorSingleChat.getString(cursorSingleChat.getColumnIndex("status"));
            String msgtime = cursorSingleChat.getString(cursorSingleChat.getColumnIndex("msg_time"));
            String msgtype = cursorSingleChat.getString(cursorSingleChat.getColumnIndex("msg_type"));
            String type = cursorSingleChat.getString(cursorSingleChat.getColumnIndex("type"));
            String filehash = cursorSingleChat.getString(cursorSingleChat.getColumnIndex("filehash"));
            String replyfilehash = cursorSingleChat.getString(cursorSingleChat.getColumnIndex("replyfilehash"));
            String replymessage = cursorSingleChat.getString(cursorSingleChat.getColumnIndex("replymessage"));
            String replyfrom = cursorSingleChat.getString(cursorSingleChat.getColumnIndex("replyfrom"));
            String filemime = cursorSingleChat.getString(cursorSingleChat.getColumnIndex("filemime"));

            chatidarray.add(chatid);
            msgarray.add(msg);
            statusarray.add(status);
            msgtimearray.add(msgtime);
            msgtypearray.add(msgtype);
            msgidarray.add(msgid);
            typearray.add(type);
            filehasharray.add(filehash);
            replyfilehasharray.add(replyfilehash);
            replymessagearray.add(replymessage);
            replyfromarray.add(replyfrom);
            runUploaderThread.add(true);
            runDownloaderThread.add(true);
            runDownloaderListener.add(true);
            runUploaderListener.add(true);
            filemimearray.add(filemime);

        }
        cursorSingleChat.close();
    }


    private void clearArray() {
        try {
            chatidarray.clear();
            msgarray.clear();
            statusarray.clear();
            msgtimearray.clear();
            msgtypearray.clear();
            typearray.clear();
            filehasharray.clear();
            msgidarray.clear();
            replyfilehasharray.clear();
            replymessagearray.clear();
            replyfromarray.clear();
            filemimearray.clear();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (popUp != null && popUp.isShowing()) {
            popUp.dismiss();
        }
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        if (dialogSelectMedia != null && dialogSelectMedia.isShowing()) {
            dialogSelectMedia.dismiss();
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

    private BroadcastReceiver mMessageReceiver  = new BroadcastReceiver() {

                                                    @Override
                                                    public void onReceive(Context context, Intent intent) {
                                                        loadchathistory();
                                                    }
                                                };
    private BroadcastReceiver mMessageReceiver1 = new BroadcastReceiver() {

                                                    @Override
                                                    public void onReceive(Context context, Intent intent) {
                                                        String message = intent.getStringExtra("mode");
                                                        setuserstate(message);
                                                    }
                                                };


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
            chatmsg = "";
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

            Log.i("LOG", "1");
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                JSONObject jsonobj = jParser.getJSONFromUrl(G.checkfileexist + fileHash, params, "GET", G.basicAuth, null);
                Log.i("LOG", "2");
                try {
                    String jsonstring = jsonobj.getString("json");
                    Log.i("LOG", "jsonstring : " + jsonstring);
                    JSONObject json = new JSONObject(jsonstring);
                    boolean success = json.getBoolean(G.TAG_SUCCESS);
                    Log.i("LOG", "3");
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
                                Toast.makeText(Singlechat.this, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
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

            String currenttime = G.helperGetTime.getTime();
            Log.i("LOG", "onPostExecute");

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
                    String id = G.cmd.addchathistory(fileMime, chatroomid, "", "0", currenttime, "2", null, fileType, fileHash, fileUrl, fileThumb, "", "", "");
                    mAdapter.newPost(fileMime, id, "", "0", currenttime, "2", null, fileHash, "", "", "", fileType);
                    mService.SendMessageOrder(fileMime, userchat, "", fileType, fileHash, fileUrl, fileThumb, "", "", "", "0", null);

                } else {
                    preparationFileUplaod(filePath, fileHash, fileMime, fileType);
                }
            }

            super.onPostExecute(result);
        }
    }


    private void preparationFileUplaod(String filePath, String fileHash, String fileMime, String fileType) {
        String currenttime = G.helperGetTime.getTime();

        G.cmd.Addfiles1(fileHash, null, null, "3", filePath);
        String id = G.cmd.addchathistory(fileMime, chatroomid, "", "0", currenttime, "2", null, fileType, fileHash, null, null, "", "", "");
        mAdapter.newPost(fileMime, id, "", "0", currenttime, "2", null, fileHash, "", "", "", fileType);
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
                    Toast.makeText(Singlechat.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            if (showDefaultImageCapture) {
                showDefaultImageCapture = false;
                try {
                    filePath = fileUri.getPath();
                    String filename = mediaFile.getName();
                    chatmsg = "";
                    try {
                        getsha(filePath, filename, true);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                catch (Exception e) {
                    Toast.makeText(Singlechat.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
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
                    chatmsg = "";
                    try {
                        getsha(filePath, imageFileName, true);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

                case G.CAMERA_PIC_REQUEST:

                    if (G.crop.equals("0")) { // disable

                        type = "2";

                        try {
                            filePath = fileUri.getPath();
                            String filename = mediaFile.getName();
                            chatmsg = "";
                            try {
                                getsha(filePath, filename, true);
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        catch (Exception e) {
                            Toast.makeText(Singlechat.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
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
                        if (G.crop.equals("0")) { // disable

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
                                        Toast.makeText(Singlechat.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(Singlechat.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            });

                            if (needCrop) { // agar tasvir gif nabashad
                                chatmsg = "";
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
                                            Toast.makeText(Singlechat.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }
                                });
                            }
                        }

                    } else if (selectedImageUri.toString().contains("video")) {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                type = "3";
                                try {
                                    geturi(data, false);
                                }
                                catch (Exception e) {
                                    Toast.makeText(Singlechat.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        });
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
                                Toast.makeText(Singlechat.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(Singlechat.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
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
                                Uri uri = data.getData();
                                File myFile = new File(uri.getPath());
                                filePath = myFile.getAbsolutePath();
                                File imgFile = new File(filePath);
                                int index = imgFile.getName().lastIndexOf('.') + 1;
                                filemime = imgFile.getName().substring(index).toLowerCase();
                                String nameOfFile = myFile.getName();
                                chatmsg = "";

                                try {
                                    getsha(filePath, nameOfFile, false);
                                }
                                catch (Exception e) {

                                    e.printStackTrace();
                                }
                            }
                            catch (Exception e) {
                                Toast.makeText(Singlechat.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(Singlechat.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
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
                            chatmsg = "Contact Information : " + "\n" + "Name : " + name + "\n" + "Number : " + number;
                            mService.SendMessageOrder(filemime, userchat, chatmsg, type, null, null, null, replymessage, replyfilehash, replyfrom, "0", null);
                            lytReplay.setVisibility(View.GONE);
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
                                chatmsg = "";
                                try {
                                    getsha(filePath, filename, false);
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            catch (Exception e) {
                                Toast.makeText(Singlechat.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
                                return;
                            }

                        }
                    });
                    break;

                default:
                    break;
            }

        } else {
            if (mService != null) {
                mService.setchatstate("5", userchat);
            }
        }
    }


    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }


    private static File getOutputMediaFile(int type) {

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Config.IMAGE_DIRECTORY_NAME);
        if ( !mediaStorageDir.exists()) {
            if ( !mediaStorageDir.mkdirs()) {
                return null;
            }
        }
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
                JSONObject jsonobj = jParser.getJSONFromUrl(G.checkfileexist + filehash, params, "GET", G.basicAuth, null);

                try {
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
                                //filemime = result.getString("mime");
                                //fileextension = result.getString("extension");
                                filethumb = c.getString("thumbnailLq");
                            }
                        }
                    } else {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(Singlechat.this, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
                catch (JSONException e) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(Singlechat.this, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            catch (Exception e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(Singlechat.this, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            return resend;
        }


        @Override
        protected void onPostExecute(String result) {

            String currenttime;
            try {

                currenttime = ts.getDateTime();
            }
            catch (Exception e) {
                currenttime = G.helperGetTime.getTime();
            }

            if (result.equals("1")) { // Resend

                int exist = G.cmd.isfileinfoexist(filehash);
                if (exist == 0) {
                    if (exists == true) {
                        G.cmd.Addfiles1(filehash, fileurl, filethumb, "5", filePath);
                        String id = G.cmd.addchathistory(filemime, chatroomid, chatmsg, "0", currenttime, "2", null, type, filehash, fileurl, filethumb, replymessage, replyfilehash, replyfrom);
                        mAdapter.newPost(filemime, id, chatmsg, "0", currenttime, "2", null, filehash, replyfilehash, replymessage, replyfrom, type);
                        mService.SendMessageOrder(filemime, userchat, chatmsg, type, filehash, fileurl, filethumb, replymessage, replyfilehash, replyfrom, "0", null);
                        lytReplay.setVisibility(View.GONE);
                        replymessage = "";
                        replyfilehash = "";
                        replyfrom = "";
                    } else {
                        alertdialogupfile();
                    }
                } else {
                    if (exists == true) {
                        G.cmd.updatefilestatus(filehash, 5);
                        mService.SendMessageOrder(filemime, userchat, chatmsg, type, filehash, fileurl, filethumb, replymessage, replyfilehash, replyfrom, "1", null);
                    } else {
                        G.cmd.updatefilestatus(filehash, 3);
                        loadchathistory();
                    }
                }

            } else {
                if (exists == true) {
                    G.cmd.Addfiles1(filehash, fileurl, filethumb, "5", filePath);
                    String id = G.cmd.addchathistory(filemime, chatroomid, chatmsg, "0", currenttime, "2", null, type, filehash, fileurl, filethumb, replymessage, replyfilehash, replyfrom);
                    mAdapter.newPost(filemime, id, chatmsg, "0", currenttime, "2", null, filehash, replyfilehash, replymessage, replyfrom, type);
                    mService.SendMessageOrder(filemime, userchat, chatmsg, type, filehash, fileurl, filethumb, replymessage, replyfilehash, replyfrom, "0", null);
                    lytReplay.setVisibility(View.GONE);
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
        String currenttime;
        try {
            currenttime = ts.getDateTime();
        }
        catch (Exception e) {
            currenttime = G.helperGetTime.getTime();
        }
        G.cmd.Addfiles1(filehash, null, null, "3", filePath);
        String id = G.cmd.addchathistory(filemime, chatroomid, chatmsg, "0", currenttime, "2", null, type, filehash, null, null, replymessage, replyfilehash, replyfrom);
        mAdapter.newPost(filemime, id, chatmsg, "0", currenttime, "2", null, filehash, replyfilehash, replymessage, replyfrom, type);
    }


    private void initDialog() {

        dialogSelectMedia = new Dialog(Singlechat.this);
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
                mService.setchatstate("3", userchat);
                pictureByCamera();
            }
        });

        tvSience = (TextView) dialogSelectMedia.findViewById(R.id.textView_sience);
        tvSience.setTypeface(G.fontAwesome);
        tvSience.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mService.setchatstate("3", userchat);
                openGallery();
            }
        });

        tvVideo = (TextView) dialogSelectMedia.findViewById(R.id.textView_video);
        tvVideo.setTypeface(G.fontAwesome);
        tvVideo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mService.setchatstate("4", userchat);
                videoByCamera();
            }
        });

        tvMusic = (TextView) dialogSelectMedia.findViewById(R.id.textView_music);
        tvMusic.setTypeface(G.fontAwesome);
        tvMusic.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mService.setchatstate("4", userchat);
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
                mService.setchatstate("4", userchat);
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
                Intent intent = new Intent(Singlechat.this, myPaint.class);
                startActivityForResult(intent, G.request_code_paint);
            }
        });

    }


    void pictureByCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, G.CAMERA_PIC_REQUEST);
    }


    void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/* video/*");
        startActivityForResult(intent, G.request_code_PICK_IMAGE);
    }


    void videoByCamera() {

        PackageManager packageManager = getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) == false) {
            Toast.makeText(Singlechat.this, getString(R.string.device_dosenot_camera_en), Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(Singlechat.this, explorer.class);
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
                    chatmsg = "My Position is : " + "\n" + "Latitude : " + String.valueOf(location.getLatitude()) + "\n" + "Longitude : " + String.valueOf(location.getLongitude());
                    type = "6";
                    mService.SendMessageOrder(filemime, userchat, chatmsg, type, null, null, null, replymessage, replyfilehash, replyfrom, "0", null);
                }
                else {
                    sendPosition = true;
                    pd = new ProgressDialog(Singlechat.this);
                    pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    pd.setMessage("Just wait...");
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
                                                  chatmsg = "My Position is : " + "\n" + "Latitude : " + String.valueOf(location.getLatitude()) + "\n" + "Longitude : " + String.valueOf(location.getLongitude());
                                                  type = "6";
                                                  mService.SendMessageOrder(filemime, userchat, chatmsg, type, null, null, null, replymessage, replyfilehash, replyfrom, "0", null);
                                              }

                                              locManager.removeUpdates(locationListener);
                                          }
                                      };


    void showSettingsAlert() {

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_exit_prompt);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        TextView tvMessage = (TextView) dialog.findViewById(R.id.textView_message_prompt);
        tvMessage.setTypeface(G.robotoBold);
        tvMessage.setText(getString(R.string.doyou_wantto_turnon_gps_en));

        Button tvYes = (Button) dialog.findViewById(R.id.btnView_yes);
        tvYes.setTypeface(G.robotoBold);
        tvYes.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.cancel();
                turnOnGps();
            }
        });

        Button tvNo = (Button) dialog.findViewById(R.id.btnView_no);
        tvNo.setTypeface(G.robotoBold);
        tvNo.setOnClickListener(new OnClickListener() {

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


    void turnOnGps() {
        startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), G.request_code_position);
    }


    private void geturi(Intent data, boolean isImage) {
        getFilePath(data.getData(), isImage);
    }


    private void geturi(Uri uri, boolean isImage) {
        getFilePath(uri, isImage);
    }


    /**
     * 
     * detect filePath from Uri and send to {@link #getsha(String, String, boolean) getsha}
     * 
     * @param uri file uri
     * @param isImage true if is image otherwise false
     */

    private void getFilePath(Uri uri, boolean isImage) {
        String[] proj = null;
        Cursor cursor = getContentResolver().query(uri, proj, null, null, null);

        String filename = "";
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            int columnIndexname = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
            filename = cursor.getString(columnIndexname);
            chatmsg = "";
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


    private void openLayoutAddText(String filename, Bitmap bit, final OnColorChangedListenerSelect l) {

        final LinearLayout layout = (LinearLayout) findViewById(R.id.layout_add_text);
        layout.setVisibility(View.VISIBLE);

        TextView txtFileName = ((TextView) findViewById(R.id.txt_file_name));
        txtFileName.setText(filename);

        ImageView iv = (ImageView) findViewById(R.id.imageView_file_pic);
        iv.setImageBitmap(bit);

        llVoice.setVisibility(View.GONE);
        findViewById(R.id.btn_send).setVisibility(View.GONE);

        Button btnSendWithText = (Button) findViewById(R.id.btn_send_file);
        btnSendWithText.setTypeface(G.fontAwesome);
        btnSendWithText.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String msgedt = edtchat.getText().toString();
                if (msgedt != null && !msgedt.isEmpty() && !msgedt.equals("null") && !msgedt.equals("")) {
                    chatmsg = edtchat.getText().toString();
                    edtchat.setText("");

                }
                closingkeybord();

                if (lytReplay.getVisibility() == view.VISIBLE) {

                    lytReplay.setVisibility(View.GONE);
                }

                l.colorChanged("ok", 1);

                layout.setVisibility(View.GONE);
                llVoice.setVisibility(View.VISIBLE);
                findViewById(R.id.btn_send).setVisibility(View.VISIBLE);
            }
        });

        findViewById(R.id.btn_cancel_send).setOnClickListener(new OnClickListener() {

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


    /**
     * 
     * get SHA1 from filePath and send to server for detection file before uploaded or no
     * 
     * @param datafile filePath
     * @param filename fileName
     * @param isImage true if is image otherwise no
     */

    private void getsha(String datafile, String filename, boolean isImage) {

        if (isImage) { // filePath is for Image so should compress image before upload
            datafile = HelperComoressImage.checkAndCompressImage(datafile);
            filePath = datafile;
        }

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA1");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(datafile);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] dataBytes = new byte[1024];

        int nread = 0;

        try {
            while ((nread = fis.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        byte[] mdbytes = md.digest();

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
                        handlerdd = new Handler();
                        handlerdd.postDelayed(new Runnable() {

                            @Override
                            public void run() {

                                singlechatlist.scrollToPosition(mAdapter.getItemCount() - 1);
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


    private void showdialog(final OnColorChangedListenerSelect listener) {

        final Dialog di = new Dialog(Singlechat.this);
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


    @SuppressWarnings("deprecation")
    private void popupoptions(View v) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);

        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View mView = layoutInflater.inflate(R.layout.popup_single_chat, null);
        popUp = new PopupWindow(Singlechat.this);
        popUp.setContentView(mView);
        popUp.setWindowLayoutMode(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        popUp.setHeight(1);
        popUp.setWidth(1);
        popUp.setTouchable(true);
        popUp.setFocusable(false);
        popUp.setOutsideTouchable(true);
        popUp.setBackgroundDrawable(new BitmapDrawable());
        popUp.showAtLocation(v, Gravity.NO_GRAVITY, location[0], location[1]);

        Button btnaddcontact = (Button) mView.findViewById(R.id.btn_add_contact);
        Button btnclearchat = (Button) mView.findViewById(R.id.btn_clear_chat);
        Button btndeletechat = (Button) mView.findViewById(R.id.btn_delete_chat);
        Button btnblockcontact = (Button) mView.findViewById(R.id.btn_block_contact);

        String clearChat = getString(R.string.clear_history_en);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {// allcaps is for api 14 and upper
            btnclearchat.setAllCaps(false);
        } else {
            clearChat = clearChat.substring(0, 1).toUpperCase() + clearChat.substring(1, 6).toLowerCase() + clearChat.substring(6, 7).toUpperCase() + clearChat.substring(7).toLowerCase();
        }
        btnclearchat.setText(clearChat);

        String blockContact = getString(R.string.block_contact_en);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {// allcaps is for api 14 and upper
            btnblockcontact.setAllCaps(false);
        } else {
            blockContact = blockContact.substring(0, 1).toUpperCase() + blockContact.substring(1, 6).toLowerCase() + blockContact.substring(6, 7).toUpperCase() + blockContact.substring(7).toLowerCase();
        }
        btnblockcontact.setText(blockContact);

        String deleteChat = getString(R.string.delete_chat_en);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {// allcaps is for api 14 and upper
            btndeletechat.setAllCaps(false);
        } else {
            deleteChat = deleteChat.substring(0, 1).toUpperCase() + deleteChat.substring(1, 7).toLowerCase() + deleteChat.substring(7, 8).toUpperCase() + deleteChat.substring(8).toLowerCase();
        }
        btndeletechat.setText(deleteChat);

        final String mobile = userchat.split("@")[0];
        btnblockcontact.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (popUp != null && popUp.isShowing()) {
                    popUp.dismiss();
                }
                int mobileExist = G.cmd.isMobileExist(mobile);

                if (mobileExist == 1) {
                    new BlockContact().execute(mobile, userchatname);
                    ll_iscontact.setVisibility(View.GONE);
                } else {
                    addContactForBlock(mobile);
                }

            }
        });
        if (iscontact != 0) {
            btnaddcontact.setVisibility(View.GONE);
        }
        if (isigap == true) {
            btnaddcontact.setVisibility(View.GONE);
            btnblockcontact.setVisibility(View.GONE);
        }

        btnclearchat.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                mAdapter.clearehistory();

                if (popUp != null && popUp.isShowing()) {
                    popUp.dismiss();
                }
                clearWithUid(userchat);

            }
        });

        btndeletechat.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (popUp != null && popUp.isShowing()) {
                    popUp.dismiss();
                }

                ConfirmationDialog cm = new ConfirmationDialog(Singlechat.this, new OnColorChangedListenerSelect() {

                    @Override
                    public void colorChanged(String key, int color) {

                    }


                    @Override
                    public void Confirmation(Boolean result) {
                        if (result) {
                            G.cmd.deletechatrooms(chatroomid);
                            deleteWithUid(userchat);
                            finish();
                        }
                    }
                });
                cm.showdialog(getString(R.string.do_you_want_delete_this_chat));

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
        mService.setchatstate("5", userchat);
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
        mService.setchatstate("2", userchat);
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
                    chatmsg = "";
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


    private class BlockContact extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new Dialog(Singlechat.this);
            pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(pDialog.getWindow().getAttributes());
            layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            pDialog.getWindow().setAttributes(layoutParams);
            pDialog.setCancelable(false);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
                pDialog.setContentView(R.layout.dialog_wait);
            } else {
                pDialog.setContentView(R.layout.dialog_wait_simple);
            }
            pDialog.show();
        }


        @Override
        protected String doInBackground(String... args) {

            final String mobile = args[0];

            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", mobile));
                JSONObject jsonobj = jParser.getJSONFromUrl(G.url + "users/black-list", params, "POST", G.basicAuth, null);

                try {

                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);
                    Boolean success = json.getBoolean(G.TAG_SUCCESS);

                    if (success) {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                G.cmd.addBlockContact(userchatname, mobile, userchatavatar, null, userchat);
                                Toast.makeText(Singlechat.this, getString(R.string.blocked_user_en), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(Singlechat.this, getString(R.string.success_false_en), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
                catch (JSONException e) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(Singlechat.this, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            catch (Exception e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(Singlechat.this, getString(R.string.internet_connection_problem_en), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            return null;
        }


        @Override
        protected void onPostExecute(String file_url) {

            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }
    }


    private static void sendchatMessageToActivity(Context mContext) {
        Intent intent = new Intent("loadinfo");
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
    public void onConfigurationChanged(Configuration newConfig) {
        Utils.checkLanguage(this);
        super.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onPause() {
        super.onPause();
        inActivity = false;

        G.HANDLER.removeCallbacksAndMessages(null);
        G.appIsShowing = false;
    }


    @Override
    protected void onStop() {
        super.onStop();

        inActivity = false;
    }


    public static MyService getService() {
        return staticSingleService;
    }


    private void clearWithUid(String uid) {
        Intent intent = new Intent("cleareWithUidChat");
        intent.putExtra("uid", uid);
        LocalBroadcastManager.getInstance(Singlechat.this).sendBroadcast(intent);
        //=======Update Last Message In Page All
        Intent intentAll = new Intent("clearHistoryAll");
        intentAll.putExtra("MODEL", "1");
        intentAll.putExtra("UID", uid);
        LocalBroadcastManager.getInstance(Singlechat.this).sendBroadcast(intentAll);
    }


    private void deleteWithUid(String uid) {
        Intent intent = new Intent("deleteWithUidChat");
        intent.putExtra("uid", uid);
        LocalBroadcastManager.getInstance(Singlechat.this).sendBroadcast(intent);
        Intent intent1 = new Intent("loadall");
        LocalBroadcastManager.getInstance(Singlechat.this).sendBroadcast(intent1);
    }

}
