// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;
import com.iGap.GroupChat;
import com.iGap.R;
import com.iGap.Singlechat;
import com.iGap.adapter.G;
import com.iGap.instruments.AndroidMultiPartEntity;
import com.iGap.instruments.AndroidMultiPartEntity.ProgressListener;
import com.iGap.instruments.Config;
import com.iGap.interfaces.OnUploadListener;


/**
 * 
 * main app uploader for upload a file to igap center
 *
 */

public class UploaderService extends Service {

    private int               downloadedSize;
    private int               percent;
    private long              totalSize;
    private boolean           updatePercent = true;
    private String            json;
    private String            fileurl;
    private String            filethumb;
    private String            filemime;
    private JSONObject        jsonObject;
    private HttpURLConnection connection;
    private Context           context;
    private Handler           handler       = new Handler();


    public int getDownloadedSize() {
        return downloadedSize;
    }


    public long getTotalSize() {
        return totalSize;
    }


    public int getPercent() {
        return percent;
    }


    public UploaderService upload(Context value) {
        this.context = value;

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                uploadFile();
            }
        });
        thread.start();

        return this;
    }


    @SuppressWarnings("deprecation")
    private String uploadFile() {
        String responseString = null;

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(Config.FILE_UPLOAD);
        httppost.setHeader("Authorization", basicAuth);

        try {

            AndroidMultiPartEntity entity = new AndroidMultiPartEntity(new ProgressListener() {

                @Override
                public void transferred(long num) {
                    percent = (int) ((num / (float) totalSize) * 100);
                    if (percent % 10 == 0) {
                        if (updatePercent == true) {
                            G.cmd.updatePercent(filehash, percent);
                            if (percent == 95) {
                                G.cmd.updatePercent(filehash, 99);
                            }
                        }
                        updatePercent = false;
                    } else {
                        updatePercent = true;
                    }

                    if (listener != null) {
                        listener.onProgressUpload(percent, false, lastFileMessageID);
                    }
                }
            });

            File sourceFile = new File(filepath);
            entity.addPart("file", new FileBody(sourceFile));

            // Extra parameters if you want to pass to server
            totalSize = entity.getContentLength();
            httppost.setEntity(entity);

            // Making server call
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity r_entity = response.getEntity();
            InputStream is = r_entity.getContent();
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 201) {

                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    is.close();
                    json = sb.toString();
                }
                catch (Exception e) {

                }
                try {
                    jsonObject = new JSONObject(json);
                }
                catch (JSONException e) {

                }

                try {
                    Boolean success = jsonObject.getBoolean(G.TAG_SUCCESS);
                    if (success == true) {
                        JSONObject result = jsonObject.getJSONObject("result");
                        fileurl = result.getString("url");
                        //                        filemime = result.getString("mime");
                        //                        fileextension = result.getString("extension");
                        filethumb = result.getString("thumbnailLq");
                        G.cmd.updatefiles1(filehash, fileurl, filethumb);
                        if (percent >= 100) {

                            handler.postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    if (messageType == 1) {

                                        String resendValue;

                                        if (resend) {
                                            resendValue = "1";
                                        } else {
                                            resendValue = "0";
                                        }
                                        if (mService != null) {
                                            mService.SendMessageOrder(filemime, userchat, chatMessage, type, filehash, fileurl, filethumb, replyMessage, replyFilehash, replyFrom, resendValue, null);
                                        } else {
                                            mService = Singlechat.getService();
                                            if (mService != null) {
                                                mService.SendMessageOrder(filemime, userchat, chatMessage, type, filehash, fileurl, filethumb, replyMessage, replyFilehash, replyFrom, resendValue, null);
                                            }
                                        }
                                    } else if (messageType == 2) {

                                        String resendValue;

                                        if (resend) {
                                            resendValue = "1"; // resend ast
                                        } else {
                                            resendValue = "0"; // resend nist
                                        }

                                        if (mService != null) {
                                            mService.sendgroupmessage(filemime, chatID, chatMessage, userchatavatar, type, filehash, fileurl, filethumb, replyMessage, replyFilehash, replyFrom, resendValue, null);
                                        } else {
                                            mService = GroupChat.getService();
                                            if (mService != null) {
                                                mService.sendgroupmessage(filemime, chatID, chatMessage, userchatavatar, type, filehash, fileurl, filethumb, replyMessage, replyFilehash, replyFrom, resendValue, null);
                                            }
                                        }
                                    }

                                    if (listener != null) {
                                        listener.onProgressUpload(percent, true, lastFileMessageID);
                                    }
                                }
                            }, 3000);
                        }

                    } else {

                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(context, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                catch (JSONException e) {

                    e.printStackTrace();
                }
            } else {
                responseString = "Error occurred! Http Status Cod1e: " + statusCode;
            }
        }
        catch (ClientProtocolException e) {
            responseString = e.toString();
        }
        catch (IOException e) {
            responseString = e.toString();
        }

        return responseString;

    }

    private String           filepath;
    private String           chatMessage;
    private String           basicAuth;
    private String           type;
    private String           filehash;
    private String           chatID;
    private String           replyMessage;
    private String           replyFilehash;
    private String           replyFrom;
    private String           userchat;
    private String           userchatavatar;
    private String           lastFileMessageID;
    private OnUploadListener listener;
    private MyService        mService;
    private int              messageType;
    private boolean          resend;


    public UploaderService filepath(String value) {
        filepath = value;
        return this;
    }


    public UploaderService chatMessage(String value) {
        chatMessage = value;
        return this;
    }


    public UploaderService Authorization(String value) {
        basicAuth = value;
        return this;
    }


    public UploaderService type(String value) {
        type = value;
        return this;
    }


    public UploaderService fileHash(String value) {
        filehash = value;
        return this;
    }


    public UploaderService chatID(String value) {
        chatID = value;
        return this;
    }


    public UploaderService replyMessage(String value) {
        replyMessage = value;
        return this;
    }


    public UploaderService replyFilehash(String value) {
        replyFilehash = value;
        return this;
    }


    public UploaderService replyFrom(String value) {
        replyFrom = value;
        return this;
    }


    public UploaderService userChat(String value) {
        userchat = value;
        return this;
    }


    public UploaderService userChatAvatar(String value) {
        userchatavatar = value;
        return this;
    }


    public UploaderService lastFileDatabaseID(String value) {
        lastFileMessageID = value;
        return this;
    }


    public UploaderService listener(OnUploadListener value) {
        listener = value;
        return this;
    }


    public UploaderService service(MyService value) {
        mService = value;
        return this;
    }


    public UploaderService messageType(int value) {
        messageType = value;
        return this;
    }


    public UploaderService filemime(String value) {
        filemime = value;
        return this;
    }


    public UploaderService resend(boolean value) {
        resend = value;
        return this;
    }


    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        return Service.START_NOT_STICKY;
    }


    @Override
    public boolean onUnbind(final Intent intent) {
        return super.onUnbind(intent);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        connection.disconnect();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
