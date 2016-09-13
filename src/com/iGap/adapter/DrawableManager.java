// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.adapter;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import com.iGap.customviews.ImageSquareProgressBar;
import com.iGap.helpers.HelperDecodeFile;


public class DrawableManager {

    private final Map<String, Bitmap> drawableMap;
    private Handler                   handler = new Handler();


    public DrawableManager(Context mContext) {
        drawableMap = new HashMap<String, Bitmap>();
    }


    public Bitmap fetchDrawable(String filepath) {
        if (drawableMap.containsKey(filepath)) {
            return drawableMap.get(filepath);
        }

        try {
            Bitmap bitmap = fetch(filepath);

            if (bitmap != null) {
                drawableMap.put(filepath, bitmap);
            }
            return bitmap;
        }
        catch (MalformedURLException e) {
            return null;
        }
        catch (IOException e) {
            return null;
        }
    }


    public void fetchDrawableOnThread(final String filepath, final ImageSquareProgressBar imageView) {

        if (drawableMap.containsKey(filepath)) {
            imageView.setImageBitmap(drawableMap.get(filepath));
            imageView.setTag(drawableMap.get(filepath));
            return;
        }

        Thread thread = new Thread() {

            @Override
            public void run() {

                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        Bitmap bitmap = fetchDrawable(filepath);
                        imageView.setImageBitmap(bitmap);
                        imageView.setTag(bitmap);
                    }
                });
            }
        };
        thread.start();
    }


    private Bitmap fetch(String filepath) throws MalformedURLException, IOException {
        File file = new File(filepath);
        Bitmap bitmap = HelperDecodeFile.decodeFile(file);
        return bitmap;
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    protected int sizeOf(Bitmap data) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            int byteSize = data.getRowBytes() * data.getHeight();
            int KB = byteSize / 1024;
            return KB;
        } else {
            int byteSize = data.getByteCount();
            int KB = byteSize / 1024;
            return KB;
        }
    }
}