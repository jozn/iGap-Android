package com.iGap.helpers;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;


public class HelperAnimation {

    private static final int MIN_SCALE = 1;


    public static void helperAnimation(ViewPager pager) {
        pager.setPageTransformer(false, new ViewPager.PageTransformer() {

            @Override
            public void transformPage(View view, float position) {
                final float normalizedposition = Math.abs(Math.abs(position) - 1);
                view.setScaleX(normalizedposition / 2 + 0.5f);
                view.setScaleY(normalizedposition / 2 + 0.5f);
            }
        });
    }


    // set difference animatino for each kind
    public static void helperAnimation(ViewPager pager, final int kind) {

        pager.setPageTransformer(false, new ViewPager.PageTransformer() {

            @Override
            public void transformPage(View view, float position) {

                if (kind == 1) {
                    final float normalizedposition = Math.abs(Math.abs(position) - 1);
                    view.setScaleX(normalizedposition / 2 + 0.5f);
                    view.setScaleY(normalizedposition / 2 + 0.5f);
                } else if (kind == 2) {
                    view.setRotationY(position * -30);
                } else if (kind == 3) {
                    int pageWidth = view.getWidth();
                    view.setTranslationX( -1 * view.getWidth() * position);
                    if (position < -1) { // [-Infinity,-1)
                        // This page is way off-screen to the left.
                        view.setAlpha(0);

                    } else if (position <= 0) { // [-1,0]
                        // Use the default slide transition when moving to the left page
                        view.setAlpha(1);
                        view.setTranslationX(0);
                        view.setScaleX(1);
                        view.setScaleY(1);

                    } else if (position <= 1) { // (0,1]
                        // Fade the page out.
                        view.setAlpha(1 - position);

                        // Counteract the default slide transition
                        view.setTranslationX(pageWidth * -position);

                        // Scale the page down (between MIN_SCALE and 1)
                        float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
                        view.setScaleX(scaleFactor);
                        view.setScaleY(scaleFactor);

                    } else { // (1,+Infinity]
                        // This page is way off-screen to the right.
                        view.setAlpha(0);
                    }
                }
            }
        });
    }


    /**
     * 
     * animation to right , accept textView for set animation
     * 
     * @param txt
     */

    public static void CounterAnimRight(TextView txt) {
        AnimationSet setAnim = new AnimationSet(true);
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 100, 0, 0);
        translateAnimation.setDuration(200);
        translateAnimation.setFillAfter(true);
        setAnim.addAnimation(translateAnimation);
        TranslateAnimation translateAnimation2 = new TranslateAnimation(0, -100, 0, 0);
        translateAnimation2.setDuration(200);
        translateAnimation2.setStartOffset(200);
        setAnim.addAnimation(translateAnimation2);
        txt.setAnimation(setAnim);
    }


    /**
     * 
     * animation to left , accept textView for set animation
     * 
     * @param txt
     */
    public static void CounterAnimLeft(TextView txt) {
        AnimationSet setAnim = new AnimationSet(true);
        TranslateAnimation translateAnimation = new TranslateAnimation(0, -100, 0, 0);
        translateAnimation.setDuration(200);
        translateAnimation.setFillAfter(true);
        setAnim.addAnimation(translateAnimation);
        TranslateAnimation translateAnimation2 = new TranslateAnimation(0, 100, 0, 0);
        translateAnimation2.setDuration(200);
        translateAnimation2.setStartOffset(200);
        setAnim.addAnimation(translateAnimation2);
        txt.setAnimation(setAnim);
    }
}
