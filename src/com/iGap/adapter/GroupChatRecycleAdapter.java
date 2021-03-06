// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;
import com.iGap.MusicPlayer;
import com.iGap.R;
import com.iGap.UserProfile;
import com.iGap.customviews.GifMovieView;
import com.iGap.customviews.ImageSquareProgressBar;
import com.iGap.customviews.MusicSquareProgressBar;
import com.iGap.helpers.HelperAnimation;
import com.iGap.helpers.HelperDialogImageView;
import com.iGap.helpers.HelperFetchWebsiteData;
import com.iGap.helpers.HelperGetFileInformation;
import com.iGap.helpers.HelperGetLink;
import com.iGap.helpers.HelperGetTime;
import com.iGap.helpers.PageMessagingPopularFunction;
import com.iGap.instruments.ImageLoader;
import com.iGap.instruments.ImageLoader1;
import com.iGap.instruments.ImageLoaderDialog;
import com.iGap.instruments.JSONParser;
import com.iGap.instruments.Utils;
import com.iGap.interfaces.OnComplet;
import com.iGap.interfaces.OnDownloadListener;
import com.iGap.interfaces.OnUploadListener;
import com.iGap.services.DownloaderService;
import com.iGap.services.MyService;
import com.iGap.services.UploaderService;


/**
 * 
 * show list of message in page group and update this view if data change
 *
 */

public class GroupChatRecycleAdapter extends RecyclerView.Adapter<GroupChatRecycleAdapter.ViewHolder> {

    //************************* ArrayLists

    private ArrayList<String>      filemime;
    private ArrayList<String>      idarray;
    private ArrayList<String>      msgarray;
    private ArrayList<String>      statusarray;
    private ArrayList<String>      msgtimearray;
    private ArrayList<String>      msgtypearray;
    private ArrayList<String>      usernames;
    private ArrayList<String>      filehasharray;
    private ArrayList<String>      replyfilehasharray;
    private ArrayList<String>      replymessagearray;
    private ArrayList<String>      replyfromarray;
    private ArrayList<String>      typearray;
    private ArrayList<String>      msgsenderarray;
    private ArrayList<String>      msgsenderavatararray;
    private ArrayList<String>      msgidarray;

    private ArrayList<Boolean>     doingDownloadOrUpload;
    private ArrayList<Boolean>     selectedItem;
    private ArrayList<Boolean>     runUploaderThread;
    private ArrayList<Boolean>     runDownloaderThread;
    private ArrayList<Boolean>     runDownloaderListener;
    private ArrayList<Boolean>     runUploaderListener;

    private ArrayList<Boolean>     autoDownload        = new ArrayList<Boolean>();
    private ArrayList<Boolean>     showIndeterminate   = new ArrayList<Boolean>();
    private ArrayList<Boolean>     highlightLayout     = new ArrayList<Boolean>();
    private ArrayList<Integer>     hashPos             = new ArrayList<Integer>();
    private ArrayList<Integer>     deletePositions     = new ArrayList<Integer>();

    //************************* private values
    private int                    countOffset;
    private int                    selectCounter;

    private String                 avatar;
    private String                 gchid;
    private String                 gchname;
    private String                 filePath;
    private String                 type;
    private String                 gchatmsg;
    private String                 filehash;
    private String                 basicAuth;
    private String                 newFilePath;
    private String                 textcolor;
    private String                 textsize;
    private String                 day;
    private String                 month;
    private String                 year;
    private String                 hijri;
    private String                 replymessage        = "";
    private String                 replyfilehash       = "";
    private String                 replyfrom           = "";
    private String                 newMessagePosition  = "";

    private boolean                load;
    private boolean                loadBottom;
    private boolean                selectItemVisible;
    private boolean                clearChat           = false;
    private boolean                smileClicked        = false;
    private boolean                detectNewMessagePos = true;

    //************************* private variables & elements

    private Button                 btnDrawerReplay;
    private TextView               txtDrawerCounter;
    private LinearLayout           layoutMusic, sideDrawer;

    private ImageLoader            imgLoader;
    private ImageLoader1           il;
    private ImageLoaderDialog      imgLoaderDialog;
    private SpannableStringBuilder buildertext;
    private ViewHolder             viewholder;
    private View                   mainView;
    private Thread                 thread;
    private DrawableManager        dm;
    private PopupWindow            popUp;
    private MediaPlayer            mp;
    private Handler                handler;
    private Dialog                 dialog;
    private MyService              mService;
    private DrawableManagerDialog  dmDialog;
    private MusicPlayer            musicPlayer;
    private Context                mContext;


    /**
     * 
     * view holder for hold a row of item in it
     *
     */

    public class ViewHolder extends RecyclerView.ViewHolder {

        public Button       btnDelete;
        public Button       btnAlarm;
        public Button       btnclearhistory;

        public TextView     txtNewMsg;
        public TextView     txtwdescription;
        public TextView     txtwtitle;
        public TextView     txtSeenNumber;
        public TextView     txttimeday;
        public TextView     messagetxv;
        public TextView     txtreplayfrom;
        public TextView     txtreplaymessage;
        public TextView     txtfilesname;
        public TextView     datetxv;
        public TextView     messagestatusicon;
        public TextView     txtJoin;

        public ImageView    imgwicon;
        public ImageView    imguseravatar;
        public ImageView    img_avatar;
        public ImageView    imgreply;
        public ImageView    img_treegonal;

        public LinearLayout ll_text;
        public LinearLayout llNewMsg;
        public LinearLayout llwebsite;
        public LinearLayout llcontainermain;
        public LinearLayout llcontainer;
        public LinearLayout lltime;
        public LinearLayout llbg;
        public LinearLayout audioll;
        public LinearLayout videoll;
        public LinearLayout llreplay;
        public LinearLayout llfiles;
        public LinearLayout llJoin;
        public LinearLayout ll_icons;
        public LinearLayout lytTimeInImage;

        public ImageSquareProgressBar messageimage, imgvideo, imgfiles;
        public MusicSquareProgressBar seekBar;
        public DownloaderService      downloaderService;
        public View                   roww, viewdate;
        public GifMovieView           gifview;

        public TextView               image_check1;
        public TextView               image_time;
        public FrameLayout            llimage;


        public ViewHolder(View row) {
            super(row);
            roww = row;
            downloaderService = new DownloaderService();

            image_check1 = (TextView) row.findViewById(R.id.lis_txt_image_check1);
            image_check1.setTypeface(G.fontAwesome);

            llimage = (FrameLayout) row.findViewById(R.id.lis_ll_image);
            image_time = (TextView) row.findViewById(R.id.lis_txt_time);

            //ImageView
            ImageSquareProgressBar.mode = 1;
            messageimage = (ImageSquareProgressBar) row.findViewById(R.id.message_image);
            imgvideo = (ImageSquareProgressBar) row.findViewById(R.id.vv_video);
            imgfiles = (ImageSquareProgressBar) row.findViewById(R.id.img_files);
            seekBar = (MusicSquareProgressBar) row.findViewById(R.id.seekBar1);
            imguseravatar = (ImageView) row.findViewById(R.id.img_useravatar);
            img_treegonal = (ImageView) row.findViewById(R.id.imageView_threegonal_group);

            //Main Layout
            llcontainer = (LinearLayout) row.findViewById(R.id.ll_container);
            llcontainermain = (LinearLayout) row.findViewById(R.id.ll_container_main);
            llwebsite = (LinearLayout) row.findViewById(R.id.ll_website);

            //timelayout
            lltime = (LinearLayout) row.findViewById(R.id.ll_time);
            llJoin = (LinearLayout) row.findViewById(R.id.ll_join);
            txttimeday = (TextView) row.findViewById(R.id.txt_timeday);
            viewdate = row.findViewById(R.id.view_date);

            //new message layout
            llNewMsg = (LinearLayout) row.findViewById(R.id.ll_new_msg);
            txtNewMsg = (TextView) row.findViewById(R.id.txt_new_msg);

            //gif
            gifview = (GifMovieView) row.findViewById(R.id.gif1);

            //bg layout
            llbg = (LinearLayout) row.findViewById(R.id.ll_bg);
            ll_text = (LinearLayout) row.findViewById(R.id.ll_text);
            ll_icons = (LinearLayout) row.findViewById(R.id.ll_icons);
            lytTimeInImage = (LinearLayout) row.findViewById(R.id.lyt_time_in_image);

            //Audio layout
            audioll = (LinearLayout) row.findViewById(R.id.audio_ll);

            //Video layout
            videoll = (LinearLayout) row.findViewById(R.id.video_ll);

            //textv layout
            messagetxv = (TextView) row.findViewById(R.id.message_txv);
            txtJoin = (TextView) row.findViewById(R.id.txt_join);
            llreplay = (LinearLayout) row.findViewById(R.id.ll_replay);
            imgreply = (ImageView) row.findViewById(R.id.img_reply);
            txtreplayfrom = (TextView) row.findViewById(R.id.txt_replay_from);
            txtreplaymessage = (TextView) row.findViewById(R.id.txt_replay_message);
            txtwtitle = (TextView) row.findViewById(R.id.txt_wtitle);
            txtwdescription = (TextView) row.findViewById(R.id.txt_wdescription);

            //files layout
            llfiles = (LinearLayout) row.findViewById(R.id.ll_files);
            txtfilesname = (TextView) row.findViewById(R.id.txt_filesname);

            messagestatusicon = (TextView) row.findViewById(R.id.message_status_icon);
            datetxv = (TextView) row.findViewById(R.id.date_txv);
            img_treegonal = (ImageView) row.findViewById(R.id.imageView_threegonal_group);
            imgwicon = (ImageView) row.findViewById(R.id.img_wicon);

            seekBar.setClickable(false);
            seekBar.setFocusable(false);
            seekBar.setEnabled(false);

        }
    }


    public GroupChatRecycleAdapter(ArrayList<String> filemime1, ArrayList<String> idarray1, ArrayList<String> msgarray1, ArrayList<String> statusarray1, ArrayList<String> msgtimearray1, ArrayList<String> msgtypearray1
                                   , ArrayList<String> usernames1, ArrayList<Boolean> selectedItem1, ArrayList<String> typearray1, ArrayList<String> filehasharray1
                                   , ArrayList<String> replyfilehasharray1, ArrayList<String> replymessagearray1, ArrayList<String> replyfromarray1, ArrayList<Boolean> runUploaderThread1, ArrayList<Boolean> runDownloaderThread1, ArrayList<Boolean> runDownloaderListener1, ArrayList<Boolean> runUploaderListener1,
                                   ArrayList<String> msgsenderarray1, ArrayList<String> msgsenderavatararray1, ArrayList<String> msgidarray1, Context context, String year1, String month1, String day1, ImageLoader1 il1, View view, DrawableManager dm1, DrawableManagerDialog dmDialog1, Handler handler1, String replymessage1, String replyfilehash1, String replyfrom1, MyService mService1, Boolean selectItemVisible1,
                                   Button btnDrawerReplay1, TextView txtDrawerCounter1, LinearLayout sideDrawer1, String gchname1, String gchid1, Boolean smileClicked1, int countOffset1, boolean load1, boolean loadBottom1, LinearLayout layoutMusic) {

        mContext = context;
        idarray = idarray1;
        this.layoutMusic = layoutMusic;
        msgarray = msgarray1;
        statusarray = statusarray1;
        msgtimearray = msgtimearray1;
        msgtypearray = msgtypearray1;
        usernames = usernames1;
        selectedItem = selectedItem1;
        typearray = typearray1;
        filehasharray = filehasharray1;
        replyfilehasharray = replyfilehasharray1;
        replymessagearray = replymessagearray1;
        replyfromarray = replyfromarray1;
        runUploaderThread = runUploaderThread1;
        runDownloaderThread = runDownloaderThread1;
        runDownloaderListener = runDownloaderListener1;
        runUploaderListener = runUploaderListener1;
        msgsenderavatararray = msgsenderavatararray1;
        filemime = filemime1;
        msgidarray = msgidarray1;
        dm = dm1;
        dmDialog = dmDialog1;
        smileClicked = smileClicked1;
        btnDrawerReplay = btnDrawerReplay1;
        txtDrawerCounter = txtDrawerCounter1;
        msgsenderarray = msgsenderarray1;
        sideDrawer = sideDrawer1;
        handler = handler1;
        thread = new Thread();
        newFilePath = null;
        replymessage = replymessage1;
        replyfilehash = replyfilehash1;
        replyfrom = replyfrom1;
        selectItemVisible = selectItemVisible1;
        countOffset = countOffset1;
        load = load1;
        loadBottom = loadBottom1;
        year = year1;
        month = month1;
        gchname = gchname1;
        gchid = gchid1;
        day = day1;
        il = il1;

        hijri = G.cmd.getsetting(13);

        resetPercent();
        mp = new MediaPlayer();
        basicAuth = G.cmd.namayesh4(3, "info");
        mService = mService1;
        mainView = view;
        textcolor = G.cmd.getsetting(5);
        textsize = G.cmd.getsetting(4);
        avatar = G.cmd.namayesh4(4, "info");
        if (avatar != null && !avatar.isEmpty() && !avatar.equals("null") && !avatar.equals("")) {} else {
            avatar = "empty";
        }
        imgLoader = new ImageLoader(mContext, basicAuth);
        imgLoaderDialog = new ImageLoaderDialog(mContext, basicAuth);

        doingDownloadOrUpload = new ArrayList<Boolean>();
        for (int i = 0; i < msgidarray.size(); i++) {
            doingDownloadOrUpload.add(false);
            highlightLayout.add(false);
            showIndeterminate.add(false);
            autoDownload.add(true);
        }

        musicPlayer = new MusicPlayer(mContext, layoutMusic, "small", basicAuth, G.cmd, gchid, "2");

        try {
            if (MusicPlayer.place != null) {
                if (MusicPlayer.place.equals(gchname) && musicPlayer.mp != null) {
                    layoutMusic.setVisibility(View.VISIBLE);
                    MusicPlayer.updateView();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void resetPercent() {
        for (int i = 0; i < filehasharray.size(); i++) {
            G.cmd.updateFilePercents(filehasharray.get(i));
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_group_chat, parent, false);
        viewholder = new ViewHolder(v);
        return viewholder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder h, final int position) {

        if (load && position == 0) {
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent intent = new Intent("LoadGroupChat");
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                }
            }, 1000);
        }

        if (loadBottom && (position + 1) == idarray.size()) {
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent intent = new Intent("LoadBottomGroupChat");
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                }
            }, 1000);
        }

        if (typearray.get(position).equals("3")) {
            h.imgvideo.setImageResource(R.drawable.video_attach_big);
        } else if (typearray.get(position).equals("7")) {
            h.imgfiles.setImageResource(R.drawable.atach);
        }

        final OnDownloadListener listener = new OnDownloadListener() {

            @Override
            public void onProgressDownload(final int percent, int downloadedSize, int fileSize, boolean completeDownload) {

                if (checkSize(position, showIndeterminate.size())) {
                    showIndeterminate.set(position, false);
                }

                if ( !clearChat && checkSize(position, runDownloaderListener.size()) && runDownloaderListener.get(position)) {
                    ((Activity) mContext).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (typearray.get(position).equals("2")) {
                                h.messageimage.setIndeterminate(false);
                                h.messageimage.setProgress(percent);
                            } else if (typearray.get(position).equals("3")) {
                                h.imgvideo.setIndeterminate(false);
                                h.imgvideo.setProgress(percent);
                            } else if (typearray.get(position).equals("4")) {
                                h.seekBar.setIndeterminate(false);
                                h.seekBar.setProgress(percent);
                            } else if (typearray.get(position).equals("7")) {
                                h.imgfiles.setIndeterminate(false);
                                h.imgfiles.setProgress(percent);
                            } else if (typearray.get(position).equals("8")) {
                                h.seekBar.setIndeterminate(false);
                                h.seekBar.setProgress(percent);
                            }
                        }
                    });
                }

                if (percent >= 100 && completeDownload) {
                    G.HANDLER.postDelayed(new Runnable() { // adaptoin view for clear progress , if not clear yet

                        @Override
                        public void run() {
                            notifyItemChanged(position);
                        }
                    }, 2000);
                }
            }
        };

        if ( !typearray.get(position).equals("1") && !typearray.get(position).equals("5") && !typearray.get(position).equals("6") && !typearray.get(position).equals("100")) {
            String filestatus = G.cmd.selectfilestatus(filehasharray.get(position));
            if (filestatus.equals("3")) {

                if (checkSize(position, showIndeterminate.size()) && checkSize(position, filehasharray.size())) {
                    G.cmd.updatefilestatus(filehasharray.get(position), 4);

                    showIndeterminate.set(position, true);
                    indeterminateProgress(position, h.messageimage);
                }

                final OnUploadListener uploadListener = new OnUploadListener() {

                    @Override
                    public void onProgressUpload(final int percent, boolean complete, String lastFileDatabaseID) {

                        if (checkSize(position, doingDownloadOrUpload.size()) && checkSize(position, showIndeterminate.size())) {
                            doingDownloadOrUpload.set(position, true);
                            showIndeterminate.set(position, false);
                        }

                        if ( !clearChat && checkSize(position, runUploaderListener.size()) && runUploaderListener.get(position)) {
                            ((Activity) mContext).runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    if (typearray.get(position).equals("2")) {
                                        h.messageimage.setIndeterminate(false);
                                        h.messageimage.setProgress(percent);
                                    } else if (typearray.get(position).equals("3")) {
                                        h.imgvideo.setIndeterminate(false);
                                        h.imgvideo.setColor("#1aaeac");
                                        h.imgvideo.setProgress(percent);
                                    } else if (typearray.get(position).equals("4")) {
                                        h.seekBar.setIndeterminate(false);
                                        h.seekBar.setProgress(percent);
                                    } else if (typearray.get(position).equals("7")) {
                                        h.imgfiles.setIndeterminate(false);
                                        h.imgfiles.setColor("#1aaeac");
                                        h.imgfiles.setProgress(percent);
                                    } else if (typearray.get(position).equals("8")) {
                                        h.seekBar.setIndeterminate(false);
                                        h.seekBar.setProgress(percent);
                                    }
                                }
                            });
                        }

                        if (percent >= 100 && complete) {
                            handler.post(new Runnable() {

                                @Override
                                public void run() {
                                    G.cmd.updatefilestatus(filehasharray.get(position), 5);
                                    handler.post(new Runnable() {

                                        @Override
                                        public void run() {
                                            Toast.makeText(mContext, mContext.getString(R.string.upload_completed_en), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        }
                    }
                };

                String filePathFile = G.cmd.selectField("files", "filehash ='" + filehasharray.get(position) + "'", 6);
                File imgFile = new File(filePathFile);
                int index = imgFile.getName().lastIndexOf('.') + 1;
                String filemime = imgFile.getName().substring(index).toLowerCase();

                int fileHashIsResend = G.resendFilehashArrayGroupChat.indexOf(filehasharray.get(position)); // check mikonim ke in filehash dar list resendha hast 
                boolean resend = false;
                if (fileHashIsResend != -1) { // agar in filehash vojud dasht pas resend ast
                    resend = true;
                }

                new UploaderService()
                        .listener(uploadListener)
                        .fileHash(filehasharray.get(position))
                        .chatMessage(msgarray.get(position))
                        .type(typearray.get(position))
                        .messageType(2)
                        .replyFilehash(replyfilehash)
                        .replyMessage(replymessage)
                        .replyFrom(replyfrom)
                        .filemime(filemime)
                        .chatID(gchid)
                        .filepath(filePathFile)
                        .Authorization(basicAuth)
                        .service(mService)
                        .userChatAvatar(avatar)
                        .resend(resend)
                        .upload(mContext);

            } else if (filestatus.equals("1")) {

                if (checkSize(position, doingDownloadOrUpload.size())) {
                    doingDownloadOrUpload.set(position, false);
                }

                final Handler handler = new Handler();
                thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        while ( !clearChat && checkSize(position, runDownloaderThread.size()) && checkSize(position, filehasharray.size()) && runDownloaderThread.get(position)) {

                            handler.post(new Runnable() {

                                @Override
                                public void run() {
                                    String y = G.cmd.selectPercent(filehasharray.get(position));
                                    int percent = Integer.parseInt(y);
                                    if (percent > 0 && percent != 100) { // agar darsad bozorgtar az sefr bud natige migirim ke dar hale download ast , chon dar ebteda hameye darsadha ra sefr shode.
                                        doingDownloadOrUpload.set(position, true);
                                        showIndeterminate.set(position, false);
                                    }

                                    if (typearray.get(position).equals("2")) {
                                        h.messageimage.setIndeterminate(false);
                                        h.messageimage.setProgress(percent);
                                    } else if (typearray.get(position).equals("3")) {
                                        h.imgvideo.setIndeterminate(false);
                                        h.imgvideo.setProgress(percent);
                                    } else if (typearray.get(position).equals("4")) {
                                        h.seekBar.setIndeterminate(false);
                                        h.seekBar.setProgress(percent);
                                    } else if (typearray.get(position).equals("7")) {
                                        h.imgfiles.setIndeterminate(false);
                                        h.imgfiles.setProgress(percent);
                                    } else if (typearray.get(position).equals("8")) {
                                        h.seekBar.setIndeterminate(false);
                                        h.seekBar.setProgress(percent);
                                    }

                                    if (percent >= 100) {
                                        runDownloaderThread.set(position, false);
                                        G.cmd.updateType(filehasharray.get(position), 2);
                                    }

                                }
                            });
                            try {
                                Thread.sleep(1000);
                            }
                            catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                thread.start();
            } else if (filestatus.equals("4")) {
                if (checkSize(position, runUploaderListener.size()) && checkSize(position, showIndeterminate.size())) {
                    runUploaderListener.set(position, false);
                    showIndeterminate.set(position, false);
                }

                final Handler handler = new Handler();
                thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        while ( !clearChat && checkSize(position, runUploaderThread.size()) && checkSize(position, filehasharray.size()) && runUploaderThread.get(position)) {
                            handler.post(new Runnable() {

                                @Override
                                public void run() {

                                    int percent = Integer.parseInt(G.cmd.selectPercent(filehasharray.get(position)));
                                    if (percent > 0 && percent != 100) { // agar darsad bozorgtar az sefr bud natige migirim ke dar hale download ast , chon dar ebteda hameye darsadha ra sefr shode.
                                        doingDownloadOrUpload.set(position, true);
                                    }

                                    if (runUploaderListener.get(position)) {
                                        runUploaderListener.set(position, false);
                                    }

                                    if (typearray.get(position).equals("2")) {
                                        h.messageimage.setIndeterminate(false);
                                        h.messageimage.setProgress(percent);
                                    } else if (typearray.get(position).equals("3")) {
                                        h.imgvideo.setIndeterminate(false);
                                        h.imgvideo.setColor("#1aaeac");
                                        h.imgvideo.setProgress(percent);
                                    } else if (typearray.get(position).equals("4")) {
                                        h.seekBar.setIndeterminate(false);
                                        h.seekBar.setProgress(percent);
                                    } else if (typearray.get(position).equals("7")) {
                                        h.imgfiles.setIndeterminate(false);
                                        h.imgfiles.setColor("#1aaeac");
                                        h.imgfiles.setProgress(percent);
                                    } else if (typearray.get(position).equals("8")) {
                                        h.seekBar.setIndeterminate(false);
                                        h.seekBar.setProgress(percent);
                                    }

                                    if (percent >= 100) {
                                        runUploaderThread.set(position, false);
                                        G.cmd.updateType(filehasharray.get(position), 1);
                                    }

                                }
                            });
                            try {
                                Thread.sleep(1000);
                            }
                            catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                thread.start();
            }
        }
        h.imguseravatar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserProfile.class);
                intent.putExtra("chatroomid", "empty");
                intent.putExtra("userchat", usernames.get(position) + "@igap.im");
                intent.putExtra("userchatname", msgsenderarray.get(position));
                intent.putExtra("userchatavatar", msgsenderavatararray.get(position));

                mContext.startActivity(intent);

            }
        });
        if (msgsenderavatararray.get(position) != null && !msgsenderavatararray.get(position).isEmpty() && !msgsenderavatararray.get(position).equals("null") && !msgsenderavatararray.get(position).equals("") && !msgsenderavatararray.get(position).equals("empty") && !msgsenderavatararray.get(position).equals("avatare user")) {

            il.DisplayImage(msgsenderavatararray.get(position), R.drawable.difaultimage, h.imguseravatar);

        } else {

            if ( !typearray.get(position).equals("100")) {

                String name = msgsenderarray.get(position);
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
                String Upname1 = mname.toUpperCase();
                Bitmap bm = G.utileProg.drawAlphabetOnPicture(h.imguseravatar.getLayoutParams().width, Upname1, "");
                h.imguseravatar.setImageBitmap(bm);
            }

        }

        if (textcolor != null && !textcolor.isEmpty() && !textcolor.equals("null") && !textcolor.equals("") && !textcolor.equals("0")) {
            h.messagetxv.setTextColor(Color.parseColor(textcolor));
        }

        if (textsize != null && !textsize.isEmpty() && !textsize.equals("null") && !textsize.equals("") && !textsize.equals("0")) {
            h.messagetxv.setTextSize(Float.parseFloat(textsize));
        }

        h.datetxv.setText(HelperGetTime.removeSecendFromTime(msgtimearray.get(position)));
        h.image_time.setText(HelperGetTime.removeSecendFromTime(msgtimearray.get(position)));

        if (position != 0) {
            HelperGetTime.setTxtToday(mContext, h.lltime, h.txttimeday, msgtimearray.get(position), msgtimearray.get(position - 1), position, year, month, day);
        } else {
            HelperGetTime.setTxtToday(mContext, h.lltime, h.txttimeday, msgtimearray.get(position), "0", position, year, month, day);
        }

        h.llNewMsg.setVisibility(View.GONE);
        if ( !G.visibleGroupID.equals(gchid)) { // agar dakhele chat budim new message ra namayesh nade
            if (detectNewMessagePos) {
                if ( !statusarray.get(position).equals("1") && !statusarray.get(position).equals("2") && !statusarray.get(position).equals("3")) { // agar ma ferestandeye payam nabudim
                    if ( !newMessagePosition.equals(statusarray.get(position)) && statusarray.get(position).equals("4")) {
                        detectNewMessagePos = false;
                        h.llNewMsg.setVisibility(View.VISIBLE);
                        h.txtNewMsg.setText("new message received");
                    }
                    newMessagePosition = statusarray.get(position);
                }
            }
        }

        if (statusarray.get(position).equals("0")) {
            h.messagestatusicon.setVisibility(View.VISIBLE);
            h.messagestatusicon.setTypeface(G.fontAwesome);
            h.messagestatusicon.setText(R.string.fa_repeat);
            h.messagestatusicon.setTextColor(mContext.getResources().getColor(R.color.red_light));
        } else {
            h.messagestatusicon.setVisibility(View.GONE);
        }

        h.image_check1.setText(h.messagestatusicon.getText());
        h.image_check1.setTextColor(h.messagestatusicon.getTextColors());
        h.image_check1.setVisibility(h.messagestatusicon.getVisibility());

        if (msgtypearray.get(position).equals("1")) {
            h.llcontainer.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            h.llcontainer.setGravity(Gravity.LEFT);
            h.roww.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            h.img_treegonal.setImageResource(R.drawable.podrecive);
            h.llbg.setBackgroundResource(R.drawable.chatboxrecive);
            h.llcontainermain.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            h.llcontainermain.setGravity(Gravity.LEFT);
            h.roww.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        } else {
            h.llcontainer.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            h.llcontainer.setGravity(Gravity.RIGHT);
            h.roww.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            h.img_treegonal.setImageResource(R.drawable.pod3);
            h.llbg.setBackgroundResource(R.drawable.chatboxsend);
            h.llcontainermain.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            h.llcontainermain.setGravity(Gravity.RIGHT);
            h.roww.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }

        if (replymessagearray.get(position) != null && !replymessagearray.get(position).isEmpty() && !replymessagearray.get(position).equals("null") && !replymessagearray.get(position).equals("")) {
            h.llreplay.setVisibility(View.VISIBLE);

            h.txtreplayfrom.setText(replyfromarray.get(position));
            buildertext = parseText(replymessagearray.get(position), false, position);

            h.txtreplaymessage.setText(buildertext);

            if (replyfilehasharray.get(position) != null && !replyfilehasharray.get(position).isEmpty() && !replyfilehasharray.get(position).equals("null") && !replyfilehasharray.get(position).equals("")) {
                try {
                    String thumb = G.cmd.getfile(3, replyfilehasharray.get(position));

                    if (thumb != null && !thumb.isEmpty() && !thumb.equals("null") && !thumb.equals("")) {
                        il.DisplayImage(thumb, R.drawable.difaultimage, h.imgreply);
                    } else {
                        h.imgreply.setVisibility(View.GONE);
                    }

                }
                catch (Exception e) {
                    h.imgreply.setVisibility(View.GONE);
                }
            }
            else {
                h.imgreply.setVisibility(View.GONE);
            }
        } else {
            h.llreplay.setVisibility(View.GONE);
        }

        if (typearray.get(position).equals("100")) {
            h.audioll.setVisibility(View.GONE);
            h.llimage.setVisibility(View.GONE);
            h.videoll.setVisibility(View.GONE);
            h.llfiles.setVisibility(View.GONE);
            h.ll_text.setVisibility(View.GONE);
            h.llcontainer.setVisibility(View.GONE);
            h.lltime.setVisibility(View.GONE);
            h.llJoin.setVisibility(View.VISIBLE);
            h.txtJoin.setText(msgarray.get(position));
            h.llwebsite.setVisibility(View.GONE);
            h.gifview.setVisibility(View.GONE);
            h.lytTimeInImage.setVisibility(View.GONE);
            h.ll_text.setVisibility(View.VISIBLE);
            h.ll_icons.setVisibility(View.VISIBLE);

        } else if (typearray.get(position).equals("1")) {
            h.llJoin.setVisibility(View.GONE);
            h.audioll.setVisibility(View.GONE);
            h.llimage.setVisibility(View.GONE);
            h.videoll.setVisibility(View.GONE);
            h.llfiles.setVisibility(View.GONE);
            h.gifview.setVisibility(View.GONE);
            h.lytTimeInImage.setVisibility(View.GONE);
            h.ll_text.setVisibility(View.VISIBLE);
            h.ll_icons.setVisibility(View.VISIBLE);
            buildertext = parseText(msgarray.get(position), true, position);

            if ((msgarray.get(position).toLowerCase().indexOf("#".toLowerCase()) != -1) || (msgarray.get(position).toLowerCase().indexOf("@".toLowerCase()) != -1)) { // Search link
                h.messagetxv.setText(buildertext, BufferType.SPANNABLE);
                h.messagetxv.setMovementMethod(LinkMovementMethod.getInstance());
            } else { // web link
                h.messagetxv.setText(buildertext, BufferType.SPANNABLE);
                h.messagetxv.setMovementMethod(new HelperGetLink(mContext));
            }

            Utils.setLayoutDirection(h.messagetxv, h.ll_text);

            URLSpan[] urlSpans = h.messagetxv.getUrls();
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

                                    ((Activity) mContext).runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {
                                            JSONObject jo;
                                            try {
                                                jo = new JSONObject(message);
                                                String title = jo.getString("title");
                                                String description = jo.getString("description");
                                                String icon = jo.getString("icon");

                                                h.llwebsite.setVisibility(View.VISIBLE);
                                                h.txtwtitle.setText(title);
                                                h.txtwdescription.setText(description);
                                                if (icon != null && !icon.isEmpty() && !icon.equals("null") && !icon.equals("")) {
                                                    if (icon.substring(0, 1).equals("/")) {
                                                        icon = url + icon;
                                                    }
                                                    il.DisplayImage(icon, R.drawable.difaultimage, h.imgwicon);
                                                } else {
                                                    h.imgwicon.setVisibility(View.GONE);
                                                }

                                            }
                                            catch (JSONException e) {

                                                e.printStackTrace();
                                                h.llwebsite.setVisibility(View.GONE);
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
                    String icon = G.cmd.getwebsiteinfo(url, 4);

                    h.llwebsite.setVisibility(View.VISIBLE);
                    h.txtwtitle.setText(title);
                    h.txtwdescription.setText(description);
                    if (icon != null && !icon.isEmpty() && !icon.equals("null") && !icon.equals("")) {
                        if (icon.substring(0, 1).equals("/")) {
                            icon = url + icon;
                        }
                        il.DisplayImage(icon, R.drawable.difaultimage, h.imgwicon);
                    } else {
                        h.imgwicon.setVisibility(View.GONE);
                    }
                }

            } else {
                h.llwebsite.setVisibility(View.GONE);
            }
        } else if (typearray.get(position).equals("2")) {
            h.llJoin.setVisibility(View.GONE);
            h.audioll.setVisibility(View.GONE);
            h.llwebsite.setVisibility(View.GONE);
            h.gifview.setVisibility(View.GONE);

            if (msgarray.get(position) != null && !msgarray.get(position).isEmpty() && !msgarray.get(position).equals("null") && !msgarray.get(position).equals("")) {
                buildertext = parseText(msgarray.get(position), true, position);
                h.messagetxv.setMovementMethod(LinkMovementMethod.getInstance());
                h.messagetxv.setText(buildertext, BufferType.SPANNABLE);
                h.ll_text.setVisibility(View.VISIBLE);
                h.lytTimeInImage.setVisibility(View.GONE);
                h.ll_icons.setVisibility(View.VISIBLE);
                Utils.setLayoutDirection(h.messagetxv, h.ll_text);
            } else {
                h.ll_text.setVisibility(View.GONE);
                h.ll_icons.setVisibility(View.GONE);
                h.lytTimeInImage.setVisibility(View.VISIBLE);
            }

            h.videoll.setVisibility(View.GONE);
            h.llfiles.setVisibility(View.GONE);
            h.llimage.setVisibility(View.VISIBLE);

            int isfileexist = G.cmd.isfileinfoexist(filehasharray.get(position));
            if (isfileexist != 0) {
                String status = G.cmd.getfile(4, filehasharray.get(position));
                if (status.equals("2") || status.equals("5") || msgtypearray.get(position).equals("2")) {
                    String filepath = G.cmd.getfile(6, filehasharray.get(position));

                    if (filepath != null && !filepath.isEmpty() && !filepath.equals("null") && !filepath.equals("")) {
                        File imgFile = new File(filepath);
                        if (imgFile.exists()) {
                            int index = imgFile.getName().lastIndexOf('.') + 1;
                            String ext = imgFile.getName().substring(index).toLowerCase();
                            //MimeTypeMap mime = MimeTypeMap.getSingleton();
                            //String type = mime.getMimeTypeFromExtension(ext);
                            if (ext.equals("gif")) {
                                h.llimage.setVisibility(View.GONE);
                                h.gifview.setVisibility(View.VISIBLE);
                                h.gifview.setMoviefilepath(filepath);
                            } else {
                                h.llimage.setVisibility(View.VISIBLE);
                                dm.fetchDrawableOnThread(filepath, h.messageimage);
                            }
                        } else {
                            G.cmd.updatefilestatus(filehasharray.get(position), 0);
                            String thumb = G.cmd.getfile(3, filehasharray.get(position));
                            int loader = R.drawable.difaultimage;
                            imgLoader.DisplayImage(thumb, loader, h.messageimage, filehasharray.get(position));
                        }
                    }
                    else {
                        String thumb = G.cmd.getfile(3, filehasharray.get(position));
                        int loader = R.drawable.difaultimage;
                        imgLoader.DisplayImage(thumb, loader, h.messageimage, filehasharray.get(position));
                    }

                } else {
                    String thumb = G.cmd.getfile(3, filehasharray.get(position));
                    int loader = R.drawable.difaultimage;
                    imgLoader.DisplayImage(thumb, loader, h.messageimage, filehasharray.get(position));
                }
            }

        } else if (typearray.get(position).equals("3")) {
            h.llJoin.setVisibility(View.GONE);
            h.gifview.setVisibility(View.GONE);
            h.audioll.setVisibility(View.GONE);
            h.llwebsite.setVisibility(View.GONE);
            h.lytTimeInImage.setVisibility(View.GONE);
            h.ll_icons.setVisibility(View.VISIBLE);
            if (msgarray.get(position) != null && !msgarray.get(position).isEmpty() && !msgarray.get(position).equals("null") && !msgarray.get(position).equals("")) {
                buildertext = parseText(msgarray.get(position), true, position);
                h.messagetxv.setMovementMethod(LinkMovementMethod.getInstance());
                h.messagetxv.setText(buildertext, BufferType.SPANNABLE);
                h.ll_text.setVisibility(View.VISIBLE);
                Utils.setLayoutDirection(h.messagetxv, h.llbg);
            } else {
                h.ll_text.setVisibility(View.GONE);
            }
            h.llimage.setVisibility(View.GONE);
            h.llfiles.setVisibility(View.GONE);
            h.videoll.setVisibility(View.VISIBLE);
            h.imgvideo.setVisibility(View.VISIBLE);
            int isfileexist = G.cmd.isfileinfoexist(filehasharray.get(position));
            if (isfileexist != 0) {
                String status = G.cmd.getfile(4, filehasharray.get(position));
                if (status.equals("2") || status.equals("5") || msgtypearray.get(position).equals("2")) {

                    String filepath = G.cmd.getfile(6, filehasharray.get(position));

                    if (filepath != null && !filepath.isEmpty() && !filepath.equals("null") && !filepath.equals("")) {
                        File imgFile = new File(filepath);
                        if (imgFile.exists()) {
                            h.imgvideo.setImageResource(R.drawable.video_attach_big_play);
                        } else {
                            G.cmd.updatefilestatus(filehasharray.get(position), 0);
                            h.imgvideo.setImageResource(R.drawable.video_attach_big_dl);
                        }
                    } else {
                        h.imgvideo.setImageResource(R.drawable.video_attach_big_dl);
                    }
                } else {
                    h.imgvideo.setImageResource(R.drawable.video_attach_big_dl);
                }

            }

        } else if (typearray.get(position).equals("4")) {
            h.llJoin.setVisibility(View.GONE);
            h.videoll.setVisibility(View.GONE);
            h.gifview.setVisibility(View.GONE);
            h.lytTimeInImage.setVisibility(View.GONE);
            h.ll_icons.setVisibility(View.VISIBLE);
            if (msgarray.get(position) != null && !msgarray.get(position).isEmpty() && !msgarray.get(position).equals("null") && !msgarray.get(position).equals("")) {
                buildertext = parseText(msgarray.get(position), true, position);
                h.messagetxv.setMovementMethod(LinkMovementMethod.getInstance());
                h.messagetxv.setText(buildertext, BufferType.SPANNABLE);
                h.ll_text.setVisibility(View.VISIBLE);
                Utils.setLayoutDirection(h.messagetxv, h.llbg);
            } else {
                h.ll_text.setVisibility(View.GONE);
            }
            h.llimage.setVisibility(View.GONE);
            h.llfiles.setVisibility(View.GONE);
            h.audioll.setVisibility(View.VISIBLE);
            h.llwebsite.setVisibility(View.GONE);
            int isfileexist = G.cmd.isfileinfoexist(filehasharray.get(position));
            if (isfileexist != 0) {
                String status = G.cmd.getfile(4, filehasharray.get(position));
                if (status.equals("2") || status.equals("5") || msgtypearray.get(position).equals("2")) {
                    String filepath = G.cmd.getfile(6, filehasharray.get(position));

                    if (filepath != null && !filepath.isEmpty() && !filepath.equals("null") && !filepath.equals("")) {

                        File imgFile = new File(filepath);
                        if ( !imgFile.exists()) {
                            G.cmd.updatefilestatus(filehasharray.get(position), 0);
                        }
                    }
                }
            }
        } else if (typearray.get(position).equals("5")) {
            h.llJoin.setVisibility(View.GONE);
            h.audioll.setVisibility(View.GONE);
            h.gifview.setVisibility(View.GONE);
            h.llwebsite.setVisibility(View.GONE);
            h.llimage.setVisibility(View.GONE);
            h.videoll.setVisibility(View.GONE);
            h.llfiles.setVisibility(View.GONE);
            h.lytTimeInImage.setVisibility(View.GONE);
            h.ll_icons.setVisibility(View.VISIBLE);
            h.ll_text.setVisibility(View.VISIBLE);
            h.messagetxv.setText(msgarray.get(position));
            Utils.setLayoutDirection(h.messagetxv, h.llbg);
        } else if (typearray.get(position).equals("6")) {
            h.llJoin.setVisibility(View.GONE);
            h.audioll.setVisibility(View.GONE);
            h.llwebsite.setVisibility(View.GONE);
            h.llimage.setVisibility(View.GONE);
            h.gifview.setVisibility(View.GONE);
            h.videoll.setVisibility(View.GONE);
            h.llfiles.setVisibility(View.GONE);
            h.lytTimeInImage.setVisibility(View.GONE);
            h.ll_icons.setVisibility(View.VISIBLE);
            h.messagetxv.setVisibility(View.VISIBLE);
            h.messagetxv.setText(msgarray.get(position));
            Utils.setLayoutDirection(h.messagetxv, h.llbg);
        } else if (typearray.get(position).equals("7")) {
            h.llJoin.setVisibility(View.GONE);
            h.audioll.setVisibility(View.GONE);
            h.gifview.setVisibility(View.GONE);
            h.llwebsite.setVisibility(View.GONE);
            h.llimage.setVisibility(View.GONE);
            h.videoll.setVisibility(View.GONE);
            h.ll_text.setVisibility(View.GONE);
            h.lytTimeInImage.setVisibility(View.GONE);
            h.ll_icons.setVisibility(View.VISIBLE);
            h.llfiles.setVisibility(View.VISIBLE);
            if (msgarray.get(position) != null && !msgarray.get(position).isEmpty() && !msgarray.get(position).equals("null") && !msgarray.get(position).equals("")) {
                h.txtfilesname.setVisibility(View.VISIBLE);
                h.txtfilesname.setText(msgarray.get(position));
            } else {
                h.txtfilesname.setVisibility(View.GONE);
            }

            int isfileexist = G.cmd.isfileinfoexist(filehasharray.get(position));
            if (isfileexist != 0) {
                String status = G.cmd.getfile(4, filehasharray.get(position));

                if (status.equals("2") || status.equals("5") || msgtypearray.get(position).equals("2")) {
                    String filepath = G.cmd.getfile(6, filehasharray.get(position));
                    if (filepath != null && !filepath.isEmpty() && !filepath.equals("null") && !filepath.equals("")) {

                        File imgFile = new File(filepath);
                        if ( !imgFile.exists()) {
                            G.cmd.updatefilestatus(filehasharray.get(position), 0);
                            h.imgfiles.setImageResource(R.drawable.atach_mini_dl);
                        } else {
                            h.imgfiles.setImageResource(R.drawable.atach);
                        }
                    } else {
                        h.imgfiles.setImageResource(R.drawable.atach_mini_dl);
                    }

                } else {
                    h.imgfiles.setImageResource(R.drawable.atach_mini_dl);
                }
            }

        } else if (typearray.get(position).equals("8")) {
            h.llJoin.setVisibility(View.GONE);
            h.videoll.setVisibility(View.GONE);
            h.gifview.setVisibility(View.GONE);
            h.lytTimeInImage.setVisibility(View.GONE);
            h.ll_icons.setVisibility(View.VISIBLE);
            if (msgarray.get(position) != null && !msgarray.get(position).isEmpty() && !msgarray.get(position).equals("null") && !msgarray.get(position).equals("")) {
                buildertext = parseText(msgarray.get(position), true, position);
                h.messagetxv.setMovementMethod(LinkMovementMethod.getInstance());
                h.messagetxv.setText(buildertext, BufferType.SPANNABLE);
                h.ll_text.setVisibility(View.GONE);
                Utils.setLayoutDirection(h.messagetxv, h.llbg);
            } else {
                h.ll_text.setVisibility(View.GONE);
            }
            h.llimage.setVisibility(View.GONE);
            h.llfiles.setVisibility(View.GONE);
            h.audioll.setVisibility(View.VISIBLE);
            h.llwebsite.setVisibility(View.GONE);
            int isfileexist = G.cmd.isfileinfoexist(filehasharray.get(position));
            if (isfileexist != 0) {
                String status = G.cmd.getfile(4, filehasharray.get(position));
                if (status.equals("2") || status.equals("5") || msgtypearray.get(position).equals("2")) {
                    String filepath = G.cmd.getfile(6, filehasharray.get(position));
                    if (filepath != null && !filepath.isEmpty() && !filepath.equals("null") && !filepath.equals("")) {
                        File imgFile = new File(filepath);
                        if ( !imgFile.exists()) {
                            G.cmd.updatefilestatus(filehasharray.get(position), 0);
                        }
                    }
                }
            }
        }

        if (selectedItem.get(position) == true) {
            h.llbg.setBackgroundResource(R.drawable.chat_item_box_selected);

            if (msgtypearray.get(position).equals("1")) {
                h.img_treegonal.setImageResource(R.drawable.podreciveselected);
            } else {
                h.img_treegonal.setImageResource(R.drawable.pod3selected);
            }
        }

        if (highlightLayout.get(position)) {
            h.llbg.setBackgroundResource(R.drawable.chat_item_box_selected);

            if (msgtypearray.get(position).equals("1")) {
                h.img_treegonal.setImageResource(R.drawable.podreciveselected);
            } else {
                h.img_treegonal.setImageResource(R.drawable.pod3selected);
            }
        }

        //************************ Auto Download Start
        if (G.autoDownload.equals("0") && autoDownload.get(position)) { // auto download enable

            autoDownload.set(position, false); // for avoid from multiple run this code with each scroll

            if ( !typearray.get(position).equals("1") && !typearray.get(position).equals("5") && !typearray.get(position).equals("6")) { // if not text

                String status = G.cmd.getFileHashStatus(4, filehasharray.get(position));

                if (status.equals("0") || status.equals("1")) {

                    if (typearray.get(position).equals("2")) {

                        downloadImage(h, position, listener);

                    } else if (typearray.get(position).equals("3")) { // video

                        downloadVideo(h, position, listener);

                    } else if (typearray.get(position).equals("4") || typearray.get(position).equals("8")) { // music

                        downloadMusic(h, position, listener);

                    } else if (typearray.get(position).equals("7")) { // file

                        downloadFile(h, position, listener);
                    }
                }
            }
        }
        //************************ Auto Download End

        h.roww.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                changeHeader();
                resethighlight();

                VisibleDrawer();
                G.longPressItem = true; // for use at onbackpress in GroupChat
                if (selectedItem.get(position) == true) {
                    selectedItem.set(position, false);
                    notifyDataSetChanged();
                    HelperAnimation.CounterAnimRight(txtDrawerCounter);
                    selectCounter--;
                    if (selectCounter == 0) {

                        UnVisibleDrawer();
                        G.longPressItem = false;

                    }
                    if (selectCounter == 1) {
                        btnDrawerReplay.setVisibility(View.VISIBLE);
                    } else {
                        btnDrawerReplay.setVisibility(View.GONE);
                    }
                    txtDrawerCounter.setText(String.valueOf(selectCounter));
                    HelperAnimation.CounterAnimLeft(txtDrawerCounter);

                } else {
                    selectedItem.set(position, true);
                    notifyDataSetChanged();
                    HelperAnimation.CounterAnimLeft(txtDrawerCounter);

                    selectCounter++;
                    if (selectCounter > 0) {
                        sideDrawer.setVisibility(View.VISIBLE);
                    }
                    txtDrawerCounter.setText(String.valueOf(selectCounter));
                    HelperAnimation.CounterAnimRight(txtDrawerCounter);
                    if (selectCounter == 1) {
                        VisibleDrawer();
                        btnDrawerReplay.setVisibility(View.VISIBLE);

                    } else {
                        btnDrawerReplay.setVisibility(View.GONE);
                    }
                }
                selectItemVisible = true;
                return false;
            }
        });

        h.roww.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (selectItemVisible == true) {

                    if (selectedItem.get(position) == true) {

                        selectedItem.set(position, false);
                        notifyDataSetChanged();
                        HelperAnimation.CounterAnimRight(txtDrawerCounter);
                        selectCounter--;
                        if (selectCounter == 0) {
                            UnVisibleDrawer();
                            G.longPressItem = false;
                        }
                        if (selectCounter == 1) {
                            btnDrawerReplay.setVisibility(View.VISIBLE);
                        } else {
                            btnDrawerReplay.setVisibility(View.GONE);
                        }
                        txtDrawerCounter.setText(String.valueOf(selectCounter));
                        HelperAnimation.CounterAnimLeft(txtDrawerCounter);

                    } else {
                        selectedItem.set(position, true);
                        notifyDataSetChanged();
                        selectCounter++;
                        if (selectCounter > 0) {
                            sideDrawer.setVisibility(View.VISIBLE);
                        }
                        if (selectCounter == 1) {
                            VisibleDrawer();
                            btnDrawerReplay.setVisibility(View.VISIBLE);
                        } else {
                            btnDrawerReplay.setVisibility(View.GONE);
                        }
                        txtDrawerCounter.setText(String.valueOf(selectCounter));
                        HelperAnimation.CounterAnimRight(txtDrawerCounter);
                    }
                } else {
                    if (statusarray.get(position).equals("0")) { // Message Not Send And Resend Here
                        if (typearray.get(position).equals("1") || typearray.get(position).equals("5") || typearray.get(position).equals("6")) {
                            mService.sendgroupmessage(filemime.get(position), gchid, msgarray.get(position), avatar, typearray.get(position), null, null, null, replymessage, replyfilehash, replyfrom, "1", msgidarray.get(position));
                        } else {

                            if (doingDownloadOrUpload.get(position) == false) { // dar surati ke upload dar hal anjam nabud eghdam kon
                                doingDownloadOrUpload.set(position, true);
                                G.resendFilehashArrayGroupChat.add(filehasharray.get(position)); // moshkhas mishavad ke in filehash renesd mishavad

                                runUploaderListener.set(position, true);
                                String filePathResend = G.cmd.selectField("files", "filehash ='" + filehasharray.get(position) + "'", 6);

                                filePath = filePathResend;
                                filehash = filehasharray.get(position);

                                if (msgarray.get(position) != null) {
                                    gchatmsg = msgarray.get(position);
                                } else {
                                    gchatmsg = "FILE";
                                }
                                type = typearray.get(position);
                                new chechfileexist().execute(filehasharray.get(position), typearray.get(position), position + "", filemime.get(position));
                            }
                        }
                    } else {

                        if (typearray.get(position).equals("2")) {

                            int isfileexist = G.cmd.isfileinfoexist(filehasharray.get(position));
                            if (isfileexist != 0) {
                                String status = G.cmd.getfile(4, filehasharray.get(position));
                                if (status.equals("0") || status.equals("1")) {

                                    downloadImage(h, position, listener);

                                } else if (status.equals("2") || msgtypearray.get(position).equals("2") || status.equals("5")) {
                                    String filePathFile = G.cmd.selectField("files", "filehash ='" + filehasharray.get(position) + "'", 6);

                                    File imgFile = new File(filePathFile);
                                    int index = imgFile.getName().lastIndexOf('.') + 1;
                                    String ext = imgFile.getName().substring(index).toLowerCase();
                                    //MimeTypeMap mime = MimeTypeMap.getSingleton();
                                    //String type = mime.getMimeTypeFromExtension(ext);

                                    if ( !ext.equals("gif")) {
                                        new HelperDialogImageView(mContext, gchid, gchname, "groupchathistory", filehasharray.get(position));
                                    }
                                }
                            }

                        } else if (typearray.get(position).equals("3")) {
                            int isfileexist = G.cmd.isfileinfoexist(filehasharray.get(position));
                            if (isfileexist != 0) {

                                String status = G.cmd.getfile(4, filehasharray.get(position));
                                if (status.equals("0") || status.equals("1")) {

                                    downloadVideo(h, position, listener);

                                } else if (status.equals("2") || status.equals("5") || msgtypearray.get(position).equals("2")) {
                                    String filepath = G.cmd.getfile(6, filehasharray.get(position));
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(filepath));
                                    intent.setDataAndType(Uri.fromFile(new File(filepath)), "video/*");
                                    ((Activity) mContext).startActivity(intent);
                                }

                            }
                        } else if (typearray.get(position).equals("4")) {

                            int isfileexist = G.cmd.isfileinfoexist(filehasharray.get(position));
                            if (isfileexist != 0) {

                                String status = G.cmd.getfile(4, filehasharray.get(position));
                                if (status.equals("0") || status.equals("1")) {

                                    downloadMusic(h, position, listener);

                                } else if (status.equals("2") || status.equals("5") || msgtypearray.get(position).equals("2")) {

                                    String filepath = G.cmd.getfile(6, filehasharray.get(position));
                                    musicPlayer.startPlayer(filepath, gchname, filehasharray.get(position));
                                }

                            }

                        } else if (typearray.get(position).equals("7")) {
                            int isfileexist = G.cmd.isfileinfoexist(filehasharray.get(position));

                            if (isfileexist != 0) {

                                String status = G.cmd.getfile(4, filehasharray.get(position));
                                if (status.equals("0") || status.equals("1")) {

                                    downloadFile(h, position, listener);

                                } else if (status.equals("2") || status.equals("5") || msgtypearray.get(position).equals("2")) {

                                    String filepath = G.cmd.getfile(6, filehasharray.get(position));
                                    int index = msgarray.get(position).lastIndexOf('.') + 1;
                                    msgarray.get(position).substring(index).toLowerCase();
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_VIEW);
                                    File file = new File(filepath);
                                    intent.setDataAndType(Uri.fromFile(file), "*/*");
                                    ((Activity) mContext).startActivityForResult(HelperGetFileInformation.appropriateProgram(filepath), 10);
                                }
                            }
                        } else if (typearray.get(position).equals("8")) {

                            String status = G.cmd.getfile(4, filehasharray.get(position));
                            if (status.equals("0") || status.equals("1")) {

                                downloadMusic(h, position, listener);

                            } else if (status.equals("2") || status.equals("5") || msgtypearray.get(position).equals("2")) {
                                String filepath = G.cmd.getfile(6, filehasharray.get(position));
                                musicPlayer.startPlayer(filepath, gchname, filehasharray.get(position));
                            }

                        }
                    }
                }
            }
        });
    }


    private void downloadImage(ViewHolder h, int position, OnDownloadListener listener) {
        if (doingDownloadOrUpload.get(position) == false) { // dar surati ke download dar hal anjam nabud eghdam kon

            doingDownloadOrUpload.set(position, true);
            showIndeterminate.set(position, true);
            indeterminateProgress(position, h.messageimage);

            String url = G.cmd.getfile(2, filehasharray.get(position));

            String filepath;
            if (filemime.get(position) != null && !filemime.get(position).equals("")) {
                filepath = G.DIR_TEMP + "/" + filehasharray.get(position) + "." + filemime.get(position);
            } else {
                filepath = G.DIR_TEMP + "/" + filehasharray.get(position) + ".jpg";
            }

            h.downloaderService.downloadPath(url)
                    .listener(listener)
                    .message(msgarray.get(position))
                    .Authorization(basicAuth)
                    .filepath(filepath)
                    .filetype(typearray.get(position))
                    .stopDownload(false)
                    .imageView(h.messageimage)
                    .drawableManager(dm)
                    .filemim(filemime.get(position))
                    .fileHash(filehasharray.get(position))
                    .adapterGroup(GroupChatRecycleAdapter.this)
                    .model(2)
                    .setPosition(position)
                    .download(mContext);
        }
    }


    private void downloadVideo(ViewHolder h, int position, OnDownloadListener listener) {
        if (doingDownloadOrUpload.get(position) == false) { // dar surati ke download dar hal anjam nabud eghdam kon

            doingDownloadOrUpload.set(position, true);
            showIndeterminate.set(position, true);
            indeterminateProgress(position, h.imgvideo);

            String url = G.cmd.getfile(2, filehasharray.get(position));

            h.downloaderService.downloadPath(url)
                    .listener(listener)
                    .Authorization(basicAuth)
                    .filetype(typearray.get(position))
                    .filepath(G.DIR_TEMP + "/" + filehasharray.get(position) + ".mp4")
                    .stopDownload(false)
                    .imageView(h.imgvideo)
                    .fileHash(filehasharray.get(position))
                    .adapterGroup(GroupChatRecycleAdapter.this)
                    .model(2)
                    .download(mContext);
        }
    }


    private void downloadMusic(ViewHolder h, int position, OnDownloadListener listener) {
        if (doingDownloadOrUpload.get(position) == false) { // dar surati ke download dar hal anjam nabud eghdam kon

            doingDownloadOrUpload.set(position, true);
            showIndeterminate.set(position, true);
            indeterminateProgress(position, h.seekBar);

            String url = G.cmd.getfile(2, filehasharray.get(position));
            h.downloaderService.downloadPath(url)
                    .listener(listener)
                    .Authorization(basicAuth)
                    .filetype(typearray.get(position))
                    .filepath(G.DIR_TEMP + "/" + filehasharray.get(position) + ".mp3")
                    .stopDownload(false)
                    .fileHash(filehasharray.get(position))
                    .adapterGroup(GroupChatRecycleAdapter.this)
                    .model(2)
                    .download(mContext);
        }
    }


    private void downloadFile(ViewHolder h, int position, OnDownloadListener listener) {
        if (doingDownloadOrUpload.get(position) == false) { // dar surati ke download dar hal anjam nabud eghdam kon

            doingDownloadOrUpload.set(position, true);
            showIndeterminate.set(position, true);
            indeterminateProgress(position, h.imgfiles);

            String url = G.cmd.getfile(2, filehasharray.get(position));
            String fileType = msgarray.get(position);
            int index = fileType.lastIndexOf('.') + 1;
            String ext = fileType.substring(index).toLowerCase();

            String filepath;
            if (filemime.get(position) != null && !filemime.get(position).equals("")) {
                filepath = G.DIR_TEMP + "/" + filehasharray.get(position) + "." + filemime.get(position);
            } else {
                filepath = G.DIR_TEMP + "/" + filehasharray.get(position) + "." + ext;
            }

            h.downloaderService.downloadPath(url)
                    .listener(listener)
                    .Authorization(basicAuth)
                    .filetype(typearray.get(position))
                    .filepath(filepath)
                    .stopDownload(false)
                    .imageView(h.imgfiles)
                    .message(msgarray.get(position))
                    .filemim(filemime.get(position))
                    .fileHash(filehasharray.get(position))
                    .adapterGroup(GroupChatRecycleAdapter.this)
                    .model(2)
                    .download(mContext);
        }
    }


    /**
     * 
     * compare size of array and current position
     * 
     * @param position : current position
     * @param size : array size
     * 
     * @return
     *         true if size not biger than position , false if size biger than position
     */
    private boolean checkSize(int pos, int size) {
        boolean doWhile = false;
        if (size >= (pos + 1)) {
            doWhile = true;
        }
        return doWhile;
    }


    @Override
    public int getItemCount() {
        return idarray.size();
    }


    public void newPost(String filemim, String id, String msg, String status, String msgtime, String msgtype, String username, String filehash, String replyfilehash, String replymessage
                        , String replyfrom, String type, String msgsender, String msgsenderavatar, String msgid) {
        int pos = msgidarray.lastIndexOf(msgid);
        if (pos == -1) { // if this message not exist , add message
            idarray.add(id);
            msgarray.add(msg);
            statusarray.add(status);
            msgtimearray.add(msgtime);
            typearray.add(type);
            usernames.add(username);
            filehasharray.add(filehash);
            replyfilehasharray.add(replyfilehash);
            replymessagearray.add(replymessage);
            replyfromarray.add(replyfrom);
            msgtypearray.add(msgtype);
            msgsenderarray.add(msgsender);
            msgsenderavatararray.add(msgsenderavatar);
            msgidarray.add(msgid);
            selectedItem.add(false);
            doingDownloadOrUpload.add(false);
            runUploaderThread.add(true);
            runDownloaderThread.add(true);
            runDownloaderListener.add(true);
            runUploaderListener.add(true);
            filemime.add(filemim);
            highlightLayout.add(false);
            showIndeterminate.add(false);
            autoDownload.add(true);
            notifyItemInserted(msgarray.size());
        }
    }


    public void invisibleDrawer() {
        for (int i = 0; i < selectedItem.size(); i++) {
            selectedItem.set(i, false);
        }
        selectCounter = 0;
        notifyDataSetChanged();
        UnVisibleDrawer();
    }


    public void updatefileid(String filehash, String msgid) {
        int pos = filehasharray.indexOf(filehash);
        if (pos != -1) {
            msgidarray.set(pos, msgid);
            statusarray.set(pos, "1");
            G.cmd.updategroupmsgstatus(msgid, "1");
            notifyItemChanged(pos);
        }
    }


    public void updatefilestatus(String msgid, String status) {
        int pos = msgidarray.indexOf(msgid);
        if (pos != -1) {
            msgidarray.set(pos, msgid);
            statusarray.set(pos, status);
            notifyItemChanged(pos);
        }

    }


    public void clearHistory() {
        clearChat = true;
    }


    private SpannableStringBuilder parseText(String text, Boolean withLink, int position) {
        PageMessagingPopularFunction pm = new PageMessagingPopularFunction();
        SpannableStringBuilder builder = pm.parseText(text, "CHAT_LIST", withLink, mContext, position);
        return builder;
    }


    private void VisibleDrawer()
    {
        sideDrawer.setVisibility(View.VISIBLE);
        TranslateAnimation showAnim = new TranslateAnimation(300, 0, 0, 0);
        showAnim.setDuration(500);
        sideDrawer.setAnimation(showAnim);
    }


    private void UnVisibleDrawer() {
        selectItemVisible = false;
        selectCounter = 0;
        sideDrawer.setVisibility(View.GONE);
    }


    public void deletetrash() {

        int size = idarray.size();
        deletePositions.clear();
        for (int i = 0; i < size; i++) {
            if (selectedItem.get(i) == true) {
                deletePositions.add(i);
            }
        }

        for (int j = (deletePositions.size() - 1); j >= 0; j--) {
            int pos = deletePositions.get(j);

            G.cmd.deletemessagegroupchat(msgidarray.get(pos));

            idarray.remove(pos);
            msgarray.remove(pos);
            statusarray.remove(pos);
            msgtimearray.remove(pos);
            msgtypearray.remove(pos);
            usernames.remove(pos);
            filehasharray.remove(pos);
            replyfilehasharray.remove(pos);
            replymessagearray.remove(pos);
            replyfromarray.remove(pos);
            typearray.remove(pos);
            msgsenderarray.remove(pos);
            msgsenderavatararray.remove(pos);
            msgidarray.remove(pos);
            selectedItem.remove(pos);
            runUploaderThread.remove(pos);
            runDownloaderThread.remove(pos);
            runDownloaderListener.remove(pos);
            runUploaderListener.remove(pos);
            filemime.remove(pos);
            highlightLayout.remove(pos);
            showIndeterminate.remove(pos);
            autoDownload.remove(pos);
        }

        if (idarray.size() == 0) {
            G.cmd.updategroupchatrooms(gchid, "", "");
        } else {
            G.cmd.updategroupchatrooms(gchid, msgarray.get(idarray.size() - 1), msgtimearray.get(idarray.size() - 1));
        }

        selectCounter = 0;
        UnVisibleDrawer();
        G.longPressItem = false;
        notifyDataSetChanged();
    }


    private void indeterminateProgress(final int position, final ImageSquareProgressBar imageView) {
        final Handler handler = new Handler();
        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (showIndeterminate.get(position)) {
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            imageView.setIndeterminate(true);
                            imageView.setProgress(5);
                        }
                    });
                    try {
                        Thread.sleep(100);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }


    private void indeterminateProgress(final int position, final MusicSquareProgressBar imageView) {
        final Handler handler = new Handler();
        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (showIndeterminate.get(position)) {
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            imageView.setIndeterminate(true);
                            imageView.setProgress(5);
                        }
                    });
                    try {
                        Thread.sleep(100);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }


    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        if (popUp != null && popUp.isShowing()) {
            popUp.dismiss();
        }
        super.onDetachedFromRecyclerView(recyclerView);
    }


    public void updateMessageID(String newMsgID, String oldMsgID) {
        int pos = msgidarray.indexOf(oldMsgID);
        if (pos != -1) {
            msgidarray.set(pos, newMsgID);
        }

    }


    public void updateFileMessageID(String newMsgID, String fileHash) {
        int pos = filehasharray.indexOf(fileHash);
        if (pos != -1) {
            msgidarray.set(pos, newMsgID);
        }
    }


    public void delete(String msgid) {

        int pos = msgidarray.indexOf(msgid);
        if (pos != -1) {
            idarray.remove(pos);
            msgarray.remove(pos);
            statusarray.remove(pos);
            msgtimearray.remove(pos);
            msgtypearray.remove(pos);
            usernames.remove(pos);
            filehasharray.remove(pos);
            replyfilehasharray.remove(pos);
            replymessagearray.remove(pos);
            replyfromarray.remove(pos);
            typearray.remove(pos);
            msgsenderarray.remove(pos);
            msgsenderavatararray.remove(pos);
            msgidarray.remove(pos);
            selectedItem.remove(pos);
            runUploaderThread.remove(pos);
            runDownloaderThread.remove(pos);
            runDownloaderListener.remove(pos);
            runUploaderListener.remove(pos);
            filemime.remove(pos);
            highlightLayout.remove(pos);
            showIndeterminate.remove(pos);
            autoDownload.remove(pos);

            notifyDataSetChanged();
        }

    }


    public ArrayList<Integer> hashSearch(String hashText) {

        resetSelectedItem();

        hashPos.clear();
        int positions = 0;

        for (int i = 0; i < (msgarray.size()); i++) {
            if (msgarray.get(i).toLowerCase().indexOf(hashText.toLowerCase()) != -1) {
                hashPos.add(positions);
            }
            positions++;
        }
        return hashPos;
    }


    public void clearHighlightLayoutBackground(int position) {
        if (position >= highlightLayout.size()) {
            //Do Nothing
        } else {
            highlightLayout.set(position, false);
        }
        notifyItemChanged(position);
    }


    public void highlightLayoutBackground(int position) {
        if (position >= highlightLayout.size()) {
            //Do Nothing
        } else {
            highlightLayout.set(position, true);
        }
        notifyItemChanged(position);
    }


    public void resethighlight() {
        hashPos.clear();
        for (int i = 0; i < highlightLayout.size(); i++) {
            highlightLayout.set(i, false);
        }
        notifyDataSetChanged();
    }


    public void resetSelectedItem() {
        for (int i = 0; i < selectedItem.size(); i++) {
            selectedItem.set(i, false);
        }
        notifyDataSetChanged();

        UnVisibleDrawer();
        G.longPressItem = false;
    }


    public void changeHeader() {
        Intent intent = new Intent("changeHeaderGroupChat");
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }


    public void setmservice(MyService mserv) {
        mService = mserv;
    }

    private JSONParser jParser = new JSONParser();


    private class StructCheckFileExist {

        public String fileHashResend;
        public String fileTypeResend;
        public String fileUrlResend;
        public String fileThumbResend;
        public String filePositionResend;
        public String filemime;
    }


    /**
     * 
     * send file hash to server and check that this file is exist in server
     *
     */

    class chechfileexist extends AsyncTask<String, String, StructCheckFileExist> {

        boolean exists;
        String  fileurl;
        String  filethumb;
        String  fileHashResend;
        String  fileTypeResend;
        String  filePositionResend;
        String  fileMimeResend;
        String  fileMsgResend = "File";


        @Override
        protected StructCheckFileExist doInBackground(String... args) {

            StructCheckFileExist struct = new StructCheckFileExist();
            fileHashResend = args[0];
            fileTypeResend = args[1];
            filePositionResend = args[2];
            fileMimeResend = args[3];
            struct.fileHashResend = fileHashResend;
            struct.fileTypeResend = fileTypeResend;
            struct.filePositionResend = filePositionResend;
            struct.filemime = fileMimeResend;

            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                JSONObject jsonobj = jParser.getJSONFromUrl(G.checkfileexist + fileHashResend, params, "GET", basicAuth, null);
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
                                String fileurl = c.getString("url");
                                //filemime = result.getString("mime");
                                //fileextension = result.getString("extension");
                                String filethumb = c.getString("thumbnailLq");
                                struct.fileUrlResend = fileurl;
                                struct.fileThumbResend = filethumb;
                            }
                        }
                    } else {
                        ((Activity) mContext).runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(mContext, mContext.getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
                catch (JSONException e) {
                    ((Activity) mContext).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(mContext, mContext.getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            catch (Exception e) {
                ((Activity) mContext).runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(mContext, mContext.getString(R.string.internet_connection_problem_en), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            return struct;
        }


        @Override
        protected void onPostExecute(StructCheckFileExist result) {

            String fileHash = result.fileHashResend;
            String fileType = result.fileTypeResend;
            String fileUrl = result.fileUrlResend;
            String fileThumb = result.fileThumbResend;
            String filePosition = result.filePositionResend;
            String filemim = result.filemime;
            fileMsgResend = msgarray.get(Integer.parseInt(filePosition));

            if (exists == true) {
                G.cmd.updatefilestatus(fileHash, 5);
                mService.sendgroupmessage(filemim, gchid, fileMsgResend, avatar, fileType, fileHash, fileUrl, fileThumb, replymessage, replyfilehash, replyfrom, "1", null);

            } else {
                G.cmd.updatefilestatus(fileHash, 3);
                notifyItemChanged(Integer.parseInt(filePosition));
            }

            super.onPostExecute(result);
        }

    }

}