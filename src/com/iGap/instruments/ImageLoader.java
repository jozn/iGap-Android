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
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import com.iGap.R;
import com.iGap.customviews.ImageSquareProgressBar;


/**
 * 
 * load image from web site
 *
 */

public class ImageLoader {

    private MemoryCache                         memoryCache = new MemoryCache();
    private FileCache                           fileCache;
    private Map<ImageSquareProgressBar, String> imageViews  = Collections.synchronizedMap(new WeakHashMap<ImageSquareProgressBar, String>());
    private final Map<String, Bitmap>           drawableMap;
    private ExecutorService                     executorService;
    private String                              basicAuth;
    private Handler                             handler     = new Handler();


    public ImageLoader(Context context, String basicAuth1) {
        fileCache = new FileCache(context);
        basicAuth = basicAuth1;
        executorService = Executors.newFixedThreadPool(5);
        drawableMap = new HashMap<String, Bitmap>();
    }

    int stub_id = R.drawable.ic_launcher;


    public void DisplayImage(final String url, int loader, final ImageSquareProgressBar imageView, final String filehash) {

        if (drawableMap.containsKey(filehash)) {
            imageView.setImageBitmap(drawableMap.get(filehash));
            imageView.setTag(drawableMap.get(filehash));
            return;
        }

        stub_id = loader;
        Bitmap bitmap = memoryCache.get(filehash);

        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            imageView.setTag(bitmap);

        } else {
            imageViews.put(imageView, filehash);
            queuePhoto(url, filehash, imageView);
            imageView.setImageResource(loader);
        }
    }


    private void queuePhoto(String url, String filehash, ImageSquareProgressBar imageView)
    {
        PhotoToLoad p = new PhotoToLoad(url, filehash, imageView);
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
            if (bi != null)
                return fastblur(bi, 5);
        }
        catch (FileNotFoundException e) {}
        return null;
    }


    //Task for the queue
    private class PhotoToLoad
    {

        public String                 url;
        public String                 filehash;
        public ImageSquareProgressBar imageView;


        public PhotoToLoad(String u, String fileHash, ImageSquareProgressBar i) {
            url = u;
            filehash = fileHash;
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
            final Bitmap bmp = getBitmap(photoToLoad.url);
            if (bmp != null) {
                memoryCache.put(photoToLoad.filehash, bmp);
                showImageWithThread(photoToLoad.url, photoToLoad.filehash, photoToLoad.imageView);

            } else {
                photoToLoad.imageView.setImageResource(stub_id);
            }
        }
    }


    boolean imageViewReused(PhotoToLoad photoToLoad) {
        String tag = imageViews.get(photoToLoad.imageView);
        if (tag == null || !tag.equals(photoToLoad.url))
            return true;
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

                //=================Copy
                showImageBitmap(bitmap, photoToLoad.imageView);

            } else {
                photoToLoad.imageView.setImageResource(stub_id);
            }
        }
    }


    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }


    public Bitmap fastblur(Bitmap sentBitmap, int radius) {

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }


    //=========================
    //=================Copy
    private void showImageWithThread(final String url, final String filehash, final ImageSquareProgressBar imageView) {

        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {

                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        Bitmap bitmap = getBitmap(url);
                        imageView.setImageBitmap(bitmap);
                        imageView.setTag(bitmap);
                        drawableMap.put(filehash, bitmap);
                    }
                });
            }
        });
        t.start();

    }


    private void showImageBitmap(final Bitmap bitmap, final ImageSquareProgressBar imageView) {
        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message message) {
                imageView.setImageBitmap((Bitmap) message.obj);
                imageView.setTag((Bitmap) message.obj);
            }
        };

        Thread thread = new Thread() {

            @Override
            public void run() {
                Message message = handler.obtainMessage(1, bitmap);
                handler.sendMessage(message);
            }
        };
        thread.start();
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
