// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.instruments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import com.iGap.adapter.G;


/**
 * get a name and return a color code
 * 
 * @param name user name or group name or channel name
 * @return color code
 */

public class UtileProg {

    public UtileProg() {}


    public UtileProg(Context context) {}


    /**
     * 
     * determinate color for each letter
     * 
     * @param name
     * @return
     *         color
     */

    public String getAlphabetColor(String name) {

        int sum = 0;
        int color = 0;
        String str = "#a14747";

        if (name == null)
            return str;

        for (int i = 0; i < name.length(); i++) {

            sum += name.codePointAt(i);
        }

        if (sum > 0) {
            color = sum % 12;
        }

        if (color == 0)
            str = "#a14747";
        else if (color == 1)
            str = "#a1477c";
        else if (color == 2)
            str = "#7c47a1";
        else if (color == 3)
            str = "#4947a1";
        else if (color == 4)
            str = "#477ea1";
        else if (color == 5)
            str = "#47a1a0";
        else if (color == 6)
            str = "#47a17a";
        else if (color == 7)
            str = "#78a147";
        else if (color == 8)
            str = "#a14d47";
        else if (color == 9)
            str = "#f9b526";
        else if (color == 10)
            str = "#f95326";
        else if (color == 11)
            str = "#26b6f9";

        return str;
    }


    /**
     * 
     * get letter and color and size finally draw bitmap with this info
     * 
     * @param with size of bitmap
     * @param text letter for drawing
     * @param color color of bitmap
     * @return
     *         bitmap that painted alphabet
     */

    public Bitmap drawAlphabetOnPicture(int with, String text, String color) {

        String mColor = "#f4f4f4";

        if (color != "" && color != null)
            mColor = color;

        Bitmap bitmap = Bitmap.createBitmap(with, with, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.parseColor(getAlphabetColor(text)));

        int fontsize = with / 2;
        Canvas cs = new Canvas(bitmap);

        Paint textPaint = new Paint(Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        textPaint.setMaskFilter(new BlurMaskFilter(1, BlurMaskFilter.Blur.SOLID));
        textPaint.setColor(Color.parseColor(mColor));
        textPaint.setTypeface(G.robotoBold);
        textPaint.setTextAlign(Align.CENTER);
        textPaint.setTextSize(fontsize);
        textPaint.setStyle(Style.FILL);

        cs.drawText(text, with / 2, with / 2 + fontsize / 3, textPaint);

        return bitmap;
    }
}
