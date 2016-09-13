// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.adapter;

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
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import com.iGap.R;
import com.iGap.instruments.JSONParser;
import com.iGap.interfaces.OnComplet;
import com.iGap.interfaces.OnDeleteComplete;


/**
 * 
 * this class use for action in group like edit or delete one group or delete member of group
 * 
 * @use use in allrecycle adapter and group recycle adapter
 *
 */

public class GroupAdapter {

    private Context          mContext;
    private Dialog           pDialog;
    private OnDeleteComplete DeleteCompletelistener;
    private JSONParser       jParser = new JSONParser();


    public GroupAdapter(Context context) {

        super();
        mContext = context;

        initDialogWait();
    }


    private void initDialogWait() {

        pDialog = new Dialog(mContext);
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
    }


    public void deleteGroup(String gchatid, String username, String membership, OnDeleteComplete OnDeleteCompletelistener) {
        DeleteCompletelistener = OnDeleteCompletelistener;
        if (membership != null) {
            if (membership.equals("1")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    new deletegroup().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, gchatid, username);
                } else {
                    new deletegroup().execute(gchatid, username);
                }
            } else {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    new deletegroupmember().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, gchatid, username);
                } else {
                    new deletegroupmember().execute(gchatid, username);
                }

            }
        }
    }


    class deletegroup extends AsyncTask<String, String, String> {

        boolean successDelete;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.show();

        }


        @Override
        protected String doInBackground(String... args) {
            String gchatid = args[0];
            try {
                String[] splited = gchatid.split("@");
                String id = splited[0];
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                JSONObject jsonobj = jParser.getJSONFromUrl(G.deletegroup + id, params, "DELETE", G.basicAuth, null);

                try {
                    String statuscode = jsonobj.getString("statuscode");
                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);
                    successDelete = json.getBoolean(G.TAG_SUCCESS);

                    if (successDelete == true) {

                        G.cmd.cleargroupchathistory(gchatid);
                        G.cmd.deletegroupchatrooms(gchatid);
                    } else {
                        if (statuscode.equals("403")) {
                            ((Activity) mContext).runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(mContext, mContext.getString(R.string.chatroom_has_member_en), Toast.LENGTH_SHORT).show();
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
                catch (JSONException e) {
                    ((Activity) mContext).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(mContext, mContext.getString(R.string.internet_connection_problem_en), Toast.LENGTH_SHORT).show();
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

            return null;
        }


        @Override
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if (successDelete == true) {
                DeleteCompletelistener.deleteComplete(true);
            }
        }

    }


    class deletegroupmember extends AsyncTask<String, String, String> {

        boolean successDelete;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog.show();
        }


        @Override
        protected String doInBackground(String... args) {
            String gchatid = args[0];
            String username = args[1];
            try {
                String[] splited = gchatid.split("@");
                String id = splited[0];
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                JSONArray array = new JSONArray();
                array.put(username);
                JSONObject jsonobj = jParser.getJSONFromUrl(G.deletegroup + id + "/members", params, "DELETE-BODY", G.basicAuth, array.toString());

                try {
                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);

                    successDelete = json.getBoolean(G.TAG_SUCCESS);

                    if (successDelete == true) {

                        G.cmd.cleargroupchathistory(gchatid);
                        G.cmd.deletegroupchatrooms(gchatid);
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
                            Toast.makeText(mContext, mContext.getString(R.string.internet_connection_problem_en), Toast.LENGTH_SHORT).show();
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

            return null;
        }


        @Override
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if (successDelete == true) {
                DeleteCompletelistener.deleteComplete(true);
            }
        }

    }


    public void editGroup(String groupuid, String newGroupName, String newGroupDescription, OnComplet listener) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new editGroupAsync(groupuid, newGroupName, newGroupDescription, listener).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new editGroupAsync(groupuid, newGroupName, newGroupDescription, listener).execute();
        }

    }


    private class editGroupAsync extends AsyncTask<String, String, String> {

        private String    groupuid;
        private String    newGroupName;
        private String    newGroupDescription;
        private OnComplet listener;
        private Boolean   result;
        private String    message;


        public editGroupAsync(String groupuid, String newGroupName, String newGroupDescription, OnComplet listener) {
            this.groupuid = groupuid;
            this.newGroupName = newGroupName;
            this.newGroupDescription = newGroupDescription;
            this.listener = listener;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog.show();
        }


        @Override
        protected String doInBackground(String... args) {
            try {
                String[] splited = groupuid.split("@");
                String id = splited[0];
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("name", newGroupName));
                params.add(new BasicNameValuePair("description", newGroupDescription));
                JSONObject jsonobj = jParser.getJSONFromUrl(G.chatroommembers + id, params, "PUT", G.basicAuth, null);

                try {
                    String statuscode = jsonobj.getString("statuscode");

                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);

                    Boolean success = json.getBoolean(G.TAG_SUCCESS);

                    if (success == true) {
                        G.cmd.updategroupinfo(groupuid, newGroupName, newGroupDescription);
                        result = true;
                        message = mContext.getString(R.string.group_successfully_edited_en);
                    } else {

                        if (statuscode.equals("403")) {
                            result = true;
                            message = "";
                        } else if (statuscode.equals("400")) {
                            result = false;
                            message = mContext.getString(R.string.illegal_characters);
                        } else {
                            result = false;
                            message = mContext.getString(R.string.Error_occurred_en);
                        }

                    }
                }
                catch (JSONException e) {
                    result = false;
                    message = mContext.getString(R.string.internet_connection_problem_en);
                }
            }
            catch (Exception e) {
                result = false;
                message = mContext.getString(R.string.internet_connection_problem_en);
            }

            return null;
        }


        @Override
        protected void onPostExecute(String file_url) {
            listener.complet(result, message);
            pDialog.dismiss();
        }
    }


    public void deleteAvatar(String groupuid, OnComplet listener) {

        new deleteavatar(groupuid, listener).execute();

    }


    private class deleteavatar extends AsyncTask<String, String, String> {

        private Boolean   success1;

        private String    groupuid;
        private OnComplet listener;
        private boolean   result = true;
        private String    message;


        public deleteavatar(String groupuid, OnComplet listener) {

            this.groupuid = groupuid;
            this.listener = listener;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog.show();
        }


        @Override
        protected String doInBackground(String... args) {
            try {
                String[] splited = groupuid.split("@");
                String id = splited[0];
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                JSONObject jsonobj = jParser.getJSONFromUrl(G.deletegroup + id + "/avatars", params, "DELETE", G.basicAuth, null);
                try {
                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);
                    success1 = json.getBoolean(G.TAG_SUCCESS);

                    if (success1 == true) {
                        G.cmd.updategroupavatar(groupuid, "empty", "empty");
                    }
                }
                catch (JSONException e) {

                    result = false;
                    message = mContext.getString(R.string.internet_connection_problem_en);
                }
            }
            catch (Exception e) {

                result = false;
                message = mContext.getString(R.string.internet_connection_problem_en);
            }

            return null;
        }


        @Override
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();

            if (result == false) {
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
            }
            if (success1) {
                listener.complet(true, "");
            }

        }

    }

}
