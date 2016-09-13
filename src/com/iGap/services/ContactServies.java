// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.services;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Service;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import com.iGap.adapter.G;
import com.iGap.instruments.JSONParser;
import com.iGap.instruments.database;


/**
 * 
 * check every change in user contact list and send to igap center all contact after change
 *
 */

public class ContactServies extends Service {

    private ArrayList<String> list     = new ArrayList<String>();
    private ArrayList<String> list2    = new ArrayList<String>();
    private ArrayList<String> list3    = new ArrayList<String>();
    private JSONArray         array    = new JSONArray();
    private JSONArray         products = new JSONArray();
    private JSONObject        Parent   = new JSONObject();
    private JSONParser        jParser  = new JSONParser();
    private MyContentObserver contentObserver;

    private database          db;
    private String            basicAuth;
    private String[]          lastSeen, fullname, mobile, registered, avatar_hq, avatar_lq, uid, contactid;


    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        contentObserver = new MyContentObserver();

        db = new database(ContactServies.this);
        db.open();

        basicAuth = db.namayesh4(3, "info");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                getApplicationContext().getContentResolver().registerContentObserver(Contacts.CONTENT_URI, true, contentObserver);
            }
        }, 10000);
        return Service.START_NOT_STICKY;
    }


    public IBinder onBind(Intent intent) {
        return null;
    }


    private class MyContentObserver extends ContentObserver {

        public MyContentObserver() {
            super(null);
        }


        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            list = new ArrayList<String>();
            list2 = new ArrayList<String>();
            list3 = new ArrayList<String>();
            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
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

            new GetContactsFormServer().execute();
        }

    }


    class GetContactsFormServer extends AsyncTask<String, String, String> {

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
                            lastSeen[i] = c.getString("lastSeen");

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

                    } else {

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
            getApplicationContext().getContentResolver().registerContentObserver(Contacts.CONTENT_URI, true, contentObserver);
        }

    }

}
