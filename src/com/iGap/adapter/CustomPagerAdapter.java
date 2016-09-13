// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import com.iGap.R;
import com.iGap.instruments.CalculationUtil;


/**
 * 
 * this adapter fill list by smile picture
 * 
 * @use for smile viewpager adapter in singchat and groupChat and Channel
 */

public class CustomPagerAdapter extends PagerAdapter {

    private ArrayList<String> tabNames = new ArrayList<String>();
    private Context           mContext;
    private EditText          mEditText;
    private int               SmileZize, smileShowSize;


    public CustomPagerAdapter(Context context, EditText ed) {

        if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            SmileZize = CalculationUtil.convertDpToPx(60, context);
            smileShowSize = CalculationUtil.convertDpToPx(48, context);
        }
        else if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            SmileZize = CalculationUtil.convertDpToPx(45, context);
            smileShowSize = CalculationUtil.convertDpToPx(36, context);
        }
        else if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            SmileZize = CalculationUtil.convertDpToPx(30, context);
            smileShowSize = CalculationUtil.convertDpToPx(24, context);
        }
        else if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
            SmileZize = CalculationUtil.convertDpToPx(22, context);
            smileShowSize = CalculationUtil.convertDpToPx(18, context);
        }
        else {
            SmileZize = CalculationUtil.convertDpToPx(30, context);
            smileShowSize = CalculationUtil.convertDpToPx(24, context);
        }

        mContext = context;
        mEditText = ed;

        tabNames.add("[smile");
        tabNames.add("[head");
        tabNames.add("[emoji");
        tabNames.add("[animal");
        tabNames.add("[hand");
        tabNames.add("[love");
        tabNames.add("[object");
    }


    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.mypage, collection, false);

        GridView gridView = (GridView) layout.findViewById(R.id.gridView1);
        gridView.setAdapter(new ImageAdapter(mContext, getCountSmilePic(tabNames.get(position))));

        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
                try {
                    InputStream inInputStream = mContext.getAssets().open("smile/" + view.getTag().toString());
                    Drawable drawable = Drawable.createFromStream(inInputStream, null);
                    addImageBetweentext(drawable, mEditText, view.getTag().toString());

                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        collection.addView(layout);
        return layout;
    }


    private void addImageBetweentext(Drawable drawable, EditText editText1, String code) {
        drawable.setBounds(0, 0, smileShowSize, smileShowSize);
        int selectionCursor = editText1.getSelectionStart();
        editText1.getText().insert(selectionCursor, code);
        selectionCursor = editText1.getSelectionStart();

        SpannableStringBuilder builder = new SpannableStringBuilder(editText1.getText());
        builder.setSpan(new ImageSpan(drawable), selectionCursor - code.length(), selectionCursor, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        editText1.setText(builder);
        editText1.setSelection(selectionCursor);
    }


    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }


    @Override
    public int getCount() {
        return tabNames.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public CharSequence getPageTitle(int position) {

        return mContext.getString(position);
    }


    public class ImageAdapter extends BaseAdapter {

        private Context           mContext;
        private ArrayList<String> listPic;


        public ImageAdapter(Context context, ArrayList<String> listPic) {
            mContext = context;

            this.listPic = listPic;
        }


        @Override
        public int getCount() {
            return listPic.size();
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

            ImageView imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(SmileZize, SmileZize));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setPadding(1, 1, 1, 1);
            imageView.setTag(listPic.get(position));

            try {
                imageView.setImageDrawable(getPic(position));
            }
            catch (Exception e) {}

            return imageView;
        }


        private Drawable getPic(int position) {

            String name = listPic.get(position);

            Drawable drawable = null;
            try
            {
                InputStream _inInputStream = mContext.getAssets().open("smile/" + name);
                drawable = Drawable.createFromStream(_inInputStream, null);
            }
            catch (Exception e) {}

            return drawable;
        }
    }


    private ArrayList<String> getCountSmilePic(String name) {

        ArrayList<String> tmp = new ArrayList<String>();

        try {
            String[] piclist = mContext.getAssets().list("smile");
            for (int i = 0; i < piclist.length; i++) {
                if (piclist[i].startsWith(name)) {
                    tmp.add(piclist[i]);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return tmp;
    }

}