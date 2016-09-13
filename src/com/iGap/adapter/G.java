// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.adapter;

import io.fabric.sdk.android.Fabric;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.iGap.R;
import com.iGap.helpers.HelperGetTime;
import com.iGap.instruments.CheckDatabase;
import com.iGap.instruments.ImageLoader1;
import com.iGap.instruments.UtileProg;
import com.iGap.instruments.database;


public class G extends Application {

    //************************* Fixed variable
    public static final String      DIR_SDCARD                    = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String      DIR_APP                       = DIR_SDCARD + "/iGap";
    public static final String      DIR_IMAGES                    = DIR_APP + "/images";
    public static final String      DIR_VIDEOS                    = DIR_APP + "/videos";
    public static final String      DIR_AUDIOS                    = DIR_APP + "/audios";
    public static final String      DIR_DOCUMENTS                 = DIR_APP + "/documents";
    public static final String      DIR_RECORD                    = DIR_APP + "/record";
    public static final String      DIR_DIALOG                    = DIR_APP + "/.DialogImage";
    public static final String      DIR_TEMP                      = DIR_APP + "/.temp";
    public static final String      DIR_HIDE                      = DIR_APP + "/.hide";
    public static final String      DIR_CROP                      = DIR_APP + "/.crop";
    private static final String     PROPERTY_ID                   = "UA-69727923-3";
    public static final String      UserAgent                     = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36";

    public static final String      TAG_SUCCESS                   = "success";
    public static final String      TAG_RESULT                    = "result";
    public static final String      TAG_STATUS_CODE               = "statuscode";
    public static final String      TAG_LQ                        = "lq";
    public static final String      TAG_HQ                        = "hq";

    public static final int         DOWNLOAD_BUFFER_SIZE          = 8 * 1024;
    public static final int         COPY_BUFFER_SIZE              = 8 * 1024;
    public static final int         NOTIFICATION_ID               = 1395000;
    public static final int         CAMERA_PIC_REQUEST            = 0;
    public static final int         SELECT_PICTURE                = 1;
    public static final int         request_code_TAKE_PICTURE     = 11;
    public static final int         request_code_PICK_IMAGE       = 12;
    public static final int         request_code_VIDEO_CAPTURED   = 13;
    public static final int         request_code_audi             = 14;
    public static final int         request_code_FILE             = 15;
    public static final int         request_code_cantact_phone    = 16;
    public static final int         request_code_position         = 17;
    public static final int         request_code_paint            = 18;
    public static final int         MEDIA_TYPE_IMAGE              = 20;

    //************************* All urls
    public static String            url                           = "https://secure.igap.im/api1/";
    public static String            detectlocation                = url + "info/country";
    public static String            register                      = url + "users/register";
    public static String            verification                  = url + "users/verify";
    public static String            registerprofile               = url + "users/profile";
    public static String            allcontact                    = url + "users/contacts";
    public static String            currenttime                   = url + "test/time";
    public static String            checkfileexist                = url + "files/";
    public static String            creategroupchat               = url + "chat-rooms";
    public static String            addmembertogroupchat          = url + "chat-rooms/";
    public static String            addmembertochannel            = url + "channels/";
    public static String            getlocation                   = url + "test/location";
    public static String            userblacklist                 = url + "users/black-list/";
    public static String            deletegroup                   = url + "chat-rooms/";
    public static String            createchannel                 = url + "channels";
    public static String            channelsearch                 = url + "channels/search/";
    public static String            chatroommembers               = url + "chat-rooms/";
    public static String            deleteavatar                  = url + "users/avatars/";
    public static String            selfdelete                    = url + "users/self-remove/";
    public static String            SelectedLanguage              = "en";

    //************************* Global Values
    public static boolean           downloadingNewVersion         = false;
    public static boolean           appIsShowing                  = false;
    public static boolean           longPressItem                 = false;
    public static boolean           createConnection              = true;
    public static boolean           hashtakSearch;
    public static boolean           showButton;
    public static boolean           hideButton;
    public static boolean           showCreateButton;

    public static String            mainUserChat                  = "";
    public static String            mainGroupID                   = "";
    public static String            mainChannelUid                = "";
    public static String            visibleUserChat               = "";
    public static String            visibleGroupID                = "";
    public static String            connectionState               = "4";
    public static String            name                          = "";
    public static String            username                      = "";
    public static String            password;
    public static String            basicAuth;
    public static String            avatarLQ;
    public static String            avatarHQ;
    public static String            gender;

    public static String            language;
    public static String            notification;
    public static String            autoDownload;
    public static String            textSize;
    public static String            textColor;
    public static String            sendByEnter;
    public static String            hijriDate;
    public static String            crop;

    public static int               messagingPageNumber;
    public static int               autoDownloadCount             = 0;
    public static int               manualDownloadCount           = 0;
    public static int               structDownloadItem            = 0;
    public static int               GENERAL_TRACKER               = 0;
    public static int               hashSearchType;                                                                                                                                 // 1==singleChat or 2==groupChat or 3==channel
    public static int               utcMillis;

    public static SQLiteDatabase    database;
    public static database          cmd;
    public static SQLiteDatabase    mydb;
    public static LayoutInflater    inflater;
    public static final Handler     HANDLER                       = new Handler();

    public static Context           context;

    public static UtileProg         utileProg;
    public static HelperGetTime     helperGetTime;
    public static ImageLoader1      imageLoader;

    //************************* Global Array
    public static ArrayList<String> resendFilehashArraySingleChat = new ArrayList<String>();
    public static ArrayList<String> resendFilehashArrayGroupChat  = new ArrayList<String>();
    public static ArrayList<String> resendFilehashArrayChannel    = new ArrayList<String>();

    //************************* Global TypeFace
    public static Typeface          fontAwesome;
    public static Typeface          neuroplp;
    public static Typeface          robotoBold;
    public static Typeface          robotoLight;
    public static Typeface          robotoRegular;


    @Override
    public void onCreate() {
        super.onCreate();
        //MultiDex.install(this);
        Fabric.with(this, new Crashlytics());

        context = getApplicationContext();
        cmd = new database(context);
        cmd.useable();
        cmd.open();

        CheckDatabase.checkDb(G.context);

        //************************* Get User Info

        username = cmd.namayesh4(1, "info");
        password = cmd.namayesh4(2, "info");
        basicAuth = cmd.namayesh4(3, "info");
        avatarLQ = cmd.namayesh4(4, "info");
        avatarHQ = cmd.namayesh4(5, "info");
        name = cmd.namayesh4(6, "info");
        gender = cmd.namayesh4(7, "info");

        //************************* Get User Settings

        language = cmd.getsetting(1);
        notification = cmd.getsetting(2);
        autoDownload = cmd.getsetting(3);
        textSize = cmd.getsetting(4);
        textColor = cmd.getsetting(5);
        sendByEnter = cmd.getsetting(6);
        hijriDate = cmd.getsetting(13);
        crop = cmd.getsetting(14);

        //************************* Define Directories
        //new File(DIR_APP).getParentFile().mkdirs();
        new File(DIR_APP).mkdirs();
        new File(DIR_TEMP).mkdirs();
        new File(DIR_IMAGES).mkdirs();
        new File(DIR_VIDEOS).mkdirs();
        new File(DIR_AUDIOS).mkdirs();
        new File(DIR_DOCUMENTS).mkdirs();
        new File(DIR_RECORD).mkdirs();
        new File(DIR_DIALOG).mkdirs();
        new File(DIR_CROP).mkdirs();

        //************************* Global Values & Variables

        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        utileProg = new UtileProg(context);
        helperGetTime = new HelperGetTime();
        imageLoader = new ImageLoader1(context, basicAuth);

        fontAwesome = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");
        neuroplp = Typeface.createFromAsset(this.getAssets(), "fonts/neuropol.ttf");
        robotoBold = Typeface.createFromAsset(getAssets(), "fonts/RobotoBold.ttf");
        robotoLight = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        robotoRegular = Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Regular.ttf");

        String utc = cmd.select("Countries", "country_name = '" + cmd.getCountry() + "'", 5);
        utcMillis = Integer.parseInt(utc) * 1000;
    }


    //************************* GoogleAnalytics
    public enum TrackerName {
        APP_TRACKER, GLOBAL_TRACKER, ECOMMERCE_TRACKER,
    }

    public HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();


    public G() {
        super();
    }


    public synchronized Tracker getTracker(TrackerName appTracker) {
        if ( !mTrackers.containsKey(appTracker)) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = (appTracker == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID) : (appTracker == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(R.xml.global_tracker) : analytics.newTracker(R.xml.ecommerce_tracker);
            mTrackers.put(appTracker, t);
        }
        return mTrackers.get(appTracker);
    }
}
