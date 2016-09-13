package com.iGap.helpers;

import java.io.File;
import java.util.ArrayList;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.iGap.Channel.Item;
import com.iGap.R;
import com.iGap.adapter.DrawableManagerDialog;
import com.iGap.adapter.G;
import com.iGap.customviews.ImageSquareProgressBar;
import com.iGap.customviews.ImageSquareProgressBarDialog;
import com.iGap.customviews.TouchImageView;
import com.iGap.instruments.ImageLoader1;
import com.iGap.instruments.ImageLoaderDialog;
import com.iGap.instruments.Utils;
import com.iGap.interfaces.OnDownloadListener;
import com.iGap.services.DownloaderService;


public class HelperDialogImageView {

    private Context               context;
    private String                uid;
    private String                name;
    private Dialog                dialog;
    private PopupWindow           popUp;
    private DrawableManagerDialog dmDialog;
    private String                filehash;
    private String                Type;
    private ImageLoader1          il;
    private ImageLoaderDialog     imgLoaderDialog;


    /**
     * open dialog for show all image in group or channel or chat
     * 
     * @param uid id for group or chat or channel
     * @param name group or channel or chat name
     * @param Type channelhistory , groupchathistory , chathistory
     * @param filehash image file hash
     */
    public HelperDialogImageView(Context context, String uid, String name, String Type, String filehash) {

        this.context = context;
        this.uid = uid;
        this.name = name;
        this.Type = Type;
        this.filehash = filehash;

        this.il = new ImageLoader1(context, G.basicAuth);
        this.imgLoaderDialog = new ImageLoaderDialog(context, G.basicAuth);
        this.dmDialog = new DrawableManagerDialog(context);

        initDialogImage();

    }


    private void initDialogImage() {

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_imageview_avatar);
        ArrayList<Item> list = new ArrayList<Item>();
        Cursor cu = null;
        cu = G.cmd.getSharedMediaItem(Type, uid);

        while (cu.moveToNext()) {

            Item item = new Item();

            item.fileHash = cu.getString(0);
            item.fileType = cu.getString(1);
            item.fileUrl = cu.getString(2);
            item.fileThumb = cu.getString(3);
            item.filePath = cu.getString(4);

            if ( !item.fileType.equals("2"))
                continue;

            list.add(item);

        }
        cu.close();

        int showItem = 1;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).fileHash.equals(filehash)) {
                showItem = i;
                break;
            }
        }

        final int listcount = list.size();

        final ViewPager pager = (ViewPager) dialog.findViewById(R.id.pager);
        HelperAnimation.helperAnimation(pager);
        ImagePagerAdapter adapter = new ImagePagerAdapter(list, il, context);
        pager.setAdapter(adapter);

        pager.setCurrentItem(showItem);

        final TextView txtOf = (TextView) dialog.findViewById(R.id.txt_of);
        txtOf.setTypeface(G.robotoBold);

        txtOf.setText(showItem + 1 + " " + context.getString(R.string.of_en) + " " + listcount);

        Button btnBack = (Button) dialog.findViewById(R.id.btn_back);
        btnBack.setTypeface(G.fontAwesome);

        Button btnSetting = (Button) dialog.findViewById(R.id.btn_setting);
        btnSetting.setTypeface(G.fontAwesome);

        btnSetting.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                popupoptionsImage(v, pager);
            }
        });

        Button btnShare = (Button) dialog.findViewById(R.id.btn_share);
        btnShare.setTypeface(G.fontAwesome);

        TextView txtUserName = (TextView) dialog.findViewById(R.id.txt_user_name);
        txtUserName.setTypeface(G.robotoBold);
        txtUserName.setText(name);

        TextView txtSendDate = (TextView) dialog.findViewById(R.id.txt_send_date);
        txtSendDate.setVisibility(View.GONE);

        final LinearLayout llTop = (LinearLayout) dialog.findViewById(R.id.ll_top);

        final LinearLayout llBottom = (LinearLayout) dialog.findViewById(R.id.ll_bottom);

        TouchImageView imgDialogImage = (TouchImageView) dialog.findViewById(R.id.img_dialog_image);
        imgDialogImage.setMaxZoom(4f);

        imgDialogImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (llBottom.getVisibility() == View.VISIBLE) {
                    AlphaAnimation animation1 = new AlphaAnimation(1, 0);
                    animation1.setDuration(400);
                    llTop.setAnimation(animation1);
                    llBottom.setAnimation(animation1);

                    llTop.setVisibility(ViewGroup.INVISIBLE);
                    llBottom.setVisibility(ViewGroup.INVISIBLE);

                } else {

                    AlphaAnimation animation1 = new AlphaAnimation(0, 1);
                    animation1.setDuration(400);
                    llTop.setAnimation(animation1);
                    llBottom.setAnimation(animation1);

                    llTop.setVisibility(ViewGroup.VISIBLE);
                    llBottom.setVisibility(ViewGroup.VISIBLE);
                }
            }
        });

        pager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {

                txtOf.setText(arg0 + 1 + " " + context.getString(R.string.of_en) + "  " + listcount);
            }


            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }


            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }

            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }


    @SuppressWarnings("deprecation")
    private void popupoptionsImage(View v, final ViewPager pager) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View mView = layoutInflater.inflate(R.layout.popup_image_dialog, null);
        popUp = new PopupWindow(context);
        popUp.setContentView(mView);
        popUp.setWindowLayoutMode(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popUp.setHeight(1);
        popUp.setWidth(1);
        popUp.setTouchable(true);
        popUp.setFocusable(false);
        popUp.setOutsideTouchable(true);
        popUp.setBackgroundDrawable(new BitmapDrawable());
        popUp.showAtLocation(v, Gravity.NO_GRAVITY, location[0], location[1]);

        Button btndeleteavatar = (Button) mView.findViewById(R.id.btn_delete_avatar);
        btndeleteavatar.setVisibility(View.GONE);

        Button btnSaveAvatar = (Button) mView.findViewById(R.id.btn_save_avatar);
        btnSaveAvatar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (popUp != null && popUp.isShowing()) {
                    popUp.dismiss();
                }

                try {
                    View currView = pager.getChildAt(pager.getCurrentItem());
                    ImageSquareProgressBar ims = (ImageSquareProgressBar) currView.findViewById(R.id.image);

                    if (Utils.SavePicToDownLoadFolder((Bitmap) ims.getTag()))
                        Toast.makeText(context, context.getString(R.string.picture_saved_en), Toast.LENGTH_SHORT).show();

                }
                catch (Exception e) {}

            }
        });

    }


    private class ImagePagerAdapter extends PagerAdapter {

        private ArrayList<Item> imageUrlsHq;
        private Context         context;


        public ImagePagerAdapter(ArrayList<Item> imageUrlsHq, ImageLoader1 imageLoader2, Context context) {
            this.imageUrlsHq = imageUrlsHq;
            this.context = context;
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

            DownloaderService downloaderService = new DownloaderService();
            final OnDownloadListener listener = new OnDownloadListener() {

                @Override
                public void onProgressDownload(final int percent,
                                               int downloadedSize, int fileSize,
                                               boolean completeDownload) {

                    ((Activity) context).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            image.setProgress(percent);
                        }
                    });
                }
            };

            int isfileexist = G.cmd.isfileinfoexist(imageUrlsHq.get(position).fileHash);
            if (isfileexist != 0) {

                String status = G.cmd.getfile(4, imageUrlsHq.get(position).fileHash);
                if (status.equals("0")) {
                    String thumb = G.cmd.getfile(3, imageUrlsHq.get(position).fileHash);
                    int loader = R.drawable.difaultimage;
                    imgLoaderDialog.DisplayImage(thumb, loader, image, imageUrlsHq.get(position).fileHash);
                    String url = G.cmd.getfile(2, imageUrlsHq.get(position).fileHash);
                    downloaderService
                            .downloadPath(url)
                            .listener(listener)
                            .Authorization(G.basicAuth)
                            .filepath(G.DIR_TEMP + "/" + imageUrlsHq.get(position).fileHash + ".jpg").filetype("2")
                            .stopDownload(false).imageView(image)
                            .imageViewType(2).drawableManager(dmDialog)
                            .fileHash(imageUrlsHq.get(position).fileHash)
                            .download(context);

                } else if (status.equals("2") || status.equals("5")) {
                    String filepath = G.cmd.getfile(6, imageUrlsHq.get(position).fileHash);
                    File imgFile = new File(filepath);
                    if (imgFile.exists()) {
                        String fileName = HelperString.getFileName(filepath);
                        File file = new File(G.DIR_DIALOG + "/" + fileName);
                        if (file.exists()) { // agar file ghablan decode shode haman masir ra farakhani kon
                            filepath = G.DIR_DIALOG + "/" + fileName;
                        } else {
                            filepath = HelperComoressImage.compressImage(filepath);
                        }
                        Bitmap bitmap = BitmapFactory.decodeFile(filepath);
                        image.setImageBitmap(bitmap);
                    } else {
                        G.cmd.updatefilestatus(filehash, 1);
                    }
                }
            }

            container.addView(view);
            return view;
        }


        @Override
        public void destroyItem(View container, int position, Object object) {

            ((ViewPager) container).removeView((View) object);
        }

    }

}