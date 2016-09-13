// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.services;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.support.v4.content.LocalBroadcastManager;
import com.iGap.adapter.G;
import com.iGap.helpers.HelperGetTime;
import com.iGap.instruments.JSONParser;
import com.iGap.instruments.database;


/**
 * 
 * after program start this class check group and channel from server and update list chat and channel and group
 *
 */

public class SplashService extends Service {

    private ArrayList<String> groupChatRoomsID         = new ArrayList<String>();
    private ArrayList<String> onlineGroupChatRoomsID   = new ArrayList<String>();
    private ArrayList<String> lostGroupChatRoomsID     = new ArrayList<String>();
    private ArrayList<String> channelChatRoomsID       = new ArrayList<String>();
    private ArrayList<String> onlineChannelChatRoomsID = new ArrayList<String>();
    private ArrayList<String> lostChannelChatRoomsID   = new ArrayList<String>();
    private ArrayList<String> list                     = new ArrayList<String>();
    private ArrayList<String> list2                    = new ArrayList<String>();
    private ArrayList<String> list3                    = new ArrayList<String>();

    private String[]          lastSeen;
    private String[]          fullname;
    private String[]          mobile;
    private String[]          registered;
    private String[]          avatar_hq;
    private String[]          avatar_lq;
    private String[]          uid;
    private String[]          contactid;

    private int               sizeOffline, sizeOfflineChannel;
    private long              utcMillis;
    private database          db;
    private String            currentTime;
    private String            basicAuth                = "0";
    private Context           mContext;
    private JSONArray         array                    = new JSONArray();
    private JSONArray         products                 = new JSONArray();
    private JSONObject        Parent                   = new JSONObject();
    private JSONParser        jParser                  = new JSONParser();


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        mContext = SplashService.this;

        db = new database(mContext);
        db.useable();
        db.open();

        String country = db.getCountry();
        String utc = db.select("Countries", "country_name = '" + country + "'", 5);
        utcMillis = Integer.parseInt(utc) * 1000;

        //==============SelectAllGroupsID Start

        Cursor cursor = db.selectAllGroupID();
        sizeOffline = cursor.getCount();
        while (cursor.moveToNext()) {
            String groupChatID = cursor.getString(cursor.getColumnIndex("groupchatid"));
            groupChatRoomsID.add(groupChatID);
        }
        cursor.close();

        //==============SelectAllGroupsID End

        //==============SelectAllChannelID Start 

        Cursor cursorChannel = db.selectAllChannelID();
        sizeOfflineChannel = cursorChannel.getCount();
        while (cursorChannel.moveToNext()) {
            String channelChatID = cursorChannel.getString(cursorChannel.getColumnIndex("uid"));
            channelChatRoomsID.add(channelChatID);
        }
        cursorChannel.close();

        //==============SelectAllChannelID End

        try {
            currentTime = new TimerServies().getDateTime();
        }
        catch (Exception e) {
            HelperGetTime helperGetTime = new HelperGetTime();
            currentTime = helperGetTime.getTime();
        }

        basicAuth = db.namayesh4(3, "info");

        new CheckChannels().execute();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return Service.START_NOT_STICKY;

    }


    private class CheckChannels extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {

            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                JSONObject jsonobj = jParser.getJSONFromUrl(G.createchannel, params, "GET", basicAuth, null);

                try {

                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);
                    Boolean success = json.getBoolean(G.TAG_SUCCESS);

                    if (success) {
                        JSONArray result = json.getJSONArray("result");

                        int sizeOnlineChannel = result.length();

                        for (int i = 0; i < result.length(); i++) {
                            JSONObject response = result.getJSONObject(i);
                            String uid = response.getString("uid");
                            String name = response.getString("name");
                            String description = response.getString("description");
                            String totalMembar = response.getString("totalMember");
                            String avatarHq = response.getString("avatarHq");
                            String avatarLq = response.getString("avatarLq");
                            String role = response.getString("role");
                            //String active = response.getString("active");

                            onlineChannelChatRoomsID.add(uid);

                            String memberShip = "";

                            int exist = db.isChannelExist("channels", uid);

                            if (exist == 0) {

                                if (role.equals("owners")) {
                                    memberShip = "1";
                                } else if (role.equals("admins")) {
                                    memberShip = "2";
                                } else if (role.equals("members")) {
                                    memberShip = "0";
                                }

                                db.addchannel(uid, name, description, totalMembar, avatarLq, avatarHq, "", "", "0", memberShip);
                                notifyAddChannel(uid, name, description, totalMembar, avatarLq, avatarHq, "", "", memberShip);

                                new GetChannelMessage().execute("", "3", uid);

                            } else {

                                if (role.equals("owners")) {
                                    memberShip = "1";
                                } else if (role.equals("admins")) {
                                    memberShip = "2";
                                } else if (role.equals("members")) {
                                    memberShip = "0";
                                }

                                db.updateChannelsInfoInSplash(uid, name, description, totalMembar, avatarLq, avatarHq, memberShip);
                                notifyUpdateChannels(uid, name, description, totalMembar, avatarLq, avatarHq, memberShip);

                                String lastMsgID = db.selectLastChannelMsgWithTime(uid);

                                if ( !lastMsgID.equals("")) { // agar payami dar channel vojud dasht az an be bad ra begir

                                    new GetChannelMessage().execute(lastMsgID, "2", uid);

                                } else { // hameye payamhara begir

                                    new GetChannelMessage().execute("", "3", uid);
                                }
                            }
                        }

                        boolean existChat = false;

                        if (sizeOnlineChannel < sizeOfflineChannel) {
                            for (String offlineChatID: channelChatRoomsID) {
                                existChat = false;
                                for (String onlineChatID: onlineChannelChatRoomsID) {
                                    if (onlineChatID.equals(offlineChatID)) {
                                        existChat = true;
                                    }
                                }
                                if ( !existChat) {
                                    lostChannelChatRoomsID.add(offlineChatID);
                                } else {
                                    existChat = false;
                                }
                            }

                            for (String channelChatID: lostChannelChatRoomsID) {
                                String active = db.selectActiveChannel(11, channelChatID);
                                if ( !active.equals("2")) {
                                    db.updateActiveChannel(channelChatID, "2");
                                    db.addchannelhistory(channelChatID, "kick", "This channel is closed", currentTime, "100", "", "0", "", "", "0", "");
                                    notifyActiveItemChannel(channelChatID, "2", "This channel is closed", currentTime);
                                }
                            }
                        }
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String v) {
            new CheckGroups().execute();
        }
    }


    private class CheckGroups extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {

            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                JSONObject jsonobj = jParser.getJSONFromUrl(G.creategroupchat, params, "GET", basicAuth, null);

                try {

                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);
                    Boolean success = json.getBoolean(G.TAG_SUCCESS);

                    if (success) {
                        JSONArray result = json.getJSONArray("result");

                        int sizeOnline = result.length();

                        for (int i = 0; i < result.length(); i++) {
                            JSONObject response = result.getJSONObject(i);
                            String id = response.getString("id");
                            String name = response.getString("name");
                            String description = response.getString("description");
                            String avatarLq = response.getString("avatarLq");
                            String avatarHq = response.getString("avatarHq");
                            String role = response.getString("role");
                            //String totalUser = response.getString("total_user");
                            String currentUser = response.getString("current_user");

                            onlineGroupChatRoomsID.add(id + "@group.igap.im");

                            String gid = id + "@group.igap.im";
                            int exist = db.isGroupExist("groupchatrooms", gid);
                            if (exist == 0) {

                                if (role.equals("members")) {
                                    role = "2";
                                } else if (role.equals("owners")) {
                                    role = "1";
                                }
                                db.Addgroupchat(gid, name, role, currentUser, avatarLq, avatarHq, "", "", description);
                                notifyAddgroupchat(gid, name, description, avatarLq, avatarHq, "", "", role);

                            } else {

                                if (role.equals("members")) {
                                    role = "2";
                                } else if (role.equals("owners")) {
                                    role = "1";
                                }

                                db.updateGroupsInfoInSplash(gid, name, role, currentUser, avatarLq, avatarHq, description);

                                notifyUpdateGroups(gid, name, description, avatarLq, avatarHq, role);

                            }
                        }

                        boolean existChat = false;
                        if (sizeOnline < sizeOffline) {

                            for (String offlineChatID: groupChatRoomsID) {
                                existChat = false;
                                for (String onlineChatID: onlineGroupChatRoomsID) {
                                    if (onlineChatID.equals(offlineChatID)) {
                                        existChat = true;
                                    }
                                }
                                if ( !existChat) {
                                    lostGroupChatRoomsID.add(offlineChatID);
                                } else {
                                    existChat = false;
                                }
                            }
                            for (String groupChatID: lostGroupChatRoomsID) {
                                String active = db.selectActive(10, groupChatID);
                                if ( !active.equals("2")) {
                                    db.updateActive(groupChatID, "2");
                                    db.addgroupchathistory("", groupChatID, "kick", "You KicKed From This Group!", "4", currentTime, "1", "", "", "100", "", "", "", "", "", "", "");
                                    notifyActiveItemGroup(groupChatID, "2", "You KicKed From This Group!", currentTime);
                                }
                            }
                        }
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String v) {
            readContact();

        }
    }


    class GetContactsFromServer extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                JSONObject jsonobj = jParser.getJSONFromUrl(G.allcontact, params, "BODY", basicAuth, array.toString());

                try {
                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);

                    Boolean success = json.getBoolean("success");

                    if (success) {
                        JSONObject result = json.getJSONObject("result");

                        products = result.getJSONArray("contacts");
                        //  db.resetTables("Contacts");
                        int save = products.length();
                        contactid = new String[save];
                        mobile = new String[save];
                        registered = new String[save];
                        avatar_hq = new String[save];
                        avatar_lq = new String[save];
                        uid = new String[save];
                        lastSeen = new String[save];
                        fullname = new String[save];
                        for (int i = 0; i < save; i++) {
                            JSONObject c = products.getJSONObject(i);
                            contactid[i] = c.getString("contactId");
                            mobile[i] = c.getString("mobile");
                            registered[i] = c.getString("registered");
                            avatar_hq[i] = c.getString("avatar_hq");
                            avatar_lq[i] = c.getString("avatar_lq");
                            uid[i] = c.getString("uid");

                            String s = c.getString("lastSeen");

                            lastSeen[i] = HelperGetTime.getStringTime(s, db);

                            try {
                                Uri uri = Uri.parse(Contacts.CONTENT_LOOKUP_URI + "/" + contactid[i]);
                                String[] projection = new String[]{ Contacts._ID, Contacts.DISPLAY_NAME, };
                                Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                                if ( !cursor.moveToNext()) // move to first (and only) row.
                                    throw new IllegalStateException("contact no longer exists for key");
                                String name = cursor.getString(1);

                                cursor.close();
                                fullname[i] = name;

                                String registerd;

                                if (registered[i].equals("true")) {
                                    registerd = "1";
                                } else {
                                    registerd = "0";
                                }

                                db.updateContacts(fullname[i], mobile[i], registerd, avatar_lq[i], avatar_hq[i], lastSeen[i], uid[i]);
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        db.getNewAvatarChatRooms();

                        db.deleteUseLessContact(mobile);

                        notifyAddContacts();

                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }

    }


    private class GetChannelMessage extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... args) {
            try {

                String msgID = args[0];
                String item = args[1];
                String channeluid = args[2];

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                JSONObject jsonobj;
                if (item.equals("2")) {
                    jsonobj = jParser.getJSONFromUrl(G.createchannel + "/" + channeluid + "/posts?last=" + msgID, params, "GET", basicAuth, null);
                } else {
                    jsonobj = jParser.getJSONFromUrl(G.createchannel + "/" + channeluid + "/posts?", params, "GET", basicAuth, null);
                }

                try {
                    jsonobj.getString("statuscode");
                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);

                    boolean success1 = json.getBoolean(G.TAG_SUCCESS);

                    if (success1 == true) {

                        JSONArray result = json.getJSONArray("result");

                        int resultSize = result.length();

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
                            String mainTime = HelperGetTime.convertTime(addDate, addTime, utcMillis);
                            String ext;

                            if (hash == null || hash.equals("null")) {
                                db.addchannelhistory(channeluid, id, text, mainTime, "1", null, "0", visit, "0", "0", "");
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

                                    db.addchannelhistory(channeluid, id, text, mainTime, ext, hash, "0", visit, "0", "0", extention);
                                    db.Addfiles(hash, url, thumbLq, "0");
                                } else {
                                    // file with text
                                    db.addchannelhistory(channeluid, id, text, mainTime, ext, hash, "0", visit, "0", "0", extention);
                                    db.Addfiles(hash, url, thumbLq, "0");
                                }
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

                            String mainTime = HelperGetTime.convertTime(addDate, addTime, utcMillis);
                            db.updatechannels(channeluid, text, mainTime);
                            newPostChannelList(channeluid, text, mainTime);
                            channelNewPostSendToAll(channeluid, text, mainTime);
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
        protected void onPostExecute(String file_url) {}
    }


    private void readContact() {
        list = new ArrayList<String>();
        list2 = new ArrayList<String>();
        list3 = new ArrayList<String>();

        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if (phones == null) {
            String[] projection = { ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY };
            phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, null);
        }

        while (phones.moveToNext())
        {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String contactId = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY));

            list.add(name);
            list2.add(phoneNumber);
            list3.add(contactId);
        }
        phones.close();

        Parent = new JSONObject();
        array = new JSONArray();

        for (int i = 0; i < list.size(); i++)
        {
            JSONObject jsonObj = new JSONObject();

            try {
                jsonObj.put("fullname", list.get(i));
                jsonObj.put("mobile", list2.get(i));
                jsonObj.put("contactId", list3.get(i));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            String USERNAME = db.namayesh4(1, "info");
            if ( !list2.get(i).equals(USERNAME)) {
                array.put(jsonObj);
            }
            try {
                Parent.put("contacts", array);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        new GetContactsFromServer().execute();
    }


    private void notifyAddChannel(String uid, String name, String description, String membersnumbers, String avatar_lq, String avatar_hq, String lastmsg, String lastdate, String membership) {

        Intent intent = new Intent("addNewChannel"); // for add channel in page massaging channel
        intent.putExtra("uid", uid);
        intent.putExtra("name", name);
        intent.putExtra("description", description);
        intent.putExtra("membersnumbers", membersnumbers);
        intent.putExtra("avatar_lq", avatar_lq);
        intent.putExtra("avatar_hq", avatar_hq);
        intent.putExtra("lastmsg", lastmsg);
        intent.putExtra("lastdate", lastdate);
        intent.putExtra("membership", membership);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

        Intent intent2 = new Intent("addNewInAll"); // for add channel in page massaging all
        intent2.putExtra("MODEL", "3");
        intent2.putExtra("UID", uid);
        intent2.putExtra("NAME", name);
        intent2.putExtra("DESC", description);
        intent2.putExtra("MEMBERS_NUMBER", membersnumbers);
        intent2.putExtra("AVATAR_LQ", avatar_lq);
        intent2.putExtra("AVATAR_HQ", avatar_hq);
        intent2.putExtra("LAST_MSG", lastmsg);
        intent2.putExtra("LAST_DATE", lastdate);
        intent2.putExtra("MEMBER_SHIP", membership);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent2);

    }


    private void notifyUpdateChannels(String uid, String name, String description, String membersnumbers, String avatar_lq, String avatar_hq, String membership) {

        Intent intent = new Intent("updateChannelItem"); // for update channel in page massaging channel
        intent.putExtra("uid", uid);
        intent.putExtra("name", name);
        intent.putExtra("description", description);
        intent.putExtra("membersnumbers", membersnumbers);
        intent.putExtra("avatar_lq", avatar_lq);
        intent.putExtra("avatar_hq", avatar_hq);
        intent.putExtra("membership", membership);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

        Intent intent2 = new Intent("updateItemInPageAll"); // for update channel in page massaging all
        intent2.putExtra("MODEL", "3");
        intent2.putExtra("UID", uid);
        intent2.putExtra("NAME", name);
        intent2.putExtra("DESC", description);
        intent2.putExtra("MEMBERS_NUMBER", membersnumbers);
        intent2.putExtra("AVATAR_LQ", avatar_lq);
        intent2.putExtra("AVATAR_HQ", avatar_hq);
        intent2.putExtra("MEMBER_SHIP", membership);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent2);

    }


    private void notifyAddgroupchat(String uid, String name, String description, String avatar_lq, String avatar_hq, String lastmsg, String lastdate, String membership) {

        Intent intent = new Intent("addNewGroup"); // for add group in page massaging group
        intent.putExtra("uid", uid);
        intent.putExtra("name", name);
        intent.putExtra("description", description);
        intent.putExtra("avatar_lq", avatar_lq);
        intent.putExtra("avatar_hq", avatar_hq);
        intent.putExtra("lastmsg", lastmsg);
        intent.putExtra("lastdate", lastdate);
        intent.putExtra("membership", membership);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

        Intent intent2 = new Intent("addNewInAll"); // for add group in page massaging all
        intent2.putExtra("MODEL", "2");
        intent2.putExtra("UID", uid);
        intent2.putExtra("NAME", name);
        intent2.putExtra("DESC", description);
        intent2.putExtra("MEMBER_SHIP", membership);
        intent2.putExtra("AVATAR_LQ", avatar_lq);
        intent2.putExtra("AVATAR_HQ", avatar_hq);
        intent2.putExtra("LAST_MSG", lastmsg);
        intent2.putExtra("LAST_DATE", lastdate);

        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent2);

    }


    private void notifyUpdateGroups(String uid, String name, String description, String avatar_lq, String avatar_hq, String membership) {

        Intent intent = new Intent("updateGroupItem"); // for update group in page massaging group
        intent.putExtra("uid", uid);
        intent.putExtra("name", name);
        intent.putExtra("description", description);
        intent.putExtra("avatar_lq", avatar_lq);
        intent.putExtra("avatar_hq", avatar_hq);
        intent.putExtra("membership", membership);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

        Intent intent2 = new Intent("updateItemInPageAll"); // for update group in page massaging all
        intent2.putExtra("MODEL", "2");
        intent2.putExtra("UID", uid);
        intent2.putExtra("NAME", name);
        intent2.putExtra("DESC", description);
        intent2.putExtra("MEMBER_SHIP", membership);
        intent2.putExtra("AVATAR_LQ", avatar_lq);
        intent2.putExtra("AVATAR_HQ", avatar_hq);

        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent2);

    }


    private void notifyActiveItemGroup(String uid, String active, String lastmsg, String lastdate) {

        Intent intent = new Intent("ActiveItemGroup"); // for update active group in page massaging group
        intent.putExtra("uid", uid);
        intent.putExtra("active", active);
        intent.putExtra("lastmsg", lastmsg);
        intent.putExtra("lastdate", lastdate);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

        Intent intent2 = new Intent("ActiveItemGroupAll"); // for update active group in page massaging all
        intent2.putExtra("MODEL", "2");
        intent2.putExtra("uid", uid);
        intent2.putExtra("active", active);
        intent2.putExtra("lastmsg", lastmsg);
        intent2.putExtra("lastdate", lastdate);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent2);

    }


    private void notifyActiveItemChannel(String uid, String active, String lastmsg, String lastdate) {

        Intent intent = new Intent("ActiveItemChannel"); // for update active channel in page massaging channel
        intent.putExtra("uid", uid);
        intent.putExtra("active", active);
        intent.putExtra("lastmsg", lastmsg);
        intent.putExtra("lastdate", lastdate);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

        Intent intent2 = new Intent("ActiveItemGroupAll"); // for update active channel in page massaging all
        intent2.putExtra("MODEL", "3");
        intent2.putExtra("uid", uid);
        intent2.putExtra("active", active);
        intent2.putExtra("lastmsg", lastmsg);
        intent2.putExtra("lastdate", lastdate);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent2);
    }


    private void notifyAddContacts() {
        Intent intent = new Intent("notifyAddContacts"); // for update list contact  in page contacts , chats
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }


    private void newPostChannelList(String uid, String lastMsg, String lastDate) {
        Intent intent = new Intent("newPostChannelList");
        intent.putExtra("uid", uid);
        intent.putExtra("lastmsg", lastMsg);
        intent.putExtra("lastdate", lastDate);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }


    private void channelNewPostSendToAll(String uid, String lastmsg, String lastdate) {
        Intent intentAll = new Intent("newPostAll");
        intentAll.putExtra("MODEL", "3"); // 3 = channel
        intentAll.putExtra("UID", uid);
        intentAll.putExtra("LAST_MSG", lastmsg);
        intentAll.putExtra("LAST_DATE", lastdate);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intentAll);
    }

}
