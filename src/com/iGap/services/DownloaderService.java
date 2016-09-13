// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.services;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.NetworkOnMainThreadException;
import com.iGap.R;
import com.iGap.adapter.ChannelRecycleAdapter;
import com.iGap.adapter.DrawableManager;
import com.iGap.adapter.DrawableManagerDialog;
import com.iGap.adapter.G;
import com.iGap.adapter.GroupChatRecycleAdapter;
import com.iGap.adapter.SingleChatRecycleAdapter;
import com.iGap.customviews.ImageSquareProgressBar;
import com.iGap.customviews.ImageSquareProgressBarDialog;
import com.iGap.helpers.HelperCopyFile;
import com.iGap.helpers.HelperString;
import com.iGap.interfaces.OnDownloadListener;


/**
 * 
 * main app downloader for download files and copy that to defined destination
 *
 */

public class DownloaderService extends Service {

    private int                   downloadedSize;
    private int                   totalSize;
    private int                   percent;
    private int                   model;
    private int                   downloaded    = 0;
    private int                   len           = 0;
    private Handler               handler       = new Handler();
    private HttpURLConnection     connection;
    private BufferedInputStream   bufferInputStream;
    private BufferedOutputStream  bufferedOutputStream;
    private Context               context;
    private ChannelRecycleAdapter Channeladapter;
    private boolean               updatePercent = true;


    public int getDownloadedSize() {
        return downloadedSize;
    }


    public int getTotalSize() {
        return totalSize;
    }


    public int getPercent() {
        return percent;
    }


    public DownloaderService download(Context value) {
        this.context = value;
        G.cmd.updatefilestatus(fileHash, 1);
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    URL url = new URL(downloadPath);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestProperty("Authorization", basicAuth);
                    File file = new File(filepath);
                    if (file.exists()) {
                        downloaded = (int) file.length();
                        connection.setRequestProperty("Range", "bytes=" + (file.length()) + "-");
                    } else {
                        connection.setRequestProperty("Range", "bytes=" + downloaded + "-");
                    }
                    connection.setRequestMethod(requestMethod);
                    connection.setDoInput(true);
                    connection.connect();

                    totalSize = connection.getContentLength();

                    bufferInputStream = new BufferedInputStream(connection.getInputStream());
                    FileOutputStream outputStream = null;
                    if (downloaded == 0) {
                        outputStream = new FileOutputStream(filepath);
                    } else {
                        outputStream = new FileOutputStream(filepath, true);
                    }
                    bufferedOutputStream = new BufferedOutputStream(outputStream, G.DOWNLOAD_BUFFER_SIZE);
                    byte[] buffer = new byte[G.DOWNLOAD_BUFFER_SIZE];
                    while ((len = bufferInputStream.read(buffer)) > 0) {
                        if (stopDownload || !isConnect()) {
                            downloadedSize = 0;
                            len = 0;
                            handler.post(new Runnable() {

                                @Override
                                public void run() {
                                    try {
                                        connection.disconnect();
                                    }
                                    catch (NetworkOnMainThreadException e) {
                                        e.printStackTrace();
                                    }
                                    closeStream();
                                }
                            });
                        } else {
                            bufferedOutputStream.write(buffer, 0, len);
                            bufferedOutputStream.flush();
                            downloadedSize += len;
                            percent = (int) (100.0f * (float) downloadedSize / totalSize);

                            final int finalDownloadedSize = downloadedSize;

                            handler.post(new Runnable() {

                                @Override
                                public void run() {

                                    try {
                                        if (listener != null) {
                                            listener.onProgressDownload(percent, finalDownloadedSize, totalSize, false);

                                            if (percent % 10 == 0) {
                                                if (updatePercent == true) {
                                                    G.cmd.updatePercent(fileHash, percent);
                                                }
                                                if (percent == 95) {
                                                    G.cmd.updatePercent(fileHash, 99);
                                                }
                                                updatePercent = false;
                                            } else {
                                                updatePercent = true;
                                            }

                                            if (percent >= 100) {
                                                listener = null;
                                            }
                                        }
                                    }
                                    catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                        }
                    }
                }
                catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (percent >= 99) {
                        if (listener != null) {
                            listener.onProgressDownload(100, downloadedSize, totalSize, true);
                            listener = null;
                            copyDownloadFile();
                        }
                    }

                    downloadedSize = 0;
                    len = 0;
                    closeStream();
                }
            }
        });

        thread.start();

        return this;
    }


    private void copyDownloadFile() {

        final String newFilePath;

        long currentTime = System.currentTimeMillis();
        if (fileType.equals("2")) {
            //Image

            if (imageViewType == 3) {
                String fileName = HelperString.getFileName(filepath);
                newFilePath = G.DIR_DIALOG + "/" + fileName;
                HelperCopyFile.copyFile(filepath, newFilePath);
                new File(filepath).delete();
            } else {

                if (filemim != null && !filemim.equals("")) {

                    newFilePath = G.DIR_IMAGES + "/" + currentTime + "." + filemim;
                    HelperCopyFile.copyFile(G.DIR_TEMP + "/" + fileHash + "." + filemim, newFilePath);
                    new File(G.DIR_TEMP + "/" + fileHash + "." + filemim).delete();
                    G.cmd.updateFilePath(2, fileHash, newFilePath);
                } else {
                    newFilePath = G.DIR_IMAGES + "/" + currentTime + ".jpg";
                    HelperCopyFile.copyFile(G.DIR_TEMP + "/" + fileHash + ".jpg", newFilePath);
                    new File(G.DIR_TEMP + "/" + fileHash + ".jpg").delete();
                    G.cmd.updateFilePath(2, fileHash, newFilePath);
                }

            }

            handler.post(new Runnable() {

                @Override
                public void run() {
                    File imgFile = new File(newFilePath);
                    if (imgFile.exists()) {

                        if (imageViewType != 1) { // baraye dialog estefade mishavad
                            drawableManagerDialog.fetchDrawableOnThread(newFilePath, imageViewDialog);
                        } else {
                            drawableManager.fetchDrawableOnThread(newFilePath, imageView);
                        }

                        if (model == 1) {
                            if (Singleadapter != null) {
                                if (position != -1) {
                                    Singleadapter.notifyItemChanged(position);
                                } else {
                                    Singleadapter.notifyDataSetChanged();
                                }
                            }
                        } else if (model == 2) {
                            if (Groupadapter != null) {
                                if (position != -1) {
                                    Groupadapter.notifyItemChanged(position);
                                } else {
                                    Groupadapter.notifyDataSetChanged();
                                }
                            }
                        } else {
                            if (Channeladapter != null) {
                                if (position != -1) {
                                    Channeladapter.notifyItemChanged(position);
                                } else {
                                    Channeladapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                }
            });

        } else if (fileType.equals("3")) {
            //Video
            HelperCopyFile.copyFile(G.DIR_TEMP + "/" + fileHash + ".mp4", G.DIR_VIDEOS + "/" + currentTime + ".mp4");
            new File(G.DIR_TEMP + "/" + fileHash + ".mp4").delete();
            newFilePath = G.DIR_VIDEOS + "/" + currentTime + ".mp4";
            G.cmd.updateFilePath(2, fileHash, newFilePath);

            handler.post(new Runnable() {

                @Override
                public void run() {
                    File imgFile = new File(newFilePath);

                    if (imgFile.exists()) {
                        imageView.setImageResource(R.drawable.video_attach_big_play);
                    } else {
                        imageView.setImageResource(R.drawable.video_attach_big_dl);
                    }
                }
            });

        } else if (fileType.equals("4")) {
            //music
            HelperCopyFile.copyFile(G.DIR_TEMP + "/" + fileHash + ".mp3", G.DIR_AUDIOS + "/" + currentTime + ".mp3");
            new File(G.DIR_TEMP + "/" + fileHash + ".mp3").delete();
            newFilePath = G.DIR_AUDIOS + "/" + currentTime + ".mp3";
            G.cmd.updateFilePath(2, fileHash, newFilePath);

        } else if (fileType.equals("7")) {
            //file

            if (filemim != null && !filemim.equals("")) {

                HelperCopyFile.copyFile(G.DIR_TEMP + "/" + fileHash + "." + filemim, G.DIR_DOCUMENTS + "/" + currentTime + "." + filemim);
                new File(G.DIR_TEMP + "/" + fileHash + "." + filemim).delete();
                newFilePath = G.DIR_DOCUMENTS + "/" + currentTime + "." + filemim;
                G.cmd.updateFilePath(2, fileHash, newFilePath);
            } else {
                int index = message.lastIndexOf('.') + 1;
                String ext = message.substring(index).toLowerCase();
                HelperCopyFile.copyFile(G.DIR_TEMP + "/" + fileHash + "." + ext, G.DIR_DOCUMENTS + "/" + currentTime + "." + ext);
                new File(G.DIR_TEMP + "/" + fileHash + "." + ext).delete();
                newFilePath = G.DIR_DOCUMENTS + "/" + currentTime + "." + ext;
                G.cmd.updateFilePath(2, fileHash, newFilePath);
            }

            handler.post(new Runnable() {

                @Override
                public void run() {
                    File imgFile = new File(newFilePath);
                    if (imgFile.exists()) {
                        imageView.setImageResource(R.drawable.atach);
                    } else {
                        imageView.setImageResource(R.drawable.atach_mini_dl);
                    }
                }
            });
        } else if (fileType.equals("8")) {
            //file
            HelperCopyFile.copyFile(G.DIR_TEMP + "/" + fileHash + ".mp3", G.DIR_RECORD + "/" + currentTime + ".mp3");
            new File(G.DIR_TEMP + "/" + fileHash + ".mp3").delete();
            newFilePath = G.DIR_RECORD + "/" + currentTime + ".mp3";
            G.cmd.updateFilePath(2, fileHash, newFilePath);
        }
    }

    private String                       downloadPath;
    private String                       filepath;
    private String                       fileType;
    private String                       requestMethod = "GET";
    private String                       basicAuth;
    private String                       fileHash;
    private String                       message;
    private String                       filemim;
    private OnDownloadListener           listener;
    private boolean                      simulate      = false;
    private boolean                      stopDownload  = false;
    private DrawableManager              drawableManager;
    private DrawableManagerDialog        drawableManagerDialog;
    private ImageSquareProgressBar       imageView;
    private ImageSquareProgressBarDialog imageViewDialog;
    private SingleChatRecycleAdapter     Singleadapter;
    private GroupChatRecycleAdapter      Groupadapter;
    private int                          imageViewType = 1;
    private int                          position      = -1;


    public DownloaderService downloadPath(String value) {
        downloadPath = value;
        return this;
    }


    public DownloaderService filepath(String value) {
        filepath = value;
        return this;
    }


    public DownloaderService filetype(String value) {
        fileType = value;
        return this;
    }


    public DownloaderService requestMethod(String value) {
        requestMethod = value;
        return this;
    }


    public DownloaderService fileHash(String value) {
        fileHash = value;
        return this;
    }


    public DownloaderService message(String value) {
        message = value;
        return this;
    }


    public DownloaderService listener(OnDownloadListener value) {
        listener = value;
        return this;
    }


    public DownloaderService simulate(boolean value) {
        simulate = value;
        return this;
    }


    public DownloaderService stopDownload(boolean value) {
        stopDownload = value;
        return this;
    }


    public DownloaderService Authorization(String value) {
        basicAuth = value;
        return this;
    }


    public DownloaderService filemim(String value) {
        filemim = value;
        return this;
    }


    public DownloaderService drawableManager(DrawableManager value) {
        drawableManager = value;
        return this;
    }


    public DownloaderService drawableManager(DrawableManagerDialog value) {
        drawableManagerDialog = value;
        return this;
    }


    public DownloaderService imageView(ImageSquareProgressBar value) {
        imageView = value;
        return this;
    }


    public DownloaderService imageView(ImageSquareProgressBarDialog value) {
        imageViewDialog = value;
        return this;
    }


    public DownloaderService adapterSingle(SingleChatRecycleAdapter value) {
        Singleadapter = value;
        return this;
    }


    public DownloaderService adapterChannel(ChannelRecycleAdapter value) {
        Channeladapter = value;
        return this;
    }


    public DownloaderService adapterGroup(GroupChatRecycleAdapter value) {
        Groupadapter = value;
        return this;
    }


    public DownloaderService model(int value) {
        model = value;
        return this;
    }


    public DownloaderService setPosition(int value) {
        position = value;
        return this;
    }


    public DownloaderService imageViewType(int value) {
        imageViewType = value;
        return this;
    }

    private ConnectivityManager conectivityManager;
    private NetworkInfo         networkInfo;


    private boolean isConnect() {
        conectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = conectivityManager.getActiveNetworkInfo();
        return networkInfo.isConnected();
    }


    private void closeStream() {
        if (bufferInputStream != null) {
            try {
                bufferInputStream.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (bufferedOutputStream != null) {
            try {
                bufferedOutputStream.flush();
                bufferedOutputStream.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        return Service.START_STICKY;
    }


    @Override
    public boolean onUnbind(final Intent intent) {
        return super.onUnbind(intent);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        connection.disconnect();
        closeStream();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
