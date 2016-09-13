// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.instruments;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.iGap.R;


/**
 * 
 * make emoji tabs
 *
 */

public class SlidingTabLayoutemoji extends HorizontalScrollView {

    public interface TabColorizer {

        int getIndicatorColor(int position);


        int getDividerColor(int position);
    }

    private static final int               TITLE_OFFSET_DIPS     = 24;
    private static final int               TAB_VIEW_PADDING_DIPS = 16;
    private static final int               TAB_VIEW_TEXT_SIZE_SP = 14;
    private List<tabParameter>             mTabs                 = new ArrayList<tabParameter>();

    private int                            mTitleOffset;
    private int                            smileTabSize;
    private ViewPager                      mViewPager;
    private ViewPager.OnPageChangeListener mViewPagerPageChangeListener;

    private final SlidingTabStripemoji     mTabStrip;


    public SlidingTabLayoutemoji(Context context) {
        this(context, null);
    }


    public SlidingTabLayoutemoji(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public SlidingTabLayoutemoji(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            smileTabSize = CalculationUtil.convertDpToPx(48, context);
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            smileTabSize = CalculationUtil.convertDpToPx(36, context);
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            smileTabSize = CalculationUtil.convertDpToPx(24, context);
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
            smileTabSize = CalculationUtil.convertDpToPx(18, context);
        }
        else {
            smileTabSize = CalculationUtil.convertDpToPx(24, context);
        }

        // Disable the Scroll Bar
        setHorizontalScrollBarEnabled(false);
        // Make sure that the Tab Strips fills this View
        setFillViewport(true);

        mTitleOffset = (int) (TITLE_OFFSET_DIPS * getResources().getDisplayMetrics().density);

        mTabStrip = new SlidingTabStripemoji(context);
        addView(mTabStrip, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }


    public void setCustomTabColorizer(TabColorizer tabColorizer) {
        mTabStrip.setCustomTabColorizer(tabColorizer);
    }


    public void setSelectedIndicatorColors(int... colors) {
        mTabStrip.setSelectedIndicatorColors(colors);
    }


    public void setDividerColors(int... colors) {
        mTabStrip.setDividerColors(colors);
    }


    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mViewPagerPageChangeListener = listener;
    }


    public void setViewPager(ViewPager viewPager) {

        fillTabs();

        setCustomTabColorizer(new TabColorizer() {

            @Override
            public int getIndicatorColor(int position) {
                return mTabs.get(position).getIndicatorColor();
            }


            @Override
            public int getDividerColor(int position) {
                return mTabs.get(position).getDividerColor();
            }

        });

        mTabStrip.removeAllViews();
        mViewPager = viewPager;
        if (viewPager != null) {
            viewPager.setOnPageChangeListener(new InternalViewPagerListener());
            populateTabStrip();
        }
    }


    protected TextView createDefaultTabView(Context context) {
        Typeface custom_font = Typeface.createFromAsset(getContext().getAssets(), "font/fontawesome-webfont.ttf");

        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, TAB_VIEW_TEXT_SIZE_SP);
        textView.setTypeface(custom_font);
        textView.setTextColor(Color.WHITE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {// allcaps is for api 14 and upper
            textView.setAllCaps(false);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            TypedValue outValue = new TypedValue();
            getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            textView.setBackgroundResource(outValue.resourceId);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            // If we're running on ICS or newer, enable all-caps to match the Action Bar tab style

        }

        int padding = (int) (TAB_VIEW_PADDING_DIPS * getResources().getDisplayMetrics().density);
        textView.setPadding(padding, padding, padding, padding);

        return textView;
    }


    @SuppressWarnings("deprecation")
    private void populateTabStrip() {
        final PagerAdapter adapter = mViewPager.getAdapter();
        final OnClickListener tabClickListener = new TabClickListener();

        for (int i = 0; i < adapter.getCount(); i++) {
            /*	View tabView = null;
            TextView tabTitleView = null;

            if (mTabViewLayoutId != 0) {
            	// If there is a custom tab view layout id set, try and inflate it
            	tabView = LayoutInflater.from(getContext()).inflate(mTabViewLayoutId, mTabStrip, false);
            	tabTitleView = (TextView) tabView.findViewById(mTabViewTextViewId);
            }

            if (tabView == null) {
            	tabView = createDefaultTabView(getContext());
            }

            if (tabTitleView == null && TextView.class.isInstance(tabView)) {
            	tabTitleView = (TextView) tabView;
            } 

             tabTitleView.setText(mTabs.get(i).getTitle());
             tabView.setOnClickListener(tabClickListener);
             tabTitleView.setBackgroundDrawable(getResources().getDrawable(mTabs.get(i).getRecPic()));
             */
            ImageView iv = new ImageView(getContext());
            iv.setBackgroundDrawable(mTabs.get(i).getRecPic());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(smileTabSize, smileTabSize);
            lp.setMargins(7, 10, 7, 10);
            iv.setLayoutParams(lp);

            LinearLayout layout = new LinearLayout(getContext());
            layout.addView(iv);
            //	iv.setOnClickListener(tabClickListener);
            layout.setOnClickListener(tabClickListener);
            mTabStrip.addView(layout);

            //mTabStrip.addView(tabView);
        }
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (mViewPager != null) {
            scrollToTab(mViewPager.getCurrentItem(), 0);
        }
    }


    private void scrollToTab(int tabIndex, int positionOffset) {

        final int tabStripChildCount = mTabStrip.getChildCount();
        if (tabStripChildCount == 0 || tabIndex < 0 || tabIndex >= tabStripChildCount) {
            return;
        }

        View selectedChild = mTabStrip.getChildAt(tabIndex);
        if (selectedChild != null) {
            setBackgroundColor();

            selectedChild.setBackgroundColor(getContext().getResources().getColor(R.color.graylight));

            int targetScrollX = selectedChild.getLeft() + positionOffset;

            if (tabIndex > 0 || positionOffset > 0) {
                // If we're not at the first child and are mid-scroll, make sure we obey the offset
                targetScrollX -= mTitleOffset;
            }

            scrollTo(targetScrollX, 0);
        }
    }


    private void setBackgroundColor() {
        for (int i = 0; i < mTabStrip.getChildCount(); i++) {
            View child = mTabStrip.getChildAt(i);
            child.setBackgroundColor(getContext().getResources().getColor(R.color.white));

        }

    }


    private class InternalViewPagerListener implements ViewPager.OnPageChangeListener {

        private int mScrollState;


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int tabStripChildCount = mTabStrip.getChildCount();
            if ((tabStripChildCount == 0) || (position < 0) || (position >= tabStripChildCount)) {
                return;
            }

            mTabStrip.onViewPagerPageChanged(position, positionOffset);

            View selectedTitle = mTabStrip.getChildAt(position);
            int extraOffset = (selectedTitle != null)
                    ? (int) (positionOffset * selectedTitle.getWidth())
                    : 0;
            scrollToTab(position, extraOffset);

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrolled(position, positionOffset,
                        positionOffsetPixels);
            }
        }


        @Override
        public void onPageScrollStateChanged(int state) {
            mScrollState = state;

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrollStateChanged(state);
            }
        }


        @Override
        public void onPageSelected(int position) {
            if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                mTabStrip.onViewPagerPageChanged(position, 0f);
                scrollToTab(position, 0);
            }

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageSelected(position);
            }
        }

    }


    private class TabClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                if (v == mTabStrip.getChildAt(i)) {
                    mViewPager.setCurrentItem(i);
                    return;
                }
            }
        }
    }


    private class tabParameter {

        private final int      mIndicatorColor;
        private final int      mDividerColor;
        private final Drawable mResPic;


        public tabParameter(String title, int indicatorColor, int dividerColor, Drawable resPic) {
            mIndicatorColor = indicatorColor;
            mDividerColor = dividerColor;
            mResPic = resPic;
        }


        int getIndicatorColor() {
            return mIndicatorColor;
        }


        int getDividerColor() {
            return mDividerColor;
        }


        Drawable getRecPic() {
            return mResPic;
        }
    }


    private void fillTabs() {

        ArrayList<String> tabNames = new ArrayList<String>();
        tabNames.add("[smile");
        tabNames.add("[head");
        tabNames.add("[emoji");
        tabNames.add("[animal");
        tabNames.add("[hand");
        tabNames.add("[love");
        tabNames.add("[object");

        for (int i = 0; i < tabNames.size(); i++) {
            mTabs.add(new tabParameter("", Color.CYAN, Color.parseColor("#2bbfbd"), getPic(tabNames.get(i))));
        }

        //		mTabs.add(new tabParameter(
        //				//	getContext().getString(R.string.groups)+
        //				"", // Title
        //				Color.YELLOW, // Indicator color
        //				Color.parseColor("#2bbfbd") // Divider color
        //				,R.drawable.a001
        //				));

    }


    private Drawable getPic(String name) {

        Drawable drawable = null;
        try
        {
            InputStream _inInputStream = getContext().getAssets().open("smile/" + name + "_1].png");
            drawable = Drawable.createFromStream(_inInputStream, null);
        }
        catch (Exception e) {}

        return drawable;
    }

}
