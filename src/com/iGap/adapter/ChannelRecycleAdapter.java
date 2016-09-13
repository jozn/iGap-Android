// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
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
import com.iGap.Channel;
import com.iGap.MusicPlayer;
import com.iGap.R;
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
import com.iGap.services.UploaderService;


/**
 * 
 * adapter baraye activitye {@link Channel} ke payamha ra baraye namayesh amade sazi mikonad
 *
 */

public class ChannelRecycleAdapter extends RecyclerView.Adapter<ChannelRecycleAdapter.ViewHolder> {

    private ArrayList<String>      filemime;
    private ArrayList<String>      idarray;
    private ArrayList<String>      msgarray;
    private ArrayList<String>      msgStatusArray;
    private ArrayList<String>      msgtimearray;
    private ArrayList<String>      msgtypearray;
    private ArrayList<String>      msgidarray;
    private ArrayList<String>      filehasharray;
    private ArrayList<String>      msgviewarray;
    private ArrayList<String>      msgsender;
    private ArrayList<String>      lastMsgIDArray;

    private ArrayList<Boolean>     doingDownloadOrUpload;
    private ArrayList<Boolean>     selectedItem;
    private ArrayList<Boolean>     runUploaderThread;
    private ArrayList<Boolean>     runDownloaderThread;
    private ArrayList<Boolean>     runDownloaderListener;
    private ArrayList<Boolean>     runUploaderListener;

    private ArrayList<Boolean>     autoDownload      = new ArrayList<Boolean>();
    private ArrayList<Boolean>     showIndeterminate = new ArrayList<Boolean>();
    private ArrayList<Boolean>     highlightLayout   = new ArrayList<Boolean>();
    private ArrayList<Integer>     hashPos           = new ArrayList<Integer>();
    private ArrayList<String>      idGetVisit        = new ArrayList<String>();
    private ArrayList<String>      visitNumber       = new ArrayList<String>();

    private String                 channelActive;
    private String                 lastFileMessageID;
    private String                 basicAuth;
    private String                 textcolor;
    private String                 textsize;
    private String                 day;
    private String                 month;
    private String                 year;
    private String                 channelName;
    private String                 channeluid;
    private String                 hijri;
    private String                 idList;

    private boolean                load;
    private boolean                loadBottom;
    private boolean                selectItemVisible;
    private boolean                clearChat         = false;
    private boolean                getVisit          = true;

    private int                    selectCounter;
    private int                    offsetPosition    = 0;
    private int                    count             = 0;
    private int                    lastPostition     = 100000;

    private ImageLoader1           il;
    private ImageLoader            imgLoader;
    private SpannableStringBuilder buildertext;

    private ViewHolder             viewholder;
    private View                   mainView;
    private TextView               txtDrawerCounter;
    private Thread                 thread;
    private DrawableManager        dm;
    private PopupWindow            popUp;
    private LinearLayout           sideDrawer;
    private Handler                handler;
    private Dialog                 dialog;
    private DrawableManagerDialog  dmDialog;
    private ImageLoaderDialog      imgLoaderDialog;
    private MusicPlayer            musicPlayer;
    private Context                mContext;


    /**
     * 
     * view holder for hold a row of item in it
     *
     */

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView               txtwdescription;
        public TextView               txtwtitle;
        public TextView               txtEye;
        public TextView               txtSeenNumber;
        public TextView               txttimeday;
        public TextView               messagetxv;
        public TextView               txtreplayfrom;
        public TextView               txtreplaymessage;
        public TextView               txtfilesname;
        public TextView               datetxv;
        public TextView               messagestatusicon;
        public TextView               txtJoin;
        public TextView               txtNewMsg;

        public ImageView              imgwicon;
        public ImageView              img_avatar;
        public ImageView              imgreply;
        public ImageView              img_tregonal;

        public ImageSquareProgressBar messageimage;
        public ImageSquareProgressBar video;
        public ImageSquareProgressBar imgfiles;

        public MusicSquareProgressBar seekBar;

        public Button                 btnDelete;
        public Button                 btnAlarm;
        public Button                 btnclearhistory;

        public LinearLayout           ll_text;
        public LinearLayout           llwebsite;
        public LinearLayout           llcontainer;
        public LinearLayout           lltime;
        public LinearLayout           llbg;
        public LinearLayout           audioll;
        public LinearLayout           videoll;
        public LinearLayout           llreplay;
        public LinearLayout           llfiles;
        public LinearLayout           llJoin;
        public LinearLayout           llNewMsg;
        public LinearLayout           ll_icons;
        public LinearLayout           lytTimeInImage;

        public View                   roww, viewdate;
        public DownloaderService      downloaderService;
        public GifMovieView           gifview;

        public TextView               image_check1;
        public TextView               image_time;
        public FrameLayout            llimage;


        public ViewHolder(View row) {
            super(row);
            roww = row;

            image_check1 = (TextView) row.findViewById(R.id.lis_txt_image_check1);
            image_check1.setTypeface(G.fontAwesome);

            llimage = (FrameLayout) row.findViewById(R.id.lis_ll_image);
            image_time = (TextView) row.findViewById(R.id.lis_txt_time);

            downloaderService = new DownloaderService();

            // =======ImageView
            ImageSquareProgressBar.mode = 1;
            messageimage = (ImageSquareProgressBar) row.findViewById(R.id.message_image);
            video = (ImageSquareProgressBar) row.findViewById(R.id.vv_video);
            imgfiles = (ImageSquareProgressBar) row.findViewById(R.id.img_files);
            seekBar = (MusicSquareProgressBar) row.findViewById(R.id.seekBar1);

            // =====================Mozaffari Edit End
            txtSeenNumber = (TextView) row.findViewById(R.id.txt_seen_number);

            // Main Layout
            llcontainer = (LinearLayout) row.findViewById(R.id.ll_container);

            // timelayout
            lltime = (LinearLayout) row.findViewById(R.id.ll_time);
            llJoin = (LinearLayout) row.findViewById(R.id.ll_join);
            txttimeday = (TextView) row.findViewById(R.id.txt_timeday);
            viewdate = row.findViewById(R.id.view_date);

            // new message layout
            llNewMsg = (LinearLayout) row.findViewById(R.id.ll_new_msg);
            txtNewMsg = (TextView) row.findViewById(R.id.txt_new_msg);

            // gif
            gifview = (GifMovieView) row.findViewById(R.id.gif1);
            // bg layout
            llbg = (LinearLayout) row.findViewById(R.id.ll_bg);
            ll_text = (LinearLayout) row.findViewById(R.id.ll_text);
            ll_icons = (LinearLayout) row.findViewById(R.id.ll_icons);
            lytTimeInImage = (LinearLayout) row.findViewById(R.id.lyt_time_in_image);

            llwebsite = (LinearLayout) row.findViewById(R.id.ll_website);
            // Audio layout
            audioll = (LinearLayout) row.findViewById(R.id.audio_ll);

            // Video layout
            videoll = (LinearLayout) row.findViewById(R.id.video_ll);

            // textv layout
            messagetxv = (TextView) row.findViewById(R.id.message_txv);
            txtJoin = (TextView) row.findViewById(R.id.txt_join);
            llreplay = (LinearLayout) row.findViewById(R.id.ll_replay);
            imgreply = (ImageView) row.findViewById(R.id.img_reply);
            txtreplayfrom = (TextView) row.findViewById(R.id.txt_replay_from);
            txtreplaymessage = (TextView) row.findViewById(R.id.txt_replay_message);
            txtwtitle = (TextView) row.findViewById(R.id.txt_wtitle);
            txtwdescription = (TextView) row.findViewById(R.id.txt_wdescription);

            // files layout
            llfiles = (LinearLayout) row.findViewById(R.id.ll_files);
            txtfilesname = (TextView) row.findViewById(R.id.txt_filesname);

            messagestatusicon = (TextView) row.findViewById(R.id.message_status_icon);
            datetxv = (TextView) row.findViewById(R.id.date_txv);
            img_tregonal = (ImageView) row.findViewById(R.id.imageView_threegonal);
            imgwicon = (ImageView) row.findViewById(R.id.img_wicon);

            txtEye = (TextView) row.findViewById(R.id.txt_eye);
            txtEye.setTypeface(G.fontAwesome);
            txtSeenNumber.setVisibility(View.VISIBLE);
            txtEye.setVisibility(View.VISIBLE);

            seekBar.setClickable(false);
            seekBar.setFocusable(false);
            seekBar.setEnabled(false);

        }
    }


    public ChannelRecycleAdapter(ArrayList<String> filemime1,
                                 ArrayList<String> idarray1, ArrayList<String> msgarray1,
                                 ArrayList<String> msgseenarray1, ArrayList<String> msgStatusArray1,
                                 ArrayList<String> msgtimearray1, ArrayList<String> msgtypearray1,
                                 ArrayList<String> msgidarray1, ArrayList<Boolean> selectedItem1,
                                 ArrayList<String> filehasharray1, ArrayList<String> msgviewarray1,
                                 ArrayList<String> msgsender1,
                                 ArrayList<Boolean> runUploaderThread1,
                                 ArrayList<Boolean> runDownloaderThread1,
                                 ArrayList<Boolean> runDownloaderListener1,
                                 ArrayList<Boolean> runUploaderListener1, Context context,
                                 String year1, String month1, String day1, ImageLoader1 il1,
                                 View view, DrawableManager dm1, DrawableManagerDialog dmDialog1,
                                 Handler handler1, Boolean selectItemVisible1,
                                 TextView txtDrawerCounter1, LinearLayout sideDrawer1,
                                 String lastFileMessageID1, Boolean smileClicked1,
                                 String channelName1, String channeluid1, String channelActive1,
                                 LinearLayout layoutMusic, boolean load1, boolean loadBottom1) {
        filemime = filemime1;
        mContext = context;
        idarray = idarray1;
        msgarray = msgarray1;
        msgStatusArray = msgStatusArray1;
        msgtimearray = msgtimearray1;
        msgtypearray = msgtypearray1;
        msgidarray = msgidarray1;
        msgsender = msgsender1;
        selectedItem = selectedItem1;
        filehasharray = filehasharray1;
        msgviewarray = msgviewarray1;
        runUploaderThread = runUploaderThread1;
        runDownloaderThread = runDownloaderThread1;
        runDownloaderListener = runDownloaderListener1;
        runUploaderListener = runUploaderListener1;
        dm = dm1;
        dmDialog = dmDialog1;
        channelActive = channelActive1;
        txtDrawerCounter = txtDrawerCounter1;
        sideDrawer = sideDrawer1;
        handler = handler1;
        channelName = channelName1;
        channeluid = channeluid1;
        thread = new Thread();
        selectItemVisible = selectItemVisible1;
        year = year1;
        month = month1;
        lastFileMessageID = lastFileMessageID1;
        day = day1;
        il = il1;
        load = load1;
        loadBottom = loadBottom1;

        hijri = G.hijriDate;

        resetPercent();

        basicAuth = G.basicAuth;
        mainView = view;
        textcolor = G.textColor;
        textsize = G.textSize;
        imgLoader = new ImageLoader(mContext, basicAuth);
        imgLoaderDialog = new ImageLoaderDialog(mContext, basicAuth);

        lastMsgIDArray = new ArrayList<String>();
        for (int i = 0; i < lastMsgIDArray.size(); i++) {
            lastMsgIDArray.add("");
        }

        doingDownloadOrUpload = new ArrayList<Boolean>();
        for (int i = 0; i < msgidarray.size(); i++) {
            doingDownloadOrUpload.add(false);
            highlightLayout.add(false);
            showIndeterminate.add(false);
            autoDownload.add(true);
        }

        if (channelActive.equals("1")) {

            if (msgarray.size() > 0) {

                getVisit = false;
                idGetVisit.clear();
                visitNumber.clear();

                idList = msgidarray.get(msgidarray.size() - 1);

                for (int i = (msgarray.size() - 1); i > (msgarray.size() - 20)
                        && i >= 0; i--) {

                    if (msgidarray.get(i) != null) {
                        if ( !msgidarray.get(i).equals("kick")
                                && !msgidarray.get(i).equals("first")
                                && !msgidarray.get(i).equals("ChannelInvite")
                                && !msgidarray.get(i).equals("InviteAgain")
                                && !msgidarray.get(i).equals("inf")
                                && msgidarray.get(i) != null) {
                            lastPostition = i;
                            idList = idList + "," + msgidarray.get(i);
                            count++;
                        }
                    }
                }

                offsetPosition = offsetPosition + count;
                getVisit = true;
                new GetView().execute();
            }
        }

        musicPlayer = new MusicPlayer(mContext, layoutMusic, "small",
                basicAuth, G.cmd, channeluid, "3");

        try {
            if (MusicPlayer.place != null) {
                if (MusicPlayer.place.equals(channelName) && MusicPlayer.mp != null) {
                    layoutMusic.setVisibility(View.VISIBLE);
                    MusicPlayer.updateView();
                }
            }
        }
        catch (Exception e) {}

    }


    private void resetPercent() {
        for (int i = 0; i < filehasharray.size(); i++) {
            G.cmd.updateFilePercents(filehasharray.get(i));
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_singlechat, parent, false);
        viewholder = new ViewHolder(v);
        return viewholder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder h, final int position) {

        if (load && position == 0) { // residan be avalin item mojud
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent intent = new Intent("LoadChannel");
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                }
            }, 1000);
        }

        if (loadBottom && (position + 1) == idarray.size()) { // residan be akharin item mojud
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent intent = new Intent("LoadBottomChannel");
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                }
            }, 1000);
        }

        if (channelActive.equals("1")) {

            if (msgarray.size() > 15) {
                if (lastPostition >= position && getVisit) {
                    getVisit = false;
                    idGetVisit.clear();
                    visitNumber.clear();

                    if (msgarray.size() > 0) {

                        idList = msgidarray.get(position);

                        for (int i = (msgarray.size() - 1 - offsetPosition); i > (msgarray
                                .size() - 10 - offsetPosition) && i >= 0; i--) {
                            if ( !msgidarray.get(i).equals("kick")
                                    && !msgidarray.get(i).equals("first")
                                    && !msgidarray.get(i).equals("ChannelInvite")
                                    && !msgidarray.get(i).equals("InviteAgain")
                                    && !msgidarray.get(i).equals("inf")
                                    && msgidarray.get(i) != null) {
                                lastPostition = i;
                                idList = idList + "," + msgidarray.get(i);
                                count++;
                            }
                        }

                        offsetPosition = offsetPosition + count;
                        getVisit = true;
                        new GetView().execute();
                    }
                }
            }
        }

        mainView.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        return true;
                    }
                }
                return false;
            }
        });

        if (msgtypearray.get(position).equals("3")) {
            h.video.setImageResource(R.drawable.video_attach_big);
        } else if (msgtypearray.get(position).equals("7")) {
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

                            if (msgtypearray.get(position).equals("2")) {
                                h.messageimage.setIndeterminate(false);
                                h.messageimage.setProgress(percent);
                            } else if (msgtypearray.get(position).equals("3")) {
                                h.video.setIndeterminate(false);
                                h.video.setProgress(percent);
                            } else if (msgtypearray.get(position).equals("4")) {
                                h.seekBar.setIndeterminate(false);
                                h.seekBar.setProgress(percent);
                            } else if (msgtypearray.get(position).equals("7")) {
                                h.imgfiles.setIndeterminate(false);
                                h.imgfiles.setProgress(percent);
                            } else if (msgtypearray.get(position).equals("8")) {
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

        if ( !msgtypearray.get(position).equals("1") && !msgtypearray.get(position).equals("5") && !msgtypearray.get(position).equals("6") && !msgtypearray.get(position).equals("100")) {
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

                                    if (msgtypearray.get(position).equals("2")) {
                                        h.messageimage.setIndeterminate(false);
                                        h.messageimage.setProgress(percent);
                                    } else if (msgtypearray.get(position).equals("3")) {
                                        h.video.setIndeterminate(false);
                                        h.video.setColor("#1aaeac");
                                        h.video.setProgress(percent);
                                    } else if (msgtypearray.get(position).equals("4")) {
                                        h.seekBar.setIndeterminate(false);
                                        h.seekBar.setProgress(percent);
                                    } else if (msgtypearray.get(position).equals("7")) {
                                        h.imgfiles.setIndeterminate(false);
                                        h.imgfiles.setColor("#1aaeac");
                                        h.imgfiles.setProgress(percent);
                                    } else if (msgtypearray.get(position).equals("8")) {
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
                            msgStatusArray.set(position, "2");
                            notifyItemChanged(position);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                new SendChannelMessageFromUploader().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, filehasharray.get(position), lastFileDatabaseID, msgarray.get(position), position + "", filemime.get(position));
                            } else {
                                new SendChannelMessageFromUploader().execute(filehasharray.get(position), lastFileDatabaseID, msgarray.get(position), position + "", filemime.get(position));
                            }

                        }
                    }
                };

                String filePathFile = G.cmd.selectField("files", "filehash ='" + filehasharray.get(position) + "'", 6);

                new UploaderService()
                        .listener(uploadListener)
                        .fileHash(filehasharray.get(position))
                        .chatMessage(msgarray.get(position))
                        .type(msgtypearray.get(position))
                        .messageType(3)
                        .filepath(filePathFile)
                        .Authorization(basicAuth)
                        .lastFileDatabaseID(lastFileMessageID)
                        .service(null)
                        .upload(mContext);

            } else if (filestatus.equals("1")) {

                if (checkSize(position, runDownloaderListener.size())) {
                    runDownloaderListener.set(position, false);
                }

                final Handler handler = new Handler();
                thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        while ( !clearChat && checkSize(position, filehasharray.size()) && checkSize(position, runDownloaderThread.size()) && runDownloaderThread.get(position)) {

                            handler.post(new Runnable() {

                                @Override
                                public void run() {
                                    String y = G.cmd.selectPercent(filehasharray.get(position));
                                    int percent = Integer.parseInt(y);

                                    if (percent > 0 && percent != 100) { // agar darsad bozorgtar az sefr bud natige migirim ke dar hale download ast , chon dar ebteda hameye darsadha ra  sefr shode.
                                        doingDownloadOrUpload.set(position, true);
                                        showIndeterminate.set(position, false);
                                    }

                                    if (msgtypearray.get(position).equals("2")) {
                                        h.messageimage.setIndeterminate(false);
                                        h.messageimage.setProgress(percent);
                                    } else if (msgtypearray.get(position).equals("3")) {
                                        h.video.setIndeterminate(false);
                                        h.video.setProgress(percent);
                                    } else if (msgtypearray.get(position).equals("4")) {
                                        h.seekBar.setIndeterminate(false);
                                        h.seekBar.setProgress(percent);
                                    } else if (msgtypearray.get(position).equals("7")) {
                                        h.imgfiles.setIndeterminate(false);
                                        h.imgfiles.setProgress(percent);
                                    } else if (msgtypearray.get(position).equals("8")) {
                                        h.seekBar.setIndeterminate(false);
                                        h.seekBar.setProgress(percent);
                                    }

                                    if (percent >= 100) {
                                        runDownloaderThread.set(position, false);
                                        G.cmd.updatefilestatus(filehasharray.get(position), 2);
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

                String percent = G.cmd.selectPercent(filehasharray.get(position));
                if ( !percent.equals("1000")) { // not uploaded file
                    final Handler handler = new Handler();
                    thread = new Thread(new Runnable() {

                        @Override
                        public void run() {

                            while ( !clearChat && checkSize(position, runUploaderThread.size()) && runUploaderThread.get(position)) {
                                handler.post(new Runnable() {

                                    @Override
                                    public void run() {

                                        int percent = Integer.parseInt(G.cmd.selectPercent(filehasharray.get(position)));

                                        if (percent > 0 && percent != 100) { // agar darsad bozorgtar az sefr bud natige migirim ke dar hale download ast , chon dar ebteda hameye darsadha ra  sefr shode.
                                            doingDownloadOrUpload.set(position, true);
                                        }

                                        if (runUploaderListener.get(position)) {
                                            runUploaderListener.set(position, false);
                                        }

                                        if (msgtypearray.get(position).equals("2")) {
                                            h.messageimage.setIndeterminate(false);
                                            h.messageimage.setProgress(percent);
                                        } else if (msgtypearray.get(position).equals("3")) {
                                            h.video.setIndeterminate(false);
                                            h.video.setColor("#1aaeac");
                                            h.video.setProgress(percent);
                                        } else if (msgtypearray.get(position).equals("4")) {
                                            h.seekBar.setIndeterminate(false);
                                            h.seekBar.setProgress(percent);
                                        } else if (msgtypearray.get(position).equals("7")) {
                                            h.imgfiles.setIndeterminate(false);
                                            h.imgfiles.setColor("#1aaeac");
                                            h.imgfiles.setProgress(percent);
                                        } else if (msgtypearray.get(position).equals("8")) {
                                            h.seekBar.setIndeterminate(false);
                                            h.seekBar.setProgress(percent);
                                        }

                                        if (percent >= 100) {
                                            runUploaderThread.set(position, false);
                                            G.cmd.updatefilestatus(filehasharray.get(position), 5);
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
                } else {}
            }
        }

        h.txtSeenNumber.setText(msgviewarray.get(position) + "");

        if (G.SelectedLanguage.equals("fa")) {
            h.txtSeenNumber.setText(HelperGetTime.stringNumberToPersianNumberFormat(h.txtSeenNumber.getText().toString()));
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

        if (Channel.visiblePosition != -1) {
            if (position == Channel.visiblePosition) {
                h.llNewMsg.setVisibility(View.VISIBLE);
                h.txtNewMsg.setText("new message received");
            } else {
                h.llNewMsg.setVisibility(View.GONE);
            }
        } else {
            h.llNewMsg.setVisibility(View.GONE);
        }

        if (msgStatusArray.get(position).equals("0")) { // Recived Message
            h.messagestatusicon.setVisibility(View.GONE);
        } else if (msgStatusArray.get(position).equals("1")) { // Send Message
            h.messagestatusicon.setVisibility(View.VISIBLE);
            h.messagestatusicon.setTypeface(G.fontAwesome);
            h.messagestatusicon.setText(R.string.fa_repeat);
            h.messagestatusicon.setTextColor(mContext.getResources().getColor(R.color.red_light));
            h.datetxv.setTextColor(mContext.getResources().getColor(R.color.light_green));
        } else if (msgStatusArray.get(position).equals("2")) { // Success For Sended Message
            h.messagestatusicon.setVisibility(View.GONE);
        }

        h.image_check1.setText(h.messagestatusicon.getText());
        h.image_check1.setTextColor(h.messagestatusicon.getTextColors());
        h.image_check1.setVisibility(h.messagestatusicon.getVisibility());

        if (msgtypearray.get(position).equals("100")) {
            h.audioll.setVisibility(View.GONE);
            h.llimage.setVisibility(View.GONE);
            h.videoll.setVisibility(View.GONE);
            h.llfiles.setVisibility(View.GONE);
            h.messagetxv.setVisibility(View.GONE);
            h.llcontainer.setVisibility(View.GONE);
            h.llJoin.setVisibility(View.VISIBLE);
            h.txtJoin.setText(msgarray.get(position));
            h.llwebsite.setVisibility(View.GONE);
            h.gifview.setVisibility(View.GONE);
            h.lytTimeInImage.setVisibility(View.GONE);
            h.ll_icons.setVisibility(View.VISIBLE);
        } else if (msgtypearray.get(position).equals("1")) {
            h.llJoin.setVisibility(View.GONE);
            h.audioll.setVisibility(View.GONE);
            h.llimage.setVisibility(View.GONE);
            h.videoll.setVisibility(View.GONE);
            h.llfiles.setVisibility(View.GONE);
            h.gifview.setVisibility(View.GONE);
            h.lytTimeInImage.setVisibility(View.GONE);
            h.ll_icons.setVisibility(View.VISIBLE);
            h.messagetxv.setVisibility(View.VISIBLE);
            buildertext = parseText(msgarray.get(position), position);

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
                                public void complet(Boolean result,
                                                    final String message) {

                                    ((Activity) mContext)
                                            .runOnUiThread(new Runnable() {

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
                                                        // catch block
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

        } else if (msgtypearray.get(position).equals("2")) {
            h.llJoin.setVisibility(View.GONE);
            h.audioll.setVisibility(View.GONE);
            h.llwebsite.setVisibility(View.GONE);
            h.gifview.setVisibility(View.GONE);

            Log.i("LOG", "message : " + msgarray.get(position));

            if (msgarray.get(position) != null && !msgarray.get(position).isEmpty() && !msgarray.get(position).equals("null") && !msgarray.get(position).equals("")) {
                buildertext = parseText(msgarray.get(position), position);
                h.messagetxv.setMovementMethod(LinkMovementMethod.getInstance());
                h.messagetxv.setText(buildertext, BufferType.SPANNABLE);
                h.lytTimeInImage.setVisibility(View.GONE);
                h.ll_icons.setVisibility(View.VISIBLE);
                h.messagetxv.setVisibility(View.VISIBLE);
                Utils.setLayoutDirection(h.messagetxv, h.ll_text);
            } else {
                h.messagetxv.setVisibility(View.GONE);
                h.lytTimeInImage.setVisibility(View.VISIBLE);
                h.ll_icons.setVisibility(View.GONE);
            }

            h.videoll.setVisibility(View.GONE);
            h.llfiles.setVisibility(View.GONE);
            h.llimage.setVisibility(View.VISIBLE);

            int isfileexist = G.cmd.isfileinfoexist(filehasharray.get(position));
            if (isfileexist != 0) {
                String status = G.cmd.getfile(4, filehasharray.get(position));
                if (status.equals("2") || status.equals("5") || msgsender.get(position).equals("1")) {
                    String filepath = G.cmd.getfile(6, filehasharray.get(position));

                    if (filepath != null && !filepath.isEmpty() && !filepath.equals("null") && !filepath.equals("")) {

                        File imgFile = new File(filepath);
                        if (imgFile.exists()) {

                            //MimeTypeMap mime = MimeTypeMap.getSingleton();
                            int index = imgFile.getName().lastIndexOf('.') + 1;
                            String ext = imgFile.getName().substring(index).toLowerCase();
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
                    } else {
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

        } else if (msgtypearray.get(position).equals("3")) {
            h.llJoin.setVisibility(View.GONE);
            h.audioll.setVisibility(View.GONE);
            h.llwebsite.setVisibility(View.GONE);
            h.gifview.setVisibility(View.GONE);
            h.lytTimeInImage.setVisibility(View.GONE);
            h.ll_icons.setVisibility(View.VISIBLE);
            if (msgarray.get(position) != null && !msgarray.get(position).isEmpty() && !msgarray.get(position).equals("null") && !msgarray.get(position).equals("")) {
                buildertext = parseText(msgarray.get(position), position);
                h.messagetxv.setMovementMethod(LinkMovementMethod.getInstance());
                h.messagetxv.setText(buildertext, BufferType.SPANNABLE);
                Utils.setLayoutDirection(h.messagetxv, h.llbg);
            } else {
                h.messagetxv.setVisibility(View.GONE);
            }
            h.llimage.setVisibility(View.GONE);
            h.llfiles.setVisibility(View.GONE);
            h.videoll.setVisibility(View.VISIBLE);

            int isfileexist = G.cmd.isfileinfoexist(filehasharray.get(position));
            if (isfileexist != 0) {
                String status = G.cmd.getfile(4, filehasharray.get(position));
                if (status.equals("2") || status.equals("5") || msgsender.get(position).equals("1")) {
                    String filepath = G.cmd.getfile(6, filehasharray.get(position));

                    if (filepath != null && !filepath.isEmpty() && !filepath.equals("null") && !filepath.equals("")) {
                        File imgFile = new File(filepath);
                        if (imgFile.exists()) {
                            h.video.setImageResource(R.drawable.video_attach_big_play);
                        } else {
                            G.cmd.updatefilestatus(filehasharray.get(position), 0);
                            h.video.setImageResource(R.drawable.video_attach_big_dl);
                        }
                    } else {
                        h.video.setImageResource(R.drawable.video_attach_big_dl);
                    }
                } else {
                    h.video.setImageResource(R.drawable.video_attach_big_dl);
                }

            }

        } else if (msgtypearray.get(position).equals("4")) {
            h.llJoin.setVisibility(View.GONE);
            h.videoll.setVisibility(View.GONE);
            h.gifview.setVisibility(View.GONE);
            h.lytTimeInImage.setVisibility(View.GONE);
            h.ll_icons.setVisibility(View.VISIBLE);
            if (msgarray.get(position) != null && !msgarray.get(position).isEmpty() && !msgarray.get(position).equals("null") && !msgarray.get(position).equals("")) {
                buildertext = parseText(msgarray.get(position), position);
                h.messagetxv.setMovementMethod(LinkMovementMethod.getInstance());
                h.messagetxv.setText(buildertext, BufferType.SPANNABLE);
                Utils.setLayoutDirection(h.messagetxv, h.llbg);
            } else {
                h.messagetxv.setVisibility(View.GONE);
            }
            h.llimage.setVisibility(View.GONE);
            h.llfiles.setVisibility(View.GONE);
            h.audioll.setVisibility(View.VISIBLE);
            h.llwebsite.setVisibility(View.GONE);
            int isfileexist = G.cmd.isfileinfoexist(filehasharray.get(position));
            if (isfileexist != 0) {
                String status = G.cmd.getfile(4, filehasharray.get(position));
                if (status.equals("2") || status.equals("5") || msgsender.get(position).equals("1")) {
                    String filepath = G.cmd.getfile(6, filehasharray.get(position));
                    if (filepath != null && !filepath.isEmpty() && !filepath.equals("null") && !filepath.equals("")) {

                        File imgFile = new File(filepath);
                        if ( !imgFile.exists()) {
                            G.cmd.updatefilestatus(filehasharray.get(position), 0);
                        }
                    }
                }
            }
        } else if (msgtypearray.get(position).equals("5")) {
            h.llJoin.setVisibility(View.GONE);
            h.audioll.setVisibility(View.GONE);
            h.llwebsite.setVisibility(View.GONE);
            h.llimage.setVisibility(View.GONE);
            h.videoll.setVisibility(View.GONE);
            h.llfiles.setVisibility(View.GONE);
            h.gifview.setVisibility(View.GONE);
            h.lytTimeInImage.setVisibility(View.GONE);
            h.ll_icons.setVisibility(View.VISIBLE);
            h.messagetxv.setVisibility(View.VISIBLE);
            h.messagetxv.setText(msgarray.get(position));
            Utils.setLayoutDirection(h.messagetxv, h.llbg);
        } else if (msgtypearray.get(position).equals("6")) {
            h.llJoin.setVisibility(View.GONE);
            h.audioll.setVisibility(View.GONE);
            h.llwebsite.setVisibility(View.GONE);
            h.llimage.setVisibility(View.GONE);
            h.videoll.setVisibility(View.GONE);
            h.llfiles.setVisibility(View.GONE);
            h.gifview.setVisibility(View.GONE);
            h.lytTimeInImage.setVisibility(View.GONE);
            h.ll_icons.setVisibility(View.VISIBLE);
            h.messagetxv.setVisibility(View.VISIBLE);
            h.messagetxv.setText(msgarray.get(position));
            Utils.setLayoutDirection(h.messagetxv, h.llbg);
        } else if (msgtypearray.get(position).equals("7")) {
            h.llJoin.setVisibility(View.GONE);
            h.audioll.setVisibility(View.GONE);
            h.llwebsite.setVisibility(View.GONE);
            h.gifview.setVisibility(View.GONE);
            h.llimage.setVisibility(View.GONE);
            h.videoll.setVisibility(View.GONE);
            h.messagetxv.setVisibility(View.GONE);
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

                if (status.equals("2") || status.equals("5") || msgsender.get(position).equals("1")) {
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

        } else if (msgtypearray.get(position).equals("8")) {
            h.llJoin.setVisibility(View.GONE);
            h.videoll.setVisibility(View.GONE);
            h.gifview.setVisibility(View.GONE);
            if (msgarray.get(position) != null && !msgarray.get(position).isEmpty() && !msgarray.get(position).equals("null") && !msgarray.get(position).equals("")) {
                buildertext = parseText(msgarray.get(position), position);
                h.messagetxv.setMovementMethod(LinkMovementMethod.getInstance());
                h.messagetxv.setText(buildertext, BufferType.SPANNABLE);
                Utils.setLayoutDirection(h.messagetxv, h.llbg);
            } else {
                h.messagetxv.setVisibility(View.GONE);
            }
            h.llimage.setVisibility(View.GONE);
            h.llfiles.setVisibility(View.GONE);
            h.llwebsite.setVisibility(View.GONE);
            h.lytTimeInImage.setVisibility(View.GONE);
            h.ll_icons.setVisibility(View.VISIBLE);
            h.audioll.setVisibility(View.VISIBLE);
            int isfileexist = G.cmd.isfileinfoexist(filehasharray.get(position));
            if (isfileexist != 0) {
                String status = G.cmd.getfile(4, filehasharray.get(position));
                if (status.equals("2") || status.equals("5") || msgsender.get(position).equals("1")) {
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

        if (selectedItem.get(position) || highlightLayout.get(position)) {
            h.llbg.setBackgroundResource(R.drawable.chat_item_box_selected);
        } else {
            h.llbg.setBackgroundResource(R.drawable.chat_item_box);
        }

        //************************ Auto Download Start
        if (G.autoDownload.equals("0") && autoDownload.get(position)) { // auto download enable

            autoDownload.set(position, false); // for avoid from multiple run this code with each scroll

            if ( !msgtypearray.get(position).equals("1") && !msgtypearray.get(position).equals("5") && !msgtypearray.get(position).equals("6")) { // if not text

                String status = G.cmd.getFileHashStatus(4, filehasharray.get(position));

                if (status.equals("0") || status.equals("1")) {

                    if (msgtypearray.get(position).equals("2")) { // image

                        downloadImage(h, position, listener);

                    } else if (msgtypearray.get(position).equals("3")) { // video

                        downloadVideo(h, position, listener);

                    } else if (msgtypearray.get(position).equals("4") || msgtypearray.get(position).equals("8")) { // music

                        downloadMusic(h, position, listener);

                    } else if (msgtypearray.get(position).equals("7")) { // file

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

                G.longPressItem = true; // for use at onbackpress in Channel
                if (selectedItem.get(position) == true) {
                    selectedItem.set(position, false);
                    notifyDataSetChanged();
                    HelperAnimation.CounterAnimRight(txtDrawerCounter);
                    selectCounter--;
                    if (selectCounter == 0) {
                        UnVisibleDrawer();
                        G.longPressItem = false;
                    }

                    txtDrawerCounter.setText(String.valueOf(selectCounter)); //HelperViewPagerAnimation
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
                }
                selectItemVisible = true;
                return false;
            }
        });

        h.roww.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (selectItemVisible == true) { // itemha dar hale select shodan baraye copy , forward ya delete hastand 

                    if (selectedItem.get(position) == true) {
                        selectedItem.set(position, false);
                        notifyDataSetChanged();
                        HelperAnimation.CounterAnimRight(txtDrawerCounter);
                        selectCounter--;
                        if (selectCounter == 0) {
                            UnVisibleDrawer();
                            G.longPressItem = false;
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
                        }
                        txtDrawerCounter.setText(String.valueOf(selectCounter));
                        HelperAnimation.CounterAnimRight(txtDrawerCounter);

                    }
                } else {
                    if (msgStatusArray.get(position).equals("1")) { // ma ferestandeye payam dar kanal hastim
                        runUploaderListener.set(position, true);
                        if (msgtypearray.get(position).equals("1") || msgtypearray.get(position).equals("5") || msgtypearray.get(position).equals("6")) {
                            new SendChannelMessage().execute(idarray.get(position), msgarray.get(position));
                        } else {
                            if (doingDownloadOrUpload.get(position) == false) { // dar surati ke upload dar halanjam nabud eghdam kon
                                doingDownloadOrUpload.set(position, true);
                                lastFileMessageID = idarray.get(position);
                                G.resendFilehashArrayChannel.add(filehasharray.get(position));
                                new chechfileexist().execute(filehasharray.get(position), msgarray.get(position), position + "", idarray.get(position), filemime.get(position));
                            }
                        }
                    } else {

                        if (msgtypearray.get(position).equals("2")) { // image

                            int isfileexist = G.cmd.isfileinfoexist(filehasharray.get(position));
                            if (isfileexist != 0) {
                                String status = G.cmd.getfile(4, filehasharray.get(position));
                                if (status.equals("0") || status.equals("1")) { // download nashode 

                                    downloadImage(h, position, listener);

                                } else if (status.equals("2") || status.equals("5") || msgsender.get(position).equals("1")) {
                                    String filePathFile = G.cmd.selectField("files", "filehash ='" + filehasharray.get(position) + "'", 6);
                                    File imgFile = new File(filePathFile);
                                    int index = imgFile.getName().lastIndexOf('.') + 1;
                                    String ext = imgFile.getName().substring(index).toLowerCase();

                                    if (ext.equals("gif")) {

                                    } else {
                                        new HelperDialogImageView(mContext, channeluid, channelName, "channelhistory", filehasharray.get(position));
                                    }

                                }
                            }

                        } else if (msgtypearray.get(position).equals("3")) { // video
                            int isfileexist = G.cmd.isfileinfoexist(filehasharray.get(position));
                            if (isfileexist != 0) {

                                String status = G.cmd.getfile(4, filehasharray.get(position));
                                if (status.equals("0") || status.equals("1")) {// download nashode

                                    downloadVideo(h, position, listener);

                                } else if (status.equals("2") || status.equals("5") || msgsender.get(position).equals("1")) {
                                    String filepath = G.cmd.getfile(6, filehasharray.get(position));
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(filepath));
                                    intent.setDataAndType(Uri.fromFile(new File(filepath)), "video/*");
                                    ((Activity) mContext).startActivity(intent);

                                }
                            }
                        } else if (msgtypearray.get(position).equals("4")) { // music

                            int isfileexist = G.cmd.isfileinfoexist(filehasharray.get(position));

                            if (isfileexist != 0) {
                                String status = G.cmd.getfile(4, filehasharray.get(position));
                                if (status.equals("0") || status.equals("1")) {// download nashode

                                    downloadMusic(h, position, listener);

                                } else if (status.equals("2") || status.equals("5") || msgsender.get(position).equals("1")) {
                                    String filepath = G.cmd.getfile(6, filehasharray.get(position));
                                    musicPlayer.startPlayer(filepath, channelName, filehasharray.get(position));
                                }
                            }

                        } else if (msgtypearray.get(position).equals("7")) { // file

                            int isfileexist = G.cmd.isfileinfoexist(filehasharray.get(position));
                            if (isfileexist != 0) {

                                String status = G.cmd.getfile(4, filehasharray.get(position));
                                if (status.equals("0") || status.equals("1")) {// download nashode

                                    downloadFile(h, position, listener);

                                } else if (status.equals("2") || status.equals("5") || msgsender.get(position).equals("1")) {

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

                        } else if (msgtypearray.get(position).equals("8")) { // recorded audio

                            String status = G.cmd.getfile(4, filehasharray.get(position));

                            if (status.equals("0") || status.equals("1")) {

                                downloadMusic(h, position, listener);

                            } else if (status.equals("2") || status.equals("5") || msgsender.get(position).equals("1")) {
                                String filepath = G.cmd.getfile(6, filehasharray.get(position));
                                MusicPlayer.startPlayer(filepath, channelName, filehasharray.get(position));
                            }
                        }
                    }
                }
            }
        });
    }


    private void downloadImage(ViewHolder h, int position, OnDownloadListener listener) {
        if (doingDownloadOrUpload.get(position) == false) {// dar surati ke upload dar halanjam nabudeghdam kon

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

            h.downloaderService
                    .downloadPath(url)
                    .listener(listener)
                    .Authorization(basicAuth)
                    .filepath(filepath)
                    .filetype(msgtypearray.get(position))
                    .stopDownload(false)
                    .imageView(h.messageimage)
                    .drawableManager(dm)
                    .filemim(filemime.get(position))
                    .adapterChannel(ChannelRecycleAdapter.this)
                    .model(3)
                    .fileHash(filehasharray.get(position))
                    .setPosition(position)
                    .download(mContext);
        }

    }


    private void downloadVideo(ViewHolder h, int position, OnDownloadListener listener) {
        if (doingDownloadOrUpload.get(position) == false) { // dar surati ke upload dar halanjam nabudeghdam kon

            doingDownloadOrUpload.set(position, true);
            showIndeterminate.set(position, true);
            indeterminateProgress(position, h.video);

            String url = G.cmd.getfile(2, filehasharray.get(position));

            h.downloaderService
                    .downloadPath(url)
                    .listener(listener)
                    .Authorization(basicAuth)
                    .filetype(msgtypearray.get(position))
                    .filepath(G.DIR_TEMP + "/" + filehasharray.get(position) + ".mp4")
                    .stopDownload(false)
                    .imageView(h.video)
                    .fileHash(filehasharray.get(position))
                    .adapterChannel(ChannelRecycleAdapter.this)
                    .model(3)
                    .download(mContext);
        }
    }


    private void downloadMusic(ViewHolder h, int position, OnDownloadListener listener) {

        if (doingDownloadOrUpload.get(position) == false) {// dar surati ke upload dar halanjam nabudeghdam kon

            doingDownloadOrUpload.set(position, true);
            showIndeterminate.set(position, true);
            indeterminateProgress(position, h.seekBar);

            String url = G.cmd.getfile(2, filehasharray.get(position));
            h.downloaderService
                    .downloadPath(url)
                    .listener(listener)
                    .Authorization(basicAuth)
                    .filetype(msgtypearray.get(position))
                    .filepath(G.DIR_TEMP + "/" + filehasharray.get(position) + ".mp3")
                    .stopDownload(false)
                    .fileHash(filehasharray.get(position))
                    .adapterChannel(ChannelRecycleAdapter.this)
                    .model(3)
                    .download(mContext);
        }
    }


    private void downloadFile(ViewHolder h, int position, OnDownloadListener listener) {
        if (doingDownloadOrUpload.get(position) == false) { // dar surati ke upload dar halanjam nabudeghdam kon

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

            h.downloaderService
                    .downloadPath(url)
                    .listener(listener)
                    .Authorization(basicAuth)
                    .filetype(msgtypearray.get(position))
                    .filepath(filepath)
                    .stopDownload(false)
                    .imageView(h.imgfiles)
                    .message(msgarray.get(position))
                    .filemim(filemime.get(position))
                    .fileHash(filehasharray.get(position))
                    .adapterChannel(ChannelRecycleAdapter.this)
                    .model(3)
                    .download(mContext);
        }

    }


    @Override
    public int getItemCount() {
        return idarray.size();
    }


    private SpannableStringBuilder parseText(String text, int position) {
        PageMessagingPopularFunction pm = new PageMessagingPopularFunction();
        SpannableStringBuilder builder = pm.parseText(text, "CHAT_LIST", true, mContext, position);
        return builder;
    }


    private void VisibleDrawer() {
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


    public void newPost(String mime, String databaseID, String msg,
                        String status, String msgtime, String msgtype, String view,
                        String filehash, String replyfilehash, String replymessage,
                        String replyfrom, String sender, String msgid, String lastMessageID) {

        filemime.add(mime);
        lastMsgIDArray.add(lastMessageID);
        idarray.add(databaseID);
        msgarray.add(msg);
        msgStatusArray.add(status);
        msgtimearray.add(msgtime);
        msgtypearray.add(msgtype);
        msgviewarray.add(view);
        filehasharray.add(filehash);
        msgsender.add(sender);
        msgidarray.add(msgid);
        doingDownloadOrUpload.add(false);
        runUploaderThread.add(true);
        runDownloaderThread.add(true);
        runDownloaderListener.add(true);
        runUploaderListener.add(true);
        selectedItem.add(false);
        highlightLayout.add(false);
        showIndeterminate.add(false);
        autoDownload.add(true);
        notifyItemInserted(msgarray.size());

    }


    public void invisibleDrawer() {

        for (int i = 0; i < selectedItem.size(); i++) {
            selectedItem.set(i, false);
        }
        notifyDataSetChanged();
        UnVisibleDrawer();
    }


    public void updateMessageStatus(String lastMsgID, String serverTime) {
        int pos = idarray.lastIndexOf(lastMsgID);
        if (pos != -1) {
            msgStatusArray.set(pos, "2");// 2 == message successfully sended!
            msgtimearray.set(pos, serverTime);
            notifyItemChanged(pos);
        }
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

    private JSONParser jParser = new JSONParser();


    private class StructResendChannelMessage {

        public boolean success;
        public String  message;
        public String  serverTime;
        public String  resendMsgID;
    }


    class SendChannelMessage extends AsyncTask<String, String, StructResendChannelMessage> {

        @Override
        protected StructResendChannelMessage doInBackground(String... args) {
            StructResendChannelMessage structChannel = new StructResendChannelMessage();
            try {

                jParser = new JSONParser();

                String channelResendMessageID = args[0];
                String channelMessage = args[1];
                String serverTime = "";

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("text", channelMessage));
                JSONObject jsonobj = jParser.getJSONFromUrl(G.createchannel + "/" + channeluid + "/posts", params, "POST", basicAuth, null);
                boolean messageSuccess = false;

                try {
                    String statuscode = jsonobj.getString("statuscode");
                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);

                    messageSuccess = json.getBoolean(G.TAG_SUCCESS);

                    if (messageSuccess == true) {

                        JSONObject result = json.getJSONObject("result");
                        String id = result.getString("id");
                        serverTime = result.getString("createAt");
                        serverTime = HelperGetTime.convertWithSingleTime(serverTime, G.utcMillis);
                        // =====set struct value
                        structChannel.resendMsgID = channelResendMessageID;
                        structChannel.message = channelMessage;
                        structChannel.success = messageSuccess;
                        structChannel.serverTime = serverTime;

                        G.cmd.deleteChannelMessage(channelResendMessageID);
                        deleteItem(channelResendMessageID);

                        // ezafe kardane mojadae file
                        G.cmd.addchannelhistory(channeluid, id, channelMessage, serverTime, "1", "", "1", "0", "1", "2", "");
                        addItem("", id, channelMessage, serverTime, "1", "");

                        G.cmd.updatechannels(channeluid, channelMessage, serverTime);
                    } else {

                        if (statuscode.equals("400")) {
                            ((Activity) mContext).runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(mContext, mContext.getString(R.string.illegal_characters), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else if (statuscode.equals("403")) {
                            String errorStatus = "";
                            JSONObject result = json.getJSONObject("result");
                            errorStatus = result.getString("errorStatus");
                            if (errorStatus.equals("1")) {
                                ((Activity) mContext)
                                        .runOnUiThread(new Runnable() {

                                            @Override
                                            public void run() {
                                                Toast.makeText(mContext, mContext.getString(R.string.channel_deleted), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else if (errorStatus.equals("2")) {
                                ((Activity) mContext)
                                        .runOnUiThread(new Runnable() {

                                            @Override
                                            public void run() {
                                                Toast.makeText(mContext, mContext.getString(R.string.channel_closed), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                ((Activity) mContext)
                                        .runOnUiThread(new Runnable() {

                                            @Override
                                            public void run() {
                                                Toast.makeText(mContext, mContext.getString(R.string.message_error1_en), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        } else {
                            ((Activity) mContext).runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(mContext, mContext.getString(R.string.message_error1_en), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                }
                catch (final JSONException e) {
                    ((Activity) mContext).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(mContext, mContext.getString(R.string.message_error1_en), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            catch (Exception e) {
                ((Activity) mContext).runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(mContext, mContext.getString(R.string.message_error2_en), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            return structChannel;
        }


        @Override
        protected void onPostExecute(StructResendChannelMessage result) {
            boolean messageSuccess = result.success;
            //String serverTime = result.serverTime;
            //String channelMessage = result.message;
            //String resendMsgID = result.resendMsgID;

            if (messageSuccess == true) {
                notif();
            }
        }
    }


    private void channelNewPost(String uid, String lastmsg, String lastdate) {
        Intent intent = new Intent("newPostChannelList");
        intent.putExtra("uid", uid);
        intent.putExtra("lastmsg", lastmsg);
        intent.putExtra("lastdate", lastdate);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

        // ===Send New Post To PageAll
        Intent intentAll = new Intent("newPostAll");
        intentAll.putExtra("MODEL", "3"); // 3 = channel
        intentAll.putExtra("UID", uid);
        intentAll.putExtra("LAST_MSG", lastmsg);
        intentAll.putExtra("LAST_DATE", lastdate);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intentAll);
    }


    public void getChannelPostForViewer(ArrayList<String> ids,
                                        ArrayList<String> hashs, ArrayList<String> extentions,
                                        ArrayList<String> texts, ArrayList<String> visits,
                                        ArrayList<String> mainTimes, ArrayList<String> msgTypes) {

        for (int i = 0; i < ids.size(); i++) {
            idarray.add(channeluid);

            msgidarray.add(ids.get(i)); // 1
            filehasharray.add(hashs.get(i));// 2
            filemime.add(extentions.get(i));
            msgarray.add(texts.get(i));
            msgviewarray.add(visits.get(i));
            msgStatusArray.add("2");
            msgsender.add("0");
            msgtimearray.add(mainTimes.get(i));
            msgtypearray.add(msgTypes.get(i));

            runUploaderThread.add(true);
            runDownloaderThread.add(true);
            runDownloaderListener.add(true);
            runUploaderListener.add(true);
            doingDownloadOrUpload.add(true);

            selectedItem.add(false);
            highlightLayout.add(false);
            showIndeterminate.add(false);
            autoDownload.add(true);
        }

        notif();

    }


    private class StructCheckFile {

        public boolean exists;
        public String  fileHash;
        public String  message;
        public String  idInDb;
        public String  msgPosition;
        public String  filemime;

    }


    /**
     * 
     * send file hash to server and check that this file is exist in server
     *
     */

    class chechfileexist extends AsyncTask<String, String, StructCheckFile> {

        @Override
        protected StructCheckFile doInBackground(String... args) {

            boolean exists;
            String fileHash = args[0];
            String channelMsg = args[1];
            String position = args[2];

            StructCheckFile struct = new StructCheckFile();
            struct.msgPosition = position;
            struct.idInDb = args[3];
            struct.filemime = args[4];

            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                JSONObject jsonobj;
                jsonobj = jParser.getJSONFromUrl(G.checkfileexist + fileHash, params, "GET", basicAuth, null);

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
                            struct.exists = exists;

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
                        Toast.makeText(mContext, mContext.getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            return struct;
        }


        @Override
        protected void onPostExecute(StructCheckFile result) {

            boolean exists = result.exists;
            String fileHash = result.fileHash;
            String message = result.message;
            String idInDb = result.idInDb;
            String position = result.msgPosition;
            String mime = result.filemime;

            if (exists == true) {
                G.cmd.updatefilestatus(fileHash, 5);
                new SendChannelMessageFromUploader().execute(fileHash, idInDb, message, position, mime);
            } else {
                G.cmd.updatefilestatus(fileHash, 3);
                notifyItemChanged(Integer.parseInt(position));
            }
            super.onPostExecute(result);
        }
    }


    /**
     * 
     * gereftane tedade dide shodane payamha
     *
     */

    class GetView extends AsyncTask<String, String, String> {

        boolean success;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... args) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                JSONObject jsonobj = jParser.getJSONFromUrl(G.addmembertochannel + channeluid + "/posts/" + idList + "/visit", params, "GET", basicAuth, null);

                try {
                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);
                    success = json.getBoolean("success");

                    if (success) {

                        JSONArray result = json.getJSONArray("result");

                        int resultSize = result.length();

                        for (int i = 0; i < resultSize; i++) {
                            JSONObject response = result.getJSONObject(i);
                            String id = response.getString("id");
                            String visit = response.getString("visit");

                            idGetVisit.add(id);
                            visitNumber.add(visit);
                        }

                    } else {

                        String errorStatus = "";
                        JSONObject result = json.getJSONObject("result");
                        errorStatus = result.getString("errorStatus");

                        if (errorStatus.equals("1")) {
                            ((Activity) mContext).runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(mContext, mContext.getString(R.string.channel_deleted), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else if (errorStatus.equals("2")) {
                            ((Activity) mContext).runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(mContext, mContext.getString(R.string.channel_closed), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            ((Activity) mContext).runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(mContext, mContext.getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                }
                catch (final JSONException e) {
                    e.printStackTrace();
                }
            }
            catch (final Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String file_url) {

            for (int i = 0; i < (idGetVisit.size()); i++) {
                int idPosition = msgidarray.lastIndexOf(idGetVisit.get(i));
                if (idPosition != -1) {
                    msgviewarray.set(idPosition, visitNumber.get(i));
                }
            }
            getVisit = true;

            if (success) {
                notif();
            }
        }
    }


    private class StructSendMessageFromUploader {

        public boolean success;
        public String  channelMessage;
        public String  msgPosition;
        public String  serverTime;
        public boolean resend;
    }


    private class SendChannelMessageFromUploader extends
            AsyncTask<String, String, StructSendMessageFromUploader> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected StructSendMessageFromUploader doInBackground(String... args) {

            StructSendMessageFromUploader struct = new StructSendMessageFromUploader();

            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();

                String channelFilehash = args[0];
                String msgFileDatabaseID = args[1];
                String channelMessage = args[2];
                String position = args[3];
                String mime = args[4];

                params.add(new BasicNameValuePair("hash", channelFilehash));
                params.add(new BasicNameValuePair("text", channelMessage));

                JSONObject jsonobj = jParser.getJSONFromUrl(G.createchannel + "/" + channeluid + "/posts", params, "POST", basicAuth, null);

                try {
                    String statuscode = jsonobj.getString("statuscode");
                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);

                    boolean success2 = json.getBoolean(G.TAG_SUCCESS);
                    struct.channelMessage = channelMessage;
                    struct.msgPosition = position;
                    struct.success = success2;

                    if (success2 == true) {

                        JSONObject result = json.getJSONObject("result");
                        String id = result.getString("id");
                        String serverTime = result.getString("createAt");
                        serverTime = HelperGetTime.convertWithSingleTime(serverTime, G.utcMillis);
                        struct.serverTime = serverTime;

                        boolean resend = false;

                        int pos = G.resendFilehashArrayChannel
                                .indexOf(channelFilehash);

                        if (pos != -1) {
                            resend = true;
                        }

                        struct.resend = resend;

                        if (resend) {

                            String msgType = G.cmd.selectField("channelhistory", "filehash = '" + channelFilehash + "'", 5);

                            // delete in file
                            G.cmd.deleteChannelMessage(msgFileDatabaseID);
                            deleteItem(msgFileDatabaseID);

                            // ezafe kardane mojadae file
                            G.cmd.addchannelhistory(channeluid, id, channelMessage, serverTime, msgType, channelFilehash, "1", "0", "1", "2", mime);
                            addItem(mime, id, channelMessage, serverTime, msgType, channelFilehash);

                        } else {
                            G.cmd.updateChannelIdAndTime(channeluid, id, serverTime, "3", msgFileDatabaseID);
                        }

                    } else {

                        if (statuscode.equals("400")) {
                            ((Activity) mContext).runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(mContext, mContext.getString(R.string.illegal_characters), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else if (statuscode.equals("403")) {
                            String errorStatus = "";
                            JSONObject result = json.getJSONObject("result");
                            errorStatus = result.getString("errorStatus");
                            if (errorStatus.equals("1")) {
                                ((Activity) mContext)
                                        .runOnUiThread(new Runnable() {

                                            @Override
                                            public void run() {
                                                Toast.makeText(mContext, mContext.getString(R.string.channel_deleted), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else if (errorStatus.equals("2")) {
                                ((Activity) mContext)
                                        .runOnUiThread(new Runnable() {

                                            @Override
                                            public void run() {
                                                Toast.makeText(mContext, mContext.getString(R.string.channel_closed), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                ((Activity) mContext)
                                        .runOnUiThread(new Runnable() {

                                            @Override
                                            public void run() {
                                                Toast.makeText(mContext, mContext.getString(R.string.message_error1_en), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        } else {
                            ((Activity) mContext).runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(mContext, mContext.getString(R.string.message_error1_en), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                }
                catch (final JSONException e) {
                    ((Activity) mContext).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(mContext, mContext.getString(R.string.channel_error2_en) + e, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            catch (final Exception e) {
                ((Activity) mContext).runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(mContext, mContext.getString(R.string.channel_error2_en) + e, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            return struct;
        }


        @Override
        protected void onPostExecute(StructSendMessageFromUploader result) {

            boolean success = result.success;
            String channelMessage = result.channelMessage;
            String serverTime = result.serverTime;
            String position = result.msgPosition;
            boolean resend = result.resend;

            if (success) {
                if (resend) {
                    notif();
                } else {
                    channelNewPost(channeluid, channelMessage, serverTime);
                    msgtimearray.set(Integer.parseInt(position), serverTime); // set serverTime
                    notifyItemChanged(Integer.parseInt(position));
                }
            }
        }
    }


    private void addItem(String mime, String msgID, String channelMessage, String serverTime, String msgType, String channelFilehash) {

        filemime.add(mime);
        idarray.add(channeluid);
        msgarray.add(channelMessage);
        msgStatusArray.add("2");
        msgtimearray.add(serverTime);
        msgtypearray.add(msgType);
        msgviewarray.add("0");
        filehasharray.add(channelFilehash);
        msgsender.add("1");
        msgidarray.add(msgID);
        runUploaderThread.add(true);
        runDownloaderThread.add(true);
        runDownloaderListener.add(true);
        runUploaderListener.add(true);
        doingDownloadOrUpload.add(true);

        selectedItem.add(false);
        highlightLayout.add(false);
        showIndeterminate.add(false);
        autoDownload.add(true);
    }


    private void notif() {
        notifyDataSetChanged();
    }


    private void deleteItem(String id) {

        int pos = idarray.indexOf(id);
        if (pos != -1) {
            filemime.remove(pos);
            idarray.remove(pos);
            msgarray.remove(pos);
            msgStatusArray.remove(pos);
            msgtimearray.remove(pos);
            msgtypearray.remove(pos);
            msgviewarray.remove(pos);
            filehasharray.remove(pos);
            msgsender.remove(pos);
            msgidarray.remove(pos);
            runUploaderThread.remove(pos);
            runDownloaderThread.remove(pos);
            runDownloaderListener.remove(pos);
            runUploaderListener.remove(pos);
            doingDownloadOrUpload.remove(pos);
            highlightLayout.remove(pos);
            showIndeterminate.remove(pos);
            autoDownload.remove(pos);
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

    private boolean checkSize(int position, int size) {
        boolean doWhile = false;
        if (size >= (position + 1)) {
            doWhile = true;
        }
        return doWhile;
    }


    public void clearHighlightLayoutBackground(int position) {
        if (position >= highlightLayout.size()) {} else {
            highlightLayout.set(position, false);
        }
        notifyItemChanged(position);
    }


    public void highlightLayoutBackground(int position) {
        if (position >= highlightLayout.size()) {} else {
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


    public String getToppestMsgID() {
        return msgidarray.get(0);
    }


    private void changeHeader() {
        Intent intent = new Intent("changeHeaderChannel");
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

}