// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.instruments;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import com.iGap.R;


/**
 * 
 * load image from web site
 *
 */

public class ImageLoaderProfileHq {

    private MemoryCache            memoryCache = new MemoryCache();
    private FileCache              fileCache;
    private Map<ImageView, String> imageViews  = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    private ExecutorService        executorService;
    private String                 basicAuth;


    public ImageLoaderProfileHq(Context context, String basicAuth1) {
        fileCache = new FileCache(context);
        basicAuth = basicAuth1;
        executorService = Executors.newFixedThreadPool(5);
    }

    int stub_id = R.drawable.difaultimage;


    public void DisplayImage(String url, int loader, ImageView imageView)
    {
        stub_id = loader;
        imageViews.put(imageView, url);
        Bitmap bitmap = memoryCache.get(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            imageView.setTag(bitmap);
        } else {
            queuePhoto(url, imageView);
            imageView.setImageResource(loader);
        }
    }


    private void queuePhoto(String url, ImageView imageView)
    {
        PhotoToLoad p = new PhotoToLoad(url, imageView);
        executorService.submit(new PhotosLoader(p));
    }


    private Bitmap getBitmap(String url)
    {
        File f = fileCache.getFile(url);

        //from SD cache
        Bitmap b = decodeFile(f);
        if (b != null)
            return b;

        //from web
        try {
            Bitmap bitmap = null;
            URL imageUrl = new URL(url);
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
            return bitmap;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


    //decodes image and scales it to reduce memory consumption
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


    //Task for the queue
    private class PhotoToLoad
    {

        public String    url;
        public ImageView imageView;


        public PhotoToLoad(String u, ImageView i) {
            url = u;
            imageView = i;
        }
    }


    class PhotosLoader implements Runnable {

        PhotoToLoad photoToLoad;


        PhotosLoader(PhotoToLoad photoToLoad) {
            this.photoToLoad = photoToLoad;
        }


        @Override
        public void run() {
            if (imageViewReused(photoToLoad))
                return;
            Bitmap bmp = getBitmap(photoToLoad.url);
            memoryCache.put(photoToLoad.url, bmp);
            if (imageViewReused(photoToLoad))
                return;
            BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
            Activity a = (Activity) photoToLoad.imageView.getContext();
            a.runOnUiThread(bd);
        }
    }


    boolean imageViewReused(PhotoToLoad photoToLoad) {
        String tag = imageViews.get(photoToLoad.imageView);
        if (tag == null || !tag.equals(photoToLoad.url)) {
            return true;
        }
        return false;
    }


    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable
    {

        Bitmap      bitmap;
        PhotoToLoad photoToLoad;


        public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
            bitmap = b;
            photoToLoad = p;
        }


        public void run()
        {
            if (imageViewReused(photoToLoad))
                return;
            if (bitmap != null) {
                photoToLoad.imageView.setImageBitmap(bitmap);
                photoToLoad.imageView.setTag(bitmap);
            } else {
                photoToLoad.imageView.setImageResource(stub_id);
            }
        }
    }


    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }

}
