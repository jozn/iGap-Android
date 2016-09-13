// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.instruments;

import java.io.File;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.iGap.R;
import com.iGap.adapter.DrawableManagerDialog;
import com.iGap.adapter.G;
import com.iGap.customviews.ImageSquareProgressBar;
import com.iGap.customviews.ImageSquareProgressBarDialog;
import com.iGap.interfaces.OnDownloadListener;
import com.iGap.interfaces.OnLoadPhotoListener;


/**
 * 
 * adapter view pager for show image in dialog
 *
 */

public class ImagePagerAdapter extends PagerAdapter {

    private ArrayList<String>     imageUrlsLq;
    private ArrayList<String>     imageUrlsHq;
    private ArrayList<String>     ids;
    private Context               context;
    private ImageLoaderProfileLq  imageLoaderProfileLq;
    private OnLoadPhotoListener   loadPhotoListener;
    private String                basicAuth, userName;
    private DrawableManagerDialog dmDialog;


    public ImagePagerAdapter(String userName, DrawableManagerDialog dmDialog, ArrayList<String> ids, ArrayList<String> imageUrlsLq, ArrayList<String> imageUrlsHq, ImageLoaderProfileLq imageLoaderProfileLq, ImageLoaderProfileHq imageLoaderProfileHq, String basicAuth, Context context) {
        this.imageUrlsLq = imageUrlsLq;
        this.imageUrlsHq = imageUrlsHq;
        this.context = context;
        this.imageLoaderProfileLq = imageLoaderProfileLq;
        this.ids = ids;
        this.basicAuth = basicAuth;
        this.userName = userName;
        this.dmDialog = dmDialog;
    }


    public ImagePagerAdapter(ArrayList<String> imageUrlsLq, ArrayList<String> imageUrlsHq, ImageLoaderProfileLq imageLoaderProfileLq, ImageLoaderProfileHq imageLoaderProfileHq, Context context) {
        this.imageUrlsLq = imageUrlsLq;
        this.imageUrlsHq = imageUrlsHq;
        this.context = context;
        this.imageLoaderProfileLq = imageLoaderProfileLq;
    }


    public ImagePagerAdapter(ArrayList<String> imageUrlsLq, ArrayList<String> imageUrlsHq, ImageLoaderProfileLq imageLoaderProfileLq, Context context) {
        this.context = context;
        this.imageLoaderProfileLq = imageLoaderProfileLq;
        this.imageUrlsLq = imageUrlsLq;
        this.imageUrlsHq = imageUrlsHq;
        ImageSquareProgressBar.mode = 3;
    }


    @Override
    public int getCount() {
        return imageUrlsHq.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.view_user_avatar, null);

        final ImageSquareProgressBarDialog image = (ImageSquareProgressBarDialog) view.findViewById(R.id.image);

        final OnDownloadListener listener = new OnDownloadListener() {

            @Override
            public void onProgressDownload(final int percent, int downloadedSize, int fileSize, boolean completeDownload) {

                ((Activity) context).runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        image.setProgress(percent);
                    }
                });
            }
        };

        final int loader = R.drawable.difaultimage;

        String profileImagePath = G.DIR_DIALOG + "/" + (userName + "_" + ids.get(position)) + ".jpg";
        File imgFile = new File(profileImagePath);

        if (imgFile.exists()) { // agar file ghblan download shode namayesh bede
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            image.setImageBitmap(bitmap);
        } else {

            loadPhotoListener = new OnLoadPhotoListener() {

                @Override
                public void onLoadPhotoListener() {

                    final ImageLoaderProfile imgLoaderProfile = new ImageLoaderProfile(context, imageUrlsHq.get(position), basicAuth, G.DIR_TEMP + "/" + (userName + "_" + ids.get(position)) + ".jpg", listener, dmDialog, image, userName, position);
                    imgLoaderProfile.getBitmap();
                }
            };
            imageLoaderProfileLq.DisplayImage(imageUrlsLq.get(position), loader, image, loadPhotoListener);
        }

        container.addView(view);
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
