// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.instruments;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.iGap.adapter.G;


/**
 * 
 * database class containt all of the SQLite commands in this project
 *
 */

public class database extends SQLiteOpenHelper {

    public final String   path = "data/data/com.iGap/databases/";
    public final String   Name = "igap";
    //public SQLiteDatabase mydb;
    private final Context mycontext;


    public database(Context context) {
        super(context, "igap", null, 1);
        mycontext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase arg0) {

    }


    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

    }


    /**
     * check if database not opend , copy that to determination path
     */

    public void useable() {
        boolean checkdb = checkdb();
        if (checkdb) {

        } else {
            this.getReadableDatabase();
            try {
                copydatabase();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * open database from path
     */

    public void open() {
        try {
            G.mydb = SQLiteDatabase.openDatabase(path + Name, null, SQLiteDatabase.OPEN_READWRITE);
        }
        catch (RuntimeException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void close() {
        G.mydb.close();
    }


    /**
     * 
     * check is exist database file in path or no
     * 
     * @return
     *         true if exist othwise false
     */

    public boolean checkdb() {
        SQLiteDatabase db = null;
        try {
            db = SQLiteDatabase.openDatabase(path + Name, null, SQLiteDatabase.OPEN_READONLY);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return db != null ? true : false;
    }


    /**
     * 
     * copy database file to destination
     * 
     * @throws IOException
     */

    public void copydatabase() throws IOException {

        OutputStream myOutput = new FileOutputStream(path + Name);
        byte[] buffer = new byte[1024];
        int lenght;
        InputStream myInput = mycontext.getAssets().open(Name);
        while ((lenght = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, lenght);
        }
        myInput.close();
        myOutput.flush();
        myOutput.close();
    }


    //********************************* Insert Query *******************************************************************

    public void adduserinfo(String username, String password, String basicauth, String avatar_lq, String avatar_Hq, String name, String gender) {

        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("basicauth", basicauth);
        values.put("avatar_lq", avatar_lq);
        values.put("avatar_Hq", avatar_Hq);
        values.put("name", name);
        values.put("gender", gender);
        G.mydb.insert("info", null, values);
    }


    public void addToGroupMember(String uid, String groupchatid, String username, String name, String avatarlq, String avatarhq, String lastseen) {

        ContentValues values = new ContentValues();
        values.put("uid", uid);
        values.put("groupchatid", groupchatid);
        values.put("username", username);
        values.put("name", name);
        values.put("avatarlq", avatarlq);
        values.put("avatarhq", avatarhq);
        values.put("lastseen", lastseen);
        G.mydb.insert("groupmember", null, values);
    }


    public String addchatroom(String userchat, String lastmsg, String lasttime, String avatar) {

        ContentValues values = new ContentValues();
        values.put("userchat", userchat);
        values.put("lastmessage", lastmsg);
        values.put("lasttime", lasttime);
        values.put("userchatavatar", avatar);
        G.mydb.insert("Chatrooms", null, values);

        Cursor cursor = G.mydb.rawQuery("SELECT * FROM Chatrooms WHERE userchat = '" + userchat + "'", null);
        cursor.moveToFirst();
        String str = cursor.getString(0);
        cursor.close();

        return str;
    }


    public String addchathistory(String filemime, String chatroomid, String msg, String status, String msgtime, String msgtype, String msgid, String type, String filehash, String fileurl, String filethumbnail, String replyMessage, String replyFilehash, String replyFrom) {
        ContentValues values = new ContentValues();
        values.put("chatroom_id", chatroomid);
        values.put("msg", msg);
        values.put("status", status);
        values.put("msg_time", msgtime);
        values.put("msg_type", msgtype);
        values.put("msg_id", msgid);
        values.put("type", type);
        values.put("filehash", filehash);
        values.put("fileurl", fileurl);
        values.put("filethumbnail", filethumbnail);
        values.put("replymessage", replyMessage);
        values.put("replyfrom", replyFrom);
        values.put("replyfilehash", replyFilehash);
        values.put("filemime", filemime);
        G.mydb.insert("chathistory", null, values);

        String str;
        if (msgid != null) {
            Cursor cursor = G.mydb.rawQuery("SELECT * FROM chathistory WHERE msg_id = '" + msgid + "' AND msg_time = '" + msgtime + "'", null);
            cursor.moveToFirst();
            str = cursor.getString(0);
            cursor.close();
        } else {
            Cursor cursor = G.mydb.rawQuery("SELECT * FROM chathistory WHERE type = '" + type + "' AND msg_time = '" + msgtime + "' AND msg = '" + msg + "'", null);
            cursor.moveToFirst();
            str = cursor.getString(0);
            cursor.close();
        }

        return str;
    }


    public void setLastMessage(String uid, String model, String message) {

        try {
            ContentValues cv = new ContentValues();
            cv.put("message", message);

            int result = G.mydb.update("lastmessage", cv, " uid = ? and model = ? ", new String[]{ uid, model });

            if (result < 1) {

                cv.put("uid", uid);
                cv.put("model", model);

                G.mydb.insert("lastmessage", null, cv);

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void AddContacts(String name, String mobile, String registered, String avatar_lq, String avatar_hq, String last_seen, String uid) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("mobile", mobile);
        values.put("registered", registered);
        values.put("avatar_lq", avatar_lq);
        values.put("avatar_hq", avatar_hq);
        values.put("last_seen", last_seen);
        values.put("uid", uid);
        G.mydb.insert("Contacts", null, values);
    }


    public void Addgroupchat(String groupchatid, String groupname, String membership, String membersnumber, String groupavatar, String groupavatarhq, String lastmessage, String lastdate, String description) {

        ContentValues values = new ContentValues();
        values.put("groupchatid", groupchatid);
        values.put("groupname", groupname);
        values.put("membership", membership);
        values.put("membersnumber", membersnumber);
        values.put("groupavatar", groupavatar);
        try {
            values.put("groupavatarhq", groupavatarhq);
        }
        catch (Exception e) {}

        values.put("lastmessage", lastmessage);
        values.put("lastdate", lastdate);
        values.put("description", description);

        G.mydb.insert("groupchatrooms", null, values);
    }


    public void addgroupchathistory(String filemime, String groupchatroomid, String msgid, String msg, String status, String msgtime, String msgtype, String msgsender, String msgsenderavatar
                                    , String type, String filehash, String fileurl, String filethumbnail, String username, String replymessage, String replyfrom, String replyfilehash) {
        ContentValues values = new ContentValues();
        values.put("groupchatroom_id", groupchatroomid);
        values.put("msg_id", msgid);
        values.put("msg", msg);
        values.put("status", status);
        values.put("msg_time", msgtime);
        values.put("msg_type", msgtype);
        values.put("msg_sender", msgsender);
        values.put("msg_sender_avatar", msgsenderavatar);
        values.put("type", type);
        values.put("filehash", filehash);
        values.put("fileurl", fileurl);
        values.put("filethumbnail", filethumbnail);
        values.put("username", username);
        values.put("replymessage", replymessage);
        values.put("replyfrom", replyfrom);
        values.put("replyfilehash", replyfilehash);
        values.put("filemime", filemime);
        G.mydb.insert("groupchathistory", null, values);
    }


    public void Addfiles(String filehash, String fileurl, String filethumb, String status) {

        ContentValues values = new ContentValues();
        values.put("filehash", filehash);
        values.put("fileurl", fileurl);
        values.put("filethumb", filethumb);
        values.put("status", status);
        G.mydb.insert("files", null, values);
    }


    public void AddMessagecash(String forwardfrom, String msg, String filehash, String fileurl, String filethumbnail, String type, String filemime) {

        ContentValues values = new ContentValues();
        values.put("forwardfrom", forwardfrom);
        values.put("msg", msg);
        values.put("filehash", filehash);
        values.put("fileurl", fileurl);
        values.put("filethumbnail", filethumbnail);
        values.put("type", type);
        values.put("filemime", filemime);
        G.mydb.insert("messagingcache", null, values);
    }


    public void Addfiles1(String filehash, String fileurl, String filethumb, String status, String filepath) {

        ContentValues values = new ContentValues();
        values.put("filehash", filehash);
        values.put("fileurl", fileurl);
        values.put("filethumb", filethumb);
        values.put("status", status);
        values.put("filepath", filepath);
        G.mydb.insert("files", null, values);
    }


    public void addchannel(String uid, String name, String description, String membersnumber, String avatar_lq, String avatar_hq, String lastmessage, String lastdate, String sound, String membership) {

        ContentValues values = new ContentValues();
        values.put("uid", uid);
        values.put("name", name);
        values.put("description", description);
        values.put("membersnumber", membersnumber);
        values.put("avatar_lq", avatar_lq);
        values.put("avatar_hq", avatar_hq);
        values.put("lastmessage", lastmessage);
        values.put("lastdate", lastdate);
        values.put("sound", sound);
        values.put("membership", membership);
        G.mydb.insert("channels", null, values);
    }


    public void addchannelhistory(String channeluid, String msg_id, String msg, String msg_time, String msg_type, String filehash, String seen, String view, String sender, String status, String filemime) {
        ContentValues values = new ContentValues();

        Log.i("LOG", "channeluid : " + channeluid + "  ||  msg_id : " + msg_id + "  ||  msg : " + msg + "  ||  msg_time : " + msg_time
                + "  ||  msg_type : " + msg_type + "  ||  filehash : " + filehash + "  ||  seen : " + seen + "  ||  view : " + view
                + "  ||  sender : " + sender + "  ||  status : " + status + "  ||  filemime : " + filemime);

        values.put("channeluid", channeluid);
        values.put("msg_id", msg_id);
        values.put("msg", msg);
        values.put("msg_time", msg_time);
        values.put("msg_type", msg_type);
        values.put("filehash", filehash);
        values.put("seen", seen);
        values.put("view", view);
        values.put("sender", sender);
        values.put("status", status);
        values.put("filemime", filemime);
        G.mydb.insert("channelhistory", null, values);
        Log.i("LOG", "***database inserted***");
    }


    public void addsupporthistory(String msg_id, String msg, String msg_time, String msg_type, String status) {
        ContentValues values = new ContentValues();
        values.put("msg_id", msg_id);
        values.put("msg", msg);
        values.put("msg_time", msg_time);
        values.put("msg_type", msg_type);
        values.put("status", status);
        G.mydb.insert("support", null, values);
    }


    public void addBlockContact(String name, String mobile, String avatar_lq, String avatar_hq, String uid) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("mobile", mobile);
        values.put("avatar_lq", avatar_lq);
        values.put("avatar_hq", avatar_hq);
        values.put("uid", uid);
        G.mydb.insert("blocklist", null, values);
    }


    public void addwebsite(String url, String title, String description, String icon) {
        ContentValues values = new ContentValues();
        values.put("url", url);
        values.put("wtitle", title);
        values.put("wdescription", description);
        values.put("wicon", icon);
        G.mydb.insert("websiteinfo", null, values);
    }


    //********************************* Select Query *******************************************************************

    public Cursor selectUnreadMessage(String table, String cause) {
        Cursor cursor = G.mydb.rawQuery("SELECT msg_id , status , msg_type FROM " + table + " WHERE " + cause, null);
        return cursor;
    }


    public Cursor selectUnreadMessageChannel(String table, String cause) {
        Cursor cursor = G.mydb.rawQuery("SELECT msg_id , seen , sender FROM " + table + " WHERE " + cause, null);
        return cursor;
    }


    public int getRowCount(String table) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM " + table, null);
        int rowCount = cursor.getCount();
        cursor.close();
        return rowCount;
    }


    public Cursor getSharedMediaItem(String tableName, String uid) {

        Cursor cursor = null;

        if (tableName.equals("chathistory")) {
            cursor = G.mydb.rawQuery(" SELECT chathistory.filehash , chathistory.type , files.fileurl , files.filethumb , files.filepath , files.id FROM chathistory INNER JOIN files ON chathistory.filehash = files.filehash where chathistory.chatroom_id = ? GROUP BY chathistory.filehash  ORDER BY files.id DESC ", new String[]{ uid });
        } else if (tableName.equals("channelhistory")) {
            cursor = G.mydb.rawQuery(" SELECT channelhistory.filehash , channelhistory.msg_type , files.fileurl , files.filethumb , files.filepath , files.id  FROM channelhistory INNER JOIN files ON channelhistory.filehash=files.filehash where channelhistory.channeluid = ? GROUP BY channelhistory.filehash  ORDER BY files.id DESC ", new String[]{ uid });
        } else if (tableName.equals("groupchathistory")) {
            cursor = G.mydb.rawQuery(" SELECT groupchathistory.filehash , groupchathistory.type , files.fileurl , files.filethumb , files.filepath , files.id  FROM groupchathistory INNER JOIN files ON groupchathistory.filehash=files.filehash where groupchathistory.groupchatroom_id = ? GROUP BY groupchathistory.filehash ORDER BY files.id DESC ", new String[]{ uid });
        }

        return cursor;
    }


    public Cursor getdataFromGroupMember(String groupchatid) {
        Cursor cursor = G.mydb.rawQuery(" SELECT * FROM  groupmember where groupchatid = ? ", new String[]{ groupchatid });
        return cursor;
    }


    public String getLastSeen(String MobileNumber) {
        Cursor cursor = null;
        try {
            cursor = G.mydb.rawQuery(" SELECT last_seen FROM  Contacts where mobile = ? ", new String[]{ MobileNumber });
            cursor.moveToFirst();
            String src = cursor.getString(0);
            cursor.close();
            return src;
        }
        catch (Exception e) {
            if (cursor != null)
                cursor.close();
            return "last seen";
        }
    }


    public void setLastSeen(String MobileNumber, String last_seen) {
        ContentValues values = new ContentValues();
        values.put("last_seen", last_seen);
        G.mydb.update("Contacts", values, " mobile = ? ", new String[]{ MobileNumber });
    }


    public int getRowCountChatroom(String userchat, String table) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM " + table + " WHERE userchat = '" + userchat + "'", null);
        int rowCount = cursor.getCount();
        cursor.close();
        return rowCount;
    }


    public int getRowCountmessagecash() {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM messagingcache GROUP BY forwardfrom", null);
        int rowCount = cursor.getCount();
        cursor.close();
        return rowCount;
    }


    public int getblocklistcount() {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM blocklist", null);
        int rowCount = cursor.getCount();
        cursor.close();
        return rowCount;
    }


    public int iscontact(String mobile) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM Contacts WHERE mobile ='" + mobile + "'", null);
        int rowCount = cursor.getCount();
        cursor.close();
        return rowCount;
    }


    public int getchathistoryRowCount(String table, String chatroomid) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM " + table + " WHERE chatroom_id = '" + chatroomid + "'", null);
        int rowCount = cursor.getCount();
        cursor.close();
        return rowCount;
    }


    public int getsupporthistoryRowCount() {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM support ", null);
        int rowCount = cursor.getCount();
        cursor.close();
        return rowCount;
    }


    public int getgroupchathistoryRowCount(String table, String groupchatroomid) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM " + table + " WHERE groupchatroom_id = '" + groupchatroomid + "'", null);
        int rowCount = cursor.getCount();
        cursor.close();
        return rowCount;
    }


    public String chathistory(int field, int row, String table, String chatroomid) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM " + table + " WHERE chatroom_id = '" + chatroomid + "'", null);
        cursor.moveToPosition(row);
        String str = cursor.getString(field);
        cursor.close();
        return str;
    }


    public String groupchathistory(int field, int row, String table, String groupchatroomid) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM " + table + " WHERE groupchatroom_id = '" + groupchatroomid + "'", null);
        cursor.moveToPosition(row);
        String str = cursor.getString(field);
        cursor.close();
        return str;
    }


    public String getGroupChatInfo(int field, String groupchatid) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM groupchatrooms WHERE groupchatid = '" + groupchatid + "'", null);
        cursor.moveToFirst();
        String str = cursor.getString(field);
        cursor.close();
        return str;
    }


    public String groupchathistory1(String groupchatroomid) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM groupchathistory WHERE groupchatroom_id = '" + groupchatroomid + "'", null);
        cursor.moveToLast();
        String str = cursor.getString(5);
        cursor.close();
        return str;
    }


    public String getmessagcash(int row, int field) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM messagingcache", null);
        cursor.moveToPosition(row);
        String str = cursor.getString(field);
        cursor.close();
        return str;
    }


    public String getmessagcashfrom(int row, int field) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM messagingcache GROUP BY forwardfrom", null);
        cursor.moveToPosition(row);
        String str = cursor.getString(field);
        cursor.close();
        return str;
    }


    public String namayeshchatroomid(String userchat) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM Chatrooms WHERE userchat = '" + userchat + "'", null);
        cursor.moveToFirst();
        String str = cursor.getString(0);
        cursor.close();
        return str;
    }


    public int getRowCount3(String table, String msgid) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM " + table + " WHERE msg_id = '" + msgid + "'", null);
        int rowCount = cursor.getCount();
        cursor.close();
        return rowCount;
    }


    public int getRowCountunreadchat(String roomid) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM chathistory WHERE chatroom_id = '" + roomid + "' And status = 4 ", null);
        int rowCount = cursor.getCount();
        cursor.close();
        return rowCount;
    }


    public int getRowCountunreadgroupchat(String groupchatroomid) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM groupchathistory WHERE groupchatroom_id = '" + groupchatroomid + "' And status = 4 ", null);
        int rowCount = cursor.getCount();
        cursor.close();
        return rowCount;
    }


    public String getCountryInfo(int field, String table, String isocode) {
        Cursor cursor = null;
        try {
            cursor = G.mydb.rawQuery("SELECT * FROM " + table + " WHERE iscode='" + isocode + "'", null);
            cursor.moveToFirst();
            String str = cursor.getString(field);
            cursor.close();
            return str;
        }
        catch (Exception e) {
            String str = "0";
            if (cursor != null) {
                cursor.close();
            }
            return str;
        }
    }


    public String getCountryInfo1(int field, String table, String id) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM " + table + " WHERE id = '" + id + "'", null);
        cursor.moveToFirst();
        String str = cursor.getString(field);
        cursor.close();
        return str;
    }


    public String getLastMessage(String uid, String model) {
        Cursor cursor = null;
        try {
            cursor = G.mydb.rawQuery(" SELECT message FROM lastmessage WHERE uid = ? and model = ? ", new String[]{ uid, model });

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                cursor.close();
                return cursor.getString(0);
            } else {
                cursor.close();
                return "";
            }
        }
        catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
            return "";
        }
    }


    public Cursor getallContacts(String registered) {
        try {
            Cursor cursor = G.mydb.rawQuery(" SELECT * FROM Contacts WHERE registered = ? ", new String[]{ registered });
            return cursor;
        }
        catch (Exception e) {
            return null;
        }
    }


    public String getBlockContact(int field, int row) {
        Cursor cursor = null;
        try {
            cursor = G.mydb.rawQuery("SELECT * FROM blocklist", null);
            cursor.moveToPosition(row);
            String str = cursor.getString(field);
            cursor.close();
            return str;
        }
        catch (Exception e) {
            String str = "0";
            if (cursor != null)
                cursor.close();
            return str;
        }
    }


    public void getNewAvatarChatRooms() {

        Cursor cursor = G.mydb.rawQuery(" SELECT userchat FROM Chatrooms ", null);

        while (cursor.moveToNext()) {

            try {
                String userchat = cursor.getString(0);
                String mobile = userchat.substring(0, userchat.indexOf("@igap.im"));
                Cursor cursor1 = G.mydb.rawQuery(" SELECT mobile , avatar_lq FROM  Contacts where mobile = ? ", new String[]{ mobile });

                if (cursor1.getCount() > 0) {
                    cursor1.moveToNext();
                    ContentValues values = new ContentValues();
                    values.put("userchatavatar", cursor1.getString(1));
                    G.mydb.update("Chatrooms", values, " userchat = ? ", new String[]{ userchat });

                    Intent intent = new Intent("updateAvatarAll"); // update avatar in page messaging all
                    intent.putExtra("MODEL", "1");
                    intent.putExtra("AVATAR", cursor1.getString(1));
                    intent.putExtra("UID", userchat + "");
                    LocalBroadcastManager.getInstance(mycontext).sendBroadcast(intent);

                    Intent intent2 = new Intent("updateAvatarChat"); // update avatar in page messaging chat
                    intent2.putExtra("avatar", cursor1.getString(1));
                    intent2.putExtra("userchat", userchat + "");
                    LocalBroadcastManager.getInstance(mycontext).sendBroadcast(intent2);

                }
                cursor1.close();
            }
            catch (Exception e) {}

        }
        cursor.close();
    }


    public String getContactsavatar(int field, String registered, String mobile) {
        Cursor cursor = null;
        try {
            cursor = G.mydb.rawQuery("SELECT * FROM Contacts WHERE registered = '" + registered + "' AND mobile = '" + mobile + "'", null);
            cursor.moveToFirst();
            String str = cursor.getString(field);
            cursor.close();
            return str;
        }
        catch (Exception e) {
            String str = "0";
            if (cursor != null)
                cursor.close();
            return str;
        }
    }


    public int isgroupmessageexist(String table, String groupchatid) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM " + table + " WHERE groupchatid = '" + groupchatid + "'", null);
        int rowCount = cursor.getCount();
        cursor.close();
        return rowCount;
    }


    public int isgroupmessagehistoryexist(String msgid) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM groupchathistory WHERE msg_id = '" + msgid + "'", null);
        int rowCount = cursor.getCount();
        cursor.close();
        return rowCount;
    }


    public int isgmexist(String table, String msgid) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM " + table + " WHERE msg_id = '" + msgid + "'", null);
        int rowCount = cursor.getCount();
        cursor.close();
        return rowCount;
    }


    public int isChannelExist(String table, String uid) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM " + table + " WHERE uid = '" + uid + "'", null);
        int rowCount = cursor.getCount();
        cursor.close();
        return rowCount;
    }


    public int isGroupExist(String table, String groupchatid) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM " + table + " WHERE groupchatid = '" + groupchatid + "'", null);
        int rowCount = cursor.getCount();
        cursor.close();
        return rowCount;
    }


    public int isSingleChatExist(String id) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM Chatrooms WHERE id = '" + id + "'", null);
        int rowCount = cursor.getCount();
        cursor.close();
        return rowCount;
    }


    public ArrayList<String> namayeshCountries(String SelectedCountries) {

        ArrayList<String> result = null;

        String query = "select * from countries where country_name like ? ";
        Cursor cursor = G.mydb.rawQuery(query, new String[]{ SelectedCountries + "%" });

        if (cursor.getCount() > 0) {
            result = new ArrayList<String>();
            while (cursor.moveToNext())
                result.add(cursor.getInt(0) + ";;" + cursor.getString(3) + "\n");
        }
        cursor.close();
        return result;
    }


    public String namayeshname(int field, String mobile) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM Contacts WHERE mobile ='" + mobile + "'", null);
        cursor.moveToFirst();
        String str = cursor.getString(field);
        cursor.close();
        return str;
    }


    public String getfile(int field, String filehash) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM files WHERE filehash ='" + filehash + "'", null);
        cursor.moveToFirst();
        String str = cursor.getString(field);
        cursor.close();
        return str;
    }


    public String getFileHashStatus(int field, String filehash) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM files WHERE filehash ='" + filehash + "'", null);
        cursor.moveToFirst();
        String str;
        try {
            str = cursor.getString(field);
        }
        catch (CursorIndexOutOfBoundsException e) {
            str = "1000";
            Log.i("LOG", "ERROR");
            e.printStackTrace();
        }
        cursor.close();
        return str;
    }


    public int isfileinfoexist(String filehash) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM files WHERE filehash = '" + filehash + "'", null);
        int rowCount = cursor.getCount();
        cursor.close();
        return rowCount;
    }


    public String getstatus(String msgid) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM chathistory WHERE msg_id='" + msgid + "'", null);
        cursor.moveToFirst();
        String str = cursor.getString(4);
        cursor.close();
        return str;
    }


    public String getchannelinfo(int field, String uid) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM channels WHERE uid='" + uid + "'", null);
        cursor.moveToFirst();
        String str = cursor.getString(field);
        cursor.close();
        return str;
    }


    public String getallchannels(int field, int row) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM channels", null);
        cursor.moveToPosition(row);
        String str = cursor.getString(field);
        cursor.close();
        return str;
    }


    public int getchannelunreadsize(String channeluid) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM channelhistory WHERE channeluid ='" + channeluid + "' AND seen = 0", null);
        int rowCount = cursor.getCount();
        cursor.close();
        return rowCount;
    }


    public String getchannelhistory(int field, int row, String channeluid) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM channelhistory WHERE channeluid='" + channeluid + "'", null);
        cursor.moveToPosition(row);
        String str = cursor.getString(field);
        cursor.close();
        return str;
    }


    public int getchannelhistorysize(String channeluid) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM channelhistory WHERE channeluid='" + channeluid + "'", null);
        int rowCount = cursor.getCount();
        cursor.close();
        return rowCount;
    }


    public Cursor getTableRow(String tableName, String columnToOrder) {
        Cursor cursor = G.mydb.rawQuery("select * from " + tableName + " order by " + columnToOrder + " desc ", null);
        return cursor;
    }


    public String selectLastGroupChatHistoryID() {
        Cursor cursor = G.mydb.rawQuery("SELECT id FROM groupchathistory ORDER BY id desc LIMIT 1", null);
        cursor.moveToFirst();
        String string = cursor.getString(0);
        cursor.close();
        return string;
    }


    public String getCountry() {
        Cursor cursor = G.mydb.rawQuery("SELECT country FROM setting", null);
        cursor.moveToFirst();
        String country = cursor.getString(0);
        if (country == null) {
            country = "Iran";
        }
        cursor.close();
        return country;
    }


    public String selectLastChannelMsgWithTime(String channelUid) {
        Cursor cursor = G.mydb.rawQuery("SELECT msg_id FROM channelhistory WHERE channeluid = '" + channelUid + "' ORDER BY msg_time DESC", null);

        String msgID = "";
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                msgID = cursor.getString(0);

                if (msgID != null)
                    if (msgID.length() > 0)
                        break;
            }
        }

        return msgID;
    }


    public String selectMembership(String gchid) {
        Cursor cursor = G.mydb.rawQuery("SELECT membership FROM groupchatrooms WHERE groupchatid = '" + gchid + "'", null);
        cursor.moveToFirst();
        String string = null;
        try {
            string = cursor.getString(0);
        }
        catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        cursor.close();
        return string;
    }


    public String select(String table, String cause, int field) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM " + table + " WHERE " + cause, null);
        cursor.moveToFirst();
        String string = cursor.getString(field);
        cursor.close();
        return string;
    }


    public String select(String table, int field) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM " + table, null);
        cursor.moveToFirst();
        String string = cursor.getString(field);
        cursor.close();
        return string;
    }


    public int selectOffsetOfID(String table, String cause, String firstID) {

        String id = firstID;

        int offset = 0;
        boolean readOffset = true;

        Cursor cursor = G.mydb.rawQuery("SELECT id FROM " + table + " WHERE " + cause + " ORDER BY id DESC", null);
        while (cursor.moveToNext()) {
            String chatid = cursor.getString(cursor.getColumnIndex("id"));
            if (id.equals(chatid) && readOffset) {
                readOffset = false;
                offset = cursor.getPosition();
                return offset;
            }
        }
        cursor.close();
        return offset;
    }


    public int selectOffsetOfIDFromTop(String table, String cause, String firstID) {

        String id = firstID;

        int offset = -1;
        boolean readOffset = true;

        Cursor cursor = G.mydb.rawQuery("SELECT id FROM " + table + " WHERE " + cause, null);
        while (cursor.moveToNext()) {
            String chatid = cursor.getString(cursor.getColumnIndex("id"));

            if (id.equals(chatid) && readOffset) {
                readOffset = false;
                offset = cursor.getPosition();
                return offset;
            }
        }
        cursor.close();
        return offset;
    }


    public int selectOffsetOfIDFromTopOrderByTime(String table, String cause, String firstID) {

        String id = firstID;

        int offset = -1;
        boolean readOffset = true;

        Cursor cursor = G.mydb.rawQuery("SELECT id FROM " + table + " WHERE " + cause + " ORDER BY msg_time", null);
        while (cursor.moveToNext()) {
            String chatid = cursor.getString(cursor.getColumnIndex("id"));

            if (id.equals(chatid) && readOffset) {
                readOffset = false;
                offset = cursor.getPosition();
                return offset;
            }
        }
        cursor.close();
        return offset;
    }


    public String selectHashtakPositionToBottom(String table, String cause, String idOffset, String chatroomID, String hashtak) {

        String hashtakID = "-1";
        Cursor cursor = G.mydb.rawQuery("SELECT msg , id FROM " + table + " WHERE " + cause + " LIMIT -1 OFFSET " + idOffset, null);
        while (cursor.moveToNext()) {

            String message = cursor.getString(0);
            if (message.toLowerCase().indexOf(hashtak.toLowerCase()) != -1) {
                String id = cursor.getString(1);
                hashtakID = id;
                break;
            }

        }
        cursor.close();
        return hashtakID;
    }


    public int selectHashtakPosition(String table, String cause, String idOffset, String chatroomID, String hashtak) {

        int offset = 0;
        String hashtakID = "";
        Cursor cursor = G.mydb.rawQuery("SELECT msg , id FROM " + table + " WHERE " + cause + " ORDER BY id DESC LIMIT -1 OFFSET " + idOffset, null);
        while (cursor.moveToNext()) {

            String message = cursor.getString(0);
            if (message.toLowerCase().indexOf(hashtak.toLowerCase()) != -1) {
                String id = cursor.getString(1);
                hashtakID = id;
                break;
            }

        }
        cursor.close();
        offset = selectOffsetOfIDFromTop(table, cause, hashtakID);
        return offset;
    }


    public String selectHashtakPosition1(String table, String cause, String idOffset, String chatroomID, String hashtak) {

        String hashtakID = "";
        Cursor cursor = G.mydb.rawQuery("SELECT msg , id FROM " + table + " WHERE " + cause + " ORDER BY id DESC LIMIT -1 OFFSET " + idOffset, null);
        while (cursor.moveToNext()) {

            String message = cursor.getString(0);
            if (message.toLowerCase().indexOf(hashtak.toLowerCase()) != -1) {
                String id = cursor.getString(1);
                hashtakID = id;
                break;
            }

        }
        cursor.close();
        return hashtakID;
    }


    public Cursor selectLimitOffset(String table, String cause, String limit, String offset) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM " + table + " WHERE " + cause + " LIMIT " + limit + " OFFSET " + offset, null);
        return cursor;
    }


    public Cursor selectLimitOffsetOrderByTime(String table, String cause, String limit, String offset) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM " + table + " WHERE " + cause + " ORDER BY msg_time LIMIT " + limit + " OFFSET " + offset, null);
        return cursor;
    }


    public String selectChannelRole(String uid) {
        Cursor cursor = G.mydb.rawQuery("SELECT membership FROM channels WHERE uid = '" + uid + "'", null);
        String role = "";
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            role = cursor.getString(0);
        }
        cursor.close();
        return role;
    }


    public String selectGroupMessageStatus(String msgID) {
        Cursor cursor = G.mydb.rawQuery("SELECT status FROM groupchathistory WHERE msg_id = '" + msgID + "'", null);
        String status = "4";
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            status = cursor.getString(0);
        }
        cursor.close();
        return status;
    }


    public String selectLastUserAvatar(String msgSender) {
        String query = "SELECT msg_sender_avatar FROM groupchathistory WHERE msg_sender ='" + msgSender + "'  ORDER BY id DESC LIMIT 1";
        Cursor cursor = G.mydb.rawQuery(query, null);
        String senderAvatar = "";
        if (cursor != null && cursor.moveToFirst()) {
            senderAvatar = cursor.getString(0); //The 0 is the column index, we only have 1 column, so the index is 0
        }
        cursor.close();
        return senderAvatar;
    }


    public String selectGroupChatLastTime(String groupchatroom_id) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM latest WHERE groupchatroom_id = '" + groupchatroom_id + "'", null);
        String lastTime;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            lastTime = cursor.getString(2);
        } else {
            lastTime = "empty";
        }
        cursor.close();
        return lastTime;
    }


    public int selectUnreadCountSingleChat(String userchat) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM chathistory WHERE status = 4 AND chatroom_id = '" + userchat + "'", null);
        int rowCount = cursor.getCount();
        cursor.close();
        return rowCount;
    }


    public int selectUnreadCountGroup(String groupchatroom_id) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM groupchathistory WHERE status = 4 AND groupchatroom_id = '" + groupchatroom_id + "'", null);
        int rowCount = cursor.getCount();
        cursor.close();
        return rowCount;
    }


    public String selectNotificationState() {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM setting", null);
        cursor.moveToFirst();
        String string = cursor.getString(2);
        cursor.close();
        return string;
    }


    public String selectChannelLastMessageID(String channelUid) {
        String query = "SELECT id from channelhistory WHERE channeluid ='" + channelUid + "'  ORDER BY id DESC limit 1";
        Cursor cursor = G.mydb.rawQuery(query, null);
        int lastId = 0;
        if (cursor != null && cursor.moveToFirst()) {
            lastId = cursor.getInt(0); //The 0 is the column index, we only have 1 column, so the index is 0
        }
        cursor.close();
        return lastId + "";
    }


    public Cursor selectUnreadChannelMessage() {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM channelhistory WHERE seen = 0", null);
        return cursor;
    }


    public String selectField(String table, String clause, int field) {
        String str;
        Cursor cursor = null;
        try {
            cursor = G.mydb.rawQuery("SELECT * FROM " + table + " WHERE " + clause, null);
            cursor.moveToFirst();
            str = cursor.getString(field);
            cursor.close();
        }
        catch (Exception e) {
            str = "0";
            if (cursor != null)
                cursor.close();
        }
        return str;
    }


    public int isChannelMessageExist(String msg_id) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM channelhistory WHERE msg_id = '" + msg_id + "'", null);
        int rowCount = cursor.getCount();
        cursor.close();
        return rowCount;
    }


    public String selectActiveChannel(int field, String uid) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM channels WHERE uid = '" + uid + "'", null);
        cursor.moveToFirst();
        String string = cursor.getString(field);
        cursor.close();
        return string;
    }


    public Cursor selectAllChannelID() {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM channels", null);
        return cursor;
    }


    public String selectActive(int field, String groupchatid) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM groupchatrooms WHERE groupchatid = '" + groupchatid + "'", null);
        cursor.moveToFirst();
        String string = cursor.getString(field);
        cursor.close();
        return string;
    }


    public Cursor selectAllGroupID() {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM groupchatrooms", null);
        return cursor;
    }


    public String selectNumberOfMembers(String groupchatid) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM groupchatrooms WHERE groupchatid = '" + groupchatid + "'", null);

        if (cursor.getCount() < 1)
            return "1";

        cursor.moveToFirst();
        String str = cursor.getString(4);
        cursor.close();
        return str;
    }


    public Cursor selectPageMessagingByTime(String table, String ColumnName) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM " + table + " ORDER BY " + ColumnName + " DESC ", null);
        return cursor;
    }


    public int isUserBlock(String mobile) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM blocklist WHERE mobile = '" + mobile + "'", null);
        int rowCount = cursor.getCount();
        cursor.close();
        return rowCount;
    }


    public int isMobileExist(String mobile) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM Contacts WHERE mobile = '" + mobile + "'", null);
        int rowCount = cursor.getCount();
        cursor.close();
        return rowCount;
    }


    public int issupportexist(String msgid) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM support WHERE msg_id = '" + msgid + "'", null);
        int rowCount = cursor.getCount();
        cursor.close();
        return rowCount;
    }


    public Cursor selectGroupChatRooms() {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM groupchatrooms GROUP BY groupchatid", null);
        return cursor;
    }


    public Cursor selectMessagingCache() {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM messagingcache", null);
        return cursor;
    }


    public int selectUnreadCountSingleChat() {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM chathistory WHERE status = 4 ", null);
        int count = 0;

        while (cursor.moveToNext()) {
            String chatroomID = cursor.getString(cursor.getColumnIndex("chatroom_id"));
            if (isSingleChatExist(chatroomID) != 0) {
                count++;
            }
        }

        cursor.close();
        return count;
    }


    public int selectUnreadCountGroupChat() {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM groupchathistory WHERE status = 4 ", null);
        int count = 0;

        while (cursor.moveToNext()) {
            String groupID = cursor.getString(cursor.getColumnIndex("groupchatroom_id"));
            if (isGroupExist("groupchatrooms", groupID) != 0) {
                count++;
            }
        }

        cursor.close();
        return count;
    }


    public int selectUnreadCountChannel() {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM channelhistory WHERE seen = 0 ", null);
        int count = 0;

        while (cursor.moveToNext()) {
            String channeluid = cursor.getString(cursor.getColumnIndex("channeluid"));
            if (isChannelExist("channels", channeluid) != 0) {
                count++;
            }
        }

        cursor.close();
        return count;
    }


    public Cursor selectUnreadMessage(String table) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM " + table + " WHERE status = 4", null);
        return cursor;
    }


    public String selectUserChannelState(String uid) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM channels WHERE uid = '" + uid + "'", null);
        int rowCount = cursor.getCount();
        String str = null;
        if (rowCount != 0) {
            cursor.moveToFirst();
            str = cursor.getString(10);
        }
        cursor.close();
        return str;
    }


    public Cursor selectHistoryByTime(String table, String chatID, String limit) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM (SELECT * FROM " + table + " WHERE " + chatID + " ORDER BY msg_time DESC LIMIT " + limit + ") WHERE " + chatID + " ORDER BY msg_time ASC", null);
        return cursor;
    }


    public Cursor selectHistoryByID(String table, String chatID) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM " + table + " WHERE " + chatID + " ORDER BY id ASC ", null);
        return cursor;
    }


    public Cursor selectHistoryByMessageID(String table, String chatID) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM " + table + " WHERE " + chatID + " ORDER BY id ASC ", null);
        return cursor;
    }


    public Cursor selectHistoryByMessageID(String table, String chatID, int offset) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM (SELECT * FROM " + table + " WHERE " + chatID + " ORDER BY id DESC LIMIT " + offset + ") WHERE " + chatID + " ORDER BY id ASC", null);
        return cursor;
    }


    public int selectHistorySize(String table, String chatID) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM " + table + " WHERE " + chatID, null);
        cursor.moveToFirst();
        int count = cursor.getCount();
        cursor.close();
        return count;
    }


    public Cursor selectSupportHistory() {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM support ORDER BY id ASC ", null);
        return cursor;
    }


    public Cursor selectfaultyDownloadList() {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM files WHERE status = 1", null);
        return cursor;
    }


    public String selectFileType(int field, String table, String filehash) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM " + table + " WHERE filehash = '" + filehash + "'", null);
        cursor.moveToFirst();
        String str = cursor.getString(field);
        cursor.close();
        return str;
    }


    public String selectMessage(int field, String table, String filehash) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM " + table + " WHERE filehash = '" + filehash + "'", null);
        cursor.moveToFirst();
        String str = cursor.getString(field);
        cursor.close();
        return str;
    }


    public String selectType(String fileHash) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM files WHERE filehash = '" + fileHash + "'", null);
        cursor.moveToFirst();
        String str = cursor.getString(8);
        cursor.close();
        return str;
    }


    public String selectfilestatus(String fileHash) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM files WHERE filehash = '" + fileHash + "'", null);
        cursor.moveToFirst();
        String str = cursor.getString(4);

        cursor.close();
        return str;
    }


    public String selectPercent(String fileHash) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM files WHERE filehash = '" + fileHash + "'", null);
        cursor.moveToFirst();

        String str = "1000"; // for detect upload percent is exist or not define a default param.
        if (cursor.getCount() != 0) {
            str = cursor.getString(7);
        }
        cursor.close();
        return str;
    }


    public String namayesh4(int field, String table) {
        Cursor cursor = null;
        try {
            cursor = G.mydb.rawQuery("SELECT * FROM " + table, null);
            cursor.moveToFirst();
            String str = cursor.getString(field);
            cursor.close();
            return str;
        }
        catch (Exception e) {
            String str = "0";
            if (cursor != null)
                cursor.close();
            return str;
        }

    }


    public String namayesh(int field, int row, String table) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM " + table, null);
        cursor.moveToPosition(row);
        String str = cursor.getString(field);
        cursor.close();
        return str;
    }


    public int getRowCount(String table, String filehash, String groupChatRoomID) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM " + table + " WHERE filehash='" + filehash + "' AND groupchatroom_id = '" + groupChatRoomID + "'", null);
        int rowCount = cursor.getCount();
        cursor.close();
        return rowCount;
    }


    public String selectMessaegId(String groupchatroom_id, String filehash) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM groupchathistory WHERE groupchatroom_id = '" + groupchatroom_id + "' AND filehash = '" + filehash + "'", null);
        String str = null;
        while (cursor.moveToNext()) {
            str = cursor.getString(cursor.getColumnIndex("msg_id"));
        }
        cursor.close();
        return str;
    }


    public int getCompletedDownload() {

        Cursor cursor = G.mydb.rawQuery("SELECT downloadcompleted FROM setting", null);
        cursor.moveToFirst();
        int i = cursor.getInt(0);
        cursor.close();
        return i;
    }


    public String getchatroominfo(String userchat, int field) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM Chatrooms WHERE userchat = '" + userchat + "'", null);
        cursor.moveToFirst();
        String str = cursor.getString(field);
        cursor.close();
        return str;
    }


    public String getgroupchatroomssound(String groupchatid) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM groupchatrooms WHERE groupchatid = '" + groupchatid + "'", null);
        String str;
        if (cursor.getCount() == 0) {
            str = "0";
        } else {
            cursor.moveToFirst();
            str = cursor.getString(9);
            cursor.close();
        }

        return str;
    }


    public String getchannelssound(String uid) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM channels WHERE uid = '" + uid + "'", null);
        cursor.moveToFirst();
        String str = cursor.getString(9);
        cursor.close();
        return str;
    }


    public int iswebsiteexist(String url) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM websiteinfo WHERE url = '" + url + "'", null);
        int size = cursor.getCount();
        return size;
    }


    public int supportunreadcount() {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM support WHERE status = 4", null);
        int size = cursor.getCount();
        return size;
    }


    public String getwebsiteinfo(String url, int field) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM websiteinfo WHERE url = '" + url + "'", null);
        cursor.moveToFirst();
        String str = cursor.getString(field);
        cursor.close();
        return str;
    }


    //********************************* Delete Query *******************************************************************

    public void deleteChannelMessageDuplicates() {
        G.mydb.execSQL("DELETE FROM channelhistory WHERE id NOT IN (SELECT MIN(id) FROM channelhistory GROUP BY msg_id)");
    }


    public void resetTables(String table) {
        G.mydb.delete(table, null, null);
    }


    public void deleteRowFromGroupMember(String groupchatid) {
        G.mydb.delete("groupmember", " groupchatid = '" + groupchatid + "' ", null);
    }


    public void deleteUseLessContact(String[] mobile) {

        Cursor cursor = G.mydb.rawQuery(" SELECT mobile FROM  Contacts ", null);

        try {
            int count = cursor.getCount();
            if (count > 0) {
                while (cursor.moveToNext()) {
                    String s = cursor.getString(0);
                    Boolean delete = true;
                    for (int j = 0; j < mobile.length; j++) {
                        if (s.equals(mobile[j])) {
                            delete = false;
                        }
                    }

                    if (delete) {
                        G.mydb.delete("Contacts", " mobile = ? ", new String[]{ s });
                    }

                }
            }
            cursor.close();
        }
        catch (Exception e) {
            if (cursor != null)
                cursor.close();
        }
    }


    public void cleargroupchathistory(String groupchatroomid) {

        //=========get last message in group 
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM groupchathistory WHERE groupchatroom_id = '" + groupchatroomid + "' ORDER BY id DESC LIMIT 1", null);

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            String msg_time = cursor.getString(5);

            Cursor cursorCount = G.mydb.rawQuery("SELECT * FROM latest WHERE groupchatroom_id = '" + groupchatroomid + "'", null);

            if (cursorCount.getCount() == 0) { //=========insert last message to lates table
                ContentValues values = new ContentValues();
                values.put("groupchatroom_id", groupchatroomid);
                values.put("last_time", msg_time);
                G.mydb.insert("latest", null, values);
            } else { //=========update last message to lates table
                ContentValues cv = new ContentValues();
                cv.put("last_time", msg_time);
                G.mydb.update("latest", cv, " groupchatroom_id = '" + groupchatroomid + "'", null);
            }
            cursorCount.close();
            //=========clear groupchathistory
            G.mydb.delete("groupchathistory", " groupchatroom_id = '" + groupchatroomid + "'", null);
        }
        cursor.close();
    }


    public void deletemessagesinglechat(String msgid) {
        G.mydb.delete("chathistory", " msg_id = '" + msgid + "'", null);
    }


    public void deletemessagegroupchat(String msgid) {
        G.mydb.delete("groupchathistory", " msg_id = '" + msgid + "'", null);
    }


    public void deletegroupchatrooms(String groupchatid) {
        G.mydb.delete("groupchatrooms", " groupchatid = '" + groupchatid + "'", null);
    }


    public void deletechatrooms(String chatroomid) {
        G.mydb.delete("Chatrooms", " id = '" + chatroomid + "'", null);
    }


    public void clearmessagecash() {
        G.mydb.delete("messagingcache", null, null);
    }


    public void clearchathistory(String chatroomid) {
        G.mydb.delete("chathistory", " chatroom_id = '" + chatroomid + "'", null);
    }


    public void deletechannel(String uid) {
        G.mydb.delete("channels", " uid = '" + uid + "'", null);
    }


    public void deletechannelhistory(String channeluid) {
        G.mydb.delete("channelhistory", " id = '" + channeluid + "'", null);
    }


    public void deleteDuplicates() {
        G.mydb.execSQL("delete from Contacts where id not in (SELECT MIN(id) FROM Contacts GROUP BY mobile)");
    }


    public void deleteFileMessageFromGroupHistory(String filehash, String groupchatroomID) {
        G.mydb.delete("groupchathistory", "groupchatroom_id = '" + groupchatroomID + "' AND filehash = '" + filehash + "' AND status = 0 ", null);
    }


    public void deleteChannelMessage(String id) {
        G.mydb.delete("channelhistory", " id = '" + id + "'", null);
    }


    public void deleteFileMessageFromChatHistory(String filehash, String chatroomID) {
        G.mydb.delete("chathistory", "chatroom_id = '" + chatroomID + "' AND filehash = '" + filehash + "' AND status = 0 ", null);
    }


    public void deleteAllInfo(String tableName) {
        try {
            File databaseFile = new File(path + Name);
            SQLiteDatabase.deleteDatabase(databaseFile);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void unblockUsers(String mobile) {
        G.mydb.delete("blocklist", " mobile = '" + mobile + "'", null);
    }


    public void clearChannelHistory(String channeluid) {
        G.mydb.delete("channelhistory", "ROWID NOT IN (SELECT ROWID FROM channelhistory WHERE channeluid = '" + channeluid + "' ORDER BY id DESC LIMIT 20)", null);
    }


    public void deleteChannel(String uid) {
        G.mydb.delete("channels", " uid = '" + uid + "'", null);
    }


    //********************************* Update Query *******************************************************************

    public void updatelang(int lang) {
        ContentValues values = new ContentValues();
        values.put("lang", lang);
        G.mydb.update("setting", values, null, null);
    }


    public String updatechatrooms(String userchat, String lastmsg, String lasttime) {
        ContentValues cv = new ContentValues();
        cv.put("lastmessage", lastmsg);
        cv.put("lasttime", lasttime);

        G.mydb.update("Chatrooms", cv, " userchat = '" + userchat + "' ", null);

        Cursor cursor = G.mydb.rawQuery("SELECT * FROM Chatrooms WHERE userchat = '" + userchat + "'", null);
        cursor.moveToFirst();
        String str = cursor.getString(0);
        cursor.close();

        return str;
    }


    public void updatechatrooms2(String userchat, String lastmsg, String lasttime) {
        ContentValues cv = new ContentValues();
        cv.put("lastmessage", lastmsg);
        cv.put("lasttime", lasttime);
        G.mydb.update("Chatrooms", cv, " userchat = '" + userchat + "' ", null);
    }


    public void updateChannels(String uid, String lastmsg, String lasttime) {
        ContentValues cv = new ContentValues();
        cv.put("lastmessage", lastmsg);
        cv.put("lastdate", lasttime);
        G.mydb.update("channels", cv, " uid = '" + uid + "' ", null);
    }


    public void updatemsgstatus(String msgid, String status) {
        ContentValues cv = new ContentValues();
        cv.put("status", status);
        G.mydb.update("chathistory", cv, "msg_id = '" + msgid + "' ", null);
    }


    public void updatemsgstatusSupport(String msgid, String status) {
        ContentValues cv = new ContentValues();
        cv.put("status", status);
        G.mydb.update("support", cv, "msg_id = '" + msgid + "' ", null);
    }


    public void updategroupmsgstatus(String msgid, String status) {
        ContentValues cv = new ContentValues();
        cv.put("status", status);
        G.mydb.update("groupchathistory", cv, "msg_id = '" + msgid + "' ", null);
    }


    public void updatesound(String groupchatid, String sound) {
        ContentValues cv = new ContentValues();
        cv.put("sound", sound);
        G.mydb.update("groupchatrooms", cv, "groupchatid = '" + groupchatid + "' ", null);
    }


    public void updategroupavatar(String groupchatid, String avatarLq, String avatarHq) {
        ContentValues cv = new ContentValues();
        cv.put("groupavatar", avatarLq);
        cv.put("groupavatarhq", avatarHq);
        G.mydb.update("groupchatrooms", cv, "groupchatid = '" + groupchatid + "' ", null);
    }


    public void updateuseravatar(String avatarlq, String avatarhq) {
        ContentValues cv = new ContentValues();
        cv.put("avatar_lq", avatarlq);
        cv.put("avatar_hq", avatarhq);
        G.mydb.update("info", cv, null, null);
    }


    public void updatechannelavatar(String uid, String avatarlq, String avatarhq) {
        ContentValues cv = new ContentValues();
        cv.put("avatar_lq", avatarlq);
        cv.put("avatar_hq", avatarhq);
        G.mydb.update("channels", cv, "uid = '" + uid + "' ", null);
    }


    public void updateContacts(String name, String mobile, String registered, String avatar_lq, String avatar_hq, String last_seen, String uid) {

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("mobile", mobile);
        values.put("registered", registered);
        values.put("avatar_lq", avatar_lq);
        values.put("avatar_hq", avatar_hq);
        values.put("last_seen", last_seen);
        values.put("uid", uid);

        Cursor cursor = G.mydb.rawQuery(" SELECT mobile FROM  Contacts where mobile = ? ", new String[]{ mobile });

        if (cursor.getCount() < 1) {
            G.mydb.insert("Contacts", null, values);
        } else {
            G.mydb.update("Contacts", values, " mobile = ? ", new String[]{ mobile });
        }
        cursor.close();
    }


    public void updategroupchatrooms(String groupchatroomid, String lastmsg, String lasttime) {
        ContentValues cv = new ContentValues();
        cv.put("lastmessage", lastmsg);
        cv.put("lastdate", lasttime);
        G.mydb.update("groupchatrooms", cv, " groupchatid = '" + groupchatroomid + "' ", null);
    }


    public void updateGroupMessageID(String messageID, String filehash) {
        ContentValues cv = new ContentValues();
        cv.put("msg_id", messageID);
        G.mydb.update("groupchathistory", cv, " filehash = '" + filehash + "' ", null);
    }


    public void updatechatsound(String id, String sound) {
        ContentValues cv = new ContentValues();
        cv.put("sound", sound);
        G.mydb.update("Chatrooms", cv, " id = '" + id + "' ", null);
    }


    public void updatechannelsound(String uid, String sound) {
        ContentValues cv = new ContentValues();
        cv.put("sound", sound);
        G.mydb.update("channels", cv, " uid = '" + uid + "' ", null);
    }


    public void updategroupchat(String groupchatid, String groupname, String membersnumber, String groupavatar, String description) {
        ContentValues cv = new ContentValues();
        cv.put("groupname", groupname);
        cv.put("membersnumber", membersnumber);
        cv.put("groupavatar", groupavatar);
        cv.put("description", description);
        G.mydb.update("groupchatrooms", cv, " groupchatid = '" + groupchatid + "' ", null);
    }


    public void updatechannels(String uid, String lastmsg, String lasttime) {

        ContentValues cv = new ContentValues();
        cv.put("lastmessage", lastmsg);
        cv.put("lastdate", lasttime);
        G.mydb.update("channels", cv, " uid = '" + uid + "' ", null);
    }


    public void updatechannelsinfo(String uid, String name, String description) {

        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("description", description);
        G.mydb.update("channels", cv, " uid = '" + uid + "' ", null);
    }


    public void updategroupinfo(String groupchatid, String groupname, String description) {

        ContentValues cv = new ContentValues();
        cv.put("groupname", groupname);
        cv.put("description", description);
        G.mydb.update("groupchatrooms", cv, " groupchatid = '" + groupchatid + "' ", null);
    }


    public void updateCropSetting(int value) {
        ContentValues values = new ContentValues();
        values.put("crop", value);
        G.mydb.update("setting", values, null, null);
    }


    public void updateHijriDate(int value) {
        ContentValues values = new ContentValues();
        values.put("hijridate", value);
        G.mydb.update("setting", values, null, null);
    }


    public void addCountry(String country) {
        ContentValues values = new ContentValues();
        values.put("country", country);
        G.mydb.update("setting", values, null, null);
    }


    public void updateContacts2(String name, String mobile, String registered, String avatar_lq, String avatar_hq, String last_seen, String uid) {

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("mobile", mobile);
        values.put("registered", registered);
        values.put("avatar_lq", avatar_lq);
        values.put("avatar_hq", avatar_hq);
        values.put("last_seen", last_seen);
        values.put("uid", uid);
        G.mydb.update("Contacts", values, " mobile = '" + mobile + "'", null);
    }


    public void updateRole(String uid, String membership) {
        ContentValues values = new ContentValues();
        values.put("membership", membership);
        G.mydb.update("channels", values, "uid = '" + uid + "'", null);
    }


    public void updateUserAvatar(String msgSender, String avatar) { // dar MyService , payame jadide groupChat
        ContentValues values = new ContentValues();
        values.put("msg_sender_avatar", avatar);
        G.mydb.update("groupchathistory", values, "msg_sender = '" + msgSender + "'", null);
    }


    public void updateFilePercents(String filehash) {
        ContentValues values = new ContentValues();
        values.put("percent", "0");
        G.mydb.update("files", values, "filehash = '" + filehash + "'", null);
    }


    public void updateGroupMembersNumber(String groupchatid, String membersnumber) {
        ContentValues values = new ContentValues();
        values.put("membersnumber", membersnumber);
        G.mydb.update("groupchatrooms", values, "groupchatid = '" + groupchatid + "'", null);
    }


    public void updateSeenAllBeforMessage(String msgID) {
        ContentValues values = new ContentValues();
        values.put("status", "3");
        G.mydb.update("chathistory", values, "msg_id = '" + msgID + "'", null);
    }


    public void updateChannelIdAndTime(String channeluid, String msg_id, String msg_time, String status, String id) {
        ContentValues values = new ContentValues();
        values.put("msg_id", msg_id);
        values.put("msg_time", msg_time);
        values.put("status", status);
        G.mydb.update("channelhistory", values, "channeluid = '" + channeluid + "' AND id = '" + id + "'", null);
    }


    public void updateChatFileStatus(String filehash, String status) {
        ContentValues cv = new ContentValues();
        cv.put("status", status);
        G.mydb.update("chathistory", cv, "filehash = '" + filehash + "'", null);
    }


    public void updateChannelMessageInResend(String channeluid, String newMsgID, String msg_time, String status, String id) {
        ContentValues values = new ContentValues();
        values.put("msg_id", newMsgID);
        values.put("msg_time", msg_time);
        values.put("status", status);
        G.mydb.update("channelhistory", values, "channeluid = '" + channeluid + "' AND id = '" + id + "'", null);
    }


    public void updateGroupMessageStatus(String groupchatroom_id, String status, String msg_id) {
        ContentValues values = new ContentValues();
        values.put("status", status);
        G.mydb.update("groupchathistory", values, "groupchatroom_id = '" + groupchatroom_id + "' AND msg_id = '" + msg_id + "'", null);
    }


    public void updateGroupMessageID(String groupchatroom_id, String oldMsgID, String newMsgID) {
        ContentValues values = new ContentValues();
        values.put("msg_id", newMsgID);
        G.mydb.update("groupchathistory", values, "groupchatroom_id = '" + groupchatroom_id + "' AND msg_id = '" + oldMsgID + "'", null);
    }


    public void updateChatMessageID(String chatroom_id, String msg_id, String newMsgID) {
        ContentValues values = new ContentValues();
        values.put("msg_id", newMsgID);
        G.mydb.update("chathistory", values, "chatroom_id = '" + chatroom_id + "' AND msg_id = '" + msg_id + "'", null);
    }


    public void updateChannelMessageStatus(String channeluid, String seen, String msg_id) {
        ContentValues values = new ContentValues();
        values.put("seen", seen);
        G.mydb.update("channelhistory", values, "channeluid = '" + channeluid + "' AND msg_id = '" + msg_id + "'", null);
    }


    public void updateActiveChannel(String uid, String active) {
        ContentValues values = new ContentValues();
        values.put("active", active);
        G.mydb.update("channels", values, "uid = '" + uid + "'", null);
    }


    public void updateActive(String groupchatid, String active) {
        ContentValues values = new ContentValues();
        values.put("active", active);
        G.mydb.update("groupchatrooms", values, "groupchatid = '" + groupchatid + "'", null);
    }


    public void updateGroupsMembers(String groupchatid, String membersnumber) {
        ContentValues values = new ContentValues();
        values.put("membersnumber", membersnumber);
        G.mydb.update("groupchatrooms", values, "groupchatid = '" + groupchatid + "'", null);
    }


    public void updateGroupsInfoInSplash(String groupchatid, String groupname, String membership, String membersnumber, String groupavatar, String groupavatarHq, String description) {
        ContentValues values = new ContentValues();
        values.put("groupchatid", groupchatid);
        values.put("groupname", groupname);
        values.put("membership", membership);
        values.put("membersnumber", membersnumber);
        values.put("groupavatar", groupavatar);
        values.put("groupavatarhq", groupavatarHq);
        values.put("description", description);
        G.mydb.update("groupchatrooms", values, "groupchatid = '" + groupchatid + "'", null);
    }


    public void updateChannelsInfoInSplash(String uid, String name, String description, String membersnumber, String avatar_lq, String avatar_hq, String membership) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("description", description);
        values.put("membersnumber", membersnumber);
        values.put("avatar_lq", avatar_lq);
        values.put("avatar_hq", avatar_hq);
        values.put("membership", membership);
        G.mydb.update("channels", values, "uid = '" + uid + "'", null);
    }


    public void updateChannelHistory(String channeluid, String filehash, String msg_id, String msg_time) {
        ContentValues cv = new ContentValues();
        cv.put("msg_id", msg_id);
        cv.put("msg_time", msg_time);
        G.mydb.update("channelhistory", cv, "filehash = '" + filehash + "' AND channeluid = '" + channeluid + "'", null);
    }


    public void updateChatroomAvatar1(String avatar, String userchat) {
        ContentValues cv = new ContentValues();
        cv.put("userchatavatar", avatar);
        G.mydb.update("Chatrooms", cv, " userchat = '" + userchat + "' ", null);
    }


    public void updateChatroomAvatar2(String avatarLq, String avatarHq, String mobile) {
        ContentValues cv = new ContentValues();
        cv.put("avatar_lq", avatarLq);
        cv.put("avatar_hq", avatarHq);
        G.mydb.update("Contacts", cv, " mobile = '" + mobile + "' ", null);
    }


    public void updateChannel(String uid, String lastmsg, String lasttime) {
        ContentValues cv = new ContentValues();
        cv.put("lastmessage", lastmsg);
        cv.put("lastdate", lasttime);
        G.mydb.update("channels", cv, " uid = '" + uid + "' ", null);
    }


    public void updateFilePath(int status, String fileHash, String filepath) {
        ContentValues cv = new ContentValues();
        cv.put("status", status);
        cv.put("filepath", filepath);
        G.mydb.update("files", cv, " filehash = '" + fileHash + "' ", null);
    }


    public void updateUploadStatus(String chatroomID, String fileHash, String messageID, String status) {
        ContentValues cv = new ContentValues();
        cv.put("msg_id", messageID);
        cv.put("status", status);
        G.mydb.update("chathistory", cv, "filehash = '" + fileHash + "' AND chatroom_id = '" + chatroomID + "'", null);
    }


    public void updatePercent(String fileHash, int percent) {
        ContentValues cv = new ContentValues();
        cv.put("percent", percent);
        G.mydb.update("files", cv, "filehash = '" + fileHash + "'", null);
    }


    public void updatefilestatus(String fileHash, int status) {
        ContentValues cv = new ContentValues();
        cv.put("status", status);
        G.mydb.update("files", cv, "filehash = '" + fileHash + "'", null);
    }


    public void updateType(String fileHash, int type) {
        ContentValues cv = new ContentValues();
        cv.put("type", type);
        G.mydb.update("files", cv, "filehash = '" + fileHash + "'", null);
    }


    public void updatefiles1(String filehash, String fileurl, String filethumb) {
        ContentValues values = new ContentValues();
        values.put("fileurl", fileurl);
        values.put("filethumb", filethumb);
        G.mydb.update("files", values, " filehash = '" + filehash + "'", null);
    }


    //setting querys
    public void updatesendbyenter(int value) {
        ContentValues values = new ContentValues();
        values.put("sendbyenter", value);
        G.mydb.update("setting", values, null, null);
    }


    public void updatetextcolor(String value) {
        ContentValues values = new ContentValues();
        values.put("textcolor", value);
        G.mydb.update("setting", values, null, null);
    }


    public void updatetextsize(int value) {
        ContentValues values = new ContentValues();
        values.put("textsize", value);
        G.mydb.update("setting", values, null, null);
    }


    public void updateautodownload(int value) {
        ContentValues values = new ContentValues();
        values.put("autodownload", value);
        G.mydb.update("setting", values, null, null);
    }


    public void updatenotification(int value) {
        ContentValues values = new ContentValues();
        values.put("notification", value);
        G.mydb.update("setting", values, null, null);
    }


    public void updateprofile(String name) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        G.mydb.update("info", values, null, null);
    }


    public void updateringtone(int value) {
        ContentValues values = new ContentValues();
        values.put("ringtone", value);
        G.mydb.update("setting", values, null, null);
    }


    public String getsetting(int field) {
        Cursor cursor = G.mydb.rawQuery("SELECT * FROM setting", null);
        cursor.moveToFirst();
        String str;
        try {
            str = cursor.getString(field);
        }
        catch (IllegalStateException e) {
            str = "0";
        }
        if (str == null) {
            str = "0";
        }
        cursor.close();
        return str;
    }


    public void updatever(int update, String vcode) {
        ContentValues values = new ContentValues();
        values.put("needupdate", update);
        values.put("vcode", vcode);
        G.mydb.update("setting", values, null, null);
    }


    public void updateselfremove(String month) {
        ContentValues values = new ContentValues();
        values.put("selfremove", month);
        G.mydb.update("setting", values, null, null);
    }


    public void setCompletedDownload(int value) {

        ContentValues values = new ContentValues();
        values.put("downloadcompleted", value);
        G.mydb.update("setting", values, null, null);
    }


    public void updategroupactive(int value, String groupchatid) {
        ContentValues values = new ContentValues();
        values.put("active", value);
        G.mydb.update("groupchatrooms", values, " groupchatid = '" + groupchatid + "'", null);
    }


    public void updatechannelactive(int value, String uid) {
        ContentValues values = new ContentValues();
        values.put("active", value);
        G.mydb.update("channels", values, " uid = '" + uid + "'", null);
    }


    public void updatechatactive(int value, String userchat) {
        ContentValues values = new ContentValues();
        values.put("active", value);
        G.mydb.update("Chatrooms", values, " userchat = '" + userchat + "'", null);
    }
}
