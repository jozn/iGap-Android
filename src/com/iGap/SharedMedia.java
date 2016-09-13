// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap;

import java.io.File;
import java.util.ArrayList;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.iGap.adapter.DrawableManager;
import com.iGap.adapter.G;
import com.iGap.customviews.ImageSquareProgressBar;
import com.iGap.helpers.HelperDialogImageView;
import com.iGap.instruments.ImageLoader1;
import com.iGap.instruments.Utils;
import com.iGap.interfaces.OnDownloadListener;
import com.iGap.services.DownloaderService;


/**
 * 
 * namayesh tasavir va file va music va video hay be ashterak gozashte shode karbar dar groh va ya kanal va chat
 *
 */

public class SharedMedia extends Activity {

    private ArrayList<Item>   listItem = new ArrayList<Item>();
    private int               listcount;
    private String            type;
    private String            uid;
    private String            name;
    private TextView          txtcount, txtOf, txtUserName, txtSendDate;
    private Dialog            dialog;
    private GridView          gridView;
    private PopupWindow       popUp;
    private ViewPager         pager;
    private ImageLoader1      il;
    private DownloaderService downloaderService;


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Utils.checkLanguage(this);
        super.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.checkLanguage(this);
        setContentView(R.layout.shared_media_layout);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            type = extras.getString("type");
            uid = extras.getString("uid");
            name = extras.getString("name");
            init();
        }
    }


    private void init() {

        downloaderService = new DownloaderService();

        il = new ImageLoader1(SharedMedia.this, G.basicAuth);

        txtcount = (TextView) findViewById(R.id.sm_txt_count);

        Button btnBack = (Button) findViewById(R.id.sm_btn_back);
        btnBack.setTypeface(G.fontAwesome);
        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button btnSelectType = (Button) findViewById(R.id.sm_btn_select_type);
        btnSelectType.setTypeface(G.fontAwesome);
        btnSelectType.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                openDialogSelectType();
            }
        });

        fillListItem(type, uid);

        gridView = (GridView) findViewById(R.id.sm_gridView);
        gridView.setAdapter(new ImageAdapter(SharedMedia.this, listItem));

        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Item item = (Item) view.getTag();

                if (item.fileType.equals("2")) {
                    ImageSquareProgressBar.mode = 3;
                    showImage(item.fileHash, view);
                }
                else if (item.fileType.equals("3")) {
                    showVideo(item.fileHash, view);
                }
                else if (item.fileType.equals("4") || item.fileType.equals("8")) {
                    playSound(item.fileHash, view);
                }
                else if (item.fileType.equals("7")) {
                    showFile(item.fileHash, view);
                }

            }
        });

    }


    private void showImage(final String fileHash, final View image) {

        DrawableManager dm = new DrawableManager(SharedMedia.this);

        final OnDownloadListener listener = new OnDownloadListener() {

            @Override
            public void onProgressDownload(final int percent, int downloadedSize, int fileSize, boolean completeDownload) {

                if (completeDownload) {

                    runOnUiThread(new Runnable() {

                        public void run() {
                            try {
                                new HelperDialogImageView(SharedMedia.this, uid, name, GetType(type), fileHash);
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        ((ImageSquareProgressBar) image).setProgress(percent);

                    }
                });

            }
        };

        int isfileexist = G.cmd.isfileinfoexist(fileHash);
        if (isfileexist != 0) {

            String status = G.cmd.getfile(4, fileHash);
            if (status.equals("0")) {
                String url = G.cmd.getfile(2, fileHash);
                downloaderService.downloadPath(url)
                        .listener(listener)
                        .Authorization(G.basicAuth)
                        .filepath(G.DIR_TEMP + "/" + fileHash + ".jpg")
                        .filetype("2")
                        .stopDownload(false)
                        .imageView((ImageSquareProgressBar) image)
                        .drawableManager(dm)
                        .fileHash(fileHash)
                        .download(SharedMedia.this);

            } else if (status.equals("2") || status.equals("5")) {

                new HelperDialogImageView(SharedMedia.this, uid, name, GetType(type), fileHash);
            }
        }

    }


    private String GetType(String type) {

        String strtype = "";

        if (type.equals("1"))
            strtype = "chathistory";
        else if (type.equals("2"))
            strtype = "groupchathistory";
        else if (type.equals("3"))
            strtype = "channelhistory";

        return strtype;
    }


    private void showVideo(final String filehash, final View image) {

        final OnDownloadListener listener = new OnDownloadListener() {

            @Override
            public void onProgressDownload(final int percent, int downloadedSize, int fileSize, boolean completeDownload) {

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        ((ImageSquareProgressBar) image).setProgress(percent);

                    }
                });

            }
        };

        int isfileexist = G.cmd.isfileinfoexist(filehash);
        if (isfileexist != 0) {

            String status = G.cmd.getfile(4, filehash);
            if (status.equals("0")) {

                String url = G.cmd.getfile(2, filehash);
                downloaderService.downloadPath(url)
                        .listener(listener)
                        .Authorization(G.basicAuth)
                        .filetype("3")
                        .filepath(G.DIR_TEMP + "/" + filehash + ".mp4")
                        .stopDownload(false)
                        .imageView((ImageSquareProgressBar) image)
                        .fileHash(filehash)
                        .download(SharedMedia.this);

            } else if (status.equals("2") || status.equals("5")) {
                String filepath = G.cmd.getfile(6, filehash);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(filepath));
                intent.setDataAndType(Uri.fromFile(new File(filepath)), "video/*");
                startActivity(intent);
            }

        }

    }


    private void playSound(final String filehash, final View image) {

        final OnDownloadListener listener = new OnDownloadListener() {

            @Override
            public void onProgressDownload(final int percent, int downloadedSize, int fileSize, boolean completeDownload) {

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        ((ImageSquareProgressBar) image).setProgress(percent);

                    }
                });
            }
        };

        String status = G.cmd.getfile(4, filehash);

        if (status.equals("0")) {
            String url = G.cmd.getfile(2, filehash);
            downloaderService.downloadPath(url)
                    .listener(listener)
                    .Authorization(G.basicAuth)
                    .filetype("4")
                    .filepath(G.DIR_TEMP + "/" + filehash + ".mp3")
                    .stopDownload(false)
                    .fileHash(filehash)
                    .download(SharedMedia.this);

        } else if (status.equals("2") || status.equals("5")) {

            String filepath = G.cmd.getfile(6, filehash);
            Intent intent = new Intent();
            intent.setAction(android.content.Intent.ACTION_VIEW);
            File file = new File(filepath);
            intent.setDataAndType(Uri.fromFile(file), "audio/*");
            startActivity(intent);

        }

    }


    private void showFile(final String filehash, final View image) {

        final OnDownloadListener listener = new OnDownloadListener() {

            @Override
            public void onProgressDownload(final int percent, int downloadedSize, int fileSize, boolean completeDownload) {

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        ((ImageSquareProgressBar) image).setProgress(percent);

                    }
                });

            }
        };

        String status = G.cmd.getfile(4, filehash);
        if (status.equals("0")) {
            String url = G.cmd.getfile(2, filehash);
            String fileType = "7";
            int index = fileType.lastIndexOf('.') + 1;
            String ext = fileType.substring(index).toLowerCase();
            downloaderService.downloadPath(url)
                    .listener(listener)
                    .Authorization(G.basicAuth)
                    .filetype("7")
                    .filepath(G.DIR_TEMP + "/" + filehash + "." + ext)
                    .stopDownload(false)
                    .imageView((ImageSquareProgressBar) image)
                    .message("7")
                    .fileHash(filehash)
                    .download(SharedMedia.this);

        } else if (status.equals("2") || status.equals("5")) {

            String filepath = G.cmd.getfile(6, filehash);
            File file = new File(filepath);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "*/*");
            startActivity(intent);
        }

    }


    /**
     * fill list from database
     *
     * @param type enter type for realize singlechat or group or channel
     * @param id for realize the persoanal page
     */
    private void fillListItem(String type, String id) {

        listItem.clear();

        Cursor cu = null;

        cu = G.cmd.getSharedMediaItem(GetType(type), id);

        while (cu.moveToNext()) {

            Item item = new Item();

            item.fileHash = cu.getString(0);
            item.fileType = cu.getString(1);

            item.fileUrl = cu.getString(2);
            item.fileThumb = cu.getString(3);

            listItem.add(item);

        }
        cu.close();
        txtcount.setText(cu.getCount() + "");
    }


    private class Item {

        String fileHash;
        String fileType;
        String fileUrl;
        String fileThumb;

    }


    public class ImageAdapter extends BaseAdapter {

        private Context         mContext;
        private ArrayList<Item> list;


        public ImageAdapter(Context context, ArrayList<Item> list) {
            mContext = context;
            this.list = list;
            ImageSquareProgressBar.mode = 2;
        }


        @Override
        public int getCount() {
            return list.size();
        }


        @Override
        public Object getItem(int position) {
            return null;
        }


        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ImageSquareProgressBar imspb;

            if (convertView == null) { // if it's not recycled, initialize some attributes
                imspb = new ImageSquareProgressBar(mContext, "");
                imspb.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, (int) getResources().getDimension(R.dimen.dp80)));
                imspb.setImageScaleType(ImageView.ScaleType.FIT_XY);
                imspb.setPadding(3, 3, 3, 3);
                imspb.setTag(list.get(position));

                if (list.get(position).fileType.equals("2")) {//image
                    ImageLoader1 imageLoader1 = new ImageLoader1(mContext, G.basicAuth);
                    imageLoader1.DisplayImage(list.get(position).fileThumb, R.drawable.difaultimage, imspb.getImageView());
                }
                else if (list.get(position).fileType.equals("3")) {//video
                    imspb.setImageResource(R.drawable.video_attach_big_play);
                }
                else if (list.get(position).fileType.equals("4") || list.get(position).fileType.equals("8")) {//music
                    imspb.setImageResource(R.drawable.atach);
                }
                else if (list.get(position).fileType.equals("7")) {//file
                    imspb.setImageResource(R.drawable.atach_mini_dl);
                }

            }
            else {
                imspb = (ImageSquareProgressBar) convertView;
            }

            return imspb;
        }

    }


    @Override
    protected void onDestroy() {

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        if (popUp != null && popUp.isShowing()) {
            popUp.dismiss();
        }

        super.onDestroy();
    }


    private void openDialogSelectType() {

        final Dialog dialog = new Dialog(SharedMedia.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //  dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.gravity = Gravity.TOP;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.shared_media_select_type);

        dialog.show();

        TextView txtSharedMedia = (TextView) dialog.findViewById(R.id.smst_txt_Shared_media);
        txtSharedMedia.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                gridView.setAdapter(new ImageAdapter(SharedMedia.this, getCustomFiletype("0")));

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        TextView txtImage = (TextView) dialog.findViewById(R.id.smst_txt_image);
        txtImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                gridView.setAdapter(new ImageAdapter(SharedMedia.this, getCustomFiletype("2")));

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        TextView txtVideo = (TextView) dialog.findViewById(R.id.smst_txt_video);
        txtVideo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                gridView.setAdapter(new ImageAdapter(SharedMedia.this, getCustomFiletype("3")));

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        TextView txtVoice = (TextView) dialog.findViewById(R.id.smst_txt_voice);
        txtVoice.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                gridView.setAdapter(new ImageAdapter(SharedMedia.this, getCustomFiletype("4")));

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        TextView txtFile = (TextView) dialog.findViewById(R.id.smst_txt_file);
        txtFile.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                gridView.setAdapter(new ImageAdapter(SharedMedia.this, getCustomFiletype("7")));

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

    }


    private ArrayList<Item> getCustomFiletype(String fileType) {

        TextView txtName = (TextView) findViewById(R.id.sm_txt_Shared_media);

        if (fileType.equals("0")) {
            txtcount.setText(listItem.size() + "");
            txtName.setText(getString(R.string.shared_media_en));
            return listItem;

        }
        else {
            ArrayList<Item> list = new ArrayList<SharedMedia.Item>();

            if (fileType.equals("2")) {
                for (int i = 0; i < listItem.size(); i++) {
                    if (listItem.get(i).fileType.equals("2"))
                        list.add(listItem.get(i));
                }
                txtName.setText(getString(R.string.image_en));
            }
            else if (fileType.equals("3")) {
                for (int i = 0; i < listItem.size(); i++) {
                    if (listItem.get(i).fileType.equals("3"))
                        list.add(listItem.get(i));
                }
                txtName.setText(getString(R.string.video_en));
            }
            else if (fileType.equals("4")) {
                for (int i = 0; i < listItem.size(); i++) {
                    if (listItem.get(i).fileType.equals("4") || listItem.get(i).fileType.equals("8"))
                        list.add(listItem.get(i));
                }
                txtName.setText(getString(R.string.voice_en));
            }
            else if (fileType.equals("7")) {
                for (int i = 0; i < listItem.size(); i++) {
                    if (listItem.get(i).fileType.equals("7"))
                        list.add(listItem.get(i));
                }
                txtName.setText(getString(R.string.file_en));
            }

            txtcount.setText(list.size() + "");

            return list;
        }
    }


    @Override
    protected void onResume() {
        Utils.checkLanguage(this);
        super.onResume();
        G.appIsShowing = true;
    }


    @Override
    protected void onPause() {
        super.onPause();
        G.appIsShowing = false;
    }

}
