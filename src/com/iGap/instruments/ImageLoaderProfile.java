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
import java.net.HttpURLConnection;
import java.net.URL;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import com.iGap.adapter.DrawableManagerDialog;
import com.iGap.adapter.G;
import com.iGap.customviews.ImageSquareProgressBarDialog;
import com.iGap.helpers.HelperCopyFile;
import com.iGap.helpers.HelperString;
import com.iGap.interfaces.OnDownloadListener;


/**
 * 
 * load image from web site
 *
 */

public class ImageLoaderProfile {

    private int                          position;
    private int                          downloadedSize;
    private int                          totalSize;
    private int                          percent;
    private int                          len           = 0;

    private String                       basicAuth;
    private String                       filepath;
    private String                       url;
    private String                       mobile        = "";

    private boolean                      updatePercent = true;

    private OnDownloadListener           listener;
    private DrawableManagerDialog        drawableManagerDialog;
    private ImageSquareProgressBarDialog img;
    private InputStream                  bufferInputStream;
    private OutputStream                 bufferedOutputStream;
    private Handler                      handler       = new Handler();


    public ImageLoaderProfile(Context context1, String url1, String basicAuth1, String filepath1, OnDownloadListener listener1, DrawableManagerDialog drawableManagerDialog1, ImageSquareProgressBarDialog img1, String mobile1, int position1) {
        basicAuth = basicAuth1;
        filepath = filepath1;
        listener = listener1;
        drawableManagerDialog = drawableManagerDialog1;
        img = img1;
        url = url1;
        position = position1;
        mobile = mobile1;
    }


    public ImageLoaderProfile(Context context1, String url1, String basicAuth1, String filepath1, OnDownloadListener listener1, DrawableManagerDialog drawableManagerDialog1, ImageSquareProgressBarDialog img1) {
        basicAuth = basicAuth1;
        filepath = filepath1;
        listener = listener1;
        drawableManagerDialog = drawableManagerDialog1;
        img = img1;
        url = url1;
    }


    public void getBitmap() {

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    URL imageUrl = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                    conn.setRequestProperty("Authorization", basicAuth);
                    conn.setConnectTimeout(30000);
                    conn.setReadTimeout(30000);
                    conn.setInstanceFollowRedirects(true);
                    totalSize = conn.getContentLength();
                    bufferInputStream = conn.getInputStream();
                    bufferedOutputStream = new FileOutputStream(filepath);

                    byte[] buffer = new byte[G.DOWNLOAD_BUFFER_SIZE];

                    while ((len = bufferInputStream.read(buffer)) > 0) {
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
                catch (Exception ex) {
                    ex.printStackTrace();
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


    private void copyDownloadFile() {

        final String newFilePath;
        String fileName = HelperString.getFileName(filepath);
        newFilePath = G.DIR_DIALOG + "/" + fileName;
        Log.i("LOG", "position : " + position);
        if (position == 0 && !mobile.equals("")) { // avalin tasvir ra ba shomareye mokhatab ham zakhire mikonim ta dar zaman didane tasavir avalin tasvir ra belafasele bebinim
            HelperCopyFile.copyFile(filepath, G.DIR_DIALOG + "/" + mobile + ".jpg");
        }
        HelperCopyFile.copyFile(filepath, newFilePath);
        new File(filepath).delete();

        handler.post(new Runnable() {

            @Override
            public void run() {
                File imgFile = new File(newFilePath);
                if (imgFile.exists()) {
                    drawableManagerDialog.fetchDrawableOnThread(newFilePath, img);
                }
            }
        });
    }
}
