// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import javax.net.ssl.SSLSocketFactory;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.util.TLSUtils;
import org.jivesoftware.smackx.chatstates.ChatState;
import org.jivesoftware.smackx.chatstates.ChatStateManager;
import org.jivesoftware.smackx.delay.packet.DelayInformation;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.receipts.DeliveryReceipt;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager.AutoReceiptMode;
import org.jivesoftware.smackx.receipts.DeliveryReceiptRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.RemoteViews;
import com.iGap.MainActivity;
import com.iGap.R;
import com.iGap.Singlechat;
import com.iGap.adapter.G;
import com.iGap.badge.ShortcutBadger;
import com.iGap.helpers.HelperGetTime;
import com.iGap.instruments.FileCache;
import com.iGap.instruments.ImageLoader2;
import com.iGap.instruments.LocalBinder;
import com.iGap.instruments.Utils;
import com.iGap.instruments.database;


/**
 * 
 * main service that all data send and receive is from this class
 *
 */

public class MyService extends Service {

    private FileCache              fileCache;
    private int                    PORT    = 444;
    private AbstractXMPPConnection connection;
    private static Context         cc;
    private Chat                   newChat;
    private Uri                    ring;
    private String                 resourceId, HOST = "secure.igap.im", USERNAME, basicAuth, NAME, PASSWORD, currenttime, ContactChatRecive, Messagefileurl,
                                   Messagefilethumbnail, fileurlforsend, filethumbnailforsend, avatar, userChatAvatar, Messagefilehash, ContactChat, Message,
                                   Messageseenid, idpayamersali, idpayamdaryafti, mmsgseen, filehashforsend;
    private MultiUserChatManager   manager;
    private MultiUserChat          muc;
    private int                    unreadCount;
    private WebSocketClient        connecClient;
    private Handler                handler = new Handler();
    private String                 groupChatID;
    private String                 channelUID;
    private String                 userChatSendMessage;
    private HelperGetTime          helperGetTime;


    @Override
    public IBinder onBind(final Intent intent) {
        return new LocalBinder<MyService>(this);
    }


    @Override
    public void onCreate() {
        helperGetTime = new HelperGetTime();
        super.onCreate();

    }


    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {

        cc = this;
        USERNAME = G.cmd.namayesh4(1, "info");
        PASSWORD = G.cmd.namayesh4(2, "info");
        NAME = G.cmd.namayesh4(6, "info");
        basicAuth = G.cmd.namayesh4(3, "info");
        avatar = G.cmd.namayesh4(4, "info");
        if (avatar != null && !avatar.isEmpty() && !avatar.equals("null") && !avatar.equals("")) {} else {
            avatar = "empty";
        }
        startsocket();

        return Service.START_STICKY;
    }


    private void startall() {
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.post(new Runnable() {

            public void run() {
                new createconnection().execute();
            }
        });
    }


    public void startsocket() {
        Log.i("Connection", "omad vase socket");
        cc = this;
        G.cmd = new database(cc);
        G.cmd.open();
        USERNAME = G.cmd.namayesh4(1, "info");
        PASSWORD = G.cmd.namayesh4(2, "info");
        NAME = G.cmd.namayesh4(6, "info");
        basicAuth = G.cmd.namayesh4(3, "info");
        avatar = G.cmd.namayesh4(4, "info");
        if (avatar != null && !avatar.isEmpty() && !avatar.equals("null") && !avatar.equals("")) {} else {
            avatar = "empty";
        }

        URI server = URI.create("wss://secure.igap.im/socket/");
        connecClient = new WebSocketClient(server, new Draft_17()) {

            @Override
            public void onOpen(ServerHandshake arg0) {

                sendconnectionstate("2");

                Log.i("Connection", "onOpen");
                JSONObject msghop = new JSONObject();
                try {
                    msghop.put("iGap", "login");
                    msghop.put("uname", USERNAME);
                    msghop.put("passwd", PASSWORD);
                    msghop.put("os", "android");

                    connecClient.send(msghop.toString());
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onMessage(String arg0) {

                Log.i("Connection", "onMessage : " + arg0);
                try {
                    JSONObject msghip = new JSONObject(arg0);

                    if (msghip.getString("iGap").equals("hip")) {
                        //====hiphop
                        JSONObject msghop = new JSONObject();
                        msghop.put("iGap", "hop");
                        connecClient.send(msghop.toString());
                    } else if (msghip.getString("iGap").equals("error")) {
                        //====error in login
                        String errorcode = msghip.getString("code");
                        Log.e("Connection", "errorcode : " + errorcode);
                        if (errorcode.equals("100")) {
                            sendconnectionstate("2");
                            //etelaate login eshtebah ast
                        } else {

                        }

                    } else if (msghip.getString("iGap").equals("resource")) {
                        //====login
                        resourceId = msghip.getString("id");
                        //ba movafaghiat login shod va resource ro gereft
                        Log.e("Connection", "resourceId : " + resourceId);
                        startall();
                    } else if (msghip.getString("iGap").equals("chatRoomKick")) {
                        //====user kicked from group
                        String msgId = msghip.getString("msgId");
                        String gid = msghip.getString("id") + "@group.igap.im";
                        String kickerName = msghip.getString("kickerName");

                        if (G.cmd.isgroupmessagehistoryexist(msgId) == 0) {
                            try {
                                kickerName = URLDecoder.decode(kickerName, "UTF-8");
                            }
                            catch (UnsupportedEncodingException e1) {
                                e1.printStackTrace();
                            }
                            String currentTimea;

                            try {
                                currentTimea = new TimerServies().getDateTime();
                            }
                            catch (Exception e) {
                                currentTimea = helperGetTime.getTime();
                            }
                            G.cmd.updategroupactive(2, gid);
                            G.cmd.addgroupchathistory("", gid, msgId, kickerName + " , Kicked you from this group", "4", currentTimea, "1", "", "", "100", null, null, null, "", "", "", "");
                            G.cmd.updategroupchatrooms(gid, kickerName + " , Kicked you from this group", currentTimea);
                            kickedFromGroup(gid, kickerName + " , Kicked you from this group", currentTimea);
                            kickedFromSendToAll(gid, kickerName + " , Kicked you from this group", currentTimea);
                        }
                    } else if (msghip.getString("iGap").equals("chatRoomInvite")) {
                        //====user joined to group
                        String msgId = msghip.getString("msgId");
                        String jid = msghip.getString("id");
                        //String inviter = msghip.getString("inviter");
                        String gname = msghip.getString("name");
                        String description = msghip.getString("description");
                        String avatarLq = msghip.getString("avatarLq");
                        String avatarHq = msghip.getString("avatarHq");
                        String currentuser = msghip.getString("current_user");

                        if (G.cmd.isgroupmessagehistoryexist(msgId) == 0) {
                            try {

                                MultiUserChat muc = manager.getMultiUserChat(jid + "@group.igap.im");
                                muc.join(USERNAME);
                                String currentTimea;
                                try {
                                    currentTimea = new TimerServies().getDateTime();
                                }
                                catch (Exception e) {
                                    currentTimea = helperGetTime.getTime();
                                }
                                String currenttime = currentTimea;
                                String gid = jid + "@group.igap.im";
                                int exist = G.cmd.isGroupExist("groupchatrooms", gid);

                                if (exist == 0) {
                                    G.cmd.Addgroupchat(gid, gname, "2", currentuser, avatarLq, avatarHq, "You join this group", currenttime, description);
                                    G.cmd.addgroupchathistory("", gid, "GroupJoin", "You join this group", "4", currenttime, "1", "empty", "empty", "100", null, null, null, "", "", "", "");
                                    NewGroup(jid + "@group.igap.im", gname, description, avatarLq, avatarHq, "You join this group", currenttime, "2");
                                    NewGroupSendToAll(jid + "@group.igap.im", gname, description, avatarLq, "You join this group", currenttime, "2");
                                } else {

                                }
                            }
                            catch (NoResponseException | XMPPErrorException | NotConnectedException e) {
                                e.printStackTrace();
                            }
                        }

                    } else if (msghip.getString("iGap").equals("channelInvite")) {
                        //====Channel Invite
                        //String msgId = msghip.getString("msgId");
                        String chuid = msghip.getString("uid");
                        String chname = msghip.getString("name");
                        String chdescription = msghip.getString("description");
                        String chtotalMember = msghip.getString("totalMember");
                        String chavatarHq = msghip.getString("avatarHq");
                        String chavatarLq = msghip.getString("avatarLq");
                        String chrole = msghip.getString("role");
                        //String active = msghip.getString("active");
                        String time = msghip.getString("time");

                        channelUID = chuid;

                        String role = null;
                        String roleText = "";
                        if (chrole.equals("owners")) {
                            role = "1";

                        } else if (chrole.equals("members")) {
                            role = "0";
                            roleText = "member";
                        } else if (chrole.equals("admins")) {
                            role = "2";
                            roleText = "admin";
                        }
                        String country = G.cmd.getCountry();
                        String utc = G.cmd.select("Countries", "country_name = '" + country + "'", 5);
                        long utcMillis = Integer.parseInt(utc) * 1000;
                        String currentTimea = HelperGetTime.convertWithSingleTime(time, utcMillis);

                        if (G.cmd.isChannelExist("channels", chuid) == 0) {

                            G.cmd.addchannel(chuid, chname, chdescription, chtotalMember, chavatarLq, chavatarHq, "You Joined to this channel", currentTimea, "0", role);
                            G.cmd.addchannelhistory(chuid, "ChannelInvite", "You Joined to this channel", currentTimea, "100", null, "0", "0", "", "0", "");
                            NewChannel(chuid, chname, chdescription, chtotalMember, chavatarLq, chavatarHq, "You Joined to this channel", currentTimea, role);
                            NewChannelSendToAll(chuid, chname, chdescription, chtotalMember, chavatarLq, "You Joined to this channel", currentTimea, role);
                        } else {
                            String oldRole = G.cmd.selectChannelRole(chuid);
                            if ( !oldRole.equals(role)) {
                                G.cmd.updateRole(chuid, role);
                                changeRole(role);
                                changeRoleInPageMessagingChannel(chuid, role);
                                changeRoleInPageMessagingAll(chuid, role);

                                //====Channel Invite
                                String message = "Your membership changed to " + roleText;
                                G.cmd.addchannelhistory(chuid, "InviteAgain", message, currentTimea, "100", null, "0", "", "0", "0", "");
                                G.cmd.updateChannels(chuid, message, currentTimea);
                                newPostChannel("", chuid, message, "0", currentTimea, "100", null, "", "", "", "", "0", "InviteAgain");
                                newPostChannelList(chuid, message, currentTimea);
                                channelNewPostSendToAll(chuid, message, currentTimea);
                            }
                        }

                    } else if (msghip.getString("iGap").equals("channelDelete")) {
                        //====Channel Deleted
                        String msgId = msghip.getString("msgId");
                        String chuid = msghip.getString("uid");
                        String time = msghip.getString("time");

                        if (G.cmd.isChannelMessageExist(msgId) == 0) {
                            String country = G.cmd.getCountry();
                            String utc = G.cmd.select("Countries", "country_name = '" + country + "'", 5);
                            long utcMillis = Integer.parseInt(utc) * 1000;
                            String currentTimea = HelperGetTime.convertWithSingleTime(time, utcMillis);
                            G.cmd.updatechannelactive(2, chuid);
                            G.cmd.addchannelhistory(chuid, msgId, "This channel is closed", currentTimea, "100", null, "0", "0", "", "0", "");
                            G.cmd.updateChannels(chuid, "This channel is closed", currentTimea);
                            channelDeleted(chuid, "This channel is closed", currentTimea);

                            channelDeletedSendToAll(chuid, "This channel is closed", currentTimea);
                        }

                    } else if (msghip.getString("iGap").equals("channelNewPost")) {
                        //====Channel New Post
                        //String msgId = msghip.getString("msgId");
                        //String uid = msghip.getString("uid");
                        final String id = msghip.getString("id");
                        final String chuid = msghip.getString("uid");
                        final String hash = msghip.getString("hash");
                        final String extention = msghip.getString("extention");
                        String text1 = msghip.getString("text");
                        final String visit = msghip.getString("visit");
                        String addDate = msghip.getString("addDate");
                        String addTimeNew = msghip.getString("addTime");
                        final String url = msghip.getString("url");
                        //String thumbnailHq = msghip.getString("thumbnailHq");
                        final String thumbnailLq = msghip.getString("thumbnailLq");

                        String text2;
                        String addTime = "";

                        try {
                            text2 = URLDecoder.decode(text1, "UTF-8");
                            addTime = URLDecoder.decode(addTimeNew, "UTF-8");
                        }
                        catch (UnsupportedEncodingException e) {
                            text2 = text1;
                            e.printStackTrace();
                        }

                        final String text = text2;
                        String country = G.cmd.getCountry();
                        String utc = G.cmd.select("Countries", "country_name = '" + country + "'", 5);
                        long utcMillis = Integer.parseInt(utc) * 1000;

                        final String serverTimeChannel = HelperGetTime.convertTime(addDate, addTime, utcMillis);

                        channelUID = chuid;

                        handler.postDelayed(new Runnable() {

                            @Override
                            public void run() {

                                int isMsgExist = G.cmd.isChannelMessageExist(id);

                                if (isMsgExist == 0) {
                                    String ext;

                                    if (hash == null || hash.equals("null") || hash.equals("")) {

                                        handler.post(new Runnable() {

                                            @Override
                                            public void run() {
                                                int isMsgExist = G.cmd.isChannelMessageExist(id);
                                                if (isMsgExist == 0) {
                                                    G.cmd.addchannelhistory(chuid, id, text, serverTimeChannel, "1", null, "0", visit, "0", "0", "");
                                                    newPostChannel(extention, chuid, text, "0", serverTimeChannel, "1", null, "", "", "", visit, "0", id);
                                                    newPostChannelList(chuid, text, serverTimeChannel);
                                                }
                                            }
                                        });

                                    } else {

                                        if (extention.equals("jpg") || extention.equals("gif") || extention.equals("JPEG") || extention.equals("png")) {
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
                                            int isFileExist = G.cmd.isChannelMessageExist(id);
                                            if (isFileExist == 0) {
                                                G.cmd.addchannelhistory(chuid, id, text, serverTimeChannel, ext, hash, "0", visit, "0", "0", extention);
                                                newPostChannel(extention, chuid, text, "0", serverTimeChannel, ext, hash, "", "", "", visit, "0", id);
                                                newPostChannelList(chuid, text, serverTimeChannel);
                                                G.cmd.Addfiles(hash, url, thumbnailLq, "0");
                                            }
                                        } else {
                                            // file with text

                                            int isFileExist = G.cmd.isChannelMessageExist(id);
                                            if (isFileExist == 0) {
                                                G.cmd.addchannelhistory(chuid, id, text, serverTimeChannel, ext, hash, "0", visit, "0", "0", extention);
                                                newPostChannel(extention, chuid, text, "0", serverTimeChannel, ext, hash, "", "", "", visit, "0", id);
                                                newPostChannelList(chuid, text, serverTimeChannel);
                                                G.cmd.Addfiles(hash, url, thumbnailLq, "0");
                                            }
                                        }
                                    }
                                    G.cmd.updateChannel(chuid, text, serverTimeChannel);

                                    channelNewPostSendToAll(chuid, text, serverTimeChannel);
                                    String channelName = G.cmd.selectField("channels", "uid = '" + chuid + "'", 2);
                                    String channelAvatar = G.cmd.selectField("channels", "uid = '" + chuid + "'", 5);
                                    String notificationState = G.cmd.selectNotificationState();
                                    if (notificationState.equals("0")) {
                                        String sound = G.cmd.selectField("channels", "uid ='" + chuid + "'", 9);
                                        if (sound.equals("0")) {// not mute notification
                                            if ( !G.appIsShowing) { // agar barname dar hale namayesh nist notificatin neshan dade shavad
                                                channelNotification(text, chuid, channelName, channelAvatar);
                                            }
                                        }
                                    }
                                } else {
                                    //Message is exist    
                                }
                            }
                        }, 2000);
                    } else if (msghip.getString("iGap").equals("systemBroadcast")) {
                        //====Igap Messages
                        //String msgId = msghip.getString("msgId");
                        String msgen = msghip.getString("en");
                        String msgfa = msghip.getString("fa");
                        ContactChatRecive = "igap@igap.im";
                        int lang = Integer.parseInt(G.cmd.getsetting(1));
                        if (lang == 0) {
                            Message = msgen;
                        } else {
                            Message = msgfa;
                        }

                        idpayamdaryafti = "igap";
                        userChatAvatar = "";
                        String currentTimea;
                        try {
                            currentTimea = new TimerServies().getDateTime();
                        }
                        catch (Exception e) {
                            currentTimea = helperGetTime.getTime();
                        }
                        currenttime = currentTimea;
                        getTime("", "1", currenttime, "", "", "");

                    } else if (msghip.getString("iGap").equals("systemVersion")) {
                        //====Igap New Version
                        //String msgId = msghip.getString("msgId");
                        String version = msghip.getString("ver_android");

                        PackageInfo pInfo = null;
                        try {
                            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                        }
                        catch (NameNotFoundException e) {
                            e.printStackTrace();
                        }
                        int verCode = pInfo.versionCode;

                        if (verCode != Integer.parseInt(version) && verCode < Integer.parseInt(version)) {
                            G.cmd.updatever(1, "" + version); // akharin versione barname
                        }

                    } else if (msghip.getString("iGap").equals("userJoin")) {
                        //====New User Joined
                        //String msgId = msghip.getString("msgId");
                        String mobile = msghip.getString("mobile");

                        Message = "this user joined iGap ...";
                        ContactChatRecive = mobile + "@igap.im";
                        idpayamdaryafti = "igap";
                        userChatAvatar = "";
                        //bayad bere ye chat besaze vase in user ba payame this user joined to igap
                        getTime("", "1", "0", null, null, null);

                    } else if (msghip.getString("iGap").equals("userDelete")) {
                        //====User Deleted
                        //String msgId = msghip.getString("msgId");
                        String mobile = msghip.getString("mobile");

                        //bayad bere age chati mojod dare ba in user, be jaye esmesh benevise deleted account va natone payam bede
                        G.cmd.updatechatactive(2, mobile);
                    } else if (msghip.getString("iGap").equals("userStatus")) {
                        //====User Status
                        //String msgId = msghip.getString("msgId");
                        String replyTo = msghip.getString("replyTo");
                        JSONObject lastSeen = msghip.getJSONObject("lastSeen");
                        sendlastseen(lastSeen, replyTo);
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onError(Exception arg0) {

            }


            @Override
            public void onClose(int arg0, String arg1, boolean arg2) {
                Log.e("Connection", "onError" + arg1);
            }
        };

        try {
            connecClient.setSocket(SSLSocketFactory.getDefault().createSocket());
        }
        catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }

        if (connecClient.isOpen()) {
            Log.e("Connection", "socket vasl bod");
            new createconnection().execute();

        } else {
            connecClient.connect();
        }
    }


    public void getlastseen(JSONArray ja, String className) {

        JSONObject jb = new JSONObject();

        try {
            jb.put("iGap", "userStatus");
            jb.put("msgId", className);
            jb.put("uname", ja);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        if (connecClient != null) {
            if (connecClient.isOpen()) {
                connecClient.send(jb.toString());
            }
        }
    }


    private void sendlastseen(JSONObject lastSeen, String className) {

        String target = "UpdateLastSeenSingleChat";

        if (className.equals("Singlechat"))
            target = "UpdateLastSeenSingleChat";

        else if (className.equals("SelectContactSingle"))
            target = "UpdateLastSeenSelectContactSingle";

        else if (className.equals("AddMemberToGroup"))
            target = "UpdateLastSeenAddMemberToGroup";
        else if (className.equals("AddMemberToChannel"))
            target = "UpdateLastSeenAddMemberToChannel";

        else if (className.equals("InviteAdmin"))
            target = "UpdateLastInviteAdmin";

        else if (className.equals("ContactList"))
            target = "UpdateLastContactList";

        else if (className.equals("InviteMemberToChannel"))
            target = "UpdateLastInviteMemberToChannel";

        Intent intent = new Intent(target);
        intent.putExtra("lastSeen", lastSeen.toString());
        LocalBroadcastManager.getInstance(cc).sendBroadcast(intent);

    }


    public Boolean getConnection() {

        if (connection == null) {
            return false;
        } else {
            return connection.isConnected();
        }
    }


    class createconnection extends AsyncTask<String, String, String> {

        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(String... args) {
            if (connection == null) {
                sendconnectionstate("2");
                Log.i("xmppconnection", "null bod omad vase sakht");
                XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();
                configBuilder.setUsernameAndPassword(USERNAME, PASSWORD);
                configBuilder.setSecurityMode(XMPPTCPConnectionConfiguration.SecurityMode.disabled);
                // configBuilder.setSocketFactory(SSLSocketFactory.getDefault());
                configBuilder.setServiceName(HOST);
                configBuilder.setPort(PORT);
                configBuilder.setHost(HOST);
                configBuilder.setResource("igap");
                configBuilder.setCompressionEnabled(true);
                configBuilder.setDebuggerEnabled(true);
                Log.i("xmppconnection", "null bod omad vase sakht  A");
                try {
                    TLSUtils.acceptAllCertificates(configBuilder);
                }
                catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                catch (KeyManagementException e) {
                    e.printStackTrace();
                }
                XMPPTCPConnection.setUseStreamManagementResumptiodDefault(true);
                XMPPTCPConnection.setUseStreamManagementDefault(true);
                Log.e("xmppconnection", "null bod omad vase sakht  B");
                connection = new XMPPTCPConnection(configBuilder.build());
                Log.e("xmppconnection", "null bod omad vase sakht  C");
                connection.setPacketReplyTimeout(10000);
                DeliveryReceiptManager.setDefaultAutoReceiptMode(AutoReceiptMode.always);
                Log.e("xmppconnection", "null bod omad vase sakht  D");
                if (connection.isConnected()) {

                    Log.e("xmppconnection", "A-connect bod omad vase login");
                    connection.disconnect();
                    SASLAuthentication.unBlacklistSASLMechanism("PLAIN");
                    SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5");

                    try {
                        connection.connect();
                        if (connection.isAuthenticated()) {
                            Log.e("xmppconnection", "A-login bod");
                            allobject();
                        } else {
                            try {
                                connection.login();
                                Log.e("xmppconnection", "A-login shod");
                                //	                            ReconnectionManager rm = ReconnectionManager.getInstanceFor(connection);
                                //	                            rm.enableAutomaticReconnection();
                                //	                            rm.setFixedDelay(10);
                                allobject();
                            }
                            catch (XMPPException | SmackException | IOException e2) {
                                e2.printStackTrace();
                            }
                        }
                    }
                    catch (SmackException | IOException | XMPPException e) {
                        sendconnectionstate("1");
                        e.printStackTrace();
                    }

                } else {

                    Log.e("xmppconnection", "null bod omad vase sakht  h");
                    try {
                        Log.e("xmppconnection", "null bod omad vase sakht  i");
                        connection.connect();
                        Log.e("xmppconnection", "A-connect shod1");
                        SASLAuthentication.unBlacklistSASLMechanism("PLAIN");
                        SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5");
                        try {
                            if (connection.isAuthenticated()) {
                                allobject();
                            } else {
                                connection.login();
                                Log.e("xmppconnection", "A-login shod1");
                                //                              ReconnectionManager rm = ReconnectionManager.getInstanceFor(connection);
                                //                              rm.enableAutomaticReconnection();
                                //                              rm.setFixedDelay(10);

                                allobject();
                            }

                        }
                        catch (XMPPException | SmackException | IOException e2) {
                            e2.printStackTrace();
                        }

                    }
                    catch (SmackException | IOException | XMPPException e2) {
                        e2.printStackTrace();
                        Log.e("xmppconnection", "null bod omad vase sakht j" + e2);
                        sendconnectionstate("2");
                    }

                }
            } else {

                if (connection.isConnected()) {
                    Log.e("xmppconnection", "B-connect bod");
                    connection.disconnect();
                    SASLAuthentication.unBlacklistSASLMechanism("PLAIN");
                    SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5");

                    try {
                        connection.connect();
                        Log.e("xmppconnection", "B-connect shod");
                        if (connection.isAuthenticated()) {
                            Log.e("xmppconnection", "B-login bod");
                            allobject();

                        } else {
                            try {
                                connection.login();
                                Log.e("xmppconnection", "B-login shod");
                                allobject();
                            }
                            catch (XMPPException | SmackException | IOException e2) {
                                e2.printStackTrace();
                            }
                        }
                    }
                    catch (SmackException | IOException | XMPPException e) {
                        sendconnectionstate("2");
                        e.printStackTrace();
                    }

                } else {
                    try {
                        connection.connect();
                        SASLAuthentication.unBlacklistSASLMechanism("PLAIN");
                        SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5");
                        try {
                            if (connection.isAuthenticated()) {

                                allobject();
                            } else {
                                connection.login();
                                //	                            ReconnectionManager rm = ReconnectionManager.getInstanceFor(connection);
                                //	                            rm.enableAutomaticReconnection();
                                //	                            rm.setFixedDelay(10);
                                allobject();
                            }

                        }
                        catch (XMPPException | SmackException | IOException e2) {
                            e2.printStackTrace();
                            sendconnectionstate("2");
                        }

                    }
                    catch (SmackException | IOException | XMPPException e2) {
                        e2.printStackTrace();
                        sendconnectionstate("2");
                    }

                }
            }
            return null;
        }
    }


    private void allobject() {

        sendconnectionstate("3");
        manager = MultiUserChatManager.getInstanceFor(connection);
        StanzaFilter filter = MessageTypeFilter.GROUPCHAT;

        connection.addSyncStanzaListener(new StanzaListener() {

            @Override
            public void processPacket(Stanza arg0) throws NotConnectedException {
                Message message = (Message) arg0;

                try {
                    JSONObject jo = new JSONObject(message.getBody());
                    String gm_daryafti = jo.getString("MESSAGE");

                    String gm_sender_avatar = jo.getString("SEAVATAR");
                    String sender = jo.getString("SENAME");
                    String senderusername = jo.getString("SEUSERNAME");
                    String msgtype = jo.getString("TYPE");

                    String filehashforsend = "", fileurlforsend = "", filethumbnailforsend = "";
                    if ( !msgtype.equals("1") && !msgtype.equals("5") && !msgtype.equals("6")) {
                        filehashforsend = jo.getString("FILEHASH");
                        fileurlforsend = jo.getString("FILEURL");
                        filethumbnailforsend = jo.getString("FILETHUMB");
                    }

                    String filemime = jo.getString("FILEMIME");
                    String groupReplyMessageRecive = jo.getString("REPLYMESSAGE");
                    String groupReplyFromRecive = jo.getString("REPLYFROM");
                    String groupReplyFilehashRecive = jo.getString("REPLYFILEHASH");
                    String groupid = message.getFrom().split("/")[0];
                    String msid = message.getStanzaId();
                    groupChatID = groupid;

                    String type, status;
                    if (senderusername.equals(USERNAME)) {
                        type = "2";
                        status = "1";
                    } else {
                        type = "1";
                        status = "4";

                        //===========update avatar
                        String avatar = G.cmd.selectLastUserAvatar(sender);

                        if ( !avatar.equals(gm_sender_avatar)) {
                            G.cmd.updateUserAvatar(sender, gm_sender_avatar);
                            updateViewAfterResendGroupChat(); // do notify in groupchat 
                        }
                    }

                    int isexist = G.cmd.isgroupmessageexist("groupchatrooms", groupid);

                    String msg_xml = message.toXML().toString();
                    if (msg_xml.contains("delay")) {

                        DelayInformation inf = (DelayInformation) message.getExtension("delay", "urn:xmpp:delay");

                        long mil = inf.getStamp().getTime();
                        String country = G.cmd.getCountry();
                        String utc = G.cmd.select("Countries", "country_name = '" + country + "'", 5);
                        long utcMillis = Integer.parseInt(utc) * 1000;
                        mil = mil + utcMillis;

                        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.ENGLISH);
                        cal.setTimeInMillis(mil);
                        String offtime = HelperGetTime.convertTimeWithCalendar(cal);
                        currenttime = offtime;

                    } else {
                        String currentTimea;
                        try {
                            currentTimea = new TimerServies().getDateTime();
                        }
                        catch (Exception e) {
                            currentTimea = helperGetTime.getTime();
                        }

                        currenttime = currentTimea;
                    }
                    if (isexist != 0) {

                        int isexist1 = G.cmd.isgroupmessagehistoryexist(msid);
                        if (isexist1 == 0 && !senderusername.equals(USERNAME)) { // agar payam vojud nadasht va ma ferestande na budim

                            if (msgtype != null) {
                                if (msgtype.equals("1")) {

                                    G.cmd.addgroupchathistory(filemime, groupid, msid, gm_daryafti, status, currenttime, type, sender, gm_sender_avatar, msgtype, null, null, null, senderusername, groupReplyMessageRecive, groupReplyFromRecive, groupReplyFilehashRecive);
                                    G.cmd.updategroupchatrooms(groupid, gm_daryafti, currenttime);
                                    groupNewPost(groupid, gm_daryafti, currenttime);
                                    groupNewPostSendToAll(groupid, gm_daryafti, currenttime);

                                    newPostGroupChat(filemime, groupid, gm_daryafti, status, currenttime, type, senderusername, null, groupReplyFilehashRecive, groupReplyMessageRecive, groupReplyFromRecive, msgtype, sender, gm_sender_avatar, msid);

                                } else if (msgtype.equals("5")) {
                                    G.cmd.addgroupchathistory(filemime, groupid, msid, gm_daryafti, status, currenttime, type, sender, gm_sender_avatar, msgtype, null, null, null, senderusername, groupReplyMessageRecive, groupReplyFromRecive, groupReplyFilehashRecive);
                                    G.cmd.updategroupchatrooms(groupid, gm_daryafti, currenttime);
                                    groupNewPost(groupid, gm_daryafti, currenttime);
                                    groupNewPostSendToAll(groupid, gm_daryafti, currenttime);
                                    newPostGroupChat(filemime, groupid, gm_daryafti, status, currenttime, type, senderusername, null, groupReplyFilehashRecive, groupReplyMessageRecive, groupReplyFromRecive, msgtype, sender, gm_sender_avatar, msid);

                                } else if (msgtype.equals("6")) {
                                    G.cmd.addgroupchathistory(filemime, groupid, msid, gm_daryafti, status, currenttime, type, sender, gm_sender_avatar, msgtype, null, null, null, senderusername, groupReplyMessageRecive, groupReplyFromRecive, groupReplyFilehashRecive);
                                    G.cmd.updategroupchatrooms(groupid, gm_daryafti, currenttime);
                                    groupNewPost(groupid, gm_daryafti, currenttime);
                                    groupNewPostSendToAll(groupid, gm_daryafti, currenttime);
                                    newPostGroupChat(filemime, groupid, gm_daryafti, status, currenttime, type, senderusername, null, groupReplyFilehashRecive, groupReplyMessageRecive, groupReplyFromRecive, msgtype, sender, gm_sender_avatar, msid);

                                } else {

                                    int count = G.cmd.getRowCount("groupchathistory", filehashforsend, groupid);
                                    String messageID = G.cmd.selectMessaegId(groupid, filehashforsend);
                                    if (count == 0) {
                                        G.cmd.addgroupchathistory(filemime, groupid, msid, gm_daryafti, status, currenttime, type, sender, gm_sender_avatar, msgtype, filehashforsend, fileurlforsend, filethumbnailforsend, senderusername, groupReplyMessageRecive, groupReplyFromRecive, groupReplyFilehashRecive);
                                        G.cmd.updategroupchatrooms(groupid, gm_daryafti, currenttime);
                                        G.cmd.Addfiles(filehashforsend, fileurlforsend, filethumbnailforsend, "0");
                                        groupNewPost(groupid, gm_daryafti, currenttime);
                                        groupNewPostSendToAll(groupid, gm_daryafti, currenttime);
                                        newPostGroupChat(filemime, groupid, gm_daryafti, status, currenttime, type, senderusername, filehashforsend, groupReplyFilehashRecive, groupReplyMessageRecive, groupReplyFromRecive, msgtype, sender, gm_sender_avatar, msid);

                                    } else {
                                        if (messageID.equals("empty") || messageID == null) {
                                            G.cmd.updateGroupMessageID(msid, filehashforsend);
                                            groupNewPost(groupid, gm_daryafti, currenttime);
                                            groupNewPostSendToAll(groupid, gm_daryafti, currenttime);
                                            updateIdfileGroupChat(filehashforsend, msid);
                                        } else {
                                            G.cmd.addgroupchathistory(filemime, groupid, msid, gm_daryafti, status, currenttime, type, sender, gm_sender_avatar, msgtype, filehashforsend, fileurlforsend, filethumbnailforsend, senderusername, groupReplyMessageRecive, groupReplyFromRecive, groupReplyFilehashRecive);
                                            G.cmd.updategroupchatrooms(groupid, gm_daryafti, currenttime);
                                            G.cmd.Addfiles(filehashforsend, fileurlforsend, filethumbnailforsend, "0");
                                            groupNewPost(groupid, gm_daryafti, currenttime);
                                            groupNewPostSendToAll(groupid, gm_daryafti, currenttime);
                                            newPostGroupChat(filemime, groupid, gm_daryafti, status, currenttime, type, senderusername, filehashforsend, groupReplyFilehashRecive, groupReplyMessageRecive, groupReplyFromRecive, msgtype, sender, gm_sender_avatar, msid);
                                        }
                                    }
                                    loadImage(filethumbnailforsend, filehashforsend);
                                }

                                if (type.equals("1")) {
                                    String notificationState = G.cmd.selectNotificationState();
                                    if (notificationState.equals("0")) {
                                        String sound = G.cmd.selectField("groupchatrooms", "groupchatid = '" + groupid + "'", 9);

                                        if (sound.equals("0")) {//not mute notification
                                            if ( !G.appIsShowing) { // agar barname dar hale namayesh nist notificatin neshan dade shavad
                                                groupChatNotification(gm_daryafti, groupid, sender, gm_sender_avatar);
                                            }
                                        }
                                    }
                                }
                            }

                        } else {
                            String st = G.cmd.selectGroupMessageStatus(msid);
                            G.cmd.updateGroupMessageStatus(groupid, st, msid);
                            updateStatusfileGroupChat(msid, st);
                        }
                    } else {}

                }
                catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }

        }, filter);
        StanzaFilter filterchat = MessageTypeFilter.CHAT;
        connection.addSyncStanzaListener(new StanzaListener() {

            @Override
            public void processPacket(Stanza arg0) throws NotConnectedException {
                Message message1 = (Message) arg0;
                ContactChatRecive = message1.getFrom();

                int index = ContactChatRecive.lastIndexOf('/');
                if (index != -1) {
                    ContactChatRecive = ContactChatRecive.substring(0, index);
                }

                String[] separated = ContactChatRecive.split("@");
                String mobile = separated[0];

                int exist = G.cmd.isUserBlock(mobile);

                if (exist == 0) {

                    String havebody = message1.getBody();

                    if (havebody != null && !havebody.equals("null")) {

                        try {
                            JSONObject jo = new JSONObject(message1.getBody());

                            String MessageModel = jo.getString("ISSEEN");
                            idpayamdaryafti = message1.getStanzaId();
                            if (MessageModel.equals("1")) {
                                //seen hast
                                Messageseenid = jo.getString("MESSAGESEENID");
                                G.cmd.updatemsgstatus(Messageseenid, "3");
                                updateMsgStatusSingleChat(Messageseenid, "3");
                            } else if (MessageModel.equals("2")) {
                                //paint hast
                                String message = message1.getBody();

                                if (G.mainUserChat.equals(ContactChatRecive) && !G.mainUserChat.equals("")) {
                                    newpaintt(message);
                                }

                            } else if (MessageModel.equals("3")) {
                                //clear paint hast
                                if (G.mainUserChat.equals(ContactChatRecive) && !G.mainUserChat.equals("")) {
                                    clearPaint();
                                }

                            } else {
                                String msg_xml = message1.toXML().toString();
                                if (msg_xml.contains("delay")) {
                                    if (MessageModel.equals("1")) {
                                        //seen hast ama offline omade
                                        Messageseenid = jo.getString("MESSAGESEENID");
                                        G.cmd.updatemsgstatus(Messageseenid, "3");
                                    } else {
                                        //payame offline hastesh

                                        int size = G.cmd.getRowCount3("chathistory", idpayamdaryafti);
                                        if (size == 0) {

                                            DelayInformation inf = (DelayInformation) message1.getExtension("delay", "urn:xmpp:delay");

                                            long mil = inf.getStamp().getTime();
                                            String country = G.cmd.getCountry();
                                            String utc = G.cmd.select("Countries", "country_name = '" + country + "'", 5);
                                            long utcMillis = Integer.parseInt(utc) * 1000;
                                            mil = mil + utcMillis;

                                            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.ENGLISH);
                                            cal.setTimeInMillis(mil);

                                            String offtime = HelperGetTime.convertTimeWithCalendar(cal);

                                            try {
                                                Message = URLDecoder.decode(jo.getString("MESSAGE"), "UTF-8");
                                            }
                                            catch (UnsupportedEncodingException e) {
                                                Message = jo.getString("MESSAGE");
                                                e.printStackTrace();
                                            }

                                            String MessageTYPE = jo.getString("TYPE");
                                            userChatAvatar = jo.getString("AVATAR");
                                            String replyMessageRecive = jo.getString("REPLYMESSAGE");
                                            String replyFromRecive = jo.getString("REPLYFROM");
                                            String replyFilehashRecive = jo.getString("REPLYFILEHASH");
                                            String filemime = jo.getString("FILEMIME");
                                            if ( !MessageTYPE.equals("1") && !MessageTYPE.equals("5") && !MessageTYPE.equals("6")) {
                                                Messagefilehash = jo.getString("FILEHASH");
                                                Messagefileurl = jo.getString("FILEURL");
                                                Messagefilethumbnail = jo.getString("FILETHUMB");
                                                loadImage(Messagefilethumbnail, Messagefilehash);
                                            }
                                            getTime(filemime, MessageTYPE, offtime, replyMessageRecive, replyFromRecive, replyFilehashRecive);
                                        }
                                    }
                                } else {

                                    int size = G.cmd.getRowCount3("chathistory", idpayamdaryafti);

                                    if (size == 0) {
                                        //chate daryaftie
                                        try {
                                            Message = URLDecoder.decode(jo.getString("MESSAGE"), "UTF-8");
                                        }
                                        catch (UnsupportedEncodingException e) {
                                            Message = jo.getString("MESSAGE");
                                        }
                                        if ( !Message.equals("paintrequest")) {
                                            String MessageTYPE = jo.getString("TYPE");
                                            userChatAvatar = jo.getString("AVATAR");
                                            String replyMessageRecive = jo.getString("REPLYMESSAGE");
                                            String replyFromRecive = jo.getString("REPLYFROM");
                                            String replyFilehashRecive = jo.getString("REPLYFILEHASH");
                                            String filemime = jo.getString("FILEMIME");
                                            if ( !MessageTYPE.equals("1") && !MessageTYPE.equals("5") && !MessageTYPE.equals("6")) {
                                                Messagefilehash = jo.getString("FILEHASH");
                                                Messagefileurl = jo.getString("FILEURL");
                                                Messagefilethumbnail = jo.getString("FILETHUMB");

                                                loadImage(Messagefilethumbnail, Messagefilehash);
                                            }
                                            getTime(filemime, MessageTYPE, "0", replyMessageRecive, replyFromRecive, replyFilehashRecive);
                                            if (G.mainUserChat.equals(ContactChatRecive)) { // payam az kasi hast ke dar hale chat kardan ba mast.
                                                sendMessageTosetstate("5");
                                            }

                                            String notificationState = G.cmd.selectNotificationState();
                                            String sound = G.cmd.selectField("Chatrooms", "userchat = '" + ContactChatRecive + "'", 4);
                                            if (notificationState.equals("0")) {
                                                if (sound.equals("0")) {
                                                    if ( !G.appIsShowing) { // agar barname dar hale namayesh nist notificatin neshan dade shavad
                                                        singleChatNotification(Message, ContactChatRecive, userChatAvatar);
                                                    }
                                                }
                                            }
                                        } else {
                                            //paint request
                                            Singlechat.showPaintAlert = true;
                                            if (G.mainUserChat.equals(ContactChatRecive) && !G.mainUserChat.equals("")) {
                                                paintrequestbroad();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    } else {

                        String frommsg = message1.getFrom();
                        String from = frommsg.split("/")[0];
                        if (from.equals("support@igap.im")) {
                            String message = message1.getBody();
                            String msgid = message1.getStanzaId();

                            int isexist = G.cmd.issupportexist(msgid);
                            if (isexist == 0) {
                                String currentTimea;
                                try {
                                    currentTimea = new TimerServies().getDateTime();
                                }
                                catch (Exception e) {
                                    currentTimea = helperGetTime.getTime();
                                }
                                G.cmd.addsupporthistory(msgid, message, currentTimea, "1", "4");

                                NewPostSupport(msgid, message, currentTimea, "1", "4");
                            }

                            updateSupportUnread();
                        } else {
                            if (G.mainUserChat.equals(ContactChatRecive)) { // payam az kasi hast ke dar hale chat kardan ba mast.
                                String msg_xml = message1.toXML().toString();
                                if (msg_xml.contains(ChatState.composing.toString())) {
                                    sendMessageTosetstate("1");
                                } else if (msg_xml.contains(ChatState.inactive.toString())) {
                                    sendMessageTosetstate("3");
                                } else if (msg_xml.contains(ChatState.active.toString())) {
                                    sendMessageTosetstate("2");
                                } else if (msg_xml.contains(ChatState.paused.toString())) {
                                    sendMessageTosetstate("4");
                                } else if (msg_xml.contains(ChatState.gone.toString())) {
                                    sendMessageTosetstate("5");
                                } else if (msg_xml.contains("received")) {
                                    DeliveryReceipt deliveryReceiptObj = (DeliveryReceipt) message1.getExtension(DeliveryReceipt.NAMESPACE);
                                    idpayamersali = deliveryReceiptObj.getId();
                                    String status = G.cmd.getstatus(idpayamersali);
                                    if ( !status.equals("3")) {
                                        G.cmd.updatemsgstatus(idpayamersali, "2");
                                        updateMsgStatusSingleChat(idpayamersali, "2");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }, filterchat);

        connection.addConnectionListener(new ConnectionListener() {

            @Override
            public void reconnectionSuccessful() {
                sendconnectionstate("4");
                Log.e("xmppconnection", "reconnectionSuccessful");
            }


            @Override
            public void reconnectionFailed(Exception arg0) {
                sendconnectionstate("2");
                Log.e("xmppconnection", "reconnectionFailed" + arg0);
            }


            @Override
            public void reconnectingIn(int arg0) {}


            @Override
            public void connectionClosedOnError(Exception arg0) {
                sendconnectionstate("2");
                Log.e("xmppconnection", "connectionClosedOnError" + arg0);
            }


            @Override
            public void connectionClosed() {
                sendconnectionstate("2");
                Log.e("xmppconnection", "connectionClosed");
            }


            @Override
            public void connected(XMPPConnection arg0) {
                sendconnectionstate("4");
                Log.e("xmppconnection", "connected" + arg0);

            }


            @Override
            public void authenticated(XMPPConnection arg0, boolean arg1) {
                sendconnectionstate("4");
                Log.e("xmppconnection", "authenticated" + arg0);
            }
        });

        Cursor cursor = G.cmd.selectGroupChatRooms();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {

                String groupChatID = cursor.getString(cursor.getColumnIndex("groupchatid"));

                muc = manager.getMultiUserChat(groupChatID);

                String y = "empty";
                try {
                    y = G.cmd.groupchathistory1(groupChatID);

                }
                catch (Exception e) {}

                if (y.equals("empty") || y.equals("") || y == null) {
                    y = G.cmd.selectGroupChatLastTime(groupChatID);
                }

                if (y.equals("empty")) {
                    try {
                        muc.join(USERNAME);
                    }
                    catch (XMPPErrorException | NoResponseException | NotConnectedException e) {
                        e.printStackTrace();
                    }
                } else {

                    String currentTimea;
                    try {
                        currentTimea = new TimerServies().getDateTime();
                    }
                    catch (Exception e) {
                        currentTimea = helperGetTime.getTime();
                    }

                    String dd = currentTimea;

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date Date1 = null;
                    Date Date2 = null;
                    long mills = 0;
                    try {
                        Date1 = (Date) formatter.parse(y);
                        Date2 = (Date) formatter.parse(dd);
                        mills = Date2.getTime() - Date1.getTime();
                    }
                    catch (ParseException e1) {
                        e1.printStackTrace();
                    }

                    int milisecond = (int) mills;
                    int seconds = (int) (milisecond / 1000);
                    DiscussionHistory history = new DiscussionHistory();
                    history.setSeconds(seconds);
                    try {
                        muc.join(USERNAME, null, history, SmackConfiguration.getDefaultPacketReplyTimeout());
                    }
                    catch (XMPPErrorException | NoResponseException | NotConnectedException e) {
                        e.printStackTrace();
                    }

                }

            }
            cursor.close();
        }

        sendconnectionstate("4");
    }


    private void loadImage(String url, String filehash) {
        ImageLoader2 imgLoader2 = new ImageLoader2(cc, basicAuth);
        imgLoader2.DisplayImage(url, filehash);
    }


    public void sendgroupmessage(String filemime, String roomid, String mess, String useravatar, String type, String filehash, String fileurl, String filethumb, String replymessage, String replyfilehash, String replyfrom, String resend, String oldMsgID) {
        filehashforsend = filehash;
        fileurlforsend = fileurl;
        filethumbnailforsend = filethumb;
        Message ms = new Message();
        String messageID = ms.getStanzaId();
        if (connection == null) {
            sendGroupMessage(filemime, roomid, "0", messageID, mess, useravatar, type, filehash, fileurl, filethumb, replymessage, replyfilehash, replyfrom, resend, oldMsgID);
        } else {
            if (manager == null) {
                manager = MultiUserChatManager.getInstanceFor(connection);
            }
            muc = manager.getMultiUserChat(roomid);
            if (replyfilehash != null && !replyfilehash.isEmpty() && !replyfilehash.equals("null") && !replyfilehash.equals("")) {

            } else {
                replyfilehash = "";
            }

            JSONObject jo = new JSONObject();

            try {
                jo.put("REPLYMESSAGE", replymessage);
                jo.put("REPLYFROM", replyfrom);
                jo.put("REPLYFILEHASH", replyfilehash);
                jo.put("FILEMIME", filemime);
                jo.put("MESSAGE", mess);
                jo.put("SEAVATAR", useravatar);
                jo.put("SENAME", NAME);
                jo.put("SEUSERNAME", USERNAME);
                jo.put("ISSEEN", "0");
                jo.put("TYPE", type);
                if ( !type.equals("1") && !type.equals("5") && !type.equals("6")) {
                    jo.put("FILEHASH", filehashforsend);
                    jo.put("FILEURL", fileurlforsend);
                    jo.put("FILETHUMB", filethumbnailforsend);
                }

            }
            catch (JSONException e1) {
                e1.printStackTrace();
            }
            ms.setBody(jo.toString());

            try {

                muc.sendMessage(ms);
                sendGroupMessage(filemime, roomid, "1", messageID, mess, useravatar, type, filehash, fileurl, filethumb, replymessage, replyfilehash, replyfrom, resend, oldMsgID);

            }
            catch (NotConnectedException e) {
                e.printStackTrace();
                sendGroupMessage(filemime, roomid, "0", messageID, mess, useravatar, type, filehash, fileurl, filethumb, replymessage, replyfilehash, replyfrom, resend, oldMsgID);

            }
        }
    }


    private void sendGroupMessage(String filemime, String groupid, String status, String newMsgID, String message, String useravatar, String type, String filehash, String fileurl, String filethumb, String replymessage, String replyfilehash, String replyfrom, String resend, String oldMsgID) {

        String senderUsername = G.cmd.namayesh4(1, "info");
        String senderName = G.cmd.namayesh4(6, "info");

        String currentTimea;
        try {
            currentTimea = new TimerServies().getDateTime();
        }
        catch (Exception e) {
            currentTimea = helperGetTime.getTime();
        }

        if (resend.equals("1")) { // Resend

            if (type.equals("1") || type.equals("5") || type.equals("6")) {

                if (status.equals("1")) { // agar internet bud va payam miraft resend anjam shavad

                    // clear message with oldMsgID
                    G.cmd.deletemessagegroupchat(oldMsgID);

                    // Add to this chatroom message with newMessageID
                    G.cmd.addgroupchathistory(filemime, groupid, newMsgID, message, status, currentTimea, "2", senderName, useravatar, type, filehash, fileurl, filethumb, senderUsername, replymessage, replyfrom, replyfilehash);
                    updateViewAfterResendGroupChat();
                }
            } else {

                // clear message with oldMsgID
                G.cmd.deleteFileMessageFromGroupHistory(filehash, groupid);

                // Add to this chatroom message with newMessageID
                G.cmd.addgroupchathistory(filemime, groupid, newMsgID, message, status, currentTimea, "2", senderName, useravatar, type, filehash, fileurl, filethumb, senderUsername, replymessage, replyfrom, replyfilehash);

                updateViewAfterResendGroupChat();

                groupNewPost(groupid, message, currenttime);
                groupNewPostSendToAll(groupid, message, currenttime);
                loadImage(filethumb, filehash);
            }

        } else { // Send
            int isexist = G.cmd.isgroupmessageexist("groupchatrooms", groupid);
            currenttime = currentTimea;
            if (isexist != 0) {

                int isexist1 = G.cmd.isgroupmessagehistoryexist(oldMsgID);
                if (isexist1 == 0) {
                    if (type.equals("1") || type.equals("5") || type.equals("6")) {
                        G.cmd.addgroupchathistory(filemime, groupid, newMsgID, message, status, currenttime, "2", senderName, useravatar, type, null, null, null, senderUsername, replymessage, replyfrom, replyfilehash);
                        G.cmd.updategroupchatrooms(groupid, message, currenttime);
                        groupNewPost(groupid, message, currenttime);
                        groupNewPostSendToAll(groupid, message, currenttime);
                        newPostGroupChatFromSender(filemime, groupid, message, status, currenttime, "2", senderUsername, null, replyfilehash, replymessage, replyfrom, type, senderName, useravatar, newMsgID);

                    } else {
                        int count = G.cmd.getRowCount("groupchathistory", filehash, groupid);
                        if (count == 0) {
                            G.cmd.addgroupchathistory(filemime, groupid, newMsgID, message, status, currenttime, "2", senderName, useravatar, type, filehash, fileurlforsend, filethumbnailforsend, senderUsername, replymessage, replyfrom, replyfilehash);
                            G.cmd.updategroupchatrooms(groupid, message, currenttime);
                            G.cmd.Addfiles(filehash, fileurlforsend, filethumbnailforsend, "0");
                            groupNewPost(groupid, message, currenttime);
                            groupNewPostSendToAll(groupid, message, currenttime);
                            newPostGroupChatFromSender(filemime, groupid, message, status, currenttime, "2", senderUsername, filehash, replyfilehash, replymessage, replyfrom, type, senderName, useravatar, newMsgID);

                        } else {
                            G.cmd.updateGroupMessageID(newMsgID, filehashforsend);
                            groupNewPost(groupid, message, currenttime);
                            groupNewPostSendToAll(groupid, message, currenttime);
                            updateIdfileGroupChatFromSender(filehashforsend, newMsgID);
                        }
                        loadImage(filethumbnailforsend, filehash);
                    }
                }
            }
        }
    }


    public void createchattest(String userchat) {
        ContactChat = userchat;

        if (connection != null) {
            newChat = ChatManager.getInstanceFor(connection).createChat(ContactChat);
        }
    }


    public void SendMessageOrder(String filemime, String userchat, String msg, String type, String filehash, String fileurl, String filethumbnailLq, String replymessage, String replyfilehash, String replyfrom, String resend, String oldMsgID) {

        String currentTimea;
        try {
            currentTimea = new TimerServies().getDateTime();
        }
        catch (Exception e) {
            currentTimea = helperGetTime.getTime();
        }
        currenttime = currentTimea;
        userChatSendMessage = userchat;
        sendtest(filemime, userchat, msg, type, filehash, fileurl, filethumbnailLq, replymessage, replyfilehash, replyfrom, currentTimea, resend, oldMsgID);

    }


    public void setchatstate(String state, String userchat) {

        try {

            if (connection != null) {

                if (newChat != null) {
                    if (state.equals("1")) {
                        ChatStateManager.getInstance(connection).setCurrentState(ChatState.composing, newChat);
                    } else if (state.equals("2")) {
                        ChatStateManager.getInstance(connection).setCurrentState(ChatState.active, newChat);
                    } else if (state.equals("3")) {
                        ChatStateManager.getInstance(connection).setCurrentState(ChatState.inactive, newChat);
                    } else if (state.equals("4")) {
                        ChatStateManager.getInstance(connection).setCurrentState(ChatState.paused, newChat);
                    } else if (state.equals("5")) {
                        ChatStateManager.getInstance(connection).setCurrentState(ChatState.gone, newChat);
                    }
                } else {
                    newChat = ChatManager.getInstanceFor(connection).createChat(userchat);
                    if (state.equals("1")) {
                        ChatStateManager.getInstance(connection).setCurrentState(ChatState.composing, newChat);
                    } else if (state.equals("2")) {
                        ChatStateManager.getInstance(connection).setCurrentState(ChatState.active, newChat);
                    } else if (state.equals("3")) {
                        ChatStateManager.getInstance(connection).setCurrentState(ChatState.inactive, newChat);
                    } else if (state.equals("4")) {
                        ChatStateManager.getInstance(connection).setCurrentState(ChatState.paused, newChat);
                    } else if (state.equals("5")) {
                        ChatStateManager.getInstance(connection).setCurrentState(ChatState.gone, newChat);
                    }
                }
            }

        }
        catch (NotConnectedException e) {
            e.printStackTrace();
        }

    }


    public void sendtest(String filemime, String userchat, String msg, String type, String filehash, String fileurl, String filethumbnailLq, String replymessage, String replyfilehash, String replyfrom, String time, String resend, String oldMsgID) {

        Message ms = new Message();
        String Messegeforsend1 = null;
        try {
            Messegeforsend1 = URLEncoder.encode(msg, "UTF-8");
        }
        catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        if (replyfilehash != null && !replyfilehash.isEmpty() && !replyfilehash.equals("null") && !replyfilehash.equals("")) {} else {
            replyfilehash = "";
        }
        G.cmd = new database(cc);
        G.cmd.open();
        avatar = G.cmd.namayesh4(4, "info");
        if (avatar != null && !avatar.isEmpty() && !avatar.equals("null") && !avatar.equals("")) {} else {
            avatar = "empty";
        }
        JSONObject jo = new JSONObject();
        try {
            jo.put("REPLYMESSAGE", replymessage);
            jo.put("REPLYFROM", replyfrom);
            jo.put("REPLYFILEHASH", replyfilehash);
            jo.put("FILEMIME", filemime);
            jo.put("MESSAGE", Messegeforsend1);
            jo.put("ISSEEN", "0");
            jo.put("TYPE", type);
            jo.put("AVATAR", avatar);
            if ( !type.equals("1") && !type.equals("5") && !type.equals("6")) {
                jo.put("FILEHASH", filehash);
                jo.put("FILEURL", fileurl);
                jo.put("FILETHUMB", filethumbnailLq);
            }
        }
        catch (JSONException e1) {
            e1.printStackTrace();
        }
        ms.setBody(jo.toString());
        ms.setType(Type.chat);

        DeliveryReceiptRequest.addTo(ms);
        String idpayamersali = ms.getStanzaId();

        String chatroomid;
        int existchat = G.cmd.getRowCountChatroom(userchat, "Chatrooms");
        if (existchat == 0) {
            String[] separated = userchat.split("@");
            String mob = separated[0];
            String av = G.cmd.getContactsavatar(4, "1", mob);
            chatroomid = G.cmd.addchatroom(userchat, msg, time, av);
        } else {
            chatroomid = G.cmd.updatechatrooms(userchat, msg, time);
        }
        if (connection == null) {
            sendMessage(filemime, existchat, userchat, chatroomid, msg, "0", time, "2", idpayamersali, type, filehash, null, null, replymessage, replyfilehash, replyfrom, resend, oldMsgID);

        } else {
            if (newChat == null) {
                newChat = ChatManager.getInstanceFor(connection).createChat(userchat);
                try {
                    newChat.sendMessage(ms);
                    sendMessage(filemime, existchat, userchat, chatroomid, msg, "1", time, "2", idpayamersali, type, filehash, null, null, replymessage, replyfilehash, replyfrom, resend, oldMsgID);
                }
                catch (NotConnectedException e) {
                    sendMessage(filemime, existchat, userchat, chatroomid, msg, "0", time, "2", idpayamersali, type, filehash, null, null, replymessage, replyfilehash, replyfrom, resend, oldMsgID);
                }
            } else {
                try {
                    newChat.sendMessage(ms);
                    sendMessage(filemime, existchat, userchat, chatroomid, msg, "1", time, "2", idpayamersali, type, filehash, null, null, replymessage, replyfilehash, replyfrom, resend, oldMsgID);
                }
                catch (NotConnectedException e) {
                    e.printStackTrace();
                    sendMessage(filemime, existchat, userchat, chatroomid, msg, "0", time, "2", idpayamersali, type, filehash, null, null, replymessage, replyfilehash, replyfrom, resend, oldMsgID);
                }
            }
        }

    }


    public void sendsupport(String msg) {

        Message ms = new Message();
        ms.setBody(NAME + " : " + msg);
        ms.setType(Type.chat);
        DeliveryReceiptRequest.addTo(ms);
        String idpayamersali = ms.getStanzaId();

        int isexist = G.cmd.issupportexist(idpayamersali);
        if (isexist == 0) {
            String currentTimea;
            try {
                currentTimea = new TimerServies().getDateTime();
            }
            catch (Exception e) {
                currentTimea = helperGetTime.getTime();
            }
            G.cmd.addsupporthistory(idpayamersali, msg, currentTimea, "2", "1");
            NewPostSupport(idpayamersali, msg, currentTimea, "2", "1");
        }

        if (connection == null) {

        } else {
            if (newChat == null) {
                newChat = ChatManager.getInstanceFor(connection).createChat("support@igap.im");
            } else {
                try {
                    newChat.sendMessage(ms);
                }
                catch (NotConnectedException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public void sendpaint(String msg) {

        Message ms = new Message();
        ms.setBody(msg);
        ms.setType(Type.chat);
        DeliveryReceiptRequest.addTo(ms);

        if (connection != null) {
            if (newChat != null) {
                try {

                    newChat.sendMessage(ms);
                    Log.e("sendpain", "ersal shod be : " + newChat.getParticipant());
                }
                catch (NotConnectedException e) {
                    e.printStackTrace();
                    Log.e("sendpain", "ersal nashod");
                }
            }
        }
    }


    public void sendpaintrequest() {

        Message ms = new Message();

        JSONObject jo = new JSONObject();
        try {
            jo.put("ISSEEN", "0");
            jo.put("MESSAGE", "paintrequest");
        }
        catch (JSONException e1) {
            e1.printStackTrace();
        }
        ms.setBody(jo.toString());
        ms.setType(Type.chat);
        DeliveryReceiptRequest.addTo(ms);

        if (connection != null) {
            if (newChat != null) {
                try {
                    newChat.sendMessage(ms);
                }
                catch (NotConnectedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void sendMessage(String filemime, int existchat, String userchat, String id, String message, String status, String messageTime, String messageType, String newMessageID, String type, String fileHash, String fileUrl, String fileThumbnail, String replyMessage, String replyFileHash, String replyFrom, String resend, String msgID) {

        if (resend.equals("1")) { // Resend

            if (type.equals("1") || type.equals("5") || type.equals("6")) {
                // clear message with oldMsgID
                G.cmd.deletemessagesinglechat(msgID);

                // Add to this chatroom message with newMessageID
                G.cmd.addchathistory(filemime, id, message, status, messageTime, messageType, newMessageID, type, fileHash, fileUrl, fileThumbnail, replyMessage, replyFileHash, replyFrom);

                updateViewAfterResendSingleChat();
            } else {

                // clear message with fileHash and status = 0
                G.cmd.deleteFileMessageFromChatHistory(fileHash, id);

                // Add to this chatroom message with newMessageID
                G.cmd.addchathistory(filemime, id, message, status, messageTime, messageType, newMessageID, type, fileHash, fileUrl, fileThumbnail, replyMessage, replyFileHash, replyFrom);

                updateViewAfterResendSingleChat();
            }

        } else { // Send
            if (type.equals("1") || type.equals("5") || type.equals("6")) {

                String cid = G.cmd.addchathistory(filemime, id, message, status, messageTime, messageType, newMessageID, type, null, null, null, replyMessage, replyFileHash, replyFrom);
                sendNewPostSingleChat(filemime, cid, message, status, messageTime, messageType, newMessageID, fileHash, replyFileHash, replyMessage, replyFrom, type);
            } else {
                G.cmd.updateUploadStatus(id, fileHash, newMessageID, status);
                updateStatusfileSingleChat(fileHash, newMessageID, status);
            }
        }

        if (existchat == 0) {
            newRoomChat(id, userchat, message, messageTime, "0", "1", "");
            newRoomChatAddToAll(id, userchat, message, messageTime, "");

        } else {
            newPostChat(userchat, message, messageTime);
            newPostChatSendToAll(userchat, message, messageTime);
        }

    }


    public void sendseen(String userchat, String msg) {
        mmsgseen = msg;
        ContactChat = userchat;

        if (connection != null) {
            if (newChat == null) {
                newChat = ChatManager.getInstanceFor(connection).createChat(userchat);
                try {
                    Message ms = new Message();

                    JSONObject jo = new JSONObject();
                    try {
                        jo.put("MESSAGESEENID", mmsgseen);
                        jo.put("ISSEEN", "1");
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }

                    ms.setBody(jo.toString());
                    newChat.sendMessage(ms);
                    updateSeenChat(userchat);
                    sendMessageToAll();
                }
                catch (NotConnectedException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Message ms = new Message();
                    JSONObject jo = new JSONObject();
                    try {
                        jo.put("MESSAGESEENID", mmsgseen);
                        jo.put("ISSEEN", "1");
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }

                    ms.setBody(jo.toString());
                    newChat.sendMessage(ms);
                    updateSeenChat(userchat);
                    sendMessageToAll();
                }
                catch (NotConnectedException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    private static void sendMessageTosetstate(String mode) {
        Intent intent = new Intent("setuserstate");
        intent.putExtra("mode", mode);
        LocalBroadcastManager.getInstance(cc).sendBroadcast(intent);
    }


    private void NewChannel(String uid, String name, String description, String membersnumbers, String avatar_lq, String avatar_hq, String lastmsg, String lastdate, String membership) {
        Intent intent = new Intent("addNewChannel");
        intent.putExtra("uid", uid);
        intent.putExtra("name", name);
        intent.putExtra("description", description);
        intent.putExtra("avatar_lq", avatar_lq);
        intent.putExtra("avatar_hq", avatar_hq);
        intent.putExtra("membership", membership);
        intent.putExtra("membersnumbers", membersnumbers);
        intent.putExtra("lastmsg", lastmsg);
        intent.putExtra("lastdate", lastdate);
        LocalBroadcastManager.getInstance(cc).sendBroadcast(intent);
    }


    private void NewChannelSendToAll(String uid, String name, String description, String membersnumbers, String avatar_lq, String lastmsg, String lastdate, String membership) {

        Intent intentAll = new Intent("addNewInAll");
        intentAll.putExtra("MODEL", "3"); // 3 = channel
        intentAll.putExtra("UID", uid);
        intentAll.putExtra("NAME", name);
        intentAll.putExtra("DESC", description);
        intentAll.putExtra("MEMBERS_NUMBER", membersnumbers);
        intentAll.putExtra("MEMBER_SHIP", membership);
        intentAll.putExtra("AVATAR_LQ", avatar_lq);
        intentAll.putExtra("LAST_MSG", lastmsg);
        intentAll.putExtra("LAST_DATE", lastdate);
        LocalBroadcastManager.getInstance(cc).sendBroadcast(intentAll);
    }


    private void channelNewPostSendToAll(String uid, String lastmsg, String lastdate) {
        Intent intentAll = new Intent("newPostAll");
        intentAll.putExtra("MODEL", "3"); // 3 = channel
        intentAll.putExtra("UID", uid);
        intentAll.putExtra("LAST_MSG", lastmsg);
        intentAll.putExtra("LAST_DATE", lastdate);
        LocalBroadcastManager.getInstance(cc).sendBroadcast(intentAll);
    }


    private void channelDeleted(String uid, String lastmsg, String lastdate) {
        Intent intent = new Intent("channelDeleted");
        intent.putExtra("uid", uid);
        intent.putExtra("lastmsg", lastmsg);
        intent.putExtra("lastdate", lastdate);
        LocalBroadcastManager.getInstance(cc).sendBroadcast(intent);
    }


    private void channelDeletedSendToAll(String uid, String lastmsg, String lastdate) {
        Intent intent = new Intent("deletedFromAll");
        intent.putExtra("MODEL", "3");
        intent.putExtra("UID", uid);
        intent.putExtra("LAST_MSG", lastmsg);
        intent.putExtra("LAST_DATE", lastdate);
        LocalBroadcastManager.getInstance(cc).sendBroadcast(intent);
    }


    private static void sendMessageToAll() {
        Intent intent = new Intent("loadall");
        LocalBroadcastManager.getInstance(cc).sendBroadcast(intent);
    }


    private String getTime(String filemime, String type, String time, String replyMessageRecive, String replyFromRecive, String replyFilehashRecive) {
        if (time.equals("0")) {
            try {
                currenttime = new TimerServies().getDateTime();
            }
            catch (Exception e) {
                currenttime = helperGetTime.getTime();
            }
        } else {
            currenttime = time;
        }
        int existchat = G.cmd.getRowCountChatroom(ContactChatRecive, "Chatrooms");
        if (existchat == 0) {
            String chatroomid = G.cmd.addchatroom(ContactChatRecive, Message, currenttime, userChatAvatar);
            if (type.equals("1")) {
                String id = G.cmd.addchathistory(filemime, chatroomid, Message, "4", currenttime, "1", idpayamdaryafti, type, null, null, null, replyMessageRecive, replyFilehashRecive, replyFromRecive);
                newPostSingleChat(filemime, id, Message, "4", currenttime, "1", idpayamdaryafti, null, replyFilehashRecive, replyMessageRecive, replyFromRecive, type);

            } else if (type.equals("5")) {
                String id = G.cmd.addchathistory(filemime, chatroomid, Message, "4", currenttime, "1", idpayamdaryafti, type, null, null, null, replyMessageRecive, replyFilehashRecive, replyFromRecive);
                newPostSingleChat(filemime, id, Message, "4", currenttime, "1", idpayamdaryafti, null, replyFilehashRecive, replyMessageRecive, replyFromRecive, type);

            } else if (type.equals("6")) {
                String id = G.cmd.addchathistory(filemime, chatroomid, Message, "4", currenttime, "1", idpayamdaryafti, type, null, null, null, replyMessageRecive, replyFilehashRecive, replyFromRecive);
                newPostSingleChat(filemime, id, Message, "4", currenttime, "1", idpayamdaryafti, null, replyFilehashRecive, replyMessageRecive, replyFromRecive, type);

            } else {
                String id = G.cmd.addchathistory(filemime, chatroomid, Message, "4", currenttime, "1", idpayamdaryafti, type, Messagefilehash, Messagefileurl, Messagefilethumbnail, replyMessageRecive, replyFilehashRecive, replyFromRecive);
                G.cmd.Addfiles(Messagefilehash, Messagefileurl, Messagefilethumbnail, "0");
                newPostSingleChat(filemime, id, Message, "4", currenttime, "1", idpayamdaryafti, Messagefilehash, replyFilehashRecive, replyMessageRecive, replyFromRecive, type);

            }

            newRoomChat(chatroomid, ContactChatRecive, Message, currenttime, "0", "1", userChatAvatar);
            newRoomChatAddToAll(chatroomid, ContactChatRecive, Message, currenttime, userChatAvatar);

        } else {
            String chatroomid = G.cmd.updatechatrooms(ContactChatRecive, Message, currenttime);
            if (type.equals("1")) {
                String id = G.cmd.addchathistory(filemime, chatroomid, Message, "4", currenttime, "1", idpayamdaryafti, type, null, null, null, replyMessageRecive, replyFilehashRecive, replyFromRecive);
                newPostSingleChat(filemime, id, Message, "4", currenttime, "1", idpayamdaryafti, null, replyFilehashRecive, replyMessageRecive, replyFromRecive, type);

            } else if (type.equals("5")) {
                String id = G.cmd.addchathistory(filemime, chatroomid, Message, "4", currenttime, "1", idpayamdaryafti, type, null, null, null, replyMessageRecive, replyFilehashRecive, replyFromRecive);
                newPostSingleChat(filemime, id, Message, "4", currenttime, "1", idpayamdaryafti, null, replyFilehashRecive, replyMessageRecive, replyFromRecive, type);

            } else if (type.equals("6")) {
                String id = G.cmd.addchathistory(filemime, chatroomid, Message, "4", currenttime, "1", idpayamdaryafti, type, null, null, null, replyMessageRecive, replyFilehashRecive, replyFromRecive);
                newPostSingleChat(filemime, id, Message, "4", currenttime, "1", idpayamdaryafti, null, replyFilehashRecive, replyMessageRecive, replyFromRecive, type);

            } else {
                String id = G.cmd.addchathistory(filemime, chatroomid, Message, "4", currenttime, "1", idpayamdaryafti, type, Messagefilehash, Messagefileurl, Messagefilethumbnail, replyMessageRecive, replyFilehashRecive, replyFromRecive);
                G.cmd.Addfiles(Messagefilehash, Messagefileurl, Messagefilethumbnail, "0");
                newPostSingleChat(filemime, id, Message, "4", currenttime, "1", idpayamdaryafti, Messagefilehash, replyFilehashRecive, replyMessageRecive, replyFromRecive, type);
            }
            newPostChat(ContactChatRecive, Message, currenttime);
            newPostChatSendToAll(ContactChatRecive, Message, currenttime);
        }
        return currenttime;
    }


    public void joinroom(String roomid, String gchname, String description, String avatar_lq, String avatar_hq, String lastmsg, String lastdate, String membership) {

        muc = manager.getMultiUserChat(roomid);
        try {
            muc.join("igap");
        }
        catch (NoResponseException | XMPPErrorException | NotConnectedException e) {
            e.printStackTrace();
        }

        NewGroup(roomid, gchname, description, avatar_lq, avatar_hq, lastmsg, lastdate, membership);
        NewGroupSendToAll(roomid, gchname, description, avatar_lq, lastmsg, lastdate, membership);

    }


    @Override
    public boolean onUnbind(final Intent intent) {
        return super.onUnbind(intent);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private boolean checkMultipleChats() {
        int count = 0;
        int kind = 0;
        int singleChatUnread = G.cmd.selectUnreadCountSingleChat();
        int groupChatUnread = G.cmd.selectUnreadCountGroupChat();
        int ChannelUnread = G.cmd.selectUnreadCountChannel();

        if (singleChatUnread != 0) {
            kind = 1;
            count++;
        }
        if (groupChatUnread != 0) {
            kind = 2;
            count++;
        }

        if (ChannelUnread != 0) {
            kind = 3;
            count++;
        }

        unreadCount = (singleChatUnread + groupChatUnread + ChannelUnread);

        if (count > 1) {

            return true;
        }

        return checkMultipleUser(kind);
    }


    private boolean checkMultipleUser(int kind) {

        if (kind == 1) {

            int count = 0;
            String lastChatroomID = "";

            Cursor singleChatUnread = G.cmd.selectUnreadMessage("chathistory");
            while (singleChatUnread.moveToNext()) {
                String chatroomID = singleChatUnread.getString(singleChatUnread.getColumnIndex("chatroom_id"));

                if ( !chatroomID.equals(lastChatroomID)) {
                    count++;
                    lastChatroomID = chatroomID;
                }
            }
            singleChatUnread.close();
            if (count > 1) {

                return true;
            } else {
                unreadCount = G.cmd.getRowCountunreadchat(lastChatroomID);
                return false;
            }

        } else if (kind == 2) {

            int count = 0;
            String lastGroupChatroomID = "";
            String lastMessageSender = "";

            Cursor groupChatUnread = G.cmd.selectUnreadMessage("groupchathistory");
            while (groupChatUnread.moveToNext()) {

                String chatroomID = groupChatUnread.getString(groupChatUnread.getColumnIndex("groupchatroom_id"));

                if ( !chatroomID.equals(lastGroupChatroomID)) {
                    count++;
                    lastGroupChatroomID = chatroomID;
                }
            }
            groupChatUnread.close();
            if (count == 1) {

                while (groupChatUnread.moveToNext()) {
                    String messageSender = groupChatUnread.getString(groupChatUnread.getColumnIndex("msg_sender"));
                    if ( !messageSender.equals(lastMessageSender)) {
                        count++;
                        lastMessageSender = messageSender;
                    }
                }

                if (count > 2) {
                    return true;
                } else {
                    return false;
                }

            } else {
                return true;
            }

        } else if (kind == 3) {

            int count = 0;
            String lastChannelID = "";
            String lastMessageSender = "";

            Cursor channelUnread = G.cmd.selectUnreadChannelMessage();
            while (channelUnread.moveToNext()) {
                String channelID = channelUnread.getString(channelUnread.getColumnIndex("channeluid"));

                if ( !channelID.equals(lastChannelID)) {
                    count++;
                    lastChannelID = channelID;
                }
            }
            channelUnread.close();
            if (count == 1) {

                while (channelUnread.moveToNext()) {
                    String messageSender = channelUnread.getString(channelUnread.getColumnIndex("sender"));
                    if ( !messageSender.equals(lastMessageSender)) {
                        count++;
                        lastMessageSender = messageSender;
                    }
                }

                if (count > 2) {
                    return true;
                } else {
                    return false;
                }

            } else {
                return true;
            }

        }

        return false;

    }


    public void singleChatNotification(String payam, String userchat, String userchatavatar) {

        Bitmap userPhoto = null;

        String time;
        try {
            time = new TimerServies().getDateTime();
        }
        catch (Exception e) {
            time = helperGetTime.getTimeClock();
        }

        // Using RemoteViews to bind custom layouts into Notification
        fileCache = new FileCache(this);
        String mobile = userchat.split("@")[0];
        String userchatname;
        try {
            userchatname = G.cmd.namayeshname(1, mobile);
        }
        catch (Exception e) {
            userchatname = mobile;
        }
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.customnotification);
        File f = fileCache.getFile(userchatavatar);
        Bitmap b = decodeFile(f);
        if (b != null) {
            userPhoto = b;
        } else {
            try {
                Bitmap bitmap = null;
                URL imageUrl = new URL(userchatavatar);
                HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                conn.setRequestProperty("Authorization", basicAuth);
                conn.setConnectTimeout(30000);
                conn.setReadTimeout(30000);
                conn.setInstanceFollowRedirects(true);
                InputStream is = conn.getInputStream();
                OutputStream os = new FileOutputStream(f);
                Utils.CopyStream(is, os);
                os.close();
                bitmap = decodeFile(f);
                userPhoto = bitmap;
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        // Open NotificationView Class on Notification Click
        Intent intent = new Intent(this, MainActivity.class);

        // Open NotificationView.java Activity
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String ringtone = G.cmd.getsetting(9);

        ring = getSound(ringtone);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                // Set Icon
                .setSmallIcon(R.drawable.igap_notification_low)
                // Set Ticker Message
                .setTicker(payam)
                // Dismiss Notification
                .setAutoCancel(true)
                // Set PendingIntent into Notification
                .setContentIntent(pIntent)
                // Set RemoteViews into Notification
                .setContent(remoteViews)
                .setSound(ring);

        //Locate and set the Text into customnotificationtext.xml TextViews
        if (checkMultipleChats() == true) {

            remoteViews.setTextViewText(R.id.title, ""); //sender not showing in multiple sender and different chat
            remoteViews.setTextViewText(R.id.text, getString(R.string.you_have_en) + unreadCount + getString(R.string.unread_message_en));
            remoteViews.setTextViewText(R.id.txt_date, time);
            remoteViews.setTextViewText(R.id.txt_count, unreadCount + "");
            remoteViews.setImageViewResource(R.id.imagenotileft, R.drawable.igap_notification_large);
        } else {

            if (unreadCount == 1) {
                remoteViews.setTextViewText(R.id.title, userchatname); //sender not showing in multiple sender and different chat
                remoteViews.setTextViewText(R.id.text, payam);
                remoteViews.setTextViewText(R.id.txt_date, time);
                remoteViews.setTextViewText(R.id.txt_count, unreadCount + "");
            } else {
                remoteViews.setTextViewText(R.id.title, userchatname); //sender not showing in multiple sender and different chat
                remoteViews.setTextViewText(R.id.text, getString(R.string.you_have_en) + unreadCount + getString(R.string.unread_message_en));
                remoteViews.setTextViewText(R.id.txt_date, time);
                remoteViews.setTextViewText(R.id.txt_count, unreadCount + "");
            }

            if (userPhoto == null) {
                remoteViews.setImageViewResource(R.id.imagenotileft, R.drawable.igap_notification_large);
            } else {
                remoteViews.setImageViewBitmap(R.id.imagenotileft, userPhoto);
            }
        }
        ShortcutBadger.applyCount(G.context, unreadCount);

        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager
        notificationmanager.notify(G.NOTIFICATION_ID, builder.build());

    }


    public void groupChatNotification(String payam, String gchid, String sender, String senderavatar) {
        Bitmap userPhoto = null;

        String time;
        try {
            time = new TimerServies().getDateTime();
        }
        catch (Exception e) {
            time = helperGetTime.getTimeClock();
        }
        // Using RemoteViews to bind custom layouts into Notification
        fileCache = new FileCache(this);
        String gchname = G.cmd.getGroupChatInfo(2, gchid);
        //        String gchavatar = db.getGroupChatInfo(5, gchid);
        //        String gchdescription = db.getGroupChatInfo(8, gchid);
        //        String gchmembership = db.getGroupChatInfo(3, gchid);
        //        String gchactive = db.getGroupChatInfo(10, gchid);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.customnotification);
        File f = fileCache.getFile(senderavatar);
        Bitmap b = decodeFile(f);

        if (b != null) {
            userPhoto = b;
        } else {
            try {
                Bitmap bitmap = null;
                URL imageUrl = new URL(senderavatar);
                HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                conn.setRequestProperty("Authorization", basicAuth);
                conn.setConnectTimeout(30000);
                conn.setReadTimeout(30000);
                conn.setInstanceFollowRedirects(true);
                InputStream is = conn.getInputStream();
                OutputStream os = new FileOutputStream(f);
                Utils.CopyStream(is, os);
                os.close();
                bitmap = decodeFile(f);
                userPhoto = bitmap;
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // Open NotificationView Class on Notification Click
        Intent intent = new Intent(this, MainActivity.class);
        // Send data to NotificationView Class

        //        intent.putExtra("gchid", gchid);
        //        intent.putExtra("gchname", gchname);
        //        intent.putExtra("gchavatar", gchavatar);
        //        intent.putExtra("gchdescription", gchdescription);
        //        intent.putExtra("gchmembership", gchmembership);
        //        intent.putExtra("gchactive", gchactive);
        // Open NotificationView.java Activity
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        String ringtone = G.cmd.getsetting(9);

        ring = getSound(ringtone);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                // Set Icon
                .setSmallIcon(R.drawable.igap_notification_low)
                // Set Ticker Message
                .setTicker(payam)
                // Dismiss Notification
                .setAutoCancel(true)
                // Set PendingIntent into Notification
                .setContentIntent(pIntent)
                // Set RemoteViews into Notification
                .setContent(remoteViews)
                .setSound(ring);

        // Locate and set the Text into customnotificationtext.xml TextViews
        if (checkMultipleChats() == true) {
            remoteViews.setTextViewText(R.id.title, ""); //sender not showing in multiple sender and different chat
            remoteViews.setTextViewText(R.id.text, "You Have " + unreadCount + " Unread Message !");
            remoteViews.setTextViewText(R.id.txt_date, time);
            remoteViews.setTextViewText(R.id.txt_count, unreadCount + "");
            remoteViews.setImageViewResource(R.id.imagenotileft, R.drawable.igap_notification_large);
        } else {

            if (unreadCount == 1) {
                remoteViews.setTextViewText(R.id.title, sender + " at " + gchname); //sender not showing in multiple sender and different chat
                remoteViews.setTextViewText(R.id.text, payam);
                remoteViews.setTextViewText(R.id.txt_date, time);
                remoteViews.setTextViewText(R.id.txt_count, unreadCount + "");
            } else {
                remoteViews.setTextViewText(R.id.title, sender + " at " + gchname); //sender not showing in multiple sender and different chat
                remoteViews.setTextViewText(R.id.text, "You Have " + unreadCount + " Unread Message !");
                remoteViews.setTextViewText(R.id.txt_date, time);
                remoteViews.setTextViewText(R.id.txt_count, unreadCount + "");
            }

            if (userPhoto == null) {
                remoteViews.setImageViewResource(R.id.imagenotileft, R.drawable.igap_notification_large);
            } else {
                remoteViews.setImageViewBitmap(R.id.imagenotileft, userPhoto);
            }
        }

        ShortcutBadger.applyCount(G.context, unreadCount);
        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationmanager.notify(G.NOTIFICATION_ID, builder.build());
    }


    public void channelNotification(String payam, String gchid, String sender, String senderavatar) {

        Bitmap userPhoto = null;

        String time;
        try {
            time = new TimerServies().getDateTime();
        }
        catch (Exception e) {
            time = helperGetTime.getTimeClock();
        }

        fileCache = new FileCache(this);
        //        String gchname = db.selectField("channels", "uid = '" + gchid + "'", 2);
        //        String gchavatar = db.selectField("channels", "uid = '" + gchid + "'", 5);
        //        String gchdescription = db.selectField("channels", "uid = '" + gchid + "'", 3);
        //        String gchmembership = db.selectField("channels", "uid = '" + gchid + "'", 10);
        //        String channelmembersnumber = db.selectField("channels", "uid = '" + gchid + "'", 4);
        //        String channelactive = db.selectField("channels", "uid = '" + gchid + "'", 11);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.customnotification);
        File f = fileCache.getFile(senderavatar);
        Bitmap b = decodeFile(f);

        if (b != null) {
            userPhoto = b;
        } else {
            try {
                Bitmap bitmap = null;
                URL imageUrl = new URL(senderavatar);
                HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                conn.setRequestProperty("Authorization", basicAuth);
                conn.setConnectTimeout(30000);
                conn.setReadTimeout(30000);
                conn.setInstanceFollowRedirects(true);
                InputStream is = conn.getInputStream();
                OutputStream os = new FileOutputStream(f);
                Utils.CopyStream(is, os);
                os.close();
                bitmap = decodeFile(f);
                userPhoto = bitmap;
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        Intent intent = new Intent(this, MainActivity.class);

        //        intent.putExtra("channeluid", gchid);
        //        intent.putExtra("channelName", gchname);
        //        intent.putExtra("channelavatarlq", gchavatar);
        //        intent.putExtra("channelDesc", gchdescription);
        //        intent.putExtra("channelmembership", gchmembership);
        //        intent.putExtra("channelmembersnumber", channelmembersnumber);
        //        intent.putExtra("channelactive", channelactive);

        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        String ringtone = G.cmd.getsetting(9);

        ring = getSound(ringtone);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.igap_notification_low)
                .setTicker(payam)
                .setAutoCancel(true)
                .setContentIntent(pIntent)
                .setContent(remoteViews)
                .setSound(ring);

        if (checkMultipleChats() == true) {
            remoteViews.setTextViewText(R.id.title, ""); //sender not showing in multiple sender and different chat
            remoteViews.setTextViewText(R.id.text, "You Have " + unreadCount + " Unread Message !");
            remoteViews.setTextViewText(R.id.txt_date, time);
            remoteViews.setTextViewText(R.id.txt_count, unreadCount + "");
            remoteViews.setImageViewResource(R.id.imagenotileft, R.drawable.igap_notification_large);
        } else {

            if (unreadCount == 1) {
                remoteViews.setTextViewText(R.id.title, sender + " Channel"); //sender not showing in multiple sender and different chat
                remoteViews.setTextViewText(R.id.text, payam);
                remoteViews.setTextViewText(R.id.txt_date, time);
                remoteViews.setTextViewText(R.id.txt_count, unreadCount + "");
            } else {
                remoteViews.setTextViewText(R.id.title, sender + " Channel"); //sender not showing in multiple sender and different chat
                remoteViews.setTextViewText(R.id.text, "You Have " + unreadCount + " Unread Message !");
                remoteViews.setTextViewText(R.id.txt_date, time);
                remoteViews.setTextViewText(R.id.txt_count, unreadCount + "");
            }

            if (userPhoto == null) {
                remoteViews.setImageViewResource(R.id.imagenotileft, R.drawable.igap_notification_large);
            } else {
                remoteViews.setImageViewBitmap(R.id.imagenotileft, userPhoto);
            }
        }

        ShortcutBadger.applyCount(G.context, unreadCount);

        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationmanager.notify(G.NOTIFICATION_ID, builder.build());
    }


    private Uri getSound(String ringtone) {

        Uri ring;

        if (ringtone.equals("0")) {
            ring = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.igap);
        } else if (ringtone.equals("1")) {
            ring = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.aooow);
        } else if (ringtone.equals("2")) {
            ring = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.bbalert);
        } else if (ringtone.equals("3")) {
            ring = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.boom);
        } else if (ringtone.equals("4")) {
            ring = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.bounce);
        } else if (ringtone.equals("5")) {
            ring = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.doodoo);
        } else if (ringtone.equals("6")) {
            ring = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.none);
        } else if (ringtone.equals("7")) {
            ring = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.jing);
        } else if (ringtone.equals("8")) {
            ring = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.lili);
        } else if (ringtone.equals("9")) {
            ring = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.msg);
        } else if (ringtone.equals("10")) {
            ring = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.newa);
        } else if (ringtone.equals("11")) {
            ring = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.onelime);
        } else if (ringtone.equals("12")) {
            ring = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tone);
        } else if (ringtone.equals("13")) {
            ring = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.woow);
        } else {
            ring = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.igap);
        }

        return ring;

    }


    private Bitmap decodeFile(File f) {
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 250;
            int width_tmp = o.outWidth;

            int scale = width_tmp / REQUIRED_SIZE;
            if (scale < 1)
                scale = 1;

            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            Bitmap bi = BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
            return bi;
        }
        catch (FileNotFoundException e) {}
        return null;
    }


    private void NewGroup(String uid, String name, String description, String avatar_lq, String avatar_hq, String lastmsg, String lastdate, String membership) {
        Intent intent = new Intent("addNewGroup");
        intent.putExtra("uid", uid);
        intent.putExtra("name", name);
        intent.putExtra("description", description);
        intent.putExtra("avatar_lq", avatar_lq);
        intent.putExtra("avatar_hq", avatar_hq);
        intent.putExtra("membership", membership);
        intent.putExtra("lastmsg", lastmsg);
        intent.putExtra("lastdate", lastdate);
        LocalBroadcastManager.getInstance(cc).sendBroadcast(intent);
    }


    private void NewGroupSendToAll(String uid, String name, String description, String avatar_lq, String lastmsg, String lastdate, String membership) {
        Intent intent = new Intent("addNewInAll");
        intent.putExtra("MODEL", "2");
        intent.putExtra("UID", uid);
        intent.putExtra("NAME", name);
        intent.putExtra("DESC", description);
        intent.putExtra("AVATAR_LQ", avatar_lq);
        intent.putExtra("MEMBERSHIP", membership);
        intent.putExtra("LAST_MSG", lastmsg);
        intent.putExtra("LAST_DATE", lastdate);
        LocalBroadcastManager.getInstance(cc).sendBroadcast(intent);
    }


    private void groupNewPost(String uid, String lastmsg, String lastdate) {
        Intent intent = new Intent("newPostGroup");
        intent.putExtra("uid", uid);
        intent.putExtra("lastmsg", lastmsg);
        intent.putExtra("lastdate", lastdate);
        LocalBroadcastManager.getInstance(cc).sendBroadcast(intent);
    }


    private void groupNewPostSendToAll(String uid, String lastmsg, String lastdate) {
        Intent intent = new Intent("newPostAll");
        intent.putExtra("UID", uid);
        intent.putExtra("MODEL", "2"); // 2 = group
        intent.putExtra("LAST_MSG", lastmsg);
        intent.putExtra("LAST_DATE", lastdate);
        LocalBroadcastManager.getInstance(cc).sendBroadcast(intent);
    }


    private void kickedFromGroup(String uid, String lastmsg, String lastdate) {
        Intent intent = new Intent("kikedFromGroup");
        intent.putExtra("uid", uid);
        intent.putExtra("lastmsg", lastmsg);
        intent.putExtra("lastdate", lastdate);
        LocalBroadcastManager.getInstance(cc).sendBroadcast(intent);
    }


    private void kickedFromSendToAll(String uid, String lastmsg, String lastdate) {
        Intent intent = new Intent("kikedFromGroupaAll");
        intent.putExtra("MODEL", "2");
        intent.putExtra("UID", uid);
        intent.putExtra("LAST_MSG", lastmsg);
        intent.putExtra("LAST_DATE", lastdate);
        LocalBroadcastManager.getInstance(cc).sendBroadcast(intent);
    }


    private void updateSeenChat(String uid) {
        Intent intent = new Intent("updateSeenChat");
        intent.putExtra("uid", uid);
        LocalBroadcastManager.getInstance(cc).sendBroadcast(intent);
        Intent intent1 = new Intent("loadall");
        LocalBroadcastManager.getInstance(cc).sendBroadcast(intent1);
    }


    private void newPostChat(String uid, String lastmsg, String lastdate) {
        Intent intent = new Intent("NewPostChat");
        intent.putExtra("uid", uid);
        intent.putExtra("lastmsg", lastmsg);
        intent.putExtra("lastdate", lastdate);
        LocalBroadcastManager.getInstance(cc).sendBroadcast(intent);
    }


    private void newPostChatSendToAll(String uid, String lastmsg, String lastdate) {
        Intent intent = new Intent("newPostAll");
        intent.putExtra("MODEL", "1"); // 1 = SingleChat
        intent.putExtra("UID", uid);
        intent.putExtra("LAST_MSG", lastmsg);
        intent.putExtra("LAST_DATE", lastdate);
        LocalBroadcastManager.getInstance(cc).sendBroadcast(intent);
    }


    private void newRoomChat(String roomid, String userchat, String lastmessage, String lasttime, String sound, String active, String userchatavatar) {
        Intent intent = new Intent("NewRoomChat");
        intent.putExtra("roomid", roomid);
        intent.putExtra("userchat", userchat);
        intent.putExtra("lastmessage", lastmessage);
        intent.putExtra("lasttime", lasttime);
        intent.putExtra("sound", sound);
        intent.putExtra("active", active);
        intent.putExtra("userchatavatar", userchatavatar);
        LocalBroadcastManager.getInstance(cc).sendBroadcast(intent);
    }


    private void newRoomChatAddToAll(String roomid, String userchat, String lastmessage, String lasttime, String userchatavatar) {
        Intent intent = new Intent("addNewInAll");
        intent.putExtra("MODEL", "1");
        intent.putExtra("ROOM_ID", roomid);
        intent.putExtra("USERCHAT", userchat);
        intent.putExtra("LAST_MSG", lastmessage);
        intent.putExtra("LAST_DATE", lasttime);
        intent.putExtra("USERCHAT_AVATAR", userchatavatar);
        LocalBroadcastManager.getInstance(cc).sendBroadcast(intent);
    }


    private void updateMsgStatusSingleChat(String msgid, String status) {
        if (G.mainUserChat.equals(ContactChatRecive)) { // payam az kasi hast ke dar hale chat kardan ba mast.
            Intent intent = new Intent("updateSeenSingleChat");
            intent.putExtra("msgid", msgid);
            intent.putExtra("status", status);
            LocalBroadcastManager.getInstance(cc).sendBroadcast(intent);
        }
    }


    private void newPostSingleChat(String filemime, String id, String msg, String status, String msgtime, String msgtype, String msgid, String filehash, String replyfilehash, String replymessage, String replyfrom, String type) {

        if (G.mainUserChat.equals(ContactChatRecive)) { // payam az kasi hast ke dar hale chat kardan ba mast.

            Intent intent = new Intent("newPostSingleChat");
            intent.putExtra("id", id);
            intent.putExtra("msg", msg);
            intent.putExtra("status", status);
            intent.putExtra("msgtime", msgtime);
            intent.putExtra("msgtype", msgtype);
            intent.putExtra("msgtime", msgtime);
            intent.putExtra("msgid", msgid);
            intent.putExtra("filehash", filehash);
            intent.putExtra("replyfilehash", replyfilehash);
            intent.putExtra("replymessage", replymessage);
            intent.putExtra("replyfrom", replyfrom);
            intent.putExtra("type", type);
            intent.putExtra("filemime", filemime);
            LocalBroadcastManager.getInstance(cc).sendBroadcast(intent);
        }
    }


    private void sendNewPostSingleChat(String filemime, String id, String msg, String status, String msgtime, String msgtype, String msgid, String filehash, String replyfilehash, String replymessage, String replyfrom, String type) {

        Intent intent = new Intent("sendNewPostSingleChat");
        intent.putExtra("id", id);
        intent.putExtra("msg", msg);
        intent.putExtra("status", status);
        intent.putExtra("msgtime", msgtime);
        intent.putExtra("msgtype", msgtype);
        intent.putExtra("msgtime", msgtime);
        intent.putExtra("msgid", msgid);
        intent.putExtra("filehash", filehash);
        intent.putExtra("replyfilehash", replyfilehash);
        intent.putExtra("replymessage", replymessage);
        intent.putExtra("replyfrom", replyfrom);
        intent.putExtra("type", type);
        intent.putExtra("filemime", filemime);
        LocalBroadcastManager.getInstance(cc).sendBroadcast(intent);
    }


    private void updateStatusfileSingleChat(String filehash, String msgid, String status) {

        if (G.mainUserChat.equals(ContactChatRecive) || G.mainUserChat.equals(userChatSendMessage)) { // payam az kasi hast ke dar hale chat kardan ba mast.

            Intent intent = new Intent("updateStatusfileSingleChat");
            intent.putExtra("filehash", filehash);
            intent.putExtra("msgid", msgid);
            intent.putExtra("status", status);
            LocalBroadcastManager.getInstance(cc).sendBroadcast(intent);
        }
    }


    private void updateStatusfileGroupChat(String msgid, String status) {
        if (G.mainGroupID.equals(groupChatID)) { //agar payam az kasi hast ke dar hale chat kardan ba mast.

            Intent intent = new Intent("UpdateStatusfileGroupChat");
            intent.putExtra("msgid", msgid);
            intent.putExtra("status", status);
            LocalBroadcastManager.getInstance(cc).sendBroadcast(intent);
        }
    }


    private void updateIdfileGroupChat(String filehash, String msgid) {
        if (G.mainGroupID.equals(groupChatID)) { //agar payam az kasi hast ke dar hale chat kardan ba mast.

            Intent intent = new Intent("UpdateIdfileGroupChat");
            intent.putExtra("filehash", filehash);
            intent.putExtra("msgid", msgid);
            LocalBroadcastManager.getInstance(cc).sendBroadcast(intent);
        }
    }


    private void updateIdfileGroupChatFromSender(String filehash, String msgid) { // ma ferestandeye payam hastim
        Intent intent = new Intent("UpdateIdfileGroupChat");
        intent.putExtra("filehash", filehash);
        intent.putExtra("msgid", msgid);
        LocalBroadcastManager.getInstance(cc).sendBroadcast(intent);
    }


    private void updateViewAfterResendSingleChat() {
        Intent intent = new Intent("updateViewAfterResendSingleChat");
        LocalBroadcastManager.getInstance(cc).sendBroadcast(intent);
    }


    private void newPostGroupChat(String filemime, String id, String msg, String status, String msgtime, String msgtype, String username, String filehash, String replyfilehash, String replymessage
                                  , String replyfrom, String type, String msgsender, String msgsenderavatar, String msgid) {
        if (G.mainGroupID.equals(groupChatID)) { //agar payam az kasi hast ke dar hale chat kardan ba mast.
            Intent intent = new Intent("NewPostGroupChat");
            intent.putExtra("id", id);
            intent.putExtra("msg", msg);
            intent.putExtra("status", status);
            intent.putExtra("msgtime", msgtime);
            intent.putExtra("msgtype", msgtype);
            intent.putExtra("username", username);
            intent.putExtra("filehash", filehash);
            intent.putExtra("replyfilehash", replyfilehash);
            intent.putExtra("replymessage", replymessage);
            intent.putExtra("replyfrom", replyfrom);
            intent.putExtra("type", type);
            intent.putExtra("msgsender", msgsender);
            intent.putExtra("msgsenderavatar", msgsenderavatar);
            intent.putExtra("msgid", msgid);
            intent.putExtra("filemime", filemime);
            LocalBroadcastManager.getInstance(cc).sendBroadcast(intent);
        }
    }


    private void updateViewAfterResendGroupChat() {
        Intent intent = new Intent("updateViewAfterResendGroupChat");
        LocalBroadcastManager.getInstance(cc).sendBroadcast(intent);
    }


    private void newPostGroupChatFromSender(String filemime, String id, String msg, String status, String msgtime, String msgtype, String username, String filehash, String replyfilehash, String replymessage
                                            , String replyfrom, String type, String msgsender, String msgsenderavatar, String msgid) {
        Intent intent = new Intent("newPostGroupChatFromSender");
        intent.putExtra("id", id);
        intent.putExtra("msg", msg);
        intent.putExtra("status", status);
        intent.putExtra("msgtime", msgtime);
        intent.putExtra("msgtype", msgtype);
        intent.putExtra("username", username);
        intent.putExtra("filehash", filehash);
        intent.putExtra("replyfilehash", replyfilehash);
        intent.putExtra("replymessage", replymessage);
        intent.putExtra("replyfrom", replyfrom);
        intent.putExtra("type", type);
        intent.putExtra("msgsender", msgsender);
        intent.putExtra("msgsenderavatar", msgsenderavatar);
        intent.putExtra("msgid", msgid);
        intent.putExtra("filemime", filemime);

        LocalBroadcastManager.getInstance(cc).sendBroadcast(intent);

    }


    private void newPostChannel(String mime, String id, String msg, String status, String msgtime, String msgtype, String filehash, String replyfilehash, String replymessage
                                , String replyfrom, String msgView, String msgsender, String msgid) {
        if (G.mainChannelUid.equals(channelUID)) { //agar payam az kasi hast ke dar hale chat kardan ba mast.

            G.cmd.updateChannelMessageStatus(id, "1", msgid);

            Intent intent = new Intent("NewPostChannel");
            intent.putExtra("id", id);
            intent.putExtra("msg", msg);
            intent.putExtra("status", status);
            intent.putExtra("msgtime", msgtime);
            intent.putExtra("msgtype", msgtype);
            intent.putExtra("view", msgView);
            intent.putExtra("filehash", filehash);
            intent.putExtra("replyfilehash", replyfilehash);
            intent.putExtra("replymessage", replymessage);
            intent.putExtra("replyfrom", replyfrom);
            intent.putExtra("msgsender", msgsender);
            intent.putExtra("msgid", msgid);
            intent.putExtra("filemime", mime);

            LocalBroadcastManager.getInstance(cc).sendBroadcast(intent);
        }
    }


    private void newPostChannelList(String uid, String lastMsg, String lastDate) {
        Intent intent = new Intent("newPostChannelList");
        intent.putExtra("uid", uid);
        intent.putExtra("lastmsg", lastMsg);
        intent.putExtra("lastdate", lastDate);
        LocalBroadcastManager.getInstance(cc).sendBroadcast(intent);
    }


    private void NewPostSupport(String msgid, String msg, String msgtime, String msgtype, String status) {
        Intent intent = new Intent("NewPostSupport");
        intent.putExtra("msg", msg);
        intent.putExtra("status", status);
        intent.putExtra("msgtime", msgtime);
        intent.putExtra("msgtype", msgtype);
        intent.putExtra("msgid", msgid);
        LocalBroadcastManager.getInstance(cc).sendBroadcast(intent);

    }


    private void changeRole(String role) { // send to channel

        if (G.mainChannelUid.equals(channelUID)) {
            Intent intent = new Intent("ChangeRole");
            intent.putExtra("ROLE", role);
            LocalBroadcastManager.getInstance(cc).sendBroadcast(intent);
        }
    }


    private void changeRoleInPageMessagingChannel(String uid, String role) { // send to PageMessagingChannel

        Intent intent = new Intent("ChangeRolePageMessagingChannel");
        intent.putExtra("UID", uid);
        intent.putExtra("ROLE", role);
        LocalBroadcastManager.getInstance(cc).sendBroadcast(intent);
    }


    private void changeRoleInPageMessagingAll(String uid, String role) { // send to PageMessagingAll

        Intent intent = new Intent("ChangeRolePageMessagingAll");
        intent.putExtra("MODEL", "3");
        intent.putExtra("UID", uid);
        intent.putExtra("ROLE", role);
        LocalBroadcastManager.getInstance(cc).sendBroadcast(intent);
    }


    private void sendconnectionstate(String state) {
        G.connectionState = state;
        Intent intent = new Intent("connectionstateReceiver");
        intent.putExtra("state", state);
        LocalBroadcastManager.getInstance(cc).sendBroadcast(intent);
    }


    private void newpaintt(String message) {
        Intent intent = new Intent("newpaint");
        intent.putExtra("message", message);
        LocalBroadcastManager.getInstance(cc).sendBroadcast(intent);
    }


    private void clearPaint() {
        Intent intent = new Intent("ClearPaint");
        LocalBroadcastManager.getInstance(cc).sendBroadcast(intent);
    }


    private void updateSupportUnread() {
        Intent intentAll = new Intent("updateSeenSupportChat");
        LocalBroadcastManager.getInstance(cc).sendBroadcast(intentAll);
    }


    private void paintrequestbroad() {
        Intent intentcccAll = new Intent("paintrequestbroad");
        LocalBroadcastManager.getInstance(cc).sendBroadcast(intentcccAll);
    }
}
