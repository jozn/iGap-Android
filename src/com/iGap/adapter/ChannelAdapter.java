// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.adapter;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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
import com.iGap.helpers.HelperGetTime;
import com.iGap.instruments.JSONParser;
import com.iGap.interfaces.OnComplet;
import com.iGap.interfaces.OnDeleteComplete;


/**
 * 
 * darkhasthaye morede niaz samte server az jomle delete kanal (delete kardan va ya tarke kanal) virayeshe kanal va bakhshi az ersale payamha
 *
 */

public class ChannelAdapter {

    private Context          mContext;
    private Dialog           pDialog;
    private OnDeleteComplete DeleteCompletelistener;
    private JSONParser       jParser = new JSONParser();


    public ChannelAdapter(Context context) {

        super();
        mContext = context;
        initDialogWait();
    }


    public void deleteChannel(String userState, String uid, OnDeleteComplete OnDeleteCompletelistener) {
        DeleteCompletelistener = OnDeleteCompletelistener;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            new deletechannel().execute(userState, uid);
        } else {
            new deletechannel().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, userState, uid);
        }
    }


    private void initDialogWait() {
        pDialog = new Dialog(mContext);
        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
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


    class deletechannel extends AsyncTask<String, String, String> {

        boolean successDelete;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.show();
        }


        @Override
        protected String doInBackground(String... args) {
            try {
                String userState = args[0];
                String uid = args[1];
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                JSONObject jsonobj = null;

                if (userState.equals("1")) {
                    jsonobj = jParser.getJSONFromUrl(G.addmembertochannel + uid, params, "DELETE", G.basicAuth, null);
                } else if (userState.equals("2") || userState.equals("0")) {
                    jsonobj = jParser.getJSONFromUrl(G.addmembertochannel + uid + "/left", params, "POST", G.basicAuth, null);
                } else {
                    jsonobj = jParser.getJSONFromUrl(G.addmembertochannel + uid + "/left", params, "POST", G.basicAuth, null);
                }
                try {
                    String statuscode = jsonobj.getString("statuscode");
                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);
                    successDelete = json.getBoolean(G.TAG_SUCCESS);

                    if (successDelete == true) {

                        G.cmd.clearChannelHistory(uid);
                        G.cmd.deleteChannel(uid);

                        ((Activity) mContext).runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(mContext, mContext.getString(R.string.channel_successfully_deleted_en), Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        if (statuscode.equals("403")) {
                            ((Activity) mContext).runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(mContext, mContext.getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
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


    public void editChannel(String newChannelName, String newChannelDescription, String channeluid, OnComplet listener) {

        new editChannel(newChannelName, newChannelDescription, channeluid, listener).execute();
    }


    private class editChannel extends AsyncTask<String, String, String> {

        private String    newChannelName;
        private String    newChannelDescription;
        private String    channeluid;
        private OnComplet listener;
        private Boolean   result = false;


        public editChannel(String newChannelName, String newChannelDescription, String channeluid, OnComplet listener) {
            this.newChannelName = newChannelName;
            this.newChannelDescription = newChannelDescription;
            this.channeluid = channeluid;
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
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("name", newChannelName));
                params.add(new BasicNameValuePair("description", newChannelDescription));
                JSONObject jsonobj = jParser.getJSONFromUrl(G.createchannel + "/" + channeluid, params, "PUT", G.basicAuth, null);

                try {
                    String statuscode = jsonobj.getString("statuscode");
                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);

                    Boolean success = json.getBoolean(G.TAG_SUCCESS);

                    if (success == true) {
                        G.cmd.updatechannelsinfo(channeluid, newChannelName, newChannelDescription);
                        ((Activity) mContext).runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(mContext, mContext.getString(R.string.channel_successfully_edited_en), Toast.LENGTH_SHORT).show();
                            }
                        });
                        result = true;

                    } else {

                        if (statuscode.equals("403")) {
                            ((Activity) mContext).runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    result = true;
                                    //Toast.makeText(mContext, mContext.getString(R.string.channel_isnot_foryou_edit_en), Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else if (statuscode.equals("400")) {
                            ((Activity) mContext).runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(mContext, mContext.getString(R.string.illegal_characters), Toast.LENGTH_SHORT).show();
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

            listener.complet(result, "");
        }
    }


    public void SendChannelMessageFromUploader(String channeluid, String channelFilehash, String lastFileDatabaseID, String channelMessage, OnComplet listener) {

        new SendChannelMessageFromUploader(channeluid, listener).execute(channelFilehash, lastFileDatabaseID, channelMessage);
    }


    private class SendChannelMessageFromUploader extends AsyncTask<String, String, String> {

        private Boolean   success2;
        private String    serverTime, channelMessage;

        private String    channeluid;
        private OnComplet listener;


        public SendChannelMessageFromUploader(String channeluid, OnComplet listener) {
            this.channeluid = channeluid;
            this.listener = listener;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... args) {
            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();

                String channelFilehash = args[0];
                String lastFileDatabaseID = args[1];
                channelMessage = args[2];
                params.add(new BasicNameValuePair("hash", channelFilehash));
                params.add(new BasicNameValuePair("text", channelMessage));
                JSONObject jsonobj = jParser.getJSONFromUrl(G.createchannel + "/" + channeluid + "/posts", params, "POST", G.basicAuth, null);

                try {
                    String statuscode = jsonobj.getString("statuscode");
                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);

                    success2 = json.getBoolean(G.TAG_SUCCESS);
                    if (success2 == true) {

                        JSONObject result = json.getJSONObject("result");
                        String id = result.getString("id");
                        serverTime = result.getString("createAt");
                        serverTime = HelperGetTime.convertWithSingleTime(serverTime, G.utcMillis);
                        G.cmd.updateChannelIdAndTime(channeluid, id, serverTime, "3", lastFileDatabaseID);

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

            return null;
        }


        @Override
        protected void onPostExecute(String file_url) {
            if (success2 == true) {

                listener.complet(true, serverTime);
            }
        }
    }


    public void createChannel(String channelUid, String channelName, String channelDesc, OnComplet onComplet) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new createChannel(channelUid, channelName, channelDesc, onComplet).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new createChannel(channelUid, channelName, channelDesc, onComplet).execute();
        }
    }


    private class createChannel extends AsyncTask<String, String, String> {

        private String    channelUid;
        private String    channelName;
        private String    channelDesc;
        private OnComplet onComplet;


        public createChannel(String channelUid, String channelName, String channelDesc, OnComplet onComplet) {
            this.channelUid = channelUid;
            this.channelName = channelName;
            this.channelDesc = channelDesc;
            this.onComplet = onComplet;
        }


        @Override
        protected String doInBackground(String... args) {
            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("uid", channelUid));
                params.add(new BasicNameValuePair("name", channelName));
                params.add(new BasicNameValuePair("description", channelDesc));
                JSONObject jsonobj = jParser.getJSONFromUrl(G.createchannel, params, "POST", G.basicAuth, null);

                try {
                    String statuscode = jsonobj.getString("statuscode");
                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);
                    Boolean success = json.getBoolean(G.TAG_SUCCESS);

                    if (success == true) {
                        onComplet.complet(true, "");

                    } else {
                        if (statuscode.equals("400")) {
                            String errorStatus = "";
                            JSONObject result = json.getJSONObject("result");
                            errorStatus = result.getString("errorStatus");
                            if (errorStatus.equals("1")) {

                                onComplet.complet(false, G.context.getString(R.string.channel_created_before));

                            } else {

                                onComplet.complet(false, G.context.getString(R.string.illegal_characters));

                            }
                        } else if (statuscode.equals("403")) {
                            String errorStatus = "";
                            JSONObject result = json.getJSONObject("result");
                            errorStatus = result.getString("errorStatus");
                            if (errorStatus.equals("7")) {

                                onComplet.complet(false, G.context.getString(R.string.channel_created_before));

                            } else {
                                onComplet.complet(false, G.context.getString(R.string.Error_occurred_en));
                            }
                        } else {
                            onComplet.complet(false, G.context.getString(R.string.Error_occurred_en));
                        }
                    }
                }
                catch (JSONException e) {
                    onComplet.complet(false, G.context.getString(R.string.Error_occurred_en));

                }
            }
            catch (Exception e) {
                onComplet.complet(false, G.context.getString(R.string.internet_connection_problem_en));
            }

            return null;
        }
    }

}
